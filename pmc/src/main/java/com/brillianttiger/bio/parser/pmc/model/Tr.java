package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Tr / 테이블 행
 *
 * KR: XHTML 테이블 행. JATS 1.4 DTD 완전 준수 모델.
 * EN: XHTML table row. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT tr (th | td)+>
 *
 * DTD: <!ATTLIST tr
 *          align (left | center | right | justify | char) #IMPLIED
 *          char CDATA #IMPLIED
 *          charoff CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          style CDATA #IMPLIED
 *          valign (top | middle | bottom | baseline) #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/tr.html
 *
 * Example:
 * <tr align="center" valign="middle">
 *     <td>Cell 1</td>
 *     <td>Cell 2</td>
 * </tr>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tr {

    // ========== Attributes / 속성 ==========

    /**
     * 수평 정렬 / Horizontal alignment
     *
     * KR: 행 내 셀들의 기본 수평 정렬.
     * EN: Default horizontal alignment for cells in the row.
     *
     * DTD: align (left | center | right | justify | char) #IMPLIED
     * Required: NO
     */
    private CellAlign align;

    /**
     * 정렬 문자 / Alignment character
     *
     * KR: align="char"일 때 정렬 기준 문자.
     * EN: Alignment character when align="char".
     *
     * DTD: char CDATA #IMPLIED
     * Required: NO
     *
     * Example: "."
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
     * 콘텐츠 유형 / Content type
     *
     * KR: 행의 콘텐츠 유형.
     * EN: Content type of the row.
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
     * KR: 행 내 셀들의 기본 수직 정렬.
     * EN: Default vertical alignment for cells in the row.
     *
     * DTD: valign (top | middle | bottom | baseline) #IMPLIED
     * Required: NO
     */
    private CellValign valign;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 헤더 셀 목록 / Header cell list
     *
     * KR: 테이블 헤더 셀 목록.
     * EN: List of table header cells.
     *
     * DTD: th*
     * Required: NO (0 or more)
     */
    private List<Th> headerCells;

    /**
     * 데이터 셀 목록 / Data cell list
     *
     * KR: 테이블 데이터 셀 목록.
     * EN: List of table data cells.
     *
     * DTD: td*
     * Required: NO (0 or more)
     */
    private List<Td> dataCells;
}
