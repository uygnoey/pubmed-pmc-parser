package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Permissions / 권한
 *
 * KR: 저작권 및 라이선스 정보
 * EN: Copyright and license information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permissions {
    private CopyrightStatement copyrightStatement;
    private CopyrightYear copyrightYear;
    private CopyrightHolder copyrightHolder;
    private License license;
    private String value;
}
