package com.brillianttiger.bio.parser.common.parser;

import com.brillianttiger.bio.parser.common.model.TextContent;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.*;
import java.util.zip.GZIPInputStream;

/**
 * XmlParserBase / XML 파서 기본 클래스
 *
 * KR: XML 파서 기본 클래스. StAX 기반 파싱의 공통 기능 제공.
 * EN: Base class for XML parsers. Provides common functionality for StAX-based parsing.
 */
public abstract class XmlParserBase {

    /**
     * 버퍼 크기 상수 (64KB) / Buffer size constant (64KB)
     */
    protected static final int BUFFER_SIZE = 65536; // 64 * 1024

    protected final XMLInputFactory factory;

    protected XmlParserBase() {
        factory = XMLInputFactory.newInstance();
        configureFactory();
    }

    /**
     * XMLInputFactory 보안 설정 / Configure XMLInputFactory security settings
     *
     * KR: XXE (XML External Entity) 공격 방지
     * EN: Prevent XXE (XML External Entity) attacks
     */
    private void configureFactory() {
        // XXE 공격 방지 / Prevent XXE attacks
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);

        // 성능 최적화 / Performance optimization
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
    }

    /**
     * 파일에서 InputStream 생성 (GZip 자동 감지) / Create InputStream from file (auto-detect GZip)
     *
     * @param path file path
     * @return buffered InputStream
     * @throws IOException if file read error occurs
     */
    protected InputStream openInputStream(Path path) throws IOException {
        InputStream is = Files.newInputStream(path);

        // GZip 압축 파일 처리 / Handle GZip compressed files
        String fileName = path.toString().toLowerCase();
        if (fileName.endsWith(".gz") || fileName.endsWith(".gzip")) {
            is = new GZIPInputStream(is);
        }

        return new BufferedInputStream(is, BUFFER_SIZE);
    }

    /**
     * XMLStreamReader 생성 / Create XMLStreamReader
     *
     * @param path file path
     * @return XMLStreamReader instance
     * @throws Exception if creation fails
     */
    protected XMLStreamReader createReader(Path path) throws Exception {
        return factory.createXMLStreamReader(openInputStream(path));
    }

    /**
     * XMLStreamReader 생성 / Create XMLStreamReader
     *
     * @param is InputStream
     * @return XMLStreamReader instance
     * @throws Exception if creation fails
     */
    protected XMLStreamReader createReader(InputStream is) throws Exception {
        return factory.createXMLStreamReader(is);
    }

    /**
     * 현재 요소의 텍스트 내용 추출 / Extract text content from current element
     *
     * @param reader XMLStreamReader
     * @return trimmed text content
     * @throws XMLStreamException if parsing error occurs
     */
    protected String getElementText(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder sb = new StringBuilder();

        // Note: hasNext() check is unnecessary. In well-formed XML, we always encounter END_ELEMENT.
        // If malformed, next() throws XMLStreamException.
        while (true) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.CHARACTERS:
                case XMLStreamConstants.CDATA:
                    sb.append(reader.getText());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return sb.toString().trim();
                case XMLStreamConstants.START_ELEMENT:
                    // 중첩 요소는 건너뛰기 / Skip nested elements
                    skipElement(reader);
                    break;
                default:
                    // Ignore other events (COMMENT, PROCESSING_INSTRUCTION, SPACE, etc.)
                    break;
            }
        }
    }

    /**
     * 현재 요소의 텍스트만 추출 (별칭 메서드) / Extract text only from current element (alias method)
     *
     * KR: getElementText의 별칭. 메서드 이름이 더 직관적임.
     * EN: Alias for getElementText. Method name is more intuitive.
     *
     * @param reader XMLStreamReader
     * @return trimmed text content
     * @throws XMLStreamException if parsing error occurs
     */
    protected String readElementText(XMLStreamReader reader) throws XMLStreamException {
        return getElementText(reader);
    }

    /**
     * Mixed content 추출 (마크업 보존) / Extract mixed content (preserve markup)
     *
     * @param reader XMLStreamReader
     * @param endTag end element name
     * @return TextContent with plain text, HTML, and raw XML
     * @throws XMLStreamException if parsing error occurs
     */
    protected TextContent getMixedContent(XMLStreamReader reader, String endTag)
            throws XMLStreamException {
        StringBuilder plainText = new StringBuilder();
        StringBuilder htmlText = new StringBuilder();
        StringBuilder rawXml = new StringBuilder();
        int depth = 1;

        // Note: hasNext() check is unnecessary. depth > 0 already guards the loop.
        // In well-formed XML, depth reaches 0 when END_ELEMENT is encountered.
        while (depth > 0) {
            int event = reader.next();

            switch (event) {
                case XMLStreamConstants.CHARACTERS:
                case XMLStreamConstants.CDATA:
                    String text = reader.getText();
                    plainText.append(text);
                    htmlText.append(escapeHtml(text));
                    rawXml.append(escapeXml(text));
                    break;

                case XMLStreamConstants.START_ELEMENT:
                    depth++;
                    String tag = reader.getLocalName();
                    String htmlTag = mapToHtmlTag(tag);
                    htmlText.append("<").append(htmlTag).append(">");
                    rawXml.append("<").append(tag);
                    appendAttributes(reader, rawXml);
                    rawXml.append(">");
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    depth--;
                    if (depth > 0) {
                        String closeTag = reader.getLocalName();
                        String htmlCloseTag = mapToHtmlTag(closeTag);
                        htmlText.append("</").append(htmlCloseTag).append(">");
                        rawXml.append("</").append(closeTag).append(">");
                    }
                    break;
                default:
                    // Ignore other events (COMMENT, PROCESSING_INSTRUCTION, SPACE, etc.)
                    break;
            }
        }

        return TextContent.builder()
                .plainText(plainText.toString().trim())
                .htmlText(htmlText.toString().trim())
                .rawXml(rawXml.toString().trim())
                .build();
    }

    /**
     * Mixed content 추출 (별칭 메서드) / Extract mixed content (alias method)
     *
     * KR: getMixedContent의 별칭. 메서드 이름이 더 직관적임.
     * EN: Alias for getMixedContent. Method name is more intuitive.
     *
     * @param reader XMLStreamReader
     * @param endTag end element name
     * @return TextContent with plain text, HTML, and raw XML
     * @throws XMLStreamException if parsing error occurs
     */
    protected TextContent extractTextContent(XMLStreamReader reader, String endTag)
            throws XMLStreamException {
        return getMixedContent(reader, endTag);
    }

    /**
     * 현재 요소 건너뛰기 / Skip current element
     *
     * @param reader XMLStreamReader
     * @throws XMLStreamException if parsing error occurs
     */
    protected void skipElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 1;
        // Note: hasNext() check is unnecessary. depth > 0 already guards the loop.
        // In well-formed XML, depth reaches 0 when matching END_ELEMENT is found.
        while (depth > 0) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                depth++;
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                depth--;
            }
        }
    }

    /**
     * 속성값 가져오기 (null-safe) / Get attribute value (null-safe)
     *
     * @param reader XMLStreamReader
     * @param name attribute name
     * @return attribute value or null
     */
    protected String getAttribute(XMLStreamReader reader, String name) {
        return reader.getAttributeValue(null, name);
    }

    /**
     * 속성값 가져오기 (네임스페이스 포함) / Get attribute value (with namespace)
     *
     * @param reader XMLStreamReader
     * @param namespace namespace URI
     * @param name attribute name
     * @return attribute value or null
     */
    protected String getAttribute(XMLStreamReader reader, String namespace, String name) {
        return reader.getAttributeValue(namespace, name);
    }

    /**
     * 속성값 가져오기 (기본값 제공) / Get attribute value with default
     *
     * @param reader XMLStreamReader
     * @param name attribute name
     * @param defaultValue default value if attribute not found
     * @return attribute value or default value
     */
    protected String getAttributeOrDefault(XMLStreamReader reader, String name, String defaultValue) {
        String value = getAttribute(reader, name);
        return value != null ? value : defaultValue;
    }

    /**
     * 필수 속성값 가져오기 / Get required attribute value
     *
     * KR: 속성이 없으면 XMLStreamException 발생
     * EN: Throws XMLStreamException if attribute not found
     *
     * @param reader XMLStreamReader
     * @param name attribute name
     * @return attribute value (never null)
     * @throws XMLStreamException if attribute not found
     */
    protected String getRequiredAttribute(XMLStreamReader reader, String name) throws XMLStreamException {
        String value = getAttribute(reader, name);
        if (value == null || value.isBlank()) {
            throw new XMLStreamException(
                String.format("Required attribute '%s' not found in element '%s'",
                    name, reader.getLocalName())
            );
        }
        return value;
    }

    /**
     * 속성값을 boolean으로 변환 / Convert attribute value to boolean
     *
     * @param reader XMLStreamReader
     * @param name attribute name
     * @param defaultValue default value if attribute not found
     * @return boolean value
     */
    protected boolean getBooleanAttribute(XMLStreamReader reader, String name, boolean defaultValue) {
        String value = getAttribute(reader, name);
        if (value == null) {
            return defaultValue;
        }
        return "Y".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) ||
               "true".equalsIgnoreCase(value);
    }

    /**
     * PubMed/JATS 인라인 태그를 HTML로 매핑 / Map PubMed/JATS inline tags to HTML
     *
     * @param tag XML tag name
     * @return HTML tag name
     */
    private String mapToHtmlTag(String tag) {
        return switch (tag) {
            case "b", "bold" -> "strong";
            case "i", "italic" -> "em";
            case "u", "underline" -> "u";
            case "sup" -> "sup";
            case "sub" -> "sub";
            case "sc" -> "span"; // style="font-variant: small-caps"
            case "monospace" -> "code";
            default -> "span";
        };
    }

    /**
     * 속성 추가 / Append attributes
     *
     * @param reader XMLStreamReader
     * @param sb StringBuilder to append to
     */
    private void appendAttributes(XMLStreamReader reader, StringBuilder sb) {
        int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            sb.append(" ")
              .append(reader.getAttributeLocalName(i))
              .append("=\"")
              .append(escapeXml(reader.getAttributeValue(i)))
              .append("\"");
        }
    }

    /**
     * HTML 이스케이프 / Escape HTML
     *
     * @param text input text
     * @return escaped text
     */
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }

    /**
     * XML 이스케이프 / Escape XML
     *
     * @param text input text
     * @return escaped text
     */
    private String escapeXml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }
}
