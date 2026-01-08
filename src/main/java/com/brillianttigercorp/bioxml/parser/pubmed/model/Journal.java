package com.brillianttigercorp.bioxml.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Journal / 저널
 *
 * DTD: <!ELEMENT Journal (ISSN?, JournalIssue, Title?, ISOAbbreviation?)>
 *
 * KR: 논문이 게재된 저널 정보
 * EN: Journal information where the article was published
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Journal {

    /**
     * ISSN / ISSN
     */
    private ISSN issn;

    /**
     * 저널 호 정보 / Journal issue
     */
    private JournalIssue journalIssue;

    /**
     * 저널 제목 / Journal title
     */
    private Title title;

    /**
     * ISO 약어 / ISO abbreviation
     */
    private ISOAbbreviation isoAbbreviation;
}
