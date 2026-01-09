package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Def / 정의
 *
 * KR: 용어의 정의. JATS 1.4 완전 준수 모델.
 * EN: Definition of a term. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT def (
 *          (label)?,
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
 *      <!ATTLIST def
 *          %jats-common-atts;
 *          rid IDREFS #IMPLIED
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/def.html
 *
 * Note: The def element contains the definition(s) for the associated term(s).
 * It can contain paragraphs, lists, figures, tables, and other block-level elements,
 * allowing for rich, detailed definitions.
 *
 * Example:
 * <def>
 *   <p>Deoxyribonucleic acid, a molecule that carries genetic instructions.</p>
 * </def>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Def {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 정의의 고유 식별자.
     * EN: Unique identifier for this definition.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 참조 ID / Reference ID
     *
     * KR: 다른 요소를 가리키는 참조 식별자.
     * EN: Reference identifier pointing to another element.
     *
     * DTD: rid IDREFS #IMPLIED
     * Required: NO
     */
    private String rid;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 정의의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this definition.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 레이블 / Label
     *
     * KR: 정의의 레이블.
     * EN: Label for the definition.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 정의의 제목.
     * EN: Title of the definition.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 정의를 설명하는 문단 목록.
     * EN: List of paragraphs describing the definition.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 중첩 정의 목록 / Nested definition list
     *
     * KR: 정의 내부의 중첩된 정의 목록.
     * EN: Nested definition lists within this definition.
     *
     * DTD: def-list*
     * Required: NO (0 or more)
     *
     * Note: Definitions can contain nested definition lists for hierarchical terms.
     */
    private List<DefList> defLists;

    // TODO: Add support for other block elements when implemented:
    // - List<PmcList> lists
    // - List<Fig> figures
    // - List<TableWrap> tableWraps
    // - List<BoxedText> boxedTexts
    // - List<DispQuote> dispQuotes
    // - List<Code> codeBlocks
    // - List<DispFormula> dispFormulas
}
