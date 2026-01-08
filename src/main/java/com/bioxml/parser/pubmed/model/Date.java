package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date / 날짜
 *
 * DTD: <!ELEMENT DateCompleted (Year, Month, Day)>
 * DTD: <!ELEMENT DateRevised (Year, Month, Day)>
 *
 * KR: 날짜 정보 (연월일)
 * EN: Date information (year, month, day)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Date {

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
}
