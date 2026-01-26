package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Tfoot / 테이블 푸터 섹션
 *
 * KR: XHTML 테이블 푸터 섹션. JATS 1.4 DTD 완전 준수 모델.
 * EN: XHTML table footer section. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT tfoot (tr)+>
 *
 * DTD: <!ATTLIST tfoot
 *          align (left | center | right | justify | char) #IMPLIED
 *          char CDATA #IMPLIED
 *          charoff CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          style CDATA #IMPLIED
 *          valign (top | middle | bottom | baseline) #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/tfoot.html
 *
 * Example:
 * <tfoot>
 *     <tr><td colspan="2">Total: 100</td></tr>
 * </tfoot>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tfoot {

    // ========== Attributes / 속성 ==========

    /**
     * 수평 정렬 / Horizontal alignment
     *
     * KR: 푸터 섹션 셀들의 기본 수평 정렬.
     * EN: Default horizontal alignment for cells in the footer section.
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
     * KR: 푸터 섹션의 콘텐츠 유형.
     * EN: Content type of the footer section.
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
     * KR: 푸터 섹션 셀들의 기본 수직 정렬.
     * EN: Default vertical alignment for cells in the footer section.
     *
     * DTD: valign (top | middle | bottom | baseline) #IMPLIED
     * Required: NO
     */
    private CellValign valign;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 행 목록 / Row list
     *
     * KR: 푸터 섹션의 테이블 행 목록.
     * EN: List of table rows in the footer section.
     *
     * DTD: (tr)+
     * Required: YES (1 or more)
     */
    private List<Tr> rows;
}
