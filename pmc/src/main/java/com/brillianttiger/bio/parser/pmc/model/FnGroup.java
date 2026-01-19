package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FnGroup / 각주 그룹
 *
 * KR: 각주를 묶는 그룹 컨테이너. JATS 1.4 DTD 완전 준수 모델.
 * EN: Container for grouping footnotes. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT fn-group (label?, title?, (fn)+)>
 *
 * DTD: <!ATTLIST fn-group
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/fn-group.html
 *
 * Example:
 * <fn-group>
 *   <title>Author Notes</title>
 *   <fn fn-type="corresp" id="cor1">
 *     <label>*</label>
 *     <p>Corresponding author: john@example.com</p>
 *   </fn>
 *   <fn fn-type="equal" id="fn1">
 *     <label>†</label>
 *     <p>These authors contributed equally to this work.</p>
 *   </fn>
 * </fn-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FnGroup {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 각주 그룹의 콘텐츠 유형.
     * EN: Type of content in the footnote group.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "author-notes", "conflict", "funding"
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
     * KR: 각주 그룹의 레이블.
     * EN: Label for the footnote group.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 각주 그룹의 제목 (예: "Author Notes", "Footnotes").
     * EN: Title of the footnote group (e.g., "Author Notes", "Footnotes").
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 각주 목록 / Footnote list
     *
     * KR: 각주 목록 (최소 1개 필요).
     * EN: List of footnotes (at least one required).
     *
     * DTD: (fn)+
     * Required: YES (1 or more)
     */
    private List<Fn> footnotes;
}
