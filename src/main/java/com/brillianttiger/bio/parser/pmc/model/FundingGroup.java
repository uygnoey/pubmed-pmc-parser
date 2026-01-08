package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FundingGroup / 연구비 그룹
 *
 * KR: 연구비 정보 그룹
 * EN: Funding information group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingGroup {
    private java.util.List<AwardGroup> awardGroups;
    private FundingStatement fundingStatement;
    private String value;
}
