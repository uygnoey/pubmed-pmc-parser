package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Medium / 매체
 *
 * DTD: <!ELEMENT Medium (#PCDATA)>
 *
 * KR: 출판 매체
 * EN: Publication medium
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medium {

    /**
     * 매체 / Medium
     */
    private String value;
}
