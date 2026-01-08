package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * RefList / 참조 목록
 *
 * DTD: <!ELEMENT ref-list (label?, title?, (%ref-list.class;)*, ref*, ref-list*)>
 *
 * KR: 논문 참조 목록 (재귀 구조)
 * EN: Article reference list (recursive structure)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefList {

    /**
     * 레이블 / Label
     */
    private Label label;

    /**
     * 제목 / Title
     */
    private Title title;

    /**
     * 참조 목록 / Reference list
     */
    private List<Ref> references;

    /**
     * 하위 참조 목록 (재귀) / Sub-reference list (recursive)
     */
    private List<RefList> refLists;
}
