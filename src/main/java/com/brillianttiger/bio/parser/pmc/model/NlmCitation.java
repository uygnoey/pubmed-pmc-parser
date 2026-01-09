package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * NlmCitation / NLM 인용
 *
 * KR: NLM 스타일 인용 (deprecated). JATS 1.4 DTD 완전 준수 모델.
 *     하위 호환성을 위해 유지됨. 새로운 콘텐츠에는 element-citation 또는 mixed-citation 사용 권장.
 * EN: NLM-style citation (deprecated). Fully compliant with JATS 1.4 DTD.
 *     Kept for backward compatibility. For new content, use element-citation or mixed-citation.
 *
 * DTD: <!ELEMENT nlm-citation (%nlm-citation-model;)*>
 *
 * DTD: <!ATTLIST nlm-citation
 *          id ID #IMPLIED
 *          publication-type CDATA #IMPLIED
 *          publication-format CDATA #IMPLIED
 *          publisher-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *          xlink:href CDATA #IMPLIED
 *          xlink:role CDATA #IMPLIED
 *          xlink:show (embed | new | none | other | replace) #IMPLIED
 *          xlink:title CDATA #IMPLIED
 *          xlink:type (simple) #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/nlm-citation.html
 *
 * Note: This element is deprecated in favor of element-citation and mixed-citation.
 * It is retained in the DTD for backward compatibility with older content.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NlmCitation {

    // ========== Attributes / 속성 ==========

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자.
     * EN: Unique identifier within the XML document.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     */
    private String id;

    /**
     * 출판 유형 / Publication type
     *
     * KR: 인용의 출판물 유형.
     * EN: Type of publication being cited.
     *
     * DTD: publication-type CDATA #IMPLIED
     * Required: NO
     */
    private PublicationType publicationType;

    /**
     * 출판 형식 / Publication format
     *
     * KR: 출판물의 형식.
     * EN: Format of publication.
     *
     * DTD: publication-format CDATA #IMPLIED
     * Required: NO
     */
    private String publicationFormat;

    /**
     * 출판사 유형 / Publisher type
     *
     * KR: 출판사의 유형.
     * EN: Type of publisher.
     *
     * DTD: publisher-type CDATA #IMPLIED
     * Required: NO
     */
    private String publisherType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 특정 용도나 목적을 나타내는 문자열.
     * EN: String indicating specific use or purpose.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * XLink href / XLink href
     *
     * KR: 인용 원문에 대한 링크 URL.
     * EN: URL link to the cited source.
     *
     * DTD: xlink:href CDATA #IMPLIED
     * Required: NO
     */
    private String xlinkHref;

    /**
     * XML Base / XML base
     *
     * KR: 상대 URI 해석을 위한 기본 URI.
     * EN: Base URI for resolving relative URIs.
     *
     * DTD: xml:base CDATA #IMPLIED
     * Required: NO
     */
    private String xmlBase;

    /**
     * XML 언어 / XML language
     *
     * KR: 내용의 언어 코드 (ISO 639).
     * EN: Language code for content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 인물 그룹 목록 / Person group list
     */
    private List<PersonGroup> personGroups;

    /**
     * 협력기관 / Collaborations
     */
    private List<Collab> collabs;

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
     * 출판물 ID 목록 / Publication ID list
     */
    private List<PubId> pubIds;

    /**
     * 코멘트 목록 / Comment list
     */
    private List<Comment> comments;

    /**
     * 외부 링크 목록 / External link list
     */
    private List<ExtLink> extLinks;
}
