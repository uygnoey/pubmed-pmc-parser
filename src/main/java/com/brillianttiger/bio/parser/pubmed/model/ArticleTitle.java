package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ArticleTitle / 논문 제목
 *
 * DTD: <!ELEMENT ArticleTitle (#PCDATA)>
 *
 * KR: 논문의 주 제목 (마크업 포함 가능)
 * EN: Main article title (may contain markup)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTitle {

    /**
     * 제목 내용 / Title content
     */
    private String value;
}
