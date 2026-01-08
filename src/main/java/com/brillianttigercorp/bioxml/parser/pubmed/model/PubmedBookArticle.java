package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubmedBookArticle / PubMed 도서 논문
 *
 * DTD: <!ELEMENT PubmedBookArticle (BookDocument, PubmedBookData?)>
 *
 * KR: PubMed 도서 논문 전체 정보
 * EN: Complete PubMed book article information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubmedBookArticle {

    /**
     * 도서 문서 / Book document
     */
    private BookDocument bookDocument;

    /**
     * PubMed 도서 데이터 / PubMed book data
     */
    private PubmedBookData pubmedBookData;
}
