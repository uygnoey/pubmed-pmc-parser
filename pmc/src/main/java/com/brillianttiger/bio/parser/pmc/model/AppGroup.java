package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AppGroup / 부록 그룹
 *
 * KR: 부록 그룹 컨테이너. JATS 1.4 DTD 완전 준수 모델.
 * EN: Container for appendices. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT app-group (label?, title?,
 *                           (%address-link.class; | %just-para.class; |
 *                            %related-article.class; | %rest-of-para.class;)*,
 *                           (app | ref-list)*)>
 *
 * DTD: <!ATTLIST app-group
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/app-group.html
 *
 * Example:
 * <app-group>
 *   <title>Appendices</title>
 *   <app id="app1">
 *     <label>Appendix A</label>
 *     <title>Supplementary Methods</title>
 *     <p>...</p>
 *   </app>
 *   <app id="app2">
 *     <label>Appendix B</label>
 *     <title>Data Tables</title>
 *     <p>...</p>
 *   </app>
 * </app-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppGroup {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 부록 그룹의 콘텐츠 유형.
     * EN: Type of content in the appendix group.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
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
     * KR: 부록 그룹의 텍스트 내용 (혼합 콘텐츠).
     * EN: Text content of the appendix group (mixed content).
     *
     * DTD: (#PCDATA | ...)*
     * Required: NO
     */
    private String value;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 레이블 / Label
     *
     * KR: 부록 그룹의 레이블.
     * EN: Label for the appendix group.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 부록 그룹의 제목.
     * EN: Title of the appendix group.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 부록 그룹 서두 단락 목록.
     * EN: List of paragraphs in the appendix group header.
     *
     * DTD: (%just-para.class; | %rest-of-para.class;)*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 부록 목록 / Appendix list
     *
     * KR: 개별 부록 목록.
     * EN: List of individual appendices.
     *
     * DTD: (app | ref-list)*
     * Required: NO (0 or more)
     */
    private List<App> apps;

    /**
     * 참조 목록 / Reference list
     *
     * KR: 참고문헌 목록 (부록 그룹에 포함될 경우).
     * EN: List of reference lists within the appendix group.
     *
     * DTD: (app | ref-list)*
     * Required: NO (0 or more)
     */
    private List<RefList> refLists;

    /**
     * 주소/링크 요소 목록 / Address/link elements
     *
     * KR: 주소 및 링크 관련 요소 목록.
     * EN: List of address and link related elements.
     *
     * DTD: %address-link.class;*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 그래픽/이미지 목록.
     * EN: List of graphics/images.
     *
     * DTD: %rest-of-para.class;*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;
}
