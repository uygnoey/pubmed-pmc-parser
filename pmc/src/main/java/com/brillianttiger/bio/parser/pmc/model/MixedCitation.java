package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MixedCitation / 혼합 인용
 *
 * KR: 비구조화된 인용 정보 (혼합 콘텐츠). JATS 1.4 DTD 완전 준수 모델.
 *     텍스트와 구조화된 요소가 혼합되어 있음.
 * EN: Unstructured citation information (mixed content). Fully compliant with JATS 1.4 DTD.
 *     Contains a mix of text and structured elements.
 *
 * DTD: <!ELEMENT mixed-citation (#PCDATA | %citation-elements;)*>
 *
 * DTD: <!ATTLIST mixed-citation
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
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/mixed-citation.html
 *
 * Note: Unlike element-citation, mixed-citation preserves the original text formatting
 * and punctuation while also allowing structured elements to be tagged.
 *
 * Example:
 * <mixed-citation publication-type="journal">
 *   <person-group person-group-type="author">
 *     <name><surname>Smith</surname> <given-names>J</given-names></name>,
 *     <name><surname>Doe</surname> <given-names>J</given-names></name>
 *   </person-group>.
 *   <article-title>Example Article Title</article-title>.
 *   <source>Journal of Examples</source>.
 *   <year>2023</year>;<volume>10</volume>(<issue>5</issue>):<fpage>100</fpage>-<lpage>115</lpage>.
 *   doi:<pub-id pub-id-type="doi">10.1234/example.2023.001</pub-id>
 * </mixed-citation>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MixedCitation {

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
    private String publicationType;

    /**
     * 출판 형식 / Publication format
     *
     * KR: 출판물의 형식 (인쇄, 전자 등).
     * EN: Format of publication (print, electronic, etc.).
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
     * XLink 실행 시점 / XLink actuate
     *
     * KR: 링크 활성화 시점.
     * EN: When to activate the link.
     *
     * DTD: xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
     * Required: NO
     */
    private XlinkActuate xlinkActuate;

    /**
     * XLink 표시 방식 / XLink show
     *
     * KR: 링크 리소스 표시 방식.
     * EN: How to display the linked resource.
     *
     * DTD: xlink:show (embed | new | none | other | replace) #IMPLIED
     * Required: NO
     */
    private XlinkShow xlinkShow;

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

    // ========== Mixed Content / 혼합 콘텐츠 ==========

    /**
     * 인용 내용 (원본 텍스트) / Citation content (original text)
     *
     * KR: 인용 내용의 전체 텍스트 (구두점, 공백 포함).
     *     구조화된 요소가 추출된 후의 텍스트 부분도 포함.
     * EN: Full text of the citation (including punctuation, whitespace).
     *     Also contains text parts after structured elements are extracted.
     *
     * DTD: #PCDATA
     * Required: NO
     */
    private String value;

    // ========== Extracted Structured Elements / 추출된 구조화 요소 ==========

    /**
     * 인물 그룹 목록 / Person group list
     *
     * KR: 저자, 편집자 등 인물 그룹 목록.
     * EN: List of person groups (authors, editors, etc.).
     *
     * DTD: %citation-elements; includes person-group
     * Required: NO (0 or more)
     */
    private List<PersonGroup> personGroups;

    /**
     * 협력기관 / Collaborations
     *
     * KR: 협력 기관/그룹 목록.
     * EN: List of collaborating organizations/groups.
     *
     * DTD: %citation-elements; includes collab
     * Required: NO (0 or more)
     */
    private List<Collab> collabs;

    /**
     * et al. 표시 / Et al. indicator
     *
     * KR: "et al." 표시.
     * EN: Et al. indicator.
     *
     * DTD: %citation-elements; includes etal
     * Required: NO
     */
    private Etal etal;

    /**
     * 이름 목록 / Name list
     *
     * KR: 개별 이름 목록 (person-group 없이 직접 포함된 경우).
     * EN: List of individual names (when included directly without person-group).
     *
     * DTD: %citation-elements; includes name
     * Required: NO (0 or more)
     */
    private List<PersonName> names;

    /**
     * 문자열 이름 목록 / String name list
     *
     * KR: 문자열 형태의 이름 목록.
     * EN: List of names in string format.
     *
     * DTD: %citation-elements; includes string-name
     * Required: NO (0 or more)
     */
    private List<StringName> stringNames;

    /**
     * 논문 제목 / Article title
     *
     * KR: 인용 논문의 제목.
     * EN: Title of the cited article.
     *
     * DTD: %citation-elements; includes article-title
     * Required: NO
     */
    private PmcArticleTitle articleTitle;

    /**
     * 챕터 제목 / Chapter title
     *
     * KR: 서적 챕터 제목.
     * EN: Chapter title for books.
     *
     * DTD: %citation-elements; includes chapter-title
     * Required: NO
     */
    private ChapterTitle chapterTitle;

    /**
     * 출처 / Source
     *
     * KR: 인용 출처 (저널명, 책명 등).
     * EN: Source of citation (journal name, book name, etc.).
     *
     * DTD: %citation-elements; includes source
     * Required: NO
     */
    private Source source;

    /**
     * 연도 / Year
     *
     * KR: 출판 연도.
     * EN: Publication year.
     *
     * DTD: %citation-elements; includes year
     * Required: NO
     */
    private Year year;

    /**
     * 월 / Month
     *
     * KR: 출판 월.
     * EN: Publication month.
     *
     * DTD: %citation-elements; includes month
     * Required: NO
     */
    private Month month;

    /**
     * 일 / Day
     *
     * KR: 출판 일.
     * EN: Publication day.
     *
     * DTD: %citation-elements; includes day
     * Required: NO
     */
    private Day day;

    /**
     * 권 / Volume
     *
     * KR: 저널 권 번호.
     * EN: Journal volume number.
     *
     * DTD: %citation-elements; includes volume
     * Required: NO
     */
    private Volume volume;

    /**
     * 호 / Issue
     *
     * KR: 저널 호 번호.
     * EN: Journal issue number.
     *
     * DTD: %citation-elements; includes issue
     * Required: NO
     */
    private PmcIssue issue;

    /**
     * 시작 페이지 / First page
     *
     * KR: 시작 페이지 번호.
     * EN: First page number.
     *
     * DTD: %citation-elements; includes fpage
     * Required: NO
     */
    private Fpage fpage;

    /**
     * 끝 페이지 / Last page
     *
     * KR: 끝 페이지 번호.
     * EN: Last page number.
     *
     * DTD: %citation-elements; includes lpage
     * Required: NO
     */
    private Lpage lpage;

    /**
     * 페이지 범위 / Page range
     *
     * KR: 페이지 범위 문자열.
     * EN: Page range string.
     *
     * DTD: %citation-elements; includes page-range
     * Required: NO
     */
    private PageRange pageRange;

    /**
     * 전자 위치 ID / Electronic location ID
     *
     * KR: 전자 출판물의 위치 ID.
     * EN: Electronic location ID.
     *
     * DTD: %citation-elements; includes elocation-id
     * Required: NO
     */
    private ElocationId elocationId;

    /**
     * 출판사명 / Publisher name
     *
     * KR: 출판사 이름.
     * EN: Publisher name.
     *
     * DTD: %citation-elements; includes publisher-name
     * Required: NO
     */
    private PublisherName publisherName;

    /**
     * 출판사 위치 / Publisher location
     *
     * KR: 출판사 위치/도시.
     * EN: Publisher location/city.
     *
     * DTD: %citation-elements; includes publisher-loc
     * Required: NO
     */
    private PublisherLoc publisherLoc;

    /**
     * 판 / Edition
     *
     * KR: 판 정보.
     * EN: Edition information.
     *
     * DTD: %citation-elements; includes edition
     * Required: NO
     */
    private Edition edition;

    /**
     * 학회명 / Conference name
     *
     * KR: 학회 이름.
     * EN: Conference name.
     *
     * DTD: %citation-elements; includes conf-name
     * Required: NO
     */
    private ConfName confName;

    /**
     * 출판물 ID 목록 / Publication ID list
     *
     * KR: DOI, PMID 등 출판물 식별자 목록.
     * EN: List of publication identifiers (DOI, PMID, etc.).
     *
     * DTD: %citation-elements; includes pub-id
     * Required: NO (0 or more)
     */
    private List<PubId> pubIds;

    /**
     * 코멘트 목록 / Comment list
     *
     * KR: 인용에 대한 코멘트 목록.
     * EN: List of comments on the citation.
     *
     * DTD: %citation-elements; includes comment
     * Required: NO (0 or more)
     */
    private List<Comment> comments;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 외부 링크 목록.
     * EN: List of external links.
     *
     * DTD: %citation-elements; includes ext-link
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     *
     * KR: URI 목록.
     * EN: List of URIs.
     *
     * DTD: %citation-elements; includes uri
     * Required: NO (0 or more)
     */
    private List<Uri> uris;

    /**
     * 주석 / Annotation
     *
     * KR: 인용에 대한 주석.
     * EN: Annotation on the citation.
     *
     * DTD: %citation-elements; includes annotation
     * Required: NO
     */
    private Annotation annotation;
}
