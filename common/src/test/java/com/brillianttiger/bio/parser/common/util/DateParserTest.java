package com.brillianttiger.bio.parser.common.util;

import com.brillianttiger.bio.parser.common.model.DateComponents;
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
}
