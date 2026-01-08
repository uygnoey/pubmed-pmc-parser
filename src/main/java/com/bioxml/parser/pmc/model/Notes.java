package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notes / 노트
 *
 * KR: 노트
 * EN: Notes
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notes {
    private String notesType;
    private java.util.List<P> paragraphs;
    private String value;
}
