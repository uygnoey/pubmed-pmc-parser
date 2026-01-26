package io.brillianttiger.bio.parser.common.model;

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
    private Season season;          // 계절 / Season
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
            sb.append(" ").append(season.getValue());
        }

        return sb.toString();
    }

    /**
     * Season / 계절
     *
     * KR: 날짜의 계절 정보
     * EN: Season information for dates
     */
    public enum Season {
        /**
         * 봄 / Spring
         */
        SPRING("Spring"),

        /**
         * 여름 / Summer
         */
        SUMMER("Summer"),

        /**
         * 가을 / Fall
         */
        FALL("Fall"),

        /**
         * 겨울 / Winter
         */
        WINTER("Winter");

        private final String value;

        Season(String value) {
            this.value = value;
        }

        /**
         * 문자열 값 반환 / Get string value
         *
         * @return DTD/JATS 문자열 값 / DTD/JATS string value
         */
        public String getValue() {
            return value;
        }

        /**
         * 문자열에서 Season enum 변환 / Parse string to Season enum
         *
         * KR: XML 파싱 시 문자열을 enum으로 변환
         * EN: Convert string to enum during XML parsing
         *
         * @param value 계절 문자열 값 / Season string value
         * @return Season enum 또는 null / Season enum or null
         */
        public static Season fromValue(String value) {
            if (value == null) {
                return null;
            }

            for (Season season : Season.values()) {
                if (season.value.equalsIgnoreCase(value)) {
                    return season;
                }
            }

            // 알 수 없는 값인 경우 null 반환 / Return null for unknown values
            return null;
        }
    }
}
