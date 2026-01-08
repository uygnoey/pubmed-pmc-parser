package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CollectiveName / 단체명
 *
 * DTD: <!ELEMENT CollectiveName (#PCDATA)>
 *
 * KR: 단체 저자명 (예: "WHO Study Group")
 * EN: Collective author name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectiveName {

    /**
     * 단체명 / Collective name
     */
    private String value;
}
