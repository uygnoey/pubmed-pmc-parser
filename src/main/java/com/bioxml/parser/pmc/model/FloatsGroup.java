package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FloatsGroup / 플로트 그룹
 *
 * KR: 그림/표 플로트 그룹
 * EN: Floats group (figures/tables)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloatsGroup {
    private java.util.List<Fig> figures;
    private java.util.List<TableWrap> tables;
    private String value;
}
