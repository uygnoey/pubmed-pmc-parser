package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AbstractText / 초록 텍스트
 *
 * DTD: <!ELEMENT AbstractText (%text; | mml:math)*>
 * DTD: <!ATTLIST AbstractText
 *          Label CDATA #IMPLIED
 *          NlmCategory (BACKGROUND | OBJECTIVE | METHODS | RESULTS | CONCLUSIONS | UNASSIGNED) #IMPLIED>
 *
 * KR: 초록의 개별 섹션 텍스트
 * EN: Individual section text of abstract
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbstractText {

    /**
     * 레이블 (예: "Background", "Methods") / Label
     */
    private String label;

    /**
     * NLM 카테고리 / NLM category
     *
     * DTD: NlmCategory (BACKGROUND | OBJECTIVE | METHODS | RESULTS | CONCLUSIONS | UNASSIGNED) #IMPLIED
     */
    private NlmCategory nlmCategory;

    /**
     * 텍스트 내용 / Text content
     */
    private String value;
}
