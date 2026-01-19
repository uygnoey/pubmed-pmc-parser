package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * InstitutionWrap / 기관 래퍼
 *
 * KR: 기관 정보를 계층적으로 그룹화하는 래퍼 요소.
 *     재귀 구조를 지원하여 복잡한 기관 계층 표현 가능 (예: 대학 > 단과대학 > 학과).
 * EN: Wrapper element for hierarchically grouping institution information.
 *     Supports recursive structure for complex institution hierarchies (e.g., University > College > Department).
 *
 * DTD: <!ELEMENT institution-wrap (institution-id*, institution*, institution-wrap*)>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/institution-wrap.html
 *
 * Note: This element supports recursive structure through the institution-wrap field,
 *       allowing representation of multi-level institutional hierarchies.
 *
 * Examples:
 * <institution-wrap>
 *   <institution-id institution-id-type="ror">https://ror.org/02mhbdp94</institution-id>
 *   <institution>Stanford University</institution>
 *   <institution-wrap>
 *     <institution>School of Medicine</institution>
 *     <institution-wrap>
 *       <institution>Department of Genetics</institution>
 *     </institution-wrap>
 *   </institution-wrap>
 * </institution-wrap>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionWrap {

    /**
     * 기관 ID 목록 / Institution ID list
     *
     * KR: 기관의 고유 식별자 목록 (ROR, ISNI, Ringgold, GRID 등).
     * EN: List of institution unique identifiers (ROR, ISNI, Ringgold, GRID, etc.).
     *
     * DTD: institution-id*
     * Required: NO (0 or more)
     */
    private List<InstitutionId> institutionIds;

    /**
     * 기관 목록 / Institution list
     *
     * KR: 기관명 목록.
     *     하나의 기관이 여러 이름을 가질 수 있음 (예: 공식명, 약칭).
     * EN: List of institution names.
     *     One institution can have multiple names (e.g., official name, abbreviation).
     *
     * DTD: institution*
     * Required: NO (0 or more)
     */
    private List<Institution> institutions;

    /**
     * 하위 기관 래퍼 목록 (재귀 구조) / Child institution wrap list (recursive structure)
     *
     * KR: 하위 계층의 기관 래퍼 목록.
     *     재귀 구조를 통해 복잡한 기관 계층 표현 가능.
     * EN: List of child institution wraps.
     *     Enables representation of complex institution hierarchies through recursive structure.
     *
     * DTD: institution-wrap*
     * Required: NO (0 or more)
     *
     * Example hierarchy:
     * - Stanford University (top level)
     *   - School of Medicine (level 2)
     *     - Department of Genetics (level 3)
     */
    private List<InstitutionWrap> institutionWraps;
}
