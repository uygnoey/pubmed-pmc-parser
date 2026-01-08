package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AltTitle / 대체 제목
 *
 * KR: 대체 제목
 * EN: Alternative title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AltTitle {
    private String altTitleType;
    private String value;
}
