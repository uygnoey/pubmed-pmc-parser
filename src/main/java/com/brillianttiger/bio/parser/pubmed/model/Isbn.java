package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Isbn / ISBN
 *
 * DTD: <!ELEMENT Isbn (#PCDATA)>
 *
 * KR: 국제 표준 도서 번호
 * EN: International Standard Book Number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Isbn {

    /**
     * ISBN 값 / ISBN value
     */
    private String value;
}
