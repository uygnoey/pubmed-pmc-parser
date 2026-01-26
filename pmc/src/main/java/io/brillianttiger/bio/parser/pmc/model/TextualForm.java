package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TextualForm / 텍스트 형식
 *
 * KR: 대안 표현의 텍스트 형식. JATS 1.4 DTD 완전 준수 모델.
 * EN: Textual form of alternative representation. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT textual-form (#PCDATA | %all-phrase;)*>
 *
 * DTD: <!ATTLIST textual-form
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/textual-form.html
 *
 * Note: Used within <alternatives> to provide a plain-text or
 * simple-text alternative to complex content like equations,
 * graphics, or tables. Often used for accessibility purposes
 * or for display in text-only environments.
 *
 * Example:
 * <alternatives>
 *     <mml:math>...</mml:math>
 *     <textual-form>x = (-b +/- sqrt(b^2 - 4ac)) / 2a</textual-form>
 * </alternatives>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextualForm {

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
     *
     * Common values: "accessibility", "print", "screen-reader"
     */
    private String specificUse;

    /**
     * XML 언어 / XML language
     *
     * KR: 텍스트 내용의 언어 코드 (ISO 639).
     * EN: Language code for text content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    // ========== Content / 내용 ==========

    /**
     * 텍스트 내용 / Text content
     *
     * KR: 대안 텍스트 표현.
     * EN: Alternative text representation.
     *
     * DTD: (#PCDATA | %all-phrase;)*
     */
    private String content;
}
