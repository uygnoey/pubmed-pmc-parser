package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PubmedArticle / PubMed 논문
 *
 * DTD: <!ELEMENT PubmedArticle (MedlineCitation, PubmedData?)>
 *
 * KR: PubMed 논문 전체 정보
 * EN: Complete PubMed article information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubmedArticle {

    /**
     * MEDLINE 인용 정보 / MEDLINE citation
     */
    private MedlineCitation medlineCitation;

    /**
     * PubMed 데이터 / PubMed data
     */
    private PubmedData pubmedData;
}
