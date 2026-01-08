package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Sec / 섹션
 *
 * DTD: <!ELEMENT sec (sec-meta?, label?, title?, (%sec-model;)*, (%sec-back-matter-mix;)*, sec*)>
 * DTD: <!ATTLIST sec
 *          id ID #IMPLIED
 *          sec-type CDATA #IMPLIED
 *          xml:lang CDATA #IMPLIED>
 *
 * KR: 논문 섹션 (재귀 구조)
 * EN: Article section (recursive structure)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sec {

    /**
     * ID 속성 / ID attribute
     */
    private String id;

    /**
     * 섹션 유형 / Section type
     */
    private String secType;

    /**
     * 언어 / Language
     */
    private String xmlLang;

    /**
     * 섹션 메타데이터 / Section metadata
     */
    private SecMeta secMeta;

    /**
     * 레이블 / Label
     */
    private Label label;

    /**
     * 제목 / Title
     */
    private Title title;

    /**
     * 문단 목록 / Paragraph list
     */
    private List<P> paragraphs;

    /**
     * 하위 섹션 목록 (재귀) / Sub-section list (recursive)
     */
    private List<Sec> sections;
}
