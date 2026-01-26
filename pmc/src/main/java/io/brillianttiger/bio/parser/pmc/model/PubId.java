package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubId / 출판물 ID
 *
 * KR: 출판물 식별자 (DOI, PMID, PMC ID 등). JATS 1.4 DTD 완전 준수 모델.
 * EN: Publication identifier (DOI, PMID, PMC ID, etc.). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT pub-id (#PCDATA)>
 *
 * DTD: <!ATTLIST pub-id
 *          assigning-authority CDATA #IMPLIED
 *          custom-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          pub-id-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *          xlink:href CDATA #IMPLIED
 *          xlink:role CDATA #IMPLIED
 *          xlink:show (embed | new | none | other | replace) #IMPLIED
 *          xlink:title CDATA #IMPLIED
 *          xlink:type (simple) #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/pub-id.html
 *
 * Example:
 * <pub-id pub-id-type="doi">10.1234/example.2023.001</pub-id>
 * <pub-id pub-id-type="pmid">12345678</pub-id>
 * <pub-id pub-id-type="pmcid">PMC1234567</pub-id>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubId {

    // ========== Attributes / 속성 ==========

    /**
     * 발급 기관 / Assigning authority
     *
     * KR: ID를 발급한 기관.
     * EN: Organization that assigned the ID.
     *
     * DTD: assigning-authority CDATA #IMPLIED
     * Required: NO
     *
     * Example: "crossref", "pubmed", "pmc"
     */
    private String assigningAuthority;

    /**
     * 사용자 정의 유형 / Custom type
     *
     * KR: pub-id-type이 "custom"인 경우 사용되는 사용자 정의 유형.
     * EN: Custom type used when pub-id-type is "custom".
     *
     * DTD: custom-type CDATA #IMPLIED
     * Required: NO
     */
    private String customType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자.
     * EN: Unique identifier within the XML document.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     */
    private String id;

    /**
     * 출판물 ID 유형 / Publication ID type
     *
     * KR: 출판물 식별자의 유형.
     * EN: Type of publication identifier.
     *
     * DTD: pub-id-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values: doi, pmid, pmcid, pmc-uid, publisher-id, manuscript, arxiv, pii, other
     */
    private PubIdType pubIdType;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 특정 용도나 목적을 나타내는 문자열.
     * EN: String indicating specific use or purpose.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * XLink href / XLink href
     *
     * KR: ID 해석을 위한 링크 URL.
     * EN: URL link for resolving the ID.
     *
     * DTD: xlink:href CDATA #IMPLIED
     * Required: NO
     */
    private String xlinkHref;

    /**
     * XLink 실행 시점 / XLink actuate
     *
     * KR: 링크 활성화 시점.
     * EN: When to activate the link.
     *
     * DTD: xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
     * Required: NO
     */
    private XlinkActuate xlinkActuate;

    /**
     * XLink 표시 방식 / XLink show
     *
     * KR: 링크 리소스 표시 방식.
     * EN: How to display the linked resource.
     *
     * DTD: xlink:show (embed | new | none | other | replace) #IMPLIED
     * Required: NO
     */
    private XlinkShow xlinkShow;

    /**
     * XML Base / XML base
     *
     * KR: 상대 URI 해석을 위한 기본 URI.
     * EN: Base URI for resolving relative URIs.
     *
     * DTD: xml:base CDATA #IMPLIED
     * Required: NO
     */
    private String xmlBase;

    /**
     * XML 언어 / XML language
     *
     * KR: 내용의 언어 코드 (ISO 639).
     * EN: Language code for content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    // ========== Content / 내용 ==========

    /**
     * ID 값 / ID value
     *
     * KR: 출판물 식별자 값.
     * EN: Publication identifier value.
     *
     * DTD: #PCDATA
     * Required: YES
     *
     * Example: "10.1234/example.2023.001" (DOI), "12345678" (PMID)
     */
    private String value;
}
