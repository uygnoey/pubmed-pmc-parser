package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DataBankName / 데이터은행 이름
 *
 * DTD: <!ELEMENT DataBankName (#PCDATA)>
 *
 * KR: 데이터 저장소 이름 (예: "GenBank", "ClinicalTrials.gov")
 * EN: Data repository name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataBankName {

    /**
     * 데이터은행 이름 / Data bank name
     */
    private String value;
}
