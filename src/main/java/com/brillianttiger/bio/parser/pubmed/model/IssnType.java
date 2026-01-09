package com.brillianttiger.bio.parser.pubmed.model;

/**
 * IssnType / ISSN 유형
 *
 * DTD: <!ATTLIST ISSN IssnType (Electronic | Print) #REQUIRED>
 *
 * KR: ISSN의 유형 (전자 또는 인쇄)
 * EN: Type of ISSN (Electronic or Print)
 */
public enum IssnType {
    /**
     * 전자 ISSN / Electronic ISSN
     *
     * KR: 전자 출판물의 ISSN (e-ISSN)
     * EN: ISSN for electronic publication (e-ISSN)
     */
    Electronic("Electronic"),

    /**
     * 인쇄 ISSN / Print ISSN
     *
     * KR: 인쇄 출판물의 ISSN (p-ISSN)
     * EN: ISSN for print publication (p-ISSN)
     */
    Print("Print");

    private final String value;

    IssnType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 IssnType enum 변환 / Convert from string to IssnType enum
     *
     * @param value 문자열 값 / String value
     * @return IssnType enum
     */
    public static IssnType fromValue(String value) {
        for (IssnType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown IssnType value: " + value);
    }
}
