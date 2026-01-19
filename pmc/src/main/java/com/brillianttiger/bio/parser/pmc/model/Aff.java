package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Aff / 소속
 *
 * KR: 저자 소속 기관 정보. JATS 1.4 완전 준수 모델.
 *     텍스트 및 구조화된 기관 정보 모두 지원.
 * EN: Author affiliation information. Fully compliant with JATS 1.4.
 *     Supports both plain text and structured institution information.
 *
 * DTD: <!ELEMENT aff (#PCDATA | %address-elements; | %aff-elements;)*>
 *
 * DTD: <!ATTLIST aff
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          rid IDREFS #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/aff.html
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Aff {

    /**
     * 내용 유형 / Content type
     * DTD: content-type CDATA #IMPLIED
     */
    private String contentType;

    /**
     * ID 속성 / ID attribute
     * DTD: id ID #IMPLIED
     */
    private String id;

    /**
     * 참조 ID / Reference ID
     * DTD: rid IDREFS #IMPLIED
     */
    private String rid;

    /**
     * 특정 용도 / Specific use
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 기관 래퍼 목록 / Institution wrap list
     * DTD: institution-wrap*
     */
    private List<InstitutionWrap> institutionWraps;

    /**
     * 주소 라인 목록 / Address line list
     *
     * KR: 주소 라인 목록. 소속 기관의 물리적 주소 정보.
     * EN: List of address lines. Physical address information of affiliation.
     *
     * DTD: addr-line*
     * Required: NO (0 or more)
     */
    private List<AddrLine> addrLines;

    /**
     * 소속 내용 / Affiliation content (텍스트)
     * DTD: (#PCDATA | %address-elements; | %aff-elements;)*
     */
    private String value;
}
