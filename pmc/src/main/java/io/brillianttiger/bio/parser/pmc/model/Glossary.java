package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Glossary / 용어집
 *
 * KR: 용어집 (정의, 약어 목록 등). JATS 1.4 DTD 완전 준수 모델.
 *     용어집은 중첩이 가능하며, 재귀 구조를 지원.
 * EN: Glossary (definitions, abbreviation lists, etc.). Fully compliant with JATS 1.4 DTD.
 *     Glossaries can be nested, supporting recursive structure.
 *
 * DTD: <!ELEMENT glossary (label?, title?, (%address-link.class; | %just-para.class; |
 *                          %rest-of-para.class;)*, glossary*)>
 *
 * DTD: <!ATTLIST glossary
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/glossary.html
 *
 * Note: %just-para.class; includes def-list which is commonly used for glossary content.
 * Glossaries may contain nested glossaries for hierarchical term organization.
 *
 * Example:
 * <glossary>
 *   <title>Abbreviations</title>
 *   <def-list>
 *     <def-item>
 *       <term>DNA</term>
 *       <def><p>Deoxyribonucleic acid</p></def>
 *     </def-item>
 *     <def-item>
 *       <term>RNA</term>
 *       <def><p>Ribonucleic acid</p></def>
 *     </def-item>
 *   </def-list>
 * </glossary>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Glossary {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 용어집의 콘텐츠 유형.
     * EN: Type of content in the glossary.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "abbreviations", "symbols", "terms"
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

    // ========== Mixed Content / 혼합 콘텐츠 ==========

    /**
     * 텍스트 내용 / Text content
     *
     * KR: 용어집의 텍스트 내용 (혼합 콘텐츠).
     * EN: Text content of the glossary (mixed content).
     *
     * DTD: (#PCDATA | ...)*
     * Required: NO
     */
    private String value;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 레이블 / Label
     *
     * KR: 용어집의 레이블.
     * EN: Label for the glossary.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 용어집의 제목 (예: "Abbreviations", "Glossary of Terms").
     * EN: Title of the glossary (e.g., "Abbreviations", "Glossary of Terms").
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 용어집 내 단락 목록.
     * EN: List of paragraphs in the glossary.
     *
     * DTD: (%just-para.class; | %rest-of-para.class;)*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 정의 목록 / Definition list
     *
     * KR: 용어-정의 쌍으로 구성된 정의 목록.
     *     용어집의 핵심 콘텐츠.
     * EN: Definition list consisting of term-definition pairs.
     *     Core content of the glossary.
     *
     * DTD: %just-para.class; includes def-list
     * Required: NO (0 or more)
     */
    private List<DefList> defLists;

    /**
     * 중첩 용어집 목록 / Nested glossary list
     *
     * KR: 이 용어집 내에 중첩된 용어집 목록 (재귀 구조).
     *     주제별로 용어를 그룹화하는 데 사용.
     * EN: List of nested glossaries within this glossary (recursive structure).
     *     Used to group terms by topic.
     *
     * DTD: glossary*
     * Required: NO (0 or more)
     *
     * Note: This supports hierarchical glossary organization.
     */
    private List<Glossary> nestedGlossaries;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 관련 외부 자료에 대한 링크 목록.
     * EN: List of links to related external resources.
     *
     * DTD: %address-link.class;*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 용어집 내 그래픽/이미지 목록.
     * EN: List of graphics/images in the glossary.
     *
     * DTD: %rest-of-para.class;*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;
}
