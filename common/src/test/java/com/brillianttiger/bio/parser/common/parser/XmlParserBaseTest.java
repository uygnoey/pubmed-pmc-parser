package com.brillianttiger.bio.parser.common.parser;

import com.brillianttiger.bio.parser.common.model.TextContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

/**
 * XmlParserBaseTest / XmlParserBase 테스트
 *
 * KR: XML 파서 기본 클래스의 보안 및 기능 테스트
 * EN: Tests for XML parser base class security and functionality
 */
@DisplayName("XmlParserBase 테스트")
class XmlParserBaseTest {

    @TempDir
    Path tempDir;

    // Test implementation of XmlParserBase for testing purposes
    private static class TestParser extends XmlParserBase {
        // Public wrapper for testing protected createReader method
        public XMLStreamReader createPublicReader(Path path) throws Exception {
            return super.createReader(path);
        }

        // Public wrapper for testing protected createReader method
        public XMLStreamReader createPublicReader(InputStream is) throws Exception {
            return super.createReader(is);
        }

        public InputStream openPublicInputStream(Path path) throws IOException {
            return super.openInputStream(path);
        }

        public String getPublicElementText(XMLStreamReader reader) throws XMLStreamException {
            return super.getElementText(reader);
        }

        public String readPublicElementText(XMLStreamReader reader) throws XMLStreamException {
            return super.readElementText(reader);
        }

        public TextContent getPublicMixedContent(XMLStreamReader reader, String endTag) throws XMLStreamException {
            return super.getMixedContent(reader, endTag);
        }

        public TextContent getPublicExtractTextContent(XMLStreamReader reader, String endTag) throws XMLStreamException {
            return super.extractTextContent(reader, endTag);
        }

        public void skipPublicElement(XMLStreamReader reader) throws XMLStreamException {
            super.skipElement(reader);
        }

        public String getPublicAttribute(XMLStreamReader reader, String name) {
            return super.getAttribute(reader, name);
        }

        public String getPublicAttribute(XMLStreamReader reader, String namespace, String name) {
            return super.getAttribute(reader, namespace, name);
        }

        public String getPublicAttributeOrDefault(XMLStreamReader reader, String name, String defaultValue) {
            return super.getAttributeOrDefault(reader, name, defaultValue);
        }

        public String getPublicRequiredAttribute(XMLStreamReader reader, String name) throws XMLStreamException {
            return super.getRequiredAttribute(reader, name);
        }

        public boolean getPublicBooleanAttribute(XMLStreamReader reader, String name, boolean defaultValue) {
            return super.getBooleanAttribute(reader, name, defaultValue);
        }
    }

    // ==================== XXE SECURITY TESTS ====================

    @Test
    @DisplayName("XXE 공격 방지: 외부 엔티티 참조 차단")
    void shouldPreventXXEWithExternalEntity() {
        // Given: Malicious XML with external entity reference
        String maliciousXml = """
                <?xml version="1.0"?>
                <!DOCTYPE root [
                  <!ENTITY xxe SYSTEM "file:///etc/passwd">
                ]>
                <root>&xxe;</root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(maliciousXml.getBytes());

        // When & Then: Should fail to parse due to security settings
        assertThatThrownBy(() -> {
            XMLStreamReader reader = parser.createPublicReader(is);
            // Try to read the content - should fail before this
            while (reader.hasNext()) {
                reader.next();
            }
        }).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("XXE 공격 방지: DTD 처리 차단")
    void shouldPreventXXEWithDTD() {
        // Given: XML with DTD declaration
        String xmlWithDtd = """
                <?xml version="1.0"?>
                <!DOCTYPE root [
                  <!ELEMENT root (#PCDATA)>
                  <!ENTITY internal "internal entity value">
                ]>
                <root>&internal;</root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xmlWithDtd.getBytes());

        // When & Then: Should fail due to DTD being disabled
        assertThatThrownBy(() -> {
            XMLStreamReader reader = parser.createPublicReader(is);
            while (reader.hasNext()) {
                reader.next();
            }
        }).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("XXE 공격 방지: 외부 파일 참조 차단")
    void shouldPreventExternalFileReference() {
        // Given: XML trying to load external file
        String maliciousXml = """
                <?xml version="1.0"?>
                <!DOCTYPE root [
                  <!ENTITY ext SYSTEM "file:///etc/hosts">
                ]>
                <root>&ext;</root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(maliciousXml.getBytes());

        // When & Then: Should be blocked
        assertThatThrownBy(() -> {
            XMLStreamReader reader = parser.createPublicReader(is);
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.CHARACTERS) {
                    reader.getText(); // Should not reach here
                }
            }
        }).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("XXE 공격 방지: 매개변수 엔티티 차단")
    void shouldPreventParameterEntity() {
        // Given: XML with parameter entity and entity reference
        String maliciousXml = """
                <?xml version="1.0"?>
                <!DOCTYPE root [
                  <!ENTITY % param1 "<!ENTITY &#x25; param2 '&xxe;'>">
                  <!ENTITY xxe "test">
                  %param1;
                ]>
                <root>&xxe;</root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(maliciousXml.getBytes());

        // When & Then: Should be blocked
        assertThatThrownBy(() -> {
            XMLStreamReader reader = parser.createPublicReader(is);
            while (reader.hasNext()) {
                reader.next();
            }
        }).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("XXE 공격 방지: 외부 DTD 차단")
    void shouldPreventExternalDTD() {
        // Given: XML referencing external DTD with entity usage
        String maliciousXml = """
                <?xml version="1.0"?>
                <!DOCTYPE root SYSTEM "http://malicious.com/evil.dtd" [
                  <!ENTITY evil "malicious content">
                ]>
                <root>&evil;</root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(maliciousXml.getBytes());

        // When & Then: Should be blocked
        assertThatThrownBy(() -> {
            XMLStreamReader reader = parser.createPublicReader(is);
            while (reader.hasNext()) {
                reader.next();
            }
        }).isInstanceOf(Exception.class);
    }

    // ==================== SAFE XML PARSING TESTS ====================

    @Test
    @DisplayName("정상 XML 파싱: 간단한 요소")
    void shouldParseSimpleXml() throws Exception {
        // Given: Safe XML without DTD
        String safeXml = """
                <?xml version="1.0"?>
                <root>
                    <element>Hello, World!</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(safeXml.getBytes());

        // When
        XMLStreamReader reader = parser.createPublicReader(is);
        String elementText = null;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                elementText = parser.getPublicElementText(reader);
                break;
            }
        }

        // Then
        assertThat(elementText).isEqualTo("Hello, World!");
    }

    @Test
    @DisplayName("정상 XML 파싱: 속성 읽기")
    void shouldParseAttributes() throws Exception {
        // Given
        String xmlWithAttributes = """
                <?xml version="1.0"?>
                <root>
                    <element id="123" type="test" flag="Y">Content</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xmlWithAttributes.getBytes());

        // When
        XMLStreamReader reader = parser.createPublicReader(is);
        String id = null;
        String type = null;
        boolean flag = false;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                id = parser.getPublicAttribute(reader, "id");
                type = parser.getPublicAttribute(reader, "type");
                flag = parser.getPublicBooleanAttribute(reader, "flag", false);
                break;
            }
        }

        // Then
        assertThat(id).isEqualTo("123");
        assertThat(type).isEqualTo("test");
        assertThat(flag).isTrue();
    }

    @Test
    @DisplayName("정상 XML 파싱: Mixed content")
    void shouldParseMixedContent() throws Exception {
        // Given
        String xmlWithMixedContent = """
                <?xml version="1.0"?>
                <root>
                    <title>Formula: E=mc<sup>2</sup> is famous</title>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xmlWithMixedContent.getBytes());

        // When
        XMLStreamReader reader = parser.createPublicReader(is);
        TextContent content = null;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "title".equals(reader.getLocalName())) {
                content = parser.getPublicMixedContent(reader, "title");
                break;
            }
        }

        // Then
        assertThat(content).isNotNull();
        assertThat(content.getPlainText()).contains("E=mc2");
        assertThat(content.getHtmlText()).contains("<sup>2</sup>");
    }

    // ==================== GZIP HANDLING TESTS ====================

    @Test
    @DisplayName("GZip 파일 자동 처리")
    void shouldHandleGzipFiles() throws Exception {
        // Given: Create GZip file
        Path xmlFile = tempDir.resolve("test.xml");
        Path gzipFile = tempDir.resolve("test.xml.gz");
        String content = """
                <?xml version="1.0"?>
                <root><element>Test content</element></root>
                """;

        Files.writeString(xmlFile, content);
        com.brillianttiger.bio.parser.common.util.GzipUtils.compress(xmlFile, gzipFile);

        // When
        TestParser parser = new TestParser();
        XMLStreamReader reader = parser.createPublicReader(gzipFile);
        String elementText = null;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                elementText = parser.getPublicElementText(reader);
                break;
            }
        }

        // Then
        assertThat(elementText).isEqualTo("Test content");
    }

    @Test
    @DisplayName("일반 XML 파일 처리")
    void shouldHandleRegularXmlFiles() throws Exception {
        // Given
        Path xmlFile = tempDir.resolve("test.xml");
        String content = """
                <?xml version="1.0"?>
                <root><element>Regular content</element></root>
                """;

        Files.writeString(xmlFile, content);

        // When
        TestParser parser = new TestParser();
        XMLStreamReader reader = parser.createPublicReader(xmlFile);
        String elementText = null;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                elementText = parser.getPublicElementText(reader);
                break;
            }
        }

        // Then
        assertThat(elementText).isEqualTo("Regular content");
    }

    // ==================== UTILITY METHOD TESTS ====================

    @Test
    @DisplayName("skipElement: 중첩 요소 건너뛰기")
    void shouldSkipNestedElements() throws Exception {
        // Given
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <skip>
                        <nested>
                            <deep>Content to skip</deep>
                        </nested>
                    </skip>
                    <keep>Important</keep>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When: Skip the first element, read the second
        String keepText = null;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String name = reader.getLocalName();
                if ("skip".equals(name)) {
                    parser.skipPublicElement(reader);
                } else if ("keep".equals(name)) {
                    keepText = parser.getPublicElementText(reader);
                    break;
                }
            }
        }

        // Then
        assertThat(keepText).isEqualTo("Important");
    }

    @Test
    @DisplayName("getAttributeOrDefault: 기본값 제공")
    void shouldReturnDefaultValue() throws Exception {
        // Given
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element id="123">Content</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When
        String id = null;
        String missingAttr = null;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                id = parser.getPublicAttributeOrDefault(reader, "id", "default");
                missingAttr = parser.getPublicAttributeOrDefault(reader, "missing", "default");
                break;
            }
        }

        // Then
        assertThat(id).isEqualTo("123");
        assertThat(missingAttr).isEqualTo("default");
    }

    @Test
    @DisplayName("getRequiredAttribute: 필수 속성 누락 시 예외")
    void shouldThrowExceptionForMissingRequiredAttribute() throws Exception {
        // Given
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element>Content</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When & Then
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                XMLStreamReader finalReader = reader;
                assertThatThrownBy(() -> parser.getPublicRequiredAttribute(finalReader, "required"))
                        .isInstanceOf(XMLStreamException.class)
                        .hasMessageContaining("Required attribute 'required' not found");
                break;
            }
        }
    }

    @Test
    @DisplayName("getBooleanAttribute: Y/yes/true 변환")
    void shouldConvertBooleanAttributes() throws Exception {
        // Given
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element flagY="Y" flagYes="yes" flagTrue="true" flagN="N">Content</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When
        boolean flagY = false;
        boolean flagYes = false;
        boolean flagTrue = false;
        boolean flagN = true;
        boolean flagMissing = false;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                flagY = parser.getPublicBooleanAttribute(reader, "flagY", false);
                flagYes = parser.getPublicBooleanAttribute(reader, "flagYes", false);
                flagTrue = parser.getPublicBooleanAttribute(reader, "flagTrue", false);
                flagN = parser.getPublicBooleanAttribute(reader, "flagN", true);
                flagMissing = parser.getPublicBooleanAttribute(reader, "missing", false);
                break;
            }
        }

        // Then
        assertThat(flagY).isTrue();
        assertThat(flagYes).isTrue();
        assertThat(flagTrue).isTrue();
        assertThat(flagN).isFalse();
        assertThat(flagMissing).isFalse();
    }

    @Test
    @DisplayName("readElementText: 별칭 메서드 테스트")
    void shouldReadElementTextWithAlias() throws Exception {
        // Given
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element>Test content</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When
        String text = null;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                text = parser.readPublicElementText(reader);
                break;
            }
        }

        // Then
        assertThat(text).isEqualTo("Test content");
    }

    // ==================== SECURITY EDGE CASES ====================

    @Test
    @DisplayName("보안: Billion Laughs 공격 방지")
    void shouldPreventBillionLaughsAttack() {
        // Given: Exponential entity expansion attack
        String maliciousXml = """
                <?xml version="1.0"?>
                <!DOCTYPE root [
                  <!ENTITY lol "lol">
                  <!ENTITY lol1 "&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;">
                  <!ENTITY lol2 "&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;">
                ]>
                <root>&lol2;</root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(maliciousXml.getBytes());

        // When & Then: Should be blocked by DTD prevention
        assertThatThrownBy(() -> {
            XMLStreamReader reader = parser.createPublicReader(is);
            while (reader.hasNext()) {
                reader.next();
            }
        }).isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("보안: CDATA 섹션 정상 처리")
    void shouldHandleCDATASafely() throws Exception {
        // Given: CDATA is safe and should work
        String xmlWithCdata = """
                <?xml version="1.0"?>
                <root>
                    <element><![CDATA[<script>alert('safe')</script>]]></element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xmlWithCdata.getBytes());

        // When
        XMLStreamReader reader = parser.createPublicReader(is);
        String text = null;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                text = parser.getPublicElementText(reader);
                break;
            }
        }

        // Then: CDATA content should be preserved as-is
        assertThat(text).contains("<script>alert('safe')</script>");
    }

    @Test
    @DisplayName("보안: 네임스페이스 정상 처리")
    void shouldHandleNamespacesSafely() throws Exception {
        // Given
        String xmlWithNamespace = """
                <?xml version="1.0"?>
                <root xmlns:custom="http://example.com/custom">
                    <custom:element custom:attr="value">Content</custom:element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xmlWithNamespace.getBytes());

        // When
        XMLStreamReader reader = parser.createPublicReader(is);
        String attrValue = null;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                attrValue = parser.getPublicAttribute(reader, "http://example.com/custom", "attr");
                break;
            }
        }

        // Then
        assertThat(attrValue).isEqualTo("value");
    }

    @Test
    @DisplayName(".gzip 확장자 파일 자동 처리")
    void shouldHandleGzipExtensionFiles() throws Exception {
        // Given: Create .gzip file (not .gz)
        Path xmlFile = tempDir.resolve("test.xml");
        Path gzipFile = tempDir.resolve("test.xml.gzip");
        String content = """
                <?xml version="1.0"?>
                <root><element>Gzip extension test</element></root>
                """;

        Files.writeString(xmlFile, content);
        com.brillianttiger.bio.parser.common.util.GzipUtils.compress(xmlFile, gzipFile);

        // When
        TestParser parser = new TestParser();
        XMLStreamReader reader = parser.createPublicReader(gzipFile);

        // Then: Should successfully decompress and read
        assertThat(reader).isNotNull();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                String text = parser.getPublicElementText(reader);
                assertThat(text).isEqualTo("Gzip extension test");
                break;
            }
        }
    }

    @Test
    @DisplayName("여러 속성이 있는 요소 처리")
    void shouldHandleMultipleAttributes() throws Exception {
        // Given: Element with multiple attributes
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element attr1="value1" attr2="value2" attr3="value3" attr4="value4" attr5="value5"/>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When: Read all attributes
        String attr1 = null, attr2 = null, attr3 = null, attr4 = null, attr5 = null;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                attr1 = parser.getPublicAttribute(reader, "attr1");
                attr2 = parser.getPublicAttribute(reader, "attr2");
                attr3 = parser.getPublicAttribute(reader, "attr3");
                attr4 = parser.getPublicAttribute(reader, "attr4");
                attr5 = parser.getPublicAttribute(reader, "attr5");
                break;
            }
        }

        // Then: All attributes should be read correctly
        assertThat(attr1).isEqualTo("value1");
        assertThat(attr2).isEqualTo("value2");
        assertThat(attr3).isEqualTo("value3");
        assertThat(attr4).isEqualTo("value4");
        assertThat(attr5).isEqualTo("value5");
    }

    @Test
    @DisplayName("getAttributeOrDefault: null 속성 처리")
    void shouldHandleNullAttributeWithDefault() throws Exception {
        // Given: Element without the attribute
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element existing="value"/>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When
        String value = null;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                value = parser.getPublicAttributeOrDefault(reader, "nonexistent", "default");
                break;
            }
        }

        // Then
        assertThat(value).isEqualTo("default");
    }

    @Test
    @DisplayName("getMixedContent: inline tags 변환")
    void shouldConvertInlineTagsInMixedContent() throws Exception {
        // Given: Mixed content with various inline tags
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <content>
                        Plain text with <b>bold</b> and <i>italic</i> and <u>underline</u>.
                        Also <sup>superscript</sup> and <sub>subscript</sub>.
                        Special: <sc>small caps</sc> and <monospace>code</monospace>.
                        Unknown <unknown>tag</unknown>.
                    </content>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When
        TextContent result = null;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "content".equals(reader.getLocalName())) {
                result = parser.getPublicMixedContent(reader, "content");
                break;
            }
        }

        // Then: Should convert to HTML tags
        assertThat(result).isNotNull();
        assertThat(result.getPlainText()).contains("Plain text", "bold", "italic");
        assertThat(result.getHtmlText()).contains("<strong>", "<em>", "<u>", "<sup>", "<sub>", "<span>", "<code>");
    }

    @Test
    @DisplayName("getBooleanAttribute: null 속성 처리")
    void shouldHandleNullBooleanAttribute() throws Exception {
        // Given: Element without the boolean attribute
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element/>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When
        boolean result = false;
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                result = parser.getPublicBooleanAttribute(reader, "nonexistent", true);
                break;
            }
        }

        // Then: Should return default value
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("getRequiredAttribute: blank 값 예외")
    void shouldThrowExceptionForBlankRequiredAttribute() throws Exception {
        // Given: Element with blank required attribute
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element required="   "/>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // When/Then: Should throw exception for blank value
        assertThatThrownBy(() -> {
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                    parser.getPublicRequiredAttribute(reader, "required");
                    break;
                }
            }
        }).isInstanceOf(XMLStreamException.class)
          .hasMessageContaining("required");
    }

    @Test
    @DisplayName("getElementText: COMMENT 무시")
    void shouldIgnoreCommentInElementText() throws Exception {
        // Line 103: switch default case coverage
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element>text<!-- comment -->more</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // Move to element
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                String text = parser.getPublicElementText(reader);
                assertThat(text).isEqualTo("textmore");
                break;
            }
        }
    }

    @Test
    @DisplayName("getMixedContent: COMMENT 무시")
    void shouldIgnoreCommentInMixedContent() throws Exception {
        // Line 155: switch default case coverage
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element>text<!-- comment --><b>bold</b></element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // Move to element
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                TextContent content = parser.getPublicMixedContent(reader, "element");
                assertThat(content.getPlainText()).contains("text");
                assertThat(content.getPlainText()).contains("bold");
                break;
            }
        }
    }

    @Test
    @DisplayName("getRequiredAttribute: 정상 값 반환")
    void shouldReturnValidRequiredAttribute() throws Exception {
        // Line 281: value != null && !value.isBlank() branch coverage
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element required="valid-value"/>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // Move to element
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                String value = parser.getPublicRequiredAttribute(reader, "required");
                assertThat(value).isEqualTo("valid-value");
                break;
            }
        }
    }

    @Test
    @DisplayName("getMixedContent: 속성 없는 요소")
    void shouldHandleElementWithoutAttributes() throws Exception {
        // Line 334: for (int i = 0; i < count; i++) with count == 0
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element><child>text</child></element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // Move to element
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                TextContent content = parser.getPublicMixedContent(reader, "element");
                assertThat(content.getPlainText()).contains("text");
                break;
            }
        }
    }

    @Test
    @DisplayName("getElementText: PROCESSING_INSTRUCTION 무시")
    void shouldIgnoreProcessingInstructionInElementText() throws Exception {
        // Line 103: switch default case with PROCESSING_INSTRUCTION
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element>text<?target instruction?>more</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // Move to element
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                String text = parser.getPublicElementText(reader);
                assertThat(text).isEqualTo("textmore");
                break;
            }
        }
    }

    @Test
    @DisplayName("getMixedContent: 속성 있는 중첩 요소")
    void shouldHandleNestedElementWithAttributes() throws Exception {
        // Line 334: for (int i = 0; i < count; i++) with count > 0
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element><child id="1" class="test">text</child></element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // Move to element
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                TextContent content = parser.getPublicMixedContent(reader, "element");
                assertThat(content.getPlainText()).contains("text");
                assertThat(content.getRawXml()).contains("id=\"1\"");
                assertThat(content.getRawXml()).contains("class=\"test\"");
                break;
            }
        }
    }

    @Test
    @DisplayName("getElementText: 중첩 요소 건너뛰기")
    void shouldSkipNestedElementInElementText() throws Exception {
        // Line 112-113: START_ELEMENT case with skipElement()
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element>before<nested>skip this</nested>after</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // Move to element
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                String text = parser.getPublicElementText(reader);
                // Nested element should be skipped, only get "before" and "after"
                assertThat(text).isEqualTo("beforeafter");
                break;
            }
        }
    }

    @Test
    @DisplayName("extractTextContent: getMixedContent 별칭 메서드")
    void shouldUseExtractTextContentAlias() throws Exception {
        // Line 209: extractTextContent() alias method
        String xml = """
                <?xml version="1.0"?>
                <root>
                    <element>text<b>bold</b>more</element>
                </root>
                """;

        TestParser parser = new TestParser();
        InputStream is = new ByteArrayInputStream(xml.getBytes());
        XMLStreamReader reader = parser.createPublicReader(is);

        // Move to element
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT && "element".equals(reader.getLocalName())) {
                TextContent content = parser.getPublicExtractTextContent(reader, "element");
                assertThat(content.getPlainText()).contains("text");
                assertThat(content.getPlainText()).contains("bold");
                assertThat(content.getPlainText()).contains("more");
                break;
            }
        }
    }
}
