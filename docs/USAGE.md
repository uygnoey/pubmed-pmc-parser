# API Usage Guide

[**한국어**](USAGE.ko.md) | **English**

Complete guide for using the PubMed & PMC XML Parser API.

---

## Table of Contents

- [Quick Start](#quick-start)
- [PubMed Parser](#pubmed-parser)
- [PMC Parser](#pmc-parser)
- [Common Patterns](#common-patterns)
- [Error Handling](#error-handling)

---

## Quick Start

### Installation

```gradle
dependencies {
    implementation 'io.brillianttiger.bio:pubmed-pmc-parser:1.0.0-SNAPSHOT'
}
```

### Basic Usage

```java
// PubMed parser
PubmedXmlParser pubmedParser = new PubmedXmlParser();
PubmedArticleSet articleSet = pubmedParser.parseFile(Paths.get("pubmed25n0001.xml.gz"));

// PMC parser
PmcXmlParser pmcParser = new PmcXmlParser();
JatsArticle article = pmcParser.parseFile(Paths.get("PMC1234567.xml"));
```

---

## PubMed Parser

### PubmedXmlParser

Main class for parsing PubMed XML files (DTD pubmed_250101).

#### Constructor

```java
PubmedXmlParser parser = new PubmedXmlParser();
```

**Features:**
- Automatic XXE attack prevention
- Automatic GZip file handling
- StAX-based streaming parser

---

### Parsing Methods

#### 1. parseFile() - Complete File Parsing

Parse entire XML file and return `PubmedArticleSet`.

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
PubmedArticleSet articleSet = parser.parseFile(xmlFile);

// Access articles
List<PubmedArticle> articles = articleSet.getPubmedArticles();
for (PubmedArticle article : articles) {
    String pmid = article.getMedlineCitation().getPmid().getValue();
    String title = article.getMedlineCitation().getArticle().getArticleTitle().getValue();
    System.out.println(pmid + ": " + title);
}
```

**When to use:**
- Small to medium files (<100MB)
- Need complete file structure (including DeleteCitation)
- Processing all articles at once

---

#### 2. parseStream() - Streaming Parsing

Memory-efficient streaming parsing with callback processing.

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

long count = parser.parseStream(xmlFile, article -> {
    // Process each article
    String pmid = article.getMedlineCitation().getPmid().getValue();

    // Save to database
    database.save(article);

    // Log progress
    if (count % 1000 == 0) {
        System.out.println("Processed: " + count);
    }
});

System.out.println("Total articles: " + count);
```

**When to use:**
- Large files (>100MB)
- Need constant memory usage O(1)
- Real-time processing required

**Performance:**
- PubMed: 20,000+ articles/sec
- Memory: Constant O(1)

---

#### 3. parseStreamBatch() - Batch Streaming

Process articles in batches for optimized database inserts.

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
int batchSize = 100;

long count = parser.parseStreamBatch(xmlFile, batchSize, batch -> {
    // Process batch of articles
    System.out.println("Processing batch of " + batch.size() + " articles");

    // Batch insert to database
    database.batchInsert(batch);
});

System.out.println("Total articles: " + count);
```

**When to use:**
- Database batch inserts
- Network batch operations
- Optimized throughput required

**Batch size recommendations:**
- Database: 100-500
- Network: 50-100
- Memory-constrained: 10-50

---

#### 4. parseStreamAll() - Multi-Type Streaming

Handle both `PubmedArticle` and `PubmedBookArticle`.

```java
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

long count = parser.parseStreamAll(xmlFile,
    article -> {
        // Handle PubmedArticle
        System.out.println("Article: " + article.getMedlineCitation().getPmid().getValue());
    },
    bookArticle -> {
        // Handle PubmedBookArticle
        System.out.println("Book: " + bookArticle.getBookDocument().getPmid().getValue());
    }
);
```

**When to use:**
- Need to handle both article types
- Different processing logic for each type

---

#### 5. extractDeleteCitation() - Extract Deleted PMIDs

Extract deleted PMID list from update files.

```java
Path updateFile = Paths.get("pubmed25n1275.xml.gz");

DeleteCitation deleteCitation = parser.extractDeleteCitation(updateFile);

if (deleteCitation != null) {
    List<PMID> deletedPmids = deleteCitation.getPmids();
    System.out.println("Deleted PMIDs: " + deletedPmids.size());

    // Delete from database
    for (PMID pmid : deletedPmids) {
        database.delete(pmid.getValue());
    }
}
```

**When to use:**
- Processing PubMed update files
- Maintaining database consistency
- FTP update file handling

---

## PMC Parser

### PmcXmlParser

Main class for parsing PMC XML files (JATS 1.4 standard).

#### Constructor

```java
PmcXmlParser parser = new PmcXmlParser();
```

**Features:**
- JATS 1.4 full support
- Namespace handling (xlink, mml)
- Recursive structure support
- tar.gz package parsing

---

### Parsing Methods

#### 1. parseFile() - Single File Parsing

Parse single PMC XML file.

```java
Path xmlFile = Paths.get("PMC1234567.xml");
JatsArticle article = parser.parseFile(xmlFile);

// Access article metadata
if (article.getFront() != null) {
    ArticleMeta meta = article.getFront().getArticleMeta();

    // Article IDs
    if (meta.getArticleIds() != null) {
        for (PmcArticleId id : meta.getArticleIds()) {
            System.out.println(id.getPubIdType() + ": " + id.getValue());
        }
    }

    // Title
    if (meta.getTitleGroup() != null) {
        String title = meta.getTitleGroup().getArticleTitle().getContent();
        System.out.println("Title: " + title);
    }
}

// Access full text
if (article.getBody() != null) {
    List<Sec> sections = article.getBody().getSections();
    for (Sec section : sections) {
        System.out.println("Section: " + section.getTitle().getContent());
    }
}
```

**When to use:**
- Single XML file processing
- Full structure access required
- Small to medium files

---

#### 2. parseStream() - Streaming Parsing

Memory-efficient streaming for large files.

```java
Path xmlFile = Paths.get("large_article.xml");

long count = parser.parseStream(xmlFile, article -> {
    // Process each article
    ArticleMeta meta = article.getFront().getArticleMeta();
    String pmcId = meta.getArticleIds().get(0).getValue();

    // Save to database
    database.save(article);
});
```

**Performance:**
- PMC: 1,600+ articles/sec
- Memory: Constant O(1)

---

#### 3. parseStreamBatch() - Batch Processing

Batch processing for database optimization.

```java
Path xmlFile = Paths.get("pmc_articles.xml");
int batchSize = 50;

long count = parser.parseStreamBatch(xmlFile, batchSize, batch -> {
    // Batch insert
    database.batchInsert(batch);
});
```

---

#### 4. parseTarGz() - Archive Parsing

Parse PMC tar.gz packages from FTP.

```java
Path tarGzFile = Paths.get("oa_comm/pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

// Parse all articles in archive
List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

System.out.println("Parsed " + articles.size() + " articles from archive");

// Process articles
for (JatsArticle article : articles) {
    ArticleMeta meta = article.getFront().getArticleMeta();
    // Process article...
}
```

**When to use:**
- PMC FTP bulk download processing
- Archive file handling
- Batch processing from tar.gz

**Features:**
- Automatic GZip decompression
- Tar archive extraction
- Integrity validation (decompression success)
- Direct XML parsing from archive entries

**Note:** PMC doesn't provide MD5 checksums, so integrity is validated through successful decompression.

---

#### 5. validateArticle() - Article Validation

Validate parsed article according to JATS 1.4 standard.

```java
JatsArticle article = parser.parseFile(xmlFile);

// Validate article
List<ValidationError> errors = parser.validateArticle(article);

if (errors.isEmpty()) {
    System.out.println("✅ Article is valid");
} else {
    System.out.println("⚠️  Validation errors: " + errors.size());
    for (ValidationError error : errors) {
        System.out.println("  - " + error.getMessage());
    }
}
```

**Validation checks:**
- Required elements (front, article-meta, title-group)
- ID format validation
- Reference integrity
- JATS 1.4 compliance

---

#### 6. parseAndValidate() - Parse and Validate

Combined parsing and validation in one step.

```java
PmcXmlParser.ValidationResult result = parser.parseAndValidate(xmlFile);

JatsArticle article = result.getArticle();
List<ValidationError> errors = result.getErrors();

if (result.isValid()) {
    System.out.println("✅ Valid article");
} else if (result.hasErrors()) {
    System.out.println("❌ Has errors");
    result.printErrors();
} else if (result.hasWarnings()) {
    System.out.println("⚠️  Has warnings");
}

// Get summary
System.out.println(result.getSummary());
```

---

## Common Patterns

### Pattern 1: Database Import

```java
PubmedXmlParser parser = new PubmedXmlParser();
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
int batchSize = 100;

parser.parseStreamBatch(xmlFile, batchSize, batch -> {
    try {
        // Start transaction
        database.beginTransaction();

        // Batch insert
        database.batchInsert(batch);

        // Commit
        database.commit();

    } catch (Exception e) {
        database.rollback();
        throw e;
    }
});
```

---

### Pattern 2: Progress Monitoring

```java
AtomicLong count = new AtomicLong(0);
AtomicLong startTime = new AtomicLong(System.currentTimeMillis());

parser.parseStream(xmlFile, article -> {
    long num = count.incrementAndGet();

    // Progress every 1000 articles
    if (num % 1000 == 0) {
        long elapsed = System.currentTimeMillis() - startTime.get();
        double throughput = num * 1000.0 / elapsed;
        System.out.printf("Progress: %,d articles (%.0f/sec)%n", num, throughput);
    }

    // Process article
    database.save(article);
});
```

---

### Pattern 3: Error Recovery

```java
AtomicInteger errorCount = new AtomicInteger(0);
List<String> errorLog = new CopyOnWriteArrayList<>();

parser.parseStream(xmlFile, article -> {
    try {
        // Validate
        validateArticle(article);

        // Process
        database.save(article);

    } catch (Exception e) {
        errorCount.incrementAndGet();
        String pmid = article.getMedlineCitation().getPmid().getValue();
        errorLog.add(pmid + ": " + e.getMessage());

        // Continue processing (don't throw)
    }
});

System.out.println("Errors: " + errorCount.get());
errorLog.forEach(System.err::println);
```

---

### Pattern 4: Filtered Processing

```java
// Only process articles with abstracts
parser.parseStream(xmlFile, article -> {
    Article articleData = article.getMedlineCitation().getArticle();

    if (articleData.getAbstract() != null) {
        // Process only articles with abstracts
        database.save(article);
    }
});
```

---

### Pattern 5: Multi-Format Export

```java
parser.parseStream(xmlFile, article -> {
    String pmid = article.getMedlineCitation().getPmid().getValue();

    // Export to JSON
    jsonExporter.export(article, "json/" + pmid + ".json");

    // Export to database
    database.save(article);

    // Export to search index
    searchIndex.index(article);
});
```

---

## Error Handling

### Common Exceptions

```java
try {
    PubmedArticleSet articleSet = parser.parseFile(xmlFile);

} catch (FileNotFoundException e) {
    System.err.println("File not found: " + xmlFile);

} catch (XMLStreamException e) {
    System.err.println("XML parsing error: " + e.getMessage());

} catch (IOException e) {
    System.err.println("I/O error: " + e.getMessage());

} catch (Exception e) {
    System.err.println("Unexpected error: " + e.getMessage());
}
```

---

### Validation Errors

```java
PmcXmlParser.ValidationResult result = parser.parseAndValidate(xmlFile);

if (!result.isValid()) {
    for (ValidationError error : result.getErrors()) {
        switch (error.getSeverity()) {
            case ERROR:
                System.err.println("ERROR: " + error.getMessage());
                break;
            case WARNING:
                System.out.println("WARNING: " + error.getMessage());
                break;
            case INFO:
                System.out.println("INFO: " + error.getMessage());
                break;
        }
    }
}
```

---

## Best Practices

### 1. Always Use Streaming for Large Files

```java
// ✅ Good - Streaming
parser.parseStream(largeFile, article -> {
    database.save(article);
});

// ❌ Bad - Load entire file
PubmedArticleSet articleSet = parser.parseFile(largeFile); // OutOfMemoryError
```

---

### 2. Use Batch Processing for Database

```java
// ✅ Good - Batch insert
parser.parseStreamBatch(xmlFile, 100, batch -> {
    database.batchInsert(batch);
});

// ❌ Bad - Individual inserts
parser.parseStream(xmlFile, article -> {
    database.insert(article); // Too slow
});
```

---

### 3. Handle Errors Gracefully

```java
// ✅ Good - Continue on error
parser.parseStream(xmlFile, article -> {
    try {
        database.save(article);
    } catch (Exception e) {
        logger.error("Failed to save article: " + e.getMessage());
        // Continue processing
    }
});

// ❌ Bad - Abort on first error
parser.parseStream(xmlFile, article -> {
    database.save(article); // Throws exception, stops processing
});
```

---

### 4. Validate Critical Data

```java
// ✅ Good - Validate before saving
parser.parseStream(xmlFile, article -> {
    if (article.getMedlineCitation() != null &&
        article.getMedlineCitation().getPmid() != null) {
        database.save(article);
    } else {
        logger.warn("Invalid article: missing PMID");
    }
});
```

---

### 5. Monitor Progress

```java
// ✅ Good - Progress monitoring
AtomicLong count = new AtomicLong(0);
parser.parseStream(xmlFile, article -> {
    long num = count.incrementAndGet();
    if (num % 1000 == 0) {
        System.out.println("Processed: " + num);
    }
    database.save(article);
});
```

---

## Next Steps

- [STREAMING.md](STREAMING.md) - Large file processing guide
- [VALIDATION.md](VALIDATION.md) - Validation and error handling
- [PUBMED-SPECIFICS.md](PUBMED-SPECIFICS.md) - PubMed special cases
- [PMC-SPECIFICS.md](PMC-SPECIFICS.md) - PMC special cases

---

**Documentation Version:** 1.0
**Last Updated:** 2026-01-12
**Parser Version:** 1.0.0-SNAPSHOT
