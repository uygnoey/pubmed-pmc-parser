package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Initials / 이니셜
 *
 * DTD: <!ELEMENT Initials (#PCDATA)>
 *
 * KR: 저자 또는 조사자의 이름 이니셜
 * EN: Author or investigator name initials
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Initials {

    /**
     * 이니셜 / Initials
     */
    private String value;
}
