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

### 4. 코드 품질 목표 ⚠️ CRITICAL
- **Branch Coverage: 100% 달성 필수** ⭐
- **Line Coverage: 100% 달성 필수** ⭐
- **Instruction Coverage: 100% 달성 필수** ⭐
- 모든 파서 클래스는 100% 커버리지 달성 필수
- 모든 모델 클래스는 100% 커버리지 달성 필수
- 예외: 자동 생성 코드만 제외

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

## Quality Standards ⚠️ MUST ACHIEVE

### Test Coverage Requirements
**모든 커버리지는 100% 달성이 목표입니다. 80%는 중간 목표가 아닙니다.**

#### 현재 진행 상황 (2026-01-19 기준)
```
전체 프로젝트:
├─ Branch Coverage: 79% → 목표 100% (21%p 남음)
├─ Instruction Coverage: 94% → 목표 100% (6%p 남음)
└─ Line Coverage: 94% → 목표 100% (6%p 남음)

패키지별 Branch Coverage:
├─ pmc.model: 100% ✅ (목표 달성!)
├─ pmc.validation: 88% → 목표 100% (12%p 남음)
└─ pmc.parser: 76% → 목표 100% (24%p 남음)
    ├─ PmcXmlParser: 81% → 목표 100% (19%p 남음)
    ├─ ArticleMetaParser: 75% → 목표 100% (25%p 남음)
    ├─ BodyParser: 78% → 목표 100% (22%p 남음)
    ├─ BackParser: 68% → 목표 100% (32%p 남음)
    ├─ FrontParser: 84% → 목표 100% (16%p 남음)
    └─ CommonPmcElementParser: 72% → 목표 100% (28%p 남음)
```

#### 달성 전략
1. **BackParser 우선 처리** (68%, 가장 낮음, 99 missed branches)
2. **CommonPmcElementParser** (72%, 11 missed branches)
3. **ArticleMetaParser** (75%, 196 missed branches - 가장 많음)
4. **BodyParser** (78%, 52 missed branches)
5. **PmcXmlParser** (81%, 85 missed branches)
6. **FrontParser** (84%, 13 missed branches)
7. **Validation 패키지** (88%, 32 missed branches)

#### 테스트 작성 원칙
- 모든 switch-case 분기 테스트
- 모든 if-else 분기 테스트
- 모든 속성 조합 테스트
- 모든 자식 요소 조합 테스트
- 예외 상황 테스트
- Null 체크 테스트
