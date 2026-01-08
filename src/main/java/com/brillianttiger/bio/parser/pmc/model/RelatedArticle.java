package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RelatedArticle / 관련 논문
 *
 * KR: 관련 논문
 * EN: Related article
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedArticle {
    private String relatedArticleType;
    private String id;
    private String xlinkHref;
    private String value;
}
