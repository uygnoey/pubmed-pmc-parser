package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Version / 버전
 *
 * DTD: <!ELEMENT version (#PCDATA | %version-elements;)*>
 * DTD: <!ATTLIST version
 *          content-type CDATA #IMPLIED
 *          designator CDATA #IMPLIED
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
 * KR: 인용되거나 설명되는 데이터 또는 소프트웨어의 완전한 버전 명령문
 * EN: Complete version statement of cited or described data or software
 *
 * Usage: 단순 번호("16")부터 복잡한 명령문("Fifth PC version, patches 2-3")까지 지원
 * Note: @designator 속성으로 단순 버전 번호 지정 가능
 * Context: <mixed-citation>, <element-citation>, <product>, <related-article>, <related-object>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Version {
    /**
     * KR: 콘텐츠 유형
     * EN: Content type
     */
    private String contentType;

    /**
     * KR: 버전 지정자 (단순 번호)
     * EN: Version designator (simple number)
     */
    private String designator;

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
     * KR: 버전 텍스트
     * EN: Version text
     */
    private String value;
}
