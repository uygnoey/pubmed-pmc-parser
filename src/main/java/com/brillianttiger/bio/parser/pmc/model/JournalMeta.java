package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JournalMeta / 저널 메타데이터
 *
 * DTD: <!ELEMENT journal-meta (journal-id+, journal-title-group?, issn*, isbn*, publisher?, notes?)>
 *
 * KR: 저널의 메타데이터 정보
 * EN: Journal metadata information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalMeta {

    /**
     * 저널 ID 목록 (1개 이상 필수) / Journal ID list (at least one required)
     */
    private List<JournalId> journalIds;

    /**
     * 저널 제목 그룹 / Journal title group
     */
    private JournalTitleGroup journalTitleGroup;

    /**
     * ISSN 목록 / ISSN list
     */
    private List<PmcIssn> issns;

    /**
     * ISBN 목록 / ISBN list
     */
    private List<PmcIsbn> isbns;

    /**
     * 출판사 / Publisher
     */
    private PmcPublisher publisher;

    /**
     * 노트 / Notes
     */
    private Notes notes;
}
