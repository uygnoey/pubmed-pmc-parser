package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DataBankList / 데이터은행 목록
 *
 * DTD: <!ELEMENT DataBankList (DataBank+)>
 * DTD: <!ATTLIST DataBankList CompleteYN (Y | N) "Y">
 *
 * KR: 데이터 저장소 목록
 * EN: Data repository list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataBankList {

    /**
     * 완전 여부: Y | N (기본값: "Y") / Complete flag (default: "Y")
     */
    @Builder.Default
    private String completeYN = "Y";

    /**
     * 데이터은행 목록 / Data bank list
     */
    private List<DataBank> dataBanks;
}
