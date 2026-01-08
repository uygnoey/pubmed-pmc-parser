package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ISOAbbreviation / ISO 약어
 *
 * DTD: <!ELEMENT ISOAbbreviation (#PCDATA)>
 *
 * KR: 저널의 ISO 표준 약어
 * EN: Journal ISO standard abbreviation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ISOAbbreviation {

    /**
     * ISO 약어 / ISO abbreviation
     */
    private String value;
}
