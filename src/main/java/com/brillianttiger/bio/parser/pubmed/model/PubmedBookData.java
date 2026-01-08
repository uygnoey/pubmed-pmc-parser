package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubmedBookData / PubMed 도서 데이터
 *
 * DTD: <!ELEMENT PubmedBookData (History?, PublicationStatus, ArticleIdList, ObjectList?)>
 *
 * KR: PubMed 도서 시스템 데이터
 * EN: PubMed book system data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubmedBookData {

    /**
     * 이력 / History
     */
    private History history;

    /**
     * 출판 상태 / Publication status
     */
    private PublicationStatus publicationStatus;

    /**
     * 논문 ID 목록 / Article ID list
     */
    private ArticleIdList articleIdList;

    /**
     * 객체 목록 / Object list
     */
    private ObjectList objectList;
}
