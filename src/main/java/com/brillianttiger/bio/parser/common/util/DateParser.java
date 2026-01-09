package com.brillianttiger.bio.parser.common.util;

import com.brillianttiger.bio.parser.common.model.DateComponents;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DateParser / 날짜 파싱 유틸리티
 *
 * KR: PubMed MedlineDate 및 다양한 날짜 형식을 파싱하는 유틸리티
 * EN: Utility for parsing PubMed MedlineDate and various date formats
 *
 * Supported formats:
 * - "2024 Jan-Feb"
 * - "2024 Spring"
 * - "2024 Jan 15-Feb 20"
 * - "2024 Jan-2025 Feb"
 * - "2024"
 * - "2024 Jan"
 * - "2024 Jan 15"
 */
public class DateParser {

    /**
     * 월 이름 매핑 / Month name mapping
     */
    private static final Map<String, Integer> MONTH_MAP = new HashMap<>();

    /**
     * 계절 매핑 / Season mapping
     */
    private static final Map<String, String> SEASON_MAP = new HashMap<>();

    static {
        // 월 이름 초기화 / Initialize month names
        MONTH_MAP.put("jan", 1);
        MONTH_MAP.put("feb", 2);
        MONTH_MAP.put("mar", 3);
        MONTH_MAP.put("apr", 4);
        MONTH_MAP.put("may", 5);
        MONTH_MAP.put("jun", 6);
        MONTH_MAP.put("jul", 7);
        MONTH_MAP.put("aug", 8);
        MONTH_MAP.put("sep", 9);
        MONTH_MAP.put("oct", 10);
        MONTH_MAP.put("nov", 11);
        MONTH_MAP.put("dec", 12);

        MONTH_MAP.put("january", 1);
        MONTH_MAP.put("february", 2);
        MONTH_MAP.put("march", 3);
        MONTH_MAP.put("april", 4);
        MONTH_MAP.put("june", 6);
        MONTH_MAP.put("july", 7);
        MONTH_MAP.put("august", 8);
        MONTH_MAP.put("september", 9);
        MONTH_MAP.put("october", 10);
        MONTH_MAP.put("november", 11);
        MONTH_MAP.put("december", 12);

        // 계절 초기화 / Initialize seasons
        SEASON_MAP.put("spring", "Spring");
        SEASON_MAP.put("summer", "Summer");
        SEASON_MAP.put("fall", "Fall");
        SEASON_MAP.put("autumn", "Autumn");
        SEASON_MAP.put("winter", "Winter");
    }

    /**
     * 정규식 패턴 / Regex patterns
     */
    private static final Pattern YEAR_PATTERN = Pattern.compile("\\b(\\d{4})\\b");
    private static final Pattern MONTH_PATTERN = Pattern.compile("\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|June|July|August|September|October|November|December)\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern DAY_PATTERN = Pattern.compile("\\b(\\d{1,2})\\b");
    private static final Pattern SEASON_PATTERN = Pattern.compile("\\b(Spring|Summer|Fall|Autumn|Winter)\\b", Pattern.CASE_INSENSITIVE);

    /**
     * MedlineDate 정보 클래스 / MedlineDate information class
     */
    public static class ParsedDate {
        private final String year;
        private final String month;
        private final String day;
        private final String season;
        private final String endYear;
        private final String endMonth;
        private final String endDay;
        private final String rawValue;

        public ParsedDate(String year, String month, String day, String season,
                          String endYear, String endMonth, String endDay, String rawValue) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.season = season;
            this.endYear = endYear;
            this.endMonth = endMonth;
            this.endDay = endDay;
            this.rawValue = rawValue;
        }

        public String getYear() { return year; }
        public String getMonth() { return month; }
        public String getDay() { return day; }
        public String getSeason() { return season; }
        public String getEndYear() { return endYear; }
        public String getEndMonth() { return endMonth; }
        public String getEndDay() { return endDay; }
        public String getRawValue() { return rawValue; }

        public boolean hasDateRange() {
            return endYear != null || endMonth != null || endDay != null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (year != null) sb.append(year);
            if (month != null) sb.append(" ").append(month);
            if (day != null) sb.append(" ").append(day);
            if (season != null) sb.append(" ").append(season);

            if (hasDateRange()) {
                sb.append(" - ");
                if (endYear != null) sb.append(endYear);
                if (endMonth != null) sb.append(" ").append(endMonth);
                if (endDay != null) sb.append(" ").append(endDay);
            }

            return sb.toString().trim();
        }
    }

    /**
     * MedlineDate 파싱 / Parse MedlineDate
     *
     * KR: MedlineDate 문자열을 파싱하여 구조화된 날짜 정보 반환
     * EN: Parse MedlineDate string and return structured date information
     *
     * @param medlineDate MedlineDate 문자열 (예: "2024 Jan-Feb") / MedlineDate string (e.g., "2024 Jan-Feb")
     * @return ParsedDate 객체 / ParsedDate object
     */
    public static ParsedDate parseMedlineDate(String medlineDate) {
        if (medlineDate == null || medlineDate.trim().isEmpty()) {
            return null;
        }

        String input = medlineDate.trim();

        // 계절 확인 / Check for season
        Matcher seasonMatcher = SEASON_PATTERN.matcher(input);
        if (seasonMatcher.find()) {
            String season = seasonMatcher.group(1);
            Matcher yearMatcher = YEAR_PATTERN.matcher(input);
            String year = yearMatcher.find() ? yearMatcher.group(1) : null;

            return new ParsedDate(year, null, null, season, null, null, null, input);
        }

        // 날짜 범위 확인 (하이픈으로 구분) / Check for date range (separated by hyphen)
        if (input.contains("-")) {
            return parseDateRange(input);
        }

        // 단일 날짜 파싱 / Parse single date
        return parseSingleDate(input);
    }

    /**
     * 단일 날짜 파싱 / Parse single date
     *
     * KR: "2024 Jan 15" 형식의 단일 날짜 파싱
     * EN: Parse single date in "2024 Jan 15" format
     */
    private static ParsedDate parseSingleDate(String input) {
        Matcher yearMatcher = YEAR_PATTERN.matcher(input);
        String year = yearMatcher.find() ? yearMatcher.group(1) : null;

        Matcher monthMatcher = MONTH_PATTERN.matcher(input);
        String month = monthMatcher.find() ? monthMatcher.group(1) : null;

        // 월 다음의 숫자를 일로 파싱 / Parse number after month as day
        String day = null;
        if (month != null) {
            int monthEndIndex = input.toLowerCase().indexOf(month.toLowerCase()) + month.length();
            String afterMonth = input.substring(monthEndIndex).trim();
            Matcher dayMatcher = DAY_PATTERN.matcher(afterMonth);
            if (dayMatcher.find()) {
                day = dayMatcher.group(1);
            }
        }

        return new ParsedDate(year, month, day, null, null, null, null, input);
    }

    /**
     * 날짜 범위 파싱 / Parse date range
     *
     * KR: "2024 Jan-Feb", "2024 Jan 15-Feb 20" 형식의 날짜 범위 파싱
     * EN: Parse date range in "2024 Jan-Feb", "2024 Jan 15-Feb 20" format
     */
    private static ParsedDate parseDateRange(String input) {
        String[] parts = input.split("-", 2);
        if (parts.length != 2) {
            return parseSingleDate(input);
        }

        String startPart = parts[0].trim();
        String endPart = parts[1].trim();

        // 시작 날짜 파싱 / Parse start date
        ParsedDate startDate = parseSingleDate(startPart);

        // 종료 날짜 파싱 / Parse end date
        Matcher endYearMatcher = YEAR_PATTERN.matcher(endPart);
        String endYear = endYearMatcher.find() ? endYearMatcher.group(1) : null;

        Matcher endMonthMatcher = MONTH_PATTERN.matcher(endPart);
        String endMonth = endMonthMatcher.find() ? endMonthMatcher.group(1) : null;

        String endDay = null;
        if (endMonth != null) {
            int monthEndIndex = endPart.toLowerCase().indexOf(endMonth.toLowerCase()) + endMonth.length();
            String afterMonth = endPart.substring(monthEndIndex).trim();
            Matcher dayMatcher = DAY_PATTERN.matcher(afterMonth);
            if (dayMatcher.find()) {
                endDay = dayMatcher.group(1);
            }
        } else {
            // 월이 없으면 숫자를 일로 간주 / If no month, treat number as day
            Matcher dayMatcher = DAY_PATTERN.matcher(endPart);
            if (dayMatcher.find()) {
                endDay = dayMatcher.group(1);
            }
        }

        return new ParsedDate(
                startDate.getYear(),
                startDate.getMonth(),
                startDate.getDay(),
                null,
                endYear,
                endMonth,
                endDay,
                input
        );
    }

    /**
     * 월 이름을 숫자로 변환 / Convert month name to number
     *
     * KR: 월 이름(Jan, February 등)을 1-12 숫자로 변환
     * EN: Convert month name (Jan, February, etc.) to 1-12 number
     *
     * @param monthName 월 이름 / Month name
     * @return 월 숫자 (1-12) 또는 null / Month number (1-12) or null
     */
    public static Integer parseMonthName(String monthName) {
        if (monthName == null || monthName.trim().isEmpty()) {
            return null;
        }

        return MONTH_MAP.get(monthName.toLowerCase());
    }

    /**
     * 월 숫자를 이름으로 변환 / Convert month number to name
     *
     * KR: 월 숫자(1-12)를 짧은 이름(Jan, Feb 등)으로 변환
     * EN: Convert month number (1-12) to short name (Jan, Feb, etc.)
     *
     * @param monthNumber 월 숫자 (1-12) / Month number (1-12)
     * @return 월 짧은 이름 또는 null / Short month name or null
     */
    public static String getMonthShortName(int monthNumber) {
        if (monthNumber < 1 || monthNumber > 12) {
            return null;
        }

        try {
            return Month.of(monthNumber).name().substring(0, 3);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * 연도 유효성 검증 / Validate year
     *
     * KR: 연도가 유효한 범위(1000-9999)인지 확인
     * EN: Check if year is in valid range (1000-9999)
     *
     * @param yearStr 연도 문자열 / Year string
     * @return 유효 여부 / Validity
     */
    public static boolean isValidYear(String yearStr) {
        if (yearStr == null || yearStr.trim().isEmpty()) {
            return false;
        }

        try {
            int year = Integer.parseInt(yearStr);
            return year >= 1000 && year <= 9999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 월 유효성 검증 / Validate month
     *
     * KR: 월이 유효한 범위(1-12)인지 확인
     * EN: Check if month is in valid range (1-12)
     *
     * @param monthStr 월 문자열 / Month string
     * @return 유효 여부 / Validity
     */
    public static boolean isValidMonth(String monthStr) {
        if (monthStr == null || monthStr.trim().isEmpty()) {
            return false;
        }

        try {
            int month = Integer.parseInt(monthStr);
            return month >= 1 && month <= 12;
        } catch (NumberFormatException e) {
            // 월 이름인 경우 / If it's a month name
            return parseMonthName(monthStr) != null;
        }
    }

    /**
     * 일 유효성 검증 / Validate day
     *
     * KR: 일이 유효한 범위(1-31)인지 확인
     * EN: Check if day is in valid range (1-31)
     *
     * @param dayStr 일 문자열 / Day string
     * @return 유효 여부 / Validity
     */
    public static boolean isValidDay(String dayStr) {
        if (dayStr == null || dayStr.trim().isEmpty()) {
            return false;
        }

        try {
            int day = Integer.parseInt(dayStr);
            return day >= 1 && day <= 31;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * ISO 8601 형식으로 변환 / Convert to ISO 8601 format
     *
     * KR: ParsedDate를 ISO 8601 형식(YYYY-MM-DD)으로 변환
     * EN: Convert ParsedDate to ISO 8601 format (YYYY-MM-DD)
     *
     * @param parsedDate ParsedDate 객체 / ParsedDate object
     * @return ISO 8601 날짜 문자열 또는 null / ISO 8601 date string or null
     */
    public static String toIso8601(ParsedDate parsedDate) {
        if (parsedDate == null || parsedDate.getYear() == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(parsedDate.getYear());

        if (parsedDate.getMonth() != null) {
            Integer monthNumber = parseMonthName(parsedDate.getMonth());
            if (monthNumber != null) {
                sb.append("-").append(String.format("%02d", monthNumber));

                if (parsedDate.getDay() != null && isValidDay(parsedDate.getDay())) {
                    sb.append("-").append(String.format("%02d", Integer.parseInt(parsedDate.getDay())));
                }
            }
        }

        return sb.toString();
    }

    // ==================== DateComponents 통합 메서드 / DateComponents Integration Methods ====================

    /**
     * MedlineDate 파싱 (DateComponents 반환) / Parse MedlineDate (returns DateComponents)
     *
     * KR: MedlineDate를 파싱하여 DateComponents 반환
     * EN: Parse MedlineDate and return DateComponents
     *
     * @param medlineDate MedlineDate 문자열 / MedlineDate string
     * @return DateComponents 객체 / DateComponents object
     */
    public static DateComponents parseMedlineDateToComponents(String medlineDate) {
        if (medlineDate == null || medlineDate.isBlank()) {
            return null;
        }

        DateComponents.DateComponentsBuilder builder = DateComponents.builder()
                .medlineDate(medlineDate);

        // 연도 추출 / Extract year
        Matcher yearMatcher = YEAR_PATTERN.matcher(medlineDate);
        if (yearMatcher.find()) {
            builder.year(Integer.parseInt(yearMatcher.group(1)));
        }

        // 계절 확인 / Check for season
        Matcher seasonMatcher = SEASON_PATTERN.matcher(medlineDate);
        if (seasonMatcher.find()) {
            String season = seasonMatcher.group(1);
            String normalizedSeason = season.substring(0, 1).toUpperCase() + season.substring(1).toLowerCase();
            builder.season(DateComponents.Season.fromValue(normalizedSeason));
            return builder.build();
        }

        // 월 범위 추출 (예: Jan-Feb) / Extract month range (e.g., Jan-Feb)
        Pattern monthRangePattern = Pattern.compile("([A-Za-z]{3,})-([A-Za-z]{3,})");
        Matcher monthRangeMatcher = monthRangePattern.matcher(medlineDate);
        if (monthRangeMatcher.find()) {
            String startMonth = monthRangeMatcher.group(1);
            Integer monthNum = parseMonthName(startMonth);
            if (monthNum != null) {
                builder.month(monthNum);
            }
        } else {
            // 단일 월 추출 / Extract single month
            Matcher monthMatcher = MONTH_PATTERN.matcher(medlineDate);
            if (monthMatcher.find()) {
                String month = monthMatcher.group(1);
                Integer monthNum = parseMonthName(month);
                if (monthNum != null) {
                    builder.month(monthNum);
                }
            }
        }

        // 일 추출 / Extract day
        Matcher monthMatcher = MONTH_PATTERN.matcher(medlineDate);
        if (monthMatcher.find()) {
            String month = monthMatcher.group(1);
            int monthEndIndex = medlineDate.toLowerCase().indexOf(month.toLowerCase()) + month.length();
            String afterMonth = medlineDate.substring(monthEndIndex).trim();
            Matcher dayMatcher = DAY_PATTERN.matcher(afterMonth);
            if (dayMatcher.find()) {
                builder.day(Integer.parseInt(dayMatcher.group(1)));
            }
        }

        return builder.build();
    }

    /**
     * ISO 8601 날짜 파싱 / Parse ISO 8601 date
     *
     * KR: ISO 8601 형식의 날짜를 파싱하여 DateComponents 반환
     * EN: Parse ISO 8601 format date and return DateComponents
     *
     * Supported formats:
     * - 2024-01-15T10:30:00Z
     * - 2024-01-15
     * - 2024-01
     * - 2024
     *
     * @param iso8601Date ISO 8601 날짜 문자열 / ISO 8601 date string
     * @return DateComponents 객체 / DateComponents object
     */
    public static DateComponents parseIso8601Date(String iso8601Date) {
        if (iso8601Date == null || iso8601Date.isBlank()) {
            return null;
        }

        DateComponents.DateComponentsBuilder builder = DateComponents.builder()
                .iso8601Date(iso8601Date);

        try {
            // YYYY-MM-DDTHH:MM:SS 형식 처리 / Handle YYYY-MM-DDTHH:MM:SS format
            String dateOnlyPart = iso8601Date;
            if (iso8601Date.contains("T")) {
                dateOnlyPart = iso8601Date.substring(0, iso8601Date.indexOf("T"));
            }

            // YYYY-MM-DD 형식 / YYYY-MM-DD format
            try {
                LocalDate date = LocalDate.parse(dateOnlyPart);
                builder.year(date.getYear())
                       .month(date.getMonthValue())
                       .day(date.getDayOfMonth());
                return builder.build();
            } catch (DateTimeParseException e) {
                // YYYY-MM 형식 시도 / Try YYYY-MM format
                try {
                    YearMonth ym = YearMonth.parse(dateOnlyPart);
                    builder.year(ym.getYear())
                           .month(ym.getMonthValue());
                    return builder.build();
                } catch (DateTimeParseException e2) {
                    // YYYY 형식 시도 / Try YYYY format
                    try {
                        Year y = Year.parse(dateOnlyPart);
                        builder.year(y.getValue());
                        return builder.build();
                    } catch (DateTimeParseException e3) {
                        // 파싱 실패 / Parsing failed
                        return builder.build();
                    }
                }
            }
        } catch (Exception e) {
            return builder.build();
        }
    }

    /**
     * 월 이름을 숫자로 변환 (별칭 메서드) / Convert month name to number (alias method)
     *
     * KR: parseMonthName의 별칭. 더 직관적인 이름.
     * EN: Alias for parseMonthName. More intuitive name.
     *
     * @param monthName 월 이름 / Month name
     * @return 월 숫자 (1-12) / Month number (1-12)
     */
    public static Integer monthNameToNumber(String monthName) {
        return parseMonthName(monthName);
    }

    /**
     * 계절을 월 범위로 변환 / Convert season to month range
     *
     * KR: 계절 이름을 시작-종료 월 범위로 변환
     * EN: Convert season name to start-end month range
     *
     * @param season 계절 이름 (Spring, Summer, Fall/Autumn, Winter) / Season name
     * @return 월 범위 배열 [시작월, 종료월] 또는 null / Month range array [start, end] or null
     */
    public static int[] seasonToMonthRange(String season) {
        if (season == null || season.isBlank()) {
            return null;
        }

        return switch (season.toLowerCase()) {
            case "spring" -> new int[]{3, 5};    // 3월-5월 / March-May
            case "summer" -> new int[]{6, 8};    // 6월-8월 / June-August
            case "fall", "autumn" -> new int[]{9, 11};  // 9월-11월 / September-November
            case "winter" -> new int[]{12, 2};   // 12월-2월 / December-February
            default -> null;
        };
    }

    /**
     * 계절의 대표 월 반환 / Get representative month for season
     *
     * KR: 계절의 중간 월을 반환 (Spring→4, Summer→7, Fall→10, Winter→1)
     * EN: Return middle month of season (Spring→4, Summer→7, Fall→10, Winter→1)
     *
     * @param season 계절 이름 / Season name
     * @return 대표 월 (1-12) 또는 null / Representative month (1-12) or null
     */
    public static Integer seasonToRepresentativeMonth(String season) {
        if (season == null || season.isBlank()) {
            return null;
        }

        return switch (season.toLowerCase()) {
            case "spring" -> 4;   // April
            case "summer" -> 7;   // July
            case "fall", "autumn" -> 10;  // October
            case "winter" -> 1;   // January
            default -> null;
        };
    }

    /**
     * ParsedDate를 DateComponents로 변환 / Convert ParsedDate to DateComponents
     *
     * KR: 기존 ParsedDate 객체를 DateComponents로 변환
     * EN: Convert legacy ParsedDate object to DateComponents
     *
     * @param parsedDate ParsedDate 객체 / ParsedDate object
     * @return DateComponents 객체 / DateComponents object
     */
    public static DateComponents toDateComponents(ParsedDate parsedDate) {
        if (parsedDate == null) {
            return null;
        }

        DateComponents.DateComponentsBuilder builder = DateComponents.builder()
                .medlineDate(parsedDate.getRawValue());

        // 연도 / Year
        if (parsedDate.getYear() != null) {
            try {
                builder.year(Integer.parseInt(parsedDate.getYear()));
            } catch (NumberFormatException ignored) {}
        }

        // 월 / Month
        if (parsedDate.getMonth() != null) {
            Integer monthNum = parseMonthName(parsedDate.getMonth());
            if (monthNum != null) {
                builder.month(monthNum);
            }
        }

        // 일 / Day
        if (parsedDate.getDay() != null) {
            try {
                builder.day(Integer.parseInt(parsedDate.getDay()));
            } catch (NumberFormatException ignored) {}
        }

        // 계절 / Season
        if (parsedDate.getSeason() != null) {
            builder.season(DateComponents.Season.fromValue(parsedDate.getSeason()));
        }

        return builder.build();
    }
}
