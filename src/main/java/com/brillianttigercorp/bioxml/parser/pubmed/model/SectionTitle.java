package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SectionTitle / 섹션 제목
 *
 * DTD: <!ELEMENT SectionTitle (#PCDATA)>
 *
 * KR: 섹션 제목
 * EN: Section title
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionTitle {

    /**
     * 섹션 제목 / Section title
     */
    private String value;
}
