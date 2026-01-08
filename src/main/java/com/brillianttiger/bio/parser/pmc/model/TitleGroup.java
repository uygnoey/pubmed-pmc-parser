package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TitleGroup / 제목 그룹
 *
 * DTD: <!ELEMENT title-group (article-title, subtitle*, trans-title-group*, alt-title*, fn-group?)>
 *
 * KR: 논문의 다양한 제목 형식
 * EN: Various article title formats
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TitleGroup {

    /**
     * 논문 제목 (필수) / Article title (required)
     */
    private PmcArticleTitle articleTitle;

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
