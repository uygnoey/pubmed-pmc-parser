package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * KeywordList / 키워드 목록
 *
 * DTD: <!ELEMENT KeywordList (Keyword+)>
 * DTD: <!ATTLIST KeywordList Owner (NLM | NLM-AUTO | NASA | PIP | KIE | NOTNLM | HHS) "NLM">
 *
 * KR: 논문 키워드 목록
 * EN: Article keyword list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeywordList {

    /**
     * 소유자 (기본값: NLM) / Owner (default: NLM)
     */
    @Builder.Default
    private KeywordOwner owner = KeywordOwner.NLM;

    /**
     * 키워드 목록 / Keyword list
     */
    private List<Keyword> keywords;
}
