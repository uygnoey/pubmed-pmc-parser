package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CitationSubset / 인용 하위집합
 *
 * DTD: <!ELEMENT CitationSubset (#PCDATA)>
 *
 * KR: MEDLINE 데이터베이스 내 특수 하위집합 코드
 * EN: Special subset code within MEDLINE database
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitationSubset {

    /**
     * 하위집합 코드 / Subset code
     */
    private String value;
}
