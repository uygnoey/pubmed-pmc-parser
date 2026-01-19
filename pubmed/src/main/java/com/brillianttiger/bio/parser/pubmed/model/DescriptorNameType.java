package com.brillianttiger.bio.parser.pubmed.model;

/**
 * DescriptorNameType / 디스크립터명 유형
 *
 * DTD: <!ATTLIST DescriptorName Type (Geographic) #IMPLIED>
 *
 * KR: 디스크립터명의 유형
 * EN: Type of descriptor name
 */
public enum DescriptorNameType {
    /**
     * Geographic
     *
     * KR: 지리적
     * EN: Geographic
     */
    Geographic("Geographic");

    private final String value;

    DescriptorNameType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 DescriptorNameType enum 변환 / Convert from string to DescriptorNameType enum
     *
     * @param value 문자열 값 / String value
     * @return DescriptorNameType enum
     */
    public static DescriptorNameType fromValue(String value) {
        for (DescriptorNameType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DescriptorNameType value: " + value);
    }
}
