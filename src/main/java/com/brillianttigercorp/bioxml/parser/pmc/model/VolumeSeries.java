package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VolumeSeries / 권 시리즈
 *
 * KR: 권 시리즈
 * EN: Volume series
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolumeSeries {
    private String value;
}
