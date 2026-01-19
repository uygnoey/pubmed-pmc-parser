package examples;

import com.brillianttiger.bio.parser.pubmed.model.*;
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * FindRetractions / 취소/정정 논문 찾기 예제
 *
 * KR: PubMed의 CommentsCorrectionsList를 활용하여 취소(Retraction), 정정(Erratum),
 *     우려표명(Expression of Concern) 등의 논문을 찾는 예제.
 *
 * EN: Example of finding retracted, corrected, or concerning articles
 *     using PubMed's CommentsCorrectionsList.
 *
 * Features:
 *   - Retraction detection (RetractionOf, RetractionIn)
 *   - Expression of Concern detection
 *   - Erratum (correction) detection
 *   - Reference relationship tracking
 *   - Statistics and detailed reporting
 *   - CSV export option
 *
 * Usage:
 *   java examples.FindRetractions <xml-file-path> [--csv output.csv] [--type TYPE]
 *
 * Examples:
 *   java examples.FindRetractions pubmed25n0001.xml.gz
 *   java examples.FindRetractions pubmed25n0001.xml.gz --csv retractions.csv
 *   java examples.FindRetractions pubmed25n0001.xml.gz --type Retraction
 */
public class FindRetractions {

    private static final String SEPARATOR = "=".repeat(80);
    private static final String SUB_SEPARATOR = "-".repeat(80);

    // CommentCorrection 타입 / CommentCorrection types
    private static final Set<String> RETRACTION_TYPES = Set.of(
        "RetractionOf",           // 이 논문이 다른 논문을 취소 / This retracts another
        "RetractionIn"            // 이 논문이 취소됨 / This is retracted
    );

    private static final Set<String> CONCERN_TYPES = Set.of(
        "ExpressionOfConcernIn",  // 우려 표명 / Expression of concern
        "ExpressionOfConcernFor"
    );

    private static final Set<String> ERRATUM_TYPES = Set.of(
        "ErratumFor",            // 정정 / Correction
        "ErratumIn",
        "CorrectionIn"
    );

    private static final Set<String> REPUBLISH_TYPES = Set.of(
        "RepublishedFrom",       // 재출판 / Republication
        "RepublishedIn"
    );

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        // 인자 파싱 / Parse arguments
        Path xmlFile = Paths.get(args[0]);
        Path csvFile = null;
        String filterType = null;

        for (int i = 1; i < args.length; i++) {
            if ("--csv".equals(args[i]) && i + 1 < args.length) {
                csvFile = Paths.get(args[++i]);
            } else if ("--type".equals(args[i]) && i + 1 < args.length) {
                filterType = args[++i];
            }
        }

        try {
            System.out.println(SEPARATOR);
            System.out.println("Retraction & Correction Finder");
            System.out.println(SEPARATOR);
            System.out.println("File: " + xmlFile);
            if (csvFile != null) {
                System.out.println("CSV Output: " + csvFile);
            }
            if (filterType != null) {
                System.out.println("Filter Type: " + filterType);
            }
            System.out.println();

            findRetractions(xmlFile, csvFile, filterType);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * 취소/정정 논문 찾기 / Find retractions and corrections
     */
    private static void findRetractions(Path xmlFile, Path csvFile, String filterType)
            throws Exception {

        System.out.println("Searching for retractions and corrections...");
        System.out.println();

        PubmedXmlParser parser = new PubmedXmlParser();

        // 통계 추적 / Track statistics
        AtomicInteger totalArticles = new AtomicInteger(0);
        Map<String, AtomicInteger> typeCounts = new HashMap<>();
        List<RetractionInfo> retractions = new ArrayList<>();

        // CSV 출력 준비 / Prepare CSV output
        BufferedWriter csvWriter = null;
        if (csvFile != null) {
            csvWriter = Files.newBufferedWriter(csvFile,
                                               StandardOpenOption.CREATE,
                                               StandardOpenOption.TRUNCATE_EXISTING);
            csvWriter.write("PMID,Title,Type,RefPMID,RefSource,Note\n");
        }

        BufferedWriter finalCsvWriter = csvWriter;
        String finalFilterType = filterType;

        // 스트리밍 파싱 / Stream parsing
        parser.parseStreamBatch(xmlFile, 100, batch -> {
            try {
                processBatch(batch, retractions, typeCounts,
                           finalCsvWriter, finalFilterType, totalArticles);
            } catch (Exception e) {
                System.err.println("Error processing batch: " + e.getMessage());
            }
        });

        // CSV 마무리 / Close CSV
        if (csvWriter != null) {
            csvWriter.close();
        }

        // 결과 출력 / Print results
        printResults(totalArticles.get(), retractions, typeCounts, csvFile);
    }

    /**
     * 배치 처리 / Process batch
     */
    private static void processBatch(
            List<PubmedArticle> batch,
            List<RetractionInfo> retractions,
            Map<String, AtomicInteger> typeCounts,
            BufferedWriter csvWriter,
            String filterType,
            AtomicInteger totalArticles) throws IOException {

        for (PubmedArticle article : batch) {
            totalArticles.incrementAndGet();

            MedlineCitation citation = article.getMedlineCitation();
            if (citation == null) {
                continue;
            }

            CommentsCorrectionsList ccList = citation.getCommentsCorrectionsList();
            if (ccList == null || ccList.getCommentsCorrections() == null ||
                ccList.getCommentsCorrections().isEmpty()) {
                continue;
            }

            // PMID와 Title 추출 / Extract PMID and title
            String pmid = citation.getPmid().getValue();
            String title = "No Title";
            if (citation.getArticle() != null &&
                citation.getArticle().getArticleTitle() != null) {
                title = citation.getArticle().getArticleTitle().getValue();
            }

            // CommentCorrections 처리 / Process CommentsCorrections
            for (CommentsCorrections cc : ccList.getCommentsCorrections()) {
                String refType = cc.getRefType();
                if (refType == null) {
                    continue;
                }

                // 필터 적용 / Apply filter
                if (filterType != null && !isMatchingType(refType, filterType)) {
                    continue;
                }

                // 관심 타입만 처리 / Process only types of interest
                if (!isInterestingType(refType)) {
                    continue;
                }

                // 통계 업데이트 / Update statistics
                typeCounts.computeIfAbsent(refType, k -> new AtomicInteger(0))
                         .incrementAndGet();

                // 참조 정보 / Reference info
                String refPmid = cc.getPmid() != null ? cc.getPmid().getValue() : "";
                String refSource = cc.getRefSource() != null ? cc.getRefSource().getValue() : "";
                String note = cc.getNote() != null ? cc.getNote() : "";

                // RetractionInfo 생성 / Create RetractionInfo
                RetractionInfo info = new RetractionInfo(
                    pmid, title, refType, refPmid, refSource, note
                );
                retractions.add(info);

                // CSV 출력 / Write to CSV
                if (csvWriter != null) {
                    csvWriter.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        escapeCsv(pmid),
                        escapeCsv(title),
                        escapeCsv(refType),
                        escapeCsv(refPmid),
                        escapeCsv(refSource),
                        escapeCsv(note)
                    ));
                }

                // 콘솔 출력 (첫 10개만) / Console output (first 10 only)
                if (retractions.size() <= 10) {
                    System.out.println(formatRetractionInfo(info));
                }
            }
        }
    }

    /**
     * 타입 매칭 확인 / Check type matching
     */
    private static boolean isMatchingType(String refType, String filterType) {
        String filter = filterType.toLowerCase();
        String type = refType.toLowerCase();

        if (filter.equals("retraction")) {
            return RETRACTION_TYPES.stream().anyMatch(t -> t.toLowerCase().contains(type));
        } else if (filter.equals("concern")) {
            return CONCERN_TYPES.stream().anyMatch(t -> t.toLowerCase().contains(type));
        } else if (filter.equals("erratum") || filter.equals("correction")) {
            return ERRATUM_TYPES.stream().anyMatch(t -> t.toLowerCase().contains(type));
        } else if (filter.equals("republish")) {
            return REPUBLISH_TYPES.stream().anyMatch(t -> t.toLowerCase().contains(type));
        } else {
            return type.contains(filter);
        }
    }

    /**
     * 관심 타입 확인 / Check if type is interesting
     */
    private static boolean isInterestingType(String refType) {
        return RETRACTION_TYPES.contains(refType) ||
               CONCERN_TYPES.contains(refType) ||
               ERRATUM_TYPES.contains(refType) ||
               REPUBLISH_TYPES.contains(refType);
    }

    /**
     * RetractionInfo 포맷 / Format RetractionInfo
     */
    private static String formatRetractionInfo(RetractionInfo info) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_SEPARATOR).append("\n");
        sb.append("PMID: ").append(info.pmid).append("\n");
        sb.append("Title: ").append(info.title).append("\n");
        sb.append("Type: ").append(info.refType).append(" - ")
          .append(getTypeDescription(info.refType)).append("\n");

        if (!info.refPmid.isEmpty()) {
            sb.append("Ref PMID: ").append(info.refPmid).append("\n");
        }
        if (!info.refSource.isEmpty()) {
            sb.append("Ref Source: ").append(info.refSource).append("\n");
        }
        if (!info.note.isEmpty()) {
            sb.append("Note: ").append(info.note).append("\n");
        }

        sb.append("\n");
        return sb.toString();
    }

    /**
     * 타입 설명 / Type description
     */
    private static String getTypeDescription(String refType) {
        if (RETRACTION_TYPES.contains(refType)) {
            return refType.contains("Of") ? "논문 취소함 / Retracts article" :
                                          "논문 취소됨 / Article retracted";
        } else if (CONCERN_TYPES.contains(refType)) {
            return "우려 표명 / Expression of concern";
        } else if (ERRATUM_TYPES.contains(refType)) {
            return "정정 / Correction";
        } else if (REPUBLISH_TYPES.contains(refType)) {
            return "재출판 / Republication";
        }
        return "기타 / Other";
    }

    /**
     * 결과 출력 / Print results
     */
    private static void printResults(int totalArticles,
                                    List<RetractionInfo> retractions,
                                    Map<String, AtomicInteger> typeCounts,
                                    Path csvFile) {
        System.out.println(SEPARATOR);
        System.out.println("Search Complete");
        System.out.println(SEPARATOR);
        System.out.printf("Total Articles Scanned:  %,d%n", totalArticles);
        System.out.printf("Articles with Issues:    %,d%n", retractions.size());
        System.out.println();

        if (!typeCounts.isEmpty()) {
            System.out.println("Breakdown by Type:");
            typeCounts.entrySet().stream()
                     .sorted((e1, e2) -> e2.getValue().get() - e1.getValue().get())
                     .forEach(e -> System.out.printf("  %-25s: %,d%n",
                                                     e.getKey(), e.getValue().get()));
            System.out.println();
        }

        if (retractions.size() > 10) {
            System.out.printf("(Showing first 10 of %,d results)%n", retractions.size());
            System.out.println();
        }

        if (csvFile != null) {
            System.out.println("Full results saved to: " + csvFile);
        }

        System.out.println();

        // 요약 / Summary
        int retractionCount = typeCounts.entrySet().stream()
            .filter(e -> RETRACTION_TYPES.contains(e.getKey()))
            .mapToInt(e -> e.getValue().get())
            .sum();

        int concernCount = typeCounts.entrySet().stream()
            .filter(e -> CONCERN_TYPES.contains(e.getKey()))
            .mapToInt(e -> e.getValue().get())
            .sum();

        int erratumCount = typeCounts.entrySet().stream()
            .filter(e -> ERRATUM_TYPES.contains(e.getKey()))
            .mapToInt(e -> e.getValue().get())
            .sum();

        System.out.println("Summary:");
        System.out.printf("  Retractions:            %,d%n", retractionCount);
        System.out.printf("  Expressions of Concern: %,d%n", concernCount);
        System.out.printf("  Errata/Corrections:     %,d%n", erratumCount);
    }

    /**
     * CSV 이스케이프 / CSV escape
     */
    private static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"");
    }

    /**
     * 사용법 출력 / Print usage
     */
    private static void printUsage() {
        System.out.println("Retraction & Correction Finder");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java examples.FindRetractions <xml-file-path> [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --csv <file>   Export results to CSV file");
        System.out.println("  --type <type>  Filter by type (Retraction, Concern, Erratum, etc.)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  Find all retractions and corrections:");
        System.out.println("    java examples.FindRetractions pubmed25n0001.xml.gz");
        System.out.println();
        System.out.println("  Export to CSV:");
        System.out.println("    java examples.FindRetractions pubmed25n0001.xml.gz --csv retractions.csv");
        System.out.println();
        System.out.println("  Filter retractions only:");
        System.out.println("    java examples.FindRetractions pubmed25n0001.xml.gz --type Retraction");
        System.out.println();
        System.out.println("  Filter expressions of concern:");
        System.out.println("    java examples.FindRetractions pubmed25n0001.xml.gz --type Concern --csv concerns.csv");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  - Retraction detection (RetractionOf, RetractionIn)");
        System.out.println("  - Expression of concern detection");
        System.out.println("  - Erratum/correction detection");
        System.out.println("  - Republication tracking");
        System.out.println("  - CSV export for further analysis");
        System.out.println("  - Detailed statistics and breakdown");
        System.out.println();
        System.out.println("Comment Types:");
        System.out.println("  Retraction:   RetractionOf, RetractionIn");
        System.out.println("  Concern:      ExpressionOfConcernIn, ExpressionOfConcernFor");
        System.out.println("  Erratum:      ErratumFor, ErratumIn, CorrectionIn");
        System.out.println("  Republish:    RepublishedFrom, RepublishedIn");
    }

    /**
     * RetractionInfo 데이터 클래스 / RetractionInfo data class
     */
    private static class RetractionInfo {
        final String pmid;
        final String title;
        final String refType;
        final String refPmid;
        final String refSource;
        final String note;

        RetractionInfo(String pmid, String title, String refType,
                      String refPmid, String refSource, String note) {
            this.pmid = pmid;
            this.title = title;
            this.refType = refType;
            this.refPmid = refPmid;
            this.refSource = refSource;
            this.note = note;
        }
    }
}
