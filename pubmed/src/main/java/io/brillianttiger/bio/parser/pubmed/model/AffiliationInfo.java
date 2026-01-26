package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AffiliationInfo / 소속 정보
 *
 * DTD: <!ELEMENT AffiliationInfo (Affiliation, Identifier*)>
 *
 * KR: 저자의 소속 기관 및 식별자
 * EN: Author affiliation and identifiers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliationInfo {

    /**
     * 소속 / Affiliation
     */
    private Affiliation affiliation;

    /**
     * 식별자 목록 / Identifier list
     */
    private List<Identifier> identifiers;
}
