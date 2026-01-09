package com.brillianttiger.bio.parser.pubmed.model;

/**
 * PubStatus / 출판 상태
 *
 * DTD: <!ATTLIST PubMedPubDate
 *          PubStatus (received | accepted | epublish | ppublish | revised | aheadofprint |
 *                     retracted | ecollection | pmc | pmcr | pubmed | pubmedr |
 *                     premedline | medline | medliner | entrez | pmc-release) #REQUIRED>
 *
 * KR: PubMedPubDate의 출판 상태 유형
 * EN: Publication status type for PubMedPubDate
 */
public enum PubStatus {

    /**
     * received / 접수됨
     *
     * KR: 원고 접수됨
     * EN: Manuscript received
     */
    RECEIVED("received"),

    /**
     * accepted / 승인됨
     *
     * KR: 원고 승인됨
     * EN: Manuscript accepted
     */
    ACCEPTED("accepted"),

    /**
     * epublish / 전자 출판
     *
     * KR: 전자 출판됨
     * EN: Electronic publication
     */
    EPUBLISH("epublish"),

    /**
     * ppublish / 인쇄 출판
     *
     * KR: 인쇄 출판됨
     * EN: Print publication
     */
    PPUBLISH("ppublish"),

    /**
     * revised / 수정됨
     *
     * KR: 수정됨
     * EN: Revised
     */
    REVISED("revised"),

    /**
     * aheadofprint / 인쇄 전 출판
     *
     * KR: 인쇄 전 출판됨
     * EN: Ahead of print
     */
    AHEADOFPRINT("aheadofprint"),

    /**
     * retracted / 철회됨
     *
     * KR: 논문 철회됨
     * EN: Article retracted
     */
    RETRACTED("retracted"),

    /**
     * ecollection / 전자 컬렉션
     *
     * KR: 전자 컬렉션
     * EN: Electronic collection
     */
    ECOLLECTION("ecollection"),

    /**
     * pmc / PMC
     *
     * KR: PubMed Central
     * EN: PubMed Central
     */
    PMC("pmc"),

    /**
     * pmcr / PMC 릴리스
     *
     * KR: PMC 릴리스됨
     * EN: PMC released
     */
    PMCR("pmcr"),

    /**
     * pubmed / PubMed
     *
     * KR: PubMed 등록됨
     * EN: PubMed registered
     */
    PUBMED("pubmed"),

    /**
     * pubmedr / PubMed 수정
     *
     * KR: PubMed 수정됨
     * EN: PubMed revised
     */
    PUBMEDR("pubmedr"),

    /**
     * premedline / Pre-MEDLINE
     *
     * KR: Pre-MEDLINE 등록됨
     * EN: Pre-MEDLINE registered
     */
    PREMEDLINE("premedline"),

    /**
     * medline / MEDLINE
     *
     * KR: MEDLINE 등록됨
     * EN: MEDLINE registered
     */
    MEDLINE("medline"),

    /**
     * medliner / MEDLINE 수정
     *
     * KR: MEDLINE 수정됨
     * EN: MEDLINE revised
     */
    MEDLINER("medliner"),

    /**
     * entrez / Entrez
     *
     * KR: Entrez 등록됨
     * EN: Entrez registered
     */
    ENTREZ("entrez"),

    /**
     * pmc-release / PMC 릴리스
     *
     * KR: PMC 릴리스 날짜
     * EN: PMC release date
     */
    PMC_RELEASE("pmc-release");

    private final String value;

    PubStatus(String value) {
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
     * 문자열에서 PubStatus enum 변환 / Parse string to PubStatus enum
     *
     * KR: XML 파싱 시 문자열을 enum으로 변환
     * EN: Convert string to enum during XML parsing
     *
     * @param value DTD 문자열 값 / DTD string value
     * @return PubStatus enum 또는 null / PubStatus enum or null
     */
    public static PubStatus fromValue(String value) {
        if (value == null) {
            return null;
        }

        for (PubStatus status : PubStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }

        // 알 수 없는 값인 경우 null 반환 / Return null for unknown values
        return null;
    }
}
