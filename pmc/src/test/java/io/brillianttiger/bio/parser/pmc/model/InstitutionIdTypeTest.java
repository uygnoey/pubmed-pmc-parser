package io.brillianttiger.bio.parser.pmc.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * InstitutionIdType Enum 테스트
 *
 * KR: InstitutionIdType enum의 모든 메서드와 값들을 테스트
 * EN: Tests all methods and values of InstitutionIdType enum
 */
@DisplayName("InstitutionIdType Enum 테스트")
class InstitutionIdTypeTest {

    @Test
    @DisplayName("values() - 모든 enum 값 반환")
    void testValues() {
        InstitutionIdType[] values = InstitutionIdType.values();

        assertNotNull(values);
        assertEquals(5, values.length);

        // 모든 값들 확인
        assertTrue(containsValue(values, InstitutionIdType.ROR));
        assertTrue(containsValue(values, InstitutionIdType.ISNI));
        assertTrue(containsValue(values, InstitutionIdType.RINGGOLD));
        assertTrue(containsValue(values, InstitutionIdType.GRID));
        assertTrue(containsValue(values, InstitutionIdType.OTHER));
    }

    @Test
    @DisplayName("valueOf() - 문자열로 enum 찾기 성공")
    void testValueOf() {
        assertEquals(InstitutionIdType.ROR, InstitutionIdType.valueOf("ROR"));
        assertEquals(InstitutionIdType.ISNI, InstitutionIdType.valueOf("ISNI"));
        assertEquals(InstitutionIdType.RINGGOLD, InstitutionIdType.valueOf("RINGGOLD"));
        assertEquals(InstitutionIdType.GRID, InstitutionIdType.valueOf("GRID"));
        assertEquals(InstitutionIdType.OTHER, InstitutionIdType.valueOf("OTHER"));
    }

    @Test
    @DisplayName("valueOf() - 존재하지 않는 이름으로 조회 시 예외 발생")
    void testValueOfInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            InstitutionIdType.valueOf("INVALID_NAME");
        });
    }

    @Test
    @DisplayName("getValue() - 모든 enum 값의 value 필드 확인")
    void testGetValue() {
        assertEquals("ror", InstitutionIdType.ROR.getValue());
        assertEquals("isni", InstitutionIdType.ISNI.getValue());
        assertEquals("ringgold", InstitutionIdType.RINGGOLD.getValue());
        assertEquals("grid", InstitutionIdType.GRID.getValue());
        assertEquals("other", InstitutionIdType.OTHER.getValue());
    }

    @Test
    @DisplayName("fromValue() - 유효한 값으로 enum 찾기")
    void testFromValueValid() {
        assertEquals(InstitutionIdType.ROR, InstitutionIdType.fromValue("ror"));
        assertEquals(InstitutionIdType.ISNI, InstitutionIdType.fromValue("isni"));
        assertEquals(InstitutionIdType.RINGGOLD, InstitutionIdType.fromValue("ringgold"));
        assertEquals(InstitutionIdType.GRID, InstitutionIdType.fromValue("grid"));
        assertEquals(InstitutionIdType.OTHER, InstitutionIdType.fromValue("other"));
    }

    @Test
    @DisplayName("fromValue() - 대소문자 무시하고 enum 찾기")
    void testFromValueCaseInsensitive() {
        assertEquals(InstitutionIdType.ROR, InstitutionIdType.fromValue("ROR"));
        assertEquals(InstitutionIdType.ISNI, InstitutionIdType.fromValue("ISNI"));
        assertEquals(InstitutionIdType.RINGGOLD, InstitutionIdType.fromValue("Ringgold"));
        assertEquals(InstitutionIdType.GRID, InstitutionIdType.fromValue("GrId"));
    }

    @Test
    @DisplayName("fromValue() - 공백이 포함된 값 처리")
    void testFromValueWithWhitespace() {
        assertEquals(InstitutionIdType.ROR, InstitutionIdType.fromValue("  ror  "));
        assertEquals(InstitutionIdType.ISNI, InstitutionIdType.fromValue(" isni "));
        assertEquals(InstitutionIdType.RINGGOLD, InstitutionIdType.fromValue("  ringgold  "));
    }

    @Test
    @DisplayName("fromValue() - null 입력 시 OTHER 반환")
    void testFromValueNull() {
        assertEquals(InstitutionIdType.OTHER, InstitutionIdType.fromValue(null));
    }

    @Test
    @DisplayName("fromValue() - 빈 문자열 입력 시 OTHER 반환")
    void testFromValueEmpty() {
        assertEquals(InstitutionIdType.OTHER, InstitutionIdType.fromValue(""));
        assertEquals(InstitutionIdType.OTHER, InstitutionIdType.fromValue("   "));
    }

    @Test
    @DisplayName("fromValue() - 알 수 없는 값 입력 시 OTHER 반환")
    void testFromValueUnknown() {
        assertEquals(InstitutionIdType.OTHER, InstitutionIdType.fromValue("unknown"));
        assertEquals(InstitutionIdType.OTHER, InstitutionIdType.fromValue("invalid"));
        assertEquals(InstitutionIdType.OTHER, InstitutionIdType.fromValue("xyz"));
    }

    @Test
    @DisplayName("모든 InstitutionIdType 값들의 일관성 검증")
    void testAllValuesConsistency() {
        for (InstitutionIdType type : InstitutionIdType.values()) {
            // fromValue로 찾은 값이 원래 값과 동일한지 확인
            assertEquals(type, InstitutionIdType.fromValue(type.getValue()));

            // getValue()가 null이 아닌지 확인
            assertNotNull(type.getValue());
            assertFalse(type.getValue().trim().isEmpty());
        }
    }

    /**
     * Helper method to check if an array contains a specific value
     */
    private boolean containsValue(InstitutionIdType[] array, InstitutionIdType value) {
        for (InstitutionIdType item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }
}
