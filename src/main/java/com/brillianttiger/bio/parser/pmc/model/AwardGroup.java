package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AwardGroup / 수여 그룹
 *
 * KR: 연구비 수여 그룹
 * EN: Award group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardGroup {
    private java.util.List<FundingSource> fundingSources;
    private java.util.List<AwardId> awardIds;
    private java.util.List<PrincipalAwardRecipient> principalAwardRecipients;
}
