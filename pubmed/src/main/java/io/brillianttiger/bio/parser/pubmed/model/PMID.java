package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PMID (PubMed ID) / PubMed 고유 식별자
 *
 * DTD: <!ELEMENT PMID (#PCDATA)>
 * DTD: <!ATTLIST PMID Version CDATA "1">
 *
 * KR: PubMed의 고유 식별 번호
 * EN: PubMed Unique Identifier
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PMID {

    /**
     * PMID 값 / PMID value
     */
    private String value;

    /**
     * 버전 (기본값: "1") / Version (default: "1")
     */
    @Builder.Default
    private String version = "1";
}
