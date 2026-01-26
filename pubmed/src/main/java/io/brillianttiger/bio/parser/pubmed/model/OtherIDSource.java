package io.brillianttiger.bio.parser.pubmed.model;

/**
 * OtherIDSource / 기타 ID 출처
 *
 * DTD: <!ATTLIST OtherID Source (NASA | KIE | PIP | POP | ARPL | CPC | IND | CPFH | CLML | NRCBL | NLM | QCIM) #REQUIRED>
 *
 * KR: 기타 ID의 출처 데이터베이스
 * EN: Source database for other ID
 */
public enum OtherIDSource {

    /**
     * NASA (National Aeronautics and Space Administration)
     *
     * KR: 미국 항공우주국
     * EN: National Aeronautics and Space Administration
     */
    NASA("NASA"),

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
     * POP (Population)
     *
     * KR: 인구
     * EN: Population
     */
    POP("POP"),

    /**
     * ARPL
     *
     * KR: ARPL
     * EN: ARPL
     */
    ARPL("ARPL"),

    /**
     * CPC (Cooperative Patent Classification)
     *
     * KR: 협력 특허 분류
     * EN: Cooperative Patent Classification
     */
    CPC("CPC"),

    /**
     * IND (Index Medicus)
     *
     * KR: 인덱스 메디쿠스
     * EN: Index Medicus
     */
    IND("IND"),

    /**
     * CPFH
     *
     * KR: CPFH
     * EN: CPFH
     */
    CPFH("CPFH"),

    /**
     * CLML (Current List of Medical Literature)
     *
     * KR: 현재 의학 문헌 목록
     * EN: Current List of Medical Literature
     */
    CLML("CLML"),

    /**
     * NRCBL (National Reference Center for Bioethics Literature)
     *
     * KR: 생명윤리 문헌 국립 참고 센터
     * EN: National Reference Center for Bioethics Literature
     */
    NRCBL("NRCBL"),

    /**
     * NLM (National Library of Medicine)
     *
     * KR: 미국 국립의학도서관
     * EN: National Library of Medicine
     */
    NLM("NLM"),

    /**
     * QCIM (Quarterly Cumulative Index Medicus)
     *
     * KR: 분기별 누적 인덱스 메디쿠스
     * EN: Quarterly Cumulative Index Medicus
     */
    QCIM("QCIM");

    private final String value;

    OtherIDSource(String value) {
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
     * 문자열에서 OtherIDSource enum 변환 / Parse string to OtherIDSource enum
     *
     * KR: XML 파싱 시 문자열을 enum으로 변환
     * EN: Convert string to enum during XML parsing
     *
     * @param value DTD 문자열 값 / DTD string value
     * @return OtherIDSource enum 또는 null / OtherIDSource enum or null
     */
    public static OtherIDSource fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (OtherIDSource source : OtherIDSource.values()) {
            if (source.value.equals(value)) {
                return source;
            }
        }

        // 알 수 없는 값인 경우 null 반환 / Return null for unknown values
        return null;
    }
}
