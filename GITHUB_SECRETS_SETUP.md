# GitHub Secrets 설정 가이드 (2026 최신판)

## 📋 목차
1. [개요](#개요)
2. [필요한 Secrets](#필요한-secrets)
3. [Central Portal 사용자 토큰 생성](#central-portal-사용자-토큰-생성)
4. [GPG 키 준비](#gpg-키-준비)
5. [GitHub Secrets 등록](#github-secrets-등록)
6. [검증](#검증)

---

## 개요

Git tag push 시 자동으로 Maven Central에 배포하기 위해서는 GitHub Repository Secrets에 다음 자격증명을 등록해야 합니다:
- Central Portal 사용자 토큰
- GPG 서명 키

---

## 필요한 Secrets

### 1. MAVEN_CENTRAL_BIO_USERNAME
**설명**: Central Portal 사용자 토큰의 Username
**값**: Central Portal에서 생성한 토큰의 Username (예: `A1B2C3D4`)

### 2. MAVEN_CENTRAL_BIO_PASSWORD
**설명**: Central Portal 사용자 토큰의 Password
**값**: Central Portal에서 생성한 토큰의 Password (매우 긴 문자열)

### 3. ORG_GRADLE_PROJECT_signingInMemoryKey
**설명**: GPG 개인키 (ASCII armored format)
**값**: GPG 비밀키 전체 내용 (아래 참고)

### 4. ORG_GRADLE_PROJECT_signingInMemoryKeyPassword
**설명**: GPG 키 비밀번호
**값**: GPG 키 생성 시 입력한 passphrase

**⚠️ 주의**: Secret 이름을 **정확히** 입력해야 합니다!

---

## Central Portal 사용자 토큰 생성

### Step 1: Central Portal 로그인

1. https://central.sonatype.com/ 접속
2. 계정 로그인 (Sign Up이 안 되어 있으면 먼저 가입)

### Step 2: 네임스페이스 검증 확인

1. 로그인 후 **Namespaces** 메뉴 확인
2. `io.brillianttiger.bio` 또는 `io.github.yourusername`가 **Verified** 상태인지 확인
3. Verified가 아니면 검증 완료 필요

### Step 3: 사용자 토큰 생성

1. 우측 상단 **Account** 클릭
2. **Generate User Token** 버튼 클릭
3. **Username**과 **Password** 복사 (창을 닫으면 다시 볼 수 없음!)

**중요**:
- Username: 짧은 문자열 (예: `A1B2C3D4`)
- Password: 매우 긴 문자열 (전체 복사!)

이 값을 `MAVEN_CENTRAL_BIO_USERNAME`과 `MAVEN_CENTRAL_BIO_PASSWORD`에 사용합니다.

---

## GPG 키 준비

### Step 1: GPG 키 목록 확인

```bash
gpg --list-secret-keys --keyid-format=long
```

출력 예시:
```
sec   rsa3072/ABCD1234EFGH5678 2026-01-26 [SC]
      ABCDEF1234567890ABCDEF1234567890ABCDEF12
uid                 [ultimate] Brilliant Tiger <dev@brillianttiger.com>
```

위에서 `ABCD1234EFGH5678`이 **Key ID**입니다.

### Step 2: GPG 개인키 내보내기 (ASCII Armored)

```bash
# 키 ID의 마지막 8자리 또는 전체 fingerprint 사용
gpg --armor --export-secret-keys ABCD1234EFGH5678
```

**출력 예시** (이 전체 내용을 복사):
```
-----BEGIN PGP PRIVATE KEY BLOCK-----

lQdGBGXJK2EBEAC9kQVZ+... (여러 줄)
...
-----END PGP PRIVATE KEY BLOCK-----
```

**중요**:
- `-----BEGIN PGP PRIVATE KEY BLOCK-----` 포함
- `-----END PGP PRIVATE KEY BLOCK-----` 포함
- 중간 빈 줄도 그대로 유지
- 전체를 복사!

### Step 3: GPG 키 공개 서버에 배포

Central Portal은 **keyserver.ubuntu.com**에서 GPG 키를 확인합니다:

```bash
# 공개 키 배포
gpg --keyserver keyserver.ubuntu.com --send-keys ABCD1234EFGH5678

# 확인
gpg --keyserver keyserver.ubuntu.com --recv-keys ABCD1234EFGH5678
```

---

## GitHub Secrets 등록

### Step 1: GitHub Repository Settings 이동

1. GitHub 저장소 페이지로 이동
2. **Settings** 탭 클릭
3. 왼쪽 메뉴에서 **Secrets and variables** → **Actions** 클릭

### Step 2: New repository secret 생성

각 Secret에 대해 다음을 반복:

#### 1. MAVEN_CENTRAL_BIO_USERNAME

- **New repository secret** 버튼 클릭
- **Name**: `MAVEN_CENTRAL_BIO_USERNAME`
- **Secret**: Central Portal 토큰 Username 붙여넣기 (예: `A1B2C3D4`)
- **Add secret** 클릭

#### 2. MAVEN_CENTRAL_BIO_PASSWORD

- **New repository secret** 버튼 클릭
- **Name**: `MAVEN_CENTRAL_BIO_PASSWORD`
- **Secret**: Central Portal 토큰 Password 붙여넣기 (긴 문자열 전체)
- **Add secret** 클릭

#### 3. ORG_GRADLE_PROJECT_signingInMemoryKey

- **New repository secret** 버튼 클릭
- **Name**: `ORG_GRADLE_PROJECT_signingInMemoryKey`
- **Secret**: GPG 개인키 전체 붙여넣기
- **Add secret** 클릭

**⚠️ 주의**: 전체 내용을 포함해야 합니다!
```
-----BEGIN PGP PRIVATE KEY BLOCK-----

(모든 내용)

-----END PGP PRIVATE KEY BLOCK-----
```

#### 4. ORG_GRADLE_PROJECT_signingInMemoryKeyPassword

- **New repository secret** 버튼 클릭
- **Name**: `ORG_GRADLE_PROJECT_signingInMemoryKeyPassword`
- **Secret**: GPG 키 passphrase 입력
- **Add secret** 클릭

---

## 검증

### 로컬에서 환경변수 테스트

배포 전에 로컬에서 환경변수가 제대로 인식되는지 테스트할 수 있습니다:

```bash
# 환경변수 설정 (임시)
export MAVEN_CENTRAL_BIO_USERNAME="your-username"
export MAVEN_CENTRAL_BIO_PASSWORD="your-token-password"
export ORG_GRADLE_PROJECT_signingInMemoryKey="$(gpg --armor --export-secret-keys YOUR_KEY_ID)"
export ORG_GRADLE_PROJECT_signingInMemoryKeyPassword="your-gpg-passphrase"

# 로컬 Maven 저장소에 배포 테스트
./gradlew publishToMavenLocal

# 확인
ls ~/.m2/repository/io/brillianttiger/bio/
```

### GitHub Actions 워크플로우 테스트

1. **테스트 태그 생성**:
   ```bash
   git tag v0.0.1-test
   git push origin v0.0.1-test
   ```

2. **Actions 탭에서 확인**:
   - GitHub 저장소 → **Actions** 탭
   - "Publish to Maven Central" 워크플로우 확인
   - 로그에서 오류 확인

3. **실패 시 디버깅**:
   - Secrets 이름 확인 (정확히 `ORG_GRADLE_PROJECT_signingInMemoryKey`)
   - GPG 키 형식 확인
   - Central Portal 토큰 유효성 확인
   - Namespace 검증 상태 확인

---

## 배포 프로세스

### 1. GitHub Release 생성

**웹 인터페이스 사용:**
1. GitHub 저장소 → **Releases** → **Create a new release**
2. **Choose a tag**: `v1.0.0` 입력
3. **Release title**: `Release 1.0.0`
4. **Description**: 릴리스 노트 작성
5. **Publish release** 클릭

**또는 GitHub CLI 사용:**
```bash
gh release create v1.0.0 \
  --title "Release 1.0.0" \
  --notes "Maven Central deployment"
```

### 2. GitHub Actions 자동 실행

- Release가 published되면 `publish.yml` 워크플로우가 자동 트리거
- 빌드 → 테스트 → Maven Central 자동 업로드 및 릴리스

### 3. Central Portal에서 확인

https://central.sonatype.com/publishing 에서 배포 상태 확인

### 4. Maven Central 동기화 대기

- 10분 ~ 2시간: Maven Central 동기화
- 2시간 ~ 24시간: 검색 가능

---

## 트러블슈팅

### 문제 1: GPG 서명 실패

**증상**:
```
Could not read PGP secret key
```

**해결책**:
- `ORG_GRADLE_PROJECT_signingInMemoryKey`에 전체 개인키가 포함되었는지 확인
- ASCII armored 형식인지 확인 (`-----BEGIN PGP PRIVATE KEY BLOCK-----`)
- 줄바꿈이 제대로 유지되었는지 확인
- GitHub에서 Secret을 다시 등록 (복사/붙여넣기 시 줄바꿈 손실 가능)

### 문제 2: 401 Unauthorized

**증상**:
```
Received status code 401 from server: Unauthorized
```

**해결책**:
- Central Portal에서 **User Token** 재생성
- `MAVEN_CENTRAL_BIO_USERNAME`과 `MAVEN_CENTRAL_BIO_PASSWORD` 확인
- Namespace가 **Verified** 상태인지 확인

### 문제 3: Publishing failed

**증상**:
```
Publishing to Maven Central failed
```

**해결책**:
- GPG 키가 keyserver.ubuntu.com에 업로드되었는지 확인
  ```bash
  gpg --keyserver keyserver.ubuntu.com --recv-keys YOUR_KEY_ID
  ```
- Central Portal에서 Namespace 검증 완료 확인
- 로그에서 상세 오류 메시지 확인

---

## 참고 자료

- [Sonatype Central Portal](https://central.sonatype.com/)
- [GitHub Encrypted Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [Vanniktech Plugin Documentation](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)

---

**작성일**: 2026-01-26
**프로젝트**: pubmed-pmc-parser v1.0.0
**플러그인**: Vanniktech Maven Publish Plugin v0.30.0
