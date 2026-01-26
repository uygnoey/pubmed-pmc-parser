package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TableWrapFoot / 테이블 래퍼 각주
 *
 * KR: 테이블 래퍼 각주 영역. JATS 1.4 DTD 완전 준수 모델.
 * EN: Table wrapper footer area. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT table-wrap-foot (
 *          (label | title)*,
 *          (attrib | fn | fn-group | p | x)*
 *      )>
 *
 * DTD: <!ATTLIST table-wrap-foot
 *          id ID #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/table-wrap-foot.html
 *
 * Example:
 * <table-wrap-foot>
 *     <fn id="tfn1"><label>*</label><p>p < 0.05</p></fn>
 *     <fn id="tfn2"><label>†</label><p>Statistically significant</p></fn>
 * </table-wrap-foot>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableWrapFoot {

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

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 레이블 목록 / Label list
     *
     * KR: 각주 영역의 레이블 목록.
     * EN: List of labels for the footer area.
     *
     * DTD: label*
     * Required: NO (0 or more)
     */
    private List<Label> labels;

    /**
     * 제목 목록 / Title list
     *
     * KR: 각주 영역의 제목 목록.
     * EN: List of titles for the footer area.
     *
     * DTD: title*
     * Required: NO (0 or more)
     */
    private List<Title> titles;

    /**
     * 각주 목록 / Footnote list
     *
     * KR: 개별 각주 목록.
     * EN: List of individual footnotes.
     *
     * DTD: fn*
     * Required: NO (0 or more)
     */
    private List<Fn> footnotes;

    /**
     * 각주 그룹 목록 / Footnote group list
     *
     * KR: 각주 그룹 목록.
     * EN: List of footnote groups.
     *
     * DTD: fn-group*
     * Required: NO (0 or more)
     */
    private List<FnGroup> fnGroups;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 각주 영역의 단락 목록.
     * EN: List of paragraphs in the footer area.
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

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
}
