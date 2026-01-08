package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Prefix / 접두어
 *
 * KR: 접두어 (Dr., Prof. 등)
 * EN: Prefix (Dr., Prof., etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prefix {
    private String value;
}
