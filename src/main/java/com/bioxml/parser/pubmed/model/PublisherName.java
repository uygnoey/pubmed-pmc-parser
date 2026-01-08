package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PublisherName / 출판사명
 *
 * DTD: <!ELEMENT PublisherName (#PCDATA)>
 *
 * KR: 출판사 이름
 * EN: Publisher name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherName {

    /**
     * 출판사명 / Publisher name
     */
    private String value;
}
