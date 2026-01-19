package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IssueTitle / 호 제목
 *
 * KR: 호 제목
 * EN: Issue title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueTitle {
    private String value;
}
