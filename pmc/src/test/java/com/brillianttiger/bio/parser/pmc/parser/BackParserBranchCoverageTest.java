package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.MixedCitation;
import com.brillianttiger.bio.parser.pmc.model.PersonGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

/**
 * BackParser의 branch coverage를 100%로 만들기 위한 테스트
 *
 * 목표: 7 missed branches → 0 missed branches
 * Lines: 459, 464, 499, 501, 531, 542, 547
 */
@DisplayName("BackParser Branch Coverage Tests")
class BackParserBranchCoverageTest {

    @Test
    @DisplayName("parseMixedCitation() - 파싱된 객체의 getValue()가 null인 경우 (Line 459 false branch)")
    void testParseMixedCitation_ChildTextNull() throws Exception {
        // Given: mixed-citation with elements that may have null values
        // When source, year, etc. are parsed but their getValue() returns null,
        // childText becomes null and line 459 false branch is triggered
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                <source></source>
                <article-title></article-title>
                Some text content
                <year></year>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: Objects with null getValue() trigger childText == null, false branch at line 459
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("Some text content");
    }

    @Test
    @DisplayName("parseMixedCitation() - XML comment for other event type (Line 464 false branch)")
    void testParseMixedCitation_CommentEvent() throws Exception {
        // Given: mixed-citation with XML comment (creates COMMENT event)
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                <!-- This is a comment -->
                Some text
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: COMMENT event (neither START_ELEMENT, CHARACTERS, nor END_ELEMENT), Line 464 false branch
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("Some text");
    }

    @Test
    @DisplayName("parseMixedCitation() - CHARACTERS event (Line 464 true branch)")
    void testParseMixedCitation_CharactersEvent() throws Exception {
        // Given: mixed-citation with direct text content
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                Some direct text content here
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: CHARACTERS event handled, Line 464 true branch covered
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("Some direct text content here");
    }

    @Test
    @DisplayName("collectElementText() - CDATA event inside isbn element (Line 499 CDATA branch)")
    void testCollectElementText_CdataEvent() throws Exception {
        // Given: isbn element with CDATA content (calls collectElementText)
        String xml = """
            <mixed-citation publication-type="book" xmlns:xlink="http://www.w3.org/1999/xlink">
                <source>Book Title</source>
                <isbn><![CDATA[978-3-16-148410-0]]></isbn>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: CDATA event inside collectElementText at Line 499
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("978-3-16-148410-0");
    }

    @Test
    @DisplayName("collectElementText() - CDATA + CHARACTERS mixed in conf-date (Line 499 both branches)")
    void testCollectElementText_CdataAndCharacters() throws Exception {
        // Given: conf-date with both CDATA and regular text
        String xml = """
            <mixed-citation publication-type="confproc" xmlns:xlink="http://www.w3.org/1999/xlink">
                <conf-date>Text before <![CDATA[2024]]> text after</conf-date>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: Both CHARACTERS and CDATA branches in collectElementText
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("2024");
    }

    @Test
    @DisplayName("collectElementText() - nested elements END_ELEMENT (Line 501 true)")
    void testCollectElementText_NestedEndElement() throws Exception {
        // Given: comment with deeply nested elements to ensure depth tracking
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                <comment>Level 0 <bold>Level 1 <italic>Level 2</italic> back to 1</bold> back to 0</comment>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: Multiple END_ELEMENT events with depth > 0, Line 501 covered
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("Level 2");
    }

    @Test
    @DisplayName("collectElementText() - XML comment in conf-loc triggers Line 501 false branch")
    void testCollectElementText_XmlCommentInElement() throws Exception {
        // Given: conf-loc element with XML comment (triggers other event type in collectElementText)
        String xml = """
            <mixed-citation publication-type="confproc" xmlns:xlink="http://www.w3.org/1999/xlink">
                <conf-loc>Location <!-- This is a comment --> Text</conf-loc>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: COMMENT event in collectElementText triggers Line 501 false branch
        // (event is not START_ELEMENT, not CHARACTERS/CDATA, not END_ELEMENT)
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("Location");
    }

    @Test
    @DisplayName("collectElementText() - Processing instruction in page-count (Line 501 false branch)")
    void testCollectElementText_ProcessingInstructionInElement() throws Exception {
        // Given: page-count with processing instruction
        String xml = """
            <mixed-citation publication-type="book" xmlns:xlink="http://www.w3.org/1999/xlink">
                <page-count count="100"><?target instruction?></page-count>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: PROCESSING_INSTRUCTION event triggers Line 501 false branch
        assertThat(citation).isNotNull();
    }

    @Test
    @DisplayName("parsePersonGroupWithText() - non-name element (Line 531 false branch)")
    void testParsePersonGroupWithText_NonNameElement() throws Exception {
        // Given: person-group with element other than name
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                <person-group person-group-type="author">
                    <collab>Research Group</collab>
                </person-group>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: non-name element in person-group, Line 531 false branch covered
        assertThat(citation).isNotNull();
    }

    @Test
    @DisplayName("parsePersonGroupWithText() - CHARACTERS event (Line 542 true)")
    void testParsePersonGroupWithText_CharactersEvent() throws Exception {
        // Given: person-group with whitespace and text between elements
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                <person-group person-group-type="author">
                    Leading text content
                    <name><surname>Smith</surname><given-names>John</given-names></name>
                    Trailing text content
                </person-group>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: Multiple CHARACTERS events in person-group, Line 542 should be covered
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("Leading text content");
        assertThat(citation.getValue()).contains("Trailing text content");
    }

    @Test
    @DisplayName("parsePersonGroupWithText() - XML comment (Line 542 false branch)")
    void testParsePersonGroupWithText_XmlComment() throws Exception {
        // Given: person-group with XML comment (not CHARACTERS or START_ELEMENT)
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                <person-group person-group-type="author">
                    <!-- Comment in person-group -->
                    <name><surname>Doe</surname></name>
                </person-group>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: COMMENT event in person-group (neither START_ELEMENT, CHARACTERS, nor END_ELEMENT at depth 1)
        assertThat(citation).isNotNull();
    }

    @Test
    @DisplayName("parsePersonGroupWithText() - nested elements with END_ELEMENT (Line 547 true branch)")
    void testParsePersonGroupWithText_NestedElements() throws Exception {
        // Given: person-group with nested elements in name
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                <person-group person-group-type="author">
                    <name>
                        <surname>Smith</surname>
                        <given-names>John</given-names>
                    </name>
                </person-group>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: END_ELEMENT events with depth > 0, Line 547 true branch covered
        assertThat(citation).isNotNull();
        assertThat(citation.getPersonGroups()).isNotEmpty();
    }

    @Test
    @DisplayName("parseMixedCitation() - comprehensive test for all branches")
    void testParseMixedCitation_ComprehensiveAllBranches() throws Exception {
        // Given: mixed-citation with multiple scenarios
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                Direct text in mixed-citation
                <person-group person-group-type="author">
                    Person group text
                    <name><surname>Smith</surname><given-names>John</given-names></name>
                    <collab>Team</collab>
                </person-group>
                <article-title>Title with <italic>italic text</italic></article-title>
                <comment>Comment with <bold>bold</bold> and <![CDATA[CDATA content]]></comment>
                <source>Journal Name</source>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then: All branches covered
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).isNotEmpty();
        assertThat(citation.getValue()).contains("Direct text");
        assertThat(citation.getValue()).contains("Person group text");
        assertThat(citation.getValue()).contains("Smith");
        assertThat(citation.getValue()).contains("John");
        assertThat(citation.getValue()).contains("italic text");
        assertThat(citation.getValue()).contains("CDATA content");
    }

    @Test
    @DisplayName("collectElementText() - Direct test with CDATA using reflection")
    void testCollectElementText_DirectCdataTest() throws Exception {
        // Given: Direct XML with CDATA in a simple element
        String xml = """
            <test-element><![CDATA[This is pure CDATA]]></test-element>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag(); // Move to START_ELEMENT of test-element

        // When: Call collectElementText directly via reflection
        Method method = BackParser.class.getDeclaredMethod("collectElementText", XMLStreamReader.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, reader, "test-element");

        // Then: CDATA content should be captured
        assertThat(result).isEqualTo("This is pure CDATA");
    }

    @Test
    @DisplayName("parseMixedCitation() - Test with attributes only, no text value (Line 459)")
    void testParseMixedCitation_ElementWithAttributesOnly() throws Exception {
        // Given: element-citation uses different parsing logic
        // Try using pub-id which might have different behavior
        String xml = """
            <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                <pub-id pub-id-type="pmid"></pub-id>
                <article-title>Title</article-title>
            </mixed-citation>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = BackParser.class.getDeclaredMethod("parseMixedCitation", XMLStreamReader.class);
        method.setAccessible(true);
        MixedCitation citation = (MixedCitation) method.invoke(null, reader);

        // Then
        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("Title");
    }

    // ==================== Helper Methods ====================

    private XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        factory.setProperty(XMLInputFactory.IS_COALESCING, false); // Ensure CDATA events are not merged
        factory.setProperty("javax.xml.stream.isReplacingEntityReferences", false); // Keep entities as-is
        return factory.createXMLStreamReader(new StringReader(xml));
    }
}
