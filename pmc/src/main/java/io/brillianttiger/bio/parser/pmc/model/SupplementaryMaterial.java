package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SupplementaryMaterial / 보충 자료
 *
 * KR: 논문의 보충 자료 요소. JATS 1.4 DTD 완전 준수 모델.
 *     비디오, 데이터셋, 추가 그림 등 본문 외 자료 포함.
 * EN: Supplementary material element for article. Fully compliant with JATS 1.4 DTD.
 *     Contains additional materials like videos, datasets, extra figures.
 *
 * DTD: <!ELEMENT supplementary-material (
 *          (object-id)*,
 *          label?,
 *          (caption)*,
 *          abstract*,
 *          kwd-group*,
 *          alt-text*,
 *          long-desc*,
 *          (email | ext-link | uri)*,
 *          (alternatives | disp-formula | disp-formula-group |
 *           chem-struct-wrap | disp-quote | speech | statement |
 *           verse-group | table-wrap | p | def-list | list |
 *           array | code | graphic | media | preformat)*,
 *          (attrib | permissions)*
 *      )>
 *
 * DTD: <!ATTLIST supplementary-material
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          mime-subtype CDATA #IMPLIED
 *          mimetype CDATA #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          position (anchor | background | float | margin) #IMPLIED
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
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/supplementary-material.html
 *
 * Example:
 * <supplementary-material id="S1" content-type="video"
 *                        xlink:href="video1.mp4" mimetype="video" mime-subtype="mp4">
 *     <label>Video 1</label>
 *     <caption><p>Experimental procedure demonstration</p></caption>
 *     <media xlink:href="video1.mp4" mimetype="video" mime-subtype="mp4"/>
 * </supplementary-material>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplementaryMaterial {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 보충 자료의 콘텐츠 유형 (video, data, figure 등).
     * EN: Content type of supplementary material (video, data, figure, etc.).
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values: video, audio, data, figure, table, code
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
     *
     * Example: "S1", "suppl-data-1"
     */
    private String id;

    /**
     * MIME 서브타입 / MIME subtype
     *
     * KR: 파일의 MIME 서브타입 (예: mp4, csv, xlsx).
     * EN: File's MIME subtype (e.g., mp4, csv, xlsx).
     *
     * DTD: mime-subtype CDATA #IMPLIED
     * Required: NO
     *
     * Common values: mp4, csv, xlsx, pdf, zip
     */
    private String mimeSubtype;

    /**
     * MIME 타입 / MIME type
     *
     * KR: 파일의 MIME 타입 (예: video, application, text).
     * EN: File's MIME type (e.g., video, application, text).
     *
     * DTD: mimetype CDATA #IMPLIED
     * Required: NO
     *
     * Common values: video, audio, application, text, image
     */
    private String mimetype;

    /**
     * 방향 / Orientation
     *
     * KR: 콘텐츠의 표시 방향.
     * EN: Display orientation of the content.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     */
    private Orientation orientation;

    /**
     * 위치 / Position
     *
     * KR: 콘텐츠의 배치 위치.
     * EN: Placement position of the content.
     *
     * DTD: position (anchor | background | float | margin) #IMPLIED
     * Required: NO
     */
    private Position position;

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
     * KR: 링크 리소스 활성화 시점.
     * EN: When to activate the linked resource.
     *
     * DTD: xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
     * Required: NO
     */
    private XlinkActuate xlinkActuate;

    /**
     * XLink href / XLink href
     *
     * KR: 보충 자료 파일 경로 또는 URL.
     * EN: Path or URL to the supplementary material file.
     *
     * DTD: xlink:href CDATA #IMPLIED
     * Required: NO
     *
     * Example: "supplementary/data.xlsx", "https://example.com/video.mp4"
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
     * Fixed: "simple"
     */
    @Builder.Default
    private String xlinkType = "simple";

    /**
     * XML Base / XML Base
     *
     * KR: 상대 URI 해석을 위한 기본 URI.
     * EN: Base URI for resolving relative URIs.
     *
     * DTD: xml:base CDATA #IMPLIED
     * Required: NO
     */
    private String xmlBase;

    /**
     * XML 언어 / XML Language
     *
     * KR: 콘텐츠의 언어 코드.
     * EN: Language code of the content.
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "ko", "de"
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 객체 ID 목록 / Object ID list
     *
     * KR: 보충 자료의 대체 식별자 목록.
     * EN: List of alternative identifiers for the supplementary material.
     *
     * DTD: (object-id)*
     * Required: NO (0 or more)
     */
    private List<ObjectId> objectIds;

    /**
     * 레이블 / Label
     *
     * KR: 보충 자료 레이블 (예: "Video 1", "Data S1").
     * EN: Label for supplementary material (e.g., "Video 1", "Data S1").
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 캡션 목록 / Caption list
     *
     * KR: 보충 자료에 대한 캡션 목록.
     * EN: List of captions for the supplementary material.
     *
     * DTD: (caption)*
     * Required: NO (0 or more)
     */
    private List<Caption> captions;

    /**
     * 초록 목록 / Abstract list
     *
     * KR: 보충 자료에 대한 초록/요약 목록.
     * EN: List of abstracts/summaries for the supplementary material.
     *
     * DTD: abstract*
     * Required: NO (0 or more)
     */
    private List<PmcAbstract> abstracts;

    /**
     * 키워드 그룹 목록 / Keyword group list
     *
     * KR: 보충 자료 관련 키워드 그룹 목록.
     * EN: List of keyword groups related to the supplementary material.
     *
     * DTD: kwd-group*
     * Required: NO (0 or more)
     */
    private List<KwdGroup> kwdGroups;

    /**
     * 대체 텍스트 목록 / Alternative text list
     *
     * KR: 접근성을 위한 대체 텍스트 목록.
     * EN: Alternative text for accessibility.
     *
     * DTD: alt-text*
     * Required: NO (0 or more)
     */
    private List<AltText> altTexts;

    /**
     * 긴 설명 목록 / Long description list
     *
     * KR: 접근성을 위한 긴 설명 목록.
     * EN: Long description for accessibility.
     *
     * DTD: long-desc*
     * Required: NO (0 or more)
     */
    private List<LongDesc> longDescs;

    /**
     * 이메일 목록 / Email list
     *
     * KR: 보충 자료 관련 이메일 주소 목록.
     * EN: List of email addresses related to the supplementary material.
     *
     * DTD: email*
     * Required: NO (0 or more)
     */
    private List<Email> emails;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 관련 외부 링크 목록.
     * EN: List of external links.
     *
     * DTD: ext-link*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     *
     * KR: 관련 URI 목록.
     * EN: List of URIs.
     *
     * DTD: uri*
     * Required: NO (0 or more)
     */
    private List<Uri> uris;

    // ========== Display Content / 표시 콘텐츠 ==========

    /**
     * 대안 목록 / Alternatives list
     *
     * KR: 다양한 포맷의 동일 콘텐츠 대안 목록.
     * EN: List of alternative representations of the same content.
     *
     * DTD: alternatives*
     * Required: NO (0 or more)
     */
    private List<Alternatives> alternatives;

    /**
     * 테이블 래퍼 목록 / Table wrapper list
     *
     * KR: 보충 자료 내 테이블 래퍼 목록.
     * EN: List of table wrappers within the supplementary material.
     *
     * DTD: table-wrap*
     * Required: NO (0 or more)
     */
    private List<TableWrap> tableWraps;

    /**
     * 문단 목록 / Paragraph list
     *
     * KR: 보충 자료 내 문단 목록.
     * EN: List of paragraphs within the supplementary material.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 정의 목록 / Definition list
     *
     * KR: 정의 목록 모음.
     * EN: Definition lists.
     *
     * DTD: def-list*
     * Required: NO (0 or more)
     */
    private List<DefList> defLists;

    /**
     * 목록 목록 / List of lists
     *
     * KR: 순서 있는/없는 목록 모음.
     * EN: Ordered/unordered lists.
     *
     * DTD: list*
     * Required: NO (0 or more)
     */
    private List<PmcList> lists;

    /**
     * 코드 목록 / Code list
     *
     * KR: 코드 조각 목록.
     * EN: List of code snippets.
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codes;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 이미지/그래픽 목록.
     * EN: List of images/graphics.
     *
     * DTD: graphic*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 비디오/오디오 미디어 목록.
     * EN: List of video/audio media.
     *
     * DTD: media*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 표시 인용문 목록 / Display quote list
     *
     * KR: 표시 인용문 목록.
     * EN: List of display quotes.
     *
     * DTD: disp-quote*
     * Required: NO (0 or more)
     */
    private List<DispQuote> dispQuotes;

    /**
     * 표시 수식 목록 / Display formula list
     *
     * KR: 보충 자료에 포함된 표시 수식 목록.
     * EN: List of display formulas in the supplementary material.
     *
     * DTD: disp-formula*
     * Required: NO (0 or more)
     */
    private List<DispFormula> dispFormulas;

    /**
     * 표시 수식 그룹 목록 / Display formula group list
     *
     * KR: 보충 자료에 포함된 표시 수식 그룹 목록.
     * EN: List of display formula groups in the supplementary material.
     *
     * DTD: disp-formula-group*
     * Required: NO (0 or more)
     */
    private List<DispFormulaGroup> dispFormulaGroups;

    /**
     * 화학 구조 래퍼 목록 / Chemical structure wrapper list
     *
     * KR: 보충 자료에 포함된 화학 구조 래퍼 목록.
     * EN: List of chemical structure wrappers in the supplementary material.
     *
     * DTD: chem-struct-wrap*
     * Required: NO (0 or more)
     */
    private List<ChemStructWrap> chemStructWraps;

    /**
     * 연설 목록 / Speech list
     *
     * KR: 보충 자료에 포함된 연설 목록.
     * EN: List of speeches in the supplementary material.
     *
     * DTD: speech*
     * Required: NO (0 or more)
     */
    private List<Speech> speeches;

    /**
     * 진술 목록 / Statement list
     *
     * KR: 보충 자료에 포함된 진술 목록.
     * EN: List of statements in the supplementary material.
     *
     * DTD: statement*
     * Required: NO (0 or more)
     */
    private List<Statement> statements;

    /**
     * 시 그룹 목록 / Verse group list
     *
     * KR: 보충 자료에 포함된 시 그룹 목록.
     * EN: List of verse groups in the supplementary material.
     *
     * DTD: verse-group*
     * Required: NO (0 or more)
     */
    private List<VerseGroup> verseGroups;

    /**
     * 배열 목록 / Array list
     *
     * KR: 보충 자료에 포함된 배열 목록.
     * EN: List of arrays in the supplementary material.
     *
     * DTD: array*
     * Required: NO (0 or more)
     */
    private List<Array> arrays;

    /**
     * 미리 형식화된 텍스트 목록 / Preformatted text list
     *
     * KR: 보충 자료에 포함된 미리 형식화된 텍스트 목록.
     * EN: List of preformatted text blocks in the supplementary material.
     *
     * DTD: preformat*
     * Required: NO (0 or more)
     */
    private List<Preformat> preformats;

    // ========== Attribution / 속성 정보 ==========

    /**
     * 속성 정보 목록 / Attribution list
     *
     * KR: 속성/출처 정보 목록.
     * EN: List of attributions/source information.
     *
     * DTD: attrib*
     * Required: NO (0 or more)
     */
    private List<Attrib> attribs;

    /**
     * 권한 정보 목록 / Permissions list
     *
     * KR: 저작권 및 라이선스 정보 목록.
     * EN: List of copyright and license information.
     *
     * DTD: permissions*
     * Required: NO (0 or more)
     */
    private List<Permissions> permissions;
}
