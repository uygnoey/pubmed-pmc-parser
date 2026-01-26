package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Day / 일
 *
 * KR: 일
 * EN: Day
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Day {
    private String value;
}
