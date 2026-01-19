package com.brillianttiger.bio.parser.pmc.model;

/**
 * TableFrame / 테이블 프레임
 *
 * KR: XHTML table frame 속성 값을 나타내는 열거형.
 *     테이블 외곽 테두리의 표시 방식을 지정.
 * EN: Enumeration representing XHTML table frame attribute values.
 *     Specifies how the outer border of the table is displayed.
 *
 * DTD: frame (void | above | below | hsides | lhs | rhs | vsides | box | border) #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/frame.html
 */
public enum TableFrame {

    /**
     * 테두리 없음 / No border
     * KR: 외곽 테두리 없음
     * EN: No outer border
     */
    VOID("void"),

    /**
     * 상단만 / Top only
     * KR: 상단 테두리만 표시
     * EN: Top border only
     */
    ABOVE("above"),

    /**
     * 하단만 / Bottom only
     * KR: 하단 테두리만 표시
     * EN: Bottom border only
     */
    BELOW("below"),

    /**
     * 상하단 / Top and bottom
     * KR: 상단과 하단 테두리 표시
     * EN: Top and bottom borders (horizontal sides)
     */
    HSIDES("hsides"),

    /**
     * 좌측만 / Left only
     * KR: 좌측 테두리만 표시
     * EN: Left border only (left-hand side)
     */
    LHS("lhs"),

    /**
     * 우측만 / Right only
     * KR: 우측 테두리만 표시
     * EN: Right border only (right-hand side)
     */
    RHS("rhs"),

    /**
     * 좌우측 / Left and right
     * KR: 좌측과 우측 테두리 표시
     * EN: Left and right borders (vertical sides)
     */
    VSIDES("vsides"),

    /**
     * 상자형 / Box
     * KR: 네 면 모두 테두리 표시
     * EN: All four sides (box)
     */
    BOX("box"),

    /**
     * 테두리형 / Border
     * KR: 네 면 모두 테두리 표시 (box와 동일)
     * EN: All four sides (same as box)
     */
    BORDER("border");

    private final String value;

    TableFrame(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return frame 값 (예: "box")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 TableFrame 변환 / Convert from string to TableFrame
     *
     * KR: XML에서 파싱한 문자열을 TableFrame enum으로 변환.
     *     매칭되는 값이 없으면 null 반환 (선택적 속성).
     * EN: Converts parsed string from XML to TableFrame enum.
     *     Returns null if no matching value found (optional attribute).
     *
     * @param value frame 속성 값 / frame attribute value
     * @return 해당하는 TableFrame, 없으면 null / Corresponding TableFrame, or null if not found
     */
    public static TableFrame fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (TableFrame frame : values()) {
            if (frame.value.equals(normalized)) {
                return frame;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
