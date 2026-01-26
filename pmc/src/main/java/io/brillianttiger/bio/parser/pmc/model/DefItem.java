package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DefItem / 정의 항목
 *
 * KR: 용어와 정의의 쌍. JATS 1.4 완전 준수 모델.
 * EN: Pair of term and definition. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT def-item (
 *          (object-id)*,
 *          label?,
 *          (term)+,
 *          (def)*)>
 *
 *      <!ATTLIST def-item
 *          %jats-common-atts;
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/def-item.html
 *
 * Note: A definition item must contain at least one term element.
 * Multiple terms can share the same definition(s), and multiple definitions
 * can be provided for the same term(s).
 *
 * Example:
 * <def-item>
 *   <term>PCR</term>
 *   <term>Polymerase Chain Reaction</term>
 *   <def><p>A method for amplifying DNA sequences.</p></def>
 * </def-item>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefItem {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 정의 항목의 고유 식별자.
     * EN: Unique identifier for this definition item.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 정의 항목의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this definition item.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 레이블 / Label
     *
     * KR: 정의 항목의 레이블.
     * EN: Label for the definition item.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 용어 목록 / Term list
     *
     * KR: 정의될 용어 목록 (최소 1개 필수).
     * EN: List of terms to be defined (at least one required).
     *
     * DTD: term+
     * Required: YES (1 or more)
     *
     * Note: Multiple terms can be provided when they share the same definition.
     */
    private List<Term> terms;

    /**
     * 정의 목록 / Definition list
     *
     * KR: 용어에 대한 정의 목록.
     * EN: List of definitions for the term(s).
     *
     * DTD: def*
     * Required: NO (0 or more)
     *
     * Note: Multiple definitions can be provided for nuanced or context-specific meanings.
     */
    private List<Def> definitions;
}
