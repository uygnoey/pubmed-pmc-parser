package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Title / 제목
 *
 * KR: 요소의 제목
 * EN: Element title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Title {
    private String value;
}
