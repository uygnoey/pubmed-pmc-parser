package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StartPage / 시작 페이지
 *
 * DTD: <!ELEMENT StartPage (#PCDATA)>
 *
 * KR: 논문 시작 페이지
 * EN: Article starting page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartPage {

    /**
     * 시작 페이지 번호 / Start page number
     */
    private String value;
}
