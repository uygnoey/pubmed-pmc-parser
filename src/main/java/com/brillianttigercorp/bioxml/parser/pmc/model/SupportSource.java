package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SupportSource / 지원 출처
 *
 * KR: 지원 출처
 * EN: Support source
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportSource {
    private String value;
}
