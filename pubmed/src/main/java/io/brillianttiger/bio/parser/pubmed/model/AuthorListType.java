package io.brillianttiger.bio.parser.pubmed.model;

/**
 * AuthorListType / 저자 목록 유형
 *
 * DTD: <!ATTLIST AuthorList Type (authors | editors) #IMPLIED>
 *
 * KR: 저자 목록의 유형 (저자 또는 편집자)
 * EN: Type of author list (authors or editors)
 */
public enum AuthorListType {
    /**
     * Authors
     *
     * KR: 저자
     * EN: Authors
     */
    authors("authors"),

    /**
     * Editors
     *
     * KR: 편집자
     * EN: Editors
     */
    editors("editors");

    private final String value;

    AuthorListType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 AuthorListType enum 변환 / Convert from string to AuthorListType enum
     *
     * @param value 문자열 값 / String value
     * @return AuthorListType enum
     */
    public static AuthorListType fromValue(String value) {
        for (AuthorListType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown AuthorListType value: " + value);
    }
}
