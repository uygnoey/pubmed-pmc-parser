package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ChemStruct / 화학 구조
 *
 * KR: 화학 구조를 나타내는 요소. JATS 1.4 DTD 완전 준수 모델.
 *     화학 화합물의 구조식, 분자식 등을 표현.
 * EN: Element representing chemical structure. Fully compliant with JATS 1.4 DTD.
 *     Represents structural formulas, molecular formulas of chemical compounds.
 *
 * DTD: <!ELEMENT chem-struct (#PCDATA | %access.class; | %address-link.class; |
 *          %break.class; | %emphasis.class; | %inline-display-noalt.class; |
 *          %inline-math.class; | %list.class; | %simple-link.class; |
 *          %simple-text.class; | %subsup.class; | alt-text | array |
 *          attrib | code | graphic | label | long-desc | media |
 *          permissions | preformat | textual-form)*>
 *
 * DTD: <!ATTLIST chem-struct
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *          xlink:href CDATA #IMPLIED
 *          xlink:role CDATA #IMPLIED
 *          xlink:show (embed | new | none | other | replace) #IMPLIED
 *          xlink:title CDATA #IMPLIED
 *          xlink:type (simple) #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/chem-struct.html
 *
 * Example:
 * <chem-struct>
 *     <graphic xlink:href="compound1.png"/>
 * </chem-struct>
 *
 * Example:
 * <chem-struct id="cs1">
 *     H<sub>2</sub>O + CO<sub>2</sub> → H<sub>2</sub>CO<sub>3</sub>
 * </chem-struct>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChemStruct {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 화학 구조의 콘텐츠 유형.
     * EN: Content type of the chemical structure.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "2d-structure", "3d-structure", "reaction", "formula"
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
     * XLink 실행 시점 / XLink actuate
     *
     * KR: 링크 리소스 활성화 시점.
     * EN: When to activate the linked resource.
     *
     * DTD: xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
     * Required: NO
     */
    private XlinkActuate xlinkActuate;

    /**
     * XLink href / XLink href
     *
     * KR: 화학 구조 파일 경로 또는 URL.
     * EN: Path or URL to the chemical structure file.
     *
     * DTD: xlink:href CDATA #IMPLIED
     * Required: NO
     */
    private String xlinkHref;

    /**
     * XLink role / XLink role
     *
     * KR: 링크의 역할을 설명하는 URI.
     * EN: URI describing the role of the link.
     *
     * DTD: xlink:role CDATA #IMPLIED
     * Required: NO
     */
    private String xlinkRole;

    /**
     * XLink 표시 방식 / XLink show
     *
     * KR: 링크 리소스 표시 방식.
     * EN: How to display the linked resource.
     *
     * DTD: xlink:show (embed | new | none | other | replace) #IMPLIED
     * Required: NO
     */
    private XlinkShow xlinkShow;

    /**
     * XLink 제목 / XLink title
     *
     * KR: 링크에 대한 사람이 읽을 수 있는 제목.
     * EN: Human-readable title for the link.
     *
     * DTD: xlink:title CDATA #IMPLIED
     * Required: NO
     */
    private String xlinkTitle;

    /**
     * XLink 타입 / XLink type
     *
     * KR: XLink 타입 (항상 "simple").
     * EN: XLink type (always "simple").
     *
     * DTD: xlink:type (simple) #IMPLIED
     * Required: NO
     * Fixed: "simple"
     */
    @Builder.Default
    private String xlinkType = "simple";

    /**
     * XML Base / XML Base
     *
     * KR: 상대 URI 해석을 위한 기본 URI.
     * EN: Base URI for resolving relative URIs.
     *
     * DTD: xml:base CDATA #IMPLIED
     * Required: NO
     */
    private String xmlBase;

    /**
     * XML 언어 / XML Language
     *
     * KR: 콘텐츠의 언어 코드.
     * EN: Language code of the content.
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    // ========== Content / 콘텐츠 ==========

    /**
     * 텍스트 값 / Text value
     *
     * KR: 화학 구조의 텍스트 표현 (혼합 콘텐츠).
     * EN: Text representation of the chemical structure (mixed content).
     *
     * DTD: #PCDATA (mixed content)
     * Required: NO
     *
     * Example: "H₂O", "CH₃COOH"
     */
    private String value;

    /**
     * 레이블 / Label
     *
     * KR: 화학 구조의 레이블.
     * EN: Label for the chemical structure.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 대체 텍스트 목록 / Alternative text list
     *
     * KR: 접근성을 위한 대체 텍스트 목록.
     * EN: Alternative text for accessibility.
     *
     * DTD: alt-text*
     * Required: NO (0 or more)
     */
    private List<AltText> altTexts;

    /**
     * 긴 설명 목록 / Long description list
     *
     * KR: 접근성을 위한 긴 설명 목록.
     * EN: Long description for accessibility.
     *
     * DTD: long-desc*
     * Required: NO (0 or more)
     */
    private List<LongDesc> longDescs;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 화학 구조 이미지 목록.
     * EN: List of chemical structure images.
     *
     * DTD: graphic*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 미디어 목록 (3D 구조 등).
     * EN: List of media (3D structures, etc.).
     *
     * DTD: media*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 배열 목록 / Array list
     *
     * KR: 배열 목록.
     * EN: List of arrays.
     *
     * DTD: array*
     * Required: NO (0 or more)
     */
    private List<Array> arrays;

    /**
     * 코드 목록 / Code list
     *
     * KR: 코드 블록 목록 (SMILES, InChI 등).
     * EN: List of code blocks (SMILES, InChI, etc.).
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codes;

    /**
     * 속성 정보 / Attribution
     *
     * KR: 속성/출처 정보.
     * EN: Attribution/source information.
     *
     * DTD: attrib?
     * Required: NO
     */
    private Attrib attrib;

    /**
     * 권한 정보 / Permissions
     *
     * KR: 저작권 및 라이선스 정보.
     * EN: Copyright and license information.
     *
     * DTD: permissions?
     * Required: NO
     */
    private Permissions permissions;

    /**
     * 텍스트 형식 목록 / Textual form list
     *
     * KR: 텍스트 형식 대안 목록.
     * EN: List of textual form alternatives.
     *
     * DTD: textual-form*
     * Required: NO (0 or more)
     */
    private List<TextualForm> textualForms;
}
