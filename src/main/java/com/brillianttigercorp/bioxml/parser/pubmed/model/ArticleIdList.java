package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ArticleIdList / 논문 ID 목록
 *
 * DTD: <!ELEMENT ArticleIdList (ArticleId+)>
 *
 * KR: 논문의 다양한 ID 목록
 * EN: Article various IDs list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleIdList {

    /**
     * 논문 ID 목록 / Article ID list
     */
    private List<ArticleId> articleIds;
}
