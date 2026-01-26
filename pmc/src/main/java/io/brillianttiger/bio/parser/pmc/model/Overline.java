package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Overline / 윗줄
 *
 * DTD: <!ELEMENT overline (#PCDATA | %all-phrase;)*>
 *
 * KR: 윗줄 서식 (텍스트 위에 줄)
 * EN: Overline formatting (line above text)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Overline {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
