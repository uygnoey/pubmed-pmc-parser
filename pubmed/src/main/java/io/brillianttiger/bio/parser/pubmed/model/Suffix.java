package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Suffix / 접미사
 *
 * DTD: <!ELEMENT Suffix (#PCDATA)>
 *
 * KR: 저자 이름 접미사 (예: "Jr", "Sr", "III")
 * EN: Author name suffix
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Suffix {

    /**
     * 접미사 / Suffix
     */
    private String value;
}
