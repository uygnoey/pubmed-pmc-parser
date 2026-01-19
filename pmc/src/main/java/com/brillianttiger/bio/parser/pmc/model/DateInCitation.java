package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DateInCitation / 인용문헌 내 날짜
 *
 * DTD: <!ELEMENT date-in-citation (#PCDATA | day | era | month | season | year |
 *          bold | fixed-case | italic | monospace | overline | overline-start |
 *          overline-end | roman | sans-serif | sc | strike | underline |
 *          underline-start | underline-end | ruby | sub | sup | x)*>
 * DTD: <!ATTLIST date-in-citation
 *          calendar CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          iso-8601-date CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          id ID #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang CDATA #IMPLIED
 *          lang-focus CDATA #IMPLIED
 *          lang-source CDATA #IMPLIED
 *          lang-variant CDATA #IMPLIED
 *          lang-translate (yes|no) #IMPLIED
 *      >
 *
 * KR: 서지 참고문헌 내 출판 이외의 날짜 (접근일, 저작권일, 특허출원일, 타임스탬프 등)
 * EN: Non-publication date within bibliographic reference (access date, copyright date, patent application date, timestamp)
 *
 * Usage: <access-date>, <time-stamp> deprecated 요소를 대체
 * Note: 출판 날짜는 <year>, <month>, <date>, <day> 사용
 * Recommendation: iso-8601-date 속성으로 기계가독 형식 제공 권장
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateInCitation {
    /**
     * KR: 달력 체계 (예: Gregorian, Islamic 등)
     * EN: Calendar system (e.g., Gregorian, Islamic)
     */
    private String calendar;

    /**
     * KR: 콘텐츠 유형 (access-date, copyright-date, patent-application-date 등)
     * EN: Content type (access-date, copyright-date, patent-application-date, etc.)
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
     * KR: 날짜 텍스트 (day, month, year 등 포함 가능)
     * EN: Date text (may include day, month, year, etc.)
     */
    private String value;
}
