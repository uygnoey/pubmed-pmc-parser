package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IssuePart / 호 파트
 *
 * KR: 호의 일부분
 * EN: Issue part
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssuePart {
    private String value;
}
