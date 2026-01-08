package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pagination / 페이지 정보
 *
 * DTD: <!ELEMENT Pagination ((StartPage, EndPage?, MedlinePgn?) | MedlinePgn)>
 *
 * KR: 논문의 페이지 범위 정보
 * EN: Article page range information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {

    /**
     * 시작 페이지 / Start page
     */
    private StartPage startPage;

    /**
     * 끝 페이지 / End page
     */
    private EndPage endPage;

    /**
     * MEDLINE 페이지 표기 / MEDLINE pagination
     */
    private MedlinePgn medlinePgn;
}
