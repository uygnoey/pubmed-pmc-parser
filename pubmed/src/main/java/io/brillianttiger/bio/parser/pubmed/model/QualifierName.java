package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QualifierName / 한정어명
 *
 * DTD: <!ELEMENT QualifierName (#PCDATA)>
 * DTD: <!ATTLIST QualifierName
 *          UI CDATA #REQUIRED
 *          AutoHM (Y) #IMPLIED
 *          MajorTopicYN (Y | N) "N">
 *
 * KR: MeSH 주제어 한정어
 * EN: MeSH subject qualifier
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualifierName {

    /**
     * UI (고유 식별자, 필수) / UI (unique identifier, required)
     */
    private String ui;

    /**
     * AutoHM 플래그 (Y만 가능) / AutoHM flag (only Y allowed)
     *
     * KR: 자동으로 매핑된 MeSH 한정어 표시 (Y인 경우에만 존재)
     * EN: Indicates automatically mapped MeSH qualifier (present only when Y)
     */
    private String autoHM;

    /**
     * 주요 주제 여부: Y | N (기본값: "N") / Major topic flag (default: "N")
     */
    @Builder.Default
    private String majorTopicYN = "N";

    /**
     * 한정어명 / Qualifier name
     */
    private String value;
}
