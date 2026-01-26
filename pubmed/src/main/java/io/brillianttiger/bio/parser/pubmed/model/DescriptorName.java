package io.brillianttiger.bio.parser.pubmed.model;

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
 *          AutoHM (Y) #IMPLIED
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
     * AutoHM 플래그 (Y만 가능) / AutoHM flag (only Y allowed)
     *
     * KR: 자동으로 매핑된 MeSH 표제어 표시 (Y인 경우에만 존재)
     * EN: Indicates automatically mapped MeSH heading (present only when Y)
     */
    private String autoHM;

    /**
     * 유형 (Geographic) / Type
     */
    private DescriptorNameType type;

    /**
     * 디스크립터명 / Descriptor name
     */
    private String value;
}
