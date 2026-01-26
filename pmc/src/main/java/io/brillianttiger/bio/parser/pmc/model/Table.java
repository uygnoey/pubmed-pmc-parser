package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Table / 표 (XHTML)
 *
 * KR: XHTML 표. JATS 1.4 DTD 완전 준수 모델.
 * EN: XHTML table. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT table (
 *          (col* | colgroup*),
 *          (thead?, tfoot?, (tbody+ | tr+))
 *      )>
 *
 * DTD: <!ATTLIST table
 *          border CDATA #IMPLIED
 *          cellpadding CDATA #IMPLIED
 *          cellspacing CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          frame (void | above | below | hsides | lhs | rhs | vsides | box | border) #IMPLIED
 *          id ID #IMPLIED
 *          rules (none | groups | rows | cols | all) #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          style CDATA #IMPLIED
 *          summary CDATA #IMPLIED
 *          width CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/table.html
 *
 * Example:
 * <table frame="hsides" rules="groups" width="100%">
 *     <thead>
 *         <tr><th>Name</th><th>Value</th></tr>
 *     </thead>
 *     <tbody>
 *         <tr><td>A</td><td>1</td></tr>
 *         <tr><td>B</td><td>2</td></tr>
 *     </tbody>
 * </table>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Table {

    // ========== Attributes / 속성 ==========

    /**
     * 테두리 / Border
     *
     * KR: 테두리 두께 (픽셀).
     * EN: Border width in pixels.
     *
     * DTD: border CDATA #IMPLIED
     * Required: NO
     */
    private String border;

    /**
     * 셀 패딩 / Cell padding
     *
     * KR: 셀 내부 여백.
     * EN: Padding within cells.
     *
     * DTD: cellpadding CDATA #IMPLIED
     * Required: NO
     */
    private String cellpadding;

    /**
     * 셀 간격 / Cell spacing
     *
     * KR: 셀 간 간격.
     * EN: Spacing between cells.
     *
     * DTD: cellspacing CDATA #IMPLIED
     * Required: NO
     */
    private String cellspacing;

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
     * 프레임 / Frame
     *
     * KR: 테이블 외곽 테두리 표시 방식.
     * EN: How to display the outer border of the table.
     *
     * DTD: frame (void | above | below | hsides | lhs | rhs | vsides | box | border) #IMPLIED
     * Required: NO
     */
    private TableFrame frame;

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
     * 규칙선 / Rules
     *
     * KR: 테이블 내부 규칙선 표시 방식.
     * EN: How to display internal rules between cells.
     *
     * DTD: rules (none | groups | rows | cols | all) #IMPLIED
     * Required: NO
     */
    private TableRules rules;

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
     * 스타일 / Style
     *
     * KR: CSS 스타일 정보.
     * EN: CSS style information.
     *
     * DTD: style CDATA #IMPLIED
     * Required: NO
     */
    private String style;

    /**
     * 요약 / Summary
     *
     * KR: 접근성을 위한 테이블 요약 설명.
     * EN: Summary description for accessibility.
     *
     * DTD: summary CDATA #IMPLIED
     * Required: NO
     */
    private String summary;

    /**
     * 너비 / Width
     *
     * KR: 테이블 너비 (픽셀 또는 백분율).
     * EN: Table width (pixels or percentage).
     *
     * DTD: width CDATA #IMPLIED
     * Required: NO
     *
     * Example: "100%", "500"
     */
    private String width;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 열 그룹 목록 / Column group list
     *
     * KR: 열 그룹 정의 목록.
     * EN: List of column group definitions.
     *
     * DTD: colgroup*
     * Required: NO (0 or more)
     */
    private List<Colgroup> colgroups;

    /**
     * 열 목록 / Column list
     *
     * KR: 개별 열 정의 목록.
     * EN: List of individual column definitions.
     *
     * DTD: col*
     * Required: NO (0 or more)
     */
    private List<Col> cols;

    /**
     * 테이블 헤더 / Table header
     *
     * KR: 테이블 헤더 섹션.
     * EN: Table header section.
     *
     * DTD: thead?
     * Required: NO (0 or 1)
     */
    private Thead thead;

    /**
     * 테이블 푸터 / Table footer
     *
     * KR: 테이블 푸터 섹션.
     * EN: Table footer section.
     *
     * DTD: tfoot?
     * Required: NO (0 or 1)
     */
    private Tfoot tfoot;

    /**
     * 테이블 바디 목록 / Table body list
     *
     * KR: 테이블 바디 섹션 목록.
     * EN: List of table body sections.
     *
     * DTD: tbody+
     * Required: YES (1 or more, if not using tr directly)
     */
    private List<Tbody> tbodies;

    /**
     * 행 목록 / Row list
     *
     * KR: 직접 정의된 테이블 행 목록 (tbody 없이).
     * EN: Directly defined table rows (without tbody).
     *
     * DTD: tr+
     * Required: YES (1 or more, if not using tbody)
     */
    private List<Tr> rows;
}
