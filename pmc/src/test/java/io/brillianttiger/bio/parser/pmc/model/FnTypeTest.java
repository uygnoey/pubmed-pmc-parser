package io.brillianttiger.bio.parser.pmc.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FnType Enum 테스트
 *
 * KR: FnType enum의 모든 메서드와 값들을 테스트
 * EN: Tests all methods and values of FnType enum
 *
 * IMPORTANT: FnType.fromValue()는 null/empty 입력에 대해 null을 반환 (다른 enum들과 다름)
 */
@DisplayName("FnType Enum 테스트")
class FnTypeTest {

    @Test
    @DisplayName("values() - 모든 enum 값 반환")
    void testValues() {
        FnType[] values = FnType.values();

        assertNotNull(values);
        assertEquals(19, values.length);

        // 주요 값들 확인
        assertTrue(containsValue(values, FnType.ABBR));
        assertTrue(containsValue(values, FnType.AUTHOR));
        assertTrue(containsValue(values, FnType.CONFLICT));
        assertTrue(containsValue(values, FnType.CORRESP));
        assertTrue(containsValue(values, FnType.OTHER));
    }

    @Test
    @DisplayName("valueOf() - 문자열로 enum 찾기 성공")
    void testValueOf() {
        assertEquals(FnType.ABBR, FnType.valueOf("ABBR"));
        assertEquals(FnType.AUTHOR, FnType.valueOf("AUTHOR"));
        assertEquals(FnType.CONFLICT, FnType.valueOf("CONFLICT"));
        assertEquals(FnType.CORRESP, FnType.valueOf("CORRESP"));
        assertEquals(FnType.OTHER, FnType.valueOf("OTHER"));
    }

    @Test
    @DisplayName("valueOf() - 존재하지 않는 이름으로 조회 시 예외 발생")
    void testValueOfInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            FnType.valueOf("INVALID_NAME");
        });
    }

    @Test
    @DisplayName("getValue() - 모든 enum 값의 value 필드 확인")
    void testGetValue() {
        assertEquals("abbr", FnType.ABBR.getValue());
        assertEquals("author", FnType.AUTHOR.getValue());
        assertEquals("con", FnType.CON.getValue());
        assertEquals("conflict", FnType.CONFLICT.getValue());
        assertEquals("corresp", FnType.CORRESP.getValue());
        assertEquals("current-aff", FnType.CURRENT_AFF.getValue());
        assertEquals("deceased", FnType.DECEASED.getValue());
        assertEquals("edited-by", FnType.EDITED_BY.getValue());
        assertEquals("equal", FnType.EQUAL.getValue());
        assertEquals("financial-disclosure", FnType.FINANCIAL_DISCLOSURE.getValue());
        assertEquals("on-leave", FnType.ON_LEAVE.getValue());
        assertEquals("participating-researchers", FnType.PARTICIPATING_RESEARCHERS.getValue());
        assertEquals("present-address", FnType.PRESENT_ADDRESS.getValue());
        assertEquals("presented-at", FnType.PRESENTED_AT.getValue());
        assertEquals("previously-at", FnType.PREVIOUSLY_AT.getValue());
        assertEquals("study-group-members", FnType.STUDY_GROUP_MEMBERS.getValue());
        assertEquals("supplementary-material", FnType.SUPPLEMENTARY_MATERIAL.getValue());
        assertEquals("supported-by", FnType.SUPPORTED_BY.getValue());
        assertEquals("other", FnType.OTHER.getValue());
    }

    @Test
    @DisplayName("fromValue() - 유효한 값으로 enum 찾기")
    void testFromValueValid() {
        assertEquals(FnType.ABBR, FnType.fromValue("abbr"));
        assertEquals(FnType.AUTHOR, FnType.fromValue("author"));
        assertEquals(FnType.CONFLICT, FnType.fromValue("conflict"));
        assertEquals(FnType.CORRESP, FnType.fromValue("corresp"));
        assertEquals(FnType.CURRENT_AFF, FnType.fromValue("current-aff"));
    }

    @Test
    @DisplayName("fromValue() - 대소문자 무시하고 enum 찾기")
    void testFromValueCaseInsensitive() {
        assertEquals(FnType.ABBR, FnType.fromValue("ABBR"));
        assertEquals(FnType.AUTHOR, FnType.fromValue("AUTHOR"));
        assertEquals(FnType.CONFLICT, FnType.fromValue("Conflict"));
        assertEquals(FnType.CORRESP, FnType.fromValue("CoRrEsP"));
    }

    @Test
    @DisplayName("fromValue() - 공백이 포함된 값 처리")
    void testFromValueWithWhitespace() {
        assertEquals(FnType.ABBR, FnType.fromValue("  abbr  "));
        assertEquals(FnType.AUTHOR, FnType.fromValue(" author "));
        assertEquals(FnType.CURRENT_AFF, FnType.fromValue("  current-aff  "));
    }

    @Test
    @DisplayName("fromValue() - null 입력 시 null 반환 (다른 enum들과 다름!)")
    void testFromValueNull() {
        // IMPORTANT: FnType은 다른 enum들과 달리 null 입력 시 null을 반환
        assertNull(FnType.fromValue(null));
    }

    @Test
    @DisplayName("fromValue() - 빈 문자열 입력 시 null 반환 (다른 enum들과 다름!)")
    void testFromValueEmpty() {
        // IMPORTANT: FnType은 다른 enum들과 달리 빈 문자열 입력 시 null을 반환
        assertNull(FnType.fromValue(""));
        assertNull(FnType.fromValue("   "));
    }

    @Test
    @DisplayName("fromValue() - 알 수 없는 값 입력 시 OTHER 반환")
    void testFromValueUnknown() {
        assertEquals(FnType.OTHER, FnType.fromValue("unknown"));
        assertEquals(FnType.OTHER, FnType.fromValue("invalid"));
        assertEquals(FnType.OTHER, FnType.fromValue("xyz"));
    }

    @Test
    @DisplayName("toString() - enum의 문자열 표현 확인")
    void testToString() {
        assertEquals("abbr", FnType.ABBR.toString());
        assertEquals("author", FnType.AUTHOR.toString());
        assertEquals("conflict", FnType.CONFLICT.toString());
        assertEquals("corresp", FnType.CORRESP.toString());
        assertEquals("other", FnType.OTHER.toString());
    }

    @Test
    @DisplayName("모든 FnType 값들의 일관성 검증")
    void testAllValuesConsistency() {
        for (FnType type : FnType.values()) {
            // fromValue로 찾은 값이 원래 값과 동일한지 확인
            assertEquals(type, FnType.fromValue(type.getValue()));

            // toString이 getValue()와 동일한지 확인
            assertEquals(type.getValue(), type.toString());

            // getValue()가 null이 아닌지 확인
            assertNotNull(type.getValue());
            assertFalse(type.getValue().trim().isEmpty());
        }
    }

    /**
     * Helper method to check if an array contains a specific value
     */
    private boolean containsValue(FnType[] array, FnType value) {
        for (FnType item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }
}
