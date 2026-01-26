package io.brillianttiger.bio.parser.pubmed.model;

/**
 * EIdType / 전자 식별자 유형
 *
 * DTD: <!ATTLIST ELocationID EIdType (doi | pii) #REQUIRED>
 *
 * KR: 전자 위치 식별자의 유형
 * EN: Type of electronic location identifier
 */
public enum EIdType {
    /**
     * DOI (Digital Object Identifier)
     *
     * KR: 디지털 객체 식별자
     * EN: Digital Object Identifier
     */
    doi("doi"),

    /**
     * PII (Publisher Item Identifier)
     *
     * KR: 출판사 항목 식별자
     * EN: Publisher Item Identifier
     */
    pii("pii"),

    /**
     * Book Accession Number
     *
     * KR: 도서 등록번호
     * EN: Book accession number
     */
    bookaccession("bookaccession");

    private final String value;

    EIdType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 문자열로부터 EIdType enum 변환 / Convert from string to EIdType enum
     *
     * @param value 문자열 값 / String value
     * @return EIdType enum
     */
    public static EIdType fromValue(String value) {
        for (EIdType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown EIdType value: " + value);
    }
}
