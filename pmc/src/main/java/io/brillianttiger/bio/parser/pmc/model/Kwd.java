package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Kwd / 키워드
 *
 * KR: 논문 키워드. JATS 1.4 완전 준수 모델.
 * EN: Article keyword. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT kwd (#PCDATA | %all-phrase;)*>
 *
 * DTD: <!ATTLIST kwd
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/kwd.html
 *
 * Note: Can contain mixed content with inline elements from %all-phrase;
 * such as bold, italic, subscript, superscript, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kwd {

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 키워드의 콘텐츠 유형 (선택적).
     * EN: Content type of keyword (optional).
     *
     * DTD: content-type CDATA #IMPLIED
     *
     * Examples: "primary", "secondary", "geographical"
     */
    private String contentType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: 키워드의 고유 ID (문서 내 참조용).
     * EN: Unique ID of the keyword (for in-document reference).
     *
     * DTD: id ID #IMPLIED
     */
    private String id;

    /**
     * 키워드 내용 / Keyword content
     *
     * KR: 키워드 텍스트 내용.
     * EN: Keyword text content.
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Can contain mixed content including inline elements.
     */
    private String value;
}
