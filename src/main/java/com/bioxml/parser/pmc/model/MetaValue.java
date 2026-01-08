package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MetaValue / 메타 값
 *
 * KR: 메타데이터 값
 * EN: Metadata value
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaValue {
    private String value;
}
