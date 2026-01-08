package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * License / 라이선스
 *
 * KR: 라이선스 정보
 * EN: License information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class License {
    private String licenseType;
    private String xlinkHref;
    private java.util.List<P> paragraphs;
}
