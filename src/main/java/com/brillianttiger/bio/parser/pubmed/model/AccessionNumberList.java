package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AccessionNumberList / 등록 번호 목록
 *
 * DTD: <!ELEMENT AccessionNumberList (AccessionNumber+)>
 *
 * KR: 데이터베이스 등록 번호 목록
 * EN: Database accession number list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessionNumberList {

    /**
     * 등록 번호 목록 / Accession number list
     */
    private List<AccessionNumber> accessionNumbers;
}
