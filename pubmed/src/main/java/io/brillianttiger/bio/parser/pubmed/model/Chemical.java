package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chemical / 화학물질
 *
 * DTD: <!ELEMENT Chemical (RegistryNumber, NameOfSubstance)>
 *
 * KR: 화학 물질 정보
 * EN: Chemical substance information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chemical {

    /**
     * 등록 번호 / Registry number
     */
    private RegistryNumber registryNumber;

    /**
     * 물질명 / Substance name
     */
    private NameOfSubstance nameOfSubstance;
}
