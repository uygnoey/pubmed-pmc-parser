package io.brillianttiger.bio.parser.pmc.model;

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

    /**
     * 목록 / List
     *
     * KR: 정의에 포함된 목록 (순서/비순서).
     * EN: Lists (ordered/unordered) in the definition.
     *
     * DTD: list*
     * Required: NO (0 or more)
     *
     * Note: PmcList를 사용 (java.util.List와의 충돌 방지).
     */
    private List<PmcList> lists;

    /**
     * 그림 목록 / Figure list
     *
     * KR: 정의에 포함된 그림 목록.
     * EN: List of figures in the definition.
     *
     * DTD: fig*
     * Required: NO (0 or more)
     */
    private List<Fig> figures;

    /**
     * 테이블 래퍼 목록 / Table wrap list
     *
     * KR: 정의에 포함된 테이블 목록.
     * EN: List of tables in the definition.
     *
     * DTD: table-wrap*
     * Required: NO (0 or more)
     */
    private List<TableWrap> tableWraps;

    /**
     * 박스 텍스트 목록 / Boxed text list
     *
     * KR: 정의에 포함된 박스 텍스트 목록.
     * EN: List of boxed text in the definition.
     *
     * DTD: boxed-text*
     * Required: NO (0 or more)
     */
    private List<BoxedText> boxedTexts;

    /**
     * 인용구 목록 / Display quote list
     *
     * KR: 정의에 포함된 인용구 목록.
     * EN: List of display quotes in the definition.
     *
     * DTD: disp-quote*
     * Required: NO (0 or more)
     */
    private List<DispQuote> dispQuotes;

    /**
     * 코드 블록 목록 / Code block list
     *
     * KR: 정의에 포함된 코드 블록 목록.
     * EN: List of code blocks in the definition.
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codeBlocks;

    /**
     * 표시 수식 목록 / Display formula list
     *
     * KR: 정의에 포함된 표시 수식 목록.
     * EN: List of display formulas in the definition.
     *
     * DTD: disp-formula*
     * Required: NO (0 or more)
     */
    private List<DispFormula> dispFormulas;
}
