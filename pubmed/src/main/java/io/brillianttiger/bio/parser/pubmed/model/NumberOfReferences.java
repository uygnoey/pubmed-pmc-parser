package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NumberOfReferences / 참고문헌 수
 *
 * DTD: <!ELEMENT NumberOfReferences (#PCDATA)>
 *
 * KR: 논문의 참고문헌 총 개수
 * EN: Total number of references in the article
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NumberOfReferences {

    /**
     * 참고문헌 수 / Number of references
     */
    private Integer value;
}
