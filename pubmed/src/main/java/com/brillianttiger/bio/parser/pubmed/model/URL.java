package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * URL / URL 주소
 *
 * DTD: <!ELEMENT URL (#PCDATA)>
 * DTD: <!ATTLIST URL
 *          lang (AF|AR|AZ|BG|CS|DA|DE|EN|EL|ES|FA|FI|FR|HE|
 *                HU|HY|IN|IS|IT|IW|JA|KA|KO|LT|MK|ML|NL|NO|
 *                PL|PT|PS|RO|RU|SL|SK|SQ|SR|SV|SW|TH|TR|UK|
 *                VI|ZH) #IMPLIED
 *          Type ( FullText | Summary | fulltext | summary) #IMPLIED>
 *
 * KR: URL 주소 (언어 및 유형 속성 포함)
 * EN: URL address (with language and type attributes)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class URL {

    /**
     * 언어 코드 (선택적) / Language code (optional)
     *
     * KR: ISO 639-1 언어 코드
     * EN: ISO 639-1 language code
     */
    private String lang;

    /**
     * 유형 (선택적): FullText, Summary, fulltext, summary / Type (optional)
     *
     * KR: URL 유형 (전문, 요약)
     * EN: URL type (full text, summary)
     */
    private String type;

    /**
     * URL 주소 / URL address
     */
    private String value;
}
