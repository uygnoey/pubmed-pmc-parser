package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AwardId / 수여 ID
 *
 * KR: 연구비 또는 수여의 고유 식별자. JATS 1.4 완전 준수 모델.
 * EN: Unique identifier for a grant or award. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT award-id (#PCDATA %award-id-elements;)*>
 *      <!ATTLIST award-id
 *          %jats-common-atts;
 *          award-id-type CDATA #IMPLIED
 *          award-type CDATA #IMPLIED
 *          %might-link-atts;
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/award-id.html
 *
 * Note: Contains the unique identifier assigned by a funding organization to a
 * grant or award. Multiple award-id elements can be used if the same grant has
 * multiple identifiers (e.g., from different organizations or systems).
 *
 * Example:
 * <award-id award-type="grant" award-id-type="doi">10.13039/100000002</award-id>
 * <award-id award-type="grant">R01GM123456</award-id>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardId {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 award-id 요소의 고유 식별자.
     * EN: Unique identifier for this award-id element.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 수여 ID 타입 / Award ID type
     *
     * KR: 수여 ID의 유형 (doi, fundref, nih 등).
     * EN: Type of award identifier (doi, fundref, nih, etc.).
     *
     * DTD: award-id-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "doi", "fundref", "nih", "nsf", "erc"
     */
    private String awardIdType;

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
     * 특정 용도 / Specific use
     *
     * KR: 이 award-id의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this award-id.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 수여 ID 값 / Award ID value
     *
     * KR: 수여 ID의 실제 값 (식별번호).
     * EN: Actual value of the award identifier.
     *
     * DTD: #PCDATA
     * Required: YES (text content)
     *
     * Example: "R01GM123456", "ERC-2019-STG-850623", "10.13039/100000002"
     *
     * Note: This is the primary content - the grant/award number or identifier string.
     */
    private String value;
}
