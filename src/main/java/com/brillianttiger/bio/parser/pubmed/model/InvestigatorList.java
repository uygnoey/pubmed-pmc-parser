package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * InvestigatorList / 조사자 목록
 *
 * DTD: <!ELEMENT InvestigatorList (Investigator+)>
 * DTD: <!ATTLIST InvestigatorList
 *          ID ID #IMPLIED>
 *
 * KR: 연구 조사자 목록
 * EN: Research investigator list
 *
 * **2024 변경사항**: ID 속성 추가 (CollectiveName.Investigators와 연결용)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigatorList {

    /**
     * 조사자 목록 ID (2024 신규) / Investigator list ID (2024 new)
     */
    private String id;

    /**
     * 조사자 목록 / Investigator list
     */
    private List<Investigator> investigators;
}
