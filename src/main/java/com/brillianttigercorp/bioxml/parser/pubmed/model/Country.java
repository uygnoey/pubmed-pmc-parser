package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Country / 국가
 *
 * DTD: <!ELEMENT Country (#PCDATA)>
 *
 * KR: 저널 출판 국가 또는 연구비 지원 국가
 * EN: Journal publication country or grant funding country
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {

    /**
     * 국가명 / Country name
     */
    private String value;
}
