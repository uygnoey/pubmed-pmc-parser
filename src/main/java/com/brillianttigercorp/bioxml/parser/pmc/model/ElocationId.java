package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ElocationId / 전자 위치 ID
 *
 * KR: 전자 위치 ID
 * EN: Electronic location ID
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElocationId {
    private String value;
}
