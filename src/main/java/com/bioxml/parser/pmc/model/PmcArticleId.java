package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PmcArticleId / PMC 논문 ID
 *
 * DTD: <!ELEMENT article-id (#PCDATA)>
 * DTD: <!ATTLIST article-id
 *          pub-id-type (doi | pmid | pmcid | pii | publisher-id | arxiv | art-access-id | other) #IMPLIED
 *          id ID #IMPLIED>
 *
 * KR: 논문의 다양한 ID (DOI, PMID, PMC ID 등)
 * EN: Article various IDs (DOI, PMID, PMC ID, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcArticleId {

    /**
     * 출판 ID 유형 / Publication ID type
     */
    private String pubIdType;

    /**
     * ID 속성 / ID attribute
     */
    private String id;

    /**
     * ID 값 / ID value
     */
    private String value;
}
