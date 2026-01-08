package com.brillianttigercorp.bioxml.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Front / 전면부
 *
 * DTD: <!ELEMENT front (journal-meta?, article-meta, notes?)>
 *
 * KR: 논문의 메타데이터를 포함하는 전면부
 * EN: Front matter containing article metadata
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Front {

    /**
     * 저널 메타데이터 / Journal metadata
     */
    private JournalMeta journalMeta;

    /**
     * 논문 메타데이터 (필수) / Article metadata (required)
     */
    private ArticleMeta articleMeta;

    /**
     * 노트 / Notes
     */
    private Notes notes;
}
