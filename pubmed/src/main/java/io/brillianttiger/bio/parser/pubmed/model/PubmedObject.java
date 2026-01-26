package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Object / 객체
 *
 * DTD: <!ELEMENT Object (Param*)>
 * DTD: <!ATTLIST Object Type CDATA #REQUIRED>
 *
 * KR: PubMed 객체 정보
 * EN: PubMed object information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubmedObject {

    /**
     * 유형 (필수) / Type (required)
     */
    private String type;

    /**
     * 파라미터 목록 / Parameter list
     */
    private List<Param> params;
}
