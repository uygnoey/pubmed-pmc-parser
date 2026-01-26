package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Surname / 성
 *
 * KR: 성
 * EN: Surname
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Surname {
    private String value;
}
