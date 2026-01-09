package com.brillianttiger.bio.parser.pmc.model;

/**
 * JournalIdType / 저널 ID 유형
 *
 * KR: JATS journal-id-type 속성 값을 나타내는 열거형.
 *     저널을 식별하는 다양한 ID 체계를 구분.
 * EN: Enumeration representing JATS journal-id-type attribute values.
 *     Distinguishes various ID schemes for journal identification.
 *
 * DTD: <!ATTLIST journal-id journal-id-type CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/journal-id-type.html
 *
 * Common Values:
 * - nlm-ta: NLM Title Abbreviation (National Library of Medicine)
 * - iso-abbrev: ISO abbreviated title
 * - publisher-id: Publisher's internal ID
 * - pmc: PubMed Central ID
 * - doi: Digital Object Identifier
 * - hwp: HighWire Press ID
 */
public enum JournalIdType {

    /**
     * NLM 제목 약어 / NLM Title Abbreviation
     *
     * KR: National Library of Medicine의 표준 저널 약어
     * EN: Standard journal abbreviation from National Library of Medicine
     *
     * Example: "J Biol Chem"
     */
    NLM_TA("nlm-ta"),

    /**
     * ISO 약어 / ISO abbreviation
     *
     * KR: ISO 4 표준에 따른 저널 약어
     * EN: Journal abbreviation according to ISO 4 standard
     *
     * Example: "J. Biol. Chem."
     */
    ISO_ABBREV("iso-abbrev"),

    /**
     * 출판사 ID / Publisher ID
     *
     * KR: 출판사의 내부 저널 식별자
     * EN: Publisher's internal journal identifier
     */
    PUBLISHER_ID("publisher-id"),

    /**
     * PMC ID / PubMed Central ID
     *
     * KR: PubMed Central의 저널 식별자
     * EN: PubMed Central journal identifier
     */
    PMC("pmc"),

    /**
     * DOI / Digital Object Identifier
     *
     * KR: 저널의 DOI (Digital Object Identifier)
     * EN: Journal's Digital Object Identifier
     */
    DOI("doi"),

    /**
     * HighWire Press ID
     *
     * KR: HighWire Press의 저널 식별자
     * EN: HighWire Press journal identifier
     */
    HWP("hwp"),

    /**
     * 기타 / Other
     *
     * KR: 위에 나열되지 않은 기타 ID 유형
     * EN: Other ID types not listed above
     */
    OTHER("other");

    private final String value;

    JournalIdType(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * KR: XML journal-id-type 속성값 반환
     * EN: Returns XML journal-id-type attribute value
     *
     * @return journal-id-type 값 (예: "nlm-ta")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 JournalIdType 변환 / Convert from string to JournalIdType
     *
     * KR: XML에서 파싱한 문자열을 JournalIdType enum으로 변환.
     *     매칭되는 값이 없으면 OTHER 반환.
     * EN: Converts parsed string from XML to JournalIdType enum.
     *     Returns OTHER if no matching value found.
     *
     * @param value journal-id-type 속성 값 / journal-id-type attribute value
     * @return 해당하는 JournalIdType, 없으면 OTHER / Corresponding JournalIdType, or OTHER if not found
     */
    public static JournalIdType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (JournalIdType type : values()) {
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
