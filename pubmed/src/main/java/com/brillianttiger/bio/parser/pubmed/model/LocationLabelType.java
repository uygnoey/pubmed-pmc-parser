package com.brillianttiger.bio.parser.pubmed.model;

/**
 * LocationLabelType / 위치 레이블 타입
 *
 * DTD: <!ATTLIST LocationLabel Type (part | chapter | section | appendix | figure | table | box) #IMPLIED>
 *
 * KR: 책 섹션의 타입 (부분, 챕터, 섹션, 부록, 그림, 표, 박스)
 * EN: Type of book section (part, chapter, section, appendix, figure, table, box)
 */
public enum LocationLabelType {
    /**
     * 부 / Part
     *
     * KR: 책의 큰 구분 단위
     * EN: Major division of a book
     */
    PART("part"),

    /**
     * 챕터 / Chapter
     *
     * KR: 책의 장
     * EN: Chapter of a book
     */
    CHAPTER("chapter"),

    /**
     * 섹션 / Section
     *
     * KR: 챕터 내의 절
     * EN: Section within a chapter
     */
    SECTION("section"),

    /**
     * 부록 / Appendix
     *
     * KR: 책의 부록
     * EN: Appendix of a book
     */
    APPENDIX("appendix"),

    /**
     * 그림 / Figure
     *
     * KR: 그림, 도표
     * EN: Figure, illustration
     */
    FIGURE("figure"),

    /**
     * 표 / Table
     *
     * KR: 데이터 표
     * EN: Data table
     */
    TABLE("table"),

    /**
     * 박스 / Box
     *
     * KR: 텍스트 박스, 강조 영역
     * EN: Text box, highlighted area
     */
    BOX("box");

    private final String value;

    LocationLabelType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 LocationLabelType enum 변환 / Convert from string to LocationLabelType enum
     *
     * @param value 문자열 값 / String value
     * @return LocationLabelType enum or null if not found
     */
    public static LocationLabelType fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (LocationLabelType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }
}
