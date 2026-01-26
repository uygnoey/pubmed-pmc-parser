package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TransAbstract / 번역 초록
 *
 * KR: 번역된 초록. JATS 1.4 완전 준수 모델.
 * EN: Translated abstract. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT trans-abstract (
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
 * DTD: <!ATTLIST trans-abstract
 *          abstract-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/trans-abstract.html
 *
 * Note: Structure is identical to abstract, but represents a translation of the
 * abstract into another language. xml:lang attribute specifies the translation language.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransAbstract {

    /**
     * 초록 유형 / Abstract type
     *
     * KR: 초록의 유형 (summary, short, toc, graphical 등).
     * EN: Type of abstract (summary, short, toc, graphical, etc.).
     *
     * DTD: abstract-type CDATA #IMPLIED
     */
    private String abstractType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: 번역 초록의 고유 ID (문서 내 참조용).
     * EN: Unique ID of the translated abstract (for in-document reference).
     *
     * DTD: id ID #IMPLIED
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 번역 초록의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this translated abstract (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 언어 / Language (IMPORTANT)
     *
     * KR: 번역 초록의 언어 (ISO 639 코드).
     *     원본과 다른 언어여야 함.
     * EN: Language of translated abstract (ISO 639 code).
     *     Must be different from the original language.
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     *
     * Examples: "en", "ko", "ja", "zh", "es", "fr"
     */
    private String xmlLang;

    /**
     * 제목 목록 / Title list
     *
     * KR: 번역 초록의 제목 목록.
     * EN: Title list of translated abstract.
     *
     * DTD: title*
     * Required: NO (0 or more)
     */
    private List<Title> titles;

    /**
     * 레이블 목록 / Label list
     *
     * KR: 번역 초록의 레이블 목록.
     * EN: Label list of translated abstract.
     *
     * DTD: label*
     * Required: NO (0 or more)
     */
    private List<Label> labels;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 번역 초록의 본문 문단 목록.
     * EN: Body paragraph list of translated abstract.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 번역 초록의 섹션 목록 (구조화된 초록).
     * EN: Section list of translated abstract (structured abstract).
     *
     * DTD: sec*
     * Required: NO (0 or more)
     */
    private List<Sec> sections;

    /**
     * 초록 내용 / Abstract content (텍스트)
     *
     * KR: 단순 텍스트 번역 초록 내용 (비구조화).
     * EN: Simple text translated abstract content (unstructured).
     *
     * Note: For simple, unstructured abstracts as plain text.
     */
    private String value;
}
