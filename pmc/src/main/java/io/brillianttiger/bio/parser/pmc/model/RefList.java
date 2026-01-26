package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * RefList / 참조 목록
 *
 * KR: 논문 참조 목록 (재귀 구조 지원). JATS 1.4 DTD 완전 준수 모델.
 * EN: Article reference list (supports recursive structure). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT ref-list (label?, title?, (%address-link.class; | %just-para.class;)*,
 *                          (ref)*, (ref-list)*)>
 *
 * DTD: <!ATTLIST ref-list
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/ref-list.html
 *
 * Example:
 * <ref-list>
 *   <title>References</title>
 *   <ref id="R1">
 *     <label>1</label>
 *     <element-citation publication-type="journal">
 *       ...
 *     </element-citation>
 *   </ref>
 *   <ref id="R2">
 *     <label>2</label>
 *     <mixed-citation publication-type="book">...</mixed-citation>
 *   </ref>
 * </ref-list>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefList {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 참조 목록의 콘텐츠 유형.
     * EN: Type of content in the reference list.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "cited", "supplementary"
     */
    private String contentType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자.
     * EN: Unique identifier within the XML document.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
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
     * KR: 참조 목록의 레이블.
     * EN: Label for the reference list.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 참조 목록의 제목 (예: "References", "Bibliography").
     * EN: Title of the reference list (e.g., "References", "Bibliography").
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 참조 목록 서두의 설명 단락.
     * EN: Introductory paragraphs in the reference list.
     *
     * DTD: (%just-para.class;)*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 참조 목록 / Reference list
     *
     * KR: 개별 참조 항목 목록.
     * EN: List of individual reference entries.
     *
     * DTD: (ref)*
     * Required: NO (0 or more)
     */
    private List<Ref> references;

    /**
     * 하위 참조 목록 (재귀) / Sub-reference list (recursive)
     *
     * KR: 중첩된 참조 목록 (재귀 구조).
     *     주제별로 참조를 그룹화하는 데 사용.
     * EN: Nested reference lists (recursive structure).
     *     Used to group references by topic.
     *
     * DTD: (ref-list)*
     * Required: NO (0 or more)
     */
    private List<RefList> refLists;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 참조 관련 외부 링크 목록.
     * EN: List of external links related to references.
     *
     * DTD: %address-link.class;*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;
}
