package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ArticleDate / 논문 날짜
 *
 * DTD: <!ELEMENT ArticleDate (Year, Month, Day)>
 * DTD: <!ATTLIST ArticleDate DateType CDATA #REQUIRED>
 *
 * KR: 논문의 전자 출판 날짜
 * EN: Article electronic publication date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDate {

    /**
     * 날짜 유형 (필수) / Date type (required)
     */
    private String dateType;

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
