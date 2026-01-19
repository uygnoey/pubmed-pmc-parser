package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Season / 계절
 *
 * DTD: <!ELEMENT Season (#PCDATA)>
 *
 * KR: 계절 정보 (예: Spring, Fall)
 * EN: Season information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {

    /**
     * 계절 / Season
     */
    private String value;
}
