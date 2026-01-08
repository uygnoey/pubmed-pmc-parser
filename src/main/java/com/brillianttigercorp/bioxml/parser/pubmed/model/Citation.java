package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Citation / 인용
 *
 * DTD: <!ELEMENT Citation (%text;)*>
 *
 * KR: 참고문헌 인용 정보
 * EN: Reference citation information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Citation {

    /**
     * 인용 내용 / Citation content
     */
    private String value;
}
