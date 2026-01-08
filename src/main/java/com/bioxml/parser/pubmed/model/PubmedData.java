package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PubmedData / PubMed 데이터
 *
 * DTD: <!ELEMENT PubmedData (History?, PublicationStatus, ArticleIdList, ObjectList?, ReferenceList*)>
 *
 * KR: PubMed 시스템 내부 데이터
 * EN: PubMed system internal data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubmedData {

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

    /**
     * 참조 목록 / Reference list
     */
    private List<ReferenceList> referenceLists;
}
