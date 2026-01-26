package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ObjectId / 객체 식별자
 *
 * KR: 콘텐츠 객체의 대체 식별자. JATS 1.4 DTD 완전 준수 모델.
 * EN: Alternative identifier for content object. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT object-id (#PCDATA)>
 *
 * DTD: <!ATTLIST object-id
 *          assigning-authority CDATA #IMPLIED
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          pub-id-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/object-id.html
 *
 * Example:
 * <object-id pub-id-type="doi">10.1234/fig.001</object-id>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectId {

    /**
     * 식별자 값 / Identifier value
     *
     * KR: 객체의 식별자 값.
     * EN: The identifier value of the object.
     *
     * DTD: #PCDATA
     * Required: YES
     */
    private String value;

    /**
     * 할당 기관 / Assigning authority
     *
     * KR: 식별자를 할당한 기관이나 조직.
     * EN: Organization or agency that assigned the identifier.
     *
     * DTD: assigning-authority CDATA #IMPLIED
     * Required: NO
     *
     * Example: "crossref", "publisher"
     */
    private String assigningAuthority;

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 식별자의 콘텐츠 유형.
     * EN: Content type of the identifier.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     */
    private String contentType;

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
     * 발행 ID 유형 / Publication ID type
     *
     * KR: 식별자의 유형 (doi, pmid 등).
     * EN: Type of the identifier (doi, pmid, etc.).
     *
     * DTD: pub-id-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values: doi, pmid, pmcid, pmc-uid, publisher-id
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
}
