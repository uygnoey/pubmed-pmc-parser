package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DateCompleted / 완료 날짜
 *
 * DTD: <!ELEMENT DateCompleted (Year, Month, Day)>
 *
 * KR: 논문의 MEDLINE 색인 완료 날짜
 * EN: MEDLINE indexing completion date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateCompleted {

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
