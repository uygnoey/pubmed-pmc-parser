package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AwardId / 수여 ID
 *
 * KR: 연구비 수여 ID
 * EN: Award identifier
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardId {
    private String value;
}
