package examples;

import io.brillianttiger.bio.parser.pubmed.model.*;
import io.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import io.brillianttiger.bio.parser.pmc.model.*;
import io.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * BasicParsing / 기본 파싱 예제
 *
 * KR: PubMed와 PMC XML 파일을 파싱하는 가장 기본적인 예제.
 *     단일 파일을 파싱하고 메타데이터를 추출하는 방법을 보여줍니다.
 *
 * EN: Most basic example of parsing PubMed and PMC XML files.
 *     Demonstrates how to parse a single file and extract metadata.
 *
 * Usage:
 *   java examples.BasicParsing <xml-file-path>
 *
 * Example:
 *   java examples.BasicParsing test-data/pubmed/baseline/pubmed25n0001.xml.gz
 *   java examples.BasicParsing test-data/pmc/PMC1234567.xml
 */
public class BasicParsing {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java examples.BasicParsing <xml-file-path>");
            System.out.println();
            System.out.println("Examples:");
            System.out.println("  PubMed: java examples.BasicParsing pubmed25n0001.xml.gz");
            System.out.println("  PMC:    java examples.BasicParsing PMC1234567.xml");
            System.exit(1);
        }

        Path xmlFile = Paths.get(args[0]);
        String fileName = xmlFile.getFileName().toString().toLowerCase();

        try {
            if (fileName.startsWith("pubmed")) {
                // PubMed 파일 파싱 / Parse PubMed file
                parsePubmedFile(xmlFile);
            } else if (fileName.startsWith("pmc") || fileName.contains("pmc")) {
                // PMC 파일 파싱 / Parse PMC file
                parsePmcFile(xmlFile);
            } else {
                System.err.println("Cannot determine file type. File name should start with 'pubmed' or 'pmc'");
                System.exit(1);
            }

        } catch (Exception e) {
            System.err.println("Error parsing file: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * PubMed 파일 파싱 / Parse PubMed file
     */
    private static void parsePubmedFile(Path xmlFile) throws Exception {
        System.out.println("========================================");
        System.out.println("Parsing PubMed File");
        System.out.println("========================================");
        System.out.println("File: " + xmlFile);
        System.out.println();

        // 파서 생성 / Create parser
        PubmedXmlParser parser = new PubmedXmlParser();

        // 파일 파싱 / Parse file
        System.out.println("Parsing...");
        PubmedArticleSet articleSet = parser.parseFile(xmlFile);
        System.out.println("✅ Parsing complete");
        System.out.println();

        // 통계 출력 / Print statistics
        int articleCount = articleSet.getPubmedArticles() != null ?
                           articleSet.getPubmedArticles().size() : 0;
        int bookCount = articleSet.getPubmedBookArticles() != null ?
                        articleSet.getPubmedBookArticles().size() : 0;
        int deletedCount = 0;
        if (articleSet.getDeleteCitation() != null &&
            articleSet.getDeleteCitation().getPmids() != null) {
            deletedCount = articleSet.getDeleteCitation().getPmids().size();
        }

        System.out.println("Statistics:");
        System.out.println("  Articles:       " + articleCount);
        System.out.println("  Book Articles:  " + bookCount);
        System.out.println("  Deleted PMIDs:  " + deletedCount);
        System.out.println();

        // 첫 번째 논문 상세 정보 출력 / Print first article details
        if (articleCount > 0) {
            PubmedArticle article = articleSet.getPubmedArticles().get(0);
            printPubmedArticleDetails(article);
        }

        // DeleteCitation 정보 출력 / Print deletion info
        if (deletedCount > 0) {
            System.out.println("========================================");
            System.out.println("Deleted PMIDs");
            System.out.println("========================================");
            DeleteCitation deleteCitation = articleSet.getDeleteCitation();
            for (PMID pmid : deleteCitation.getPmids()) {
                System.out.println("  - " + pmid.getValue());
            }
            System.out.println();
        }
    }

    /**
     * PubMed 논문 상세 정보 출력 / Print PubMed article details
     */
    private static void printPubmedArticleDetails(PubmedArticle article) {
        System.out.println("========================================");
        System.out.println("First Article Details");
        System.out.println("========================================");

        MedlineCitation citation = article.getMedlineCitation();

        // PMID
        String pmid = citation.getPmid().getValue();
        System.out.println("PMID: " + pmid);

        // Title
        Article articleData = citation.getArticle();
        if (articleData != null && articleData.getArticleTitle() != null) {
            String title = articleData.getArticleTitle().getValue();
            System.out.println("Title: " + title);
        }

        // Authors
        if (articleData != null && articleData.getAuthorList() != null) {
            AuthorList authorList = articleData.getAuthorList();
            System.out.println("Authors: " + authorList.getAuthors().size());

            // 처음 3명만 출력 / Print first 3 authors
            int count = Math.min(3, authorList.getAuthors().size());
            for (int i = 0; i < count; i++) {
                Author author = authorList.getAuthors().get(i);
                String name = formatAuthorName(author);
                System.out.println("  " + (i + 1) + ". " + name);
            }
            if (authorList.getAuthors().size() > 3) {
                System.out.println("  ... and " + (authorList.getAuthors().size() - 3) + " more");
            }
        }

        // Journal
        if (articleData != null && articleData.getJournal() != null) {
            Journal journal = articleData.getJournal();
            if (journal.getTitle() != null) {
                System.out.println("Journal: " + journal.getTitle().getValue());
            }

            // Publication date
            if (journal.getJournalIssue() != null &&
                journal.getJournalIssue().getPubDate() != null) {
                PubDate pubDate = journal.getJournalIssue().getPubDate();
                String dateStr = formatPubDate(pubDate);
                System.out.println("Publication Date: " + dateStr);
            }
        }

        // Abstract
        if (articleData != null && articleData.getAbstract() != null) {
            PubmedAbstract abstractData = articleData.getAbstract();
            if (abstractData.getAbstractTexts() != null) {
                int textCount = abstractData.getAbstractTexts().size();
                System.out.println("Abstract: " + textCount + " section(s)");
            }
        }

        System.out.println();
    }

    /**
     * PMC 파일 파싱 / Parse PMC file
     */
    private static void parsePmcFile(Path xmlFile) throws Exception {
        System.out.println("========================================");
        System.out.println("Parsing PMC File");
        System.out.println("========================================");
        System.out.println("File: " + xmlFile);
        System.out.println();

        // 파서 생성 / Create parser
        PmcXmlParser parser = new PmcXmlParser();

        // 파일 파싱 / Parse file
        System.out.println("Parsing...");
        JatsArticle article = parser.parseFile(xmlFile);
        System.out.println("✅ Parsing complete");
        System.out.println();

        // 상세 정보 출력 / Print details
        printPmcArticleDetails(article);
    }

    /**
     * PMC 논문 상세 정보 출력 / Print PMC article details
     */
    private static void printPmcArticleDetails(JatsArticle article) {
        System.out.println("========================================");
        System.out.println("Article Details");
        System.out.println("========================================");

        Front front = article.getFront();
        if (front == null || front.getArticleMeta() == null) {
            System.out.println("No article metadata found");
            return;
        }

        ArticleMeta meta = front.getArticleMeta();

        // Article IDs
        if (meta.getArticleIds() != null) {
            System.out.println("Article IDs:");
            for (PmcArticleId id : meta.getArticleIds()) {
                System.out.println("  " + id.getPubIdType() + ": " + id.getValue());
            }
        }

        // Title
        if (meta.getTitleGroup() != null &&
            meta.getTitleGroup().getArticleTitle() != null) {
            String title = meta.getTitleGroup().getArticleTitle().getContent();
            System.out.println("Title: " + title);
        }

        // Authors
        if (meta.getContribGroups() != null && !meta.getContribGroups().isEmpty()) {
            ContribGroup contribGroup = meta.getContribGroups().get(0);
            if (contribGroup.getContribs() != null) {
                int authorCount = contribGroup.getContribs().size();
                System.out.println("Authors: " + authorCount);

                // 처음 3명만 출력 / Print first 3 authors
                int count = Math.min(3, authorCount);
                for (int i = 0; i < count; i++) {
                    Contrib contrib = contribGroup.getContribs().get(i);
                    String name = formatContribName(contrib);
                    System.out.println("  " + (i + 1) + ". " + name);
                }
                if (authorCount > 3) {
                    System.out.println("  ... and " + (authorCount - 3) + " more");
                }
            }
        }

        // Journal
        if (front.getJournalMeta() != null) {
            JournalMeta journalMeta = front.getJournalMeta();
            if (journalMeta.getJournalTitleGroup() != null &&
                journalMeta.getJournalTitleGroup().getJournalTitle() != null) {
                String journalTitle = journalMeta.getJournalTitleGroup()
                                                 .getJournalTitle()
                                                 .getValue();
                System.out.println("Journal: " + journalTitle);
            }
        }

        // Publication date
        if (meta.getPubDates() != null && !meta.getPubDates().isEmpty()) {
            PubDate pubDate = meta.getPubDates().get(0);
            String dateStr = formatPmcPubDate(pubDate);
            System.out.println("Publication Date: " + dateStr);
        }

        // Abstract
        if (meta.getAbstracts() != null && !meta.getAbstracts().isEmpty()) {
            System.out.println("Abstract: " + meta.getAbstracts().size() + " abstract(s)");
        }

        // Body sections
        if (article.getBody() != null && article.getBody().getSections() != null) {
            int sectionCount = article.getBody().getSections().size();
            System.out.println("Body Sections: " + sectionCount);
        }

        // Figures
        if (article.getFloatsGroup() != null &&
            article.getFloatsGroup().getFigs() != null) {
            int figCount = article.getFloatsGroup().getFigs().size();
            System.out.println("Figures: " + figCount);
        }

        // Tables
        if (article.getFloatsGroup() != null &&
            article.getFloatsGroup().getTableWraps() != null) {
            int tableCount = article.getFloatsGroup().getTableWraps().size();
            System.out.println("Tables: " + tableCount);
        }

        System.out.println();
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
        return "Unknown";
    }

    /**
     * PMC 저자 이름 포맷 / Format PMC contributor name
     */
    private static String formatContribName(Contrib contrib) {
        if (contrib.getName() != null) {
            Name name = contrib.getName();
            String surname = name.getSurname() != null ?
                           name.getSurname().getValue() : "";
            String givenNames = name.getGivenNames() != null ?
                               name.getGivenNames().getValue() : "";
            return surname + " " + givenNames;
        } else if (contrib.getCollabName() != null) {
            return contrib.getCollabName().getValue();
        }
        return "Unknown";
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
            sb.append(" ").append(pubDate.getMonth());
        }
        if (pubDate.getDay() != null) {
            sb.append(" ").append(pubDate.getDay());
        }

        return sb.toString().trim();
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
            sb.append(" ").append(pubDate.getMonth().getValue());
        }
        if (pubDate.getDay() != null) {
            sb.append(" ").append(pubDate.getDay().getValue());
        }

        return sb.toString().trim();
    }
}
