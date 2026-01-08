package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthorNotes / 저자 노트
 *
 * KR: 저자 노트 (교신저자 정보, 각주 등)
 * EN: Author notes (corresponding author info, footnotes, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorNotes {
    private java.util.List<Corresp> corresps;
    private java.util.List<Fn> footnotes;
    private String value;
}
