package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FigGroup / 그림 그룹
 *
 * KR: 관련 그림들을 그룹화하는 컨테이너. JATS 1.4 DTD 완전 준수 모델.
 *     여러 패널로 구성된 그림이나 관련 이미지를 하나의 그룹으로 묶음.
 * EN: Container grouping related figures. Fully compliant with JATS 1.4 DTD.
 *     Groups multi-panel figures or related images together.
 *
 * DTD: <!ELEMENT fig-group (
 *          (object-id)*,
 *          label?,
 *          (caption)*,
 *          abstract*,
 *          kwd-group*,
 *          alt-text*,
 *          long-desc*,
 *          (email | ext-link | uri)*,
 *          (alternatives | block-alternatives | fig | graphic | media)*,
 *          (attrib | permissions)*
 *      )>
 *
 * DTD: <!ATTLIST fig-group
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          position (anchor | background | float | margin) "float"
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/fig-group.html
 *
 * Example:
 * <fig-group id="fg1">
 *     <label>Figure 1</label>
 *     <caption><title>Patient outcomes</title></caption>
 *     <fig id="fig1a"><label>A</label><graphic xlink:href="fig1a.jpg"/></fig>
 *     <fig id="fig1b"><label>B</label><graphic xlink:href="fig1b.jpg"/></fig>
 * </fig-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FigGroup {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 그림 그룹의 콘텐츠 유형.
     * EN: Content type of the figure group.
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
     * KR: 그림 그룹의 대체 식별자 목록.
     * EN: List of alternative identifiers for the figure group.
     *
     * DTD: (object-id)*
     * Required: NO (0 or more)
     */
    private List<ObjectId> objectIds;

    /**
     * 레이블 / Label
     *
     * KR: 그림 그룹 레이블 (예: "Figure 1").
     * EN: Label for the figure group (e.g., "Figure 1").
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 캡션 목록 / Caption list
     *
     * KR: 그림 그룹에 대한 캡션 목록.
     * EN: List of captions for the figure group.
     *
     * DTD: (caption)*
     * Required: NO (0 or more)
     */
    private List<Caption> captions;

    /**
     * 초록 목록 / Abstract list
     *
     * KR: 그림 그룹에 대한 초록/요약 목록.
     * EN: List of abstracts/summaries for the figure group.
     *
     * DTD: abstract*
     * Required: NO (0 or more)
     */
    private List<PmcAbstract> abstracts;

    /**
     * 키워드 그룹 목록 / Keyword group list
     *
     * KR: 그림 그룹 관련 키워드 그룹 목록.
     * EN: List of keyword groups related to the figure group.
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
     * 그림 목록 / Figure list
     *
     * KR: 그림 요소 목록.
     * EN: List of figure elements.
     *
     * DTD: fig*
     * Required: NO (0 or more)
     */
    private List<Fig> figs;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 그래픽 요소 목록.
     * EN: List of graphic elements.
     *
     * DTD: graphic*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 미디어 요소 목록.
     * EN: List of media elements.
     *
     * DTD: media*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

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
