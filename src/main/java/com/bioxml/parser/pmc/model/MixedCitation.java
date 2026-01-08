package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MixedCitation / 혼합 인용
 *
 * DTD: <!ELEMENT mixed-citation (#PCDATA | %citation-elements;)*>
 * DTD: <!ATTLIST mixed-citation publication-type CDATA #IMPLIED>
 *
 * KR: 비구조화된 인용 정보 (혼합 콘텐츠)
 * EN: Unstructured citation information (mixed content)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MixedCitation {

    /**
     * 출판 유형 / Publication type
     */
    private String publicationType;

    /**
     * 인용 내용 (텍스트와 구조화 요소 혼합) / Citation content (mixed text and structured elements)
     */
    private String value;

    /**
     * 인물 그룹 목록 / Person group list
     */
    private java.util.List<PersonGroup> personGroups;

    /**
     * 논문 제목 / Article title
     */
    private PmcArticleTitle articleTitle;

    /**
     * 출처 / Source
     */
    private Source source;

    /**
     * 연도 / Year
     */
    private Year year;

    /**
     * 월 / Month
     */
    private Month month;

    /**
     * 권 / Volume
     */
    private Volume volume;

    /**
     * 호 / Issue
     */
    private PmcIssue issue;

    /**
     * 시작 페이지 / First page
     */
    private Fpage fpage;

    /**
     * 끝 페이지 / Last page
     */
    private Lpage lpage;

    /**
     * 출판사명 / Publisher name
     */
    private PublisherName publisherName;

    /**
     * 외부 링크 목록 / External link list
     */
    private java.util.List<ExtLink> extLinks;
}
