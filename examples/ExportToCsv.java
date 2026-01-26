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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * ExportToCsv / CSV 내보내기 예제
 *
 * KR: PubMed와 PMC 논문 데이터를 CSV 형식으로 내보내는 예제.
 *     대용량 파일 처리를 위한 스트리밍 방식 지원.
 *
 * EN: Example of exporting PubMed and PMC article data to CSV format.
 *     Supports streaming approach for large file processing.
 *
 * Features:
 *   - Customizable field selection
 *   - Memory-efficient streaming
 *   - Progress monitoring
 *   - Proper CSV escaping
 *   - Support for both PubMed and PMC
 *
 * Usage:
 *   java examples.ExportToCsv <xml-file-path> <output-csv> [--fields FIELDS]
 *
 * Examples:
 *   java examples.ExportToCsv pubmed25n0001.xml.gz output.csv
 *   java examples.ExportToCsv pubmed25n0001.xml.gz output.csv --fields pmid,title,authors,journal
 */
public class ExportToCsv {

    private static final int PROGRESS_INTERVAL = 1000;

    // 사용 가능한 필드 / Available fields
    private static final Set<String> AVAILABLE_FIELDS = Set.of(
        "pmid", "title", "authors", "journal", "pubdate",
        "abstract", "doi", "keywords", "mesh", "affiliations"
    );

    // 기본 필드 / Default fields
    private static final List<String> DEFAULT_FIELDS = List.of(
        "pmid", "title", "authors", "journal", "pubdate", "abstract"
    );

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        Path xmlFile = Paths.get(args[0]);
        Path csvFile = Paths.get(args[1]);

        // 필드 선택 파싱 / Parse field selection
        List<String> fields = DEFAULT_FIELDS;
        for (int i = 2; i < args.length; i++) {
            if ("--fields".equals(args[i]) && i + 1 < args.length) {
                String fieldsArg = args[++i];
                fields = Arrays.asList(fieldsArg.split(","));

                // 유효성 검사 / Validate fields
                for (String field : fields) {
                    if (!AVAILABLE_FIELDS.contains(field.toLowerCase())) {
                        System.err.println("Invalid field: " + field);
                        System.err.println("Available fields: " + AVAILABLE_FIELDS);
                        System.exit(1);
                    }
                }
            }
        }

        String fileName = xmlFile.getFileName().toString().toLowerCase();

        try {
            System.out.println("========================================");
            System.out.println("CSV Export");
            System.out.println("========================================");
            System.out.println("Input:  " + xmlFile);
            System.out.println("Output: " + csvFile);
            System.out.println("Fields: " + String.join(", ", fields));
            System.out.println();

            if (fileName.startsWith("pubmed")) {
                // PubMed CSV 내보내기 / Export PubMed to CSV
                exportPubmedToCsv(xmlFile, csvFile, fields);
            } else if (fileName.startsWith("pmc") || fileName.contains("pmc")) {
                // PMC CSV 내보내기 / Export PMC to CSV
                exportPmcToCsv(xmlFile, csvFile, fields);
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
     * PubMed CSV 내보내기 / Export PubMed to CSV
     */
    private static void exportPubmedToCsv(Path xmlFile, Path csvFile, List<String> fields)
            throws Exception {

        System.out.println("Exporting PubMed data to CSV...");
        System.out.println();

        PubmedXmlParser parser = new PubmedXmlParser();

        // CSV 파일 준비 / Prepare CSV file
        BufferedWriter writer = Files.newBufferedWriter(csvFile,
                                                       StandardOpenOption.CREATE,
                                                       StandardOpenOption.TRUNCATE_EXISTING);

        // CSV 헤더 작성 / Write CSV header
        writer.write(fields.stream()
                          .map(String::toUpperCase)
                          .collect(Collectors.joining(",")) + "\n");

        // 통계 추적 / Track statistics
        AtomicInteger articleCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();

        // 스트리밍 파싱 / Stream parsing
        parser.parseStreamBatch(xmlFile, 100, batch -> {
            try {
                for (PubmedArticle article : batch) {
                    int count = articleCount.incrementAndGet();

                    // 진행 상황 출력 / Print progress
                    if (count % PROGRESS_INTERVAL == 0) {
                        printProgress(count, startTime);
                    }

                    // CSV 행 작성 / Write CSV row
                    String csvRow = extractPubmedFields(article, fields);
                    writer.write(csvRow + "\n");
                }
            } catch (Exception e) {
                System.err.println("Error processing batch: " + e.getMessage());
            }
        });

        writer.close();

        // 최종 통계 / Final statistics
        printFinalStatistics(articleCount.get(), startTime, csvFile);
    }

    /**
     * PubMed 필드 추출 / Extract PubMed fields
     */
    private static String extractPubmedFields(PubmedArticle article, List<String> fields) {
        MedlineCitation citation = article.getMedlineCitation();
        if (citation == null) {
            return fields.stream().map(f -> "").collect(Collectors.joining(","));
        }

        Article articleData = citation.getArticle();

        Map<String, String> fieldValues = new HashMap<>();

        // PMID
        fieldValues.put("pmid", citation.getPmid() != null ?
                               citation.getPmid().getValue() : "");

        // Title
        if (articleData != null && articleData.getArticleTitle() != null) {
            fieldValues.put("title", articleData.getArticleTitle().getValue());
        } else {
            fieldValues.put("title", "");
        }

        // Authors
        if (articleData != null && articleData.getAuthorList() != null) {
            String authors = articleData.getAuthorList().getAuthors().stream()
                .map(ExportToCsv::formatAuthorName)
                .collect(Collectors.joining("; "));
            fieldValues.put("authors", authors);
        } else {
            fieldValues.put("authors", "");
        }

        // Journal
        if (articleData != null && articleData.getJournal() != null &&
            articleData.getJournal().getTitle() != null) {
            fieldValues.put("journal", articleData.getJournal().getTitle().getValue());
        } else {
            fieldValues.put("journal", "");
        }

        // PubDate
        if (articleData != null && articleData.getJournal() != null &&
            articleData.getJournal().getJournalIssue() != null &&
            articleData.getJournal().getJournalIssue().getPubDate() != null) {
            fieldValues.put("pubdate",
                          formatPubDate(articleData.getJournal().getJournalIssue().getPubDate()));
        } else {
            fieldValues.put("pubdate", "");
        }

        // Abstract
        if (articleData != null && articleData.getAbstract() != null &&
            articleData.getAbstract().getAbstractTexts() != null) {
            String abstractText = articleData.getAbstract().getAbstractTexts().stream()
                .map(AbstractText::getValue)
                .collect(Collectors.joining(" "));
            fieldValues.put("abstract", abstractText);
        } else {
            fieldValues.put("abstract", "");
        }

        // DOI
        String doi = "";
        if (citation.getPubmedData() != null &&
            citation.getPubmedData().getArticleIdList() != null) {
            for (ArticleId id : citation.getPubmedData().getArticleIdList().getArticleIds()) {
                if ("doi".equals(id.getIdType())) {
                    doi = id.getValue();
                    break;
                }
            }
        }
        fieldValues.put("doi", doi);

        // Keywords
        if (citation.getKeywordLists() != null && !citation.getKeywordLists().isEmpty()) {
            String keywords = citation.getKeywordLists().stream()
                .flatMap(kwl -> kwl.getKeywords().stream())
                .map(Keyword::getValue)
                .collect(Collectors.joining("; "));
            fieldValues.put("keywords", keywords);
        } else {
            fieldValues.put("keywords", "");
        }

        // MeSH terms
        if (citation.getMeshHeadingList() != null &&
            citation.getMeshHeadingList().getMeshHeadings() != null) {
            String mesh = citation.getMeshHeadingList().getMeshHeadings().stream()
                .map(mh -> mh.getDescriptorName() != null ?
                          mh.getDescriptorName().getValue() : "")
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("; "));
            fieldValues.put("mesh", mesh);
        } else {
            fieldValues.put("mesh", "");
        }

        // Affiliations
        if (articleData != null && articleData.getAuthorList() != null) {
            String affiliations = articleData.getAuthorList().getAuthors().stream()
                .filter(a -> a.getAffiliationInfo() != null && !a.getAffiliationInfo().isEmpty())
                .flatMap(a -> a.getAffiliationInfo().stream())
                .map(aff -> aff.getAffiliation() != null ? aff.getAffiliation().getValue() : "")
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.joining("; "));
            fieldValues.put("affiliations", affiliations);
        } else {
            fieldValues.put("affiliations", "");
        }

        // CSV 행 생성 / Generate CSV row
        return fields.stream()
                    .map(f -> escapeCsv(fieldValues.getOrDefault(f, "")))
                    .collect(Collectors.joining(","));
    }

    /**
     * PMC CSV 내보내기 / Export PMC to CSV
     */
    private static void exportPmcToCsv(Path xmlFile, Path csvFile, List<String> fields)
            throws Exception {

        String fileName = xmlFile.getFileName().toString();

        if (fileName.endsWith(".tar.gz")) {
            System.out.println("Exporting PMC tar.gz archive to CSV...");
        } else {
            System.out.println("Exporting PMC data to CSV...");
        }
        System.out.println();

        PmcXmlParser parser = new PmcXmlParser();

        // CSV 파일 준비 / Prepare CSV file
        BufferedWriter writer = Files.newBufferedWriter(csvFile,
                                                       StandardOpenOption.CREATE,
                                                       StandardOpenOption.TRUNCATE_EXISTING);

        // CSV 헤더 작성 / Write CSV header
        writer.write(fields.stream()
                          .map(String::toUpperCase)
                          .collect(Collectors.joining(",")) + "\n");

        // 통계 추적 / Track statistics
        AtomicInteger articleCount = new AtomicInteger(0);
        long startTime = System.currentTimeMillis();

        if (fileName.endsWith(".tar.gz")) {
            // TAR.GZ 아카이브 처리 / Process TAR.GZ
            var articles = parser.parseTarGz(xmlFile);
            for (JatsArticle article : articles) {
                int count = articleCount.incrementAndGet();

                if (count % PROGRESS_INTERVAL == 0) {
                    printProgress(count, startTime);
                }

                String csvRow = extractPmcFields(article, fields);
                writer.write(csvRow + "\n");
            }
        } else {
            // 스트리밍 파싱 / Stream parsing
            parser.parseStreamBatch(xmlFile, 100, batch -> {
                try {
                    for (JatsArticle article : batch) {
                        int count = articleCount.incrementAndGet();

                        if (count % PROGRESS_INTERVAL == 0) {
                            printProgress(count, startTime);
                        }

                        String csvRow = extractPmcFields(article, fields);
                        writer.write(csvRow + "\n");
                    }
                } catch (Exception e) {
                    System.err.println("Error processing batch: " + e.getMessage());
                }
            });
        }

        writer.close();

        // 최종 통계 / Final statistics
        printFinalStatistics(articleCount.get(), startTime, csvFile);
    }

    /**
     * PMC 필드 추출 / Extract PMC fields
     */
    private static String extractPmcFields(JatsArticle article, List<String> fields) {
        Front front = article.getFront();
        if (front == null || front.getArticleMeta() == null) {
            return fields.stream().map(f -> "").collect(Collectors.joining(","));
        }

        ArticleMeta meta = front.getArticleMeta();
        Map<String, String> fieldValues = new HashMap<>();

        // PMID (PMC ID)
        String pmcId = "";
        String pmid = "";
        String doi = "";
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
        fieldValues.put("pmid", !pmcId.isEmpty() ? "PMC" + pmcId :
                               (!pmid.isEmpty() ? pmid : ""));
        fieldValues.put("doi", doi);

        // Title
        if (meta.getTitleGroup() != null &&
            meta.getTitleGroup().getArticleTitle() != null) {
            fieldValues.put("title", meta.getTitleGroup().getArticleTitle().getContent());
        } else {
            fieldValues.put("title", "");
        }

        // Authors
        if (meta.getContribGroups() != null && !meta.getContribGroups().isEmpty()) {
            String authors = meta.getContribGroups().stream()
                .flatMap(cg -> cg.getContribs() != null ? cg.getContribs().stream() : null)
                .map(ExportToCsv::formatContribName)
                .collect(Collectors.joining("; "));
            fieldValues.put("authors", authors);
        } else {
            fieldValues.put("authors", "");
        }

        // Journal
        if (front.getJournalMeta() != null &&
            front.getJournalMeta().getJournalTitleGroup() != null &&
            front.getJournalMeta().getJournalTitleGroup().getJournalTitle() != null) {
            fieldValues.put("journal",
                          front.getJournalMeta().getJournalTitleGroup()
                               .getJournalTitle().getValue());
        } else {
            fieldValues.put("journal", "");
        }

        // PubDate
        if (meta.getPubDates() != null && !meta.getPubDates().isEmpty()) {
            fieldValues.put("pubdate", formatPmcPubDate(meta.getPubDates().get(0)));
        } else {
            fieldValues.put("pubdate", "");
        }

        // Abstract
        if (meta.getAbstracts() != null && !meta.getAbstracts().isEmpty()) {
            String abstractText = meta.getAbstracts().stream()
                .flatMap(abs -> abs.getParagraphs() != null ?
                              abs.getParagraphs().stream() : null)
                .map(P::getContent)
                .collect(Collectors.joining(" "));
            fieldValues.put("abstract", abstractText);
        } else {
            fieldValues.put("abstract", "");
        }

        // Keywords
        if (meta.getKwdGroups() != null && !meta.getKwdGroups().isEmpty()) {
            String keywords = meta.getKwdGroups().stream()
                .flatMap(kg -> kg.getKeywords() != null ? kg.getKeywords().stream() : null)
                .map(Kwd::getValue)
                .collect(Collectors.joining("; "));
            fieldValues.put("keywords", keywords);
        } else {
            fieldValues.put("keywords", "");
        }

        // MeSH (PMC doesn't have MeSH)
        fieldValues.put("mesh", "");

        // Affiliations
        if (meta.getContribGroups() != null && !meta.getContribGroups().isEmpty()) {
            String affiliations = meta.getContribGroups().stream()
                .flatMap(cg -> cg.getAffs() != null ? cg.getAffs().stream() : null)
                .map(Aff::getContent)
                .filter(s -> s != null && !s.isEmpty())
                .distinct()
                .collect(Collectors.joining("; "));
            fieldValues.put("affiliations", affiliations);
        } else {
            fieldValues.put("affiliations", "");
        }

        // CSV 행 생성 / Generate CSV row
        return fields.stream()
                    .map(f -> escapeCsv(fieldValues.getOrDefault(f, "")))
                    .collect(Collectors.joining(","));
    }

    // ========== Helper Methods / 유틸리티 메서드 ==========

    /**
     * 저자 이름 포맷 / Format author name
     */
    private static String formatAuthorName(Author author) {
        if (author.getLastName() != null) {
            String lastName = author.getLastName().getValue();
            String initials = author.getInitials() != null ?
                             author.getInitials().getValue() : "";
            return lastName + " " + initials;
        } else if (author.getCollectiveName() != null) {
            return author.getCollectiveName().getValue();
        }
        return "";
    }

    /**
     * PMC 저자 이름 포맷 / Format PMC contributor name
     */
    private static String formatContribName(Contrib contrib) {
        if (contrib.getName() != null) {
            Name name = contrib.getName();
            String surname = name.getSurname() != null ? name.getSurname().getValue() : "";
            String givenNames = name.getGivenNames() != null ?
                               name.getGivenNames().getValue() : "";
            return surname + " " + givenNames;
        } else if (contrib.getCollabName() != null) {
            return contrib.getCollabName().getValue();
        }
        return "";
    }

    /**
     * PubMed 출판일 포맷 / Format PubMed publication date
     */
    private static String formatPubDate(PubDate pubDate) {
        if (pubDate.getMedlineDate() != null) {
            return pubDate.getMedlineDate().getValue();
        }

        StringBuilder sb = new StringBuilder();
        if (pubDate.getYear() != null) {
            sb.append(pubDate.getYear());
        }
        if (pubDate.getMonth() != null) {
            sb.append("-").append(pubDate.getMonth());
        }
        if (pubDate.getDay() != null) {
            sb.append("-").append(pubDate.getDay());
        }

        return sb.toString();
    }

    /**
     * PMC 출판일 포맷 / Format PMC publication date
     */
    private static String formatPmcPubDate(PubDate pubDate) {
        StringBuilder sb = new StringBuilder();

        if (pubDate.getYear() != null) {
            sb.append(pubDate.getYear().getValue());
        }
        if (pubDate.getMonth() != null) {
            sb.append("-").append(pubDate.getMonth().getValue());
        }
        if (pubDate.getDay() != null) {
            sb.append("-").append(pubDate.getDay().getValue());
        }

        return sb.toString();
    }

    /**
     * CSV 이스케이프 / CSV escape
     */
    private static String escapeCsv(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        // 따옴표 이스케이프 / Escape quotes
        String escaped = value.replace("\"", "\"\"");

        // 쉼표, 따옴표, 줄바꿈이 있으면 따옴표로 감싸기
        // Wrap in quotes if contains comma, quote, or newline
        if (escaped.contains(",") || escaped.contains("\"") || escaped.contains("\n")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }

    /**
     * 진행 상황 출력 / Print progress
     */
    private static void printProgress(int count, long startTime) {
        long elapsed = System.currentTimeMillis() - startTime;
        double seconds = elapsed / 1000.0;
        double throughput = count / seconds;

        System.out.printf("Progress: %,d articles (%.0f articles/sec)%n",
                         count, throughput);
    }

    /**
     * 최종 통계 출력 / Print final statistics
     */
    private static void printFinalStatistics(int articleCount, long startTime, Path csvFile) {
        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;
        double seconds = elapsed / 1000.0;
        double throughput = articleCount / seconds;

        System.out.println();
        System.out.println("========================================");
        System.out.println("Export Complete");
        System.out.println("========================================");
        System.out.printf("Total Articles:  %,d%n", articleCount);
        System.out.printf("Time Elapsed:    %.2f seconds%n", seconds);
        System.out.printf("Throughput:      %.0f articles/sec%n", throughput);
        System.out.println("Output File:     " + csvFile);
        System.out.println();
    }

    /**
     * 사용법 출력 / Print usage
     */
    private static void printUsage() {
        System.out.println("CSV Export Example");
        System.out.println();
        System.out.println("Usage:");
        System.out.println("  java examples.ExportToCsv <xml-file-path> <output-csv> [options]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  --fields <fields>  Comma-separated list of fields to export");
        System.out.println();
        System.out.println("Available Fields:");
        System.out.println("  pmid          PMID or PMC ID");
        System.out.println("  title         Article title");
        System.out.println("  authors       Author list");
        System.out.println("  journal       Journal name");
        System.out.println("  pubdate       Publication date");
        System.out.println("  abstract      Abstract text");
        System.out.println("  doi           DOI");
        System.out.println("  keywords      Keywords");
        System.out.println("  mesh          MeSH terms (PubMed only)");
        System.out.println("  affiliations  Author affiliations");
        System.out.println();
        System.out.println("Default Fields:");
        System.out.println("  pmid, title, authors, journal, pubdate, abstract");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  Export with default fields:");
        System.out.println("    java examples.ExportToCsv pubmed25n0001.xml.gz output.csv");
        System.out.println();
        System.out.println("  Export with custom fields:");
        System.out.println("    java examples.ExportToCsv pubmed25n0001.xml.gz output.csv --fields pmid,title,authors");
        System.out.println();
        System.out.println("  Export all fields:");
        System.out.println("    java examples.ExportToCsv pubmed25n0001.xml.gz output.csv --fields pmid,title,authors,journal,pubdate,abstract,doi,keywords,mesh,affiliations");
        System.out.println();
        System.out.println("  PMC archive:");
        System.out.println("    java examples.ExportToCsv pmc_oa_comm_xml.tar.gz pmc_output.csv");
        System.out.println();
        System.out.println("Features:");
        System.out.println("  - Customizable field selection");
        System.out.println("  - Memory-efficient streaming for large files");
        System.out.println("  - Real-time progress monitoring");
        System.out.println("  - Proper CSV escaping and formatting");
        System.out.println("  - Support for both PubMed and PMC formats");
    }
}
