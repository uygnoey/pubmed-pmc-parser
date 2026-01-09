package com.brillianttiger.bio.parser.pmc;

import com.brillianttiger.bio.parser.pmc.model.*;
import com.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PmcXmlParserTest / PMC XML 파서 테스트
 *
 * KR: PMC XML 파서의 모든 기능을 테스트하는 종합 테스트 클래스
 * EN: Comprehensive test class for all PMC XML parser functionality
 *
 * Test Cases:
 * 1. Article attributes parsing
 * 2. Journal metadata parsing
 * 3. Article metadata parsing
 * 4. Title and authors parsing
 * 5. Abstract and keywords parsing
 * 6. Publication dates and history parsing
 * 7. Permissions and copyright parsing
 * 8. Body section parsing
 * 9. Back references parsing
 * 10. Large file streaming
 */
class PmcXmlParserTest {

    private static PmcXmlParser parser;
    private static Path sampleXmlPath;

    @BeforeAll
    static void setUp() {
        parser = new PmcXmlParser();
        // src/test/resources/sample-pmc.xml
        sampleXmlPath = Paths.get("src/test/resources/sample-pmc.xml");
    }

    /**
     * 테스트 1: Article 속성 파싱 / Test 1: Article Attributes Parsing
     *
     * KR: Article 요소의 모든 속성이 올바르게 파싱되는지 검증
     * EN: Verify all Article element attributes are parsed correctly
     */
    @Test
    void testArticleAttributes() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);

        // Then
        assertNotNull(result, "파싱 결과가 null이 아니어야 함 / Parse result should not be null");

        List<PmcArticle> articles = result.getArticles();
        assertNotNull(articles, "Article 리스트가 null이 아니어야 함 / Article list should not be null");
        assertEquals(1, articles.size(), "Article이 1개여야 함 / Should have 1 article");

        PmcArticle article = articles.get(0);
        assertEquals("research-article", article.getArticleType(), "Article type 검증 / Verify article type");
        assertEquals("1.3", article.getDtdVersion(), "DTD version 검증 / Verify DTD version");
        assertEquals("en", article.getXmlLang(), "언어 속성 검증 / Verify language attribute");
    }

    /**
     * 테스트 2: Journal Metadata 파싱 / Test 2: Journal Metadata Parsing
     *
     * KR: Journal-meta의 모든 하위 요소가 올바르게 파싱되는지 검증
     * EN: Verify all Journal-meta child elements are parsed correctly
     */
    @Test
    void testJournalMetadata() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        PmcArticle article = result.getArticles().get(0);
        Front front = article.getFront();

        // Then
        assertNotNull(front, "Front가 null이 아니어야 함 / Front should not be null");

        JournalMeta journalMeta = front.getJournalMeta();
        assertNotNull(journalMeta, "JournalMeta가 null이 아니어야 함 / JournalMeta should not be null");

        // Journal IDs
        assertNotNull(journalMeta.getJournalIds(), "Journal ID 리스트가 null이 아니어야 함 / Journal ID list should not be null");
        assertEquals(4, journalMeta.getJournalIds().size(), "Journal ID 4개 확인 / Should have 4 journal IDs");

        JournalId nlmTaId = journalMeta.getJournalIds().stream()
                .filter(id -> id.getJournalIdType() == JournalIdType.NLM_TA)
                .findFirst()
                .orElse(null);
        assertNotNull(nlmTaId, "NLM-TA ID가 존재해야 함 / NLM-TA ID should exist");
        assertEquals("J Biomed Inform", nlmTaId.getValue());

        // Journal Title Group
        assertNotNull(journalMeta.getJournalTitleGroups(), "JournalTitleGroups가 null이 아니어야 함 / JournalTitleGroups should not be null");
        JournalTitleGroup titleGroup = journalMeta.getJournalTitleGroups().get(0);
        assertNotNull(titleGroup, "JournalTitleGroup이 null이 아니어야 함 / JournalTitleGroup should not be null");
        assertNotNull(titleGroup.getJournalTitles(), "JournalTitles가 null이 아니어야 함 / JournalTitles should not be null");
        assertEquals("Journal of Biomedical Informatics", titleGroup.getJournalTitles().get(0).getValue());

        // ISSN
        assertNotNull(journalMeta.getIssns(), "ISSN 리스트가 null이 아니어야 함 / ISSN list should not be null");
        assertEquals(2, journalMeta.getIssns().size(), "ISSN 2개 확인 / Should have 2 ISSNs");

        Issn ppubIssn = journalMeta.getIssns().stream()
                .filter(issn -> issn.getPubType() != null && issn.getPubType() == PubType.PPUB)
                .findFirst()
                .orElse(null);
        assertNotNull(ppubIssn, "Print ISSN이 존재해야 함 / Print ISSN should exist");
        assertEquals("1532-0464", ppubIssn.getValue());

        // Publisher
        assertNotNull(journalMeta.getPublisher(), "Publisher가 null이 아니어야 함 / Publisher should not be null");
        assertNotNull(journalMeta.getPublisher().getPublisherNames(), "PublisherNames가 null이 아니어야 함 / PublisherNames should not be null");
        assertEquals("Elsevier Science", journalMeta.getPublisher().getPublisherNames().get(0).getValue());
    }

    /**
     * 테스트 3: Article Metadata 기본 정보 파싱 / Test 3: Article Metadata Basic Info Parsing
     *
     * KR: Article-meta의 기본 정보 (ID, 카테고리 등)가 올바르게 파싱되는지 검증
     * EN: Verify Article-meta basic information is parsed correctly
     */
    @Test
    void testArticleMetadataBasicInfo() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        ArticleMeta articleMeta = result.getArticles().get(0).getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta, "ArticleMeta가 null이 아니어야 함 / ArticleMeta should not be null");

        // Article IDs
        assertNotNull(articleMeta.getArticleIds(), "Article ID 리스트가 null이 아니어야 함 / Article ID list should not be null");
        assertEquals(4, articleMeta.getArticleIds().size(), "Article ID 4개 확인 / Should have 4 article IDs");

        PmcArticleId pmid = articleMeta.getArticleIds().stream()
                .filter(id -> "pmid".equals(id.getPubIdType()))
                .findFirst()
                .orElse(null);
        assertNotNull(pmid, "PMID가 존재해야 함 / PMID should exist");
        assertEquals("12345678", pmid.getValue());

        PmcArticleId doi = articleMeta.getArticleIds().stream()
                .filter(id -> "doi".equals(id.getPubIdType()))
                .findFirst()
                .orElse(null);
        assertNotNull(doi, "DOI가 존재해야 함 / DOI should exist");
        assertEquals("10.1016/j.jbi.2023.104321", doi.getValue());

        // Article Categories
        assertNotNull(articleMeta.getArticleCategories(), "ArticleCategories가 null이 아니어야 함 / ArticleCategories should not be null");

        // Volume and Issue
        assertNotNull(articleMeta.getVolume(), "Volume이 null이 아니어야 함 / Volume should not be null");
        assertEquals("145", articleMeta.getVolume().getValue());

        assertNotNull(articleMeta.getIssue(), "Issue가 null이 아니어야 함 / Issue should not be null");
        assertEquals("3", articleMeta.getIssue().getValue());

        // Page information
        assertNotNull(articleMeta.getFpage(), "Fpage가 null이 아니어야 함 / Fpage should not be null");
        assertEquals("100", articleMeta.getFpage().getValue());

        assertNotNull(articleMeta.getLpage(), "Lpage가 null이 아니어야 함 / Lpage should not be null");
        assertEquals("125", articleMeta.getLpage().getValue());

        assertNotNull(articleMeta.getPageRange(), "PageRange가 null이 아니어야 함 / PageRange should not be null");
        assertEquals("100-125", articleMeta.getPageRange().getValue());
    }

    /**
     * 테스트 4: Title과 Authors 파싱 / Test 4: Title and Authors Parsing
     *
     * KR: 제목 그룹과 저자 정보가 올바르게 파싱되는지 검증
     * EN: Verify title group and author information are parsed correctly
     */
    @Test
    void testTitleAndAuthors() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        ArticleMeta articleMeta = result.getArticles().get(0).getFront().getArticleMeta();

        // Then: Title Group
        assertNotNull(articleMeta.getTitleGroup(), "TitleGroup이 null이 아니어야 함 / TitleGroup should not be null");

        ArticleTitle articleTitle = articleMeta.getTitleGroup().getArticleTitle();
        assertNotNull(articleTitle, "ArticleTitle이 null이 아니어야 함 / ArticleTitle should not be null");
        assertTrue(articleTitle.getContent().contains("Advanced XML Parsing"), "제목 내용 확인 / Verify title content");

        assertNotNull(articleMeta.getTitleGroup().getSubtitles(), "Subtitles가 null이 아니어야 함 / Subtitles should not be null");
        Subtitle subtitle = articleMeta.getTitleGroup().getSubtitles().get(0);
        assertNotNull(subtitle, "Subtitle이 null이 아니어야 함 / Subtitle should not be null");
        assertEquals("A Comprehensive Study", subtitle.getContent());

        // Contrib Groups
        assertNotNull(articleMeta.getContribGroups(), "ContribGroup 리스트가 null이 아니어야 함 / ContribGroup list should not be null");
        assertEquals(1, articleMeta.getContribGroups().size());

        ContribGroup contribGroup = articleMeta.getContribGroups().get(0);
        assertNotNull(contribGroup.getContributors(), "Contributor 리스트가 null이 아니어야 함 / Contributor list should not be null");
        assertEquals(3, contribGroup.getContributors().size(), "저자 3명 확인 / Should have 3 contributors");

        // First author (personal)
        Contrib firstAuthor = contribGroup.getContributors().get(0);
        assertEquals("author", firstAuthor.getContribType(), "ContribType 확인 / Verify contrib type");
        assertEquals("yes", firstAuthor.getCorresp(), "Corresponding author 확인 / Verify corresponding author");

        assertNotNull(firstAuthor.getContribIds(), "ContribId 리스트가 null이 아니어야 함 / ContribId list should not be null");
        assertEquals(1, firstAuthor.getContribIds().size());
        assertEquals(ContribIdType.ORCID, firstAuthor.getContribIds().get(0).getContribIdType());
        assertEquals("0000-0002-1234-5678", firstAuthor.getContribIds().get(0).getValue());

        Name name = firstAuthor.getName();
        assertNotNull(name, "Name이 null이 아니어야 함 / Name should not be null");
        assertEquals("Kim", name.getSurname().getValue());
        assertEquals("Yong-Min", name.getGivenNames().getValue());
        assertEquals("Dr.", name.getPrefix().getValue());
        assertEquals("PhD", name.getSuffix().getValue());

        assertNotNull(firstAuthor.getEmails(), "Emails가 null이 아니어야 함 / Emails should not be null");
        assertEquals("yongmin.kim@example.edu", firstAuthor.getEmails().get(0).getValue());

        assertNotNull(firstAuthor.getRoles(), "Roles가 null이 아니어야 함 / Roles should not be null");
        assertEquals("Principal Investigator", firstAuthor.getRoles().get(0).getValue());

        // Second author
        Contrib secondAuthor = contribGroup.getContributors().get(1);
        assertEquals("Lee", secondAuthor.getName().getSurname().getValue());
        assertEquals("Su-Jin", secondAuthor.getName().getGivenNames().getValue());

        assertNotNull(secondAuthor.getDegrees(), "Degrees가 null이 아니어야 함 / Degrees should not be null");
        assertEquals("MSc", secondAuthor.getDegrees().get(0).getValue());

        // Third author (collaborative)
        Contrib thirdAuthor = contribGroup.getContributors().get(2);
        assertNotNull(thirdAuthor.getCollab(), "Collab이 null이 아니어야 함 / Collab should not be null");
        assertEquals("Biomedical Informatics Research Consortium", thirdAuthor.getCollab().getValue());

        // Affiliations
        assertNotNull(articleMeta.getAffiliations(), "Affiliation 리스트가 null이 아니어야 함 / Affiliation list should not be null");
        assertEquals(2, articleMeta.getAffiliations().size(), "소속 2개 확인 / Should have 2 affiliations");

        Aff firstAff = articleMeta.getAffiliations().get(0);
        assertEquals("aff1", firstAff.getId());
    }

    /**
     * 테스트 5: Abstract와 Keywords 파싱 / Test 5: Abstract and Keywords Parsing
     *
     * KR: 초록과 키워드가 올바르게 파싱되는지 검증
     * EN: Verify abstract and keywords are parsed correctly
     */
    @Test
    void testAbstractAndKeywords() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        ArticleMeta articleMeta = result.getArticles().get(0).getFront().getArticleMeta();

        // Then: Abstract
        assertNotNull(articleMeta.getAbstracts(), "Abstract 리스트가 null이 아니어야 함 / Abstract list should not be null");
        assertEquals(1, articleMeta.getAbstracts().size());

        PmcAbstract abstract1 = articleMeta.getAbstracts().get(0);
        assertNotNull(abstract1.getTitle(), "Abstract title이 null이 아니어야 함 / Abstract title should not be null");
        assertEquals("Abstract", abstract1.getTitle().getValue());

        assertNotNull(abstract1.getSections(), "Abstract section 리스트가 null이 아니어야 함 / Abstract section list should not be null");
        assertEquals(4, abstract1.getSections().size(), "Section 4개 확인 / Should have 4 sections");

        Sec backgroundSec = abstract1.getSections().stream()
                .filter(sec -> sec.getTitle() != null && "Background".equals(sec.getTitle().getValue()))
                .findFirst()
                .orElse(null);
        assertNotNull(backgroundSec, "Background section이 존재해야 함 / Background section should exist");
        assertNotNull(backgroundSec.getParagraphs(), "Paragraph 리스트가 null이 아니어야 함 / Paragraph list should not be null");
        assertTrue(backgroundSec.getParagraphs().get(0).getValue().contains("Biomedical literature mining"));

        // Translated Abstract
        assertNotNull(articleMeta.getTransAbstracts(), "TransAbstract 리스트가 null이 아니어야 함 / TransAbstract list should not be null");
        assertEquals(1, articleMeta.getTransAbstracts().size());

        TransAbstract transAbstract = articleMeta.getTransAbstracts().get(0);
        assertEquals("ko", transAbstract.getXmlLang());

        // Keywords
        assertNotNull(articleMeta.getKwdGroups(), "KwdGroup 리스트가 null이 아니어야 함 / KwdGroup list should not be null");
        assertEquals(2, articleMeta.getKwdGroups().size());

        KwdGroup authorKwds = articleMeta.getKwdGroups().stream()
                .filter(kg -> "author".equals(kg.getKwdGroupType()))
                .findFirst()
                .orElse(null);
        assertNotNull(authorKwds, "Author keywords가 존재해야 함 / Author keywords should exist");
        if (authorKwds.getTitles() != null && !authorKwds.getTitles().isEmpty()) {
            assertEquals("Keywords", authorKwds.getTitles().get(0).getValue());
        }

        assertNotNull(authorKwds.getKeywords(), "Keyword 리스트가 null이 아니어야 함 / Keyword list should not be null");
        assertEquals(5, authorKwds.getKeywords().size(), "Keyword 5개 확인 / Should have 5 keywords");

        Kwd firstKwd = authorKwds.getKeywords().get(0);
        assertEquals("XML parsing", firstKwd.getValue());
    }

    /**
     * 테스트 6: Publication Dates와 History 파싱 / Test 6: Publication Dates and History Parsing
     *
     * KR: 출판 날짜와 이력 정보가 올바르게 파싱되는지 검증
     * EN: Verify publication dates and history information are parsed correctly
     */
    @Test
    void testPublicationDatesAndHistory() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        ArticleMeta articleMeta = result.getArticles().get(0).getFront().getArticleMeta();

        // Then: Publication Dates
        assertNotNull(articleMeta.getPubDates(), "PubDate 리스트가 null이 아니어야 함 / PubDate list should not be null");
        assertEquals(3, articleMeta.getPubDates().size(), "PubDate 3개 확인 / Should have 3 pub dates");

        PmcPubDate ppubDate = articleMeta.getPubDates().stream()
                .filter(pd -> "ppub".equals(pd.getPubType()))
                .findFirst()
                .orElse(null);
        assertNotNull(ppubDate, "Print pub date가 존재해야 함 / Print pub date should exist");
        assertEquals("15", ppubDate.getDay().getValue());
        assertEquals("03", ppubDate.getMonth().getValue());
        assertEquals("2024", ppubDate.getYear().getValue());

        PmcPubDate epubDate = articleMeta.getPubDates().stream()
                .filter(pd -> "epub".equals(pd.getPubType()))
                .findFirst()
                .orElse(null);
        assertNotNull(epubDate, "Electronic pub date가 존재해야 함 / Electronic pub date should exist");

        // History
        assertNotNull(articleMeta.getHistory(), "History가 null이 아니어야 함 / History should not be null");
        assertNotNull(articleMeta.getHistory().getDates(), "History Date 리스트가 null이 아니어야 함 / History Date list should not be null");
        assertEquals(3, articleMeta.getHistory().getDates().size(), "History date 3개 확인 / Should have 3 history dates");

        PmcDate receivedDate = articleMeta.getHistory().getDates().stream()
                .filter(date -> "received".equals(date.getDateType()))
                .findFirst()
                .orElse(null);
        assertNotNull(receivedDate, "Received date가 존재해야 함 / Received date should exist");
        assertEquals("10", receivedDate.getDay().getValue());
        assertEquals("11", receivedDate.getMonth().getValue());
        assertEquals("2023", receivedDate.getYear().getValue());

        PmcDate acceptedDate = articleMeta.getHistory().getDates().stream()
                .filter(date -> "accepted".equals(date.getDateType()))
                .findFirst()
                .orElse(null);
        assertNotNull(acceptedDate, "Accepted date가 존재해야 함 / Accepted date should exist");
    }

    /**
     * 테스트 7: Permissions와 Copyright 파싱 / Test 7: Permissions and Copyright Parsing
     *
     * KR: 권한과 저작권 정보가 올바르게 파싱되는지 검증
     * EN: Verify permissions and copyright information are parsed correctly
     */
    @Test
    void testPermissionsAndCopyright() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        ArticleMeta articleMeta = result.getArticles().get(0).getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getPermissions(), "Permissions가 null이 아니어야 함 / Permissions should not be null");

        Permissions permissions = articleMeta.getPermissions();

        assertNotNull(permissions.getCopyrightStatements(), "CopyrightStatements가 null이 아니어야 함 / CopyrightStatements should not be null");
        assertFalse(permissions.getCopyrightStatements().isEmpty(), "CopyrightStatements가 비어있지 않아야 함 / CopyrightStatements should not be empty");
        assertTrue(permissions.getCopyrightStatements().get(0).getValue().contains("Copyright"));
        assertTrue(permissions.getCopyrightStatements().get(0).getValue().contains("2024"));

        assertNotNull(permissions.getCopyrightYears(), "CopyrightYears가 null이 아니어야 함 / CopyrightYears should not be null");
        assertFalse(permissions.getCopyrightYears().isEmpty(), "CopyrightYears가 비어있지 않아야 함 / CopyrightYears should not be empty");
        assertEquals("2024", permissions.getCopyrightYears().get(0).getValue());

        assertNotNull(permissions.getCopyrightHolders(), "CopyrightHolders가 null이 아니어야 함 / CopyrightHolders should not be null");
        assertFalse(permissions.getCopyrightHolders().isEmpty(), "CopyrightHolders가 비어있지 않아야 함 / CopyrightHolders should not be empty");
        assertEquals("Elsevier Science", permissions.getCopyrightHolders().get(0).getValue());

        assertNotNull(permissions.getLicenses(), "Licenses가 null이 아니어야 함 / Licenses should not be null");
        assertFalse(permissions.getLicenses().isEmpty(), "Licenses가 비어있지 않아야 함 / Licenses should not be empty");
        assertEquals("open-access", permissions.getLicenses().get(0).getLicenseType());
        assertTrue(permissions.getLicenses().get(0).getXlinkHref().contains("creativecommons.org"));
    }

    /**
     * 테스트 8: Body Section 파싱 / Test 8: Body Section Parsing
     *
     * KR: Body의 Section 구조가 올바르게 파싱되는지 검증
     * EN: Verify Body section structure is parsed correctly
     */
    @Test
    void testBodySectionParsing() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        Body body = result.getArticles().get(0).getBody();

        // Then
        assertNotNull(body, "Body가 null이 아니어야 함 / Body should not be null");
        assertNotNull(body.getSections(), "Section 리스트가 null이 아니어야 함 / Section list should not be null");
        assertEquals(4, body.getSections().size(), "Section 4개 확인 / Should have 4 sections");

        // Introduction section
        Sec introSec = body.getSections().stream()
                .filter(sec -> "sec1".equals(sec.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(introSec, "Introduction section이 존재해야 함 / Introduction section should exist");
        assertEquals("1", introSec.getLabel().getValue());
        assertEquals("Introduction", introSec.getTitle().getValue());

        assertNotNull(introSec.getParagraphs(), "Paragraph 리스트가 null이 아니어야 함 / Paragraph list should not be null");
        assertTrue(introSec.getParagraphs().size() > 0, "Paragraph가 존재해야 함 / Should have paragraphs");

        // Nested subsections
        assertNotNull(introSec.getSections(), "Section 리스트가 null이 아니어야 함 / Section list should not be null");
        assertEquals(1, introSec.getSections().size(), "Subsection 1개 확인 / Should have 1 subsection");

        Sec motivationSec = introSec.getSections().get(0);
        assertEquals("sec1-1", motivationSec.getId());
        assertEquals("1.1", motivationSec.getLabel().getValue());
        assertEquals("Motivation", motivationSec.getTitle().getValue());

        // Results section
        Sec resultsSec = body.getSections().stream()
                .filter(sec -> "sec3".equals(sec.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(resultsSec, "Results section이 존재해야 함 / Results section should exist");

        // Note: Sec 모델에 figures와 tableWraps 필드가 없음 (DTD에서 sec 요소에 포함되지 않음)
        // Figure와 Table은 별도의 최상위 요소이거나 Body 또는 Article에 직접 속함
    }

    /**
     * 테스트 9: Back References 파싱 / Test 9: Back References Parsing
     *
     * KR: Back의 참고문헌 리스트가 올바르게 파싱되는지 검증
     * EN: Verify Back reference list is parsed correctly
     */
    @Test
    void testBackReferencesParsing() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        Back back = result.getArticles().get(0).getBack();

        // Then
        assertNotNull(back, "Back이 null이 아니어야 함 / Back should not be null");

        // Acknowledgments
        assertNotNull(back.getAcknowledgments(), "Acknowledgments가 null이 아니어야 함 / Acknowledgments should not be null");

        // Reference List
        assertNotNull(back.getRefLists(), "RefLists가 null이 아니어야 함 / RefLists should not be null");
        RefList refList = back.getRefLists().get(0);
        assertNotNull(refList.getTitle(), "RefList title이 null이 아니어야 함 / RefList title should not be null");
        assertEquals("References", refList.getTitle().getValue());

        assertNotNull(refList.getReferences(), "Reference 리스트가 null이 아니어야 함 / Reference list should not be null");
        assertEquals(3, refList.getReferences().size(), "Reference 3개 확인 / Should have 3 references");

        // First reference (journal article)
        Ref firstRef = refList.getReferences().get(0);
        assertEquals("ref1", firstRef.getId());
        assertEquals("1", firstRef.getLabel().getValue());

        // Second reference (book)
        Ref secondRef = refList.getReferences().get(1);
        assertEquals("ref2", secondRef.getId());
        assertEquals("2", secondRef.getLabel().getValue());

        // Third reference (web)
        Ref thirdRef = refList.getReferences().get(2);
        assertEquals("ref3", thirdRef.getId());

        // Footnote Group
        assertNotNull(back.getFnGroups(), "FnGroups가 null이 아니어야 함 / FnGroups should not be null");
        FnGroup fnGroup = back.getFnGroups().get(0);
        assertNotNull(fnGroup.getFootnotes(), "Footnote 리스트가 null이 아니어야 함 / Footnote list should not be null");
        assertEquals(2, fnGroup.getFootnotes().size(), "Footnote 2개 확인 / Should have 2 footnotes");

        Fn conflictFn = fnGroup.getFootnotes().stream()
                .filter(fn -> "conflict".equals(fn.getFnType()))
                .findFirst()
                .orElse(null);
        assertNotNull(conflictFn, "Conflict of interest footnote가 존재해야 함 / Conflict footnote should exist");
    }

    /**
     * 테스트 10: Counts 파싱 / Test 10: Counts Parsing
     *
     * KR: Article counts 정보가 올바르게 파싱되는지 검증
     * EN: Verify article counts information is parsed correctly
     */
    @Test
    void testCountsParsing() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        ArticleMeta articleMeta = result.getArticles().get(0).getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getCounts(), "Counts가 null이 아니어야 함 / Counts should not be null");

        Counts counts = articleMeta.getCounts();
        assertEquals(5, counts.getFigCount(), "Figure count 확인 / Verify figure count");
        assertEquals(3, counts.getTableCount(), "Table count 확인 / Verify table count");
        assertEquals(2, counts.getEquationCount(), "Equation count 확인 / Verify equation count");
        assertEquals(45, counts.getRefCount(), "Reference count 확인 / Verify reference count");
        assertEquals(26, counts.getPageCount(), "Page count 확인 / Verify page count");
    }

    /**
     * 테스트 11: 대용량 파일 스트리밍 / Test 11: Large File Streaming
     *
     * KR: 스트리밍 모드가 메모리 효율적으로 동작하는지 검증
     * EN: Verify streaming mode works memory-efficiently
     */
    @Test
    void testLargeFileStreaming() throws Exception {
        // Given: Create a large XML file with multiple articles
        Path largeXmlPath = createLargeXmlFile(50); // 50 articles

        // When: Parse using streaming mode
        AtomicInteger articleCount = new AtomicInteger(0);

        parser.parseStream(largeXmlPath, article -> {
            articleCount.incrementAndGet();
            assertNotNull(article.getFront(), "각 article의 Front가 null이 아니어야 함 / Each article's Front should not be null");
        });

        // Then: Verify all articles were processed
        assertEquals(50, articleCount.get(), "50개 article 처리 확인 / Should process 50 articles");

        // Clean up
        Files.deleteIfExists(largeXmlPath);
    }

    /**
     * 테스트 12: GZip 파일 처리 / Test 12: GZip File Handling
     *
     * KR: GZip으로 압축된 파일이 올바르게 처리되는지 검증
     * EN: Verify GZip compressed files are handled correctly
     */
    @Test
    void testGzipFileHandling(@TempDir Path tempDir) throws Exception {
        // Given: Create a gzipped XML file
        Path gzipFile = tempDir.resolve("test-pmc.xml.gz");

        try (FileOutputStream fos = new FileOutputStream(gzipFile.toFile());
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            Files.copy(sampleXmlPath, gzos);
        }

        // When: Parse the gzipped file
        PmcArticleSet result = parser.parse(gzipFile);

        // Then: Should parse successfully
        assertNotNull(result, "GZip 파일 파싱 결과가 null이 아니어야 함 / GZip file parse result should not be null");
        assertEquals(1, result.getArticles().size(), "Article 1개 확인 / Should have 1 article");

        PmcArticle article = result.getArticles().get(0);
        assertEquals("research-article", article.getArticleType());
    }

    /**
     * 테스트 13: Funding Group 파싱 / Test 13: Funding Group Parsing
     *
     * KR: 연구비 지원 정보가 올바르게 파싱되는지 검증
     * EN: Verify funding information is parsed correctly
     */
    @Test
    void testFundingGroupParsing() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        ArticleMeta articleMeta = result.getArticles().get(0).getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getFundingGroups(), "FundingGroup 리스트가 null이 아니어야 함 / FundingGroup list should not be null");
        assertEquals(1, articleMeta.getFundingGroups().size());

        FundingGroup fundingGroup = articleMeta.getFundingGroups().get(0);
        assertNotNull(fundingGroup.getAwardGroups(), "AwardGroup 리스트가 null이 아니어야 함 / AwardGroup list should not be null");
        assertEquals(1, fundingGroup.getAwardGroups().size());
    }

    /**
     * 테스트 14: Author Notes 파싱 / Test 14: Author Notes Parsing
     *
     * KR: 저자 노트 정보가 올바르게 파싱되는지 검증
     * EN: Verify author notes information is parsed correctly
     */
    @Test
    void testAuthorNotesParsing() throws Exception {
        // When
        PmcArticleSet result = parser.parse(sampleXmlPath);
        ArticleMeta articleMeta = result.getArticles().get(0).getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getAuthorNotes(), "AuthorNotes가 null이 아니어야 함 / AuthorNotes should not be null");

        AuthorNotes authorNotes = articleMeta.getAuthorNotes();
        assertNotNull(authorNotes.getCorresps(), "Corresps가 null이 아니어야 함 / Corresps should not be null");
        assertEquals("cor1", authorNotes.getCorresps().get(0).getId());

        assertNotNull(authorNotes.getFootnotes(), "Footnotes가 null이 아니어야 함 / Footnotes should not be null");
        assertEquals(1, authorNotes.getFootnotes().size());

        Fn contributionFn = authorNotes.getFootnotes().get(0);
        assertEquals("con", contributionFn.getFnType());
    }

    // ==================== Helper Methods ====================

    /**
     * 대용량 테스트용 XML 파일 생성 / Create large XML file for testing
     */
    private Path createLargeXmlFile(int articleCount) throws IOException {
        Path tempFile = Files.createTempFile("test-large-pmc-", ".xml");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!DOCTYPE pmc-articleset PUBLIC \"-//NLM//DTD ARTICLE SET 2.0//EN\" \"https://dtd.nlm.nih.gov/ncbi/pmc/articleset/nlm-articleset-2.0.dtd\">\n");
            writer.write("<pmc-articleset>\n");

            for (int i = 1; i <= articleCount; i++) {
                writer.write(String.format(
                    "  <article article-type=\"research-article\" dtd-version=\"1.3\" xml:lang=\"en\">\n" +
                    "    <front>\n" +
                    "      <journal-meta>\n" +
                    "        <journal-title-group>\n" +
                    "          <journal-title>Test Journal</journal-title>\n" +
                    "        </journal-title-group>\n" +
                    "      </journal-meta>\n" +
                    "      <article-meta>\n" +
                    "        <article-id pub-id-type=\"pmid\">%d</article-id>\n" +
                    "        <title-group>\n" +
                    "          <article-title>Test Article %d</article-title>\n" +
                    "        </title-group>\n" +
                    "        <pub-date pub-type=\"epub\">\n" +
                    "          <year>2024</year>\n" +
                    "        </pub-date>\n" +
                    "      </article-meta>\n" +
                    "    </front>\n" +
                    "  </article>\n",
                    i, i
                ));
            }

            writer.write("</pmc-articleset>\n");
        }

        return tempFile;
    }
}
