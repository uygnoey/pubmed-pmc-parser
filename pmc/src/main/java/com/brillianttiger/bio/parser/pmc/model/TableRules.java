package com.brillianttiger.bio.parser.pmc.model;

/**
 * TableRules / 테이블 규칙선
 *
 * KR: XHTML table rules 속성 값을 나타내는 열거형.
 *     테이블 내부 규칙선(셀 간 구분선)의 표시 방식을 지정.
 * EN: Enumeration representing XHTML table rules attribute values.
 *     Specifies how internal rules (cell dividers) are displayed.
 *
 * DTD: rules (none | groups | rows | cols | all) #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/rules.html
 */
public enum TableRules {

    /**
     * 규칙선 없음 / No rules
     * KR: 내부 규칙선 없음
     * EN: No internal rules
     */
    NONE("none"),

    /**
     * 그룹 간 규칙선 / Rules between groups
     * KR: thead, tbody, tfoot 그룹 간 규칙선만 표시
     * EN: Rules between thead, tbody, tfoot groups only
     */
    GROUPS("groups"),

    /**
     * 행 간 규칙선 / Rules between rows
     * KR: 행 간 수평 규칙선만 표시
     * EN: Horizontal rules between rows only
     */
    ROWS("rows"),

    /**
     * 열 간 규칙선 / Rules between columns
     * KR: 열 간 수직 규칙선만 표시
     * EN: Vertical rules between columns only
     */
    COLS("cols"),

    /**
     * 모든 규칙선 / All rules
     * KR: 모든 행과 열 간 규칙선 표시
     * EN: Rules between all rows and columns
     */
    ALL("all");

    private final String value;

    TableRules(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * @return rules 값 (예: "all")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 TableRules 변환 / Convert from string to TableRules
     *
     * KR: XML에서 파싱한 문자열을 TableRules enum으로 변환.
     *     매칭되는 값이 없으면 null 반환 (선택적 속성).
     * EN: Converts parsed string from XML to TableRules enum.
     *     Returns null if no matching value found (optional attribute).
     *
     * @param value rules 속성 값 / rules attribute value
     * @return 해당하는 TableRules, 없으면 null / Corresponding TableRules, or null if not found
     */
    public static TableRules fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (TableRules rules : values()) {
            if (rules.value.equals(normalized)) {
                return rules;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}
