package io.brillianttiger.bio.parser.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * TextContent / Mixed content 처리 모델
 *
 * KR: Mixed content를 처리하는 공통 모델.
 *     PubMed의 %text; entity와 JATS의 mixed content에 사용.
 * EN: Common model for handling mixed content.
 *     Used for PubMed %text; entity and JATS mixed content.
 *
 * 지원 인라인 요소 / Supported inline elements:
 * - PubMed: b, i, u, sup, sub
 * - JATS: bold, italic, underline, sup, sub, monospace, sc, xref, ext-link
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TextContent {

    /**
     * 원본 텍스트 (마크업 제거) / Plain text (markup removed)
     */
    private String plainText;

    /**
     * 마크업 보존 텍스트 (HTML 형식) / Markup preserved text (HTML format)
     */
    private String htmlText;

    /**
     * 원본 XML 텍스트 / Original XML text
     */
    private String rawXml;

    /**
     * 인라인 요소 목록 / List of inline elements
     */
    private List<InlineElement> inlineElements;

    /**
     * InlineElement / 인라인 요소
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InlineElement {
        private InlineType type;
        private String content;
        private int startIndex;
        private int endIndex;
        private Map<String, String> attributes;
    }

    /**
     * InlineType / 인라인 요소 타입
     */
    public enum InlineType {
        BOLD,           // PubMed: b, JATS: bold
        ITALIC,         // PubMed: i, JATS: italic
        UNDERLINE,      // PubMed: u, JATS: underline
        SUPERSCRIPT,    // sup
        SUBSCRIPT,      // sub
        MONOSPACE,      // JATS: monospace
        SMALL_CAPS,     // JATS: sc
        XREF,           // JATS: xref
        EXT_LINK,       // JATS: ext-link
        NAMED_CONTENT,  // JATS: named-content
        STYLED_CONTENT  // JATS: styled-content
    }
}
