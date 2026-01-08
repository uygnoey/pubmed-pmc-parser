package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Anonymous / 익명
 *
 * KR: 익명 저자
 * EN: Anonymous author
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Anonymous {
    private String value;
}
