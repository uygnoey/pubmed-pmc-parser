package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subject / 주제
 *
 * KR: 논문의 주제 또는 분류를 나타내는 요소.
 *     Mixed content를 포함할 수 있음 (텍스트 + 인라인 요소).
 * EN: Element representing article subject or classification.
 *     Can contain mixed content (text + inline elements).
 *
 * DTD: <!ELEMENT subject (#PCDATA | %all-phrase;)*>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/subject.html
 *
 * Note: Mixed content (#PCDATA | %all-phrase;)* means text can be mixed with inline elements
 *       like <italic>, <bold>, <sub>, <sup>, etc.
 *       Currently represented as String for simplicity.
 *
 * Examples:
 * <subject>Molecular Biology</subject>
 * <subject>Research Article</subject>
 * <subject><italic>In vitro</italic> Studies</subject>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

    /**
     * 주제 내용 / Subject content
     *
     * KR: 주제의 텍스트 내용.
     *     Mixed content (텍스트 + 인라인 마크업)를 포함할 수 있음.
     * EN: Text content of the subject.
     *     Can contain mixed content (text + inline markup).
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Currently represented as plain String.
     *       For full mixed content support, consider using a richer content model.
     *
     * Examples:
     * - "Molecular Biology"
     * - "Research Article"
     * - "In vitro Studies" (may include markup)
     */
    private String content;

    /**
     * 주제 코드 / Subject code
     *
     * KR: 주제의 코드 또는 식별자 (선택적).
     * EN: Subject code or identifier (optional).
     *
     * Examples:
     * - "Q1" (subject heading code)
     * - "570" (Dewey Decimal Classification)
     */
    private String code;

    /**
     * 주제 유형 / Subject type
     *
     * KR: 주제의 유형 (선택적).
     * EN: Type of subject (optional).
     *
     * Examples:
     * - "primary" (primary subject)
     * - "secondary" (secondary subject)
     */
    private String type;
}
