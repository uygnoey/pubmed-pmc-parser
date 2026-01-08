package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Conference / 학회
 *
 * KR: 학회 정보
 * EN: Conference information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conference {
    private String confDate;
    private String confName;
    private String confNum;
    private String confLoc;
    private String confSponsor;
    private String confTheme;
    private String confAcronym;
    private String value;
}
