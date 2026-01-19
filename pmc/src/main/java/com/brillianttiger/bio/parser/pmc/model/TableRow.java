package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TableRow / 표 행
 *
 * KR: 표 행
 * EN: Table row
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableRow {
    private java.util.List<TableCell> cells;
}
