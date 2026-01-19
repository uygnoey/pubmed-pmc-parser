# PubMed & PMC XML Parser

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.org/projects/jdk/21/)
[![Gradle](https://img.shields.io/badge/Gradle-8.5-green.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**완전한 PubMed 및 PMC XML 파서 - 대용량 파일 스트리밍 파싱 지원**

PubMed Baseline/Update 파일과 PMC Open Access XML을 고성능으로 파싱하는 Java 라이브러리입니다.
DTD 기준으로 **모든 요소와 속성**을 빠짐없이 파싱하며, 메모리 효율적인 스트리밍 방식을 지원합니다.

**한국어** | [English](README.md)

## 📋 목차

- [주요 특징](#-주요-특징)
- [지원 형식](#-지원-형식)
- [성능](#-성능)
- [설치](#-설치)
- [빠른 시작](#-빠른-시작)
- [사용 예제](#-사용-예제)
- [테스트 결과](#-테스트-결과)
- [프로젝트 구조](#-프로젝트-구조)
- [기여](#-기여)
- [라이선스](#-라이선스)

## ✨ 주요 특징

- ✅ **완전한 DTD 지원**: PubMed DTD 250101 및 JATS 1.4 모든 요소 파싱
- ⚡ **고성능**: PubMed 20K+ articles/sec, PMC 1.6K+ articles/sec
- 💾 **메모리 효율**: StAX 기반 스트리밍 파싱 (O(1) 메모리)
- 📦 **GZip 직접 처리**: 압축 해제 없이 .gz 파일 직접 파싱
- 🗜️ **tar.gz 아카이브 지원**: PMC tar.gz 패키지 직접 처리
- 🔒 **무결성 검증**: MD5 (PubMed) / SHA-256 (PMC) 체크섬 검증
- 🎯 **타입 안전성**: 강타입 모델, Map<String, Object> 금지
- 📚 **완전한 문서화**: 한글/영어 이중 JavaDoc

## 🗂️ 지원 형식

### PubMed

- **DTD 버전**: 250101 (2025년 1월 1일)
- **DTD 출처**: https://dtd.nlm.nih.gov/ncbi/pubmed/out/pubmed_250101.dtd
- **파일 형식**: XML, XML.gz
- **데이터 출처**:
  - Baseline: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/
  - Update: https://ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/

**지원 요소:**
- MedlineCitation (5개 속성 모두)
- Article (Journal, Title, Abstract, Authors, PublicationType)
- MeshHeadingList (Descriptors + Qualifiers)
- ChemicalList, KeywordList, GrantList
- ReferenceList (재귀적 중첩 구조)
- CommentsCorrections (7가지 타입)
- PubmedBookArticle

### PMC (PubMed Central)

- **표준**: JATS 1.4 (Journal Article Tag Suite)
- **DTD 출처**: https://jats.nlm.nih.gov/archiving/1.4/
- **파일 형식**: XML, tar.gz
- **데이터 출처**: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/

**지원 요소:**
- Front (ArticleMeta, TitleGroup, ContribGroup, Abstract)
- Body (Sections, Paragraphs, 중첩 구조)
- Back (RefList, Acknowledgments, Glossary)
- FloatsGroup (Figure, Table, Graphic)
- SubArticle (재귀 파싱)
- ElementCitation, MixedCitation

## ⚡ 성능

**실제 파일 테스트 결과** (2026-01-12):

| 파서 | 논문 수 | 파일 크기 | 처리 시간 | 처리 속도 | 메모리 |
|------|---------|----------|----------|----------|--------|
| PubMed | 30,000 | 19MB | 1.64s | **18,282/s** | 58MB |
| PMC | 3,028 | 43MB | 1.83s | **1,651/s** | - |

**목표 대비:**
- ✅ PubMed: 목표 1,000/s → **2,077% 달성**
- ✅ PMC: 목표 100/s → **1,651% 달성**
- ✅ 메모리: 목표 <500MB → **11.7% 사용**

## 🚀 설치

### Gradle

```gradle
dependencies {
    implementation 'com.brillianttiger.bio:pubmed-pmc-parser:1.0.0-SNAPSHOT'
}
```

### Maven

```xml
<dependency>
    <groupId>com.brillianttiger.bio</groupId>
    <artifactId>pubmed-pmc-parser</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 소스에서 직접 빌드

```bash
git clone https://github.com/yourusername/pubmed-pmc-parser.git
cd pubmed-pmc-parser
./gradlew build
```

## 🏃 빠른 시작

### 1. PubMed 파일 파싱

```java
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import com.brillianttiger.bio.parser.pubmed.model.*;
import java.nio.file.Paths;

public class QuickStart {
    public static void main(String[] args) throws Exception {
        // 파서 생성
        PubmedXmlParser parser = new PubmedXmlParser();

        // 파일 파싱 (GZip 자동 처리)
        PubmedArticleSet articleSet = parser.parseFile(
            Paths.get("pubmed25n0001.xml.gz")
        );

        // 데이터 사용
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

### 2. PMC 파일 파싱

```java
import com.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import com.brillianttiger.bio.parser.pmc.model.*;
import java.nio.file.Paths;

public class PmcQuickStart {
    public static void main(String[] args) throws Exception {
        PmcXmlParser parser = new PmcXmlParser();

        // 단일 XML 파일 파싱
        JatsArticle article = parser.parseFile(Paths.get("pmc-article.xml"));

        // 데이터 접근
        Front front = article.getFront();
        ArticleMeta meta = front.getArticleMeta();
        TitleGroup titleGroup = meta.getTitleGroup();
        System.out.println("Title: " + titleGroup.getArticleTitle().getContent());

        // tar.gz 아카이브 파싱 (여러 XML 포함)
        List<JatsArticle> articles = parser.parseTarGz(
            Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.tar.gz")
        );

        System.out.println("Parsed " + articles.size() + " articles");
    }
}
```

## 📖 사용 예제

### MD5 체크섬 검증

```java
import com.brillianttiger.bio.parser.common.util.Md5Verifier;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Md5Example {
    public static void main(String[] args) throws Exception {
        Path xmlFile = Paths.get("pubmed25n0001.xml.gz");

        // MD5 검증 (pubmed25n0001.xml.gz.md5 파일 필요)
        boolean isValid = Md5Verifier.verifyPubmedFile(xmlFile);

        if (isValid) {
            System.out.println("✅ MD5 checksum verified");
        } else {
            System.err.println("❌ MD5 checksum mismatch!");
        }

        // 직접 MD5 계산
        String md5Hash = Md5Verifier.calculateMd5(xmlFile);
        System.out.println("MD5: " + md5Hash);
    }
}
```

### 스트리밍 파싱 (대용량 파일 최적화)

```java
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import com.brillianttiger.bio.parser.pubmed.model.PubmedArticle;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamingExample {
    public static void main(String[] args) throws Exception {
        PubmedXmlParser parser = new PubmedXmlParser();
        AtomicInteger count = new AtomicInteger(0);

        // Consumer 콜백으로 스트리밍 파싱
        parser.parseStream(Paths.get("pubmed25n0001.xml.gz"), article -> {
            int num = count.incrementAndGet();

            // 각 article 처리
            String pmid = article.getMedlineCitation().getPmid().getValue();
            System.out.printf("Processing article #%d: PMID=%s%n", num, pmid);

            // 데이터베이스 저장, 색인 생성 등
            saveToDatabase(article);

            // 진행 상황 출력
            if (num % 1000 == 0) {
                System.out.println("Processed " + num + " articles...");
            }
        });

        System.out.println("Total articles: " + count.get());
    }

    private static void saveToDatabase(PubmedArticle article) {
        // TODO: 데이터베이스 저장 로직
    }
}
```

### PMC tar.gz 스트리밍 파싱

```java
import com.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import com.brillianttiger.bio.parser.pmc.model.JatsArticle;
import java.nio.file.Paths;

public class PmcStreamingExample {
    public static void main(String[] args) throws Exception {
        PmcXmlParser parser = new PmcXmlParser();

        // tar.gz 아카이브를 스트리밍으로 처리 (압축 해제 없음)
        long articleCount = parser.parseStream(
            Paths.get("pmc_oa_comm_xml.PMC000xxxxxx.baseline.tar.gz"),
            article -> {
                // 각 article 처리
                Front front = article.getFront();
                if (front != null && front.getArticleMeta() != null) {
                    TitleGroup titleGroup = front.getArticleMeta().getTitleGroup();
                    if (titleGroup != null) {
                        String title = titleGroup.getArticleTitle().getContent();
                        System.out.println("Title: " + title);
                    }
                }

                // 데이터 저장
                processArticle(article);
            }
        );

        System.out.println("Total PMC articles: " + articleCount);
    }

    private static void processArticle(JatsArticle article) {
        // TODO: article 처리 로직
    }
}
```

### 배치 처리 예제

```java
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import com.brillianttiger.bio.parser.common.util.Md5Verifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BatchProcessingExample {
    public static void main(String[] args) throws Exception {
        PubmedXmlParser parser = new PubmedXmlParser();

        // baseline 디렉토리의 모든 .gz 파일 찾기
        Path baselineDir = Paths.get("test-data/pubmed/baseline");
        List<Path> gzFiles = Files.list(baselineDir)
            .filter(p -> p.toString().endsWith(".xml.gz"))
            .sorted()
            .collect(Collectors.toList());

        int totalArticles = 0;
        long startTime = System.currentTimeMillis();

        // 각 파일 순차 처리
        for (Path file : gzFiles) {
            System.out.println("Processing: " + file.getFileName());

            // MD5 검증
            if (!Md5Verifier.verifyPubmedFile(file)) {
                System.err.println("⚠️  MD5 mismatch: " + file.getFileName());
                continue;
            }

            // 스트리밍 파싱
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
        // TODO: article 처리 (DB 저장, 색인 생성 등)
    }
}
```

## 🧪 테스트 결과

프로젝트는 실제 PubMed 및 PMC 파일로 완전히 검증되었습니다.

### Unit Tests

- **PubMed**: 22개 테스트 (100% PASSED)
- **PMC**: 25개 테스트 (100% PASSED)

### Integration Tests

| 테스트 | 파일 | 논문 수 | 결과 |
|--------|------|---------|------|
| PubMed Baseline | pubmed25n0001.xml.gz | 30,000 | ✅ 100% |
| PMC oa_comm | PMC000xxxxxx.baseline.tar.gz | 3,028 | ✅ 100% |

### Performance Tests

- ✅ 30K+ articles 스트리밍 성능: 20,776/s
- ✅ 메모리 사용량: 58.3MB (30K articles)
- ✅ GZip vs Non-GZip 비교
- ✅ 처리 시나리오별 throughput 측정

**테스트 실행:**

```bash
# 모든 테스트 실행
./gradlew test

# 통합 테스트만 실행
./gradlew test --tests "*IntegrationTest"

# 성능 테스트만 실행
./gradlew test --tests "*PerformanceTest"
```

**자세한 테스트 결과:**
- [PubMed Integration Test](claudedocs/integration-test-results/baseline-pubmed25n0001-integration-test-2026-01-12.md)
- [PMC Integration Test](claudedocs/integration-test-results/pmc-package-integration-test-2026-01-12.md)
- [Complete Test Report](claudedocs/complete-integration-test-report-2026-01-12.md)

## 📁 프로젝트 구조

```
pubmed-pmc-parser/
├── src/
│   ├── main/java/com/brillianttiger/bio/parser/
│   │   ├── pubmed/
│   │   │   ├── model/           # PubMed 도메인 모델 (41+ 클래스)
│   │   │   ├── parser/          # PubMed XML 파서
│   │   │   └── PubmedParser.java
│   │   ├── pmc/
│   │   │   ├── model/           # PMC JATS 모델 (200+ 클래스)
│   │   │   ├── parser/          # PMC XML 파서
│   │   │   └── PmcParser.java
│   │   └── common/
│   │       ├── model/           # 공통 모델
│   │       └── util/            # 유틸리티 (MD5, SHA-256)
│   └── test/
│       ├── java/                # 테스트 코드
│       └── resources/           # 테스트 XML 파일
├── test-data/
│   ├── pubmed/                  # PubMed 실제 파일
│   └── pmc/                     # PMC 실제 파일
├── claudedocs/                  # 테스트 리포트 & 문서
├── build.gradle
├── CLAUDE.md                    # 프로젝트 가이드
├── README.md                    # 영어 버전
└── README.ko.md                 # 이 파일 (한국어)
```

## 🔧 개발 환경

- **Java**: 21+
- **Gradle**: 8.5
- **주요 의존성**:
  - Lombok 1.18.36
  - Apache Commons Compress 1.26.0
  - SLF4J + Logback

## 📚 참고 자료

### PubMed
- [PubMed DTD Documentation](https://dtd.nlm.nih.gov/ncbi/pubmed/doc/out/250101/index.html)
- [PubMed FTP Site](https://ftp.ncbi.nlm.nih.gov/pubmed/)
- [NLM XML Resources](https://www.nlm.nih.gov/databases/dtd/)

### PMC
- [JATS Tag Library](https://jats.nlm.nih.gov/archiving/tag-library/1.3/)
- [PMC FTP Site](https://ftp.ncbi.nlm.nih.gov/pub/pmc/)
- [NISO JATS Standard](https://www.niso.org/standards-committees/jats)

## 🤝 기여

기여는 언제나 환영합니다!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### 개발 가이드

```bash
# 프로젝트 클론
git clone https://github.com/yourusername/pubmed-pmc-parser.git
cd pubmed-pmc-parser

# 빌드
./gradlew build

# 테스트
./gradlew test

# Fat JAR 생성
./gradlew fatJar

# 코드 스타일 (Lombok 적용)
./gradlew generateEffectiveLombokConfig
```

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다 - 자세한 내용은 [LICENSE](LICENSE) 파일을 참고하세요.

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

## 🙏 감사의 말

- **NLM (National Library of Medicine)**: PubMed 및 PMC 데이터 제공
- **NISO**: JATS 표준 개발 및 유지보수

## 📧 연락처

- **프로젝트 홈**: https://github.com/yourusername/pubmed-pmc-parser
- **이슈 트래커**: https://github.com/yourusername/pubmed-pmc-parser/issues

---

**생명의학 연구 커뮤니티를 위해 ❤️로 만들었습니다**
