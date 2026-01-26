# Validation & Error Handling Guide

[**한국어**](VALIDATION.ko.md) | **English**

Complete guide for file integrity verification, data validation, and error handling.

---

## Table of Contents

- [File Integrity Verification](#file-integrity-verification)
- [Data Validation](#data-validation)
- [Error Handling Patterns](#error-handling-patterns)
- [Common Issues](#common-issues)

---

## File Integrity Verification

### PubMed MD5 Verification

PubMed FTP provides `.md5` checksum files for integrity verification.

#### Basic MD5 Verification

```java
import io.brillianttiger.bio.parser.common.util.Md5Verifier;

Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

// Automatic verification (looks for .md5 file)
boolean isValid = Md5Verifier.verifyPubmedFile(xmlFile);

if (isValid) {
    System.out.println("✅ MD5 verification passed");
} else {
    System.out.println("❌ MD5 verification failed - file may be corrupted");
}
```

#### Manual MD5 Verification

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
Path md5File = Paths.get("pubmed25n0001.xml.gz.md5");

// Manual verification with separate MD5 file
boolean isValid = Md5Verifier.verify(xmlFile, md5File);

if (!isValid) {
    throw new IOException("File integrity check failed");
}
```

#### Calculate MD5 Hash

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

// Calculate MD5 hash
String md5Hash = Md5Verifier.calculateMd5(xmlFile);
System.out.println("MD5: " + md5Hash);

// Extract hash from .md5 file
String expectedMd5 = Md5Verifier.extractMd5FromFile(md5File);
System.out.println("Expected: " + expectedMd5);

// Compare
if (md5Hash.equals(expectedMd5)) {
    System.out.println("✅ Match");
}
```

#### Production Workflow

```java
public void processPubmedFile(Path xmlFile) throws Exception {
    // 1. Verify integrity before parsing
    System.out.println("Verifying file integrity...");
    boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);

    if (!md5Valid) {
        throw new IOException("MD5 verification failed: " + xmlFile);
    }

    System.out.println("✅ Integrity verified");

    // 2. Parse file
    System.out.println("Parsing file...");
    PubmedXmlParser parser = new PubmedXmlParser();
    parser.parseStream(xmlFile, article -> {
        database.save(article);
    });

    System.out.println("✅ Processing complete");
}
```

---

### PMC Integrity Verification

PMC doesn't provide MD5/SHA checksums. Integrity is validated through successful tar.gz decompression.

#### Automatic Validation

```java
PmcXmlParser parser = new PmcXmlParser();
Path tarGzFile = Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

try {
    // parseTarGz() automatically validates integrity
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
    System.out.println("✅ Archive integrity validated");

} catch (IOException e) {
    System.err.println("❌ Archive corrupted: " + e.getMessage());
}
```

#### Manual SHA-256 Calculation

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

// Usage
Path tarGzFile = Paths.get("pmc_package.tar.gz");
String sha256 = calculateSha256(tarGzFile);
System.out.println("SHA-256: " + sha256);
```

---

## Data Validation

### PubMed Article Validation

```java
private void validatePubmedArticle(PubmedArticle article) {
    // 1. Required: MedlineCitation
    Objects.requireNonNull(article.getMedlineCitation(),
        "MedlineCitation is required");

    // 2. Required: PMID
    Objects.requireNonNull(article.getMedlineCitation().getPmid(),
        "PMID is required");

    String pmid = article.getMedlineCitation().getPmid().getValue();
    Objects.requireNonNull(pmid, "PMID value is required");

    // 3. PMID format (digits only)
    if (!pmid.matches("\\d+")) {
        throw new IllegalArgumentException("Invalid PMID format: " + pmid);
    }

    // 4. Article data
    Article articleData = article.getMedlineCitation().getArticle();
    if (articleData != null) {
        // Title is required
        Objects.requireNonNull(articleData.getArticleTitle(),
            "Article title is required");
    }
}

// Usage with streaming
parser.parseStream(xmlFile, article -> {
    try {
        validatePubmedArticle(article);
        database.save(article);
    } catch (Exception e) {
        logger.warn("Validation failed for PMID {}: {}",
            article.getMedlineCitation().getPmid().getValue(),
            e.getMessage());
    }
});
```

---

### PMC Article Validation

```java
PmcXmlParser parser = new PmcXmlParser();
Path xmlFile = Paths.get("PMC1234567.xml");

// Parse and validate
PmcXmlParser.ValidationResult result = parser.parseAndValidate(xmlFile);

JatsArticle article = result.getArticle();
List<ValidationError> errors = result.getErrors();

// Check validation status
if (result.isValid()) {
    System.out.println("✅ Article is valid");
    database.save(article);

} else if (result.hasErrors()) {
    System.out.println("❌ Validation errors:");
    result.printErrors();

} else if (result.hasWarnings()) {
    System.out.println("⚠️  Warnings:");
    result.printErrors();
    database.save(article); // Save with warnings
}

// Get summary
System.out.println(result.getSummary());
```

#### Manual Validation

```java
JatsArticle article = parser.parseFile(xmlFile);

// Validate manually
List<ValidationError> errors = parser.validateArticle(article);

if (errors.isEmpty()) {
    System.out.println("✅ Valid");
} else {
    for (ValidationError error : errors) {
        System.out.printf("[%s] %s%n",
            error.getSeverity(),
            error.getMessage());
    }
}
```

#### Validation Error Handling

```java
for (ValidationError error : errors) {
    switch (error.getSeverity()) {
        case ERROR:
            // Critical error - don't save
            logger.error("Validation error: {}", error.getMessage());
            break;

        case WARNING:
            // Warning - save with caution
            logger.warn("Validation warning: {}", error.getMessage());
            database.save(article);
            break;

        case INFO:
            // Information only
            logger.info("Validation info: {}", error.getMessage());
            break;
    }
}
```

---

## Error Handling Patterns

### Pattern 1: Fail Fast

Stop on first error (suitable for critical operations).

```java
try {
    // Verify integrity
    boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);
    if (!md5Valid) {
        throw new IOException("MD5 verification failed");
    }

    // Parse file
    PubmedArticleSet articleSet = parser.parseFile(xmlFile);

    // Validate all articles
    for (PubmedArticle article : articleSet.getPubmedArticles()) {
        validatePubmedArticle(article);
        database.save(article);
    }

    System.out.println("✅ All articles processed successfully");

} catch (Exception e) {
    logger.error("Processing failed: {}", e.getMessage());
    throw e; // Re-throw to abort
}
```

---

### Pattern 2: Continue on Error

Continue processing despite errors (suitable for batch operations).

```java
AtomicInteger successCount = new AtomicInteger(0);
AtomicInteger errorCount = new AtomicInteger(0);
List<String> errorLog = new CopyOnWriteArrayList<>();

parser.parseStream(xmlFile, article -> {
    try {
        // Validate
        validatePubmedArticle(article);

        // Save
        database.save(article);

        successCount.incrementAndGet();

    } catch (Exception e) {
        errorCount.incrementAndGet();
        String pmid = article.getMedlineCitation().getPmid().getValue();
        String errorMsg = pmid + ": " + e.getMessage();
        errorLog.add(errorMsg);
        logger.warn("Failed to process article: {}", errorMsg);
        // Continue to next article
    }
});

// Summary
System.out.printf("Success: %d, Errors: %d%n",
    successCount.get(), errorCount.get());

// Write error log
if (!errorLog.isEmpty()) {
    Files.write(Paths.get("errors.log"), errorLog);
}
```

---

### Pattern 3: Retry on Error

Retry failed operations with exponential backoff.

```java
private void saveWithRetry(PubmedArticle article, int maxRetries) {
    int retries = 0;
    Exception lastException = null;

    while (retries < maxRetries) {
        try {
            database.save(article);
            return; // Success

        } catch (Exception e) {
            lastException = e;
            retries++;

            if (retries < maxRetries) {
                long backoff = (long) Math.pow(2, retries) * 1000; // Exponential backoff
                logger.warn("Save failed, retrying in {}ms (attempt {}/{})",
                    backoff, retries, maxRetries);

                Thread.sleep(backoff);
            }
        }
    }

    // All retries failed
    throw new RuntimeException("Failed after " + maxRetries + " retries", lastException);
}

// Usage
parser.parseStream(xmlFile, article -> {
    saveWithRetry(article, 3);
});
```

---

### Pattern 4: Dead Letter Queue

Save failed items for later reprocessing.

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
        logger.warn("Article queued for retry: {}",
            article.getMedlineCitation().getPmid().getValue());
    }
});

// Process dead letter queue
System.out.println("Retrying " + deadLetterQueue.size() + " failed articles...");

while (!deadLetterQueue.isEmpty()) {
    PubmedArticle article = deadLetterQueue.poll();
    try {
        database.save(article);
    } catch (Exception e) {
        logger.error("Retry failed for PMID {}: {}",
            article.getMedlineCitation().getPmid().getValue(),
            e.getMessage());
    }
}
```

---

### Pattern 5: Circuit Breaker

Stop processing if error rate exceeds threshold.

```java
AtomicInteger totalCount = new AtomicInteger(0);
AtomicInteger errorCount = new AtomicInteger(0);
double errorThreshold = 0.05; // 5% error rate

parser.parseStream(xmlFile, article -> {
    int total = totalCount.incrementAndGet();

    try {
        database.save(article);

    } catch (Exception e) {
        int errors = errorCount.incrementAndGet();
        double errorRate = (double) errors / total;

        logger.warn("Error processing article: {}", e.getMessage());

        // Circuit breaker: Stop if error rate > threshold
        if (errorRate > errorThreshold && total > 100) {
            throw new RuntimeException(String.format(
                "Error rate %.2f%% exceeds threshold %.2f%% - aborting",
                errorRate * 100, errorThreshold * 100));
        }
    }
});
```

---

## Common Issues

### Issue 1: MD5 File Not Found

**Error:**
```
FileNotFoundException: MD5 file not found: pubmed25n0001.xml.gz.md5
```

**Solution:**
```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
Path md5File = Paths.get("pubmed25n0001.xml.gz.md5");

// Check if MD5 file exists
if (!Files.exists(md5File)) {
    // Download MD5 file
    downloadMd5File(xmlFile, md5File);
}

// Then verify
boolean isValid = Md5Verifier.verifyPubmedFile(xmlFile);
```

---

### Issue 2: MD5 Mismatch

**Error:**
```
MD5 verification failed: Expected d41d8cd98f00b204e9800998ecf8427e, got ...
```

**Causes:**
1. File corrupted during download
2. File modified after download
3. Wrong MD5 file

**Solution:**
```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

// Re-download file
System.out.println("MD5 mismatch - re-downloading file...");
downloadFile(xmlFile);

// Verify again
boolean isValid = Md5Verifier.verifyPubmedFile(xmlFile);

if (!isValid) {
    throw new IOException("File still corrupted after re-download");
}
```

---

### Issue 3: Invalid PMID

**Error:**
```
NullPointerException: PMID is null
```

**Solution:**
```java
parser.parseStream(xmlFile, article -> {
    // Defensive check
    if (article.getMedlineCitation() == null ||
        article.getMedlineCitation().getPmid() == null ||
        article.getMedlineCitation().getPmid().getValue() == null) {

        logger.warn("Skipping article with missing PMID");
        return; // Skip this article
    }

    String pmid = article.getMedlineCitation().getPmid().getValue();
    database.save(article);
});
```

---

### Issue 4: Corrupt TAR Archive

**Error:**
```
IOException: tar.gz file is corrupted or invalid
```

**Solution:**
```java
Path tarGzFile = Paths.get("pmc_package.tar.gz");

try {
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

} catch (IOException e) {
    logger.error("Archive corrupted: {}", e.getMessage());

    // Re-download archive
    System.out.println("Re-downloading archive...");
    downloadArchive(tarGzFile);

    // Retry
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
}
```

---

### Issue 5: XML Parsing Error

**Error:**
```
XMLStreamException: Unexpected end of file
```

**Causes:**
1. Incomplete file download
2. Corrupted GZip file
3. Invalid XML structure

**Solution:**
```java
// 1. Verify file integrity first
boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);
if (!md5Valid) {
    throw new IOException("File corrupted - MD5 check failed");
}

// 2. Try parsing with error recovery
try {
    parser.parseStream(xmlFile, article -> {
        database.save(article);
    });
} catch (XMLStreamException e) {
    logger.error("XML parsing error: {}", e.getMessage());

    // Option A: Re-download file
    downloadFile(xmlFile);

    // Option B: Skip this file and continue with next
    logger.warn("Skipping corrupted file: {}", xmlFile);
}
```

---

## Next Steps

- [USAGE.md](USAGE.md) - API usage guide
- [STREAMING.md](STREAMING.md) - Large file processing
- [PUBMED-SPECIFICS.md](PUBMED-SPECIFICS.md) - PubMed special cases
- [PMC-SPECIFICS.md](PMC-SPECIFICS.md) - PMC special cases

---

**Documentation Version:** 1.0
**Last Updated:** 2026-01-12
**Parser Version:** 1.0.0-SNAPSHOT
