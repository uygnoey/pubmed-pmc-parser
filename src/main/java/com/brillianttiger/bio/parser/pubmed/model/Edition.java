package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Edition / 판
 *
 * DTD: <!ELEMENT Edition (#PCDATA)>
 *
 * KR: 도서 판
 * EN: Book edition
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Edition {

    /**
     * 판 정보 / Edition information
     */
    private String value;
}
