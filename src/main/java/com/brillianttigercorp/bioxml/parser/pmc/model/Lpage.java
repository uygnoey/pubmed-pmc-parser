package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lpage / 끝 페이지
 *
 * KR: 끝 페이지
 * EN: Last page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lpage {
    private String value;
}
