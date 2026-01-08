package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EndPage / 끝 페이지
 *
 * DTD: <!ELEMENT EndPage (#PCDATA)>
 *
 * KR: 논문 끝 페이지
 * EN: Article ending page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndPage {

    /**
     * 끝 페이지 번호 / End page number
     */
    private String value;
}
