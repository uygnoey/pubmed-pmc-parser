# PubMed Special Cases Guide

[**한국어**](PUBMED-SPECIFICS.ko.md) | **English**

Complete guide for handling PubMed-specific special cases and edge cases.

---

## Table of Contents

- [MedlineDate Handling](#medlinedate-handling)
- [DeleteCitation Processing](#deletecitation-processing)
- [PubmedBookArticle vs PubmedArticle](#pubmedbookarticle-vs-pubmedarticle)
- [CommentsCorrections Types](#commentscorrections-types)
- [Special Date Formats](#special-date-formats)
- [PMID Edge Cases](#pmid-edge-cases)

---

## MedlineDate Handling

### What is MedlineDate?

MedlineDate is a **non-standard date string** used when structured date elements (Year, Month, Day) cannot represent the publication date.

**Common formats:**
- Seasonal dates: `"2024 Spring"`, `"2024 Winter-Spring"`
- Month ranges: `"2024 Jan-Feb"`, `"2024 Nov-Dec"`
- Quarterly dates: `"2024 Q1"`, `"2024 1st Quarter"`
- Year ranges: `"2023-2024"`
- Partial dates: `"2024 Jan 15-Feb 20"`

---

### When MedlineDate Appears

MedlineDate appears when `<PubDate>` has no structured date:

```xml
<!-- Standard structured date -->
<PubDate>
    <Year>2024</Year>
    <Month>Jan</Month>
    <Day>15</Day>
</PubDate>

<!-- Non-standard date → MedlineDate -->
<PubDate>
    <MedlineDate>2024 Spring</MedlineDate>
</PubDate>
```

---

### Parsing MedlineDate

#### Basic Parsing

```java
PubmedXmlParser parser = new PubmedXmlParser();
Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

parser.parseStream(xmlFile, article -> {
    PubDate pubDate = article.getMedlineCitation()
                             .getArticle()
                             .getJournal()
                             .getJournalIssue()
                             .getPubDate();

    // Check if MedlineDate exists
    if (pubDate.getMedlineDate() != null) {
        String dateStr = pubDate.getMedlineDate().getValue();
        System.out.println("Non-standard date: " + dateStr);

        // Handle accordingly
        handleMedlineDate(dateStr);
    } else {
        // Standard date fields
        String year = pubDate.getYear();
        String month = pubDate.getMonth();
        String day = pubDate.getDay();
    }
});
```

---

### MedlineDate Format Examples

#### 1. Seasonal Dates

```java
// Format: "YYYY Season"
MedlineDate medlineDate = pubDate.getMedlineDate();
String value = medlineDate.getValue();

// Examples:
"2024 Spring"          // Spring 2024
"2024 Summer"          // Summer 2024
"2024 Fall"            // Fall 2024
"2024 Winter"          // Winter 2024
"2024 Winter-Spring"   // Winter to Spring 2024
```

**Handling strategy:**
```java
if (value.matches("\\d{4} (Spring|Summer|Fall|Winter).*")) {
    // Extract year
    int year = Integer.parseInt(value.substring(0, 4));

    // Map season to approximate month
    String season = value.substring(5).trim();
    int approximateMonth = switch (season) {
        case "Spring" -> 4;  // April
        case "Summer" -> 7;  // July
        case "Fall", "Autumn" -> 10;  // October
        case "Winter" -> 1;  // January
        default -> 1;
    };

    // Store as approximate date
    LocalDate approximateDate = LocalDate.of(year, approximateMonth, 1);
}
```

---

#### 2. Month Ranges

```java
// Format: "YYYY Mon-Mon"
// Examples:
"2024 Jan-Feb"         // January to February 2024
"2024 Nov-Dec"         // November to December 2024
"2024 Mar-May"         // March to May 2024
```

**Handling strategy:**
```java
if (value.matches("\\d{4} [A-Za-z]{3}-[A-Za-z]{3}")) {
    String[] parts = value.split(" ");
    int year = Integer.parseInt(parts[0]);
    String[] months = parts[1].split("-");

    String startMonth = months[0];  // "Jan"
    String endMonth = months[1];    // "Feb"

    // Store start and end dates
    LocalDate startDate = parseMonthToDate(year, startMonth);
    LocalDate endDate = parseMonthToDate(year, endMonth).plusMonths(1).minusDays(1);
}
```

---

#### 3. Quarterly Dates

```java
// Format: "YYYY Q#" or "YYYY #st Quarter"
// Examples:
"2024 Q1"              // First quarter 2024
"2024 Q2"              // Second quarter 2024
"2024 1st Quarter"     // First quarter 2024
"2024 2nd Qu"          // Second quarter 2024
```

**Handling strategy:**
```java
if (value.matches("\\d{4} (Q\\d|\\d(st|nd|rd|th) Qu.*)")) {
    int year = Integer.parseInt(value.substring(0, 4));

    // Extract quarter number
    int quarter;
    if (value.contains("Q")) {
        quarter = Integer.parseInt(value.replaceAll(".*Q(\\d).*", "$1"));
    } else {
        quarter = Integer.parseInt(value.replaceAll("\\d{4} (\\d).*", "$1"));
    }

    // Map quarter to start month
    int startMonth = (quarter - 1) * 3 + 1;
    LocalDate quarterStart = LocalDate.of(year, startMonth, 1);
}
```

---

#### 4. Year Ranges

```java
// Format: "YYYY-YYYY"
// Examples:
"2023-2024"            // Year 2023 to 2024
"2022-2023"            // Year 2022 to 2023
```

**Handling strategy:**
```java
if (value.matches("\\d{4}-\\d{4}")) {
    String[] years = value.split("-");
    int startYear = Integer.parseInt(years[0]);
    int endYear = Integer.parseInt(years[1]);

    // Use start year or midpoint
    int representativeYear = startYear;  // Or: (startYear + endYear) / 2
}
```

---

### Using DateParser Utility

The parser includes a utility class for MedlineDate parsing:

```java
import com.brillianttiger.bio.parser.common.util.DateParser;
import com.brillianttiger.bio.parser.common.model.DateComponents;

// Parse MedlineDate
String medlineDateStr = "2024 Jan-Feb";
DateParser.ParsedDate parsed = DateParser.parseMedlineDate(medlineDateStr);

// Access parsed components
String year = parsed.getYear();        // "2024"
String month = parsed.getMonth();      // "Jan"
String endMonth = parsed.getEndMonth(); // "Feb"
boolean isRange = parsed.hasDateRange(); // true

// Convert to DateComponents
DateComponents components = DateParser.parseMedlineDateToComponents(medlineDateStr);
System.out.println(components.getYear());         // "2024"
System.out.println(components.getMonth());        // "01"
System.out.println(components.getMedlineDate());  // "2024 Jan-Feb"
```

---

### Production Pattern

```java
private LocalDate extractPublicationDate(PubDate pubDate) {
    // Try structured date first
    if (pubDate.getYear() != null) {
        int year = Integer.parseInt(pubDate.getYear());
        int month = pubDate.getMonth() != null ? parseMonth(pubDate.getMonth()) : 1;
        int day = pubDate.getDay() != null ? Integer.parseInt(pubDate.getDay()) : 1;
        return LocalDate.of(year, month, day);
    }

    // Fall back to MedlineDate
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

    // No date available
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

## DeleteCitation Processing

### What is DeleteCitation?

DeleteCitation appears in **PubMed update files** and contains a list of PMIDs that have been deleted from the database.

**Why articles get deleted:**
- Duplicate entries
- Data quality issues
- Copyright violations
- Retracted publications
- Indexing errors

---

### File Structure

Update files (`pubmed25n1275.xml.gz`, etc.) can contain:
- `<PubmedArticle>` - New or updated articles
- `<PubmedBookArticle>` - New or updated book articles
- `<DeleteCitation>` - Deleted PMIDs

```xml
<PubmedArticleSet>
    <PubmedArticle>
        <!-- Updated article data -->
    </PubmedArticle>

    <DeleteCitation>
        <PMID Version="1">12345678</PMID>
        <PMID Version="1">87654321</PMID>
        <PMID Version="1">11223344</PMID>
    </DeleteCitation>
</PubmedArticleSet>
```

---

### Extracting DeleteCitation

#### Method 1: Extract Only DeleteCitation

Use `extractDeleteCitation()` to get only the deletion list:

```java
PubmedXmlParser parser = new PubmedXmlParser();
Path updateFile = Paths.get("pubmed25n1275.xml.gz");

// Extract deleted PMIDs
DeleteCitation deleteCitation = parser.extractDeleteCitation(updateFile);

if (deleteCitation != null) {
    List<PMID> deletedPmids = deleteCitation.getPmids();
    System.out.println("Deleted PMIDs: " + deletedPmids.size());

    // Delete from database
    for (PMID pmid : deletedPmids) {
        String pmidValue = pmid.getValue();
        database.delete(pmidValue);
        logger.info("Deleted PMID: {}", pmidValue);
    }
}
```

---

#### Method 2: Parse Complete File

Use `parseFile()` to get both articles and deletions:

```java
Path updateFile = Paths.get("pubmed25n1275.xml.gz");
PubmedArticleSet articleSet = parser.parseFile(updateFile);

// Process new/updated articles
List<PubmedArticle> articles = articleSet.getPubmedArticles();
for (PubmedArticle article : articles) {
    String pmid = article.getMedlineCitation().getPmid().getValue();
    database.upsert(article);  // Insert or update
}

// Process deletions
DeleteCitation deleteCitation = articleSet.getDeleteCitation();
if (deleteCitation != null) {
    for (PMID pmid : deleteCitation.getPmids()) {
        database.delete(pmid.getValue());
    }
}
```

---

### Production Workflow

#### Daily Update Processing

```java
public void processUpdateFile(Path updateFile) throws Exception {
    System.out.println("Processing update file: " + updateFile);

    // 1. Verify file integrity
    boolean md5Valid = Md5Verifier.verifyPubmedFile(updateFile);
    if (!md5Valid) {
        throw new IOException("MD5 verification failed: " + updateFile);
    }

    // 2. Parse update file
    PubmedXmlParser parser = new PubmedXmlParser();
    PubmedArticleSet articleSet = parser.parseFile(updateFile);

    // 3. Process updates
    int updateCount = 0;
    List<PubmedArticle> articles = articleSet.getPubmedArticles();

    for (PubmedArticle article : articles) {
        String pmid = article.getMedlineCitation().getPmid().getValue();

        // Upsert: Insert if new, update if exists
        database.upsert(article);
        updateCount++;

        if (updateCount % 100 == 0) {
            System.out.println("Processed updates: " + updateCount);
        }
    }

    // 4. Process deletions
    DeleteCitation deleteCitation = articleSet.getDeleteCitation();
    int deleteCount = 0;

    if (deleteCitation != null) {
        for (PMID pmid : deleteCitation.getPmids()) {
            String pmidValue = pmid.getValue();

            // Delete from database
            database.delete(pmidValue);
            deleteCount++;

            // Log deletion
            logger.info("Deleted PMID: {}", pmidValue);
        }
    }

    // 5. Summary
    System.out.printf("Update complete: %d updated, %d deleted%n",
                      updateCount, deleteCount);
}
```

---

#### Streaming with DeleteCitation

```java
AtomicInteger updateCount = new AtomicInteger(0);
AtomicInteger deleteCount = new AtomicInteger(0);

// Stream articles
parser.parseStream(updateFile, article -> {
    database.upsert(article);
    updateCount.incrementAndGet();
});

// Extract deletions separately
DeleteCitation deleteCitation = parser.extractDeleteCitation(updateFile);
if (deleteCitation != null) {
    for (PMID pmid : deleteCitation.getPmids()) {
        database.delete(pmid.getValue());
        deleteCount.incrementAndGet();
    }
}

System.out.printf("Updates: %d, Deletions: %d%n",
                  updateCount.get(), deleteCount.get());
```

---

### Transaction Safety

```java
public void processUpdateFileTransactional(Path updateFile) throws Exception {
    try {
        database.beginTransaction();

        // Process updates
        PubmedArticleSet articleSet = parser.parseFile(updateFile);

        // Upsert articles
        for (PubmedArticle article : articleSet.getPubmedArticles()) {
            database.upsert(article);
        }

        // Delete articles
        DeleteCitation deleteCitation = articleSet.getDeleteCitation();
        if (deleteCitation != null) {
            for (PMID pmid : deleteCitation.getPmids()) {
                database.delete(pmid.getValue());
            }
        }

        // Commit transaction
        database.commit();
        System.out.println("✅ Update transaction committed");

    } catch (Exception e) {
        database.rollback();
        logger.error("❌ Update transaction failed: {}", e.getMessage());
        throw e;
    }
}
```

---

## PubmedBookArticle vs PubmedArticle

### Differences

PubMed contains two types of content:

| Feature | PubmedArticle | PubmedBookArticle |
|---------|---------------|-------------------|
| Root Element | `<MedlineCitation>` | `<BookDocument>` |
| Content Type | Journal articles | Book chapters, books |
| Metadata | `<Article>` | `<Book>` |
| Volume/Issue | Yes | No (uses sections) |
| ISBN | No | Yes |
| Authors | AuthorList | AuthorList (similar) |

---

### Parsing PubmedBookArticle

#### Detecting Type

```java
parser.parseStreamAll(xmlFile,
    article -> {
        // Handle PubmedArticle
        String pmid = article.getMedlineCitation().getPmid().getValue();
        String title = article.getMedlineCitation().getArticle().getArticleTitle().getValue();
        System.out.println("Article PMID " + pmid + ": " + title);
    },
    bookArticle -> {
        // Handle PubmedBookArticle
        String pmid = bookArticle.getBookDocument().getPmid().getValue();
        // Book-specific processing
        System.out.println("Book PMID " + pmid);
    }
);
```

---

#### PubmedBookArticle Structure

```java
PubmedBookArticle bookArticle = // parsed

// Access BookDocument
BookDocument bookDoc = bookArticle.getBookDocument();

// PMID
String pmid = bookDoc.getPmid().getValue();

// Book metadata
Book book = bookDoc.getBook();
if (book != null) {
    // Title
    BookTitle bookTitle = book.getBookTitle();
    String title = bookTitle != null ? bookTitle.getValue() : null;

    // ISBN
    List<ISBN> isbns = book.getIsbns();
    for (ISBN isbn : isbns) {
        System.out.println("ISBN: " + isbn.getValue());
    }

    // Authors
    AuthorList authorList = book.getAuthorList();
    if (authorList != null) {
        for (Author author : authorList.getAuthors()) {
            // Similar to PubmedArticle author handling
        }
    }
}
```

---

### Unified Processing

```java
public void processAllContent(Path xmlFile) throws Exception {
    AtomicInteger articleCount = new AtomicInteger(0);
    AtomicInteger bookCount = new AtomicInteger(0);

    parser.parseStreamAll(xmlFile,
        // Article handler
        article -> {
            String pmid = article.getMedlineCitation().getPmid().getValue();
            database.saveArticle(article);
            articleCount.incrementAndGet();
        },

        // Book article handler
        bookArticle -> {
            String pmid = bookArticle.getBookDocument().getPmid().getValue();
            database.saveBookArticle(bookArticle);
            bookCount.incrementAndGet();
        }
    );

    System.out.printf("Processed: %d articles, %d book articles%n",
                      articleCount.get(), bookCount.get());
}
```

---

## CommentsCorrections Types

### Overview

CommentsCorrections represents relationships between articles, such as corrections, retractions, comments, and updates.

**23 different types defined in DTD:**

---

### RefType Enum

```java
public enum RefType {
    ASSOCIATED_DATASET,              // Related dataset
    ASSOCIATED_PUBLICATION,          // Related publication

    COMMENT_IN,                      // Comment published in
    COMMENT_ON,                      // Comments on this article

    CORRECTED_AND_REPUBLISHED_IN,    // Corrected version in
    CORRECTED_AND_REPUBLISHED_FROM,  // Correction of

    ERRATUM_IN,                      // Erratum published in
    ERRATUM_FOR,                     // Erratum for this article

    EXPRESSION_OF_CONCERN_IN,        // Concern expressed in
    EXPRESSION_OF_CONCERN_FOR,       // Concern about this article

    REPUBLISHED_IN,                  // Republished in
    REPUBLISHED_FROM,                // Republication of

    RETRACTED_AND_REPUBLISHED_IN,    // Retracted and republished in
    RETRACTED_AND_REPUBLISHED_FROM,  // Retraction and republication of

    RETRACTIONIN,                    // Retracted in
    RETRACTION_OF,                   // Retraction of this article

    UPDATE_IN,                       // Updated in
    UPDATE_OF,                       // Update of

    SUMMARY_FOR_PATIENTS_IN,         // Patient summary in
    ORIGINAL_REPORT_IN,              // Original report in

    REPRINT_IN,                      // Reprinted in
    REPRINT_OF,                      // Reprint of

    CITES                            // Citation
}
```

---

### Parsing CommentsCorrections

```java
parser.parseStream(xmlFile, article -> {
    MedlineCitation citation = article.getMedlineCitation();
    List<CommentsCorrections> commentsList = citation.getCommentsCorrectionsList();

    if (commentsList != null && !commentsList.isEmpty()) {
        for (CommentsCorrections cc : commentsList) {
            RefType refType = cc.getRefType();
            String refSource = cc.getRefSource().getValue();
            PMID linkedPmid = cc.getPmid();

            System.out.printf("Type: %s, Source: %s", refType, refSource);
            if (linkedPmid != null) {
                System.out.printf(", PMID: %s", linkedPmid.getValue());
            }
            System.out.println();
        }
    }
});
```

---

### Important RefType Patterns

#### 1. Retractions

```java
// Check if article is retracted
boolean isRetracted = commentsList.stream()
    .anyMatch(cc -> cc.getRefType() == RefType.RETRACTION_IN);

if (isRetracted) {
    // Find retraction notice
    CommentsCorrections retraction = commentsList.stream()
        .filter(cc -> cc.getRefType() == RefType.RETRACTION_IN)
        .findFirst()
        .orElse(null);

    if (retraction != null && retraction.getPmid() != null) {
        String retractionPmid = retraction.getPmid().getValue();
        System.out.println("⚠️  Retracted - see PMID: " + retractionPmid);
    }

    // Mark as retracted in database
    database.markAsRetracted(pmid);
}
```

---

#### 2. Corrections/Errata

```java
// Check for corrections
List<CommentsCorrections> corrections = commentsList.stream()
    .filter(cc -> cc.getRefType() == RefType.ERRATUM_IN ||
                  cc.getRefType() == RefType.CORRECTED_AND_REPUBLISHED_IN)
    .toList();

if (!corrections.isEmpty()) {
    System.out.println("📝 Article has corrections:");
    for (CommentsCorrections correction : corrections) {
        System.out.println("  - " + correction.getRefSource().getValue());
        if (correction.getPmid() != null) {
            System.out.println("    PMID: " + correction.getPmid().getValue());
        }
    }
}
```

---

#### 3. Updates

```java
// Check for updates
boolean hasUpdate = commentsList.stream()
    .anyMatch(cc -> cc.getRefType() == RefType.UPDATE_IN);

if (hasUpdate) {
    CommentsCorrections update = commentsList.stream()
        .filter(cc -> cc.getRefType() == RefType.UPDATE_IN)
        .findFirst()
        .orElse(null);

    if (update != null && update.getPmid() != null) {
        String updatedPmid = update.getPmid().getValue();
        System.out.println("🔄 Updated version available: PMID " + updatedPmid);

        // Store relationship
        database.recordUpdate(pmid, updatedPmid);
    }
}
```

---

### Production Pattern

```java
public class ArticleQualityChecker {

    public QualityStatus checkArticleQuality(PubmedArticle article) {
        MedlineCitation citation = article.getMedlineCitation();
        List<CommentsCorrections> ccList = citation.getCommentsCorrectionsList();

        if (ccList == null || ccList.isEmpty()) {
            return QualityStatus.NORMAL;
        }

        // Check for critical issues
        for (CommentsCorrections cc : ccList) {
            RefType type = cc.getRefType();

            // Retracted - highest priority
            if (type == RefType.RETRACTION_IN ||
                type == RefType.RETRACTION_OF) {
                return QualityStatus.RETRACTED;
            }

            // Expression of concern
            if (type == RefType.EXPRESSION_OF_CONCERN_IN ||
                type == RefType.EXPRESSION_OF_CONCERN_FOR) {
                return QualityStatus.CONCERN;
            }

            // Has correction
            if (type == RefType.ERRATUM_FOR ||
                type == RefType.CORRECTED_AND_REPUBLISHED_FROM) {
                return QualityStatus.CORRECTED;
            }
        }

        return QualityStatus.NORMAL;
    }

    public enum QualityStatus {
        NORMAL,      // No issues
        CORRECTED,   // Has corrections
        CONCERN,     // Expression of concern
        RETRACTED    // Retracted article
    }
}
```

---

## Special Date Formats

### Date Element Combinations

PubMed uses flexible date representation:

```java
// Complete date
<PubDate>
    <Year>2024</Year>
    <Month>Jan</Month>
    <Day>15</Day>
</PubDate>

// Year and month only
<PubDate>
    <Year>2024</Year>
    <Month>01</Month>
</PubDate>

// Year only
<PubDate>
    <Year>2024</Year>
</PubDate>

// Non-standard date
<PubDate>
    <MedlineDate>2024 Spring</MedlineDate>
</PubDate>
```

---

### Month Format Variations

Month can be:
- Numeric: `"01"`, `"1"`, `"12"`
- Short name: `"Jan"`, `"Feb"`, `"Mar"`
- Full name: `"January"`, `"February"`

```java
private int parseMonth(String month) {
    if (month == null) return 1;

    // Try numeric first
    try {
        int m = Integer.parseInt(month);
        return (m >= 1 && m <= 12) ? m : 1;
    } catch (NumberFormatException e) {
        // Not numeric, try name
    }

    // Month name mapping
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

### Handling Partial Dates

```java
public LocalDate parsePubDate(PubDate pubDate) {
    // Priority 1: Structured date
    if (pubDate.getYear() != null) {
        int year = Integer.parseInt(pubDate.getYear());

        // Has month?
        if (pubDate.getMonth() != null) {
            int month = parseMonth(pubDate.getMonth());

            // Has day?
            if (pubDate.getDay() != null) {
                int day = Integer.parseInt(pubDate.getDay());
                return LocalDate.of(year, month, day);
            }

            // Month only - use first day
            return LocalDate.of(year, month, 1);
        }

        // Year only - use January 1st
        return LocalDate.of(year, 1, 1);
    }

    // Priority 2: MedlineDate
    if (pubDate.getMedlineDate() != null) {
        return parseMedlineDate(pubDate.getMedlineDate().getValue());
    }

    // No date available
    return null;
}
```

---

## PMID Edge Cases

### PMID Format

**Standard format:**
- Numeric only: `12345678`
- 8 digits typical, but can vary
- Version attribute: `<PMID Version="1">12345678</PMID>`

---

### Handling Missing PMID

```java
parser.parseStream(xmlFile, article -> {
    MedlineCitation citation = article.getMedlineCitation();

    // Defensive check
    if (citation == null || citation.getPmid() == null) {
        logger.warn("Article missing MedlineCitation or PMID - skipping");
        return;
    }

    PMID pmidObj = citation.getPmid();
    String pmid = pmidObj.getValue();

    if (pmid == null || pmid.trim().isEmpty()) {
        logger.warn("PMID value is null or empty - skipping");
        return;
    }

    // Validate PMID format
    if (!pmid.matches("\\d+")) {
        logger.warn("Invalid PMID format: {} - skipping", pmid);
        return;
    }

    // Process article
    database.save(article);
});
```

---

### PMID Version

```java
PMID pmidObj = citation.getPmid();
String pmid = pmidObj.getValue();        // "12345678"
String version = pmidObj.getVersion();    // "1"

// Most PMIDs have Version="1"
// Version tracking is rarely needed for most use cases
```

---

## Best Practices

### 1. Always Check for MedlineDate

```java
// ✅ Good - Check both date types
PubDate pubDate = article.getPubDate();
LocalDate date = null;

if (pubDate.getYear() != null) {
    date = parseStructuredDate(pubDate);
} else if (pubDate.getMedlineDate() != null) {
    date = parseMedlineDate(pubDate.getMedlineDate().getValue());
}

// ❌ Bad - Assume structured date exists
int year = Integer.parseInt(pubDate.getYear()); // NullPointerException
```

---

### 2. Process DeleteCitation in Update Files

```java
// ✅ Good - Handle deletions
PubmedArticleSet articleSet = parser.parseFile(updateFile);

// Process updates
for (PubmedArticle article : articleSet.getPubmedArticles()) {
    database.upsert(article);
}

// Process deletions
if (articleSet.getDeleteCitation() != null) {
    for (PMID pmid : articleSet.getDeleteCitation().getPmids()) {
        database.delete(pmid.getValue());
    }
}

// ❌ Bad - Ignore deletions
// Deleted articles will remain in database
```

---

### 3. Check Article Quality Status

```java
// ✅ Good - Check for retractions/corrections
List<CommentsCorrections> ccList = citation.getCommentsCorrectionsList();
boolean isRetracted = ccList != null && ccList.stream()
    .anyMatch(cc -> cc.getRefType() == RefType.RETRACTION_IN);

if (isRetracted) {
    // Mark as retracted, don't use for analysis
    article.setRetracted(true);
}

// ❌ Bad - Ignore quality issues
// Retracted articles treated as valid
```

---

### 4. Handle Both Article Types

```java
// ✅ Good - Handle both types
parser.parseStreamAll(xmlFile,
    article -> processArticle(article),
    bookArticle -> processBookArticle(bookArticle)
);

// ❌ Bad - Only handle PubmedArticle
parser.parseStream(xmlFile, article -> {
    // PubmedBookArticle will be ignored
});
```

---

### 5. Defensive PMID Validation

```java
// ✅ Good - Validate before use
String pmid = null;
if (citation != null &&
    citation.getPmid() != null &&
    citation.getPmid().getValue() != null) {
    pmid = citation.getPmid().getValue();
    if (pmid.matches("\\d+")) {
        database.save(article);
    }
}

// ❌ Bad - Assume PMID exists
String pmid = citation.getPmid().getValue(); // NullPointerException
```

---

## Next Steps

- [USAGE.md](USAGE.md) - API usage guide
- [STREAMING.md](STREAMING.md) - Large file processing
- [VALIDATION.md](VALIDATION.md) - Validation and error handling
- [PMC-SPECIFICS.md](PMC-SPECIFICS.md) - PMC special cases

---

**Documentation Version:** 1.0
**Last Updated:** 2026-01-12
**Parser Version:** 1.0.0-SNAPSHOT
