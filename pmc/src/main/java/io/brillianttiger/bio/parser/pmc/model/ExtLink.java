package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ExtLink / 외부 링크
 *
 * DTD: <!ELEMENT ext-link (#PCDATA | %all-phrase;)*>
 * DTD: <!ATTLIST ext-link
 *          assigning-authority CDATA #IMPLIED
 *          ext-link-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
 *          xlink:href CDATA #IMPLIED
 *          xlink:role CDATA #IMPLIED
 *          xlink:show (embed | new | none | other | replace) #IMPLIED
 *          xlink:title CDATA #IMPLIED
 *          xlink:type (simple) #IMPLIED
 *      >
 *
 * KR: 외부 링크 (URI, 이메일, FTP 등)
 * EN: External link (URI, email, FTP, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExtLink {
    /**
     * KR: 링크 유형 (uri, email, ftp, doi 등)
     * EN: Link type (uri, email, ftp, doi, etc.)
     */
    private String extLinkType;

    /**
     * KR: 권한 할당 주체
     * EN: Assigning authority
     */
    private String assigningAuthority;

    /**
     * KR: ID 속성
     * EN: ID attribute
     */
    private String id;

    /**
     * KR: 특정 용도
     * EN: Specific use
     */
    private String specificUse;

    /**
     * KR: XLink href (실제 URL)
     * EN: XLink href (actual URL)
     */
    private String xlinkHref;

    /**
     * KR: XLink actuate (onLoad, onRequest, other, none)
     * EN: XLink actuate (onLoad, onRequest, other, none)
     */
    private String xlinkActuate;

    /**
     * KR: XLink role
     * EN: XLink role
     */
    private String xlinkRole;

    /**
     * KR: XLink show (embed, new, none, other, replace)
     * EN: XLink show (embed, new, none, other, replace)
     */
    private String xlinkShow;

    /**
     * KR: XLink title
     * EN: XLink title
     */
    private String xlinkTitle;

    /**
     * KR: XLink type (항상 "simple")
     * EN: XLink type (always "simple")
     */
    private String xlinkType;

    /**
     * KR: 링크 텍스트 (표시될 텍스트)
     * EN: Link text (display text)
     */
    private String value;
}
