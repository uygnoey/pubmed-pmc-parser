package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AwardGroup / 수여 그룹
 *
 * KR: 개별 연구비 또는 상금 수여 정보 그룹. JATS 1.4 완전 준수 모델.
 * EN: Individual grant or award information group. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT award-group (funding-source*, award-id*,
 *                             principal-award-recipient*,
 *                             principal-investigator*,
 *                             support-source*)>
 *      <!ATTLIST award-group
 *          %jats-common-atts;
 *          award-type CDATA #IMPLIED
 *          %might-link-atts;
 *          %xlink-simple-link-atts;
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/award-group.html
 *
 * Note: Contains information about a single grant or award, including funding
 * source, award identifiers, and recipients. Multiple award-group elements can
 * be used within a funding-group for articles with multiple funding sources.
 *
 * Example:
 * <award-group award-type="grant" id="award1">
 *   <funding-source>National Science Foundation</funding-source>
 *   <award-id>NSF-1234567</award-id>
 *   <principal-award-recipient>
 *     <name><surname>Smith</surname><given-names>John</given-names></name>
 *   </principal-award-recipient>
 * </award-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardGroup {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 award-group의 고유 식별자.
     * EN: Unique identifier for this award-group.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 수여 타입 / Award type
     *
     * KR: 수여 또는 연구비의 유형 (grant, contract, fellowship 등).
     * EN: Type of award or grant (grant, contract, fellowship, etc.).
     *
     * DTD: award-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "grant", "contract", "fellowship", "scholarship"
     */
    private String awardType;

    /**
     * 참조 ID / Reference ID
     *
     * KR: 다른 요소를 가리키는 참조 식별자.
     * EN: Reference identifier pointing to another element.
     *
     * DTD: rid IDREFS #IMPLIED (from might-link-atts)
     * Required: NO
     */
    private String rid;

    /**
     * XLink 참조 URL / XLink reference URL
     *
     * KR: 수여 정보 상세 페이지 URL.
     * EN: URL to detailed award information page.
     *
     * DTD: xlink:href CDATA #IMPLIED (from xlink-simple-link-atts)
     * Required: NO
     */
    private String xlinkHref;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 award-group의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this award-group.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     *
     * Example: "primary-funding", "equipment-funding"
     */
    private String specificUse;

    /**
     * 연구비 출처 목록 / Funding source list
     *
     * KR: 연구비 제공 기관 또는 출처 목록.
     * EN: List of funding organizations or sources.
     *
     * DTD: funding-source*
     * Required: NO (0 or more)
     *
     * Example: "National Institutes of Health", "European Research Council"
     */
    private List<FundingSource> fundingSources;

    /**
     * 수여 ID 목록 / Award ID list
     *
     * KR: 연구비 또는 수여의 식별번호 목록.
     * EN: List of grant or award identifier numbers.
     *
     * DTD: award-id*
     * Required: NO (0 or more)
     *
     * Example: "R01GM123456", "ERC-2019-STG-850623"
     */
    private List<AwardId> awardIds;

    /**
     * 주 수여 수령인 목록 / Principal award recipient list
     *
     * KR: 연구비를 수령하는 주요 기관 또는 개인 목록.
     * EN: List of primary institutions or individuals receiving the award.
     *
     * DTD: principal-award-recipient*
     * Required: NO (0 or more)
     *
     * Note: Typically the institution receiving the grant funds.
     */
    private List<PrincipalAwardRecipient> principalAwardRecipients;

    /**
     * 주 연구자 목록 / Principal investigator list
     *
     * KR: 연구의 주 책임자(PI) 목록.
     * EN: List of principal investigators (PI) for the research.
     *
     * DTD: principal-investigator*
     * Required: NO (0 or more)
     *
     * Note: The primary researcher(s) responsible for the funded project.
     */
    private List<PrincipalAwardRecipient> principalInvestigators;

    /**
     * 지원 출처 목록 / Support source list
     *
     * KR: 기타 지원 출처 목록.
     * EN: List of other support sources.
     *
     * DTD: support-source*
     * Required: NO (0 or more)
     *
     * Note: Additional sources of support beyond primary funding.
     */
    private List<SupportSource> supportSources;
}
