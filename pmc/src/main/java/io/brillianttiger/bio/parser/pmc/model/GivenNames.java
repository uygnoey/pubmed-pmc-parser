package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GivenNames / 이름
 *
 * KR: 이름
 * EN: Given Names
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GivenNames {
    private String value;
}
