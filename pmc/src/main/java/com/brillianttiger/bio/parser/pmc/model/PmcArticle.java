package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PmcArticle / PMC 논문
 *
 * DTD: <!ELEMENT article (front, body?, back?, floats-group?, sub-article*, response*)>
 * DTD: <!ATTLIST article
 *          article-type CDATA #IMPLIED
 *          dtd-version CDATA #IMPLIED
 *          xml:lang CDATA "en">
 *
 * KR: JATS/NLM 형식의 PMC 논문 전체 구조
 * EN: Complete PMC article structure in JATS/NLM format
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcArticle {

    /**
     * 논문 유형 / Article type
     */
    private String articleType;

    /**
     * DTD 버전 / DTD version
     */
    private String dtdVersion;

    /**
     * 언어 (기본값: "en") / Language (default: "en")
     */
    @Builder.Default
    private String xmlLang = "en";

    /**
     * 전면부 (필수) / Front matter (required)
     */
    private Front front;

    /**
     * 본문 / Body
     */
    private Body body;

    /**
     * 후면부 / Back matter
     */
    private Back back;

    /**
     * Floats 그룹 / Floats group
     */
    private FloatsGroup floatsGroup;

    /**
     * 하위 논문 목록 / Sub-article list
     */
    private List<SubArticle> subArticles;

    /**
     * 응답 목록 / Response list
     */
    private List<Response> responses;
}
