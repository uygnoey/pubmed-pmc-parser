package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Year / 연도
 *
 * KR: 연도
 * EN: Year
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Year {
    private String value;
}
