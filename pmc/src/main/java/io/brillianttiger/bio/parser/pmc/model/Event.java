package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Event / 출판 이벤트
 *
 * KR: 출판 이력의 개별 이벤트 (온라인 우선 출판, 인쇄 출판 등). JATS 1.3+ 모델.
 * EN: Individual publishing event in publication history (online first, print publication, etc.). JATS 1.3+ model.
 *
 * DTD: <!ELEMENT event (
 *          (event-desc | title)*,
 *          date+
 *      )>
 *
 * DTD: <!ATTLIST event
 *          event-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.3/element/event.html
 *
 * Note: Introduced in JATS 1.3 to track detailed publication events.
 * Common event-type values:
 * - online-first: First online publication (온라인 우선 출판)
 * - print-publication: Print publication (인쇄 출판)
 * - correction: Correction event (정정)
 * - retraction: Retraction event (철회)
 * - preprint: Preprint posting (프리프린트 게시)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    /**
     * 이벤트 유형 / Event type
     *
     * KR: 출판 이벤트의 유형 (online-first, print-publication, correction 등).
     * EN: Type of publishing event (online-first, print-publication, correction, etc.).
     *
     * DTD: event-type CDATA #IMPLIED
     *
     * Common values:
     * - online-first: First online publication
     * - print-publication: Print publication
     * - epub: Electronic publication
     * - ppub: Print publication
     * - correction: Correction
     * - retraction: Retraction
     * - preprint: Preprint posting
     */
    private String eventType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: 이벤트의 고유 ID (문서 내 참조용).
     * EN: Unique ID of the event (for in-document reference).
     *
     * DTD: id ID #IMPLIED
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 이벤트의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this event (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 언어 / Language
     *
     * KR: 이벤트 설명의 언어 (ISO 639 코드).
     * EN: Language of event description (ISO 639 code).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     */
    private String xmlLang;

    /**
     * 이벤트 설명 / Event description
     *
     * KR: 이벤트에 대한 설명 텍스트.
     * EN: Descriptive text about the event.
     *
     * DTD: event-desc*
     * Required: NO (0 or more)
     */
    private String eventDesc;

    /**
     * 제목 / Title
     *
     * KR: 이벤트 제목.
     * EN: Event title.
     *
     * DTD: title*
     * Required: NO (0 or more)
     */
    private String title;

    /**
     * 날짜 목록 / Date list (REQUIRED)
     *
     * KR: 이벤트의 날짜 정보 (최소 1개 필수).
     * EN: Date information for the event (at least one required).
     *
     * DTD: date+
     * Required: YES (1 or more)
     *
     * Note: An event must have at least one date.
     */
    private List<PmcDate> dates;
}
