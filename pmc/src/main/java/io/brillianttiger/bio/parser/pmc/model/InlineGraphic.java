package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * InlineGraphic / 인라인 그래픽
 *
 * KR: 텍스트 내 인라인 그래픽/이미지. JATS 1.4 DTD 완전 준수 모델.
 * EN: Inline graphic/image within text. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT inline-graphic (alt-text | long-desc)*>
 *
 * DTD: <!ATTLIST inline-graphic
 *          baseline-shift CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          mime-subtype CDATA #IMPLIED
 *          mimetype CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *          xlink:href CDATA #REQUIRED
 *          xlink:role CDATA #IMPLIED
 *          xlink:show (embed | new | none | other | replace) #IMPLIED
 *          xlink:title CDATA #IMPLIED
 *          xlink:type (simple) #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/inline-graphic.html
 *
 * Note: Unlike <graphic>, <inline-graphic> is used for small images
 * that appear within running text, such as symbols, small icons, or
 * special characters not available in the font.
 *
 * Example:
 * <p>The reaction proceeds via <inline-graphic xlink:href="arrow.png"/> to the product.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InlineGraphic {

    // ========== Attributes / 속성 ==========

    /**
     * 기준선 이동 / Baseline shift
     *
     * KR: 기준선 대비 이미지 위치 조정.
     * EN: Adjustment of image position relative to baseline.
     *
     * DTD: baseline-shift CDATA #IMPLIED
     * Required: NO
     */
    private String baselineShift;

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 인라인 그래픽의 콘텐츠 유형.
     * EN: Content type of the inline graphic.
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
     */
    private String mimetype;

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
     * KR: 인라인 그래픽 파일 경로 또는 URL.
     * EN: Path or URL to the inline graphic file.
     *
     * DTD: xlink:href CDATA #REQUIRED
     * Required: YES
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
}
