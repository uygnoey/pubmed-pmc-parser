package io.brillianttiger.bio.parser.pubmed.model;

/**
 * SupplMeshNameType / 보충 MeSH명 유형
 *
 * DTD: <!ATTLIST SupplMeshName Type (Disease | Protocol | Organism) #REQUIRED>
 *
 * KR: 보충 MeSH명의 유형
 * EN: Type of supplementary MeSH name
 */
public enum SupplMeshNameType {
    /**
     * Disease
     *
     * KR: 질병
     * EN: Disease
     */
    Disease("Disease"),

    /**
     * Protocol
     *
     * KR: 프로토콜
     * EN: Protocol
     */
    Protocol("Protocol"),

    /**
     * Organism
     *
     * KR: 유기체
     * EN: Organism
     */
    Organism("Organism"),

    /**
     * Anatomy
     *
     * KR: 해부학 (2024 DTD 추가)
     * EN: Anatomy (Added in 2024 DTD)
     */
    Anatomy("Anatomy"),

    /**
     * Population
     *
     * KR: 집단 (2024 DTD 추가)
     * EN: Population (Added in 2024 DTD)
     */
    Population("Population");

    private final String value;

    SupplMeshNameType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 SupplMeshNameType enum 변환 / Convert from string to SupplMeshNameType enum
     *
     * @param value 문자열 값 / String value
     * @return SupplMeshNameType enum
     */
    public static SupplMeshNameType fromValue(String value) {
        for (SupplMeshNameType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown SupplMeshNameType value: " + value);
    }
}
