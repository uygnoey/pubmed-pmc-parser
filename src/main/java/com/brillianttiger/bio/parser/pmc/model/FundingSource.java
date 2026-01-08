package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FundingSource / 연구비 출처
 *
 * KR: 연구비 출처
 * EN: Funding source
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingSource {
    private String value;
}
