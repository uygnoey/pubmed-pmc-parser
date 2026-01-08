package com.brillianttigercorp.bioxml.parser;

import com.brillianttigercorp.bioxml.parser.pmc.model.PmcArticle;
import com.brillianttigercorp.bioxml.parser.pmc.model.PmcArticleSet;
import com.brillianttigercorp.bioxml.parser.pmc.parser.PmcXmlParser;
import com.brillianttigercorp.bioxml.parser.pubmed.model.PubmedArticle;
import com.brillianttigercorp.bioxml.parser.pubmed.model.PubmedArticleSet;
import com.brillianttigercorp.bioxml.parser.pubmed.model.PubmedBookArticle;
import com.brillianttigercorp.bioxml.parser.pubmed.parser.PubmedXmlParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main / CLI 메인 클래스
 *
 * KR: PubMed/PMC XML 파일을 파싱하고 통계를 출력하는 CLI 애플리케이션
 * EN: CLI application for parsing PubMed/PMC XML files and printing statistics
 *
 * Usage:
 *   java -jar pubmed-pmc-parser.jar <xml-file-path> [options]
 *
 * Options:
 *   --type=pubmed   Parse as PubMed XML (default)
 *   --type=pmc      Parse as PMC XML
 *   --stream        Use streaming mode (memory efficient)
 */
public class Main {

    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");
    private static final DecimalFormat TIME_FORMAT = new DecimalFormat("#,##0.00");

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        try {
            // 인자 파싱 / Parse arguments
            String filePath = args[0];
            String type = getOption(args, "--type", "pubmed");
            boolean streamMode = hasOption(args, "--stream");

            // 파일 존재 확인 / Check file exists
            Path xmlPath = Paths.get(filePath);
            if (!Files.exists(xmlPath)) {
                System.err.println("❌ 파일을 찾을 수 없습니다 / File not found: " + filePath);
                System.exit(1);
            }

            // 파일 정보 출력 / Print file info
            printFileInfo(xmlPath);

            // 타입에 따라 파싱 / Parse by type
            if ("pmc".equalsIgnoreCase(type)) {
                parsePmcXml(xmlPath, streamMode);
            } else {
                parsePubmedXml(xmlPath, streamMode);
            }

        } catch (Exception e) {
            System.err.println("\n❌ 오류 발생 / Error occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * PubMed XML 파싱 / Parse PubMed XML
     */
    private static void parsePubmedXml(Path xmlPath, boolean streamMode) throws Exception {
        System.out.println("\n📄 PubMed XML 파싱 시작 / Starting PubMed XML parsing...");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        PubmedXmlParser parser = new PubmedXmlParser();
        long startTime = System.currentTimeMillis();

        if (streamMode) {
            // 스트리밍 모드 / Streaming mode
            parseStreamingPubmed(parser, xmlPath, startTime);
        } else {
            // 전체 파싱 모드 / Full parsing mode
            parseFullPubmed(parser, xmlPath, startTime);
        }
    }

    /**
     * PubMed 전체 파싱 / Full PubMed parsing
     */
    private static void parseFullPubmed(PubmedXmlParser parser, Path xmlPath, long startTime) throws Exception {
        System.out.println("📦 전체 파싱 모드 / Full parsing mode");
        System.out.println();

        PubmedArticleSet articleSet = parser.parse(xmlPath);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // 통계 계산 / Calculate statistics
        int articleCount = articleSet.getPubmedArticles() != null ? articleSet.getPubmedArticles().size() : 0;
        int bookArticleCount = articleSet.getPubmedBookArticles() != null ? articleSet.getPubmedBookArticles().size() : 0;
        int deletedPmidCount = 0;

        if (articleSet.getDeleteCitation() != null && articleSet.getDeleteCitation().getPmids() != null) {
            deletedPmidCount = articleSet.getDeleteCitation().getPmids().size();
        }

        // 통계 출력 / Print statistics
        printStatistics(articleCount, bookArticleCount, deletedPmidCount, elapsedTime);
    }

    /**
     * PubMed 스트리밍 파싱 / Streaming PubMed parsing
     */
    private static void parseStreamingPubmed(PubmedXmlParser parser, Path xmlPath, long startTime) throws Exception {
        System.out.println("⚡ 스트리밍 모드 (메모리 효율적) / Streaming mode (memory efficient)");
        System.out.println();

        AtomicInteger articleCount = new AtomicInteger(0);
        AtomicInteger bookArticleCount = new AtomicInteger(0);

        // Article 카운터 / Article counter
        parser.parseStream(xmlPath,
                article -> {
                    int count = articleCount.incrementAndGet();
                    if (count % 1000 == 0) {
                        System.out.print("\r처리 중 / Processing: " + NUMBER_FORMAT.format(count) + " articles");
                    }
                },
                bookArticle -> {
                    bookArticleCount.incrementAndGet();
                }
        );

        System.out.println(); // 줄바꿈 / New line

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // 통계 출력 / Print statistics
        printStatistics(articleCount.get(), bookArticleCount.get(), 0, elapsedTime);
        System.out.println("\n⚠️  스트리밍 모드에서는 DeleteCitation을 파싱하지 않습니다.");
        System.out.println("    Streaming mode does not parse DeleteCitation.");
    }

    /**
     * PMC XML 파싱 / Parse PMC XML
     */
    private static void parsePmcXml(Path xmlPath, boolean streamMode) throws Exception {
        System.out.println("\n📄 PMC XML 파싱 시작 / Starting PMC XML parsing...");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        PmcXmlParser parser = new PmcXmlParser();
        long startTime = System.currentTimeMillis();

        if (streamMode) {
            // 스트리밍 모드 / Streaming mode
            parseStreamingPmc(parser, xmlPath, startTime);
        } else {
            // 전체 파싱 모드 / Full parsing mode
            parseFullPmc(parser, xmlPath, startTime);
        }
    }

    /**
     * PMC 전체 파싱 / Full PMC parsing
     */
    private static void parseFullPmc(PmcXmlParser parser, Path xmlPath, long startTime) throws Exception {
        System.out.println("📦 전체 파싱 모드 / Full parsing mode");
        System.out.println();

        PmcArticleSet articleSet = parser.parse(xmlPath);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // 통계 계산 / Calculate statistics
        int articleCount = articleSet.getArticles() != null ? articleSet.getArticles().size() : 0;

        // PMC 통계 출력 / Print PMC statistics
        printPmcStatistics(articleCount, elapsedTime);
    }

    /**
     * PMC 스트리밍 파싱 / Streaming PMC parsing
     */
    private static void parseStreamingPmc(PmcXmlParser parser, Path xmlPath, long startTime) throws Exception {
        System.out.println("⚡ 스트리밍 모드 (메모리 효율적) / Streaming mode (memory efficient)");
        System.out.println();

        AtomicInteger articleCount = new AtomicInteger(0);

        // Article 카운터 / Article counter
        parser.parseStream(xmlPath, article -> {
            int count = articleCount.incrementAndGet();
            if (count % 100 == 0) {
                System.out.print("\r처리 중 / Processing: " + NUMBER_FORMAT.format(count) + " articles");
            }
        });

        System.out.println(); // 줄바꿈 / New line

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // PMC 통계 출력 / Print PMC statistics
        printPmcStatistics(articleCount.get(), elapsedTime);
    }

    /**
     * PubMed 통계 출력 / Print PubMed statistics
     */
    private static void printStatistics(int articleCount, int bookArticleCount, int deletedPmidCount, long elapsedTime) {
        System.out.println("\n✅ 파싱 완료 / Parsing completed");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println();
        System.out.println("📊 통계 / Statistics:");
        System.out.println("  ├─ PubmedArticle:      " + NUMBER_FORMAT.format(articleCount) + " articles");
        System.out.println("  ├─ PubmedBookArticle:  " + NUMBER_FORMAT.format(bookArticleCount) + " book articles");
        System.out.println("  └─ DeleteCitation:     " + NUMBER_FORMAT.format(deletedPmidCount) + " deleted PMIDs");
        System.out.println();

        int totalArticles = articleCount + bookArticleCount;
        System.out.println("📈 총 처리 / Total processed: " + NUMBER_FORMAT.format(totalArticles) + " articles");
        System.out.println();

        printPerformanceMetrics(totalArticles, elapsedTime);
    }

    /**
     * PMC 통계 출력 / Print PMC statistics
     */
    private static void printPmcStatistics(int articleCount, long elapsedTime) {
        System.out.println("\n✅ 파싱 완료 / Parsing completed");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println();
        System.out.println("📊 통계 / Statistics:");
        System.out.println("  └─ PMC Articles:  " + NUMBER_FORMAT.format(articleCount) + " articles");
        System.out.println();

        printPerformanceMetrics(articleCount, elapsedTime);
    }

    /**
     * 성능 지표 출력 / Print performance metrics
     */
    private static void printPerformanceMetrics(int totalArticles, long elapsedTime) {
        double seconds = elapsedTime / 1000.0;
        double articlesPerSecond = totalArticles / seconds;

        System.out.println("⏱️  처리 시간 / Processing time:");
        System.out.println("  ├─ 총 시간 / Total:        " + TIME_FORMAT.format(seconds) + " seconds");
        System.out.println("  ├─ 처리량 / Throughput:    " + TIME_FORMAT.format(articlesPerSecond) + " articles/sec");

        if (seconds > 60) {
            double minutes = seconds / 60.0;
            System.out.println("  └─ (약 " + TIME_FORMAT.format(minutes) + " 분 / minutes)");
        }

        System.out.println();
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * 파일 정보 출력 / Print file information
     */
    private static void printFileInfo(Path xmlPath) throws Exception {
        long fileSize = Files.size(xmlPath);
        String fileName = xmlPath.getFileName().toString();
        boolean isGzipped = fileName.toLowerCase().endsWith(".gz");

        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("📁 파일 정보 / File Information");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  파일명 / Filename:  " + fileName);
        System.out.println("  경로 / Path:        " + xmlPath.toAbsolutePath());
        System.out.println("  크기 / Size:        " + formatFileSize(fileSize));
        System.out.println("  압축 / Compressed:  " + (isGzipped ? "Yes (GZip)" : "No"));
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * 파일 크기 포맷팅 / Format file size
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return TIME_FORMAT.format(bytes / 1024.0) + " KB";
        } else if (bytes < 1024 * 1024 * 1024) {
            return TIME_FORMAT.format(bytes / (1024.0 * 1024.0)) + " MB";
        } else {
            return TIME_FORMAT.format(bytes / (1024.0 * 1024.0 * 1024.0)) + " GB";
        }
    }

    /**
     * 옵션 값 가져오기 / Get option value
     */
    private static String getOption(String[] args, String optionName, String defaultValue) {
        for (String arg : args) {
            if (arg.startsWith(optionName + "=")) {
                return arg.substring(optionName.length() + 1);
            }
        }
        return defaultValue;
    }

    /**
     * 옵션 존재 확인 / Check if option exists
     */
    private static boolean hasOption(String[] args, String optionName) {
        for (String arg : args) {
            if (arg.equals(optionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 사용법 출력 / Print usage
     */
    private static void printUsage() {
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  PubMed & PMC XML Parser");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println();
        System.out.println("사용법 / Usage:");
        System.out.println("  java -jar pubmed-pmc-parser.jar <xml-file-path> [options]");
        System.out.println();
        System.out.println("옵션 / Options:");
        System.out.println("  --type=pubmed   PubMed XML로 파싱 (기본값)");
        System.out.println("                  Parse as PubMed XML (default)");
        System.out.println();
        System.out.println("  --type=pmc      PMC XML로 파싱");
        System.out.println("                  Parse as PMC XML");
        System.out.println();
        System.out.println("  --stream        스트리밍 모드 사용 (메모리 효율적)");
        System.out.println("                  Use streaming mode (memory efficient)");
        System.out.println();
        System.out.println("예시 / Examples:");
        System.out.println("  # PubMed XML 전체 파싱");
        System.out.println("  java -jar pubmed-pmc-parser.jar pubmed24n0001.xml.gz");
        System.out.println();
        System.out.println("  # PubMed XML 스트리밍 파싱");
        System.out.println("  java -jar pubmed-pmc-parser.jar pubmed24n0001.xml.gz --stream");
        System.out.println();
        System.out.println("  # PMC XML 파싱");
        System.out.println("  java -jar pubmed-pmc-parser.jar pmc_article.xml --type=pmc");
        System.out.println();
        System.out.println("  # PMC XML 스트리밍 파싱");
        System.out.println("  java -jar pubmed-pmc-parser.jar pmc_article.xml --type=pmc --stream");
        System.out.println();
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
