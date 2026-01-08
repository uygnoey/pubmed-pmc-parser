package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ItemList / 항목 목록
 *
 * DTD: <!ELEMENT ItemList (Item+)>
 * DTD: <!ATTLIST ItemList ListType CDATA #REQUIRED>
 *
 * KR: 항목 목록
 * EN: Item list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemList {

    /**
     * 목록 유형 (필수) / List type (required)
     */
    private String listType;

    /**
     * 항목 목록 / Item list
     */
    private List<Item> items;
}
