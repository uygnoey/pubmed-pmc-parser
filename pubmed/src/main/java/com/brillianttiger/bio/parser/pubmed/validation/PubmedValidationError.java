package com.brillianttiger.bio.parser.pubmed.validation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubmedValidationError / PubMed 검증 오류
 *
 * KR: PubMed article 검증 시 발견된 오류 정보.
 *     PMC JatsArticleValidator와 동일한 구조.
 * EN: Error information found during PubMed article validation.
 *     Same structure as PMC JatsArticleValidator.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubmedValidationError {

    /**
     * 오류 심각도 / Error severity
     */
    private Severity severity;

    /**
     * 오류 코드 / Error code
     */
    private ErrorCode code;

    /**
     * 오류 메시지 / Error message
     */
    private String message;

    /**
     * 오류 위치 (XPath 스타일) / Error location (XPath style)
     *
     * 예: /PubmedArticleSet/PubmedArticle[1]/MedlineCitation/PMID
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
         * 정보 - 참고용 / Info - for information only
         */
        INFO
    }

    /**
     * 오류 코드 열거형 / Error code enum
     *
     * KR: PubMed DTD 2025.01.01 기준 검증 오류 코드.
     * EN: Validation error codes based on PubMed DTD 2025.01.01.
     */
    public enum ErrorCode {
        // ===== 필수 요소 오류 / Required Element Errors =====
        /**
         * 필수 요소 누락 / Missing required element
         */
        MISSING_REQUIRED_ELEMENT,

        /**
         * 필수 속성 누락 / Missing required attribute
         */
        MISSING_REQUIRED_ATTRIBUTE,

        /**
         * 빈 필수 요소 / Empty required element
         */
        EMPTY_REQUIRED_ELEMENT,

        // ===== ID 형식 오류 / ID Format Errors =====
        /**
         * 잘못된 PMID 형식 / Invalid PMID format
         */
        INVALID_PMID_FORMAT,

        /**
         * 잘못된 DOI 형식 / Invalid DOI format
         */
        INVALID_DOI_FORMAT,

        /**
         * 잘못된 ORCID 형식 / Invalid ORCID format
         */
        INVALID_ORCID_FORMAT,

        /**
         * 잘못된 MeSH UI 형식 / Invalid MeSH UI format
         */
        INVALID_MESH_UI_FORMAT,

        // ===== 날짜 범위 오류 / Date Range Errors =====
        /**
         * 잘못된 연도 범위 / Invalid year range
         */
        INVALID_YEAR_RANGE,

        /**
         * 잘못된 월 범위 / Invalid month range
         */
        INVALID_MONTH_RANGE,

        /**
         * 잘못된 일 범위 / Invalid day range
         */
        INVALID_DAY_RANGE,

        /**
         * 날짜 검증 실패 / Date validation failure
         */
        DATE_VALIDATION_FAILURE,

        // ===== 기타 오류 / Other Errors =====
        /**
         * 잘못된 형식 / Invalid format
         */
        INVALID_FORMAT,

        /**
         * 검증 실패 / Validation failure
         */
        VALIDATION_FAILURE
    }

    // ===== 편의 메서드 / Convenience Methods =====

    /**
     * ERROR 레벨 오류 생성 / Create ERROR level error
     */
    public static PubmedValidationError error(ErrorCode code, String message, String location) {
        return PubmedValidationError.builder()
                .severity(Severity.ERROR)
                .code(code)
                .message(message)
                .location(location)
                .build();
    }

    /**
     * WARNING 레벨 오류 생성 / Create WARNING level error
     */
    public static PubmedValidationError warning(ErrorCode code, String message, String location) {
        return PubmedValidationError.builder()
                .severity(Severity.WARNING)
                .code(code)
                .message(message)
                .location(location)
                .build();
    }

    /**
     * INFO 레벨 오류 생성 / Create INFO level error
     */
    public static PubmedValidationError info(ErrorCode code, String message, String location) {
        return PubmedValidationError.builder()
                .severity(Severity.INFO)
                .code(code)
                .message(message)
                .location(location)
                .build();
    }
}
