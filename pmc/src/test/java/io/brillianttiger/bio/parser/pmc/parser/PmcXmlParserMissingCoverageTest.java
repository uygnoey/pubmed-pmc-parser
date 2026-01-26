package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PmcXmlParser의 누락된 커버리지를 테스트
 *
 * KR: 0% 커버리지 메서드와 부분 커버리지 메서드를 테스트하여 커버리지 향상
 * EN: Test uncovered and partially covered methods to improve coverage
 */
class PmcXmlParserMissingCoverageTest {

    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

    private XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);  // 네임스페이스 인식 활성화
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        return factory.createXMLStreamReader(new StringReader(xml));
    }

    // ==================== Simple Parser Methods (0% coverage) ====================

    @Test
    @DisplayName("parseAltText() - alt-text 요소 파싱")
    void testParseAltText() throws Exception {
        String xml = "<alt-text>Alternative text description</alt-text>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        AltText altText = PmcXmlParser.parseAltText(reader);

        assertThat(altText).isNotNull();
        assertThat(altText.getValue()).isEqualTo("Alternative text description");
    }

    @Test
    @DisplayName("parseLongDesc() - long-desc 요소 파싱")
    void testParseLongDesc() throws Exception {
        String xml = "<long-desc>This is a long description for accessibility</long-desc>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        LongDesc longDesc = PmcXmlParser.parseLongDesc(reader);

        assertThat(longDesc).isNotNull();
        assertThat(longDesc.getValue()).isEqualTo("This is a long description for accessibility");
    }

    @Test
    @DisplayName("parseMedia() - media 요소 파싱")
    void testParseMedia() throws Exception {
        String xml = """
            <media id="media1" xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="video.mp4">
            </media>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Media media = PmcXmlParser.parseMedia(reader);

        assertThat(media).isNotNull();
        assertThat(media.getId()).isEqualTo("media1");
        assertThat(media.getXlinkHref()).isEqualTo("video.mp4");
    }

    @Test
    @DisplayName("parseMedia() - 최소 속성만 있는 경우")
    void testParseMedia_MinimalAttributes() throws Exception {
        String xml = "<media></media>";
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Media media = PmcXmlParser.parseMedia(reader);

        assertThat(media).isNotNull();
        assertThat(media.getId()).isNull();
        assertThat(media.getXlinkHref()).isNull();
    }

    // ==================== Validation Methods ====================

    @Test
    @DisplayName("validateArticle() - 유효한 article 검증")
    void testValidateArticle() throws Exception {
        // 유효한 article 생성 (JatsArticle 자체가 article 요소)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .articleIds(List.of(  // articleIds 복수형 사용
                                        PmcArticleId.builder()
                                                .pubIdType("pmid")  // String 타입 사용
                                                .value("12345678")
                                                .build()
                                ))
                                .titleGroup(TitleGroup.builder()  // titleGroup 사용
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article Title")  // content 필드 사용
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();

        PmcXmlParser parser = new PmcXmlParser();
        List<io.brillianttiger.bio.parser.pmc.validation.ValidationError> errors = parser.validateArticle(article);

        assertThat(errors).isNotNull();
    }

    @Test
    @DisplayName("parseAndValidate() - 파일 파싱 및 검증")
    void testParseAndValidate() throws Exception {
        // 테스트 리소스에서 실제 파일 사용
        Path testFile = Paths.get("src/test/resources/pmc/simple_article.xml");

        PmcXmlParser parser = new PmcXmlParser();
        PmcXmlParser.ValidationResult result = parser.parseAndValidate(testFile);

        assertThat(result).isNotNull();
        assertThat(result.getArticle()).isNotNull();
        assertThat(result.getErrors()).isNotNull();
    }

    // ==================== parseInteger() - partial coverage ====================

    @Test
    @DisplayName("parseInteger() - 유효하지 않은 문자열")
    void testParseInteger_InvalidString() {
        Integer result = PmcXmlParser.parseInteger("invalid");
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("parseInteger() - null 입력")
    void testParseInteger_NullInput() {
        Integer result = PmcXmlParser.parseInteger(null);
        assertThat(result).isNull();
    }

    // ==================== parseTable() - edge cases ====================

    @Test
    @DisplayName("parseTable() - colgroup 포함 (스킵 처리 확인)")
    void testParseTable_WithColgroup() throws Exception {
        String xml = """
            <table>
                <colgroup>
                    <col/>
                    <col/>
                </colgroup>
                <tbody>
                    <tr>
                        <td>Cell 1</td>
                        <td>Cell 2</td>
                    </tr>
                </tbody>
            </table>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Table table = PmcXmlParser.parseTable(reader);

        // parseTable()은 현재 colgroup을 스킵하므로 파싱이 성공하면 OK
        assertThat(table).isNotNull();
        assertThat(table.getTbodies()).isNotNull().hasSize(1);
    }

    @Test
    @DisplayName("parseCaption() - title 없이 paragraph만")
    void testParseCaption_ParagraphOnly() throws Exception {
        String xml = """
            <caption>
                <p>This is a caption paragraph without title</p>
            </caption>
            """;
        XMLStreamReader reader = createReader(xml);
        reader.next();

        Caption caption = PmcXmlParser.parseCaption(reader);

        assertThat(caption).isNotNull();
        assertThat(caption.getTitle()).isNull();
        assertThat(caption.getParagraphs()).isNotNull();
        assertThat(caption.getParagraphs()).hasSize(1);
    }

    // ==================== parseFrontStub() / parseSubArticle() ====================
    // Note: parseFrontStub()과 parseSubArticle()은 private 메서드이므로
    // 직접 테스트하지 않고 parseJatsArticle()을 통해 간접적으로 테스트됨
    // sub-article 관련 커버리지는 PmcXmlParserTest.java의 통합 테스트에서 확보

    // ==================== parseFile / parseStream / parseStreamBatch ====================
    // These tests cover the finally block exception handling paths

    @Test
    @DisplayName("parseFile() - 정상 파싱")
    void testParseFile() throws Exception {
        Path testFile = Paths.get("src/test/resources/pmc/simple_article.xml");

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(testFile);

        assertThat(article).isNotNull();
        assertThat(article.getFront()).isNotNull();
    }

    @Test
    @DisplayName("parseStream() - Consumer 사용")
    void testParseStream() throws Exception {
        Path testFile = Paths.get("src/test/resources/pmc/simple_article.xml");

        PmcXmlParser parser = new PmcXmlParser();

        // Consumer로 article 수집
        final JatsArticle[] result = new JatsArticle[1];
        parser.parseStream(testFile, article -> {
            result[0] = article;
        });

        assertThat(result[0]).isNotNull();
        assertThat(result[0].getFront()).isNotNull();
    }

    @Test
    @DisplayName("parseStreamBatch() - 배치 크기 지정")
    void testParseStreamBatch() throws Exception {
        Path testFile = Paths.get("src/test/resources/pmc/simple_article.xml");

        PmcXmlParser parser = new PmcXmlParser();

        // Consumer로 article 수집
        final int[] count = {0};
        parser.parseStreamBatch(testFile, 10, article -> {
            count[0]++;
        });

        assertThat(count[0]).isGreaterThan(0);
    }
}
