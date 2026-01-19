package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Supplement / 보충
 *
 * KR: 보충 번호
 * EN: Supplement number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplement {
    private String value;
}
