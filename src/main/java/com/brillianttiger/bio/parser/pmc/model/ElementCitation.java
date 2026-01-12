package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ElementCitation / 요소 인용
 *
 * KR: 구조화된 인용 정보. JATS 1.4 DTD 완전 준수 모델.
 *     모든 인용 요소가 별도 XML 요소로 태그되어 있음.
 * EN: Structured citation information. Fully compliant with JATS 1.4 DTD.
 *     All citation elements are tagged as separate XML elements.
 *
 * DTD: <!ELEMENT element-citation (%element-citation-model;)*>
 *
 * DTD: <!ATTLIST element-citation
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
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/element-citation.html
 *
 * Example:
 * <element-citation publication-type="journal">
 *   <person-group person-group-type="author">
 *     <name><surname>Smith</surname><given-names>John</given-names></name>
 *     <name><surname>Doe</surname><given-names>Jane</given-names></name>
 *   </person-group>
 *   <article-title>Example Article Title</article-title>
 *   <source>Journal of Examples</source>
 *   <year iso-8601-date="2023">2023</year>
 *   <volume>10</volume>
 *   <issue>5</issue>
 *   <fpage>100</fpage>
 *   <lpage>115</lpage>
 *   <pub-id pub-id-type="doi">10.1234/example.2023.001</pub-id>
 *   <pub-id pub-id-type="pmid">12345678</pub-id>
 * </element-citation>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElementCitation {

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
     *
     * Common values: journal, book, confproc, thesis, patent, software, data, webpage, etc.
     */
    private PublicationType publicationType;

    /**
     * 출판 형식 / Publication format
     *
     * KR: 출판물의 형식 (인쇄, 전자 등).
     * EN: Format of publication (print, electronic, etc.).
     *
     * DTD: publication-format CDATA #IMPLIED
     * Required: NO
     *
     * Example: "print", "electronic", "epub"
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
     *
     * Example: "commercial", "government", "university"
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

    // ========== Child Elements - Person/Organization / 자식 요소 - 인물/조직 ==========

    /**
     * 인물 그룹 목록 / Person group list
     *
     * KR: 저자, 편집자 등 인물 그룹 목록.
     * EN: List of person groups (authors, editors, etc.).
     *
     * DTD: person-group*
     * Required: NO (0 or more)
     */
    private List<PersonGroup> personGroups;

    /**
     * 협력기관 / Collaborations
     *
     * KR: 협력 기관/그룹 목록.
     * EN: List of collaborating organizations/groups.
     *
     * DTD: collab*
     * Required: NO (0 or more)
     */
    private List<Collab> collabs;

    /**
     * et al. 표시 / Et al. indicator
     *
     * KR: "et al." 표시 (추가 저자 있음을 나타냄).
     * EN: Et al. indicator (indicates additional authors).
     *
     * DTD: etal?
     * Required: NO
     */
    private Etal etal;

    // ========== Child Elements - Title/Source / 자식 요소 - 제목/출처 ==========

    /**
     * 논문 제목 / Article title
     *
     * KR: 인용 논문의 제목.
     * EN: Title of the cited article.
     *
     * DTD: article-title?
     * Required: NO
     */
    private PmcArticleTitle articleTitle;

    /**
     * 챕터 제목 / Chapter title
     *
     * KR: 서적 챕터 제목.
     * EN: Chapter title for books.
     *
     * DTD: chapter-title?
     * Required: NO
     */
    private ChapterTitle chapterTitle;

    /**
     * 파트 제목 / Part title
     *
     * KR: 파트/섹션 제목.
     * EN: Part or section title.
     *
     * DTD: part-title?
     * Required: NO
     */
    private PartTitle partTitle;

    /**
     * 번역 제목 / Translated title
     *
     * KR: 번역된 제목.
     * EN: Translated title.
     *
     * DTD: trans-title*
     * Required: NO (0 or more)
     */
    private List<TransTitle> transTitles;

    /**
     * 출처 (저널명, 책명 등) / Source (journal name, book name, etc.)
     *
     * KR: 인용 출처 (저널명, 책명 등).
     * EN: Source of citation (journal name, book name, etc.).
     *
     * DTD: source?
     * Required: NO
     */
    private Source source;

    /**
     * 번역 출처 / Translated source
     *
     * KR: 번역된 출처명.
     * EN: Translated source name.
     *
     * DTD: trans-source*
     * Required: NO (0 or more)
     */
    private List<TransSource> transSources;

    // ========== Child Elements - Date / 자식 요소 - 날짜 ==========

    /**
     * 연도 / Year
     *
     * KR: 출판 연도.
     * EN: Publication year.
     *
     * DTD: year?
     * Required: NO
     */
    private Year year;

    /**
     * 월 / Month
     *
     * KR: 출판 월.
     * EN: Publication month.
     *
     * DTD: month?
     * Required: NO
     */
    private Month month;

    /**
     * 일 / Day
     *
     * KR: 출판 일.
     * EN: Publication day.
     *
     * DTD: day?
     * Required: NO
     */
    private Day day;

    /**
     * 계절 / Season
     *
     * KR: 계절 정보 (예: Spring, Summer).
     * EN: Season information (e.g., Spring, Summer).
     *
     * DTD: season?
     * Required: NO
     */
    private Season season;

    /**
     * 날짜 범위 / Date range
     *
     * KR: 날짜 범위 정보.
     * EN: Date range information.
     *
     * DTD: date-in-citation*
     * Required: NO (0 or more)
     */
    private List<DateInCitation> datesInCitation;

    /**
     * 문자열 날짜 / String date
     *
     * KR: 구조화되지 않은 날짜 문자열.
     * EN: Unstructured date string.
     *
     * DTD: string-date?
     * Required: NO
     */
    private StringDate stringDate;

    // ========== Child Elements - Volume/Issue/Page / 자식 요소 - 권/호/페이지 ==========

    /**
     * 권 / Volume
     *
     * KR: 저널 권 번호.
     * EN: Journal volume number.
     *
     * DTD: volume?
     * Required: NO
     */
    private Volume volume;

    /**
     * 호 / Issue
     *
     * KR: 저널 호 번호.
     * EN: Journal issue number.
     *
     * DTD: issue?
     * Required: NO
     */
    private PmcIssue issue;

    /**
     * 보충호 / Supplement
     *
     * KR: 보충호 정보.
     * EN: Supplement information.
     *
     * DTD: supplement?
     * Required: NO
     */
    private Supplement supplement;

    /**
     * 시작 페이지 / First page
     *
     * KR: 시작 페이지 번호.
     * EN: First page number.
     *
     * DTD: fpage?
     * Required: NO
     */
    private Fpage fpage;

    /**
     * 끝 페이지 / Last page
     *
     * KR: 끝 페이지 번호.
     * EN: Last page number.
     *
     * DTD: lpage?
     * Required: NO
     */
    private Lpage lpage;

    /**
     * 페이지 범위 / Page range
     *
     * KR: 페이지 범위 문자열.
     * EN: Page range string.
     *
     * DTD: page-range?
     * Required: NO
     */
    private PageRange pageRange;

    /**
     * 전자 위치 ID / Electronic location ID
     *
     * KR: 전자 출판물의 위치 ID (e-locator).
     * EN: Electronic location ID (e-locator).
     *
     * DTD: elocation-id?
     * Required: NO
     */
    private ElocationId elocationId;

    /**
     * 페이지 수 / Page count
     *
     * KR: 총 페이지 수.
     * EN: Total page count.
     *
     * DTD: page-count?
     * Required: NO
     */
    private PageCount pageCount;

    // ========== Child Elements - Publisher / 자식 요소 - 출판사 ==========

    /**
     * 출판사명 / Publisher name
     *
     * KR: 출판사 이름.
     * EN: Publisher name.
     *
     * DTD: publisher-name?
     * Required: NO
     */
    private PublisherName publisherName;

    /**
     * 출판사 위치 / Publisher location
     *
     * KR: 출판사 위치/도시.
     * EN: Publisher location/city.
     *
     * DTD: publisher-loc?
     * Required: NO
     */
    private PublisherLoc publisherLoc;

    /**
     * 판 / Edition
     *
     * KR: 판 정보 (예: "2nd ed.").
     * EN: Edition information (e.g., "2nd ed.").
     *
     * DTD: edition?
     * Required: NO
     */
    private Edition edition;

    // ========== Child Elements - Conference / 자식 요소 - 학회 ==========

    /**
     * 학회명 / Conference name
     *
     * KR: 학회/컨퍼런스 이름.
     * EN: Conference name.
     *
     * DTD: conf-name?
     * Required: NO
     */
    private ConfName confName;

    /**
     * 학회 위치 / Conference location
     *
     * KR: 학회 개최 장소.
     * EN: Conference location.
     *
     * DTD: conf-loc?
     * Required: NO
     */
    private ConfLoc confLoc;

    /**
     * 학회 날짜 / Conference date
     *
     * KR: 학회 날짜.
     * EN: Conference date.
     *
     * DTD: conf-date?
     * Required: NO
     */
    private ConfDate confDate;

    /**
     * 학회 스폰서 / Conference sponsor
     *
     * KR: 학회 스폰서/주최자.
     * EN: Conference sponsor/organizer.
     *
     * DTD: conf-sponsor*
     * Required: NO (0 or more)
     */
    private List<ConfSponsor> confSponsors;

    // ========== Child Elements - Identifiers / 자식 요소 - 식별자 ==========

    /**
     * 출판물 ID 목록 / Publication ID list
     *
     * KR: DOI, PMID, ISBN 등 출판물 식별자 목록.
     * EN: List of publication identifiers (DOI, PMID, ISBN, etc.).
     *
     * DTD: pub-id*
     * Required: NO (0 or more)
     */
    private List<PubId> pubIds;

    /**
     * ISBN 목록 / ISBN list
     *
     * KR: ISBN 목록.
     * EN: List of ISBNs.
     *
     * DTD: isbn*
     * Required: NO (0 or more)
     */
    private List<PmcIsbn> isbns;

    /**
     * ISSN 목록 / ISSN list
     *
     * KR: ISSN 목록.
     * EN: List of ISSNs.
     *
     * DTD: issn*
     * Required: NO (0 or more)
     */
    private List<Issn> issns;

    // ========== Child Elements - Others / 자식 요소 - 기타 ==========

    /**
     * 코멘트 / Comment
     *
     * KR: 인용에 대한 코멘트.
     * EN: Comment on the citation.
     *
     * DTD: comment*
     * Required: NO (0 or more)
     */
    private List<Comment> comments;

    /**
     * 주석 / Annotation
     *
     * KR: 인용에 대한 주석.
     * EN: Annotation on the citation.
     *
     * DTD: annotation?
     * Required: NO
     */
    private Annotation annotation;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 외부 링크 목록.
     * EN: List of external links.
     *
     * DTD: ext-link*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     *
     * KR: URI 목록.
     * EN: List of URIs.
     *
     * DTD: uri*
     * Required: NO (0 or more)
     */
    private List<Uri> uris;

    /**
     * 특허 번호 / Patent number
     *
     * KR: 특허 번호.
     * EN: Patent number.
     *
     * DTD: patent?
     * Required: NO
     */
    private Patent patent;

    /**
     * 시리즈 / Series
     *
     * KR: 시리즈 정보.
     * EN: Series information.
     *
     * DTD: series?
     * Required: NO
     */
    private Series series;

    /**
     * 크기 / Size
     *
     * KR: 자료의 크기 정보.
     * EN: Size of the material.
     *
     * DTD: size?
     * Required: NO
     */
    private Size size;

    /**
     * 데이터 제목 / Data title
     *
     * KR: 데이터셋 제목.
     * EN: Dataset title.
     *
     * DTD: data-title?
     * Required: NO
     */
    private DataTitle dataTitle;

    /**
     * 버전 / Version
     *
     * KR: 소프트웨어/데이터 버전.
     * EN: Software/data version.
     *
     * DTD: version?
     * Required: NO
     */
    private Version version;

    /**
     * 접근 날짜 / Access date
     *
     * KR: 웹 자료 접근 날짜.
     * EN: Date of access for web resources.
     *
     * DTD: date-in-citation (with content-type="access-date")*
     * Required: NO
     */
    private DateInCitation accessDate;
}
