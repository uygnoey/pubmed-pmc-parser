package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Item / 항목
 *
 * DTD: <!ELEMENT Item (#PCDATA)>
 *
 * KR: 목록 항목
 * EN: List item
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    /**
     * 항목 내용 / Item content
     */
    private String value;
}
