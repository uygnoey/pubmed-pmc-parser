package io.brillianttiger.bio.parser.pmc.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PublicationType Enum 테스트
 *
 * KR: PublicationType enum의 모든 메서드와 값들을 테스트
 * EN: Tests all methods and values of PublicationType enum
 */
@DisplayName("PublicationType Enum 테스트")
class PublicationTypeTest {

    @Test
    @DisplayName("values() - 모든 enum 값 반환")
    void testValues() {
        PublicationType[] values = PublicationType.values();

        assertNotNull(values);
        assertEquals(20, values.length);

        // 주요 값들 확인
        assertTrue(containsValue(values, PublicationType.JOURNAL));
        assertTrue(containsValue(values, PublicationType.BOOK));
        assertTrue(containsValue(values, PublicationType.THESIS));
        assertTrue(containsValue(values, PublicationType.PATENT));
        assertTrue(containsValue(values, PublicationType.OTHER));
    }

    @Test
    @DisplayName("valueOf() - 문자열로 enum 찾기 성공")
    void testValueOf() {
        assertEquals(PublicationType.JOURNAL, PublicationType.valueOf("JOURNAL"));
        assertEquals(PublicationType.BOOK, PublicationType.valueOf("BOOK"));
        assertEquals(PublicationType.THESIS, PublicationType.valueOf("THESIS"));
        assertEquals(PublicationType.PATENT, PublicationType.valueOf("PATENT"));
        assertEquals(PublicationType.OTHER, PublicationType.valueOf("OTHER"));
    }

    @Test
    @DisplayName("valueOf() - 존재하지 않는 이름으로 조회 시 예외 발생")
    void testValueOfInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            PublicationType.valueOf("INVALID_NAME");
        });
    }

    @Test
    @DisplayName("getValue() - 모든 enum 값의 value 필드 확인")
    void testGetValue() {
        assertEquals("journal", PublicationType.JOURNAL.getValue());
        assertEquals("book", PublicationType.BOOK.getValue());
        assertEquals("confproc", PublicationType.CONFPROC.getValue());
        assertEquals("thesis", PublicationType.THESIS.getValue());
        assertEquals("patent", PublicationType.PATENT.getValue());
        assertEquals("software", PublicationType.SOFTWARE.getValue());
        assertEquals("data", PublicationType.DATA.getValue());
        assertEquals("database", PublicationType.DATABASE.getValue());
        assertEquals("preprint", PublicationType.PREPRINT.getValue());
        assertEquals("webpage", PublicationType.WEBPAGE.getValue());
        assertEquals("report", PublicationType.REPORT.getValue());
        assertEquals("gov", PublicationType.GOV.getValue());
        assertEquals("standard", PublicationType.STANDARD.getValue());
        assertEquals("working-paper", PublicationType.WORKING_PAPER.getValue());
        assertEquals("letter", PublicationType.LETTER.getValue());
        assertEquals("letter-to-editor", PublicationType.LETTER_TO_EDITOR.getValue());
        assertEquals("news", PublicationType.NEWS.getValue());
        assertEquals("commun", PublicationType.COMMUN.getValue());
        assertEquals("review", PublicationType.REVIEW.getValue());
        assertEquals("other", PublicationType.OTHER.getValue());
    }

    @Test
    @DisplayName("fromValue() - 유효한 값으로 enum 찾기")
    void testFromValueValid() {
        assertEquals(PublicationType.JOURNAL, PublicationType.fromValue("journal"));
        assertEquals(PublicationType.BOOK, PublicationType.fromValue("book"));
        assertEquals(PublicationType.THESIS, PublicationType.fromValue("thesis"));
        assertEquals(PublicationType.PATENT, PublicationType.fromValue("patent"));
        assertEquals(PublicationType.CONFPROC, PublicationType.fromValue("confproc"));
        assertEquals(PublicationType.SOFTWARE, PublicationType.fromValue("software"));
        assertEquals(PublicationType.DATA, PublicationType.fromValue("data"));
    }

    @Test
    @DisplayName("fromValue() - 대소문자 무시하고 enum 찾기")
    void testFromValueCaseInsensitive() {
        assertEquals(PublicationType.JOURNAL, PublicationType.fromValue("JOURNAL"));
        assertEquals(PublicationType.BOOK, PublicationType.fromValue("BOOK"));
        assertEquals(PublicationType.THESIS, PublicationType.fromValue("Thesis"));
        assertEquals(PublicationType.PATENT, PublicationType.fromValue("PaTeNt"));
    }

    @Test
    @DisplayName("fromValue() - 공백이 포함된 값 처리")
    void testFromValueWithWhitespace() {
        assertEquals(PublicationType.JOURNAL, PublicationType.fromValue("  journal  "));
        assertEquals(PublicationType.BOOK, PublicationType.fromValue(" book "));
        assertEquals(PublicationType.WORKING_PAPER, PublicationType.fromValue("  working-paper  "));
    }

    @Test
    @DisplayName("fromValue() - null 입력 시 OTHER 반환")
    void testFromValueNull() {
        assertEquals(PublicationType.OTHER, PublicationType.fromValue(null));
    }

    @Test
    @DisplayName("fromValue() - 빈 문자열 입력 시 OTHER 반환")
    void testFromValueEmpty() {
        assertEquals(PublicationType.OTHER, PublicationType.fromValue(""));
        assertEquals(PublicationType.OTHER, PublicationType.fromValue("   "));
    }

    @Test
    @DisplayName("fromValue() - 알 수 없는 값 입력 시 OTHER 반환")
    void testFromValueUnknown() {
        assertEquals(PublicationType.OTHER, PublicationType.fromValue("unknown"));
        assertEquals(PublicationType.OTHER, PublicationType.fromValue("invalid"));
        assertEquals(PublicationType.OTHER, PublicationType.fromValue("xyz"));
    }

    @Test
    @DisplayName("toString() - enum의 문자열 표현 확인")
    void testToString() {
        assertEquals("journal", PublicationType.JOURNAL.toString());
        assertEquals("book", PublicationType.BOOK.toString());
        assertEquals("thesis", PublicationType.THESIS.toString());
        assertEquals("patent", PublicationType.PATENT.toString());
        assertEquals("other", PublicationType.OTHER.toString());
    }

    @Test
    @DisplayName("모든 PublicationType 값들의 일관성 검증")
    void testAllValuesConsistency() {
        for (PublicationType type : PublicationType.values()) {
            // fromValue로 찾은 값이 원래 값과 동일한지 확인
            assertEquals(type, PublicationType.fromValue(type.getValue()));

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
    private boolean containsValue(PublicationType[] array, PublicationType value) {
        for (PublicationType item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }
}
