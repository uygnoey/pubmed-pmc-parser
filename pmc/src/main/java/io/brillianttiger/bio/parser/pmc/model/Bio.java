package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Bio / 약력
 *
 * KR: 저자 약력 (논문 뒷부분에 포함되는 저자 소개). JATS 1.4 DTD 완전 준수 모델.
 * EN: Author biography (author introduction at the end of article). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT bio (label?, title?, (%sec-opt-title-model;)*, (sec)*)>
 *
 * DTD: <!ATTLIST bio
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          rid IDREFS #IMPLIED
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
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/bio.html
 *
 * Example:
 * <bio id="bio1">
 *   <title>Author Biography</title>
 *   <p><bold>John Doe</bold> is a Professor of Molecular Biology at University of Example.
 *   He received his Ph.D. from MIT in 2005 and has published over 100 papers
 *   in the field of cancer research.</p>
 *   <graphic xlink:href="author-photo.jpg"/>
 * </bio>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bio {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 약력의 콘텐츠 유형.
     * EN: Type of content in the biography.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     */
    private String contentType;

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
     * 참조 ID 목록 / Reference ID list
     *
     * KR: 이 약력이 참조하는 저자의 ID 목록.
     * EN: List of author IDs this biography refers to.
     *
     * DTD: rid IDREFS #IMPLIED
     * Required: NO
     */
    private String rid;

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
     * XLink 실행 시점 / XLink actuate
     *
     * KR: 외부 약력 링크 활성화 시점.
     * EN: When to activate the external biography link.
     *
     * DTD: xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
     * Required: NO
     */
    private XlinkActuate xlinkActuate;

    /**
     * XLink href / XLink href
     *
     * KR: 외부 약력 파일 경로 또는 URL.
     * EN: Path or URL to external biography file.
     *
     * DTD: xlink:href CDATA #IMPLIED
     * Required: NO
     */
    private String xlinkHref;

    /**
     * XLink role / XLink role
     *
     * KR: 링크의 역할을 설명하는 URI.
     * EN: URI describing the role of the link.
     *
     * DTD: xlink:role CDATA #IMPLIED
     * Required: NO
     */
    private String xlinkRole;

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
     * XLink 제목 / XLink title
     *
     * KR: 링크에 대한 사람이 읽을 수 있는 제목.
     * EN: Human-readable title for the link.
     *
     * DTD: xlink:title CDATA #IMPLIED
     * Required: NO
     */
    private String xlinkTitle;

    /**
     * XLink 타입 / XLink type
     *
     * KR: XLink 타입 (항상 "simple").
     * EN: XLink type (always "simple").
     *
     * DTD: xlink:type (simple) #IMPLIED
     * Required: NO
     */
    private String xlinkType;

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
     * 텍스트 내용 / Text content
     *
     * KR: 약력의 텍스트 내용 (혼합 콘텐츠).
     * EN: Text content of the biography (mixed content).
     *
     * DTD: (#PCDATA | %sec-opt-title-model;)*
     * Required: NO
     */
    private String value;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 레이블 / Label
     *
     * KR: 약력의 레이블.
     * EN: Label for the biography.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 약력의 제목 (예: "About the Author").
     * EN: Title of the biography (e.g., "About the Author").
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 약력 내용 단락 목록.
     * EN: List of paragraphs in the biography.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 약력 내 섹션 목록.
     * EN: List of sections in the biography.
     *
     * DTD: (sec)*
     * Required: NO (0 or more)
     */
    private List<Sec> sections;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 약력 내 그래픽 목록 (저자 사진 등).
     * EN: List of graphics in the biography (author photos, etc.).
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 약력 내 미디어 목록.
     * EN: List of media in the biography.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 약력 관련 외부 링크 목록 (개인 웹사이트, ORCID 등).
     * EN: List of external links related to biography (personal website, ORCID, etc.).
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;
}
