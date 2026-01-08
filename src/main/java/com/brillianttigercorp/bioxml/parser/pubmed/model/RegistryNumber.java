package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegistryNumber / 등록 번호
 *
 * DTD: <!ELEMENT RegistryNumber (#PCDATA)>
 *
 * KR: 화학 물질 등록 번호 (CAS 번호)
 * EN: Chemical substance registry number (CAS number)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistryNumber {

    /**
     * 등록 번호 / Registry number
     */
    private String value;
}
