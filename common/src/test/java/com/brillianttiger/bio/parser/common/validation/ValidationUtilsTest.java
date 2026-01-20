package com.brillianttiger.bio.parser.common.validation;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ValidationUtils 테스트 / ValidationUtils Test
 *
 * KR: 검증 유틸리티 테스트
 * EN: Tests for validation utility
 */
class ValidationUtilsTest {

    // ========== validateRequired Tests ==========

    @Test
    void testValidateRequiredNullValue() {
        // null 값 검증 / Validate null value
        Optional<ValidationError> error = ValidationUtils.validateRequired(null, "testField");

        assertTrue(error.isPresent());
        assertEquals("testField", error.get().field());
        assertEquals("testField is required", error.get().message());
        assertEquals(Severity.ERROR, error.get().severity());
    }

    @Test
    void testValidateRequiredEmptyString() {
        // 빈 문자열 검증 / Validate empty string
        Optional<ValidationError> error = ValidationUtils.validateRequired("", "username");

        assertTrue(error.isPresent());
        assertEquals("username", error.get().field());
        assertEquals("username cannot be empty", error.get().message());
        assertEquals(Severity.ERROR, error.get().severity());
    }

    @Test
    void testValidateRequiredWhitespaceString() {
        // 공백 문자열 검증 / Validate whitespace string
        Optional<ValidationError> error = ValidationUtils.validateRequired("   ", "email");

        assertTrue(error.isPresent());
        assertEquals("email", error.get().field());
        assertTrue(error.get().message().contains("cannot be empty"));
    }

    @Test
    void testValidateRequiredValidString() {
        // 유효한 문자열 / Valid string
        Optional<ValidationError> error = ValidationUtils.validateRequired("valid value", "field");

        assertFalse(error.isPresent());
    }

    @Test
    void testValidateRequiredNonStringObject() {
        // 문자열이 아닌 객체 / Non-string object
        Optional<ValidationError> error = ValidationUtils.validateRequired(12345, "count");

        assertFalse(error.isPresent());
    }

    @Test
    void testValidateRequiredZeroValue() {
        // 0은 유효한 값 / Zero is valid
        Optional<ValidationError> error = ValidationUtils.validateRequired(0, "count");

        assertFalse(error.isPresent());
    }

    // ========== validatePmid Tests ==========

    @Test
    void testValidatePmidValid() {
        // 유효한 PMID / Valid PMID
        assertFalse(ValidationUtils.validatePmid("12345678").isPresent());
        assertFalse(ValidationUtils.validatePmid("1").isPresent());
        assertFalse(ValidationUtils.validatePmid("123").isPresent());
    }

    @Test
    void testValidatePmidValidWithWhitespace() {
        // 공백 포함 유효한 PMID / Valid PMID with whitespace
        assertFalse(ValidationUtils.validatePmid("  12345  ").isPresent());
    }

    @Test
    void testValidatePmidTooLong() {
        // 너무 긴 PMID (9자리) / PMID too long (9 digits)
        Optional<ValidationError> error = ValidationUtils.validatePmid("123456789");

        assertTrue(error.isPresent());
        assertEquals("PMID", error.get().field());
        assertTrue(error.get().message().contains("Invalid PMID format"));
        assertTrue(error.get().message().contains("1-8 digits"));
    }

    @Test
    void testValidatePmidInvalidCharacters() {
        // 문자 포함 / Contains letters
        Optional<ValidationError> error = ValidationUtils.validatePmid("abc123");

        assertTrue(error.isPresent());
        assertEquals("PMID", error.get().field());
        assertTrue(error.get().message().contains("Invalid PMID format"));
    }

    @Test
    void testValidatePmidSpecialCharacters() {
        // 특수 문자 포함 / Contains special characters
        Optional<ValidationError> error = ValidationUtils.validatePmid("12-345");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidatePmidNull() {
        // null은 검증하지 않음 / null is not validated
        assertFalse(ValidationUtils.validatePmid(null).isPresent());
    }

    @Test
    void testValidatePmidEmpty() {
        // 빈 문자열은 검증하지 않음 / empty string is not validated
        assertFalse(ValidationUtils.validatePmid("").isPresent());
        assertFalse(ValidationUtils.validatePmid("   ").isPresent());
    }

    // ========== validateDoi Tests ==========

    @Test
    void testValidateDoiValid() {
        // 유효한 DOI / Valid DOI
        assertFalse(ValidationUtils.validateDoi("10.1001/jama.2023.12345").isPresent());
        assertFalse(ValidationUtils.validateDoi("10.1038/nature12345").isPresent());
        assertFalse(ValidationUtils.validateDoi("10.1234/abc-def_123").isPresent());
    }

    @Test
    void testValidateDoiValidWithWhitespace() {
        // 공백 포함 유효한 DOI / Valid DOI with whitespace
        assertFalse(ValidationUtils.validateDoi("  10.1001/jama.2023  ").isPresent());
    }

    @Test
    void testValidateDoiInvalidPrefix() {
        // 잘못된 접두사 (10.이 아님) / Invalid prefix (not 10.)
        Optional<ValidationError> error = ValidationUtils.validateDoi("11.1234/invalid");

        assertTrue(error.isPresent());
        assertEquals("DOI", error.get().field());
        assertTrue(error.get().message().contains("Invalid DOI format"));
        assertTrue(error.get().message().contains("10.xxxx/yyyy"));
    }

    @Test
    void testValidateDoiMissingSuffix() {
        // 슬래시 뒤 부분 없음 / Missing suffix after slash
        Optional<ValidationError> error = ValidationUtils.validateDoi("10.1234/");

        assertTrue(error.isPresent());
        assertEquals("DOI", error.get().field());
    }

    @Test
    void testValidateDoiNoSlash() {
        // 슬래시 없음 / No slash
        Optional<ValidationError> error = ValidationUtils.validateDoi("10.1234");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidateDoiTooShortPrefix() {
        // 접두사 숫자가 3자리 (최소 4자리 필요) / Prefix too short (need at least 4 digits)
        Optional<ValidationError> error = ValidationUtils.validateDoi("10.123/test");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidateDoiNull() {
        // null은 검증하지 않음 / null is not validated
        assertFalse(ValidationUtils.validateDoi(null).isPresent());
    }

    @Test
    void testValidateDoiEmpty() {
        // 빈 문자열은 검증하지 않음 / empty string is not validated
        assertFalse(ValidationUtils.validateDoi("").isPresent());
    }

    @Test
    void testValidateDoiComplexValid() {
        // 복잡한 유효한 DOI / Complex valid DOI
        assertFalse(ValidationUtils.validateDoi("10.1371/journal.pone.0123456").isPresent());
        assertFalse(ValidationUtils.validateDoi("10.1016/j.cell.2023.01.001").isPresent());
    }

    // ========== validateOrcid Tests ==========

    @Test
    void testValidateOrcidValid() {
        // 유효한 ORCID / Valid ORCID
        assertFalse(ValidationUtils.validateOrcid("0000-0002-1825-0097").isPresent());
        assertFalse(ValidationUtils.validateOrcid("0000-0001-2345-6789").isPresent());
    }

    @Test
    void testValidateOrcidValidWithXChecksum() {
        // X 체크섬 유효한 ORCID / Valid ORCID with X checksum
        assertFalse(ValidationUtils.validateOrcid("0000-0002-1825-009X").isPresent());
    }

    @Test
    void testValidateOrcidValidWithWhitespace() {
        // 공백 포함 유효한 ORCID / Valid ORCID with whitespace
        assertFalse(ValidationUtils.validateOrcid("  0000-0002-1825-0097  ").isPresent());
    }

    @Test
    void testValidateOrcidTooLong() {
        // 너무 긴 ORCID / ORCID too long
        Optional<ValidationError> error = ValidationUtils.validateOrcid("0000-0002-1825-00971");

        assertTrue(error.isPresent());
        assertEquals("ORCID", error.get().field());
        assertTrue(error.get().message().contains("Invalid ORCID format"));
        assertTrue(error.get().message().contains("xxxx-xxxx-xxxx-xxxx"));
    }

    @Test
    void testValidateOrcidTooShort() {
        // 너무 짧은 ORCID / ORCID too short
        Optional<ValidationError> error = ValidationUtils.validateOrcid("0000-0002-1825-009");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidateOrcidInvalidFormat() {
        // 잘못된 형식 (하이픈 없음) / Invalid format (no hyphens)
        Optional<ValidationError> error = ValidationUtils.validateOrcid("0000000218250097");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidateOrcidInvalidCharacter() {
        // 잘못된 문자 (Y는 불가) / Invalid character (Y not allowed)
        Optional<ValidationError> error = ValidationUtils.validateOrcid("0000-0002-1825-00Y7");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidateOrcidXInWrongPosition() {
        // X가 잘못된 위치 (마지막이 아님) / X in wrong position (not last)
        Optional<ValidationError> error = ValidationUtils.validateOrcid("0000-0002-182X-0097");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidateOrcidNull() {
        // null은 검증하지 않음 / null is not validated
        assertFalse(ValidationUtils.validateOrcid(null).isPresent());
    }

    @Test
    void testValidateOrcidEmpty() {
        // 빈 문자열은 검증하지 않음 / empty string is not validated
        assertFalse(ValidationUtils.validateOrcid("").isPresent());
    }

    // ========== validateOrcidChecksum Tests ==========

    @Test
    void testValidateOrcidChecksumValid() {
        // 유효한 ORCID 체크섬 / Valid ORCID checksum
        // 실제 유효한 ORCID 예시들 / Real valid ORCID examples
        assertFalse(ValidationUtils.validateOrcidChecksum("0000-0002-1825-0097").isPresent());
        assertFalse(ValidationUtils.validateOrcidChecksum("0000-0001-5109-3700").isPresent());
        assertFalse(ValidationUtils.validateOrcidChecksum("0000-0002-9079-593X").isPresent());
    }

    @Test
    void testValidateOrcidChecksumInvalid() {
        // 잘못된 체크섬 / Invalid checksum
        Optional<ValidationError> error = ValidationUtils.validateOrcidChecksum("0000-0002-1825-0096");

        assertTrue(error.isPresent());
        assertEquals("ORCID", error.get().field());
        assertTrue(error.get().message().contains("Invalid ORCID checksum"));
        assertTrue(error.get().message().contains("Expected"));
    }

    @Test
    void testValidateOrcidChecksumInvalidFormat() {
        // 형식 오류도 감지 / Detects format errors too
        Optional<ValidationError> error = ValidationUtils.validateOrcidChecksum("invalid-format");

        assertTrue(error.isPresent());
        assertTrue(error.get().message().contains("Invalid ORCID format"));
    }

    @Test
    void testValidateOrcidChecksumNull() {
        // null은 검증하지 않음 / null is not validated
        assertFalse(ValidationUtils.validateOrcidChecksum(null).isPresent());
    }

    @Test
    void testValidateOrcidChecksumEmpty() {
        // 빈 문자열은 검증하지 않음 / Empty string is not validated
        assertFalse(ValidationUtils.validateOrcidChecksum("").isPresent());
        assertFalse(ValidationUtils.validateOrcidChecksum("   ").isPresent());
    }

    // ========== ValidationError Helper Methods Tests ==========

    @Test
    void testValidationErrorFactoryMethods() {
        // error() 팩토리 메서드 / error() factory method
        ValidationError error = ValidationError.error("field1", "Error message");
        assertEquals("field1", error.field());
        assertEquals("Error message", error.message());
        assertEquals(Severity.ERROR, error.severity());

        // warning() 팩토리 메서드 / warning() factory method
        ValidationError warning = ValidationError.warning("field2", "Warning message");
        assertEquals("field2", warning.field());
        assertEquals("Warning message", warning.message());
        assertEquals(Severity.WARNING, warning.severity());

        // info() 팩토리 메서드 / info() factory method
        ValidationError info = ValidationError.info("field3", "Info message");
        assertEquals("field3", info.field());
        assertEquals("Info message", info.message());
        assertEquals(Severity.INFO, info.severity());
    }

    @Test
    void testValidationErrorRecord() {
        // record의 불변성 및 메서드 검증 / Verify record immutability and methods
        ValidationError error1 = new ValidationError("field", "message", Severity.ERROR);
        ValidationError error2 = new ValidationError("field", "message", Severity.ERROR);
        ValidationError error3 = new ValidationError("field", "different", Severity.ERROR);

        // equals 검증 / Verify equals
        assertEquals(error1, error2);
        assertNotEquals(error1, error3);

        // hashCode 검증 / Verify hashCode
        assertEquals(error1.hashCode(), error2.hashCode());

        // toString 검증 / Verify toString
        String str = error1.toString();
        assertTrue(str.contains("field"));
        assertTrue(str.contains("message"));
        assertTrue(str.contains("ERROR"));
    }

    // ========== Integration Tests ==========

    @Test
    void testCombinedValidation() {
        // 여러 검증 조합 / Combined validations
        String pmid = "12345678";
        String doi = "10.1001/test.2023";
        String orcid = "0000-0002-1825-0097";

        // 모두 유효 / All valid
        assertFalse(ValidationUtils.validateRequired(pmid, "PMID").isPresent());
        assertFalse(ValidationUtils.validatePmid(pmid).isPresent());
        assertFalse(ValidationUtils.validateDoi(doi).isPresent());
        assertFalse(ValidationUtils.validateOrcid(orcid).isPresent());
    }

    @Test
    void testRealWorldPmidExamples() {
        // 실제 PMID 예시 / Real-world PMID examples
        assertFalse(ValidationUtils.validatePmid("36945458").isPresent()); // 실제 논문
        assertFalse(ValidationUtils.validatePmid("1").isPresent()); // 첫 번째 PMID
        assertFalse(ValidationUtils.validatePmid("12345").isPresent());
    }

    @Test
    void testRealWorldDoiExamples() {
        // 실제 DOI 예시 / Real-world DOI examples
        assertFalse(ValidationUtils.validateDoi("10.1056/NEJMoa2035389").isPresent());
        assertFalse(ValidationUtils.validateDoi("10.1038/s41586-023-06415-8").isPresent());
        assertFalse(ValidationUtils.validateDoi("10.1126/science.abq1841").isPresent());
    }

    @Test
    void testSeverityEnum() {
        // Severity enum 검증 / Verify Severity enum
        assertEquals(3, Severity.values().length);
        assertEquals(Severity.ERROR, Severity.valueOf("ERROR"));
        assertEquals(Severity.WARNING, Severity.valueOf("WARNING"));
        assertEquals(Severity.INFO, Severity.valueOf("INFO"));
    }

    // ========== validateMeshUi Tests ==========

    @Test
    void testValidateMeshUiValid() {
        // 유효한 MeSH UI / Valid MeSH UI
        assertFalse(ValidationUtils.validateMeshUi("D000001").isPresent());
        assertFalse(ValidationUtils.validateMeshUi("D123456").isPresent());
        assertFalse(ValidationUtils.validateMeshUi("C000657").isPresent());
        assertFalse(ValidationUtils.validateMeshUi("C999999").isPresent());
    }

    @Test
    void testValidateMeshUiValidWithWhitespace() {
        // 공백 포함 유효한 MeSH UI / Valid MeSH UI with whitespace
        assertFalse(ValidationUtils.validateMeshUi("  D000001  ").isPresent());
    }

    @Test
    void testValidateMeshUiInvalidPrefix() {
        // 잘못된 접두사 (D나 C가 아님) / Invalid prefix (not D or C)
        Optional<ValidationError> error = ValidationUtils.validateMeshUi("E000001");

        assertTrue(error.isPresent());
        assertEquals("MeSH UI", error.get().field());
        assertTrue(error.get().message().contains("Invalid MeSH UI format"));
        assertTrue(error.get().message().contains("D or C"));
    }

    @Test
    void testValidateMeshUiTooShort() {
        // 너무 짧음 (6자리 미만) / Too short (less than 6 digits)
        Optional<ValidationError> error = ValidationUtils.validateMeshUi("D00001");

        assertTrue(error.isPresent());
        assertTrue(error.get().message().contains("Invalid MeSH UI format"));
    }

    @Test
    void testValidateMeshUiTooLong() {
        // 너무 김 (6자리 초과) / Too long (more than 6 digits)
        Optional<ValidationError> error = ValidationUtils.validateMeshUi("D0000001");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidateMeshUiInvalidCharacters() {
        // 문자 포함 / Contains letters
        Optional<ValidationError> error = ValidationUtils.validateMeshUi("D12345A");

        assertTrue(error.isPresent());
    }

    @Test
    void testValidateMeshUiNull() {
        // null은 검증하지 않음 / null is not validated
        assertFalse(ValidationUtils.validateMeshUi(null).isPresent());
    }

    @Test
    void testValidateMeshUiEmpty() {
        // 빈 문자열은 검증하지 않음 / empty string is not validated
        assertFalse(ValidationUtils.validateMeshUi("").isPresent());
    }

    // ========== validateYear Tests ==========

    @Test
    void testValidateYearValid() {
        // 유효한 연도 / Valid years
        assertFalse(ValidationUtils.validateYear(1809).isPresent()); // Minimum
        assertFalse(ValidationUtils.validateYear(2024).isPresent());
        assertFalse(ValidationUtils.validateYear(2025).isPresent());
        assertFalse(ValidationUtils.validateYear(java.time.Year.now().getValue()).isPresent());
    }

    @Test
    void testValidateYearTooOld() {
        // 너무 오래된 연도 / Year too old
        Optional<ValidationError> error = ValidationUtils.validateYear(1808);

        assertTrue(error.isPresent());
        assertEquals("Year", error.get().field());
        assertTrue(error.get().message().contains("before minimum year"));
        assertTrue(error.get().message().contains("1809"));
    }

    @Test
    void testValidateYearTooFarInFuture() {
        // 너무 먼 미래 연도 / Year too far in future
        int maxYear = java.time.Year.now().getValue() + 5;
        Optional<ValidationError> error = ValidationUtils.validateYear(maxYear + 1);

        assertTrue(error.isPresent());
        assertEquals("Year", error.get().field());
        assertTrue(error.get().message().contains("after maximum year"));
    }

    @Test
    void testValidateYearNull() {
        // null은 검증하지 않음 / null is not validated
        assertFalse(ValidationUtils.validateYear(null).isPresent());
    }

    // ========== validateMonth Tests ==========

    @Test
    void testValidateMonthValid() {
        // 유효한 월 / Valid months
        for (int month = 1; month <= 12; month++) {
            assertFalse(ValidationUtils.validateMonth(month).isPresent());
        }
    }

    @Test
    void testValidateMonthTooSmall() {
        // 1보다 작음 / Less than 1
        Optional<ValidationError> error = ValidationUtils.validateMonth(0);

        assertTrue(error.isPresent());
        assertEquals("Month", error.get().field());
        assertTrue(error.get().message().contains("between 1 and 12"));
    }

    @Test
    void testValidateMonthTooLarge() {
        // 12보다 큼 / Greater than 12
        Optional<ValidationError> error = ValidationUtils.validateMonth(13);

        assertTrue(error.isPresent());
        assertEquals("Month", error.get().field());
        assertTrue(error.get().message().contains("between 1 and 12"));
    }

    @Test
    void testValidateMonthNull() {
        // null은 검증하지 않음 / null is not validated
        assertFalse(ValidationUtils.validateMonth(null).isPresent());
    }

    // ========== validateDay Tests ==========

    @Test
    void testValidateDayValid() {
        // 유효한 일 / Valid days
        for (int day = 1; day <= 31; day++) {
            assertFalse(ValidationUtils.validateDay(day).isPresent());
        }
    }

    @Test
    void testValidateDayTooSmall() {
        // 1보다 작음 / Less than 1
        Optional<ValidationError> error = ValidationUtils.validateDay(0);

        assertTrue(error.isPresent());
        assertEquals("Day", error.get().field());
        assertTrue(error.get().message().contains("between 1 and 31"));
    }

    @Test
    void testValidateDayTooLarge() {
        // 31보다 큼 / Greater than 31
        Optional<ValidationError> error = ValidationUtils.validateDay(32);

        assertTrue(error.isPresent());
        assertEquals("Day", error.get().field());
        assertTrue(error.get().message().contains("between 1 and 31"));
    }

    @Test
    void testValidateDayNull() {
        // null은 검증하지 않음 / null is not validated
        assertFalse(ValidationUtils.validateDay(null).isPresent());
    }
}
