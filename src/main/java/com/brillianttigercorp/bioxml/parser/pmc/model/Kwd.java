package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Kwd / 키워드
 *
 * DTD: <!ELEMENT kwd (#PCDATA | %kwd-elements;)*>
 *
 * KR: 논문 키워드
 * EN: Article keyword
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kwd {

    /**
     * 키워드 / Keyword
     */
    private String value;
}
