package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DefList / 정의 목록
 *
 * KR: 용어와 정의로 구성된 정의 목록. JATS 1.4 완전 준수 모델.
 * EN: Definition list consisting of terms and definitions. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT def-list (
 *          (object-id)*,
 *          label?,
 *          title?,
 *          term-head?,
 *          def-head?,
 *          (def-item)*,
 *          (def-list)*)>
 *
 *      <!ATTLIST def-list
 *          %jats-common-atts;
 *          continued-from IDREF #IMPLIED
 *          list-content CDATA #IMPLIED
 *          list-type CDATA #IMPLIED
 *          prefix-word CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/def-list.html
 *
 * Note: Definition lists are used for glossaries, abbreviation lists, or any
 * content structured as term-definition pairs. They can be nested for hierarchical
 * definitions.
 *
 * Example:
 * <def-list>
 *   <title>Abbreviations</title>
 *   <def-item>
 *     <term>DNA</term>
 *     <def><p>Deoxyribonucleic acid</p></def>
 *   </def-item>
 *   <def-item>
 *     <term>RNA</term>
 *     <def><p>Ribonucleic acid</p></def>
 *   </def-item>
 * </def-list>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefList {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 정의 목록의 고유 식별자.
     * EN: Unique identifier for this definition list.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 이전 목록 참조 / Continued from
     *
     * KR: 이 목록이 이어지는 이전 정의 목록의 ID.
     * EN: ID of the previous definition list that this list continues from.
     *
     * DTD: continued-from IDREF #IMPLIED
     * Required: NO
     */
    private String continuedFrom;

    /**
     * 목록 콘텐츠 / List content
     *
     * KR: 정의 목록의 콘텐츠 유형.
     * EN: Type of content in the definition list.
     *
     * DTD: list-content CDATA #IMPLIED
     * Required: NO
     *
     * Example: "abbreviations", "glossary", "symbols"
     */
    private String listContent;

    /**
     * 목록 타입 / List type
     *
     * KR: 정의 목록의 표시 스타일.
     * EN: Display style of the definition list.
     *
     * DTD: list-type CDATA #IMPLIED
     * Required: NO
     */
    private String listType;

    /**
     * 접두어 / Prefix word
     *
     * KR: 정의 목록 앞에 표시될 접두어 텍스트.
     * EN: Prefix text to display before the definition list.
     *
     * DTD: prefix-word CDATA #IMPLIED
     * Required: NO
     */
    private String prefixWord;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 정의 목록의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this definition list.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 레이블 / Label
     *
     * KR: 정의 목록의 레이블.
     * EN: Label for the definition list.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 정의 목록의 제목.
     * EN: Title of the definition list.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 정의 항목 목록 / Definition item list
     *
     * KR: 용어-정의 쌍의 목록.
     * EN: List of term-definition pairs.
     *
     * DTD: def-item*
     * Required: NO (0 or more)
     */
    private List<DefItem> items;

    /**
     * 중첩 정의 목록 / Nested definition list
     *
     * KR: 이 정의 목록 내부의 중첩된 정의 목록 (재귀 구조).
     * EN: Nested definition lists within this list (recursive structure).
     *
     * DTD: def-list*
     * Required: NO (0 or more)
     *
     * Note: Definition lists can be nested for hierarchical glossaries.
     */
    private List<DefList> nestedDefLists;
}
