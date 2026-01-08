package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DataBank / 데이터 은행
 *
 * DTD: <!ELEMENT DataBank (DataBankName, AccessionNumberList?)>
 *
 * KR: 데이터 저장소 및 등록 번호
 * EN: Data repository and accession numbers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataBank {

    /**
     * 데이터은행 이름 / Data bank name
     */
    private DataBankName dataBankName;

    /**
     * 등록 번호 목록 / Accession number list
     */
    private AccessionNumberList accessionNumberList;
}
