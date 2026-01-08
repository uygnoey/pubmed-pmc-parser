package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AppGroup / 부록 그룹
 *
 * KR: 부록 그룹
 * EN: Appendix group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppGroup {
    private java.util.List<App> apps;
    private String value;
}
