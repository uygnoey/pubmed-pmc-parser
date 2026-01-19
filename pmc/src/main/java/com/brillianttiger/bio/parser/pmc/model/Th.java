package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Th / 테이블 헤더 셀
 *
 * KR: XHTML 테이블 헤더 셀. JATS 1.4 DTD 완전 준수 모델.
 * EN: XHTML table header cell. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT th (#PCDATA | %inside-cell;)*>
 *
 * DTD: <!ATTLIST th
 *          abbr CDATA #IMPLIED
 *          align (left | center | right | justify | char) #IMPLIED
 *          axis CDATA #IMPLIED
 *          char CDATA #IMPLIED
 *          charoff CDATA #IMPLIED
 *          colspan CDATA "1"
 *          content-type CDATA #IMPLIED
 *          headers IDREFS #IMPLIED
 *          id ID #IMPLIED
 *          rowspan CDATA "1"
 *          scope (row | col | rowgroup | colgroup) #IMPLIED
 *          style CDATA #IMPLIED
 *          valign (top | middle | bottom | baseline) #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/th.html
 *
 * Example:
 * <th colspan="2" align="center" scope="col">Header Text</th>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Th {

    // ========== Attributes / 속성 ==========

    /**
     * 약어 / Abbreviation
     *
     * KR: 셀 내용의 약어 (접근성용).
     * EN: Abbreviation for cell content (for accessibility).
     *
     * DTD: abbr CDATA #IMPLIED
     * Required: NO
     */
    private String abbr;

    /**
     * 수평 정렬 / Horizontal alignment
     *
     * KR: 셀 내용의 수평 정렬.
     * EN: Horizontal alignment of cell content.
     *
     * DTD: align (left | center | right | justify | char) #IMPLIED
     * Required: NO
     */
    private CellAlign align;

    /**
     * 축 / Axis
     *
     * KR: 셀이 속하는 카테고리 (접근성용).
     * EN: Category to which the cell belongs (for accessibility).
     *
     * DTD: axis CDATA #IMPLIED
     * Required: NO
     */
    private String axis;

    /**
     * 정렬 문자 / Alignment character
     *
     * KR: align="char"일 때 정렬 기준 문자.
     * EN: Alignment character when align="char".
     *
     * DTD: char CDATA #IMPLIED
     * Required: NO
     */
    private String alignChar;

    /**
     * 정렬 문자 오프셋 / Alignment character offset
     *
     * KR: 정렬 문자의 오프셋.
     * EN: Offset of the alignment character.
     *
     * DTD: charoff CDATA #IMPLIED
     * Required: NO
     */
    private String charoff;

    /**
     * 열 병합 / Column span
     *
     * KR: 셀이 차지하는 열의 수.
     * EN: Number of columns the cell spans.
     *
     * DTD: colspan CDATA "1"
     * Required: NO
     * Default: "1"
     */
    @Builder.Default
    private Integer colspan = 1;

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 셀의 콘텐츠 유형.
     * EN: Content type of the cell.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     */
    private String contentType;

    /**
     * 헤더 참조 / Headers reference
     *
     * KR: 이 셀과 연관된 헤더 셀의 ID 목록 (접근성용).
     * EN: List of header cell IDs associated with this cell (for accessibility).
     *
     * DTD: headers IDREFS #IMPLIED
     * Required: NO
     */
    private String headers;

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
     * 행 병합 / Row span
     *
     * KR: 셀이 차지하는 행의 수.
     * EN: Number of rows the cell spans.
     *
     * DTD: rowspan CDATA "1"
     * Required: NO
     * Default: "1"
     */
    @Builder.Default
    private Integer rowspan = 1;

    /**
     * 범위 / Scope
     *
     * KR: 헤더가 적용되는 범위.
     * EN: Scope of the header.
     *
     * DTD: scope (row | col | rowgroup | colgroup) #IMPLIED
     * Required: NO
     */
    private String scope;

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
     * 수직 정렬 / Vertical alignment
     *
     * KR: 셀 내용의 수직 정렬.
     * EN: Vertical alignment of cell content.
     *
     * DTD: valign (top | middle | bottom | baseline) #IMPLIED
     * Required: NO
     */
    private CellValign valign;

    // ========== Content / 내용 ==========

    /**
     * 셀 내용 / Cell content
     *
     * KR: 셀의 텍스트/혼합 내용.
     * EN: Text/mixed content of the cell.
     *
     * DTD: (#PCDATA | %inside-cell;)*
     */
    private String content;
}
