package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Colgroup / 테이블 열 그룹
 *
 * KR: XHTML 테이블 열 그룹. JATS 1.4 DTD 완전 준수 모델.
 * EN: XHTML table column group. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT colgroup (col)*>
 *
 * DTD: <!ATTLIST colgroup
 *          align (left | center | right | justify | char) #IMPLIED
 *          char CDATA #IMPLIED
 *          charoff CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          span CDATA "1"
 *          style CDATA #IMPLIED
 *          valign (top | middle | bottom | baseline) #IMPLIED
 *          width CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/colgroup.html
 *
 * Example:
 * <colgroup span="2" width="50%">
 *     <col width="25%"/>
 *     <col width="25%"/>
 * </colgroup>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Colgroup {

    // ========== Attributes / 속성 ==========

    /**
     * 수평 정렬 / Horizontal alignment
     *
     * KR: 열 그룹 내 셀들의 기본 수평 정렬.
     * EN: Default horizontal alignment for cells in the column group.
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
     * KR: 열 그룹의 콘텐츠 유형.
     * EN: Content type of the column group.
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
     * 열 범위 / Column span
     *
     * KR: 이 그룹이 포함하는 열의 수.
     * EN: Number of columns in this group.
     *
     * DTD: span CDATA "1"
     * Required: NO
     * Default: "1"
     */
    @Builder.Default
    private Integer span = 1;

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
     * KR: 열 그룹 내 셀들의 기본 수직 정렬.
     * EN: Default vertical alignment for cells in the column group.
     *
     * DTD: valign (top | middle | bottom | baseline) #IMPLIED
     * Required: NO
     */
    private CellValign valign;

    /**
     * 너비 / Width
     *
     * KR: 열 그룹 너비 (픽셀 또는 백분율).
     * EN: Column group width (pixels or percentage).
     *
     * DTD: width CDATA #IMPLIED
     * Required: NO
     */
    private String width;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 열 목록 / Column list
     *
     * KR: 그룹 내 개별 열 정의 목록.
     * EN: List of individual column definitions in the group.
     *
     * DTD: (col)*
     * Required: NO (0 or more)
     */
    private List<Col> cols;
}
