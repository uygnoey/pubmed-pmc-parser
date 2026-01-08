package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Volume / 권
 *
 * DTD: <!ELEMENT Volume (#PCDATA)>
 *
 * KR: 저널의 권(volume) 번호
 * EN: Journal volume number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Volume {

    /**
     * 권 번호 / Volume number
     */
    private String value;
}
