package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Era / 시대
 *
 * KR: 시대 (BC, AD 등)
 * EN: Era (BC, AD, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Era {
    private String value;
}
