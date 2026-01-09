package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * NestedKwd / 중첩 키워드
 *
 * KR: 계층적으로 중첩된 키워드. JATS 1.4 완전 준수 모델.
 * EN: Hierarchically nested keyword. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT nested-kwd (kwd, nested-kwd*)>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/nested-kwd.html
 *
 * Note: Used for hierarchical keyword structures, common in controlled vocabularies
 * with parent-child relationships. The recursive structure allows for multiple
 * levels of nesting.
 *
 * Example hierarchy:
 * - Diseases (level 1)
 *   - Cardiovascular Diseases (level 2)
 *     - Hypertension (level 3)
 *     - Coronary Artery Disease (level 3)
 *   - Diabetes (level 2)
 *     - Type 1 Diabetes (level 3)
 *     - Type 2 Diabetes (level 3)
 *
 * XML Example:
 * <nested-kwd>
 *   <kwd>Diseases</kwd>
 *   <nested-kwd>
 *     <kwd>Cardiovascular Diseases</kwd>
 *     <nested-kwd>
 *       <kwd>Hypertension</kwd>
 *     </nested-kwd>
 *   </nested-kwd>
 * </nested-kwd>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NestedKwd {

    /**
     * 키워드 / Keyword (REQUIRED)
     *
     * KR: 이 레벨의 키워드 (필수).
     * EN: Keyword at this level (required).
     *
     * DTD: kwd
     * Required: YES
     *
     * Note: Every nested-kwd must have exactly one kwd element.
     */
    private Kwd keyword;

    /**
     * 하위 중첩 키워드 목록 / Child nested keyword list
     *
     * KR: 하위 계층의 중첩 키워드 목록 (재귀 구조).
     * EN: Child nested keyword list (recursive structure).
     *
     * DTD: nested-kwd*
     * Required: NO (0 or more)
     *
     * Note: Allows unlimited depth of nesting through recursive structure.
     * Each nested-kwd can contain its own nested-kwd children.
     */
    private List<NestedKwd> nestedKeywords;
}
