package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Aff / 소속
 *
 * DTD: <!ELEMENT aff (#PCDATA | %aff-elements;)*>
 * DTD: <!ATTLIST aff id ID #IMPLIED>
 *
 * KR: 저자 소속 정보
 * EN: Author affiliation information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aff {

    /**
     * ID 속성 / ID attribute
     */
    private String id;

    /**
     * 소속 내용 / Affiliation content
     */
    private String value;
}
