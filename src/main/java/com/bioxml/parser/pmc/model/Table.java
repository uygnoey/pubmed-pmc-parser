package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Table / 표
 *
 * KR: 표
 * EN: Table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Table {
    private String frame;
    private String colsep;
    private String rowsep;
    private java.util.List<TableRow> rows;
}
