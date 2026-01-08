package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title / 제목
 *
 * DTD: <!ELEMENT Title (#PCDATA)>
 *
 * KR: 저널의 완전한 제목
 * EN: Full journal title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Title {

    /**
     * 제목 / Title
     */
    private String value;
}
