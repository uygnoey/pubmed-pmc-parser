package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LastName / 성
 *
 * DTD: <!ELEMENT LastName (#PCDATA)>
 *
 * KR: 저자 또는 조사자의 성
 * EN: Author or investigator last name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LastName {

    /**
     * 성 / Last name
     */
    private String value;
}
