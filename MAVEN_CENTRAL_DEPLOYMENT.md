# Maven Central 배포 가이드 (2026 최신판)

## 🚨 중요 변경사항

**2025년 6월 30일부터 OSSRH가 완전 종료**되고 **Central Portal**로 전환되었습니다.

- ❌ **옛날 방식**: Sonatype OSSRH + Jira 티켓 + Nexus Staging Plugin
- ✅ **새로운 방식**: Central Portal + 간단한 가입 + Vanniktech Plugin

---

## 📋 목차
1. [사전 준비](#사전-준비)
2. [Central Portal 계정 생성](#central-portal-계정-생성)
3. [GPG 키 생성 및 배포](#gpg-키-생성-및-배포)
4. [자동 배포 설정 (GitHub Actions)](#자동-배포-설정-github-actions)
5. [수동 배포 (로컬)](#수동-배포-로컬)
6. [배포 확인](#배포-확인)

---

## 1. 사전 준비

### ✅ 이미 완료된 항목
- [x] 프로젝트 품질 (100% 테스트 커버리지)
- [x] 문서화 (README, JavaDoc)
- [x] Maven POM 메타데이터 (`gradle.properties`에 설정완료)
- [x] 오픈소스 라이선스 (Apache 2.0)
- [x] Vanniktech Maven Publish Plugin 설정

### ⚠️ 필요한 항목
- [ ] Central Portal 계정 (https://central.sonatype.com/)
- [ ] 네임스페이스 검증 (Namespace Verification)
- [ ] GPG 키페어 생성 및 배포
- [ ] GitHub Secrets 설정 (자동 배포용)

---

## 2. Central Portal 계정 생성

### Step 1: 계정 생성 (매우 간단!)

1. **https://central.sonatype.com/ 접속**
2. **Sign Up** 또는 GitHub/Google 계정으로 로그인
3. 이메일 인증 완료

**🎉 Jira 티켓 불필요!** 2023년부터 티켓 없이 바로 가입 가능합니다.

### Step 2: Namespace 검증

**네임스페이스**란 Maven의 groupId (예: `io.brillianttiger.bio`)를 말합니다.

#### 옵션 A: GitHub 기반 네임스페이스 (가장 쉬움) ✅

```
Namespace: io.github.yourusername
검증 방법: GitHub 계정으로 로그인만 하면 자동 검증!
승인 시간: 즉시
```

#### 옵션 B: 커스텀 도메인 네임스페이스 (이미 요청됨)

```
Namespace: io.brillianttiger.bio
검증 방법: 도메인 소유권 증명 (DNS TXT 레코드 또는 웹사이트 파일)
승인 시간: 1-2 영업일
```

**현재 상태**: `io.brillianttiger.bio`로 도메인 인증 요청 완료 ✅

### Step 3: 사용자 토큰 생성

1. Central Portal 로그인
2. **Account** → **Generate User Token** 클릭
3. Username과 Password(Token) 복사
4. GitHub Secrets에 저장할 예정

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
gpg --list-secret-keys --keyid-format=long

# 출력 예시:
# sec   rsa3072/ABCD1234EFGH5678 2026-01-26 [SC]
#       ABCDEF1234567890ABCDEF1234567890ABCDEF12
# uid                 [ultimate] Brilliant Tiger <dev@brillianttiger.com>
```

위에서 `ABCD1234EFGH5678`이 **Key ID**입니다.

### Step 4: 공개 키 배포 (Central Portal)

Central Portal에서는 keyserver.ubuntu.com만 확인하므로:

```bash
# Key ID의 마지막 8자리 또는 전체 사용
gpg --keyserver keyserver.ubuntu.com --send-keys ABCD1234EFGH5678

# 또는 다른 keyserver에도 배포 (선택)
gpg --keyserver keys.openpgp.org --send-keys ABCD1234EFGH5678
gpg --keyserver pgp.mit.edu --send-keys ABCD1234EFGH5678
```

### Step 5: 개인 키 내보내기 (GitHub Secrets용)

```bash
# ASCII Armored 형식으로 내보내기
gpg --armor --export-secret-keys ABCD1234EFGH5678

# 출력 전체를 복사 (-----BEGIN ~ -----END 포함)
```

---

## 4. 자동 배포 설정 (GitHub Actions)

### Step 1: GitHub Secrets 설정

GitHub Repository → **Settings** → **Secrets and variables** → **Actions**에서 다음 4개 Secret 등록:

| Secret 이름 | 값 | 설명 |
|------------|---|------|
| `MAVEN_CENTRAL_BIO_USERNAME` | `your-username` | Central Portal 사용자 토큰의 Username |
| `MAVEN_CENTRAL_BIO_PASSWORD` | `your-token` | Central Portal 사용자 토큰의 Password |
| `ORG_GRADLE_PROJECT_signingInMemoryKey` | `-----BEGIN PGP...-----END PGP...` | GPG 개인키 전체 (Step 3-5에서 복사) |
| `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword` | `your-gpg-passphrase` | GPG 키 비밀번호 |

**주의**: Secret 이름을 정확히 입력해야 합니다!

**상세 가이드**: [GITHUB_SECRETS_SETUP.md](./GITHUB_SECRETS_SETUP.md)

### Step 2: 자동 배포 워크플로우

`.github/workflows/publish.yml` 파일이 이미 설정되어 있습니다.

### Step 3: 릴리스 배포

GitHub에서 Release를 생성하면 자동으로 배포됩니다:

**방법 1: GitHub 웹 인터페이스 사용 (권장)**

1. GitHub 저장소 페이지 → **Releases** 섹션
2. **Create a new release** 클릭
3. **Choose a tag**: `v1.0.0` 입력 (새 태그 생성)
4. **Release title**: `v1.0.0` 또는 `Release 1.0.0`
5. **Description**: 릴리스 노트 작성
6. **Publish release** 클릭 → **자동 배포 시작!**

**방법 2: GitHub CLI 사용**

```bash
# 릴리스 버전 결정
VERSION="1.0.0"

# GitHub Release 생성 (자동으로 tag도 생성됨)
gh release create v${VERSION} \
  --title "Release ${VERSION}" \
  --notes "Maven Central deployment for version ${VERSION}"
```

**방법 3: Git tag 후 Release 생성**

```bash
# 1. Git tag 생성
git tag v1.0.0
git push origin v1.0.0

# 2. GitHub에서 해당 tag로 Release 생성
# (웹 인터페이스 또는 gh CLI 사용)
```

### Step 4: GitHub Actions 확인

1. Release를 publish하면 자동으로 워크플로우 실행
2. GitHub 저장소 → **Actions** 탭
3. "Publish to Maven Central" 워크플로우 실행 확인
4. 약 5-10분 소요

**성공 시**: 자동으로 Maven Central에 배포 완료! 🎉

**주의**: Draft release는 배포되지 않습니다. 반드시 **Publish release**를 클릭해야 합니다.

---

## 5. 수동 배포 (로컬)

### Step 1: 환경변수 설정

```bash
# ~/.zshrc 또는 ~/.bashrc에 추가
export MAVEN_CENTRAL_BIO_USERNAME="your-username"
export MAVEN_CENTRAL_BIO_PASSWORD="your-token"
export ORG_GRADLE_PROJECT_signingInMemoryKey="$(gpg --armor --export-secret-keys YOUR_KEY_ID)"
export ORG_GRADLE_PROJECT_signingInMemoryKeyPassword="your-gpg-passphrase"

# 적용
source ~/.zshrc  # 또는 source ~/.bashrc
```

### Step 2: 버전 업데이트

`build.gradle.kts` 파일에서 SNAPSHOT 제거:

```kotlin
version = "1.0.0"  // "1.0.0-SNAPSHOT" → "1.0.0"
```

### Step 3: 빌드 및 배포

```bash
# 빌드 및 테스트
./gradlew clean build

# Maven Central에 자동 배포 및 릴리스
./gradlew publishAndReleaseToMavenCentral --no-configuration-cache
```

**Vanniktech 플러그인**이 자동으로:
1. 아티팩트 빌드
2. GPG 서명
3. Central Portal에 업로드
4. 자동 릴리스까지 처리!

### Step 4: 배포 확인

로그에서 다음 메시지 확인:

```
> Task :publishAndReleaseToMavenCentral
Successfully published to Maven Central
Deployment ID: xxxx-xxxx-xxxx-xxxx
```

---

## 6. 배포 확인

### Step 1: Maven Central Search

약 10분 ~ 2시간 후:

```
https://central.sonatype.com/
→ "io.brillianttiger.bio" 검색
```

또는:

```
https://search.maven.org/
→ "io.brillianttiger.bio" 검색
```

### Step 2: Gradle 사용자 테스트

```kotlin
dependencies {
    implementation("io.brillianttiger.bio:pubmed:1.0.0")
    implementation("io.brillianttiger.bio:pmc:1.0.0")
    implementation("io.brillianttiger.bio:common:1.0.0")
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

- [ ] Central Portal 계정 생성 및 네임스페이스 검증
- [ ] GPG 키 생성 및 keyserver에 업로드
- [ ] GitHub Secrets 4개 모두 등록
- [ ] 모든 테스트 통과 (100% 커버리지)
- [ ] README.md 버전 업데이트
- [ ] 코드 변경사항 commit 및 push
- [ ] **GitHub Release 생성 (Publish)**

---

## 🔧 트러블슈팅

### 문제 1: GPG 서명 실패

```
Could not read PGP secret key
```

**해결책**:
- `ORG_GRADLE_PROJECT_signingInMemoryKey`에 전체 개인키 포함 확인
- ASCII armored 형식 확인 (`-----BEGIN PGP PRIVATE KEY BLOCK-----`)
- 줄바꿈이 제대로 유지되었는지 확인

### 문제 2: 401 Unauthorized

```
Received status code 401 from server: Unauthorized
```

**해결책**:
- Central Portal에서 **User Token** 재생성
- `MAVEN_CENTRAL_BIO_USERNAME`과 `MAVEN_CENTRAL_BIO_PASSWORD` 확인
- Namespace 검증 완료 확인

### 문제 3: Central Portal에서 찾을 수 없음

**해결책**:
- 10분 ~ 2시간 대기 (Central 동기화 시간)
- Deployment ID 로그 확인
- https://central.sonatype.com/publishing 에서 배포 상태 확인

---

## 📚 참고 자료

- [Sonatype Central Portal](https://central.sonatype.com/)
- [Vanniktech Gradle Maven Publish Plugin](https://vanniktech.github.io/gradle-maven-publish-plugin/)
- [Maven Central Setup Guide](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)
- [GitHub Actions Publishing Guide](https://docs.github.com/en/actions/publishing-packages/publishing-java-packages-with-gradle)

---

## 🚀 간단 요약

### 옛날 방식 (OSSRH - 종료됨) ❌
1. Sonatype Jira 계정 생성
2. Jira 티켓 생성 및 승인 대기 (수일)
3. Nexus Staging Plugin 설정
4. 수동으로 Nexus에서 Close → Release

### 새로운 방식 (Central Portal) ✅
1. Central Portal 가입 (즉시)
2. Namespace 검증 (GitHub 기반은 즉시)
3. Vanniktech Plugin 설정
4. **GitHub Release 생성 → 자동 배포 완료!**

**소요 시간**:
- 초기 설정: 30분
- 배포: GitHub Release 생성 → 자동!

---

**작성일**: 2026-01-26
**프로젝트**: pubmed-pmc-parser v1.0.0
**플러그인**: Vanniktech Maven Publish Plugin v0.30.0
