package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ContribId / 기여자 ID
 *
 * DTD: <!ELEMENT contrib-id (#PCDATA)>
 * DTD: <!ATTLIST contrib-id
 *          contrib-id-type CDATA #IMPLIED
 *          id ID #IMPLIED>
 *
 * KR: 기여자의 고유 식별자 (ORCID 등)
 * EN: Contributor unique identifier (ORCID, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContribId {

    /**
     * 기여자 ID 유형 / Contributor ID type
     */
    private String contribIdType;

    /**
     * ID 속성 / ID attribute
     */
    private String id;

    /**
     * ID 값 / ID value
     */
    private String value;
}
