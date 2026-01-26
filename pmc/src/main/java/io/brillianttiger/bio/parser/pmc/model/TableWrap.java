package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TableWrap / 테이블 래퍼
 *
 * KR: 논문 테이블 래퍼. JATS 1.4 DTD 완전 준수 모델.
 * EN: Article table wrapper. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT table-wrap (
 *          (object-id)*,
 *          label?,
 *          (caption)*,
 *          abstract*,
 *          kwd-group*,
 *          alt-text*,
 *          long-desc*,
 *          (alternatives | disp-quote | speech | statement |
 *           verse-group | def-list | list | array | code | graphic |
 *           media | preformat | table | oasis:table)*,
 *          (table-wrap-foot | attrib | permissions)*
 *      )>
 *
 * DTD: <!ATTLIST table-wrap
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          orientation (portrait | landscape) #IMPLIED
 *          position (anchor | background | float | margin) "float"
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/table-wrap.html
 *
 * Example:
 * <table-wrap id="tbl1" position="float" orientation="landscape">
 *     <label>Table 1</label>
 *     <caption><title>Patient demographics</title></caption>
 *     <table frame="hsides" rules="groups">...</table>
 *     <table-wrap-foot>
 *         <fn id="tfn1"><p>*p < 0.05</p></fn>
 *     </table-wrap-foot>
 * </table-wrap>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableWrap {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 테이블의 콘텐츠 유형.
     * EN: Content type of the table.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     */
    private String contentType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: 고유 식별자. xref 등에서 참조에 사용.
     * EN: Unique identifier. Used for references from xref, etc.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     *
     * Example: "tbl1", "T01"
     */
    private String id;

    /**
     * 방향 / Orientation
     *
     * KR: 테이블의 표시 방향.
     * EN: Display orientation of the table.
     *
     * DTD: orientation (portrait | landscape) #IMPLIED
     * Required: NO
     */
    private Orientation orientation;

    /**
     * 위치 / Position
     *
     * KR: 테이블의 배치 위치.
     * EN: Placement position of the table.
     *
     * DTD: position (anchor | background | float | margin) "float"
     * Required: NO
     * Default: "float"
     */
    @Builder.Default
    private Position position = Position.FLOAT;

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
     * XML 언어 / XML language
     *
     * KR: 테이블 내용의 언어 코드 (ISO 639).
     * EN: Language code for table content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "ko", "ja"
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 객체 ID 목록 / Object ID list
     *
     * KR: 테이블의 대체 식별자 목록.
     * EN: List of alternative identifiers for the table.
     *
     * DTD: (object-id)*
     * Required: NO (0 or more)
     */
    private List<ObjectId> objectIds;

    /**
     * 레이블 / Label
     *
     * KR: 테이블의 레이블 (예: "Table 1", "표 1").
     * EN: Label of the table (e.g., "Table 1").
     *
     * DTD: label?
     * Required: NO (0 or 1)
     */
    private Label label;

    /**
     * 캡션 목록 / Caption list
     *
     * KR: 테이블 캡션 목록 (일반적으로 1개).
     * EN: List of table captions (usually 1).
     *
     * DTD: (caption)*
     * Required: NO (0 or more)
     */
    private List<Caption> captions;

    /**
     * 초록 목록 / Abstract list
     *
     * KR: 테이블에 대한 초록/요약 목록.
     * EN: List of abstracts/summaries for the table.
     *
     * DTD: abstract*
     * Required: NO (0 or more)
     */
    private List<Abstract> abstracts;

    /**
     * 키워드 그룹 목록 / Keyword group list
     *
     * KR: 테이블과 관련된 키워드 그룹 목록.
     * EN: List of keyword groups related to the table.
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
     * 대안 목록 / Alternatives list
     *
     * KR: 동일 콘텐츠의 대안 표현 목록.
     * EN: List of alternative representations of the same content.
     *
     * DTD: alternatives*
     * Required: NO (0 or more)
     */
    private List<Alternatives> alternatives;

    /**
     * 테이블 목록 / Table list
     *
     * KR: XHTML 테이블 목록 (일반적으로 1개).
     * EN: List of XHTML tables (usually 1).
     *
     * DTD: table*
     * Required: NO (0 or more)
     */
    private List<Table> tables;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 테이블 이미지 파일 목록 (이미지 형태의 테이블).
     * EN: List of graphic files for table images.
     *
     * DTD: graphic*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 미디어 파일 목록.
     * EN: List of media files.
     *
     * DTD: media*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 코드 목록 / Code list
     *
     * KR: 코드 블록 목록.
     * EN: List of code blocks.
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codes;

    /**
     * 정의 목록 목록 / Definition list list
     *
     * KR: 정의 목록들.
     * EN: List of definition lists.
     *
     * DTD: def-list*
     * Required: NO (0 or more)
     */
    private List<DefList> defLists;

    /**
     * 테이블 래퍼 각주 목록 / Table wrap footer list
     *
     * KR: 테이블 하단 각주 정보.
     * EN: Footer information at the bottom of the table.
     *
     * DTD: table-wrap-foot*
     * Required: NO (0 or more)
     */
    private List<TableWrapFoot> tableWrapFoots;

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
