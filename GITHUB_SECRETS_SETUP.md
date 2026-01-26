# GitHub Secrets 설정 가이드

## 📋 목차
1. [개요](#개요)
2. [필요한 Secrets](#필요한-secrets)
3. [GPG 키 준비](#gpg-키-준비)
4. [GitHub Secrets 등록](#github-secrets-등록)
5. [검증](#검증)

---

## 개요

Git tag push 시 자동으로 Maven Central에 배포하기 위해서는 GitHub Repository Secrets에 다음 자격증명을 등록해야 합니다:
- Sonatype OSSRH 계정 정보
- GPG 서명 키

---

## 필요한 Secrets

### 1. OSSRH_USERNAME
**설명**: Sonatype OSSRH 사용자 이름
**값**: Sonatype Jira 계정 username (예: `yourusername`)

### 2. OSSRH_PASSWORD
**설명**: Sonatype OSSRH 비밀번호
**값**: Sonatype Jira 계정 비밀번호

### 3. GPG_SIGNING_KEY
**설명**: GPG 개인키 (ASCII armored format)
**값**: GPG 비밀키 전체 내용 (아래 참고)

### 4. GPG_SIGNING_PASSWORD
**설명**: GPG 키 비밀번호
**값**: GPG 키 생성 시 입력한 passphrase

---

## GPG 키 준비

### Step 1: GPG 키 목록 확인

```bash
gpg --list-secret-keys --keyid-format=long
```

출력 예시:
```
sec   rsa3072/ABCD1234EFGH5678 2024-01-26 [SC]
      ABCDEF1234567890ABCDEF1234567890ABCDEF12
uid                 [ultimate] Brilliant Tiger <dev@brillianttiger.com>
```

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

### Step 3: 개인키 Base64 인코딩 (선택사항)

GitHub Secrets에 개인키를 직접 붙여넣으면 줄바꿈 문제가 발생할 수 있습니다.
Base64로 인코딩하면 안전하게 저장할 수 있습니다:

```bash
# macOS/Linux
gpg --armor --export-secret-keys ABCD1234EFGH5678 | base64

# 결과를 GPG_SIGNING_KEY에 저장
```

**중요**: Base64로 인코딩한 경우, `build.gradle.kts`에서 디코딩 로직을 추가해야 합니다.
현재 설정은 ASCII armored 형식을 직접 사용하므로, **Base64 인코딩 없이** 그대로 복사하는 것을 권장합니다.

---

## GitHub Secrets 등록

### Step 1: GitHub Repository Settings 이동

1. GitHub 저장소 페이지로 이동
2. **Settings** 탭 클릭
3. 왼쪽 메뉴에서 **Secrets and variables** → **Actions** 클릭

### Step 2: New repository secret 생성

각 Secret에 대해 다음을 반복:

1. **New repository secret** 버튼 클릭
2. **Name** 입력:
   - `OSSRH_USERNAME`
   - `OSSRH_PASSWORD`
   - `GPG_SIGNING_KEY`
   - `GPG_SIGNING_PASSWORD`
3. **Secret** 입력:
   - 해당하는 값 붙여넣기
4. **Add secret** 클릭

### GPG_SIGNING_KEY 등록 시 주의사항

**전체 내용을 포함**해야 합니다:
```
-----BEGIN PGP PRIVATE KEY BLOCK-----

(모든 내용)

-----END PGP PRIVATE KEY BLOCK-----
```

**확인 사항**:
- ✅ `-----BEGIN PGP PRIVATE KEY BLOCK-----` 포함
- ✅ `-----END PGP PRIVATE KEY BLOCK-----` 포함
- ✅ 중간에 빈 줄도 그대로 유지
- ❌ 앞뒤 공백이나 추가 줄바꿈 없음

---

## 검증

### 로컬에서 환경변수 테스트

배포 전에 로컬에서 환경변수가 제대로 인식되는지 테스트할 수 있습니다:

```bash
# 환경변수 설정 (임시)
export OSSRH_USERNAME="your-username"
export OSSRH_PASSWORD="your-password"
export GPG_SIGNING_KEY="$(gpg --armor --export-secret-keys YOUR_KEY_ID)"
export GPG_SIGNING_PASSWORD="your-gpg-passphrase"

# 빌드 및 서명 테스트
./gradlew clean build publishToMavenLocal

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
   - Secrets 이름 확인 (대소문자 정확히)
   - GPG 키 형식 확인
   - OSSRH 계정 승인 상태 확인

---

## 배포 프로세스

### 1. 버전 태그 생성 및 Push

```bash
# 릴리스 버전 태그
git tag v1.0.0
git push origin v1.0.0
```

### 2. GitHub Actions 자동 실행

- `publish.yml` 워크플로우가 자동으로 트리거됨
- 빌드 → 테스트 → Maven Central 업로드

### 3. Sonatype Nexus에서 릴리스 승인

1. https://s01.oss.sonatype.org/ 로그인
2. 좌측 **Staging Repositories** 클릭
3. `iobrillanttiger-XXXX` 찾기
4. **Close** 버튼 클릭 (검증 5-10분)
5. **Release** 버튼 클릭
6. 확인

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
- `GPG_SIGNING_KEY`에 전체 개인키가 포함되었는지 확인
- ASCII armored 형식인지 확인 (`-----BEGIN PGP PRIVATE KEY BLOCK-----`)
- 줄바꿈이 제대로 유지되었는지 확인

### 문제 2: 401 Unauthorized

**증상**:
```
Received status code 401 from server: Unauthorized
```

**해결책**:
- `OSSRH_USERNAME`과 `OSSRH_PASSWORD` 확인
- Sonatype OSSRH 티켓이 "RESOLVED" 상태인지 확인
- 도메인 소유권 증명 완료 확인

### 문제 3: POM 검증 실패

**증상**:
```
POM validation failed
```

**해결책**:
- `build.gradle.kts`의 POM 메타데이터 확인
- 필수 필드: name, description, url, licenses, developers, scm

---

## 참고 자료

- [Sonatype OSSRH Guide](https://central.sonatype.org/publish/publish-guide/)
- [GitHub Encrypted Secrets](https://docs.github.com/en/actions/security-guides/encrypted-secrets)
- [GPG Signing Guide](https://central.sonatype.org/publish/requirements/gpg/)

---

**작성일**: 2026-01-26
**프로젝트**: pubmed-pmc-parser v1.0.0
