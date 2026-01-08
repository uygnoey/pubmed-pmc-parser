package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ElementCitation / 요소 인용
 *
 * DTD: <!ELEMENT element-citation (%element-citation-model;)*>
 * DTD: <!ATTLIST element-citation
 *          publication-type (book | confproc | gov | journal | other | patent |
 *                            standard | thesis | webpage | software | data | database) #IMPLIED>
 *
 * KR: 구조화된 인용 정보
 * EN: Structured citation information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElementCitation {

    /**
     * 출판 유형 / Publication type
     */
    private String publicationType;

    /**
     * 인물 그룹 목록 / Person group list
     */
    private java.util.List<PersonGroup> personGroups;

    /**
     * 논문 제목 / Article title
     */
    private PmcArticleTitle articleTitle;

    /**
     * 출처 (저널명, 책명 등) / Source (journal name, book name, etc.)
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
     * 일 / Day
     */
    private Day day;

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
     * 페이지 범위 / Page range
     */
    private PageRange pageRange;

    /**
     * 전자 위치 ID / Electronic location ID
     */
    private ElocationId elocationId;

    /**
     * 출판사명 / Publisher name
     */
    private PublisherName publisherName;

    /**
     * 출판사 위치 / Publisher location
     */
    private PublisherLoc publisherLoc;

    /**
     * 판 / Edition
     */
    private Edition edition;

    /**
     * 챕터 제목 / Chapter title
     */
    private ChapterTitle chapterTitle;

    /**
     * 학회명 / Conference name
     */
    private ConfName confName;

    /**
     * 학회 위치 / Conference location
     */
    private ConfLoc confLoc;

    /**
     * DOI 등 article-id 목록 / Article ID list (DOI, etc.)
     */
    private java.util.List<PmcArticleId> articleIds;

    /**
     * 코멘트 / Comment
     */
    private Comment comment;

    /**
     * 외부 링크 목록 / External link list
     */
    private java.util.List<ExtLink> extLinks;
}
