package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConfName / 학회명
 *
 * KR: 학회 이름
 * EN: Conference name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfName {
    private String value;
}
