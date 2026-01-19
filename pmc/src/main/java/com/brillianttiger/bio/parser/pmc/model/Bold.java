package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bold / 굵은 글씨
 *
 * DTD: <!ELEMENT bold (#PCDATA | %all-phrase;)*>
 *
 * KR: 굵은 글씨 (볼드체) 서식
 * EN: Bold formatting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bold {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
