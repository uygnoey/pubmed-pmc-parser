package com.brillianttiger.bio.parser.pubmed;

import com.brillianttiger.bio.parser.pubmed.model.PubmedArticle;
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * PubmedPerformanceTest / PubMed 성능 테스트
 *
 * KR: PubMed 파서의 성능 측정 및 최적화 검증
 * EN: Performance measurement and optimization verification for PubMed parser
 *
 * 테스트 내용 / Test Coverage:
 * - 30,000건 이상 스트리밍 파싱 시간 측정
 * - 메모리 사용량 측정 (heap 사용량)
 * - GZip vs Non-GZip 파싱 성능 비교
 * - 처리량(throughput) 측정
 * - 스트리밍 vs 일괄 처리 비교
 */
class PubmedPerformanceTest {

    private static final String BASE_DIR = "test-data/pubmed";
    private static final String DOC_DIR = "claudedocs/performance-test-results";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Runtime runtime;

    @BeforeEach
    void setUp() {
        runtime = Runtime.getRuntime();
        // GC 실행으로 메모리 측정 정확도 향상
        System.gc();
    }

    /**
     * 30,000건 이상 스트리밍 파싱 성능 테스트
     * Streaming parsing performance test with 30,000+ articles
     */
    @Test
    @Disabled("Performance test - requires large test files from NCBI FTP. Not for CI.")
    void testStreamingPerformanceWithLargeDataset() throws Exception {
        Path xmlFile = Paths.get(BASE_DIR, "baseline", "pubmed25n0001.xml.gz");
        assumeTrue(Files.exists(xmlFile), "Skipping performance test - test file not found: " + xmlFile);

        System.out.println("========================================");
        System.out.println("PubMed Streaming Performance Test");
        System.out.println("Target: 30,000+ articles");
        System.out.println("========================================\n");

        PubmedXmlParser parser = new PubmedXmlParser();
        PerformanceMetrics metrics = new PerformanceMetrics("Streaming - 30K+ articles");

        // 초기 메모리 측정
        long memoryBefore = getUsedMemory();
        metrics.memoryBefore = memoryBefore;

        // 파싱 시작
        long startTime = System.nanoTime();
        AtomicInteger count = new AtomicInteger(0);

        parser.parseStream(xmlFile, article -> {
            int num = count.incrementAndGet();

            // 간단한 처리 (실제 사용 시나리오)
            if (article.getMedlineCitation() != null) {
                article.getMedlineCitation().getPmid(); // PMID 접근
            }

            if (num % 5000 == 0) {
                long currentMemory = getUsedMemory();
                System.out.printf("  진행: %,d articles | 메모리: %s%n",
                    num, formatBytes(currentMemory));
            }
        });

        long endTime = System.nanoTime();
        long memoryAfter = getUsedMemory();

        // 메트릭 계산
        metrics.articleCount = count.get();
        metrics.elapsedTimeMs = (endTime - startTime) / 1_000_000;
        metrics.memoryAfter = memoryAfter;
        metrics.memoryUsed = memoryAfter - memoryBefore;
        metrics.throughput = count.get() * 1000.0 / metrics.elapsedTimeMs;

        // 결과 출력
        System.out.println("\n" + metrics);

        // Assertions
        assertTrue(count.get() >= 30000, "Should parse at least 30,000 articles");
        assertTrue(metrics.throughput > 1000, "Throughput should be >1000 articles/sec");
        assertTrue(metrics.memoryUsed < 500 * 1024 * 1024, "Memory usage should be <500MB for streaming");

        // 샘플링 데이터 수집 (더 자세한 분석)
        List<PerformanceMetrics> allMetrics = new ArrayList<>();
        allMetrics.add(metrics);

        // 리포트 생성
        generatePerformanceReport("streaming-performance", allMetrics);

        System.out.println("✅ 스트리밍 성능 테스트 통과");
    }

    /**
     * GZip vs Non-GZip 파싱 성능 비교
     * Compare parsing performance: GZip vs Non-GZip
     */
    @Test
    @Disabled("Performance test - requires large test files from NCBI FTP. Not for CI.")
    void testGzipVsNonGzipPerformance() throws Exception {
        Path gzipFile = Paths.get(BASE_DIR, "baseline", "pubmed25n0001.xml.gz");
        assumeTrue(Files.exists(gzipFile), "Skipping performance test - test file not found: " + gzipFile);

        System.out.println("========================================");
        System.out.println("GZip vs Non-GZip Performance Test");
        System.out.println("========================================\n");

        // Non-GZip 파일 생성 (압축 해제)
        Path uncompressedFile = Paths.get(BASE_DIR, "baseline", "pubmed25n0001-uncompressed.xml");
        if (!Files.exists(uncompressedFile)) {
            System.out.println("압축 해제 중...");
            decompressFile(gzipFile, uncompressedFile);
            System.out.println("✅ 압축 해제 완료\n");
        }

        PubmedXmlParser parser = new PubmedXmlParser();
        List<PerformanceMetrics> allMetrics = new ArrayList<>();

        // 1. GZip 파일 파싱
        System.out.println("1. GZip 파일 파싱 테스트");
        PerformanceMetrics gzipMetrics = measureParsingPerformance(
            parser, gzipFile, "GZip Compressed"
        );
        allMetrics.add(gzipMetrics);
        System.out.println(gzipMetrics);

        // GC 실행
        System.gc();
        Thread.sleep(500);

        // 2. Non-GZip 파일 파싱
        System.out.println("\n2. Non-GZip 파일 파싱 테스트");
        PerformanceMetrics uncompressedMetrics = measureParsingPerformance(
            parser, uncompressedFile, "Uncompressed"
        );
        allMetrics.add(uncompressedMetrics);
        System.out.println(uncompressedMetrics);

        // 비교 분석
        System.out.println("\n========================================");
        System.out.println("성능 비교 / Performance Comparison");
        System.out.println("========================================");

        double throughputRatio = gzipMetrics.throughput / uncompressedMetrics.throughput;
        double timeRatio = (double) gzipMetrics.elapsedTimeMs / uncompressedMetrics.elapsedTimeMs;

        System.out.printf("GZip 처리 속도: %,.0f articles/sec%n", gzipMetrics.throughput);
        System.out.printf("Non-GZip 처리 속도: %,.0f articles/sec%n", uncompressedMetrics.throughput);
        System.out.printf("속도 비율 (GZip/Non-GZip): %.2f배%n", throughputRatio);
        System.out.printf("시간 비율 (GZip/Non-GZip): %.2f배%n", timeRatio);
        System.out.printf("%nGZip 파일 크기: %s%n", formatBytes(Files.size(gzipFile)));
        System.out.printf("Non-GZip 파일 크기: %s%n", formatBytes(Files.size(uncompressedFile)));
        System.out.printf("압축률: %.1f%%%n",
            (1 - (double) Files.size(gzipFile) / Files.size(uncompressedFile)) * 100);

        // Assertions
        assertTrue(gzipMetrics.articleCount == uncompressedMetrics.articleCount,
            "Should parse same number of articles");

        // 리포트 생성
        generatePerformanceReport("gzip-vs-nongzip", allMetrics);

        System.out.println("\n✅ GZip vs Non-GZip 성능 테스트 통과");

        // 정리: 압축 해제 파일 삭제 (선택적)
        // Files.deleteIfExists(uncompressedFile);
    }

    /**
     * 메모리 사용량 상세 측정
     * Detailed memory usage measurement
     */
    @Test
    @Disabled("Performance test - requires large test files from NCBI FTP. Not for CI.")
    void testMemoryUsageWithLargeDataset() throws Exception {
        Path xmlFile = Paths.get(BASE_DIR, "baseline", "pubmed25n0001.xml.gz");
        assumeTrue(Files.exists(xmlFile), "Skipping performance test - test file not found: " + xmlFile);

        System.out.println("========================================");
        System.out.println("Memory Usage Measurement Test");
        System.out.println("========================================\n");

        PubmedXmlParser parser = new PubmedXmlParser();

        // 여러 번 측정하여 평균 메모리 사용량 계산
        int iterations = 3;
        List<Long> memoryUsages = new ArrayList<>();

        for (int i = 0; i < iterations; i++) {
            System.out.printf("반복 %d/%d...%n", i + 1, iterations);

            // GC 실행
            System.gc();
            Thread.sleep(500);

            long memoryBefore = getUsedMemory();
            AtomicInteger count = new AtomicInteger(0);

            parser.parseStream(xmlFile, article -> {
                count.incrementAndGet();
            });

            long memoryAfter = getUsedMemory();
            long memoryUsed = memoryAfter - memoryBefore;
            memoryUsages.add(memoryUsed);

            System.out.printf("  논문 수: %,d | 메모리: %s%n%n",
                count.get(), formatBytes(memoryUsed));
        }

        // 평균 메모리 사용량
        long avgMemory = (long) memoryUsages.stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0);

        System.out.println("========================================");
        System.out.println("메모리 사용량 통계");
        System.out.println("========================================");
        System.out.printf("평균 메모리: %s%n", formatBytes(avgMemory));
        System.out.printf("최소 메모리: %s%n",
            formatBytes(memoryUsages.stream().mapToLong(Long::longValue).min().orElse(0)));
        System.out.printf("최대 메모리: %s%n",
            formatBytes(memoryUsages.stream().mapToLong(Long::longValue).max().orElse(0)));

        // Assertions
        assertTrue(avgMemory < 500 * 1024 * 1024,
            "Average memory usage should be <500MB for streaming");

        System.out.println("\n✅ 메모리 사용량 측정 테스트 통과");
    }

    /**
     * 처리량 벤치마크 (다양한 시나리오)
     * Throughput benchmark with various scenarios
     */
    @Test
    @Disabled("Performance test - requires large test files from NCBI FTP. Not for CI.")
    void testThroughputBenchmark() throws Exception {
        Path xmlFile = Paths.get(BASE_DIR, "baseline", "pubmed25n0001.xml.gz");
        assumeTrue(Files.exists(xmlFile), "Skipping performance test - test file not found: " + xmlFile);

        System.out.println("========================================");
        System.out.println("Throughput Benchmark Test");
        System.out.println("========================================\n");

        PubmedXmlParser parser = new PubmedXmlParser();
        List<PerformanceMetrics> allMetrics = new ArrayList<>();

        // 시나리오 1: 최소 처리 (PMID만 접근)
        System.out.println("시나리오 1: 최소 처리 (PMID only)");
        PerformanceMetrics minimalMetrics = new PerformanceMetrics("Minimal Processing");
        long startTime = System.nanoTime();
        AtomicInteger count1 = new AtomicInteger(0);

        parser.parseStream(xmlFile, article -> {
            count1.incrementAndGet();
            if (article.getMedlineCitation() != null) {
                article.getMedlineCitation().getPmid();
            }
        });

        minimalMetrics.elapsedTimeMs = (System.nanoTime() - startTime) / 1_000_000;
        minimalMetrics.articleCount = count1.get();
        minimalMetrics.throughput = count1.get() * 1000.0 / minimalMetrics.elapsedTimeMs;
        allMetrics.add(minimalMetrics);
        System.out.println(minimalMetrics + "\n");

        // 시나리오 2: 중간 처리 (Title, Authors 접근)
        System.out.println("시나리오 2: 중간 처리 (Title + Authors)");
        PerformanceMetrics mediumMetrics = new PerformanceMetrics("Medium Processing");
        startTime = System.nanoTime();
        AtomicInteger count2 = new AtomicInteger(0);

        parser.parseStream(xmlFile, article -> {
            count2.incrementAndGet();
            if (article.getMedlineCitation() != null) {
                article.getMedlineCitation().getPmid();
                var art = article.getMedlineCitation().getArticle();
                if (art != null) {
                    art.getArticleTitle();
                    art.getAuthorList();
                }
            }
        });

        mediumMetrics.elapsedTimeMs = (System.nanoTime() - startTime) / 1_000_000;
        mediumMetrics.articleCount = count2.get();
        mediumMetrics.throughput = count2.get() * 1000.0 / mediumMetrics.elapsedTimeMs;
        allMetrics.add(mediumMetrics);
        System.out.println(mediumMetrics + "\n");

        // 시나리오 3: 전체 처리 (모든 필드 접근)
        System.out.println("시나리오 3: 전체 처리 (All fields)");
        PerformanceMetrics fullMetrics = new PerformanceMetrics("Full Processing");
        startTime = System.nanoTime();
        AtomicInteger count3 = new AtomicInteger(0);

        parser.parseStream(xmlFile, article -> {
            count3.incrementAndGet();
            // 전체 데이터 접근 (실제 사용 시나리오)
            processFullArticle(article);
        });

        fullMetrics.elapsedTimeMs = (System.nanoTime() - startTime) / 1_000_000;
        fullMetrics.articleCount = count3.get();
        fullMetrics.throughput = count3.get() * 1000.0 / fullMetrics.elapsedTimeMs;
        allMetrics.add(fullMetrics);
        System.out.println(fullMetrics + "\n");

        // 비교
        System.out.println("========================================");
        System.out.println("처리량 비교 / Throughput Comparison");
        System.out.println("========================================");
        allMetrics.forEach(m ->
            System.out.printf("%-20s: %,10.0f articles/sec%n", m.testName, m.throughput)
        );

        // Assertions
        assertTrue(minimalMetrics.throughput > 5000, "Minimal processing should be >5000 articles/sec");
        assertTrue(fullMetrics.throughput > 1000, "Full processing should be >1000 articles/sec");

        // 리포트 생성
        generatePerformanceReport("throughput-benchmark", allMetrics);

        System.out.println("\n✅ 처리량 벤치마크 테스트 통과");
    }

    // ========== Helper Methods ==========

    /**
     * 파싱 성능 측정
     */
    private PerformanceMetrics measureParsingPerformance(
        PubmedXmlParser parser, Path xmlFile, String testName) throws Exception {

        PerformanceMetrics metrics = new PerformanceMetrics(testName);

        long memoryBefore = getUsedMemory();
        long startTime = System.nanoTime();
        AtomicInteger count = new AtomicInteger(0);

        parser.parseStream(xmlFile, article -> {
            count.incrementAndGet();
        });

        long endTime = System.nanoTime();
        long memoryAfter = getUsedMemory();

        metrics.articleCount = count.get();
        metrics.elapsedTimeMs = (endTime - startTime) / 1_000_000;
        metrics.memoryBefore = memoryBefore;
        metrics.memoryAfter = memoryAfter;
        metrics.memoryUsed = memoryAfter - memoryBefore;
        metrics.throughput = count.get() * 1000.0 / metrics.elapsedTimeMs;
        metrics.fileSize = Files.size(xmlFile);

        return metrics;
    }

    /**
     * 전체 Article 처리 (실제 사용 시나리오 시뮬레이션)
     */
    private void processFullArticle(PubmedArticle article) {
        if (article.getMedlineCitation() == null) return;

        var citation = article.getMedlineCitation();
        citation.getPmid();
        citation.getStatus();
        citation.getDateCompleted();
        citation.getDateRevised();

        if (citation.getArticle() != null) {
            var art = citation.getArticle();
            art.getArticleTitle();
            art.getAbstractInfo();
            art.getAuthorList();
            art.getJournal();
            art.getPublicationTypeList();
        }

        citation.getMeshHeadingList();
        citation.getKeywordLists();
        citation.getChemicalList();
    }

    /**
     * 압축 해제
     */
    private void decompressFile(Path gzipFile, Path outputFile) throws IOException {
        try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(gzipFile.toFile()));
             FileOutputStream fos = new FileOutputStream(outputFile.toFile())) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = gzis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }

    /**
     * 사용 중인 메모리 측정
     */
    private long getUsedMemory() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * 바이트 포맷팅
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + "B";
        if (bytes < 1024 * 1024) return String.format("%.1fKB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1fMB", bytes / (1024.0 * 1024));
        return String.format("%.2fGB", bytes / (1024.0 * 1024 * 1024));
    }

    /**
     * 성능 리포트 생성
     */
    private void generatePerformanceReport(String testName, List<PerformanceMetrics> allMetrics)
        throws IOException {

        Files.createDirectories(Paths.get(DOC_DIR));

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path reportFile = Paths.get(DOC_DIR, testName + "-" + timestamp + ".md");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile.toFile()))) {
            writer.write("# PubMed Performance Test Report\n\n");
            writer.write(String.format("**테스트:** %s\n", testName));
            writer.write(String.format("**실행 일시:** %s\n\n", LocalDateTime.now().format(TIMESTAMP_FORMAT)));
            writer.write("---\n\n");

            writer.write("## 성능 측정 결과 / Performance Results\n\n");
            writer.write("| 테스트 | 논문 수 | 처리 시간 | 처리 속도 | 메모리 사용 | 파일 크기 |\n");
            writer.write("|--------|---------|-----------|-----------|-------------|----------|\n");

            for (PerformanceMetrics m : allMetrics) {
                writer.write(String.format("| %s | %,d | %.2fs | %,.0f/s | %s | %s |\n",
                    m.testName,
                    m.articleCount,
                    m.elapsedTimeMs / 1000.0,
                    m.throughput,
                    m.memoryUsed > 0 ? formatBytes(m.memoryUsed) : "N/A",
                    m.fileSize > 0 ? formatBytes(m.fileSize) : "N/A"
                ));
            }

            writer.write("\n## 상세 메트릭 / Detailed Metrics\n\n");
            for (PerformanceMetrics m : allMetrics) {
                writer.write(String.format("### %s\n\n", m.testName));
                writer.write("```\n");
                writer.write(m.toString());
                writer.write("\n```\n\n");
            }

            writer.write("---\n\n");
            writer.write("**생성 시각:** " + LocalDateTime.now().format(TIMESTAMP_FORMAT) + "\n");
        }

        System.out.printf("📄 성능 리포트 생성: %s%n", reportFile.getFileName());
    }

    /**
     * 성능 메트릭 / Performance metrics
     */
    private static class PerformanceMetrics {
        String testName;
        int articleCount;
        long elapsedTimeMs;
        long memoryBefore;
        long memoryAfter;
        long memoryUsed;
        double throughput;
        long fileSize;

        PerformanceMetrics(String testName) {
            this.testName = testName;
        }

        @Override
        public String toString() {
            return String.format(
                "테스트: %s\n" +
                "  논문 수: %,d\n" +
                "  처리 시간: %.3fs\n" +
                "  처리 속도: %,.0f articles/sec\n" +
                "  메모리 사용: %s\n" +
                "  평균 메모리/article: %.2fKB",
                testName,
                articleCount,
                elapsedTimeMs / 1000.0,
                throughput,
                formatBytes(memoryUsed),
                articleCount > 0 ? memoryUsed / 1024.0 / articleCount : 0
            );
        }

        private String formatBytes(long bytes) {
            if (bytes < 1024) return bytes + "B";
            if (bytes < 1024 * 1024) return String.format("%.1fKB", bytes / 1024.0);
            if (bytes < 1024 * 1024 * 1024) return String.format("%.1fMB", bytes / (1024.0 * 1024));
            return String.format("%.2fGB", bytes / (1024.0 * 1024 * 1024));
        }
    }
}
