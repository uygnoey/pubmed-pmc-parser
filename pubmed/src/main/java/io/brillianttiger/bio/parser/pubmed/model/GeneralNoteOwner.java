package io.brillianttiger.bio.parser.pubmed.model;

/**
 * GeneralNoteOwner / 일반 노트 소유자
 *
 * DTD: <!ATTLIST GeneralNote Owner (NLM | NASA | PIP | KIE | HSR | HMD) "NLM">
 *
 * KR: 일반 노트의 소유자 유형
 * EN: General note owner type
 */
public enum GeneralNoteOwner {

    /**
     * NLM (National Library of Medicine)
     *
     * KR: 미국 국립의학도서관
     * EN: National Library of Medicine
     */
    NLM("NLM"),

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
     * HSR (Health Services Research)
     *
     * KR: 보건 서비스 연구
     * EN: Health Services Research
     */
    HSR("HSR"),

    /**
     * HMD (History of Medicine Division)
     *
     * KR: 의학사 부서
     * EN: History of Medicine Division
     */
    HMD("HMD");

    private final String value;

    GeneralNoteOwner(String value) {
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
     * 문자열에서 GeneralNoteOwner enum 변환 / Parse string to GeneralNoteOwner enum
     *
     * KR: XML 파싱 시 문자열을 enum으로 변환
     * EN: Convert string to enum during XML parsing
     *
     * @param value DTD 문자열 값 / DTD string value
     * @return GeneralNoteOwner enum 또는 null / GeneralNoteOwner enum or null
     */
    public static GeneralNoteOwner fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (GeneralNoteOwner owner : GeneralNoteOwner.values()) {
            if (owner.value.equals(value)) {
                return owner;
            }
        }

        // 알 수 없는 값인 경우 null 반환 / Return null for unknown values
        return null;
    }
}
