package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * PubHistory / 출판 이력
 *
 * KR: 논문의 상세 출판 이력 (온라인/인쇄 출판 등). JATS 1.3+ 모델.
 * EN: Detailed publication history of article (online/print publication, etc.). JATS 1.3+ model.
 *
 * DTD: <!ELEMENT pub-history (date | event)*>
 *
 * DTD: <!ATTLIST pub-history
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.3/element/pub-history.html
 *
 * Note: Introduced in JATS 1.3 to provide more detailed publication history
 * than the simpler 'history' element. Can contain multiple events tracking
 * different publication milestones (e.g., online-first, print publication).
 *
 * Difference from 'history':
 * - history: General manuscript lifecycle dates (received, accepted, etc.)
 * - pub-history: Detailed publication events (online-first, print, corrections, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PubHistory {

    /**
     * ID 속성 / ID attribute
     *
     * KR: 출판 이력의 고유 ID (문서 내 참조용).
     * EN: Unique ID of the publication history (for in-document reference).
     *
     * DTD: id ID #IMPLIED
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 출판 이력의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this publication history (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 날짜 목록 / Date list
     *
     * KR: 출판 관련 날짜 목록.
     * EN: Publication-related date list.
     *
     * DTD: date*
     * Required: NO (0 or more)
     */
    private List<PmcDate> dates;

    /**
     * 이벤트 목록 / Event list
     *
     * KR: 출판 이벤트 목록 (온라인 우선 출판, 인쇄 출판 등).
     * EN: Publication event list (online-first, print publication, etc.).
     *
     * DTD: event*
     * Required: NO (0 or more)
     *
     * Common events:
     * - online-first: First online publication
     * - print-publication: Print publication
     * - correction: Correction
     * - retraction: Retraction
     * - preprint: Preprint posting
     */
    private List<Event> events;
}
