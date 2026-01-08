package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OtherID / 기타 ID
 *
 * DTD: <!ELEMENT OtherID (#PCDATA)>
 * DTD: <!ATTLIST OtherID Source (NASA | KIE | PIP | POP | ARPL | CPC | IND | CPFH | CLML | NRCBL | NLM | QCIM) #REQUIRED>
 *
 * KR: 다른 데이터베이스의 식별자
 * EN: Identifier from other database
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherID {

    /**
     * 출처 (필수) / Source (required)
     */
    private String source;

    /**
     * ID 값 / ID value
     */
    private String value;
}
