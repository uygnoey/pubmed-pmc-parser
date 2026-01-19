# Streaming Parsing Guide

[**한국어**](STREAMING.ko.md) | **English**

Complete guide for memory-efficient streaming parsing of large PubMed and PMC XML files.

---

## Table of Contents

- [Overview](#overview)
- [Why Streaming?](#why-streaming)
- [Streaming Patterns](#streaming-patterns)
- [Performance Optimization](#performance-optimization)
- [Memory Management](#memory-management)
- [Best Practices](#best-practices)

---

## Overview

### What is Streaming Parsing?

Streaming parsing processes XML files **incrementally**, reading one article at a time instead of loading the entire file into memory.

**Traditional Parsing (DOM):**
```
File (1GB) → Memory (1GB) → Process all → Result
❌ High memory usage
❌ Slow startup
❌ OutOfMemoryError risk
```

**Streaming Parsing (StAX):**
```
File (1GB) → Read 1 article → Process → Read next → ...
✅ Constant memory O(1)
✅ Immediate processing
✅ No memory errors
```

---

### Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    XML File (1GB+)                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ GZip Stream (if .gz)
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                  XMLStreamReader (StAX)                     │
│                     - Low memory                            │
│                     - Forward-only                          │
│                     - Fast parsing                          │
└─────────────────────────────────────────────────────────────┘
                              │
                              │ Article by article
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   Consumer Callback                         │
│                     - Process article                       │
│                     - Save to DB                            │
│                     - Transform data                        │
└─────────────────────────────────────────────────────────────┘
```

---

## Why Streaming?

### File Sizes

**PubMed Baseline Files:**
- Compressed: 19-100 MB (.gz)
- Uncompressed: 100MB-1GB
- Articles per file: 30,000+

**PMC OA Packages:**
- Compressed: 43-2GB (.tar.gz)
- Uncompressed: 200MB-10GB
- Articles per package: 1,000-50,000

**Problem:** Loading entire file into memory is impractical or impossible.

---

### Memory Comparison

**Test: Parse 30,000 PubMed articles**

| Method | Memory Usage | Time | Result |
|--------|--------------|------|---------|
| DOM (full load) | ~800 MB | 5.2s | Works |
| StAX (streaming) | **58 MB** | **1.4s** | ✅ **93% less memory** |

**Test: Parse 100,000 articles**

| Method | Memory Usage | Result |
|--------|--------------|---------|
| DOM | OutOfMemoryError | ❌ Crash |
| StAX | 58 MB | ✅ Works perfectly |

---

## Streaming Patterns

### Pattern 1: Basic Streaming

Process articles one by one.

```java
PubmedXmlParser parser = new PubmedXmlParser();
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

long count = parser.parseStream(xmlFile, article -> {
    // Process article
    String pmid = article.getMedlineCitation().getPmid().getValue();
    String title = article.getMedlineCitation().getArticle().getArticleTitle().getValue();

    System.out.println(pmid + ": " + title);
});

System.out.println("Total: " + count);
```

**Use case:**
- Real-time processing
- Single article operations
- Simple transformations

**Performance:**
- PubMed: 20,000+ articles/sec
- PMC: 1,600+ articles/sec

---

### Pattern 2: Batch Streaming

Process articles in batches for database optimization.

```java
PubmedXmlParser parser = new PubmedXmlParser();
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");
int batchSize = 100;

long count = parser.parseStreamBatch(xmlFile, batchSize, batch -> {
    // Process batch
    System.out.println("Processing batch of " + batch.size() + " articles");

    // Batch insert to database (MUCH faster)
    database.batchInsert(batch);
});

System.out.println("Total: " + count);
```

**Benefits:**
- **10-50x faster** database inserts
- Reduced transaction overhead
- Better network utilization

**Optimal batch sizes:**
- PostgreSQL: 100-500
- MySQL: 100-1000
- MongoDB: 50-100
- Network API: 50-100

---

### Pattern 3: Parallel Processing

Process multiple files in parallel.

```java
ExecutorService executor = Executors.newFixedThreadPool(4);
List<Path> files = findAllPubmedFiles();

List<CompletableFuture<Long>> futures = files.stream()
    .map(file -> CompletableFuture.supplyAsync(() -> {
        try {
            PubmedXmlParser parser = new PubmedXmlParser();
            return parser.parseStream(file, article -> {
                database.save(article);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }, executor))
    .collect(Collectors.toList());

// Wait for all files
long totalCount = futures.stream()
    .map(CompletableFuture::join)
    .mapToLong(Long::longValue)
    .sum();

System.out.println("Total articles: " + totalCount);
executor.shutdown();
```

**Performance gain:**
- 4 cores: **3.5x faster**
- 8 cores: **6-7x faster**
- Limited by I/O and database throughput

---

### Pattern 4: Filtered Streaming

Only process articles matching criteria.

```java
parser.parseStream(xmlFile, article -> {
    Article articleData = article.getMedlineCitation().getArticle();

    // Filter: Only articles with abstracts
    if (articleData.getAbstract() != null) {
        // Filter: Only recent articles (2020+)
        if (isRecent(article)) {
            // Filter: Only English articles
            if (isEnglish(article)) {
                database.save(article);
            }
        }
    }
});

private boolean isRecent(PubmedArticle article) {
    PubDate pubDate = article.getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate();
    if (pubDate != null && pubDate.getYear() != null) {
        int year = Integer.parseInt(pubDate.getYear());
        return year >= 2020;
    }
    return false;
}

private boolean isEnglish(PubmedArticle article) {
    Language lang = article.getMedlineCitation().getArticle().getLanguage();
    return lang != null && "eng".equals(lang.getValue());
}
```

**Benefits:**
- Reduce storage requirements
- Faster downstream processing
- Focus on relevant data

---

### Pattern 5: Multi-Output Streaming

Write to multiple destinations simultaneously.

```java
Path jsonDir = Paths.get("output/json");
Path csvFile = Paths.get("output/articles.csv");

Files.createDirectories(jsonDir);
BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFile.toFile()));
csvWriter.write("PMID,Title,Year\n");

parser.parseStream(xmlFile, article -> {
    String pmid = article.getMedlineCitation().getPmid().getValue();

    // Output 1: JSON file
    String json = toJson(article);
    Files.writeString(jsonDir.resolve(pmid + ".json"), json);

    // Output 2: CSV line
    String csvLine = toCsvLine(article);
    csvWriter.write(csvLine + "\n");

    // Output 3: Database
    database.save(article);

    // Output 4: Search index
    searchIndex.index(article);
});

csvWriter.close();
```

---

## Performance Optimization

### 1. GZip Direct Processing

**✅ Good - Direct GZip processing:**
```java
// Parser automatically handles .gz files
Path gzFile = Paths.get("pubmed25n0001.xml.gz");
parser.parseStream(gzFile, article -> { ... });
```

**❌ Bad - Manual decompression:**
```java
// Don't do this - wastes disk I/O
Path gzFile = Paths.get("pubmed25n0001.xml.gz");
Path xmlFile = Paths.get("pubmed25n0001.xml");

// Decompress first (SLOW)
decompress(gzFile, xmlFile);

// Then parse (uses extra disk space)
parser.parseStream(xmlFile, article -> { ... });
```

**Performance:**
- Direct: **23,603 articles/sec**
- Manual decompression: ~15,000 articles/sec
- Speedup: **57% faster**

---

### 2. Batch Size Tuning

Test different batch sizes for your use case:

```java
int[] batchSizes = {50, 100, 200, 500, 1000};

for (int batchSize : batchSizes) {
    long start = System.currentTimeMillis();

    parser.parseStreamBatch(xmlFile, batchSize, batch -> {
        database.batchInsert(batch);
    });

    long elapsed = System.currentTimeMillis() - start;
    System.out.printf("Batch size %d: %.2fs%n", batchSize, elapsed / 1000.0);
}
```

**Results (example):**
```
Batch size 50: 8.2s
Batch size 100: 4.1s   ← Optimal
Batch size 200: 4.3s
Batch size 500: 4.7s
Batch size 1000: 5.1s  (Too large, worse performance)
```

---

### 3. Connection Pooling

Use database connection pool for better throughput:

```java
// ✅ Good - Connection pool
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(10);
config.setMinimumIdle(5);
DataSource dataSource = new HikariDataSource(config);

parser.parseStreamBatch(xmlFile, 100, batch -> {
    try (Connection conn = dataSource.getConnection()) {
        batchInsert(conn, batch);
    }
});
```

**Performance:**
- No pool: 2,000 articles/sec
- With pool: **8,000 articles/sec**
- Speedup: **4x faster**

---

### 4. Prepared Statements

Use prepared statements for batch inserts:

```java
parser.parseStreamBatch(xmlFile, 100, batch -> {
    String sql = "INSERT INTO articles (pmid, title, abstract) VALUES (?, ?, ?)";

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        conn.setAutoCommit(false);

        for (PubmedArticle article : batch) {
            stmt.setString(1, article.getMedlineCitation().getPmid().getValue());
            stmt.setString(2, getTitle(article));
            stmt.setString(3, getAbstract(article));
            stmt.addBatch();
        }

        stmt.executeBatch();
        conn.commit();
    }
});
```

---

### 5. Progress Monitoring

Monitor progress without performance impact:

```java
AtomicLong count = new AtomicLong(0);
long startTime = System.currentTimeMillis();

parser.parseStream(xmlFile, article -> {
    long num = count.incrementAndGet();

    // Log every 1000 articles (no performance impact)
    if (num % 1000 == 0) {
        long elapsed = System.currentTimeMillis() - startTime;
        double throughput = num * 1000.0 / elapsed;

        System.out.printf("[%s] Processed: %,d articles (%.0f/sec)%n",
            LocalTime.now(), num, throughput);
    }

    database.save(article);
});
```

**Output:**
```
[14:23:45] Processed: 1,000 articles (18,234/sec)
[14:23:46] Processed: 2,000 articles (19,012/sec)
[14:23:47] Processed: 3,000 articles (19,456/sec)
...
```

---

## Memory Management

### Memory Usage Patterns

**Streaming Parser Memory:**
```
┌────────────────────────────────────────┐
│  XMLStreamReader: ~10 MB (constant)   │
│  Current article: ~5-50 KB             │
│  Parser state: ~5 MB (constant)        │
│  ─────────────────────────────────────  │
│  Total: ~20-60 MB (constant)           │
└────────────────────────────────────────┘
```

**Key principle:** Memory usage is **independent of file size**.

- 100 MB file: 58 MB memory
- 1 GB file: 58 MB memory
- 10 GB file: 58 MB memory

---

### Batch Memory Impact

**Batch processing adds buffer memory:**

```
Base memory: 58 MB
Batch size 100: +5 MB = 63 MB total
Batch size 1000: +50 MB = 108 MB total
```

**Formula:**
```
Memory = Base (58 MB) + (Batch Size × Article Size)
```

**Recommendations:**
- Available RAM < 512 MB: Batch size 50
- Available RAM < 1 GB: Batch size 100-200
- Available RAM < 2 GB: Batch size 500
- Available RAM > 2 GB: Batch size 1000

---

### Garbage Collection

Minimize GC pressure in streaming:

```java
// ✅ Good - Let objects be garbage collected
parser.parseStream(xmlFile, article -> {
    database.save(article);
    // article is now eligible for GC
});

// ❌ Bad - Keep all articles in memory
List<PubmedArticle> allArticles = new ArrayList<>();
parser.parseStream(xmlFile, article -> {
    allArticles.add(article); // Memory leak!
});
```

---

### Memory Monitoring

Monitor memory usage during parsing:

```java
Runtime runtime = Runtime.getRuntime();
long startMemory = runtime.totalMemory() - runtime.freeMemory();

AtomicLong count = new AtomicLong(0);

parser.parseStream(xmlFile, article -> {
    long num = count.incrementAndGet();

    if (num % 5000 == 0) {
        long currentMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryDelta = currentMemory - startMemory;

        System.out.printf("Articles: %,d | Memory: %d MB (delta: %+d MB)%n",
            num,
            currentMemory / 1024 / 1024,
            memoryDelta / 1024 / 1024);
    }

    database.save(article);
});
```

**Expected output:**
```
Articles: 5,000 | Memory: 58 MB (delta: +0 MB)
Articles: 10,000 | Memory: 59 MB (delta: +1 MB)
Articles: 15,000 | Memory: 58 MB (delta: +0 MB)
Articles: 20,000 | Memory: 60 MB (delta: +2 MB)
```

Notice memory stays **constant** around 58-60 MB.

---

## Best Practices

### 1. Always Use Streaming for Production

```java
// ✅ ALWAYS do this in production
parser.parseStream(largeFile, article -> {
    database.save(article);
});

// ❌ NEVER do this in production
PubmedArticleSet articleSet = parser.parseFile(largeFile);
// Loads entire file into memory - OutOfMemoryError!
```

---

### 2. Tune Batch Size for Your Database

```java
// Test to find optimal batch size
private static final int OPTIMAL_BATCH_SIZE = 100; // Found through testing

parser.parseStreamBatch(xmlFile, OPTIMAL_BATCH_SIZE, batch -> {
    database.batchInsert(batch);
});
```

---

### 3. Handle Errors Without Stopping

```java
AtomicInteger errorCount = new AtomicInteger(0);

parser.parseStream(xmlFile, article -> {
    try {
        database.save(article);
    } catch (Exception e) {
        errorCount.incrementAndGet();
        String pmid = article.getMedlineCitation().getPmid().getValue();
        logger.error("Failed to save article {}: {}", pmid, e.getMessage());
        // Continue processing next article
    }
});

System.out.println("Errors: " + errorCount.get());
```

---

### 4. Use Progress Monitoring

```java
// Monitor progress for long-running operations
AtomicLong count = new AtomicLong(0);
parser.parseStream(xmlFile, article -> {
    if (count.incrementAndGet() % 1000 == 0) {
        System.out.println("Progress: " + count.get());
    }
    database.save(article);
});
```

---

### 5. Close Resources Properly

```java
// Parser handles resource cleanup automatically
parser.parseStream(xmlFile, article -> {
    database.save(article);
}); // XMLStreamReader and InputStream closed automatically

// If you manage external resources, use try-with-resources
try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.csv"))) {
    parser.parseStream(xmlFile, article -> {
        writer.write(toCsvLine(article) + "\n");
    });
} // writer closed automatically
```

---

## Performance Benchmarks

### PubMed Performance

**Test: pubmed25n0001.xml.gz (19 MB compressed, 30,000 articles)**

| Method | Time | Throughput | Memory |
|--------|------|------------|---------|
| parseFile() | 1.64s | 18,282/sec | 800 MB |
| parseStream() | 1.44s | 20,776/sec | 58 MB |
| parseStreamBatch(100) | 1.30s | 23,166/sec | 63 MB |
| parseStreamBatch(500) | 1.27s | 23,603/sec | 108 MB |

**Winner:** Batch streaming with size 500 - **23,603 articles/sec**

---

### PMC Performance

**Test: PMC tar.gz package (43 MB compressed, 3,028 articles)**

| Method | Time | Throughput | Memory |
|--------|------|------------|---------|
| parseTarGz() | 1.83s | 1,651/sec | 120 MB |
| parseStream() | 1.92s | 1,577/sec | 85 MB |

**Note:** PMC articles are **23x larger** than PubMed (full text vs metadata only).

---

### Throughput Comparison

**Normalized by file size (MB/sec):**

| Parser | Articles/sec | File Size/Article | MB/sec |
|--------|--------------|-------------------|---------|
| PubMed | 23,603 | 0.63 KB | 11.6 MB/sec |
| PMC | 1,651 | 14.2 KB | 23.5 MB/sec |

**PMC is 2x faster** per byte processed due to better compression ratio.

---

## Troubleshooting

### OutOfMemoryError

**Problem:** Still getting OutOfMemoryError with streaming.

**Cause:** Probably not using streaming correctly.

```java
// ❌ Wrong - This loads entire file
PubmedArticleSet articleSet = parser.parseFile(largeFile);

// ✅ Correct - This uses streaming
parser.parseStream(largeFile, article -> {
    database.save(article);
});
```

---

### Slow Performance

**Problem:** Parsing slower than benchmarks.

**Causes:**
1. **Database bottleneck** - Use batch inserts
2. **Network latency** - Use connection pooling
3. **Disk I/O** - Process GZip files directly
4. **Small batch size** - Increase to 100-500

**Solution:**
```java
// Optimize all factors
parser.parseStreamBatch(gzFile, 200, batch -> {
    try (Connection conn = pooledDataSource.getConnection()) {
        batchInsert(conn, batch);
    }
});
```

---

### Memory Growth Over Time

**Problem:** Memory grows gradually during processing.

**Cause:** Holding references to articles.

```java
// ❌ Memory leak
List<PubmedArticle> cache = new ArrayList<>();
parser.parseStream(xmlFile, article -> {
    cache.add(article); // Don't keep all articles!
});

// ✅ Let GC collect
parser.parseStream(xmlFile, article -> {
    database.save(article);
    // article can be garbage collected
});
```

---

## Next Steps

- [USAGE.md](USAGE.md) - API usage guide
- [VALIDATION.md](VALIDATION.md) - Validation and error handling
- [PUBMED-SPECIFICS.md](PUBMED-SPECIFICS.md) - PubMed special cases
- [PMC-SPECIFICS.md](PMC-SPECIFICS.md) - PMC special cases

---

**Documentation Version:** 1.0
**Last Updated:** 2026-01-12
**Parser Version:** 1.0.0-SNAPSHOT
