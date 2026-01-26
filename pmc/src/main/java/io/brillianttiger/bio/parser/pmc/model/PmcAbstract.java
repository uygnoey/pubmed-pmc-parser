package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PmcAbstract / PMC 초록
 *
 * DTD: <!ELEMENT abstract (label?, title?, (%abstract-model;)*, (%sec-back-matter-mix;)*)>
 * DTD: <!ATTLIST abstract
 *          abstract-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          xml:lang CDATA #IMPLIED>
 *
 * KR: 논문 초록
 * EN: Article abstract
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmcAbstract {

    /**
     * 초록 유형 / Abstract type
     */
    private String abstractType;

    /**
     * ID 속성 / ID attribute
     */
    private String id;

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
     * 문단 목록 / Paragraph list
     */
    private List<P> paragraphs;

    /**
     * 섹션 목록 / Section list
     */
    private List<Sec> sections;
}
