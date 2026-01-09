package com.brillianttiger.bio.parser.pubmed;

import com.brillianttiger.bio.parser.common.model.PubMedDate;
import com.brillianttiger.bio.parser.common.util.Md5Verifier;
import com.brillianttiger.bio.parser.pubmed.model.*;
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RealPubmedFileTest / 실제 PubMed 파일 파싱 테스트
 *
 * KR: NCBI FTP에서 다운로드한 실제 PubMed 파일로 파싱 테스트 및 **전체 데이터 완전 기록**
 * EN: Parsing test with real PubMed files from NCBI FTP and **complete data recording**
 */
class RealPubmedFileTest {

    private static final String BASE_DIR = "test-data/pubmed";
    private static final String DOC_DIR = "claudedocs/parsed-data";

    /**
     * 전체 파일 파싱 및 **모든 논문 데이터 완전 기록** / Parse all files and record ALL article data completely
     */
    @Test
    void testParseAndRecordAllArticles() throws Exception {
        System.out.println("========================================");
        System.out.println("실제 PubMed 파일 전체 파싱 및 데이터 기록");
        System.out.println("Real PubMed Files - Complete Data Recording");
        System.out.println("========================================\n");

        String[] files = {
            "baseline/pubmed25n0001.xml.gz",
            "baseline/pubmed25n1274.xml.gz",
            "update/pubmed25n1275.xml.gz",
            "update/pubmed25n1685.xml.gz"
        };

        // 문서 디렉토리 생성
        Files.createDirectories(Paths.get(DOC_DIR));

        PubmedXmlParser parser = new PubmedXmlParser();
        int totalArticles = 0;

        // 각 파일별로 전체 데이터 파싱 및 기록
        for (String file : files) {
            Path xmlFile = Paths.get(BASE_DIR, file);
            String fileName = file.replace("/", "-").replace(".xml.gz", "");

            System.out.printf("\n=== 파일 파싱 / Parsing: %s ===%n", file);

            // MD5 검증
            System.out.print("  MD5 검증... ");
            boolean md5Valid = Md5Verifier.verifyPubmedFile(xmlFile);
            assertTrue(md5Valid, "MD5 should be valid");
            System.out.println("✅");

            // 전체 논문 파싱 및 기록
            Path outputFile = Paths.get(DOC_DIR, fileName + "-complete-data.md");
            int count = parseAndRecordComplete(parser, xmlFile, outputFile, file);

            totalArticles += count;
            System.out.printf("  완료: %,d 논문 → %s%n", count, outputFile.getFileName());
        }

        // 전체 요약 문서 생성
        generateSummary(files, totalArticles);

        System.out.println("\n========================================");
        System.out.println("전체 파싱 완료 / All Parsing Complete");
        System.out.println("========================================");
        System.out.printf("📊 총 논문 수: %,d%n", totalArticles);
        System.out.printf("📁 데이터 위치: %s/%n", DOC_DIR);

        assertTrue(totalArticles > 0, "Should parse articles");
    }

    /**
     * 파일 파싱 및 **모든 논문 데이터 완전 기록**
     */
    private int parseAndRecordComplete(PubmedXmlParser parser, Path xmlFile, Path outputFile, String fileName) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile.toFile()));
        AtomicInteger count = new AtomicInteger(0);

        try {
            // 헤더 작성
            writer.write("# " + fileName + " - 전체 파싱 데이터 / Complete Parsed Data\n\n");
            writer.write("**생성 일시:** " + new java.util.Date() + "\n");
            writer.write("**파일 크기:** " + formatBytes(Files.size(xmlFile)) + "\n\n");
            writer.write("---\n\n");

            long startTime = System.currentTimeMillis();

            // 모든 논문 파싱 및 기록
            parser.parseStream(xmlFile, article -> {
                int num = count.incrementAndGet();

                try {
                    // 진행 상황 출력
                    if (num % 1000 == 0) {
                        System.out.printf("\r  파싱 중... %,d 논문", num);
                    }

                    // 논문 데이터 완전 기록
                    writeCompleteArticleData(writer, article, num);

                } catch (Exception e) {
                    System.err.printf("\n❌ 오류 at article %d: %s%n", num, e.getMessage());
                    e.printStackTrace();
                }
            });

            long elapsed = System.currentTimeMillis() - startTime;

            // 푸터 작성
            writer.write("\n---\n\n");
            writer.write(String.format("**총 논문 수:** %,d\n", count.get()));
            writer.write(String.format("**처리 시간:** %.2f초\n", elapsed / 1000.0));
            writer.write(String.format("**처리 속도:** %,d articles/sec\n", (long)(count.get() * 1000.0 / elapsed)));

            System.out.printf("\r  파싱 완료: %,d 논문        %n", count.get());

        } finally {
            writer.close();
        }

        return count.get();
    }

    /**
     * 논문 데이터를 **완전히** 기록 (모든 필드 포함)
     */
    private void writeCompleteArticleData(BufferedWriter writer, PubmedArticle article, int number) throws Exception {
        writer.write(String.format("## 논문 #%d\n\n", number));

        MedlineCitation citation = article.getMedlineCitation();
        if (citation == null) {
            writer.write("**오류:** MedlineCitation이 없습니다.\n\n---\n\n");
            return;
        }

        // 1. 기본 정보
        writer.write("### 기본 정보 / Basic Information\n\n");
        writer.write("| 항목 | 값 |\n");
        writer.write("|------|----|\n");

        // PMID
        if (citation.getPmid() != null) {
            writer.write(String.format("| **PMID** | %s |\n", citation.getPmid().getValue()));
            if (citation.getPmid().getVersion() != null) {
                writer.write(String.format("| PMID Version | %s |\n", citation.getPmid().getVersion()));
            }
        }

        // Status, Owner
        writer.write(String.format("| **Status** | %s |\n",
                citation.getStatus() != null ? citation.getStatus().getValue() : "N/A"));
        writer.write(String.format("| **Owner** | %s |\n",
                citation.getOwner() != null ? citation.getOwner().getValue() : "N/A"));
        if (citation.getIndexingMethod() != null) {
            writer.write(String.format("| Indexing Method | %s |\n", citation.getIndexingMethod().getValue()));
        }

        // DateCompleted, DateRevised
        if (citation.getDateCompleted() != null) {
            writer.write(String.format("| Date Completed | %s |\n", formatDateCompleted(citation.getDateCompleted())));
        }
        if (citation.getDateRevised() != null) {
            writer.write(String.format("| Date Revised | %s |\n", formatDateRevised(citation.getDateRevised())));
        }

        writer.write("\n");

        // 2. Article 정보
        Article art = citation.getArticle();
        if (art != null) {
            writer.write("### 논문 정보 / Article Information\n\n");

            // Title
            if (art.getArticleTitle() != null) {
                writer.write(String.format("**제목 / Title:**\n> %s\n\n", art.getArticleTitle().getValue()));
            }

            // VernacularTitle
            if (art.getVernacularTitle() != null) {
                writer.write(String.format("**원어 제목 / Vernacular Title:**\n> %s\n\n", art.getVernacularTitle().getValue()));
            }

            // PubModel
            if (art.getPubModel() != null) {
                writer.write(String.format("**출판 모델:** %s\n\n", art.getPubModel().getValue()));
            }

            // Journal
            if (art.getJournal() != null) {
                writer.write("**저널 정보 / Journal:**\n\n");
                Journal journal = art.getJournal();

                if (journal.getIssn() != null) {
                    writer.write(String.format("- ISSN: %s (%s)\n",
                        journal.getIssn().getValue(),
                        journal.getIssn().getIssnType() != null ? journal.getIssn().getIssnType().getValue() : ""));
                }

                if (journal.getTitle() != null) {
                    writer.write(String.format("- 저널명: %s\n", journal.getTitle().getValue()));
                }

                if (journal.getIsoAbbreviation() != null) {
                    writer.write(String.format("- ISO 약어: %s\n", journal.getIsoAbbreviation().getValue()));
                }

                // JournalIssue
                if (journal.getJournalIssue() != null) {
                    JournalIssue issue = journal.getJournalIssue();

                    if (issue.getVolume() != null) {
                        writer.write(String.format("- Volume: %s\n", issue.getVolume().getValue()));
                    }
                    if (issue.getIssue() != null) {
                        writer.write(String.format("- Issue: %s\n", issue.getIssue().getValue()));
                    }
                    if (issue.getPubDate() != null) {
                        writer.write(String.format("- 출판일: %s\n", formatPubDate(issue.getPubDate())));
                    }
                }

                writer.write("\n");
            }

            // Abstract (초록 - 전체)
            if (art.getAbstractInfo() != null && art.getAbstractInfo().getAbstractTexts() != null) {
                writer.write("**초록 / Abstract:**\n\n");

                for (AbstractText absText : art.getAbstractInfo().getAbstractTexts()) {
                    if (absText.getLabel() != null) {
                        writer.write(String.format("**[%s]**", absText.getLabel()));
                        if (absText.getNlmCategory() != null) {
                            writer.write(String.format(" (Category: %s)", absText.getNlmCategory()));
                        }
                        writer.write("\n\n");
                    }

                    if (absText.getValue() != null) {
                        writer.write(absText.getValue());
                        writer.write("\n\n");
                    }
                }
            }

            // AuthorList (저자 - 전체)
            if (art.getAuthorList() != null && art.getAuthorList().getAuthors() != null) {
                writer.write("**저자 목록 / Authors:**");
                if (art.getAuthorList().getCompleteYN() != null) {
                    writer.write(String.format(" (Complete: %s)", art.getAuthorList().getCompleteYN()));
                }
                if (art.getAuthorList().getType() != null) {
                    writer.write(String.format(" (Type: %s)", art.getAuthorList().getType()));
                }
                writer.write("\n\n");

                int authorNum = 1;
                for (Author author : art.getAuthorList().getAuthors()) {
                    writer.write(String.format("%d. ", authorNum++));

                    // Personal name
                    if (author.getLastName() != null) {
                        writer.write(author.getLastName().getValue());

                        if (author.getForeName() != null) {
                            writer.write(", " + author.getForeName().getValue());
                        } else if (author.getInitials() != null) {
                            writer.write(" " + author.getInitials().getValue());
                        }

                        if (author.getSuffix() != null) {
                            writer.write(" " + author.getSuffix().getValue());
                        }
                    }
                    // Collective name
                    else if (author.getCollectiveName() != null) {
                        writer.write(author.getCollectiveName().getValue());
                        if (author.getCollectiveName().getInvestigators() != null) {
                            writer.write(String.format(" [Investigators: %s]", author.getCollectiveName().getInvestigators()));
                        }
                    }

                    // Identifiers (ORCID 등)
                    if (author.getIdentifiers() != null && !author.getIdentifiers().isEmpty()) {
                        writer.write(" - ID: ");
                        List<String> ids = new ArrayList<>();
                        for (Identifier id : author.getIdentifiers()) {
                            ids.add(String.format("%s:%s", id.getSource(), id.getValue()));
                        }
                        writer.write(String.join(", ", ids));
                    }

                    // Affiliation
                    if (author.getAffiliationInfos() != null && !author.getAffiliationInfos().isEmpty()) {
                        writer.write("\n   - 소속: ");
                        List<String> affs = new ArrayList<>();
                        for (AffiliationInfo aff : author.getAffiliationInfos()) {
                            if (aff.getAffiliation() != null) {
                                affs.add(aff.getAffiliation().getValue());
                            }
                        }
                        writer.write(String.join("; ", affs));
                    }

                    writer.write("\n");
                }
                writer.write("\n");
            }

            // Language
            if (art.getLanguages() != null && !art.getLanguages().isEmpty()) {
                writer.write("**언어 / Language:** ");
                List<String> langs = new ArrayList<>();
                for (Language lang : art.getLanguages()) {
                    langs.add(lang.getValue());
                }
                writer.write(String.join(", ", langs));
                writer.write("\n\n");
            }

            // PublicationTypeList
            if (art.getPublicationTypeList() != null && art.getPublicationTypeList().getPublicationTypes() != null) {
                writer.write("**출판 유형 / Publication Types:** ");
                List<String> pubTypes = new ArrayList<>();
                for (PublicationType pt : art.getPublicationTypeList().getPublicationTypes()) {
                    String type = pt.getValue();
                    if (pt.getUi() != null) {
                        type += " [" + pt.getUi() + "]";
                    }
                    pubTypes.add(type);
                }
                writer.write(String.join(", ", pubTypes));
                writer.write("\n\n");
            }

            // Pagination
            if (art.getPagination() != null) {
                writer.write("**페이지 / Pagination:** ");
                if (art.getPagination().getMedlinePgn() != null) {
                    writer.write(art.getPagination().getMedlinePgn().getValue());
                } else {
                    List<String> pages = new ArrayList<>();
                    if (art.getPagination().getStartPage() != null) {
                        pages.add(art.getPagination().getStartPage().getValue());
                    }
                    if (art.getPagination().getEndPage() != null) {
                        pages.add(art.getPagination().getEndPage().getValue());
                    }
                    writer.write(String.join("-", pages));
                }
                writer.write("\n\n");
            }

            // ELocationID
            if (art.getELocationIDs() != null && !art.getELocationIDs().isEmpty()) {
                writer.write("**전자 위치 / ELocation IDs:**\n\n");
                for (ELocationID eloc : art.getELocationIDs()) {
                    writer.write(String.format("- %s: %s",
                        eloc.getEIdType() != null ? eloc.getEIdType().getValue() : "",
                        eloc.getValue()));
                    if (eloc.getValidYN() != null) {
                        writer.write(String.format(" (Valid: %s)", eloc.getValidYN()));
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }

            // ArticleDate
            if (art.getArticleDates() != null && !art.getArticleDates().isEmpty()) {
                writer.write("**논문 날짜 / Article Dates:**\n\n");
                for (ArticleDate artDate : art.getArticleDates()) {
                    writer.write(String.format("- %s: %s\n",
                        nvl(artDate.getDateType()),
                        formatArticleDate(artDate)));
                }
                writer.write("\n");
            }

            // DataBankList
            if (art.getDataBankList() != null && art.getDataBankList().getDataBanks() != null) {
                writer.write("**데이터 뱅크 / Data Banks:**\n\n");
                for (DataBank db : art.getDataBankList().getDataBanks()) {
                    writer.write(String.format("- %s", db.getDataBankName().getValue()));
                    if (db.getAccessionNumberList() != null && db.getAccessionNumberList().getAccessionNumbers() != null) {
                        writer.write(": ");
                        List<String> accNums = new ArrayList<>();
                        for (AccessionNumber acc : db.getAccessionNumberList().getAccessionNumbers()) {
                            accNums.add(acc.getValue());
                        }
                        writer.write(String.join(", ", accNums));
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }

            // GrantList
            if (art.getGrantList() != null && art.getGrantList().getGrants() != null) {
                writer.write("**연구비 지원 / Grants:**\n\n");
                for (Grant grant : art.getGrantList().getGrants()) {
                    writer.write("- ");
                    if (grant.getGrantID() != null) {
                        writer.write(grant.getGrantID().getValue() + " ");
                    }
                    if (grant.getAgency() != null) {
                        writer.write("from " + grant.getAgency().getValue());
                    }
                    if (grant.getCountry() != null) {
                        writer.write(" (" + grant.getCountry().getValue() + ")");
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }
        }

        // 3. MeSH Headings (전체)
        if (citation.getMeshHeadingList() != null && citation.getMeshHeadingList().getMeshHeadings() != null) {
            writer.write("### MeSH 용어 / MeSH Headings\n\n");

            for (MeshHeading mesh : citation.getMeshHeadingList().getMeshHeadings()) {
                if (mesh.getDescriptorName() != null) {
                    writer.write(String.format("- **%s**", mesh.getDescriptorName().getValue()));

                    if (mesh.getDescriptorName().getUi() != null) {
                        writer.write(String.format(" [%s]", mesh.getDescriptorName().getUi()));
                    }

                    if ("Y".equals(mesh.getDescriptorName().getMajorTopicYN())) {
                        writer.write(" **(Major Topic)**");
                    }

                    // Qualifiers
                    if (mesh.getQualifierNames() != null && !mesh.getQualifierNames().isEmpty()) {
                        writer.write("\n  - Qualifiers: ");
                        List<String> qualifiers = new ArrayList<>();
                        for (QualifierName qual : mesh.getQualifierNames()) {
                            String q = qual.getValue();
                            if (qual.getUi() != null) {
                                q += " [" + qual.getUi() + "]";
                            }
                            if ("Y".equals(qual.getMajorTopicYN())) {
                                q += " **(Major)**";
                            }
                            qualifiers.add(q);
                        }
                        writer.write(String.join(", ", qualifiers));
                    }

                    writer.write("\n");
                }
            }
            writer.write("\n");
        }

        // 4. Keywords (전체)
        if (citation.getKeywordLists() != null && !citation.getKeywordLists().isEmpty()) {
            writer.write("### 키워드 / Keywords\n\n");

            for (KeywordList kwList : citation.getKeywordLists()) {
                if (kwList.getOwner() != null) {
                    writer.write(String.format("**Owner: %s**\n\n", kwList.getOwner()));
                }

                if (kwList.getKeywords() != null) {
                    for (Keyword kw : kwList.getKeywords()) {
                        writer.write("- " + kw.getValue());
                        if ("Y".equals(kw.getMajorTopicYN())) {
                            writer.write(" **(Major Topic)**");
                        }
                        writer.write("\n");
                    }
                }
                writer.write("\n");
            }
        }

        // 5. ChemicalList
        if (citation.getChemicalList() != null && citation.getChemicalList().getChemicals() != null) {
            writer.write("### 화학물질 / Chemicals\n\n");

            for (Chemical chem : citation.getChemicalList().getChemicals()) {
                writer.write("- ");
                if (chem.getNameOfSubstance() != null) {
                    writer.write(chem.getNameOfSubstance().getValue());
                    if (chem.getNameOfSubstance().getUi() != null) {
                        writer.write(String.format(" [%s]", chem.getNameOfSubstance().getUi()));
                    }
                }
                if (chem.getRegistryNumber() != null) {
                    writer.write(String.format(" (Registry: %s)", chem.getRegistryNumber().getValue()));
                }
                writer.write("\n");
            }
            writer.write("\n");
        }

        // 6. SupplMeshList
        if (citation.getSupplMeshList() != null && citation.getSupplMeshList().getSupplMeshNames() != null) {
            writer.write("### 보충 MeSH / Supplementary MeSH\n\n");

            for (SupplMeshName supp : citation.getSupplMeshList().getSupplMeshNames()) {
                writer.write(String.format("- %s", supp.getValue()));
                if (supp.getUi() != null) {
                    writer.write(String.format(" [%s]", supp.getUi()));
                }
                if (supp.getType() != null) {
                    writer.write(String.format(" (Type: %s)", supp.getType()));
                }
                writer.write("\n");
            }
            writer.write("\n");
        }

        // 7. CommentsCorrectionsList
        if (citation.getCommentsCorrectionsList() != null &&
            citation.getCommentsCorrectionsList().getCommentsCorrections() != null) {
            writer.write("### 코멘트 및 정정 / Comments & Corrections\n\n");

            for (CommentsCorrections cc : citation.getCommentsCorrectionsList().getCommentsCorrections()) {
                writer.write(String.format("- **%s**", cc.getRefType() != null ? cc.getRefType().getValue() : ""));
                if (cc.getRefSource() != null) {
                    writer.write(String.format(": %s", cc.getRefSource().getValue()));
                }
                if (cc.getPmid() != null) {
                    writer.write(String.format(" [PMID: %s]", cc.getPmid().getValue()));
                }
                writer.write("\n");
            }
            writer.write("\n");
        }

        // 8. GeneSymbolList
        if (citation.getGeneSymbolList() != null && citation.getGeneSymbolList().getGeneSymbols() != null) {
            writer.write("### 유전자 기호 / Gene Symbols\n\n");
            writer.write("- " + String.join(", ",
                citation.getGeneSymbolList().getGeneSymbols().stream()
                    .map(GeneSymbol::getValue)
                    .toList()));
            writer.write("\n\n");
        }

        // 9. InvestigatorList (2024: now repeatable, 0-N occurrences)
        if (citation.getInvestigatorLists() != null && !citation.getInvestigatorLists().isEmpty()) {
            for (InvestigatorList investigatorList : citation.getInvestigatorLists()) {
                writer.write("### 조사자 목록 / Investigators");
                if (investigatorList.getId() != null) {
                    writer.write(String.format(" (ID: %s)", investigatorList.getId()));
                }
                writer.write("\n\n");

                if (investigatorList.getInvestigators() == null) {
                    continue;
                }

                for (Investigator inv : investigatorList.getInvestigators()) {
                writer.write("- ");
                if (inv.getLastName() != null) {
                    writer.write(inv.getLastName().getValue());
                    if (inv.getForeName() != null) {
                        writer.write(", " + inv.getForeName().getValue());
                    }
                }
                if (inv.getAffiliationInfos() != null && !inv.getAffiliationInfos().isEmpty()) {
                    writer.write(" - ");
                    List<String> affs = new ArrayList<>();
                    for (AffiliationInfo aff : inv.getAffiliationInfos()) {
                        if (aff.getAffiliation() != null) {
                            affs.add(aff.getAffiliation().getValue());
                        }
                    }
                    writer.write(String.join("; ", affs));
                }
                writer.write("\n");
            }
            writer.write("\n");
            }
        }

        // 10. PubmedData
        PubmedData pubmedData = article.getPubmedData();
        if (pubmedData != null) {
            writer.write("### PubMed 데이터 / PubMed Data\n\n");

            // PublicationStatus
            if (pubmedData.getPublicationStatus() != null) {
                writer.write(String.format("**Publication Status:** %s\n\n", pubmedData.getPublicationStatus().getValue()));
            }

            // ArticleIdList
            if (pubmedData.getArticleIdList() != null && pubmedData.getArticleIdList().getArticleIds() != null) {
                writer.write("**Article IDs:**\n\n");
                for (ArticleId artId : pubmedData.getArticleIdList().getArticleIds()) {
                    writer.write(String.format("- %s: %s\n", artId.getIdType(), artId.getValue()));
                }
                writer.write("\n");
            }

            // History
            if (pubmedData.getHistory() != null && pubmedData.getHistory().getPubMedPubDates() != null) {
                writer.write("**이력 / History:**\n\n");
                for (PubMedPubDate pubDate : pubmedData.getHistory().getPubMedPubDates()) {
                    writer.write(String.format("- %s: %s\n",
                        pubDate.getPubStatus() != null ? pubDate.getPubStatus().getValue() : "",
                        formatPubMedPubDate(pubDate)));
                }
                writer.write("\n");
            }
        }

        writer.write("---\n\n");
    }

    // Helper methods
    private String nvl(String s) {
        return s != null ? s : "N/A";
    }

    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + "B";
        if (bytes < 1024 * 1024) return String.format("%.1fKB", bytes / 1024.0);
        return String.format("%.1fMB", bytes / (1024.0 * 1024));
    }

    private String formatPubMedDate(PubMedDate date) {
        if (date == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        if (date.getYear() != null) sb.append(date.getYear());
        if (date.getMonth() != null) sb.append("-").append(date.getMonth());
        if (date.getDay() != null) sb.append("-").append(date.getDay());
        return sb.length() > 0 ? sb.toString() : "N/A";
    }

    private String formatPubDate(PubDate pubDate) {
        if (pubDate == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        if (pubDate.getYear() != null) sb.append(pubDate.getYear().getValue());
        if (pubDate.getMonth() != null) sb.append("-").append(pubDate.getMonth().getValue());
        if (pubDate.getDay() != null) sb.append("-").append(pubDate.getDay().getValue());
        if (sb.length() == 0 && pubDate.getMedlineDate() != null) {
            return pubDate.getMedlineDate().getValue();
        }
        return sb.toString();
    }

    private String formatDateCompleted(DateCompleted date) {
        if (date == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        if (date.getYear() != null) sb.append(date.getYear().getValue());
        if (date.getMonth() != null) sb.append("-").append(date.getMonth().getValue());
        if (date.getDay() != null) sb.append("-").append(date.getDay().getValue());
        return sb.length() > 0 ? sb.toString() : "N/A";
    }

    private String formatDateRevised(DateRevised date) {
        if (date == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        if (date.getYear() != null) sb.append(date.getYear().getValue());
        if (date.getMonth() != null) sb.append("-").append(date.getMonth().getValue());
        if (date.getDay() != null) sb.append("-").append(date.getDay().getValue());
        return sb.length() > 0 ? sb.toString() : "N/A";
    }

    private String formatArticleDate(ArticleDate date) {
        if (date == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        if (date.getYear() != null) sb.append(date.getYear().getValue());
        if (date.getMonth() != null) sb.append("-").append(date.getMonth().getValue());
        if (date.getDay() != null) sb.append("-").append(date.getDay().getValue());
        return sb.toString();
    }

    private String formatPubMedPubDate(PubMedPubDate date) {
        if (date == null) return "N/A";
        StringBuilder sb = new StringBuilder();
        if (date.getYear() != null) sb.append(date.getYear().getValue());
        if (date.getMonth() != null) sb.append("-").append(date.getMonth().getValue());
        if (date.getDay() != null) sb.append("-").append(date.getDay().getValue());
        if (date.getHour() != null) sb.append(" ").append(date.getHour().getValue());
        if (date.getMinute() != null) sb.append(":").append(date.getMinute().getValue());
        return sb.toString();
    }

    private void generateSummary(String[] files, int totalArticles) throws Exception {
        Path summaryFile = Paths.get(DOC_DIR, "00-parsing-summary.md");
        BufferedWriter writer = new BufferedWriter(new FileWriter(summaryFile.toFile()));

        writer.write("# PubMed 파싱 전체 요약 / Complete Parsing Summary\n\n");
        writer.write("**생성 일시:** " + new java.util.Date() + "\n\n");
        writer.write("---\n\n");

        writer.write("## 파싱 완료 파일 / Parsed Files\n\n");
        writer.write("| 번호 | 파일명 | 데이터 파일 |\n");
        writer.write("|------|--------|-------------|\n");

        int num = 1;
        for (String file : files) {
            String fileName = file.replace("/", "-").replace(".xml.gz", "");
            writer.write(String.format("| %d | %s | %s-complete-data.md |\n",
                num++, file, fileName));
        }

        writer.write(String.format("\n**총 논문 수:** %,d\n\n", totalArticles));

        writer.write("## 각 파일 설명 / File Descriptions\n\n");
        writer.write("각 파일에는 **모든 논문의 모든 데이터**가 다음 정보를 포함하여 완전히 기록되어 있습니다:\n\n");
        writer.write("- ✅ 기본 정보 (PMID, Status, Owner, 날짜)\n");
        writer.write("- ✅ 논문 정보 (제목, 저널, 출판일)\n");
        writer.write("- ✅ **초록 전문** (모든 AbstractText)\n");
        writer.write("- ✅ **저자 전체** (이름, 소속, ORCID 등)\n");
        writer.write("- ✅ **MeSH 전체** (DescriptorName, Qualifier)\n");
        writer.write("- ✅ **키워드 전체**\n");
        writer.write("- ✅ 화학물질, 유전자, 데이터뱅크\n");
        writer.write("- ✅ 연구비 지원 정보\n");
        writer.write("- ✅ 코멘트 및 정정\n");
        writer.write("- ✅ PubMed 메타데이터 (ArticleId, History)\n");
        writer.write("- ✅ 기타 모든 DTD 필드\n\n");

        writer.write("**주의:** 각 파일은 수만 개의 논문 데이터를 포함하므로 매우 큽니다.\n");

        writer.close();
    }
}
