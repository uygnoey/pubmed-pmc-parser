# PubMed & PMC XML Parser

## Project Overview
PubMed와 PMC(PubMed Central) XML 파일을 파싱하는 완전한 Java 라이브러리.
DTD 기준으로 **모든 요소와 속성**을 빠짐없이 파싱해야 함.

## Goals
1. PubMed Baseline/Update XML 파일 완전 파싱
2. PMC Open Access XML 파일 완전 파싱
3. 메모리 효율적인 스트리밍 파싱 지원
4. Spring Batch 통합 지원

## Tech Stack
- Java 17+
- Gradle 8.5
- StAX (Streaming API for XML) - 메모리 효율성
- Lombok - 보일러플레이트 제거
- 선택적: Spring Batch for ETL

## Project Structure
```
pubmed-pmc-parser/
├── src/main/java/com/bioxml/parser/
│   ├── pubmed/
│   │   ├── model/          # PubMed 도메인 모델 (41+ 클래스)
│   │   ├── parser/         # PubMed XML 파서
│   │   └── PubmedParser.java
│   ├── pmc/
│   │   ├── model/          # PMC 도메인 모델
│   │   ├── parser/         # PMC XML 파서  
│   │   └── PmcParser.java
│   └── common/
│       ├── model/          # 공통 모델
│       └── util/           # 유틸리티
├── src/test/java/
├── build.gradle
├── settings.gradle
├── CLAUDE.md
└── SKILL.md
```

## Critical Requirements

### 1. 완전성 (Completeness)
- DTD에 정의된 **모든 요소** 파싱 필수
- DTD에 정의된 **모든 속성** 파싱 필수
- 빠지는 데이터 없어야 함

### 2. 메모리 효율성
- Baseline 파일: ~100MB gzipped, ~1GB uncompressed
- StAX 스트리밍으로 상수 메모리 사용
- Consumer 콜백 패턴 지원

### 3. 보안
- XXE (XML External Entity) 공격 방지
- DTD 처리 비활성화

## Commands
```bash
# Gradle Wrapper 생성 (최초 1회)
gradle wrapper

# Build
./gradlew build

# Test
./gradlew test

# Clean Build
./gradlew clean build

# Fat JAR 생성 (의존성 포함)
./gradlew fatJar

# Run with specific file
java -jar build/libs/pubmed-pmc-parser-1.0.0-SNAPSHOT-all.jar /path/to/pubmed24n0001.xml.gz
```

## References

### PubMed
- DTD: https://dtd.nlm.nih.gov/ncbi/pubmed/out/pubmed_230101.dtd
- DTD Documentation: https://dtd.nlm.nih.gov/ncbi/pubmed/doc/out/230101/index.html
- Baseline FTP: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/
- Update FTP: https://ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/

### PMC
- DTD: https://dtd.nlm.nih.gov/ncbi/pmc/articleset/nlm-articleset-2.0.dtd
- JATS DTD: https://jats.nlm.nih.gov/archiving/tag-library/1.3/
- OA FTP: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/

## Development Notes
- 한국어 주석 OK
- Lombok 사용 (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- 각 모델 클래스에 DTD 주석 포함
- 테스트 코드 필수
