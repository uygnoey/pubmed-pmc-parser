package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fn / 각주
 *
 * KR: 각주
 * EN: Footnote
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fn {
    private String fnType;
    private String id;
    private Label label;
    private java.util.List<P> paragraphs;
    private String value;
}
