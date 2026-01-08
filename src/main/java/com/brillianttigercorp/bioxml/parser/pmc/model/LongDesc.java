package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LongDesc / 긴 설명
 *
 * KR: 긴 설명 (접근성)
 * EN: Long description (accessibility)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LongDesc {
    private String value;
}
