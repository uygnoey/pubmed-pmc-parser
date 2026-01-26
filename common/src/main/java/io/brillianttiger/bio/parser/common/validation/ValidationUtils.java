package io.brillianttiger.bio.parser.common.validation;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * ValidationUtils / 검증 유틸리티
 *
 * KR: PubMed 및 학술 데이터 검증을 위한 유틸리티 클래스.
 *     필수 필드, PMID, DOI, ORCID 등의 형식 검증 제공.
 * EN: Validation utility class for PubMed and academic data.
 *     Provides validation for required fields, PMID, DOI, ORCID formats.
 */
public final class ValidationUtils {

    /**
     * PMID 패턴 / PMID pattern
     *
     * KR: PubMed ID는 1-8자리 숫자
     * EN: PubMed ID is 1-8 digits
     */
    private static final Pattern PMID_PATTERN = Pattern.compile("^\\d{1,8}$");

    /**
     * DOI 패턴 / DOI pattern
     *
     * KR: DOI는 10.xxxx/yyyy 형식
     *     예: 10.1001/jama.2023.12345
     * EN: DOI follows 10.xxxx/yyyy format
     *     Example: 10.1001/jama.2023.12345
     */
    private static final Pattern DOI_PATTERN = Pattern.compile(
            "^10\\.\\d{4,}(\\.\\d+)*/[\\S]+$"
    );

    /**
     * ORCID 패턴 / ORCID pattern
     *
     * KR: ORCID는 xxxx-xxxx-xxxx-xxxx 형식
     *     16자리 숫자, 마지막 자리는 X 가능
     *     예: 0000-0002-1825-0097
     * EN: ORCID follows xxxx-xxxx-xxxx-xxxx format
     *     16 digits, last digit can be X
     *     Example: 0000-0002-1825-0097
     */
    private static final Pattern ORCID_PATTERN = Pattern.compile(
            "^\\d{4}-\\d{4}-\\d{4}-\\d{3}[0-9X]$"
    );

    /**
     * MeSH UI 패턴 / MeSH UI pattern
     *
     * KR: MeSH Unique Identifier는 D 또는 C로 시작하는 6자리 숫자
     *     예: D000001, C000657
     * EN: MeSH Unique Identifier starts with D or C followed by 6 digits
     *     Example: D000001, C000657
     */
    private static final Pattern MESH_UI_PATTERN = Pattern.compile(
            "^[DC]\\d{6}$"
    );

    /**
     * 최소 유효 연도 / Minimum valid year
     *
     * KR: PubMed는 1809년부터 시작 (최초 의학 저널)
     * EN: PubMed starts from 1809 (first medical journal)
     */
    private static final int MIN_YEAR = 1809;

    /**
     * 최대 유효 연도 / Maximum valid year
     *
     * KR: 현재 연도 + 5년 (미래 출판 허용)
     * EN: Current year + 5 years (allow future publications)
     */
    private static final int MAX_YEAR = java.time.Year.now().getValue() + 5;

    private ValidationUtils() {
        // 유틸리티 클래스: 인스턴스화 방지 / Utility class: prevent instantiation
    }

    /**
     * 필수 필드 검증 / Validate required field
     *
     * KR: 값이 null이거나 빈 문자열인 경우 오류 반환.
     *     문자열이 아닌 경우 null 체크만 수행.
     * EN: Returns error if value is null or empty string.
     *     For non-string values, only checks for null.
     *
     * @param value 검증할 값 / Value to validate
     * @param fieldName 필드명 / Field name
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     */
    public static Optional<ValidationError> validateRequired(Object value, String fieldName) {
        if (value == null) {
            return Optional.of(ValidationError.error(
                    fieldName,
                    fieldName + " is required"
            ));
        }

        // 문자열인 경우 빈 값 체크 / Check for empty string
        if (value instanceof String str && str.trim().isEmpty()) {
            return Optional.of(ValidationError.error(
                    fieldName,
                    fieldName + " cannot be empty"
            ));
        }

        return Optional.empty();
    }

    /**
     * PMID 검증 / Validate PMID
     *
     * KR: PubMed ID 형식 검증.
     *     1-8자리 숫자여야 함.
     *     null이나 빈 문자열은 Optional.empty() 반환 (필수 검증은 validateRequired 사용).
     * EN: Validate PubMed ID format.
     *     Must be 1-8 digits.
     *     Returns Optional.empty() for null or empty (use validateRequired for required check).
     *
     * @param pmid PMID 문자열 / PMID string
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     *
     * @example
     * validatePmid("12345678")     → Optional.empty() (valid)
     * validatePmid("123456789")    → error (too long)
     * validatePmid("abc123")       → error (contains letters)
     * validatePmid(null)           → Optional.empty() (not validated)
     */
    public static Optional<ValidationError> validatePmid(String pmid) {
        // null이나 빈 값은 검증하지 않음 (필수 검증은 별도로)
        // Don't validate null or empty (use separate required validation)
        if (pmid == null || pmid.trim().isEmpty()) {
            return Optional.empty();
        }

        String trimmed = pmid.trim();

        if (!PMID_PATTERN.matcher(trimmed).matches()) {
            return Optional.of(ValidationError.error(
                    "PMID",
                    "Invalid PMID format. Must be 1-8 digits. Got: " + trimmed
            ));
        }

        return Optional.empty();
    }

    /**
     * DOI 검증 / Validate DOI
     *
     * KR: DOI (Digital Object Identifier) 형식 검증.
     *     10.xxxx/yyyy 형식이어야 함.
     *     null이나 빈 문자열은 Optional.empty() 반환.
     * EN: Validate DOI (Digital Object Identifier) format.
     *     Must follow 10.xxxx/yyyy format.
     *     Returns Optional.empty() for null or empty.
     *
     * @param doi DOI 문자열 / DOI string
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     *
     * @example
     * validateDoi("10.1001/jama.2023.12345")  → Optional.empty() (valid)
     * validateDoi("10.1038/nature12345")      → Optional.empty() (valid)
     * validateDoi("11.1234/invalid")          → error (must start with 10.)
     * validateDoi("10.123")                   → error (missing suffix)
     */
    public static Optional<ValidationError> validateDoi(String doi) {
        // null이나 빈 값은 검증하지 않음
        // Don't validate null or empty
        if (doi == null || doi.trim().isEmpty()) {
            return Optional.empty();
        }

        String trimmed = doi.trim();

        if (!DOI_PATTERN.matcher(trimmed).matches()) {
            return Optional.of(ValidationError.error(
                    "DOI",
                    "Invalid DOI format. Must follow 10.xxxx/yyyy pattern. Got: " + trimmed
            ));
        }

        return Optional.empty();
    }

    /**
     * ORCID 검증 / Validate ORCID
     *
     * KR: ORCID (Open Researcher and Contributor ID) 형식 검증.
     *     xxxx-xxxx-xxxx-xxxx 형식, 마지막 자리는 X 가능.
     *     null이나 빈 문자열은 Optional.empty() 반환.
     * EN: Validate ORCID (Open Researcher and Contributor ID) format.
     *     Must follow xxxx-xxxx-xxxx-xxxx format, last digit can be X.
     *     Returns Optional.empty() for null or empty.
     *
     * @param orcid ORCID 문자열 / ORCID string
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     *
     * @example
     * validateOrcid("0000-0002-1825-0097")  → Optional.empty() (valid)
     * validateOrcid("0000-0002-1825-009X")  → Optional.empty() (valid, X checksum)
     * validateOrcid("0000-0002-1825-00971") → error (too long)
     * validateOrcid("0000-0002-1825-00Y7")  → error (invalid character)
     */
    public static Optional<ValidationError> validateOrcid(String orcid) {
        // null이나 빈 값은 검증하지 않음
        // Don't validate null or empty
        if (orcid == null || orcid.trim().isEmpty()) {
            return Optional.empty();
        }

        String trimmed = orcid.trim();

        if (!ORCID_PATTERN.matcher(trimmed).matches()) {
            return Optional.of(ValidationError.error(
                    "ORCID",
                    "Invalid ORCID format. Must follow xxxx-xxxx-xxxx-xxxx pattern. Got: " + trimmed
            ));
        }

        return Optional.empty();
    }

    /**
     * ORCID 체크섬 검증 / Validate ORCID checksum
     *
     * KR: ORCID의 체크섬(마지막 자리)을 ISO 7064 mod 11-2 알고리즘으로 검증.
     *     형식 검증은 validateOrcid()를 먼저 수행할 것.
     * EN: Validate ORCID checksum (last digit) using ISO 7064 mod 11-2 algorithm.
     *     Perform format validation with validateOrcid() first.
     *
     * @param orcid ORCID 문자열 / ORCID string
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     */
    public static Optional<ValidationError> validateOrcidChecksum(String orcid) {
        if (orcid == null || orcid.trim().isEmpty()) {
            return Optional.empty();
        }

        String trimmed = orcid.trim();

        // 형식 검증 먼저 / Format validation first
        if (!ORCID_PATTERN.matcher(trimmed).matches()) {
            return Optional.of(ValidationError.error(
                    "ORCID",
                    "Invalid ORCID format for checksum validation"
            ));
        }

        // 하이픈 제거 / Remove hyphens
        String digits = trimmed.replace("-", "");

        // ISO 7064 mod 11-2 체크섬 계산 / Calculate ISO 7064 mod 11-2 checksum
        int total = 0;
        for (int i = 0; i < 15; i++) {
            int digit = Character.getNumericValue(digits.charAt(i));
            total = (total + digit) * 2;
        }

        int remainder = total % 11;
        int checkDigit = (12 - remainder) % 11;
        char expectedCheckChar = (checkDigit == 10) ? 'X' : (char) ('0' + checkDigit);

        char actualCheckChar = digits.charAt(15);

        if (actualCheckChar != expectedCheckChar) {
            return Optional.of(ValidationError.error(
                    "ORCID",
                    String.format("Invalid ORCID checksum. Expected: %c, Got: %c",
                            expectedCheckChar, actualCheckChar)
            ));
        }

        return Optional.empty();
    }

    /**
     * MeSH UI 검증 / Validate MeSH UI
     *
     * KR: MeSH Unique Identifier 형식 검증.
     *     D 또는 C로 시작하는 6자리 숫자여야 함.
     *     null이나 빈 문자열은 Optional.empty() 반환.
     * EN: Validate MeSH Unique Identifier format.
     *     Must start with D or C followed by 6 digits.
     *     Returns Optional.empty() for null or empty.
     *
     * @param meshUi MeSH UI 문자열 / MeSH UI string
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     *
     * @example
     * validateMeshUi("D000001")  → Optional.empty() (valid)
     * validateMeshUi("C000657")  → Optional.empty() (valid)
     * validateMeshUi("D00001")   → error (too short)
     * validateMeshUi("E000001")  → error (must start with D or C)
     * validateMeshUi("D12345A")  → error (must be digits)
     */
    public static Optional<ValidationError> validateMeshUi(String meshUi) {
        // null이나 빈 값은 검증하지 않음
        // Don't validate null or empty
        if (meshUi == null || meshUi.trim().isEmpty()) {
            return Optional.empty();
        }

        String trimmed = meshUi.trim();

        if (!MESH_UI_PATTERN.matcher(trimmed).matches()) {
            return Optional.of(ValidationError.error(
                    "MeSH UI",
                    "Invalid MeSH UI format. Must start with D or C followed by 6 digits. Got: " + trimmed
            ));
        }

        return Optional.empty();
    }

    /**
     * 연도 범위 검증 / Validate year range
     *
     * KR: 연도가 유효한 범위(1809 ~ 현재+5년) 내에 있는지 검증.
     *     null은 Optional.empty() 반환.
     * EN: Validate year is within valid range (1809 ~ current+5 years).
     *     Returns Optional.empty() for null.
     *
     * @param year 연도 / Year
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     *
     * @example
     * validateYear(2024)  → Optional.empty() (valid)
     * validateYear(1809)  → Optional.empty() (valid, minimum)
     * validateYear(1808)  → error (too old)
     * validateYear(2100)  → error (too far in future)
     */
    public static Optional<ValidationError> validateYear(Integer year) {
        // null은 검증하지 않음
        // Don't validate null
        if (year == null) {
            return Optional.empty();
        }

        if (year < MIN_YEAR) {
            return Optional.of(ValidationError.error(
                    "Year",
                    String.format("Year %d is before minimum year %d", year, MIN_YEAR)
            ));
        }

        if (year > MAX_YEAR) {
            return Optional.of(ValidationError.error(
                    "Year",
                    String.format("Year %d is after maximum year %d", year, MAX_YEAR)
            ));
        }

        return Optional.empty();
    }

    /**
     * 월 범위 검증 / Validate month range
     *
     * KR: 월이 유효한 범위(1-12) 내에 있는지 검증.
     *     null은 Optional.empty() 반환.
     * EN: Validate month is within valid range (1-12).
     *     Returns Optional.empty() for null.
     *
     * @param month 월 / Month
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     */
    public static Optional<ValidationError> validateMonth(Integer month) {
        // null은 검증하지 않음
        // Don't validate null
        if (month == null) {
            return Optional.empty();
        }

        if (month < 1 || month > 12) {
            return Optional.of(ValidationError.error(
                    "Month",
                    String.format("Month must be between 1 and 12. Got: %d", month)
            ));
        }

        return Optional.empty();
    }

    /**
     * 일 범위 검증 / Validate day range
     *
     * KR: 일이 유효한 범위(1-31) 내에 있는지 검증.
     *     null은 Optional.empty() 반환.
     * EN: Validate day is within valid range (1-31).
     *     Returns Optional.empty() for null.
     *
     * @param day 일 / Day
     * @return 검증 오류 (있을 경우) / Validation error (if present)
     */
    public static Optional<ValidationError> validateDay(Integer day) {
        // null은 검증하지 않음
        // Don't validate null
        if (day == null) {
            return Optional.empty();
        }

        if (day < 1 || day > 31) {
            return Optional.of(ValidationError.error(
                    "Day",
                    String.format("Day must be between 1 and 31. Got: %d", day)
            ));
        }

        return Optional.empty();
    }
}
