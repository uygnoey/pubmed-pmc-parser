package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MedlinePgn (MEDLINE Pagination) / MEDLINE 페이지 표기
 *
 * DTD: <!ELEMENT MedlinePgn (#PCDATA)>
 *
 * KR: MEDLINE 스타일 페이지 범위 (예: "123-45", "e12345")
 * EN: MEDLINE style page range
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedlinePgn {

    /**
     * 페이지 범위 / Page range
     */
    private String value;
}
