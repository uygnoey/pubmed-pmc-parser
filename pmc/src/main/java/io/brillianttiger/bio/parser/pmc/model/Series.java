package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Series / 시리즈
 *
 * DTD: <!ELEMENT series (#PCDATA | %series-elements;)*>
 * DTD: <!ATTLIST series
 *          content-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          id ID #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang CDATA #IMPLIED
 *          lang-focus CDATA #IMPLIED
 *          lang-focus-custom CDATA #IMPLIED
 *          lang-group CDATA #IMPLIED
 *          lang-source CDATA #IMPLIED
 *          lang-source-custom CDATA #IMPLIED
 *          lang-translate (yes|no) #IMPLIED
 *          lang-variant CDATA #IMPLIED
 *          lang-variant-custom CDATA #IMPLIED
 *      >
 *
 * KR: 서지 인용에서 출판 시리즈의 일부인 간행물 식별
 * EN: Identify publication that is part of a publication series in bibliographic citation
 *
 * Note: <series-title>, <series-text>와 다름
 * Parent: <element-citation>, <mixed-citation>, <nlm-citation>, <product>, <related-article>, <related-object>
 *
 * Example:
 * <ref id="B1">
 *   <mixed-citation publication-type="journal">
 *     <string-name><surname>Fenchel</surname></string-name>
 *     <year>1982</year>
 *     <article-title>Ecology of Heterotropic Microflagellates</article-title>
 *     <source>Marine Ecology</source>
 *     <series>Marine Ecology Progress Series</series>
 *     <volume>8</volume>
 *   </mixed-citation>
 * </ref>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Series {
    /**
     * KR: 콘텐츠 유형
     * EN: Content type
     */
    private String contentType;

    /**
     * KR: 특정 용도
     * EN: Specific use
     */
    private String specificUse;

    /**
     * KR: ID 속성
     * EN: ID attribute
     */
    private String id;

    /**
     * KR: XML base
     * EN: XML base
     */
    private String xmlBase;

    /**
     * KR: XML 언어
     * EN: XML language
     */
    private String xmlLang;

    /**
     * KR: 언어 초점
     * EN: Language focus
     */
    private String langFocus;

    /**
     * KR: 사용자 정의 언어 초점
     * EN: Custom language focus
     */
    private String langFocusCustom;

    /**
     * KR: 언어 그룹
     * EN: Language group
     */
    private String langGroup;

    /**
     * KR: 언어 출처
     * EN: Language source
     */
    private String langSource;

    /**
     * KR: 사용자 정의 언어 출처
     * EN: Custom language source
     */
    private String langSourceCustom;

    /**
     * KR: 번역 여부 (yes, no)
     * EN: Language translate (yes, no)
     */
    private String langTranslate;

    /**
     * KR: 언어 변형
     * EN: Language variant
     */
    private String langVariant;

    /**
     * KR: 사용자 정의 언어 변형
     * EN: Custom language variant
     */
    private String langVariantCustom;

    /**
     * KR: 시리즈명
     * EN: Series name
     */
    private String value;
}
