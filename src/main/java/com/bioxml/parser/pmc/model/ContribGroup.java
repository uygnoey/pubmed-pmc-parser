package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ContribGroup / 기여자 그룹
 *
 * DTD: <!ELEMENT contrib-group (contrib+, xref*, aff*)>
 * DTD: <!ATTLIST contrib-group content-type CDATA #IMPLIED>
 *
 * KR: 논문의 저자, 편집자 등 기여자 그룹
 * EN: Article contributors group (authors, editors, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContribGroup {

    /**
     * 콘텐츠 유형 / Content type
     */
    private String contentType;

    /**
     * 기여자 목록 (1개 이상 필수) / Contributor list (at least one required)
     */
    private List<Contrib> contributors;

    /**
     * 상호참조 목록 / Cross-reference list
     */
    private List<Xref> xrefs;

    /**
     * 소속 목록 / Affiliation list
     */
    private List<Aff> affiliations;
}
