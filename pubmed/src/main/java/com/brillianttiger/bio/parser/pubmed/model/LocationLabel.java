package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LocationLabel / 위치 레이블
 *
 * DTD: <!ELEMENT LocationLabel (#PCDATA)>
 * DTD: <!ATTLIST LocationLabel Type (part | chapter | section | appendix | figure | table | box) #IMPLIED>
 *
 * KR: 섹션 위치 레이블
 * EN: Section location label
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationLabel {

    /**
     * 유형 / Type
     */
    private LocationLabelType type;

    /**
     * 위치 레이블 / Location label
     */
    private String value;
}
