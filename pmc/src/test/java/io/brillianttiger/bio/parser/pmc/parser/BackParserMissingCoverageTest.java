package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BackParser 커버리지 향상 테스트
 * Missing coverage tests for BackParser
 */
class BackParserMissingCoverageTest {

    private XMLInputFactory xmlInputFactory;

    @BeforeEach
    void setUp() {
        xmlInputFactory = XMLInputFactory.newInstance();
    }

    private XMLStreamReader createReader(String xml) throws XMLStreamException {
        return xmlInputFactory.createXMLStreamReader(new StringReader(xml));
    }

    @Test
    @DisplayName("BackParser 생성자 테스트")
    void testBackParserConstructor() {
        BackParser parser = new BackParser();
        assertThat(parser).isNotNull();
    }

    // ==================== Simple Element Parsers (0% Coverage) ====================

    @Test
    @DisplayName("parseAppGroup() - 기본 테스트")
    void testParseAppGroup() throws Exception {
        String xml = "<app-group>Appendix A</app-group>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        AppGroup appGroup = BackParser.parseAppGroup(reader);

        assertThat(appGroup).isNotNull();
        assertThat(appGroup.getValue()).isEqualTo("Appendix A");
    }

    @Test
    @DisplayName("parseBio() - 기본 테스트")
    void testParseBio() throws Exception {
        String xml = "<bio>Author biography information</bio>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Bio bio = BackParser.parseBio(reader);

        assertThat(bio).isNotNull();
        assertThat(bio.getValue()).isEqualTo("Author biography information");
    }

    @Test
    @DisplayName("parseGlossary() - 기본 테스트")
    void testParseGlossary() throws Exception {
        String xml = "<glossary>Term definitions and glossary</glossary>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Glossary glossary = BackParser.parseGlossary(reader);

        assertThat(glossary).isNotNull();
        assertThat(glossary.getValue()).isEqualTo("Term definitions and glossary");
    }

    @Test
    @DisplayName("parseNotes() - 기본 테스트")
    void testParseNotes() throws Exception {
        String xml = "<notes>Additional notes</notes>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Notes notes = BackParser.parseNotes(reader);

        assertThat(notes).isNotNull();
        assertThat(notes.getValue()).isEqualTo("Additional notes");
    }

    @Test
    @DisplayName("parseSuffix() - 기본 테스트")
    void testParseSuffix() throws Exception {
        String xml = "<suffix>Jr.</suffix>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Suffix suffix = BackParser.parseSuffix(reader);

        assertThat(suffix).isNotNull();
        assertThat(suffix.getValue()).isEqualTo("Jr.");
    }

    // ==================== Back Element with Multiple Children ====================

    @Test
    @DisplayName("parseBack() - app-group 포함")
    void testParseBack_WithAppGroup() throws Exception {
        String xml = """
            <back>
                <app-group>Appendix content</app-group>
            </back>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Back back = BackParser.parseBack(reader);

        assertThat(back).isNotNull();
        assertThat(back.getAppGroups()).hasSize(1);
        assertThat(back.getAppGroups().get(0).getValue()).isEqualTo("Appendix content");
    }

    @Test
    @DisplayName("parseBack() - bio 포함")
    void testParseBack_WithBio() throws Exception {
        String xml = """
            <back>
                <bio>Biography of the author</bio>
            </back>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Back back = BackParser.parseBack(reader);

        assertThat(back).isNotNull();
        assertThat(back.getBiographies()).hasSize(1);
        assertThat(back.getBiographies().get(0).getValue()).isEqualTo("Biography of the author");
    }

    @Test
    @DisplayName("parseBack() - glossary 포함")
    void testParseBack_WithGlossary() throws Exception {
        String xml = """
            <back>
                <glossary>Glossary terms</glossary>
            </back>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Back back = BackParser.parseBack(reader);

        assertThat(back).isNotNull();
        assertThat(back.getGlossaries()).hasSize(1);
        assertThat(back.getGlossaries().get(0).getValue()).isEqualTo("Glossary terms");
    }

    @Test
    @DisplayName("parseBack() - notes 포함")
    void testParseBack_WithNotes() throws Exception {
        String xml = """
            <back>
                <notes>Additional notes section</notes>
            </back>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Back back = BackParser.parseBack(reader);

        assertThat(back).isNotNull();
        assertThat(back.getNotesList()).hasSize(1);
        assertThat(back.getNotesList().get(0).getValue()).isEqualTo("Additional notes section");
    }

    @Test
    @DisplayName("parseBack() - 복합 케이스 (app-group, bio, glossary, notes)")
    void testParseBack_WithMultipleElements() throws Exception {
        String xml = """
            <back>
                <app-group>Appendix</app-group>
                <bio>Author bio</bio>
                <glossary>Terms</glossary>
                <notes>Notes</notes>
            </back>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Back back = BackParser.parseBack(reader);

        assertThat(back).isNotNull();
        assertThat(back.getAppGroups()).hasSize(1);
        assertThat(back.getBiographies()).hasSize(1);
        assertThat(back.getGlossaries()).hasSize(1);
        assertThat(back.getNotesList()).hasSize(1);
    }

    // ==================== RefList Edge Cases ====================

    @Test
    @DisplayName("parseRefList() - title과 label 포함")
    void testParseRefList_WithTitleAndLabel() throws Exception {
        String xml = """
            <ref-list>
                <label>References</label>
                <title>Bibliography</title>
            </ref-list>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        RefList refList = BackParser.parseRefList(reader);

        assertThat(refList).isNotNull();
        assertThat(refList.getLabel()).isNotNull();
        assertThat(refList.getTitle()).isNotNull();
    }

    @Test
    @DisplayName("parseRefList() - 여러 ref 포함")
    void testParseRefList_WithMultipleRefs() throws Exception {
        String xml = """
            <ref-list>
                <ref id="ref1">
                    <label>1</label>
                </ref>
                <ref id="ref2">
                    <label>2</label>
                </ref>
            </ref-list>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        RefList refList = BackParser.parseRefList(reader);

        assertThat(refList).isNotNull();
        assertThat(refList.getReferences()).hasSize(2);
    }

    // ==================== Ref Edge Cases ====================

    @Test
    @DisplayName("parseRef() - element-citation 포함")
    void testParseRef_WithElementCitation() throws Exception {
        String xml = """
            <ref id="ref1">
                <element-citation publication-type="journal">
                    <article-title>Test Article</article-title>
                </element-citation>
            </ref>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Ref ref = BackParser.parseRef(reader);

        assertThat(ref).isNotNull();
        assertThat(ref.getId()).isEqualTo("ref1");
        assertThat(ref.getElementCitations()).isNotNull();
        assertThat(ref.getElementCitations()).hasSize(1);
    }

    @Test
    @DisplayName("parseRef() - mixed-citation 포함")
    void testParseRef_WithMixedCitation() throws Exception {
        String xml = """
            <ref id="ref2">
                <mixed-citation publication-type="book">
                    Test Citation
                </mixed-citation>
            </ref>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Ref ref = BackParser.parseRef(reader);

        assertThat(ref).isNotNull();
        assertThat(ref.getId()).isEqualTo("ref2");
        assertThat(ref.getMixedCitations()).isNotNull();
        assertThat(ref.getMixedCitations()).hasSize(1);
    }

    @Test
    @DisplayName("parseRef() - label과 모든 속성 포함")
    void testParseRef_WithLabelAndAllAttributes() throws Exception {
        String xml = """
            <ref id="ref3" content-type="supplementary" specific-use="test">
                <label>S1</label>
            </ref>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Ref ref = BackParser.parseRef(reader);

        assertThat(ref).isNotNull();
        assertThat(ref.getId()).isEqualTo("ref3");
        // contentType, specificUse는 현재 parseRef()에서 파싱하지 않음 (id만 파싱)
        assertThat(ref.getLabel()).isNotNull();
    }

    // ==================== ElementCitation Edge Cases ====================

    @Test
    @DisplayName("parseElementCitation() - person-group 포함")
    void testParseElementCitation_WithPersonGroup() throws Exception {
        String xml = """
            <element-citation publication-type="journal">
                <person-group person-group-type="author">
                    <name>
                        <surname>Smith</surname>
                        <given-names>John</given-names>
                    </name>
                </person-group>
            </element-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ElementCitation citation = BackParser.parseElementCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getPersonGroups()).hasSize(1);
        assertThat(citation.getPersonGroups().get(0).getNames()).hasSize(1);
    }

    @Test
    @DisplayName("parseElementCitation() - 다양한 요소 포함")
    void testParseElementCitation_WithVariousElements() throws Exception {
        String xml = """
            <element-citation publication-type="journal">
                <article-title>Article Title</article-title>
                <source>Journal Name</source>
                <year>2023</year>
                <volume>10</volume>
                <fpage>100</fpage>
                <lpage>110</lpage>
            </element-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ElementCitation citation = BackParser.parseElementCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getArticleTitle()).isNotNull();
        assertThat(citation.getSource()).isNotNull();
        assertThat(citation.getYear()).isNotNull();
        assertThat(citation.getVolume()).isNotNull();
        assertThat(citation.getFpage()).isNotNull();
        assertThat(citation.getLpage()).isNotNull();
    }

    @Test
    @DisplayName("parseElementCitation() - pub-id 포함")
    void testParseElementCitation_WithPubId() throws Exception {
        String xml = """
            <element-citation publication-type="journal">
                <pub-id pub-id-type="pmid">12345678</pub-id>
                <pub-id pub-id-type="doi">10.1000/xyz</pub-id>
            </element-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ElementCitation citation = BackParser.parseElementCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getPubIds()).hasSize(2);
    }

    // ==================== MixedCitation Edge Cases ====================

    @Test
    @DisplayName("parseMixedCitation() - person-group 포함")
    void testParseMixedCitation_WithPersonGroup() throws Exception {
        String xml = """
            <mixed-citation publication-type="book">
                <person-group person-group-type="author">
                    <name>
                        <surname>Doe</surname>
                        <given-names>Jane</given-names>
                    </name>
                </person-group>
                Mixed citation text content
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getPersonGroups()).hasSize(1);
        assertThat(citation.getValue()).contains("Mixed citation text content");
    }

    @Test
    @DisplayName("parseMixedCitation() - 다양한 inline 요소 포함")
    void testParseMixedCitation_WithInlineElements() throws Exception {
        String xml = """
            <mixed-citation publication-type="journal">
                <article-title>Title</article-title>
                <source>Source</source>
                <year>2023</year>
                <volume>5</volume>
                <fpage>10</fpage>
                <lpage>20</lpage>
                Some additional text
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getArticleTitle()).isNotNull();
        assertThat(citation.getSource()).isNotNull();
        assertThat(citation.getYear()).isNotNull();
        assertThat(citation.getVolume()).isNotNull();
        assertThat(citation.getFpage()).isNotNull();
        assertThat(citation.getLpage()).isNotNull();
    }

    @Test
    @DisplayName("parseMixedCitation() - pub-id 포함")
    void testParseMixedCitation_WithPubId() throws Exception {
        String xml = """
            <mixed-citation publication-type="journal">
                <pub-id pub-id-type="doi">10.1234/test</pub-id>
                Citation content
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getPubIds()).hasSize(1);
    }

    @Test
    @DisplayName("parseMixedCitation() - ext-link 포함")
    void testParseMixedCitation_WithExtLink() throws Exception {
        String xml = """
            <mixed-citation publication-type="web">
                <ext-link ext-link-type="uri">http://example.com</ext-link>
                Web citation
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getExtLinks()).hasSize(1);
    }

    // ==================== PersonGroup and PersonName Edge Cases ====================

    @Test
    @DisplayName("parsePersonGroup() - suffix 포함한 이름")
    void testParsePersonGroup_WithSuffix() throws Exception {
        String xml = """
            <person-group person-group-type="editor">
                <name>
                    <surname>Smith</surname>
                    <given-names>Robert</given-names>
                    <suffix>Jr.</suffix>
                </name>
            </person-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PersonGroup personGroup = BackParser.parsePersonGroup(reader);

        assertThat(personGroup).isNotNull();
        assertThat(personGroup.getPersonGroupType()).isEqualTo("editor");
        assertThat(personGroup.getNames()).hasSize(1);
        assertThat(personGroup.getNames().get(0).getSuffix()).isNotNull();
        assertThat(personGroup.getNames().get(0).getSuffix().getValue()).isEqualTo("Jr.");
    }

    @Test
    @DisplayName("parsePersonGroup() - collab 포함")
    void testParsePersonGroup_WithCollab() throws Exception {
        String xml = """
            <person-group person-group-type="author">
                <collab>Research Consortium</collab>
            </person-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PersonGroup personGroup = BackParser.parsePersonGroup(reader);

        assertThat(personGroup).isNotNull();
        assertThat(personGroup.getCollabs()).hasSize(1);
        assertThat(personGroup.getCollabs().get(0).getValue()).isEqualTo("Research Consortium");
    }

    @Test
    @DisplayName("parsePersonGroup() - etal 포함")
    void testParsePersonGroup_WithEtal() throws Exception {
        String xml = """
            <person-group person-group-type="author">
                <name>
                    <surname>First</surname>
                </name>
                <etal/>
            </person-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PersonGroup personGroup = BackParser.parsePersonGroup(reader);

        assertThat(personGroup).isNotNull();
        assertThat(personGroup.getNames()).hasSize(1);
        assertThat(personGroup.getEtals()).hasSize(1);
    }

    @Test
    @DisplayName("parsePersonName() - prefix와 suffix 모두 포함")
    void testParsePersonName_WithPrefixAndSuffix() throws Exception {
        String xml = """
            <name>
                <prefix>Dr.</prefix>
                <surname>Johnson</surname>
                <given-names>Emily</given-names>
                <suffix>PhD</suffix>
            </name>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PersonName personName = BackParser.parsePersonName(reader);

        assertThat(personName).isNotNull();
        assertThat(personName.getPrefix()).isNotNull();
        assertThat(personName.getPrefix().getValue()).isEqualTo("Dr.");
        assertThat(personName.getSurname()).isNotNull();
        assertThat(personName.getSurname().getValue()).isEqualTo("Johnson");
        assertThat(personName.getGivenNames()).isNotNull();
        assertThat(personName.getGivenNames().getValue()).isEqualTo("Emily");
        assertThat(personName.getSuffix()).isNotNull();
        assertThat(personName.getSuffix().getValue()).isEqualTo("PhD");
    }

    @Test
    @DisplayName("parsePersonName() - surname만 있는 경우")
    void testParsePersonName_SurnameOnly() throws Exception {
        String xml = """
            <name>
                <surname>SingleName</surname>
            </name>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PersonName personName = BackParser.parsePersonName(reader);

        assertThat(personName).isNotNull();
        assertThat(personName.getSurname()).isNotNull();
        assertThat(personName.getSurname().getValue()).isEqualTo("SingleName");
        assertThat(personName.getGivenNames()).isNull();
    }

    // ==================== FnGroup Edge Cases ====================

    @Test
    @DisplayName("parseFnGroup() - 여러 footnote 포함")
    void testParseFnGroup_WithMultipleFootnotes() throws Exception {
        String xml = """
            <fn-group>
                <fn id="fn1">
                    <p>First footnote</p>
                </fn>
                <fn id="fn2">
                    <p>Second footnote</p>
                </fn>
            </fn-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        FnGroup fnGroup = BackParser.parseFnGroup(reader);

        assertThat(fnGroup).isNotNull();
        assertThat(fnGroup.getFootnotes()).hasSize(2);
    }

    @Test
    @DisplayName("parseFnGroup() - 빈 footnote 그룹")
    void testParseFnGroup_Empty() throws Exception {
        String xml = "<fn-group></fn-group>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        FnGroup fnGroup = BackParser.parseFnGroup(reader);

        assertThat(fnGroup).isNotNull();
        assertThat(fnGroup.getFootnotes()).isNull();
    }

    // Note: collectElementText is a private method and is tested indirectly through
    // parseMixedCitation and other methods that use it

    // ==================== 100% Branch Coverage Tests ====================

    @Test
    @DisplayName("parseBack() - label, title, sec 포함 (누락 브랜치)")
    void testParseBack_WithLabelTitleSec() throws Exception {
        String xml = """
            <back>
                <label>Back Matter</label>
                <title>References and Notes</title>
                <title>Additional Information</title>
                <ref-list>
                    <ref id="ref1">
                        <label>1</label>
                        <element-citation publication-type="journal">
                            <article-title>Test Article</article-title>
                        </element-citation>
                    </ref>
                </ref-list>
                <sec>
                    <title>Supplementary Section</title>
                    <p>Additional content</p>
                </sec>
                <unknown-element>Should be skipped</unknown-element>
            </back>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Back back = BackParser.parseBack(reader);

        assertThat(back).isNotNull();
        assertThat(back.getLabel()).isNotNull();
        assertThat(back.getLabel().getValue()).isEqualTo("Back Matter");
        assertThat(back.getTitles()).isNotNull();
        assertThat(back.getTitles()).hasSize(2);
        assertThat(back.getTitles().get(0).getValue()).isEqualTo("References and Notes");
        assertThat(back.getTitles().get(1).getValue()).isEqualTo("Additional Information");
        assertThat(back.getRefLists()).hasSize(1);
        assertThat(back.getSections()).isNotNull();
        assertThat(back.getSections()).hasSize(1);
    }

    @Test
    @DisplayName("parseRefList() - 재귀 ref-list와 unknown 요소 (누락 브랜치)")
    void testParseRefList_WithNestedRefListAndUnknown() throws Exception {
        String xml = """
            <ref-list>
                <label>References</label>
                <title>Bibliography</title>
                <ref id="ref1">
                    <label>1</label>
                    <element-citation publication-type="journal">
                        <source>Journal Name</source>
                    </element-citation>
                </ref>
                <ref-list>
                    <label>Sub References</label>
                    <ref id="ref2">
                        <label>2</label>
                        <element-citation publication-type="book">
                            <source>Book Title</source>
                        </element-citation>
                    </ref>
                </ref-list>
                <unknown-element>Should be skipped</unknown-element>
            </ref-list>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        RefList refList = BackParser.parseRefList(reader);

        assertThat(refList).isNotNull();
        assertThat(refList.getLabel()).isNotNull();
        assertThat(refList.getTitle()).isNotNull();
        assertThat(refList.getReferences()).hasSize(1);
        assertThat(refList.getRefLists()).isNotNull(); // 재귀 ref-list
        assertThat(refList.getRefLists()).hasSize(1);
    }

    @Test
    @DisplayName("parseRef() - unknown 요소 포함 (누락 브랜치)")
    void testParseRef_WithUnknownElement() throws Exception {
        String xml = """
            <ref id="ref1">
                <label>1</label>
                <element-citation publication-type="journal">
                    <article-title>Test</article-title>
                </element-citation>
                <unknown-element>Should be skipped</unknown-element>
            </ref>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Ref ref = BackParser.parseRef(reader);

        assertThat(ref).isNotNull();
        assertThat(ref.getId()).isEqualTo("ref1");
        assertThat(ref.getLabel()).isNotNull();
        assertThat(ref.getElementCitations()).hasSize(1);
    }

    @Test
    @DisplayName("parseElementCitation() - month, day, page-range, elocation-id, article-id, comment 포함 (누락 브랜치)")
    void testParseElementCitation_AllMissingElements() throws Exception {
        String xml = """
            <element-citation publication-type="journal">
                <person-group person-group-type="author">
                    <name>
                        <surname>Smith</surname>
                        <given-names>John</given-names>
                    </name>
                </person-group>
                <article-title>Complete Citation Test</article-title>
                <source>Journal Name</source>
                <year>2024</year>
                <month>01</month>
                <day>15</day>
                <volume>10</volume>
                <issue>2</issue>
                <fpage>100</fpage>
                <lpage>110</lpage>
                <page-range>100-110</page-range>
                <elocation-id>e123456</elocation-id>
                <article-id pub-id-type="doi">10.1234/test</article-id>
                <pub-id pub-id-type="pmid">12345678</pub-id>
                <comment>This is a comment</comment>
                <ext-link ext-link-type="uri">http://example.com</ext-link>
            </element-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ElementCitation citation = BackParser.parseElementCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getPublicationType()).isEqualTo("journal");
        assertThat(citation.getMonth()).isNotNull();
        assertThat(citation.getMonth().getValue()).isEqualTo("01");
        assertThat(citation.getDay()).isNotNull();
        assertThat(citation.getDay().getValue()).isEqualTo("15");
        assertThat(citation.getPageRange()).isNotNull();
        assertThat(citation.getPageRange().getValue()).isEqualTo("100-110");
        assertThat(citation.getElocationId()).isNotNull();
        assertThat(citation.getElocationId().getValue()).isEqualTo("e123456");
        assertThat(citation.getArticleIds()).isNotNull(); // article-id 브랜치
        assertThat(citation.getArticleIds()).hasSize(1);
        assertThat(citation.getComments()).isNotNull(); // comment 브랜치
        assertThat(citation.getComments()).hasSize(1);
    }

    @Test
    @DisplayName("parseMixedCitation() - page-range와 comment 포함 (누락 브랜치)")
    void testParseMixedCitation_WithPageRangeAndComment() throws Exception {
        String xml = """
            <mixed-citation publication-type="journal">
                Smith J, Doe A. Test Article.
                <source>Journal</source>.
                <year>2024</year>;
                <volume>10</volume>(<issue>2</issue>):
                <page-range>100-110</page-range>.
                <comment>Published online ahead of print</comment>
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getPageRange()).isNotNull();
        assertThat(citation.getPageRange().getValue()).isEqualTo("100-110");
        assertThat(citation.getComments()).isNotNull();
        assertThat(citation.getComments()).hasSize(1);
        assertThat(citation.getValue()).contains("Smith J, Doe A");
    }

    @Test
    @DisplayName("parseMixedCitation() - null 값 반환 테스트 (null 체크 브랜치)")
    void testParseMixedCitation_WithEmptyElements() throws Exception {
        String xml = """
            <mixed-citation publication-type="journal">
                <article-title></article-title>
                <source></source>
                <year></year>
                <month></month>
                <day></day>
                <volume></volume>
                <issue></issue>
                <fpage></fpage>
                <lpage></lpage>
                <publisher-name></publisher-name>
                <publisher-loc></publisher-loc>
                <edition></edition>
                <conf-name></conf-name>
                <pub-id pub-id-type="pmid"></pub-id>
                <ext-link ext-link-type="uri"></ext-link>
                <string-name></string-name>
                <etal></etal>
                <collab></collab>
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        // 빈 요소들도 파싱되어야 함 (null 체크 브랜치 커버)
    }

    @Test
    @DisplayName("parseMixedCitation() - 알려지지 않은 요소들 (default case)")
    void testParseMixedCitation_WithUnknownElements() throws Exception {
        String xml = """
            <mixed-citation publication-type="journal">
                <date-in-citation>2024-01-15</date-in-citation>
                <isbn>978-1-234567-89-0</isbn>
                <page-count count="250"/>
                <conf-date>2024-03-20</conf-date>
                <conf-loc>New York</conf-loc>
                <unknown-element>Some content</unknown-element>
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        // 알려지지 않은 요소의 텍스트도 수집되어야 함
        assertThat(citation.getValue()).isNotEmpty();
    }

    @Test
    @DisplayName("collectTextAndParsePersonGroup() - 다양한 시나리오 (누락 브랜치)")
    void testCollectTextAndParsePersonGroup_EdgeCases() throws Exception {
        String xml = """
            <mixed-citation>
                <person-group person-group-type="author">
                    <name>
                        <surname>Smith</surname>
                        <given-names>John</given-names>
                    </name>
                    <name>
                        <surname>OnlyLastName</surname>
                    </name>
                    <name>
                        <given-names>OnlyFirstName</given-names>
                    </name>
                    Some text content
                </person-group>
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getPersonGroups()).hasSize(1);
        assertThat(citation.getPersonGroups().get(0).getNames()).hasSize(3);
        // 텍스트 수집 테스트
        assertThat(citation.getValue()).isNotEmpty();
    }

    @Test
    @DisplayName("parsePersonGroup() - unknown 요소 포함 (누락 브랜치)")
    void testParsePersonGroup_WithUnknownElement() throws Exception {
        String xml = """
            <person-group person-group-type="author">
                <name>
                    <surname>Smith</surname>
                </name>
                <unknown-element>Should be skipped</unknown-element>
            </person-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PersonGroup personGroup = BackParser.parsePersonGroup(reader);

        assertThat(personGroup).isNotNull();
        assertThat(personGroup.getPersonGroupType()).isEqualTo("author");
        assertThat(personGroup.getNames()).hasSize(1);
    }

    @Test
    @DisplayName("parsePersonName() - unknown 요소 포함 (누락 브랜치)")
    void testParsePersonName_WithUnknownElement() throws Exception {
        String xml = """
            <name>
                <surname>Johnson</surname>
                <given-names>Emily</given-names>
                <unknown-element>Should be skipped</unknown-element>
            </name>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PersonName personName = BackParser.parsePersonName(reader);

        assertThat(personName).isNotNull();
        assertThat(personName.getSurname()).isNotNull();
        assertThat(personName.getGivenNames()).isNotNull();
    }

    @Test
    @DisplayName("parseFnGroup() - fn 아닌 요소 포함 (누락 브랜치)")
    void testParseFnGroup_WithNonFnElement() throws Exception {
        String xml = """
            <fn-group>
                <fn id="fn1">
                    <p>First footnote</p>
                </fn>
                <unknown-element>Should be skipped</unknown-element>
                <fn id="fn2">
                    <p>Second footnote</p>
                </fn>
            </fn-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        FnGroup fnGroup = BackParser.parseFnGroup(reader);

        assertThat(fnGroup).isNotNull();
        assertThat(fnGroup.getFootnotes()).hasSize(2);
    }

    @Test
    @DisplayName("collectElementText() - 중첩 요소와 CDATA 포함 (private 메서드 간접 테스트)")
    void testCollectElementText_NestedAndCDATA() throws Exception {
        String xml = """
            <mixed-citation publication-type="journal">
                <conf-loc>
                    <nested>New York</nested>
                    <![CDATA[Some CDATA content]]>
                </conf-loc>
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        // collectElementText가 중첩 요소와 CDATA를 처리해야 함
        assertThat(citation.getValue()).isNotEmpty();
    }

    @Test
    @DisplayName("parseMixedCitation() - childText null 브랜치 테스트")
    void testParseMixedCitation_NullChildText() throws Exception {
        String xml = """
            <mixed-citation publication-type="journal">
                Text before
                <person-group person-group-type="author">
                    <name>
                        <surname>Smith</surname>
                    </name>
                </person-group>
                Text after
            </mixed-citation>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        MixedCitation citation = BackParser.parseMixedCitation(reader);

        assertThat(citation).isNotNull();
        assertThat(citation.getValue()).contains("Text before");
        assertThat(citation.getValue()).contains("Text after");
        assertThat(citation.getPersonGroups()).hasSize(1);
    }
}
