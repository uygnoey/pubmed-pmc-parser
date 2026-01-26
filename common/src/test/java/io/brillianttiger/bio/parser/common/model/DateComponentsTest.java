package io.brillianttiger.bio.parser.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * DateComponentsTest / DateComponents 모델 테스트
 *
 * KR: 날짜 파싱 및 변환 기능 테스트.
 * EN: Tests for date parsing and conversion features.
 */
@DisplayName("DateComponents 모델 테스트")
class DateComponentsTest {

    // ==================== STANDARD DATE TESTS ====================

    @Test
    @DisplayName("표준 날짜: 년-월-일 파싱")
    void shouldParseStandardDate() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(3)
                .day(15)
                .build();

        // Then
        assertThat(date.getYear()).isEqualTo(2024);
        assertThat(date.getMonth()).isEqualTo(3);
        assertThat(date.getDay()).isEqualTo(15);
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(2024, 3, 15));
    }

    @Test
    @DisplayName("표준 날짜: 년-월만 있는 경우")
    void shouldParseYearMonth() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(6)
                .build();

        // Then
        assertThat(date.getYear()).isEqualTo(2024);
        assertThat(date.getMonth()).isEqualTo(6);
        assertThat(date.getDay()).isNull();
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(2024, 6, 1));  // day defaults to 1
    }

    @Test
    @DisplayName("표준 날짜: 년만 있는 경우")
    void shouldParseYearOnly() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .year(2024)
                .build();

        // Then
        assertThat(date.getYear()).isEqualTo(2024);
        assertThat(date.getMonth()).isNull();
        assertThat(date.getDay()).isNull();
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(2024, 1, 1));  // month and day default to 1
    }

    // ==================== MEDLINE DATE TESTS ====================

    @Test
    @DisplayName("MedlineDate: '2024 Jan-Feb' 파싱")
    void shouldParseMedlineDateMonthRange() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .medlineDate("2024 Jan-Feb")
                .build();

        // Then
        assertThat(date.getMedlineDate()).isEqualTo("2024 Jan-Feb");
        assertThat(date.toDisplayString()).isEqualTo("2024 Jan-Feb");
    }

    @Test
    @DisplayName("MedlineDate: '2024 Spring' 파싱")
    void shouldParseMedlineDateSeason() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .medlineDate("2024 Spring")
                .build();

        // Then
        assertThat(date.getMedlineDate()).isEqualTo("2024 Spring");
        assertThat(date.toDisplayString()).isEqualTo("2024 Spring");
    }

    @Test
    @DisplayName("MedlineDate: '2024 Winter-Spring' 파싱")
    void shouldParseMedlineDateSeasonRange() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .medlineDate("2024 Winter-Spring")
                .build();

        // Then
        assertThat(date.getMedlineDate()).isEqualTo("2024 Winter-Spring");
        assertThat(date.toDisplayString()).isEqualTo("2024 Winter-Spring");
    }

    @Test
    @DisplayName("MedlineDate: '2024 Q1' 파싱 (분기)")
    void shouldParseMedlineDateQuarter() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .medlineDate("2024 Q1")
                .build();

        // Then
        assertThat(date.getMedlineDate()).isEqualTo("2024 Q1");
        assertThat(date.toDisplayString()).isEqualTo("2024 Q1");
    }

    @Test
    @DisplayName("MedlineDate: '2023-2024' 파싱 (년 범위)")
    void shouldParseMedlineDateYearRange() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .medlineDate("2023-2024")
                .build();

        // Then
        assertThat(date.getMedlineDate()).isEqualTo("2023-2024");
        assertThat(date.toDisplayString()).isEqualTo("2023-2024");
    }

    // ==================== SEASON TESTS ====================

    @Test
    @DisplayName("Season: Spring 파싱")
    void shouldParseSpring() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .year(2024)
                .season(DateComponents.Season.SPRING)
                .build();

        // Then
        assertThat(date.getSeason()).isEqualTo(DateComponents.Season.SPRING);
        assertThat(date.getSeason().getValue()).isEqualTo("Spring");
        assertThat(date.toDisplayString()).isEqualTo("2024 Spring");
    }

    @Test
    @DisplayName("Season: fromValue() - 대소문자 무시")
    void shouldParseSeasonCaseInsensitive() {
        // When & Then
        assertThat(DateComponents.Season.fromValue("Spring")).isEqualTo(DateComponents.Season.SPRING);
        assertThat(DateComponents.Season.fromValue("spring")).isEqualTo(DateComponents.Season.SPRING);
        assertThat(DateComponents.Season.fromValue("SPRING")).isEqualTo(DateComponents.Season.SPRING);
        assertThat(DateComponents.Season.fromValue("summer")).isEqualTo(DateComponents.Season.SUMMER);
        assertThat(DateComponents.Season.fromValue("Fall")).isEqualTo(DateComponents.Season.FALL);
        assertThat(DateComponents.Season.fromValue("WINTER")).isEqualTo(DateComponents.Season.WINTER);
    }

    @Test
    @DisplayName("Season: fromValue() - null 처리")
    void shouldHandleNullSeason() {
        // When & Then
        assertThat(DateComponents.Season.fromValue(null)).isNull();
    }

    @Test
    @DisplayName("Season: fromValue() - 알 수 없는 값")
    void shouldHandleUnknownSeason() {
        // When & Then
        assertThat(DateComponents.Season.fromValue("Unknown")).isNull();
        assertThat(DateComponents.Season.fromValue("")).isNull();
    }

    @Test
    @DisplayName("Season: 모든 계절 값 확인")
    void shouldHaveAllSeasons() {
        // Then
        assertThat(DateComponents.Season.SPRING.getValue()).isEqualTo("Spring");
        assertThat(DateComponents.Season.SUMMER.getValue()).isEqualTo("Summer");
        assertThat(DateComponents.Season.FALL.getValue()).isEqualTo("Fall");
        assertThat(DateComponents.Season.WINTER.getValue()).isEqualTo("Winter");
    }

    // ==================== ISO8601 DATE TESTS ====================

    @Test
    @DisplayName("ISO8601: '2024-03-15' 형식")
    void shouldParseIso8601Date() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(3)
                .day(15)
                .iso8601Date("2024-03-15")
                .build();

        // Then
        assertThat(date.getIso8601Date()).isEqualTo("2024-03-15");
        assertThat(date.toLocalDate()).isEqualTo(LocalDate.of(2024, 3, 15));
    }

    @Test
    @DisplayName("ISO8601: '2024-03' 형식 (년-월)")
    void shouldParseIso8601YearMonth() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(3)
                .iso8601Date("2024-03")
                .build();

        // Then
        assertThat(date.getIso8601Date()).isEqualTo("2024-03");
    }

    @Test
    @DisplayName("ISO8601: '2024' 형식 (년)")
    void shouldParseIso8601Year() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .year(2024)
                .iso8601Date("2024")
                .build();

        // Then
        assertThat(date.getIso8601Date()).isEqualTo("2024");
    }

    // ==================== JATS-SPECIFIC ATTRIBUTES ====================

    @Test
    @DisplayName("JATS 속성: pub-type, date-type")
    void shouldParseJatsAttributes() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(1)
                .day(20)
                .dateType("received")
                .pubType("epub")
                .publicationFormat("electronic")
                .build();

        // Then
        assertThat(date.getDateType()).isEqualTo("received");
        assertThat(date.getPubType()).isEqualTo("epub");
        assertThat(date.getPublicationFormat()).isEqualTo("electronic");
    }

    @Test
    @DisplayName("JATS 속성: string-date, era, calendar")
    void shouldParseJatsExtendedAttributes() {
        // Given & When
        DateComponents date = DateComponents.builder()
                .stringDate("Spring 2024")
                .era("CE")
                .calendar("Gregorian")
                .build();

        // Then
        assertThat(date.getStringDate()).isEqualTo("Spring 2024");
        assertThat(date.getEra()).isEqualTo("CE");
        assertThat(date.getCalendar()).isEqualTo("Gregorian");
    }

    // ==================== CONVERSION TESTS ====================

    @Test
    @DisplayName("toLocalDate(): 년-월-일 변환")
    void shouldConvertToLocalDate() {
        // Given
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(6)
                .day(15)
                .build();

        // When
        LocalDate localDate = date.toLocalDate();

        // Then
        assertThat(localDate).isEqualTo(LocalDate.of(2024, 6, 15));
    }

    @Test
    @DisplayName("toLocalDate(): 년만 있을 때 기본값 적용")
    void shouldConvertToLocalDateWithDefaults() {
        // Given
        DateComponents date = DateComponents.builder()
                .year(2024)
                .build();

        // When
        LocalDate localDate = date.toLocalDate();

        // Then
        assertThat(localDate).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    @DisplayName("toLocalDate(): 년이 없으면 null 반환")
    void shouldReturnNullWhenNoYear() {
        // Given
        DateComponents date = DateComponents.builder()
                .month(6)
                .day(15)
                .build();

        // When
        LocalDate localDate = date.toLocalDate();

        // Then
        assertThat(localDate).isNull();
    }

    @Test
    @DisplayName("toDisplayString(): MedlineDate 우선")
    void shouldDisplayMedlineDateFirst() {
        // Given
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(3)
                .medlineDate("2024 Jan-Feb")
                .build();

        // When
        String display = date.toDisplayString();

        // Then
        assertThat(display).isEqualTo("2024 Jan-Feb");
    }

    @Test
    @DisplayName("toDisplayString(): string-date 두 번째 우선")
    void shouldDisplayStringDateSecond() {
        // Given
        DateComponents date = DateComponents.builder()
                .year(2024)
                .stringDate("Spring 2024")
                .build();

        // When
        String display = date.toDisplayString();

        // Then
        assertThat(display).isEqualTo("Spring 2024");
    }

    @Test
    @DisplayName("toDisplayString(): 표준 날짜 형식 (년-월-일)")
    void shouldDisplayStandardFormat() {
        // Given
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(3)
                .day(15)
                .build();

        // When
        String display = date.toDisplayString();

        // Then
        assertThat(display).isEqualTo("2024-03-15");
    }

    @Test
    @DisplayName("toDisplayString(): 계절 포함")
    void shouldDisplayWithSeason() {
        // Given
        DateComponents date = DateComponents.builder()
                .year(2024)
                .season(DateComponents.Season.SPRING)
                .build();

        // When
        String display = date.toDisplayString();

        // Then
        assertThat(display).isEqualTo("2024 Spring");
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("빈 DateComponents 객체")
    void shouldHandleEmptyDateComponents() {
        // When
        DateComponents date = DateComponents.builder().build();

        // Then
        assertThat(date.getYear()).isNull();
        assertThat(date.getMonth()).isNull();
        assertThat(date.getDay()).isNull();
        assertThat(date.toLocalDate()).isNull();
        assertThat(date.toDisplayString()).isEmpty();
    }

    @Test
    @DisplayName("month/day가 null일 때 기본값 1 적용")
    void shouldApplyDefaultsForNullMonthDay() {
        // Given
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(null)
                .day(null)
                .build();

        // When
        LocalDate localDate = date.toLocalDate();

        // Then
        assertThat(localDate).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    @DisplayName("유효하지 않은 날짜는 LocalDate 변환 시 예외 발생")
    void shouldThrowExceptionForInvalidDate() {
        // Given
        DateComponents date = DateComponents.builder()
                .year(2024)
                .month(2)
                .day(30)  // February 30th doesn't exist
                .build();

        // When & Then
        assertThatThrownBy(() -> date.toLocalDate())
                .isInstanceOf(java.time.DateTimeException.class);
    }
}
