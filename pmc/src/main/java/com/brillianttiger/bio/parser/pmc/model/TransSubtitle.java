package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TransSubtitle / 번역 부제목
 *
 * KR: 논문의 번역된 부제목을 나타내는 요소.
 *     Mixed content (텍스트 + 인라인 요소)를 포함할 수 있음.
 * EN: Element representing the translated subtitle of the article.
 *     Can contain mixed content (text + inline elements).
 *
 * DTD: <!ELEMENT trans-subtitle (#PCDATA | %all-phrase;)*>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/trans-subtitle.html
 *
 * Note: Mixed content (#PCDATA | %all-phrase;)* means text can be mixed with inline elements
 *       like <italic>, <bold>, <sub>, <sup>, etc.
 *       Currently represented as String for simplicity.
 *
 * Examples:
 * <trans-subtitle>종합적 분석</trans-subtitle>
 * <trans-subtitle><italic>In vivo</italic> 및 <italic>In vitro</italic> 연구</trans-subtitle>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransSubtitle {

    /**
     * 번역 부제목 내용 / Translated subtitle content
     *
     * KR: 번역된 부제목의 텍스트 내용.
     *     Mixed content (텍스트 + 인라인 마크업)를 포함할 수 있음.
     * EN: Text content of the translated subtitle.
     *     Can contain mixed content (text + inline markup).
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Currently represented as plain String.
     *       For full mixed content support, consider using a richer content model.
     *
     * Examples:
     * - "종합적 분석" (Korean)
     * - "包括的分析" (Japanese)
     * - "Analyse complète" (French)
     */
    private String content;
}
