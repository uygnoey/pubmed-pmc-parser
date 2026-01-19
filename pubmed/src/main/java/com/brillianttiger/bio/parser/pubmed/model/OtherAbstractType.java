package com.brillianttiger.bio.parser.pubmed.model;

/**
 * OtherAbstractType / 기타 초록 유형
 *
 * DTD: <!ATTLIST OtherAbstract Type (AAMC | AIDS | KIE | PIP | NASA | Publisher) #REQUIRED>
 *
 * KR: 기타 초록의 유형
 * EN: Type of other abstract
 */
public enum OtherAbstractType {
    /**
     * AAMC (Association of American Medical Colleges)
     *
     * KR: 미국 의과대학 협회
     * EN: Association of American Medical Colleges
     */
    AAMC("AAMC"),

    /**
     * AIDS
     *
     * KR: AIDS 관련 초록
     * EN: AIDS-related abstract
     */
    AIDS("AIDS"),

    /**
     * KIE (Kennedy Institute of Ethics)
     *
     * KR: 케네디 윤리 연구소
     * EN: Kennedy Institute of Ethics
     */
    KIE("KIE"),

    /**
     * PIP (Population Information Program)
     *
     * KR: 인구 정보 프로그램
     * EN: Population Information Program
     */
    PIP("PIP"),

    /**
     * NASA
     *
     * KR: NASA 관련 초록
     * EN: NASA-related abstract
     */
    NASA("NASA"),

    /**
     * Publisher
     *
     * KR: 출판사 제공 초록
     * EN: Publisher-provided abstract
     */
    Publisher("Publisher"),

    /**
     * Plain Language Summary
     *
     * KR: 쉬운 언어로 작성된 요약
     * EN: Plain language summary (2024년 추가)
     */
    plain_language_summary("plain-language-summary");

    private final String value;

    OtherAbstractType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 OtherAbstractType enum 변환 / Convert from string to OtherAbstractType enum
     *
     * @param value 문자열 값 / String value
     * @return OtherAbstractType enum
     */
    public static OtherAbstractType fromValue(String value) {
        for (OtherAbstractType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown OtherAbstractType value: " + value);
    }
}
