package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubDate / 출판 날짜
 *
 * KR: 논문의 출판 날짜 정보. JATS 1.4 완전 준수 모델.
 * EN: Article publication date information. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT pub-date (
 *          ((day?, month?) | season)?,
 *          year,
 *          era?
 *      )>
 *
 * DTD: <!ATTLIST pub-date
 *          assigning-authority CDATA #IMPLIED
 *          calendar CDATA #IMPLIED
 *          date-type CDATA #IMPLIED
 *          iso-8601-date CDATA #IMPLIED
 *          pub-type (ppub | epub | epub-ppub | ppub-epub | collection | epreprint) #IMPLIED
 *          publication-format (print | electronic | print-electronic | online) #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/pub-date.html
 *
 * Note: pub-type values:
 * - ppub: Print publication
 * - epub: Electronic publication
 * - epub-ppub: Electronic publication before print
 * - ppub-epub: Print publication before electronic
 * - collection: Collection date
 * - epreprint: Electronic preprint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubDate {

    /**
     * 할당 기관 / Assigning authority
     *
     * KR: 날짜를 할당한 기관이나 조직.
     * EN: Organization or agency that assigned the date.
     *
     * DTD: assigning-authority CDATA #IMPLIED
     */
    private String assigningAuthority;

    /**
     * 달력 체계 / Calendar system
     *
     * KR: 사용된 달력 체계 (gregorian, julian 등).
     * EN: Calendar system used (gregorian, julian, etc.).
     *
     * DTD: calendar CDATA #IMPLIED
     */
    private String calendar;

    /**
     * 날짜 유형 / Date type
     *
     * KR: 날짜의 의미나 역할 (pub, received, accepted 등).
     * EN: Meaning or role of the date (pub, received, accepted, etc.).
     *
     * DTD: date-type CDATA #IMPLIED
     */
    private String dateType;

    /**
     * ISO 8601 형식 날짜 / ISO 8601 formatted date
     *
     * KR: ISO 8601 표준 형식의 날짜 (YYYY-MM-DD).
     * EN: Date in ISO 8601 standard format (YYYY-MM-DD).
     *
     * DTD: iso-8601-date CDATA #IMPLIED
     *
     * Example: "2024-01-15"
     */
    private String iso8601Date;

    /**
     * 출판 유형 / Publication type
     *
     * KR: 출판 매체 유형 (ppub, epub, collection 등).
     * EN: Publication medium type (ppub, epub, collection, etc.).
     *
     * DTD: pub-type (ppub | epub | epub-ppub | ppub-epub | collection | epreprint) #IMPLIED
     *
     * Values:
     * - ppub: Print publication (종이 출판)
     * - epub: Electronic publication (전자 출판)
     * - epub-ppub: Electronic before print (전자 우선 출판)
     * - ppub-epub: Print before electronic (종이 우선 출판)
     * - collection: Collection date (컬렉션 날짜)
     * - epreprint: Electronic preprint (전자 프리프린트)
     */
    private String pubType;

    /**
     * 출판 형식 / Publication format
     *
     * KR: 출판 형식 (print, electronic, online 등).
     * EN: Publication format (print, electronic, online, etc.).
     *
     * DTD: publication-format (print | electronic | print-electronic | online) #IMPLIED
     *
     * Values:
     * - print: Print format (종이 형식)
     * - electronic: Electronic format (전자 형식)
     * - print-electronic: Both print and electronic (종이+전자)
     * - online: Online format (온라인 형식)
     */
    private String publicationFormat;

    /**
     * 일 / Day
     *
     * KR: 출판 일.
     * EN: Publication day.
     *
     * DTD: day?
     */
    private Day day;

    /**
     * 월 / Month
     *
     * KR: 출판 월.
     * EN: Publication month.
     *
     * DTD: month?
     */
    private Month month;

    /**
     * 계절 / Season
     *
     * KR: 출판 계절 (Spring, Summer, Fall, Winter 등).
     * EN: Publication season (Spring, Summer, Fall, Winter, etc.).
     *
     * DTD: season?
     *
     * Note: Alternative to day/month when publication is by season.
     */
    private Season season;

    /**
     * 연도 / Year (REQUIRED)
     *
     * KR: 출판 연도 (필수).
     * EN: Publication year (required).
     *
     * DTD: year
     * Required: YES
     */
    private Year year;

    /**
     * 시대 / Era
     *
     * KR: 시대 표시 (BC, AD, CE, BCE 등).
     * EN: Era designation (BC, AD, CE, BCE, etc.).
     *
     * DTD: era?
     */
    private Era era;
}
