# PMC (PubMed Central) 특수 케이스 가이드

[**한국어**](PMC-SPECIFICS.ko.md) | [English](PMC-SPECIFICS.md)

PMC 특유의 특수 케이스와 JATS 1.4 예외 상황 처리에 대한 완전한 가이드입니다.

---

## 목차

- [체크섬 파일 없음](#체크섬-파일-없음)
- [TAR.GZ 패키지 처리](#targz-패키지-처리)
- [Sub-Article 재귀 구조](#sub-article-재귀-구조)
- [Front vs Front-Stub](#front-vs-front-stub)
- [Response 요소](#response-요소)
- [FloatsGroup](#floatsgroup)
- [네임스페이스 처리](#네임스페이스-처리)
- [테이블 처리](#테이블-처리)

---

## 체크섬 파일 없음

### PubMed와의 주요 차이점

**PubMed:**
- MD5 체크섬 파일(`.md5`) 제공
- 파싱 전 파일 무결성 검증 가능
- 예: `pubmed25n0001.xml.gz.md5`

**PMC:**
- ❌ MD5 또는 SHA 체크섬 파일 **제공 안 함**
- 압축 해제 성공 여부로 파일 무결성 검증
- 사전 파싱 검증 불가능

---

### 무결성 검증 전략

#### 파싱을 통한 자동 검증

```java
PmcXmlParser parser = new PmcXmlParser();
Path tarGzFile = Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

try {
    // 파싱 성공 시 아카이브 유효함
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
    System.out.println("✅ 아카이브 무결성 검증됨");
    System.out.println("파싱된 논문 수: " + articles.size());

} catch (IOException e) {
    // 압축 해제 또는 파싱 실패 → 파일 손상됨
    System.err.println("❌ 아카이브 손상됨: " + e.getMessage());

    // 파일 재다운로드
    downloadArchive(tarGzFile);
}
```

---

#### 수동 SHA-256 계산

자체 체크섬을 유지해야 하는 경우:

```java
import java.security.MessageDigest;
import java.nio.file.Files;
import java.io.InputStream;

public String calculateSha256(Path file) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    try (InputStream is = Files.newInputStream(file)) {
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            md.update(buffer, 0, bytesRead);
        }
    }

    byte[] digest = md.digest();
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
        sb.append(String.format("%02x", b));
    }
    return sb.toString();
}

// 사용법
Path tarGzFile = Paths.get("pmc_package.tar.gz");
String sha256 = calculateSha256(tarGzFile);
System.out.println("SHA-256: " + sha256);

// 향후 검증을 위해 이 체크섬 저장
database.storeChecksum(tarGzFile.getFileName().toString(), sha256);
```

---

### 프로덕션 패턴

```java
public void processPmcArchiveWithValidation(Path tarGzFile) throws Exception {
    // 1. 처리 전 체크섬 계산
    String currentChecksum = calculateSha256(tarGzFile);

    // 2. 이전에 이 파일을 처리했는지 확인
    String storedChecksum = database.getChecksum(tarGzFile.getFileName().toString());
    if (storedChecksum != null && storedChecksum.equals(currentChecksum)) {
        System.out.println("✅ 파일이 이미 처리됨, 건너뜀");
        return;
    }

    // 3. 파싱 시도 (무결성 검증)
    try {
        PmcXmlParser parser = new PmcXmlParser();
        List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

        // 4. 논문을 데이터베이스에 저장
        for (JatsArticle article : articles) {
            database.save(article);
        }

        // 5. 재처리 방지를 위해 체크섬 저장
        database.storeChecksum(tarGzFile.getFileName().toString(), currentChecksum);
        System.out.println("✅ 처리 완료");

    } catch (IOException e) {
        logger.error("❌ 아카이브 손상됨: {}", e.getMessage());

        // 재다운로드 및 재시도
        downloadArchive(tarGzFile);
        processPmcArchiveWithValidation(tarGzFile);
    }
}
```

---

## TAR.GZ 패키지 처리

### PMC FTP 구조

PMC는 압축된 tar.gz 패키지로 논문을 제공합니다:

```
ftp://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/
├── oa_comm/xml/
│   ├── pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz
│   ├── pmc_oa_comm_xml.PMC001xxxxxx.baseline.2025-12-18.tar.gz
│   └── ...
├── oa_noncomm/xml/
│   ├── pmc_oa_noncomm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz
│   └── ...
└── oa_other/xml/
    └── ...
```

**각 패키지 포함 내용:**
- 여러 XML 파일(패키지당 100~30,000개 논문)
- 체크섬 파일 없음
- GZip 압축 + TAR 아카이브

---

### TAR.GZ 패키지 파싱

#### 방법 1: 전체 아카이브 파싱

```java
PmcXmlParser parser = new PmcXmlParser();
Path tarGzFile = Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

// 아카이브의 모든 논문 파싱
List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

System.out.println("총 논문 수: " + articles.size());

// 논문 처리
for (JatsArticle article : articles) {
    ArticleMeta meta = article.getFront().getArticleMeta();

    // PMC ID 가져오기
    List<PmcArticleId> ids = meta.getArticleIds();
    String pmcId = ids.stream()
                      .filter(id -> "pmc".equals(id.getPubIdType()))
                      .map(PmcArticleId::getValue)
                      .findFirst()
                      .orElse(null);

    System.out.println("처리 중: " + pmcId);
    database.save(article);
}
```

---

#### 방법 2: TAR.GZ에서 스트리밍

대용량 아카이브의 경우 스트리밍을 사용하여 논문을 하나씩 처리합니다:

```java
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

public void streamTarGzArchive(Path tarGzFile) throws Exception {
    try (InputStream fis = Files.newInputStream(tarGzFile);
         GzipCompressorInputStream gis = new GzipCompressorInputStream(fis);
         TarArchiveInputStream tis = new TarArchiveInputStream(gis)) {

        TarArchiveEntry entry;
        PmcXmlParser parser = new PmcXmlParser();
        int count = 0;

        while ((entry = tis.getNextTarEntry()) != null) {
            if (!entry.isDirectory() && entry.getName().endsWith(".xml")) {
                // 스트림에서 XML 파싱
                JatsArticle article = parser.parseStream(tis);
                database.save(article);
                count++;

                if (count % 100 == 0) {
                    System.out.println("처리됨: " + count);
                }
            }
        }

        System.out.println("총 논문 수: " + count);
    }
}
```

---

### 배치 처리

```java
public void processPmcBulkDownload(Path downloadDir) throws Exception {
    // 모든 tar.gz 파일 찾기
    List<Path> archives = Files.walk(downloadDir)
        .filter(path -> path.toString().endsWith(".tar.gz"))
        .sorted()
        .toList();

    System.out.println("발견된 아카이브: " + archives.size() + "개");

    for (Path archive : archives) {
        System.out.println("\n처리 중: " + archive.getFileName());

        try {
            List<JatsArticle> articles = parser.parseTarGz(archive);

            // 데이터베이스에 배치 삽입
            database.batchInsert(articles);

            System.out.printf("✅ %s: %d개 논문%n",
                              archive.getFileName(),
                              articles.size());

        } catch (Exception e) {
            logger.error("❌ 처리 실패 {}: {}",
                         archive.getFileName(),
                         e.getMessage());
            // 다음 아카이브 계속 진행
        }
    }
}
```

---

## Sub-Article 재귀 구조

### Sub-Article이란?

Sub-article은 JATS 1.4의 **재귀 구조**로 다음을 나타냅니다:
- 본문의 번역본
- 관련 논문 또는 동반 자료
- 상당한 내용이 있는 부록
- 응답 및 논평

**주요 특징:** Sub-article은 sub-article을 포함할 수 있습니다(재귀 중첩).

---

### Sub-Article 구조

```xml
<article>
    <front>...</front>
    <body>...</body>

    <!-- 본문은 sub-article을 포함할 수 있음 -->
    <sub-article article-type="translation" xml:lang="es" id="S1">
        <front-stub>
            <title-group>
                <article-title>Título en español</article-title>
            </title-group>
        </front-stub>
        <body>...</body>

        <!-- Sub-article은 중첩된 sub-article을 포함할 수 있음 -->
        <sub-article article-type="addendum" id="S1-1">
            <front-stub>...</front-stub>
            <body>...</body>
        </sub-article>
    </sub-article>

    <sub-article article-type="reply" id="S2">
        <front-stub>...</front-stub>
        <body>...</body>
    </sub-article>
</article>
```

---

### Sub-Article 파싱

```java
PmcXmlParser parser = new PmcXmlParser();
Path xmlFile = Paths.get("PMC1234567.xml");
JatsArticle article = parser.parseFile(xmlFile);

// Sub-article 확인
List<SubArticle> subArticles = article.getSubArticles();

if (subArticles != null && !subArticles.isEmpty()) {
    System.out.println("발견된 sub-article: " + subArticles.size() + "개");

    for (SubArticle subArticle : subArticles) {
        // Sub-article 속성
        String articleType = subArticle.getArticleType().getValue();  // "translation", "reply" 등
        String id = subArticle.getId();                                // "S1"
        String lang = subArticle.getXmlLang();                        // "es", "fr" 등

        System.out.printf("Sub-article: type=%s, id=%s, lang=%s%n",
                          articleType, id, lang);

        // 메타데이터 접근
        if (subArticle.getFrontStub() != null) {
            TitleGroup titleGroup = subArticle.getFrontStub().getTitleGroup();
            if (titleGroup != null) {
                String title = titleGroup.getArticleTitle().getContent();
                System.out.println("제목: " + title);
            }
        }

        // 본문 접근
        if (subArticle.getBody() != null) {
            System.out.println("본문 내용 있음");
        }

        // 재귀: 중첩된 sub-article 확인
        List<SubArticle> nestedSubArticles = subArticle.getSubArticles();
        if (nestedSubArticles != null && !nestedSubArticles.isEmpty()) {
            System.out.println("  └─ " + nestedSubArticles.size() + "개의 중첩된 sub-article 있음");
        }
    }
}
```

---

### 일반적인 Sub-Article 유형

```java
// article-type 값
public enum ArticleType {
    TRANSLATION,         // 번역본
    ABSTRACT,            // 초록만
    LETTER,              // 서신
    REPLY,               // 답변
    ADDENDUM,            // 추가 사항
    COMMENTARY,          // 해설
    CORRECTION,          // 정정
    RETRACTION           // 철회
}
```

---

### 재귀 처리 패턴

```java
public void processSubArticles(List<SubArticle> subArticles, int depth) {
    if (subArticles == null || subArticles.isEmpty()) {
        return;
    }

    String indent = "  ".repeat(depth);

    for (SubArticle subArticle : subArticles) {
        String type = subArticle.getArticleType() != null ?
                      subArticle.getArticleType().getValue() : "unknown";
        String lang = subArticle.getXmlLang() != null ?
                      subArticle.getXmlLang() : "en";

        System.out.printf("%s└─ Sub-article: %s [%s]%n", indent, type, lang);

        // 데이터베이스에 저장
        database.saveSubArticle(subArticle, depth);

        // 중첩된 sub-article 재귀 처리
        if (subArticle.getSubArticles() != null) {
            processSubArticles(subArticle.getSubArticles(), depth + 1);
        }

        // Response 처리
        if (subArticle.getResponses() != null) {
            for (Response response : subArticle.getResponses()) {
                System.out.printf("%s  └─ Response: %s%n",
                                  indent,
                                  response.getResponseType());
            }
        }
    }
}

// 사용법
JatsArticle article = parser.parseFile(xmlFile);
processSubArticles(article.getSubArticles(), 0);
```

---

### 번역 처리

```java
// 모든 번역본 추출
public Map<String, SubArticle> extractTranslations(JatsArticle article) {
    Map<String, SubArticle> translations = new HashMap<>();

    if (article.getSubArticles() != null) {
        for (SubArticle subArticle : article.getSubArticles()) {
            ArticleType type = subArticle.getArticleType();

            if (type == ArticleType.TRANSLATION && subArticle.getXmlLang() != null) {
                String lang = subArticle.getXmlLang();
                translations.put(lang, subArticle);
            }
        }
    }

    return translations;
}

// 사용법
Map<String, SubArticle> translations = extractTranslations(article);
System.out.println("사용 가능한 언어: " + translations.keySet());

// 스페인어 번역 가져오기
SubArticle spanish = translations.get("es");
if (spanish != null) {
    String title = spanish.getFrontStub()
                          .getTitleGroup()
                          .getArticleTitle()
                          .getContent();
    System.out.println("스페인어 제목: " + title);
}
```

---

## Front vs Front-Stub

### 주요 차이점

| 기능 | `<front>` | `<front-stub>` |
|---------|-----------|----------------|
| 사용처 | 본문 논문 | Sub-article, response |
| Journal Meta | ✅ 필수 | ❌ 포함 안 됨 |
| Article Meta | ✅ 포함됨 | ✅ 포함됨 |
| 복잡도 | 전체 메타데이터 | 축약됨 |
| 크기 | 더 큼 | 더 작음 |

---

### Front 구조

**본문 논문의 완전한 front matter:**

```xml
<front>
    <!-- 저널 메타데이터 -->
    <journal-meta>
        <journal-id>...</journal-id>
        <journal-title-group>...</journal-title-group>
        <issn>...</issn>
        <publisher>...</publisher>
    </journal-meta>

    <!-- 논문 메타데이터 -->
    <article-meta>
        <article-id>...</article-id>
        <title-group>...</title-group>
        <contrib-group>...</contrib-group>
        <pub-date>...</pub-date>
        <abstract>...</abstract>
    </article-meta>
</front>
```

---

### Front-Stub 구조

**Sub-article의 축약된 front matter:**

```xml
<sub-article article-type="reply">
    <front-stub>
        <!-- journal-meta 없음 -->
        <!-- 논문별 메타데이터만 -->
        <title-group>
            <article-title>Author's Response</article-title>
        </title-group>
        <contrib-group>...</contrib-group>
    </front-stub>
    <body>...</body>
</sub-article>
```

---

### Front vs Front-Stub 파싱

```java
// 본문 논문 - front 있음
JatsArticle article = parser.parseFile(xmlFile);
Front front = article.getFront();

if (front != null) {
    // 저널 메타데이터 접근
    JournalMeta journalMeta = front.getJournalMeta();
    String journalTitle = journalMeta.getJournalTitleGroup()
                                     .getJournalTitle()
                                     .getValue();

    // 논문 메타데이터 접근
    ArticleMeta articleMeta = front.getArticleMeta();
    String articleTitle = articleMeta.getTitleGroup()
                                     .getArticleTitle()
                                     .getContent();
}

// Sub-article - front-stub 있음
SubArticle subArticle = article.getSubArticles().get(0);

// 어떤 유형이 있는지 확인
if (subArticle.getFront() != null) {
    // 전체 front (sub-article에서는 드물음)
    Front subFront = subArticle.getFront();
    // journal-meta와 article-meta 모두 있음
} else if (subArticle.getFrontStub() != null) {
    // Front-stub (sub-article에서 일반적)
    FrontStub frontStub = subArticle.getFrontStub();

    // journal-meta 사용 불가
    // 논문별 메타데이터만
    TitleGroup titleGroup = frontStub.getTitleGroup();
    List<ContribGroup> contribGroups = frontStub.getContribGroups();
    List<PubDate> pubDates = frontStub.getPubDates();
}
```

---

### 통합 메타데이터 접근 패턴

```java
public class MetadataExtractor {

    public String getTitle(SubArticle subArticle) {
        // 먼저 front 시도
        if (subArticle.getFront() != null) {
            ArticleMeta meta = subArticle.getFront().getArticleMeta();
            if (meta != null && meta.getTitleGroup() != null) {
                return meta.getTitleGroup().getArticleTitle().getContent();
            }
        }

        // front-stub로 폴백
        if (subArticle.getFrontStub() != null) {
            TitleGroup titleGroup = subArticle.getFrontStub().getTitleGroup();
            if (titleGroup != null && titleGroup.getArticleTitle() != null) {
                return titleGroup.getArticleTitle().getContent();
            }
        }

        return null;
    }

    public List<ContribGroup> getContributors(SubArticle subArticle) {
        // 먼저 front 시도
        if (subArticle.getFront() != null) {
            ArticleMeta meta = subArticle.getFront().getArticleMeta();
            if (meta != null) {
                return meta.getContribGroups();
            }
        }

        // front-stub로 폴백
        if (subArticle.getFrontStub() != null) {
            return subArticle.getFrontStub().getContribGroups();
        }

        return Collections.emptyList();
    }
}
```

---

## Response 요소

### Response란?

Response는 논문에 대한 공식 답변을 나타냅니다:
- 동료 심사에 대한 저자의 답변
- 토론
- 심사자 보고서
- 편집자 의견
- 정정 및 추가 사항

---

### Response 구조

```xml
<article>
    <front>...</front>
    <body>...</body>
    <back>...</back>

    <!-- 논문 수준의 Response -->
    <response response-type="reply" id="R1">
        <front-stub>
            <title-group>
                <article-title>Author's Response to Reviewer Comments</article-title>
            </title-group>
            <contrib-group>...</contrib-group>
        </front-stub>
        <body>
            <p>We thank the reviewer for their insightful comments...</p>
        </body>
    </response>

    <response response-type="reviewer-report" id="R2">
        <front-stub>
            <title-group>
                <article-title>Reviewer Report</article-title>
            </title-group>
        </front-stub>
        <body>
            <p>This manuscript presents...</p>
        </body>
    </response>
</article>
```

---

### Response 파싱

```java
JatsArticle article = parser.parseFile(xmlFile);
List<Response> responses = article.getResponses();

if (responses != null && !responses.isEmpty()) {
    System.out.println("발견된 response: " + responses.size() + "개");

    for (Response response : responses) {
        // Response 속성
        String id = response.getId();                           // "R1"
        ResponseType type = response.getResponseType();         // REPLY, REVIEWER_REPORT 등
        String lang = response.getXmlLang();                    // "en"

        System.out.printf("Response: id=%s, type=%s%n", id, type);

        // front-stub를 통한 메타데이터 접근
        if (response.getFrontStub() != null) {
            FrontStub frontStub = response.getFrontStub();
            TitleGroup titleGroup = frontStub.getTitleGroup();

            if (titleGroup != null) {
                String title = titleGroup.getArticleTitle().getContent();
                System.out.println("제목: " + title);
            }

            // 기여자
            List<ContribGroup> contribGroups = frontStub.getContribGroups();
            if (contribGroups != null) {
                System.out.println("기여자 수: " + contribGroups.size());
            }
        }

        // 본문 내용 접근
        if (response.getBody() != null) {
            Body body = response.getBody();
            System.out.println("응답 본문 있음");
        }
    }
}
```

---

### Response 유형

```java
public enum ResponseType {
    ADDENDUM,           // 추가 사항
    DISCUSSION,         // 토론
    REPLY,              // 답변
    AUTHOR_COMMENT,     // 저자 코멘트
    REVIEWER_REPORT     // 심사자 보고서
}
```

---

### 프로덕션 패턴

```java
public void extractPeerReview(JatsArticle article) {
    List<Response> responses = article.getResponses();

    if (responses == null) {
        return;
    }

    // 유형별로 분리
    List<Response> reviewerReports = new ArrayList<>();
    List<Response> authorReplies = new ArrayList<>();

    for (Response response : responses) {
        ResponseType type = response.getResponseType();

        if (type == ResponseType.REVIEWER_REPORT) {
            reviewerReports.add(response);
        } else if (type == ResponseType.REPLY ||
                   type == ResponseType.AUTHOR_COMMENT) {
            authorReplies.add(response);
        }
    }

    // 심사자 보고서 처리
    for (Response report : reviewerReports) {
        String reportId = report.getId();
        String reportContent = extractBodyText(report.getBody());

        database.saveReviewerReport(article.getId(), reportId, reportContent);
        System.out.println("심사자 보고서 저장됨: " + reportId);
    }

    // 저자 답변 처리
    for (Response reply : authorReplies) {
        String replyId = reply.getId();
        String replyContent = extractBodyText(reply.getBody());

        database.saveAuthorReply(article.getId(), replyId, replyContent);
        System.out.println("저자 답변 저장됨: " + replyId);
    }
}
```

---

## FloatsGroup

### FloatsGroup이란?

FloatsGroup은 본문에서 참조되지만 끝부분이나 별도 섹션에 배치될 수 있는 **부유 요소**를 포함합니다:
- 그림(이미지, 다이어그램)
- 테이블
- 박스형 텍스트
- 화학 구조
- 보충 자료

---

### FloatsGroup 구조

```xml
<article>
    <front>...</front>
    <body>
        <p>See <xref ref-type="fig" rid="fig1">Figure 1</xref>...</p>
        <p>Data in <xref ref-type="table" rid="tbl1">Table 1</xref>...</p>
    </body>

    <floats-group>
        <fig id="fig1">
            <label>Figure 1</label>
            <caption>
                <title>Study Design</title>
                <p>Schematic representation...</p>
            </caption>
            <graphic xlink:href="fig1.jpg"/>
        </fig>

        <table-wrap id="tbl1">
            <label>Table 1</label>
            <caption>
                <title>Patient Demographics</title>
            </caption>
            <table>...</table>
        </table-wrap>

        <supplementary-material id="S1">
            <label>Supplementary Material 1</label>
            <caption>
                <title>Additional Data</title>
            </caption>
        </supplementary-material>
    </floats-group>
</article>
```

---

### FloatsGroup 파싱

```java
JatsArticle article = parser.parseFile(xmlFile);
FloatsGroup floatsGroup = article.getFloatsGroup();

if (floatsGroup != null) {
    // 그림 처리
    List<Fig> figures = floatsGroup.getFigs();
    if (figures != null) {
        System.out.println("그림 수: " + figures.size());
        for (Fig fig : figures) {
            String id = fig.getId();                    // "fig1"
            String label = fig.getLabel().getValue();  // "Figure 1"

            Caption caption = fig.getCaption();
            if (caption != null && caption.getTitle() != null) {
                String title = caption.getTitle().getContent();
                System.out.println("  " + label + ": " + title);
            }

            // 이미지 파일 경로 가져오기
            List<Graphic> graphics = fig.getGraphics();
            if (graphics != null) {
                for (Graphic graphic : graphics) {
                    String href = graphic.getXlinkHref();  // "fig1.jpg"
                    System.out.println("    이미지: " + href);
                }
            }
        }
    }

    // 테이블 처리
    List<TableWrap> tables = floatsGroup.getTableWraps();
    if (tables != null) {
        System.out.println("테이블 수: " + tables.size());
        for (TableWrap tableWrap : tables) {
            String id = tableWrap.getId();
            String label = tableWrap.getLabel() != null ?
                           tableWrap.getLabel().getValue() : null;
            System.out.println("  " + label);

            // 테이블 내용 접근
            if (tableWrap.getTable() != null) {
                Table table = tableWrap.getTable();
                // 테이블 데이터 처리...
            }
        }
    }

    // 보충 자료 처리
    List<SupplementaryMaterial> supplements = floatsGroup.getSupplementaryMaterials();
    if (supplements != null) {
        System.out.println("보충 자료 수: " + supplements.size());
        for (SupplementaryMaterial supp : supplements) {
            String id = supp.getId();
            String label = supp.getLabel() != null ?
                           supp.getLabel().getValue() : null;
            System.out.println("  " + label);
        }
    }
}
```

---

### 프로덕션 패턴: 그림 추출

```java
public class FigureExtractor {

    public List<FigureMetadata> extractFigures(JatsArticle article) {
        List<FigureMetadata> figures = new ArrayList<>();
        FloatsGroup floatsGroup = article.getFloatsGroup();

        if (floatsGroup == null || floatsGroup.getFigs() == null) {
            return figures;
        }

        for (Fig fig : floatsGroup.getFigs()) {
            FigureMetadata metadata = new FigureMetadata();

            // ID와 라벨
            metadata.setId(fig.getId());
            metadata.setLabel(fig.getLabel() != null ?
                             fig.getLabel().getValue() : null);

            // 캡션
            Caption caption = fig.getCaption();
            if (caption != null) {
                if (caption.getTitle() != null) {
                    metadata.setTitle(caption.getTitle().getContent());
                }
                // 캡션 단락 추출
                List<String> captionParagraphs = extractParagraphs(caption.getContent());
                metadata.setCaptionText(String.join(" ", captionParagraphs));
            }

            // 이미지 파일
            List<Graphic> graphics = fig.getGraphics();
            if (graphics != null) {
                List<String> imageFiles = graphics.stream()
                    .map(Graphic::getXlinkHref)
                    .filter(href -> href != null)
                    .toList();
                metadata.setImageFiles(imageFiles);
            }

            figures.add(metadata);
        }

        return figures;
    }

    @Data
    public static class FigureMetadata {
        private String id;
        private String label;
        private String title;
        private String captionText;
        private List<String> imageFiles;
    }
}
```

---

## 네임스페이스 처리

### PMC의 일반적인 네임스페이스

PMC XML은 여러 네임스페이스를 사용합니다:

| 네임스페이스 | 접두사 | 사용처 |
|-----------|--------|-------|
| XLink | `xlink` | 링크 및 참조 |
| MathML | `mml` | 수학 공식 |
| XHTML | `xhtml` | HTML 테이블 요소 |

---

### XLink 네임스페이스

**사용처:** 그래픽, 외부 링크 등의 링크 속성

```xml
<graphic xlink:href="figure1.jpg"
         xlink:type="simple"
         xlink:role="original-image"
         xlink:show="embed"/>

<ext-link ext-link-type="uri"
          xlink:href="https://example.com"
          xlink:type="simple">사이트 방문</ext-link>
```

---

### XLink 속성 파싱

```java
// xlink:href가 있는 그래픽
Fig fig = floatsGroup.getFigs().get(0);
List<Graphic> graphics = fig.getGraphics();

for (Graphic graphic : graphics) {
    String href = graphic.getXlinkHref();       // "figure1.jpg"
    String type = graphic.getXlinkType();       // "simple"
    String role = graphic.getXlinkRole();       // "original-image"
    String show = graphic.getXlinkShow();       // "embed"

    System.out.printf("그래픽: href=%s, type=%s%n", href, type);
}

// xlink:href가 있는 외부 링크
List<ExtLink> extLinks = article.getFront()
                                .getArticleMeta()
                                .getExtLinks();

for (ExtLink extLink : extLinks) {
    String href = extLink.getXlinkHref();       // "https://example.com"
    String type = extLink.getExtLinkType();     // "uri"
    String content = extLink.getContent();      // "사이트 방문"

    System.out.printf("외부 링크: %s -> %s%n", content, href);
}
```

---

### MathML 네임스페이스

**사용처:** 수학 공식

```xml
<inline-formula>
    <mml:math display="inline">
        <mml:mrow>
            <mml:mi>E</mml:mi>
            <mml:mo>=</mml:mo>
            <mml:mi>m</mml:mi>
            <mml:msup>
                <mml:mi>c</mml:mi>
                <mml:mn>2</mml:mn>
            </mml:msup>
        </mml:mrow>
    </mml:math>
</inline-formula>
```

---

### MathML 파싱

```java
// 인라인 공식
Body body = article.getBody();
List<Sec> sections = body.getSections();

for (Sec section : sections) {
    List<P> paragraphs = section.getParagraphs();

    for (P paragraph : paragraphs) {
        List<InlineFormula> formulas = paragraph.getInlineFormulas();

        if (formulas != null) {
            for (InlineFormula formula : formulas) {
                MmlMath math = formula.getMmlMath();

                if (math != null) {
                    String mathContent = math.getContent();
                    System.out.println("공식: " + mathContent);
                }
            }
        }
    }
}

// 디스플레이 공식
List<DispFormula> dispFormulas = body.getDisplayFormulas();
if (dispFormulas != null) {
    for (DispFormula dispFormula : dispFormulas) {
        MmlMath math = dispFormula.getMmlMath();
        if (math != null) {
            System.out.println("디스플레이 공식: " + math.getContent());
        }
    }
}
```

---

## 테이블 처리

### JATS의 XHTML 테이블

PMC 논문은 전체 HTML 테이블 마크업이 있는 XHTML 테이블을 포함할 수 있습니다:

```xml
<table-wrap id="tbl1">
    <label>Table 1</label>
    <caption>
        <title>Patient Demographics</title>
    </caption>
    <table frame="hsides" rules="groups">
        <thead>
            <tr>
                <th>Characteristic</th>
                <th>Value</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Age (years)</td>
                <td>45.2 ± 12.3</td>
            </tr>
            <tr>
                <td>Gender (M/F)</td>
                <td>120/80</td>
            </tr>
        </tbody>
    </table>
</table-wrap>
```

---

### 테이블 파싱

```java
FloatsGroup floatsGroup = article.getFloatsGroup();
List<TableWrap> tableWraps = floatsGroup.getTableWraps();

for (TableWrap tableWrap : tableWraps) {
    String id = tableWrap.getId();                      // "tbl1"
    String label = tableWrap.getLabel().getValue();    // "Table 1"

    Caption caption = tableWrap.getCaption();
    String title = caption.getTitle().getContent();    // "Patient Demographics"

    System.out.printf("테이블: %s - %s%n", label, title);

    // 테이블 구조 접근
    Table table = tableWrap.getTable();
    if (table != null) {
        // 테이블 속성
        String frame = table.getFrame();    // "hsides"
        String rules = table.getRules();    // "groups"

        // 테이블 헤드
        Thead thead = table.getThead();
        if (thead != null) {
            List<Tr> headerRows = thead.getTrs();
            for (Tr tr : headerRows) {
                List<Th> headers = tr.getThs();
                for (Th th : headers) {
                    String headerText = th.getContent();
                    System.out.println("헤더: " + headerText);
                }
            }
        }

        // 테이블 본문
        Tbody tbody = table.getTbody();
        if (tbody != null) {
            List<Tr> bodyRows = tbody.getTrs();
            for (Tr tr : bodyRows) {
                List<Td> cells = tr.getTds();
                for (Td td : cells) {
                    String cellContent = td.getContent();
                    System.out.println("셀: " + cellContent);
                }
            }
        }
    }
}
```

---

### 테이블 데이터 추출

```java
public class TableExtractor {

    public List<String[]> extractTableData(TableWrap tableWrap) {
        List<String[]> rows = new ArrayList<>();
        Table table = tableWrap.getTable();

        if (table == null) {
            return rows;
        }

        // 헤더 행 추출
        if (table.getThead() != null) {
            for (Tr tr : table.getThead().getTrs()) {
                String[] headerRow = tr.getThs().stream()
                    .map(Th::getContent)
                    .toArray(String[]::new);
                rows.add(headerRow);
            }
        }

        // 데이터 행 추출
        if (table.getTbody() != null) {
            for (Tr tr : table.getTbody().getTrs()) {
                String[] dataRow = tr.getTds().stream()
                    .map(Td::getContent)
                    .toArray(String[]::new);
                rows.add(dataRow);
            }
        }

        return rows;
    }

    public void exportTableToCsv(TableWrap tableWrap, Path outputFile) throws IOException {
        List<String[]> rows = extractTableData(tableWrap);

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            for (String[] row : rows) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
        }

        System.out.println("테이블이 다음으로 내보내짐: " + outputFile);
    }
}
```

---

## 모범 사례

### 1. 항상 TAR.GZ 압축 해제 오류 처리

```java
// ✅ 좋음 - 압축 해제 오류 처리
try {
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
} catch (IOException e) {
    logger.error("아카이브 손상됨: {}", e.getMessage());
    downloadArchive(tarGzFile);  // 재다운로드
}

// ❌ 나쁨 - 압축 해제가 성공한다고 가정
List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
```

---

### 2. Sub-Article 확인

```java
// ✅ 좋음 - 본문과 sub-article 모두 처리
JatsArticle article = parser.parseFile(xmlFile);
database.save(article);

if (article.getSubArticles() != null) {
    for (SubArticle subArticle : article.getSubArticles()) {
        database.saveSubArticle(subArticle);
    }
}

// ❌ 나쁨 - 본문만 처리
database.save(article);  // Sub-article 무시됨
```

---

### 3. Front vs Front-Stub 처리

```java
// ✅ 좋음 - front와 front-stub 모두 확인
String getTitle(SubArticle subArticle) {
    if (subArticle.getFront() != null) {
        return subArticle.getFront().getArticleMeta()
                        .getTitleGroup().getArticleTitle().getContent();
    } else if (subArticle.getFrontStub() != null) {
        return subArticle.getFrontStub()
                        .getTitleGroup().getArticleTitle().getContent();
    }
    return null;
}

// ❌ 나쁨 - front만 있다고 가정
return subArticle.getFront().getArticleMeta()...  // NullPointerException
```

---

### 4. 그림과 테이블 추출

```java
// ✅ 좋음 - 모든 float 추출
FloatsGroup floatsGroup = article.getFloatsGroup();
if (floatsGroup != null) {
    extractFigures(floatsGroup.getFigs());
    extractTables(floatsGroup.getTableWraps());
    extractSupplements(floatsGroup.getSupplementaryMaterials());
}

// ❌ 나쁨 - floats-group 무시
// 그림과 테이블이 누락됨
```

---

### 5. XLink 속성 검증

```java
// ✅ 좋음 - xlink:href 검증
Graphic graphic = fig.getGraphics().get(0);
String href = graphic.getXlinkHref();

if (href != null && !href.trim().isEmpty()) {
    // 파일 존재 확인
    Path imagePath = Paths.get(baseDir, href);
    if (Files.exists(imagePath)) {
        processImage(imagePath);
    } else {
        logger.warn("이미지를 찾을 수 없음: {}", href);
    }
}

// ❌ 나쁨 - xlink:href가 유효하다고 가정
Path imagePath = Paths.get(graphic.getXlinkHref());  // NullPointerException
```

---

## 다음 단계

- [USAGE.ko.md](USAGE.ko.md) - API 사용 가이드
- [STREAMING.ko.md](STREAMING.ko.md) - 대용량 파일 처리
- [VALIDATION.ko.md](VALIDATION.ko.md) - 검증 및 에러 처리
- [PUBMED-SPECIFICS.ko.md](PUBMED-SPECIFICS.ko.md) - PubMed 특수 케이스

---

**문서 버전:** 1.0
**최종 업데이트:** 2026-01-12
**파서 버전:** 1.0.0-SNAPSHOT
