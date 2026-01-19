package com.brillianttiger.bio.parser.pmc.model;

/**
 * Position / 위치
 *
 * KR: JATS position 속성 값을 나타내는 열거형.
 *     그림, 테이블 등 부유 요소의 배치 위치를 지정.
 * EN: Enumeration representing JATS position attribute values.
 *     Specifies placement of floating elements like figures and tables.
 *
 * DTD: position (anchor | background | float | margin) "float"
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/position.html
 */
public enum Position {

    /**
     * 고정 위치 / Anchored position
     * KR: 텍스트 내 참조 위치에 고정
     * EN: Fixed at the reference position in text
     */
    ANCHOR("anchor"),

    /**
     * 배경 / Background
     * KR: 배경 요소로 배치
     * EN: Placed as a background element
     */
    BACKGROUND("background"),

    /**
     * 부유 / Float (기본값)
     * KR: 페이지 내 최적 위치에 부유 (기본값)
     * EN: Floats to optimal position on page (default)
     */
    FLOAT("float"),

    /**
     * 여백 / Margin
     * KR: 페이지 여백에 배치
     * EN: Placed in page margin
     */
    MARGIN("margin");

    private final String value;

    Position(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return position 값 (예: "float")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 Position 변환 / Convert from string to Position
     *
     * KR: XML에서 파싱한 문자열을 Position enum으로 변환.
     *     매칭되는 값이 없으면 FLOAT 반환 (기본값).
     * EN: Converts parsed string from XML to Position enum.
     *     Returns FLOAT if no matching value found (default).
     *
     * @param value position 속성 값 / position attribute value
     * @return 해당하는 Position, 없으면 FLOAT / Corresponding Position, or FLOAT if not found
     */
    public static Position fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return FLOAT;
        }

        String normalized = value.trim().toLowerCase();
        for (Position pos : values()) {
            if (pos.value.equals(normalized)) {
                return pos;
            }
        }

        return FLOAT;
    }

    @Override
    public String toString() {
        return value;
    }
}
