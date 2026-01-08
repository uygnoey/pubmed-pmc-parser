package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PmcIsbn / PMC ISBN
 *
 * DTD: <!ELEMENT isbn (#PCDATA)>
 * DTD: <!ATTLIST isbn content-type CDATA #IMPLIED>
 *
 * KR: 저널의 ISBN
 * EN: Journal ISBN
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcIsbn {

    /**
     * 콘텐츠 유형 / Content type
     */
    private String contentType;

    /**
     * ISBN 값 / ISBN value
     */
    private String value;
}
