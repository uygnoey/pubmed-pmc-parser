package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ArticleMeta 파서 미커버 코드 테스트 / ArticleMeta Parser Missing Coverage Test
 * <p>
 * KR: ArticleMetaParser의 미커버된 메소드들을 테스트
 * EN: Tests uncovered methods in ArticleMetaParser
 * <p>
 * Coverage Target: ArticleMetaParser 41% → 100%
 */
class ArticleMetaParserMissingCoverageTest {

    /**
     * Test 1: supplementary-material 파싱
     * 가장 큰 미커버 메소드 (629 missed instructions)
     */
    @Test
    void testParseSupplementaryMaterial(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Supplementary Material Test</article-title>
                      </title-group>
                      <supplementary-material id="supp1" mimetype="application/pdf"
                                              content-type="local-data" position="float">
                        <object-id pub-id-type="doi">10.1234/supp1</object-id>
                        <label>Supplementary File 1</label>
                        <caption>
                          <title>Additional Data</title>
                          <p>Complete dataset used in this study</p>
                        </caption>
                        <p>This file contains all raw data.</p>
                        <graphic xmlns:xlink="http://www.w3.org/1999/xlink"
                                 xlink:href="supplement-thumbnail.jpg"/>
                      </supplementary-material>
                    </article-meta>
                  </front>
                </article>
                """;

        Path xmlFile = tempDir.resolve("supplementary_material.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        assertThat(meta.getSupplementaryMaterials()).hasSize(1);

        SupplementaryMaterial supp = meta.getSupplementaryMaterials().get(0);
        assertThat(supp.getId()).isEqualTo("supp1");
        assertThat(supp.getMimetype()).isEqualTo("application/pdf");
        assertThat(supp.getContentType()).isEqualTo("local-data");
        assertThat(supp.getPosition()).isNotNull();

        assertThat(supp.getObjectIds()).hasSize(1);
        assertThat(supp.getObjectIds().get(0).getPubIdType()).isEqualTo(PubIdType.DOI);

        assertThat(supp.getLabel()).isNotNull();
        assertThat(supp.getLabel().getValue()).contains("Supplementary File 1");

        assertThat(supp.getCaptions()).hasSize(1);
        Caption caption = supp.getCaptions().get(0);
        assertThat(caption.getTitle()).isNotNull();
        assertThat(caption.getParagraphs()).hasSize(1);

        assertThat(supp.getParagraphs()).hasSize(1);
        assertThat(supp.getGraphics()).hasSize(1);
    }

    /**
     * Test 2: chem-struct-wrap (화학 구조) 파싱 in supplementary-material
     * 401 missed instructions
     */
    @Test
    void testParseChemStructWrap(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Chemistry Test</article-title>
                      </title-group>
                      <supplementary-material id="supp-chem">
                        <chem-struct-wrap id="chem1">
                          <label>Structure 1</label>
                          <caption>
                            <title>Benzene Ring</title>
                            <p>Aromatic compound structure</p>
                          </caption>
                          <chem-struct>C6H6</chem-struct>
                          <graphic xmlns:xlink="http://www.w3.org/1999/xlink"
                                   xlink:href="benzene.png"/>
                        </chem-struct-wrap>
                      </supplementary-material>
                    </article-meta>
                  </front>
                </article>
                """;

        Path xmlFile = tempDir.resolve("chem_struct.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        assertThat(meta.getSupplementaryMaterials()).hasSize(1);

        SupplementaryMaterial supp = meta.getSupplementaryMaterials().get(0);
        assertThat(supp.getChemStructWraps()).hasSize(1);

        ChemStructWrap chemWrap = supp.getChemStructWraps().get(0);
        assertThat(chemWrap.getId()).isEqualTo("chem1");
        assertThat(chemWrap.getLabel()).isNotNull();
        assertThat(chemWrap.getCaptions()).hasSize(1);
        assertThat(chemWrap.getCaptions().get(0).getTitle()).isNotNull();
        assertThat(chemWrap.getChemStructs()).hasSize(1);
        assertThat(chemWrap.getChemStructs().get(0).getValue()).contains("C6H6");
        assertThat(chemWrap.getGraphics()).hasSize(1);
    }

    /**
     * Test 3: array (배열/표) 파싱 in supplementary-material
     * 267 missed instructions
     */
    @Test
    void testParseArray(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Array Test</article-title>
                      </title-group>
                      <supplementary-material id="supp-array">
                        <array id="arr1">
                          <label>Array 1</label>
                          <tbody>
                            <tr>
                              <td>1.0</td>
                              <td>2.0</td>
                              <td>3.0</td>
                            </tr>
                            <tr>
                              <td>4.0</td>
                              <td>5.0</td>
                              <td>6.0</td>
                            </tr>
                          </tbody>
                        </array>
                      </supplementary-material>
                    </article-meta>
                  </front>
                </article>
                """;

        Path xmlFile = tempDir.resolve("array.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        SupplementaryMaterial supp = meta.getSupplementaryMaterials().get(0);

        assertThat(supp.getArrays()).hasSize(1);
        Array array = supp.getArrays().get(0);
        assertThat(array.getId()).isEqualTo("arr1");
        assertThat(array.getLabel()).isNotNull();
        assertThat(array.getTbodies()).hasSize(1);
        assertThat(array.getTbodies().get(0).getRows()).hasSize(2);
    }

    /**
     * Test 4: fn-group (각주 그룹) 파싱
     * 102 missed instructions
     */
    @Test
    void testParseFnGroup(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Footnote Test</article-title>
                      </title-group>
                      <author-notes>
                        <fn id="fn1" fn-type="con">
                          <label>1</label>
                          <p>Author contribution statement</p>
                        </fn>
                        <fn id="fn2" fn-type="conflict">
                          <label>2</label>
                          <p>No conflicts of interest</p>
                        </fn>
                      </author-notes>
                    </article-meta>
                  </front>
                </article>
                """;

        Path xmlFile = tempDir.resolve("fn_group.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        assertThat(meta.getAuthorNotes()).isNotNull();
        assertThat(meta.getAuthorNotes().getFootnotes()).hasSize(2);

        Fn fn1 = meta.getAuthorNotes().getFootnotes().get(0);
        assertThat(fn1.getId()).isEqualTo("fn1");
        assertThat(fn1.getFnType()).isEqualTo("con");
        assertThat(fn1.getLabel()).isNotNull();
        assertThat(fn1.getParagraphs()).hasSize(1);

        Fn fn2 = meta.getAuthorNotes().getFootnotes().get(1);
        assertThat(fn2.getId()).isEqualTo("fn2");
        assertThat(fn2.getFnType()).isEqualTo("conflict");
    }

    /**
     * Test 5: disp-formula (표시 수식) 파싱 in supplementary-material
     * 73 missed instructions
     */
    @Test
    void testParseDispFormula(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Formula Test</article-title>
                      </title-group>
                      <supplementary-material id="supp-formula">
                        <disp-formula id="eq1">
                          <label>(1)</label>
                          <mml:math xmlns:mml="http://www.w3.org/1998/Math/MathML">
                            <mml:mrow>
                              <mml:mi>E</mml:mi>
                              <mml:mo>=</mml:mo>
                              <mml:mi>m</mml:mi>
                              <mml:msup>
                                <mml:mi>c</mml:mi>
                                <mml:mn>2</mml:mn>
                              </mml:msup>
                            </mml:mrow>
                          </mml:math>
                        </disp-formula>
                      </supplementary-material>
                    </article-meta>
                  </front>
                </article>
                """;

        Path xmlFile = tempDir.resolve("disp_formula.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        SupplementaryMaterial supp = meta.getSupplementaryMaterials().get(0);

        assertThat(supp.getDispFormulas()).hasSize(1);
        DispFormula formula = supp.getDispFormulas().get(0);
        assertThat(formula.getId()).isEqualTo("eq1");
        assertThat(formula.getLabel()).isNotNull();
        assertThat(formula.getMmlMath()).isNotNull();
    }

    /**
     * Test 6: alternatives (대체 표현) 파싱 in supplementary-material
     * 184 missed instructions
     */
    @Test
    void testParseAlternatives(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Alternatives Test</article-title>
                      </title-group>
                      <supplementary-material id="supp-alt">
                        <alternatives id="alt1">
                          <graphic xmlns:xlink="http://www.w3.org/1999/xlink"
                                   xlink:href="figure.png" mimetype="image/png"/>
                          <graphic xmlns:xlink="http://www.w3.org/1999/xlink"
                                   xlink:href="figure.svg" mimetype="image/svg+xml"/>
                        </alternatives>
                      </supplementary-material>
                    </article-meta>
                  </front>
                </article>
                """;

        Path xmlFile = tempDir.resolve("alternatives.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        SupplementaryMaterial supp = meta.getSupplementaryMaterials().get(0);

        assertThat(supp.getAlternatives()).hasSize(1);
        Alternatives alt = supp.getAlternatives().get(0);
        assertThat(alt.getId()).isEqualTo("alt1");
        assertThat(alt.getGraphics()).hasSize(2);
        assertThat(alt.getGraphics().get(0).getMimetype()).isEqualTo("image/png");
        assertThat(alt.getGraphics().get(1).getMimetype()).isEqualTo("image/svg+xml");
    }

    /**
     * Test 7: preformat (사전 포맷 텍스트) 파싱 in supplementary-material
     * 144 missed instructions
     */
    @Test
    void testParsePreformat(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Preformat Test</article-title>
                      </title-group>
                      <supplementary-material id="supp-pre">
                        <preformat id="pre1" position="anchor">
function hello() {
    console.log("Hello, World!");
}
                        </preformat>
                      </supplementary-material>
                    </article-meta>
                  </front>
                </article>
                """.stripLeading();

        Path xmlFile = tempDir.resolve("preformat.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        SupplementaryMaterial supp = meta.getSupplementaryMaterials().get(0);

        assertThat(supp.getPreformats()).hasSize(1);
        Preformat pre = supp.getPreformats().get(0);
        assertThat(pre.getId()).isEqualTo("pre1");
        assertThat(pre.getPosition()).isNotNull();
        assertThat(pre.getContent()).contains("function hello");
    }

    /**
     * Test 8: pub-history (출판 이력) 파싱 via ArticleMeta
     * 84 missed instructions
     */
    @Test
    void testParsePubHistory(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Pub History Test</article-title>
                      </title-group>
                      <pub-history>
                        <event event-type="received">
                          <date date-type="received" iso-8601-date="2024-01-15">
                            <day>15</day>
                            <month>01</month>
                            <year>2024</year>
                          </date>
                        </event>
                        <event event-type="accepted">
                          <date date-type="accepted" iso-8601-date="2024-03-20">
                            <day>20</day>
                            <month>03</month>
                            <year>2024</year>
                          </date>
                        </event>
                      </pub-history>
                    </article-meta>
                  </front>
                </article>
                """;

        Path xmlFile = tempDir.resolve("pub_history.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        assertThat(meta.getPubHistory()).isNotNull();

        PubHistory pubHistory = meta.getPubHistory();
        assertThat(pubHistory.getEvents()).hasSize(2);

        Event received = pubHistory.getEvents().get(0);
        assertThat(received.getEventType()).isEqualTo("received");
        assertThat(received.getDates()).hasSize(1);
        assertThat(received.getDates().get(0).getDay().getValue()).isEqualTo("15");

        Event accepted = pubHistory.getEvents().get(1);
        assertThat(accepted.getEventType()).isEqualTo("accepted");
        assertThat(accepted.getDates().get(0).getMonth().getValue()).isEqualTo("03");
    }

    /**
     * Test 9: contrib with multiple affiliations
     * parseContrib 79% → 100%
     */
    @Test
    void testParseContribWithMultipleAffiliations(@TempDir Path tempDir) throws Exception {
        String xml = """
                <?xml version="1.0"?>
                <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                    "JATS-archivearticle1-4.dtd">
                <article dtd-version="1.4">
                  <front>
                    <article-meta>
                      <title-group>
                        <article-title>Multi-Affiliation Test</article-title>
                      </title-group>
                      <contrib-group>
                        <contrib contrib-type="author">
                          <name>
                            <surname>Smith</surname>
                            <given-names>John</given-names>
                          </name>
                          <xref ref-type="aff" rid="aff1">1</xref>
                          <xref ref-type="aff" rid="aff2">2</xref>
                          <aff id="aff-inline">
                            <label>*</label>
                            <addr-line>Current address: New York, USA</addr-line>
                          </aff>
                        </contrib>
                      </contrib-group>
                    </article-meta>
                  </front>
                </article>
                """;

        Path xmlFile = tempDir.resolve("multi_aff.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        ArticleMeta meta = article.getFront().getArticleMeta();
        ContribGroup group = meta.getContribGroups().get(0);
        Contrib contrib = group.getContributors().get(0);

        assertThat(contrib.getName()).isNotNull();
        assertThat(contrib.getName().getSurname().getValue()).isEqualTo("Smith");
        assertThat(contrib.getXrefs()).hasSize(2);
        assertThat(contrib.getAffiliations()).hasSize(1);
        assertThat(contrib.getAffiliations().get(0).getAddrLines()).hasSize(1);
    }
}
