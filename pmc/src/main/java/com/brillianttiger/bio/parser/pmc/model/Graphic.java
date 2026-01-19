package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Graphic / 그래픽
 *
 * KR: 그래픽/이미지 요소. JATS 1.4 DTD 완전 준수 모델.
 * EN: Graphic/image element. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT graphic (
 *          (alt-text | long-desc)*,
 *          (abstract)*,
 *          (attrib)*,
 *          (permissions)*
 *      )>
 *
 * DTD: <!ATTLIST graphic
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          mime-subtype CDATA #IMPLIED
 *          mimetype CDATA #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          position (anchor | background | float | margin) #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *          xlink:href CDATA #REQUIRED
 *          xlink:role CDATA #IMPLIED
 *          xlink:show (embed | new | none | other | replace) #IMPLIED
 *          xlink:title CDATA #IMPLIED
 *          xlink:type (simple) #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/graphic.html
 *
 * Example:
 * <graphic xlink:href="figure1.jpg" mimetype="image" mime-subtype="jpeg"
 *          orientation="portrait" position="float">
 *     <alt-text>Graph showing patient recovery rates</alt-text>
 * </graphic>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Graphic {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 그래픽의 콘텐츠 유형.
     * EN: Content type of the graphic.
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
     * MIME 서브타입 / MIME subtype
     *
     * KR: 미디어 서브타입 (예: jpeg, png, gif).
     * EN: Media subtype (e.g., jpeg, png, gif).
     *
     * DTD: mime-subtype CDATA #IMPLIED
     * Required: NO
     *
     * Common values: jpeg, png, gif, tiff, svg+xml
     */
    private String mimeSubtype;

    /**
     * MIME 타입 / MIME type
     *
     * KR: 미디어 타입 (예: image).
     * EN: Media type (e.g., image).
     *
     * DTD: mimetype CDATA #IMPLIED
     * Required: NO
     *
     * Common values: image, application
     */
    private String mimetype;

    /**
     * 방향 / Orientation
     *
     * KR: 그래픽의 표시 방향.
     * EN: Display orientation of the graphic.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     */
    private Orientation orientation;

    /**
     * 위치 / Position
     *
     * KR: 그래픽의 배치 위치.
     * EN: Placement position of the graphic.
     *
     * DTD: position (anchor | background | float | margin) #IMPLIED
     * Required: NO
     */
    private Position position;

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
     * XLink href (필수) / XLink href (required)
     *
     * KR: 그래픽 파일 경로 또는 URL.
     * EN: Path or URL to the graphic file.
     *
     * DTD: xlink:href CDATA #REQUIRED
     * Required: YES
     *
     * Example: "images/fig1.jpg", "https://example.com/fig.png"
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

    // ========== Child Elements / 자식 요소 ==========

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
     * 초록 목록 / Abstract list
     *
     * KR: 그래픽에 대한 초록/요약 목록.
     * EN: List of abstracts/summaries for the graphic.
     *
     * DTD: abstract*
     * Required: NO (0 or more)
     */
    private List<Abstract> abstracts;

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
