package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AccessionNumber / 등록 번호
 *
 * DTD: <!ELEMENT AccessionNumber (#PCDATA)>
 *
 * KR: 데이터베이스 등록 번호
 * EN: Database accession number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessionNumber {

    /**
     * 등록 번호 / Accession number
     */
    private String value;
}
