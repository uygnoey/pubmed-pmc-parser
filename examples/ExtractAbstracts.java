package examples;

import io.brillianttiger.bio.parser.pubmed.model.*;
import io.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import io.brillianttiger.bio.parser.pmc.model.*;
import io.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ExtractAbstracts / 초록 추출 예제
 *
 * KR: PubMed와 PMC 논문에서 초록(Abstract)을 추출하는 예제.
 *     구조화된 초록(Structured Abstract)을 포함한 모든 초록 형식을 지원합니다.
 *
 * EN: Example of extracting abstracts from PubMed and PMC articles.
 *     Supports all abstract formats including structured abstracts.
 *
 * Features:
 *   - Structured abstract parsing (Background, Methods, Results, etc.)
 *   - Plain text output
 *   - Batch file processing
 *   - Statistics reporting
 *   - Multiple output formats
 *
 * Usage:
 *   java examples.ExtractAbstracts <xml-file-path> [output-file]
 *
 * Examples:
 *   java examples.ExtractAbstracts pubmed25n0001.xml.gz
 *   java examples.ExtractAbstracts pubmed25n0001.xml.gz abstracts.txt
 *   java examples.ExtractAbstracts pmc_article.xml abstracts.txt
 */
public class ExtractAbstracts {

    private static final String SEPARATOR = "=" .repeat(80);
    private static final String SUB_SEPARATOR = "-".repeat(80);

    public static void main(String[] args) {
        if (args.length < 1) {
            printUsage();
            System.exit(1);
        }

        Path xmlFile = Paths.get(args[0]);
        Path outputFile = args.length > 1 ? Paths.get(args[1]) : null;

        String fileName = xmlFile.getFileName().toString().toLowerCase();

        try {
            System.out.println(SEPARATOR);
            System.out.println("Abstract Extraction");
            System.out.println(SEPARATOR);
            System.out.println("File:   " + xmlFile);
            if (outputFile != null) {
                System.out.println("Output: " + outputFile);
            }
            System.out.println();

            if (fileName.startsWith("pubmed")) {
                // PubMed 초록 추출 / Extract PubMed abstracts
                extractPubmedAbstracts(xmlFile, outputFile);
            } else if (fileName.startsWith("pmc") || fileName.contains("pmc")) {
                // PMC 초록 추출 / Extract PMC abstracts
                extractPmcAbstracts(xmlFile, outputFile);
            } else {
                System.err.println("Cannot determine file type");
                System.exit(1);
            }

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * PubMed 초록 추출 / Extract PubMed abstracts
     */
    private static void extractPubmedAbstracts(Path xmlFile, Path outputFile) throws Exception {
        System.out.println("Extracting PubMed abstracts...");
        System.out.println();

        PubmedXmlParser parser = new PubmedXmlParser();

        // 통계 추적 / Track statistics
        AtomicInteger totalArticles = new AtomicInteger(0);
        AtomicInteger articlesWithAbstract = new AtomicInteger(0);
        AtomicInteger structuredAbstracts = new AtomicInteger(0);

        // 출력 준비 / Prepare output
        BufferedWriter writer = null;
        if (outputFile != null) {
            writer = Files.newBufferedWriter(outputFile,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.TRUNCATE_EXISTING);
        }

        BufferedWriter finalWriter = writer;

        // 스트리밍 파싱 / Stream parsing
        parser.parseStreamBatch(xmlFile, 100, batch -> {
            try {
                processPubmedBatch(batch, finalWriter,
                                 totalArticles, articlesWithAbstract, structuredAbstracts);
            } catch (Exception e) {
                System.err.println("Error processing batch: " + e.getMessage());
            }
        });

        // 출력 마무리 / Close output
        if (writer != null) {
            writer.close();
        }

        // 통계 출력 / Print statistics
        printStatistics(totalArticles.get(), articlesWithAbstract.get(),
                       structuredAbstracts.get(), outputFile);
    }

    /**
     * PubMed 배치 처리 / Process PubMed batch
     */
    private static void processPubmedBatch(
            List<PubmedArticle> batch,
            BufferedWriter writer,
            AtomicInteger totalArticles,
            AtomicInteger articlesWithAbstract,
            AtomicInteger structuredAbstracts) throws IOException {

        for (PubmedArticle article : batch) {
            totalArticles.incrementAndGet();

            MedlineCitation citation = article.getMedlineCitation();
            if (citation == null || citation.getArticle() == null) {
                continue;
            }

            Article articleData = citation.getArticle();
            PubmedAbstract abstractData = articleData.getAbstract();

            if (abstractData == null || abstractData.getAbstractTexts() == null ||
                abstractData.getAbstractTexts().isEmpty()) {
                continue;
            }

            articlesWithAbstract.incrementAndGet();

            // PMID
            String pmid = citation.getPmid().getValue();

            // Title
            String title = articleData.getArticleTitle() != null ?
                          articleData.getArticleTitle().getValue() : "No Title";

            // 구조화된 초록 여부 확인 / Check if structured
            boolean isStructured = abstractData.getAbstractTexts().stream()
                                              .anyMatch(at -> at.getLabel() != null ||
                                                            at.getNlmCategory() != null);

            if (isStructured) {
                structuredAbstracts.incrementAndGet();
            }

            // 초록 추출 / Extract abstract
            StringBuilder abstractText = new StringBuilder();
            for (AbstractText at : abstractData.getAbstractTexts()) {
                if (at.getLabel() != null || at.getNlmCategory() != null) {
                    // 구조화된 초록 / Structured abstract
                    String label = at.getLabel() != null ? at.getLabel() : at.getNlmCategory();
                    abstractText.append("[").append(label).append("]\n");
                }
                abstractText.append(at.getValue()).append("\n\n");
            }

            // 출력 / Output
            String output = formatPubmedAbstract(pmid, title, abstractText.toString(), isStructured);

            if (writer != null) {
                writer.write(output);
            } else {
                System.out.print(output);
            }
        }
    }

    /**
     * PubMed 초록 포맷 / Format PubMed abstract
     */
    private static String formatPubmedAbstract(String pmid, String title,
                                               String abstractText, boolean isStructured) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_SEPARATOR).append("\n");
        sb.append("PMID: ").append(pmid).append("\n");
        sb.append("Title: ").append(title).append("\n");
        if (isStructured) {
            sb.append("Type: Structured Abstract\n");
        }
        sb.append(SUB_SEPARATOR).append("\n");
        sb.append(abstractText);
        sb.append("\n");
        return sb.toString();
    }

    /**
     * PMC 초록 추출 / Extract PMC abstracts
     */
    private static void extractPmcAbstracts(Path xmlFile, Path outputFile) throws Exception {
        String fileName = xmlFile.getFileName().toString();

        if (fileName.endsWith(".tar.gz")) {
            System.out.println("Extracting PMC abstracts from tar.gz archive...");
        } else {
            System.out.println("Extracting PMC abstracts...");
        }
        System.out.println();

        PmcXmlParser parser = new PmcXmlParser();

        // 통계 추적 / Track statistics
        AtomicInteger totalArticles = new AtomicInteger(0);
        AtomicInteger articlesWithAbstract = new AtomicInteger(0);
        AtomicInteger structuredAbstracts = new AtomicInteger(0);

        // 출력 준비 / Prepare output
        BufferedWriter writer = null;
        if (outputFile != null) {
            writer = Files.newBufferedWriter(outputFile,
                                            StandardOpenOption.CREATE,
                                            StandardOpenOption.TRUNCATE_EXISTING);
        }

        BufferedWriter finalWriter = writer;

        if (fileName.endsWith(".tar.gz")) {
            // TAR.GZ 아카이브 처리 / Process TAR.GZ
            var articles = parser.parseTarGz(xmlFile);
            for (JatsArticle article : articles) {
                processPmcArticle(article, finalWriter,
                                totalArticles, articlesWithAbstract, structuredAbstracts);
            }
        } else {
            // 스트리밍 파싱 / Stream parsing
            parser.parseStreamBatch(xmlFile, 100, batch -> {
                try {
                    processPmcBatch(batch, finalWriter,
                                  totalArticles, articlesWithAbstract, structuredAbstracts);
                } catch (Exception e) {
                    System.err.println("Error processing batch: " + e.getMessage());
                }
            });
        }

        // 출력 마무리 / Close output
        if (writer != null) {
            writer.close();
        }

        // 통계 출력 / Print statistics
        printStatistics(totalArticles.get(), articlesWithAbstract.get(),
                       structuredAbstracts.get(), outputFile);
    }

    /**
     * PMC 배치 처리 / Process PMC batch
     */
    private static void processPmcBatch(
            List<JatsArticle> batch,
            BufferedWriter writer,
            AtomicInteger totalArticles,
            AtomicInteger articlesWithAbstract,
            AtomicInteger structuredAbstracts) throws IOException {

        for (JatsArticle article : batch) {
            processPmcArticle(article, writer,
                            totalArticles, articlesWithAbstract, structuredAbstracts);
        }
    }

    /**
     * PMC 논문 처리 / Process PMC article
     */
    private static void processPmcArticle(
            JatsArticle article,
            BufferedWriter writer,
            AtomicInteger totalArticles,
            AtomicInteger articlesWithAbstract,
            AtomicInteger structuredAbstracts) throws IOException {

        totalArticles.incrementAndGet();

        Front front = article.getFront();
        if (front == null || front.getArticleMeta() == null) {
            return;
        }

        ArticleMeta meta = front.getArticleMeta();

        // 초록 확인 / Check abstract
        if (meta.getAbstracts() == null || meta.getAbstracts().isEmpty()) {
            return;
        }

        articlesWithAbstract.incrementAndGet();

        // Article IDs
        String pmcId = null;
        String pmid = null;
        String doi = null;

        if (meta.getArticleIds() != null) {
            for (PmcArticleId id : meta.getArticleIds()) {
                if ("pmc".equals(id.getPubIdType())) {
                    pmcId = id.getValue();
                } else if ("pmid".equals(id.getPubIdType())) {
                    pmid = id.getValue();
                } else if ("doi".equals(id.getPubIdType())) {
                    doi = id.getValue();
                }
            }
        }

        // Title
        String title = "No Title";
        if (meta.getTitleGroup() != null &&
            meta.getTitleGroup().getArticleTitle() != null) {
            title = meta.getTitleGroup().getArticleTitle().getContent();
        }

        // 초록 추출 / Extract abstracts
        for (PmcAbstract abstractData : meta.getAbstracts()) {
            StringBuilder abstractText = new StringBuilder();
            boolean isStructured = false;

            // Sec 기반 구조화된 초록 / Structured abstract with sections
            if (abstractData.getSections() != null && !abstractData.getSections().isEmpty()) {
                isStructured = true;
                structuredAbstracts.incrementAndGet();

                for (Sec sec : abstractData.getSections()) {
                    if (sec.getTitle() != null) {
                        abstractText.append("[").append(sec.getTitle().getValue()).append("]\n");
                    }
                    if (sec.getParagraphs() != null) {
                        for (P p : sec.getParagraphs()) {
                            abstractText.append(p.getContent()).append("\n\n");
                        }
                    }
                }
            }
            // 단순 paragraphs / Simple paragraphs
            else if (abstractData.getParagraphs() != null &&
                    !abstractData.getParagraphs().isEmpty()) {
                for (P p : abstractData.getParagraphs()) {
                    abstractText.append(p.getContent()).append("\n\n");
                }
            }

            // 출력 / Output
            String output = formatPmcAbstract(pmcId, pmid, doi, title,
                                             abstractText.toString(), isStructured);

            if (writer != null) {
                writer.write(output);
            } else {
                System.out.print(output);
            }
        }
    }

    /**
     * PMC 초록 포맷 / Format PMC abstract
     */
    private static String formatPmcAbstract(String pmcId, String pmid, String doi,
                                            String title, String abstractText,
                                            boolean isStructured) {
        StringBuilder sb = new StringBuilder();
        sb.append(SUB_SEPARATOR).append("\n");

        if (pmcId != null) {
            sb.append("PMC ID: ").append(pmcId).append("\n");
        }
        if (pmid != null) {
            sb.append("PMID: ").append(pmid).append("\n");
        }
        if (doi != null) {
            sb.append("DOI: ").append(doi).append("\n");
        }

        sb.append("Title: ").append(title).append("\n");

        if (isStructured) {
            sb.append("Type: Structured Abstract\n");
        }

        sb.append(SUB_SEPARATOR).append("\n");
        sb.append(abstractText);
        sb.append("\n");

        return sb.toString();
    }

    /**
     * 통계 출력 / Print statistics
     */
    private static void printStatistics(int totalArticles, int articlesWithAbstract,
                                       int structuredAbstracts, Path outputFile) {
        System.out.println(SEPARATOR);
        System.out.println("Extraction Complete");
        System.out.println(SEPARATOR);
        System.out.printf("Total Articles:          %,d%n", totalArticles);
        System.out.printf("Articles with Abstract:  %,d (%.1f%%)%n",
                         articlesWithAbstract,
                         totalArticles > 0 ? (100.0 * articlesWithAbstract / totalArticles) : 0);
        System.out.printf("Structured Abstracts:    %,d (%.1f%%)%n",
                         structuredAbstracts,
                         articlesWithAbstract > 0 ? (100.0 * structuredAbstracts / articlesWithAbstract) : 0);

        if (outputFile != null) {
            System.out.println();
            System.out.println("Output saved to: " + outputFile);
        }
        System.out.println();
    }

    /**
     * 사용법 출력 / Print usage
     */
    private static void printUsage() {
        System.out.println("Abstract Extraction Example");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java examples.ExtractAbstracts <xml-file-path> [output-file]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  xml-file-path  Path to PubMed or PMC XML file (required)");
        System.out.println("  output-file    Output text file path (optional, default: stdout)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  Display to console:");
        System.out.println("    java examples.ExtractAbstracts pubmed25n0001.xml.gz");
        System.out.println();
        System.out.println("  Save to file:");
        System.out.println("    java examples.ExtractAbstracts pubmed25n0001.xml.gz abstracts.txt");
        System.out.println();
        System.out.println("  PMC article:");
        System.out.println("    java examples.ExtractAbstracts PMC1234567.xml abstracts.txt");
        System.out.println();
        System.out.println("  PMC archive:");
        System.out.println("    java examples.ExtractAbstracts pmc_oa_comm_xml.tar.gz abstracts.txt");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  - Structured abstract parsing (Background, Methods, Results, etc.)");
        System.out.println("  - Plain text output with clear formatting");
        System.out.println("  - Statistics reporting (total, structured, percentage)");
        System.out.println("  - Memory-efficient streaming for large files");
        System.out.println("  - Support for both PubMed and PMC formats");
    }
}
