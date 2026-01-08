package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agency / 지원 기관
 *
 * DTD: <!ELEMENT Agency (#PCDATA)>
 *
 * KR: 연구비 지원 기관명
 * EN: Grant funding agency name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agency {

    /**
     * 기관명 / Agency name
     */
    private String value;
}
