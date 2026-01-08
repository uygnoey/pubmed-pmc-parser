package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubDate (Publication Date) / 출판 날짜
 *
 * DTD: <!ELEMENT PubDate ((Year, ((Month, Day?) | Season)?) | MedlineDate)>
 *
 * KR: 저널 출판 날짜 (구조화된 날짜 또는 비정형 날짜)
 * EN: Journal publication date (structured or unstructured)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubDate {

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
     * 계절 / Season
     */
    private Season season;

    /**
     * MEDLINE 날짜 (비정형) / MedlineDate (unstructured)
     */
    private MedlineDate medlineDate;
}
