package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DescriptorName / 디스크립터명
 *
 * DTD: <!ELEMENT DescriptorName (#PCDATA)>
 * DTD: <!ATTLIST DescriptorName
 *          UI CDATA #REQUIRED
 *          MajorTopicYN (Y | N) "N"
 *          Type (Geographic) #IMPLIED>
 *
 * KR: MeSH 주제어 디스크립터
 * EN: MeSH subject descriptor
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescriptorName {

    /**
     * UI (고유 식별자, 필수) / UI (unique identifier, required)
     */
    private String ui;

    /**
     * 주요 주제 여부: Y | N (기본값: "N") / Major topic flag (default: "N")
     */
    @Builder.Default
    private String majorTopicYN = "N";

    /**
     * 유형 (Geographic) / Type
     */
    private String type;

    /**
     * 디스크립터명 / Descriptor name
     */
    private String value;
}
