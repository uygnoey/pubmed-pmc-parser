package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Glossary / 용어집
 *
 * KR: 용어집
 * EN: Glossary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Glossary {
    private String id;
    private Title title;
    private java.util.List<GlossaryEntry> entries;
    private String value;
}
