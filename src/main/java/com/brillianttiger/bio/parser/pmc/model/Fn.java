package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Fn / 각주
 *
 * KR: 개별 각주 요소. JATS 1.4 DTD 완전 준수 모델.
 * EN: Individual footnote element. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT fn (label?, (p)+)>
 *
 * DTD: <!ATTLIST fn
 *          fn-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          symbol CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/fn.html
 *
 * Common fn-type values:
 * - abbr: Abbreviation
 * - author: Author-related footnote
 * - con: Contributor
 * - conflict: Conflict of interest
 * - corresp: Corresponding author
 * - current-aff: Current affiliation
 * - deceased: Deceased author
 * - edited-by: Edited by
 * - equal: Equal contribution
 * - financial-disclosure: Financial disclosure
 * - on-leave: On leave
 * - participating-researchers: Participating researchers
 * - present-address: Present address
 * - presented-at: Presented at
 * - previously-at: Previously at
 * - study-group-members: Study group members
 * - supplementary-material: Supplementary material
 * - supported-by: Supported by
 * - other: Other
 *
 * Example:
 * <fn fn-type="corresp" id="cor1">
 *   <label>*</label>
 *   <p>Corresponding author: john.doe@university.edu</p>
 * </fn>
 *
 * <fn fn-type="equal" id="fn1">
 *   <label>†</label>
 *   <p>These authors contributed equally to this work.</p>
 * </fn>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fn {

    // ========== Attributes / 속성 ==========

    /**
     * 각주 유형 / Footnote type
     *
     * KR: 각주의 유형 (교신저자, 이해충돌 등).
     * EN: Type of footnote (corresponding author, conflict of interest, etc.).
     *
     * DTD: fn-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values: corresp, conflict, equal, deceased, present-address, etc.
     * See FnType enum for full list.
     */
    private FnType fnType;

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
     * 기호 / Symbol
     *
     * KR: 각주를 나타내는 기호 (레이블 대신 사용).
     * EN: Symbol representing the footnote (used instead of label).
     *
     * DTD: symbol CDATA #IMPLIED
     * Required: NO
     *
     * Example: "*", "†", "‡", "§"
     */
    private String symbol;

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
     * KR: 각주의 레이블 (예: "1", "a", "*").
     * EN: Label for the footnote (e.g., "1", "a", "*").
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 각주 내용 단락 목록 (최소 1개 필요).
     * EN: List of paragraphs in the footnote (at least one required).
     *
     * DTD: (p)+
     * Required: YES (1 or more)
     */
    private List<P> paragraphs;

    /**
     * 테이블 래퍼 목록 / Table wrapper list
     *
     * KR: 각주 내 테이블 목록.
     * EN: List of tables in the footnote.
     *
     * DTD: Allowed within p content
     * Required: NO (0 or more)
     */
    private List<TableWrap> tableWraps;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 각주 내 그래픽 목록.
     * EN: List of graphics in the footnote.
     *
     * DTD: Allowed within p content
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 각주 내 외부 링크 목록.
     * EN: List of external links in the footnote.
     *
     * DTD: Allowed within p content
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;
}
