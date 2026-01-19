package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Issue / 호
 *
 * KR: 호 번호
 * EN: Issue number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcIssue {
    private String value;
}
