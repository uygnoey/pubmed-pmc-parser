# 멀티모듈 구조 마이그레이션 완료 보고서

## 📅 작업 일자
2026-01-12

## 🎯 작업 목표
기존 단일 모듈 프로젝트를 멀티모듈 Gradle 프로젝트로 전환

## 📦 최종 프로젝트 구조

```
pubmed-pmc-parser/
├── build.gradle.kts          # 루트 빌드 설정
├── settings.gradle.kts        # 멀티모듈 설정
├── config/
│   └── checkstyle/
│       ├── checkstyle.xml
│       └── suppressions.xml
├── common/                    # 공통 모듈
│   ├── build.gradle.kts
│   └── src/
│       ├── main/java/com/brillianttiger/bio/parser/common/
│       └── test/java/com/brillianttiger/bio/parser/common/
├── pubmed/                    # PubMed 파서 모듈
│   ├── build.gradle.kts
│   └── src/
│       ├── main/java/com/brillianttiger/bio/parser/pubmed/
│       └── test/
│           ├── java/com/brillianttiger/bio/parser/pubmed/
│           └── resources/
└── pmc/                       # PMC 파서 모듈
    ├── build.gradle.kts
    └── src/
        ├── main/java/com/brillianttiger/bio/parser/pmc/
        └── test/
            ├── java/com/brillianttiger/bio/parser/pmc/
            └── resources/
```

## 🔧 주요 변경 사항

### 1. 빌드 시스템 변경

**루트 build.gradle.kts**
- Java 21 toolchain 설정
- `java-library` 플러그인 적용 (API 의존성 지원)
- Lombok 8.11 플러그인
- JaCoCo 테스트 커버리지 (80% 최소 요구사항)
- Checkstyle 10.12.5 (Google Java Style 기반)
- 자동 Javadoc JAR 및 Sources JAR 생성

**서브프로젝트 공통 설정**
```kotlin
subprojects {
    repositories {
        mavenCentral()
    }
    group = "io.brillianttiger.bio"
    version = "1.0.0-SNAPSHOT"

    // Java 21, Javadoc/Sources JAR, JaCoCo, Checkstyle 등 자동 적용
}
```

### 2. 모듈별 역할

#### Common 모듈
**목적**: 공통 유틸리티 및 베이스 클래스
**의존성**:
- commons-io:2.15.1
- commons-lang3:3.14.0
- slf4j-api:2.0.9
- JUnit Jupiter 5.10.1

**패키지**:
- `common.model` - 공통 모델 클래스
- `common.parser` - 공통 파서 베이스 클래스
- `common.util` - 유틸리티 클래스
- `common.validation` - 유효성 검증 클래스

#### PubMed 모듈
**목적**: PubMed XML 파서 (스트리밍 지원)
**의존성**:
- `api(project(":common"))` - Common 모듈 API 의존
- commons-compress:1.26.0 (GZip 처리)
- commons-codec:1.15

**주요 기능**:
- PubMed Baseline/Update 파일 파싱
- 스트리밍 모드 지원 (메모리 효율)
- Fat JAR 생성 가능 (독립 실행)

**생성 JAR**: `pubmed-pmc-parser-pubmed.jar` (426 KB)

#### PMC 모듈
**목적**: PMC (PubMed Central) XML 파서 (JATS 1.4 지원)
**의존성**:
- `api(project(":common"))` - Common 모듈 API 의존
- commons-compress:1.26.0 (TAR.GZ 처리)
- commons-codec:1.15

**주요 기능**:
- JATS 1.4 (ANSI/NISO Z39.96-2024) 표준 완전 지원
- TAR.GZ 아카이브 파싱 지원
- 재귀 구조 처리 (sec, ref-list, sub-article)
- Fat JAR 생성 가능 (독립 실행)

**생성 JAR**: `pubmed-pmc-parser-pmc.jar` (1.2 MB)

### 3. 소스 파일 마이그레이션

**이동 완료**:
- ✅ Common 소스 파일: 28개
- ✅ PubMed 소스 파일: 169개
- ✅ PMC 소스 파일: 362개
- ✅ 테스트 파일 전체
- ✅ 테스트 리소스 전체

**삭제**:
- 기존 `src/` 디렉토리 완전 제거
- 기존 `settings.gradle` 제거 (settings.gradle.kts로 대체)

## 📊 빌드 결과

### 성공적으로 생성된 아티팩트

```
common/build/libs/
├── pubmed-pmc-parser-common.jar              (67 KB)
├── pubmed-pmc-parser-common-javadoc.jar
└── pubmed-pmc-parser-common-sources.jar

pubmed/build/libs/
├── pubmed-pmc-parser-pubmed.jar             (426 KB)
├── pubmed-pmc-parser-pubmed-javadoc.jar
└── pubmed-pmc-parser-pubmed-sources.jar

pmc/build/libs/
├── pubmed-pmc-parser-pmc.jar              (1.2 MB)
├── pubmed-pmc-parser-pmc-javadoc.jar
└── pubmed-pmc-parser-pmc-sources.jar
```

### 빌드 명령어

```bash
# 전체 빌드
./gradlew build

# 모듈별 빌드
./gradlew :common:build
./gradlew :pubmed:build
./gradlew :pmc:build

# Fat JAR 생성 (독립 실행 가능)
./gradlew :pubmed:fatJar    # pubmed-parser-all.jar
./gradlew :pmc:fatJar        # pmc-parser-all.jar

# 테스트 실행
./gradlew test

# JaCoCo 통합 커버리지 리포트
./gradlew jacocoRootReport

# Checkstyle 검사
./gradlew checkstyleMain checkstyleTest

# 모든 태스크 확인
./gradlew tasks

# 프로젝트 구조 확인
./gradlew projects
```

## ⚠️ 알려진 이슈 및 해결 필요 사항

### 1. 테스트 실패 (우선순위: 중)
**문제**: 일부 테스트에서 리소스 파일 경로 오류
**원인**: 멀티모듈 구조로 변경되면서 테스트 리소스 경로가 변경됨
**영향**:
- Common 모듈: 2개 테스트 실패 (XXE 공격 방지 테스트)
- PubMed 모듈: 17개 테스트 실패 (파일 경로 문제)

**해결 방법**:
```java
// Before (단일 모듈)
Path xmlPath = Paths.get("src/test/resources/sample.xml");

// After (멀티모듈)
Path xmlPath = Paths.get("pubmed/src/test/resources/sample.xml");
// 또는
ClassLoader classLoader = getClass().getClassLoader();
Path xmlPath = Paths.get(classLoader.getResource("sample.xml").toURI());
```

### 2. Checkstyle 설정 오류 (우선순위: 중)
**문제**: Checkstyle이 설정 파일을 찾지 못함
```
Unable to create Root Module: config {/Users/.../config/checkstyle/checkstyle.xml}
```

**원인**: 서브프로젝트에서 루트 프로젝트의 config 디렉토리 경로 문제

**현재 설정**:
```kotlin
checkstyle {
    configFile = rootProject.file("config/checkstyle/checkstyle.xml")
}
```

**해결 방법**:
1. 각 모듈에서 상대 경로 확인
2. SuppressionFilter의 경로 수정 필요
3. 또는 config 디렉토리를 각 모듈로 복사

### 3. 임시 회피 방법
현재는 다음 옵션으로 빌드 가능:
```bash
# 테스트와 Checkstyle 제외하고 빌드
./gradlew build -x test -x checkstyleMain -x checkstyleTest
```

## ✅ 완료된 작업

1. ✅ 멀티모듈 디렉토리 구조 생성
2. ✅ settings.gradle.kts 작성
3. ✅ 루트 build.gradle.kts 작성 (공통 설정)
4. ✅ 모듈별 build.gradle.kts 작성
5. ✅ 소스 파일 마이그레이션 (common, pubmed, pmc)
6. ✅ 테스트 파일 마이그레이션
7. ✅ 테스트 리소스 마이그레이션
8. ✅ 기존 src/ 디렉토리 정리
9. ✅ 기존 settings.gradle 제거
10. ✅ 빌드 성공 확인
11. ✅ JAR 파일 생성 확인

## 📋 남은 작업 (Todo)

1. ⏳ 테스트 리소스 경로 수정
   - Common 모듈 XXE 테스트 수정
   - PubMed 모듈 파일 경로 테스트 수정
   - 리소스 로딩을 ClassLoader 기반으로 변경

2. ⏳ Checkstyle 설정 수정
   - 루트 프로젝트 config 경로 문제 해결
   - SuppressionFilter 경로 조정
   - 각 모듈에서 정상 동작 확인

## 🎓 학습 내용

### Gradle 멀티모듈 주의사항

1. **플러그인 적용 순서**
   - 루트에서 `apply false`로 선언
   - 각 모듈에서 명시적으로 `plugins {}` 블록에 적용
   - `java-library` 플러그인이 `api()` 의존성 사용에 필수

2. **repositories 설정**
   - `allprojects {}` 블록에 선언해도 서브프로젝트에서 인식 안 될 수 있음
   - 각 모듈의 build.gradle.kts에 명시적으로 선언하는 것이 안전

3. **의존성 관리**
   - `api()`: 전이 의존성 (API로 노출)
   - `implementation()`: 내부 구현용 (외부에 노출 안 됨)
   - Common 모듈은 `api()`로 의존해야 하위 모듈에서 Common 클래스 사용 가능

4. **settings.gradle vs settings.gradle.kts**
   - 둘 다 있으면 .gradle이 우선됨
   - Kotlin DSL 사용 시 반드시 .gradle 제거 필요

## 🔍 검증 체크리스트

- [x] 프로젝트 구조 확인: `./gradlew projects`
- [x] 빌드 성공: `./gradlew build -x test -x checkstyle`
- [x] JAR 파일 생성 확인
- [x] 모듈 간 의존성 작동 확인 (pubmed/pmc → common)
- [ ] 전체 테스트 통과
- [ ] Checkstyle 검사 통과
- [ ] Fat JAR 실행 테스트
- [ ] JaCoCo 커버리지 리포트 생성

## 📚 참고 자료

- [Gradle Multi-Project Builds](https://docs.gradle.org/current/userguide/multi_project_builds.html)
- [Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html)
- [Gradle Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)
- 프로젝트 CLAUDE.md 및 SKILL.md

## 📝 메모

- 기존 Main.java는 루트 프로젝트에 있었으나 멀티모듈 구조에서는 제거됨
- 각 모듈(pubmed, pmc)은 독립적으로 실행 가능한 Fat JAR 생성 가능
- Common 모듈은 라이브러리로만 사용되며 단독 실행 불가
- Lombok 8.11로 업그레이드하여 Java 21 완벽 지원
