package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PageRange / 페이지 범위
 *
 * KR: 페이지 범위
 * EN: Page range
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRange {
    private String value;
}
