package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Year / 연도
 *
 * DTD: <!ELEMENT Year (#PCDATA)>
 *
 * KR: 연도 정보
 * EN: Year information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Year {

    /**
     * 연도 / Year
     */
    private String value;
}
