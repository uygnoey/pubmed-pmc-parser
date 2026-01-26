# PubMed & PMC XML Parser

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.org/projects/jdk/21/)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-green.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Complete PubMed and PMC XML Parser with High-Performance Streaming Support**

A high-performance Java library for parsing PubMed Baseline/Update files and PMC Open Access XML.
Supports **complete DTD coverage** with memory-efficient streaming parsing.

[**한국어**](README.ko.md) | **English**

## 📋 Table of Contents

- [Key Features](#-key-features)
- [Supported Formats](#-supported-formats)
- [Performance](#-performance)
- [Installation](#-installation)
- [Quick Start](#-quick-start)
- [Usage Examples](#-usage-examples)
- [Test Results](#-test-results)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Key Features

- ✅ **Complete DTD Support**: Full parsing of PubMed DTD 250101 and JATS 1.4
- ⚡ **High Performance**: PubMed 20K+ articles/sec, PMC 1.6K+ articles/sec
- 💾 **Memory Efficient**: StAX-based streaming with O(1) memory
- 📦 **Direct GZip Processing**: Parse .gz files without decompression
- 🗜️ **tar.gz Archive Support**: Direct processing of PMC tar.gz packages
- 🔒 **Integrity Verification**: MD5 (PubMed) / SHA-256 (PMC) checksum validation
- 🎯 **Type Safety**: Strongly-typed models, no Map<String, Object>
- 📚 **Complete Documentation**: Bilingual JavaDoc (Korean/English)

## 🗂️ Supported Formats

### PubMed

- **DTD Version**: 250101 (January 1, 2025)
- **DTD Source**: https://dtd.nlm.nih.gov/ncbi/pubmed/out/pubmed_250101.dtd
- **File Formats**: XML, XML.gz
- **Data Source**:
  - Baseline: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/
  - Update: https://ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/

**Supported Elements:**
- MedlineCitation (all 5 attributes)
- Article (Journal, Title, Abstract, Authors, PublicationType)
- MeshHeadingList (Descriptors + Qualifiers)
- ChemicalList, KeywordList, GrantList
- ReferenceList (recursive nested structure)
- CommentsCorrections (7 types)
- PubmedBookArticle

### PMC (PubMed Central)

- **Standard**: JATS 1.4 (Journal Article Tag Suite)
- **DTD Source**: https://jats.nlm.nih.gov/archiving/1.4/
- **File Formats**: XML, tar.gz
- **Data Source**: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/

**Supported Elements:**
- Front (ArticleMeta, TitleGroup, ContribGroup, Abstract)
- Body (Sections, Paragraphs, nested structure)
- Back (RefList, Acknowledgments, Glossary)
- FloatsGroup (Figure, Table, Graphic)
- SubArticle (recursive parsing)
- ElementCitation, MixedCitation

## ⚡ Performance

**Real File Test Results** (2026-01-12):

| Parser | Articles | File Size | Time | Throughput | Memory |
|--------|----------|-----------|------|------------|--------|
| PubMed | 30,000 | 19MB | 1.64s | **18,282/s** | 58MB |
| PMC | 3,028 | 43MB | 1.83s | **1,651/s** | - |

**vs. Target:**
- ✅ PubMed: Target 1,000/s → **2,077% achieved**
- ✅ PMC: Target 100/s → **1,651% achieved**
- ✅ Memory: Target <500MB → **11.7% used**

## 🚀 Installation

### Gradle

```gradle
dependencies {
    implementation 'io.brillianttiger.bio:pubmed-pmc-parser:1.0.0-SNAPSHOT'
}
```

### Maven

```xml
<dependency>
    <groupId>io.brillianttiger.bio</groupId>
    <artifactId>pubmed-pmc-parser</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Build from Source

```bash
git clone https://github.com/BrilliantTiger/pubmed-pmc-parser.git
cd pubmed-pmc-parser
./gradlew build
```

## 🏃 Quick Start

### 1. Parse PubMed File

```java
import io.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import io.brillianttiger.bio.parser.pubmed.model.*;
import java.nio.file.Paths;

public class QuickStart {
    public static void main(String[] args) throws Exception {
        // Create parser
        PubmedXmlParser parser = new PubmedXmlParser();

        // Parse file (auto-detects GZip)
        PubmedArticleSet articleSet = parser.parseFile(
            Paths.get("pubmed25n0001.xml.gz")
        );

        // Use data
        for (PubmedArticle article : articleSet.getPubmedArticles()) {
            MedlineCitation citation = article.getMedlineCitation();
            System.out.println("PMID: " + citation.getPmid().getValue());

            Article art = citation.getArticle();
            if (art != null) {
                System.out.println("Title: " + art.getArticleTitle().getValue());
            }
        }
    }
}
```

### 2. Parse PMC File

```java
import io.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import io.brillianttiger.bio.parser.pmc.model.*;
import java.nio.file.Paths;

public class PmcQuickStart {
    public static void main(String[] args) throws Exception {
        PmcXmlParser parser = new PmcXmlParser();

        // Parse single XML file
        JatsArticle article = parser.parseFile(Paths.get("pmc-article.xml"));

        // Access data
        Front front = article.getFront();
        ArticleMeta meta = front.getArticleMeta();
        TitleGroup titleGroup = meta.getTitleGroup();
        System.out.println("Title: " + titleGroup.getArticleTitle().getContent());

        // Parse tar.gz archive (multiple XMLs)
        List<JatsArticle> articles = parser.parseTarGz(
            Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.tar.gz")
        );

        System.out.println("Parsed " + articles.size() + " articles");
    }
}
```

## 📖 Usage Examples

### MD5 Checksum Verification

```java
import io.brillianttiger.bio.parser.common.util.Md5Verifier;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Md5Example {
    public static void main(String[] args) throws Exception {
        Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

        // Verify MD5 (requires pubmed25n0001.xml.gz.md5 file)
        boolean isValid = Md5Verifier.verifyPubmedFile(xmlFile);

        if (isValid) {
            System.out.println("✅ MD5 checksum verified");
        } else {
            System.err.println("❌ MD5 checksum mismatch!");
        }

        // Calculate MD5 directly
        String md5Hash = Md5Verifier.calculateMd5(xmlFile);
        System.out.println("MD5: " + md5Hash);
    }
}
```

### Streaming Parser (Optimized for Large Files)

```java
import io.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import io.brillianttiger.bio.parser.pubmed.model.PubmedArticle;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamingExample {
    public static void main(String[] args) throws Exception {
        PubmedXmlParser parser = new PubmedXmlParser();
        AtomicInteger count = new AtomicInteger(0);

        // Stream parsing with Consumer callback
        parser.parseStream(Paths.get("pubmed25n0001.xml.gz"), article -> {
            int num = count.incrementAndGet();

            // Process each article
            String pmid = article.getMedlineCitation().getPmid().getValue();
            System.out.printf("Processing article #%d: PMID=%s%n", num, pmid);

            // Save to database, create index, etc.
            saveToDatabase(article);

            // Progress reporting
            if (num % 1000 == 0) {
                System.out.println("Processed " + num + " articles...");
            }
        });

        System.out.println("Total articles: " + count.get());
    }

    private static void saveToDatabase(PubmedArticle article) {
        // TODO: Database save logic
    }
}
```

### PMC tar.gz Streaming

```java
import io.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import io.brillianttiger.bio.parser.pmc.model.JatsArticle;
import java.nio.file.Paths;

public class PmcStreamingExample {
    public static void main(String[] args) throws Exception {
        PmcXmlParser parser = new PmcXmlParser();

        // Stream tar.gz archive (no decompression)
        long articleCount = parser.parseStream(
            Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.tar.gz"),
            article -> {
                // Process each article
                Front front = article.getFront();
                if (front != null && front.getArticleMeta() != null) {
                    TitleGroup titleGroup = front.getArticleMeta().getTitleGroup();
                    if (titleGroup != null) {
                        String title = titleGroup.getArticleTitle().getContent();
                        System.out.println("Title: " + title);
                    }
                }

                // Save data
                processArticle(article);
            }
        );

        System.out.println("Total PMC articles: " + articleCount);
    }

    private static void processArticle(JatsArticle article) {
        // TODO: Article processing logic
    }
}
```

### Batch Processing

```java
import io.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import io.brillianttiger.bio.parser.common.util.Md5Verifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BatchProcessingExample {
    public static void main(String[] args) throws Exception {
        PubmedXmlParser parser = new PubmedXmlParser();

        // Find all .gz files in baseline directory
        Path baselineDir = Paths.get("test-data/pubmed/baseline");
        List<Path> gzFiles = Files.list(baselineDir)
            .filter(p -> p.toString().endsWith(".xml.gz"))
            .sorted()
            .collect(Collectors.toList());

        int totalArticles = 0;
        long startTime = System.currentTimeMillis();

        // Process each file sequentially
        for (Path file : gzFiles) {
            System.out.println("Processing: " + file.getFileName());

            // Verify MD5
            if (!Md5Verifier.verifyPubmedFile(file)) {
                System.err.println("⚠️  MD5 mismatch: " + file.getFileName());
                continue;
            }

            // Stream parsing
            AtomicInteger count = new AtomicInteger(0);
            parser.parseStream(file, article -> {
                count.incrementAndGet();
                processArticle(article);
            });

            totalArticles += count.get();
            System.out.printf("  ✅ Processed %,d articles%n", count.get());
        }

        long elapsed = System.currentTimeMillis() - startTime;
        double throughput = totalArticles * 1000.0 / elapsed;

        System.out.println("\n========================================");
        System.out.printf("Total articles: %,d%n", totalArticles);
        System.out.printf("Total time: %.2fs%n", elapsed / 1000.0);
        System.out.printf("Throughput: %,.0f articles/sec%n", throughput);
    }

    private static void processArticle(PubmedArticle article) {
        // TODO: Process article (save to DB, create index, etc.)
    }
}
```

## 🧪 Test Results

The project has been fully validated with real PubMed and PMC files.

### Unit Tests

- **PubMed**: 22 tests (100% PASSED)
- **PMC**: 25 tests (100% PASSED)

### Integration Tests

| Test | File | Articles | Result |
|------|------|----------|--------|
| PubMed Baseline | pubmed25n0001.xml.gz | 30,000 | ✅ 100% |
| PMC oa_comm | PMC000xxxxxx.baseline.tar.gz | 3,028 | ✅ 100% |

### Performance Tests

- ✅ 30K+ articles streaming: 20,776/s
- ✅ Memory usage: 58.3MB (30K articles)
- ✅ GZip vs Non-GZip comparison
- ✅ Throughput by processing scenario

**Run Tests:**

```bash
# Run all tests
./gradlew test

# Integration tests only
./gradlew test --tests "*IntegrationTest"

# Performance tests only
./gradlew test --tests "*PerformanceTest"
```

**Detailed Test Results:**
- [PubMed Integration Test](claudedocs/integration-test-results/baseline-pubmed25n0001-integration-test-2026-01-12.md)
- [PMC Integration Test](claudedocs/integration-test-results/pmc-package-integration-test-2026-01-12.md)
- [Complete Test Report](claudedocs/complete-integration-test-report-2026-01-12.md)

## 📁 Project Structure

```
pubmed-pmc-parser/
├── src/
│   ├── main/java/com/brillianttiger/bio/parser/
│   │   ├── pubmed/
│   │   │   ├── model/           # PubMed domain models (41+ classes)
│   │   │   ├── parser/          # PubMed XML parser
│   │   │   └── PubmedParser.java
│   │   ├── pmc/
│   │   │   ├── model/           # PMC JATS models (200+ classes)
│   │   │   ├── parser/          # PMC XML parser
│   │   │   └── PmcParser.java
│   │   └── common/
│   │       ├── model/           # Common models
│   │       └── util/            # Utilities (MD5, SHA-256)
│   └── test/
│       ├── java/                # Test code
│       └── resources/           # Test XML files
├── test-data/
│   ├── pubmed/                  # Real PubMed files
│   └── pmc/                     # Real PMC files
├── claudedocs/                  # Test reports & documentation
├── build.gradle
├── CLAUDE.md                    # Project guide
├── README.md                    # This file (English)
└── README.ko.md                 # Korean version
```

## 🔧 Development Environment

- **Java**: 21+
- **Gradle**: 8.5
- **Key Dependencies**:
  - Lombok 1.18.36
  - Apache Commons Compress 1.26.0
  - SLF4J + Logback

## 📚 References

### PubMed
- [PubMed DTD Documentation](https://dtd.nlm.nih.gov/ncbi/pubmed/doc/out/250101/index.html)
- [PubMed FTP Site](https://ftp.ncbi.nlm.nih.gov/pubmed/)
- [NLM XML Resources](https://www.nlm.nih.gov/databases/dtd/)

### PMC
- [JATS Tag Library](https://jats.nlm.nih.gov/archiving/tag-library/1.3/)
- [PMC FTP Site](https://ftp.ncbi.nlm.nih.gov/pub/pmc/)
- [NISO JATS Standard](https://www.niso.org/standards-committees/jats)

## 🤝 Contributing

Contributions are always welcome!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guide

```bash
# Clone project
git clone https://github.com/BrilliantTiger/pubmed-pmc-parser.git
cd pubmed-pmc-parser

# Build
./gradlew build

# Test
./gradlew test

# Create Fat JAR
./gradlew fatJar

# Code style (with Lombok)
./gradlew generateEffectiveLombokConfig
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2026 BrilliantTiger Bio

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🙏 Acknowledgments

- **NLM (National Library of Medicine)**: PubMed and PMC data provision
- **NISO**: JATS standard development and maintenance

## 📧 Contact

- **Project Home**: https://github.com/BrilliantTiger/pubmed-pmc-parser
- **Issue Tracker**: https://github.com/BrilliantTiger/pubmed-pmc-parser/issues
