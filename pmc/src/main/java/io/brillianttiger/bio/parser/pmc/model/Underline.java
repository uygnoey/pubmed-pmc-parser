package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Underline / 밑줄
 *
 * DTD: <!ELEMENT underline (#PCDATA | %all-phrase;)*>
 *
 * KR: 밑줄 서식
 * EN: Underline formatting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Underline {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
