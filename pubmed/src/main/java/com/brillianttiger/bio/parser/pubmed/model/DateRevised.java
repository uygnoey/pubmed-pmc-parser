package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DateRevised / 수정 날짜
 *
 * DTD: <!ELEMENT DateRevised (Year, Month, Day)>
 *
 * KR: 논문의 MEDLINE 레코드 수정 날짜
 * EN: MEDLINE record revision date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRevised {

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
