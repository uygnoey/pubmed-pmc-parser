# PMC (PubMed Central) Special Cases Guide

[**한국어**](PMC-SPECIFICS.ko.md) | **English**

Complete guide for handling PMC-specific special cases and JATS 1.4 edge cases.

---

## Table of Contents

- [No Checksum Files](#no-checksum-files)
- [TAR.GZ Package Handling](#targz-package-handling)
- [Sub-Article Recursive Structures](#sub-article-recursive-structures)
- [Front vs Front-Stub](#front-vs-front-stub)
- [Response Elements](#response-elements)
- [FloatsGroup](#floatsgroup)
- [Namespace Handling](#namespace-handling)
- [Table Processing](#table-processing)

---

## No Checksum Files

### Key Difference from PubMed

**PubMed:**
- Provides MD5 checksum files (`.md5`)
- Can verify file integrity before parsing
- Example: `pubmed25n0001.xml.gz.md5`

**PMC:**
- ❌ **NO** MD5 or SHA checksum files provided
- File integrity validated through successful decompression
- No pre-parsing verification available

---

### Integrity Validation Strategy

#### Automatic Validation Through Parsing

```java
PmcXmlParser parser = new PmcXmlParser();
Path tarGzFile = Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

try {
    // If parsing succeeds, archive is valid
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
    System.out.println("✅ Archive integrity validated");
    System.out.println("Parsed " + articles.size() + " articles");

} catch (IOException e) {
    // Decompression or parsing failed → file is corrupted
    System.err.println("❌ Archive corrupted: " + e.getMessage());

    // Re-download file
    downloadArchive(tarGzFile);
}
```

---

#### Manual SHA-256 Calculation

If you need to maintain your own checksums:

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

// Usage
Path tarGzFile = Paths.get("pmc_package.tar.gz");
String sha256 = calculateSha256(tarGzFile);
System.out.println("SHA-256: " + sha256);

// Store this checksum for future validation
database.storeChecksum(tarGzFile.getFileName().toString(), sha256);
```

---

### Production Pattern

```java
public void processPmcArchiveWithValidation(Path tarGzFile) throws Exception {
    // 1. Calculate checksum before processing
    String currentChecksum = calculateSha256(tarGzFile);

    // 2. Check if we've processed this file before
    String storedChecksum = database.getChecksum(tarGzFile.getFileName().toString());
    if (storedChecksum != null && storedChecksum.equals(currentChecksum)) {
        System.out.println("✅ File already processed, skipping");
        return;
    }

    // 3. Try parsing (validates integrity)
    try {
        PmcXmlParser parser = new PmcXmlParser();
        List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

        // 4. Save articles to database
        for (JatsArticle article : articles) {
            database.save(article);
        }

        // 5. Store checksum to prevent reprocessing
        database.storeChecksum(tarGzFile.getFileName().toString(), currentChecksum);
        System.out.println("✅ Processing complete");

    } catch (IOException e) {
        logger.error("❌ Archive corrupted: {}", e.getMessage());

        // Re-download and retry
        downloadArchive(tarGzFile);
        processPmcArchiveWithValidation(tarGzFile);
    }
}
```

---

## TAR.GZ Package Handling

### PMC FTP Structure

PMC provides articles in compressed tar.gz packages:

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

**Each package contains:**
- Multiple XML files (100-30,000 articles per package)
- No checksum files
- GZip compression + TAR archive

---

### Parsing TAR.GZ Packages

#### Method 1: Parse Entire Archive

```java
PmcXmlParser parser = new PmcXmlParser();
Path tarGzFile = Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

// Parse all articles in the archive
List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

System.out.println("Total articles: " + articles.size());

// Process articles
for (JatsArticle article : articles) {
    ArticleMeta meta = article.getFront().getArticleMeta();

    // Get PMC ID
    List<PmcArticleId> ids = meta.getArticleIds();
    String pmcId = ids.stream()
                      .filter(id -> "pmc".equals(id.getPubIdType()))
                      .map(PmcArticleId::getValue)
                      .findFirst()
                      .orElse(null);

    System.out.println("Processing: " + pmcId);
    database.save(article);
}
```

---

#### Method 2: Streaming from TAR.GZ

For large archives, use streaming to process articles one by one:

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
                // Parse XML from stream
                JatsArticle article = parser.parseStream(tis);
                database.save(article);
                count++;

                if (count % 100 == 0) {
                    System.out.println("Processed: " + count);
                }
            }
        }

        System.out.println("Total articles: " + count);
    }
}
```

---

### Batch Processing

```java
public void processPmcBulkDownload(Path downloadDir) throws Exception {
    // Find all tar.gz files
    List<Path> archives = Files.walk(downloadDir)
        .filter(path -> path.toString().endsWith(".tar.gz"))
        .sorted()
        .toList();

    System.out.println("Found " + archives.size() + " archives");

    for (Path archive : archives) {
        System.out.println("\nProcessing: " + archive.getFileName());

        try {
            List<JatsArticle> articles = parser.parseTarGz(archive);

            // Batch insert to database
            database.batchInsert(articles);

            System.out.printf("✅ %s: %d articles%n",
                              archive.getFileName(),
                              articles.size());

        } catch (Exception e) {
            logger.error("❌ Failed to process {}: {}",
                         archive.getFileName(),
                         e.getMessage());
            // Continue with next archive
        }
    }
}
```

---

## Sub-Article Recursive Structures

### What is Sub-Article?

Sub-article is a **recursive structure** in JATS 1.4 that represents:
- Translations of the main article
- Related articles or companion pieces
- Appendices with substantial content
- Responses and commentary

**Key feature:** Sub-articles can contain sub-articles (recursive nesting).

---

### Sub-Article Structure

```xml
<article>
    <front>...</front>
    <body>...</body>

    <!-- Main article can contain sub-articles -->
    <sub-article article-type="translation" xml:lang="es" id="S1">
        <front-stub>
            <title-group>
                <article-title>Título en español</article-title>
            </title-group>
        </front-stub>
        <body>...</body>

        <!-- Sub-article can contain nested sub-articles -->
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

### Parsing Sub-Articles

```java
PmcXmlParser parser = new PmcXmlParser();
Path xmlFile = Paths.get("PMC1234567.xml");
JatsArticle article = parser.parseFile(xmlFile);

// Check for sub-articles
List<SubArticle> subArticles = article.getSubArticles();

if (subArticles != null && !subArticles.isEmpty()) {
    System.out.println("Found " + subArticles.size() + " sub-articles");

    for (SubArticle subArticle : subArticles) {
        // Sub-article attributes
        String articleType = subArticle.getArticleType().getValue();  // "translation", "reply", etc.
        String id = subArticle.getId();                                // "S1"
        String lang = subArticle.getXmlLang();                        // "es", "fr", etc.

        System.out.printf("Sub-article: type=%s, id=%s, lang=%s%n",
                          articleType, id, lang);

        // Access metadata
        if (subArticle.getFrontStub() != null) {
            TitleGroup titleGroup = subArticle.getFrontStub().getTitleGroup();
            if (titleGroup != null) {
                String title = titleGroup.getArticleTitle().getContent();
                System.out.println("Title: " + title);
            }
        }

        // Access body
        if (subArticle.getBody() != null) {
            System.out.println("Has body content");
        }

        // Recursive: Check for nested sub-articles
        List<SubArticle> nestedSubArticles = subArticle.getSubArticles();
        if (nestedSubArticles != null && !nestedSubArticles.isEmpty()) {
            System.out.println("  └─ Has " + nestedSubArticles.size() + " nested sub-articles");
        }
    }
}
```

---

### Common Sub-Article Types

```java
// article-type values
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

### Recursive Processing Pattern

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

        // Save to database
        database.saveSubArticle(subArticle, depth);

        // Recursively process nested sub-articles
        if (subArticle.getSubArticles() != null) {
            processSubArticles(subArticle.getSubArticles(), depth + 1);
        }

        // Process responses
        if (subArticle.getResponses() != null) {
            for (Response response : subArticle.getResponses()) {
                System.out.printf("%s  └─ Response: %s%n",
                                  indent,
                                  response.getResponseType());
            }
        }
    }
}

// Usage
JatsArticle article = parser.parseFile(xmlFile);
processSubArticles(article.getSubArticles(), 0);
```

---

### Translation Handling

```java
// Extract all translations
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

// Usage
Map<String, SubArticle> translations = extractTranslations(article);
System.out.println("Available languages: " + translations.keySet());

// Get Spanish translation
SubArticle spanish = translations.get("es");
if (spanish != null) {
    String title = spanish.getFrontStub()
                          .getTitleGroup()
                          .getArticleTitle()
                          .getContent();
    System.out.println("Spanish title: " + title);
}
```

---

## Front vs Front-Stub

### Key Differences

| Feature | `<front>` | `<front-stub>` |
|---------|-----------|----------------|
| Usage | Main article | Sub-articles, responses |
| Journal Meta | ✅ Required | ❌ Not included |
| Article Meta | ✅ Included | ✅ Included |
| Complexity | Full metadata | Abbreviated |
| Size | Larger | Smaller |

---

### Front Structure

**Complete front matter for main article:**

```xml
<front>
    <!-- Journal metadata -->
    <journal-meta>
        <journal-id>...</journal-id>
        <journal-title-group>...</journal-title-group>
        <issn>...</issn>
        <publisher>...</publisher>
    </journal-meta>

    <!-- Article metadata -->
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

### Front-Stub Structure

**Abbreviated front matter for sub-articles:**

```xml
<sub-article article-type="reply">
    <front-stub>
        <!-- NO journal-meta -->
        <!-- Only article-specific metadata -->
        <title-group>
            <article-title>Author's Response</article-title>
        </title-group>
        <contrib-group>...</contrib-group>
    </front-stub>
    <body>...</body>
</sub-article>
```

---

### Parsing Front vs Front-Stub

```java
// Main article - has front
JatsArticle article = parser.parseFile(xmlFile);
Front front = article.getFront();

if (front != null) {
    // Access journal metadata
    JournalMeta journalMeta = front.getJournalMeta();
    String journalTitle = journalMeta.getJournalTitleGroup()
                                     .getJournalTitle()
                                     .getValue();

    // Access article metadata
    ArticleMeta articleMeta = front.getArticleMeta();
    String articleTitle = articleMeta.getTitleGroup()
                                     .getArticleTitle()
                                     .getContent();
}

// Sub-article - has front-stub
SubArticle subArticle = article.getSubArticles().get(0);

// Check which type is present
if (subArticle.getFront() != null) {
    // Full front (rare in sub-articles)
    Front subFront = subArticle.getFront();
    // Has both journal-meta and article-meta
} else if (subArticle.getFrontStub() != null) {
    // Front-stub (common in sub-articles)
    FrontStub frontStub = subArticle.getFrontStub();

    // NO journal-meta available
    // Only article-specific metadata
    TitleGroup titleGroup = frontStub.getTitleGroup();
    List<ContribGroup> contribGroups = frontStub.getContribGroups();
    List<PubDate> pubDates = frontStub.getPubDates();
}
```

---

### Unified Metadata Access Pattern

```java
public class MetadataExtractor {

    public String getTitle(SubArticle subArticle) {
        // Try front first
        if (subArticle.getFront() != null) {
            ArticleMeta meta = subArticle.getFront().getArticleMeta();
            if (meta != null && meta.getTitleGroup() != null) {
                return meta.getTitleGroup().getArticleTitle().getContent();
            }
        }

        // Fall back to front-stub
        if (subArticle.getFrontStub() != null) {
            TitleGroup titleGroup = subArticle.getFrontStub().getTitleGroup();
            if (titleGroup != null && titleGroup.getArticleTitle() != null) {
                return titleGroup.getArticleTitle().getContent();
            }
        }

        return null;
    }

    public List<ContribGroup> getContributors(SubArticle subArticle) {
        // Try front first
        if (subArticle.getFront() != null) {
            ArticleMeta meta = subArticle.getFront().getArticleMeta();
            if (meta != null) {
                return meta.getContribGroups();
            }
        }

        // Fall back to front-stub
        if (subArticle.getFrontStub() != null) {
            return subArticle.getFrontStub().getContribGroups();
        }

        return Collections.emptyList();
    }
}
```

---

## Response Elements

### What is Response?

Response represents formal replies to articles:
- Author's reply to peer review
- Discussions
- Reviewer reports
- Editor comments
- Corrections and addenda

---

### Response Structure

```xml
<article>
    <front>...</front>
    <body>...</body>
    <back>...</back>

    <!-- Responses at article level -->
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

### Parsing Responses

```java
JatsArticle article = parser.parseFile(xmlFile);
List<Response> responses = article.getResponses();

if (responses != null && !responses.isEmpty()) {
    System.out.println("Found " + responses.size() + " responses");

    for (Response response : responses) {
        // Response attributes
        String id = response.getId();                           // "R1"
        ResponseType type = response.getResponseType();         // REPLY, REVIEWER_REPORT, etc.
        String lang = response.getXmlLang();                    // "en"

        System.out.printf("Response: id=%s, type=%s%n", id, type);

        // Access metadata via front-stub
        if (response.getFrontStub() != null) {
            FrontStub frontStub = response.getFrontStub();
            TitleGroup titleGroup = frontStub.getTitleGroup();

            if (titleGroup != null) {
                String title = titleGroup.getArticleTitle().getContent();
                System.out.println("Title: " + title);
            }

            // Contributors
            List<ContribGroup> contribGroups = frontStub.getContribGroups();
            if (contribGroups != null) {
                System.out.println("Contributors: " + contribGroups.size());
            }
        }

        // Access body content
        if (response.getBody() != null) {
            Body body = response.getBody();
            System.out.println("Has response body");
        }
    }
}
```

---

### Response Types

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

### Production Pattern

```java
public void extractPeerReview(JatsArticle article) {
    List<Response> responses = article.getResponses();

    if (responses == null) {
        return;
    }

    // Separate by type
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

    // Process reviewer reports
    for (Response report : reviewerReports) {
        String reportId = report.getId();
        String reportContent = extractBodyText(report.getBody());

        database.saveReviewerReport(article.getId(), reportId, reportContent);
        System.out.println("Saved reviewer report: " + reportId);
    }

    // Process author replies
    for (Response reply : authorReplies) {
        String replyId = reply.getId();
        String replyContent = extractBodyText(reply.getBody());

        database.saveAuthorReply(article.getId(), replyId, replyContent);
        System.out.println("Saved author reply: " + replyId);
    }
}
```

---

## FloatsGroup

### What is FloatsGroup?

FloatsGroup contains **floating elements** that are referenced from the body but may be placed at the end or in a separate section:
- Figures (images, diagrams)
- Tables
- Boxed text
- Chemical structures
- Supplementary materials

---

### FloatsGroup Structure

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

### Parsing FloatsGroup

```java
JatsArticle article = parser.parseFile(xmlFile);
FloatsGroup floatsGroup = article.getFloatsGroup();

if (floatsGroup != null) {
    // Process figures
    List<Fig> figures = floatsGroup.getFigs();
    if (figures != null) {
        System.out.println("Figures: " + figures.size());
        for (Fig fig : figures) {
            String id = fig.getId();                    // "fig1"
            String label = fig.getLabel().getValue();  // "Figure 1"

            Caption caption = fig.getCaption();
            if (caption != null && caption.getTitle() != null) {
                String title = caption.getTitle().getContent();
                System.out.println("  " + label + ": " + title);
            }

            // Get image file path
            List<Graphic> graphics = fig.getGraphics();
            if (graphics != null) {
                for (Graphic graphic : graphics) {
                    String href = graphic.getXlinkHref();  // "fig1.jpg"
                    System.out.println("    Image: " + href);
                }
            }
        }
    }

    // Process tables
    List<TableWrap> tables = floatsGroup.getTableWraps();
    if (tables != null) {
        System.out.println("Tables: " + tables.size());
        for (TableWrap tableWrap : tables) {
            String id = tableWrap.getId();
            String label = tableWrap.getLabel() != null ?
                           tableWrap.getLabel().getValue() : null;
            System.out.println("  " + label);

            // Access table content
            if (tableWrap.getTable() != null) {
                Table table = tableWrap.getTable();
                // Process table data...
            }
        }
    }

    // Process supplementary materials
    List<SupplementaryMaterial> supplements = floatsGroup.getSupplementaryMaterials();
    if (supplements != null) {
        System.out.println("Supplementary materials: " + supplements.size());
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

### Production Pattern: Extract Figures

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

            // ID and label
            metadata.setId(fig.getId());
            metadata.setLabel(fig.getLabel() != null ?
                             fig.getLabel().getValue() : null);

            // Caption
            Caption caption = fig.getCaption();
            if (caption != null) {
                if (caption.getTitle() != null) {
                    metadata.setTitle(caption.getTitle().getContent());
                }
                // Extract caption paragraphs
                List<String> captionParagraphs = extractParagraphs(caption.getContent());
                metadata.setCaptionText(String.join(" ", captionParagraphs));
            }

            // Image files
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

## Namespace Handling

### Common Namespaces in PMC

PMC XML uses multiple namespaces:

| Namespace | Prefix | Usage |
|-----------|--------|-------|
| XLink | `xlink` | Links and references |
| MathML | `mml` | Mathematical formulas |
| XHTML | `xhtml` | HTML table elements |

---

### XLink Namespace

**Usage:** Link attributes for graphics, external links, etc.

```xml
<graphic xlink:href="figure1.jpg"
         xlink:type="simple"
         xlink:role="original-image"
         xlink:show="embed"/>

<ext-link ext-link-type="uri"
          xlink:href="https://example.com"
          xlink:type="simple">Visit site</ext-link>
```

---

### Parsing XLink Attributes

```java
// Graphics with xlink:href
Fig fig = floatsGroup.getFigs().get(0);
List<Graphic> graphics = fig.getGraphics();

for (Graphic graphic : graphics) {
    String href = graphic.getXlinkHref();       // "figure1.jpg"
    String type = graphic.getXlinkType();       // "simple"
    String role = graphic.getXlinkRole();       // "original-image"
    String show = graphic.getXlinkShow();       // "embed"

    System.out.printf("Graphic: href=%s, type=%s%n", href, type);
}

// External links with xlink:href
List<ExtLink> extLinks = article.getFront()
                                .getArticleMeta()
                                .getExtLinks();

for (ExtLink extLink : extLinks) {
    String href = extLink.getXlinkHref();       // "https://example.com"
    String type = extLink.getExtLinkType();     // "uri"
    String content = extLink.getContent();      // "Visit site"

    System.out.printf("External link: %s -> %s%n", content, href);
}
```

---

### MathML Namespace

**Usage:** Mathematical formulas

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

### Parsing MathML

```java
// Inline formulas
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
                    System.out.println("Formula: " + mathContent);
                }
            }
        }
    }
}

// Display formulas
List<DispFormula> dispFormulas = body.getDisplayFormulas();
if (dispFormulas != null) {
    for (DispFormula dispFormula : dispFormulas) {
        MmlMath math = dispFormula.getMmlMath();
        if (math != null) {
            System.out.println("Display formula: " + math.getContent());
        }
    }
}
```

---

## Table Processing

### XHTML Tables in JATS

PMC articles can contain XHTML tables with full HTML table markup:

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

### Parsing Tables

```java
FloatsGroup floatsGroup = article.getFloatsGroup();
List<TableWrap> tableWraps = floatsGroup.getTableWraps();

for (TableWrap tableWrap : tableWraps) {
    String id = tableWrap.getId();                      // "tbl1"
    String label = tableWrap.getLabel().getValue();    // "Table 1"

    Caption caption = tableWrap.getCaption();
    String title = caption.getTitle().getContent();    // "Patient Demographics"

    System.out.printf("Table: %s - %s%n", label, title);

    // Access table structure
    Table table = tableWrap.getTable();
    if (table != null) {
        // Table attributes
        String frame = table.getFrame();    // "hsides"
        String rules = table.getRules();    // "groups"

        // Table head
        Thead thead = table.getThead();
        if (thead != null) {
            List<Tr> headerRows = thead.getTrs();
            for (Tr tr : headerRows) {
                List<Th> headers = tr.getThs();
                for (Th th : headers) {
                    String headerText = th.getContent();
                    System.out.println("Header: " + headerText);
                }
            }
        }

        // Table body
        Tbody tbody = table.getTbody();
        if (tbody != null) {
            List<Tr> bodyRows = tbody.getTrs();
            for (Tr tr : bodyRows) {
                List<Td> cells = tr.getTds();
                for (Td td : cells) {
                    String cellContent = td.getContent();
                    System.out.println("Cell: " + cellContent);
                }
            }
        }
    }
}
```

---

### Extract Table Data

```java
public class TableExtractor {

    public List<String[]> extractTableData(TableWrap tableWrap) {
        List<String[]> rows = new ArrayList<>();
        Table table = tableWrap.getTable();

        if (table == null) {
            return rows;
        }

        // Extract header row
        if (table.getThead() != null) {
            for (Tr tr : table.getThead().getTrs()) {
                String[] headerRow = tr.getThs().stream()
                    .map(Th::getContent)
                    .toArray(String[]::new);
                rows.add(headerRow);
            }
        }

        // Extract data rows
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

        System.out.println("Table exported to: " + outputFile);
    }
}
```

---

## Best Practices

### 1. Always Handle TAR.GZ Decompression Errors

```java
// ✅ Good - Handle decompression errors
try {
    List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
} catch (IOException e) {
    logger.error("Archive corrupted: {}", e.getMessage());
    downloadArchive(tarGzFile);  // Re-download
}

// ❌ Bad - Assume decompression succeeds
List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
```

---

### 2. Check for Sub-Articles

```java
// ✅ Good - Process main article and sub-articles
JatsArticle article = parser.parseFile(xmlFile);
database.save(article);

if (article.getSubArticles() != null) {
    for (SubArticle subArticle : article.getSubArticles()) {
        database.saveSubArticle(subArticle);
    }
}

// ❌ Bad - Only process main article
database.save(article);  // Sub-articles ignored
```

---

### 3. Handle Front vs Front-Stub

```java
// ✅ Good - Check both front and front-stub
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

// ❌ Bad - Assume only front exists
return subArticle.getFront().getArticleMeta()...  // NullPointerException
```

---

### 4. Extract Figures and Tables

```java
// ✅ Good - Extract all floats
FloatsGroup floatsGroup = article.getFloatsGroup();
if (floatsGroup != null) {
    extractFigures(floatsGroup.getFigs());
    extractTables(floatsGroup.getTableWraps());
    extractSupplements(floatsGroup.getSupplementaryMaterials());
}

// ❌ Bad - Ignore floats-group
// Figures and tables will be missed
```

---

### 5. Validate XLink Attributes

```java
// ✅ Good - Validate xlink:href
Graphic graphic = fig.getGraphics().get(0);
String href = graphic.getXlinkHref();

if (href != null && !href.trim().isEmpty()) {
    // Check if file exists
    Path imagePath = Paths.get(baseDir, href);
    if (Files.exists(imagePath)) {
        processImage(imagePath);
    } else {
        logger.warn("Image not found: {}", href);
    }
}

// ❌ Bad - Assume xlink:href is valid
Path imagePath = Paths.get(graphic.getXlinkHref());  // NullPointerException
```

---

## Next Steps

- [USAGE.md](USAGE.md) - API usage guide
- [STREAMING.md](STREAMING.md) - Large file processing
- [VALIDATION.md](VALIDATION.md) - Validation and error handling
- [PUBMED-SPECIFICS.md](PUBMED-SPECIFICS.md) - PubMed special cases

---

**Documentation Version:** 1.0
**Last Updated:** 2026-01-12
**Parser Version:** 1.0.0-SNAPSHOT
