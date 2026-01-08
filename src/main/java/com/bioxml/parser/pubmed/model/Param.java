package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Param / 파라미터
 *
 * DTD: <!ELEMENT Param (#PCDATA)>
 * DTD: <!ATTLIST Param Name CDATA #REQUIRED>
 *
 * KR: 객체의 파라미터
 * EN: Object parameter
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Param {

    /**
     * 이름 (필수) / Name (required)
     */
    private String name;

    /**
     * 값 / Value
     */
    private String value;
}
