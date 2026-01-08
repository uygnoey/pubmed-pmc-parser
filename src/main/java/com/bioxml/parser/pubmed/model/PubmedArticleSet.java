package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PubmedArticleSet / PubMed 논문 세트 (최상위 루트 요소)
 *
 * DTD: <!ELEMENT PubmedArticleSet ((PubmedArticle | PubmedBookArticle)+, DeleteCitation?)>
 *
 * KR: PubMed XML 파일의 최상위 컨테이너. 일반 논문과 도서 논문, 삭제된 논문 정보를 포함
 * EN: Top-level container for PubMed XML file. Contains regular articles, book articles, and deleted citation info
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubmedArticleSet {

    /**
     * PubMed 일반 논문 목록 / PubMed article list
     */
    private List<PubmedArticle> pubmedArticles;

    /**
     * PubMed 도서 논문 목록 / PubMed book article list
     */
    private List<PubmedBookArticle> pubmedBookArticles;

    /**
     * 삭제된 인용 정보 / Deleted citation information
     */
    private DeleteCitation deleteCitation;
}
