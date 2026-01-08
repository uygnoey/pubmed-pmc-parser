package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SubArticle / 하위 논문
 *
 * KR: 하위 논문 (embedded article)
 * EN: Sub-article (embedded article)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubArticle {
    private String articleType;
    private String id;
    private Front frontStub;
    private Body body;
    private Back back;
    private String value;
}
