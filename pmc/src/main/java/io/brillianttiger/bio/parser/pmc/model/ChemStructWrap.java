package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ChemStructWrap / 화학 구조 래퍼
 *
 * KR: 화학 구조를 감싸는 컨테이너 요소. JATS 1.4 DTD 완전 준수 모델.
 *     레이블, 캡션, 대안 표현 등과 함께 화학 구조를 표시.
 * EN: Container element wrapping chemical structure. Fully compliant with JATS 1.4 DTD.
 *     Displays chemical structure with label, caption, alternative representations.
 *
 * DTD: <!ELEMENT chem-struct-wrap (
 *          (object-id)*,
 *          label?,
 *          (caption)*,
 *          abstract*,
 *          kwd-group*,
 *          alt-text*,
 *          long-desc*,
 *          (email | ext-link | uri)*,
 *          (alternatives | chem-struct | code | graphic | media |
 *           preformat | textual-form)*,
 *          (attrib | permissions)*
 *      )>
 *
 * DTD: <!ATTLIST chem-struct-wrap
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          position (anchor | background | float | margin) "float"
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/chem-struct-wrap.html
 *
 * Example:
 * <chem-struct-wrap id="chem1" position="float">
 *     <label>Compound 1</label>
 *     <caption><p>Chemical structure of aspirin</p></caption>
 *     <chem-struct>
 *         <graphic xlink:href="aspirin.png"/>
 *     </chem-struct>
 * </chem-struct-wrap>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChemStructWrap {

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
     * Example: "compound", "reaction", "pathway"
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
     *
     * Example: "chem1", "compound-1"
     */
    private String id;

    /**
     * 방향 / Orientation
     *
     * KR: 콘텐츠의 표시 방향.
     * EN: Display orientation of the content.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     */
    private Orientation orientation;

    /**
     * 위치 / Position
     *
     * KR: 콘텐츠의 배치 위치.
     * EN: Placement position of the content.
     *
     * DTD: position (anchor | background | float | margin) "float"
     * Required: NO
     * Default: "float"
     */
    @Builder.Default
    private Position position = Position.FLOAT;

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

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 객체 ID 목록 / Object ID list
     *
     * KR: 화학 구조의 대체 식별자 목록.
     * EN: List of alternative identifiers for the chemical structure.
     *
     * DTD: (object-id)*
     * Required: NO (0 or more)
     */
    private List<ObjectId> objectIds;

    /**
     * 레이블 / Label
     *
     * KR: 화학 구조 레이블 (예: "Compound 1", "Structure A").
     * EN: Label for the chemical structure (e.g., "Compound 1", "Structure A").
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 캡션 목록 / Caption list
     *
     * KR: 화학 구조에 대한 캡션 목록.
     * EN: List of captions for the chemical structure.
     *
     * DTD: (caption)*
     * Required: NO (0 or more)
     */
    private List<Caption> captions;

    /**
     * 초록 목록 / Abstract list
     *
     * KR: 화학 구조에 대한 초록/요약 목록.
     * EN: List of abstracts/summaries for the chemical structure.
     *
     * DTD: abstract*
     * Required: NO (0 or more)
     */
    private List<PmcAbstract> abstracts;

    /**
     * 키워드 그룹 목록 / Keyword group list
     *
     * KR: 화학 구조 관련 키워드 그룹 목록.
     * EN: List of keyword groups related to the chemical structure.
     *
     * DTD: kwd-group*
     * Required: NO (0 or more)
     */
    private List<KwdGroup> kwdGroups;

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
     * 이메일 목록 / Email list
     *
     * KR: 관련 이메일 주소 목록.
     * EN: List of related email addresses.
     *
     * DTD: email*
     * Required: NO (0 or more)
     */
    private List<Email> emails;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 관련 외부 링크 목록.
     * EN: List of external links.
     *
     * DTD: ext-link*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     *
     * KR: 관련 URI 목록.
     * EN: List of URIs.
     *
     * DTD: uri*
     * Required: NO (0 or more)
     */
    private List<Uri> uris;

    // ========== Display Content / 표시 콘텐츠 ==========

    /**
     * 대안 목록 / Alternatives list
     *
     * KR: 다양한 포맷의 동일 콘텐츠 대안 목록.
     * EN: List of alternative representations of the same content.
     *
     * DTD: alternatives*
     * Required: NO (0 or more)
     */
    private List<Alternatives> alternatives;

    /**
     * 화학 구조 목록 / Chemical structure list
     *
     * KR: 화학 구조 요소 목록.
     * EN: List of chemical structure elements.
     *
     * DTD: chem-struct*
     * Required: NO (0 or more)
     */
    private List<ChemStruct> chemStructs;

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
     * 그래픽 목록 / Graphic list
     *
     * KR: 이미지/그래픽 목록.
     * EN: List of images/graphics.
     *
     * DTD: graphic*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 미디어 목록.
     * EN: List of media.
     *
     * DTD: media*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * Preformat 목록 / Preformat list
     *
     * KR: 미리 형식화된 텍스트 목록.
     * EN: List of preformatted text.
     *
     * DTD: preformat*
     * Required: NO (0 or more)
     */
    private List<Preformat> preformats;

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

    // ========== Attribution / 속성 정보 ==========

    /**
     * 속성 정보 목록 / Attribution list
     *
     * KR: 속성/출처 정보 목록.
     * EN: List of attributions/source information.
     *
     * DTD: attrib*
     * Required: NO (0 or more)
     */
    private List<Attrib> attribs;

    /**
     * 권한 정보 목록 / Permissions list
     *
     * KR: 저작권 및 라이선스 정보 목록.
     * EN: List of copyright and license information.
     *
     * DTD: permissions*
     * Required: NO (0 or more)
     */
    private List<Permissions> permissions;
}
