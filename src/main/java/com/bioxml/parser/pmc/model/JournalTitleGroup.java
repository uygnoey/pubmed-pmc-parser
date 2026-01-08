package com.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JournalTitleGroup / 저널 제목 그룹
 *
 * DTD: <!ELEMENT journal-title-group (journal-title*, journal-subtitle*, trans-title-group*, abbrev-journal-title*)>
 *
 * KR: 저널의 다양한 제목 형식
 * EN: Various journal title formats
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalTitleGroup {

    /**
     * 저널 제목 목록 / Journal title list
     */
    private List<JournalTitle> journalTitles;

    /**
     * 저널 부제목 목록 / Journal subtitle list
     */
    private List<JournalSubtitle> journalSubtitles;

    /**
     * 번역 제목 그룹 목록 / Translated title group list
     */
    private List<TransTitleGroup> transTitleGroups;

    /**
     * 약칭 제목 목록 / Abbreviated title list
     */
    private List<AbbrevJournalTitle> abbrevJournalTitles;
}
