package io.brillianttiger.bio.parser.pmc.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ValidationError / 검증 오류
 *
 * KR: JATS article 검증 시 발견된 오류 정보.
 * EN: Error information found during JATS article validation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    /**
     * 오류 심각도 / Error severity
     */
    private Severity severity;

    /**
     * 오류 코드 / Error code
     */
    private String code;

    /**
     * 오류 메시지 / Error message
     */
    private String message;

    /**
     * 오류 위치 (XPath 스타일) / Error location (XPath style)
     */
    private String location;

    /**
     * 오류 세부 정보 / Error details
     */
    private String details;

    /**
     * 오류 심각도 열거형 / Error severity enum
     */
    public enum Severity {
        /**
         * 오류 - 반드시 수정 필요 / Error - must be fixed
         */
        ERROR,

        /**
         * 경고 - 수정 권장 / Warning - recommended to fix
         */
        WARNING,

        /**
         * 정보 - 참고 사항 / Info - informational
         */
        INFO
    }

    /**
     * 오류 코드 상수 / Error code constants
     */
    public static class ErrorCode {
        // 필수 요소 누락 / Missing required elements
        public static final String MISSING_REQUIRED_ELEMENT = "MISSING_REQUIRED_ELEMENT";
        public static final String MISSING_FRONT = "MISSING_FRONT";
        public static final String MISSING_ARTICLE_META = "MISSING_ARTICLE_META";
        public static final String MISSING_TITLE_GROUP = "MISSING_TITLE_GROUP";
        public static final String MISSING_ARTICLE_TITLE = "MISSING_ARTICLE_TITLE";

        // ID 형식 오류 / ID format errors
        public static final String INVALID_DOI_FORMAT = "INVALID_DOI_FORMAT";
        public static final String INVALID_PMCID_FORMAT = "INVALID_PMCID_FORMAT";
        public static final String INVALID_PMID_FORMAT = "INVALID_PMID_FORMAT";
        public static final String INVALID_ORCID_FORMAT = "INVALID_ORCID_FORMAT";

        // 참조 무결성 오류 / Reference integrity errors
        public static final String BROKEN_XREF_REFERENCE = "BROKEN_XREF_REFERENCE";
        public static final String BROKEN_AFF_REFERENCE = "BROKEN_AFF_REFERENCE";
        public static final String BROKEN_FN_REFERENCE = "BROKEN_FN_REFERENCE";

        // 속성 오류 / Attribute errors
        public static final String MISSING_RECOMMENDED_ATTRIBUTE = "MISSING_RECOMMENDED_ATTRIBUTE";
        public static final String INVALID_ATTRIBUTE_VALUE = "INVALID_ATTRIBUTE_VALUE";

        // 네임스페이스 오류 / Namespace errors
        public static final String INVALID_XLINK_HREF = "INVALID_XLINK_HREF";
        public static final String MISSING_NAMESPACE = "MISSING_NAMESPACE";
    }

    /**
     * ERROR 심각도 검증 오류 생성 / Create ERROR severity validation error
     */
    public static ValidationError error(String code, String message, String location) {
        return ValidationError.builder()
                .severity(Severity.ERROR)
                .code(code)
                .message(message)
                .location(location)
                .build();
    }

    /**
     * ERROR 심각도 검증 오류 생성 (세부 정보 포함) / Create ERROR severity validation error (with details)
     */
    public static ValidationError error(String code, String message, String location, String details) {
        return ValidationError.builder()
                .severity(Severity.ERROR)
                .code(code)
                .message(message)
                .location(location)
                .details(details)
                .build();
    }

    /**
     * WARNING 심각도 검증 오류 생성 / Create WARNING severity validation error
     */
    public static ValidationError warning(String code, String message, String location) {
        return ValidationError.builder()
                .severity(Severity.WARNING)
                .code(code)
                .message(message)
                .location(location)
                .build();
    }

    /**
     * WARNING 심각도 검증 오류 생성 (세부 정보 포함) / Create WARNING severity validation error (with details)
     */
    public static ValidationError warning(String code, String message, String location, String details) {
        return ValidationError.builder()
                .severity(Severity.WARNING)
                .code(code)
                .message(message)
                .location(location)
                .details(details)
                .build();
    }

    /**
     * INFO 심각도 검증 오류 생성 / Create INFO severity validation error
     */
    public static ValidationError info(String code, String message, String location) {
        return ValidationError.builder()
                .severity(Severity.INFO)
                .code(code)
                .message(message)
                .location(location)
                .build();
    }

    public static ValidationError info(String code, String message, String location, String details) {
        return ValidationError.builder()
                .severity(Severity.INFO)
                .code(code)
                .message(message)
                .location(location)
                .details(details)
                .build();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(severity).append("] ");
        sb.append(code).append(": ");
        sb.append(message);
        if (location != null && !location.isEmpty()) {
            sb.append(" (at: ").append(location).append(")");
        }
        if (details != null && !details.isEmpty()) {
            sb.append(" - ").append(details);
        }
        return sb.toString();
    }
}
