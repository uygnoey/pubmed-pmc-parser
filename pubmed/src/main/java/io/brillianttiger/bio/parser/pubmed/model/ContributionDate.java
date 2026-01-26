package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ContributionDate / 기여 날짜
 *
 * DTD: <!ELEMENT ContributionDate ( Year, ((Month, Day?) | Season)? )>
 *
 * KR: 도서 문서에 대한 기여 날짜
 * EN: Contribution date for book document
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContributionDate {

    /**
     * 연도 (필수) / Year (required)
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
}
