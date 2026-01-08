package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FnGroup / 각주 그룹
 *
 * KR: 각주 그룹
 * EN: Footnote group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FnGroup {
    private java.util.List<Fn> footnotes;
    private String value;
}
