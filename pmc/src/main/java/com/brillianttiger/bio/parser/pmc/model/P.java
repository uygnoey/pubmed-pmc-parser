package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * P / 문단
 *
 * KR: 문단 요소. JATS 1.4 완전 준수 모델.
 * EN: Paragraph element. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT p (#PCDATA | %all-phrase; | %block-elements;)*>
 *      <!ATTLIST p
 *          %jats-common-atts;
 *          content-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/p.html
 *
 * Note: Paragraphs support mixed content including text, inline elements
 * (bold, italic, links, etc.), and block-level elements (tables, figures, etc.).
 * The content model is very flexible to accommodate various publishing needs.
 *
 * Example:
 * <p id="p1">This is a <bold>paragraph</bold> with <italic>mixed</italic> content.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class P {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 문단의 고유 식별자.
     * EN: Unique identifier for this paragraph.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 콘텐츠 타입 / Content type
     *
     * KR: 문단의 콘텐츠 유형 또는 역할.
     * EN: Type or role of the paragraph content.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "disclaimer", "ethics", "data-availability"
     */
    private String contentType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 문단의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this paragraph.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 언어 / Language
     *
     * KR: 이 문단의 언어 (ISO 639 코드).
     * EN: Language of this paragraph (ISO 639 code).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "ko", "ja", "zh"
     */
    private String xmlLang;

    /**
     * 텍스트 콘텐츠 / Text content
     *
     * KR: 문단의 텍스트 콘텐츠 (mixed content).
     * EN: Paragraph text content (mixed content).
     *
     * DTD: #PCDATA | %all-phrase; | %block-elements;
     * Required: NO (can be empty)
     *
     * Note: This field contains the paragraph text. In a full implementation,
     * this would be a complex structure supporting inline formatting elements
     * (bold, italic, xref, etc.) and nested block elements. For simplicity,
     * we store the text content here.
     */
    private String value;
}
