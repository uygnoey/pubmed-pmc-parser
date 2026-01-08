package com.bioxml.parser.common.util;

import java.time.Month;
import java.time.Year;
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
}
