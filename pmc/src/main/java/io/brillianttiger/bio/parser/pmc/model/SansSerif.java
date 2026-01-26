package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sans-Serif / 산세리프체
 *
 * DTD: <!ELEMENT sans-serif (#PCDATA | %all-phrase;)*>
 *
 * KR: 산세리프체 (고딕체, 장식이 없는 폰트) 서식
 * EN: Sans-serif (gothic, non-serif font) formatting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SansSerif {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
