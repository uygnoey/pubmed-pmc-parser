package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CopyrightHolder / 저작권자
 *
 * KR: 저작권 소유자
 * EN: Copyright holder
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyrightHolder {
    private String value;
}
