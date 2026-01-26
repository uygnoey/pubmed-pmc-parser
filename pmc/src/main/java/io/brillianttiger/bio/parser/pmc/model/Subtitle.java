package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subtitle / 부제목
 *
 * KR: 논문의 부제목을 나타내는 요소.
 *     Mixed content (텍스트 + 인라인 요소)를 포함할 수 있음.
 * EN: Element representing the subtitle of the article.
 *     Can contain mixed content (text + inline elements).
 *
 * DTD: <!ELEMENT subtitle (#PCDATA | %all-phrase;)*>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/subtitle.html
 *
 * Note: Mixed content (#PCDATA | %all-phrase;)* means text can be mixed with inline elements
 *       like <italic>, <bold>, <sub>, <sup>, etc.
 *       Currently represented as String for simplicity.
 *
 * Examples:
 * <subtitle>A Comprehensive Analysis</subtitle>
 * <subtitle><italic>In vivo</italic> and <italic>In vitro</italic> Studies</subtitle>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subtitle {

    /**
     * 부제목 내용 / Subtitle content
     *
     * KR: 부제목의 텍스트 내용.
     *     Mixed content (텍스트 + 인라인 마크업)를 포함할 수 있음.
     * EN: Text content of the subtitle.
     *     Can contain mixed content (text + inline markup).
     *
     * DTD: (#PCDATA | %all-phrase;)*
     *
     * Note: Currently represented as plain String.
     *       For full mixed content support, consider using a richer content model.
     */
    private String content;
}
