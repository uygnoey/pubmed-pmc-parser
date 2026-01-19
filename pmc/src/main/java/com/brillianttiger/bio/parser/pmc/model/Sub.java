package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sub (Subscript) / 아래첨자
 *
 * DTD: <!ELEMENT sub (#PCDATA | %all-phrase;)*>
 *
 * KR: 아래첨자 서식 (예: H₂O의 ₂)
 * EN: Subscript formatting (e.g., ₂ in H₂O)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sub {
    /**
     * KR: 텍스트 값 (중첩된 formatting 요소 포함 가능)
     * EN: Text value (can contain nested formatting elements)
     */
    private String value;
}
