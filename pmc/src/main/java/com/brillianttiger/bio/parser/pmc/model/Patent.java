package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Patent / 특허
 *
 * DTD: <!ELEMENT patent (#PCDATA | %patent-elements;)*>
 * DTD: <!ATTLIST patent
 *          content-type CDATA #IMPLIED
 *          country CDATA #IMPLIED
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
 * KR: 서지 참고문헌에서 특허 번호 식별
 * EN: Identify patent number in bibliographic reference
 *
 * Context: <element-citation>, <mixed-citation>, <nlm-citation>, <product>, <related-article>, <related-object>
 *
 * Example:
 * <patent country="US">United States patent US 6,980,855</patent>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patent {
    /**
     * KR: 콘텐츠 유형
     * EN: Content type
     */
    private String contentType;

    /**
     * KR: 국가 코드 (예: US, KR, JP 등)
     * EN: Country code (e.g., US, KR, JP, etc.)
     */
    private String country;

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
     * KR: 특허 번호 텍스트
     * EN: Patent number text
     */
    private String value;
}
