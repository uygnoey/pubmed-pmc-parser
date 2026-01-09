package com.brillianttiger.bio.parser.common.parser.examples;

import com.brillianttiger.bio.parser.common.parser.StreamParser.ProgressCallback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SimpleStreamParserExample 테스트 / SimpleStreamParserExample Test
 *
 * KR: StreamParser 인터페이스 구현 예시 테스트
 * EN: Tests for StreamParser interface implementation example
 */
class SimpleStreamParserExampleTest {

    @TempDir
    Path tempDir;

    /**
     * 테스트용 XML 생성 / Create test XML
     *
     * @param articleCount Article 개수 / Number of articles
     * @return XML 파일 경로 / XML file path
     */
    private Path createTestXml(int articleCount) throws Exception {
        Path xmlFile = tempDir.resolve("test-articles.xml");

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<ArticleSet>\n");

        for (int i = 1; i <= articleCount; i++) {
            xml.append("  <Article>\n");
            xml.append("    <Id>").append(i).append("</Id>\n");
            xml.append("    <Title>Sample Title ").append(i).append("</Title>\n");
            xml.append("    <Author>Author ").append(i).append("</Author>\n");
            xml.append("    <Year>").append(2024).append("</Year>\n");
            xml.append("  </Article>\n");
        }

        xml.append("</ArticleSet>\n");

        Files.writeString(xmlFile, xml.toString());
        return xmlFile;
    }

    @Test
    void testParseStream() throws Exception {
        // 테스트 파일 생성 / Create test file
        Path xmlFile = createTestXml(5);

        // 파서 생성 / Create parser
        SimpleStreamParserExample parser = new SimpleStreamParserExample();

        // Article 수집 / Collect articles
        List<SimpleArticle> articles = new ArrayList<>();

        // 파싱 / Parse
        long count = parser.parseStream(xmlFile, articles::add);

        // 검증 / Verify
        assertEquals(5, count);
        assertEquals(5, articles.size());

        // 첫 번째 Article 검증 / Verify first article
        SimpleArticle first = articles.get(0);
        assertEquals("1", first.getId());
        assertEquals("Sample Title 1", first.getTitle());
        assertEquals("Author 1", first.getAuthor());
        assertEquals(2024, first.getYear());

        // 마지막 Article 검증 / Verify last article
        SimpleArticle last = articles.get(4);
        assertEquals("5", last.getId());
        assertEquals("Sample Title 5", last.getTitle());
        assertEquals("Author 5", last.getAuthor());
    }

    @Test
    void testParseStreamBatch() throws Exception {
        // 10개의 Article 생성 / Create 10 articles
        Path xmlFile = createTestXml(10);

        SimpleStreamParserExample parser = new SimpleStreamParserExample();

        // 배치 수집 / Collect batches
        List<List<SimpleArticle>> batches = new ArrayList<>();

        // 배치 크기 3으로 파싱 / Parse with batch size 3
        long count = parser.parseStreamBatch(xmlFile, 3, batches::add);

        // 검증 / Verify
        assertEquals(10, count);

        // 배치 개수 검증: 3개 + 3개 + 3개 + 1개 = 4 batches
        // Verify batch count: 3 + 3 + 3 + 1 = 4 batches
        assertEquals(4, batches.size());

        // 각 배치 크기 검증 / Verify each batch size
        assertEquals(3, batches.get(0).size());
        assertEquals(3, batches.get(1).size());
        assertEquals(3, batches.get(2).size());
        assertEquals(1, batches.get(3).size()); // 마지막 배치 / Last batch

        // 전체 Article 개수 검증 / Verify total article count
        int total = batches.stream().mapToInt(List::size).sum();
        assertEquals(10, total);
    }

    @Test
    void testParseStreamWithProgress() throws Exception {
        // 100개의 Article 생성 / Create 100 articles
        Path xmlFile = createTestXml(100);

        SimpleStreamParserExample parser = new SimpleStreamParserExample();

        // 진행 상황 추적 / Track progress
        AtomicLong processedCount = new AtomicLong(0);
        AtomicInteger progressCallCount = new AtomicInteger(0);

        ProgressCallback progress = (count, total, percentage) -> {
            processedCount.set(count);
            progressCallCount.incrementAndGet();

            // 전체 수를 모르므로 -1이어야 함 / Total is unknown, should be -1
            assertEquals(-1, total);
            assertEquals(-1.0, percentage);
        };

        // 파싱 / Parse
        List<SimpleArticle> articles = new ArrayList<>();
        long count = parser.parseStream(xmlFile, articles::add, progress);

        // 검증 / Verify
        assertEquals(100, count);
        assertEquals(100, articles.size());
        assertEquals(100, processedCount.get());
        assertEquals(100, progressCallCount.get()); // 각 Article마다 호출 / Called for each article
    }

    @Test
    void testParseStreamBatchWithProgress() throws Exception {
        // 25개의 Article 생성 / Create 25 articles
        Path xmlFile = createTestXml(25);

        SimpleStreamParserExample parser = new SimpleStreamParserExample();

        // 진행 상황 추적 / Track progress
        AtomicLong lastProcessedCount = new AtomicLong(0);
        AtomicInteger progressCallCount = new AtomicInteger(0);

        ProgressCallback progress = (count, total, percentage) -> {
            lastProcessedCount.set(count);
            progressCallCount.incrementAndGet();
        };

        // 배치 크기 10으로 파싱 / Parse with batch size 10
        List<List<SimpleArticle>> batches = new ArrayList<>();
        long count = parser.parseStreamBatch(xmlFile, 10, batches::add, progress);

        // 검증 / Verify
        assertEquals(25, count);
        assertEquals(3, batches.size()); // 10 + 10 + 5 = 3 batches

        // 진행 상황 콜백 호출 검증 / Verify progress callback calls
        // 3번 호출되어야 함: 각 배치마다 / Should be called 3 times: for each batch
        assertEquals(3, progressCallCount.get());
        assertEquals(25, lastProcessedCount.get());
    }

    @Test
    void testEmptyFile() throws Exception {
        // 빈 XML 파일 / Empty XML file
        Path xmlFile = tempDir.resolve("empty.xml");
        Files.writeString(xmlFile, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<ArticleSet>\n</ArticleSet>\n");

        SimpleStreamParserExample parser = new SimpleStreamParserExample();

        List<SimpleArticle> articles = new ArrayList<>();
        long count = parser.parseStream(xmlFile, articles::add);

        // 검증 / Verify
        assertEquals(0, count);
        assertEquals(0, articles.size());
    }

    @Test
    void testLargeFile() throws Exception {
        // 대용량 파일 (1000개 Article) / Large file (1000 articles)
        Path xmlFile = createTestXml(1000);

        SimpleStreamParserExample parser = new SimpleStreamParserExample();

        // 메모리 효율성 확인: 모든 Article을 메모리에 로드하지 않고 처리
        // Verify memory efficiency: process without loading all articles into memory
        AtomicInteger processedCount = new AtomicInteger(0);

        long count = parser.parseStream(xmlFile, article -> {
            assertNotNull(article);
            assertNotNull(article.getId());
            assertNotNull(article.getTitle());
            processedCount.incrementAndGet();
        });

        // 검증 / Verify
        assertEquals(1000, count);
        assertEquals(1000, processedCount.get());
    }

    @Test
    void testBatchProcessingEfficiency() throws Exception {
        // 배치 처리 효율성 테스트 / Test batch processing efficiency
        Path xmlFile = createTestXml(100);

        SimpleStreamParserExample parser = new SimpleStreamParserExample();

        // 배치 핸들러 호출 횟수 추적 / Track batch handler call count
        AtomicInteger batchCallCount = new AtomicInteger(0);

        parser.parseStreamBatch(xmlFile, 20, batch -> {
            batchCallCount.incrementAndGet();
            assertTrue(batch.size() <= 20); // 배치 크기 검증 / Verify batch size
        });

        // 100개 Article, 배치 크기 20 → 5번 호출되어야 함
        // 100 articles, batch size 20 → should be called 5 times
        assertEquals(5, batchCallCount.get());
    }

    @Test
    void testProgressCallbackCalculatePercentage() {
        // 진행률 계산 테스트 / Test percentage calculation

        // 50% 진행 / 50% progress
        double percentage = ProgressCallback.calculatePercentage(50, 100);
        assertEquals(50.0, percentage);

        // 100% 진행 / 100% progress
        percentage = ProgressCallback.calculatePercentage(100, 100);
        assertEquals(100.0, percentage);

        // 전체 수 알 수 없음 / Total unknown
        percentage = ProgressCallback.calculatePercentage(50, -1);
        assertEquals(-1.0, percentage);

        // 100% 초과 방지 / Prevent exceeding 100%
        percentage = ProgressCallback.calculatePercentage(150, 100);
        assertEquals(100.0, percentage);
    }

    @Test
    void testPartialXml() throws Exception {
        // 일부 필드만 있는 XML / XML with partial fields
        Path xmlFile = tempDir.resolve("partial.xml");

        String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <ArticleSet>
                  <Article>
                    <Id>1</Id>
                    <Title>Only Title</Title>
                  </Article>
                  <Article>
                    <Id>2</Id>
                    <Author>Only Author</Author>
                  </Article>
                </ArticleSet>
                """;

        Files.writeString(xmlFile, xml);

        SimpleStreamParserExample parser = new SimpleStreamParserExample();

        List<SimpleArticle> articles = new ArrayList<>();
        long count = parser.parseStream(xmlFile, articles::add);

        // 검증 / Verify
        assertEquals(2, count);

        SimpleArticle first = articles.get(0);
        assertEquals("1", first.getId());
        assertEquals("Only Title", first.getTitle());
        assertNull(first.getAuthor());
        assertNull(first.getYear());

        SimpleArticle second = articles.get(1);
        assertEquals("2", second.getId());
        assertNull(second.getTitle());
        assertEquals("Only Author", second.getAuthor());
    }
}
