package io.brillianttiger.bio.parser.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * TextContentTest / TextContent 모델 테스트
 *
 * KR: Mixed content 처리 모델의 변환 기능 테스트.
 * EN: Tests for mixed content processing model conversion features.
 */
@DisplayName("TextContent 모델 테스트")
class TextContentTest {

    // ==================== PLAIN TEXT TESTS ====================

    @Test
    @DisplayName("plainText: 마크업이 없는 순수 텍스트")
    void shouldStorePlainText() {
        // Given
        String plainText = "This is plain text without markup.";

        // When
        TextContent content = TextContent.builder()
                .plainText(plainText)
                .build();

        // Then
        assertThat(content.getPlainText()).isEqualTo(plainText);
        assertThat(content.getHtmlText()).isNull();
        assertThat(content.getRawXml()).isNull();
    }

    @Test
    @DisplayName("plainText: 인라인 마크업이 제거된 텍스트")
    void shouldExtractPlainTextFromMarkup() {
        // Given
        String plainText = "E=mc2";  // 마크업 제거 후
        String htmlText = "E=mc<sup>2</sup>";  // HTML 형식
        String rawXml = "E=mc<sup>2</sup>";  // 원본 XML

        // When
        TextContent content = TextContent.builder()
                .plainText(plainText)
                .htmlText(htmlText)
                .rawXml(rawXml)
                .build();

        // Then
        assertThat(content.getPlainText()).isEqualTo("E=mc2");
        assertThat(content.getPlainText()).doesNotContain("<sup>");
        assertThat(content.getPlainText()).doesNotContain("</sup>");
    }

    // ==================== HTML TEXT TESTS ====================

    @Test
    @DisplayName("htmlText: 인라인 마크업이 보존된 HTML 형식 텍스트")
    void shouldPreserveMarkupInHtmlText() {
        // Given
        String plainText = "This is bold and italic text.";
        String htmlText = "This is <b>bold</b> and <i>italic</i> text.";

        // When
        TextContent content = TextContent.builder()
                .plainText(plainText)
                .htmlText(htmlText)
                .build();

        // Then
        assertThat(content.getHtmlText()).contains("<b>bold</b>");
        assertThat(content.getHtmlText()).contains("<i>italic</i>");
    }

    @Test
    @DisplayName("htmlText: 복잡한 중첩 마크업 보존")
    void shouldPreserveNestedMarkup() {
        // Given
        String htmlText = "Formula: <i>E</i>=<i>m</i><i>c</i><sup>2</sup>";

        // When
        TextContent content = TextContent.builder()
                .htmlText(htmlText)
                .build();

        // Then
        assertThat(content.getHtmlText())
                .contains("<i>E</i>")
                .contains("<sup>2</sup>");
    }

    // ==================== RAW XML TESTS ====================

    @Test
    @DisplayName("rawXml: 원본 XML 텍스트 보존")
    void shouldPreserveRawXml() {
        // Given
        String rawXml = "<ArticleTitle>Test <i>italic</i> <sup>superscript</sup></ArticleTitle>";

        // When
        TextContent content = TextContent.builder()
                .rawXml(rawXml)
                .build();

        // Then
        assertThat(content.getRawXml()).isEqualTo(rawXml);
    }

    @Test
    @DisplayName("rawXml: JATS 네임스페이스 속성 보존")
    void shouldPreserveNamespaceAttributes() {
        // Given
        String rawXml = "<ext-link ext-link-type=\"uri\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"https://example.com\">link</ext-link>";

        // When
        TextContent content = TextContent.builder()
                .rawXml(rawXml)
                .build();

        // Then
        assertThat(content.getRawXml())
                .contains("xmlns:xlink")
                .contains("xlink:href");
    }

    // ==================== INLINE ELEMENTS TESTS ====================

    @Test
    @DisplayName("inlineElements: 인라인 요소 목록 파싱")
    void shouldParseInlineElements() {
        // Given
        TextContent.InlineElement bold = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.BOLD)
                .content("bold text")
                .startIndex(8)
                .endIndex(17)
                .build();

        TextContent.InlineElement italic = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.ITALIC)
                .content("italic text")
                .startIndex(22)
                .endIndex(33)
                .build();

        List<TextContent.InlineElement> elements = Arrays.asList(bold, italic);

        // When
        TextContent content = TextContent.builder()
                .plainText("This is bold text and italic text.")
                .inlineElements(elements)
                .build();

        // Then
        assertThat(content.getInlineElements()).hasSize(2);
        assertThat(content.getInlineElements().get(0).getType()).isEqualTo(TextContent.InlineType.BOLD);
        assertThat(content.getInlineElements().get(0).getContent()).isEqualTo("bold text");
        assertThat(content.getInlineElements().get(1).getType()).isEqualTo(TextContent.InlineType.ITALIC);
    }

    @Test
    @DisplayName("inlineElements: xref 요소의 속성 파싱")
    void shouldParseXrefElementWithAttributes() {
        // Given
        Map<String, String> attributes = new HashMap<>();
        attributes.put("ref-type", "fig");
        attributes.put("rid", "fig1");

        TextContent.InlineElement xref = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.XREF)
                .content("Figure 1")
                .startIndex(4)
                .endIndex(12)
                .attributes(attributes)
                .build();

        // When
        TextContent content = TextContent.builder()
                .plainText("See Figure 1")
                .inlineElements(Arrays.asList(xref))
                .build();

        // Then
        assertThat(content.getInlineElements()).hasSize(1);
        TextContent.InlineElement element = content.getInlineElements().get(0);
        assertThat(element.getType()).isEqualTo(TextContent.InlineType.XREF);
        assertThat(element.getAttributes()).containsEntry("ref-type", "fig");
        assertThat(element.getAttributes()).containsEntry("rid", "fig1");
    }

    @Test
    @DisplayName("inlineElements: ext-link 요소의 xlink:href 속성")
    void shouldParseExtLinkWithXlinkHref() {
        // Given
        Map<String, String> attributes = new HashMap<>();
        attributes.put("ext-link-type", "uri");
        attributes.put("xlink:href", "https://example.com");

        TextContent.InlineElement extLink = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.EXT_LINK)
                .content("example.com")
                .attributes(attributes)
                .build();

        // When
        TextContent content = TextContent.builder()
                .inlineElements(Arrays.asList(extLink))
                .build();

        // Then
        TextContent.InlineElement element = content.getInlineElements().get(0);
        assertThat(element.getAttributes()).containsEntry("xlink:href", "https://example.com");
    }

    // ==================== INLINE TYPE TESTS ====================

    @Test
    @DisplayName("InlineType: 모든 타입 지원 확인")
    void shouldSupportAllInlineTypes() {
        // Given & When & Then
        assertThat(TextContent.InlineType.BOLD).isNotNull();
        assertThat(TextContent.InlineType.ITALIC).isNotNull();
        assertThat(TextContent.InlineType.UNDERLINE).isNotNull();
        assertThat(TextContent.InlineType.SUPERSCRIPT).isNotNull();
        assertThat(TextContent.InlineType.SUBSCRIPT).isNotNull();
        assertThat(TextContent.InlineType.MONOSPACE).isNotNull();
        assertThat(TextContent.InlineType.SMALL_CAPS).isNotNull();
        assertThat(TextContent.InlineType.XREF).isNotNull();
        assertThat(TextContent.InlineType.EXT_LINK).isNotNull();
        assertThat(TextContent.InlineType.NAMED_CONTENT).isNotNull();
        assertThat(TextContent.InlineType.STYLED_CONTENT).isNotNull();
    }

    // ==================== COMPLEX MIXED CONTENT TESTS ====================

    @Test
    @DisplayName("복잡한 mixed content: 여러 인라인 요소 조합")
    void shouldHandleComplexMixedContent() {
        // Given
        String plainText = "The formula E=mc2 is Einstein's famous equation.";
        String htmlText = "The formula <i>E</i>=<i>m</i><i>c</i><sup>2</sup> is Einstein's famous equation.";
        String rawXml = "<p>The formula <italic>E</italic>=<italic>m</italic><italic>c</italic><sup>2</sup> is Einstein's famous equation.</p>";

        TextContent.InlineElement italic1 = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.ITALIC)
                .content("E")
                .startIndex(12)
                .endIndex(13)
                .build();

        TextContent.InlineElement italic2 = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.ITALIC)
                .content("m")
                .startIndex(14)
                .endIndex(15)
                .build();

        TextContent.InlineElement italic3 = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.ITALIC)
                .content("c")
                .startIndex(15)
                .endIndex(16)
                .build();

        TextContent.InlineElement sup = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.SUPERSCRIPT)
                .content("2")
                .startIndex(16)
                .endIndex(17)
                .build();

        List<TextContent.InlineElement> elements = Arrays.asList(italic1, italic2, italic3, sup);

        // When
        TextContent content = TextContent.builder()
                .plainText(plainText)
                .htmlText(htmlText)
                .rawXml(rawXml)
                .inlineElements(elements)
                .build();

        // Then
        assertThat(content.getPlainText()).isEqualTo("The formula E=mc2 is Einstein's famous equation.");
        assertThat(content.getHtmlText()).contains("<i>E</i>=<i>m</i><i>c</i><sup>2</sup>");
        assertThat(content.getInlineElements()).hasSize(4);
        assertThat(content.getInlineElements())
                .extracting(TextContent.InlineElement::getType)
                .containsExactly(
                        TextContent.InlineType.ITALIC,
                        TextContent.InlineType.ITALIC,
                        TextContent.InlineType.ITALIC,
                        TextContent.InlineType.SUPERSCRIPT
                );
    }

    @Test
    @DisplayName("복잡한 mixed content: 중첩된 xref와 마크업")
    void shouldHandleNestedXrefAndMarkup() {
        // Given
        String plainText = "See Figure 1 and Table 2 for details.";

        Map<String, String> xrefAttrs1 = new HashMap<>();
        xrefAttrs1.put("ref-type", "fig");
        xrefAttrs1.put("rid", "fig1");

        Map<String, String> xrefAttrs2 = new HashMap<>();
        xrefAttrs2.put("ref-type", "table");
        xrefAttrs2.put("rid", "table2");

        TextContent.InlineElement xref1 = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.XREF)
                .content("Figure 1")
                .startIndex(4)
                .endIndex(12)
                .attributes(xrefAttrs1)
                .build();

        TextContent.InlineElement xref2 = TextContent.InlineElement.builder()
                .type(TextContent.InlineType.XREF)
                .content("Table 2")
                .startIndex(17)
                .endIndex(24)
                .attributes(xrefAttrs2)
                .build();

        // When
        TextContent content = TextContent.builder()
                .plainText(plainText)
                .inlineElements(Arrays.asList(xref1, xref2))
                .build();

        // Then
        assertThat(content.getInlineElements()).hasSize(2);
        assertThat(content.getInlineElements().get(0).getAttributes()).containsEntry("rid", "fig1");
        assertThat(content.getInlineElements().get(1).getAttributes()).containsEntry("rid", "table2");
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("빈 TextContent 객체")
    void shouldHandleEmptyTextContent() {
        // When
        TextContent content = TextContent.builder().build();

        // Then
        assertThat(content.getPlainText()).isNull();
        assertThat(content.getHtmlText()).isNull();
        assertThat(content.getRawXml()).isNull();
        assertThat(content.getInlineElements()).isNull();
    }

    @Test
    @DisplayName("null 값 처리")
    void shouldHandleNullValues() {
        // When
        TextContent content = TextContent.builder()
                .plainText(null)
                .htmlText(null)
                .rawXml(null)
                .inlineElements(null)
                .build();

        // Then
        assertThat(content.getPlainText()).isNull();
        assertThat(content.getHtmlText()).isNull();
        assertThat(content.getRawXml()).isNull();
        assertThat(content.getInlineElements()).isNull();
    }

    @Test
    @DisplayName("빈 문자열 처리")
    void shouldHandleEmptyStrings() {
        // When
        TextContent content = TextContent.builder()
                .plainText("")
                .htmlText("")
                .rawXml("")
                .build();

        // Then
        assertThat(content.getPlainText()).isEmpty();
        assertThat(content.getHtmlText()).isEmpty();
        assertThat(content.getRawXml()).isEmpty();
    }

    @Test
    @DisplayName("특수 문자 처리")
    void shouldHandleSpecialCharacters() {
        // Given
        String plainText = "Special chars: <>&\"'";
        String htmlText = "Special chars: &lt;&gt;&amp;&quot;&apos;";

        // When
        TextContent content = TextContent.builder()
                .plainText(plainText)
                .htmlText(htmlText)
                .build();

        // Then
        assertThat(content.getPlainText()).contains("<>&\"'");
        assertThat(content.getHtmlText()).contains("&lt;&gt;&amp;");
    }
}
