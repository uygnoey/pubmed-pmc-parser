package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ArticleMetaParser의 큰 메서드들에 대한 테스트
 *
 * KR: instruction count가 큰 메서드들을 테스트하여 커버리지 대폭 향상
 * EN: Test methods with high instruction counts to significantly improve coverage
 */
class ArticleMetaParserLargeMethodsTest {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

    private XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        return factory.createXMLStreamReader(new StringReader(xml));
    }

    // ==================== parseArticleMeta (507 instructions) ====================

    @Test
    @DisplayName("parseArticleMeta() - 기본 article-meta 파싱")
    void testParseArticleMeta_Basic() throws Exception {
        String xml = """
            <article-meta>
                <article-id pub-id-type="doi">10.1234/example</article-id>
                <title-group>
                    <article-title>Sample Article Title</article-title>
                </title-group>
                <pub-date pub-type="epub">
                    <year>2024</year>
                    <month>01</month>
                    <day>15</day>
                </pub-date>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta articleMeta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(articleMeta).isNotNull();
        assertThat(articleMeta.getArticleIds()).hasSize(1);
        assertThat(articleMeta.getArticleIds().get(0).getPubIdType()).isEqualTo("doi");
        assertThat(articleMeta.getArticleIds().get(0).getValue()).isEqualTo("10.1234/example");
        assertThat(articleMeta.getTitleGroup()).isNotNull();
        assertThat(articleMeta.getPubDates()).hasSize(1);
    }

    @Test
    @DisplayName("parseArticleMeta() - contrib-group과 aff 포함")
    void testParseArticleMeta_WithContribAndAff() throws Exception {
        String xml = """
            <article-meta>
                <contrib-group>
                    <contrib contrib-type="author">
                        <name>
                            <surname>Smith</surname>
                            <given-names>John</given-names>
                        </name>
                    </contrib>
                </contrib-group>
                <aff id="aff1">Department of Biology</aff>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta articleMeta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(articleMeta).isNotNull();
        assertThat(articleMeta.getContribGroups()).hasSize(1);
        assertThat(articleMeta.getAffiliations()).hasSize(1);
        assertThat(articleMeta.getAffiliations().get(0).getId()).isEqualTo("aff1");
    }

    @Test
    @DisplayName("parseArticleMeta() - 다양한 메타데이터 포함")
    void testParseArticleMeta_Comprehensive() throws Exception {
        String xml = """
            <article-meta>
                <article-id pub-id-type="pmid">12345678</article-id>
                <article-id pub-id-type="pmc">PMC1234567</article-id>
                <volume>25</volume>
                <issue>3</issue>
                <fpage>123</fpage>
                <lpage>145</lpage>
                <abstract>
                    <p>This is an abstract.</p>
                </abstract>
                <kwd-group>
                    <kwd>biology</kwd>
                    <kwd>research</kwd>
                </kwd-group>
                <funding-group>
                    <funding-source>NIH</funding-source>
                </funding-group>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ArticleMeta articleMeta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(articleMeta).isNotNull();
        assertThat(articleMeta.getArticleIds()).hasSize(2);
        assertThat(articleMeta.getVolume()).isNotNull();
        assertThat(articleMeta.getIssue()).isNotNull();
        assertThat(articleMeta.getFpage()).isNotNull();
        assertThat(articleMeta.getLpage()).isNotNull();
        assertThat(articleMeta.getAbstracts()).hasSize(1);
        assertThat(articleMeta.getKwdGroups()).hasSize(1);
        assertThat(articleMeta.getFundingGroups()).hasSize(1);
    }

    // ==================== parseSupplementaryMaterial (629 instructions) ====================

    @Test
    @DisplayName("parseSupplementaryMaterial() - 기본 supplementary-material 파싱")
    void testParseSupplementaryMaterial_Basic() throws Exception {
        String xml = """
            <supplementary-material id="supp1" mimetype="application" mime-subtype="pdf">
                <label>Supplementary Material 1</label>
                <caption>
                    <title>Additional Data</title>
                    <p>Supplementary figures and tables.</p>
                </caption>
            </supplementary-material>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        SupplementaryMaterial suppMat = ArticleMetaParser.parseSupplementaryMaterial(reader);

        assertThat(suppMat).isNotNull();
        assertThat(suppMat.getId()).isEqualTo("supp1");
        assertThat(suppMat.getMimetype()).isEqualTo("application");
        assertThat(suppMat.getMimeSubtype()).isEqualTo("pdf");
        assertThat(suppMat.getLabel()).isNotNull();
        assertThat(suppMat.getCaptions()).hasSize(1);
    }

    @Test
    @DisplayName("parseSupplementaryMaterial() - xlink 속성 포함")
    void testParseSupplementaryMaterial_WithXlink() throws Exception {
        String xml = """
            <supplementary-material xmlns:xlink="http://www.w3.org/1999/xlink"
                                   id="supp2"
                                   xlink:href="http://example.com/data.pdf"
                                   xlink:type="simple">
                <label>Supplementary File</label>
            </supplementary-material>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        SupplementaryMaterial suppMat = ArticleMetaParser.parseSupplementaryMaterial(reader);

        assertThat(suppMat).isNotNull();
        assertThat(suppMat.getId()).isEqualTo("supp2");
        assertThat(suppMat.getXlinkHref()).isEqualTo("http://example.com/data.pdf");
        assertThat(suppMat.getXlinkType()).isEqualTo("simple");
    }

    @Test
    @DisplayName("parseSupplementaryMaterial() - 다양한 자식 요소 포함")
    void testParseSupplementaryMaterial_WithMultipleChildren() throws Exception {
        String xml = """
            <supplementary-material xmlns:xlink="http://www.w3.org/1999/xlink" id="supp3">
                <object-id pub-id-type="doi">10.1234/supp.data</object-id>
                <label>Supplementary Data</label>
                <caption>
                    <title>Dataset 1</title>
                </caption>
                <abstract>
                    <p>Raw experimental data.</p>
                </abstract>
                <kwd-group>
                    <kwd>dataset</kwd>
                    <kwd>raw data</kwd>
                </kwd-group>
                <p>Description paragraph.</p>
                <graphic xlink:href="figure-s1.jpg"/>
            </supplementary-material>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        SupplementaryMaterial suppMat = ArticleMetaParser.parseSupplementaryMaterial(reader);

        assertThat(suppMat).isNotNull();
        assertThat(suppMat.getObjectIds()).hasSize(1);
        assertThat(suppMat.getCaptions()).hasSize(1);
        assertThat(suppMat.getAbstracts()).hasSize(1);
        assertThat(suppMat.getKwdGroups()).hasSize(1);
        assertThat(suppMat.getParagraphs()).hasSize(1);
        assertThat(suppMat.getGraphics()).hasSize(1);
    }

    // ==================== parseContrib (318 instructions) ====================

    @Test
    @DisplayName("parseContrib() - 기본 contributor 파싱")
    void testParseContrib_Basic() throws Exception {
        String xml = """
            <contrib contrib-type="author">
                <name>
                    <surname>Doe</surname>
                    <given-names>Jane</given-names>
                </name>
            </contrib>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Contrib contrib = ArticleMetaParser.parseContrib(reader);

        assertThat(contrib).isNotNull();
        assertThat(contrib.getContribType()).isEqualTo("author");
        assertThat(contrib.getName()).isNotNull();
        assertThat(contrib.getName().getSurname().getValue()).isEqualTo("Doe");
        assertThat(contrib.getName().getGivenNames().getValue()).isEqualTo("Jane");
    }

    @Test
    @DisplayName("parseContrib() - 소속 및 이메일 포함")
    void testParseContrib_WithAffAndEmail() throws Exception {
        String xml = """
            <contrib contrib-type="author" corresp="yes" id="author1">
                <name>
                    <surname>Smith</surname>
                    <given-names>John A.</given-names>
                </name>
                <degrees>PhD</degrees>
                <aff id="aff1">Harvard Medical School</aff>
                <email>john.smith@example.com</email>
                <role>Principal Investigator</role>
            </contrib>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Contrib contrib = ArticleMetaParser.parseContrib(reader);

        assertThat(contrib).isNotNull();
        assertThat(contrib.getContribType()).isEqualTo("author");
        assertThat(contrib.getCorresp()).isEqualTo("yes");
        assertThat(contrib.getId()).isEqualTo("author1");
        assertThat(contrib.getDegrees()).hasSize(1);
        assertThat(contrib.getAffiliations()).hasSize(1);
        assertThat(contrib.getEmails()).hasSize(1);
        assertThat(contrib.getRoles()).hasSize(1);
    }

    @Test
    @DisplayName("parseContrib() - collab 및 다양한 요소 포함")
    void testParseContrib_WithCollab() throws Exception {
        String xml = """
            <contrib contrib-type="author">
                <collab>The International Consortium</collab>
                <contrib-id contrib-id-type="orcid">0000-0001-2345-6789</contrib-id>
                <xref ref-type="aff" rid="aff1">1</xref>
                <fn id="fn1">
                    <p>Equal contribution</p>
                </fn>
            </contrib>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Contrib contrib = ArticleMetaParser.parseContrib(reader);

        assertThat(contrib).isNotNull();
        assertThat(contrib.getCollab()).isNotNull();
        assertThat(contrib.getContribIds()).hasSize(1);
        assertThat(contrib.getXrefs()).hasSize(1);
        assertThat(contrib.getFootnotes()).hasSize(1);
    }

    // ==================== parseChemStructWrap (401 instructions) ====================

    @Test
    @DisplayName("parseChemStructWrap() - 기본 chem-struct-wrap 파싱")
    void testParseChemStructWrap_Basic() throws Exception {
        String xml = """
            <chem-struct-wrap id="chem1">
                <label>Chemical Structure 1</label>
                <caption>
                    <title>Benzene Ring</title>
                </caption>
            </chem-struct-wrap>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStructWrap chemWrap = ArticleMetaParser.parseChemStructWrap(reader);

        assertThat(chemWrap).isNotNull();
        assertThat(chemWrap.getId()).isEqualTo("chem1");
        assertThat(chemWrap.getLabel()).isNotNull();
        assertThat(chemWrap.getCaptions()).hasSize(1);
    }

    // ==================== parseChemStruct (279 instructions) ====================

    @Test
    @DisplayName("parseChemStruct() - 기본 chem-struct 파싱")
    void testParseChemStruct_Basic() throws Exception {
        String xml = """
            <chem-struct id="cs1">
                <label>Structure 1</label>
            </chem-struct>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ChemStruct chemStruct = ArticleMetaParser.parseChemStruct(reader);

        assertThat(chemStruct).isNotNull();
        assertThat(chemStruct.getId()).isEqualTo("cs1");
        assertThat(chemStruct.getLabel()).isNotNull();
    }

    // ==================== parseArray (267 instructions) ====================

    @Test
    @DisplayName("parseArray() - 기본 array 파싱")
    void testParseArray_Basic() throws Exception {
        String xml = """
            <array id="arr1">
                <label>Array 1</label>
                <caption>
                    <title>Data Array</title>
                </caption>
            </array>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Array array = ArticleMetaParser.parseArray(reader);

        assertThat(array).isNotNull();
        assertThat(array.getId()).isEqualTo("arr1");
        assertThat(array.getLabel()).isNotNull();
    }

    // ==================== parseAlternatives (184 instructions) ====================

    @Test
    @DisplayName("parseAlternatives() - 기본 alternatives 파싱")
    void testParseAlternatives_Basic() throws Exception {
        String xml = """
            <alternatives xmlns:xlink="http://www.w3.org/1999/xlink" id="alt1">
                <graphic xlink:href="figure1.jpg"/>
                <graphic xlink:href="figure1.png"/>
            </alternatives>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Alternatives alternatives = ArticleMetaParser.parseAlternatives(reader);

        assertThat(alternatives).isNotNull();
        assertThat(alternatives.getId()).isEqualTo("alt1");
        assertThat(alternatives.getGraphics()).hasSize(2);
    }

    // ==================== parsePreformat (144 instructions) ====================

    @Test
    @DisplayName("parsePreformat() - 기본 preformat 파싱")
    void testParsePreformat_Basic() throws Exception {
        String xml = """
            <preformat id="pre1" preformat-type="code">
                public class Example {
                    // code here
                }
            </preformat>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Preformat preformat = ArticleMetaParser.parsePreformat(reader);

        assertThat(preformat).isNotNull();
        assertThat(preformat.getId()).isEqualTo("pre1");
        assertThat(preformat.getPreformatType()).isEqualTo("code");
    }

    // ==================== parseKwdGroup (137 instructions) ====================

    @Test
    @DisplayName("parseKwdGroup() - 기본 kwd-group 파싱")
    void testParseKwdGroup_Basic() throws Exception {
        String xml = """
            <kwd-group kwd-group-type="author">
                <title>Keywords</title>
                <kwd>biology</kwd>
                <kwd>research</kwd>
                <kwd>genetics</kwd>
            </kwd-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        KwdGroup kwdGroup = ArticleMetaParser.parseKwdGroup(reader);

        assertThat(kwdGroup).isNotNull();
        assertThat(kwdGroup.getKwdGroupType()).isEqualTo("author");
        assertThat(kwdGroup.getTitles()).hasSize(1);
        assertThat(kwdGroup.getKeywords()).hasSize(3);
    }

    // ==================== parseCounts (134 instructions) ====================

    @Test
    @DisplayName("parseCounts() - 기본 counts 파싱")
    void testParseCounts_Basic() throws Exception {
        String xml = """
            <counts>
                <fig-count count="5"/>
                <table-count count="3"/>
                <equation-count count="10"/>
                <ref-count count="25"/>
                <page-count count="12"/>
                <word-count count="5000"/>
            </counts>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Counts counts = ArticleMetaParser.parseCounts(reader);

        assertThat(counts).isNotNull();
        assertThat(counts.getFigCount()).isNotNull();
        assertThat(counts.getTableCount()).isNotNull();
        assertThat(counts.getEquationCount()).isNotNull();
        assertThat(counts.getRefCount()).isNotNull();
        assertThat(counts.getPageCount()).isNotNull();
        assertThat(counts.getWordCount()).isNotNull();
    }

    // ==================== parsePermissions (114 instructions) ====================

    @Test
    @DisplayName("parsePermissions() - 기본 permissions 파싱")
    void testParsePermissions_Basic() throws Exception {
        String xml = """
            <permissions>
                <copyright-statement>© 2024 The Authors</copyright-statement>
                <copyright-year>2024</copyright-year>
                <copyright-holder>The Authors</copyright-holder>
                <license license-type="open-access">
                    <license-p>This is an open access article.</license-p>
                </license>
            </permissions>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Permissions permissions = ArticleMetaParser.parsePermissions(reader);

        assertThat(permissions).isNotNull();
        assertThat(permissions.getCopyrightStatements()).hasSize(1);
        assertThat(permissions.getCopyrightYears()).hasSize(1);
        assertThat(permissions.getCopyrightHolders()).hasSize(1);
        assertThat(permissions.getLicenses()).hasSize(1);
    }

    // ==================== parseTitleGroup (107 instructions) ====================

    @Test
    @DisplayName("parseTitleGroup() - 기본 title-group 파싱")
    void testParseTitleGroup_Basic() throws Exception {
        String xml = """
            <title-group>
                <article-title>Main Article Title</article-title>
                <subtitle>A Detailed Subtitle</subtitle>
                <alt-title alt-title-type="short">Short Title</alt-title>
            </title-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        TitleGroup titleGroup = ArticleMetaParser.parseTitleGroup(reader);

        assertThat(titleGroup).isNotNull();
        assertThat(titleGroup.getArticleTitle()).isNotNull();
        assertThat(titleGroup.getSubtitles()).hasSize(1);
        assertThat(titleGroup.getAltTitles()).hasSize(1);
    }

    // ==================== parseContribGroup (102 instructions) ====================

    @Test
    @DisplayName("parseContribGroup() - 기본 contrib-group 파싱")
    void testParseContribGroup_Basic() throws Exception {
        String xml = """
            <contrib-group content-type="authors">
                <contrib contrib-type="author">
                    <name>
                        <surname>Smith</surname>
                        <given-names>John</given-names>
                    </name>
                </contrib>
                <contrib contrib-type="author">
                    <name>
                        <surname>Doe</surname>
                        <given-names>Jane</given-names>
                    </name>
                </contrib>
            </contrib-group>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        ContribGroup contribGroup = ArticleMetaParser.parseContribGroup(reader);

        assertThat(contribGroup).isNotNull();
        assertThat(contribGroup.getContentType()).isEqualTo("authors");
        assertThat(contribGroup.getContributors()).hasSize(2);
    }

    // ==================== parsePmcPubDate (102 instructions) ====================

    @Test
    @DisplayName("parsePmcPubDate() - 기본 pub-date 파싱")
    void testParsePmcPubDate_Basic() throws Exception {
        String xml = """
            <pub-date pub-type="epub" date-type="pub">
                <year>2024</year>
                <month>03</month>
                <day>15</day>
            </pub-date>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        PmcPubDate pubDate = ArticleMetaParser.parsePmcPubDate(reader);

        assertThat(pubDate).isNotNull();
        assertThat(pubDate.getPubType()).isEqualTo("epub");
        assertThat(pubDate.getDateType()).isEqualTo("pub");
        assertThat(pubDate.getYear()).isNotNull();
        assertThat(pubDate.getMonth()).isNotNull();
        assertThat(pubDate.getDay()).isNotNull();
    }
}
