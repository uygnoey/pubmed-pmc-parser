package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GeneSymbolList / 유전자 심볼 목록
 *
 * DTD: <!ELEMENT GeneSymbolList (GeneSymbol+)>
 *
 * KR: 유전자 심볼 목록
 * EN: Gene symbol list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneSymbolList {

    /**
     * 유전자 심볼 목록 / Gene symbol list
     */
    private List<GeneSymbol> geneSymbols;
}
