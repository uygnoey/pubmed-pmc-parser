package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response / 응답
 *
 * KR: 논문에 대한 응답
 * EN: Response to article
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private String responseType;
    private String id;
    private Front frontStub;
    private Body body;
    private Back back;
    private String value;
}
