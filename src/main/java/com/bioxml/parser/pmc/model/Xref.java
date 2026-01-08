package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Xref / 교차 참조
 *
 * KR: 교차 참조
 * EN: Cross reference
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Xref {
    private String refType;
    private String rid;
    private String value;
}
