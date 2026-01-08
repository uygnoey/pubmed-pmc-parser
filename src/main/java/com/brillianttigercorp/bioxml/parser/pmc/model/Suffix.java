package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Suffix / 접미사
 *
 * DTD: <!ELEMENT suffix (#PCDATA)>
 *
 * KR: 이름의 접미사 (Jr., Sr., III 등)
 * EN: Name suffix (Jr., Sr., III, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Suffix {

    /**
     * 접미사 값 / Suffix value
     */
    private String value;
}
