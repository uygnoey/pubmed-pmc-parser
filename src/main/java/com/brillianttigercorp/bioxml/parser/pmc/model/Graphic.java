package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Graphic / 그래픽
 *
 * KR: 그래픽 요소
 * EN: Graphic element
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Graphic {
    private String id;
    private String xlinkHref;
    private String mimetype;
    private String mimeSubtype;
}
