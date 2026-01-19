package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sc (Small Caps) / 작은 대문자
 *
 * DTD: <!ELEMENT sc (#PCDATA | %all-phrase;)*>
 *
 * KR: 작은 대문자 (스몰캡) 서식
 * EN: Small caps formatting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sc {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
