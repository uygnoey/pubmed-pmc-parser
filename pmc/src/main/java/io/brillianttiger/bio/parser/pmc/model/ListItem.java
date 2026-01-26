package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ListItem / 목록 항목
 *
 * KR: 목록의 개별 항목. JATS 1.4 완전 준수 모델.
 * EN: Individual list item. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT list-item (
 *          (object-id)*,
 *          label?,
 *          title?,
 *          (address | alternatives | answer | answer-set | array |
 *           block-alternatives | boxed-text | chem-struct-wrap | code |
 *           explanation | fig | fig-group | graphic | media | preformat |
 *           question | question-wrap | question-wrap-group |
 *           supplementary-material | table-wrap | table-wrap-group |
 *           disp-formula | disp-formula-group | def-list | list |
 *           tex-math | mml:math | p | related-article | related-object |
 *           disp-quote | speech | statement | verse-group | x)*)>
 *
 *      <!ATTLIST list-item
 *          %jats-common-atts;
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/list-item.html
 *
 * Note: List items can contain paragraphs, other lists (for nested lists),
 * figures, tables, and various other block-level elements. This allows for
 * complex nested structures within lists.
 *
 * Example:
 * <list-item id="li1">
 *   <label>1.</label>
 *   <p>This is the first item with a paragraph.</p>
 * </list-item>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListItem {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 목록 항목의 고유 식별자.
     * EN: Unique identifier for this list item.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 목록 항목의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this list item.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 레이블 / Label
     *
     * KR: 목록 항목의 레이블 (예: "1.", "a)", "•").
     * EN: Label for the list item (e.g., "1.", "a)", "•").
     *
     * DTD: label?
     * Required: NO
     *
     * Note: The label typically appears before the item content.
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 목록 항목의 제목.
     * EN: Title of the list item.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 이 목록 항목에 포함된 문단 목록.
     * EN: List of paragraphs in this list item.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 중첩 목록 / Nested list
     *
     * KR: 이 항목 내부의 중첩된 목록 (재귀 구조).
     * EN: Nested list within this item (recursive structure).
     *
     * DTD: list*
     * Required: NO (0 or more)
     *
     * Note: Lists can be nested within list items to create hierarchical structures.
     */
    private List<PmcList> nestedLists;

    /**
     * 그림 목록 / Figure list
     *
     * KR: 목록 항목에 포함된 그림 목록.
     * EN: List of figures in the list item.
     *
     * DTD: fig*
     * Required: NO (0 or more)
     */
    private List<Fig> figures;

    /**
     * 테이블 래퍼 목록 / Table wrap list
     *
     * KR: 목록 항목에 포함된 테이블 목록.
     * EN: List of tables in the list item.
     *
     * DTD: table-wrap*
     * Required: NO (0 or more)
     */
    private List<TableWrap> tableWraps;

    /**
     * 정의 목록 / Definition list
     *
     * KR: 목록 항목에 포함된 정의 목록.
     * EN: List of definition lists in the list item.
     *
     * DTD: def-list*
     * Required: NO (0 or more)
     */
    private List<DefList> defLists;

    /**
     * 박스 텍스트 목록 / Boxed text list
     *
     * KR: 목록 항목에 포함된 박스 텍스트 목록.
     * EN: List of boxed text in the list item.
     *
     * DTD: boxed-text*
     * Required: NO (0 or more)
     */
    private List<BoxedText> boxedTexts;

    /**
     * 인용구 목록 / Display quote list
     *
     * KR: 목록 항목에 포함된 인용구 목록.
     * EN: List of display quotes in the list item.
     *
     * DTD: disp-quote*
     * Required: NO (0 or more)
     */
    private List<DispQuote> dispQuotes;

    /**
     * 코드 블록 목록 / Code block list
     *
     * KR: 목록 항목에 포함된 코드 블록 목록.
     * EN: List of code blocks in the list item.
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codeBlocks;

    /**
     * 표시 수식 목록 / Display formula list
     *
     * KR: 목록 항목에 포함된 표시 수식 목록.
     * EN: List of display formulas in the list item.
     *
     * DTD: disp-formula*
     * Required: NO (0 or more)
     */
    private List<DispFormula> dispFormulas;
}
