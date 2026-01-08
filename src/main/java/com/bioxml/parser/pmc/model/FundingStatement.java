package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FundingStatement / 연구비 문구
 *
 * KR: 연구비 지원 문구
 * EN: Funding statement
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingStatement {
    private String value;
}
