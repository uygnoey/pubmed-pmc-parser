package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Suffix / 접미어
 *
 * KR: 접미어 (Jr., Sr., III 등)
 * EN: Suffix (Jr., Sr., III, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcSuffix {
    private String value;
}
