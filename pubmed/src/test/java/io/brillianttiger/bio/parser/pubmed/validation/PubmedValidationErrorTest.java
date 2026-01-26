package io.brillianttiger.bio.parser.pubmed.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PubmedValidationErrorTest / PubMed Validation Error 테스트
 *
 * KR: PubmedValidationError static factory 메서드 및 필드 테스트.
 * EN: Test PubmedValidationError static factory methods and fields.
 */
class PubmedValidationErrorTest {

    @Test
    @DisplayName("Test 1: error() factory method should create ERROR severity")
    void test01_errorFactoryMethod() {
        PubmedValidationError error = PubmedValidationError.error(
                PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                "Test message",
                "/test/location"
        );

        assertNotNull(error);
        assertEquals(PubmedValidationError.Severity.ERROR, error.getSeverity());
        assertEquals(PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT, error.getCode());
        assertEquals("Test message", error.getMessage());
        assertEquals("/test/location", error.getLocation());
    }

    @Test
    @DisplayName("Test 2: warning() factory method should create WARNING severity")
    void test02_warningFactoryMethod() {
        PubmedValidationError warning = PubmedValidationError.warning(
                PubmedValidationError.ErrorCode.INVALID_FORMAT,
                "Warning message",
                "/test/location"
        );

        assertNotNull(warning);
        assertEquals(PubmedValidationError.Severity.WARNING, warning.getSeverity());
        assertEquals(PubmedValidationError.ErrorCode.INVALID_FORMAT, warning.getCode());
        assertEquals("Warning message", warning.getMessage());
        assertEquals("/test/location", warning.getLocation());
    }

    @Test
    @DisplayName("Test 3: info() factory method should create INFO severity")
    void test03_infoFactoryMethod() {
        PubmedValidationError info = PubmedValidationError.info(
                PubmedValidationError.ErrorCode.VALIDATION_FAILURE,
                "Info message",
                "/test/location"
        );

        assertNotNull(info);
        assertEquals(PubmedValidationError.Severity.INFO, info.getSeverity());
        assertEquals(PubmedValidationError.ErrorCode.VALIDATION_FAILURE, info.getCode());
        assertEquals("Info message", info.getMessage());
        assertEquals("/test/location", info.getLocation());
    }

    @Test
    @DisplayName("Test 4: Builder should work correctly")
    void test04_builderPattern() {
        PubmedValidationError error = PubmedValidationError.builder()
                .severity(PubmedValidationError.Severity.ERROR)
                .code(PubmedValidationError.ErrorCode.INVALID_PMID_FORMAT)
                .message("Test")
                .location("/test")
                .details("Additional details")
                .build();

        assertNotNull(error);
        assertEquals("Additional details", error.getDetails());
    }

    @Test
    @DisplayName("Test 5: All Severity enum values should be accessible")
    void test05_severityEnumValues() {
        PubmedValidationError.Severity[] values = PubmedValidationError.Severity.values();
        assertEquals(3, values.length);
        assertEquals(PubmedValidationError.Severity.ERROR, PubmedValidationError.Severity.valueOf("ERROR"));
        assertEquals(PubmedValidationError.Severity.WARNING, PubmedValidationError.Severity.valueOf("WARNING"));
        assertEquals(PubmedValidationError.Severity.INFO, PubmedValidationError.Severity.valueOf("INFO"));
    }

    @Test
    @DisplayName("Test 6: All ErrorCode enum values should be accessible")
    void test06_errorCodeEnumValues() {
        PubmedValidationError.ErrorCode[] values = PubmedValidationError.ErrorCode.values();
        assertTrue(values.length > 0);

        // Test some key error codes
        assertNotNull(PubmedValidationError.ErrorCode.valueOf("MISSING_REQUIRED_ELEMENT"));
        assertNotNull(PubmedValidationError.ErrorCode.valueOf("INVALID_PMID_FORMAT"));
        assertNotNull(PubmedValidationError.ErrorCode.valueOf("INVALID_YEAR_RANGE"));
    }
}
