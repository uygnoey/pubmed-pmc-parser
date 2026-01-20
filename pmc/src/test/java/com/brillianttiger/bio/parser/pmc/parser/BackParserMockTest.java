package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.MixedCitation;
import com.brillianttiger.bio.parser.pmc.model.Source;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;

import javax.xml.stream.XMLInputFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * BackParser의 마지막 2개 브랜치를 커버하기 위한 Mockito 기반 테스트
 *
 * Line 459: childText null check false branch
 * Line 499: CDATA event branch in collectElementText
 */
@DisplayName("BackParser Mock-based Edge Case Tests")
class BackParserMockTest {

    @Test
    @DisplayName("collectElementText() - Mocked CDATA event (Line 499 CDATA branch)")
    void testCollectElementText_MockedCdataEvent() throws Exception {
        // Given: Mock XMLStreamReader that returns CDATA event
        XMLStreamReader reader = mock(XMLStreamReader.class);

        // Sequence: START_ELEMENT → CHARACTERS → CDATA → END_ELEMENT
        when(reader.next())
                .thenReturn(XMLStreamConstants.CHARACTERS)  // First event: regular text
                .thenReturn(XMLStreamConstants.CDATA)       // Second event: CDATA
                .thenReturn(XMLStreamConstants.END_ELEMENT); // Third event: end

        when(reader.getText())
                .thenReturn("Regular text ")
                .thenReturn("CDATA content");

        // Mock depth tracking: END_ELEMENT at depth 0 breaks loop
        // Depth starts at 1 in collectElementText

        // When: Call collectElementText directly
        Method method = BackParser.class.getDeclaredMethod("collectElementText", XMLStreamReader.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, reader, "test-element");

        // Then: Both CHARACTERS and CDATA content should be captured
        assertThat(result).contains("Regular text");
        assertThat(result).contains("CDATA content");

        // Verify CDATA event was actually processed
        verify(reader, times(3)).next();
        verify(reader, times(2)).getText();
    }

    @Test
    @DisplayName("collectElementText() - Multiple CDATA events (Line 499 comprehensive)")
    void testCollectElementText_MultipleCdataEvents() throws Exception {
        // Given: Mock with multiple CDATA sections
        XMLStreamReader reader = mock(XMLStreamReader.class);

        when(reader.next())
                .thenReturn(XMLStreamConstants.CDATA)       // First CDATA
                .thenReturn(XMLStreamConstants.CHARACTERS)  // Text
                .thenReturn(XMLStreamConstants.CDATA)       // Second CDATA
                .thenReturn(XMLStreamConstants.END_ELEMENT);

        when(reader.getText())
                .thenReturn("CDATA1 ")
                .thenReturn("text ")
                .thenReturn("CDATA2");

        // When
        Method method = BackParser.class.getDeclaredMethod("collectElementText", XMLStreamReader.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, reader, "test");

        // Then
        assertThat(result).contains("CDATA1");
        assertThat(result).contains("CDATA2");
        assertThat(result).contains("text");
    }

    @Test
    @DisplayName("parseRef() - Mock parseSource to return null value (Line 459 false branch)")
    void testParseRef_WithMockedNullSourceValue() throws Exception {
        // Given: Use MockedStatic to mock CommonPmcElementParser.parseSource
        // Use public parseRef() method instead of reflection to avoid bytecode instrumentation bypass
        String xml = """
            <ref id="ref1">
                <mixed-citation publication-type="journal" xmlns:xlink="http://www.w3.org/1999/xlink">
                    <source>Test Source</source>
                    <year>2024</year>
                </mixed-citation>
            </ref>
            """;

        try (MockedStatic<CommonPmcElementParser> mockedStatic = mockStatic(CommonPmcElementParser.class, Mockito.CALLS_REAL_METHODS)) {
            // Mock parseSource to return Source with null value
            // Manually advance XMLStreamReader to skip the source element
            // This allows subsequent elements (like year) to be parsed normally
            mockedStatic.when(() -> CommonPmcElementParser.parseSource(any(XMLStreamReader.class)))
                    .thenAnswer(invocation -> {
                        XMLStreamReader reader = invocation.getArgument(0);

                        // Manually advance XMLStreamReader past the source element
                        while (reader.hasNext()) {
                            int event = reader.next();
                            if (event == XMLStreamConstants.END_ELEMENT &&
                                    reader.getLocalName().equals("source")) {
                                break;
                            }
                        }

                        // Return Source with null value - triggers Line 459 false branch
                        return Source.builder().value(null).build();
                    });

            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(xml));
            reader.nextTag(); // Move to <ref>

            // When: Call public method directly (no reflection)
            com.brillianttiger.bio.parser.pmc.model.Ref ref = BackParser.parseRef(reader);

            // Verify parseSource was called (to check if mock is working)
            mockedStatic.verify(() -> CommonPmcElementParser.parseSource(any(XMLStreamReader.class)));

            // Then: childText from source is null, Line 459 false branch triggered
            // The null value is NOT appended to textContent
            assertThat(ref).isNotNull();
            assertThat(ref.getMixedCitations()).isNotNull().hasSize(1);

            com.brillianttiger.bio.parser.pmc.model.MixedCitation citation = ref.getMixedCitations().get(0);
            assertThat(citation).isNotNull();
            assertThat(citation.getSource()).isNotNull();
            assertThat(citation.getSource().getValue()).isNull(); // Source value should be null
            assertThat(citation.getValue()).contains("2024"); // Year is there
            assertThat(citation.getValue()).doesNotContain("Test Source"); // Source not appended due to null value
        }
    }

    @Test
    @DisplayName("collectElementText() - CDATA with nested elements (comprehensive depth test)")
    void testCollectElementText_CdataWithNestedElements() throws Exception {
        // Given: Complex scenario with CDATA and nested elements
        XMLStreamReader reader = mock(XMLStreamReader.class);

        when(reader.next())
                .thenReturn(XMLStreamConstants.CHARACTERS)      // Text
                .thenReturn(XMLStreamConstants.START_ELEMENT)   // <nested>
                .thenReturn(XMLStreamConstants.CDATA)           // CDATA inside nested
                .thenReturn(XMLStreamConstants.END_ELEMENT)     // </nested>
                .thenReturn(XMLStreamConstants.CDATA)           // CDATA after nested
                .thenReturn(XMLStreamConstants.END_ELEMENT);    // </parent>

        when(reader.getText())
                .thenReturn("text1 ")
                .thenReturn("CDATA_nested ")
                .thenReturn("CDATA_after");

        // When
        Method method = BackParser.class.getDeclaredMethod("collectElementText", XMLStreamReader.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, reader, "parent");

        // Then: All content including CDATA should be captured
        assertThat(result).contains("text1");
        assertThat(result).contains("CDATA_nested");
        assertThat(result).contains("CDATA_after");
    }
}
