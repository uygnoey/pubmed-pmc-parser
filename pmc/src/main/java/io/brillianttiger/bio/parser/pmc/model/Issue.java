package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Issue / 호 (FrontStub용)
 *
 * DTD: <!ELEMENT issue (#PCDATA | %issue-elements;)*>
 * DTD: <!ATTLIST issue
 *          content-type CDATA #IMPLIED
 *          seq CDATA #IMPLIED
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
 * KR: 저널의 호 번호 또는 드물게 도서의 호 번호
 * EN: Journal issue number or rarely book issue number
 *
 * Usage:
 * - 기사 메타데이터 (<article-meta>)
 * - 서지 인용
 * - 관련 기사/객체 정보
 *
 * Tips:
 * - 합병호(예: "2-3")는 한 요소에 모두 포함
 * - @content-type 속성으로 권(volume) 관련 호 수와 출판 순서 번호 구분 가능
 * - 동일한 정보를 다양한 @content-type으로 여러 <issue> 요소로 반복 가능
 *
 * Note: PmcIssue는 간단한 문자열 값만, Issue는 모든 속성 포함
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issue {
    /**
     * KR: 콘텐츠 유형 (volume-related, publication-order 등)
     * EN: Content type (volume-related, publication-order, etc.)
     */
    private String contentType;

    /**
     * KR: 순서 번호
     * EN: Sequence number
     */
    private String seq;

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
     * KR: 호 번호 텍스트
     * EN: Issue number text
     */
    private String value;
}
