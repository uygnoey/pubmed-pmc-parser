package com.brillianttiger.bio.parser.pmc.model;

/**
 * FnType / 각주 유형
 *
 * KR: JATS fn-type 속성 값을 나타내는 열거형.
 *     각주의 종류를 분류하는 데 사용.
 * EN: Enumeration representing JATS fn-type attribute values.
 *     Used to classify the type of footnote.
 *
 * DTD: fn-type CDATA #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/fn-type.html
 *
 * Common Values:
 * - abbr: Abbreviation or acronym
 * - author: Author-related footnote
 * - con: Contributor footnote
 * - conflict: Conflict of interest
 * - corresp: Corresponding author
 * - current-aff: Current affiliation
 * - deceased: Deceased author
 * - edited-by: Edited by
 * - equal: Equal contribution
 * - financial-disclosure: Financial disclosure
 * - on-leave: On leave
 * - participating-researchers: Participating researchers
 * - present-address: Present address
 * - presented-at: Presented at (conference)
 * - previously-at: Previously at
 * - study-group-members: Study group members
 * - supplementary-material: Supplementary material
 * - supported-by: Supported by
 * - other: Other footnote type
 */
public enum FnType {

    /**
     * 약어 / Abbreviation
     * KR: 약어 또는 두문자어
     * EN: Abbreviation or acronym
     */
    ABBR("abbr"),

    /**
     * 저자 관련 / Author-related
     * KR: 저자 관련 각주
     * EN: Author-related footnote
     */
    AUTHOR("author"),

    /**
     * 기여자 / Contributor
     * KR: 기여자 각주
     * EN: Contributor footnote
     */
    CON("con"),

    /**
     * 이해 충돌 / Conflict of interest
     * KR: 이해 충돌 선언
     * EN: Conflict of interest statement
     */
    CONFLICT("conflict"),

    /**
     * 교신저자 / Corresponding author
     * KR: 교신저자 정보
     * EN: Corresponding author information
     */
    CORRESP("corresp"),

    /**
     * 현재 소속 / Current affiliation
     * KR: 현재 소속 기관
     * EN: Current affiliation
     */
    CURRENT_AFF("current-aff"),

    /**
     * 사망 / Deceased
     * KR: 사망한 저자 표시
     * EN: Deceased author indicator
     */
    DECEASED("deceased"),

    /**
     * 편집자 / Edited by
     * KR: 편집자 정보
     * EN: Edited by information
     */
    EDITED_BY("edited-by"),

    /**
     * 동등 기여 / Equal contribution
     * KR: 동등하게 기여한 저자
     * EN: Authors who contributed equally
     */
    EQUAL("equal"),

    /**
     * 재정 공개 / Financial disclosure
     * KR: 재정적 관계 공개
     * EN: Financial disclosure statement
     */
    FINANCIAL_DISCLOSURE("financial-disclosure"),

    /**
     * 휴직 중 / On leave
     * KR: 휴직 중인 저자
     * EN: Author currently on leave
     */
    ON_LEAVE("on-leave"),

    /**
     * 참여 연구자 / Participating researchers
     * KR: 연구에 참여한 연구자 목록
     * EN: List of participating researchers
     */
    PARTICIPATING_RESEARCHERS("participating-researchers"),

    /**
     * 현재 주소 / Present address
     * KR: 현재 주소
     * EN: Present address
     */
    PRESENT_ADDRESS("present-address"),

    /**
     * 발표 장소 / Presented at
     * KR: 발표된 학회나 회의
     * EN: Conference or meeting where presented
     */
    PRESENTED_AT("presented-at"),

    /**
     * 이전 소속 / Previously at
     * KR: 이전 소속 기관
     * EN: Previous affiliation
     */
    PREVIOUSLY_AT("previously-at"),

    /**
     * 연구 그룹 구성원 / Study group members
     * KR: 연구 그룹 구성원 목록
     * EN: List of study group members
     */
    STUDY_GROUP_MEMBERS("study-group-members"),

    /**
     * 보충 자료 / Supplementary material
     * KR: 보충 자료 관련 각주
     * EN: Supplementary material footnote
     */
    SUPPLEMENTARY_MATERIAL("supplementary-material"),

    /**
     * 지원 정보 / Supported by
     * KR: 지원 기관이나 펀딩 정보
     * EN: Funding or support information
     */
    SUPPORTED_BY("supported-by"),

    /**
     * 기타 / Other
     * KR: 기타 각주 유형
     * EN: Other footnote types
     */
    OTHER("other");

    private final String value;

    FnType(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return fn-type 값 (예: "corresp")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 FnType 변환 / Convert from string to FnType
     *
     * KR: XML에서 파싱한 문자열을 FnType enum으로 변환.
     *     매칭되는 값이 없으면 OTHER 반환.
     * EN: Converts parsed string from XML to FnType enum.
     *     Returns OTHER if no matching value found.
     *
     * @param value fn-type 속성 값 / fn-type attribute value
     * @return 해당하는 FnType, 없으면 OTHER / Corresponding FnType, or OTHER if not found
     */
    public static FnType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (FnType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }

        return OTHER;
    }

    @Override
    public String toString() {
        return value;
    }
}
