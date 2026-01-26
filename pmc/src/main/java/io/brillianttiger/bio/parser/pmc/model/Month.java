package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Month / 월
 *
 * KR: 월
 * EN: Month
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Month {
    private String value;
}
