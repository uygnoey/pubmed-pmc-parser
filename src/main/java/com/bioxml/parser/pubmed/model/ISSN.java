package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ISSN (International Standard Serial Number) / 국제표준연속간행물번호
 *
 * DTD: <!ELEMENT ISSN (#PCDATA)>
 * DTD: <!ATTLIST ISSN IssnType (Electronic | Print) #REQUIRED>
 *
 * KR: 저널의 ISSN 번호와 유형
 * EN: Journal ISSN number and type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ISSN {

    /**
     * ISSN 값 / ISSN value
     */
    private String value;

    /**
     * ISSN 유형: Electronic | Print (필수) / ISSN type (required)
     */
    private String issnType;
}
