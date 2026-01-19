package com.brillianttiger.bio.parser.pmc.model;

import lombok.Getter;

/**
 * PubIdType / 출판 ID 유형
 *
 * KR: 논문 ID의 유형을 나타내는 열거형.
 *     DOI, PMID, PMC ID 등 다양한 식별자 유형을 포함.
 * EN: Enumeration representing publication ID type.
 *     Includes various identifier types such as DOI, PMID, PMC ID, etc.
 *
 * DTD: <!ATTLIST article-id
 *          pub-id-type CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/pub-id-type.html
 *
 * Common Values:
 * - doi: Digital Object Identifier
 * - pmid: PubMed ID
 * - pmcid: PubMed Central ID
 * - pmc-uid: PMC Unique ID
 * - publisher-id: Publisher's ID
 * - manuscript: Manuscript ID
 * - ark: Archival Resource Key
 * - art-access-id: Article Access ID
 */
@Getter
public enum PubIdType {
    /**
     * DOI (Digital Object Identifier)
     *
     * KR: 디지털 객체 식별자
     * EN: Digital Object Identifier
     *
     * Example: 10.1371/journal.pone.0123456
     */
    DOI("doi"),

    /**
     * PMID (PubMed ID)
     *
     * KR: PubMed 고유 ID
     * EN: PubMed unique identifier
     *
     * Example: 12345678
     */
    PMID("pmid"),

    /**
     * PMCID (PubMed Central ID)
     *
     * KR: PubMed Central 고유 ID
     * EN: PubMed Central unique identifier
     *
     * Example: PMC1234567
     */
    PMCID("pmcid"),

    /**
     * PMC-UID (PMC Unique ID)
     *
     * KR: PMC 고유 식별자
     * EN: PMC unique identifier
     */
    PMC_UID("pmc-uid"),

    /**
     * Publisher ID
     *
     * KR: 출판사 고유 ID
     * EN: Publisher's unique identifier
     */
    PUBLISHER_ID("publisher-id"),

    /**
     * Manuscript ID
     *
     * KR: 원고 ID
     * EN: Manuscript identifier
     */
    MANUSCRIPT("manuscript"),

    /**
     * ARK (Archival Resource Key)
     *
     * KR: 아카이브 리소스 키
     * EN: Archival Resource Key
     */
    ARK("ark"),

    /**
     * Article Access ID
     *
     * KR: 논문 접근 ID
     * EN: Article access identifier
     */
    ART_ACCESS_ID("art-access-id"),

    /**
     * arXiv ID
     *
     * KR: arXiv 프리프린트 ID
     * EN: arXiv preprint identifier
     */
    ARXIV("arxiv"),

    /**
     * PII (Publisher Item Identifier)
     *
     * KR: 출판사 항목 식별자
     * EN: Publisher Item Identifier
     */
    PII("pii"),

    /**
     * Other / Unknown
     *
     * KR: 기타 또는 알 수 없는 유형
     * EN: Other or unknown type
     */
    OTHER("other");

    private final String value;

    PubIdType(String value) {
        this.value = value;
    }

    /**
     * 문자열 값으로부터 PubIdType을 찾아 반환 / Find and return PubIdType from string value
     *
     * @param value 문자열 값 / String value
     * @return 매칭되는 PubIdType, 없으면 OTHER / Matching PubIdType, or OTHER if not found
     */
    public static PubIdType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (PubIdType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }

        return OTHER;
    }
}
