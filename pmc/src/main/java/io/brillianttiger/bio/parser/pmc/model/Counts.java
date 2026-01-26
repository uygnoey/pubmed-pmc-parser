package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Counts / 카운트
 *
 * KR: 논문 요소 카운트 (페이지, 그림, 표, 참조문헌 등)
 * EN: Article element counts (pages, figures, tables, references, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Counts {
    private Integer pageCount;
    private Integer figCount;
    private Integer tableCount;
    private Integer equationCount;
    private Integer refCount;
    private Integer wordCount;
    private String value;
}
