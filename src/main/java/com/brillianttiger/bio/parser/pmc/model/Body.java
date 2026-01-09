package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Body / 본문
 *
 * KR: 논문의 본문 내용. JATS 1.4 완전 준수 모델.
 * EN: Article body content. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT body (
 *          (address | alternatives | answer | answer-set | array |
 *           block-alternatives | boxed-text | chem-struct-wrap | code |
 *           explanation | fig | fig-group | graphic | media | preformat |
 *           question | question-wrap | question-wrap-group |
 *           supplementary-material | table-wrap | table-wrap-group |
 *           disp-formula | disp-formula-group | def-list | list |
 *           tex-math | mml:math | p | related-article | related-object |
 *           ack | disp-quote | speech | statement | verse-group | x)*,
 *          (sec)*,
 *          sig-block?)>
 *
 *      <!ATTLIST body
 *          %jats-common-atts;
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/body.html
 *
 * Note: The body contains the main narrative content of the article.
 * It typically consists of sections (sec elements), but can also contain
 * paragraphs, figures, tables, and other block-level elements directly.
 *
 * Example:
 * <body>
 *   <sec id="s1" sec-type="intro">
 *     <title>Introduction</title>
 *     <p>This is the introduction...</p>
 *   </sec>
 *   <sec id="s2" sec-type="methods">
 *     <title>Methods</title>
 *     <p>Methods description...</p>
 *   </sec>
 * </body>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Body {

    /**
     * ID 속성 / ID attribute
     *
     * KR: body 요소의 고유 식별자.
     * EN: Unique identifier for the body element.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 body의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this body.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 본문에 포함된 섹션 목록.
     * EN: List of sections in the body.
     *
     * DTD: sec*
     * Required: NO (0 or more)
     *
     * Note: Most articles organize their body content into sections.
     */
    private List<Sec> sections;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 섹션에 포함되지 않은 직접 포함된 문단 목록.
     * EN: List of paragraphs directly contained in the body (not within sections).
     *
     * DTD: p*
     * Required: NO (0 or more)
     *
     * Note: Paragraphs can appear directly in the body without being in a section.
     */
    private List<P> paragraphs;

    // TODO: Add support for other block elements when implemented:
    // - List<Fig> figures
    // - List<TableWrap> tableWraps
    // - List<PmcList> lists (renamed to avoid conflict with java.util.List)
    // - List<DefList> defLists
    // - List<BoxedText> boxedTexts
    // - List<DispQuote> dispQuotes
    // - List<Code> codeBlocks
    // - List<DispFormula> dispFormulas
}
