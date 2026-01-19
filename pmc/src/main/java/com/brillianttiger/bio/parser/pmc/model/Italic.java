package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Italic / 이탤릭체
 *
 * DTD: <!ELEMENT italic (#PCDATA | %all-phrase;)*>
 *
 * KR: 이탤릭(기울임) 서식
 * EN: Italic formatting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Italic {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
