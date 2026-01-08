package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EndingDate / 종료 날짜
 *
 * DTD: <!ELEMENT EndingDate (#PCDATA)>
 *
 * KR: 종료 날짜
 * EN: Ending date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndingDate {

    /**
     * 종료 날짜 / Ending date
     */
    private String value;
}
