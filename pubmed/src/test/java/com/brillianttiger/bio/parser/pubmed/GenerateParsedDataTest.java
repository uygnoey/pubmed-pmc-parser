package com.brillianttiger.bio.parser.pubmed;

import com.brillianttiger.bio.parser.pubmed.model.*;
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * GenerateParsedDataTest - NCBI FTP 다운로드 실제 데이터로 파싱 및 검증
 *
 * KR: 실제 NCBI FTP에서 다운로드한 데이터로 파싱하고 상세 결과 기록
 * EN: Parse real data downloaded from NCBI FTP and record detailed results
 */
class GenerateParsedDataTest {

    @Test
    void generatePubmedParsedDataDetails() throws Exception {
        System.out.println("📚 Generating PubMed parsed data details...");

        PubmedXmlParser parser = new PubmedXmlParser();
        List<PubmedArticle> articles = new ArrayList<>();

        // Parse from REAL PubMed baseline file downloaded from NCBI FTP
        Path pubmedFile = Paths.get("../test-docs-2601220906/downloads/pubmed25n0001.xml.gz");
        System.out.println("   Parsing from NCBI FTP download: " + pubmedFile);
        System.out.println("   Source: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/pubmed25n0001.xml.gz");

        parser.parseStream(pubmedFile, article -> {
            if (articles.size() < 15) {
                articles.add(article);
                System.out.println("   Article #" + articles.size() + " parsed");
            }
        });

        System.out.println("   Total parsed: " + articles.size() + " articles");

        Path outputFile = Paths.get("../test-docs-2601220906/pubmed/parsed-data-details.md");
        System.out.println("   Writing to " + outputFile + "...");

        try (PrintWriter w = new PrintWriter(new FileWriter(outputFile.toFile()))) {
            w.println("# PubMed 실제 파싱 데이터 (15건)");
            w.println();
            w.println("**파일:** pubmed25n0001.xml.gz (실제 NCBI FTP 다운로드)");
            w.println("**소스:** https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/pubmed25n0001.xml.gz");
            w.println("**파싱 시간:** " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            w.println("**DTD 버전:** PubMed 2025.01.01");
            w.println();
            w.println("---");
            w.println();

            int index = 1;
            for (PubmedArticle article : articles) {
                writePubmedArticleDetail(w, article, index++);
            }
        }

        System.out.println("✅ PubMed parsed data details generated!");
    }

    private void writePubmedArticleDetail(PrintWriter w, PubmedArticle article, int index) {
        w.println("## Article #" + index);
        w.println();

        MedlineCitation citation = article.getMedlineCitation();
        if (citation != null) {
            // PMID
            if (citation.getPmid() != null) {
                w.println("### PMID");
                w.println(citation.getPmid().getValue());
                w.println();
            }

            // Article
            Article mainArticle = citation.getArticle();
            if (mainArticle != null) {
                // Title
                if (mainArticle.getArticleTitle() != null) {
                    w.println("### 제목");
                    w.println("> " + mainArticle.getArticleTitle().getValue());
                    w.println();
                }

                // Journal
                if (mainArticle.getJournal() != null) {
                    Journal journal = mainArticle.getJournal();
                    w.println("### 저널");
                    if (journal.getTitle() != null) {
                        w.println("**Title:** " + journal.getTitle());
                    }
                    if (journal.getIsoAbbreviation() != null) {
                        w.println("**ISO Abbreviation:** " + journal.getIsoAbbreviation());
                    }
                    if (journal.getIssn() != null) {
                        w.println("**ISSN:** " + journal.getIssn().getValue() + " (" + journal.getIssn().getIssnType() + ")");
                    }
                    w.println();
                }

                // Publication Date
                if (mainArticle.getJournal() != null && mainArticle.getJournal().getJournalIssue() != null) {
                    JournalIssue issue = mainArticle.getJournal().getJournalIssue();
                    if (issue.getPubDate() != null) {
                        w.println("### Publication Date");
                        PubDate pubDate = issue.getPubDate();
                        StringBuilder dateStr = new StringBuilder();
                        if (pubDate.getYear() != null) dateStr.append(pubDate.getYear().getValue());
                        if (pubDate.getMonth() != null) dateStr.append("-").append(pubDate.getMonth().getValue());
                        if (pubDate.getDay() != null) dateStr.append("-").append(pubDate.getDay().getValue());
                        w.println(dateStr.toString());
                        w.println();
                    }
                }

                // Authors
                if (mainArticle.getAuthorList() != null && mainArticle.getAuthorList().getAuthors() != null) {
                    List<Author> authors = mainArticle.getAuthorList().getAuthors();
                    w.println("### 저자 (" + authors.size() + "명)");
                    for (int i = 0; i < Math.min(authors.size(), 10); i++) {
                        Author author = authors.get(i);
                        StringBuilder authorStr = new StringBuilder();
                        authorStr.append((i + 1)).append(". ");
                        if (author.getLastName() != null) {
                            authorStr.append(author.getLastName());
                        }
                        if (author.getForeName() != null) {
                            authorStr.append(", ").append(author.getForeName());
                        }
                        if (author.getInitials() != null) {
                            authorStr.append(" ").append(author.getInitials());
                        }
                        w.println(authorStr.toString());
                    }
                    if (authors.size() > 10) {
                        w.println("... (" + (authors.size() - 10) + " more authors)");
                    }
                    w.println();
                }

                // Abstract
                if (mainArticle.getAbstractInfo() != null) {
                    Abstract abstractObj = mainArticle.getAbstractInfo();
                    if (abstractObj.getAbstractTexts() != null) {
                        w.println("### 초록");
                        for (AbstractText text : abstractObj.getAbstractTexts()) {
                            if (text.getLabel() != null) {
                                w.println("**" + text.getLabel() + ":**");
                            }
                            if (text.getValue() != null) {
                                String content = text.getValue();
                                if (content.length() > 500) {
                                    w.println(content.substring(0, 500) + "...");
                                } else {
                                    w.println(content);
                                }
                            }
                            w.println();
                        }
                    }
                }
            }

            // MeSH Headings
            if (citation.getMeshHeadingList() != null && citation.getMeshHeadingList().getMeshHeadings() != null) {
                List<MeshHeading> meshHeadings = citation.getMeshHeadingList().getMeshHeadings();
                w.println("### MeSH Headings (" + meshHeadings.size() + "개)");
                for (int i = 0; i < Math.min(meshHeadings.size(), 10); i++) {
                    MeshHeading mesh = meshHeadings.get(i);
                    if (mesh.getDescriptorName() != null) {
                        w.println("- " + mesh.getDescriptorName().getValue());
                        if (mesh.getDescriptorName().getUi() != null) {
                            w.println("  - UI: " + mesh.getDescriptorName().getUi());
                        }
                    }
                }
                if (meshHeadings.size() > 10) {
                    w.println("... (" + (meshHeadings.size() - 10) + " more headings)");
                }
                w.println();
            }
        }

        w.println("---");
        w.println();
    }
}
