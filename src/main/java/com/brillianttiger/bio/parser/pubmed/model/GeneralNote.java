package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GeneralNote / 일반 노트
 *
 * DTD: <!ELEMENT GeneralNote (#PCDATA)>
 * DTD: <!ATTLIST GeneralNote Owner (NLM | NASA | PIP | KIE | HSR | HMD) "NLM">
 *
 * KR: 일반 메모
 * EN: General note
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralNote {

    /**
     * 소유자 (기본값: "NLM") / Owner (default: "NLM")
     */
    @Builder.Default
    private String owner = "NLM";

    /**
     * 노트 내용 / Note content
     */
    private String value;
}
