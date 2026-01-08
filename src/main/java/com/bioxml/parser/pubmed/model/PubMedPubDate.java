package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubMedPubDate / PubMed 출판 날짜
 *
 * DTD: <!ELEMENT PubMedPubDate (Year, Month, Day, (Hour, (Minute, Second?)?)?)>
 * DTD: <!ATTLIST PubMedPubDate
 *          PubStatus (received | accepted | epublish | ppublish | revised | aheadofprint |
 *                     retracted | ecollection | pmc | pmcr | pubmed | pubmedr |
 *                     premedline | medline | medliner | entrez | pmc-release) #REQUIRED>
 *
 * KR: PubMed 시스템 내 상세 날짜 정보
 * EN: Detailed date information within PubMed system
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubMedPubDate {

    /**
     * 출판 상태 (필수) / Publication status (required)
     */
    private String pubStatus;

    /**
     * 연도 / Year
     */
    private Year year;

    /**
     * 월 / Month
     */
    private Month month;

    /**
     * 일 / Day
     */
    private Day day;

    /**
     * 시 / Hour
     */
    private Hour hour;

    /**
     * 분 / Minute
     */
    private Minute minute;

    /**
     * 초 / Second
     */
    private Second second;
}
