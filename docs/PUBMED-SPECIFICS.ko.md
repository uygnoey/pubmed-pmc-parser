# PubMed 특수 케이스 가이드

[**한국어**](PUBMED-SPECIFICS.ko.md) | [English](PUBMED-SPECIFICS.md)

PubMed 특유의 특수 케이스와 예외 상황 처리에 대한 완전한 가이드입니다.

---

## 목차

- [MedlineDate 처리](#medlinedate-처리)
- [DeleteCitation 처리](#deletecitation-처리)
- [PubmedBookArticle vs PubmedArticle](#pubmedbookarticle-vs-pubmedarticle)
- [CommentsCorrections 타입](#commentscorrections-타입)
- [특수 날짜 형식](#특수-날짜-형식)
- [PMID 예외 케이스](#pmid-예외-케이스)

---

## MedlineDate 처리

### MedlineDate란?

MedlineDate는 구조화된 날짜 요소(Year, Month, Day)로 출판 날짜를 표현할 수 없을 때 사용되는 **비표준 날짜 문자열**입니다.

**일반적인 형식:**
- 계절 날짜: `"2024 Spring"`, `"2024 Winter-Spring"`
- 월 범위: `"2024 Jan-Feb"`, `"2024 Nov-Dec"`
- 분기 날짜: `"2024 Q1"`, `"2024 1st Quarter"`
- 연도 범위: `"2023-2024"`
- 부분 날짜: `"2024 Jan 15-Feb 20"`

---

### MedlineDate가 나타나는 경우

MedlineDate는 `<PubDate>`에 구조화된 날짜가 없을 때 나타납니다:

```xml
<!-- 표준 구조화 날짜 -->
<PubDate>
    <Year>2024</Year>
    <Month>Jan</Month>
    <Day>15</Day>
</PubDate>

<!-- 비표준 날짜 → MedlineDate -->
<PubDate>
    <MedlineDate>2024 Spring</MedlineDate>
</PubDate>
```

---

### MedlineDate 파싱

#### 기본 파싱

```java
PubmedXmlParser parser = new PubmedXmlParser();
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

parser.parseStream(xmlFile, article -> {
    PubDate pubDate = article.getMedlineCitation()
                             .getArticle()
                             .getJournal()
                             .getJournalIssue()
                             .getPubDate();

    // MedlineDate 존재 확인
    if (pubDate.getMedlineDate() != null) {
        String dateStr = pubDate.getMedlineDate().getValue();
        System.out.println("비표준 날짜: " + dateStr);

        // 적절히 처리
        handleMedlineDate(dateStr);
    } else {
        // 표준 날짜 필드
        String year = pubDate.getYear();
        String month = pubDate.getMonth();
        String day = pubDate.getDay();
    }
});
```

---

### MedlineDate 형식 예제

#### 1. 계절 날짜

```java
// 형식: "YYYY Season"
MedlineDate medlineDate = pubDate.getMedlineDate();
String value = medlineDate.getValue();

// 예제:
"2024 Spring"          // 2024년 봄
"2024 Summer"          // 2024년 여름
"2024 Fall"            // 2024년 가을
"2024 Winter"          // 2024년 겨울
"2024 Winter-Spring"   // 2024년 겨울-봄
```

**처리 전략:**
```java
if (value.matches("\\d{4} (Spring|Summer|Fall|Winter).*")) {
    // 연도 추출
    int year = Integer.parseInt(value.substring(0, 4));

    // 계절을 근사 월로 매핑
    String season = value.substring(5).trim();
    int approximateMonth = switch (season) {
        case "Spring" -> 4;  // 4월
        case "Summer" -> 7;  // 7월
        case "Fall", "Autumn" -> 10;  // 10월
        case "Winter" -> 1;  // 1월
        default -> 1;
    };

    // 근사 날짜로 저장
    LocalDate approximateDate = LocalDate.of(year, approximateMonth, 1);
}
```

---

#### 2. 월 범위

```java
// 형식: "YYYY Mon-Mon"
// 예제:
"2024 Jan-Feb"         // 2024년 1월~2월
"2024 Nov-Dec"         // 2024년 11월~12월
"2024 Mar-May"         // 2024년 3월~5월
```

**처리 전략:**
```java
if (value.matches("\\d{4} [A-Za-z]{3}-[A-Za-z]{3}")) {
    String[] parts = value.split(" ");
    int year = Integer.parseInt(parts[0]);
    String[] months = parts[1].split("-");

    String startMonth = months[0];  // "Jan"
    String endMonth = months[1];    // "Feb"

    // 시작일과 종료일 저장
    LocalDate startDate = parseMonthToDate(year, startMonth);
    LocalDate endDate = parseMonthToDate(year, endMonth).plusMonths(1).minusDays(1);
}
```

---

#### 3. 분기 날짜

```java
// 형식: "YYYY Q#" 또는 "YYYY #st Quarter"
// 예제:
"2024 Q1"              // 2024년 1분기
"2024 Q2"              // 2024년 2분기
"2024 1st Quarter"     // 2024년 1분기
"2024 2nd Qu"          // 2024년 2분기
```

**처리 전략:**
```java
if (value.matches("\\d{4} (Q\\d|\\d(st|nd|rd|th) Qu.*)")) {
    int year = Integer.parseInt(value.substring(0, 4));

    // 분기 번호 추출
    int quarter;
    if (value.contains("Q")) {
        quarter = Integer.parseInt(value.replaceAll(".*Q(\\d).*", "$1"));
    } else {
        quarter = Integer.parseInt(value.replaceAll("\\d{4} (\\d).*", "$1"));
    }

    // 분기를 시작 월로 매핑
    int startMonth = (quarter - 1) * 3 + 1;
    LocalDate quarterStart = LocalDate.of(year, startMonth, 1);
}
```

---

#### 4. 연도 범위

```java
// 형식: "YYYY-YYYY"
// 예제:
"2023-2024"            // 2023년~2024년
"2022-2023"            // 2022년~2023년
```

**처리 전략:**
```java
if (value.matches("\\d{4}-\\d{4}")) {
    String[] years = value.split("-");
    int startYear = Integer.parseInt(years[0]);
    int endYear = Integer.parseInt(years[1]);

    // 시작 연도 또는 중간값 사용
    int representativeYear = startYear;  // 또는: (startYear + endYear) / 2
}
```

---

### DateParser 유틸리티 사용

파서에는 MedlineDate 파싱을 위한 유틸리티 클래스가 포함되어 있습니다:

```java
import com.brillianttiger.bio.parser.common.util.DateParser;
import com.brillianttiger.bio.parser.common.model.DateComponents;

// MedlineDate 파싱
String medlineDateStr = "2024 Jan-Feb";
DateParser.ParsedDate parsed = DateParser.parseMedlineDate(medlineDateStr);

// 파싱된 구성 요소 접근
String year = parsed.getYear();        // "2024"
String month = parsed.getMonth();      // "Jan"
String endMonth = parsed.getEndMonth(); // "Feb"
boolean isRange = parsed.hasDateRange(); // true

// DateComponents로 변환
DateComponents components = DateParser.parseMedlineDateToComponents(medlineDateStr);
System.out.println(components.getYear());         // "2024"
System.out.println(components.getMonth());        // "01"
System.out.println(components.getMedlineDate());  // "2024 Jan-Feb"
```

---

### 프로덕션 패턴

```java
private LocalDate extractPublicationDate(PubDate pubDate) {
    // 먼저 구조화된 날짜 시도
    if (pubDate.getYear() != null) {
        int year = Integer.parseInt(pubDate.getYear());
        int month = pubDate.getMonth() != null ? parseMonth(pubDate.getMonth()) : 1;
        int day = pubDate.getDay() != null ? Integer.parseInt(pubDate.getDay()) : 1;
        return LocalDate.of(year, month, day);
    }

    // MedlineDate로 폴백
    if (pubDate.getMedlineDate() != null) {
        String medlineDate = pubDate.getMedlineDate().getValue();
        DateParser.ParsedDate parsed = DateParser.parseMedlineDate(medlineDate);

        if (parsed != null && parsed.getYear() != null) {
            int year = Integer.parseInt(parsed.getYear());
            int month = parsed.getMonth() != null ? parseMonth(parsed.getMonth()) : 1;
            int day = parsed.getDay() != null ? Integer.parseInt(parsed.getDay()) : 1;
            return LocalDate.of(year, month, day);
        }
    }

    // 날짜 정보 없음
    return null;
}

private int parseMonth(String monthStr) {
    return switch (monthStr.toLowerCase()) {
        case "jan", "january", "01" -> 1;
        case "feb", "february", "02" -> 2;
        case "mar", "march", "03" -> 3;
        case "apr", "april", "04" -> 4;
        case "may", "05" -> 5;
        case "jun", "june", "06" -> 6;
        case "jul", "july", "07" -> 7;
        case "aug", "august", "08" -> 8;
        case "sep", "september", "09" -> 9;
        case "oct", "october", "10" -> 10;
        case "nov", "november", "11" -> 11;
        case "dec", "december", "12" -> 12;
        default -> 1;
    };
}
```

---

## DeleteCitation 처리

### DeleteCitation이란?

DeleteCitation은 **PubMed 업데이트 파일**에 나타나며, 데이터베이스에서 삭제된 PMID 목록을 포함합니다.

**논문이 삭제되는 이유:**
- 중복 항목
- 데이터 품질 문제
- 저작권 위반
- 논문 철회
- 색인 오류

---

### 파일 구조

업데이트 파일(`pubmed25n1275.xml.gz` 등)은 다음을 포함할 수 있습니다:
- `<PubmedArticle>` - 신규 또는 업데이트된 논문
- `<PubmedBookArticle>` - 신규 또는 업데이트된 도서 논문
- `<DeleteCitation>` - 삭제된 PMID

```xml
<PubmedArticleSet>
    <PubmedArticle>
        <!-- 업데이트된 논문 데이터 -->
    </PubmedArticle>

    <DeleteCitation>
        <PMID Version="1">12345678</PMID>
        <PMID Version="1">87654321</PMID>
        <PMID Version="1">11223344</PMID>
    </DeleteCitation>
</PubmedArticleSet>
```

---

### DeleteCitation 추출

#### 방법 1: DeleteCitation만 추출

`extractDeleteCitation()`을 사용하여 삭제 목록만 가져옵니다:

```java
PubmedXmlParser parser = new PubmedXmlParser();
Path updateFile = Paths.get("pubmed25n1275.xml.gz");

// 삭제된 PMID 추출
DeleteCitation deleteCitation = parser.extractDeleteCitation(updateFile);

if (deleteCitation != null) {
    List<PMID> deletedPmids = deleteCitation.getPmids();
    System.out.println("삭제된 PMID 수: " + deletedPmids.size());

    // 데이터베이스에서 삭제
    for (PMID pmid : deletedPmids) {
        String pmidValue = pmid.getValue();
        database.delete(pmidValue);
        logger.info("PMID 삭제됨: {}", pmidValue);
    }
}
```

---

#### 방법 2: 전체 파일 파싱

`parseFile()`을 사용하여 논문과 삭제 항목을 모두 가져옵니다:

```java
Path updateFile = Paths.get("pubmed25n1275.xml.gz");
PubmedArticleSet articleSet = parser.parseFile(updateFile);

// 신규/업데이트 논문 처리
List<PubmedArticle> articles = articleSet.getPubmedArticles();
for (PubmedArticle article : articles) {
    String pmid = article.getMedlineCitation().getPmid().getValue();
    database.upsert(article);  // 삽입 또는 업데이트
}

// 삭제 항목 처리
DeleteCitation deleteCitation = articleSet.getDeleteCitation();
if (deleteCitation != null) {
    for (PMID pmid : deleteCitation.getPmids()) {
        database.delete(pmid.getValue());
    }
}
```

---

### 프로덕션 워크플로

#### 일일 업데이트 처리

```java
public void processUpdateFile(Path updateFile) throws Exception {
    System.out.println("업데이트 파일 처리 중: " + updateFile);

    // 1. 파일 무결성 검증
    boolean md5Valid = Md5Verifier.verifyPubmedFile(updateFile);
    if (!md5Valid) {
        throw new IOException("MD5 검증 실패: " + updateFile);
    }

    // 2. 업데이트 파일 파싱
    PubmedXmlParser parser = new PubmedXmlParser();
    PubmedArticleSet articleSet = parser.parseFile(updateFile);

    // 3. 업데이트 처리
    int updateCount = 0;
    List<PubmedArticle> articles = articleSet.getPubmedArticles();

    for (PubmedArticle article : articles) {
        String pmid = article.getMedlineCitation().getPmid().getValue();

        // Upsert: 신규면 삽입, 존재하면 업데이트
        database.upsert(article);
        updateCount++;

        if (updateCount % 100 == 0) {
            System.out.println("처리된 업데이트: " + updateCount);
        }
    }

    // 4. 삭제 항목 처리
    DeleteCitation deleteCitation = articleSet.getDeleteCitation();
    int deleteCount = 0;

    if (deleteCitation != null) {
        for (PMID pmid : deleteCitation.getPmids()) {
            String pmidValue = pmid.getValue();

            // 데이터베이스에서 삭제
            database.delete(pmidValue);
            deleteCount++;

            // 삭제 로그
            logger.info("PMID 삭제됨: {}", pmidValue);
        }
    }

    // 5. 요약
    System.out.printf("업데이트 완료: %d개 업데이트, %d개 삭제%n",
                      updateCount, deleteCount);
}
```

---

#### DeleteCitation과 함께 스트리밍

```java
AtomicInteger updateCount = new AtomicInteger(0);
AtomicInteger deleteCount = new AtomicInteger(0);

// 논문 스트리밍
parser.parseStream(updateFile, article -> {
    database.upsert(article);
    updateCount.incrementAndGet();
});

// 삭제 항목 별도 추출
DeleteCitation deleteCitation = parser.extractDeleteCitation(updateFile);
if (deleteCitation != null) {
    for (PMID pmid : deleteCitation.getPmids()) {
        database.delete(pmid.getValue());
        deleteCount.incrementAndGet();
    }
}

System.out.printf("업데이트: %d개, 삭제: %d개%n",
                  updateCount.get(), deleteCount.get());
```

---

### 트랜잭션 안전성

```java
public void processUpdateFileTransactional(Path updateFile) throws Exception {
    try {
        database.beginTransaction();

        // 업데이트 처리
        PubmedArticleSet articleSet = parser.parseFile(updateFile);

        // 논문 Upsert
        for (PubmedArticle article : articleSet.getPubmedArticles()) {
            database.upsert(article);
        }

        // 논문 삭제
        DeleteCitation deleteCitation = articleSet.getDeleteCitation();
        if (deleteCitation != null) {
            for (PMID pmid : deleteCitation.getPmids()) {
                database.delete(pmid.getValue());
            }
        }

        // 트랜잭션 커밋
        database.commit();
        System.out.println("✅ 업데이트 트랜잭션 커밋됨");

    } catch (Exception e) {
        database.rollback();
        logger.error("❌ 업데이트 트랜잭션 실패: {}", e.getMessage());
        throw e;
    }
}
```

---

## PubmedBookArticle vs PubmedArticle

### 차이점

PubMed는 두 가지 유형의 콘텐츠를 포함합니다:

| 기능 | PubmedArticle | PubmedBookArticle |
|---------|---------------|-------------------|
| 루트 요소 | `<MedlineCitation>` | `<BookDocument>` |
| 콘텐츠 유형 | 저널 논문 | 도서 챕터, 도서 |
| 메타데이터 | `<Article>` | `<Book>` |
| Volume/Issue | 있음 | 없음 (섹션 사용) |
| ISBN | 없음 | 있음 |
| 저자 | AuthorList | AuthorList (유사) |

---

### PubmedBookArticle 파싱

#### 유형 감지

```java
parser.parseStreamAll(xmlFile,
    article -> {
        // PubmedArticle 처리
        String pmid = article.getMedlineCitation().getPmid().getValue();
        String title = article.getMedlineCitation().getArticle().getArticleTitle().getValue();
        System.out.println("논문 PMID " + pmid + ": " + title);
    },
    bookArticle -> {
        // PubmedBookArticle 처리
        String pmid = bookArticle.getBookDocument().getPmid().getValue();
        // 도서 전용 처리
        System.out.println("도서 PMID " + pmid);
    }
);
```

---

#### PubmedBookArticle 구조

```java
PubmedBookArticle bookArticle = // 파싱됨

// BookDocument 접근
BookDocument bookDoc = bookArticle.getBookDocument();

// PMID
String pmid = bookDoc.getPmid().getValue();

// 도서 메타데이터
Book book = bookDoc.getBook();
if (book != null) {
    // 제목
    BookTitle bookTitle = book.getBookTitle();
    String title = bookTitle != null ? bookTitle.getValue() : null;

    // ISBN
    List<ISBN> isbns = book.getIsbns();
    for (ISBN isbn : isbns) {
        System.out.println("ISBN: " + isbn.getValue());
    }

    // 저자
    AuthorList authorList = book.getAuthorList();
    if (authorList != null) {
        for (Author author : authorList.getAuthors()) {
            // PubmedArticle 저자 처리와 유사
        }
    }
}
```

---

### 통합 처리

```java
public void processAllContent(Path xmlFile) throws Exception {
    AtomicInteger articleCount = new AtomicInteger(0);
    AtomicInteger bookCount = new AtomicInteger(0);

    parser.parseStreamAll(xmlFile,
        // 논문 핸들러
        article -> {
            String pmid = article.getMedlineCitation().getPmid().getValue();
            database.saveArticle(article);
            articleCount.incrementAndGet();
        },

        // 도서 논문 핸들러
        bookArticle -> {
            String pmid = bookArticle.getBookDocument().getPmid().getValue();
            database.saveBookArticle(bookArticle);
            bookCount.incrementAndGet();
        }
    );

    System.out.printf("처리됨: %d개 논문, %d개 도서 논문%n",
                      articleCount.get(), bookCount.get());
}
```

---

## CommentsCorrections 타입

### 개요

CommentsCorrections는 논문 간의 관계(수정, 철회, 댓글, 업데이트 등)를 나타냅니다.

**DTD에 정의된 23가지 유형:**

---

### RefType 열거형

```java
public enum RefType {
    ASSOCIATED_DATASET,              // 관련 데이터셋
    ASSOCIATED_PUBLICATION,          // 관련 출판물

    COMMENT_IN,                      // 댓글 게시됨
    COMMENT_ON,                      // 이 논문에 대한 댓글

    CORRECTED_AND_REPUBLISHED_IN,    // 수정 버전 게시됨
    CORRECTED_AND_REPUBLISHED_FROM,  // 수정 대상

    ERRATUM_IN,                      // 정오표 게시됨
    ERRATUM_FOR,                     // 이 논문의 정오표

    EXPRESSION_OF_CONCERN_IN,        // 우려 표명 게시됨
    EXPRESSION_OF_CONCERN_FOR,       // 이 논문에 대한 우려

    REPUBLISHED_IN,                  // 재출판됨
    REPUBLISHED_FROM,                // 재출판 대상

    RETRACTED_AND_REPUBLISHED_IN,    // 철회 및 재출판됨
    RETRACTED_AND_REPUBLISHED_FROM,  // 철회 및 재출판 대상

    RETRACTIONIN,                    // 철회됨
    RETRACTION_OF,                   // 이 논문의 철회

    UPDATE_IN,                       // 업데이트됨
    UPDATE_OF,                       // 업데이트 대상

    SUMMARY_FOR_PATIENTS_IN,         // 환자용 요약 게시됨
    ORIGINAL_REPORT_IN,              // 원본 보고서 게시됨

    REPRINT_IN,                      // 재인쇄됨
    REPRINT_OF,                      // 재인쇄 대상

    CITES                            // 인용
}
```

---

### CommentsCorrections 파싱

```java
parser.parseStream(xmlFile, article -> {
    MedlineCitation citation = article.getMedlineCitation();
    List<CommentsCorrections> commentsList = citation.getCommentsCorrectionsList();

    if (commentsList != null && !commentsList.isEmpty()) {
        for (CommentsCorrections cc : commentsList) {
            RefType refType = cc.getRefType();
            String refSource = cc.getRefSource().getValue();
            PMID linkedPmid = cc.getPmid();

            System.out.printf("유형: %s, 출처: %s", refType, refSource);
            if (linkedPmid != null) {
                System.out.printf(", PMID: %s", linkedPmid.getValue());
            }
            System.out.println();
        }
    }
});
```

---

### 중요한 RefType 패턴

#### 1. 철회

```java
// 논문 철회 여부 확인
boolean isRetracted = commentsList.stream()
    .anyMatch(cc -> cc.getRefType() == RefType.RETRACTION_IN);

if (isRetracted) {
    // 철회 통지 찾기
    CommentsCorrections retraction = commentsList.stream()
        .filter(cc -> cc.getRefType() == RefType.RETRACTION_IN)
        .findFirst()
        .orElse(null);

    if (retraction != null && retraction.getPmid() != null) {
        String retractionPmid = retraction.getPmid().getValue();
        System.out.println("⚠️  철회됨 - PMID 참조: " + retractionPmid);
    }

    // 데이터베이스에 철회로 표시
    database.markAsRetracted(pmid);
}
```

---

#### 2. 수정/정오표

```java
// 수정 사항 확인
List<CommentsCorrections> corrections = commentsList.stream()
    .filter(cc -> cc.getRefType() == RefType.ERRATUM_IN ||
                  cc.getRefType() == RefType.CORRECTED_AND_REPUBLISHED_IN)
    .toList();

if (!corrections.isEmpty()) {
    System.out.println("📝 논문에 수정 사항 있음:");
    for (CommentsCorrections correction : corrections) {
        System.out.println("  - " + correction.getRefSource().getValue());
        if (correction.getPmid() != null) {
            System.out.println("    PMID: " + correction.getPmid().getValue());
        }
    }
}
```

---

#### 3. 업데이트

```java
// 업데이트 확인
boolean hasUpdate = commentsList.stream()
    .anyMatch(cc -> cc.getRefType() == RefType.UPDATE_IN);

if (hasUpdate) {
    CommentsCorrections update = commentsList.stream()
        .filter(cc -> cc.getRefType() == RefType.UPDATE_IN)
        .findFirst()
        .orElse(null);

    if (update != null && update.getPmid() != null) {
        String updatedPmid = update.getPmid().getValue();
        System.out.println("🔄 업데이트 버전 사용 가능: PMID " + updatedPmid);

        // 관계 저장
        database.recordUpdate(pmid, updatedPmid);
    }
}
```

---

### 프로덕션 패턴

```java
public class ArticleQualityChecker {

    public QualityStatus checkArticleQuality(PubmedArticle article) {
        MedlineCitation citation = article.getMedlineCitation();
        List<CommentsCorrections> ccList = citation.getCommentsCorrectionsList();

        if (ccList == null || ccList.isEmpty()) {
            return QualityStatus.NORMAL;
        }

        // 중요한 문제 확인
        for (CommentsCorrections cc : ccList) {
            RefType type = cc.getRefType();

            // 철회됨 - 최고 우선순위
            if (type == RefType.RETRACTION_IN ||
                type == RefType.RETRACTION_OF) {
                return QualityStatus.RETRACTED;
            }

            // 우려 표명
            if (type == RefType.EXPRESSION_OF_CONCERN_IN ||
                type == RefType.EXPRESSION_OF_CONCERN_FOR) {
                return QualityStatus.CONCERN;
            }

            // 수정 있음
            if (type == RefType.ERRATUM_FOR ||
                type == RefType.CORRECTED_AND_REPUBLISHED_FROM) {
                return QualityStatus.CORRECTED;
            }
        }

        return QualityStatus.NORMAL;
    }

    public enum QualityStatus {
        NORMAL,      // 문제 없음
        CORRECTED,   // 수정 사항 있음
        CONCERN,     // 우려 표명
        RETRACTED    // 철회된 논문
    }
}
```

---

## 특수 날짜 형식

### 날짜 요소 조합

PubMed는 유연한 날짜 표현을 사용합니다:

```java
// 완전한 날짜
<PubDate>
    <Year>2024</Year>
    <Month>Jan</Month>
    <Day>15</Day>
</PubDate>

// 연도와 월만
<PubDate>
    <Year>2024</Year>
    <Month>01</Month>
</PubDate>

// 연도만
<PubDate>
    <Year>2024</Year>
</PubDate>

// 비표준 날짜
<PubDate>
    <MedlineDate>2024 Spring</MedlineDate>
</PubDate>
```

---

### 월 형식 변형

월은 다음과 같은 형태일 수 있습니다:
- 숫자: `"01"`, `"1"`, `"12"`
- 짧은 이름: `"Jan"`, `"Feb"`, `"Mar"`
- 전체 이름: `"January"`, `"February"`

```java
private int parseMonth(String month) {
    if (month == null) return 1;

    // 먼저 숫자 시도
    try {
        int m = Integer.parseInt(month);
        return (m >= 1 && m <= 12) ? m : 1;
    } catch (NumberFormatException e) {
        // 숫자가 아니면 이름 시도
    }

    // 월 이름 매핑
    return switch (month.toLowerCase()) {
        case "jan", "january" -> 1;
        case "feb", "february" -> 2;
        case "mar", "march" -> 3;
        case "apr", "april" -> 4;
        case "may" -> 5;
        case "jun", "june" -> 6;
        case "jul", "july" -> 7;
        case "aug", "august" -> 8;
        case "sep", "september" -> 9;
        case "oct", "october" -> 10;
        case "nov", "november" -> 11;
        case "dec", "december" -> 12;
        default -> 1;
    };
}
```

---

### 부분 날짜 처리

```java
public LocalDate parsePubDate(PubDate pubDate) {
    // 우선순위 1: 구조화된 날짜
    if (pubDate.getYear() != null) {
        int year = Integer.parseInt(pubDate.getYear());

        // 월 있음?
        if (pubDate.getMonth() != null) {
            int month = parseMonth(pubDate.getMonth());

            // 일 있음?
            if (pubDate.getDay() != null) {
                int day = Integer.parseInt(pubDate.getDay());
                return LocalDate.of(year, month, day);
            }

            // 월만 있음 - 1일 사용
            return LocalDate.of(year, month, 1);
        }

        // 연도만 있음 - 1월 1일 사용
        return LocalDate.of(year, 1, 1);
    }

    // 우선순위 2: MedlineDate
    if (pubDate.getMedlineDate() != null) {
        return parseMedlineDate(pubDate.getMedlineDate().getValue());
    }

    // 날짜 정보 없음
    return null;
}
```

---

## PMID 예외 케이스

### PMID 형식

**표준 형식:**
- 숫자만: `12345678`
- 일반적으로 8자리, 하지만 변동 가능
- Version 속성: `<PMID Version="1">12345678</PMID>`

---

### 누락된 PMID 처리

```java
parser.parseStream(xmlFile, article -> {
    MedlineCitation citation = article.getMedlineCitation();

    // 방어적 확인
    if (citation == null || citation.getPmid() == null) {
        logger.warn("MedlineCitation 또는 PMID 누락 - 건너뜀");
        return;
    }

    PMID pmidObj = citation.getPmid();
    String pmid = pmidObj.getValue();

    if (pmid == null || pmid.trim().isEmpty()) {
        logger.warn("PMID 값이 null 또는 비어있음 - 건너뜀");
        return;
    }

    // PMID 형식 검증
    if (!pmid.matches("\\d+")) {
        logger.warn("잘못된 PMID 형식: {} - 건너뜀", pmid);
        return;
    }

    // 논문 처리
    database.save(article);
});
```

---

### PMID Version

```java
PMID pmidObj = citation.getPmid();
String pmid = pmidObj.getValue();        // "12345678"
String version = pmidObj.getVersion();    // "1"

// 대부분의 PMID는 Version="1"
// 대부분의 사용 사례에서 버전 추적은 거의 필요하지 않음
```

---

## 모범 사례

### 1. 항상 MedlineDate 확인

```java
// ✅ 좋음 - 두 날짜 유형 모두 확인
PubDate pubDate = article.getPubDate();
LocalDate date = null;

if (pubDate.getYear() != null) {
    date = parseStructuredDate(pubDate);
} else if (pubDate.getMedlineDate() != null) {
    date = parseMedlineDate(pubDate.getMedlineDate().getValue());
}

// ❌ 나쁨 - 구조화된 날짜가 있다고 가정
int year = Integer.parseInt(pubDate.getYear()); // NullPointerException
```

---

### 2. 업데이트 파일에서 DeleteCitation 처리

```java
// ✅ 좋음 - 삭제 항목 처리
PubmedArticleSet articleSet = parser.parseFile(updateFile);

// 업데이트 처리
for (PubmedArticle article : articleSet.getPubmedArticles()) {
    database.upsert(article);
}

// 삭제 처리
if (articleSet.getDeleteCitation() != null) {
    for (PMID pmid : articleSet.getDeleteCitation().getPmids()) {
        database.delete(pmid.getValue());
    }
}

// ❌ 나쁨 - 삭제 항목 무시
// 삭제된 논문이 데이터베이스에 남아있음
```

---

### 3. 논문 품질 상태 확인

```java
// ✅ 좋음 - 철회/수정 확인
List<CommentsCorrections> ccList = citation.getCommentsCorrectionsList();
boolean isRetracted = ccList != null && ccList.stream()
    .anyMatch(cc -> cc.getRefType() == RefType.RETRACTION_IN);

if (isRetracted) {
    // 철회로 표시, 분석에 사용하지 않음
    article.setRetracted(true);
}

// ❌ 나쁨 - 품질 문제 무시
// 철회된 논문을 유효한 것으로 처리
```

---

### 4. 두 논문 유형 모두 처리

```java
// ✅ 좋음 - 두 유형 모두 처리
parser.parseStreamAll(xmlFile,
    article -> processArticle(article),
    bookArticle -> processBookArticle(bookArticle)
);

// ❌ 나쁨 - PubmedArticle만 처리
parser.parseStream(xmlFile, article -> {
    // PubmedBookArticle은 무시됨
});
```

---

### 5. 방어적 PMID 검증

```java
// ✅ 좋음 - 사용 전 검증
String pmid = null;
if (citation != null &&
    citation.getPmid() != null &&
    citation.getPmid().getValue() != null) {
    pmid = citation.getPmid().getValue();
    if (pmid.matches("\\d+")) {
        database.save(article);
    }
}

// ❌ 나쁨 - PMID가 있다고 가정
String pmid = citation.getPmid().getValue(); // NullPointerException
```

---

## 다음 단계

- [USAGE.ko.md](USAGE.ko.md) - API 사용 가이드
- [STREAMING.ko.md](STREAMING.ko.md) - 대용량 파일 처리
- [VALIDATION.ko.md](VALIDATION.ko.md) - 검증 및 에러 처리
- [PMC-SPECIFICS.ko.md](PMC-SPECIFICS.ko.md) - PMC 특수 케이스

---

**문서 버전:** 1.0
**최종 업데이트:** 2026-01-12
**파서 버전:** 1.0.0-SNAPSHOT
