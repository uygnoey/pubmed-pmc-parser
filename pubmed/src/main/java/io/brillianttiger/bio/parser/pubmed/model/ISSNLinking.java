package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ISSNLinking / 연결 ISSN
 *
 * DTD: <!ELEMENT ISSNLinking (#PCDATA)>
 *
 * KR: 인쇄본과 전자본을 연결하는 ISSN
 * EN: ISSN linking print and electronic versions
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ISSNLinking {

    /**
     * 연결 ISSN 값 / Linking ISSN value
     */
    private String value;
}
