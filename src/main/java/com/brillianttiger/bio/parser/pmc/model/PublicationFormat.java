package com.brillianttiger.bio.parser.pmc.model;

/**
 * PublicationFormat / 출판 형식
 *
 * KR: JATS publication-format 속성 값을 나타내는 열거형.
 *     저널의 출판 형식(인쇄/전자)을 구분.
 * EN: Enumeration representing JATS publication-format attribute values.
 *     Distinguishes journal publication formats (print/electronic).
 *
 * DTD: <!ATTLIST issn publication-format (print | electronic | print-electronic | online) #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/publication-format.html
 *
 * Values:
 * - print: Print publication only
 * - electronic: Electronic publication only
 * - print-electronic: Both print and electronic
 * - online: Online publication (similar to electronic)
 */
public enum PublicationFormat {

    /**
     * 인쇄본 / Print publication
     *
     * KR: 인쇄본으로만 출판
     * EN: Published in print format only
     */
    PRINT("print"),

    /**
     * 전자본 / Electronic publication
     *
     * KR: 전자 형식으로만 출판
     * EN: Published in electronic format only
     */
    ELECTRONIC("electronic"),

    /**
     * 인쇄+전자 / Print and electronic
     *
     * KR: 인쇄본과 전자본 모두 출판
     * EN: Published in both print and electronic formats
     */
    PRINT_ELECTRONIC("print-electronic"),

    /**
     * 온라인 / Online publication
     *
     * KR: 온라인으로 출판 (전자본과 유사)
     * EN: Published online (similar to electronic)
     */
    ONLINE("online"),

    /**
     * 기타 / Other
     *
     * KR: 위에 나열되지 않은 기타 출판 형식
     * EN: Other publication formats not listed above
     */
    OTHER("other");

    private final String value;

    PublicationFormat(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * KR: XML publication-format 속성값 반환
     * EN: Returns XML publication-format attribute value
     *
     * @return publication-format 값 (예: "print-electronic")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 PublicationFormat 변환 / Convert from string to PublicationFormat
     *
     * KR: XML에서 파싱한 문자열을 PublicationFormat enum으로 변환.
     *     매칭되는 값이 없으면 OTHER 반환.
     * EN: Converts parsed string from XML to PublicationFormat enum.
     *     Returns OTHER if no matching value found.
     *
     * @param value publication-format 속성 값 / publication-format attribute value
     * @return 해당하는 PublicationFormat, 없으면 OTHER / Corresponding PublicationFormat, or OTHER if not found
     */
    public static PublicationFormat fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (PublicationFormat format : values()) {
            if (format.value.equals(normalized)) {
                return format;
            }
        }

        return OTHER;
    }

    @Override
    public String toString() {
        return value;
    }
}
