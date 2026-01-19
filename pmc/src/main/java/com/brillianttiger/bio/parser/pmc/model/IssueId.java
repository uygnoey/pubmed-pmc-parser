package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IssueId / 호 ID
 *
 * KR: 호 식별자
 * EN: Issue identifier
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueId {
    private String pubIdType;
    private String value;
}
