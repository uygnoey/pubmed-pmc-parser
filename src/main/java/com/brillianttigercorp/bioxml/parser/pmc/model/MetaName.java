package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MetaName / 메타 이름
 *
 * KR: 메타데이터 이름
 * EN: Metadata name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaName {
    private String value;
}
