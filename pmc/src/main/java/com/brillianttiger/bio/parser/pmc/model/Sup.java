package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sup (Superscript) / 위첨자
 *
 * DTD: <!ELEMENT sup (#PCDATA | %all-phrase;)*>
 *
 * KR: 위첨자 서식 (예: x²의 ²)
 * EN: Superscript formatting (e.g., ² in x²)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sup {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
