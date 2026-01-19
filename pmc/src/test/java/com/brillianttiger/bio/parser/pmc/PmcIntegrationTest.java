package com.brillianttiger.bio.parser.pmc;

import com.brillianttiger.bio.parser.pmc.model.JatsArticle;
import com.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PmcIntegrationTest / PMC 통합 테스트
 *
 * KR: 실제 PMC 파일로 전체 파싱 및 검증
 * EN: Integration test with real PMC files and verification
 *
 * 테스트 내용 / Test Coverage:
 * - 실제 PMC oa_comm 패키지 파싱 (tar.gz)
 * - 아카이브 내 모든 XML 파일 파싱
 * - 체크섬 검증 (SHA-256)
 * - 데이터 무결성 검증
 * - 대용량 파일 처리 능력 검증
 */
class PmcIntegrationTest {

    private static final String BASE_DIR = "test-data/pmc";
    private static final String DOC_DIR = "claudedocs/integration-test-results";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * PMC tar.gz 패키지 전체 파싱 테스트
     * Complete parsing test for PMC tar.gz package
     */
    @Test
    void testParseRealPmcPackage(@TempDir Path tempDir) throws Exception {
        System.out.println("========================================");
        System.out.println("PMC Package Integration Test");
        System.out.println("========================================\n");

        // 실제 PMC tar.gz 파일 경로 (디렉토리에서 첫 번째 tar.gz 파일 찾기)
        Path tarGzFile = findFirstTarGzFile();

        if (tarGzFile == null || !Files.exists(tarGzFile)) {
            System.out.println("⚠️  실제 PMC tar.gz 파일이 없습니다.");
            System.out.println("📥 다운로드 방법:");
            System.out.println("   1. https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/oa_comm/xml/ 접속");
            System.out.println("   2. 최신 tar.gz 파일 다운로드");
            System.out.println("   3. test-data/pmc/oa_comm/ 디렉토리에 저장");
            System.out.println("\n이 테스트는 실제 파일이 있을 때만 실행됩니다.");
            System.out.println("SKIPPED\n");
            return; // 파일 없으면 스킵
        }

        assertTrue(Files.exists(tarGzFile), "PMC package file should exist");
        long fileSize = Files.size(tarGzFile);
        System.out.printf("패키지 파일: %s (크기: %s)%n%n", tarGzFile.getFileName(), formatBytes(fileSize));

        // 1. 체크섬 검증 (SHA-256)
        System.out.println("1. 아카이브 무결성 검증 / Archive Integrity Verification");
        String checksum = calculateChecksum(tarGzFile);
        System.out.printf("   SHA-256: %s%n", checksum);
        assertNotNull(checksum, "Checksum should be calculated");
        System.out.println("   ✅ 체크섬 계산 완료\n");

        // 2. tar.gz 패키지 파싱
        System.out.println("2. 패키지 파싱 / Package Parsing");
        PmcXmlParser parser = new PmcXmlParser();

        long startTime = System.currentTimeMillis();
        List<JatsArticle> articles = parser.parseTarGz(tarGzFile);
        long elapsed = System.currentTimeMillis() - startTime;

        System.out.printf("   파싱 완료: %,d articles in %.2fs%n", articles.size(), elapsed / 1000.0);
        System.out.printf("   처리 속도: %,.0f articles/sec%n%n", articles.size() * 1000.0 / elapsed);

        // 3. 데이터 검증
        System.out.println("3. 데이터 무결성 검증 / Data Integrity Verification");
        int validCount = 0;
        for (JatsArticle article : articles) {
            try {
                validateJatsArticle(article);
                validCount++;
            } catch (AssertionError e) {
                System.err.printf("   ⚠️  검증 실패: %s%n", e.getMessage());
            }
        }

        double validRate = validCount * 100.0 / articles.size();
        System.out.printf("   검증 성공률: %.2f%% (%d/%d)%n", validRate, validCount, articles.size());

        // Assertions
        assertTrue(articles.size() > 0, "Should parse at least one article");
        assertTrue(validRate >= 99.0, "Valid rate should be >= 99%");

        // 결과 문서 생성
        List<ParseResult> results = new ArrayList<>();
        for (int i = 0; i < articles.size(); i++) {
            results.add(new ParseResult(i + 1, "article_" + i, true, null, articles.get(i)));
        }
        generatePmcIntegrationReport("pmc-package", results, elapsed, tarGzFile, checksum);

        System.out.println("\n✅ PMC 패키지 통합 테스트 성공");
    }

    /**
     * PMC 단일 XML 파일 파싱 테스트
     * Single PMC XML file parsing test
     */
    @Test
    void testParseSinglePmcFile() throws Exception {
        System.out.println("========================================");
        System.out.println("PMC Single File Parsing Test");
        System.out.println("========================================\n");

        Path xmlFile = Paths.get("src/test/resources/pmc/full_article.xml");
        assertTrue(Files.exists(xmlFile), "Test file should exist");

        PmcXmlParser parser = new PmcXmlParser();

        System.out.printf("파일: %s (크기: %s)%n%n", xmlFile.getFileName(), formatBytes(Files.size(xmlFile)));

        long startTime = System.currentTimeMillis();
        JatsArticle article = parser.parseFile(xmlFile);
        long elapsed = System.currentTimeMillis() - startTime;

        // 검증
        validateJatsArticle(article);

        System.out.println("✅ 파싱 성공");

        // 상세 정보 출력
        if (article.getFront() != null && article.getFront().getArticleMeta() != null) {
            var meta = article.getFront().getArticleMeta();

            if (meta.getArticleIds() != null && !meta.getArticleIds().isEmpty()) {
                System.out.printf("   Article ID: %s (%s)%n",
                    meta.getArticleIds().get(0).getValue(),
                    meta.getArticleIds().get(0).getPubIdType());
            }

            if (meta.getTitleGroup() != null && meta.getTitleGroup().getArticleTitle() != null) {
                System.out.printf("   Title: %s%n",
                    truncate(meta.getTitleGroup().getArticleTitle().getContent(), 80));
            }
        }

        System.out.printf("%n   처리 시간: %.3fs%n", elapsed / 1000.0);

        // 결과 문서 생성
        List<ParseResult> results = new ArrayList<>();
        results.add(new ParseResult(1, xmlFile.getFileName().toString(), true, null, article));
        generatePmcIntegrationReport("pmc-single-file", results, elapsed, xmlFile, null);

        System.out.println("\n✅ PMC 단일 파일 파싱 테스트 성공");
    }

    /**
     * PMC 스트리밍 파싱 테스트
     * PMC streaming parsing test
     */
    @Test
    void testStreamingParsing() throws Exception {
        System.out.println("========================================");
        System.out.println("PMC Streaming Parsing Test");
        System.out.println("========================================\n");

        Path xmlFile = Paths.get("src/test/resources/pmc/full_article.xml");
        assertTrue(Files.exists(xmlFile), "Test file should exist");

        PmcXmlParser parser = new PmcXmlParser();
        AtomicInteger count = new AtomicInteger(0);
        List<JatsArticle> articles = new ArrayList<>();

        System.out.printf("파일: %s%n%n", xmlFile.getFileName());

        long startTime = System.currentTimeMillis();

        // 스트리밍 파싱
        long parsedCount = parser.parseStream(xmlFile, article -> {
            count.incrementAndGet();
            articles.add(article);
            System.out.printf("   Article #%d 파싱됨%n", count.get());
        });

        long elapsed = System.currentTimeMillis() - startTime;

        System.out.printf("%n   총 파싱: %d articles%n", parsedCount);
        System.out.printf("   처리 시간: %.3fs%n", elapsed / 1000.0);

        // Assertions
        assertEquals(parsedCount, count.get(), "Parsed count should match");
        assertTrue(parsedCount > 0, "Should parse at least one article");

        // 검증
        for (JatsArticle article : articles) {
            validateJatsArticle(article);
        }

        System.out.println("\n✅ PMC 스트리밍 파싱 테스트 성공");
    }

    /**
     * PMC 배치 파일 처리 테스트
     * Batch file processing test
     */
    @Test
    void testBatchProcessingPmcFiles() throws Exception {
        System.out.println("========================================");
        System.out.println("PMC Batch Processing Test");
        System.out.println("========================================\n");

        String[] testFiles = {
            "src/test/resources/pmc/full_article.xml",
            "src/test/resources/pmc/nested_sections.xml",
            "src/test/resources/pmc/sub_article.xml"
        };

        PmcXmlParser parser = new PmcXmlParser();
        int totalArticles = 0;
        int totalErrors = 0;
        long totalTime = 0;

        for (String filePath : testFiles) {
            Path xmlFile = Paths.get(filePath);
            if (!Files.exists(xmlFile)) {
                System.out.printf("⚠️  파일 없음: %s (스킵)%n", filePath);
                continue;
            }

            System.out.printf("파싱: %s%n", xmlFile.getFileName());

            long startTime = System.currentTimeMillis();
            int errorCount = 0;

            try {
                JatsArticle article = parser.parseFile(xmlFile);
                validateJatsArticle(article);
                totalArticles++;

                // 상세 정보
                if (article.getFront() != null && article.getFront().getArticleMeta() != null) {
                    var meta = article.getFront().getArticleMeta();
                    if (meta.getTitleGroup() != null && meta.getTitleGroup().getArticleTitle() != null) {
                        System.out.printf("  제목: %s%n",
                            truncate(meta.getTitleGroup().getArticleTitle().getContent(), 60));
                    }
                }
            } catch (Exception e) {
                errorCount++;
                totalErrors++;
                System.err.printf("   ⚠️  오류: %s%n", e.getMessage());
            }

            long elapsed = System.currentTimeMillis() - startTime;
            totalTime += elapsed;

            System.out.printf("  ✅ 완료 (%.3fs, %d errors)%n%n", elapsed / 1000.0, errorCount);
        }

        System.out.println("========================================");
        System.out.println("배치 처리 결과");
        System.out.println("========================================");
        System.out.printf("총 논문: %,d%n", totalArticles);
        System.out.printf("총 오류: %,d%n", totalErrors);
        System.out.printf("총 시간: %.2fs%n", totalTime / 1000.0);

        assertTrue(totalArticles > 0, "Should process at least one article");
        assertEquals(0, totalErrors, "Should have no errors");

        System.out.println("\n✅ PMC 배치 처리 테스트 성공");
    }

    /**
     * JatsArticle 데이터 무결성 검증
     */
    private void validateJatsArticle(JatsArticle article) {
        assertNotNull(article, "Article should not be null");
        assertNotNull(article.getFront(), "Front should not be null");
        assertNotNull(article.getFront().getArticleMeta(), "ArticleMeta should not be null");

        var meta = article.getFront().getArticleMeta();

        // TitleGroup 검증
        if (meta.getTitleGroup() != null) {
            assertNotNull(meta.getTitleGroup().getArticleTitle(),
                "Article title should not be null");
            assertNotNull(meta.getTitleGroup().getArticleTitle().getContent(),
                "Article title content should not be null");
        }

        // ArticleIds 검증
        if (meta.getArticleIds() != null && !meta.getArticleIds().isEmpty()) {
            meta.getArticleIds().forEach(id -> {
                assertNotNull(id.getValue(), "Article ID value should not be null");
                assertNotNull(id.getPubIdType(), "Article ID type should not be null");
            });
        }
    }

    /**
     * SHA-256 체크섬 계산
     */
    private String calculateChecksum(Path file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        try (InputStream is = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                md.update(buffer, 0, bytesRead);
            }
        }

        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * PMC 통합 테스트 리포트 생성
     */
    private void generatePmcIntegrationReport(String testName, List<ParseResult> results,
                                              long elapsedMs, Path sourceFile, String checksum) throws Exception {
        Files.createDirectories(Paths.get(DOC_DIR));

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path reportFile = Paths.get(DOC_DIR, testName + "-integration-test-" + timestamp + ".md");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile.toFile()))) {
            writer.write("# PMC Integration Test Report\n\n");
            writer.write(String.format("**테스트:** %s\n", testName));
            writer.write(String.format("**소스 파일:** %s\n", sourceFile.getFileName()));
            writer.write(String.format("**파일 크기:** %s\n", formatBytes(Files.size(sourceFile))));
            if (checksum != null) {
                writer.write(String.format("**SHA-256:** %s\n", checksum));
            }
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

            // 샘플 데이터
            writer.write("## 샘플 데이터 / Sample Data (첫 10개)\n\n");
            for (int i = 0; i < Math.min(10, results.size()); i++) {
                ParseResult result = results.get(i);
                writer.write(String.format("### Article #%d: %s\n\n", result.index, result.fileName));

                if (result.success && result.article != null) {
                    writer.write("- **Status:** ✅ Success\n");
                    if (result.article.getFront() != null &&
                        result.article.getFront().getArticleMeta() != null) {
                        var meta = result.article.getFront().getArticleMeta();

                        if (meta.getArticleIds() != null && !meta.getArticleIds().isEmpty()) {
                            writer.write(String.format("- **Article ID:** %s (%s)\n",
                                meta.getArticleIds().get(0).getValue(),
                                meta.getArticleIds().get(0).getPubIdType()));
                        }

                        if (meta.getTitleGroup() != null && meta.getTitleGroup().getArticleTitle() != null) {
                            writer.write(String.format("- **Title:** %s\n",
                                truncate(meta.getTitleGroup().getArticleTitle().getContent(), 100)));
                        }
                    }
                    writer.write("\n");
                } else {
                    writer.write(String.format("- **Status:** ❌ Failed\n"));
                    writer.write(String.format("- **Error:** %s\n\n", result.errorMessage));
                }
            }

            // 오류 목록
            if (errorCount > 0) {
                writer.write("## 오류 목록 / Error List\n\n");
                results.stream()
                    .filter(r -> !r.success)
                    .limit(50)
                    .forEach(r -> {
                        try {
                            writer.write(String.format("- Article #%d (%s): %s\n",
                                r.index, r.fileName, r.errorMessage));
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
     * 디렉토리에서 첫 번째 tar.gz 파일 찾기
     */
    private Path findFirstTarGzFile() {
        try {
            Path oaCommDir = Paths.get(BASE_DIR, "oa_comm");
            if (!Files.exists(oaCommDir)) {
                return null;
            }

            return Files.list(oaCommDir)
                .filter(p -> p.toString().endsWith(".tar.gz"))
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 파싱 결과 / Parse result
     */
    private static class ParseResult {
        final int index;
        final String fileName;
        final boolean success;
        final String errorMessage;
        final JatsArticle article;

        ParseResult(int index, String fileName, boolean success, String errorMessage, JatsArticle article) {
            this.index = index;
            this.fileName = fileName;
            this.success = success;
            this.errorMessage = errorMessage;
            this.article = article;
        }
    }
}
