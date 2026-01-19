package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ChemicalList / 화학물질 목록
 *
 * DTD: <!ELEMENT ChemicalList (Chemical+)>
 *
 * KR: 논문에 언급된 화학 물질 목록
 * EN: Chemical substances mentioned in article
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChemicalList {

    /**
     * 화학물질 목록 / Chemical list
     */
    private List<Chemical> chemicals;
}
