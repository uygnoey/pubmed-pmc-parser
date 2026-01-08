package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * App / 부록
 *
 * KR: 부록
 * EN: Appendix
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class App {
    private String id;
    private Label label;
    private Title title;
    private java.util.List<P> paragraphs;
    private java.util.List<Sec> sections;
}
