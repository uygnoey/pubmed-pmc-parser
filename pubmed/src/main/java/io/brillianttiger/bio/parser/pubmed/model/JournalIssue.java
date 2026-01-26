package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JournalIssue / 저널 호 정보
 *
 * DTD: <!ELEMENT JournalIssue (Volume?, Issue?, PubDate)>
 * DTD: <!ATTLIST JournalIssue CitedMedium (Internet | Print) #REQUIRED>
 *
 * KR: 저널의 권호 및 출판 날짜 정보
 * EN: Journal volume, issue and publication date information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalIssue {

    /**
     * 인용 매체: Internet | Print (필수) / Cited medium (required)
     *
     * DTD: CitedMedium (Internet | Print) #REQUIRED
     */
    private CitedMedium citedMedium;

    /**
     * 권 / Volume
     */
    private Volume volume;

    /**
     * 호 / Issue
     */
    private Issue issue;

    /**
     * 출판 날짜 / Publication date
     */
    private PubDate pubDate;
}
