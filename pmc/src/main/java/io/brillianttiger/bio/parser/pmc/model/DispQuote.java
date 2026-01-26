package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DispQuote / 인용구
 *
 * KR: 표시되는 인용구 블록. JATS 1.4 완전 준수 모델.
 * EN: Displayed quotation block. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT disp-quote (
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
 *           disp-quote | speech | statement | verse-group | x)*,
 *          (attrib | permissions)*)>
 *
 *      <!ATTLIST disp-quote
 *          %jats-common-atts;
 *          content-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/disp-quote.html
 *
 * Note: Display quotes are used for block quotations that are set off from
 * the main text. They can contain paragraphs, lists, figures, and other
 * block-level elements, as well as attribution information.
 *
 * Example:
 * <disp-quote>
 *   <p>To be, or not to be, that is the question.</p>
 *   <attrib>William Shakespeare, Hamlet</attrib>
 * </disp-quote>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispQuote {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 인용구의 고유 식별자.
     * EN: Unique identifier for this displayed quote.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 콘텐츠 타입 / Content type
     *
     * KR: 인용구의 콘텐츠 유형.
     * EN: Type of the quote content.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "extract", "epigraph", "motto"
     */
    private String contentType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 인용구의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this displayed quote.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 언어 / Language
     *
     * KR: 이 인용구의 언어 (ISO 639 코드).
     * EN: Language of this displayed quote (ISO 639 code).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "ko", "la" (Latin for classical quotes)
     */
    private String xmlLang;

    /**
     * 레이블 / Label
     *
     * KR: 인용구의 레이블.
     * EN: Label for the displayed quote.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 인용구의 제목.
     * EN: Title of the displayed quote.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 인용구 내용을 구성하는 문단 목록.
     * EN: List of paragraphs constituting the quote content.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 출처 / Attribution
     *
     * KR: 인용구의 출처 또는 저자 정보.
     * EN: Attribution or source information for the quote.
     *
     * DTD: attrib*
     * Required: NO (0 or more)
     *
     * Example: "William Shakespeare, Hamlet"
     */
    private String attrib;

    /**
     * 권한 정보 / Permissions
     *
     * KR: 인용구의 저작권 및 권한 정보.
     * EN: Copyright and permissions information for the quote.
     *
     * DTD: permissions*
     * Required: NO (0 or more)
     */
    private Permissions permissions;

    /**
     * 목록 / List
     *
     * KR: 인용구에 포함된 목록 (순서/비순서).
     * EN: Lists (ordered/unordered) in the quote.
     *
     * DTD: list*
     * Required: NO (0 or more)
     *
     * Note: PmcList를 사용 (java.util.List와의 충돌 방지).
     */
    private List<PmcList> lists;

    /**
     * 정의 목록 / Definition list
     *
     * KR: 인용구에 포함된 정의 목록.
     * EN: List of definition lists in the quote.
     *
     * DTD: def-list*
     * Required: NO (0 or more)
     */
    private List<DefList> defLists;

    /**
     * 중첩 인용구 / Nested quotes
     *
     * KR: 인용구 내부의 중첩된 인용구 (재귀 구조).
     * EN: Nested quotes within the quote (recursive structure).
     *
     * DTD: disp-quote*
     * Required: NO (0 or more)
     *
     * Note: Quotes can be nested for layered quotations.
     */
    private List<DispQuote> nestedQuotes;

    /**
     * 코드 블록 목록 / Code block list
     *
     * KR: 인용구에 포함된 코드 블록 목록.
     * EN: List of code blocks in the quote.
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codeBlocks;
}
