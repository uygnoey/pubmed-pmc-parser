package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FundingGroup / 연구비 그룹
 *
 * KR: 연구비 및 재정 지원 정보 그룹. JATS 1.4 완전 준수 모델.
 * EN: Funding and financial support information group. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT funding-group (award-group*, funding-statement*, open-access?)>
 *      <!ATTLIST funding-group
 *          %jats-common-atts;
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/funding-group.html
 *
 * Note: Container for funding information, including grant/award details and
 * funding acknowledgment statements. Multiple award-group and funding-statement
 * elements can be included to accommodate different funding sources or formats.
 *
 * Example:
 * <funding-group>
 *   <award-group>
 *     <funding-source>National Institutes of Health</funding-source>
 *     <award-id>R01GM123456</award-id>
 *   </award-group>
 *   <funding-statement>This work was supported by NIH grant R01GM123456.</funding-statement>
 * </funding-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingGroup {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 funding-group의 고유 식별자.
     * EN: Unique identifier for this funding-group.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 funding-group의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this funding-group.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     *
     * Example: "primary-funding", "supplementary-funding"
     */
    private String specificUse;

    /**
     * 수여 그룹 목록 / Award group list
     *
     * KR: 개별 연구비 수여 정보를 담은 award-group 목록.
     * EN: List of award-group elements containing individual grant/award information.
     *
     * DTD: award-group*
     * Required: NO (0 or more)
     *
     * Note: Each award-group typically represents one grant or funding source.
     * Multiple award-group elements can be used for articles with multiple funders.
     */
    private List<AwardGroup> awardGroups;

    /**
     * 연구비 서술문 목록 / Funding statement list
     *
     * KR: 연구비 지원에 대한 텍스트 설명 목록.
     * EN: List of textual funding acknowledgment statements.
     *
     * DTD: funding-statement*
     * Required: NO (0 or more)
     *
     * Note: Funding statements provide human-readable acknowledgment of funding sources.
     * Can be used in addition to or instead of structured award-group information.
     */
    private List<FundingStatement> fundingStatements;
}
