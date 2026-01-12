package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Ack / 감사의 글
 *
 * KR: 감사의 글 (연구 지원, 기여자 감사 등). JATS 1.4 DTD 완전 준수 모델.
 * EN: Acknowledgments (research support, contributor thanks, etc.). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT ack (label?, title?, subtitle*, alt-title*,
 *                     (%sec-opt-title-model;)*)>
 *
 * DTD: <!ATTLIST ack
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          rid IDREFS #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/ack.html
 *
 * Note: %sec-opt-title-model; contains the content typically found in sections:
 * paragraphs, lists, figures, tables, etc.
 *
 * Example:
 * <ack>
 *   <title>Acknowledgments</title>
 *   <p>We thank the funding agencies and collaborators.</p>
 *   <p>Special thanks to Dr. Kim for reviewing the manuscript.</p>
 * </ack>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ack {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 감사의 글의 콘텐츠 유형.
     * EN: Type of acknowledgment content.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "funding", "contributors", "general"
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
     * 참조 ID 목록 / Reference ID list
     *
     * KR: 이 감사의 글이 참조하는 요소들의 ID 목록.
     * EN: List of IDs of elements referenced by this acknowledgment.
     *
     * DTD: rid IDREFS #IMPLIED
     * Required: NO
     */
    private String rid;

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
     * KR: 감사의 글 레이블.
     * EN: Label for the acknowledgment.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 감사의 글 제목.
     * EN: Title of the acknowledgment.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 부제목 목록 / Subtitle list
     *
     * KR: 감사의 글 부제목 목록.
     * EN: List of subtitles for the acknowledgment.
     *
     * DTD: subtitle*
     * Required: NO (0 or more)
     */
    private List<Subtitle> subtitles;

    /**
     * 대체 제목 목록 / Alternative title list
     *
     * KR: 대체 제목 목록 (다른 언어, 축약형 등).
     * EN: List of alternative titles (different languages, abbreviated, etc.).
     *
     * DTD: alt-title*
     * Required: NO (0 or more)
     */
    private List<AltTitle> altTitles;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 감사의 글 내용 단락 목록.
     * EN: List of paragraphs in the acknowledgment.
     *
     * DTD: (%sec-opt-title-model;)* - includes p
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 하위 섹션 목록.
     * EN: List of subsections.
     *
     * DTD: (%sec-opt-title-model;)* - includes sec
     * Required: NO (0 or more)
     */
    private List<Sec> sections;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 그래픽/이미지 목록.
     * EN: List of graphics/images.
     *
     * DTD: (%sec-opt-title-model;)* - includes graphic
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 미디어 객체 목록.
     * EN: List of media objects.
     *
     * DTD: (%sec-opt-title-model;)* - includes media
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 각주 그룹 목록 / Footnote group list
     *
     * KR: 각주 그룹 목록.
     * EN: List of footnote groups.
     *
     * DTD: (%sec-opt-title-model;)* - includes fn-group
     * Required: NO (0 or more)
     */
    private List<FnGroup> fnGroups;
}
