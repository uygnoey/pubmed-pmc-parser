package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bio / 약력
 *
 * KR: 저자 약력
 * EN: Author biography
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bio {
    private String id;
    private java.util.List<P> paragraphs;
    private String value;
}
