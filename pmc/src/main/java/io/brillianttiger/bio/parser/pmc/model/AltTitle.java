package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AltTitle / 대체 제목
 *
 * KR: 논문의 대체 제목을 나타내는 요소.
 *     Mixed content (텍스트 + 인라인 요소)를 포함할 수 있음.
 *     약칭, 짧은 제목, 런닝 헤더 등 다양한 대체 제목 형식을 표현.
 * EN: Element representing alternative title of the article.
 *     Can contain mixed content (text + inline elements).
 *     Represents various alternative title formats such as abbreviation, short title, running header, etc.
 *
 * DTD: <!ELEMENT alt-title (#PCDATA | %all-phrase;)*>
 *
 * DTD: <!ATTLIST alt-title
 *          alt-title-type CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/alt-title.html
 *
 * Note: Mixed content (#PCDATA | %all-phrase;)* means text can be mixed with inline elements
 *       like <italic>, <bold>, <sub>, <sup>, etc.
 *       Currently represented as String for simplicity.
 *
 * Examples:
 * <alt-title alt-title-type="running-head">Gene Expression Study</alt-title>
 * <alt-title alt-title-type="short">Gene Study</alt-title>
 * <alt-title alt-title-type="abbreviated">GES</alt-title>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AltTitle {

    /**
     * 대체 제목 유형 / Alternative title type
     *
     * KR: 대체 제목의 유형 (running-head, short, abbreviated 등).
     * EN: Type of alternative title (running-head, short, abbreviated, etc.).
     *
     * DTD: alt-title-type CDATA #IMPLIED
     *
     * Common values:
     * - "running-head": 페이지 상단 제목 / Running header
     * - "short": 짧은 제목 / Short title
     * - "abbreviated": 약칭 제목 / Abbreviated title
     * - "toc": 목차용 제목 / Table of contents title
     */
    private String altTitleType;

    /**
     * 대체 제목 내용 / Alternative title content
     *
     * KR: 대체 제목의 텍스트 내용.
     *     Mixed content (텍스트 + 인라인 마크업)를 포함할 수 있음.
     * EN: Text content of the alternative title.
     *     Can contain mixed content (text + inline markup).
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Currently represented as plain String.
     *       For full mixed content support, consider using a richer content model.
     *
     * Examples:
     * - "Gene Expression Study" (running-head)
     * - "Gene Study" (short)
     * - "GES" (abbreviated)
     */
    private String content;
}
