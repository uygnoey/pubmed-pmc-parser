package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ArticleTitle / 논문 제목
 *
 * KR: 참조문헌 논문 제목
 * EN: Article title in reference
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcArticleTitle {
    private String value;
}
