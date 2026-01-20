package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ArticleMetaParser의 branch coverage 향상을 위한 테스트
 * <p>
 * 목표: 다양한 조건문 경로를 테스트하여 branch coverage를 높임
 */
@DisplayName("ArticleMetaParser Branch Coverage Tests")
class ArticleMetaParserBranchTest {

    private static XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(xml));
        reader.next(); // Move to START_ELEMENT
        return reader;
    }

    @Test
    @DisplayName("parseSupplementaryMaterial() - 다양한 자식 요소 포함 (branch coverage 향상)")
    void testParseSupplementaryMaterial_ComprehensiveChildren() throws Exception {
        String xml = """
            <supplementary-material xmlns:xlink="http://www.w3.org/1999/xlink" id="supp1" content-type="data">
                <object-id pub-id-type="doi">10.1234/data1</object-id>
                <label>Supplementary Data</label>
                <caption>
                    <title>Additional Results</title>
                </caption>
                <abstract>
                    <p>Supplementary data description</p>
                </abstract>
                <kwd-group>
                    <kwd>dataset</kwd>
                </kwd-group>
                <alt-text>Alternative text</alt-text>
                <long-desc>Long description</long-desc>
                <email>data@example.com</email>
                <ext-link ext-link-type="uri" xlink:href="http://example.com">External Link</ext-link>
                <uri>http://example.com/data</uri>
                <p>Paragraph content</p>
                <def-list>
                    <def-item>
                        <term>Term1</term>
                        <def><p>Definition1</p></def>
                    </def-item>
                </def-list>
                <list>
                    <list-item><p>Item 1</p></list-item>
                </list>
                <code>sample code</code>
                <graphic xlink:href="figure.jpg"/>
                <media mimetype="video" xlink:href="video.mp4"/>
                <attrib>Attribution text</attrib>
                <permissions>
                    <copyright-statement>Copyright 2024</copyright-statement>
                </permissions>
            </supplementary-material>
            """;
        XMLStreamReader reader = createReader(xml);

        SupplementaryMaterial material = ArticleMetaParser.parseSupplementaryMaterial(reader);

        assertThat(material).isNotNull();
        assertThat(material.getId()).isEqualTo("supp1");
        assertThat(material.getContentType()).isEqualTo("data");
        assertThat(material.getObjectIds()).hasSize(1);
        assertThat(material.getLabel()).isNotNull();
        assertThat(material.getCaptions()).hasSize(1);
        assertThat(material.getAbstracts()).hasSize(1);
        assertThat(material.getKwdGroups()).hasSize(1);
        assertThat(material.getAltTexts()).hasSize(1);
        assertThat(material.getLongDescs()).hasSize(1);
        assertThat(material.getEmails()).hasSize(1);
        assertThat(material.getExtLinks()).hasSize(1);
        assertThat(material.getUris()).hasSize(1);
        assertThat(material.getParagraphs()).hasSize(1);
        assertThat(material.getDefLists()).hasSize(1);
        assertThat(material.getLists()).hasSize(1);
        assertThat(material.getCodes()).hasSize(1);
        assertThat(material.getGraphics()).hasSize(1);
        assertThat(material.getMedias()).hasSize(1);
        assertThat(material.getAttribs()).hasSize(1);
        assertThat(material.getPermissions()).hasSize(1);
    }

    @Test
    @DisplayName("parseSupplementaryMaterial() - 수식 및 화학 구조 포함")
    void testParseSupplementaryMaterial_WithFormulasAndChem() throws Exception {
        String xml = """
            <supplementary-material xmlns:xlink="http://www.w3.org/1999/xlink" id="supp2">
                <disp-formula id="eq1">
                    <label>Eq. 1</label>
                </disp-formula>
                <disp-formula-group>
                    <disp-formula id="eq2">
                        <label>Eq. 2</label>
                    </disp-formula>
                </disp-formula-group>
                <chem-struct-wrap>
                    <chem-struct>Chemical structure</chem-struct>
                </chem-struct-wrap>
            </supplementary-material>
            """;
        XMLStreamReader reader = createReader(xml);

        SupplementaryMaterial material = ArticleMetaParser.parseSupplementaryMaterial(reader);

        assertThat(material).isNotNull();
        assertThat(material.getDispFormulas()).hasSize(1);
        assertThat(material.getDispFormulaGroups()).hasSize(1);
        assertThat(material.getChemStructWraps()).hasSize(1);
    }

    @Test
    @DisplayName("parseSupplementaryMaterial() - 테이블 및 텍스트 요소 포함")
    void testParseSupplementaryMaterial_WithTablesAndText() throws Exception {
        String xml = """
            <supplementary-material xmlns:xlink="http://www.w3.org/1999/xlink" id="supp3">
                <alternatives>
                    <graphic xlink:href="alt1.jpg"/>
                    <graphic xlink:href="alt2.jpg"/>
                </alternatives>
                <table-wrap>
                    <table>
                        <tr><td>Data</td></tr>
                    </table>
                </table-wrap>
                <array>
                    <tbody>
                        <tr><td>Array data</td></tr>
                    </tbody>
                </array>
                <disp-quote>
                    <p>Quoted text</p>
                </disp-quote>
                <speech>
                    <speaker>Speaker</speaker>
                    <p>Speech content</p>
                </speech>
                <statement>
                    <p>Statement content</p>
                </statement>
                <verse-group>
                    <verse-line>Verse line</verse-line>
                </verse-group>
                <preformat>Preformatted text</preformat>
            </supplementary-material>
            """;
        XMLStreamReader reader = createReader(xml);

        SupplementaryMaterial material = ArticleMetaParser.parseSupplementaryMaterial(reader);

        assertThat(material).isNotNull();
        assertThat(material.getAlternatives()).hasSize(1);
        assertThat(material.getTableWraps()).hasSize(1);
        assertThat(material.getArrays()).hasSize(1);
        assertThat(material.getDispQuotes()).hasSize(1);
        assertThat(material.getSpeeches()).hasSize(1);
        assertThat(material.getStatements()).hasSize(1);
        assertThat(material.getVerseGroups()).hasSize(1);
        assertThat(material.getPreformats()).hasSize(1);
    }

    @Test
    @DisplayName("parseChemStructWrap() - 다양한 자식 요소 포함 (branch coverage 향상)")
    void testParseChemStructWrap_ComprehensiveChildren() throws Exception {
        String xml = """
            <chem-struct-wrap xmlns:xlink="http://www.w3.org/1999/xlink" id="chem1">
                <object-id pub-id-type="chem">CHEM001</object-id>
                <label>Chemical 1</label>
                <caption>
                    <title>Chemical Structure</title>
                </caption>
                <abstract>
                    <p>Chemical description</p>
                </abstract>
                <kwd-group>
                    <kwd>organic</kwd>
                </kwd-group>
                <alt-text>Alt text</alt-text>
                <long-desc>Long description</long-desc>
                <email>chem@example.com</email>
                <ext-link ext-link-type="uri" xlink:href="http://example.com">Link</ext-link>
                <uri>http://example.com/chem</uri>
                <chem-struct>C6H12O6</chem-struct>
                <alternatives>
                    <graphic xlink:href="alt1.jpg"/>
                    <graphic xlink:href="alt2.jpg"/>
                </alternatives>
                <code>chemical notation</code>
                <graphic xlink:href="chem.jpg"/>
                <media mimetype="image" xlink:href="chem.png"/>
                <preformat>preformatted chem</preformat>
                <textual-form>Textual representation</textual-form>
                <attrib>Source</attrib>
                <permissions>
                    <copyright-statement>Copyright 2024</copyright-statement>
                </permissions>
            </chem-struct-wrap>
            """;
        XMLStreamReader reader = createReader(xml);

        ChemStructWrap wrap = ArticleMetaParser.parseChemStructWrap(reader);

        assertThat(wrap).isNotNull();
        assertThat(wrap.getId()).isEqualTo("chem1");
        assertThat(wrap.getObjectIds()).hasSize(1);
        assertThat(wrap.getLabel()).isNotNull();
        assertThat(wrap.getCaptions()).hasSize(1);
        assertThat(wrap.getAbstracts()).hasSize(1);
        assertThat(wrap.getKwdGroups()).hasSize(1);
        assertThat(wrap.getAltTexts()).hasSize(1);
        assertThat(wrap.getLongDescs()).hasSize(1);
        assertThat(wrap.getEmails()).hasSize(1);
        assertThat(wrap.getExtLinks()).hasSize(1);
        assertThat(wrap.getUris()).hasSize(1);
        assertThat(wrap.getChemStructs()).isNotEmpty();
        assertThat(wrap.getAlternatives()).hasSize(1);
        assertThat(wrap.getCodes()).hasSize(1);
        assertThat(wrap.getGraphics()).hasSize(1);
        assertThat(wrap.getMedias()).hasSize(1);
        assertThat(wrap.getPreformats()).hasSize(1);
        assertThat(wrap.getTextualForms()).hasSize(1);
        assertThat(wrap.getAttribs()).hasSize(1);
        assertThat(wrap.getPermissions()).hasSize(1);
    }

    @Test
    @DisplayName("parseArray() - 다양한 자식 요소 포함 (branch coverage 향상)")
    void testParseArray_ComprehensiveChildren() throws Exception {
        String xml = """
            <array xmlns:xlink="http://www.w3.org/1999/xlink" id="arr1" content-type="data">
                <label>Array 1</label>
                <alt-text>Array alt text</alt-text>
                <long-desc>Array description</long-desc>
                <email>array@example.com</email>
                <ext-link ext-link-type="uri" xlink:href="http://example.com">Link</ext-link>
                <uri>http://example.com/array</uri>
                <alternatives>
                    <graphic xlink:href="array1.jpg"/>
                    <graphic xlink:href="array2.jpg"/>
                </alternatives>
                <graphic xlink:href="array.jpg"/>
                <media mimetype="image" xlink:href="array.png"/>
                <tbody>
                    <tr>
                        <td>Data 1</td>
                        <td>Data 2</td>
                    </tr>
                </tbody>
                <attrib>Attribution</attrib>
                <permissions>
                    <copyright-statement>Copyright 2024</copyright-statement>
                </permissions>
            </array>
            """;
        XMLStreamReader reader = createReader(xml);

        Array array = ArticleMetaParser.parseArray(reader);

        assertThat(array).isNotNull();
        assertThat(array.getId()).isEqualTo("arr1");
        assertThat(array.getContentType()).isEqualTo("data");
        assertThat(array.getLabel()).isNotNull();
        assertThat(array.getAltTexts()).hasSize(1);
        assertThat(array.getLongDescs()).hasSize(1);
        assertThat(array.getEmails()).hasSize(1);
        assertThat(array.getExtLinks()).hasSize(1);
        assertThat(array.getUris()).hasSize(1);
        assertThat(array.getAlternatives()).hasSize(1);
        assertThat(array.getGraphics()).hasSize(1);
        assertThat(array.getMedias()).hasSize(1);
        assertThat(array.getTbodies()).hasSize(1);
        assertThat(array.getAttribs()).hasSize(1);
        assertThat(array.getPermissions()).hasSize(1);
    }

    @Test
    @DisplayName("parseAlternatives() - 다양한 대안 요소 포함 (branch coverage 향상)")
    void testParseAlternatives_ComprehensiveAlternatives() throws Exception {
        String xml = """
            <alternatives xmlns:xlink="http://www.w3.org/1999/xlink" id="alt1">
                <object-id pub-id-type="alt">ALT001</object-id>
                <table>
                    <tr><td>Table</td></tr>
                </table>
                <graphic xlink:href="alt1.jpg"/>
                <graphic xlink:href="alt2.jpg"/>
                <media mimetype="video" xlink:href="alt.mp4"/>
                <preformat>Preformatted alternative</preformat>
                <array>
                    <tbody>
                        <tr><td>Array</td></tr>
                    </tbody>
                </array>
                <code>code alternative</code>
                <supplementary-material id="supp1">
                    <label>Supplement</label>
                </supplementary-material>
            </alternatives>
            """;
        XMLStreamReader reader = createReader(xml);

        Alternatives alternatives = ArticleMetaParser.parseAlternatives(reader);

        assertThat(alternatives).isNotNull();
        assertThat(alternatives.getId()).isEqualTo("alt1");
        assertThat(alternatives.getObjectIds()).hasSize(1);
        assertThat(alternatives.getTables()).hasSize(1);
        assertThat(alternatives.getGraphics()).hasSize(2);
        assertThat(alternatives.getMedias()).hasSize(1);
        assertThat(alternatives.getPreformats()).hasSize(1);
        assertThat(alternatives.getArrays()).hasSize(1);
        assertThat(alternatives.getCodes()).hasSize(1);
        assertThat(alternatives.getSupplementaryMaterials()).hasSize(1);
    }

    @Test
    @DisplayName("parseArticleMeta() - 다양한 메타데이터 요소 포함 (branch coverage 향상)")
    void testParseArticleMeta_ComprehensiveMetadata() throws Exception {
        String xml = """
            <article-meta xmlns:xlink="http://www.w3.org/1999/xlink">
                <article-id pub-id-type="pmid">12345678</article-id>
                <article-id pub-id-type="doi">10.1234/example</article-id>
                <article-categories>
                    <subj-group>
                        <subject>Research Article</subject>
                    </subj-group>
                </article-categories>
                <title-group>
                    <article-title>Test Article</article-title>
                </title-group>
                <contrib-group>
                    <contrib>
                        <name>
                            <surname>Doe</surname>
                            <given-names>John</given-names>
                        </name>
                    </contrib>
                </contrib-group>
                <aff id="aff1">Institution</aff>
                <author-notes>
                    <corresp id="cor1">Correspondence</corresp>
                </author-notes>
                <pub-date pub-type="epub">
                    <day>15</day>
                    <month>01</month>
                    <year>2024</year>
                </pub-date>
                <volume>10</volume>
                <issue>5</issue>
                <fpage>100</fpage>
                <lpage>120</lpage>
                <history>
                    <date date-type="received">
                        <day>01</day>
                        <month>01</month>
                        <year>2024</year>
                    </date>
                </history>
                <permissions>
                    <copyright-statement>Copyright 2024</copyright-statement>
                    <copyright-year>2024</copyright-year>
                    <copyright-holder>Authors</copyright-holder>
                </permissions>
                <self-uri xlink:href="http://example.com/article"/>
                <related-article related-article-type="companion" xlink:href="http://example.com/related"/>
                <abstract>
                    <p>Abstract text</p>
                </abstract>
                <trans-abstract xml:lang="ko">
                    <p>Korean abstract</p>
                </trans-abstract>
                <kwd-group>
                    <kwd>keyword1</kwd>
                    <kwd>keyword2</kwd>
                </kwd-group>
                <funding-group>
                    <award-group>
                        <funding-source>Grant Agency</funding-source>
                        <award-id>12345</award-id>
                    </award-group>
                </funding-group>
                <conference>
                    <conf-name>Conference 2024</conf-name>
                </conference>
                <counts>
                    <fig-count count="5"/>
                    <table-count count="3"/>
                    <ref-count count="20"/>
                </counts>
                <custom-meta-group>
                    <custom-meta>
                        <meta-name>custom</meta-name>
                        <meta-value>value</meta-value>
                    </custom-meta>
                </custom-meta-group>
            </article-meta>
            """;
        XMLStreamReader reader = createReader(xml);

        ArticleMeta meta = ArticleMetaParser.parseArticleMeta(reader);

        assertThat(meta).isNotNull();
        assertThat(meta.getArticleIds()).hasSize(2);
        assertThat(meta.getArticleCategories()).isNotNull();
        assertThat(meta.getTitleGroup()).isNotNull();
        assertThat(meta.getContribGroups()).hasSize(1);
        assertThat(meta.getAffiliations()).hasSize(1);
        assertThat(meta.getAuthorNotes()).isNotNull();
        assertThat(meta.getPubDates()).hasSize(1);
        assertThat(meta.getVolume()).isNotNull();
        assertThat(meta.getIssue()).isNotNull();
        assertThat(meta.getFpage()).isNotNull();
        assertThat(meta.getLpage()).isNotNull();
        assertThat(meta.getHistory()).isNotNull();
        assertThat(meta.getPermissions()).isNotNull();
        assertThat(meta.getSelfUris()).hasSize(1);
        assertThat(meta.getRelatedArticles()).hasSize(1);
        assertThat(meta.getAbstracts()).hasSize(1);
        assertThat(meta.getTransAbstracts()).hasSize(1);
        assertThat(meta.getKwdGroups()).hasSize(1);
        assertThat(meta.getFundingGroups()).hasSize(1);
        assertThat(meta.getConferences()).hasSize(1);
        assertThat(meta.getCounts()).isNotNull();
        assertThat(meta.getCustomMetaGroup()).isNotNull();
    }

    @Test
    @DisplayName("parseContrib() - 다양한 contributor 정보 포함 (branch coverage 향상)")
    void testParseContrib_ComprehensiveInfo() throws Exception {
        String xml = """
            <contrib contrib-type="author" corresp="yes">
                <contrib-id contrib-id-type="orcid">0000-0001-2345-6789</contrib-id>
                <name>
                    <surname>Smith</surname>
                    <given-names>Jane</given-names>
                </name>
                <degrees>PhD</degrees>
                <address>
                    <addr-line>123 Main St</addr-line>
                </address>
                <aff id="aff2">Department</aff>
                <author-comment>
                    <p>Comment</p>
                </author-comment>
                <bio>
                    <p>Biography</p>
                </bio>
                <email>jane@example.com</email>
                <ext-link ext-link-type="uri" xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="http://example.com">Link</ext-link>
                <uri>http://example.com</uri>
                <on-behalf-of>On behalf of group</on-behalf-of>
                <role>Principal Investigator</role>
                <xref ref-type="aff" rid="aff1">1</xref>
                <xref ref-type="corresp" rid="cor1">*</xref>
                <fn id="fn1">
                    <p>Footnote</p>
                </fn>
            </contrib>
            """;
        XMLStreamReader reader = createReader(xml);

        Contrib contrib = ArticleMetaParser.parseContrib(reader);

        assertThat(contrib).isNotNull();
        assertThat(contrib.getContribType()).isEqualTo("author");
        assertThat(contrib.getCorresp()).isEqualTo("yes");
        assertThat(contrib.getContribIds()).hasSize(1);
        assertThat(contrib.getName()).isNotNull();
        assertThat(contrib.getDegrees()).hasSize(1);
        assertThat(contrib.getAddresses()).hasSize(1);
        assertThat(contrib.getAffiliations()).hasSize(1);
        assertThat(contrib.getAuthorComment()).isNotNull();
        assertThat(contrib.getBio()).isNotNull();
        assertThat(contrib.getEmails()).hasSize(1);
        assertThat(contrib.getExtLinks()).hasSize(1);
        assertThat(contrib.getUris()).hasSize(1);
        assertThat(contrib.getOnBehalfOf()).isNotNull();
        assertThat(contrib.getRoles()).hasSize(1);
        assertThat(contrib.getXrefs()).hasSize(2);
        assertThat(contrib.getFootnotes()).hasSize(1);
    }
}
