package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Edition / 판
 *
 * KR: 판 (1st, 2nd 등)
 * EN: Edition (1st, 2nd, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Edition {
    private String value;
}
