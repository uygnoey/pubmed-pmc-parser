package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Ref / 참조
 *
 * KR: 개별 참조 항목. JATS 1.4 DTD 완전 준수 모델.
 * EN: Individual reference entry. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT ref (label?, (%citation.class;)+)>
 *
 * DTD: <!ATTLIST ref
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/ref.html
 *
 * Note: %citation.class; includes element-citation, mixed-citation, nlm-citation, note, x
 *
 * Example:
 * <ref id="B1" content-type="journal">
 *   <label>1</label>
 *   <element-citation publication-type="journal">
 *     <person-group person-group-type="author">
 *       <name><surname>Smith</surname><given-names>John</given-names></name>
 *     </person-group>
 *     <article-title>Example Article</article-title>
 *     <source>Journal of Examples</source>
 *     <year iso-8601-date="2023">2023</year>
 *     <volume>10</volume>
 *     <fpage>1</fpage>
 *     <lpage>10</lpage>
 *     <pub-id pub-id-type="doi">10.1234/example.2023.001</pub-id>
 *   </element-citation>
 * </ref>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ref {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 참조의 콘텐츠 유형.
     * EN: Type of content in the reference.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "journal", "book", "web"
     */
    private String contentType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자 (인용 참조용).
     * EN: Unique identifier within the XML document (for citation references).
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     *
     * Example: "B1", "R1", "ref1"
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 특정 용도나 목적을 나타내는 문자열.
     * EN: String indicating specific use or purpose.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * XML Base / XML base
     *
     * KR: 상대 URI 해석을 위한 기본 URI.
     * EN: Base URI for resolving relative URIs.
     *
     * DTD: xml:base CDATA #IMPLIED
     * Required: NO
     */
    private String xmlBase;

    /**
     * XML 언어 / XML language
     *
     * KR: 내용의 언어 코드 (ISO 639).
     * EN: Language code for content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 레이블 / Label
     *
     * KR: 참조 레이블 (예: "[1]", "1.", "a)").
     * EN: Reference label (e.g., "[1]", "1.", "a)").
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 요소 인용 목록 / Element citation list
     *
     * KR: 구조화된 인용 정보 목록.
     * EN: List of structured citation information.
     *
     * DTD: %citation.class; includes element-citation
     * Required: NO (0 or more, but at least one citation type is required)
     */
    private List<ElementCitation> elementCitations;

    /**
     * 혼합 인용 목록 / Mixed citation list
     *
     * KR: 비구조화된 인용 정보 목록 (텍스트와 구조화 요소 혼합).
     * EN: List of unstructured citation information (mixed text and structured elements).
     *
     * DTD: %citation.class; includes mixed-citation
     * Required: NO (0 or more)
     */
    private List<MixedCitation> mixedCitations;

    /**
     * NLM 인용 목록 / NLM citation list
     *
     * KR: NLM 스타일 인용 정보 목록 (deprecated, 호환성을 위해 유지).
     * EN: List of NLM-style citation information (deprecated, kept for compatibility).
     *
     * DTD: %citation.class; includes nlm-citation
     * Required: NO (0 or more)
     */
    private List<NlmCitation> nlmCitations;

    /**
     * 노트 목록 / Note list
     *
     * KR: 참조 관련 노트 목록.
     * EN: List of notes related to the reference.
     *
     * DTD: %citation.class; includes note
     * Required: NO (0 or more)
     */
    private List<Note> notes;
}
