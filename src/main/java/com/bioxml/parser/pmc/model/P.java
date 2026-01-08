package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * P / 문단
 *
 * KR: 문단
 * EN: Paragraph
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class P {
    private String value;
}
