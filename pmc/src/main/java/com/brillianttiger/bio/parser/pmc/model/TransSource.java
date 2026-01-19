package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TransSource / 번역 출판원 (DEPRECATED)
 *
 * DTD: <!ELEMENT trans-source (#PCDATA | %source-elements;)*>
 * DTD: <!ATTLIST trans-source
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
 * KR: 원문과 다른 언어로 번역된 출판원 제목 (deprecated - 사용 권장하지 않음)
 * EN: Translated publication source title (deprecated - not recommended)
 *
 * DEPRECATED: 본 요소는 더 이상 사용을 권장하지 않습니다.
 * 권장 대안: 동일한 출처를 다양한 xml:lang 속성을 가진 여러 <source> 요소로 반복하고,
 * @lang-variant 속성으로 원문/번역 구분
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransSource {
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
     * KR: 번역 출판원 제목
     * EN: Translated source title
     */
    private String value;
}
