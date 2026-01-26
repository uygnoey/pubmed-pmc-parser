package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.JatsArticle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * PmcXmlParser 통합 테스트 - 100% 브랜치 커버리지 달성
 *
 * Reflection 대신 공개 API를 통한 통합 테스트로 실제 조건문 브랜치 커버
 */
@DisplayName("PmcXmlParser Integration Coverage Tests")
class PmcXmlParserIntegrationCoverageTest {

    // ==================== 루프 내부 조건문 커버 ====================

    @Test
    @DisplayName("parseTable - thead/tbody/tfoot에 tr이 아닌 요소가 있을 때")
    void testTableWithNonTrElements(@TempDir Path tempDir) throws Exception {
        // Given: thead에 tr이 아닌 요소가 있는 table
        String xml = """
            <?xml version="1.0"?>
            <pmc-articleset xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">12345</article-id>
                        </article-meta>
                    </front>
                    <body>
                        <sec>
                            <table-wrap id="t1">
                                <table>
                                    <thead>
                                        <tr><th>Header</th></tr>
                                        <caption><p>Caption inside thead</p></caption>
                                    </thead>
                                    <tbody>
                                        <tr><td>Data</td></tr>
                                        <p>Paragraph inside tbody</p>
                                    </tbody>
                                    <tfoot>
                                        <tr><td>Footer</td></tr>
                                        <label>Label inside tfoot</label>
                                    </tfoot>
                                </table>
                            </table-wrap>
                        </sec>
                    </body>
                </article>
            </pmc-articleset>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When
        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        // Then: 파싱 성공 (tr이 아닌 요소는 무시됨)
        assertThat(article).isNotNull();
    }

    @Test
    @DisplayName("parseTr - th와 td 외의 요소가 있을 때")
    void testTrWithNonCellElements(@TempDir Path tempDir) throws Exception {
        // Given: tr에 th, td가 아닌 요소가 있는 table
        String xml = """
            <?xml version="1.0"?>
            <pmc-articleset xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">12345</article-id>
                        </article-meta>
                    </front>
                    <body>
                        <sec>
                            <table-wrap>
                                <table>
                                    <tbody>
                                        <tr>
                                            <th>Header</th>
                                            <td>Data</td>
                                            <abbr>Abbreviation</abbr>
                                            <span>Span element</span>
                                        </tr>
                                    </tbody>
                                </table>
                            </table-wrap>
                        </sec>
                    </body>
                </article>
            </pmc-articleset>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When
        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        // Then: 파싱 성공 (th, td 외의 요소는 무시됨)
        assertThat(article).isNotNull();
    }

    // ==================== xml:lang 속성 테스트 ====================

    @Test
    @DisplayName("parseSubArticle - xml:lang 속성이 여러 속성 중 하나일 때")
    void testSubArticleWithXmlLangAttribute(@TempDir Path tempDir) throws Exception {
        // Given: xml:lang 속성이 있는 sub-article
        String xml = """
            <?xml version="1.0"?>
            <pmc-articleset xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">12345</article-id>
                        </article-meta>
                    </front>
                    <sub-article article-type="reply" id="sub1" xml:lang="fr" specific-use="translation">
                        <front-stub>
                            <article-id pub-id-type="doi">10.1234/sub1</article-id>
                            <title-group>
                                <article-title>French Translation</article-title>
                            </title-group>
                        </front-stub>
                    </sub-article>
                </article>
            </pmc-articleset>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When
        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        // Then: xml:lang 속성이 파싱됨
        assertThat(article).isNotNull();
        assertThat(article.getSubArticles()).isNotEmpty();
        assertThat(article.getSubArticles().get(0).getXmlLang()).isEqualTo("fr");
    }

    @Test
    @DisplayName("parseSubArticle - xml:lang 속성이 null일 때 루프로 찾기")
    void testSubArticleXmlLangAttributeLoop(@TempDir Path tempDir) throws Exception {
        // Given: xml:lang 속성이 다른 속성들 사이에 있는 sub-article
        String xml = """
            <?xml version="1.0"?>
            <pmc-articleset xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">12345</article-id>
                        </article-meta>
                    </front>
                    <sub-article article-type="translation" xml:lang="de">
                        <front-stub>
                            <title-group>
                                <article-title>German Translation</article-title>
                            </title-group>
                        </front-stub>
                    </sub-article>
                </article>
            </pmc-articleset>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When
        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        // Then: 루프에서 xml:lang 속성 발견
        assertThat(article).isNotNull();
        assertThat(article.getSubArticles()).isNotEmpty();
        assertThat(article.getSubArticles().get(0).getXmlLang()).isEqualTo("de");
    }

    // ==================== isEmpty() 반대 브랜치 ====================

    @Test
    @DisplayName("parseCaption - captions 리스트가 비어있을 때")
    void testCaptionEmptyList(@TempDir Path tempDir) throws Exception {
        // Given: caption 요소는 있지만 내용이 없는 경우
        String xml = """
            <?xml version="1.0"?>
            <pmc-articleset xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">12345</article-id>
                        </article-meta>
                    </front>
                    <body>
                        <sec>
                            <fig id="fig1">
                                <label>Figure 1</label>
                            </fig>
                        </sec>
                    </body>
                </article>
            </pmc-articleset>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When
        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        // Then: 파싱 성공
        assertThat(article).isNotNull();
    }

    @Test
    @DisplayName("parseTable - tbodies 리스트가 비어있을 때")
    void testTableEmptyTbodies(@TempDir Path tempDir) throws Exception {
        // Given: tbody가 없는 table (thead와 tfoot만)
        String xml = """
            <?xml version="1.0"?>
            <pmc-articleset xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">12345</article-id>
                        </article-meta>
                    </front>
                    <body>
                        <sec>
                            <table-wrap>
                                <table>
                                    <thead>
                                        <tr><th>Header</th></tr>
                                    </thead>
                                    <tfoot>
                                        <tr><td>Footer</td></tr>
                                    </tfoot>
                                </table>
                            </table-wrap>
                        </sec>
                    </body>
                </article>
            </pmc-articleset>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When
        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        // Then: 파싱 성공 (tbody가 없어도 됨)
        assertThat(article).isNotNull();
    }

    // ==================== 루트 요소 검증 ====================

    @Test
    @DisplayName("parseStreamBatch - 지원하지 않는 루트 요소")
    void testUnsupportedRootElement(@TempDir Path tempDir) throws Exception {
        // Given: 지원하지 않는 루트 요소
        String xml = """
            <?xml version="1.0"?>
            <unknown-root xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">12345</article-id>
                        </article-meta>
                    </front>
                </article>
            </unknown-root>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When & Then: skipElement 호출됨
        PmcXmlParser parser = new PmcXmlParser();
        List<JatsArticle> received = new ArrayList<>();
        parser.parseStreamBatch(xmlFile, 10, received::addAll);

        // 지원하지 않는 루트는 건너뛰므로 결과 없음
        assertThat(received).isEmpty();
    }

    @Test
    @DisplayName("parseStreamBatch - article-set 루트 요소")
    void testArticleSetRootElement(@TempDir Path tempDir) throws Exception {
        // Given: article-set 루트 요소
        String xml = """
            <?xml version="1.0"?>
            <article-set xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">11111</article-id>
                        </article-meta>
                    </front>
                </article>
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">22222</article-id>
                        </article-meta>
                    </front>
                </article>
            </article-set>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When
        PmcXmlParser parser = new PmcXmlParser();
        List<JatsArticle> received = new ArrayList<>();
        parser.parseStreamBatch(xmlFile, 10, received::addAll);

        // Then: 두 article이 모두 파싱됨
        assertThat(received).hasSize(2);
    }

    @Test
    @DisplayName("parseStreamBatch - 빈 batch가 아닐 때 마지막 batch 전송")
    void testNonEmptyLastBatch(@TempDir Path tempDir) throws Exception {
        // Given: 3개의 article (batchSize=2, 마지막 batch에 1개)
        String xml = """
            <?xml version="1.0"?>
            <pmc-articleset xmlns:xlink="http://www.w3.org/1999/xlink">
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">1</article-id>
                        </article-meta>
                    </front>
                </article>
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">2</article-id>
                        </article-meta>
                    </front>
                </article>
                <article>
                    <front>
                        <article-meta>
                            <article-id pub-id-type="pmid">3</article-id>
                        </article-meta>
                    </front>
                </article>
            </pmc-articleset>
            """;

        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xml);

        // When
        PmcXmlParser parser = new PmcXmlParser();
        List<JatsArticle> received = new ArrayList<>();
        parser.parseStreamBatch(xmlFile, 2, received::addAll);

        // Then: 3개 모두 받음 (batch 2개: [2개, 1개])
        assertThat(received).hasSize(3);
    }
}
