package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ForeName / 이름
 *
 * DTD: <!ELEMENT ForeName (#PCDATA)>
 *
 * KR: 저자 또는 조사자의 이름
 * EN: Author or investigator first name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeName {

    /**
     * 이름 / First name
     */
    private String value;
}
