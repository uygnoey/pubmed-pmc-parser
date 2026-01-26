package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Season / 계절
 *
 * KR: 계절
 * EN: Season
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    private String value;
}
