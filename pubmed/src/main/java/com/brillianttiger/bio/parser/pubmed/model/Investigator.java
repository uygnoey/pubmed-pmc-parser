package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Investigator / 조사자
 *
 * DTD: <!ELEMENT Investigator (LastName, ForeName?, Initials?, Suffix?, Identifier*, AffiliationInfo*)>
 * DTD: <!ATTLIST Investigator ValidYN (Y | N) "Y">
 *
 * KR: 연구 조사자 정보
 * EN: Research investigator information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Investigator {

    /**
     * 유효 여부: Y | N (기본값: "Y") / Valid flag (default: "Y")
     */
    @Builder.Default
    private String validYN = "Y";

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
     * 식별자 목록 / Identifier list
     */
    private List<Identifier> identifiers;

    /**
     * 소속 정보 목록 / Affiliation info list
     */
    private List<AffiliationInfo> affiliationInfos;
}
