package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Institution / 기관
 *
 * KR: 저자 소속 기관 정보.
 *     대학, 연구소, 병원, 회사 등 다양한 기관 유형 지원.
 *     Mixed content를 포함할 수 있음 (텍스트 + 인라인 요소).
 * EN: Author affiliation institution information.
 *     Supports various institution types such as universities, research institutes, hospitals, companies.
 *     Can contain mixed content (text + inline elements).
 *
 * DTD: <!ELEMENT institution (#PCDATA | %all-phrase;)*>
 *
 * DTD: <!ATTLIST institution
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:href CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/institution.html
 *
 * Note: Mixed content (#PCDATA | %all-phrase;)* means text can be mixed with inline elements
 *       like <italic>, <bold>, <sub>, <sup>, etc.
 *       Currently represented as String for simplicity.
 *
 * Examples:
 * <institution>Stanford University</institution>
 * <institution content-type="university">Seoul National University</institution>
 * <institution xlink:href="https://ror.org/02mhbdp94">MIT</institution>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Institution {

    /**
     * 내용 유형 / Content type
     *
     * KR: 기관의 유형 (university, hospital, company, research-institute 등).
     * EN: Type of institution (university, hospital, company, research-institute, etc.).
     *
     * DTD: content-type CDATA #IMPLIED
     *
     * Common values:
     * - "university": 대학 / University
     * - "hospital": 병원 / Hospital
     * - "company": 회사 / Company
     * - "research-institute": 연구소 / Research Institute
     * - "government": 정부 기관 / Government Agency
     */
    private String contentType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: 기관의 고유 ID (문서 내 참조용).
     * EN: Unique ID of the institution (for in-document reference).
     *
     * DTD: id ID #IMPLIED
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 기관 정보의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this institution information (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * XLink href 속성 / XLink href attribute
     *
     * KR: 기관의 외부 링크 (웹사이트, ROR ID 등).
     * EN: External link to the institution (website, ROR ID, etc.).
     *
     * DTD: xlink:href CDATA #IMPLIED
     *
     * Examples:
     * - "https://www.stanford.edu"
     * - "https://ror.org/02mhbdp94"
     */
    private String xlinkHref;

    /**
     * 기관명 / Institution name
     *
     * KR: 기관의 이름 (텍스트 내용).
     *     Mixed content를 포함할 수 있음.
     * EN: Name of the institution (text content).
     *     Can contain mixed content.
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Currently represented as plain String.
     *       For full mixed content support, consider using a richer content model.
     *
     * Examples:
     * - "Stanford University"
     * - "Seoul National University Hospital"
     * - "Korea Advanced Institute of Science and Technology (KAIST)"
     */
    private String content;
}
