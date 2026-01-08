package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BookTitle / 도서 제목
 *
 * DTD: <!ELEMENT BookTitle (#PCDATA)>
 *
 * KR: 도서 제목
 * EN: Book title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookTitle {

    /**
     * 도서 제목 / Book title
     */
    private String value;
}
