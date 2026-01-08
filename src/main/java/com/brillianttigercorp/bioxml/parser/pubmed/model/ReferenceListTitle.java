package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReferenceListTitle / 참고문헌 목록 제목
 *
 * DTD: <!ELEMENT ReferenceList (Title?, Reference*, ReferenceList*)>
 * DTD: <!ELEMENT Title (%text;)*>
 *
 * KR: 참고문헌 목록의 제목
 * EN: Title of reference list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceListTitle {

    /**
     * 제목 값 / Title value
     */
    private String value;
}
