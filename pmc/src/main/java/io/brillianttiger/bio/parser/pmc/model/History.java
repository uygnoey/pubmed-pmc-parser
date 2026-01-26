package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * History / 논문 이력
 *
 * KR: 논문의 주요 날짜 이력 (접수, 승인, 출판 등). JATS 1.4 완전 준수 모델.
 * EN: Article date history (received, accepted, published, etc.). Fully compliant with JATS 1.4.
 *
 * DTD: <!ELEMENT history (date | era | string-date)*>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/history.html
 *
 * Note: Contains key dates in the article's lifecycle such as:
 * - received: When manuscript was received
 * - accepted: When manuscript was accepted
 * - rev-recd: When revised manuscript was received
 * - pub: When article was published
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {

    /**
     * 날짜 목록 / Date list
     *
     * KR: 논문 이력의 주요 날짜들 (접수일, 승인일 등).
     * EN: Key dates in article history (received, accepted, etc.).
     *
     * DTD: date*
     * Required: NO (0 or more)
     *
     * Common date types:
     * - received: Manuscript received date
     * - accepted: Manuscript accepted date
     * - rev-recd: Revised manuscript received date
     * - corrected: Correction date
     * - pub: Publication date
     * - retracted: Retraction date
     */
    private List<PmcDate> dates;

    /**
     * 시대 목록 / Era list
     *
     * KR: 시대 표시 목록 (BC, AD 등).
     * EN: Era designation list (BC, AD, etc.).
     *
     * DTD: era*
     * Required: NO (0 or more)
     */
    private List<Era> eras;

    /**
     * 문자열 날짜 목록 / String date list
     *
     * KR: 비정형 날짜 문자열 목록.
     * EN: Unstructured date string list.
     *
     * DTD: string-date*
     * Required: NO (0 or more)
     */
    private List<StringDate> stringDates;
}
