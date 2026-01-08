package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Uri / URI
 *
 * KR: URI (Uniform Resource Identifier)
 * EN: URI (Uniform Resource Identifier)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Uri {
    private String value;
}
