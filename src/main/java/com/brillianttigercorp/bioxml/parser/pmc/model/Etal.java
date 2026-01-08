package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Etal / 기타
 *
 * KR: 기타 저자 (et al.)
 * EN: Et al. (and others)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Etal {
    private String value;
}
