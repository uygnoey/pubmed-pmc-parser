package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PrincipalAwardRecipient / 주 수여자
 *
 * KR: 주요 연구비 수여자
 * EN: Principal award recipient
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalAwardRecipient {
    private Name name;
    private StringName stringName;
}
