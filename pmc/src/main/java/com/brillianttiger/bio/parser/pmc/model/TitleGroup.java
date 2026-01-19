package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TitleGroup / 제목 그룹
 *
 * KR: 논문의 다양한 제목 형식을 포함하는 그룹.
 *     주 제목, 부제목, 번역 제목, 대체 제목 등을 포함.
 * EN: Group containing various title formats of the article.
 *     Includes main title, subtitle, translated title, alternative title, etc.
 *
 * DTD: <!ELEMENT title-group (article-title, subtitle*, trans-title-group*, alt-title*, fn-group?)>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/title-group.html
 *
 * Examples:
 * <title-group>
 *   <article-title>Molecular Biology of Cancer Cells</article-title>
 *   <subtitle>A Comprehensive Analysis</subtitle>
 *   <trans-title-group xml:lang="ko">
 *     <trans-title>암 세포의 분자 생물학</trans-title>
 *   </trans-title-group>
 *   <alt-title alt-title-type="running-head">Cancer Cell Biology</alt-title>
 * </title-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TitleGroup {

    /**
     * 논문 제목 (필수) / Article title (required)
     *
     * KR: 논문의 주 제목.
     * EN: Main title of the article.
     *
     * DTD: article-title
     * Required: YES
     */
    private ArticleTitle articleTitle;

    /**
     * 부제목 목록 / Subtitle list
     */
    private List<Subtitle> subtitles;

    /**
     * 번역 제목 그룹 목록 / Translated title group list
     */
    private List<TransTitleGroup> transTitleGroups;

    /**
     * 대체 제목 목록 / Alternative title list
     */
    private List<AltTitle> altTitles;

    /**
     * 각주 그룹 / Footnote group
     */
    private FnGroup fnGroup;
}
