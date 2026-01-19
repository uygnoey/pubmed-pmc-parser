package com.brillianttiger.bio.parser.pubmed.model;

/**
 * IndexingMethod / 색인 방법
 *
 * DTD: IndexingMethod (Automated | Curated) #IMPLIED
 *
 * KR: MEDLINE 인용의 색인 방법
 * EN: Indexing method for MEDLINE citation
 *
 * NOTE: 실제 데이터에서 "Manual" 값도 발견됨 (DTD에는 없음)
 * NOTE: "Manual" value found in real data (not in DTD)
 */
public enum IndexingMethod {
    /**
     * 자동 색인 / Automated indexing
     *
     * KR: 자동화된 알고리즘을 통한 색인
     * EN: Indexed by automated algorithms
     */
    Automated("Automated"),

    /**
     * 큐레이션 색인 / Curated indexing
     *
     * KR: 전문가의 수동 큐레이션을 통한 색인
     * EN: Indexed by expert manual curation
     */
    Curated("Curated"),

    /**
     * 수동 색인 / Manual indexing
     *
     * KR: 수동 색인 (실제 데이터에서 발견, DTD에는 없음)
     * EN: Manual indexing (found in real data, not in DTD)
     */
    Manual("Manual");

    private final String value;

    IndexingMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 IndexingMethod enum 변환 / Convert from string to IndexingMethod enum
     *
     * @param value 문자열 값 / String value
     * @return IndexingMethod enum
     */
    public static IndexingMethod fromValue(String value) {
        for (IndexingMethod method : values()) {
            if (method.value.equals(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown IndexingMethod value: " + value);
    }
}
