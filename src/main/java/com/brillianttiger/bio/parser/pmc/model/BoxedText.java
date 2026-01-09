package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * BoxedText / 박스 텍스트
 *
 * KR: 상자 안에 표시되는 텍스트 블록. JATS 1.4 완전 준수 모델.
 * EN: Text block displayed in a box. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT boxed-text (
 *          (object-id)*,
 *          sec-meta?,
 *          label?,
 *          caption?,
 *          (address | alternatives | answer | answer-set | array |
 *           block-alternatives | boxed-text | chem-struct-wrap | code |
 *           explanation | fig | fig-group | graphic | media | preformat |
 *           question | question-wrap | question-wrap-group |
 *           supplementary-material | table-wrap | table-wrap-group |
 *           disp-formula | disp-formula-group | def-list | list |
 *           tex-math | mml:math | p | related-article | related-object |
 *           disp-quote | speech | statement | verse-group | x)*,
 *          (sec)*,
 *          (attrib | permissions)*)>
 *
 *      <!ATTLIST boxed-text
 *          %jats-common-atts;
 *          content-type CDATA #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          position (anchor | background | float | margin) "float"
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/boxed-text.html
 *
 * Note: Boxed text is used for sidebars, text boxes, and other content
 * that should be visually separated from the main narrative flow.
 * Common uses include case studies, examples, tips, warnings, and summaries.
 *
 * Example:
 * <boxed-text position="float">
 *   <caption><title>Case Study</title></caption>
 *   <p>A 45-year-old patient presented with symptoms of...</p>
 * </boxed-text>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoxedText {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 박스 텍스트의 고유 식별자.
     * EN: Unique identifier for this boxed text.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 콘텐츠 타입 / Content type
     *
     * KR: 박스 텍스트의 콘텐츠 유형.
     * EN: Type of the boxed text content.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "case-study", "example", "tip", "warning", "summary", "sidebar"
     */
    private String contentType;

    /**
     * 방향 / Orientation
     *
     * KR: 박스 텍스트의 페이지 방향.
     * EN: Page orientation for the boxed text.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     *
     * Example: "portrait", "landscape"
     */
    private String orientation;

    /**
     * 위치 / Position
     *
     * KR: 박스 텍스트의 배치 위치.
     * EN: Positioning of the boxed text.
     *
     * DTD: position (anchor | background | float | margin) "float"
     * Required: NO (default: "float")
     *
     * Example: "anchor", "background", "float", "margin"
     */
    private String position;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 박스 텍스트의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this boxed text.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 섹션 메타데이터 / Section metadata
     *
     * KR: 박스 텍스트의 메타데이터.
     * EN: Metadata for the boxed text.
     *
     * DTD: sec-meta?
     * Required: NO
     */
    private SecMeta secMeta;

    /**
     * 레이블 / Label
     *
     * KR: 박스 텍스트의 레이블.
     * EN: Label for the boxed text.
     *
     * DTD: label?
     * Required: NO
     *
     * Example: "Box 1", "Case Study 1"
     */
    private Label label;

    /**
     * 캡션 / Caption
     *
     * KR: 박스 텍스트의 캡션 (제목 포함).
     * EN: Caption for the boxed text (including title).
     *
     * DTD: caption?
     * Required: NO
     */
    private Caption caption;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 박스 텍스트 내용을 구성하는 문단 목록.
     * EN: List of paragraphs constituting the boxed text content.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 박스 텍스트 내부의 섹션 목록.
     * EN: List of sections within the boxed text.
     *
     * DTD: sec*
     * Required: NO (0 or more)
     *
     * Note: Boxed text can contain sections for structured content.
     */
    private List<Sec> sections;

    /**
     * 출처 / Attribution
     *
     * KR: 박스 텍스트의 출처 또는 저자 정보.
     * EN: Attribution or source information for the boxed text.
     *
     * DTD: attrib*
     * Required: NO (0 or more)
     */
    private String attrib;

    /**
     * 권한 정보 / Permissions
     *
     * KR: 박스 텍스트의 저작권 및 권한 정보.
     * EN: Copyright and permissions information for the boxed text.
     *
     * DTD: permissions*
     * Required: NO (0 or more)
     */
    private Permissions permissions;

    // TODO: Add support for other block elements when implemented:
    // - List<Fig> figures
    // - List<TableWrap> tableWraps
    // - List<PmcList> lists
    // - List<DefList> defLists
    // - List<DispQuote> dispQuotes
    // - List<Code> codeBlocks
    // - List<DispFormula> dispFormulas
}
