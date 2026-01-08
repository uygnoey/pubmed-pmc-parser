package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Book / 도서
 *
 * DTD: <!ELEMENT Book (
 *      Publisher,
 *      BookTitle,
 *      PubDate,
 *      BeginningDate?,
 *      EndingDate?,
 *      AuthorList*,
 *      InvestigatorList?,
 *      Volume?,
 *      VolumeTitle?,
 *      Edition?,
 *      CollectionTitle?,
 *      Isbn*,
 *      ELocationID*,
 *      Medium?,
 *      ReportNumber?
 *  )>
 *
 * KR: 도서 정보
 * EN: Book information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    /**
     * 출판사 / Publisher
     */
    private Publisher publisher;

    /**
     * 도서 제목 / Book title
     */
    private BookTitle bookTitle;

    /**
     * 출판 날짜 / Publication date
     */
    private PubDate pubDate;

    /**
     * 시작 날짜 / Beginning date
     */
    private BeginningDate beginningDate;

    /**
     * 종료 날짜 / Ending date
     */
    private EndingDate endingDate;

    /**
     * 저자 목록 / Author list
     */
    private List<AuthorList> authorLists;

    /**
     * 조사자 목록 / Investigator list
     */
    private InvestigatorList investigatorList;

    /**
     * 권 / Volume
     */
    private Volume volume;

    /**
     * 권 제목 / Volume title
     */
    private VolumeTitle volumeTitle;

    /**
     * 판 / Edition
     */
    private Edition edition;

    /**
     * 컬렉션 제목 / Collection title
     */
    private CollectionTitle collectionTitle;

    /**
     * ISBN 목록 / ISBN list
     */
    private List<Isbn> isbns;

    /**
     * 전자 위치 ID 목록 / Electronic location ID list
     */
    private List<ELocationID> eLocationIDs;

    /**
     * 매체 / Medium
     */
    private Medium medium;

    /**
     * 보고서 번호 / Report number
     */
    private ReportNumber reportNumber;
}
