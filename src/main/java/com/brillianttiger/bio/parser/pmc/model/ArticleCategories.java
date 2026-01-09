package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ArticleCategories / 논문 카테고리
 *
 * KR: 논문의 주제 분류 및 시리즈 정보를 나타내는 요소.
 *     주제 그룹, 시리즈 제목 등을 포함.
 * EN: Element representing article subject classification and series information.
 *     Includes subject groups, series titles, etc.
 *
 * DTD: <!ELEMENT article-categories (subj-group+)>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/article-categories.html
 *
 * Note: JATS 1.4 requires at least one subj-group.
 *
 * Examples:
 * <article-categories>
 *   <subj-group subj-group-type="heading">
 *     <subject>Research Article</subject>
 *   </subj-group>
 *   <subj-group subj-group-type="discipline">
 *     <subject>Biology</subject>
 *   </subj-group>
 * </article-categories>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategories {

    /**
     * 주제 그룹 목록 / Subject group list
     */
    private List<SubjGroup> subjGroups;

    /**
     * 시리즈 제목 목록 / Series title list
     */
    private List<SeriesTitle> seriesTitles;

    /**
     * 시리즈 텍스트 목록 / Series text list
     */
    private List<SeriesText> seriesTexts;

    /**
     * 값 / Value
     */
    private String value;
}
