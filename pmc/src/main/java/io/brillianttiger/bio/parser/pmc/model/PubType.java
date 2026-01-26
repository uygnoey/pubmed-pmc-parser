package io.brillianttiger.bio.parser.pmc.model;

/**
 * PubType / 출판 유형
 *
 * KR: JATS pub-type 속성 값을 나타내는 열거형.
 *     인쇄본/전자본 출판 시점을 구분.
 * EN: Enumeration representing JATS pub-type attribute values.
 *     Distinguishes print/electronic publication timing.
 *
 * DTD: <!ATTLIST issn pub-type (ppub | epub | ppub-epub | epub-ppub) #IMPLIED>
 * DTD: <!ATTLIST pub-date pub-type (ppub | epub | epub-ppub | ppub-epub | collection | epreprint) #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/pub-type.html
 *
 * Values:
 * - ppub: Print publication
 * - epub: Electronic publication
 * - ppub-epub: Print published before electronic
 * - epub-ppub: Electronic published before print
 * - collection: Collection publication
 * - epreprint: Electronic preprint
 */
public enum PubType {

    /**
     * 인쇄본 출판 / Print publication
     *
     * KR: 인쇄본으로 출판된 버전
     * EN: Version published in print
     *
     * Example: ISSN for print edition
     */
    PPUB("ppub"),

    /**
     * 전자본 출판 / Electronic publication
     *
     * KR: 전자 형식으로 출판된 버전
     * EN: Version published electronically
     *
     * Example: eISSN for electronic edition
     */
    EPUB("epub"),

    /**
     * 인쇄본 먼저 출판 / Print published before electronic
     *
     * KR: 인쇄본이 전자본보다 먼저 출판됨
     * EN: Print version published before electronic version
     */
    PPUB_EPUB("ppub-epub"),

    /**
     * 전자본 먼저 출판 / Electronic published before print
     *
     * KR: 전자본이 인쇄본보다 먼저 출판됨
     * EN: Electronic version published before print version
     */
    EPUB_PPUB("epub-ppub"),

    /**
     * 컬렉션 출판 / Collection publication
     *
     * KR: 컬렉션 형태로 출판됨
     * EN: Published as a collection
     */
    COLLECTION("collection"),

    /**
     * 전자 사전 출판 / Electronic preprint
     *
     * KR: 전자 형식으로 사전 출판됨 (preprint)
     * EN: Published as an electronic preprint
     */
    EPREPRINT("epreprint"),

    /**
     * 기타 / Other
     *
     * KR: 위에 나열되지 않은 기타 출판 유형
     * EN: Other publication types not listed above
     */
    OTHER("other");

    private final String value;

    PubType(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * KR: XML pub-type 속성값 반환
     * EN: Returns XML pub-type attribute value
     *
     * @return pub-type 값 (예: "ppub")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 PubType 변환 / Convert from string to PubType
     *
     * KR: XML에서 파싱한 문자열을 PubType enum으로 변환.
     *     매칭되는 값이 없으면 OTHER 반환.
     * EN: Converts parsed string from XML to PubType enum.
     *     Returns OTHER if no matching value found.
     *
     * @param value pub-type 속성 값 / pub-type attribute value
     * @return 해당하는 PubType, 없으면 OTHER / Corresponding PubType, or OTHER if not found
     */
    public static PubType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (PubType type : values()) {
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
