package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PmcPubDate / PMC 출판 날짜
 *
 * DTD: <!ELEMENT pub-date (((day?, month?) | season)?, year, era?, string-date?)>
 * DTD: <!ATTLIST pub-date
 *          pub-type (ppub | epub | epub-ppub | epreprint | collection | nihms-submitted | pmc-release) #IMPLIED
 *          publication-format (print | electronic | print-electronic) #IMPLIED
 *          date-type (pub | preprint | epublish | ppublish | ecorrected | pcorrected | eretracted | pretracted) #IMPLIED
 *          iso-8601-date CDATA #IMPLIED>
 *
 * KR: 논문 출판 날짜
 * EN: Article publication date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcPubDate {

    /**
     * 출판 유형 / Publication type
     */
    private String pubType;

    /**
     * 출판 형식 / Publication format
     */
    private String publicationFormat;

    /**
     * 날짜 유형 / Date type
     */
    private String dateType;

    /**
     * ISO 8601 날짜 / ISO 8601 date
     */
    private String iso8601Date;

    /**
     * 일 / Day
     */
    private Day day;

    /**
     * 월 / Month
     */
    private Month month;

    /**
     * 계절 / Season
     */
    private Season season;

    /**
     * 연도 (필수) / Year (required)
     */
    private Year year;

    /**
     * 시대 / Era
     */
    private Era era;

    /**
     * 문자열 날짜 / String date
     */
    private StringDate stringDate;
}
