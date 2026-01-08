package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Ref / 참조
 *
 * DTD: <!ELEMENT ref (label?, (%citation.class;)+)>
 * DTD: <!ATTLIST ref id ID #IMPLIED>
 *
 * KR: 개별 참조 항목
 * EN: Individual reference entry
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ref {

    /**
     * ID 속성 / ID attribute
     */
    private String id;

    /**
     * 레이블 / Label
     */
    private Label label;

    /**
     * 요소 인용 목록 / Element citation list
     */
    private List<ElementCitation> elementCitations;

    /**
     * 혼합 인용 목록 / Mixed citation list
     */
    private List<MixedCitation> mixedCitations;
}
