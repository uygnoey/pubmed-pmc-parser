package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Issn / ISSN (International Standard Serial Number)
 *
 * KR: 저널의 ISSN을 나타내는 요소.
 *     인쇄본(pISSN)과 전자본(eISSN)을 구분하여 저장.
 * EN: Element representing journal's ISSN.
 *     Distinguishes between print ISSN (pISSN) and electronic ISSN (eISSN).
 *
 * DTD: <!ELEMENT issn (#PCDATA)>
 * DTD: <!ATTLIST issn
 *          content-type CDATA #IMPLIED
 *          publication-format (print | electronic | print-electronic | online) #IMPLIED
 *          pub-type (ppub | epub | ppub-epub | epub-ppub) #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/issn.html
 *
 * ISSN Format: XXXX-XXXX (8 digits with hyphen)
 *
 * Examples:
 * - <issn pub-type="ppub">0021-9258</issn> (print ISSN)
 * - <issn pub-type="epub">1083-351X</issn> (electronic ISSN)
 * - <issn publication-format="print">0028-0836</issn>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Issn {

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: ISSN 콘텐츠의 유형을 나타내는 자유 형식 속성.
     * EN: Free-form attribute indicating the type of ISSN content.
     *
     * DTD: content-type CDATA #IMPLIED
     */
    private String contentType;

    /**
     * 출판 형식 / Publication format
     *
     * KR: 출판 형식을 나타내는 열거형.
     *     예: PRINT, ELECTRONIC, PRINT_ELECTRONIC, ONLINE
     * EN: Enumeration indicating publication format.
     *     Examples: PRINT, ELECTRONIC, PRINT_ELECTRONIC, ONLINE
     *
     * DTD: publication-format (print | electronic | print-electronic | online) #IMPLIED
     */
    private PublicationFormat publicationFormat;

    /**
     * 출판 유형 / Publication type
     *
     * KR: 출판 시점을 나타내는 열거형.
     *     예: PPUB (인쇄본), EPUB (전자본), PPUB_EPUB, EPUB_PPUB
     * EN: Enumeration indicating publication timing.
     *     Examples: PPUB (print), EPUB (electronic), PPUB_EPUB, EPUB_PPUB
     *
     * DTD: pub-type (ppub | epub | ppub-epub | epub-ppub) #IMPLIED
     */
    private PubType pubType;

    /**
     * ISSN 값 / ISSN value
     *
     * KR: ISSN 번호 (형식: XXXX-XXXX).
     *     8자리 숫자와 하이픈으로 구성.
     * EN: ISSN number (format: XXXX-XXXX).
     *     Composed of 8 digits and hyphen.
     *
     * DTD: (#PCDATA)
     *
     * Examples:
     * - "0021-9258" (Journal of Biological Chemistry, print)
     * - "1083-351X" (Journal of Biological Chemistry, electronic)
     * - "0028-0836" (Nature)
     */
    private String value;
}
