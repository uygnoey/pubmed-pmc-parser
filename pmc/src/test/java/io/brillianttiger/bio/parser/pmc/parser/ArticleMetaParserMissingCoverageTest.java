package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ArticleMetaParser의 누락된 커버리지를 테스트
 *
 * KR: 0% 커버리지 메서드와 부분 커버리지 메서드를 테스트하여 커버리지 향상
 * EN: Test uncovered and partially covered methods to improve coverage
 */
class ArticleMetaParserMissingCoverageTest {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

    private XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        factory.setProperty(XMLInputFactory.IS_COALESCING, false); // Don't merge CDATA with CHARACTERS
        return factory.createXMLStreamReader(new StringReader(xml));
    }

    // ==================== 0% Coverage Public Methods ====================

    @Test
    @DisplayName("parseFnGroup() - footnote 그룹 파싱")
    void testParseFnGroup() throws Exception {
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

        FnGroup fnGroup = ArticleMetaParser.parseFnGroup(reader);

        assertThat(fnGroup).isNotNull();
        assertThat(fnGroup.getFootnotes()).hasSize(2);
        assertThat(fnGroup.getFootnotes().get(0).getId()).isEqualTo("fn1");
        assertThat(fnGroup.getFootnotes().get(1).getId()).isEqualTo("fn2");
    }

    @Test
    @DisplayName("parseDispFormulaGroup() - 수식 그룹 파싱")
    void testParseDispFormulaGroup() throws Exception {
        String xml = """
            <disp-formula-group>
                <disp-formula id="eq1">
                    <tex-math>x^2 + y^2 = z^2</tex-math>
                </disp-formula>
            </disp-formula-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        DispFormulaGroup group = ArticleMetaParser.parseDispFormulaGroup(reader);

        assertThat(group).isNotNull();
        assertThat(group.getDispFormulas()).hasSize(1);
        assertThat(group.getDispFormulas().get(0).getId()).isEqualTo("eq1");
    }

    @Test
    @DisplayName("parseAffAlternatives() - affiliation 대안 파싱")
    void testParseAffAlternatives() throws Exception {
        String xml = """
            <aff-alternatives>
                <aff id="aff1">Department of Biology</aff>
                <aff id="aff2" xml:lang="ko">생물학과</aff>
            </aff-alternatives>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        AffAlternatives affAlternatives = ArticleMetaParser.parseAffAlternatives(reader);

        assertThat(affAlternatives).isNotNull();
        assertThat(affAlternatives.getAffiliations()).hasSize(2);
        assertThat(affAlternatives.getAffiliations().get(0).getId()).isEqualTo("aff1");
        assertThat(affAlternatives.getAffiliations().get(1).getId()).isEqualTo("aff2");
    }

    @Test
    @DisplayName("parseTextualForm() - 수식의 텍스트 형식 파싱")
    void testParseTextualForm() throws Exception {
        String xml = "<textual-form>x squared plus y squared equals z squared</textual-form>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        TextualForm textualForm = ArticleMetaParser.parseTextualForm(reader);

        assertThat(textualForm).isNotNull();
        assertThat(textualForm.getContent()).isEqualTo("x squared plus y squared equals z squared");
    }

    @Test
    @DisplayName("parseSpeech() - 발언 파싱")
    void testParseSpeech() throws Exception {
        String xml = """
            <speech>
                <speaker>Dr. Smith</speaker>
                <p>Thank you for the introduction.</p>
            </speech>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Speech speech = ArticleMetaParser.parseSpeech(reader);

        assertThat(speech).isNotNull();
    }

    @Test
    @DisplayName("parseStatement() - statement 파싱")
    void testParseStatement() throws Exception {
        String xml = """
            <statement>
                <label>Theorem 1</label>
                <p>Every prime number greater than 2 is odd.</p>
            </statement>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Statement statement = ArticleMetaParser.parseStatement(reader);

        assertThat(statement).isNotNull();
    }

    @Test
    @DisplayName("parseVerseGroup() - 시(verse) 그룹 파싱")
    void testParseVerseGroup() throws Exception {
        String xml = """
            <verse-group>
                <verse-line>Roses are red</verse-line>
                <verse-line>Violets are blue</verse-line>
            </verse-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        VerseGroup verseGroup = ArticleMetaParser.parseVerseGroup(reader);

        assertThat(verseGroup).isNotNull();
    }

    @Test
    @DisplayName("parsePubDateNotAvailable() - 출판일 없음 파싱")
    void testParsePubDateNotAvailable() throws Exception {
        String xml = "<pub-date-not-available/>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PubDateNotAvailable pubDateNotAvailable = ArticleMetaParser.parsePubDateNotAvailable(reader);

        assertThat(pubDateNotAvailable).isNotNull();
    }

    @Test
    @DisplayName("parseTransSubtitle() - 번역된 부제목 파싱")
    void testParseTransSubtitle() throws Exception {
        String xml = "<trans-subtitle xml:lang=\"ko\">번역된 부제목</trans-subtitle>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        TransSubtitle transSubtitle = ArticleMetaParser.parseTransSubtitle(reader);

        assertThat(transSubtitle).isNotNull();
    }

    @Test
    @DisplayName("parseAnonymous() - 익명 저자 파싱")
    void testParseAnonymous() throws Exception {
        String xml = "<anonymous/>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Anonymous anonymous = ArticleMetaParser.parseAnonymous(reader);

        assertThat(anonymous).isNotNull();
    }

    @Test
    @DisplayName("parseAddress() - 주소 파싱")
    void testParseAddress() throws Exception {
        String xml = """
            <address>
                <addr-line>123 Main St</addr-line>
                <city>Boston</city>
            </address>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Address address = ArticleMetaParser.parseAddress(reader);

        assertThat(address).isNotNull();
    }

    @Test
    @DisplayName("parseAuthorComment() - 저자 코멘트 파싱")
    void testParseAuthorComment() throws Exception {
        String xml = """
            <author-comment>
                <p>The authors contributed equally to this work.</p>
            </author-comment>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        AuthorComment authorComment = ArticleMetaParser.parseAuthorComment(reader);

        assertThat(authorComment).isNotNull();
    }

    @Test
    @DisplayName("parseBio() - 저자 약력 파싱")
    void testParseBio() throws Exception {
        String xml = """
            <bio id="bio1">
                <p>Dr. Smith is a professor of biology.</p>
            </bio>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Bio bio = ArticleMetaParser.parseBio(reader);

        assertThat(bio).isNotNull();
    }

    @Test
    @DisplayName("parseEtal() - et al 파싱")
    void testParseEtal() throws Exception {
        String xml = "<etal/>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Etal etal = ArticleMetaParser.parseEtal(reader);

        assertThat(etal).isNotNull();
    }

    @Test
    @DisplayName("parseOnBehalfOf() - 대리 작성자 파싱")
    void testParseOnBehalfOf() throws Exception {
        String xml = "<on-behalf-of>on behalf of the Research Consortium</on-behalf-of>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        OnBehalfOf onBehalfOf = ArticleMetaParser.parseOnBehalfOf(reader);

        assertThat(onBehalfOf).isNotNull();
    }

    @Test
    @DisplayName("parseRelatedObject() - 관련 객체 파싱")
    void testParseRelatedObject() throws Exception {
        String xml = "<related-object id=\"ro1\" object-id=\"dataset-001\" object-id-type=\"doi\"/>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        RelatedObject relatedObject = ArticleMetaParser.parseRelatedObject(reader);

        assertThat(relatedObject).isNotNull();
    }

    @Test
    @DisplayName("parseSupportGroup() - 지원 그룹 파싱")
    void testParseSupportGroup() throws Exception {
        String xml = """
            <support-group>
                <funding-source>NIH</funding-source>
                <award-id>R01-12345</award-id>
            </support-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        SupportGroup supportGroup = ArticleMetaParser.parseSupportGroup(reader);

        assertThat(supportGroup).isNotNull();
    }

    @Test
    @DisplayName("parseConference() - 학회 정보 파싱")
    void testParseConference() throws Exception {
        String xml = """
            <conference>
                <conf-name>Annual Biology Conference</conf-name>
                <conf-date>2024-03-15</conf-date>
            </conference>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Conference conference = ArticleMetaParser.parseConference(reader);

        assertThat(conference).isNotNull();
    }

    @Test
    @DisplayName("parseCustomMetaGroup() - 커스텀 메타 그룹 파싱")
    void testParseCustomMetaGroup() throws Exception {
        String xml = """
            <custom-meta-group>
                <custom-meta>
                    <meta-name>priority</meta-name>
                    <meta-value>high</meta-value>
                </custom-meta>
            </custom-meta-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        CustomMetaGroup customMetaGroup = ArticleMetaParser.parseCustomMetaGroup(reader);

        assertThat(customMetaGroup).isNotNull();
    }

    @Test
    @DisplayName("parseSeason() - 계절 파싱")
    void testParseSeason() throws Exception {
        String xml = "<season>Spring</season>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Season season = ArticleMetaParser.parseSeason(reader);

        assertThat(season).isNotNull();
    }

    @Test
    @DisplayName("parseEra() - 시대 파싱")
    void testParseEra() throws Exception {
        String xml = "<era>CE</era>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Era era = ArticleMetaParser.parseEra(reader);

        assertThat(era).isNotNull();
    }

    @Test
    @DisplayName("parseStringDate() - 문자열 날짜 파싱")
    void testParseStringDate() throws Exception {
        String xml = "<string-date>March 2024</string-date>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        StringDate stringDate = ArticleMetaParser.parseStringDate(reader);

        assertThat(stringDate).isNotNull();
    }

    @Test
    @DisplayName("parseAttrib() - attribution 파싱")
    void testParseAttrib() throws Exception {
        String xml = "<attrib>© 2024 The Authors</attrib>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Attrib attrib = ArticleMetaParser.parseAttrib(reader);

        assertThat(attrib).isNotNull();
    }

    // ==================== Private Method Indirect Testing ====================

    @Test
    @DisplayName("parseAff() - institution-wrap 포함 (private 메서드 간접 테스트)")
    void testParseAff_WithInstitutionWrap() throws Exception {
        String xml = """
            <aff id="aff1">
                <institution-wrap>
                    <institution-id institution-id-type="ror">https://ror.org/012345</institution-id>
                    <institution>Harvard University</institution>
                </institution-wrap>
            </aff>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Aff aff = ArticleMetaParser.parseAff(reader);

        assertThat(aff).isNotNull();
        assertThat(aff.getId()).isEqualTo("aff1");
        assertThat(aff.getInstitutionWraps()).isNotNull().hasSize(1);
        assertThat(aff.getInstitutionWraps().get(0).getInstitutionIds()).hasSize(1);
        assertThat(aff.getInstitutionWraps().get(0).getInstitutions()).hasSize(1);
        assertThat(aff.getInstitutionWraps().get(0).getInstitutions().get(0).getContent())
                .isEqualTo("Harvard University");
    }

    @Test
    @DisplayName("parseAff() - 재귀적 institution-wrap 파싱")
    void testParseAff_WithNestedInstitutionWrap() throws Exception {
        String xml = """
            <aff id="aff2">
                <institution-wrap>
                    <institution>Department of Biology</institution>
                    <institution-wrap>
                        <institution>Harvard University</institution>
                    </institution-wrap>
                </institution-wrap>
            </aff>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Aff aff = ArticleMetaParser.parseAff(reader);

        assertThat(aff).isNotNull();
        assertThat(aff.getInstitutionWraps()).hasSize(1);
        assertThat(aff.getInstitutionWraps().get(0).getInstitutionWraps()).hasSize(1);
    }

    // ==================== Additional Coverage Tests (Target: 100%) ====================

    /**
     * Test 26: parseArticleMeta with uncommon elements - volume-id, volume-series, issue-id, issue-title
     * Covers lines 78-91 switch cases
     */
    @Test
    @DisplayName("Test 26: parseArticleMeta - uncommon volume/issue elements")
    void test26_ParseArticleMeta_UncommonVolumeIssueElements() throws Exception {
        String xml = """
            <article-meta>
                <title-group><article-title>Test</article-title></title-group>
                <volume-id>VOL-123</volume-id>
                <volume-series>Series A</volume-series>
                <issue-id>ISS-456</issue-id>
                <issue-title>Special Issue on Testing</issue-title>
                <issue-sponsor>Test Foundation</issue-sponsor>
                <issue-part>Part 1</issue-part>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getVolumeId()).isNotNull();
        assertThat(meta.getVolumeSeries()).isNotNull();
        assertThat(meta.getIssueId()).isNotNull();
        assertThat(meta.getIssueTitle()).isNotNull();
        assertThat(meta.getIssueSponsor()).isNotNull();
        assertThat(meta.getIssuePart()).isNotNull();
    }

    /**
     * Test 27: parseArticleMeta with isbn, supplement
     * Covers lines 99-103 switch cases
     */
    @Test
    @DisplayName("Test 27: parseArticleMeta - isbn and supplement")
    void test27_ParseArticleMeta_IsbnAndSupplement() throws Exception {
        String xml = """
            <article-meta>
                <title-group><article-title>Test</article-title></title-group>
                <isbn>978-3-16-148410-0</isbn>
                <supplement>Supplementary Data</supplement>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getIsbns()).hasSize(1);
        assertThat(meta.getSupplement()).isNotNull();
    }

    /**
     * Test 28: parseArticleMeta with email, ext-link, uri
     * Covers lines 117-124 switch cases
     */
    @Test
    @DisplayName("Test 28: parseArticleMeta - email, ext-link, uri")
    void test28_ParseArticleMeta_ContactElements() throws Exception {
        String xml = """
            <article-meta xmlns:xlink="http://www.w3.org/1999/xlink">
                <title-group><article-title>Test</article-title></title-group>
                <email>test@example.com</email>
                <ext-link ext-link-type="uri" xlink:href="http://example.com">Example</ext-link>
                <uri>http://example.com/resource</uri>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getEmails()).hasSize(1);
        assertThat(meta.getExtLinks()).hasSize(1);
        assertThat(meta.getUris()).hasSize(1);
    }

    /**
     * Test 29: parseArticleMeta with pub-history, related-object, support-group
     * Covers lines 132-160 switch cases
     */
    @Test
    @DisplayName("Test 29: parseArticleMeta - pub-history, related-object, support-group")
    void test29_ParseArticleMeta_HistoryAndRelatedElements() throws Exception {
        String xml = """
            <article-meta>
                <title-group><article-title>Test</article-title></title-group>
                <pub-history>
                    <date date-type="received"><year>2024</year></date>
                </pub-history>
                <related-object>Related Object Info</related-object>
                <support-group>Supported by XYZ Foundation</support-group>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getPubHistory()).isNotNull();
        assertThat(meta.getRelatedObjects()).hasSize(1);
        assertThat(meta.getSupportGroups()).hasSize(1);
    }

    /**
     * Test 30: parseArticleMeta with unknown element (default case)
     * Covers line 172 default case
     */
    @Test
    @DisplayName("Test 30: parseArticleMeta - unknown element triggers default case")
    void test30_ParseArticleMeta_UnknownElement() throws Exception {
        String xml = """
            <article-meta>
                <title-group><article-title>Test</article-title></title-group>
                <unknown-element>Should be skipped</unknown-element>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getTitleGroup()).isNotNull();
    }

    /**
     * Test 31: parseArticleMeta with empty collections
     * Covers lines 186-199 isEmpty() true branches
     */
    @Test
    @DisplayName("Test 31: parseArticleMeta - empty collections")
    void test31_ParseArticleMeta_EmptyCollections() throws Exception {
        String xml = """
            <article-meta>
                <title-group><article-title>Minimal Article</article-title></title-group>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getIsbns()).isNull();
        assertThat(meta.getEmails()).isNull();
        assertThat(meta.getExtLinks()).isNull();
        assertThat(meta.getUris()).isNull();
        assertThat(meta.getSupplementaryMaterials()).isNull();
        assertThat(meta.getRelatedObjects()).isNull();
        assertThat(meta.getSupportGroups()).isNull();
    }

    /**
     * Test 32: parseTitleGroup with fn-group and unknown element
     * Covers lines 235, 238 (fn-group case and default case)
     */
    @Test
    @DisplayName("Test 32: parseTitleGroup - fn-group and unknown element")
    void test32_ParseTitleGroup_FnGroupAndDefault() throws Exception {
        String xml = """
            <title-group>
                <article-title>Test Title</article-title>
                <fn-group>
                    <fn id="fn1"><p>Footnote</p></fn>
                </fn-group>
                <unknown-element>Skip this</unknown-element>
            </title-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        TitleGroup titleGroup = ArticleMetaParser.parseTitleGroup(reader);

        assertThat(titleGroup).isNotNull();
        assertThat(titleGroup.getFnGroup()).isNotNull();
    }

    /**
     * Test 33: parseContribGroup with xref, aff, unknown element
     * Covers lines 281, 284, 287 (xref, aff cases and default)
     */
    @Test
    @DisplayName("Test 33: parseContribGroup - xref, aff, unknown element")
    void test33_ParseContribGroup_XrefAffAndDefault() throws Exception {
        String xml = """
            <contrib-group>
                <contrib contrib-type="author">
                    <name><surname>Smith</surname></name>
                </contrib>
                <xref ref-type="aff" rid="aff1">1</xref>
                <aff id="aff1">Department of Biology</aff>
                <unknown-element>Skip this</unknown-element>
            </contrib-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ContribGroup contribGroup = ArticleMetaParser.parseContribGroup(reader);

        assertThat(contribGroup).isNotNull();
        assertThat(contribGroup.getXrefs()).hasSize(1);
        assertThat(contribGroup.getAffiliations()).hasSize(1);
    }

    /**
     * Test 34: parseContribGroup with empty collections
     * Covers lines 297-299 isEmpty() true branches
     */
    @Test
    @DisplayName("Test 34: parseContribGroup - empty collections")
    void test34_ParseContribGroup_EmptyCollections() throws Exception {
        String xml = """
            <contrib-group>
            </contrib-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ContribGroup contribGroup = ArticleMetaParser.parseContribGroup(reader);

        assertThat(contribGroup).isNotNull();
        assertThat(contribGroup.getContributors()).isNull();
        assertThat(contribGroup.getXrefs()).isNull();
        assertThat(contribGroup.getAffiliations()).isNull();
    }

    /**
     * Test 35: parseContrib with string-name, anonymous, etal
     * Covers lines 349, 355, 376 (string-name, anonymous, etal cases)
     */
    @Test
    @DisplayName("Test 35: parseContrib - string-name, anonymous, etal")
    void test35_ParseContrib_StringNameAnonymousEtal() throws Exception {
        String xml = """
            <contrib contrib-type="author">
                <string-name>Dr. John Smith, PhD</string-name>
                <anonymous>Anonymous</anonymous>
                <etal>et al.</etal>
            </contrib>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Contrib contrib = ArticleMetaParser.parseContrib(reader);

        assertThat(contrib).isNotNull();
        assertThat(contrib.getStringName()).isNotNull();
        assertThat(contrib.getAnonymous()).isNotNull();
        assertThat(contrib.getEtal()).isNotNull();
    }

    /**
     * Test 36: parseContrib with unknown element (default case)
     * Covers line 397 default case
     */
    @Test
    @DisplayName("Test 36: parseContrib - unknown element")
    void test36_ParseContrib_UnknownElement() throws Exception {
        String xml = """
            <contrib contrib-type="author">
                <name><surname>Smith</surname></name>
                <unknown-element>Skip</unknown-element>
            </contrib>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Contrib contrib = ArticleMetaParser.parseContrib(reader);

        assertThat(contrib).isNotNull();
        assertThat(contrib.getName()).isNotNull();
    }

    /**
     * Test 37: parseAff with institution-id, addr-line, label, unknown element
     * Covers lines 464, 465, 469, 470 (institution-id, addr-line, label, default)
     */
    @Test
    @DisplayName("Test 37: parseAff - institution-id, addr-line, label, unknown")
    void test37_ParseAff_InstitutionIdAddrLineLabel() throws Exception {
        String xml = """
            <aff id="aff1">
                <label>1</label>
                <institution-wrap>
                    <institution-id institution-id-type="ror">ROR-123</institution-id>
                    <institution>Harvard University</institution>
                </institution-wrap>
                <addr-line>Cambridge, MA</addr-line>
                <unknown-element>Skip</unknown-element>
            </aff>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Aff aff = ArticleMetaParser.parseAff(reader);

        assertThat(aff).isNotNull();
        assertThat(aff.getInstitutionWraps()).hasSize(1);
        assertThat(aff.getAddrLines()).hasSize(1);
    }

    /**
     * Test 38: parseAff with institution-wrap containing unknown element and empty institutions
     * Covers lines 526, 538 (default case and isEmpty() true) via parseAff → parseInstitutionWrap
     */
    @Test
    @DisplayName("Test 38: parseAff - institution-wrap with unknown element and empty institutions")
    void test38_ParseAff_InstitutionWrapDefaultAndEmpty() throws Exception {
        String xml = """
            <aff id="aff1">
                <institution-wrap>
                    <institution-id institution-id-type="ror">ROR-123</institution-id>
                    <unknown-element>Skip</unknown-element>
                </institution-wrap>
            </aff>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Aff aff = ArticleMetaParser.parseAff(reader);

        assertThat(aff).isNotNull();
        assertThat(aff.getInstitutionWraps()).hasSize(1);
        InstitutionWrap wrap = aff.getInstitutionWraps().get(0);
        assertThat(wrap.getInstitutionIds()).hasSize(1);
        assertThat(wrap.getInstitutions()).isNull(); // Empty
    }

    /**
     * Test 39: parsePmcPubDate with season, era, string-date, unknown element
     * Covers lines 615, 621, 624, 627 (season, era, string-date, default)
     */
    @Test
    @DisplayName("Test 39: parsePmcPubDate - season, era, string-date, unknown")
    void test39_ParsePmcPubDate_SeasonEraStringDate() throws Exception {
        String xml = """
            <pub-date pub-type="ppub">
                <season>Spring</season>
                <year>2024</year>
                <era>CE</era>
                <string-date>Spring 2024 CE</string-date>
                <unknown-element>Skip</unknown-element>
            </pub-date>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PmcPubDate pubDate = ArticleMetaParser.parsePmcPubDate(reader);

        assertThat(pubDate).isNotNull();
        assertThat(pubDate.getSeason()).isNotNull();
        assertThat(pubDate.getEra()).isNotNull();
        assertThat(pubDate.getStringDate()).isNotNull();
    }

    /**
     * Test 40: parseKwdGroup with label, unknown element, empty collections
     * Covers lines 671, 680, 690, 692 (label, default, isEmpty() branches)
     */
    @Test
    @DisplayName("Test 40: parseKwdGroup - label, unknown, empty")
    void test40_ParseKwdGroup_LabelDefaultEmpty() throws Exception {
        String xml = """
            <kwd-group>
                <label>Keywords:</label>
                <unknown-element>Skip</unknown-element>
            </kwd-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        KwdGroup kwdGroup = ArticleMetaParser.parseKwdGroup(reader);

        assertThat(kwdGroup).isNotNull();
        assertThat(kwdGroup.getLabels()).hasSize(1);
        assertThat(kwdGroup.getKeywords()).isNull(); // Empty
    }

    /**
     * Test 41: parsePmcAbstract with unknown element (default case)
     * Covers line 725
     */
    @Test
    @DisplayName("Test 41: parsePmcAbstract - unknown element")
    void test41_ParsePmcAbstract_UnknownElement() throws Exception {
        String xml = """
            <abstract>
                <p>Abstract text</p>
                <unknown-element>Skip</unknown-element>
            </abstract>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PmcAbstract abstract_ = ArticleMetaParser.parsePmcAbstract(reader);

        assertThat(abstract_).isNotNull();
        assertThat(abstract_.getParagraphs()).hasSize(1);
    }

    /**
     * Test 42: parseSupplementaryMaterial with unknown element (default case)
     * Covers line 923
     */
    @Test
    @DisplayName("Test 42: parseSupplementaryMaterial - unknown element")
    void test42_ParseSupplementaryMaterial_UnknownElement() throws Exception {
        String xml = """
            <supplementary-material id="supp1">
                <label>Supplement 1</label>
                <unknown-element>Skip</unknown-element>
            </supplementary-material>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        SupplementaryMaterial supp = ArticleMetaParser.parseSupplementaryMaterial(reader);

        assertThat(supp).isNotNull();
        assertThat(supp.getLabel()).isNotNull();
    }

    /**
     * Test 43: parseTransTitleGroup with trans-subtitle, unknown, empty
     * Covers lines 1026, 1029, 1039 (trans-subtitle, default, isEmpty())
     */
    @Test
    @DisplayName("Test 43: parseTransTitleGroup - trans-subtitle, unknown, empty")
    void test43_ParseTransTitleGroup_TransSubtitleDefaultEmpty() throws Exception {
        String xml = """
            <trans-title-group xml:lang="ko">
                <trans-title>번역된 제목</trans-title>
                <trans-subtitle>번역된 부제목</trans-subtitle>
                <unknown-element>Skip</unknown-element>
            </trans-title-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        TransTitleGroup transTitleGroup = ArticleMetaParser.parseTransTitleGroup(reader);

        assertThat(transTitleGroup).isNotNull();
        assertThat(transTitleGroup.getTransSubtitles()).hasSize(1);
    }

    /**
     * Test 44: parseFnGroup with label, title, unknown, empty
     * Covers lines 1103, 1106, 1112, 1123 (label, title, default, isEmpty())
     */
    @Test
    @DisplayName("Test 44: parseFnGroup - label, title, unknown, empty")
    void test44_ParseFnGroup_LabelTitleDefaultEmpty() throws Exception {
        String xml = """
            <fn-group>
                <label>Notes:</label>
                <title>Footnotes</title>
                <unknown-element>Skip</unknown-element>
            </fn-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        FnGroup fnGroup = ArticleMetaParser.parseFnGroup(reader);

        assertThat(fnGroup).isNotNull();
        assertThat(fnGroup.getLabel()).isNotNull();
        assertThat(fnGroup.getTitle()).isNotNull();
        assertThat(fnGroup.getFootnotes()).isNull(); // Empty
    }

    /**
     * Test 45: parseName with unknown element (default case)
     * Covers line 1174
     */
    @Test
    @DisplayName("Test 45: parseName - unknown element")
    void test45_ParseName_UnknownElement() throws Exception {
        String xml = """
            <name>
                <surname>Smith</surname>
                <given-names>John</given-names>
                <unknown-element>Skip</unknown-element>
            </name>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Name name = ArticleMetaParser.parseName(reader);

        assertThat(name).isNotNull();
        assertThat(name.getSurname()).isNotNull();
    }

    /**
     * Test 46: parseFn with label, unknown, empty
     * Covers lines 1257, 1263, 1274 (label, default, isEmpty())
     */
    @Test
    @DisplayName("Test 46: parseFn - label, unknown, empty")
    void test46_ParseFn_LabelDefaultEmpty() throws Exception {
        String xml = """
            <fn id="fn1">
                <label>*</label>
                <unknown-element>Skip</unknown-element>
            </fn>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Fn fn = ArticleMetaParser.parseFn(reader);

        assertThat(fn).isNotNull();
        assertThat(fn.getLabel()).isNotNull();
        assertThat(fn.getParagraphs()).isNull(); // Empty
    }

    /**
     * Test 47: parseAuthorNotes with unknown, empty
     * Covers lines 1306, 1317 (default, isEmpty())
     */
    @Test
    @DisplayName("Test 47: parseAuthorNotes - unknown, empty corresps")
    void test47_ParseAuthorNotes_DefaultEmpty() throws Exception {
        String xml = """
            <author-notes>
                <fn id="fn1"><p>Note</p></fn>
                <unknown-element>Skip</unknown-element>
            </author-notes>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        AuthorNotes authorNotes = ArticleMetaParser.parseAuthorNotes(reader);

        assertThat(authorNotes).isNotNull();
        assertThat(authorNotes.getCorresps()).isNull(); // Empty
    }

    /**
     * Test 48: parsePmcHistory with unknown element, empty dates
     * Covers lines 1344, 1354 (else branch, isEmpty())
     */
    @Test
    @DisplayName("Test 48: parsePmcHistory - unknown element, empty")
    void test48_ParsePmcHistory_UnknownEmpty() throws Exception {
        String xml = """
            <history>
                <unknown-element>Skip</unknown-element>
            </history>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PmcHistory history = ArticleMetaParser.parsePmcHistory(reader);

        assertThat(history).isNotNull();
        assertThat(history.getDates()).isNull(); // Empty
    }

    /**
     * Test 49: parsePmcDate with season, unknown
     * Covers lines 1379, 1382 (season, default)
     */
    @Test
    @DisplayName("Test 49: parsePmcDate - season, unknown")
    void test49_ParsePmcDate_SeasonDefault() throws Exception {
        String xml = """
            <date date-type="received">
                <season>Spring</season>
                <year>2024</year>
                <unknown-element>Skip</unknown-element>
            </date>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PmcDate pmcDate = ArticleMetaParser.parsePmcDate(reader);

        assertThat(pmcDate).isNotNull();
        assertThat(pmcDate.getSeason()).isNotNull();
    }

    /**
     * Test 50: parsePermissions with unknown, empty copyrightStatements
     * Covers lines 1423, 1433 (default, isEmpty())
     */
    @Test
    @DisplayName("Test 50: parsePermissions - unknown, empty")
    void test50_ParsePermissions_DefaultEmpty() throws Exception {
        String xml = """
            <permissions>
                <copyright-year>2024</copyright-year>
                <unknown-element>Skip</unknown-element>
            </permissions>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Permissions permissions = ArticleMetaParser.parsePermissions(reader);

        assertThat(permissions).isNotNull();
        assertThat(permissions.getCopyrightStatements()).isNull(); // Empty
    }

    /**
     * Test 51: parseTransAbstract with xmlLang == null
     * Covers line 1454 (xmlLang fallback)
     */
    @Test
    @DisplayName("Test 51: parseTransAbstract - xmlLang fallback")
    void test51_ParseTransAbstract_XmlLangFallback() throws Exception {
        String xml = """
            <trans-abstract>Translated abstract text</trans-abstract>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        TransAbstract transAbstract = ArticleMetaParser.parseTransAbstract(reader);

        assertThat(transAbstract).isNotNull();
        // xmlLang can be null or from namespace attribute
    }

    /**
     * Test 52: parseAwardGroup with unknown, empty fundingSources and awardIds
     * Covers lines 1513, 1524, 1525 (default, isEmpty() branches)
     */
    @Test
    @DisplayName("Test 52: parseAwardGroup - unknown, empty")
    void test52_ParseAwardGroup_DefaultEmpty() throws Exception {
        String xml = """
            <award-group>
                <unknown-element>Skip</unknown-element>
            </award-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        AwardGroup awardGroup = ArticleMetaParser.parseAwardGroup(reader);

        assertThat(awardGroup).isNotNull();
        assertThat(awardGroup.getFundingSources()).isNull();
        assertThat(awardGroup.getAwardIds()).isNull();
    }

    /**
     * Test 53: parseCounts with unknown element (default case)
     * Covers line 1602
     */
    @Test
    @DisplayName("Test 53: parseCounts - unknown element")
    void test53_ParseCounts_UnknownElement() throws Exception {
        String xml = """
            <counts>
                <page-count count="10"/>
                <unknown-element>Skip</unknown-element>
            </counts>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Counts counts = ArticleMetaParser.parseCounts(reader);

        assertThat(counts).isNotNull();
        assertThat(counts.getPageCount()).isEqualTo(10);
    }

    /**
     * Test 54: parseDispFormula with mml:math, unknown
     * Covers lines 1758, 1761 (mml:math, default)
     */
    @Test
    @DisplayName("Test 54: parseDispFormula - mml:math, unknown")
    void test54_ParseDispFormula_MmlMathDefault() throws Exception {
        String xml = """
            <disp-formula id="eq1">
                <mml:math xmlns:mml="http://www.w3.org/1998/Math/MathML">
                    <mml:mi>x</mml:mi>
                </mml:math>
                <unknown-element>Skip</unknown-element>
            </disp-formula>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        DispFormula formula = ArticleMetaParser.parseDispFormula(reader);

        assertThat(formula).isNotNull();
        assertThat(formula.getMmlMath()).isNotNull();
    }

    /**
     * Test 55: parseDispFormulaGroup with label, unknown, empty
     * Covers lines 1797, 1803, 1814 (label, default, isEmpty())
     */
    @Test
    @DisplayName("Test 55: parseDispFormulaGroup - label, unknown, empty")
    void test55_ParseDispFormulaGroup_LabelDefaultEmpty() throws Exception {
        String xml = """
            <disp-formula-group>
                <label>Equation Group 1</label>
                <unknown-element>Skip</unknown-element>
            </disp-formula-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        DispFormulaGroup group = ArticleMetaParser.parseDispFormulaGroup(reader);

        assertThat(group).isNotNull();
        assertThat(group.getLabel()).isNotNull();
        assertThat(group.getDispFormulas()).isNull(); // Empty
    }

    /**
     * Test 56: parseAlternatives with table-wrap (should skip), unknown, empty graphics
     * Covers lines 1861, 1876, 1888 (table-wrap skip, default, isEmpty())
     */
    @Test
    @DisplayName("Test 56: parseAlternatives - table-wrap skip, unknown, empty")
    void test56_ParseAlternatives_TableWrapDefaultEmpty() throws Exception {
        String xml = """
            <alternatives xmlns:xlink="http://www.w3.org/1999/xlink">
                <table-wrap id="tw1"><table></table></table-wrap>
                <media mimetype="video/mp4" xlink:href="video.mp4"/>
                <unknown-element>Skip</unknown-element>
            </alternatives>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Alternatives alternatives = ArticleMetaParser.parseAlternatives(reader);

        assertThat(alternatives).isNotNull();
        assertThat(alternatives.getMedias()).hasSize(1);
        assertThat(alternatives.getGraphics()).isNull(); // Empty
    }

    /**
     * Test 57: parsePreformat with attrib, permissions, unknown, empty content
     * Covers lines 1938-1946, 1959-1961 (attrib, permissions, default, isEmpty() branches)
     */
    @Test
    @DisplayName("Test 57: parsePreformat - attrib, permissions, unknown, empty")
    void test57_ParsePreformat_AttribPermissionsDefaultEmpty() throws Exception {
        String xml = """
            <preformat>
                <attrib>Source: Example</attrib>
                <permissions><copyright-year>2024</copyright-year></permissions>
                <unknown-element>Skip</unknown-element>
            </preformat>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Preformat preformat = ArticleMetaParser.parsePreformat(reader);

        assertThat(preformat).isNotNull();
        assertThat(preformat.getAttribs()).hasSize(1);
        assertThat(preformat.getPermissions()).hasSize(1);
        // Content may contain whitespace between elements, so we check it's not meaningful text
        String content = preformat.getContent();
        if (content != null) {
            assertThat(content.trim()).isEmpty();
        }
    }

    /**
     * Test 58: parseArticleMeta with supplementary-material
     * Covers line 127-129 (supplementary-material case) - LINE 54 switch branch
     */
    @Test
    @DisplayName("Test 58: parseArticleMeta - supplementary-material")
    void test58_ParseArticleMeta_SupplementaryMaterial() throws Exception {
        String xml = """
            <article-meta xmlns:xlink="http://www.w3.org/1999/xlink">
                <title-group><article-title>Test</article-title></title-group>
                <supplementary-material id="SM1" xlink:href="data.xlsx">
                    <label>Supplementary Data 1</label>
                    <caption><p>Raw data</p></caption>
                </supplementary-material>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getSupplementaryMaterials()).hasSize(1);
    }

    /**
     * Test 59: parseArticleMeta with empty supplementary-materials collection
     * Covers line 191 isEmpty() true branch
     */
    @Test
    @DisplayName("Test 59: parseArticleMeta - empty supplementary-materials")
    void test59_ParseArticleMeta_EmptySupplementaryMaterials() throws Exception {
        String xml = """
            <article-meta>
                <title-group><article-title>Minimal</article-title></title-group>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getSupplementaryMaterials()).isNull(); // Empty collection
    }

    /**
     * Test 60: parseAff with empty textContent
     * Covers line 464 (textContent.length() > 0) false branch
     */
    @Test
    @DisplayName("Test 60: parseAff - empty textContent")
    void test60_ParseAff_EmptyTextContent() throws Exception {
        String xml = """
            <aff id="aff1">
                <institution-wrap>
                    <institution>Harvard</institution>
                </institution-wrap>
            </aff>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Aff aff = ArticleMetaParser.parseAff(reader);

        assertThat(aff).isNotNull();
        assertThat(aff.getInstitutionWraps()).hasSize(1);
        // Value should be null or empty when only whitespace
        if (aff.getValue() != null) {
            assertThat(aff.getValue().trim()).isEmpty();
        }
    }

    /**
     * Test 61: parseCounts with elements but no count attributes
     * Covers lines 1561, 1568, 1575, 1582, 1589, 1596 (all if (xxxAttr != null) false branches)
     */
    @Test
    @DisplayName("Test 61: parseCounts - elements without count attributes")
    void test61_ParseCounts_ElementsWithoutCountAttributes() throws Exception {
        String xml = """
            <counts>
                <page-count/>
                <fig-count/>
                <table-count/>
                <equation-count/>
                <ref-count/>
                <word-count/>
            </counts>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Counts counts = ArticleMetaParser.parseCounts(reader);

        assertThat(counts).isNotNull();
        assertThat(counts.getPageCount()).isNull();
        assertThat(counts.getFigCount()).isNull();
        assertThat(counts.getTableCount()).isNull();
        assertThat(counts.getEquationCount()).isNull();
        assertThat(counts.getRefCount()).isNull();
        assertThat(counts.getWordCount()).isNull();
    }

    /**
     * Test 62: parseChemStruct with various elements
     * Covers line 2333 switch branches (label, alt-text, long-desc, graphic, media, attrib, permissions)
     */
    @Test
    @DisplayName("Test 62: parseChemStruct - multiple elements")
    void test62_ParseChemStruct_MultipleElements() throws Exception {
        String xml = """
            <chem-struct xmlns:xlink="http://www.w3.org/1999/xlink" id="chem1">
                <label>Structure 1</label>
                <alt-text>Chemical structure diagram</alt-text>
                <long-desc>Detailed description of the chemical structure</long-desc>
                <graphic xlink:href="chem1.png"/>
                <media mimetype="image/svg+xml" xlink:href="chem1.svg"/>
                <attrib>Source: ChemDraw</attrib>
                <permissions><copyright-statement>CC BY 4.0</copyright-statement></permissions>
            </chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getLabel()).isNotNull();
        assertThat(chemStruct.getAltTexts()).hasSize(1);
        assertThat(chemStruct.getLongDescs()).hasSize(1);
        assertThat(chemStruct.getGraphics()).hasSize(1);
        assertThat(chemStruct.getMedias()).hasSize(1);
        assertThat(chemStruct.getAttrib()).isNotNull();
        assertThat(chemStruct.getPermissions()).isNotNull();
    }

    /**
     * Test 63: parseChemStruct with array, code, textual-form
     * Covers line 2333 additional switch branches (array, code, textual-form, default)
     */
    @Test
    @DisplayName("Test 63: parseChemStruct - array, code, textual-form, unknown")
    void test63_ParseChemStruct_ArrayCodeTextual() throws Exception {
        String xml = """
            <chem-struct id="chem2">
                <array><tbody><tr><td>Data</td></tr></tbody></array>
                <code>C6H12O6</code>
                <textual-form>Glucose</textual-form>
                <unknown-element>Skip this</unknown-element>
            </chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getArrays()).hasSize(1);
        assertThat(chemStruct.getCodes()).hasSize(1);
        assertThat(chemStruct.getTextualForms()).hasSize(1);
    }

    /**
     * Test 64: parsePubHistory with event elements
     * Covers parsePubHistory "event" case (line 2477-2478) and parseEvent method (12 branches)
     * parseEvent covers: event-desc, title, date, default cases
     */
    @Test
    @DisplayName("Test 64: parsePubHistory - with event elements")
    void test64_ParsePubHistory_WithEvents() throws Exception {
        String xml = """
            <pub-history>
                <date date-type="received">
                    <day>15</day>
                    <month>3</month>
                    <year>2024</year>
                </date>
                <event event-type="accepted" id="evt1">
                    <event-desc>Manuscript accepted after peer review</event-desc>
                    <title>Acceptance</title>
                    <date date-type="accepted">
                        <day>20</day>
                        <month>4</month>
                        <year>2024</year>
                    </date>
                    <date date-type="rev-request">
                        <day>25</day>
                        <month>3</month>
                        <year>2024</year>
                    </date>
                    <unknown-element>Skip this</unknown-element>
                </event>
                <event event-type="published">
                    <title>Publication</title>
                    <date date-type="pub">
                        <day>1</day>
                        <month>5</month>
                        <year>2024</year>
                    </date>
                </event>
            </pub-history>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PubHistory pubHistory = ArticleMetaParser.parsePubHistory(reader);

        assertThat(pubHistory).isNotNull();
        assertThat(pubHistory.getDates()).hasSize(1); // One top-level date
        assertThat(pubHistory.getEvents()).hasSize(2); // Two events

        // First event
        Event event1 = pubHistory.getEvents().get(0);
        assertThat(event1.getEventType()).isEqualTo("accepted");
        assertThat(event1.getId()).isEqualTo("evt1");
        assertThat(event1.getEventDesc()).isEqualTo("Manuscript accepted after peer review");
        assertThat(event1.getTitle()).isEqualTo("Acceptance");
        assertThat(event1.getDates()).hasSize(2);

        // Second event
        Event event2 = pubHistory.getEvents().get(1);
        assertThat(event2.getEventType()).isEqualTo("published");
        assertThat(event2.getTitle()).isEqualTo("Publication");
        assertThat(event2.getDates()).hasSize(1);
    }

    /**
     * Test 65: parseAff - with multiple text fragments
     * Covers line 465: textContent.append(" ") when textContent.length() > 0
     */
    @Test
    @DisplayName("Test 65: parseAff - with multiple text fragments separated by elements")
    void test65_ParseAff_WithMultipleTextFragments() throws Exception {
        String xml = """
            <aff id="aff1">
                Department of Biology
                <institution-wrap>
                    <institution>Harvard University</institution>
                </institution-wrap>
                Cambridge, MA
                <addr-line>02138</addr-line>
                USA
            </aff>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Aff aff = ArticleMetaParser.parseAff(reader);

        assertThat(aff).isNotNull();
        assertThat(aff.getId()).isEqualTo("aff1");
        assertThat(aff.getValue()).contains("Department of Biology");
        assertThat(aff.getValue()).contains("Cambridge, MA");
        assertThat(aff.getValue()).contains("USA");
        // Verify that spaces were added between text fragments (line 465)
        assertThat(aff.getValue()).matches(".*Biology\\s+Cambridge.*");
        assertThat(aff.getInstitutionWraps()).hasSize(1);
        assertThat(aff.getAddrLines()).hasSize(1);
    }

    /**
     * Test 66: parsePubHistory - with unknown element
     * Covers line 2481: default case skipElement(reader)
     */
    @Test
    @DisplayName("Test 66: parsePubHistory - with unknown element")
    void test66_ParsePubHistory_WithUnknownElement() throws Exception {
        String xml = """
            <pub-history>
                <date date-type="received">
                    <day>15</day>
                    <month>3</month>
                    <year>2024</year>
                </date>
                <unknown-pub-element>Some content</unknown-pub-element>
                <event event-type="accepted">
                    <title>Accepted</title>
                </event>
                <another-unknown>More content</another-unknown>
            </pub-history>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PubHistory pubHistory = ArticleMetaParser.parsePubHistory(reader);

        assertThat(pubHistory).isNotNull();
        assertThat(pubHistory.getDates()).hasSize(1);
        assertThat(pubHistory.getEvents()).hasSize(1);
        // Unknown elements should be skipped without error (line 2481)
    }

    /**
     * Test 67: parseAffAlternatives - with unknown element
     * Covers line 2409: else case skipElement(reader)
     */
    @Test
    @DisplayName("Test 67: parseAffAlternatives - with unknown element")
    void test67_ParseAffAlternatives_WithUnknownElement() throws Exception {
        String xml = """
            <aff-alternatives>
                <aff id="aff-en">
                    <institution>Harvard University</institution>
                </aff>
                <unknown-aff-element>Skip this</unknown-aff-element>
                <aff id="aff-jp" xml:lang="ja">
                    <institution>ハーバード大学</institution>
                </aff>
                <another-unknown>Also skip</another-unknown>
            </aff-alternatives>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        AffAlternatives affAlternatives = ArticleMetaParser.parseAffAlternatives(reader);

        assertThat(affAlternatives).isNotNull();
        assertThat(affAlternatives.getAffiliations()).hasSize(2);
        // Unknown elements should be skipped without error (line 2409)
        assertThat(affAlternatives.getAffiliations().get(0).getId()).isEqualTo("aff-en");
        assertThat(affAlternatives.getAffiliations().get(1).getId()).isEqualTo("aff-jp");
    }

    /**
     * Test 68: parseChemStruct - with no optional elements (empty collections)
     * Covers isEmpty() true branches for altTexts, longDescs, graphics, medias, arrays, codes, textualForms
     * This should cover the 4 missed branches in parseChemStruct
     */
    @Test
    @DisplayName("Test 68: parseChemStruct - with no optional elements")
    void test68_ParseChemStruct_NoOptionalElements() throws Exception {
        String xml = """
            <chem-struct id="chem-empty">
                <!-- No optional elements, only text content -->
                H2O
            </chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getId()).isEqualTo("chem-empty");
        assertThat(chemStruct.getValue()).isEqualTo("H2O");
        // All optional collections should be null (isEmpty() true branches)
        assertThat(chemStruct.getAltTexts()).isNull();
        assertThat(chemStruct.getLongDescs()).isNull();
        assertThat(chemStruct.getGraphics()).isNull();
        assertThat(chemStruct.getMedias()).isNull();
        assertThat(chemStruct.getArrays()).isNull();
        assertThat(chemStruct.getCodes()).isNull();
        assertThat(chemStruct.getTextualForms()).isNull();
        assertThat(chemStruct.getLabel()).isNull();
        assertThat(chemStruct.getAttrib()).isNull();
        assertThat(chemStruct.getPermissions()).isNull();
    }

    /**
     * Test 69: parsePreformat - with no optional elements
     * Covers isEmpty() true branches for attribs and permissions
     * This should cover the 3 missed branches in parsePreformat
     */
    @Test
    @DisplayName("Test 69: parsePreformat - with no optional elements")
    void test69_ParsePreformat_NoOptionalElements() throws Exception {
        String xml = """
            <preformat id="pre1">
                function main() {
                    console.log("Hello World");
                }
            </preformat>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Preformat preformat = ArticleMetaParser.parsePreformat(reader);

        assertThat(preformat).isNotNull();
        assertThat(preformat.getId()).isEqualTo("pre1");
        assertThat(preformat.getContent()).contains("function main()");
        // Optional collections should be null (isEmpty() true branches)
        assertThat(preformat.getAttribs()).isNull();
        assertThat(preformat.getPermissions()).isNull();
    }

    /**
     * Test 70: parseChemStructWrap - with empty collections
     * Covers isEmpty() true branch for chemStructs list
     * This should cover 1-2 missed branches in parseChemStructWrap
     */
    @Test
    @DisplayName("Test 70: parseChemStructWrap - empty")
    void test70_ParseChemStructWrap_Empty() throws Exception {
        String xml = """
            <chem-struct-wrap id="wrap-empty">
                <label>Empty Wrap</label>
                <!-- No chem-struct elements -->
            </chem-struct-wrap>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStructWrap chemStructWrap = ArticleMetaParser.parseChemStructWrap(reader);

        assertThat(chemStructWrap).isNotNull();
        assertThat(chemStructWrap.getId()).isEqualTo("wrap-empty");
        assertThat(chemStructWrap.getLabel()).isNotNull();
        // ChemStructs list should be null when empty
        assertThat(chemStructWrap.getChemStructs()).isNull();
    }

    /**
     * Test 71: parseChemStruct - with NO text content
     * Covers value.length() == 0 false branch (line 2378)
     * Previous Test 68 had text "H2O", so value.length() > 0 was true
     */
    @Test
    @DisplayName("Test 71: parseChemStruct - with NO text content")
    void test71_ParseChemStruct_NoTextContent() throws Exception {
        String xml = """
            <chem-struct id="chem-no-text"><label>Compound A</label></chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getId()).isEqualTo("chem-no-text");
        assertThat(chemStruct.getLabel()).isNotNull();
        // Value should be null when no text content (value.length() == 0)
        assertThat(chemStruct.getValue()).isNull();
    }

    /**
     * Test 72: parsePreformat - with NO content
     * Covers content.length() == 0 false branch (line 1959)
     * Previous Test 69 had content, so content.length() > 0 was true
     */
    @Test
    @DisplayName("Test 72: parsePreformat - with NO content")
    void test72_ParsePreformat_NoContent() throws Exception {
        String xml = """
            <preformat id="pre-no-content"><attrib>Attribution text</attrib></preformat>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Preformat preformat = ArticleMetaParser.parsePreformat(reader);

        assertThat(preformat).isNotNull();
        assertThat(preformat.getId()).isEqualTo("pre-no-content");
        assertThat(preformat.getAttribs()).hasSize(1);
        // Content should be null when no text content
        assertThat(preformat.getContent()).isNull();
    }

    /**
     * Test 73: parseChemStructWrap - with unknown element
     * Covers default case in switch (line 2206)
     * Previous Test 70 only had <label>, which is a known element
     */
    @Test
    @DisplayName("Test 73: parseChemStructWrap - with unknown element")
    void test73_ParseChemStructWrap_UnknownElement() throws Exception {
        String xml = """
            <chem-struct-wrap id="wrap-unknown">
                <unknown-element>Skip this</unknown-element>
                <chem-struct id="cs1">C6H12O6</chem-struct>
                <another-unknown>Also skip</another-unknown>
            </chem-struct-wrap>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStructWrap chemStructWrap = ArticleMetaParser.parseChemStructWrap(reader);

        assertThat(chemStructWrap).isNotNull();
        assertThat(chemStructWrap.getId()).isEqualTo("wrap-unknown");
        // Unknown elements should be skipped (line 2206)
        assertThat(chemStructWrap.getChemStructs()).hasSize(1);
        assertThat(chemStructWrap.getChemStructs().get(0).getId()).isEqualTo("cs1");
    }

    /**
     * Test 74: parseChemStruct - with CDATA section
     * Covers CDATA event handling (line 2368: CHARACTERS || CDATA)
     * Most tests use CHARACTERS, but CDATA is also valid
     */
    @Test
    @DisplayName("Test 74: parseChemStruct - with CDATA section")
    void test74_ParseChemStruct_WithCDATA() throws Exception {
        String xml = """
            <chem-struct id="chem-cdata">
                <![CDATA[C6H5-COOH]]>
            </chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getId()).isEqualTo("chem-cdata");
        // CDATA content should be captured
        assertThat(chemStruct.getValue()).isEqualTo("C6H5-COOH");
    }

    /**
     * Test 75: parsePubHistory - with NO dates (only events)
     * Covers dates.isEmpty() true branch (line 2494)
     * Previous Test 66 had dates, so dates.isEmpty() was false
     */
    @Test
    @DisplayName("Test 75: parsePubHistory - with NO dates (only events)")
    void test75_ParsePubHistory_NoDates() throws Exception {
        String xml = """
            <pub-history id="hist1">
                <event event-type="received">
                    <title>Received</title>
                </event>
                <event event-type="accepted">
                    <title>Accepted</title>
                </event>
            </pub-history>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PubHistory pubHistory = ArticleMetaParser.parsePubHistory(reader);

        assertThat(pubHistory).isNotNull();
        assertThat(pubHistory.getId()).isEqualTo("hist1");
        assertThat(pubHistory.getEvents()).hasSize(2);
        // Dates should be null when empty (dates.isEmpty() true)
        assertThat(pubHistory.getDates()).isNull();
    }

    /**
     * Test 76: parseAffAlternatives - empty affiliations list
     * Covers affiliations.isEmpty() false branch (line 2419)
     * This is the OPPOSITE of Test 67 which had affiliations
     *
     * Note: Actually, we need to test isEmpty() TRUE (when list is empty).
     * Test 67 already tested with 2 affiliations, so isEmpty() was false.
     * We need a case with NO affiliations for isEmpty() true.
     */
    @Test
    @DisplayName("Test 76: parseAffAlternatives - truly empty (no aff elements)")
    void test76_ParseAffAlternatives_TrulyEmpty() throws Exception {
        String xml = """
            <aff-alternatives>
                <unknown-element>This should be skipped</unknown-element>
            </aff-alternatives>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        AffAlternatives affAlternatives = ArticleMetaParser.parseAffAlternatives(reader);

        assertThat(affAlternatives).isNotNull();
        // Affiliations should be null when empty (isEmpty() true)
        assertThat(affAlternatives.getAffiliations()).isNull();
    }

    /**
     * Test 77: parseAff - with COMMENT (covers else block's false branch)
     * Covers line 468: if (event == END_ELEMENT) false branch when COMMENT occurs
     */
    @Test
    @DisplayName("Test 77: parseAff - with COMMENT")
    void test77_ParseAff_WithComment() throws Exception {
        String xml = """
            <aff id="aff1">
                <!-- This comment tests the else block -->
                <institution-wrap>
                    <institution>Test Institution</institution>
                </institution-wrap>
            </aff>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Aff aff = ArticleMetaParser.parseAff(reader);

        assertThat(aff).isNotNull();
        assertThat(aff.getId()).isEqualTo("aff1");
        assertThat(aff.getInstitutionWraps()).hasSize(1);
    }

    /**
     * Test 78: parsePreformat - with COMMENT (covers else block's false branch)
     * Covers line 1933: if (event == END_ELEMENT) false branch when COMMENT occurs
     */
    @Test
    @DisplayName("Test 78: parsePreformat - with COMMENT")
    void test78_ParsePreformat_WithComment() throws Exception {
        String xml = """
            <preformat id="pre1" preformat-type="code" position="anchor">
                <!-- Comment before content -->
                function test() { return true; }
            </preformat>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Preformat preformat = ArticleMetaParser.parsePreformat(reader);

        assertThat(preformat).isNotNull();
        assertThat(preformat.getId()).isEqualTo("pre1");
        assertThat(preformat.getPreformatType()).isEqualTo("code");
        assertThat(preformat.getPosition()).isEqualTo(Position.ANCHOR);
        assertThat(preformat.getContent()).contains("function test()");
    }

    /**
     * Test 79: parseChemStruct - with CDATA section
     * Covers line 2341: CDATA branch
     */
    @Test
    @DisplayName("Test 79: parseChemStruct - with CDATA section")
    void test79_ParseChemStruct_WithCDATA() throws Exception {
        String xml = """
            <chem-struct id="chem1">
                <![CDATA[Chemical structure data with special characters: <>&]]>
            </chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getId()).isEqualTo("chem1");
        assertThat(chemStruct.getValue()).isNotNull();
        assertThat(chemStruct.getValue()).contains("Chemical structure data");
    }

    /**
     * Test 80: parseChemStruct - mixed CHARACTERS and CDATA
     * Additional coverage for CDATA handling
     */
    @Test
    @DisplayName("Test 80: parseChemStruct - mixed content")
    void test80_ParseChemStruct_MixedContent() throws Exception {
        String xml = """
            <chem-struct id="chem2">
                Normal text
                <![CDATA[<special>CDATA content</special>]]>
                More text
            </chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getId()).isEqualTo("chem2");
        assertThat(chemStruct.getValue()).isNotNull();
    }

    /**
     * Test 81: parseChemStruct with CDATA section
     * This test covers the CDATA branch separately from CHARACTERS
     * Note: Even with IS_COALESCING=false, the default StAX implementation may still
     * convert CDATA sections to CHARACTERS events. This test verifies that CDATA content
     * is correctly parsed regardless of the event type.
     */
    @Test
    @DisplayName("Test 81: parseChemStruct - CDATA section")
    void test81_ParseChemStruct_CDataSection() throws Exception {
        String xml = """
            <chem-struct id="chem-cdata"><![CDATA[C6H12O6]]></chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next(); // Move to START_ELEMENT <chem-struct>

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getId()).isEqualTo("chem-cdata");
        assertThat(chemStruct.getValue()).isEqualTo("C6H12O6");
    }

    /**
     * Test 82: ArticleMetaParser private constructor test
     * This test covers the private constructor of the utility class using reflection
     */
    @Test
    @DisplayName("Test 82: ArticleMetaParser - Private constructor")
    void test82_ArticleMetaParser_PrivateConstructor() throws Exception {
        // Use reflection to access the private constructor
        var constructor = ArticleMetaParser.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Verify that calling the constructor throws UnsupportedOperationException
        try {
            constructor.newInstance();
            throw new AssertionError("Expected UnsupportedOperationException");
        } catch (Exception e) {
            // Unwrap the InvocationTargetException
            Throwable cause = e.getCause();
            assertThat(cause).isInstanceOf(UnsupportedOperationException.class);
            assertThat(cause.getMessage()).isEqualTo("Utility class should not be instantiated");
        }
    }
}
