package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product / 제품 정보
 *
 * DTD: <!ELEMENT product (#PCDATA | %product-elements;)*>
 * DTD: <!ATTLIST product
 *          id ID #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang CDATA #IMPLIED
 *          product-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          hreflang CDATA #IMPLIED
 *          xlink:href CDATA #IMPLIED
 *          xlink:type (simple) #IMPLIED
 *          xlink:role CDATA #IMPLIED
 *          xlink:title CDATA #IMPLIED
 *          xlink:show (new|replace|embed|other|none) #IMPLIED
 *          xlink:actuate (onLoad|onRequest|other|none) #IMPLIED
 *          lang-focus CDATA #IMPLIED
 *          lang-source CDATA #IMPLIED
 *          lang-variant CDATA #IMPLIED
 *          lang-translate (yes|no) #IMPLIED
 *      >
 *
 * KR: 도서, 소프트웨어, 웹사이트, 하드웨어 등 제품 메타데이터 컨테이너
 * EN: Container for product metadata (books, software, websites, hardware)
 *
 * Usage: 주로 book-review, product-review 논문에서 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
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
     * KR: 제품 유형
     * EN: Product type
     */
    private String productType;

    /**
     * KR: 특정 용도
     * EN: Specific use
     */
    private String specificUse;

    /**
     * KR: 링크 언어
     * EN: Link language
     */
    private String hreflang;

    /**
     * KR: XLink href
     * EN: XLink href
     */
    private String xlinkHref;

    /**
     * KR: XLink type (항상 "simple")
     * EN: XLink type (always "simple")
     */
    private String xlinkType;

    /**
     * KR: XLink role
     * EN: XLink role
     */
    private String xlinkRole;

    /**
     * KR: XLink title
     * EN: XLink title
     */
    private String xlinkTitle;

    /**
     * KR: XLink show (new, replace, embed, other, none)
     * EN: XLink show (new, replace, embed, other, none)
     */
    private String xlinkShow;

    /**
     * KR: XLink actuate (onLoad, onRequest, other, none)
     * EN: XLink actuate (onLoad, onRequest, other, none)
     */
    private String xlinkActuate;

    /**
     * KR: 언어 초점
     * EN: Language focus
     */
    private String langFocus;

    /**
     * KR: 언어 출처
     * EN: Language source
     */
    private String langSource;

    /**
     * KR: 언어 변형
     * EN: Language variant
     */
    private String langVariant;

    /**
     * KR: 번역 여부 (yes, no)
     * EN: Language translate (yes, no)
     */
    private String langTranslate;

    /**
     * KR: 제품 정보 텍스트
     * EN: Product information text
     */
    private String value;
}
