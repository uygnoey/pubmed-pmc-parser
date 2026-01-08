package com.brillianttiger.bio.parser.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DateComponents / 날짜 컴포넌트 공통 모델
 *
 * KR: 날짜 컴포넌트 공통 모델.
 *     PubMed의 PubDate, ArticleDate와 JATS의 pub-date, date에 사용.
 * EN: Common model for date components.
 *     Used for PubMed PubDate, ArticleDate and JATS pub-date, date.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateComponents {

    private Integer year;
    private Integer month;
    private Integer day;
    private String season;          // "Spring", "Summer", etc.
    private String medlineDate;     // "2024 Jan-Feb" 같은 비정형
    private String stringDate;      // JATS string-date
    private String era;             // JATS era

    // JATS 전용 속성 / JATS-specific attributes
    private String dateType;        // received, accepted, pub, etc.
    private String pubType;         // ppub, epub, etc.
    private String publicationFormat;  // print, electronic
    private String iso8601Date;     // ISO 8601 형식
    private String calendar;        // 달력 종류

    /**
     * LocalDate로 변환 (가능한 경우) / Convert to LocalDate (if possible)
     *
     * @return LocalDate or null
     */
    public LocalDate toLocalDate() {
        if (year == null) {
            return null;
        }
        int m = month != null ? month : 1;
        int d = day != null ? day : 1;
        return LocalDate.of(year, m, d);
    }

    /**
     * 날짜 문자열 표현 / Date string representation
     *
     * @return formatted date string
     */
    public String toDisplayString() {
        if (medlineDate != null) {
            return medlineDate;
        }
        if (stringDate != null) {
            return stringDate;
        }

        StringBuilder sb = new StringBuilder();
        if (year != null) {
            sb.append(year);
        }
        if (month != null) {
            sb.append("-").append(String.format("%02d", month));
        }
        if (day != null) {
            sb.append("-").append(String.format("%02d", day));
        }
        if (season != null) {
            sb.append(" ").append(season);
        }

        return sb.toString();
    }
}
