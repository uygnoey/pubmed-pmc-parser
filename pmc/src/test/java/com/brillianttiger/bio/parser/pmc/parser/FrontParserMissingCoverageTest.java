package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * FrontParserMissingCoverageTest
 *
 * KR: FrontParser의 누락된 커버리지를 향상시키기 위한 테스트
 * EN: Tests to improve missing coverage in FrontParser
 */
@DisplayName("FrontParser Missing Coverage Tests")
class FrontParserMissingCoverageTest {

    private final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

    @Test
    @DisplayName("FrontParser 생성자 테스트")
    void testFrontParserConstructor() {
        // Given & When
        FrontParser parser = new FrontParser();

        // Then
        assertThat(parser).isNotNull();
    }

    @Test
    @DisplayName("parsePmcPublisher() - publisher-name만 있는 경우")
    void testParsePmcPublisher_WithPublisherNameOnly() throws Exception {
        // Given
        String xml = """
            <publisher>
                <publisher-name>Test Publisher</publisher-name>
            </publisher>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        PmcPublisher publisher = FrontParser.parsePmcPublisher(reader);

        // Then
        assertThat(publisher).isNotNull();
        assertThat(publisher.getPublisherName()).isNotNull();
        assertThat(publisher.getPublisherName().getValue()).isEqualTo("Test Publisher");
        assertThat(publisher.getPublisherLoc()).isNull();
    }

    @Test
    @DisplayName("parsePmcPublisher() - publisher-name과 publisher-loc 모두 있는 경우")
    void testParsePmcPublisher_WithPublisherNameAndLoc() throws Exception {
        // Given
        String xml = """
            <publisher>
                <publisher-name>Test Publisher</publisher-name>
                <publisher-loc>New York, USA</publisher-loc>
            </publisher>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        PmcPublisher publisher = FrontParser.parsePmcPublisher(reader);

        // Then
        assertThat(publisher).isNotNull();
        assertThat(publisher.getPublisherName()).isNotNull();
        assertThat(publisher.getPublisherName().getValue()).isEqualTo("Test Publisher");
        assertThat(publisher.getPublisherLoc()).isNotNull();
        assertThat(publisher.getPublisherLoc().getValue()).isEqualTo("New York, USA");
    }

    @Test
    @DisplayName("parsePmcIssn() - pub-type과 content-type 속성 포함")
    void testParsePmcIssn_WithAttributes() throws Exception {
        // Given
        String xml = """
            <issn pub-type="ppub" content-type="print">1234-5678</issn>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        PmcIssn issn = FrontParser.parsePmcIssn(reader);

        // Then
        assertThat(issn).isNotNull();
        assertThat(issn.getValue()).isEqualTo("1234-5678");
        assertThat(issn.getPubType()).isEqualTo("ppub");
        assertThat(issn.getContentType()).isEqualTo("print");
    }

    @Test
    @DisplayName("parsePmcIssn() - 속성 없이 값만 있는 경우")
    void testParsePmcIssn_WithoutAttributes() throws Exception {
        // Given
        String xml = """
            <issn>1234-5678</issn>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        PmcIssn issn = FrontParser.parsePmcIssn(reader);

        // Then
        assertThat(issn).isNotNull();
        assertThat(issn.getValue()).isEqualTo("1234-5678");
        assertThat(issn.getPubType()).isNull();
        assertThat(issn.getContentType()).isNull();
    }

    @Test
    @DisplayName("parseNotes() - 텍스트 값 파싱")
    void testParseNotes() throws Exception {
        // Given
        String xml = """
            <notes>This is a test note.</notes>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Notes notes = FrontParser.parseNotes(reader);

        // Then
        assertThat(notes).isNotNull();
        assertThat(notes.getValue()).isEqualTo("This is a test note.");
    }

    @Test
    @DisplayName("parseJournalSubtitle() - 텍스트 값 파싱")
    void testParseJournalSubtitle() throws Exception {
        // Given
        String xml = """
            <journal-subtitle>Test Journal Subtitle</journal-subtitle>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalSubtitle subtitle = FrontParser.parseJournalSubtitle(reader);

        // Then
        assertThat(subtitle).isNotNull();
        assertThat(subtitle.getValue()).isEqualTo("Test Journal Subtitle");
    }

    @Test
    @DisplayName("parseTransTitleGroup() - ArticleMetaParser에 위임")
    void testParseTransTitleGroup() throws Exception {
        // Given
        String xml = """
            <trans-title-group xml:lang="ko">
                <trans-title>번역된 제목</trans-title>
            </trans-title-group>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        TransTitleGroup transTitleGroup = FrontParser.parseTransTitleGroup(reader);

        // Then
        assertThat(transTitleGroup).isNotNull();
        assertThat(transTitleGroup.getXmlLang()).isEqualTo("ko");
        assertThat(transTitleGroup.getTransTitle()).isNotNull();
        assertThat(transTitleGroup.getTransTitle().getContent()).isEqualTo("번역된 제목");
    }

    @Test
    @DisplayName("parsePmcPublisher() - 알 수 없는 요소 무시")
    void testParsePmcPublisher_WithUnknownElement() throws Exception {
        // Given
        String xml = """
            <publisher>
                <publisher-name>Test Publisher</publisher-name>
                <unknown-element>Should be skipped</unknown-element>
                <publisher-loc>Test Location</publisher-loc>
            </publisher>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        PmcPublisher publisher = FrontParser.parsePmcPublisher(reader);

        // Then
        assertThat(publisher).isNotNull();
        assertThat(publisher.getPublisherName().getValue()).isEqualTo("Test Publisher");
        assertThat(publisher.getPublisherLoc().getValue()).isEqualTo("Test Location");
    }

    @Test
    @DisplayName("parseFront() - journal-meta와 article-meta 포함")
    void testParseFront() throws Exception {
        // Given
        String xml = """
            <front>
                <journal-meta>
                    <journal-id journal-id-type="nlm-ta">Test Journal</journal-id>
                </journal-meta>
                <article-meta>
                    <article-id pub-id-type="pmid">12345678</article-id>
                </article-meta>
            </front>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Front front = FrontParser.parseFront(reader);

        // Then
        assertThat(front).isNotNull();
        assertThat(front.getJournalMeta()).isNotNull();
        assertThat(front.getArticleMeta()).isNotNull();
    }

    @Test
    @DisplayName("parseFront() - notes 포함")
    void testParseFront_WithNotes() throws Exception {
        // Given
        String xml = """
            <front>
                <article-meta>
                    <article-id pub-id-type="pmid">12345678</article-id>
                </article-meta>
                <notes>First note</notes>
                <notes>Second note</notes>
            </front>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Front front = FrontParser.parseFront(reader);

        // Then
        assertThat(front).isNotNull();
        assertThat(front.getNotesList()).hasSize(2);
        assertThat(front.getNotesList().get(0).getValue()).isEqualTo("First note");
        assertThat(front.getNotesList().get(1).getValue()).isEqualTo("Second note");
    }

    @Test
    @DisplayName("parseJournalMeta() - 전체 요소")
    void testParseJournalMeta_Complete() throws Exception {
        // Given
        String xml = """
            <journal-meta>
                <journal-id journal-id-type="nlm-ta">Test J</journal-id>
                <journal-id journal-id-type="iso-abbrev">Test Journal</journal-id>
                <journal-title-group>
                    <journal-title>Test Journal</journal-title>
                </journal-title-group>
                <issn pub-type="ppub">1234-5678</issn>
                <issn pub-type="epub">1234-5679</issn>
                <publisher>
                    <publisher-name>Test Publisher</publisher-name>
                </publisher>
                <notes>Journal note</notes>
            </journal-meta>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalMeta journalMeta = FrontParser.parseJournalMeta(reader);

        // Then
        assertThat(journalMeta).isNotNull();
        assertThat(journalMeta.getJournalIds()).hasSize(2);
        assertThat(journalMeta.getJournalTitleGroups()).hasSize(1);
        assertThat(journalMeta.getIssns()).hasSize(2);
        assertThat(journalMeta.getPublisher()).isNotNull();
        assertThat(journalMeta.getNotesList()).hasSize(1);
    }

    @Test
    @DisplayName("parseJournalTitleGroup() - 전체 요소")
    void testParseJournalTitleGroup_Complete() throws Exception {
        // Given
        String xml = """
            <journal-title-group>
                <journal-title>Main Title</journal-title>
                <journal-subtitle>Subtitle</journal-subtitle>
                <trans-title-group xml:lang="ko">
                    <trans-title>번역 제목</trans-title>
                </trans-title-group>
                <abbrev-journal-title>Abbrev</abbrev-journal-title>
            </journal-title-group>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalTitleGroup titleGroup = FrontParser.parseJournalTitleGroup(reader);

        // Then
        assertThat(titleGroup).isNotNull();
        assertThat(titleGroup.getJournalTitles()).hasSize(1);
        assertThat(titleGroup.getJournalSubtitles()).hasSize(1);
        assertThat(titleGroup.getTransTitleGroups()).hasSize(1);
        assertThat(titleGroup.getAbbrevJournalTitles()).hasSize(1);
    }

    @Test
    @DisplayName("parseIssn() - 전체 속성")
    void testParseIssn_WithAllAttributes() throws Exception {
        // Given
        String xml = """
            <issn content-type="print" publication-format="print" pub-type="ppub">1234-5678</issn>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Issn issn = FrontParser.parseIssn(reader);

        // Then
        assertThat(issn).isNotNull();
        assertThat(issn.getValue()).isEqualTo("1234-5678");
        assertThat(issn.getContentType()).isEqualTo("print");
        assertThat(issn.getPublicationFormat()).isEqualTo(PublicationFormat.PRINT);
        assertThat(issn.getPubType()).isEqualTo(PubType.PPUB);
    }

    @Test
    @DisplayName("parseJournalId() - 전체 속성")
    void testParseJournalId_WithAllAttributes() throws Exception {
        // Given
        String xml = """
            <journal-id journal-id-type="nlm-ta" specific-use="primary">Test J</journal-id>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalId journalId = FrontParser.parseJournalId(reader);

        // Then
        assertThat(journalId).isNotNull();
        assertThat(journalId.getValue()).isEqualTo("Test J");
        assertThat(journalId.getJournalIdType()).isEqualTo(JournalIdType.NLM_TA);
        assertThat(journalId.getSpecificUse()).isEqualTo("primary");
    }

    @Test
    @DisplayName("parsePublisher() - publisher-name과 publisher-loc 여러 개")
    void testParsePublisher_WithMultipleElements() throws Exception {
        // Given
        String xml = """
            <publisher>
                <publisher-name>Primary Publisher</publisher-name>
                <publisher-name>Secondary Publisher</publisher-name>
                <publisher-loc>New York</publisher-loc>
                <publisher-loc>London</publisher-loc>
            </publisher>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Publisher publisher = FrontParser.parsePublisher(reader);

        // Then
        assertThat(publisher).isNotNull();
        assertThat(publisher.getPublisherNames()).hasSize(2);
        assertThat(publisher.getPublisherLocs()).hasSize(2);
    }

    @Test
    @DisplayName("parseJournalTitle() - 간단한 텍스트")
    void testParseJournalTitle() throws Exception {
        // Given
        String xml = """
            <journal-title>Test Journal Title</journal-title>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalTitle journalTitle = FrontParser.parseJournalTitle(reader);

        // Then
        assertThat(journalTitle).isNotNull();
        assertThat(journalTitle.getValue()).isEqualTo("Test Journal Title");
    }

    @Test
    @DisplayName("parseAbbrevJournalTitle() - 간단한 텍스트")
    void testParseAbbrevJournalTitle() throws Exception {
        // Given
        String xml = """
            <abbrev-journal-title>Test J</abbrev-journal-title>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        AbbrevJournalTitle abbrevTitle = FrontParser.parseAbbrevJournalTitle(reader);

        // Then
        assertThat(abbrevTitle).isNotNull();
        assertThat(abbrevTitle.getValue()).isEqualTo("Test J");
    }

    @Test
    @DisplayName("parseJournalMeta() - 최소 요소만 (journal-id만)")
    void testParseJournalMeta_MinimalWithOnlyJournalId() throws Exception {
        // Given
        String xml = """
            <journal-meta>
                <journal-id journal-id-type="nlm-ta">Test J</journal-id>
            </journal-meta>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalMeta journalMeta = FrontParser.parseJournalMeta(reader);

        // Then
        assertThat(journalMeta).isNotNull();
        assertThat(journalMeta.getJournalIds()).hasSize(1);
        assertThat(journalMeta.getJournalTitleGroups()).isNull();
        assertThat(journalMeta.getIssns()).isNull();
        assertThat(journalMeta.getIsbns()).isNull();
        assertThat(journalMeta.getPublisher()).isNull();
        assertThat(journalMeta.getNotesList()).isNull();
    }

    @Test
    @DisplayName("parseJournalMeta() - 알 수 없는 요소 무시")
    void testParseJournalMeta_WithUnknownElement() throws Exception {
        // Given
        String xml = """
            <journal-meta>
                <journal-id journal-id-type="nlm-ta">Test J</journal-id>
                <unknown-element>Should be skipped</unknown-element>
            </journal-meta>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalMeta journalMeta = FrontParser.parseJournalMeta(reader);

        // Then
        assertThat(journalMeta).isNotNull();
        assertThat(journalMeta.getJournalIds()).hasSize(1);
    }

    @Test
    @DisplayName("parseJournalTitleGroup() - 최소 요소 (비어있음)")
    void testParseJournalTitleGroup_Empty() throws Exception {
        // Given
        String xml = """
            <journal-title-group>
            </journal-title-group>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalTitleGroup titleGroup = FrontParser.parseJournalTitleGroup(reader);

        // Then
        assertThat(titleGroup).isNotNull();
        assertThat(titleGroup.getJournalTitles()).isNull();
        assertThat(titleGroup.getJournalSubtitles()).isNull();
        assertThat(titleGroup.getTransTitleGroups()).isNull();
        assertThat(titleGroup.getAbbrevJournalTitles()).isNull();
    }

    @Test
    @DisplayName("parseJournalTitleGroup() - 알 수 없는 요소 무시")
    void testParseJournalTitleGroup_WithUnknownElement() throws Exception {
        // Given
        String xml = """
            <journal-title-group>
                <journal-title>Main Title</journal-title>
                <unknown-element>Should be skipped</unknown-element>
            </journal-title-group>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalTitleGroup titleGroup = FrontParser.parseJournalTitleGroup(reader);

        // Then
        assertThat(titleGroup).isNotNull();
        assertThat(titleGroup.getJournalTitles()).hasSize(1);
    }

    @Test
    @DisplayName("parsePublisher() - 최소 요소 (publisher-name 1개만)")
    void testParsePublisher_MinimalWithOnlyName() throws Exception {
        // Given
        String xml = """
            <publisher>
                <publisher-name>Test Publisher</publisher-name>
            </publisher>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Publisher publisher = FrontParser.parsePublisher(reader);

        // Then
        assertThat(publisher).isNotNull();
        assertThat(publisher.getPublisherNames()).hasSize(1);
        assertThat(publisher.getPublisherLocs()).isNull();
    }

    @Test
    @DisplayName("parsePublisher() - 알 수 없는 요소 무시")
    void testParsePublisher_WithUnknownElement() throws Exception {
        // Given
        String xml = """
            <publisher>
                <publisher-name>Test Publisher</publisher-name>
                <unknown-element>Should be skipped</unknown-element>
            </publisher>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Publisher publisher = FrontParser.parsePublisher(reader);

        // Then
        assertThat(publisher).isNotNull();
        assertThat(publisher.getPublisherNames()).hasSize(1);
    }

    @Test
    @DisplayName("parseFront() - 최소 요소 (article-meta만)")
    void testParseFront_MinimalWithOnlyArticleMeta() throws Exception {
        // Given
        String xml = """
            <front>
                <article-meta>
                    <article-id pub-id-type="pmid">12345678</article-id>
                </article-meta>
            </front>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Front front = FrontParser.parseFront(reader);

        // Then
        assertThat(front).isNotNull();
        assertThat(front.getJournalMeta()).isNull();
        assertThat(front.getArticleMeta()).isNotNull();
        assertThat(front.getNotesList()).isNull();
    }

    @Test
    @DisplayName("parseFront() - 알 수 없는 요소 무시")
    void testParseFront_WithUnknownElement() throws Exception {
        // Given
        String xml = """
            <front>
                <article-meta>
                    <article-id pub-id-type="pmid">12345678</article-id>
                </article-meta>
                <unknown-element>Should be skipped</unknown-element>
            </front>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Front front = FrontParser.parseFront(reader);

        // Then
        assertThat(front).isNotNull();
        assertThat(front.getArticleMeta()).isNotNull();
    }

    @Test
    @DisplayName("parseJournalMeta() - isbn element가 있는 경우 (Line 91-92 커버)")
    void testParseJournalMeta_WithIsbn() throws Exception {
        // Given: journal-meta with isbn element
        String xml = """
            <journal-meta>
                <journal-id journal-id-type="nlm-ta">Test Journal</journal-id>
                <isbn>978-3-16-148410-0</isbn>
                <issn pub-type="ppub">1234-5678</issn>
            </journal-meta>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalMeta journalMeta = FrontParser.parseJournalMeta(reader);

        // Then: isbn should be parsed and added to isbns list
        assertThat(journalMeta).isNotNull();
        assertThat(journalMeta.getIsbns()).isNotNull().hasSize(1);
        assertThat(journalMeta.getIsbns().get(0).getValue()).isEqualTo("978-3-16-148410-0");
    }

    @Test
    @DisplayName("parsePublisher() - publisher-name 없음 (Line 257 true branch)")
    void testParsePublisher_EmptyPublisherNames() throws Exception {
        // Given: publisher without publisher-name (only publisher-loc)
        String xml = """
            <publisher>
                <publisher-loc>New York, NY</publisher-loc>
            </publisher>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        Publisher publisher = FrontParser.parsePublisher(reader);

        // Then: publisherNames should be null (isEmpty() returns true)
        assertThat(publisher).isNotNull();
        assertThat(publisher.getPublisherNames()).isNull();  // isEmpty() true branch
        assertThat(publisher.getPublisherLocs()).isNotNull().hasSize(1);
        assertThat(publisher.getPublisherLocs().get(0).getValue()).isEqualTo("New York, NY");
    }

    @Test
    @DisplayName("parseJournalMeta() - isbn 없음 명시적 테스트 (Line 112 true branch)")
    void testParseJournalMeta_NoIsbn_ExplicitTest() throws Exception {
        // Given: journal-meta without any isbn elements
        String xml = """
            <journal-meta>
                <journal-id journal-id-type="nlm-ta">Test J</journal-id>
                <issn pub-type="ppub">1234-5678</issn>
            </journal-meta>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT

        // When
        JournalMeta journalMeta = FrontParser.parseJournalMeta(reader);

        // Then: isbns should be null because isbns.isEmpty() is true
        assertThat(journalMeta).isNotNull();
        assertThat(journalMeta.getIsbns()).isNull();  // Line 112 true branch: isbns.isEmpty() ? null
        assertThat(journalMeta.getIssns()).isNotNull().hasSize(1);
    }

    /**
     * XML 문자열에서 XMLStreamReader 생성
     * Create XMLStreamReader from XML string
     */
    private XMLStreamReader createReader(String xml) throws XMLStreamException {
        return xmlInputFactory.createXMLStreamReader(new StringReader(xml));
    }
}
