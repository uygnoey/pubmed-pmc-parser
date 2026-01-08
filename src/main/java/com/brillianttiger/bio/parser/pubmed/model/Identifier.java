package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Identifier / 식별자
 *
 * DTD: <!ELEMENT Identifier (#PCDATA)>
 * DTD: <!ATTLIST Identifier Source CDATA #REQUIRED>
 *
 * KR: 저자 또는 소속의 외부 식별자 (ORCID 등)
 * EN: Author or affiliation external identifier (ORCID, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Identifier {

    /**
     * 출처 (필수) / Source (required)
     */
    private String source;

    /**
     * 식별자 값 / Identifier value
     */
    private String value;
}
