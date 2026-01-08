package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CollectionTitle / 컬렉션 제목
 *
 * DTD: <!ELEMENT CollectionTitle (#PCDATA)>
 *
 * KR: 컬렉션 제목
 * EN: Collection title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionTitle {

    /**
     * 컬렉션 제목 / Collection title
     */
    private String value;
}
