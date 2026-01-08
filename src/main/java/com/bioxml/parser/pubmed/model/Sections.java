package com.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Sections / 섹션 목록
 *
 * DTD: <!ELEMENT Sections (Section+)>
 *
 * KR: 도서 섹션 목록
 * EN: Book section list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sections {

    /**
     * 섹션 목록 / Section list
     */
    private List<Section> sections;
}
