package io.brillianttiger.bio.parser.pmc.model;

/**
 * CellAlign / 셀 수평 정렬
 *
 * KR: XHTML 테이블 셀 align 속성 값을 나타내는 열거형.
 *     셀 내용의 수평 정렬 방식을 지정.
 * EN: Enumeration representing XHTML table cell align attribute values.
 *     Specifies horizontal alignment of cell content.
 *
 * DTD: align (left | center | right | justify | char) #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/align.html
 */
public enum CellAlign {

    /**
     * 왼쪽 정렬 / Left align
     * KR: 셀 내용을 왼쪽으로 정렬
     * EN: Align cell content to the left
     */
    LEFT("left"),

    /**
     * 가운데 정렬 / Center align
     * KR: 셀 내용을 가운데로 정렬
     * EN: Center cell content
     */
    CENTER("center"),

    /**
     * 오른쪽 정렬 / Right align
     * KR: 셀 내용을 오른쪽으로 정렬
     * EN: Align cell content to the right
     */
    RIGHT("right"),

    /**
     * 양쪽 정렬 / Justify
     * KR: 셀 내용을 양쪽으로 정렬
     * EN: Justify cell content
     */
    JUSTIFY("justify"),

    /**
     * 문자 정렬 / Character align
     * KR: 특정 문자(예: 소수점)를 기준으로 정렬
     * EN: Align on a specific character (e.g., decimal point)
     */
    CHAR("char");

    private final String value;

    CellAlign(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return align 값 (예: "center")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 CellAlign 변환 / Convert from string to CellAlign
     *
     * KR: XML에서 파싱한 문자열을 CellAlign enum으로 변환.
     *     매칭되는 값이 없으면 null 반환 (선택적 속성).
     * EN: Converts parsed string from XML to CellAlign enum.
     *     Returns null if no matching value found (optional attribute).
     *
     * @param value align 속성 값 / align attribute value
     * @return 해당하는 CellAlign, 없으면 null / Corresponding CellAlign, or null if not found
     */
    public static CellAlign fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (CellAlign align : values()) {
            if (align.value.equals(normalized)) {
                return align;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
