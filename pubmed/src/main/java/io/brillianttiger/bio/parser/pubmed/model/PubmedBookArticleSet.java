package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PubmedBookArticleSet / PubMed 도서 논문 세트 (최상위 루트 요소)
 *
 * DTD: <!ELEMENT PubmedBookArticleSet (PubmedBookArticle*)>
 *
 * KR: PubMed 도서 논문 XML 파일의 최상위 컨테이너. 도서 논문들을 포함
 * EN: Top-level container for PubMed book article XML file. Contains book articles
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubmedBookArticleSet {

    /**
     * PubMed 도서 논문 목록 (0개 이상) / PubMed book article list (zero or more)
     */
    private List<PubmedBookArticle> pubmedBookArticles;
}
