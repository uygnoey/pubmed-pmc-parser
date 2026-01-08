package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ObjectList / 객체 목록
 *
 * DTD: <!ELEMENT ObjectList (Object+)>
 *
 * KR: PubMed 객체 목록
 * EN: PubMed object list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectList {

    /**
     * 객체 목록 / Object list
     */
    private List<PubmedObject> objects;
}
