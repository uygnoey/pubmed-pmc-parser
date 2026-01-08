package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * BookDocument / 도서 문서
 *
 * DTD: <!ELEMENT BookDocument (
 *      PMID,
 *      ArticleIdList,
 *      Book,
 *      ArticleTitle?,
 *      VernacularTitle?,
 *      Pagination?,
 *      Language*,
 *      AuthorList*,
 *      InvestigatorList?,
 *      PublicationType*,
 *      Abstract?,
 *      Sections?,
 *      KeywordList*,
 *      CoiStatement?,
 *      GrantList?,
 *      ItemList*,
 *      ReferenceList*
 *  )>
 *
 * KR: PubMed 도서 문서 정보
 * EN: PubMed book document information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDocument {

    /**
     * PMID / PMID
     */
    private PMID pmid;

    /**
     * 논문 ID 목록 / Article ID list
     */
    private ArticleIdList articleIdList;

    /**
     * 도서 / Book
     */
    private Book book;

    /**
     * 논문 제목 / Article title
     */
    private ArticleTitle articleTitle;

    /**
     * 자국어 제목 / Vernacular title
     */
    private VernacularTitle vernacularTitle;

    /**
     * 페이지 정보 / Pagination
     */
    private Pagination pagination;

    /**
     * 언어 목록 / Language list
     */
    private List<Language> languages;

    /**
     * 저자 목록 / Author list
     */
    private List<AuthorList> authorLists;

    /**
     * 조사자 목록 / Investigator list
     */
    private InvestigatorList investigatorList;

    /**
     * 출판 유형 목록 / Publication type list
     */
    private List<PublicationType> publicationTypes;

    /**
     * 초록 / Abstract
     */
    private Abstract abstractInfo;

    /**
     * 섹션 목록 / Sections
     */
    private Sections sections;

    /**
     * 키워드 목록 / Keyword list
     */
    private List<KeywordList> keywordLists;

    /**
     * 이해충돌 성명 / Conflict of interest statement
     */
    private CoiStatement coiStatement;

    /**
     * 연구비 목록 / Grant list
     */
    private GrantList grantList;

    /**
     * 항목 목록 / Item list
     */
    private List<ItemList> itemLists;

    /**
     * 참조 목록 / Reference list
     */
    private List<ReferenceList> referenceLists;
}
