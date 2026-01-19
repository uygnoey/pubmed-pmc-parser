package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Month / 월
 *
 * DTD: <!ELEMENT Month (#PCDATA)>
 *
 * KR: 월 정보
 * EN: Month information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Month {

    /**
     * 월 / Month
     */
    private String value;
}
