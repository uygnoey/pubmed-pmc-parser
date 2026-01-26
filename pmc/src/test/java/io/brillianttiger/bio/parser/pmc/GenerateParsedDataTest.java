package io.brillianttiger.bio.parser.pmc;

import io.brillianttiger.bio.parser.pmc.model.*;
import io.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import org.junit.jupiter.api.Disabled;
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
 * PMC 실제 파싱 데이터 생성 테스트
 */
public class GenerateParsedDataTest {

    @Test
    @Disabled("Manual test: requires external file from ../test-docs-*/. Not for CI.")
    void generatePmcParsedDataDetails() throws Exception {
        System.out.println("📚 Generating PMC parsed data details...");

        PmcXmlParser parser = new PmcXmlParser();
        List<JatsArticle> articles = new ArrayList<>();

        // Extract and parse from REAL PMC tar.gz file downloaded from NCBI FTP
        Path tarGzFile = Paths.get("../test-docs-2601220906/downloads/pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");
        Path tempDir = Paths.get("../test-docs-2601220906/pmc/temp_extracted");

        System.out.println("   Extracting from NCBI FTP download: " + tarGzFile);
        System.out.println("   Source: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/oa_comm/xml/oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");

        // Extract first 10 XML files from tar.gz
        java.nio.file.Files.createDirectories(tempDir);
        ProcessBuilder pb = new ProcessBuilder(
            "tar", "-xzf", tarGzFile.toAbsolutePath().toString(),
            "-C", tempDir.toAbsolutePath().toString(),
            "--strip-components=1"
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();

        // Parse extracted XML files
        java.nio.file.Files.walk(tempDir)
            .filter(path -> path.toString().endsWith(".xml"))
            .limit(10)
            .forEach(xmlPath -> {
                try {
                    JatsArticle article = parser.parseFile(xmlPath);
                    articles.add(article);
                    System.out.println("   Article #" + articles.size() + " parsed: " + xmlPath.getFileName());
                } catch (Exception e) {
                    System.err.println("   Failed to parse: " + xmlPath.getFileName() + " - " + e.getMessage());
                }
            });

        // Cleanup temp directory
        java.nio.file.Files.walk(tempDir)
            .sorted(java.util.Comparator.reverseOrder())
            .forEach(path -> {
                try {
                    java.nio.file.Files.delete(path);
                } catch (Exception e) {
                    // Ignore cleanup errors
                }
            });

        System.out.println("   Total parsed: " + articles.size() + " articles");

        Path outputFile = Paths.get("../test-docs-2601220906/pmc/parsed-data-details.md");
        System.out.println("   Writing to " + outputFile + "...");

        try (PrintWriter w = new PrintWriter(new FileWriter(outputFile.toFile()))) {
            w.println("# PMC 실제 파싱 데이터 (10건)");
            w.println();
            w.println("**파일:** pmc_oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz (실제 NCBI FTP 다운로드)");
            w.println("**소스:** https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/oa_comm/xml/oa_comm_xml.PMC000xxxxxx.baseline.2025-12-18.tar.gz");
            w.println("**파싱 시간:** " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            w.println("**DTD 버전:** JATS 1.4");
            w.println();
            w.println("---");
            w.println();

            for (int i = 0; i < articles.size(); i++) {
                printArticle(w, articles.get(i), i + 1);
            }
        }

        System.out.println("✅ PMC parsed data details created: " + outputFile);
    }

    private void printArticle(PrintWriter w, JatsArticle article, int num) {
        w.println("## Article #" + num);
        w.println();

        Front front = article.getFront();
        if (front == null || front.getArticleMeta() == null) {
            w.println("*(No front matter)*");
            w.println();
            w.println("---");
            w.println();
            return;
        }

        ArticleMeta meta = front.getArticleMeta();

        // IDs
        if (meta.getArticleIds() != null && !meta.getArticleIds().isEmpty()) {
            w.println("### Article IDs");
            for (PmcArticleId id : meta.getArticleIds()) {
                w.println("- **" + id.getPubIdType() + ":** " + id.getValue());
            }
            w.println();
        }

        // Title
        if (meta.getTitleGroup() != null && meta.getTitleGroup().getArticleTitle() != null) {
            w.println("### 제목");
            w.println("> " + meta.getTitleGroup().getArticleTitle());
            w.println();
        }

        // Journal
        if (front.getJournalMeta() != null) {
            JournalMeta jm = front.getJournalMeta();
            w.println("### 저널");
            w.println(jm);
            w.println();
        }

        // Publication Dates
        if (meta.getPubDates() != null && !meta.getPubDates().isEmpty()) {
            w.println("### Publication Dates");
            for (PmcPubDate pd : meta.getPubDates()) {
                String date = "";
                if (pd.getYear() != null) date += pd.getYear();
                if (pd.getMonth() != null) date += "-" + pd.getMonth();
                if (pd.getDay() != null) date += "-" + pd.getDay();
                w.println("- **" + (pd.getPubType() != null ? pd.getPubType() : "date") + ":** " + date);
            }
            w.println();
        }

        // Authors
        if (meta.getContribGroups() != null && !meta.getContribGroups().isEmpty()) {
            ContribGroup cg = meta.getContribGroups().get(0);
            if (cg.getContributors() != null && !cg.getContributors().isEmpty()) {
                List<Contrib> contribs = cg.getContributors();
                w.println("### 저자 (" + contribs.size() + "명)");
                for (int i = 0; i < Math.min(12, contribs.size()); i++) {
                    Contrib contrib = contribs.get(i);
                    w.println((i + 1) + ". " + contrib.getName());
                }
                if (contribs.size() > 12) w.println("- ... 외 " + (contribs.size() - 12) + "명");
                w.println();
            }
        }

        // Affiliations
        if (meta.getAffiliations() != null && !meta.getAffiliations().isEmpty()) {
            w.println("### 소속 기관 (" + meta.getAffiliations().size() + "개)");
            for (int i = 0; i < Math.min(5, meta.getAffiliations().size()); i++) {
                Aff aff = meta.getAffiliations().get(i);
                w.println("**#" + aff.getId() + ":** " + aff.getValue());
            }
            if (meta.getAffiliations().size() > 5) w.println("- ... 외 " + (meta.getAffiliations().size() - 5) + "개");
            w.println();
        }

        // Abstract
        if (meta.getAbstracts() != null && !meta.getAbstracts().isEmpty()) {
            w.println("### 초록");
            PmcAbstract abs = meta.getAbstracts().get(0);
            if (abs.getTitle() != null) {
                w.println("**" + abs.getTitle() + "**");
                w.println();
            }

            // Abstract paragraphs
            if (abs.getParagraphs() != null && !abs.getParagraphs().isEmpty()) {
                for (P p : abs.getParagraphs()) {
                    if (p.getValue() != null) {
                        String text = p.getValue();
                        if (text.length() > 500) {
                            w.println(text.substring(0, 500) + "...");
                        } else {
                            w.println(text);
                        }
                        w.println();
                    }
                }
            }

            // Abstract sections (structured abstract)
            if (abs.getSections() != null && !abs.getSections().isEmpty()) {
                for (Sec sec : abs.getSections()) {
                    if (sec.getTitle() != null) {
                        w.println("**" + sec.getTitle() + ":**");
                        w.println();
                    }
                    if (sec.getParagraphs() != null && !sec.getParagraphs().isEmpty()) {
                        for (P p : sec.getParagraphs()) {
                            if (p.getValue() != null) {
                                String text = p.getValue();
                                if (text.length() > 500) {
                                    w.println(text.substring(0, 500) + "...");
                                } else {
                                    w.println(text);
                                }
                                w.println();
                            }
                        }
                    }
                }
            }
        }

        // Body sections with FULL CONTENT
        if (article.getBody() != null && article.getBody().getSections() != null) {
            List<Sec> sections = article.getBody().getSections();
            w.println("### 본문 내용 (" + sections.size() + " 섹션)");
            w.println();

            for (Sec sec : sections) {
                // Section title
                if (sec.getTitle() != null) {
                    w.println("#### " + sec.getTitle());
                    w.println();
                }

                // Section paragraphs
                if (sec.getParagraphs() != null && !sec.getParagraphs().isEmpty()) {
                    for (P p : sec.getParagraphs()) {
                        if (p.getValue() != null) {
                            String text = p.getValue();
                            if (text.length() > 800) {
                                w.println(text.substring(0, 800) + "...");
                            } else {
                                w.println(text);
                            }
                            w.println();
                        }
                    }
                }

                // Subsections
                if (sec.getSections() != null && !sec.getSections().isEmpty()) {
                    for (Sec subsec : sec.getSections()) {
                        if (subsec.getTitle() != null) {
                            w.println("##### " + subsec.getTitle());
                            w.println();
                        }

                        if (subsec.getParagraphs() != null && !subsec.getParagraphs().isEmpty()) {
                            for (P p : subsec.getParagraphs()) {
                                if (p.getValue() != null) {
                                    String text = p.getValue();
                                    if (text.length() > 600) {
                                        w.println(text.substring(0, 600) + "...");
                                    } else {
                                        w.println(text);
                                    }
                                    w.println();
                                }
                            }
                        }
                    }
                }

                w.println();
            }
        }

        // References with detailed citation
        if (article.getBack() != null && article.getBack().getRefLists() != null
                && !article.getBack().getRefLists().isEmpty()) {
            RefList refList = article.getBack().getRefLists().get(0);
            if (refList.getReferences() != null) {
                List<Ref> refs = refList.getReferences();
                w.println("### 참고문헌 (" + refs.size() + "개)");
                w.println();

                for (int i = 0; i < Math.min(10, refs.size()); i++) {
                    Ref ref = refs.get(i);
                    w.print((i + 1) + ". ");

                    // Element citation (structured)
                    if (ref.getElementCitations() != null && !ref.getElementCitations().isEmpty()) {
                        ElementCitation cit = ref.getElementCitations().get(0);

                        // Authors
                        if (cit.getPersonGroups() != null && !cit.getPersonGroups().isEmpty()) {
                            PersonGroup pg = cit.getPersonGroups().get(0);
                            if (pg.getNames() != null && !pg.getNames().isEmpty()) {
                                PersonName pn = pg.getNames().get(0);
                                w.print(pn.getSurname().getValue());
                                if (pn.getGivenNames() != null) {
                                    w.print(" " + pn.getGivenNames().getValue());
                                }
                                if (pg.getNames().size() > 1) {
                                    w.print(" et al.");
                                }
                                w.print(". ");
                            }
                        }

                        // Title
                        if (cit.getArticleTitle() != null) {
                            w.print("\"" + cit.getArticleTitle().getValue() + "\" ");
                        }

                        // Source
                        if (cit.getSource() != null) {
                            w.print("*" + cit.getSource().getValue() + "*");
                        }

                        // Year, Volume, Pages
                        if (cit.getYear() != null) {
                            w.print(" " + cit.getYear().getValue());
                        }
                        if (cit.getVolume() != null) {
                            w.print("; " + cit.getVolume().getValue());
                        }
                        if (cit.getFpage() != null) {
                            w.print(": " + cit.getFpage().getValue());
                            if (cit.getLpage() != null) {
                                w.print("-" + cit.getLpage().getValue());
                            }
                        }

                        // DOI
                        if (cit.getPubIds() != null && !cit.getPubIds().isEmpty()) {
                            for (PubId pubId : cit.getPubIds()) {
                                if (pubId.getPubIdType() != null && "DOI".equalsIgnoreCase(pubId.getPubIdType().getValue())) {
                                    w.print(" DOI: " + pubId.getValue());
                                }
                            }
                        }

                        w.println();
                    }
                    // Mixed citation (unstructured)
                    else if (ref.getMixedCitations() != null && !ref.getMixedCitations().isEmpty()) {
                        MixedCitation mc = ref.getMixedCitations().get(0);
                        // MixedCitation은 복잡한 구조이므로 toString() 사용
                        String citation = mc.toString();
                        if (citation.length() > 200) {
                            w.println(citation.substring(0, 200) + "...");
                        } else {
                            w.println(citation);
                        }
                    }
                    else {
                        w.println("(No citation content)");
                    }
                }

                if (refs.size() > 10) w.println();
                if (refs.size() > 10) w.println("- ... 외 " + (refs.size() - 10) + "개");
                w.println();
            }
        }

        w.println("---");
        w.println();
    }
}
