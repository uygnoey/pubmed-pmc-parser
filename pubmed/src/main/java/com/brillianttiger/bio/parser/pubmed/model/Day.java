package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Day / 일
 *
 * DTD: <!ELEMENT Day (#PCDATA)>
 *
 * KR: 일 정보
 * EN: Day information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Day {

    /**
     * 일 / Day
     */
    private String value;
}
