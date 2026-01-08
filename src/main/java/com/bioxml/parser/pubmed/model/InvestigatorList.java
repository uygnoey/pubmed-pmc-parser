package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * InvestigatorList / 조사자 목록
 *
 * DTD: <!ELEMENT InvestigatorList (Investigator+)>
 *
 * KR: 연구 조사자 목록
 * EN: Research investigator list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestigatorList {

    /**
     * 조사자 목록 / Investigator list
     */
    private List<Investigator> investigators;
}
