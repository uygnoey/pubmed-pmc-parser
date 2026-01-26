# API 사용 가이드

**한국어** | [English](USAGE.md)

PubMed & PMC XML Parser API의 완전한 사용 가이드입니다.

---

## 목차

- [빠른 시작](#빠른-시작)
- [PubMed 파서](#pubmed-파서)
- [PMC 파서](#pmc-파서)
- [일반적인 패턴](#일반적인-패턴)
- [에러 처리](#에러-처리)

---

## 빠른 시작

### 설치

```gradle
dependencies {
    implementation 'io.brillianttiger.bio:pubmed-pmc-parser:1.0.0-SNAPSHOT'
}
```

### 기본 사용법

```java
// PubMed 파서
PubmedXmlParser pubmedParser = new PubmedXmlParser();
PubmedArticleSet articleSet = pubmedParser.parseFile(Paths.get("pubmed25n0001.xml.gz"));

// PMC 파서
PmcXmlParser pmcParser = new PmcXmlParser();
JatsArticle article = pmcParser.parseFile(Paths.get("PMC1234567.xml"));
```

---

## PubMed 파서

### PubmedXmlParser

PubMed XML 파일(DTD pubmed_250101)을 파싱하는 메인 클래스입니다.

#### 생성자

```java
PubmedXmlParser parser = new PubmedXmlParser();
```

**주요 기능:**
- 자동 XXE 공격 방지
- 자동 GZip 파일 처리
- StAX 기반 스트리밍 파서

---

### 파싱 메서드

#### 1. parseFile() - 전체 파일 파싱

전체 XML 파일을 파싱하여 `PubmedArticleSet`을 반환합니다.

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
PubmedArticleSet articleSet = parser.parseFile(xmlFile);

// 논문 접근
List<PubmedArticle> articles = articleSet.getPubmedArticles();
for (PubmedArticle article : articles) {
    String pmid = article.getMedlineCitation().getPmid().getValue();
    String title = article.getMedlineCitation().getArticle().getArticleTitle().getValue();
    System.out.println(pmid + ": " + title);
}
```

**사용 시점:**
- 중소형 파일 (<100MB)
- 전체 파일 구조 필요 (DeleteCitation 포함)
- 모든 논문을 한번에 처리할 때

---

#### 2. parseStream() - 스트리밍 파싱

콜백 처리 방식의 메모리 효율적인 스트리밍 파싱입니다.

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

long count = parser.parseStream(xmlFile, article -> {
    // 각 논문 처리
    String pmid = article.getMedlineCitation().getPmid().getValue();

    // 데이터베이스 저장
    database.save(article);

    // 진행 상황 로그
    if (count % 1000 == 0) {
        System.out.println("Processed: " + count);
    }
});

System.out.println("Total articles: " + count);
```

**사용 시점:**
- 대용량 파일 (>100MB)
- 상수 메모리 사용 O(1) 필요
- 실시간 처리 필요

**성능:**
- PubMed: 20,000+ 논문/초
- 메모리: 상수 O(1)

---

#### 3. parseStreamBatch() - 배치 스트리밍

데이터베이스 배치 삽입을 위해 논문을 배치 단위로 처리합니다.

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
int batchSize = 100;

long count = parser.parseStreamBatch(xmlFile, batchSize, batch -> {
    // 배치 논문 처리
    System.out.println("Processing batch of " + batch.size() + " articles");

    // 데이터베이스 배치 삽입
    database.batchInsert(batch);
});

System.out.println("Total articles: " + count);
```

**사용 시점:**
- 데이터베이스 배치 삽입
- 네트워크 배치 작업
- 최적화된 처리량 필요

**배치 크기 권장사항:**
- 데이터베이스: 100-500
- 네트워크: 50-100
- 메모리 제약: 10-50

---

#### 4. parseStreamAll() - 다중 타입 스트리밍

`PubmedArticle`과 `PubmedBookArticle` 모두 처리합니다.

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

long count = parser.parseStreamAll(xmlFile,
    article -> {
        // PubmedArticle 처리
        System.out.println("Article: " + article.getMedlineCitation().getPmid().getValue());
    },
    bookArticle -> {
        // PubmedBookArticle 처리
        System.out.println("Book: " + bookArticle.getBookDocument().getPmid().getValue());
    }
);
```

**사용 시점:**
- 두 가지 논문 타입 모두 처리 필요
- 각 타입에 대해 다른 처리 로직 필요

---

#### 5. extractDeleteCitation() - 삭제된 PMID 추출

Update 파일에서 삭제된 PMID 목록을 추출합니다.

```java
Path updateFile = Paths.get("pubmed25n1275.xml.gz");

DeleteCitation deleteCitation = parser.extractDeleteCitation(updateFile);

if (deleteCitation != null) {
    List<PMID> deletedPmids = deleteCitation.getPmids();
    System.out.println("Deleted PMIDs: " + deletedPmids.size());

    // 데이터베이스에서 삭제
    for (PMID pmid : deletedPmids) {
        database.delete(pmid.getValue());
    }
}
```

**사용 시점:**
- PubMed update 파일 처리
- 데이터베이스 일관성 유지
- FTP update 파일 처리

---

## PMC 파서

### PmcXmlParser

PMC XML 파일(JATS 1.4 표준)을 파싱하는 메인 클래스입니다.

#### 생성자

```java
PmcXmlParser parser = new PmcXmlParser();
```

**주요 기능:**
- JATS 1.4 완전 지원
- 네임스페이스 처리 (xlink, mml)
- 재귀 구조 지원
- tar.gz 패키지 파싱

---

### 파싱 메서드

#### 1. parseFile() - 단일 파일 파싱

단일 PMC XML 파일을 파싱합니다.

```java
Path xmlFile = Paths.get("PMC1234567.xml");
JatsArticle article = parser.parseFile(xmlFile);

// 논문 메타데이터 접근
if (article.getFront() != null) {
    ArticleMeta meta = article.getFront().getArticleMeta();

    // Article ID들
    if (meta.getArticleIds() != null) {
        for (PmcArticleId id : meta.getArticleIds()) {
            System.out.println(id.getPubIdType() + ": " + id.getValue());
        }
    }

    // 제목
    if (meta.getTitleGroup() != null) {
        String title = meta.getTitleGroup().getArticleTitle().getContent();
        System.out.println("Title: " + title);
    }
}

// 전문 접근
if (article.getBody() != null) {
    List<Sec> sections = article.getBody().getSections();
    for (Sec section : sections) {
        System.out.println("Section: " + section.getTitle().getContent());
    }
}
```

**사용 시점:**
- 단일 XML 파일 처리
- 전체 구조 접근 필요
- 중소형 파일

---

#### 2. parseStream() - 스트리밍 파싱

대용량 파일을 위한 메모리 효율적 스트리밍입니다.

```java
Path xmlFile = Paths.get("large_article.xml");

long count = parser.parseStream(xmlFile, article -> {
    // 각 논문 처리
    ArticleMeta meta = article.getFront().getArticleMeta();
    String pmcId = meta.getArticleIds().get(0).getValue();

    // 데이터베이스 저장
    database.save(article);
});
```

**성능:**
- PMC: 1,600+ 논문/초
- 메모리: 상수 O(1)

---

#### 3. parseStreamBatch() - 배치 처리

데이터베이스 최적화를 위한 배치 처리입니다.

```java
Path xmlFile = Paths.get("pmc_articles.xml");
int batchSize = 50;

long count = parser.parseStreamBatch(xmlFile, batchSize, batch -> {
    // 배치 삽입
    database.batchInsert(batch);
});
```

---

#### 4. parseTarGz() - 아카이브 파싱

FTP에서 받은 PMC tar.gz 패키지를 파싱합니다.

```java
Path tarGzFile = Paths.get("oa_comm/pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

// 아카이브 내 모든 논문 파싱
List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

System.out.println("Parsed " + articles.size() + " articles from archive");

// 논문 처리
for (JatsArticle article : articles) {
    ArticleMeta meta = article.getFront().getArticleMeta();
    // 논문 처리...
}
```

**사용 시점:**
- PMC FTP 대량 다운로드 처리
- 아카이브 파일 처리
- tar.gz에서 배치 처리

**주요 기능:**
- 자동 GZip 압축 해제
- Tar 아카이브 추출
- 무결성 검증 (압축 해제 성공 여부)
- 아카이브 엔트리에서 직접 XML 파싱

**참고:** PMC는 MD5 체크섬을 제공하지 않으므로, 압축 해제 성공 여부로 무결성을 검증합니다.

---

#### 5. validateArticle() - 논문 검증

JATS 1.4 표준에 따라 파싱된 논문을 검증합니다.

```java
JatsArticle article = parser.parseFile(xmlFile);

// 논문 검증
List<ValidationError> errors = parser.validateArticle(article);

if (errors.isEmpty()) {
    System.out.println("✅ 논문이 유효합니다");
} else {
    System.out.println("⚠️  검증 오류: " + errors.size());
    for (ValidationError error : errors) {
        System.out.println("  - " + error.getMessage());
    }
}
```

**검증 항목:**
- 필수 요소 (front, article-meta, title-group)
- ID 형식 검증
- 참조 무결성
- JATS 1.4 준수

---

#### 6. parseAndValidate() - 파싱 및 검증

파싱과 검증을 한번에 수행합니다.

```java
PmcXmlParser.ValidationResult result = parser.parseAndValidate(xmlFile);

JatsArticle article = result.getArticle();
List<ValidationError> errors = result.getErrors();

if (result.isValid()) {
    System.out.println("✅ 유효한 논문");
} else if (result.hasErrors()) {
    System.out.println("❌ 오류 있음");
    result.printErrors();
} else if (result.hasWarnings()) {
    System.out.println("⚠️  경고 있음");
}

// 요약 정보
System.out.println(result.getSummary());
```

---

## 일반적인 패턴

### 패턴 1: 데이터베이스 임포트

```java
PubmedXmlParser parser = new PubmedXmlParser();
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
int batchSize = 100;

parser.parseStreamBatch(xmlFile, batchSize, batch -> {
    try {
        // 트랜잭션 시작
        database.beginTransaction();

        // 배치 삽입
        database.batchInsert(batch);

        // 커밋
        database.commit();

    } catch (Exception e) {
        database.rollback();
        throw e;
    }
});
```

---

### 패턴 2: 진행 상황 모니터링

```java
AtomicLong count = new AtomicLong(0);
AtomicLong startTime = new AtomicLong(System.currentTimeMillis());

parser.parseStream(xmlFile, article -> {
    long num = count.incrementAndGet();

    // 1000개마다 진행 상황 출력
    if (num % 1000 == 0) {
        long elapsed = System.currentTimeMillis() - startTime.get();
        double throughput = num * 1000.0 / elapsed;
        System.out.printf("진행: %,d 논문 (%.0f개/초)%n", num, throughput);
    }

    // 논문 처리
    database.save(article);
});
```

---

### 패턴 3: 에러 복구

```java
AtomicInteger errorCount = new AtomicInteger(0);
List<String> errorLog = new CopyOnWriteArrayList<>();

parser.parseStream(xmlFile, article -> {
    try {
        // 검증
        validateArticle(article);

        // 처리
        database.save(article);

    } catch (Exception e) {
        errorCount.incrementAndGet();
        String pmid = article.getMedlineCitation().getPmid().getValue();
        errorLog.add(pmid + ": " + e.getMessage());

        // 계속 처리 (throw 하지 않음)
    }
});

System.out.println("오류: " + errorCount.get());
errorLog.forEach(System.err::println);
```

---

### 패턴 4: 필터링 처리

```java
// 초록이 있는 논문만 처리
parser.parseStream(xmlFile, article -> {
    Article articleData = article.getMedlineCitation().getArticle();

    if (articleData.getAbstract() != null) {
        // 초록이 있는 논문만 처리
        database.save(article);
    }
});
```

---

### 패턴 5: 다중 포맷 내보내기

```java
parser.parseStream(xmlFile, article -> {
    String pmid = article.getMedlineCitation().getPmid().getValue();

    // JSON으로 내보내기
    jsonExporter.export(article, "json/" + pmid + ".json");

    // 데이터베이스에 저장
    database.save(article);

    // 검색 인덱스에 추가
    searchIndex.index(article);
});
```

---

## 에러 처리

### 일반적인 예외

```java
try {
    PubmedArticleSet articleSet = parser.parseFile(xmlFile);

} catch (FileNotFoundException e) {
    System.err.println("파일을 찾을 수 없음: " + xmlFile);

} catch (XMLStreamException e) {
    System.err.println("XML 파싱 오류: " + e.getMessage());

} catch (IOException e) {
    System.err.println("I/O 오류: " + e.getMessage());

} catch (Exception e) {
    System.err.println("예상치 못한 오류: " + e.getMessage());
}
```

---

### 검증 오류

```java
PmcXmlParser.ValidationResult result = parser.parseAndValidate(xmlFile);

if (!result.isValid()) {
    for (ValidationError error : result.getErrors()) {
        switch (error.getSeverity()) {
            case ERROR:
                System.err.println("오류: " + error.getMessage());
                break;
            case WARNING:
                System.out.println("경고: " + error.getMessage());
                break;
            case INFO:
                System.out.println("정보: " + error.getMessage());
                break;
        }
    }
}
```

---

## 모범 사례

### 1. 대용량 파일에는 항상 스트리밍 사용

```java
// ✅ 좋음 - 스트리밍
parser.parseStream(largeFile, article -> {
    database.save(article);
});

// ❌ 나쁨 - 전체 파일 로드
PubmedArticleSet articleSet = parser.parseFile(largeFile); // OutOfMemoryError
```

---

### 2. 데이터베이스에는 배치 처리 사용

```java
// ✅ 좋음 - 배치 삽입
parser.parseStreamBatch(xmlFile, 100, batch -> {
    database.batchInsert(batch);
});

// ❌ 나쁨 - 개별 삽입
parser.parseStream(xmlFile, article -> {
    database.insert(article); // 너무 느림
});
```

---

### 3. 에러를 적절히 처리

```java
// ✅ 좋음 - 에러 발생 시 계속 진행
parser.parseStream(xmlFile, article -> {
    try {
        database.save(article);
    } catch (Exception e) {
        logger.error("논문 저장 실패: " + e.getMessage());
        // 계속 처리
    }
});

// ❌ 나쁨 - 첫 에러에서 중단
parser.parseStream(xmlFile, article -> {
    database.save(article); // 예외 발생 시 처리 중단
});
```

---

### 4. 중요 데이터 검증

```java
// ✅ 좋음 - 저장 전 검증
parser.parseStream(xmlFile, article -> {
    if (article.getMedlineCitation() != null &&
        article.getMedlineCitation().getPmid() != null) {
        database.save(article);
    } else {
        logger.warn("유효하지 않은 논문: PMID 누락");
    }
});
```

---

### 5. 진행 상황 모니터링

```java
// ✅ 좋음 - 진행 상황 모니터링
AtomicLong count = new AtomicLong(0);
parser.parseStream(xmlFile, article -> {
    long num = count.incrementAndGet();
    if (num % 1000 == 0) {
        System.out.println("처리됨: " + num);
    }
    database.save(article);
});
```

---

## 다음 단계

- [STREAMING.ko.md](STREAMING.ko.md) - 대용량 파일 처리 가이드
- [VALIDATION.ko.md](VALIDATION.ko.md) - 검증 및 에러 처리
- [PUBMED-SPECIFICS.ko.md](PUBMED-SPECIFICS.ko.md) - PubMed 특수 케이스
- [PMC-SPECIFICS.ko.md](PMC-SPECIFICS.ko.md) - PMC 특수 케이스

---

**문서 버전:** 1.0
**최종 업데이트:** 2026-01-12
**파서 버전:** 1.0.0-SNAPSHOT
