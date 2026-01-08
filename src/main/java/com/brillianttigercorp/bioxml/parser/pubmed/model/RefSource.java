package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RefSource / 참조 출처
 *
 * DTD: <!ELEMENT RefSource (#PCDATA)>
 *
 * KR: 코멘트/정정 참조의 출처 정보
 * EN: Comment/correction reference source
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefSource {

    /**
     * 출처 정보 / Reference source
     */
    private String value;
}
