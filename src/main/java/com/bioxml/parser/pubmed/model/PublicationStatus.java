package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PublicationStatus / 출판 상태
 *
 * DTD: <!ELEMENT PublicationStatus (#PCDATA)>
 *
 * KR: PubMed 내 출판 상태 (ppublish, epublish, aheadofprint 등)
 * EN: Publication status in PubMed
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationStatus {

    /**
     * 출판 상태 / Publication status
     */
    private String value;
}
