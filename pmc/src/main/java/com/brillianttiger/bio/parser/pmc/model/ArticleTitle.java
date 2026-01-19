package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ArticleTitle / 논문 제목
 *
 * KR: 논문의 주 제목을 나타내는 요소.
 *     Mixed content (텍스트 + 인라인 요소)를 포함할 수 있음.
 * EN: Element representing the main title of the article.
 *     Can contain mixed content (text + inline elements).
 *
 * DTD: <!ELEMENT article-title (#PCDATA | %all-phrase;)*>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/article-title.html
 *
 * Note: Mixed content (#PCDATA | %all-phrase;)* means text can be mixed with inline elements
 *       like <italic>, <bold>, <sub>, <sup>, <sc>, etc.
 *       Currently represented as String for simplicity.
 *
 * Examples:
 * <article-title>A Study of Gene Expression</article-title>
 * <article-title><italic>In vitro</italic> Analysis of Cell Behavior</article-title>
 * <article-title xml:lang="en">Molecular Biology of Cancer Cells</article-title>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTitle {

    /**
     * 언어 코드 / Language code
     *
     * KR: 제목의 언어를 나타내는 ISO 639 언어 코드.
     * EN: ISO 639 language code indicating the language of the title.
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     *
     * Examples:
     * - "en" (English)
     * - "ko" (Korean)
     * - "ja" (Japanese)
     */
    private String xmlLang;

    /**
     * 제목 내용 / Title content
     *
     * KR: 논문 제목의 텍스트 내용.
     *     Mixed content (텍스트 + 인라인 마크업)를 포함할 수 있음.
     * EN: Text content of the article title.
     *     Can contain mixed content (text + inline markup).
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Currently represented as plain String.
     *       For full mixed content support, consider using a richer content model.
     *
     * Examples:
     * - "A Study of Gene Expression"
     * - "In vitro Analysis of Cell Behavior"
     * - "Molecular Biology of Cancer Cells"
     */
    private String content;
}
