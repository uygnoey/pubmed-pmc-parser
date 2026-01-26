package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MeshHeading / MeSH 주제어
 *
 * DTD: <!ELEMENT MeshHeading (DescriptorName, QualifierName*)>
 *
 * KR: MeSH 주제어 및 한정어
 * EN: MeSH subject heading and qualifiers
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeshHeading {

    /**
     * 디스크립터명 / Descriptor name
     */
    private DescriptorName descriptorName;

    /**
     * 한정어명 목록 / Qualifier name list
     */
    private List<QualifierName> qualifierNames;
}
