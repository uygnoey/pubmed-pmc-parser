package com.brillianttiger.bio.parser.common.parser;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StaxParserUtils 테스트
 */
class StaxParserUtilsTest {

    private static final String TEST_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <root>
                <article id="123" status="published">
                    <title>Test Article</title>
                    <author name="John Doe" email="john@example.com"/>
                    <abstract>This is a test abstract.</abstract>
                </article>
                <metadata version="1.0"/>
            </root>
            """;

    private XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        return factory.createXMLStreamReader(new StringReader(xml));
    }

    @Test
    void testMoveToStartElement() throws Exception {
        XMLStreamReader reader = createReader(TEST_XML);

        // 첫 번째 시작 요소로 이동 (root)
        assertTrue(StaxParserUtils.moveToStartElement(reader));
        assertEquals("root", reader.getLocalName());

        // 다음 시작 요소로 이동 (article)
        assertTrue(StaxParserUtils.moveToStartElement(reader));
        assertEquals("article", reader.getLocalName());
    }

    @Test
    void testMoveToStartElementWithName() throws Exception {
        XMLStreamReader reader = createReader(TEST_XML);

        // "title" 요소로 이동
        assertTrue(StaxParserUtils.moveToStartElement(reader, "title"));
        assertEquals("title", reader.getLocalName());

        // "abstract" 요소로 이동
        assertTrue(StaxParserUtils.moveToStartElement(reader, "abstract"));
        assertEquals("abstract", reader.getLocalName());

        // 존재하지 않는 요소
        assertFalse(StaxParserUtils.moveToStartElement(reader, "nonexistent"));
    }

    @Test
    void testGetAttributes() throws Exception {
        XMLStreamReader reader = createReader(TEST_XML);

        // article 요소로 이동
        StaxParserUtils.moveToStartElement(reader, "article");

        // 속성 가져오기
        Map<String, String> attrs = StaxParserUtils.getAttributes(reader);
        assertEquals(2, attrs.size());
        assertEquals("123", attrs.get("id"));
        assertEquals("published", attrs.get("status"));

        // author 요소로 이동
        StaxParserUtils.moveToStartElement(reader, "author");
        attrs = StaxParserUtils.getAttributes(reader);
        assertEquals(2, attrs.size());
        assertEquals("John Doe", attrs.get("name"));
        assertEquals("john@example.com", attrs.get("email"));
    }

    @Test
    void testIsStartElement() throws Exception {
        XMLStreamReader reader = createReader(TEST_XML);

        // root 요소로 이동
        StaxParserUtils.moveToStartElement(reader);
        assertTrue(StaxParserUtils.isStartElement(reader, "root"));
        assertFalse(StaxParserUtils.isStartElement(reader, "article"));

        // article 요소로 이동
        StaxParserUtils.moveToStartElement(reader);
        assertTrue(StaxParserUtils.isStartElement(reader, "article"));
        assertFalse(StaxParserUtils.isStartElement(reader, "root"));
    }

    @Test
    void testIsEndElement() throws Exception {
        XMLStreamReader reader = createReader(TEST_XML);

        // title 요소로 이동하고 끝까지 읽기
        StaxParserUtils.moveToStartElement(reader, "title");

        // 텍스트 읽기
        reader.next(); // CHARACTERS

        // 종료 요소로 이동
        reader.next(); // END_ELEMENT
        assertTrue(StaxParserUtils.isEndElement(reader, "title"));
        assertFalse(StaxParserUtils.isEndElement(reader, "article"));
    }

    @Test
    void testGetAttributesEmpty() throws Exception {
        XMLStreamReader reader = createReader(TEST_XML);

        // title 요소 (속성 없음)
        StaxParserUtils.moveToStartElement(reader, "title");
        Map<String, String> attrs = StaxParserUtils.getAttributes(reader);
        assertTrue(attrs.isEmpty());
    }

    @Test
    void testGetNamespaceURI() throws Exception {
        String nsXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <root xmlns="http://example.com/ns">
                    <element>Test</element>
                </root>
                """;

        XMLStreamReader reader = createReader(nsXml);
        StaxParserUtils.moveToStartElement(reader);

        String namespaceURI = StaxParserUtils.getNamespaceURI(reader);
        assertEquals("http://example.com/ns", namespaceURI);
    }

    @Test
    void testGetPrefix() throws Exception {
        String nsXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <ex:root xmlns:ex="http://example.com/ns">
                    <ex:element>Test</ex:element>
                </ex:root>
                """;

        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(nsXml));

        StaxParserUtils.moveToStartElement(reader);

        String prefix = StaxParserUtils.getPrefix(reader);
        assertEquals("ex", prefix);
    }

    @Test
    void testAttributePreservesOrder() throws Exception {
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <element first="1" second="2" third="3" fourth="4"/>
                """;

        XMLStreamReader reader = createReader(xml);
        StaxParserUtils.moveToStartElement(reader);

        Map<String, String> attrs = StaxParserUtils.getAttributes(reader);

        // LinkedHashMap 사용으로 순서 보존 확인
        assertEquals(4, attrs.size());
        String[] keys = attrs.keySet().toArray(new String[0]);
        assertEquals("first", keys[0]);
        assertEquals("second", keys[1]);
        assertEquals("third", keys[2]);
        assertEquals("fourth", keys[3]);
    }

    @Test
    void testMoveToStartElementNoMatch() throws Exception {
        // Line 27: while (reader.hasNext()) false branch
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <root><child/></root>
                """;

        XMLStreamReader reader = createReader(xml);
        StaxParserUtils.moveToStartElement(reader, "root");

        // Move past root and child
        reader.next(); // END_ELEMENT root
        reader.next(); // END_DOCUMENT

        // Should return false when no more elements
        assertFalse(StaxParserUtils.moveToStartElement(reader));
    }

    @Test
    void testIsAtStartElementFalse() throws Exception {
        // Line 80: getEventType() == START_ELEMENT false branch
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <root>text</root>
                """;

        XMLStreamReader reader = createReader(xml);
        StaxParserUtils.moveToStartElement(reader);
        reader.next(); // Move to CHARACTERS

        // Should return false when not at START_ELEMENT
        assertFalse(StaxParserUtils.isStartElement(reader, "root"));
    }

    @Test
    void testIsAtEndElementFalse() throws Exception {
        // Line 92: getEventType() == END_ELEMENT false branch
        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <root>text</root>
                """;

        XMLStreamReader reader = createReader(xml);
        StaxParserUtils.moveToStartElement(reader);

        // At START_ELEMENT, should return false
        assertFalse(StaxParserUtils.isEndElement(reader, "root"));
    }
}
