package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Array / 배열
 *
 * KR: 테이블 형식의 배열/행렬. JATS 1.4 DTD 완전 준수 모델.
 * EN: Tabular array/matrix. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT array (
 *          (label)?,
 *          (alt-text | long-desc)*,
 *          (email | ext-link | uri)*,
 *          (alternatives | graphic | media | tbody)+,
 *          (attrib | permissions)*
 *      )>
 *
 * DTD: <!ATTLIST array
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/array.html
 *
 * Note: Array is used for simple tabular structures within text,
 * typically simpler than table-wrap. Often used for data matrices,
 * equations in matrix form, etc.
 *
 * Example:
 * <array>
 *     <tbody>
 *         <tr><td>a</td><td>b</td></tr>
 *         <tr><td>c</td><td>d</td></tr>
 *     </tbody>
 * </array>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Array {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 배열의 콘텐츠 유형.
     * EN: Content type of the array.
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
     * KR: 배열의 표시 방향.
     * EN: Display orientation of the array.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     */
    private Orientation orientation;

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
     * KR: 배열 내용의 언어 코드 (ISO 639).
     * EN: Language code for array content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 레이블 / Label
     *
     * KR: 배열의 레이블.
     * EN: Label of the array.
     *
     * DTD: label?
     * Required: NO (0 or 1)
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
     * 이메일 목록 / Email list
     *
     * KR: 관련 이메일 목록.
     * EN: List of related emails.
     */
    private List<Email> emails;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 외부 링크 목록.
     * EN: List of external links.
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     *
     * KR: URI 목록.
     * EN: List of URIs.
     */
    private List<Uri> uris;

    /**
     * 대안 목록 / Alternatives list
     *
     * KR: 동일 콘텐츠의 대안 표현 목록.
     * EN: List of alternative representations of the same content.
     */
    private List<Alternatives> alternatives;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 그래픽 표현 목록.
     * EN: List of graphic representations.
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 미디어 표현 목록.
     * EN: List of media representations.
     */
    private List<Media> medias;

    /**
     * 테이블 바디 목록 / Table body list
     *
     * KR: 배열 데이터를 담는 테이블 바디 목록.
     * EN: List of table bodies containing array data.
     *
     * DTD: tbody+
     * Required: YES (1 or more)
     */
    private List<Tbody> tbodies;

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
