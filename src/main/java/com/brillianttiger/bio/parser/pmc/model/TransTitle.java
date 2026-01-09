package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TransTitle / 번역 제목
 *
 * KR: 논문의 번역된 제목을 나타내는 요소.
 *     Mixed content (텍스트 + 인라인 요소)를 포함할 수 있음.
 * EN: Element representing the translated title of the article.
 *     Can contain mixed content (text + inline elements).
 *
 * DTD: <!ELEMENT trans-title (#PCDATA | %all-phrase;)*>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/trans-title.html
 *
 * Note: Mixed content (#PCDATA | %all-phrase;)* means text can be mixed with inline elements
 *       like <italic>, <bold>, <sub>, <sup>, etc.
 *       Currently represented as String for simplicity.
 *
 * Examples:
 * <trans-title>유전자 발현 연구</trans-title>
 * <trans-title><italic>In vitro</italic> 세포 행동 분석</trans-title>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransTitle {

    /**
     * 번역 제목 내용 / Translated title content
     *
     * KR: 번역된 제목의 텍스트 내용.
     *     Mixed content (텍스트 + 인라인 마크업)를 포함할 수 있음.
     * EN: Text content of the translated title.
     *     Can contain mixed content (text + inline markup).
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Currently represented as plain String.
     *       For full mixed content support, consider using a richer content model.
     *
     * Examples:
     * - "유전자 발현 연구" (Korean)
     * - "遺伝子発現の研究" (Japanese)
     * - "Étude de l'expression génétique" (French)
     */
    private String content;
}
