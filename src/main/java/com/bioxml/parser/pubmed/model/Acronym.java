package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Acronym / 약어
 *
 * DTD: <!ELEMENT Acronym (#PCDATA)>
 *
 * KR: 연구비 지원 기관 약어
 * EN: Grant funding agency acronym
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Acronym {

    /**
     * 약어 / Acronym
     */
    private String value;
}
