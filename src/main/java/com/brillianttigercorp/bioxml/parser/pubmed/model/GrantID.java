package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GrantID / 연구비 ID
 *
 * DTD: <!ELEMENT GrantID (#PCDATA)>
 *
 * KR: 연구비 지원 고유 ID
 * EN: Grant unique identifier
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrantID {

    /**
     * 연구비 ID / Grant ID
     */
    private String value;
}
