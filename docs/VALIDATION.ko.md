# 검증 및 에러 처리 가이드

**한국어** | [English](VALIDATION.md)

파일 무결성 검증, 데이터 검증 및 에러 처리를 위한 완전한 가이드입니다.

---

## 목차

- [파일 무결성 검증](#파일-무결성-검증)
- [데이터 검증](#데이터-검증)
- [에러 처리 패턴](#에러-처리-패턴)
- [일반적인 문제](#일반적인-문제)

---

## 파일 무결성 검증

### PubMed MD5 검증

PubMed FTP는 무결성 검증을 위한 `.md5` 체크섬 파일을 제공합니다.

#### 기본 MD5 검증

```java
import io.brillianttiger.bio.parser.common.util.Md5Verifier;

Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

// 자동 검증 (.md5 파일 찾기)
boolean isValid = Md5Verifier.verifyPubmedFile(xmlFile);

if (isValid) {
    System.out.println("✅ MD5 검증 통과");
} else {
    System.out.println("❌ MD5 검증 실패 - 파일이 손상되었을 수 있음");
}
```

#### 수동 MD5 검증

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
Path md5File = Paths.get("pubmed25n0001.xml.gz.md5");

// 별도 MD5 파일로 수동 검증
boolean isValid = Md5Verifier.verify(xmlFile, md5File);

if (!isValid) {
    throw new IOException("파일 무결성 검사 실패");
}
```

#### MD5 해시 계산

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

// MD5 해시 계산
String md5Hash = Md5Verifier.calculateMd5(xmlFile);
System.out.println("MD5: " + md5Hash);

// .md5 파일에서 해시 추출
String expectedMd5 = Md5Verifier.extractMd5FromFile(md5File);
System.out.println("기대값: " + expectedMd5);

// 비교
if (md5Hash.equals(expectedMd5)) {
    System.out.println("✅ 일치");
}
```

#### 운영 워크플로

```java
public void processPubmedFile(Path xmlFile) throws Exception {
    // 1. 파싱 전 무결성 검증
    System.out.println("파일 무결성 검증 중...");
    boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);

    if (!md5Valid) {
        throw new IOException("MD5 검증 실패: " + xmlFile);
    }

    System.out.println("✅ 무결성 검증됨");

    // 2. 파일 파싱
    System.out.println("파일 파싱 중...");
    PubmedXmlParser parser = new PubmedXmlParser();
    parser.parseStream(xmlFile, article -> {
        database.save(article);
    });

    System.out.println("✅ 처리 완료");
}
```

---

### PMC 무결성 검증

PMC는 MD5/SHA 체크섬을 제공하지 않습니다. 무결성은 tar.gz 압축 해제 성공 여부로 검증됩니다.

#### 자동 검증

```java
PmcXmlParser parser = new PmcXmlParser();
Path tarGzFile = Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

try {
    // parseTarGz()는 자동으로 무결성 검증
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
    System.out.println("✅ 아카이브 무결성 검증됨");

} catch (IOException e) {
    System.err.println("❌ 아카이브 손상됨: " + e.getMessage());
}
```

#### 수동 SHA-256 계산

```java
import java.security.MessageDigest;

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
```

---

## 데이터 검증

### PubMed 논문 검증

```java
private void validatePubmedArticle(PubmedArticle article) {
    // 1. 필수: MedlineCitation
    Objects.requireNonNull(article.getMedlineCitation(),
        "MedlineCitation이 필요합니다");

    // 2. 필수: PMID
    Objects.requireNonNull(article.getMedlineCitation().getPmid(),
        "PMID가 필요합니다");

    String pmid = article.getMedlineCitation().getPmid().getValue();
    Objects.requireNonNull(pmid, "PMID 값이 필요합니다");

    // 3. PMID 형식 (숫자만)
    if (!pmid.matches("\\d+")) {
        throw new IllegalArgumentException("유효하지 않은 PMID 형식: " + pmid);
    }

    // 4. Article 데이터
    Article articleData = article.getMedlineCitation().getArticle();
    if (articleData != null) {
        // Title은 필수
        Objects.requireNonNull(articleData.getArticleTitle(),
            "논문 제목이 필요합니다");
    }
}

// 스트리밍과 함께 사용
parser.parseStream(xmlFile, article -> {
    try {
        validatePubmedArticle(article);
        database.save(article);
    } catch (Exception e) {
        logger.warn("PMID {} 검증 실패: {}",
            article.getMedlineCitation().getPmid().getValue(),
            e.getMessage());
    }
});
```

---

### PMC 논문 검증

```java
PmcXmlParser parser = new PmcXmlParser();
Path xmlFile = Paths.get("PMC1234567.xml");

// 파싱 및 검증
PmcXmlParser.ValidationResult result = parser.parseAndValidate(xmlFile);

JatsArticle article = result.getArticle();
List<ValidationError> errors = result.getErrors();

// 검증 상태 확인
if (result.isValid()) {
    System.out.println("✅ 논문이 유효합니다");
    database.save(article);

} else if (result.hasErrors()) {
    System.out.println("❌ 검증 오류:");
    result.printErrors();

} else if (result.hasWarnings()) {
    System.out.println("⚠️  경고:");
    result.printErrors();
    database.save(article); // 경고와 함께 저장
}

// 요약 정보
System.out.println(result.getSummary());
```

#### 수동 검증

```java
JatsArticle article = parser.parseFile(xmlFile);

// 수동 검증
List<ValidationError> errors = parser.validateArticle(article);

if (errors.isEmpty()) {
    System.out.println("✅ 유효함");
} else {
    for (ValidationError error : errors) {
        System.out.printf("[%s] %s%n",
            error.getSeverity(),
            error.getMessage());
    }
}
```

#### 검증 오류 처리

```java
for (ValidationError error : errors) {
    switch (error.getSeverity()) {
        case ERROR:
            // 중요 오류 - 저장하지 않음
            logger.error("검증 오류: {}", error.getMessage());
            break;

        case WARNING:
            // 경고 - 주의해서 저장
            logger.warn("검증 경고: {}", error.getMessage());
            database.save(article);
            break;

        case INFO:
            // 정보만
            logger.info("검증 정보: {}", error.getMessage());
            break;
    }
}
```

---

## 에러 처리 패턴

### 패턴 1: 즉시 실패

첫 번째 오류에서 중단 (중요한 작업에 적합).

```java
try {
    // 무결성 검증
    boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);
    if (!md5Valid) {
        throw new IOException("MD5 검증 실패");
    }

    // 파일 파싱
    PubmedArticleSet articleSet = parser.parseFile(xmlFile);

    // 모든 논문 검증
    for (PubmedArticle article : articleSet.getPubmedArticles()) {
        validatePubmedArticle(article);
        database.save(article);
    }

    System.out.println("✅ 모든 논문이 성공적으로 처리됨");

} catch (Exception e) {
    logger.error("처리 실패: {}", e.getMessage());
    throw e; // 재발생하여 중단
}
```

---

### 패턴 2: 에러 발생 시 계속

오류에도 불구하고 처리 계속 (배치 작업에 적합).

```java
AtomicInteger successCount = new AtomicInteger(0);
AtomicInteger errorCount = new AtomicInteger(0);
List<String> errorLog = new CopyOnWriteArrayList<>();

parser.parseStream(xmlFile, article -> {
    try {
        // 검증
        validatePubmedArticle(article);

        // 저장
        database.save(article);

        successCount.incrementAndGet();

    } catch (Exception e) {
        errorCount.incrementAndGet();
        String pmid = article.getMedlineCitation().getPmid().getValue();
        String errorMsg = pmid + ": " + e.getMessage();
        errorLog.add(errorMsg);
        logger.warn("논문 처리 실패: {}", errorMsg);
        // 다음 논문 계속
    }
});

// 요약
System.out.printf("성공: %d, 오류: %d%n",
    successCount.get(), errorCount.get());

// 오류 로그 작성
if (!errorLog.isEmpty()) {
    Files.write(Paths.get("errors.log"), errorLog);
}
```

---

### 패턴 3: 재시도

지수 백오프로 실패한 작업 재시도.

```java
private void saveWithRetry(PubmedArticle article, int maxRetries) {
    int retries = 0;
    Exception lastException = null;

    while (retries < maxRetries) {
        try {
            database.save(article);
            return; // 성공

        } catch (Exception e) {
            lastException = e;
            retries++;

            if (retries < maxRetries) {
                long backoff = (long) Math.pow(2, retries) * 1000; // 지수 백오프
                logger.warn("저장 실패, {}ms 후 재시도 (시도 {}/{})",
                    backoff, retries, maxRetries);

                Thread.sleep(backoff);
            }
        }
    }

    // 모든 재시도 실패
    throw new RuntimeException("재시도 " + maxRetries + "번 후 실패", lastException);
}

// 사용법
parser.parseStream(xmlFile, article -> {
    saveWithRetry(article, 3);
});
```

---

### 패턴 4: 데드 레터 큐

나중에 재처리하기 위해 실패한 항목 저장.

```java
Queue<PubmedArticle> deadLetterQueue = new ConcurrentLinkedQueue<>();
AtomicInteger failureCount = new AtomicInteger(0);

parser.parseStream(xmlFile, article -> {
    try {
        validatePubmedArticle(article);
        database.save(article);

    } catch (Exception e) {
        failureCount.incrementAndGet();
        deadLetterQueue.offer(article);
        logger.warn("재시도를 위해 논문 대기열 추가: {}",
            article.getMedlineCitation().getPmid().getValue());
    }
});

// 데드 레터 큐 처리
System.out.println("실패한 논문 " + deadLetterQueue.size() + "개 재시도 중...");

while (!deadLetterQueue.isEmpty()) {
    PubmedArticle article = deadLetterQueue.poll();
    try {
        database.save(article);
    } catch (Exception e) {
        logger.error("PMID {} 재시도 실패: {}",
            article.getMedlineCitation().getPmid().getValue(),
            e.getMessage());
    }
}
```

---

### 패턴 5: 서킷 브레이커

오류율이 임계값을 초과하면 처리 중지.

```java
AtomicInteger totalCount = new AtomicInteger(0);
AtomicInteger errorCount = new AtomicInteger(0);
double errorThreshold = 0.05; // 5% 오류율

parser.parseStream(xmlFile, article -> {
    int total = totalCount.incrementAndGet();

    try {
        database.save(article);

    } catch (Exception e) {
        int errors = errorCount.incrementAndGet();
        double errorRate = (double) errors / total;

        logger.warn("논문 처리 오류: {}", e.getMessage());

        // 서킷 브레이커: 오류율 > 임계값이면 중지
        if (errorRate > errorThreshold && total > 100) {
            throw new RuntimeException(String.format(
                "오류율 %.2f%%가 임계값 %.2f%%를 초과 - 중단",
                errorRate * 100, errorThreshold * 100));
        }
    }
});
```

---

## 일반적인 문제

### 문제 1: MD5 파일을 찾을 수 없음

**오류:**
```
FileNotFoundException: MD5 파일을 찾을 수 없음: pubmed25n0001.xml.gz.md5
```

**해결책:**
```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
Path md5File = Paths.get("pubmed25n0001.xml.gz.md5");

// MD5 파일이 존재하는지 확인
if (!Files.exists(md5File)) {
    // MD5 파일 다운로드
    downloadMd5File(xmlFile, md5File);
}

// 그 다음 검증
boolean isValid = Md5Verifier.verifyPubmedFile(xmlFile);
```

---

### 문제 2: MD5 불일치

**오류:**
```
MD5 검증 실패: 기대값 d41d8cd98f00b204e9800998ecf8427e, 실제 ...
```

**원인:**
1. 다운로드 중 파일 손상
2. 다운로드 후 파일 수정
3. 잘못된 MD5 파일

**해결책:**
```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

// 파일 재다운로드
System.out.println("MD5 불일치 - 파일 재다운로드 중...");
downloadFile(xmlFile);

// 다시 검증
boolean isValid = Md5Verifier.verifyPubmedFile(xmlFile);

if (!isValid) {
    throw new IOException("재다운로드 후에도 파일이 여전히 손상됨");
}
```

---

### 문제 3: 유효하지 않은 PMID

**오류:**
```
NullPointerException: PMID가 null입니다
```

**해결책:**
```java
parser.parseStream(xmlFile, article -> {
    // 방어적 확인
    if (article.getMedlineCitation() == null ||
        article.getMedlineCitation().getPmid() == null ||
        article.getMedlineCitation().getPmid().getValue() == null) {

        logger.warn("PMID가 누락된 논문 건너뜀");
        return; // 이 논문 건너뜀
    }

    String pmid = article.getMedlineCitation().getPmid().getValue();
    database.save(article);
});
```

---

### 문제 4: 손상된 TAR 아카이브

**오류:**
```
IOException: tar.gz 파일이 손상되었거나 유효하지 않습니다
```

**해결책:**
```java
Path tarGzFile = Paths.get("pmc_package.tar.gz");

try {
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

} catch (IOException e) {
    logger.error("아카이브 손상됨: {}", e.getMessage());

    // 아카이브 재다운로드
    System.out.println("아카이브 재다운로드 중...");
    downloadArchive(tarGzFile);

    // 재시도
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
}
```

---

### 문제 5: XML 파싱 오류

**오류:**
```
XMLStreamException: 예상치 못한 파일 끝
```

**원인:**
1. 불완전한 파일 다운로드
2. 손상된 GZip 파일
3. 유효하지 않은 XML 구조

**해결책:**
```java
// 1. 먼저 파일 무결성 검증
boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);
if (!md5Valid) {
    throw new IOException("파일 손상됨 - MD5 확인 실패");
}

// 2. 에러 복구로 파싱 시도
try {
    parser.parseStream(xmlFile, article -> {
        database.save(article);
    });
} catch (XMLStreamException e) {
    logger.error("XML 파싱 오류: {}", e.getMessage());

    // 옵션 A: 파일 재다운로드
    downloadFile(xmlFile);

    // 옵션 B: 이 파일 건너뛰고 다음 계속
    logger.warn("손상된 파일 건너뜀: {}", xmlFile);
}
```

---

## 다음 단계

- [USAGE.ko.md](USAGE.ko.md) - API 사용 가이드
- [STREAMING.ko.md](STREAMING.ko.md) - 대용량 파일 처리
- [PUBMED-SPECIFICS.ko.md](PUBMED-SPECIFICS.ko.md) - PubMed 특수 케이스
- [PMC-SPECIFICS.ko.md](PMC-SPECIFICS.ko.md) - PMC 특수 케이스

---

**문서 버전:** 1.0
**최종 업데이트:** 2026-01-12
**파서 버전:** 1.0.0-SNAPSHOT
