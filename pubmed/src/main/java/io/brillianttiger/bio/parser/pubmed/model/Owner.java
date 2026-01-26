package io.brillianttiger.bio.parser.pubmed.model;

/**
 * Owner / 소유자
 *
 * DTD: Owner (NLM | NASA | PIP | KIE | HSR | HMD | NOTNLM) "NLM"
 *
 * KR: MEDLINE 인용 데이터의 소유 기관
 * EN: Owning organization of MEDLINE citation data
 */
public enum Owner {
    /**
     * 미국 국립의학도서관 / National Library of Medicine
     *
     * KR: 기본값, 대부분의 MEDLINE 레코드
     * EN: Default value, most MEDLINE records
     */
    NLM("NLM"),

    /**
     * 미국 항공우주국 / National Aeronautics and Space Administration
     *
     * KR: 우주의학 관련 문헌
     * EN: Space medicine related literature
     */
    NASA("NASA"),

    /**
     * 인구정보 프로그램 / Population Information Program
     *
     * KR: 인구 및 가족계획 관련
     * EN: Population and family planning related
     */
    PIP("PIP"),

    /**
     * 케네디 윤리연구소 / Kennedy Institute of Ethics
     *
     * KR: 생명윤리 관련 문헌
     * EN: Bioethics related literature
     */
    KIE("KIE"),

    /**
     * 보건서비스 연구 / Health Services Research
     *
     * KR: 보건의료서비스 연구
     * EN: Health services research
     */
    HSR("HSR"),

    /**
     * 의학사 / History of Medicine Division
     *
     * KR: 의학사 관련 문헌
     * EN: History of medicine related literature
     */
    HMD("HMD"),

    /**
     * NLM 아님 / Not NLM
     *
     * KR: NLM 소유가 아닌 데이터
     * EN: Data not owned by NLM
     */
    NOTNLM("NOTNLM");

    private final String value;

    Owner(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 Owner enum 변환 / Convert from string to Owner enum
     *
     * @param value 문자열 값 / String value
     * @return Owner enum
     */
    public static Owner fromValue(String value) {
        for (Owner owner : values()) {
            if (owner.value.equals(value)) {
                return owner;
            }
        }
        throw new IllegalArgumentException("Unknown Owner value: " + value);
    }
}
