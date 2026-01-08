package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Corresp / 교신 정보
 *
 * KR: 교신저자 정보
 * EN: Corresponding author information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Corresp {
    private String id;
    private java.util.List<Email> emails;
    private java.util.List<ExtLink> extLinks;
    private String value;
}
