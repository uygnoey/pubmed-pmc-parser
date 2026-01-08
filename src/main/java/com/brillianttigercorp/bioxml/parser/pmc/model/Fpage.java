package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fpage / 시작 페이지
 *
 * KR: 시작 페이지
 * EN: First page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fpage {
    private String value;
}
