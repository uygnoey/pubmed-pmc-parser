package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Size / 크기
 *
 * DTD: <!ELEMENT size (#PCDATA | bold | fixed-case | italic | monospace | overline |
 *          overline-start | overline-end | roman | sans-serif | sc | strike |
 *          underline | underline-start | underline-end | x)*>
 * DTD: <!ATTLIST size
 *          units CDATA #REQUIRED
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
 * KR: 서지 참고문헌 또는 제품 메타데이터에서 자료의 측정값 또는 범위
 * EN: Measurement or extent of material in bibliographic reference or product metadata
 *
 * REQUIRED: units 속성은 필수 (pages, minutes, linear feet 등)
 * Context: <element-citation>, <mixed-citation>, <product>, <related-article>, <related-object>
 *
 * Note:
 * - 기사 메타데이터: 인쇄 쪽 수에 <page-count> 사용
 * - 제품 설명: 쪽 수에 <size> 사용
 * - <page-count>는 <product> 내에 중첩 불가
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Size {
    /**
     * KR: 측정 단위 (필수) - pages, minutes, linear feet 등
     * EN: Measurement units (required) - pages, minutes, linear feet, etc.
     */
    private String units;

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
     * KR: 크기 값
     * EN: Size value
     */
    private String value;
}
