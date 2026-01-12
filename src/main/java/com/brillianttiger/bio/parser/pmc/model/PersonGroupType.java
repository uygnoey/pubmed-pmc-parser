package com.brillianttiger.bio.parser.pmc.model;

import lombok.Getter;

/**
 * PersonGroupType / 인물 그룹 유형
 *
 * KR: 인물 그룹의 역할/유형을 나타내는 열거형. JATS 1.4 DTD 완전 준수.
 *     저자, 편집자, 번역자 등 다양한 역할을 포함.
 * EN: Enumeration representing person group role/type. Fully compliant with JATS 1.4 DTD.
 *     Includes various roles such as author, editor, translator, etc.
 *
 * DTD: <!ATTLIST person-group
 *          person-group-type (allauthors | assignee | author | compiler |
 *                             curator | director | editor | guest-editor |
 *                             inventor | transed | translator) #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/person-group-type.html
 *
 * Example:
 * <person-group person-group-type="author">
 *   <name><surname>Smith</surname><given-names>John</given-names></name>
 * </person-group>
 */
@Getter
public enum PersonGroupType {

    /**
     * 저자
     *
     * KR: 저자
     * EN: Author
     */
    AUTHOR("author"),

    /**
     * 편집자
     *
     * KR: 편집자
     * EN: Editor
     */
    EDITOR("editor"),

    /**
     * 객원 편집자
     *
     * KR: 객원 편집자
     * EN: Guest editor
     */
    GUEST_EDITOR("guest-editor"),

    /**
     * 번역자
     *
     * KR: 번역자
     * EN: Translator
     */
    TRANSLATOR("translator"),

    /**
     * 번역 편집자
     *
     * KR: 번역 편집자
     * EN: Translator editor
     */
    TRANSED("transed"),

    /**
     * 컴파일러
     *
     * KR: 편찬자/컴파일러
     * EN: Compiler
     */
    COMPILER("compiler"),

    /**
     * 발명자
     *
     * KR: 발명자 (특허용)
     * EN: Inventor (for patents)
     */
    INVENTOR("inventor"),

    /**
     * 양수인
     *
     * KR: 양수인 (특허용)
     * EN: Assignee (for patents)
     */
    ASSIGNEE("assignee"),

    /**
     * 디렉터
     *
     * KR: 디렉터
     * EN: Director
     */
    DIRECTOR("director"),

    /**
     * 큐레이터
     *
     * KR: 큐레이터
     * EN: Curator
     */
    CURATOR("curator"),

    /**
     * 전체 저자
     *
     * KR: 전체 저자 목록
     * EN: All authors
     */
    ALLAUTHORS("allauthors"),

    /**
     * 기여자
     *
     * KR: 기여자
     * EN: Contributor
     */
    CONTRIBUTOR("contributor"),

    /**
     * 연구자
     *
     * KR: 연구자
     * EN: Researcher
     */
    RESEARCHER("researcher"),

    /**
     * 기타
     *
     * KR: 기타 또는 알 수 없는 유형
     * EN: Other or unknown type
     */
    OTHER("other");

    private final String value;

    PersonGroupType(String value) {
        this.value = value;
    }

    /**
     * 문자열 값으로부터 PersonGroupType을 찾아 반환 / Find and return PersonGroupType from string value
     *
     * @param value 문자열 값 / String value
     * @return 매칭되는 PersonGroupType, 없으면 OTHER / Matching PersonGroupType, or OTHER if not found
     */
    public static PersonGroupType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (PersonGroupType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }

        return OTHER;
    }

    @Override
    public String toString() {
        return value;
    }
}
