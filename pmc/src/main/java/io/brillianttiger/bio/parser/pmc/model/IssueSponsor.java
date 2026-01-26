package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IssueSponsor / 호 스폰서
 *
 * KR: 호 스폰서
 * EN: Issue sponsor
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueSponsor {
    private String value;
}
