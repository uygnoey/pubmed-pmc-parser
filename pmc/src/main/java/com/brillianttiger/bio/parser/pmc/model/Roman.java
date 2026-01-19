package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Roman / 로마체
 *
 * DTD: <!ELEMENT roman (#PCDATA | %all-phrase;)*>
 *
 * KR: 로마체 (정자체, serif 폰트) 서식
 * EN: Roman (serif font) formatting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Roman {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
