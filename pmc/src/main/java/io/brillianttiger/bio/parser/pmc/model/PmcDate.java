package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date / 날짜
 *
 * KR: 논문 이력의 특정 날짜 정보 (접수, 승인 등). JATS 1.4 완전 준수 모델.
 * EN: Specific date information in article history (received, accepted, etc.). Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT date (
 *          ((day?, month?) | season)?,
 *          year?,
 *          era?,
 *          string-date?
 *      )>
 *
 * DTD: <!ATTLIST date
 *          calendar CDATA #IMPLIED
 *          date-type CDATA #IMPLIED
 *          iso-8601-date CDATA #IMPLIED
 *          publication-format CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/date.html
 *
 * Note: date-type common values:
 * - received: Manuscript received date (논문 접수일)
 * - accepted: Manuscript accepted date (논문 승인일)
 * - rev-recd: Revised manuscript received (수정본 접수일)
 * - corrected: Correction date (정정일)
 * - pub: Publication date (출판일)
 * - retracted: Retraction date (철회일)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcDate {

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
     * KR: 날짜의 의미 (received, accepted, rev-recd, corrected, pub, retracted).
     * EN: Meaning of the date (received, accepted, rev-recd, corrected, pub, retracted).
     *
     * DTD: date-type CDATA #IMPLIED
     *
     * Common values:
     * - received: Manuscript received (논문 접수)
     * - accepted: Manuscript accepted (논문 승인)
     * - rev-recd: Revised manuscript received (수정본 접수)
     * - corrected: Correction date (정정)
     * - pub: Publication date (출판)
     * - retracted: Retraction date (철회)
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
     * Example: "2023-05-15"
     */
    private String iso8601Date;

    /**
     * 출판 형식 / Publication format
     *
     * KR: 출판 형식 (print, electronic, online 등).
     * EN: Publication format (print, electronic, online, etc.).
     *
     * DTD: publication-format CDATA #IMPLIED
     */
    private String publicationFormat;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 날짜의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this date (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 일 / Day
     *
     * KR: 날짜의 일.
     * EN: Day of the date.
     *
     * DTD: day?
     */
    private Day day;

    /**
     * 월 / Month
     *
     * KR: 날짜의 월.
     * EN: Month of the date.
     *
     * DTD: month?
     */
    private Month month;

    /**
     * 계절 / Season
     *
     * KR: 날짜의 계절 (Spring, Summer, Fall, Winter 등).
     * EN: Season of the date (Spring, Summer, Fall, Winter, etc.).
     *
     * DTD: season?
     *
     * Note: Alternative to day/month when date is by season.
     */
    private Season season;

    /**
     * 연도 / Year
     *
     * KR: 날짜의 연도.
     * EN: Year of the date.
     *
     * DTD: year?
     * Required: NO (optional in date, unlike pub-date)
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

    /**
     * 문자열 날짜 / String date
     *
     * KR: 비정형 날짜 문자열 (예: "Spring 2024", "Early 2023").
     * EN: Unstructured date string (e.g., "Spring 2024", "Early 2023").
     *
     * DTD: string-date?
     */
    private StringDate stringDate;
}
