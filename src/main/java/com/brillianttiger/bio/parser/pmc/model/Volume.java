package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Volume / 권
 *
 * KR: 권 번호
 * EN: Volume number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Volume {
    private String value;
}
