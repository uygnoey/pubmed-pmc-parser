package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AuthorList / 저자 목록
 *
 * DTD: <!ELEMENT AuthorList (Author+)>
 * DTD: <!ATTLIST AuthorList
 *          CompleteYN (Y | N) "Y"
 *          Type (authors | editors) #IMPLIED>
 *
 * KR: 논문의 저자 또는 편집자 목록
 * EN: Article author or editor list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorList {

    /**
     * 완전 여부: Y | N (기본값: "Y") / Complete flag (default: "Y")
     */
    @Builder.Default
    private String completeYN = "Y";

    /**
     * 유형: authors | editors / Type
     *
     * DTD: Type (authors | editors) #IMPLIED
     */
    private AuthorListType type;

    /**
     * 저자 목록 / Author list
     */
    private List<Author> authors;
}
