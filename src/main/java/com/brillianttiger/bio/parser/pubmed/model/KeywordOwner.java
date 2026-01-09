package com.brillianttiger.bio.parser.pubmed.model;

/**
 * KeywordOwner / 키워드 소유자
 *
 * DTD: <!ATTLIST KeywordList Owner (NLM | NLM-AUTO | NASA | PIP | KIE | NOTNLM | HHS) "NLM">
 *
 * KR: 키워드 목록의 소유자 유형
 * EN: Keyword list owner type
 */
public enum KeywordOwner {

    /**
     * NLM (National Library of Medicine)
     *
     * KR: 미국 국립의학도서관
     * EN: National Library of Medicine
     */
    NLM("NLM"),

    /**
     * NLM-AUTO (NLM Automatic)
     *
     * KR: NLM 자동
     * EN: NLM Automatic
     */
    NLM_AUTO("NLM-AUTO"),

    /**
     * NASA (National Aeronautics and Space Administration)
     *
     * KR: 미국 항공우주국
     * EN: National Aeronautics and Space Administration
     */
    NASA("NASA"),

    /**
     * PIP (Population Information Program)
     *
     * KR: 인구 정보 프로그램
     * EN: Population Information Program
     */
    PIP("PIP"),

    /**
     * KIE (Kennedy Institute of Ethics)
     *
     * KR: 케네디 윤리 연구소
     * EN: Kennedy Institute of Ethics
     */
    KIE("KIE"),

    /**
     * NOTNLM (Not NLM)
     *
     * KR: NLM이 아님
     * EN: Not NLM
     */
    NOTNLM("NOTNLM"),

    /**
     * HHS (Health and Human Services)
     *
     * KR: 보건복지부
     * EN: Health and Human Services
     */
    HHS("HHS");

    private final String value;

    KeywordOwner(String value) {
        this.value = value;
    }

    /**
     * 문자열 값 반환 / Get string value
     *
     * @return DTD 문자열 값 / DTD string value
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 KeywordOwner enum 변환 / Parse string to KeywordOwner enum
     *
     * KR: XML 파싱 시 문자열을 enum으로 변환
     * EN: Convert string to enum during XML parsing
     *
     * @param value DTD 문자열 값 / DTD string value
     * @return KeywordOwner enum 또는 null / KeywordOwner enum or null
     */
    public static KeywordOwner fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (KeywordOwner owner : KeywordOwner.values()) {
            if (owner.value.equals(value)) {
                return owner;
            }
        }

        // 알 수 없는 값인 경우 null 반환 / Return null for unknown values
        return null;
    }
}
