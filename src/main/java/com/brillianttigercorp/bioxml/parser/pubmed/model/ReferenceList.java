package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ReferenceList / 참조 목록
 *
 * DTD: <!ELEMENT ReferenceList (Title?, Reference*, ReferenceList*)>
 *
 * KR: 참고문헌 목록 (재귀 구조 지원)
 * EN: Reference list (supports recursive structure)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceList {

    /**
     * 제목 / Title
     */
    private Title title;

    /**
     * 참조 목록 / Reference list
     */
    private List<Reference> references;

    /**
     * 하위 참조 목록 (재귀) / Sub-reference list (recursive)
     */
    private List<ReferenceList> referenceLists;
}
