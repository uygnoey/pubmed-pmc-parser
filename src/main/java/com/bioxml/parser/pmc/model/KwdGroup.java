package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * KwdGroup / 키워드 그룹
 *
 * DTD: <!ELEMENT kwd-group (label?, title?, (kwd | compound-kwd | nested-kwd | x)+)>
 * DTD: <!ATTLIST kwd-group
 *          kwd-group-type CDATA #IMPLIED
 *          xml:lang CDATA #IMPLIED>
 *
 * KR: 논문 키워드 그룹
 * EN: Article keyword group
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KwdGroup {

    /**
     * 키워드 그룹 유형 / Keyword group type
     */
    private String kwdGroupType;

    /**
     * 언어 / Language
     */
    private String xmlLang;

    /**
     * 레이블 / Label
     */
    private Label label;

    /**
     * 제목 / Title
     */
    private Title title;

    /**
     * 키워드 목록 / Keyword list
     */
    private List<Kwd> keywords;
}
