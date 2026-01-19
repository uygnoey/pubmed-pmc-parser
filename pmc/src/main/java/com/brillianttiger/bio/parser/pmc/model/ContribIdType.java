package com.brillianttiger.bio.parser.pmc.model;

import lombok.Getter;

/**
 * ContribIdType / 기여자 ID 유형 Enum
 *
 * KR: 기여자 식별자의 유형을 나타내는 열거형.
 *     ORCID, ISNI, Scopus 등 다양한 기여자 식별 체계 지원.
 * EN: Enumeration representing contributor identifier types.
 *     Supports various contributor identification systems such as ORCID, ISNI, Scopus, etc.
 *
 * DTD: <!ATTLIST contrib-id
 *          contrib-id-type CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/contrib-id-type.html
 *
 * Common values:
 * - orcid: Open Researcher and Contributor ID
 * - isni: International Standard Name Identifier
 * - scopus: Scopus Author ID
 * - researcher-id: ResearcherID (Web of Science)
 * - wos-researcher-id: Web of Science ResearcherID
 */
@Getter
public enum ContribIdType {
    /**
     * ORCID (Open Researcher and Contributor ID)
     *
     * KR: 오픈 연구자 및 기여자 ID - 고유한 연구자 식별자
     * EN: Open Researcher and Contributor ID - unique researcher identifier
     *
     * Example: 0000-0001-2345-6789
     */
    ORCID("orcid"),

    /**
     * ISNI (International Standard Name Identifier)
     *
     * KR: 국제 표준 이름 식별자
     * EN: International Standard Name Identifier
     *
     * Example: 0000 0001 2345 6789
     */
    ISNI("isni"),

    /**
     * Scopus Author ID
     *
     * KR: Scopus 저자 ID
     * EN: Scopus Author ID
     *
     * Example: 12345678900
     */
    SCOPUS("scopus"),

    /**
     * ResearcherID (Web of Science)
     *
     * KR: ResearcherID (Web of Science 플랫폼)
     * EN: ResearcherID (Web of Science platform)
     *
     * Example: A-1234-2023
     */
    RESEARCHER_ID("researcher-id"),

    /**
     * Web of Science ResearcherID
     *
     * KR: Web of Science ResearcherID (명시적 형식)
     * EN: Web of Science ResearcherID (explicit form)
     */
    WOS_RESEARCHER_ID("wos-researcher-id"),

    /**
     * Other / Unknown contributor ID type
     *
     * KR: 기타 또는 알 수 없는 기여자 ID 유형
     * EN: Other or unknown contributor ID type
     */
    OTHER("other");

    private final String value;

    ContribIdType(String value) {
        this.value = value;
    }

    /**
     * 문자열 값으로부터 ContribIdType enum을 반환 / Return ContribIdType enum from string value
     *
     * KR: 주어진 문자열 값에 해당하는 ContribIdType을 반환.
     *     매칭되는 값이 없으면 OTHER를 반환.
     * EN: Returns the ContribIdType corresponding to the given string value.
     *     Returns OTHER if no matching value is found.
     *
     * @param value 기여자 ID 유형 문자열 / Contributor ID type string
     * @return ContribIdType enum 값 / ContribIdType enum value
     */
    public static ContribIdType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (ContribIdType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }

        return OTHER;
    }
}
