package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JournalId / 저널 ID
 *
 * KR: 저널의 고유 식별자를 나타내는 요소.
 *     다양한 ID 체계(NLM-TA, PMC, DOI 등)로 저널을 식별.
 * EN: Element representing journal's unique identifier.
 *     Identifies journal using various ID schemes (NLM-TA, PMC, DOI, etc.).
 *
 * DTD: <!ELEMENT journal-id (#PCDATA)>
 * DTD: <!ATTLIST journal-id
 *          journal-id-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/journal-id.html
 *
 * Examples:
 * - <journal-id journal-id-type="nlm-ta">J Biol Chem</journal-id>
 * - <journal-id journal-id-type="pmc">pmc</journal-id>
 * - <journal-id journal-id-type="doi">10.1001/jama</journal-id>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalId {

    /**
     * 저널 ID 유형 / Journal ID type
     *
     * KR: 저널 ID 체계를 나타내는 열거형.
     *     예: NLM_TA, ISO_ABBREV, PUBLISHER_ID, PMC, DOI, HWP
     * EN: Enumeration indicating journal ID scheme.
     *     Examples: NLM_TA, ISO_ABBREV, PUBLISHER_ID, PMC, DOI, HWP
     *
     * DTD: journal-id-type CDATA #IMPLIED
     */
    private JournalIdType journalIdType;

    /**
     * 특수 용도 / Specific use
     *
     * KR: 특정 용도나 처리 방식을 나타내는 자유 형식 속성.
     * EN: Free-form attribute indicating specific use or processing.
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 언어 코드 / Language code
     *
     * KR: 저널 ID의 언어 (ISO 639-1 코드).
     * EN: Language of the journal ID (ISO 639-1 code).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     */
    private String xmlLang;

    /**
     * 저널 ID 값 / Journal ID value
     *
     * KR: 저널 ID 문자열.
     *     예: "J Biol Chem", "pmc", "10.1001/jama"
     * EN: Journal ID string.
     *     Examples: "J Biol Chem", "pmc", "10.1001/jama"
     *
     * DTD: (#PCDATA)
     */
    private String value;
}
