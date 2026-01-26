package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InstitutionId / 기관 ID
 *
 * KR: 기관의 고유 식별자 (ROR, ISNI, Ringgold, GRID 등).
 *     JATS 1.4 완전 준수 모델.
 * EN: Institution unique identifier (ROR, ISNI, Ringgold, GRID, etc.).
 *     Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT institution-id (#PCDATA)>
 *
 * DTD: <!ATTLIST institution-id
 *          content-type CDATA #IMPLIED
 *          institution-id-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/institution-id.html
 *
 * Examples:
 * <institution-id institution-id-type="ror">https://ror.org/02mhbdp94</institution-id>
 * <institution-id institution-id-type="isni">0000 0001 2345 6789</institution-id>
 * <institution-id institution-id-type="ringgold">12345</institution-id>
 * <institution-id institution-id-type="grid">grid.1234.5</institution-id>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionId {

    /**
     * 내용 유형 / Content type
     *
     * KR: ID의 내용 유형 (선택적 추가 분류).
     * EN: Content type of the ID (optional additional classification).
     *
     * DTD: content-type CDATA #IMPLIED
     */
    private String contentType;

    /**
     * 기관 ID 유형 / Institution ID type
     *
     * KR: 기관 ID의 유형 (ror, isni, ringgold, grid 등).
     * EN: Type of institution ID (ror, isni, ringgold, grid, etc.).
     *
     * DTD: institution-id-type CDATA #IMPLIED
     *
     * Common values:
     * - "ror": Research Organization Registry
     * - "isni": International Standard Name Identifier
     * - "ringgold": Ringgold Identifier
     * - "grid": Global Research Identifier Database (replaced by ROR)
     */
    private InstitutionIdType institutionIdType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 ID의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this ID (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * ID 값 / ID value
     *
     * KR: 기관 ID의 실제 값.
     * EN: Actual value of the institution ID.
     *
     * DTD: (#PCDATA)
     *
     * Examples:
     * - ROR: "https://ror.org/02mhbdp94" or "02mhbdp94"
     * - ISNI: "0000 0001 2345 6789"
     * - Ringgold: "12345"
     * - GRID: "grid.1234.5"
     */
    private String value;
}
