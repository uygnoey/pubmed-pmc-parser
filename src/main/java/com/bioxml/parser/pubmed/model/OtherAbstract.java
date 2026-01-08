package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * OtherAbstract / 기타 초록
 *
 * DTD: <!ELEMENT OtherAbstract (AbstractText+, CopyrightInformation?)>
 * DTD: <!ATTLIST OtherAbstract
 *          Type (AAMC | AIDS | KIE | PIP | NASA | Publisher) #REQUIRED
 *          Language CDATA "eng">
 *
 * KR: 다국어 초록 또는 특수 목적 초록
 * EN: Multilingual or special purpose abstract
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherAbstract {

    /**
     * 유형 (필수) / Type (required)
     */
    private String type;

    /**
     * 언어 (기본값: "eng") / Language (default: "eng")
     */
    @Builder.Default
    private String language = "eng";

    /**
     * 초록 텍스트 목록 / Abstract text list
     */
    private List<AbstractText> abstractTexts;

    /**
     * 저작권 정보 / Copyright information
     */
    private CopyrightInformation copyrightInformation;
}
