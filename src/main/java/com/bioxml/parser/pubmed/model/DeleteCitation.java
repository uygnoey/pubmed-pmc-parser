package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DeleteCitation / 삭제된 인용 정보
 *
 * DTD: <!ELEMENT DeleteCitation (PMID+)>
 *
 * KR: 삭제된 PubMed 논문의 PMID 목록
 * EN: List of PMIDs for deleted PubMed articles
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCitation {

    /**
     * 삭제된 PMID 목록 (1개 이상 필수) / Deleted PMID list (at least one required)
     */
    private List<PMID> pmids;
}
