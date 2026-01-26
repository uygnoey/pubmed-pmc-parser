package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AuthorNotes / 저자 노트
 *
 * KR: 저자 노트 (교신저자 정보, 각주, 기여 정보 등). JATS 1.4 DTD 완전 준수 모델.
 * EN: Author notes (corresponding author info, footnotes, contribution info, etc.). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT author-notes (label?, title?, (%address-link.class; | corresp | fn | p)*)>
 *
 * DTD: <!ATTLIST author-notes
 *          id ID #IMPLIED
 *          rid IDREFS #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/author-notes.html
 *
 * Example:
 * <author-notes>
 *   <corresp id="cor1">
 *     <label>*</label>
 *     Corresponding author: <email>john.doe@university.edu</email>
 *   </corresp>
 *   <fn fn-type="equal" id="fn1">
 *     <label>†</label>
 *     <p>These authors contributed equally to this work.</p>
 *   </fn>
 *   <fn fn-type="present-address" id="fn2">
 *     <label>‡</label>
 *     <p>Present address: Department of Biology, Stanford University</p>
 *   </fn>
 * </author-notes>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorNotes {

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
     * 참조 ID 목록 / Reference ID list
     *
     * KR: 이 저자 노트가 참조하는 요소들의 ID 목록.
     * EN: List of IDs of elements referenced by this author notes.
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
     * KR: 저자 노트의 레이블.
     * EN: Label for the author notes.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 저자 노트의 제목.
     * EN: Title of the author notes.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 교신저자 정보 목록 / Corresponding author list
     *
     * KR: 교신저자 정보 목록.
     * EN: List of corresponding author information.
     *
     * DTD: corresp*
     * Required: NO (0 or more)
     */
    private List<Corresp> corresps;

    /**
     * 각주 목록 / Footnote list
     *
     * KR: 저자 관련 각주 목록.
     * EN: List of author-related footnotes.
     *
     * DTD: fn*
     * Required: NO (0 or more)
     */
    private List<Fn> footnotes;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 저자 노트 내용 단락 목록.
     * EN: List of paragraphs in the author notes.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 저자 관련 외부 링크 목록.
     * EN: List of external links related to authors.
     *
     * DTD: %address-link.class;*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;
}
