package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TransTitleGroup / 번역 제목 그룹
 *
 * KR: 논문의 번역된 제목과 부제목을 포함하는 그룹.
 *     특정 언어로 번역된 제목 정보를 담음.
 * EN: Group containing translated title and subtitle of the article.
 *     Contains title information translated into a specific language.
 *
 * DTD: <!ELEMENT trans-title-group (trans-title, trans-subtitle*)>
 *
 * DTD: <!ATTLIST trans-title-group
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/trans-title-group.html
 *
 * Examples:
 * <trans-title-group xml:lang="ko">
 *   <trans-title>유전자 발현 연구</trans-title>
 *   <trans-subtitle>종합적 분석</trans-subtitle>
 * </trans-title-group>
 *
 * <trans-title-group xml:lang="ja">
 *   <trans-title>遺伝子発現の研究</trans-title>
 * </trans-title-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransTitleGroup {

    /**
     * 언어 코드 / Language code
     *
     * KR: 번역된 제목의 언어를 나타내는 ISO 639 언어 코드.
     * EN: ISO 639 language code indicating the language of the translated title.
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     *
     * Examples:
     * - "ko" (Korean)
     * - "ja" (Japanese)
     * - "zh" (Chinese)
     * - "fr" (French)
     * - "de" (German)
     */
    private String xmlLang;

    /**
     * 번역 제목 (필수) / Translated title (required)
     *
     * KR: 번역된 주 제목.
     * EN: Translated main title.
     *
     * DTD: trans-title
     * Required: YES
     */
    private TransTitle transTitle;

    /**
     * 번역 부제목 목록 / Translated subtitle list
     *
     * KR: 번역된 부제목 목록.
     * EN: List of translated subtitles.
     *
     * DTD: trans-subtitle*
     * Required: NO
     */
    private List<TransSubtitle> transSubtitles;
}
