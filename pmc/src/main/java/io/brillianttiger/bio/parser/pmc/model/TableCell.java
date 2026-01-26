package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TableCell / 표 셀
 *
 * KR: 표 셀
 * EN: Table cell
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableCell {
    private String align;
    private String valign;
    private String colspan;
    private String rowspan;
    private String value;
}
