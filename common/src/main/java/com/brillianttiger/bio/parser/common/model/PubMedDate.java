package com.brillianttiger.bio.parser.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubMed 날짜 (Year/Month/Day 조합) / PubMed Date
 *
 * KR: DateCompleted, DateRevised 등에 사용되는 날짜 구조
 * EN: Date structure used for DateCompleted, DateRevised, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubMedDate {

    /**
     * 년도 / Year
     */
    private String year;

    /**
     * 월 / Month
     */
    private String month;

    /**
     * 일 / Day
     */
    private String day;
}
