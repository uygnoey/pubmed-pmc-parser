package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Second / 초
 *
 * DTD: <!ELEMENT Second (#PCDATA)>
 *
 * KR: 시각 정보 - 초
 * EN: Time information - second
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Second {

    /**
     * 초 / Second
     */
    private String value;
}
