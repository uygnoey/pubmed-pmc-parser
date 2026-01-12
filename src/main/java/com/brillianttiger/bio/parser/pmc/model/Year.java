package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Year / 연도
 *
 * KR: 출판 연도. JATS 1.4 DTD 완전 준수 모델.
 * EN: Publication year. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT year (#PCDATA)>
 *
 * DTD: <!ATTLIST year
 *          calendar CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          iso-8601-date CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/year.html
 *
 * Example:
 * <year iso-8601-date="2023">2023</year>
 * <year iso-8601-date="2023-06">2023</year>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Year {

    // ========== Attributes / 속성 ==========

    /**
     * 달력 유형 / Calendar type
     *
     * KR: 달력 시스템 (예: gregorian, julian).
     * EN: Calendar system (e.g., gregorian, julian).
     *
     * DTD: calendar CDATA #IMPLIED
     * Required: NO
     */
    private String calendar;

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 연도의 콘텐츠 유형.
     * EN: Type of content for the year.
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
     * ISO 8601 날짜 / ISO 8601 date
     *
     * KR: ISO 8601 형식의 날짜 (예: "2023", "2023-06", "2023-06-15").
     * EN: Date in ISO 8601 format (e.g., "2023", "2023-06", "2023-06-15").
     *
     * DTD: iso-8601-date CDATA #IMPLIED
     * Required: NO
     *
     * Note: This attribute provides a machine-readable version of the date.
     */
    private String iso8601Date;

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
     * 연도 값 / Year value
     *
     * KR: 연도 텍스트 (예: "2023", "1999").
     * EN: Year text (e.g., "2023", "1999").
     *
     * DTD: #PCDATA
     * Required: YES
     */
    private String value;
}
