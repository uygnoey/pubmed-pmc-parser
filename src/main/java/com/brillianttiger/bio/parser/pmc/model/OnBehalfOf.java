package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OnBehalfOf / 대표
 *
 * KR: ~를 대표하여
 * EN: On behalf of
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnBehalfOf {
    private String value;
}
