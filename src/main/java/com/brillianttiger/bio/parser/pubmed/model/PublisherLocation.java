package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PublisherLocation / 출판사 위치
 *
 * DTD: <!ELEMENT PublisherLocation (#PCDATA)>
 *
 * KR: 출판사 위치
 * EN: Publisher location
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherLocation {

    /**
     * 출판사 위치 / Publisher location
     */
    private String value;
}
