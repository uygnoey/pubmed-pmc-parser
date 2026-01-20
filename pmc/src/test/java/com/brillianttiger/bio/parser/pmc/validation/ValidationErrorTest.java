package com.brillianttiger.bio.parser.pmc.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ValidationError 클래스 테스트 / ValidationError class tests
 *
 * KR: ValidationError의 모든 기능을 테스트하여 100% 커버리지 달성
 * EN: Tests all ValidationError functionality to achieve 100% coverage
 */
@DisplayName("ValidationError 테스트")
class ValidationErrorTest {

    @Test
    @DisplayName("error() 팩토리 메서드 - ERROR 심각도")
    void testErrorFactoryMethod() {
        // Given
        String code = "MISSING_REQUIRED";
        String message = "Required element is missing";
        String location = "article-meta";

        // When
        ValidationError error = ValidationError.error(code, message, location);

        // Then
        assertNotNull(error);
        assertEquals(ValidationError.Severity.ERROR, error.getSeverity());
        assertEquals(code, error.getCode());
        assertEquals(message, error.getMessage());
        assertEquals(location, error.getLocation());
        assertNull(error.getDetails());
    }

    @Test
    @DisplayName("warning() 팩토리 메서드 - WARNING 심각도")
    void testWarningFactoryMethod() {
        // Given
        String code = "MISSING_RECOMMENDED";
        String message = "Recommended attribute is missing";
        String location = "article[@article-type]";

        // When
        ValidationError warning = ValidationError.warning(code, message, location);

        // Then
        assertNotNull(warning);
        assertEquals(ValidationError.Severity.WARNING, warning.getSeverity());
        assertEquals(code, warning.getCode());
        assertEquals(message, warning.getMessage());
        assertEquals(location, warning.getLocation());
        assertNull(warning.getDetails());
    }

    @Test
    @DisplayName("info() 팩토리 메서드 - INFO 심각도")
    void testInfoFactoryMethod() {
        // Given
        String code = "INFO_MESSAGE";
        String message = "Informational message";
        String location = "abstract";

        // When
        ValidationError info = ValidationError.info(code, message, location);

        // Then
        assertNotNull(info);
        assertEquals(ValidationError.Severity.INFO, info.getSeverity());
        assertEquals(code, info.getCode());
        assertEquals(message, info.getMessage());
        assertEquals(location, info.getLocation());
        assertNull(info.getDetails());
    }

    @Test
    @DisplayName("Builder 패턴 - 모든 필드 설정")
    void testBuilderWithAllFields() {
        // Given & When
        ValidationError error = ValidationError.builder()
                .severity(ValidationError.Severity.ERROR)
                .code("INVALID_FORMAT")
                .message("Invalid format detected")
                .location("article-id[@pub-id-type='doi']")
                .details("Expected format: 10.xxxx/xxxxx")
                .build();

        // Then
        assertNotNull(error);
        assertEquals(ValidationError.Severity.ERROR, error.getSeverity());
        assertEquals("INVALID_FORMAT", error.getCode());
        assertEquals("Invalid format detected", error.getMessage());
        assertEquals("article-id[@pub-id-type='doi']", error.getLocation());
        assertEquals("Expected format: 10.xxxx/xxxxx", error.getDetails());
    }

    @Test
    @DisplayName("Builder 패턴 - 필수 필드만 설정")
    void testBuilderWithRequiredFieldsOnly() {
        // Given & When
        ValidationError error = ValidationError.builder()
                .severity(ValidationError.Severity.WARNING)
                .code("WARN_CODE")
                .message("Warning message")
                .build();

        // Then
        assertNotNull(error);
        assertEquals(ValidationError.Severity.WARNING, error.getSeverity());
        assertEquals("WARN_CODE", error.getCode());
        assertEquals("Warning message", error.getMessage());
        assertNull(error.getLocation());
        assertNull(error.getDetails());
    }

    @Test
    @DisplayName("toString() - ERROR 심각도")
    void testToStringWithError() {
        // Given
        ValidationError error = ValidationError.error(
                "MISSING_TITLE",
                "Article title is required",
                "article-meta/title-group"
        );

        // When
        String result = error.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ERROR"));
        assertTrue(result.contains("MISSING_TITLE"));
        assertTrue(result.contains("Article title is required"));
        assertTrue(result.contains("article-meta/title-group"));
    }

    @Test
    @DisplayName("toString() - WARNING 심각도")
    void testToStringWithWarning() {
        // Given
        ValidationError warning = ValidationError.warning(
                "MISSING_ABSTRACT",
                "Abstract is recommended",
                "article-meta"
        );

        // When
        String result = warning.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("WARNING"));
        assertTrue(result.contains("MISSING_ABSTRACT"));
        assertTrue(result.contains("Abstract is recommended"));
        assertTrue(result.contains("article-meta"));
    }

    @Test
    @DisplayName("toString() - INFO 심각도")
    void testToStringWithInfo() {
        // Given
        ValidationError info = ValidationError.info(
                "INFO_CODE",
                "Informational message",
                "body"
        );

        // When
        String result = info.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("INFO"));
        assertTrue(result.contains("INFO_CODE"));
        assertTrue(result.contains("Informational message"));
        assertTrue(result.contains("body"));
    }

    @Test
    @DisplayName("toString() - details 포함")
    void testToStringWithDetails() {
        // Given
        ValidationError error = ValidationError.builder()
                .severity(ValidationError.Severity.ERROR)
                .code("INVALID_DOI")
                .message("DOI format is invalid")
                .location("article-id[@pub-id-type='doi']")
                .details("Expected format: 10.xxxx/xxxxx, got: invalid-doi")
                .build();

        // When
        String result = error.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("ERROR"));
        assertTrue(result.contains("INVALID_DOI"));
        assertTrue(result.contains("DOI format is invalid"));
        assertTrue(result.contains("article-id[@pub-id-type='doi']"));
        assertTrue(result.contains("Expected format: 10.xxxx/xxxxx, got: invalid-doi"));
    }

    @Test
    @DisplayName("Severity enum - 모든 값 존재")
    void testSeverityEnum() {
        // When & Then
        assertEquals(3, ValidationError.Severity.values().length);
        assertNotNull(ValidationError.Severity.valueOf("ERROR"));
        assertNotNull(ValidationError.Severity.valueOf("WARNING"));
        assertNotNull(ValidationError.Severity.valueOf("INFO"));
    }

    @Test
    @DisplayName("equals() and hashCode() - 동일 객체")
    void testEqualsAndHashCodeWithSameObject() {
        // Given
        ValidationError error1 = ValidationError.error("CODE1", "Message1", "Location1");
        ValidationError error2 = ValidationError.error("CODE1", "Message1", "Location1");

        // When & Then
        assertEquals(error1, error2);
        assertEquals(error1.hashCode(), error2.hashCode());
    }

    @Test
    @DisplayName("equals() - 다른 객체")
    void testEqualsWithDifferentObject() {
        // Given
        ValidationError error1 = ValidationError.error("CODE1", "Message1", "Location1");
        ValidationError error2 = ValidationError.error("CODE2", "Message2", "Location2");

        // When & Then
        assertNotEquals(error1, error2);
    }

    @Test
    @DisplayName("Setter 테스트 - severity")
    void testSetSeverity() {
        // Given
        ValidationError error = ValidationError.builder()
                .severity(ValidationError.Severity.INFO)
                .code("CODE")
                .message("Message")
                .build();

        // When
        error.setSeverity(ValidationError.Severity.ERROR);

        // Then
        assertEquals(ValidationError.Severity.ERROR, error.getSeverity());
    }

    @Test
    @DisplayName("Setter 테스트 - code")
    void testSetCode() {
        // Given
        ValidationError error = ValidationError.error("OLD_CODE", "Message", "Location");

        // When
        error.setCode("NEW_CODE");

        // Then
        assertEquals("NEW_CODE", error.getCode());
    }

    @Test
    @DisplayName("Setter 테스트 - message")
    void testSetMessage() {
        // Given
        ValidationError error = ValidationError.error("CODE", "Old Message", "Location");

        // When
        error.setMessage("New Message");

        // Then
        assertEquals("New Message", error.getMessage());
    }

    @Test
    @DisplayName("Setter 테스트 - location")
    void testSetLocation() {
        // Given
        ValidationError error = ValidationError.error("CODE", "Message", "Old Location");

        // When
        error.setLocation("New Location");

        // Then
        assertEquals("New Location", error.getLocation());
    }

    @Test
    @DisplayName("Setter 테스트 - details")
    void testSetDetails() {
        // Given
        ValidationError error = ValidationError.error("CODE", "Message", "Location");

        // When
        error.setDetails("New Details");

        // Then
        assertEquals("New Details", error.getDetails());
    }

    @Test
    @DisplayName("NoArgsConstructor 테스트")
    void testNoArgsConstructor() {
        // When
        ValidationError error = new ValidationError();

        // Then
        assertNotNull(error);
        assertNull(error.getSeverity());
        assertNull(error.getCode());
        assertNull(error.getMessage());
        assertNull(error.getLocation());
        assertNull(error.getDetails());
    }

    @Test
    @DisplayName("AllArgsConstructor 테스트")
    void testAllArgsConstructor() {
        // When
        ValidationError error = new ValidationError(
                ValidationError.Severity.ERROR,
                "CODE",
                "Message",
                "Location",
                "Details"
        );

        // Then
        assertNotNull(error);
        assertEquals(ValidationError.Severity.ERROR, error.getSeverity());
        assertEquals("CODE", error.getCode());
        assertEquals("Message", error.getMessage());
        assertEquals("Location", error.getLocation());
        assertEquals("Details", error.getDetails());
    }

    // ========================================
    // ErrorCode 상수 테스트
    // ========================================

    @Test
    @DisplayName("ErrorCode - 필수 요소 관련 상수")
    void testErrorCodeRequiredElements() {
        // When & Then
        assertEquals("MISSING_REQUIRED_ELEMENT", ValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT);
        assertEquals("MISSING_FRONT", ValidationError.ErrorCode.MISSING_FRONT);
        assertEquals("MISSING_ARTICLE_META", ValidationError.ErrorCode.MISSING_ARTICLE_META);
        assertEquals("MISSING_TITLE_GROUP", ValidationError.ErrorCode.MISSING_TITLE_GROUP);
        assertEquals("MISSING_ARTICLE_TITLE", ValidationError.ErrorCode.MISSING_ARTICLE_TITLE);
    }

    @Test
    @DisplayName("ErrorCode - ID 포맷 관련 상수")
    void testErrorCodeIdFormats() {
        // When & Then
        assertEquals("INVALID_DOI_FORMAT", ValidationError.ErrorCode.INVALID_DOI_FORMAT);
        assertEquals("INVALID_PMCID_FORMAT", ValidationError.ErrorCode.INVALID_PMCID_FORMAT);
        assertEquals("INVALID_PMID_FORMAT", ValidationError.ErrorCode.INVALID_PMID_FORMAT);
        assertEquals("INVALID_ORCID_FORMAT", ValidationError.ErrorCode.INVALID_ORCID_FORMAT);
    }

    @Test
    @DisplayName("ErrorCode - 참조 무결성 관련 상수")
    void testErrorCodeReferenceIntegrity() {
        // When & Then
        assertEquals("BROKEN_XREF_REFERENCE", ValidationError.ErrorCode.BROKEN_XREF_REFERENCE);
        assertEquals("BROKEN_AFF_REFERENCE", ValidationError.ErrorCode.BROKEN_AFF_REFERENCE);
        assertEquals("BROKEN_FN_REFERENCE", ValidationError.ErrorCode.BROKEN_FN_REFERENCE);
    }

    @Test
    @DisplayName("ErrorCode - 권장 속성 관련 상수")
    void testErrorCodeRecommendedAttributes() {
        // When & Then
        assertEquals("MISSING_RECOMMENDED_ATTRIBUTE", ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE);
        assertEquals("INVALID_ATTRIBUTE_VALUE", ValidationError.ErrorCode.INVALID_ATTRIBUTE_VALUE);
    }

    @Test
    @DisplayName("ErrorCode 상수를 ValidationError와 함께 사용")
    void testErrorCodeWithValidationError() {
        // When
        ValidationError error1 = ValidationError.error(
                ValidationError.ErrorCode.MISSING_FRONT,
                "Front is missing",
                "/article"
        );

        ValidationError error2 = ValidationError.error(
                ValidationError.ErrorCode.INVALID_DOI_FORMAT,
                "Invalid DOI",
                "/article/front/article-meta/article-id"
        );

        // Then
        assertEquals(ValidationError.ErrorCode.MISSING_FRONT, error1.getCode());
        assertEquals(ValidationError.ErrorCode.INVALID_DOI_FORMAT, error2.getCode());
    }

    // ========================================
    // Missing Coverage Tests (Line 174, 177, 68)
    // ========================================

    @Test
    @DisplayName("toString() - location이 null일 때")
    void testToStringWithNullLocation() {
        // Given: location이 null인 ValidationError
        ValidationError error = ValidationError.builder()
                .severity(ValidationError.Severity.ERROR)
                .code("ERROR_CODE")
                .message("Error message")
                .location(null)  // null location
                .details("Some details")
                .build();

        // When
        String result = error.toString();

        // Then: location 부분이 포함되지 않아야 함
        assertNotNull(result);
        assertTrue(result.contains("ERROR"));
        assertTrue(result.contains("ERROR_CODE"));
        assertTrue(result.contains("Error message"));
        assertTrue(result.contains("Some details"));
        assertFalse(result.contains("(at:")); // location이 null이므로 "(at:" 문자열이 없어야 함
    }

    @Test
    @DisplayName("toString() - location이 empty일 때")
    void testToStringWithEmptyLocation() {
        // Given: location이 empty string인 ValidationError
        ValidationError error = ValidationError.builder()
                .severity(ValidationError.Severity.WARNING)
                .code("WARN_CODE")
                .message("Warning message")
                .location("")  // empty location
                .details("Some details")
                .build();

        // When
        String result = error.toString();

        // Then: location 부분이 포함되지 않아야 함
        assertNotNull(result);
        assertTrue(result.contains("WARNING"));
        assertTrue(result.contains("WARN_CODE"));
        assertTrue(result.contains("Warning message"));
        assertTrue(result.contains("Some details"));
        assertFalse(result.contains("(at:")); // location이 empty이므로 "(at:" 문자열이 없어야 함
    }

    @Test
    @DisplayName("toString() - details가 empty일 때")
    void testToStringWithEmptyDetails() {
        // Given: details가 empty string인 ValidationError
        ValidationError error = ValidationError.builder()
                .severity(ValidationError.Severity.INFO)
                .code("INFO_CODE")
                .message("Info message")
                .location("/article/front")
                .details("")  // empty details
                .build();

        // When
        String result = error.toString();

        // Then: details 부분이 포함되지 않아야 함
        assertNotNull(result);
        assertTrue(result.contains("INFO"));
        assertTrue(result.contains("INFO_CODE"));
        assertTrue(result.contains("Info message"));
        assertTrue(result.contains("/article/front"));
        // details가 empty이므로 " - " 구분자 뒤에 내용이 없어야 함
        // (더 정확한 검증을 위해 " - " 자체가 없는지 확인)
        int dashIndex = result.indexOf(" - ");
        assertTrue(dashIndex == -1 || result.substring(dashIndex + 3).trim().isEmpty());
    }

    @Test
    @DisplayName("toString() - location과 details 모두 null일 때")
    void testToStringWithNullLocationAndDetails() {
        // Given: location과 details 모두 null
        ValidationError error = ValidationError.builder()
                .severity(ValidationError.Severity.ERROR)
                .code("SIMPLE_ERROR")
                .message("Simple error message")
                .location(null)  // null location
                .details(null)   // null details
                .build();

        // When
        String result = error.toString();

        // Then: 기본 정보만 포함
        assertNotNull(result);
        assertTrue(result.contains("ERROR"));
        assertTrue(result.contains("SIMPLE_ERROR"));
        assertTrue(result.contains("Simple error message"));
        assertFalse(result.contains("(at:"));
        assertFalse(result.contains(" - "));
    }

    @Test
    @DisplayName("ErrorCode 인스턴스 생성 (inner class coverage)")
    void testErrorCodeInnerClassCoverage() {
        // When: ErrorCode 인스턴스 생성 (inner class를 커버하기 위해)
        ValidationError.ErrorCode errorCodeInstance = new ValidationError.ErrorCode();

        // Then: 인스턴스가 생성되어야 함
        assertNotNull(errorCodeInstance);

        // ErrorCode 상수들이 제대로 접근 가능한지 확인
        assertNotNull(ValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT);
        assertNotNull(ValidationError.ErrorCode.INVALID_DOI_FORMAT);
        assertNotNull(ValidationError.ErrorCode.BROKEN_XREF_REFERENCE);
    }
}
