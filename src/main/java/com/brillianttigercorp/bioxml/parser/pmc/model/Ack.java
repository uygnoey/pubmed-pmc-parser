package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Ack / 감사의 글
 *
 * KR: 감사의 글
 * EN: Acknowledgments
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ack {
    private String id;
    private Title title;
    private java.util.List<P> paragraphs;
    private String value;
}
