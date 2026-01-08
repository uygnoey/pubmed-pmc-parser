package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ArticleId / 논문 ID
 *
 * DTD: <!ELEMENT ArticleId (#PCDATA)>
 * DTD: <!ATTLIST ArticleId IdType (doi | pii | pmcpid | pmpid | pmc | mid | sici | pubmed | medline | pmcid | pmcbook) "pubmed">
 *
 * KR: 논문의 다양한 ID (DOI, PMID, PMC 등)
 * EN: Article various IDs (DOI, PMID, PMC, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleId {

    /**
     * ID 유형 (기본값: "pubmed") / ID type (default: "pubmed")
     */
    @Builder.Default
    private String idType = "pubmed";

    /**
     * ID 값 / ID value
     */
    private String value;
}
