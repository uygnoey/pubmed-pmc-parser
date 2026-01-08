package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PmcArticleSet / PMC 논문 세트 (최상위 루트 요소)
 *
 * DTD: <!ELEMENT pmc-articleset (article+)>
 *
 * KR: PMC XML 파일의 최상위 컨테이너. 하나 이상의 논문을 포함
 * EN: Top-level container for PMC XML file. Contains one or more articles
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcArticleSet {

    /**
     * 논문 목록 (1개 이상 필수) / Article list (at least one required)
     */
    private List<PmcArticle> articles;
}
