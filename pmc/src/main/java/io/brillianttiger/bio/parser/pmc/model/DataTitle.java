package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DataTitle / 데이터 제목
 *
 * DTD: <!ELEMENT data-title (#PCDATA | %data-title-elements;)*>
 * DTD: <!ATTLIST data-title
 *          id ID #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
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
 * KR: 인용된 데이터 출처(또는 그 구성 요소)의 공식 제목/이름
 * EN: Official title/name of cited data source (or its component)
 *
 * Usage: 데이터셋이나 스프레드시트의 제목 등
 * Context: <element-citation>, <mixed-citation>, <product>, <related-article>, <related-object>
 *
 * Recommendation: 데이터셋 인용에서 계층적 관계를 명확히 하기 위해
 * <data-title>(논문 동등 제목용)과 <source>(데이터 저장소명)을 함께 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataTitle {
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
     * KR: 데이터 제목 텍스트
     * EN: Data title text
     */
    private String value;
}
