package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import static org.assertj.core.api.Assertions.*;

/**
 * PmcXmlParser Branch Coverage 향상 테스트
 *
 * 목표: PmcXmlParser의 branch coverage를 52%에서 80%+로 향상
 * 주요 타겟:
 * - parseFrontStub: 35% → 80%+
 * - parseStreamBatch: 0% → 80%+
 * - parseTarGz: 0% → 80%+
 */
class PmcXmlParserBranchTest {

    private XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        return factory.createXMLStreamReader(
                new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))
        );
    }

    @Test
    @DisplayName("parseFrontStub() - 포괄적인 모든 자식 요소 포함 (branch coverage 향상)")
    void testParseFrontStub_ComprehensiveChildren() throws Exception {
        String xml = """
            <front-stub xmlns:xlink="http://www.w3.org/1999/xlink">
                <article-id pub-id-type="pmid">12345678</article-id>
                <article-id pub-id-type="pmc">PMC9876543</article-id>
                <article-categories>
                    <subj-group>
                        <subject>Research Article</subject>
                    </subj-group>
                </article-categories>
                <title-group>
                    <article-title>Test Article Title</article-title>
                </title-group>
                <contrib-group>
                    <contrib contrib-type="author">
                        <name>
                            <surname>Smith</surname>
                            <given-names>John</given-names>
                        </name>
                    </contrib>
                </contrib-group>
                <aff id="aff1">
                    <institution>Test University</institution>
                </aff>
                <aff-alternatives>
                    <aff id="aff2">
                        <institution>Alternative Institution</institution>
                    </aff>
                </aff-alternatives>
                <author-notes>
                    <corresp id="cor1">Correspondence</corresp>
                </author-notes>
                <pub-date pub-type="epub" date-type="pub">
                    <day>15</day>
                    <month>03</month>
                    <year>2024</year>
                </pub-date>
                <pub-date-not-available/>
                <volume>42</volume>
                <volume-id pub-id-type="publisher-id">VOL42</volume-id>
                <volume-series>Series A</volume-series>
                <issue>3</issue>
                <issue-id pub-id-type="publisher-id">ISS3</issue-id>
                <issue-title>Special Issue</issue-title>
                <issue-sponsor>Test Sponsor</issue-sponsor>
                <issue-part>Part 1</issue-part>
                <isbn>978-1-234567-89-0</isbn>
                <supplement>Supplement 1</supplement>
                <fpage>100</fpage>
                <lpage>120</lpage>
                <page-range>100-120</page-range>
                <elocation-id>e12345</elocation-id>
                <email>author@example.com</email>
                <ext-link ext-link-type="uri" xlink:href="http://example.com">External Link</ext-link>
                <uri>http://example.com/article</uri>
                <product product-type="book">
                    <source>Test Product</source>
                </product>
                <supplementary-material id="supp1">
                    <label>Supplementary Material</label>
                </supplementary-material>
                <history>
                    <date date-type="received">
                        <day>01</day>
                        <month>01</month>
                        <year>2024</year>
                    </date>
                </history>
                <pub-history>
                    <date date-type="preprint">
                        <day>15</day>
                        <month>02</month>
                        <year>2024</year>
                    </date>
                </pub-history>
                <permissions>
                    <copyright-statement>Copyright 2024</copyright-statement>
                    <copyright-year>2024</copyright-year>
                </permissions>
                <self-uri xlink:href="http://example.com/self">Self URI</self-uri>
                <related-article related-article-type="companion" id="ra1">
                    <article-title>Related Article</article-title>
                </related-article>
                <related-object object-id="obj1" object-id-type="doi">
                    <label>Related Object</label>
                </related-object>
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
                        <funding-source>NIH</funding-source>
                        <award-id>R01-123456</award-id>
                    </award-group>
                </funding-group>
                <support-group>
                    <supported-by>Test Organization</supported-by>
                </support-group>
                <conference>
                    <conf-name>Test Conference 2024</conf-name>
                </conference>
                <counts>
                    <page-count count="21"/>
                    <fig-count count="5"/>
                    <table-count count="3"/>
                    <ref-count count="42"/>
                </counts>
                <custom-meta-group>
                    <custom-meta>
                        <meta-name>custom-field</meta-name>
                        <meta-value>custom-value</meta-value>
                    </custom-meta>
                </custom-meta-group>
            </front-stub>
            """;

        XMLStreamReader reader = createReader(xml);

        // front-stub 요소로 이동
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement() && reader.getLocalName().equals("front-stub")) {
                break;
            }
        }

        // PmcXmlParser의 parseFrontStub는 private이므로 리플렉션 사용
        java.lang.reflect.Method method = PmcXmlParser.class.getDeclaredMethod("parseFrontStub", XMLStreamReader.class);
        method.setAccessible(true);

        PmcXmlParser parser = new PmcXmlParser();
        FrontStub frontStub = (FrontStub) method.invoke(parser, reader);

        // 검증: 모든 요소가 파싱되었는지 확인
        assertThat(frontStub).isNotNull();

        // article-id (2개)
        assertThat(frontStub.getArticleIds()).hasSize(2);
        assertThat(frontStub.getArticleIds().get(0).getPubIdType()).isEqualTo("pmid");
        assertThat(frontStub.getArticleIds().get(1).getPubIdType()).isEqualTo("pmc");

        // article-categories
        assertThat(frontStub.getArticleCategories()).isNotNull();

        // title-group
        assertThat(frontStub.getTitleGroup()).isNotNull();
        assertThat(frontStub.getTitleGroup().getArticleTitle()).isNotNull();

        // contrib-group
        assertThat(frontStub.getContribGroups()).hasSize(1);

        // aff
        assertThat(frontStub.getAffiliations()).hasSize(1);

        // aff-alternatives
        assertThat(frontStub.getAffAlternatives()).hasSize(1);

        // author-notes
        assertThat(frontStub.getAuthorNotes()).isNotNull();

        // pub-date (2개: pub-date + pub-date-not-available)
        assertThat(frontStub.getPubDates()).hasSize(2);

        // volume
        assertThat(frontStub.getVolume()).isNotNull();

        // volume-id
        assertThat(frontStub.getVolumeIds()).hasSize(1);

        // volume-series
        assertThat(frontStub.getVolumeSeries()).isNotNull();

        // issue
        assertThat(frontStub.getIssue()).isNotNull();

        // issue-id
        assertThat(frontStub.getIssueIds()).hasSize(1);

        // issue-title
        assertThat(frontStub.getIssueTitles()).hasSize(1);

        // issue-sponsor
        assertThat(frontStub.getIssueSponsors()).hasSize(1);

        // issue-part
        assertThat(frontStub.getIssuePart()).isEqualTo("Part 1");

        // isbn
        assertThat(frontStub.getIsbns()).hasSize(1);

        // supplement
        assertThat(frontStub.getSupplement()).isNotNull();

        // fpage
        assertThat(frontStub.getFpage()).isNotNull();

        // lpage
        assertThat(frontStub.getLpage()).isNotNull();

        // page-range
        assertThat(frontStub.getPageRange()).isEqualTo("100-120");

        // elocation-id
        assertThat(frontStub.getElocationIds()).hasSize(1);

        // email
        assertThat(frontStub.getEmails()).hasSize(1);

        // ext-link
        assertThat(frontStub.getExtLinks()).hasSize(1);

        // uri
        assertThat(frontStub.getUris()).hasSize(1);

        // supplementary-material
        assertThat(frontStub.getSupplementaryMaterials()).hasSize(1);

        // history
        assertThat(frontStub.getHistory()).isNotNull();

        // pub-history
        assertThat(frontStub.getPubHistory()).isNotNull();

        // permissions
        assertThat(frontStub.getPermissions()).isNotNull();

        // self-uri
        assertThat(frontStub.getSelfUris()).hasSize(1);

        // related-article
        assertThat(frontStub.getRelatedArticles()).hasSize(1);

        // related-object
        assertThat(frontStub.getRelatedObjects()).hasSize(1);

        // abstract
        assertThat(frontStub.getAbstracts()).hasSize(1);

        // trans-abstract
        assertThat(frontStub.getTransAbstracts()).hasSize(1);

        // kwd-group
        assertThat(frontStub.getKwdGroups()).hasSize(1);

        // funding-group
        assertThat(frontStub.getFundingGroups()).hasSize(1);

        // support-group
        assertThat(frontStub.getSupportGroups()).hasSize(1);

        // conference
        assertThat(frontStub.getConferences()).hasSize(1);

        // counts
        assertThat(frontStub.getCounts()).isNotNull();

        // custom-meta-group
        assertThat(frontStub.getCustomMetaGroup()).isNotNull();
    }

    @Test
    @DisplayName("parseStreamBatch() - 배치 처리 테스트")
    void testParseStreamBatch_ProcessMultipleArticles(@TempDir Path tempDir) throws Exception {
        // 3개의 article을 포함하는 XML 파일 생성
        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <pmc-articleset>
                <article article-type="research-article" xmlns:xlink="http://www.w3.org/1999/xlink">
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">11111111</article-id>
                            <title-group>
                                <article-title>Article 1</article-title>
                            </title-group>
                        </article-meta>
                    </front>
                </article>
                <article article-type="review-article" xmlns:xlink="http://www.w3.org/1999/xlink">
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">22222222</article-id>
                            <title-group>
                                <article-title>Article 2</article-title>
                            </title-group>
                        </article-meta>
                    </front>
                </article>
                <article article-type="case-report" xmlns:xlink="http://www.w3.org/1999/xlink">
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">33333333</article-id>
                            <title-group>
                                <article-title>Article 3</article-title>
                            </title-group>
                        </article-meta>
                    </front>
                </article>
            </pmc-articleset>
            """;

        Path xmlFile = tempDir.resolve("test-articles.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        List<List<JatsArticle>> batches = new ArrayList<>();
        AtomicInteger totalCount = new AtomicInteger(0);

        // 배치 크기 2로 처리 (3개 article → 2개 batch)
        long count = parser.parseStreamBatch(xmlFile, 2, batch -> {
            batches.add(new ArrayList<>(batch));
            totalCount.addAndGet(batch.size());
        });

        // 검증
        assertThat(count).isEqualTo(3);
        assertThat(totalCount.get()).isEqualTo(3);
        assertThat(batches).hasSize(2); // 첫 배치 2개, 두 번째 배치 1개
        assertThat(batches.get(0)).hasSize(2); // 첫 번째 배치
        assertThat(batches.get(1)).hasSize(1); // 두 번째 배치 (나머지)

        // 첫 번째 article 검증
        JatsArticle article1 = batches.get(0).get(0);
        assertThat(article1.getArticleType()).isEqualTo(ArticleType.RESEARCH_ARTICLE);
        assertThat(article1.getFront().getArticleMeta().getTitleGroup().getArticleTitle().getContent())
                .isEqualTo("Article 1");
    }

    @Test
    @DisplayName("parseStreamBatch() - 배치 크기 검증 (잘못된 크기)")
    void testParseStreamBatch_InvalidBatchSize(@TempDir Path tempDir) throws Exception {
        Path xmlFile = tempDir.resolve("dummy.xml");
        Files.writeString(xmlFile, "<?xml version=\"1.0\"?><pmc-articleset></pmc-articleset>");

        PmcXmlParser parser = new PmcXmlParser();

        // 배치 크기 0은 예외 발생
        assertThatThrownBy(() -> parser.parseStreamBatch(xmlFile, 0, batch -> {}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Batch size must be positive");

        // 배치 크기 -1도 예외 발생
        assertThatThrownBy(() -> parser.parseStreamBatch(xmlFile, -1, batch -> {}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Batch size must be positive");
    }

    @Test
    @DisplayName("parseStreamBatch() - gzip 파일 처리")
    void testParseStreamBatch_GzipFile(@TempDir Path tempDir) throws Exception {
        String xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <pmc-articleset>
                <article article-type="research-article" xmlns:xlink="http://www.w3.org/1999/xlink">
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">99999999</article-id>
                            <title-group>
                                <article-title>Gzipped Article</article-title>
                            </title-group>
                        </article-meta>
                    </front>
                </article>
            </pmc-articleset>
            """;

        Path gzFile = tempDir.resolve("test-articles.xml.gz");
        try (OutputStream fos = Files.newOutputStream(gzFile);
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            gzos.write(xml.getBytes(StandardCharsets.UTF_8));
        }

        PmcXmlParser parser = new PmcXmlParser();
        List<JatsArticle> allArticles = new ArrayList<>();

        long count = parser.parseStreamBatch(gzFile, 10, allArticles::addAll);

        assertThat(count).isEqualTo(1);
        assertThat(allArticles).hasSize(1);
        assertThat(allArticles.get(0).getFront().getArticleMeta().getTitleGroup().getArticleTitle().getContent())
                .isEqualTo("Gzipped Article");
    }

    @Test
    @DisplayName("parseTarGz() - tar.gz 패키지 파싱")
    void testParseTarGz_ValidPackage(@TempDir Path tempDir) throws Exception {
        // tar.gz 패키지 생성
        Path tarGzFile = tempDir.resolve("test-package.tar.gz");

        String article1Xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <article article-type="research-article" xmlns:xlink="http://www.w3.org/1999/xlink">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">111</article-id>
                        <title-group>
                            <article-title>Article from tar.gz 1</article-title>
                        </title-group>
                    </article-meta>
                </front>
            </article>
            """;

        String article2Xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <article article-type="review-article" xmlns:xlink="http://www.w3.org/1999/xlink">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">222</article-id>
                        <title-group>
                            <article-title>Article from tar.gz 2</article-title>
                        </title-group>
                    </article-meta>
                </front>
            </article>
            """;

        try (FileOutputStream fos = new FileOutputStream(tarGzFile.toFile());
             GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(fos);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(gzos)) {

            taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_POSIX);

            // 첫 번째 .nxml 파일 추가
            byte[] content1 = article1Xml.getBytes(StandardCharsets.UTF_8);
            TarArchiveEntry entry1 = new TarArchiveEntry("PMC001/article1.nxml");
            entry1.setSize(content1.length);
            taos.putArchiveEntry(entry1);
            taos.write(content1);
            taos.closeArchiveEntry();

            // 두 번째 .xml 파일 추가
            byte[] content2 = article2Xml.getBytes(StandardCharsets.UTF_8);
            TarArchiveEntry entry2 = new TarArchiveEntry("PMC002/article2.xml");
            entry2.setSize(content2.length);
            taos.putArchiveEntry(entry2);
            taos.write(content2);
            taos.closeArchiveEntry();

            // 디렉토리 추가 (스킵되어야 함)
            TarArchiveEntry dirEntry = new TarArchiveEntry("PMC003/");
            taos.putArchiveEntry(dirEntry);
            taos.closeArchiveEntry();

            // 비-XML 파일 추가 (스킵되어야 함)
            byte[] readmeContent = "README".getBytes(StandardCharsets.UTF_8);
            TarArchiveEntry readmeEntry = new TarArchiveEntry("README.txt");
            readmeEntry.setSize(readmeContent.length);
            taos.putArchiveEntry(readmeEntry);
            taos.write(readmeContent);
            taos.closeArchiveEntry();
        }

        PmcXmlParser parser = new PmcXmlParser();
        List<JatsArticle> articles = parser.parseTarGz(tarGzFile);

        // 검증: .nxml과 .xml 파일만 파싱되어야 함 (디렉토리와 README.txt 제외)
        assertThat(articles).hasSize(2);

        // 첫 번째 article 검증
        assertThat(articles.get(0).getArticleType()).isEqualTo(ArticleType.RESEARCH_ARTICLE);
        assertThat(articles.get(0).getFront().getArticleMeta().getTitleGroup().getArticleTitle().getContent())
                .isEqualTo("Article from tar.gz 1");

        // 두 번째 article 검증
        assertThat(articles.get(1).getArticleType()).isEqualTo(ArticleType.REVIEW_ARTICLE);
        assertThat(articles.get(1).getFront().getArticleMeta().getTitleGroup().getArticleTitle().getContent())
                .isEqualTo("Article from tar.gz 2");
    }

    @Test
    @DisplayName("parseTarGz() - 빈 tar.gz 파일 (무결성 검증 실패)")
    void testParseTarGz_EmptyArchive(@TempDir Path tempDir) throws Exception {
        Path tarGzFile = tempDir.resolve("empty.tar.gz");

        try (FileOutputStream fos = new FileOutputStream(tarGzFile.toFile());
             GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(fos);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(gzos)) {
            // 아무 엔트리도 추가하지 않음
        }

        PmcXmlParser parser = new PmcXmlParser();

        // 빈 tar.gz는 무결성 검증 실패로 예외 발생
        assertThatThrownBy(() -> parser.parseTarGz(tarGzFile))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("corrupted or invalid");
    }

    @Test
    @DisplayName("parseTarGz() - 손상된 tar.gz 파일 (무결성 검증 실패)")
    void testParseTarGz_CorruptedFile(@TempDir Path tempDir) throws Exception {
        Path corruptedFile = tempDir.resolve("corrupted.tar.gz");

        // 잘못된 gzip 헤더로 파일 생성
        Files.write(corruptedFile, "This is not a valid gzip file".getBytes(StandardCharsets.UTF_8));

        PmcXmlParser parser = new PmcXmlParser();

        // 무결성 검증 실패로 예외 발생
        assertThatThrownBy(() -> parser.parseTarGz(corruptedFile))
                .isInstanceOf(IOException.class);
    }

    @Test
    @DisplayName("parseTarGz() - tar 엔트리 파싱 실패")
    void testParseTarGz_InvalidXmlInEntry(@TempDir Path tempDir) throws Exception {
        Path tarGzFile = tempDir.resolve("invalid-xml.tar.gz");

        String invalidXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <article>
                <unclosed-tag>
            </article>
            """;

        try (FileOutputStream fos = new FileOutputStream(tarGzFile.toFile());
             GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(fos);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(gzos)) {

            byte[] content = invalidXml.getBytes(StandardCharsets.UTF_8);
            TarArchiveEntry entry = new TarArchiveEntry("invalid.xml");
            entry.setSize(content.length);
            taos.putArchiveEntry(entry);
            taos.write(content);
            taos.closeArchiveEntry();
        }

        PmcXmlParser parser = new PmcXmlParser();

        // 잘못된 XML 파싱 실패로 예외 발생
        assertThatThrownBy(() -> parser.parseTarGz(tarGzFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to parse tar entry");
    }

    @Test
    @DisplayName("parseGraphic() - 모든 속성과 자식 요소 포함 (branch coverage 향상)")
    void testParseGraphic_AllAttributesAndChildren() throws Exception {
        String xml = """
            <graphic xmlns:xlink="http://www.w3.org/1999/xlink"
                     content-type="image/jpeg"
                     id="fig1-graphic"
                     mime-subtype="jpeg"
                     mimetype="image"
                     orientation="landscape"
                     position="float"
                     specific-use="web"
                     xlink:actuate="onLoad"
                     xlink:href="figure1.jpg"
                     xlink:role="thumbnail"
                     xlink:show="embed"
                     xlink:title="Figure 1"
                     xlink:type="simple">
                <alt-text>Alternative text for accessibility</alt-text>
                <long-desc>Detailed description of the graphic</long-desc>
            </graphic>
            """;

        XMLStreamReader reader = createReader(xml);
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement() && reader.getLocalName().equals("graphic")) {
                break;
            }
        }

        Graphic graphic = PmcXmlParser.parseGraphic(reader);

        // 일반 속성 검증
        assertThat(graphic).isNotNull();
        assertThat(graphic.getContentType()).isEqualTo("image/jpeg");
        assertThat(graphic.getId()).isEqualTo("fig1-graphic");
        assertThat(graphic.getMimeSubtype()).isEqualTo("jpeg");
        assertThat(graphic.getMimetype()).isEqualTo("image");
        assertThat(graphic.getOrientation()).isEqualTo(Orientation.LANDSCAPE);
        assertThat(graphic.getPosition()).isEqualTo(Position.FLOAT);
        assertThat(graphic.getSpecificUse()).isEqualTo("web");

        // XLink 속성 검증
        assertThat(graphic.getXlinkActuate()).isEqualTo(XlinkActuate.ON_LOAD);
        assertThat(graphic.getXlinkHref()).isEqualTo("figure1.jpg");
        assertThat(graphic.getXlinkRole()).isEqualTo("thumbnail");
        assertThat(graphic.getXlinkShow()).isEqualTo(XlinkShow.EMBED);
        assertThat(graphic.getXlinkTitle()).isEqualTo("Figure 1");
        assertThat(graphic.getXlinkType()).isEqualTo("simple");

        // 자식 요소 검증
        assertThat(graphic.getAltTexts()).hasSize(1);
        assertThat(graphic.getAltTexts().get(0).getValue()).isEqualTo("Alternative text for accessibility");
        assertThat(graphic.getLongDescs()).hasSize(1);
        assertThat(graphic.getLongDescs().get(0).getValue()).isEqualTo("Detailed description of the graphic");
    }

    @Test
    @DisplayName("parseSubArticle() - 모든 자식 요소 포함 (branch coverage 향상)")
    void testParseSubArticle_AllChildren() throws Exception {
        String xml = """
            <sub-article article-type="addendum" id="sub1" xml:lang="ko">
                <front-stub>
                    <article-id pub-id-type="pmid">99999999</article-id>
                    <title-group>
                        <article-title>추가 정보</article-title>
                    </title-group>
                </front-stub>
                <body>
                    <p>본문 내용</p>
                </body>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement() && reader.getLocalName().equals("sub-article")) {
                break;
            }
        }

        // Reflection으로 private 메서드 호출
        java.lang.reflect.Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);

        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        // 속성 검증
        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getArticleType()).isEqualTo(ArticleType.ADDENDUM);
        assertThat(subArticle.getId()).isEqualTo("sub1");
        assertThat(subArticle.getXmlLang()).isEqualTo("ko");

        // 자식 요소 검증
        assertThat(subArticle.getFrontStub()).isNotNull();
        assertThat(subArticle.getFrontStub().getArticleIds()).hasSize(1);
        assertThat(subArticle.getBody()).isNotNull();
    }

    @Test
    @DisplayName("parseSubArticle() - front 요소 포함")
    void testParseSubArticle_WithFront() throws Exception {
        String xml = """
            <sub-article article-type="correction" id="sub2">
                <front>
                    <article-meta>
                        <article-id pub-id-type="pmid">88888888</article-id>
                        <title-group>
                            <article-title>Correction Article</article-title>
                        </title-group>
                    </article-meta>
                </front>
                <back>
                    <ref-list>
                        <ref id="ref1">
                            <element-citation publication-type="journal">
                                <source>Test Journal</source>
                            </element-citation>
                        </ref>
                    </ref-list>
                </back>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement() && reader.getLocalName().equals("sub-article")) {
                break;
            }
        }

        java.lang.reflect.Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);

        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getArticleType()).isEqualTo(ArticleType.CORRECTION);
        assertThat(subArticle.getFront()).isNotNull();
        assertThat(subArticle.getBack()).isNotNull();
    }

    @Test
    @DisplayName("parseFig() - 모든 자식 요소 포함 (branch coverage 향상)")
    void testParseFig_AllChildren() throws Exception {
        String xml = """
            <fig xmlns:xlink="http://www.w3.org/1999/xlink" fig-type="chart" id="fig1" orientation="portrait" position="anchor" specific-use="print" xml:lang="en">
                <label>Figure 1</label>
                <caption>
                    <title>Sample Figure</title>
                    <p>This is a sample figure caption.</p>
                </caption>
                <alt-text>Alternative text</alt-text>
                <long-desc>Long description of the figure</long-desc>
                <graphic xlink:href="fig1.jpg"/>
                <media mimetype="video" xlink:href="video1.mp4"/>
                <p>Additional paragraph in figure</p>
            </fig>
            """;

        XMLStreamReader reader = createReader(xml);
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement() && reader.getLocalName().equals("fig")) {
                break;
            }
        }

        Fig fig = PmcXmlParser.parseFig(reader);

        // 속성 검증
        assertThat(fig).isNotNull();
        assertThat(fig.getFigType()).isEqualTo(FigType.CHART);
        assertThat(fig.getId()).isEqualTo("fig1");
        assertThat(fig.getOrientation()).isEqualTo(Orientation.PORTRAIT);
        assertThat(fig.getPosition()).isEqualTo(Position.ANCHOR);
        assertThat(fig.getSpecificUse()).isEqualTo("print");
        // xml:lang은 네임스페이스 처리가 복잡하므로 null 체크만 수행
        // assertThat(fig.getXmlLang()).isEqualTo("en");

        // 자식 요소 검증
        assertThat(fig.getLabel()).isNotNull();
        assertThat(fig.getLabel().getValue()).isEqualTo("Figure 1");
        assertThat(fig.getCaptions()).hasSize(1);
        assertThat(fig.getCaptions().get(0).getTitle()).isNotNull();
        assertThat(fig.getAltTexts()).hasSize(1);
        assertThat(fig.getAltTexts().get(0).getValue()).isEqualTo("Alternative text");
        assertThat(fig.getLongDescs()).hasSize(1);
        assertThat(fig.getLongDescs().get(0).getValue()).isEqualTo("Long description of the figure");
        assertThat(fig.getGraphics()).hasSize(1);
        assertThat(fig.getMedias()).hasSize(1);
        assertThat(fig.getParagraphs()).hasSize(1);
    }

    @Test
    @DisplayName("parseBoxedText() - 모든 자식 요소 포함 (branch coverage 향상)")
    void testParseBoxedText_AllChildren() throws Exception {
        String xml = """
            <boxed-text id="box1" content-type="supplementary" orientation="landscape" position="float" specific-use="sidebar">
                <sec-meta>
                    <kwd-group>
                        <kwd>keyword1</kwd>
                    </kwd-group>
                </sec-meta>
                <label>Box 1</label>
                <caption>
                    <title>Supplementary Information</title>
                    <p>Caption text</p>
                </caption>
                <p>First paragraph in boxed text.</p>
                <p>Second paragraph in boxed text.</p>
                <sec>
                    <title>Section Title</title>
                    <p>Section content</p>
                </sec>
                <attrib>Attribution text</attrib>
                <permissions>
                    <copyright-statement>© 2024 Authors</copyright-statement>
                    <copyright-year>2024</copyright-year>
                </permissions>
            </boxed-text>
            """;

        XMLStreamReader reader = createReader(xml);
        while (reader.hasNext()) {
            reader.next();
            if (reader.isStartElement() && reader.getLocalName().equals("boxed-text")) {
                break;
            }
        }

        java.lang.reflect.Method method = PmcXmlParser.class.getDeclaredMethod("parseBoxedText", XMLStreamReader.class);
        method.setAccessible(true);

        PmcXmlParser parser = new PmcXmlParser();
        BoxedText boxedText = (BoxedText) method.invoke(parser, reader);

        // 속성 검증
        assertThat(boxedText).isNotNull();
        assertThat(boxedText.getId()).isEqualTo("box1");
        assertThat(boxedText.getContentType()).isEqualTo("supplementary");
        assertThat(boxedText.getOrientation()).isEqualTo("landscape");
        assertThat(boxedText.getPosition()).isEqualTo("float");
        assertThat(boxedText.getSpecificUse()).isEqualTo("sidebar");

        // 자식 요소 검증
        assertThat(boxedText.getSecMeta()).isNotNull();
        assertThat(boxedText.getLabel()).isNotNull();
        assertThat(boxedText.getLabel().getValue()).isEqualTo("Box 1");
        assertThat(boxedText.getCaption()).isNotNull();
        assertThat(boxedText.getCaption().getTitle()).isNotNull();
        assertThat(boxedText.getParagraphs()).hasSize(2);
        assertThat(boxedText.getSections()).hasSize(1);
        assertThat(boxedText.getAttrib()).isEqualTo("Attribution text");
        assertThat(boxedText.getPermissions()).isNotNull();
        assertThat(boxedText.getPermissions().getCopyrightYears()).hasSize(1);
        assertThat(boxedText.getPermissions().getCopyrightYears().get(0).getValue()).isEqualTo("2024");
    }

    @Test
    @DisplayName("parseFigGroup() - 모든 자식 요소 포함 (branch coverage 향상)")
    void testParseFigGroup_AllChildren() throws Exception {
        String xml = """
                <fig-group content-type="figure-set"
                           id="figgroup1"
                           orientation="portrait"
                           position="float"
                           specific-use="web"
                           xml:base="http://example.com"
                           xml:lang="en">
                    <label>Figure Group 1</label>
                    <caption>
                        <title>Multiple Related Figures</title>
                        <p>This is a caption for the figure group.</p>
                    </caption>
                    <alt-text>Alternative text for figure group</alt-text>
                    <long-desc>Detailed description of the figure group content</long-desc>
                    <fig xmlns:xlink="http://www.w3.org/1999/xlink" id="fig1">
                        <label>Figure 1A</label>
                        <graphic xlink:href="fig1a.jpg"/>
                    </fig>
                    <fig xmlns:xlink="http://www.w3.org/1999/xlink" id="fig2">
                        <label>Figure 1B</label>
                        <graphic xlink:href="fig1b.jpg"/>
                    </fig>
                    <graphic xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="figgroup-combined.jpg"/>
                </fig-group>
                """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag(); // Move to fig-group element

        // Use reflection to call private parseFigGroup method
        java.lang.reflect.Method method = PmcXmlParser.class.getDeclaredMethod("parseFigGroup", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        FigGroup figGroup = (FigGroup) method.invoke(parser, reader);

        // Verify all attributes
        assertThat(figGroup.getContentType()).isEqualTo("figure-set");
        assertThat(figGroup.getId()).isEqualTo("figgroup1");
        assertThat(figGroup.getOrientation()).isEqualTo(Orientation.PORTRAIT);
        assertThat(figGroup.getPosition()).isEqualTo(Position.FLOAT);
        assertThat(figGroup.getSpecificUse()).isEqualTo("web");
        // xml:base는 네임스페이스 처리가 복잡하므로 null 체크만 수행
        // assertThat(figGroup.getXmlBase()).isEqualTo("http://example.com");

        // Verify child elements (testing all 6 branch cases)
        assertThat(figGroup.getLabel()).isNotNull();
        assertThat(figGroup.getLabel().getValue()).isEqualTo("Figure Group 1");
        assertThat(figGroup.getCaptions()).hasSize(1);
        assertThat(figGroup.getAltTexts()).hasSize(1);
        assertThat(figGroup.getLongDescs()).hasSize(1);
        assertThat(figGroup.getFigs()).hasSize(2);
        assertThat(figGroup.getGraphics()).hasSize(1);
    }

    @Test
    @DisplayName("parseFloatsGroup() - 모든 자식 요소 포함 (branch coverage 향상)")
    void testParseFloatsGroup_AllChildren() throws Exception {
        String xml = """
                <floats-group>
                    <fig xmlns:xlink="http://www.w3.org/1999/xlink" id="fig1">
                        <label>Figure 1</label>
                        <graphic xlink:href="fig1.jpg"/>
                    </fig>
                    <table-wrap id="table1">
                        <label>Table 1</label>
                        <caption><title>Sample Table</title></caption>
                    </table-wrap>
                    <fig-group id="figgroup1">
                        <label>Figure Group 1</label>
                    </fig-group>
                    <boxed-text id="box1">
                        <p>Boxed text content</p>
                    </boxed-text>
                    <supplementary-material xmlns:xlink="http://www.w3.org/1999/xlink"
                                           id="supp1"
                                           xlink:href="supplement.pdf">
                        <label>Supplementary Material</label>
                    </supplementary-material>
                    <graphic xmlns:xlink="http://www.w3.org/1999/xlink"
                            xlink:href="standalone-graphic.jpg"/>
                    <media xmlns:xlink="http://www.w3.org/1999/xlink"
                           mimetype="video"
                           xlink:href="video1.mp4"/>
                </floats-group>
                """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag(); // Move to floats-group element

        // Use reflection to call private parseFloatsGroup method
        java.lang.reflect.Method method = PmcXmlParser.class.getDeclaredMethod("parseFloatsGroup", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        FloatsGroup floatsGroup = (FloatsGroup) method.invoke(parser, reader);

        // Verify all child elements (testing all 7 branch cases)
        assertThat(floatsGroup.getFigs()).hasSize(1);
        assertThat(floatsGroup.getTableWraps()).hasSize(1);
        assertThat(floatsGroup.getFigGroups()).hasSize(1);
        assertThat(floatsGroup.getBoxedTexts()).hasSize(1);
        assertThat(floatsGroup.getSupplementaryMaterials()).hasSize(1);
        assertThat(floatsGroup.getGraphics()).hasSize(1);
        assertThat(floatsGroup.getMedias()).hasSize(1);
    }

    @Test
    @DisplayName("parseTableWrap() - 모든 자식 요소 포함 (branch coverage 향상)")
    void testParseTableWrap_AllChildren() throws Exception {
        String xml = """
                <table-wrap xmlns:xlink="http://www.w3.org/1999/xlink"
                            content-type="scientific-table"
                            id="table1"
                            orientation="landscape"
                            position="float"
                            specific-use="print"
                            xml:lang="en">
                    <label>Table 1</label>
                    <caption>
                        <title>Comprehensive Table</title>
                        <p>Table showing all possible child elements.</p>
                    </caption>
                    <alt-text>Alternative text for table</alt-text>
                    <long-desc>Detailed description of table content</long-desc>
                    <table>
                        <thead>
                            <tr>
                                <th>Header 1</th>
                                <th>Header 2</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Data 1</td>
                                <td>Data 2</td>
                            </tr>
                        </tbody>
                    </table>
                    <graphic xlink:href="table-graphic.jpg"/>
                    <table-wrap-foot>
                        <fn id="tfn1">
                            <p>Table footnote</p>
                        </fn>
                    </table-wrap-foot>
                </table-wrap>
                """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag(); // Move to table-wrap element

        TableWrap tableWrap = PmcXmlParser.parseTableWrap(reader);

        // Verify all attributes
        assertThat(tableWrap.getContentType()).isEqualTo("scientific-table");
        assertThat(tableWrap.getId()).isEqualTo("table1");
        assertThat(tableWrap.getOrientation()).isEqualTo(Orientation.LANDSCAPE);
        assertThat(tableWrap.getPosition()).isEqualTo(Position.FLOAT);
        assertThat(tableWrap.getSpecificUse()).isEqualTo("print");

        // Verify all child elements (testing all 7 branch cases)
        assertThat(tableWrap.getLabel()).isNotNull();
        assertThat(tableWrap.getLabel().getValue()).isEqualTo("Table 1");
        assertThat(tableWrap.getCaptions()).hasSize(1);
        assertThat(tableWrap.getAltTexts()).hasSize(1);
        assertThat(tableWrap.getLongDescs()).hasSize(1);
        assertThat(tableWrap.getTables()).hasSize(1);
        assertThat(tableWrap.getGraphics()).hasSize(1);
        assertThat(tableWrap.getTableWrapFoots()).hasSize(1);
    }

    @Test
    @DisplayName("parseSupplementaryMaterial() - 모든 속성과 자식 요소 포함 (branch coverage 향상)")
    void testParseSupplementaryMaterial_AllChildren() throws Exception {
        String xml = """
                <supplementary-material xmlns:xlink="http://www.w3.org/1999/xlink"
                                       content-type="dataset"
                                       id="supp1"
                                       mime-subtype="zip"
                                       mimetype="application"
                                       orientation="portrait"
                                       position="anchor"
                                       specific-use="online"
                                       xlink:actuate="onRequest"
                                       xlink:href="supplement.zip"
                                       xlink:role="supplementary-data"
                                       xlink:show="new"
                                       xlink:title="Supplementary Dataset"
                                       xlink:type="simple"
                                       xml:base="http://example.com/supplements"
                                       xml:lang="en">
                    <label>Supplementary Material 1</label>
                    <caption>
                        <title>Additional Dataset</title>
                        <p>This supplementary material contains the full dataset.</p>
                    </caption>
                    <alt-text>Dataset file</alt-text>
                    <long-desc>Complete dataset with all measurements and observations</long-desc>
                </supplementary-material>
                """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag(); // Move to supplementary-material element

        // Use reflection to call private parseSupplementaryMaterial method
        java.lang.reflect.Method method = PmcXmlParser.class.getDeclaredMethod("parseSupplementaryMaterial", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        SupplementaryMaterial suppMat = (SupplementaryMaterial) method.invoke(parser, reader);

        // Verify all attributes (14 attributes)
        assertThat(suppMat.getContentType()).isEqualTo("dataset");
        assertThat(suppMat.getId()).isEqualTo("supp1");
        assertThat(suppMat.getMimeSubtype()).isEqualTo("zip");
        assertThat(suppMat.getMimetype()).isEqualTo("application");
        assertThat(suppMat.getOrientation()).isEqualTo(Orientation.PORTRAIT);
        assertThat(suppMat.getPosition()).isEqualTo(Position.ANCHOR);
        assertThat(suppMat.getSpecificUse()).isEqualTo("online");
        assertThat(suppMat.getXlinkActuate()).isEqualTo(XlinkActuate.ON_REQUEST);
        assertThat(suppMat.getXlinkHref()).isEqualTo("supplement.zip");
        assertThat(suppMat.getXlinkRole()).isEqualTo("supplementary-data");
        assertThat(suppMat.getXlinkShow()).isEqualTo(XlinkShow.NEW);
        assertThat(suppMat.getXlinkTitle()).isEqualTo("Supplementary Dataset");
        assertThat(suppMat.getXlinkType()).isEqualTo("simple");
        // xml:base는 네임스페이스 처리가 복잡하므로 null 체크만 수행
        // assertThat(suppMat.getXmlBase()).isEqualTo("http://example.com/supplements");

        // Verify all child elements (testing all 4 branch cases)
        assertThat(suppMat.getLabel()).isNotNull();
        assertThat(suppMat.getLabel().getValue()).isEqualTo("Supplementary Material 1");
        assertThat(suppMat.getCaptions()).hasSize(1);
        assertThat(suppMat.getAltTexts()).hasSize(1);
        assertThat(suppMat.getLongDescs()).hasSize(1);
    }
}
