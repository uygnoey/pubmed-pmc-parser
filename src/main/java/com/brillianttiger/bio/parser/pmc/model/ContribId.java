package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ContribId / 기여자 ID
 *
 * KR: 기여자의 고유 식별자 (ORCID, ISNI, Scopus 등).
 *     JATS 1.4 완전 준수 모델.
 * EN: Contributor unique identifier (ORCID, ISNI, Scopus, etc.).
 *     Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT contrib-id (#PCDATA)>
 *
 * DTD: <!ATTLIST contrib-id
 *          authenticated (true | false) #IMPLIED
 *          content-type CDATA #IMPLIED
 *          contrib-id-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/contrib-id.html
 *
 * Examples:
 * <contrib-id contrib-id-type="orcid" authenticated="true">0000-0001-2345-6789</contrib-id>
 * <contrib-id contrib-id-type="scopus">12345678900</contrib-id>
 * <contrib-id contrib-id-type="researcher-id">A-1234-2023</contrib-id>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContribId {

    /**
     * 인증 여부 / Authentication status
     *
     * KR: ID가 인증되었는지 여부 (true | false).
     *     true인 경우 ID가 공식적으로 검증되었음을 의미.
     * EN: Whether the ID is authenticated (true | false).
     *     true means the ID has been officially verified.
     *
     * DTD: authenticated (true | false) #IMPLIED
     *
     * Example: "true" for verified ORCID
     */
    private String authenticated;

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
     * 기여자 ID 유형 / Contributor ID type
     *
     * KR: 기여자 ID의 유형 (orcid, isni, scopus, researcher-id 등).
     * EN: Type of contributor ID (orcid, isni, scopus, researcher-id, etc.).
     *
     * DTD: contrib-id-type CDATA #IMPLIED
     *
     * Common values:
     * - "orcid": Open Researcher and Contributor ID
     * - "isni": International Standard Name Identifier
     * - "scopus": Scopus Author ID
     * - "researcher-id": ResearcherID (Web of Science)
     * - "wos-researcher-id": Web of Science ResearcherID
     */
    private ContribIdType contribIdType;

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
     * KR: 기여자 ID의 실제 값.
     * EN: Actual value of the contributor ID.
     *
     * DTD: (#PCDATA)
     *
     * Examples:
     * - ORCID: "0000-0001-2345-6789"
     * - Scopus: "12345678900"
     * - ResearcherID: "A-1234-2023"
     * - ISNI: "0000 0001 2345 6789"
     */
    private String value;
}
