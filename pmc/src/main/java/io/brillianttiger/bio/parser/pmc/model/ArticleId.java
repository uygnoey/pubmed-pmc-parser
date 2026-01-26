package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ArticleId / 논문 ID
 *
 * KR: 논문의 다양한 식별자를 나타내는 요소.
 *     DOI, PMID, PMC ID 등 다양한 유형의 ID를 포함.
 * EN: Element representing various article identifiers.
 *     Includes various types of IDs such as DOI, PMID, PMC ID, etc.
 *
 * DTD: <!ELEMENT article-id (#PCDATA)>
 * DTD: <!ATTLIST article-id
 *          assigning-authority CDATA #IMPLIED
 *          pub-id-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/article-id.html
 *
 * Examples:
 * <article-id pub-id-type="doi">10.1371/journal.pone.0123456</article-id>
 * <article-id pub-id-type="pmid">12345678</article-id>
 * <article-id pub-id-type="pmcid">PMC1234567</article-id>
 * <article-id pub-id-type="publisher-id">ABC-2024-001</article-id>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleId {

    /**
     * 출판 ID 유형 / Publication ID type
     *
     * KR: ID의 유형 (DOI, PMID, PMC ID 등).
     * EN: Type of ID (DOI, PMID, PMC ID, etc.).
     *
     * DTD: pub-id-type CDATA #IMPLIED
     *
     * Examples:
     * - DOI: Digital Object Identifier
     * - PMID: PubMed ID
     * - PMCID: PubMed Central ID
     * - PUBLISHER_ID: Publisher's ID
     */
    private PubIdType pubIdType;

    /**
     * 할당 기관 / Assigning authority
     *
     * KR: ID를 할당한 기관 또는 조직.
     * EN: Organization or authority that assigned the ID.
     *
     * DTD: assigning-authority CDATA #IMPLIED
     *
     * Examples:
     * - "Crossref" for DOIs
     * - "NLM" for PMIDs
     */
    private String assigningAuthority;

    /**
     * 특정 용도 / Specific use
     *
     * KR: ID의 특정 용도 또는 목적.
     * EN: Specific use or purpose of the ID.
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * ID 값 / ID value
     *
     * KR: 실제 ID 값.
     * EN: Actual ID value.
     *
     * DTD: (#PCDATA)
     *
     * Examples:
     * - "10.1371/journal.pone.0123456" (DOI)
     * - "12345678" (PMID)
     * - "PMC1234567" (PMCID)
     */
    private String value;
}
