package io.brillianttiger.bio.parser.pmc.model;

/**
 * Orientation / 방향
 *
 * KR: JATS orientation 속성 값을 나타내는 열거형.
 *     그림, 테이블, 그래픽 등의 표시 방향을 지정.
 * EN: Enumeration representing JATS orientation attribute values.
 *     Specifies display orientation of figures, tables, graphics, etc.
 *
 * DTD: orientation (portrait | landscape) #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/orientation.html
 */
public enum Orientation {

    /**
     * 세로 방향 / Portrait orientation
     * KR: 세로 방향 (일반적인 페이지 방향)
     * EN: Portrait orientation (normal page orientation)
     */
    PORTRAIT("portrait"),

    /**
     * 가로 방향 / Landscape orientation
     * KR: 가로 방향 (넓은 테이블/그림용)
     * EN: Landscape orientation (for wide tables/figures)
     */
    LANDSCAPE("landscape");

    private final String value;

    Orientation(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return orientation 값 (예: "portrait")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 Orientation 변환 / Convert from string to Orientation
     *
     * KR: XML에서 파싱한 문자열을 Orientation enum으로 변환.
     *     매칭되는 값이 없으면 null 반환 (선택적 속성).
     * EN: Converts parsed string from XML to Orientation enum.
     *     Returns null if no matching value found (optional attribute).
     *
     * @param value orientation 속성 값 / orientation attribute value
     * @return 해당하는 Orientation, 없으면 null / Corresponding Orientation, or null if not found
     */
    public static Orientation fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (Orientation orient : values()) {
            if (orient.value.equals(normalized)) {
                return orient;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
