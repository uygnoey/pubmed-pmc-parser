package io.brillianttiger.bio.parser.pubmed.model;

/**
 * NlmCategory / NLM 카테고리
 *
 * DTD: <!ATTLIST AbstractText NlmCategory (BACKGROUND | OBJECTIVE | METHODS | RESULTS | CONCLUSIONS | UNASSIGNED) #IMPLIED>
 *
 * KR: 초록 텍스트의 NLM 카테고리
 * EN: NLM category for abstract text
 */
public enum NlmCategory {
    /**
     * BACKGROUND
     *
     * KR: 배경
     * EN: Background
     */
    BACKGROUND("BACKGROUND"),

    /**
     * OBJECTIVE
     *
     * KR: 목적
     * EN: Objective
     */
    OBJECTIVE("OBJECTIVE"),

    /**
     * METHODS
     *
     * KR: 방법
     * EN: Methods
     */
    METHODS("METHODS"),

    /**
     * RESULTS
     *
     * KR: 결과
     * EN: Results
     */
    RESULTS("RESULTS"),

    /**
     * CONCLUSIONS
     *
     * KR: 결론
     * EN: Conclusions
     */
    CONCLUSIONS("CONCLUSIONS"),

    /**
     * UNASSIGNED
     *
     * KR: 미지정
     * EN: Unassigned
     */
    UNASSIGNED("UNASSIGNED");

    private final String value;

    NlmCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 NlmCategory enum 변환 / Convert from string to NlmCategory enum
     *
     * @param value 문자열 값 / String value
     * @return NlmCategory enum
     */
    public static NlmCategory fromValue(String value) {
        for (NlmCategory category : values()) {
            if (category.value.equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown NlmCategory value: " + value);
    }
}
