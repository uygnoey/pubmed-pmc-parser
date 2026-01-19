package com.brillianttiger.bio.parser.pmc.model;

import lombok.Getter;

/**
 * NameStyle / 이름 스타일 Enum
 *
 * KR: 이름의 표기 방식을 나타내는 열거형.
 *     서양식(성 뒤), 동양식(성 앞), 아이슬란드식, 이름만 등의 스타일 지원.
 * EN: Enumeration representing name formatting styles.
 *     Supports Western (surname last), Eastern (surname first), Icelandic, given-name-only styles.
 *
 * DTD: <!ATTLIST name
 *          name-style (western | eastern | islensk | given-only) "western"
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/name-style.html
 *
 * Default: western
 */
@Getter
public enum NameStyle {
    /**
     * Western name style (surname last)
     *
     * KR: 서양식 이름 스타일 (성이 뒤에)
     * EN: Western name style (surname last)
     *
     * Example: John Smith → given-names="John", surname="Smith"
     * Format: [prefix] given-names surname [suffix]
     */
    WESTERN("western"),

    /**
     * Eastern name style (surname first)
     *
     * KR: 동양식 이름 스타일 (성이 앞에)
     * EN: Eastern name style (surname first)
     *
     * Example: 김철수 → surname="김", given-names="철수"
     * Format: surname given-names
     */
    EASTERN("eastern"),

    /**
     * Icelandic name style
     *
     * KR: 아이슬란드식 이름 스타일
     * EN: Icelandic name style
     *
     * Note: Uses patronymic/matronymic naming system
     * Example: Jón Einarsson (son of Einar)
     */
    ISLENSK("islensk"),

    /**
     * Given name only style
     *
     * KR: 이름만 사용하는 스타일 (성 없음)
     * EN: Given name only style (no surname)
     *
     * Example: Madonna, Prince
     * Format: given-names only
     */
    GIVEN_ONLY("given-only");

    private final String value;

    NameStyle(String value) {
        this.value = value;
    }

    /**
     * 문자열 값으로부터 NameStyle enum을 반환 / Return NameStyle enum from string value
     *
     * KR: 주어진 문자열 값에 해당하는 NameStyle을 반환.
     *     매칭되는 값이 없거나 null이면 기본값 WESTERN을 반환.
     * EN: Returns the NameStyle corresponding to the given string value.
     *     Returns default value WESTERN if no matching value is found or value is null.
     *
     * @param value 이름 스타일 문자열 / Name style string
     * @return NameStyle enum 값 (기본값: WESTERN) / NameStyle enum value (default: WESTERN)
     */
    public static NameStyle fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return WESTERN; // Default value
        }

        String normalized = value.trim().toLowerCase();
        for (NameStyle style : values()) {
            if (style.value.equals(normalized)) {
                return style;
            }
        }

        return WESTERN; // Default value
    }
}
