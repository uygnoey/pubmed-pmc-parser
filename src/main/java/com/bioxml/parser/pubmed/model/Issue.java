package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Issue / 호
 *
 * DTD: <!ELEMENT Issue (#PCDATA)>
 *
 * KR: 저널의 호(issue) 번호
 * EN: Journal issue number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issue {

    /**
     * 호 번호 / Issue number
     */
    private String value;
}
