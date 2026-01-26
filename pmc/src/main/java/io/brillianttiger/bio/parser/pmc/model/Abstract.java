package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Abstract / 초록
 *
 * KR: 논문 초록. JATS 1.4 완전 준수 모델.
 * EN: Article abstract. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT abstract (
 *          (object-id)*,
 *          (title | label)*,
 *          (address | alternatives | answer | answer-set | array |
 *           block-alternatives | boxed-text | chem-struct-wrap | code |
 *           explanation | fig | fig-group | graphic | media | preformat |
 *           question | question-wrap | question-wrap-group |
 *           supplementary-material | table-wrap | table-wrap-group |
 *           disp-formula | disp-formula-group | def-list | list |
 *           tex-math | mml:math | p | related-article | related-object |
 *           disp-quote | speech | statement | verse-group | x | sec)*,
 *          (sec-meta?, (title | label)*,
 *           (%block-display.class; | %block-math.class; | %just-para.class; |
 *            %list.class; | %nothing-but-para.class; | %related-article.class; |
 *            %rest-of-para.class; | x)*, sec*)*
 *      )>
 *
 * DTD: <!ATTLIST abstract
 *          abstract-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/abstract.html
 *
 * Note: abstract-type common values:
 * - summary: General summary abstract (일반 요약 초록)
 * - short: Short abstract (짧은 초록)
 * - executive-summary: Executive summary (경영진 요약)
 * - toc: Table of contents summary (목차 요약)
 * - web-summary: Web summary (웹 요약)
 * - graphical: Graphical abstract (그래픽 초록)
 * - author-highlights: Author highlights (저자 하이라이트)
 * - plain-language-summary: Plain language summary (일반어 요약)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Abstract {

    /**
     * 초록 유형 / Abstract type
     *
     * KR: 초록의 유형 (summary, short, toc, graphical 등).
     * EN: Type of abstract (summary, short, toc, graphical, etc.).
     *
     * DTD: abstract-type CDATA #IMPLIED
     *
     * Common values:
     * - summary: General summary
     * - short: Short abstract
     * - executive-summary: Executive summary
     * - toc: Table of contents summary
     * - web-summary: Web summary
     * - graphical: Graphical abstract
     * - author-highlights: Author highlights
     * - plain-language-summary: Plain language summary
     */
    private String abstractType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: 초록의 고유 ID (문서 내 참조용).
     * EN: Unique ID of the abstract (for in-document reference).
     *
     * DTD: id ID #IMPLIED
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 초록의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this abstract (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 언어 / Language
     *
     * KR: 초록의 언어 (ISO 639 코드).
     * EN: Language of abstract (ISO 639 code).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     *
     * Examples: "en", "ko", "ja", "zh"
     */
    private String xmlLang;

    /**
     * 제목 목록 / Title list
     *
     * KR: 초록의 제목 목록.
     * EN: Title list of abstract.
     *
     * DTD: title*
     * Required: NO (0 or more)
     */
    private List<Title> titles;

    /**
     * 레이블 목록 / Label list
     *
     * KR: 초록의 레이블 목록.
     * EN: Label list of abstract.
     *
     * DTD: label*
     * Required: NO (0 or more)
     */
    private List<Label> labels;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 초록의 본문 문단 목록.
     * EN: Body paragraph list of abstract.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 초록의 섹션 목록 (구조화된 초록).
     * EN: Section list of abstract (structured abstract).
     *
     * DTD: sec*
     * Required: NO (0 or more)
     *
     * Note: Used for structured abstracts with subsections like
     * Background, Methods, Results, Conclusions.
     */
    private List<Sec> sections;

    /**
     * 초록 내용 / Abstract content (텍스트)
     *
     * KR: 단순 텍스트 초록 내용 (비구조화).
     * EN: Simple text abstract content (unstructured).
     *
     * Note: For simple, unstructured abstracts as plain text.
     */
    private String value;
}
