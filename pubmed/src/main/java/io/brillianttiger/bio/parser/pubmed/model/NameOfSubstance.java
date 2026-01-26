package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NameOfSubstance / 물질명
 *
 * DTD: <!ELEMENT NameOfSubstance (#PCDATA)>
 * DTD: <!ATTLIST NameOfSubstance UI CDATA #REQUIRED>
 *
 * KR: 화학 물질명
 * EN: Chemical substance name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameOfSubstance {

    /**
     * UI (고유 식별자, 필수) / UI (unique identifier, required)
     */
    private String ui;

    /**
     * 물질명 / Substance name
     */
    private String value;
}
