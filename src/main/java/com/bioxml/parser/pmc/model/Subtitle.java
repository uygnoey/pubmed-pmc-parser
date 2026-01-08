package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subtitle / 부제
 *
 * KR: 부제
 * EN: Subtitle
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subtitle {
    private String value;
}
