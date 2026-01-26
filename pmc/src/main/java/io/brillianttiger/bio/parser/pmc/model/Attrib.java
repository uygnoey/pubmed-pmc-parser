package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Attrib / 귀속
 *
 * KR: 출처 귀속 정보
 * EN: Attribution information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attrib {
    private String value;
}
