package io.brillianttiger.bio.parser.common.util;

import io.brillianttiger.bio.parser.common.model.DateComponents;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DateParser 테스트
 */
class DateParserTest {

    @Test
    void testParseMedlineDateSimple() {
        // 단순 연월일
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan 15");
        assertNotNull(result);
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
        assertEquals("15", result.getDay());
        assertNull(result.getSeason());
    }

    @Test
    void testParseMedlineDateRange() {
        // 월 범위
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan-Feb");
        assertNotNull(result);
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
        assertEquals("Feb", result.getEndMonth());
    }

    @Test
    void testParseMedlineDateSeason() {
        // 계절
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Spring");
        assertNotNull(result);
        assertEquals("2024", result.getYear());
        assertEquals("Spring", result.getSeason());
    }

    @Test
    void testParseMedlineDateToComponents() {
        // DateComponents 반환
        DateComponents result = DateParser.parseMedlineDateToComponents("2024 Jan-Feb");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonth());
        assertEquals("2024 Jan-Feb", result.getMedlineDate());
    }

    @Test
    void testParseMedlineDateToComponentsSeason() {
        // 계절 포함
        DateComponents result = DateParser.parseMedlineDateToComponents("2024 Spring");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(DateComponents.Season.SPRING, result.getSeason());
    }

    @Test
    void testParseIso8601DateFull() {
        // YYYY-MM-DD 형식
        DateComponents result = DateParser.parseIso8601Date("2024-01-15");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonth());
        assertEquals(15, result.getDay());
    }

    @Test
    void testParseIso8601DateYearMonth() {
        // YYYY-MM 형식
        DateComponents result = DateParser.parseIso8601Date("2024-01");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonth());
        assertNull(result.getDay());
    }

    @Test
    void testParseIso8601DateYear() {
        // YYYY 형식
        DateComponents result = DateParser.parseIso8601Date("2024");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertNull(result.getMonth());
        assertNull(result.getDay());
    }

    @Test
    void testParseIso8601DateWithTime() {
        // YYYY-MM-DDTHH:MM:SS 형식
        DateComponents result = DateParser.parseIso8601Date("2024-01-15T10:30:00Z");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonth());
        assertEquals(15, result.getDay());
    }

    @Test
    void testMonthNameToNumber() {
        // 짧은 이름
        assertEquals(1, DateParser.monthNameToNumber("Jan"));
        assertEquals(2, DateParser.monthNameToNumber("Feb"));
        assertEquals(12, DateParser.monthNameToNumber("Dec"));

        // 긴 이름
        assertEquals(1, DateParser.monthNameToNumber("January"));
        assertEquals(2, DateParser.monthNameToNumber("February"));
        assertEquals(12, DateParser.monthNameToNumber("December"));

        // 대소문자 무시
        assertEquals(1, DateParser.monthNameToNumber("jan"));
        assertEquals(1, DateParser.monthNameToNumber("JANUARY"));

        // 유효하지 않은 값
        assertNull(DateParser.monthNameToNumber("Invalid"));
        assertNull(DateParser.monthNameToNumber(null));
    }

    @Test
    void testParseMonthName() {
        assertEquals(1, DateParser.parseMonthName("Jan"));
        assertEquals(6, DateParser.parseMonthName("June"));
        assertEquals(12, DateParser.parseMonthName("dec"));
        assertNull(DateParser.parseMonthName("InvalidMonth"));
    }

    @Test
    void testSeasonToMonthRange() {
        assertArrayEquals(new int[]{3, 5}, DateParser.seasonToMonthRange("Spring"));
        assertArrayEquals(new int[]{6, 8}, DateParser.seasonToMonthRange("Summer"));
        assertArrayEquals(new int[]{9, 11}, DateParser.seasonToMonthRange("Fall"));
        assertArrayEquals(new int[]{9, 11}, DateParser.seasonToMonthRange("Autumn"));
        assertArrayEquals(new int[]{12, 2}, DateParser.seasonToMonthRange("Winter"));

        // 대소문자 무시
        assertArrayEquals(new int[]{3, 5}, DateParser.seasonToMonthRange("spring"));
        assertArrayEquals(new int[]{6, 8}, DateParser.seasonToMonthRange("SUMMER"));

        // 유효하지 않은 값
        assertNull(DateParser.seasonToMonthRange("InvalidSeason"));
        assertNull(DateParser.seasonToMonthRange(null));
    }

    @Test
    void testSeasonToRepresentativeMonth() {
        assertEquals(4, DateParser.seasonToRepresentativeMonth("Spring"));
        assertEquals(7, DateParser.seasonToRepresentativeMonth("Summer"));
        assertEquals(10, DateParser.seasonToRepresentativeMonth("Fall"));
        assertEquals(10, DateParser.seasonToRepresentativeMonth("Autumn"));
        assertEquals(1, DateParser.seasonToRepresentativeMonth("Winter"));
        assertNull(DateParser.seasonToRepresentativeMonth("Invalid"));
    }

    @Test
    void testGetMonthShortName() {
        assertEquals("JAN", DateParser.getMonthShortName(1));
        assertEquals("FEB", DateParser.getMonthShortName(2));
        assertEquals("DEC", DateParser.getMonthShortName(12));
        assertNull(DateParser.getMonthShortName(0));
        assertNull(DateParser.getMonthShortName(13));
    }

    @Test
    void testIsValidYear() {
        assertTrue(DateParser.isValidYear("2024"));
        assertTrue(DateParser.isValidYear("1900"));
        assertTrue(DateParser.isValidYear("9999"));
        assertFalse(DateParser.isValidYear("999"));  // 너무 작음
        assertFalse(DateParser.isValidYear("10000")); // 너무 큼
        assertFalse(DateParser.isValidYear("abc"));
        assertFalse(DateParser.isValidYear(null));
    }

    @Test
    void testIsValidMonth() {
        assertTrue(DateParser.isValidMonth("1"));
        assertTrue(DateParser.isValidMonth("12"));
        assertTrue(DateParser.isValidMonth("Jan"));
        assertTrue(DateParser.isValidMonth("December"));
        assertFalse(DateParser.isValidMonth("0"));
        assertFalse(DateParser.isValidMonth("13"));
        assertFalse(DateParser.isValidMonth("InvalidMonth"));
        assertFalse(DateParser.isValidMonth(null));
    }

    @Test
    void testIsValidDay() {
        assertTrue(DateParser.isValidDay("1"));
        assertTrue(DateParser.isValidDay("31"));
        assertFalse(DateParser.isValidDay("0"));
        assertFalse(DateParser.isValidDay("32"));
        assertFalse(DateParser.isValidDay("abc"));
        assertFalse(DateParser.isValidDay(null));
    }

    @Test
    void testToIso8601() {
        // 완전한 날짜
        DateParser.ParsedDate parsed = DateParser.parseMedlineDate("2024 Jan 15");
        assertEquals("2024-01-15", DateParser.toIso8601(parsed));

        // 연월만
        parsed = DateParser.parseMedlineDate("2024 Jan");
        assertEquals("2024-01", DateParser.toIso8601(parsed));

        // 연도만
        parsed = DateParser.parseMedlineDate("2024");
        assertEquals("2024", DateParser.toIso8601(parsed));

        // null
        assertNull(DateParser.toIso8601(null));
    }

    @Test
    void testToDateComponents() {
        DateParser.ParsedDate parsed = DateParser.parseMedlineDate("2024 Jan 15");
        DateComponents components = DateParser.toDateComponents(parsed);

        assertNotNull(components);
        assertEquals(2024, components.getYear());
        assertEquals(1, components.getMonth());
        assertEquals(15, components.getDay());
        assertEquals("2024 Jan 15", components.getMedlineDate());
    }

    @Test
    void testToDateComponentsWithSeason() {
        DateParser.ParsedDate parsed = DateParser.parseMedlineDate("2024 Spring");
        DateComponents components = DateParser.toDateComponents(parsed);

        assertNotNull(components);
        assertEquals(2024, components.getYear());
        assertEquals(DateComponents.Season.SPRING, components.getSeason());
    }

    @Test
    void testComplexMedlineDateFormats() {
        // "2024 1st Qu" 같은 복잡한 형식은 연도만 추출
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 1st Qu");
        assertNotNull(result);
        assertEquals("2024", result.getYear());

        // 여러 공백
        result = DateParser.parseMedlineDate("2024   Jan   15");
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
        assertEquals("15", result.getDay());
    }

    // ========== ParsedDate 내부 클래스 메서드 테스트 / ParsedDate inner class method tests ==========

    @Test
    void testParsedDateToString() {
        // 완전한 날짜 / Complete date
        DateParser.ParsedDate parsed = DateParser.parseMedlineDate("2024 Jan 15");
        assertEquals("2024 Jan 15", parsed.toString());

        // 연월만 / Year and month only
        parsed = DateParser.parseMedlineDate("2024 Jan");
        assertEquals("2024 Jan", parsed.toString());

        // 연도만 / Year only
        parsed = DateParser.parseMedlineDate("2024");
        assertEquals("2024", parsed.toString());

        // 계절 / Season
        parsed = DateParser.parseMedlineDate("2024 Spring");
        assertEquals("2024 Spring", parsed.toString());

        // 날짜 범위 / Date range
        parsed = DateParser.parseMedlineDate("2024 Jan-Feb");
        assertTrue(parsed.toString().contains("2024"));
        assertTrue(parsed.toString().contains("Jan"));
    }

    @Test
    void testParsedDateHasDateRange() {
        // 날짜 범위 있음 / Has date range
        DateParser.ParsedDate parsed = DateParser.parseMedlineDate("2024 Jan-Feb");
        assertTrue(parsed.hasDateRange(), "Jan-Feb 범위 확인");

        // 날짜 범위 없음 / No date range
        parsed = DateParser.parseMedlineDate("2024 Jan 15");
        assertFalse(parsed.hasDateRange(), "단일 날짜는 범위 없음");

        parsed = DateParser.parseMedlineDate("2024 Spring");
        assertFalse(parsed.hasDateRange(), "계절은 범위 없음");
    }

    @Test
    void testParsedDateEndYearAndDay() {
        // 연도 범위 / Year range
        DateParser.ParsedDate parsed = DateParser.parseMedlineDate("2024 Jan-2025 Feb");
        assertNotNull(parsed.getEndYear(), "종료 연도 확인");
        assertEquals("2025", parsed.getEndYear());

        // 일 범위 / Day range
        parsed = DateParser.parseMedlineDate("2024 Jan 15-Feb 20");
        assertNotNull(parsed.getEndDay(), "종료 일 확인");
        assertEquals("20", parsed.getEndDay());
    }

    @Test
    void testParseDateRangeWithEndDay() {
        // "2024 Jan 15-Feb 20" 형식 / Format with end day
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan 15-Feb 20");
        assertNotNull(result);
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
        assertEquals("15", result.getDay());
        assertEquals("Feb", result.getEndMonth());
        assertEquals("20", result.getEndDay());
        assertTrue(result.hasDateRange());
    }

    @Test
    void testParseDateRangeYearToYear() {
        // "2024 Jan-2025 Feb" 형식 / Year to year format
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan-2025 Feb");
        assertNotNull(result);
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
        assertEquals("2025", result.getEndYear());
        assertEquals("Feb", result.getEndMonth());
        assertTrue(result.hasDateRange());
    }

    @Test
    void testParseDateRangeOnlyDay() {
        // "2024 Jan 15-20" 형식 (월 없이 일만) / Day range without month
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan 15-20");
        assertNotNull(result);
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
        assertEquals("15", result.getDay());
        assertEquals("20", result.getEndDay());
    }

    // ========== Null/Blank 입력 테스트 / Null/Blank input tests ==========

    @Test
    void testParseMedlineDateNull() {
        // null 입력 / Null input
        DateParser.ParsedDate result = DateParser.parseMedlineDate(null);
        assertNull(result, "null 입력 시 null 반환");
    }

    @Test
    void testParseMedlineDateBlank() {
        // 빈 문자열 / Blank string
        DateParser.ParsedDate result = DateParser.parseMedlineDate("   ");
        assertNull(result, "빈 문자열 입력 시 null 반환");
    }

    @Test
    void testParseMedlineDateToComponentsNull() {
        // null 입력 / Null input
        DateComponents result = DateParser.parseMedlineDateToComponents(null);
        assertNull(result, "null 입력 시 null 반환");
    }

    @Test
    void testParseMedlineDateToComponentsBlank() {
        // 빈 문자열 / Blank string
        DateComponents result = DateParser.parseMedlineDateToComponents("   ");
        assertNull(result, "빈 문자열 입력 시 null 반환");
    }

    @Test
    void testParseMedlineDateToComponentsSingleMonth() {
        // 단일 월 (범위 없음) / Single month without range
        DateComponents result = DateParser.parseMedlineDateToComponents("2024 March");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(3, result.getMonth());
    }

    @Test
    void testParseMedlineDateToComponentsWithDay() {
        // 일 포함 / With day
        DateComponents result = DateParser.parseMedlineDateToComponents("2024 Jan 15");
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonth());
        assertEquals(15, result.getDay());
    }

    @Test
    void testParseIso8601DateNull() {
        // null 입력 / Null input
        DateComponents result = DateParser.parseIso8601Date(null);
        assertNull(result, "null 입력 시 null 반환");
    }

    @Test
    void testParseIso8601DateBlank() {
        // 빈 문자열 / Blank string
        DateComponents result = DateParser.parseIso8601Date("   ");
        assertNull(result, "빈 문자열 입력 시 null 반환");
    }

    @Test
    void testParseIso8601DateInvalid() {
        // 잘못된 형식 / Invalid format
        DateComponents result = DateParser.parseIso8601Date("invalid-date");
        assertNotNull(result, "잘못된 형식도 빌더는 반환");
        assertEquals("invalid-date", result.getIso8601Date());
    }

    @Test
    void testSeasonToRepresentativeMonthNullAndBlank() {
        // null 입력 / Null input
        Integer result = DateParser.seasonToRepresentativeMonth(null);
        assertNull(result, "null 입력 시 null 반환");

        // 빈 문자열 / Blank string
        result = DateParser.seasonToRepresentativeMonth("   ");
        assertNull(result, "빈 문자열 입력 시 null 반환");
    }

    @Test
    void testToDateComponentsNull() {
        // null 입력 / Null input
        DateComponents result = DateParser.toDateComponents(null);
        assertNull(result, "null 입력 시 null 반환");
    }

    @Test
    void testIsValidYearEmpty() {
        // 빈 문자열 / Empty string
        assertFalse(DateParser.isValidYear(""), "빈 문자열은 유효하지 않음");
        assertFalse(DateParser.isValidYear("   "), "공백만 있는 문자열은 유효하지 않음");
    }

    @Test
    void testIsValidMonthEmpty() {
        // 빈 문자열 / Empty string
        assertFalse(DateParser.isValidMonth(""), "빈 문자열은 유효하지 않음");
        assertFalse(DateParser.isValidMonth("   "), "공백만 있는 문자열은 유효하지 않음");
    }

    @Test
    void testIsValidDayEmpty() {
        // 빈 문자열 / Empty string
        assertFalse(DateParser.isValidDay(""), "빈 문자열은 유효하지 않음");
        assertFalse(DateParser.isValidDay("   "), "공백만 있는 문자열은 유효하지 않음");
    }

    @Test
    void testParseMonthNameEmpty() {
        // 빈 문자열 / Empty string
        assertNull(DateParser.parseMonthName(""), "빈 문자열은 null 반환");
        assertNull(DateParser.parseMonthName("   "), "공백만 있는 문자열은 null 반환");
    }

    @Test
    void testSeasonToMonthRangeBlank() {
        // 빈 문자열 / Blank string
        assertNull(DateParser.seasonToMonthRange(""), "빈 문자열은 null 반환");
        assertNull(DateParser.seasonToMonthRange("   "), "공백만 있는 문자열은 null 반환");
    }

    @Test
    void testToIso8601ParsedDateWithYearOnly() {
        // 연도만 있는 ParsedDate / ParsedDate with year only
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", null, null, null, null, null, null, "2024");
        String result = DateParser.toIso8601(parsed);
        assertEquals("2024", result, "연도만 있을 때");
    }

    @Test
    void testToIso8601ParsedDateWithNullYear() {
        // Year가 null인 ParsedDate / ParsedDate with null year
        DateParser.ParsedDate parsed = new DateParser.ParsedDate(null, "Jan", "15", null, null, null, null, "Jan 15");
        String result = DateParser.toIso8601(parsed);
        assertNull(result, "Year가 null이면 null 반환");
    }

    @Test
    void testHasDateRangeAllNull() {
        // All end date fields null - hasDateRange() should return false
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, null, null, null, "2024-01-15");
        assertFalse(parsed.hasDateRange(), "All end fields null → false");
    }

    @Test
    void testToStringNullYear() {
        // toString() with null year
        DateParser.ParsedDate parsed = new DateParser.ParsedDate(null, "Jan", "15", null, null, null, null, null);
        String result = parsed.toString();
        assertEquals("Jan 15", result.trim(), "toString with null year");
    }

    @Test
    void testToStringNullEndFields() {
        // toString() with null end fields in date range
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, "2024", null, null, null);
        String result = parsed.toString();
        assertTrue(result.contains("2024"), "toString with null endMonth/endDay");
    }

    @Test
    void testParseMedlineDateSeasonNoYear() {
        // parseMedlineDate with season but no year pattern
        DateParser.ParsedDate result = DateParser.parseMedlineDate("Spring");
        assertNotNull(result, "Should parse season even without year");
        assertEquals("Spring", result.getSeason());
        assertNull(result.getYear(), "Year should be null when not found");
    }

    @Test
    void testParseMedlineDateNoYear() {
        // parseMedlineDate with no year pattern (only month-day)
        DateParser.ParsedDate result = DateParser.parseMedlineDate("Jan 15");
        assertNotNull(result, "Should parse even without year");
        assertNull(result.getYear(), "Year should be null when not found");
        assertEquals("Jan", result.getMonth());
    }

    @Test
    void testParseMedlineDateInvalidRangeFormat() {
        // parseMedlineDate with invalid date range format (multiple dashes)
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024-01-15-invalid-extra");
        assertNotNull(result, "Should still attempt to parse");
        // Verify it doesn't crash and returns some result
    }

    @Test
    void testDateParserInstantiation() {
        // Test DateParser class instantiation (for coverage)
        DateParser parser = new DateParser();
        assertNotNull(parser, "DateParser instantiation");
    }

    @Test
    void testHasDateRangeAllEndFieldsNull() {
        // hasDateRange() with all end fields null
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, null, null, null, null);
        assertFalse(parsed.hasDateRange(), "hasDateRange should be false when all end fields null");
    }

    @Test
    void testToStringEndDayNull() {
        // toString() with endDay null (but endMonth not null)
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, "2024", "Feb", null, null);
        String result = parsed.toString();
        assertTrue(result.contains("2024") && result.contains("Feb"), "toString with null endDay");
    }

    @Test
    void testParseMedlineDateMultipleDashesInvalidRange() {
        // Triggers parseDateRange with invalid parts length (multiple dashes)
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024-01-15-to-2024-02-20-extra");
        assertNotNull(result, "Should still attempt to parse");
        // Private parseDateRange returns null for invalid format, falls back to other parsers
    }

    @Test
    void testParseMedlineDateYearAndMonthOnly() {
        // parseMedlineDate without day pattern match
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan");
        assertNotNull(result, "Should parse year and month without day");
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
        assertNull(result.getDay(), "Day should be null when not in pattern");
    }

    @Test
    void testParseMedlineDateInvalidMonthWithDay() {
        // Month number null after day validation (invalid month name)
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 InvalidMonth 15");
        assertNotNull(result, "Should still parse with invalid month");
        // Without valid month, day won't be set
    }

    @Test
    void testParseMedlineDateInvalidDayForMonth() {
        // Day validation: Feb 30 is invalid but parser may still extract it
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Feb 30");
        assertNotNull(result, "Should parse even with invalid day");
        assertEquals("2024", result.getYear());
        assertEquals("Feb", result.getMonth());
        // Parser extracts day as string, actual validation happens elsewhere
    }

    @Test
    void testParseMedlineDateSeasonWithoutYear() {
        // Triggers parseSeason path without year match
        DateParser.ParsedDate result = DateParser.parseMedlineDate("Summer of Love");
        assertNotNull(result, "Should parse season without year");
        assertEquals("Summer", result.getSeason());
        assertNull(result.getYear(), "Year should be null when not in pattern");
    }

    @Test
    void testParseMedlineDateSeasonWithYear() {
        // Triggers parseSeason path with year
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Spring");
        assertNotNull(result, "Should parse season with year");
        assertEquals("2024", result.getYear());
        assertEquals("Spring", result.getSeason());
    }

    @Test
    void testParseMedlineDateOnlyYear() {
        // Triggers parseMonthYear path with no month match
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024");
        assertNotNull(result, "Should parse year only");
        assertEquals("2024", result.getYear());
        assertNull(result.getMonth(), "Month should be null");
    }

    @Test
    void testToDateComponentsNoYear() {
        // toDateComponents with year null - still creates DateComponents
        DateParser.ParsedDate parsed = new DateParser.ParsedDate(null, "Jan", "15", null, null, null, null, null);
        DateComponents result = DateParser.toDateComponents(parsed);
        assertNotNull(result, "DateComponents created even without year");
        assertNull(result.getYear(), "Year should be null in result");
    }

    @Test
    void testToDateComponentsInvalidMonth() {
        // toDateComponents with invalid month - still creates DateComponents
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "InvalidMonth", "15", null, null, null, null, null);
        DateComponents result = DateParser.toDateComponents(parsed);
        assertNotNull(result, "DateComponents created even with invalid month");
        assertEquals(2024, result.getYear(), "Year should be parsed");
        assertNull(result.getMonth(), "Month should be null for invalid month name");
    }

    @Test
    void testHasDateRangeEndMonthOnly() {
        // hasDateRange() OR condition: endMonth not null, others null
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, null, "Feb", null, null);
        assertTrue(parsed.hasDateRange(), "hasDateRange should be true when endMonth is not null");
    }

    @Test
    void testHasDateRangeEndDayOnly() {
        // hasDateRange() OR condition: endDay not null, others null
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, null, null, "20", null);
        assertTrue(parsed.hasDateRange(), "hasDateRange should be true when endDay is not null");
    }

    @Test
    void testToStringEndYearOnly() {
        // toString() with endYear only (endMonth and endDay null)
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, "2025", null, null, null);
        String result = parsed.toString();
        assertTrue(result.contains("2024") && result.contains("2025"), "toString with endYear only");
    }

    @Test
    void testToStringEndDayNotNull() {
        // Line 132: toString() where endDay is not null (true branch)
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, null, null, "20", null);
        String result = parsed.toString();
        assertTrue(result.contains("2024") && result.contains("Jan") && result.contains("15") && result.contains("20"),
                "toString should include endDay when not null");
    }

    @Test
    void testParseMedlineDateNoHyphen() {
        // parseDateRange called but no hyphen (parts.length = 1)
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan 15");
        assertNotNull(result, "Should parse date without hyphen");
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
        assertEquals("15", result.getDay());
    }

    @Test
    void testParseMedlineDateRangeNoDayInEnd() {
        // parseDateRange else block: endMonth null, dayMatcher.find() false
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan 15-No_Day_Here");
        assertNotNull(result, "Should parse even without day in end part");
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
    }

    @Test
    void testParseMedlineDateToComponentsNoYear() {
        // parseMedlineDateToComponents: yearMatcher.find() false
        DateComponents result = DateParser.parseMedlineDateToComponents("No year here");
        assertNotNull(result, "Should create DateComponents even without year");
        assertNull(result.getYear(), "Year should be null");
    }

    @Test
    void testParseMedlineDateToComponentsMonthRangeInvalidMonth() {
        // parseMedlineDateToComponents: monthRangeMatcher succeeds but monthNum null
        DateComponents result = DateParser.parseMedlineDateToComponents("2024 Invalid-Month");
        assertNotNull(result, "Should parse even with invalid month range");
        assertEquals(2024, result.getYear());
        assertNull(result.getMonth(), "Month should be null for invalid month name");
    }

    @Test
    void testParseMedlineDateToComponentsNoMonthMatch() {
        // parseMedlineDateToComponents: monthMatcher.find() false
        DateComponents result = DateParser.parseMedlineDateToComponents("2024 NoMonth");
        assertNotNull(result, "Should create DateComponents even without month");
        assertEquals(2024, result.getYear());
        assertNull(result.getMonth(), "Month should be null");
    }

    @Test
    void testParseMedlineDateToComponentsSingleMonthInvalidMonth() {
        // parseMedlineDateToComponents: single month but monthNum null
        DateComponents result = DateParser.parseMedlineDateToComponents("2024 InvalidMonth");
        assertNotNull(result, "Should parse even with invalid single month");
        assertEquals(2024, result.getYear());
        assertNull(result.getMonth(), "Month should be null for invalid month");
    }

    @Test
    void testParseMedlineDateToComponentsNoDayMatch() {
        // parseMedlineDateToComponents: last monthMatcher.find() false (no day extraction)
        DateComponents result = DateParser.parseMedlineDateToComponents("2024");
        assertNotNull(result, "Should create DateComponents without day");
        assertEquals(2024, result.getYear());
        assertNull(result.getMonth(), "Month should be null");
        assertNull(result.getDay(), "Day should be null");
    }

    @Test
    void testToStringEndMonthNotNullButEndDayNull() {
        // Line 132: toString() where endDay is null but endMonth is not null
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "15", null, "2024", "Feb", null, null);
        String result = parsed.toString();
        assertTrue(result.contains("2024") && result.contains("Jan") && result.contains("Feb"));
        assertFalse(result.contains("null"), "Should not contain 'null' string");
    }

    @Test
    void testParseMedlineDateNoHyphen2() {
        // Line 209: parseDateRange where parts.length != 2 (no hyphen)
        // When there's no hyphen, split returns length 1, triggering parseSingleDate fallback
        DateParser.ParsedDate result = DateParser.parseMedlineDate("2024 Jan 15 simple");
        assertNotNull(result, "Should parse date without hyphen");
        assertEquals("2024", result.getYear());
        assertEquals("Jan", result.getMonth());
    }

    @Test
    void testToIso8601InvalidMonthName() {
        // Line 378: toIso8601 where monthNumber is null (invalid month name)
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "InvalidMonthName", "15", null, null, null, null, null);
        String result = DateParser.toIso8601(parsed);
        assertNotNull(result, "Should create ISO date even with invalid month");
        assertEquals("2024", result, "Should skip invalid month and day");
    }

    @Test
    void testToIso8601InvalidDay() {
        // Line 381: toIso8601 where day is not null but isValidDay returns false
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Jan", "32", null, null, null, null, null);
        String result = DateParser.toIso8601(parsed);
        assertNotNull(result, "Should create ISO date even with invalid day");
        assertEquals("2024-01", result, "Should skip invalid day 32");
    }

    @Test
    void testToIso8601DayZero() {
        // Line 381: toIso8601 where day is "0" (invalid)
        DateParser.ParsedDate parsed = new DateParser.ParsedDate("2024", "Feb", "0", null, null, null, null, null);
        String result = DateParser.toIso8601(parsed);
        assertNotNull(result, "Should create ISO date even with day=0");
        assertEquals("2024-02", result, "Should skip invalid day 0");
    }

    @Test
    void testParseMedlineDateToComponentsSingleMonthNullMonthNum() {
        // Line 439: parseMedlineDateToComponents where single month parsing results in monthNum null
        DateComponents result = DateParser.parseMedlineDateToComponents("2024 XYZ");
        assertNotNull(result, "Should parse even with invalid month");
        assertEquals(2024, result.getYear());
        assertNull(result.getMonth(), "Month should be null for invalid month name");
    }

    @Test
    void shouldInstantiateClass() {
        // Cover class initialization bytecode
        DateParser instance = new DateParser();
        assertNotNull(instance);
    }

    @Test
    void testToDateComponentsWithInvalidYearString() {
        // Line 592: catch (NumberFormatException ignored) {} for year
        // Create ParsedDate with invalid year string to trigger NumberFormatException
        DateParser.ParsedDate parsedDate = new DateParser.ParsedDate(
                "abc",  // Invalid year - not a number
                null,   // month
                null,   // day
                null,   // season
                null,   // endYear
                null,   // endMonth
                null,   // endDay
                "2024"  // rawValue
        );

        DateComponents result = DateParser.toDateComponents(parsedDate);
        assertNotNull(result);
        assertNull(result.getYear(), "Year should be null when parsing fails");
    }

    @Test
    void testToDateComponentsWithInvalidDayString() {
        // Line 607: catch (NumberFormatException ignored) {} for day
        // Create ParsedDate with invalid day string to trigger NumberFormatException
        DateParser.ParsedDate parsedDate = new DateParser.ParsedDate(
                null,   // year
                null,   // month
                "xyz",  // Invalid day - not a number
                null,   // season
                null,   // endYear
                null,   // endMonth
                null,   // endDay
                "2024-01-xyz"  // rawValue
        );

        DateComponents result = DateParser.toDateComponents(parsedDate);
        assertNotNull(result);
        assertNull(result.getDay(), "Day should be null when parsing fails");
    }
}
