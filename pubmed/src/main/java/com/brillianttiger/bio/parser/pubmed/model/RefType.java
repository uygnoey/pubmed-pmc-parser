package com.brillianttiger.bio.parser.pubmed.model;

/**
 * RefType / 참조 유형
 *
 * DTD: RefType (
 *          AssociatedDataset | AssociatedPublication |
 *          CommentIn | CommentOn |
 *          CorrectedandRepublishedIn | CorrectedandRepublishedFrom |
 *          ErratumIn | ErratumFor |
 *          ExpressionOfConcernIn | ExpressionOfConcernFor |
 *          RepublishedIn | RepublishedFrom |
 *          RetractedandRepublishedIn | RetractedandRepublishedFrom |
 *          RetractionIn | RetractionOf |
 *          UpdateIn | UpdateOf |
 *          SummaryForPatientsIn | OriginalReportIn |
 *          ReprintIn | ReprintOf |
 *          Cites
 *      ) #REQUIRED
 *
 * KR: 코멘트/정정 참조 유형
 * EN: Comment/correction reference type
 */
public enum RefType {

    /**
     * 관련 데이터셋 / Associated dataset
     */
    ASSOCIATED_DATASET("AssociatedDataset"),

    /**
     * 관련 출판물 / Associated publication
     */
    ASSOCIATED_PUBLICATION("AssociatedPublication"),

    /**
     * ~에 코멘트됨 / Comment in
     */
    COMMENT_IN("CommentIn"),

    /**
     * ~를 코멘트함 / Comment on
     */
    COMMENT_ON("CommentOn"),

    /**
     * ~에서 정정 및 재출판됨 / Corrected and republished in
     */
    CORRECTED_AND_REPUBLISHED_IN("CorrectedandRepublishedIn"),

    /**
     * ~를 정정 및 재출판함 / Corrected and republished from
     */
    CORRECTED_AND_REPUBLISHED_FROM("CorrectedandRepublishedFrom"),

    /**
     * ~에 오류 정정됨 / Erratum in
     */
    ERRATUM_IN("ErratumIn"),

    /**
     * ~의 오류 정정 / Erratum for
     */
    ERRATUM_FOR("ErratumFor"),

    /**
     * ~에서 우려 표명됨 / Expression of concern in
     */
    EXPRESSION_OF_CONCERN_IN("ExpressionOfConcernIn"),

    /**
     * ~에 대한 우려 표명 / Expression of concern for
     */
    EXPRESSION_OF_CONCERN_FOR("ExpressionOfConcernFor"),

    /**
     * ~에 재출판됨 / Republished in
     */
    REPUBLISHED_IN("RepublishedIn"),

    /**
     * ~를 재출판함 / Republished from
     */
    REPUBLISHED_FROM("RepublishedFrom"),

    /**
     * ~에서 철회 및 재출판됨 / Retracted and republished in
     */
    RETRACTED_AND_REPUBLISHED_IN("RetractedandRepublishedIn"),

    /**
     * ~를 철회 및 재출판함 / Retracted and republished from
     */
    RETRACTED_AND_REPUBLISHED_FROM("RetractedandRepublishedFrom"),

    /**
     * ~에서 철회됨 / Retraction in
     */
    RETRACTION_IN("RetractionIn"),

    /**
     * ~를 철회함 / Retraction of
     */
    RETRACTION_OF("RetractionOf"),

    /**
     * ~에서 업데이트됨 / Update in
     */
    UPDATE_IN("UpdateIn"),

    /**
     * ~를 업데이트함 / Update of
     */
    UPDATE_OF("UpdateOf"),

    /**
     * 인용 / Cites
     */
    CITES("Cites"),

    /**
     * ~에서 환자용 요약됨 / Summary for patients in
     */
    SUMMARY_FOR_PATIENTS_IN("SummaryForPatientsIn"),

    /**
     * ~에서 원본 보고됨 / Original report in
     */
    ORIGINAL_REPORT_IN("OriginalReportIn"),

    /**
     * ~에서 재인쇄됨 / Reprint in
     */
    REPRINT_IN("ReprintIn"),

    /**
     * ~를 재인쇄함 / Reprint of
     */
    REPRINT_OF("ReprintOf");

    private final String value;

    RefType(String value) {
        this.value = value;
    }

    /**
     * 문자열 값 반환 / Get string value
     *
     * @return DTD 문자열 값 / DTD string value
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 RefType enum 변환 / Parse string to RefType enum
     *
     * KR: XML 파싱 시 문자열을 enum으로 변환
     * EN: Convert string to enum during XML parsing
     *
     * @param value DTD 문자열 값 / DTD string value
     * @return RefType enum 또는 null / RefType enum or null
     */
    public static RefType fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (RefType type : RefType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        // 알 수 없는 값인 경우 null 반환 / Return null for unknown values
        return null;
    }
}
