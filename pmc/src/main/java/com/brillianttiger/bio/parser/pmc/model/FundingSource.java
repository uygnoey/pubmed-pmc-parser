package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FundingSource / 연구비 출처
 *
 * KR: 연구비 제공 기관 또는 출처 정보. JATS 1.4 완전 준수 모델.
 * EN: Funding organization or source information. Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT funding-source (institution-wrap | %just-rendition.class;)*>
 *      <!ATTLIST funding-source
 *          %jats-common-atts;
 *          country CDATA #IMPLIED
 *          %might-link-atts;
 *          source-type CDATA #IMPLIED
 *          %xlink-simple-link-atts;
 *          specific-use CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/funding-source.html
 *
 * Note: Identifies the organization(s) or individual(s) that provided funding for
 * the research. Can contain structured institution information via institution-wrap
 * or simple text content.
 *
 * Example:
 * <funding-source country="US">
 *   <institution-wrap>
 *     <institution>National Institutes of Health</institution>
 *     <institution-id institution-id-type="FundRef">http://dx.doi.org/10.13039/100000002</institution-id>
 *   </institution-wrap>
 * </funding-source>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingSource {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이 funding-source의 고유 식별자.
     * EN: Unique identifier for this funding-source.
     *
     * DTD: id ID #IMPLIED (from jats-common-atts)
     * Required: NO
     */
    private String id;

    /**
     * 국가 코드 / Country code
     *
     * KR: 연구비 출처의 국가 코드 (ISO 3166).
     * EN: Country code of the funding source (ISO 3166).
     *
     * DTD: country CDATA #IMPLIED
     * Required: NO
     *
     * Example: "US", "GB", "DE", "JP"
     */
    private String country;

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
     * 출처 타입 / Source type
     *
     * KR: 연구비 출처의 유형 (government, private, industry 등).
     * EN: Type of funding source (government, private, industry, etc.).
     *
     * DTD: source-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "government", "private", "industry", "university", "foundation"
     */
    private String sourceType;

    /**
     * XLink 참조 URL / XLink reference URL
     *
     * KR: 연구비 출처 정보 페이지 URL (FundRef DOI 등).
     * EN: URL to funding source information page (FundRef DOI, etc.).
     *
     * DTD: xlink:href CDATA #IMPLIED (from xlink-simple-link-atts)
     * Required: NO
     *
     * Example: "http://dx.doi.org/10.13039/100000002"
     */
    private String xlinkHref;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 funding-source의 특정 용도 또는 적용 범위.
     * EN: Specific use or scope of this funding-source.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * 기관 정보 래퍼 목록 / Institution wrap list
     *
     * KR: 구조화된 기관 정보를 담는 래퍼 목록.
     * EN: List of structured institution information wrappers.
     *
     * DTD: institution-wrap*
     * Required: NO (0 or more)
     *
     * Note: Provides structured representation of funding institution with
     * institution name, ID, and other identifiers.
     */
    private List<InstitutionWrap> institutionWraps;

    /**
     * 텍스트 콘텐츠 / Text content
     *
     * KR: 비구조화 텍스트로 표현된 연구비 출처.
     * EN: Funding source as unstructured text.
     *
     * DTD: %just-rendition.class; (text content)
     * Required: NO
     *
     * Example: "National Institutes of Health"
     *
     * Note: Used when institution-wrap is not provided, or for simple text representation.
     */
    private String value;
}
