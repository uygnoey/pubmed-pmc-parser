package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ArticleCategories / 논문 카테고리
 *
 * DTD: <!ELEMENT article-categories (subj-group*, series-title*, series-text*)>
 *
 * KR: 논문의 주제 분류 및 시리즈 정보
 * EN: Article subject classification and series information
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
