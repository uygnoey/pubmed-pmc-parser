package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AltText / 대체 텍스트
 *
 * KR: 대체 텍스트 (접근성)
 * EN: Alternative text (accessibility)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AltText {
    private String value;
}
