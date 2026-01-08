package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PmcPublisher / PMC 출판사
 *
 * DTD: <!ELEMENT publisher (publisher-name, publisher-loc?)>
 *
 * KR: 저널 출판사 정보
 * EN: Journal publisher information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcPublisher {

    /**
     * 출판사명 / Publisher name
     */
    private PublisherName publisherName;

    /**
     * 출판사 위치 / Publisher location
     */
    private PublisherLoc publisherLoc;
}
