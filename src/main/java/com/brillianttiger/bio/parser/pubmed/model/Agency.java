package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agency / 지원 기관
 *
 * DTD: <!ELEMENT Agency (%text;)*>
 *
 * KR: 연구비 지원 기관명 (혼합 콘텐츠 지원)
 * EN: Grant funding agency name (supports mixed content)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agency {

    /**
     * 기관명 / Agency name
     */
    private String value;
}
