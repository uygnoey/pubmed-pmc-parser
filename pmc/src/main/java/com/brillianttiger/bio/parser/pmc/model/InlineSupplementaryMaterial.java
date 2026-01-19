package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * InlineSupplementaryMaterial / 인라인 보충 자료
 *
 * KR: 텍스트 내에 인라인으로 표시되는 보충 자료 참조. JATS 1.4 DTD 완전 준수 모델.
 *     본문 텍스트 중간에 보충 자료에 대한 링크나 참조를 포함.
 * EN: Inline supplementary material reference within text. Fully compliant with JATS 1.4 DTD.
 *     Contains links or references to supplementary materials within body text.
 *
 * DTD: <!ELEMENT inline-supplementary-material (#PCDATA | %inline-display-noalt.class; |
 *          %inline-math.class; | %simple-link.class; | %simple-text.class;)*>
 *
 * DTD: <!ATTLIST inline-supplementary-material
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          mime-subtype CDATA #IMPLIED
 *          mimetype CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          vocab CDATA #IMPLIED
 *          vocab-identifier CDATA #IMPLIED
 *          vocab-term CDATA #IMPLIED
 *          vocab-term-identifier CDATA #IMPLIED
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
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/inline-supplementary-material.html
 *
 * Example:
 * <p>See the <inline-supplementary-material xlink:href="video1.mp4"
 *       mimetype="video" mime-subtype="mp4">supplementary video</inline-supplementary-material>
 *    for more details.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InlineSupplementaryMaterial {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 보충 자료의 콘텐츠 유형.
     * EN: Content type of the supplementary material.
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
     * KR: 파일의 MIME 서브타입 (예: mp4, csv).
     * EN: File's MIME subtype (e.g., mp4, csv).
     *
     * DTD: mime-subtype CDATA #IMPLIED
     * Required: NO
     */
    private String mimeSubtype;

    /**
     * MIME 타입 / MIME type
     *
     * KR: 파일의 MIME 타입 (예: video, application).
     * EN: File's MIME type (e.g., video, application).
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
     * 어휘/용어집 / Vocabulary
     *
     * KR: 사용된 어휘/용어집 이름.
     * EN: Name of the vocabulary/terminology used.
     *
     * DTD: vocab CDATA #IMPLIED
     * Required: NO
     */
    private String vocab;

    /**
     * 어휘 식별자 / Vocabulary identifier
     *
     * KR: 어휘/용어집의 고유 식별자.
     * EN: Unique identifier of the vocabulary/terminology.
     *
     * DTD: vocab-identifier CDATA #IMPLIED
     * Required: NO
     */
    private String vocabIdentifier;

    /**
     * 어휘 용어 / Vocabulary term
     *
     * KR: 어휘에서 사용된 특정 용어.
     * EN: Specific term used from the vocabulary.
     *
     * DTD: vocab-term CDATA #IMPLIED
     * Required: NO
     */
    private String vocabTerm;

    /**
     * 어휘 용어 식별자 / Vocabulary term identifier
     *
     * KR: 어휘 용어의 고유 식별자.
     * EN: Unique identifier of the vocabulary term.
     *
     * DTD: vocab-term-identifier CDATA #IMPLIED
     * Required: NO
     */
    private String vocabTermIdentifier;

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
     * KR: 보충 자료 파일 경로 또는 URL.
     * EN: Path or URL to the supplementary material file.
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
     * KR: 인라인 보충 자료의 표시 텍스트 (혼합 콘텐츠).
     * EN: Display text of the inline supplementary material (mixed content).
     *
     * DTD: #PCDATA (mixed content)
     * Required: NO
     *
     * Example: "supplementary video", "data file S1"
     */
    private String value;

    /**
     * 대체 목록 / Alternatives list
     *
     * KR: 다양한 포맷의 동일 콘텐츠 대안.
     * EN: Alternative representations of the same content.
     *
     * DTD: alternatives*
     * Required: NO (0 or more)
     */
    private List<Alternatives> alternatives;

    /**
     * 인라인 그래픽 목록 / Inline graphic list
     *
     * KR: 인라인 그래픽 요소 목록.
     * EN: List of inline graphic elements.
     *
     * DTD: inline-graphic*
     * Required: NO (0 or more)
     */
    private List<InlineGraphic> inlineGraphics;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 외부 링크 목록.
     * EN: List of external links.
     *
     * DTD: ext-link*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     *
     * KR: URI 목록.
     * EN: List of URIs.
     *
     * DTD: uri*
     * Required: NO (0 or more)
     */
    private List<Uri> uris;
}
