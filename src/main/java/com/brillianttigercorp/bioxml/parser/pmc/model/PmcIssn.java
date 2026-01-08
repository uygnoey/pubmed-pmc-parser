package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PmcIssn / PMC ISSN
 *
 * DTD: <!ELEMENT issn (#PCDATA)>
 * DTD: <!ATTLIST issn
 *          pub-type (ppub | epub) #IMPLIED
 *          content-type CDATA #IMPLIED>
 *
 * KR: 저널의 ISSN (인쇄본/전자본)
 * EN: Journal ISSN (print/electronic)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcIssn {

    /**
     * 출판 유형: ppub | epub / Publication type
     */
    private String pubType;

    /**
     * 콘텐츠 유형 / Content type
     */
    private String contentType;

    /**
     * ISSN 값 / ISSN value
     */
    private String value;
}
