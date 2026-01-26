package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Strike / 취소선
 *
 * DTD: <!ELEMENT strike (#PCDATA | %all-phrase;)*>
 *
 * KR: 취소선 서식 (가운데 줄)
 * EN: Strikethrough formatting (line through text)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Strike {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
