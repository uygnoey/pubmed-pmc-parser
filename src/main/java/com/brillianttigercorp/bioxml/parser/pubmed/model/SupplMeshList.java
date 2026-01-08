package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SupplMeshList / 보충 MeSH 목록
 *
 * DTD: <!ELEMENT SupplMeshList (SupplMeshName+)>
 *
 * KR: 보충 MeSH 개념 목록
 * EN: Supplementary MeSH concept list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplMeshList {

    /**
     * 보충 MeSH 목록 / Supplementary MeSH list
     */
    private List<SupplMeshName> supplMeshNames;
}
