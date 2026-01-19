package com.brillianttiger.bio.parser.common.validation;

/**
 * ValidationError / 검증 오류 정보
 *
 * KR: 검증 오류 정보를 담는 불변 레코드.
 *     필드명, 오류 메시지, 심각도를 포함.
 * EN: Immutable record containing validation error information.
 *     Includes field name, error message, and severity.
 *
 * @param field 필드명 / Field name
 * @param message 오류 메시지 / Error message
 * @param severity 심각도 / Severity level
 */
public record ValidationError(
        String field,
        String message,
        Severity severity
) {
    /**
     * 오류 생성 헬퍼 메서드 / Error creation helper
     *
     * KR: ERROR 심각도의 검증 오류 생성
     * EN: Create validation error with ERROR severity
     *
     * @param field 필드명 / Field name
     * @param message 오류 메시지 / Error message
     * @return ValidationError 객체 / ValidationError object
     */
    public static ValidationError error(String field, String message) {
        return new ValidationError(field, message, Severity.ERROR);
    }

    /**
     * 경고 생성 헬퍼 메서드 / Warning creation helper
     *
     * KR: WARNING 심각도의 검증 오류 생성
     * EN: Create validation error with WARNING severity
     *
     * @param field 필드명 / Field name
     * @param message 오류 메시지 / Error message
     * @return ValidationError 객체 / ValidationError object
     */
    public static ValidationError warning(String field, String message) {
        return new ValidationError(field, message, Severity.WARNING);
    }

    /**
     * 정보 생성 헬퍼 메서드 / Info creation helper
     *
     * KR: INFO 심각도의 검증 오류 생성
     * EN: Create validation error with INFO severity
     *
     * @param field 필드명 / Field name
     * @param message 오류 메시지 / Error message
     * @return ValidationError 객체 / ValidationError object
     */
    public static ValidationError info(String field, String message) {
        return new ValidationError(field, message, Severity.INFO);
    }
}
