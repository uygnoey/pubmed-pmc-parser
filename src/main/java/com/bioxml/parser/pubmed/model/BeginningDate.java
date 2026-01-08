package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BeginningDate / 시작 날짜
 *
 * DTD: <!ELEMENT BeginningDate (#PCDATA)>
 *
 * KR: 시작 날짜
 * EN: Beginning date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeginningDate {

    /**
     * 시작 날짜 / Beginning date
     */
    private String value;
}
