package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Language / 언어
 *
 * DTD: <!ELEMENT Language (#PCDATA)>
 *
 * KR: 논문의 언어 코드 (ISO 639-2)
 * EN: Article language code (ISO 639-2)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    /**
     * 언어 코드 (예: "eng", "kor") / Language code
     */
    private String value;
}
