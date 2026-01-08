package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Affiliation / 소속
 *
 * DTD: <!ELEMENT Affiliation (#PCDATA)>
 *
 * KR: 저자의 소속 기관 정보
 * EN: Author's affiliation information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Affiliation {

    /**
     * 소속 정보 / Affiliation information
     */
    private String value;
}
