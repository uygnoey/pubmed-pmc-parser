package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SelfUri / 자체 URI
 *
 * KR: 논문 자체 URI
 * EN: Article self URI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelfUri {
    private String contentType;
    private String xlinkHref;
    private String value;
}
