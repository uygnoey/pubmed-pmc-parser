package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GeneSymbol / 유전자 기호
 *
 * DTD: <!ELEMENT GeneSymbol (#PCDATA)>
 *
 * KR: 유전자 심볼 (예: "TP53", "BRCA1")
 * EN: Gene symbol
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneSymbol {

    /**
     * 유전자 심볼 / Gene symbol
     */
    private String value;
}
