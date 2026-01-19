package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CompoundKwdPart / 복합 키워드 부분
 *
 * KR: 복합 키워드의 개별 구성 부분. JATS 1.4 완전 준수 모델.
 * EN: Individual part of a compound keyword. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT compound-kwd-part (#PCDATA | %all-phrase;)*>
 *
 * DTD: <!ATTLIST compound-kwd-part
 *          content-type CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/compound-kwd-part.html
 *
 * Note: Used to represent individual components of a compound keyword.
 * Example: A compound keyword "Diabetes Mellitus Type 2" might have parts:
 * - Part 1 (disease): "Diabetes Mellitus"
 * - Part 2 (type): "Type 2"
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompoundKwdPart {

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 키워드 부분의 콘텐츠 유형 (선택적).
     * EN: Content type of keyword part (optional).
     *
     * DTD: content-type CDATA #IMPLIED
     *
     * Examples:
     * - "term": Main term (주요 용어)
     * - "code": Classification code (분류 코드)
     * - "modifier": Modifier term (수식어)
     * - "heading": Subject heading (주제명)
     * - "subheading": Subheading (부제목)
     */
    private String contentType;

    /**
     * 키워드 부분 내용 / Keyword part content
     *
     * KR: 키워드 부분의 텍스트 내용.
     * EN: Text content of keyword part.
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Can contain mixed content including inline elements.
     */
    private String value;
}
