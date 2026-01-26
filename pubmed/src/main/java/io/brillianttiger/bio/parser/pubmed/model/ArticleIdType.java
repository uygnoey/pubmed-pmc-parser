package io.brillianttiger.bio.parser.pubmed.model;

/**
 * ArticleIdType / 논문 ID 유형
 *
 * DTD: <!ATTLIST ArticleId IdType (doi | pii | pmcpid | pmpid | pmc | mid | sici |
 *                                  pubmed | medline | pmcid | pmcbook | bookaccession) "pubmed">
 *
 * KR: 논문 식별자 유형
 * EN: Article identifier type
 */
public enum ArticleIdType {

    /**
     * doi / DOI (Digital Object Identifier)
     *
     * KR: 디지털 객체 식별자
     * EN: Digital Object Identifier
     */
    DOI("doi"),

    /**
     * pii / PII (Publisher Item Identifier)
     *
     * KR: 출판사 아이템 식별자
     * EN: Publisher Item Identifier
     */
    PII("pii"),

    /**
     * pmcpid / PMC Publisher ID
     *
     * KR: PMC 출판사 ID
     * EN: PMC Publisher ID
     */
    PMCPID("pmcpid"),

    /**
     * pmpid / PubMed Publisher ID
     *
     * KR: PubMed 출판사 ID
     * EN: PubMed Publisher ID
     */
    PMPID("pmpid"),

    /**
     * pmc / PMC (PubMed Central)
     *
     * KR: PubMed Central ID
     * EN: PubMed Central ID
     */
    PMC("pmc"),

    /**
     * mid / Manuscript ID
     *
     * KR: 원고 ID
     * EN: Manuscript ID
     */
    MID("mid"),

    /**
     * sici / SICI (Serial Item and Contribution Identifier)
     *
     * KR: 연속 간행물 아이템 및 기여 식별자
     * EN: Serial Item and Contribution Identifier
     */
    SICI("sici"),

    /**
     * pubmed / PubMed ID (PMID)
     *
     * KR: PubMed ID
     * EN: PubMed ID
     */
    PUBMED("pubmed"),

    /**
     * medline / MEDLINE ID
     *
     * KR: MEDLINE ID
     * EN: MEDLINE ID
     */
    MEDLINE("medline"),

    /**
     * pmcid / PMC ID
     *
     * KR: PMC ID
     * EN: PMC ID
     */
    PMCID("pmcid"),

    /**
     * pmcbook / PMC Book ID
     *
     * KR: PMC 도서 ID
     * EN: PMC Book ID
     */
    PMCBOOK("pmcbook"),

    /**
     * bookaccession / Book Accession Number
     *
     * KR: 도서 접근 번호 (2024 DTD 추가)
     * EN: Book Accession Number (Added in 2024 DTD)
     */
    BOOKACCESSION("bookaccession");

    private final String value;

    ArticleIdType(String value) {
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
     * 문자열에서 ArticleIdType enum 변환 / Parse string to ArticleIdType enum
     *
     * KR: XML 파싱 시 문자열을 enum으로 변환
     * EN: Convert string to enum during XML parsing
     *
     * @param value DTD 문자열 값 / DTD string value
     * @return ArticleIdType enum 또는 null / ArticleIdType enum or null
     */
    public static ArticleIdType fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (ArticleIdType type : ArticleIdType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }

        // 알 수 없는 값인 경우 null 반환 / Return null for unknown values
        return null;
    }
}
