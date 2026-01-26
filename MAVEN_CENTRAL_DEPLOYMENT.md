# Maven Central 배포 가이드

## 📋 목차
1. [사전 준비](#사전-준비)
2. [Sonatype OSSRH 계정 생성](#sonatype-ossrh-계정-생성)
3. [GPG 키 생성 및 배포](#gpg-키-생성-및-배포)
4. [Gradle 설정 수정](#gradle-설정-수정)
5. [배포 실행](#배포-실행)
6. [릴리스 승인](#릴리스-승인)

---

## 1. 사전 준비

### ✅ 이미 완료된 항목
- [x] 프로젝트 품질 (100% 테스트 커버리지)
- [x] 문서화 (README, JavaDoc)
- [x] Maven POM 메타데이터
- [x] 오픈소스 라이선스 (Apache 2.0)

### ⚠️ 필요한 항목
- [ ] Sonatype OSSRH 계정
- [ ] GPG 키페어
- [ ] 도메인 소유권 증명 (또는 GitHub 기반 그룹 ID)
- [ ] Gradle Signing 플러그인 설정

---

## 2. Sonatype OSSRH 계정 생성

### Step 1: Sonatype Jira 계정 생성
1. https://issues.sonatype.org/secure/Signup!default.jspa 접속
2. 계정 생성 (Username, Email, Full Name)
3. 이메일 인증 완료

### Step 2: New Project Ticket 생성
1. https://issues.sonatype.org/secure/CreateIssue.jspa 접속
2. 다음 정보 입력:
   ```
   Project: Community Support - Open Source Project Repository Hosting (OSSRH)
   Issue Type: New Project
   Summary: Request for io.brillianttiger.bio or io.github.yourusername
   Group Id: io.brillianttiger.bio (또는 io.github.yourusername)
   Project URL: https://github.com/brillianttiger/pubmed-pmc-parser
   SCM URL: https://github.com/brillianttiger/pubmed-pmc-parser.git
   ```

### Step 3: 도메인 소유권 증명

**옵션 A: GitHub 기반 그룹 ID 사용 (추천)** ✅
```
Group ID: io.github.yourusername
- 도메인 소유권 증명 불필요
- GitHub 계정만 있으면 OK
- 승인 빠름 (1-2일)
```

**옵션 B: 커스텀 도메인 (brillianttiger.com)**
```
Group ID: io.brillianttiger.bio
- DNS TXT 레코드 추가 필요
- 또는 brillianttiger.com 도메인 소유 증명
- 승인 느림 (수일~수주)
```

### Step 4: 승인 대기
- 보통 1-2 영업일 내 승인
- Jira 티켓에서 "RESOLVED" 상태 확인

---

## 3. GPG 키 생성 및 배포

### Step 1: GPG 설치
```bash
# macOS
brew install gnupg

# Ubuntu/Debian
sudo apt-get install gnupg

# Windows
# https://www.gnupg.org/download/ 에서 Gpg4win 다운로드
```

### Step 2: GPG 키 생성
```bash
# 키 생성
gpg --gen-key

# 입력 정보:
# - Real name: Brilliant Tiger (또는 본인 이름)
# - Email: dev@brillianttiger.com (또는 본인 이메일)
# - Passphrase: 안전한 비밀번호 (기억할 것!)
```

### Step 3: 키 ID 확인
```bash
# 키 목록 확인
gpg --list-keys

# 출력 예시:
# pub   rsa3072 2024-01-26 [SC]
#       ABCD1234EFGH5678IJKL9012MNOP3456QRST7890  <- 이것이 Key ID
# uid           [ultimate] Brilliant Tiger <dev@brillianttiger.com>
```

### Step 4: 공개 키 배포
```bash
# Key ID의 마지막 8자리 사용
gpg --keyserver keyserver.ubuntu.com --send-keys QRST7890
gpg --keyserver keys.openpgp.org --send-keys QRST7890
gpg --keyserver pgp.mit.edu --send-keys QRST7890
```

### Step 5: 개인 키 내보내기 (백업)
```bash
# 안전한 곳에 백업
gpg --export-secret-keys QRST7890 > gpg-secret.key
```

---

## 4. Gradle 설정 수정

### Step 1: gradle.properties 수정

프로젝트 루트에 `gradle.properties` 파일 생성 또는 수정:

```properties
# Maven Central Credentials
signing.keyId=QRST7890
signing.password=YOUR_GPG_PASSPHRASE
signing.secretKeyRingFile=/Users/yourusername/.gnupg/secring.gpg

ossrhUsername=YOUR_SONATYPE_USERNAME
ossrhPassword=YOUR_SONATYPE_PASSWORD

# Version (SNAPSHOT 제거)
version=1.0.0
```

**⚠️ 보안 주의:**
- `gradle.properties`를 `.gitignore`에 추가
- 또는 `~/.gradle/gradle.properties` (홈 디렉토리)에 저장

### Step 2: build.gradle.kts 수정

`build.gradle.kts` 파일 상단에 추가:

```kotlin
plugins {
    id("io.freefair.lombok") version "8.11" apply false
    id("maven-publish")
    id("signing")
}

group = "io.brillianttiger.bio"  // 또는 io.github.yourusername
version = "1.0.0"  // SNAPSHOT 제거
```

각 서브프로젝트에 signing 설정 추가:

```kotlin
subprojects {
    // 기존 설정...

    afterEvaluate {
        // 기존 publishing 설정 유지...

        // Signing 설정 추가
        extensions.findByType<SigningExtension>()?.apply {
            sign(extensions.getByType<PublishingExtension>().publications["maven"])
        }

        // Publishing Repository 추가
        extensions.findByType<PublishingExtension>()?.apply {
            repositories {
                maven {
                    val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

                    credentials {
                        username = findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                        password = findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
                    }
                }
            }
        }
    }
}
```

### Step 3: 플러그인 적용 확인

각 서브모듈의 `build.gradle.kts`에 다음이 있는지 확인:

```kotlin
plugins {
    id("java-library")
    id("maven-publish")
    id("signing")
    id("io.freefair.lombok")
}
```

---

## 5. 배포 실행

### Step 1: 빌드 및 테스트
```bash
./gradlew clean build

# 테스트 통과 확인
./gradlew test
```

### Step 2: 로컬 배포 테스트
```bash
# 로컬 Maven 저장소에 배포 테스트
./gradlew publishToMavenLocal

# 확인
ls ~/.m2/repository/com/brillianttiger/bio/
```

### Step 3: Staging Repository에 배포
```bash
# 모든 아티팩트를 Sonatype에 업로드
./gradlew publish

# 또는 개별 모듈
./gradlew :common:publish
./gradlew :pubmed:publish
./gradlew :pmc:publish
```

---

## 6. 릴리스 승인

### Step 1: Sonatype Nexus Repository Manager 접속
1. https://s01.oss.sonatype.org/ 로그인
2. 좌측 메뉴 "Staging Repositories" 클릭

### Step 2: Staging Repository 찾기
1. 목록에서 `combrillanttiger-XXXX` 또는 `iogithub-XXXX` 찾기
2. 체크박스 선택

### Step 3: Close → Release
1. "Close" 버튼 클릭 (검증 시작)
2. 검증 통과 확인 (5-10분)
3. "Release" 버튼 클릭
4. 확인 대화상자에서 "Confirm"

### Step 4: 배포 완료 확인
- Maven Central 동기화: 10분 ~ 2시간
- 검색 가능: 2시간 ~ 24시간
- 확인: https://search.maven.org/artifact/io.brillianttiger.bio/pubmed-pmc-parser

---

## 7. 배포 후 확인

### Step 1: Maven Central 검색
```
https://search.maven.org/
→ "io.brillianttiger.bio" 검색
```

### Step 2: Gradle 사용자 테스트
```gradle
dependencies {
    implementation 'io.brillianttiger.bio:pubmed:1.0.0'
    implementation 'io.brillianttiger.bio:pmc:1.0.0'
}
```

### Step 3: Maven 사용자 테스트
```xml
<dependency>
    <groupId>io.brillianttiger.bio</groupId>
    <artifactId>pubmed</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## 📝 체크리스트

배포 전 최종 확인:

- [ ] 모든 테스트 통과 (100% 커버리지)
- [ ] README.md 업데이트 (버전 변경)
- [ ] CHANGELOG.md 작성
- [ ] Git 태그 생성 (`v1.0.0`)
- [ ] JavaDoc 생성 확인
- [ ] Sources JAR 생성 확인
- [ ] POM 메타데이터 완성
- [ ] GPG 서명 설정 완료
- [ ] Sonatype 자격증명 설정

---

## 🔧 트러블슈팅

### 문제 1: GPG 서명 실패
```
Solution: gpg-agent 재시작
$ gpgconf --kill gpg-agent
$ gpg-agent --daemon
```

### 문제 2: 401 Unauthorized
```
Solution: gradle.properties 자격증명 확인
- ossrhUsername
- ossrhPassword
```

### 문제 3: POM 검증 실패
```
Solution: POM 필수 필드 확인
- name, description, url
- licenses, developers, scm
```

### 문제 4: Javadoc 생성 오류
```
Solution: build.gradle.kts에 추가
tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}
```

---

## 📚 참고 자료

- [Sonatype OSSRH Guide](https://central.sonatype.org/publish/publish-guide/)
- [Gradle Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)
- [Gradle Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html)
- [Maven Central Search](https://search.maven.org/)

---

## 🚀 간단 요약

### GitHub 기반 배포 (가장 쉬움) ✅

```bash
# 1. Sonatype 계정 생성 + 티켓 (io.github.yourusername)
# 2. GPG 키 생성 및 배포
# 3. build.gradle.kts 수정 (group, version, signing)
# 4. 배포
./gradlew clean build publish

# 5. Nexus에서 Close → Release
```

**예상 소요 시간:** 2-3시간 (초기 설정) + 1-2일 (승인 대기)

---

**작성일:** 2026-01-26
**프로젝트:** pubmed-pmc-parser v1.0.0
