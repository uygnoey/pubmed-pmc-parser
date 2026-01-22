package com.brillianttiger.bio.parser.pubmed;

import com.brillianttiger.bio.parser.common.util.Md5Verifier;
import com.brillianttiger.bio.parser.pubmed.model.PubmedArticle;
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * PubmedIntegrationTest / PubMed 통합 테스트
 *
 * KR: 실제 PubMed baseline/update 파일로 전체 파싱 및 MD5 검증
 * EN: Integration test with real PubMed baseline/update files and MD5 verification
 *
 * 테스트 내용 / Test Coverage:
 * - 실제 PubMed baseline 파일 (pubmed25n0001.xml.gz) 전체 파싱
 * - 실제 PubMed update 파일 전체 파싱
 * - MD5 체크섬 검증
 * - 파싱 성공률 측정
 * - 데이터 무결성 검증
 */
class PubmedIntegrationTest {

    private static final String BASE_DIR = "test-data/pubmed";
    private static final String DOC_DIR = "claudedocs/integration-test-results";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 실제 PubMed baseline 파일 전체 파싱 테스트
     * Real PubMed baseline file complete parsing test
     */
    @Test
    void testParseRealBaselineFile() throws Exception {
        Path xmlFile = Paths.get(BASE_DIR, "baseline", "pubmed25n0001.xml.gz");
        assumeTrue(Files.exists(xmlFile), "Skipping integration test - test file not found: " + xmlFile);

        System.out.println("========================================");
        System.out.println("PubMed Baseline Integration Test");
        System.out.println("========================================\n");

        // MD5 검증 / MD5 verification
        System.out.println("1. MD5 체크섬 검증 / MD5 Checksum Verification");
        boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);
        assertTrue(md5Valid, "MD5 checksum should be valid");
        System.out.println("   ✅ MD5 검증 성공 / MD5 verification passed\n");

        // 전체 파싱 / Complete parsing
        System.out.println("2. 전체 파일 파싱 / Complete File Parsing");
        PubmedXmlParser parser = new PubmedXmlParser();
        List<ParseResult> results = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        parser.parseStream(xmlFile, article -> {
            int num = count.incrementAndGet();

            try {
                // 데이터 무결성 검증 / Data integrity validation
                validateArticle(article);
                results.add(new ParseResult(num, true, null, article));

                if (num % 1000 == 0) {
                    System.out.printf("   파싱 진행: %,d articles...%n", num);
                }
            } catch (Exception e) {
                errorCount.incrementAndGet();
                results.add(new ParseResult(num, false, e.getMessage(), article));
                System.err.printf("   ⚠️  Article #%d 오류: %s%n", num, e.getMessage());
            }
        });

        long elapsed = System.currentTimeMillis() - startTime;
        double throughput = count.get() * 1000.0 / elapsed;

        System.out.printf("   파싱 완료: %,d articles in %.2fs%n", count.get(), elapsed / 1000.0);
        System.out.printf("   처리 속도: %,.0f articles/sec%n", throughput);
        System.out.printf("   오류 건수: %,d (%.2f%%)%n%n",
            errorCount.get(), errorCount.get() * 100.0 / count.get());

        // Assertions
        assertTrue(count.get() > 0, "Should parse at least one article");
        assertTrue(count.get() > 25000, "Baseline file should contain >25000 articles");
        assertTrue(errorCount.get() < count.get() * 0.01, "Error rate should be <1%");

        // 결과 문서 생성 / Generate result document
        generateIntegrationTestReport("baseline-pubmed25n0001", results, elapsed, xmlFile);

        System.out.println("✅ Baseline 파일 통합 테스트 성공");
    }

    /**
     * 실제 PubMed update 파일 전체 파싱 테스트
     * Real PubMed update file complete parsing test
     */
    @Test
    void testParseRealUpdateFile() throws Exception {
        Path xmlFile = Paths.get(BASE_DIR, "update", "pubmed25n1275.xml.gz");
        assumeTrue(Files.exists(xmlFile), "Skipping integration test - test file not found: " + xmlFile);

        System.out.println("========================================");
        System.out.println("PubMed Update Integration Test");
        System.out.println("========================================\n");

        // MD5 검증
        System.out.println("1. MD5 체크섬 검증");
        boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);
        assertTrue(md5Valid, "MD5 checksum should be valid");
        System.out.println("   ✅ MD5 검증 성공\n");

        // 전체 파싱
        System.out.println("2. 전체 파일 파싱");
        PubmedXmlParser parser = new PubmedXmlParser();
        List<ParseResult> results = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger errorCount = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        parser.parseStream(xmlFile, article -> {
            int num = count.incrementAndGet();

            try {
                validateArticle(article);
                results.add(new ParseResult(num, true, null, article));

                if (num % 500 == 0) {
                    System.out.printf("   파싱 진행: %,d articles...%n", num);
                }
            } catch (Exception e) {
                errorCount.incrementAndGet();
                results.add(new ParseResult(num, false, e.getMessage(), article));
            }
        });

        long elapsed = System.currentTimeMillis() - startTime;
        double throughput = count.get() * 1000.0 / elapsed;

        System.out.printf("   파싱 완료: %,d articles in %.2fs%n", count.get(), elapsed / 1000.0);
        System.out.printf("   처리 속도: %,.0f articles/sec%n", throughput);
        System.out.printf("   오류 건수: %,d (%.2f%%)%n%n",
            errorCount.get(), errorCount.get() * 100.0 / count.get());

        // Assertions
        assertTrue(count.get() > 0, "Should parse at least one article");
        assertTrue(errorCount.get() < count.get() * 0.01, "Error rate should be <1%");

        // 결과 문서 생성
        generateIntegrationTestReport("update-pubmed25n1275", results, elapsed, xmlFile);

        System.out.println("✅ Update 파일 통합 테스트 성공");
    }

    /**
     * 여러 파일 배치 처리 테스트
     * Batch processing test with multiple files
     */
    @Test
    void testBatchProcessing() throws Exception {
        String[] files = {
            "baseline/pubmed25n0001.xml.gz",
            "baseline/pubmed25n1274.xml.gz",
            "update/pubmed25n1275.xml.gz",
            "update/pubmed25n1685.xml.gz"
        };

        // At least one file should exist for this test to run
        boolean anyFileExists = false;
        for (String file : files) {
            if (Files.exists(Paths.get(BASE_DIR, file))) {
                anyFileExists = true;
                break;
            }
        }
        assumeTrue(anyFileExists, "Skipping batch processing test - no test files found in " + BASE_DIR);

        System.out.println("========================================");
        System.out.println("PubMed Batch Processing Test");
        System.out.println("========================================\n");

        PubmedXmlParser parser = new PubmedXmlParser();
        int totalArticles = 0;
        int totalErrors = 0;
        long totalTime = 0;

        for (String file : files) {
            Path xmlFile = Paths.get(BASE_DIR, file);
            if (!Files.exists(xmlFile)) {
                System.out.printf("⚠️  파일 없음: %s (스킵)%n", file);
                continue;
            }

            System.out.printf("파싱: %s%n", file);

            // MD5 검증
            boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);
            assertTrue(md5Valid, "MD5 should be valid for: " + file);

            // 파싱
            AtomicInteger count = new AtomicInteger(0);
            AtomicInteger errorCount = new AtomicInteger(0);
            long startTime = System.currentTimeMillis();

            parser.parseStream(xmlFile, article -> {
                count.incrementAndGet();
                try {
                    validateArticle(article);
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                }
            });

            long elapsed = System.currentTimeMillis() - startTime;

            System.out.printf("  ✅ %,d articles (%.2fs, %,d errors)%n%n",
                count.get(), elapsed / 1000.0, errorCount.get());

            totalArticles += count.get();
            totalErrors += errorCount.get();
            totalTime += elapsed;
        }

        System.out.println("========================================");
        System.out.println("배치 처리 결과 / Batch Processing Results");
        System.out.println("========================================");
        System.out.printf("총 논문: %,d%n", totalArticles);
        System.out.printf("총 오류: %,d (%.3f%%)%n", totalErrors, totalErrors * 100.0 / totalArticles);
        System.out.printf("총 시간: %.2fs%n", totalTime / 1000.0);
        System.out.printf("평균 처리 속도: %,.0f articles/sec%n", totalArticles * 1000.0 / totalTime);

        assertTrue(totalArticles > 50000, "Should process >50k articles in batch");
        assertTrue(totalErrors < totalArticles * 0.01, "Batch error rate should be <1%");
    }

    /**
     * Article 데이터 무결성 검증
     * Validate article data integrity
     */
    private void validateArticle(PubmedArticle article) {
        assertNotNull(article, "Article should not be null");
        assertNotNull(article.getMedlineCitation(), "MedlineCitation should not be null");
        assertNotNull(article.getMedlineCitation().getPmid(), "PMID should not be null");
        assertNotNull(article.getMedlineCitation().getPmid().getValue(), "PMID value should not be null");

        // Article 존재 시 기본 필드 검증
        if (article.getMedlineCitation().getArticle() != null) {
            assertNotNull(article.getMedlineCitation().getArticle().getArticleTitle(),
                "Article title should not be null");
        }
    }

    /**
     * 통합 테스트 결과 리포트 생성
     * Generate integration test report
     */
    private void generateIntegrationTestReport(String testName, List<ParseResult> results,
                                                long elapsedMs, Path xmlFile) throws Exception {
        Files.createDirectories(Paths.get(DOC_DIR));

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path reportFile = Paths.get(DOC_DIR, testName + "-integration-test-" + timestamp + ".md");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile.toFile()))) {
            writer.write("# PubMed Integration Test Report\n\n");
            writer.write(String.format("**테스트 파일:** %s\n", xmlFile.getFileName()));
            writer.write(String.format("**파일 크기:** %s\n", formatBytes(Files.size(xmlFile))));
            writer.write(String.format("**실행 일시:** %s\n", LocalDateTime.now().format(TIMESTAMP_FORMAT)));
            writer.write(String.format("**처리 시간:** %.2f초\n\n", elapsedMs / 1000.0));

            writer.write("---\n\n");

            // 통계
            long successCount = results.stream().filter(r -> r.success).count();
            long errorCount = results.stream().filter(r -> !r.success).count();
            double successRate = successCount * 100.0 / results.size();

            writer.write("## 파싱 통계 / Parsing Statistics\n\n");
            writer.write("| 항목 | 값 |\n");
            writer.write("|------|----|\n");
            writer.write(String.format("| 총 논문 수 | %,d |\n", results.size()));
            writer.write(String.format("| 성공 | %,d (%.2f%%) |\n", successCount, successRate));
            writer.write(String.format("| 실패 | %,d (%.2f%%) |\n", errorCount, errorCount * 100.0 / results.size()));
            writer.write(String.format("| 처리 속도 | %,.0f articles/sec |\n",
                results.size() * 1000.0 / elapsedMs));
            writer.write("\n");

            // 샘플 데이터 (처음 10개)
            writer.write("## 샘플 데이터 / Sample Data (첫 10개)\n\n");
            for (int i = 0; i < Math.min(10, results.size()); i++) {
                ParseResult result = results.get(i);
                writer.write(String.format("### Article #%d\n\n", result.index));

                if (result.success && result.article != null) {
                    if (result.article.getMedlineCitation() != null) {
                        writer.write(String.format("- **PMID:** %s\n",
                            result.article.getMedlineCitation().getPmid().getValue()));

                        if (result.article.getMedlineCitation().getArticle() != null) {
                            var article = result.article.getMedlineCitation().getArticle();
                            if (article.getArticleTitle() != null) {
                                writer.write(String.format("- **Title:** %s\n",
                                    truncate(article.getArticleTitle().getValue(), 100)));
                            }
                        }
                    }
                    writer.write("- **Status:** ✅ Success\n\n");
                } else {
                    writer.write(String.format("- **Status:** ❌ Failed\n"));
                    writer.write(String.format("- **Error:** %s\n\n", result.errorMessage));
                }
            }

            // 오류 목록 (있는 경우)
            if (errorCount > 0) {
                writer.write("## 오류 목록 / Error List\n\n");
                results.stream()
                    .filter(r -> !r.success)
                    .limit(50)  // 최대 50개만
                    .forEach(r -> {
                        try {
                            writer.write(String.format("- Article #%d: %s\n", r.index, r.errorMessage));
                        } catch (Exception e) {
                            // ignore
                        }
                    });
                writer.write("\n");
            }

            writer.write("---\n\n");
            writer.write("**테스트 결과:** ");
            if (successRate >= 99.0) {
                writer.write("✅ PASSED (Success rate >= 99%)\n");
            } else {
                writer.write(String.format("⚠️  WARNING (Success rate: %.2f%%)\n", successRate));
            }
        }

        System.out.printf("📄 리포트 생성: %s%n", reportFile.getFileName());
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + "B";
        if (bytes < 1024 * 1024) return String.format("%.1fKB", bytes / 1024.0);
        return String.format("%.1fMB", bytes / (1024.0 * 1024));
    }

    private String truncate(String s, int maxLen) {
        if (s == null) return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen) + "...";
    }

    /**
     * 파싱 결과 / Parse result
     */
    private static class ParseResult {
        final int index;
        final boolean success;
        final String errorMessage;
        final PubmedArticle article;

        ParseResult(int index, boolean success, String errorMessage, PubmedArticle article) {
            this.index = index;
            this.success = success;
            this.errorMessage = errorMessage;
            this.article = article;
        }
    }
}
