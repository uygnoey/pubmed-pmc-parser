package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * VernacularTitle / 자국어 제목
 *
 * DTD: <!ELEMENT VernacularTitle (#PCDATA)>
 *
 * KR: 논문의 자국어(현지어) 제목
 * EN: Article title in vernacular language
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VernacularTitle {

    /**
     * 자국어 제목 / Vernacular title
     */
    private String value;
}
