package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Keyword / 키워드
 *
 * DTD: <!ELEMENT Keyword (#PCDATA)>
 * DTD: <!ATTLIST Keyword MajorTopicYN (Y | N) "N">
 *
 * KR: 논문 키워드
 * EN: Article keyword
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {

    /**
     * 주요 주제 여부: Y | N (기본값: "N") / Major topic flag (default: "N")
     */
    @Builder.Default
    private String majorTopicYN = "N";

    /**
     * 키워드 / Keyword
     */
    private String value;
}
