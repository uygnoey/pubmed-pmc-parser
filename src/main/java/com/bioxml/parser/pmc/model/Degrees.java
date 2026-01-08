package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Degrees / 학위
 *
 * KR: 학위 (PhD, MD 등)
 * EN: Degrees (PhD, MD, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Degrees {
    private String value;
}
