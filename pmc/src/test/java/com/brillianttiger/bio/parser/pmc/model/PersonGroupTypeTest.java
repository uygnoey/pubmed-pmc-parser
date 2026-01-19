package com.brillianttiger.bio.parser.pmc.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PersonGroupType Enum 테스트
 *
 * KR: PersonGroupType enum의 모든 메서드와 값들을 테스트
 * EN: Tests all methods and values of PersonGroupType enum
 */
@DisplayName("PersonGroupType Enum 테스트")
class PersonGroupTypeTest {

    @Test
    @DisplayName("values() - 모든 enum 값 반환")
    void testValues() {
        PersonGroupType[] values = PersonGroupType.values();

        assertNotNull(values);
        assertEquals(14, values.length);

        // 주요 값들 확인
        assertTrue(containsValue(values, PersonGroupType.AUTHOR));
        assertTrue(containsValue(values, PersonGroupType.EDITOR));
        assertTrue(containsValue(values, PersonGroupType.TRANSLATOR));
        assertTrue(containsValue(values, PersonGroupType.CURATOR));
        assertTrue(containsValue(values, PersonGroupType.OTHER));
    }

    @Test
    @DisplayName("valueOf() - 문자열로 enum 찾기 성공")
    void testValueOf() {
        assertEquals(PersonGroupType.AUTHOR, PersonGroupType.valueOf("AUTHOR"));
        assertEquals(PersonGroupType.EDITOR, PersonGroupType.valueOf("EDITOR"));
        assertEquals(PersonGroupType.TRANSLATOR, PersonGroupType.valueOf("TRANSLATOR"));
        assertEquals(PersonGroupType.CURATOR, PersonGroupType.valueOf("CURATOR"));
        assertEquals(PersonGroupType.OTHER, PersonGroupType.valueOf("OTHER"));
    }

    @Test
    @DisplayName("valueOf() - 존재하지 않는 이름으로 조회 시 예외 발생")
    void testValueOfInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            PersonGroupType.valueOf("INVALID_NAME");
        });
    }

    @Test
    @DisplayName("getValue() - 모든 enum 값의 value 필드 확인")
    void testGetValue() {
        assertEquals("author", PersonGroupType.AUTHOR.getValue());
        assertEquals("editor", PersonGroupType.EDITOR.getValue());
        assertEquals("guest-editor", PersonGroupType.GUEST_EDITOR.getValue());
        assertEquals("translator", PersonGroupType.TRANSLATOR.getValue());
        assertEquals("transed", PersonGroupType.TRANSED.getValue());
        assertEquals("compiler", PersonGroupType.COMPILER.getValue());
        assertEquals("inventor", PersonGroupType.INVENTOR.getValue());
        assertEquals("assignee", PersonGroupType.ASSIGNEE.getValue());
        assertEquals("director", PersonGroupType.DIRECTOR.getValue());
        assertEquals("curator", PersonGroupType.CURATOR.getValue());
        assertEquals("allauthors", PersonGroupType.ALLAUTHORS.getValue());
        assertEquals("contributor", PersonGroupType.CONTRIBUTOR.getValue());
        assertEquals("researcher", PersonGroupType.RESEARCHER.getValue());
        assertEquals("other", PersonGroupType.OTHER.getValue());
    }

    @Test
    @DisplayName("fromValue() - 유효한 값으로 enum 찾기")
    void testFromValueValid() {
        assertEquals(PersonGroupType.AUTHOR, PersonGroupType.fromValue("author"));
        assertEquals(PersonGroupType.EDITOR, PersonGroupType.fromValue("editor"));
        assertEquals(PersonGroupType.TRANSLATOR, PersonGroupType.fromValue("translator"));
        assertEquals(PersonGroupType.CURATOR, PersonGroupType.fromValue("curator"));
        assertEquals(PersonGroupType.COMPILER, PersonGroupType.fromValue("compiler"));
    }

    @Test
    @DisplayName("fromValue() - 대소문자 무시하고 enum 찾기")
    void testFromValueCaseInsensitive() {
        assertEquals(PersonGroupType.AUTHOR, PersonGroupType.fromValue("AUTHOR"));
        assertEquals(PersonGroupType.EDITOR, PersonGroupType.fromValue("EDITOR"));
        assertEquals(PersonGroupType.TRANSLATOR, PersonGroupType.fromValue("Translator"));
        assertEquals(PersonGroupType.CURATOR, PersonGroupType.fromValue("CuRaToR"));
    }

    @Test
    @DisplayName("fromValue() - 공백이 포함된 값 처리")
    void testFromValueWithWhitespace() {
        assertEquals(PersonGroupType.AUTHOR, PersonGroupType.fromValue("  author  "));
        assertEquals(PersonGroupType.EDITOR, PersonGroupType.fromValue(" editor "));
        assertEquals(PersonGroupType.GUEST_EDITOR, PersonGroupType.fromValue("  guest-editor  "));
    }

    @Test
    @DisplayName("fromValue() - null 입력 시 OTHER 반환")
    void testFromValueNull() {
        assertEquals(PersonGroupType.OTHER, PersonGroupType.fromValue(null));
    }

    @Test
    @DisplayName("fromValue() - 빈 문자열 입력 시 OTHER 반환")
    void testFromValueEmpty() {
        assertEquals(PersonGroupType.OTHER, PersonGroupType.fromValue(""));
        assertEquals(PersonGroupType.OTHER, PersonGroupType.fromValue("   "));
    }

    @Test
    @DisplayName("fromValue() - 알 수 없는 값 입력 시 OTHER 반환")
    void testFromValueUnknown() {
        assertEquals(PersonGroupType.OTHER, PersonGroupType.fromValue("unknown"));
        assertEquals(PersonGroupType.OTHER, PersonGroupType.fromValue("invalid"));
        assertEquals(PersonGroupType.OTHER, PersonGroupType.fromValue("xyz"));
    }

    @Test
    @DisplayName("toString() - enum의 문자열 표현 확인")
    void testToString() {
        assertEquals("author", PersonGroupType.AUTHOR.toString());
        assertEquals("editor", PersonGroupType.EDITOR.toString());
        assertEquals("translator", PersonGroupType.TRANSLATOR.toString());
        assertEquals("curator", PersonGroupType.CURATOR.toString());
        assertEquals("other", PersonGroupType.OTHER.toString());
    }

    @Test
    @DisplayName("모든 PersonGroupType 값들의 일관성 검증")
    void testAllValuesConsistency() {
        for (PersonGroupType type : PersonGroupType.values()) {
            // fromValue로 찾은 값이 원래 값과 동일한지 확인
            assertEquals(type, PersonGroupType.fromValue(type.getValue()));

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
    private boolean containsValue(PersonGroupType[] array, PersonGroupType value) {
        for (PersonGroupType item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }
}
