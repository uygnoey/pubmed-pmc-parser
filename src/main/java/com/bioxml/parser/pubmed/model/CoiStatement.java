package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CoiStatement (Conflict of Interest Statement) / 이해충돌 성명
 *
 * DTD: <!ELEMENT CoiStatement (#PCDATA)>
 *
 * KR: 저자의 이해충돌 관련 성명
 * EN: Author's conflict of interest statement
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoiStatement {

    /**
     * 이해충돌 성명 내용 / COI statement content
     */
    private String value;
}
