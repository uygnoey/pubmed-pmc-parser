package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Monospace / 고정폭 글꼴
 *
 * DTD: <!ELEMENT monospace (#PCDATA | %all-phrase;)*>
 *
 * KR: 고정폭 (모노스페이스, 코드용) 글꼴 서식
 * EN: Monospace (fixed-width, code) font formatting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Monospace {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
