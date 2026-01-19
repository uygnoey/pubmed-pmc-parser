package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConfDate / 학술대회 날짜
 *
 * DTD: <!ELEMENT conf-date (#PCDATA | day | era | month | season | year |
 *          bold | fixed-case | italic | monospace | overline | overline-start |
 *          overline-end | roman | sans-serif | sc | strike | underline |
 *          underline-start | underline-end | x)*>
 * DTD: <!ATTLIST conf-date
 *          calendar CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          iso-8601-date CDATA #IMPLIED
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
 * KR: 학술대회가 개최된 날짜
 * EN: Date when conference was held
 *
 * Recommendation: 별도의 시작/종료 날짜가 있는 경우 단일 <conf-date> 요소로 통합하고,
 * @iso-8601-date 속성으로 기계가독 형식 제공
 *
 * Example:
 * <conference>
 *   <conf-name>23rd International Summer School of Brain Research</conf-name>
 *   <conf-date iso-8601-date="2003-08-25">2003 Aug 25-29</conf-date>
 * </conference>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfDate {
    /**
     * KR: 달력 체계
     * EN: Calendar system
     */
    private String calendar;

    /**
     * KR: 콘텐츠 유형
     * EN: Content type
     */
    private String contentType;

    /**
     * KR: ISO 8601 형식 날짜 (기계가독)
     * EN: ISO 8601 format date (machine-readable)
     */
    private String iso8601Date;

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
     * KR: 학술대회 날짜 텍스트
     * EN: Conference date text
     */
    private String value;
}
