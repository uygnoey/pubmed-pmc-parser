package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExtLink / 외부 링크
 *
 * KR: 외부 링크
 * EN: External link
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtLink {
    private String extLinkType;
    private String xlinkHref;
    private String value;
}
