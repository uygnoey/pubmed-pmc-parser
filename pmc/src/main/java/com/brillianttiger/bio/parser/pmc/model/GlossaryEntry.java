package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GlossaryEntry / 용어집 항목
 *
 * KR: 용어집 항목
 * EN: Glossary entry
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlossaryEntry {
    private String term;
    private String definition;
}
