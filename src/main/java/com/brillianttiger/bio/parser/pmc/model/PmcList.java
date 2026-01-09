package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PmcList / 목록
 *
 * KR: 정렬 또는 비정렬 목록. JATS 1.4 완전 준수 모델.
 * EN: Ordered or unordered list. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT list (
 *          (object-id)*,
 *          label?,
 *          title?,
 *          (list-item)+)>
 *
 *      <!ATTLIST list
 *          %jats-common-atts;
 *          continued-from IDREF #IMPLIED
 *          list-content CDATA #IMPLIED
 *          list-type CDATA #IMPLIED
 *          prefix-word CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/list.html
 *
 * Note: Lists can be ordered (numbered), unordered (bulleted), or other types.
 * The list-type attribute specifies the formatting style (bullet, order, alpha-lower, etc.).
 * Lists must contain at least one list-item.
 *
 * Example:
 * <list list-type="bullet">
 *   <list-item><p>First item</p></list-item>
 *   <list-item><p>Second item</p></list-item>
 *   <list-item><p>Third item</p></list-item>
 * </list>
 *
 * Note: Named "PmcList" to avoid conflict with java.util.List.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcList {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 목록의 고유 식별자.
     * EN: Unique identifier for this list.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 이전 목록 참조 / Continued from
     *
     * KR: 이 목록이 이어지는 이전 목록의 ID.
     * EN: ID of the previous list that this list continues from.
     *
     * DTD: continued-from IDREF #IMPLIED
     * Required: NO
     *
     * Note: Used when a list is interrupted by other content and then resumed.
     */
    private String continuedFrom;

    /**
     * 목록 콘텐츠 / List content
     *
     * KR: 목록 항목의 콘텐츠 유형.
     * EN: Type of content in the list items.
     *
     * DTD: list-content CDATA #IMPLIED
     * Required: NO
     *
     * Example: "authors", "affiliations", "copyright-statements"
     */
    private String listContent;

    /**
     * 목록 타입 / List type
     *
     * KR: 목록의 표시 스타일 (bullet, order, alpha-lower 등).
     * EN: Display style of the list (bullet, order, alpha-lower, etc.).
     *
     * DTD: list-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "bullet", "order", "alpha-lower", "alpha-upper",
     *          "roman-lower", "roman-upper", "simple"
     */
    private String listType;

    /**
     * 접두어 / Prefix word
     *
     * KR: 목록 앞에 표시될 접두어 텍스트.
     * EN: Prefix text to display before the list.
     *
     * DTD: prefix-word CDATA #IMPLIED
     * Required: NO
     *
     * Example: "including", "such as", "for example"
     */
    private String prefixWord;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 목록의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this list.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 레이블 / Label
     *
     * KR: 목록의 레이블.
     * EN: Label for the list.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 목록의 제목.
     * EN: Title of the list.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 목록 항목 목록 / List item list
     *
     * KR: 목록에 포함된 항목들 (최소 1개 필수).
     * EN: List of items in this list (at least one required).
     *
     * DTD: list-item+
     * Required: YES (1 or more)
     *
     * Note: A list must contain at least one list-item element.
     */
    private List<ListItem> items;
}
