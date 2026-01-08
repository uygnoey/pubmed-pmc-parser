package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SupportGroup / 지원 그룹
 *
 * KR: 지원 정보 그룹
 * EN: Support information group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportGroup {
    private java.util.List<FundingSource> fundingSources;
    private SupportSource supportSource;
    private String value;
}
