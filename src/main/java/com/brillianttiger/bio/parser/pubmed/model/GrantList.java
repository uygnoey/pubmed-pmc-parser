package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * GrantList / 연구비 목록
 *
 * DTD: <!ELEMENT GrantList (Grant+)>
 * DTD: <!ATTLIST GrantList CompleteYN (Y | N) "Y">
 *
 * KR: 연구비 지원 목록
 * EN: Grant funding list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrantList {

    /**
     * 완전 여부: Y | N (기본값: "Y") / Complete flag (default: "Y")
     */
    @Builder.Default
    private String completeYN = "Y";

    /**
     * 연구비 목록 / Grant list
     */
    private List<Grant> grants;
}
