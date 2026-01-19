package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author / 저자
 *
 * DTD: <!ELEMENT Author (
 *          ((LastName, ForeName?, Initials?, Suffix?) | CollectiveName),
 *          Identifier*,
 *          AffiliationInfo*
 *      )>
 * DTD: <!ATTLIST Author
 *          ValidYN (Y | N) "Y"
 *          EqualContrib (Y | N) #IMPLIED>
 *
 * KR: 논문 저자 정보
 * EN: Article author information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    /**
     * 유효 여부: Y | N (기본값: "Y") / Valid flag (default: "Y")
     */
    @Builder.Default
    private String validYN = "Y";

    /**
     * 동등 기여 여부: Y | N / Equal contribution flag
     */
    private String equalContrib;

    /**
     * 성 / Last name
     */
    private LastName lastName;

    /**
     * 이름 / First name
     */
    private ForeName foreName;

    /**
     * 이니셜 / Initials
     */
    private Initials initials;

    /**
     * 접미사 / Suffix
     */
    private Suffix suffix;

    /**
     * 단체명 / Collective name
     */
    private CollectiveName collectiveName;

    /**
     * 식별자 목록 / Identifier list
     */
    private List<Identifier> identifiers;

    /**
     * 소속 정보 목록 / Affiliation info list
     */
    private List<AffiliationInfo> affiliationInfos;
}
