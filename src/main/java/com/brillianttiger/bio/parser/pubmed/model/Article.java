package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Article / 논문
 *
 * DTD: <!ELEMENT Article (
 *      Journal,
 *      ArticleTitle,
 *      ((Pagination, ELocationID*) | ELocationID+),
 *      Abstract?,
 *      AuthorList?,
 *      Language+,
 *      DataBankList?,
 *      GrantList?,
 *      PublicationTypeList,
 *      VernacularTitle?,
 *      ArticleDate*
 *  )>
 * DTD: <!ATTLIST Article
 *          PubModel (Print | Print-Electronic | Electronic | Electronic-Print | Electronic-eCollection) #REQUIRED>
 *
 * KR: 논문의 핵심 정보
 * EN: Core article information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    /**
     * 출판 모델 (필수) / Publication model (required)
     */
    private String pubModel;

    /**
     * 저널 / Journal
     */
    private Journal journal;

    /**
     * 논문 제목 / Article title
     */
    private ArticleTitle articleTitle;

    /**
     * 페이지 정보 / Pagination
     */
    private Pagination pagination;

    /**
     * 전자 위치 ID 목록 / Electronic location ID list
     */
    private List<ELocationID> eLocationIDs;

    /**
     * 초록 / Abstract
     */
    private Abstract abstractInfo;

    /**
     * 저자 목록 / Author list
     */
    private AuthorList authorList;

    /**
     * 언어 목록 / Language list
     */
    private List<Language> languages;

    /**
     * 데이터은행 목록 / Data bank list
     */
    private DataBankList dataBankList;

    /**
     * 연구비 목록 / Grant list
     */
    private GrantList grantList;

    /**
     * 출판 유형 목록 / Publication type list
     */
    private PublicationTypeList publicationTypeList;

    /**
     * 자국어 제목 / Vernacular title
     */
    private VernacularTitle vernacularTitle;

    /**
     * 논문 날짜 목록 / Article date list
     */
    private List<ArticleDate> articleDates;
}
