package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Source / 출처
 *
 * KR: 인용 출처 (저널명, 책명 등). JATS 1.4 DTD 완전 준수 모델.
 * EN: Citation source (journal name, book name, etc.). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT source (#PCDATA | %source-elements;)*>
 *
 * DTD: <!ATTLIST source
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/source.html
 *
 * Example:
 * <source>Journal of Biological Chemistry</source>
 * <source xml:lang="de">Zeitschrift für Physik</source>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Source {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 출처의 콘텐츠 유형.
     * EN: Type of content in the source.
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
     * 출처 값 / Source value
     *
     * KR: 출처명 텍스트 (저널명, 책명 등).
     * EN: Source name text (journal name, book name, etc.).
     *
     * DTD: #PCDATA
     * Required: YES
     */
    private String value;
}
