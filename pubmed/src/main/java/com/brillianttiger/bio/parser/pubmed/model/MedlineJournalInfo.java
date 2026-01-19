package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MedlineJournalInfo / MEDLINE 저널 정보
 *
 * DTD: <!ELEMENT MedlineJournalInfo (Country?, MedlineTA, NlmUniqueID?, ISSNLinking?)>
 *
 * KR: MEDLINE 데이터베이스 내 저널 메타데이터
 * EN: Journal metadata within MEDLINE database
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedlineJournalInfo {

    /**
     * 출판 국가 / Publishing country
     */
    private Country country;

    /**
     * MEDLINE 제목 약어 / MEDLINE title abbreviation
     */
    private MedlineTA medlineTA;

    /**
     * NLM 고유 ID / NLM unique ID
     */
    private NlmUniqueID nlmUniqueID;

    /**
     * 연결 ISSN / Linking ISSN
     */
    private ISSNLinking issnLinking;
}
