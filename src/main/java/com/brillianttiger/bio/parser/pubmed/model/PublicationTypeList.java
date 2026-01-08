package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PublicationTypeList / 출판 유형 목록
 *
 * DTD: <!ELEMENT PublicationTypeList (PublicationType+)>
 *
 * KR: 논문의 출판 유형 목록
 * EN: Article publication type list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationTypeList {

    /**
     * 출판 유형 목록 / Publication type list
     */
    private List<PublicationType> publicationTypes;
}
