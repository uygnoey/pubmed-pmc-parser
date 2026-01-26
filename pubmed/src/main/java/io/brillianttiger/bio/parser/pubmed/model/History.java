package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * History / 이력
 *
 * DTD: <!ELEMENT History (PubMedPubDate+)>
 *
 * KR: 논문의 출판 이력
 * EN: Article publication history
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {

    /**
     * PubMed 출판 날짜 목록 / PubMed publication date list
     */
    private List<PubMedPubDate> pubMedPubDates;
}
