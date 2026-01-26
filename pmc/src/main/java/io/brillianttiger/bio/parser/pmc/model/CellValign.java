package io.brillianttiger.bio.parser.pmc.model;

/**
 * CellValign / 셀 수직 정렬
 *
 * KR: XHTML 테이블 셀 valign 속성 값을 나타내는 열거형.
 *     셀 내용의 수직 정렬 방식을 지정.
 * EN: Enumeration representing XHTML table cell valign attribute values.
 *     Specifies vertical alignment of cell content.
 *
 * DTD: valign (top | middle | bottom | baseline) #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/valign.html
 */
public enum CellValign {

    /**
     * 상단 정렬 / Top align
     * KR: 셀 내용을 상단으로 정렬
     * EN: Align cell content to the top
     */
    TOP("top"),

    /**
     * 중앙 정렬 / Middle align
     * KR: 셀 내용을 수직 중앙으로 정렬
     * EN: Center cell content vertically
     */
    MIDDLE("middle"),

    /**
     * 하단 정렬 / Bottom align
     * KR: 셀 내용을 하단으로 정렬
     * EN: Align cell content to the bottom
     */
    BOTTOM("bottom"),

    /**
     * 기준선 정렬 / Baseline align
     * KR: 텍스트 기준선에 맞춤
     * EN: Align on text baseline
     */
    BASELINE("baseline");

    private final String value;

    CellValign(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return valign 값 (예: "middle")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 CellValign 변환 / Convert from string to CellValign
     *
     * KR: XML에서 파싱한 문자열을 CellValign enum으로 변환.
     *     매칭되는 값이 없으면 null 반환 (선택적 속성).
     * EN: Converts parsed string from XML to CellValign enum.
     *     Returns null if no matching value found (optional attribute).
     *
     * @param value valign 속성 값 / valign attribute value
     * @return 해당하는 CellValign, 없으면 null / Corresponding CellValign, or null if not found
     */
    public static CellValign fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (CellValign valign : values()) {
            if (valign.value.equals(normalized)) {
                return valign;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
