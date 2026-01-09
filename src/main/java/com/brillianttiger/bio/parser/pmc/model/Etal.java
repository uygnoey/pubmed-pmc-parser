package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Etal / Et al.
 *
 * KR: "et al." (et alii, 그 외 다른 사람들) 표시. JATS 1.4 DTD 완전 준수 모델.
 *     인용에서 모든 저자를 나열하지 않았음을 나타냄.
 * EN: "Et al." (et alii, and others) indicator. Fully compliant with JATS 1.4 DTD.
 *     Indicates that not all authors have been listed in a citation.
 *
 * DTD: <!ELEMENT etal (#PCDATA)>
 *
 * DTD: <!ATTLIST etal
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/etal.html
 *
 * Example:
 * <etal/>
 * <etal>et al.</etal>
 * <etal>and others</etal>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Etal {

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

    // ========== Content / 내용 ==========

    /**
     * Et al. 값 / Et al. value
     *
     * KR: 텍스트 값 (기본: 빈 문자열, "et al.", "and others" 등).
     * EN: Text value (default: empty string, "et al.", "and others", etc.).
     *
     * DTD: #PCDATA
     * Required: NO (can be empty element)
     */
    private String value;
}
