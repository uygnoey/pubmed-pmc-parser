package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CopyrightYear / 저작권 연도
 *
 * KR: 저작권 연도
 * EN: Copyright year
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyrightYear {
    private String value;
}
