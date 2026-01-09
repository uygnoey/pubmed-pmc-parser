package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Fig / 그림
 *
 * KR: 논문 그림. JATS 1.4 DTD 완전 준수 모델.
 * EN: Article figure. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT fig (
 *          (object-id)*,
 *          label?,
 *          (caption)*,
 *          abstract*,
 *          kwd-group*,
 *          alt-text*,
 *          long-desc*,
 *          (email | ext-link | uri)*,
 *          (alternatives | disp-formula | disp-formula-group |
 *           chem-struct-wrap | disp-quote | speech | statement |
 *           verse-group | table-wrap | p | def-list | list |
 *           array | code | graphic | media | preformat)*,
 *          (attrib | permissions)*
 *      )>
 *
 * DTD: <!ATTLIST fig
 *          fig-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          position (anchor | background | float | margin) "float"
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/fig.html
 *
 * Example:
 * <fig id="fig1" fig-type="chart" position="float" orientation="portrait">
 *     <label>Figure 1</label>
 *     <caption><title>Study results</title><p>...</p></caption>
 *     <graphic xlink:href="fig1.jpg"/>
 * </fig>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fig {

    // ========== Attributes / 속성 ==========

    /**
     * 그림 유형 / Figure type
     *
     * KR: 그림의 유형 (chart, map, scheme 등).
     * EN: Type of figure (chart, map, scheme, etc.).
     *
     * DTD: fig-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values: map, chart, scheme, drawing, photo, illustration
     */
    private FigType figType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: 고유 식별자. xref 등에서 참조에 사용.
     * EN: Unique identifier. Used for references from xref, etc.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     *
     * Example: "fig1", "F01"
     */
    private String id;

    /**
     * 방향 / Orientation
     *
     * KR: 그림의 표시 방향.
     * EN: Display orientation of the figure.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     */
    private Orientation orientation;

    /**
     * 위치 / Position
     *
     * KR: 그림의 배치 위치.
     * EN: Placement position of the figure.
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
     * XML 언어 / XML language
     *
     * KR: 그림 내용의 언어 코드 (ISO 639).
     * EN: Language code for figure content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "ko", "ja"
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 객체 ID 목록 / Object ID list
     *
     * KR: 그림의 대체 식별자 목록.
     * EN: List of alternative identifiers for the figure.
     *
     * DTD: (object-id)*
     * Required: NO (0 or more)
     */
    private List<ObjectId> objectIds;

    /**
     * 레이블 / Label
     *
     * KR: 그림의 레이블 (예: "Figure 1", "그림 1").
     * EN: Label of the figure (e.g., "Figure 1").
     *
     * DTD: label?
     * Required: NO (0 or 1)
     */
    private Label label;

    /**
     * 캡션 목록 / Caption list
     *
     * KR: 그림 캡션 목록 (일반적으로 1개).
     * EN: List of figure captions (usually 1).
     *
     * DTD: (caption)*
     * Required: NO (0 or more)
     */
    private List<Caption> captions;

    /**
     * 초록 목록 / Abstract list
     *
     * KR: 그림에 대한 초록/요약 목록.
     * EN: List of abstracts/summaries for the figure.
     *
     * DTD: abstract*
     * Required: NO (0 or more)
     */
    private List<Abstract> abstracts;

    /**
     * 키워드 그룹 목록 / Keyword group list
     *
     * KR: 그림과 관련된 키워드 그룹 목록.
     * EN: List of keyword groups related to the figure.
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
     * KR: 관련 이메일 목록.
     * EN: List of related emails.
     *
     * DTD: (email | ext-link | uri)*
     * Required: NO (0 or more)
     */
    private List<Email> emails;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 외부 링크 목록.
     * EN: List of external links.
     *
     * DTD: (email | ext-link | uri)*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     *
     * KR: URI 목록.
     * EN: List of URIs.
     *
     * DTD: (email | ext-link | uri)*
     * Required: NO (0 or more)
     */
    private List<Uri> uris;

    /**
     * 대안 목록 / Alternatives list
     *
     * KR: 동일 콘텐츠의 대안 표현 목록.
     * EN: List of alternative representations of the same content.
     *
     * DTD: alternatives*
     * Required: NO (0 or more)
     */
    private List<Alternatives> alternatives;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 그림 이미지 파일 목록.
     * EN: List of graphic image files.
     *
     * DTD: graphic*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 비디오, 오디오 등 미디어 파일 목록.
     * EN: List of media files (video, audio, etc.).
     *
     * DTD: media*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 테이블 래퍼 목록 / Table wrap list
     *
     * KR: 그림 내 테이블 목록.
     * EN: List of tables within the figure.
     *
     * DTD: table-wrap*
     * Required: NO (0 or more)
     */
    private List<TableWrap> tableWraps;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 그림 내 텍스트 단락 목록.
     * EN: List of text paragraphs within the figure.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 정의 목록 목록 / Definition list list
     *
     * KR: 그림 내 정의 목록들.
     * EN: List of definition lists within the figure.
     *
     * DTD: def-list*
     * Required: NO (0 or more)
     */
    private List<DefList> defLists;

    /**
     * 코드 목록 / Code list
     *
     * KR: 그림 내 코드 블록들.
     * EN: Code blocks within the figure.
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codes;

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
