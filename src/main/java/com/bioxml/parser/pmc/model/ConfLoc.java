package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConfLoc / 학회 위치
 *
 * KR: 학회 개최 위치
 * EN: Conference location
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfLoc {
    private String value;
}
