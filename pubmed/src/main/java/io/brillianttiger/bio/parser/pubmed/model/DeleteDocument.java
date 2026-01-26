package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DeleteDocument / 삭제된 도서 문서 정보
 *
 * DTD: <!ELEMENT DeleteDocument (PMID*)>
 *
 * KR: 삭제된 도서 문서의 PMID 목록 (0개 이상)
 * EN: List of PMIDs for deleted book documents (zero or more)
 *
 * NOTE: FTP 파일에서만 포함됨 / Included in FTP files only
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteDocument {

    /**
     * 삭제된 PMID 목록 (0개 이상) / Deleted PMID list (zero or more)
     */
    private List<PMID> pmids;
}
