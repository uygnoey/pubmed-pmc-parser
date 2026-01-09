package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Sec / 섹션
 *
 * KR: 논문 섹션 (재귀 구조). JATS 1.4 완전 준수 모델.
 * EN: Article section (recursive structure). Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT sec (
 *          sec-meta?,
 *          (label | title)*,
 *          (address | alternatives | answer | answer-set | array |
 *           block-alternatives | boxed-text | chem-struct-wrap | code |
 *           explanation | fig | fig-group | graphic | media | preformat |
 *           question | question-wrap | question-wrap-group |
 *           supplementary-material | table-wrap | table-wrap-group |
 *           disp-formula | disp-formula-group | def-list | list |
 *           tex-math | mml:math | p | related-article | related-object |
 *           ack | disp-quote | speech | statement | verse-group | x)*,
 *          (sec)*,
 *          (fn-group | glossary | ref-list | sig-block)*)>
 *
 *      <!ATTLIST sec
 *          %jats-common-atts;
 *          disp-level CDATA #IMPLIED
 *          sec-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/sec.html
 *
 * Note: Sections can be nested recursively to any depth, allowing hierarchical
 * document structures. Common sec-type values include: intro, methods, results,
 * discussion, conclusions, materials, cases, subjects, supplementary-material.
 *
 * Example:
 * <sec id="s1" sec-type="intro">
 *   <title>Introduction</title>
 *   <p>This is the introduction section.</p>
 *   <sec id="s1-1">
 *     <title>Background</title>
 *     <p>Background information...</p>
 *   </sec>
 * </sec>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sec {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 섹션의 고유 식별자.
     * EN: Unique identifier for this section.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 표시 레벨 / Display level
     *
     * KR: 섹션의 표시 계층 레벨 (1, 2, 3 등).
     * EN: Display hierarchy level of the section (1, 2, 3, etc.).
     *
     * DTD: disp-level CDATA #IMPLIED
     * Required: NO
     *
     * Example: "1", "2", "3"
     */
    private String dispLevel;

    /**
     * 섹션 타입 / Section type
     *
     * KR: 섹션의 유형 (intro, methods, results, discussion 등).
     * EN: Type of section (intro, methods, results, discussion, etc.).
     *
     * DTD: sec-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "intro", "materials", "methods", "materials|methods",
     *          "results", "discussion", "results|discussion", "conclusions",
     *          "cases", "subjects", "supplementary-material"
     */
    private String secType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 섹션의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this section.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 언어 / Language
     *
     * KR: 이 섹션의 언어 (ISO 639 코드).
     * EN: Language of this section (ISO 639 code).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "ko", "ja", "zh"
     */
    private String xmlLang;

    /**
     * 섹션 메타데이터 / Section metadata
     *
     * KR: 섹션의 메타데이터 정보.
     * EN: Section metadata information.
     *
     * DTD: sec-meta?
     * Required: NO
     */
    private SecMeta secMeta;

    /**
     * 레이블 / Label
     *
     * KR: 섹션 레이블 (예: "1", "A", "I").
     * EN: Section label (e.g., "1", "A", "I").
     *
     * DTD: label*
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 섹션 제목.
     * EN: Section title.
     *
     * DTD: title*
     * Required: NO
     */
    private Title title;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 이 섹션에 포함된 문단 목록.
     * EN: List of paragraphs in this section.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 하위 섹션 목록 (재귀) / Sub-section list (recursive)
     *
     * KR: 이 섹션에 포함된 하위 섹션 목록 (무한 중첩 가능).
     * EN: List of sub-sections in this section (can be nested infinitely).
     *
     * DTD: sec*
     * Required: NO (0 or more)
     *
     * Note: This recursive structure allows sections to be nested to any depth,
     * supporting complex hierarchical document structures.
     */
    private List<Sec> sections;

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
