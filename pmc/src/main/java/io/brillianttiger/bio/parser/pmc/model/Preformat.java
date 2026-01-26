package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Preformat / 미리 형식화된 텍스트
 *
 * KR: 미리 형식화된 텍스트 (공백, 줄바꿈 유지). JATS 1.4 DTD 완전 준수 모델.
 * EN: Preformatted text (preserves whitespace and line breaks). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT preformat (#PCDATA | %all-phrase; | %access.class; | %address-link.class;)*>
 *
 * DTD: <!ATTLIST preformat
 *          id ID #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          position (anchor | background | float | margin) #IMPLIED
 *          preformat-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *          xml:space (default | preserve) "preserve"
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/preformat.html
 *
 * Example:
 * <preformat preformat-type="ascii-art" xml:space="preserve">
 *    +---+---+
 *    | A | B |
 *    +---+---+
 * </preformat>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preformat {

    // ========== Attributes / 속성 ==========

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
     * KR: 표시 방향.
     * EN: Display orientation.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     */
    private Orientation orientation;

    /**
     * 위치 / Position
     *
     * KR: 배치 위치.
     * EN: Placement position.
     *
     * DTD: position (anchor | background | float | margin) #IMPLIED
     * Required: NO
     */
    private Position position;

    /**
     * Preformat 유형 / Preformat type
     *
     * KR: 미리 형식화된 텍스트의 유형.
     * EN: Type of preformatted text.
     *
     * DTD: preformat-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values: ascii-art, code, program-listing
     */
    private String preformatType;

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
     * KR: 내용의 언어 코드 (ISO 639).
     * EN: Language code for content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    /**
     * XML 공백 처리 / XML space handling
     *
     * KR: 공백 처리 방식.
     * EN: Whitespace handling mode.
     *
     * DTD: xml:space (default | preserve) "preserve"
     * Required: NO
     * Default: "preserve"
     */
    @Builder.Default
    private String xmlSpace = "preserve";

    // ========== Content / 내용 ==========

    /**
     * 텍스트 내용 / Text content
     *
     * KR: 미리 형식화된 텍스트 내용.
     * EN: Preformatted text content.
     *
     * DTD: (#PCDATA | %all-phrase; | ...)*
     */
    private String content;

    /**
     * 속성 정보 목록 / Attribution list
     *
     * KR: 속성/출처 정보 목록.
     * EN: List of attributions/source information.
     */
    private List<Attrib> attribs;

    /**
     * 권한 정보 목록 / Permissions list
     *
     * KR: 저작권 및 라이선스 정보 목록.
     * EN: List of copyright and license information.
     */
    private List<Permissions> permissions;
}
