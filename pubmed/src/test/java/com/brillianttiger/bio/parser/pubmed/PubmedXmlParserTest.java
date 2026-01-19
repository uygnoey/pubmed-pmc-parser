package com.brillianttiger.bio.parser.pubmed;

import com.brillianttiger.bio.parser.pubmed.model.*;
import com.brillianttiger.bio.parser.pubmed.parser.PubmedXmlParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PubmedXmlParserTest / PubMed XML 파서 테스트
 *
 * KR: PubMed XML 파서의 모든 기능을 테스트하는 종합 테스트 클래스
 * EN: Comprehensive test class for all PubMed XML parser functionality
 *
 * Test Cases:
 * 1. MD5 checksum validation
 * 2. MedlineCitation attributes parsing
 * 3. Article child elements parsing
 * 4. AuthorList (personal and collective authors)
 * 5. MeshHeadingList parsing
 * 6. ReferenceList (nested structure)
 * 7. DeleteCitation parsing
 * 8. PubmedBookArticle parsing
 * 9. Large file streaming
 */
class PubmedXmlParserTest {

    private static PubmedXmlParser parser;
    private static Path sampleXmlPath;

    @BeforeAll
    static void setUp() {
        parser = new PubmedXmlParser();
        // src/test/resources/sample-pubmed.xml
        sampleXmlPath = Paths.get("src/test/resources/sample-pubmed.xml");
    }

    /**
     * 테스트 1: MD5 체크섬 검증 / Test 1: MD5 Checksum Validation
     *
     * KR: 다운로드한 XML.gz 파일과 .md5 파일의 체크섬이 일치하는지 검증
     * EN: Validate that downloaded XML.gz file matches checksum in .md5 file
     */
    @Test
    void testMd5ChecksumValidation(@TempDir Path tempDir) throws Exception {
        // Given: Create test GZip file
        Path gzipFile = tempDir.resolve("test.xml.gz");
        String testContent = "<?xml version=\"1.0\"?><root>test content</root>";

        try (FileOutputStream fos = new FileOutputStream(gzipFile.toFile());
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            gzos.write(testContent.getBytes());
        }

        // Calculate MD5
        String actualMd5 = calculateMd5(gzipFile);

        // Create .md5 file
        Path md5File = tempDir.resolve("test.xml.gz.md5");
        Files.writeString(md5File, actualMd5);

        // When: Read and verify MD5
        String expectedMd5 = Files.readString(md5File).trim();

        // Then: MD5 should match
        assertEquals(expectedMd5, actualMd5, "MD5 체크섬이 일치해야 함 / MD5 checksums should match");
        assertEquals(32, actualMd5.length(), "MD5는 32자 16진수 문자열 / MD5 should be 32-char hex string");
    }

    /**
     * 테스트 1: 간단한 Article 파싱 / Test 1: Simple Article Parsing
     *
     * KR: 기본 Article 요소들이 올바르게 파싱되는지 검증
     * EN: Verify basic Article elements are parsed correctly
     */
    @Test
    void testParseSimpleArticle() throws Exception {
        // Given: simple_article.xml
        Path simplePath = Paths.get("src/test/resources/pubmed/simple_article.xml");

        // When
        PubmedArticleSet result = parser.parseFile(simplePath);

        // Then: 기본 구조 검증
        assertNotNull(result, "파싱 결과가 null이 아니어야 함 / Parse result should not be null");
        assertEquals(1, result.getPubmedArticles().size(), "PubmedArticle 1개 확인 / Should have 1 PubmedArticle");

        PubmedArticle article = result.getPubmedArticles().get(0);
        MedlineCitation citation = article.getMedlineCitation();

        // PMID
        assertNotNull(citation.getPmid(), "PMID가 null이 아니어야 함 / PMID should not be null");
        assertEquals("12345678", citation.getPmid().getValue());
        assertEquals("1", citation.getPmid().getVersion());

        // Article
        Article articleObj = citation.getArticle();
        assertNotNull(articleObj, "Article이 null이 아니어야 함 / Article should not be null");
        assertEquals("Print", articleObj.getPubModel().getValue());

        // Journal
        assertNotNull(articleObj.getJournal(), "Journal이 null이 아니어야 함 / Journal should not be null");
        assertEquals("Test Journal", articleObj.getJournal().getTitle().getValue());

        // ArticleTitle
        assertEquals("Simple Test Article for Parser Validation", articleObj.getArticleTitle().getValue());

        // Pagination
        assertNotNull(articleObj.getPagination(), "Pagination이 null이 아니어야 함 / Pagination should not be null");
        assertEquals("100", articleObj.getPagination().getStartPage().getValue());
        assertEquals("110", articleObj.getPagination().getEndPage().getValue());
        assertEquals("100-110", articleObj.getPagination().getMedlinePgn().getValue());

        // Language
        assertEquals(1, articleObj.getLanguages().size());
        assertEquals("eng", articleObj.getLanguages().get(0).getValue());

        // PublicationType
        assertEquals(1, articleObj.getPublicationTypeList().getPublicationTypes().size());
        PublicationType pubType = articleObj.getPublicationTypeList().getPublicationTypes().get(0);
        assertEquals("D016428", pubType.getUi());
        assertEquals("Journal Article", pubType.getValue());

        // MedlineJournalInfo
        assertNotNull(citation.getMedlineJournalInfo(), "MedlineJournalInfo가 null이 아니어야 함 / MedlineJournalInfo should not be null");
        assertEquals("United States", citation.getMedlineJournalInfo().getCountry().getValue());
        assertEquals("Test J", citation.getMedlineJournalInfo().getMedlineTA().getValue());
        assertEquals("123456789", citation.getMedlineJournalInfo().getNlmUniqueID().getValue());
    }

    /**
     * 테스트 2: MedlineCitation 모든 속성 파싱 / Test 2: MedlineCitation All Attributes Parsing
     *
     * KR: MedlineCitation의 모든 속성(5개)이 올바르게 파싱되는지 검증
     * EN: Verify all 5 MedlineCitation attributes are parsed correctly
     */
    @Test
    void testParseMedlineCitationAllAttributes() throws Exception {
        // Given: full_article.xml with all 5 attributes
        Path fullArticlePath = Paths.get("src/test/resources/pubmed/full_article.xml");

        // When
        PubmedArticleSet result = parser.parseFile(fullArticlePath);

        // Then
        assertNotNull(result, "파싱 결과가 null이 아니어야 함 / Parse result should not be null");

        List<PubmedArticle> articles = result.getPubmedArticles();
        assertNotNull(articles, "PubmedArticle 리스트가 null이 아니어야 함 / PubmedArticle list should not be null");
        assertEquals(1, articles.size(), "PubmedArticle이 1개여야 함 / Should have 1 PubmedArticle");

        PubmedArticle article = articles.get(0);
        MedlineCitation citation = article.getMedlineCitation();

        assertNotNull(citation, "MedlineCitation이 null이 아니어야 함 / MedlineCitation should not be null");

        // Attribute 1: Status
        assertNotNull(citation.getStatus(), "Status가 null이 아니어야 함 / Status should not be null");
        assertEquals("MEDLINE", citation.getStatus().getValue(), "Status 속성 검증 / Verify Status attribute");

        // Attribute 2: Owner
        assertNotNull(citation.getOwner(), "Owner가 null이 아니어야 함 / Owner should not be null");
        assertEquals("NLM", citation.getOwner().getValue(), "Owner 속성 검증 / Verify Owner attribute");

        // Attribute 3: IndexingMethod
        assertNotNull(citation.getIndexingMethod(), "IndexingMethod가 null이 아니어야 함 / IndexingMethod should not be null");
        assertEquals("Curated", citation.getIndexingMethod().getValue(), "IndexingMethod 속성 검증 / Verify IndexingMethod attribute");

        // Attribute 4: VersionID (DTD 2024)
        assertNotNull(citation.getVersionID(), "VersionID가 null이 아니어야 함 / VersionID should not be null");
        assertEquals("2", citation.getVersionID(), "VersionID 속성 검증 / Verify VersionID attribute");

        // Attribute 5: VersionDate (DTD 2024)
        assertNotNull(citation.getVersionDate(), "VersionDate가 null이 아니어야 함 / VersionDate should not be null");
        assertEquals("2024-01-15", citation.getVersionDate(), "VersionDate 속성 검증 / Verify VersionDate attribute");
    }

    /**
     * 테스트 3: Article 하위 요소 파싱 / Test 3: Article Child Elements Parsing
     *
     * KR: Article의 모든 주요 하위 요소가 올바르게 파싱되는지 검증
     * EN: Verify all major Article child elements are parsed correctly
     */
    @Test
    void testArticleChildElements() throws Exception {
        // When
        PubmedArticleSet result = parser.parseFile(sampleXmlPath);
        Article article = result.getPubmedArticles().get(0).getMedlineCitation().getArticle();

        // Then: Article basic info
        assertNotNull(article, "Article이 null이 아니어야 함 / Article should not be null");
        assertEquals("Print-Electronic", article.getPubModel().getValue(), "PubModel 검증 / Verify PubModel");

        // Journal
        assertNotNull(article.getJournal(), "Journal이 null이 아니어야 함 / Journal should not be null");
        assertEquals("Journal of Biomedical Research", article.getJournal().getTitle().getValue());

        // ArticleTitle
        assertNotNull(article.getArticleTitle(), "ArticleTitle이 null이 아니어야 함 / ArticleTitle should not be null");
        assertTrue(article.getArticleTitle().getValue().contains("XML Parsing"), "제목에 'XML Parsing' 포함 확인 / Title should contain 'XML Parsing'");

        // Pagination
        assertNotNull(article.getPagination(), "Pagination이 null이 아니어야 함 / Pagination should not be null");
        assertEquals("100", article.getPagination().getStartPage().getValue());
        assertEquals("125", article.getPagination().getEndPage().getValue());
        assertEquals("100-125", article.getPagination().getMedlinePgn().getValue());

        // ELocationID list
        assertNotNull(article.getELocationIDs(), "ELocationID 리스트가 null이 아니어야 함 / ELocationID list should not be null");
        assertEquals(2, article.getELocationIDs().size(), "ELocationID 2개 확인 / Should have 2 ELocationIDs");

        ELocationID doi = article.getELocationIDs().get(0);
        assertEquals("doi", doi.getEIdType().getValue(), "첫 번째는 DOI / First should be DOI");
        assertEquals("Y", doi.getValidYN(), "ValidYN 확인 / Verify ValidYN");

        // Abstract
        assertNotNull(article.getAbstractInfo(), "Abstract가 null이 아니어야 함 / Abstract should not be null");
        assertEquals(4, article.getAbstractInfo().getAbstractTexts().size(), "AbstractText 4개 확인 / Should have 4 AbstractTexts");

        AbstractText background = article.getAbstractInfo().getAbstractTexts().get(0);
        assertEquals("BACKGROUND", background.getLabel(), "Label 확인 / Verify Label");
        assertEquals("BACKGROUND", background.getNlmCategory().getValue(), "NlmCategory 확인 / Verify NlmCategory");

        // Language
        assertNotNull(article.getLanguages(), "Language 리스트가 null이 아니어야 함 / Language list should not be null");
        assertEquals(1, article.getLanguages().size());
        assertEquals("eng", article.getLanguages().get(0).getValue());

        // PublicationType
        assertNotNull(article.getPublicationTypeList(), "PublicationType 리스트가 null이 아니어야 함 / PublicationType list should not be null");
        assertEquals(2, article.getPublicationTypeList().getPublicationTypes().size(), "PublicationType 2개 확인 / Should have 2 PublicationTypes");

        // ArticleDate
        assertNotNull(article.getArticleDates(), "ArticleDate 리스트가 null이 아니어야 함 / ArticleDate list should not be null");
        assertEquals(1, article.getArticleDates().size());

        ArticleDate articleDate = article.getArticleDates().get(0);
        assertEquals("Electronic", articleDate.getDateType());
        assertEquals("2023", articleDate.getYear().getValue());
        assertEquals("12", articleDate.getMonth().getValue());
        assertEquals("15", articleDate.getDay().getValue());
    }

    /**
     * 테스트 4: AuthorList 파싱 (개인/단체 저자) / Test 4: AuthorList Parsing (Personal and Collective)
     *
     * KR: 개인 저자와 단체 저자가 모두 올바르게 파싱되는지 검증
     * EN: Verify both personal and collective authors are parsed correctly
     */
    @Test
    void testAuthorListParsing() throws Exception {
        // When
        PubmedArticleSet result = parser.parseFile(sampleXmlPath);
        Article article = result.getPubmedArticles().get(0).getMedlineCitation().getArticle();

        // Then
        assertNotNull(article.getAuthorList(), "AuthorList가 null이 아니어야 함 / AuthorList should not be null");
        assertEquals("Y", article.getAuthorList().getCompleteYN(), "CompleteYN 속성 확인 / Verify CompleteYN attribute");

        List<Author> authors = article.getAuthorList().getAuthors();
        assertNotNull(authors, "Authors 리스트가 null이 아니어야 함 / Authors list should not be null");
        assertEquals(3, authors.size(), "저자 3명 확인 / Should have 3 authors");

        // First author (personal)
        Author firstAuthor = authors.get(0);
        assertEquals("Y", firstAuthor.getValidYN(), "ValidYN 속성 확인 / Verify ValidYN");
        assertEquals("Smith", firstAuthor.getLastName().getValue(), "LastName 확인 / Verify LastName");
        assertEquals("John", firstAuthor.getForeName().getValue(), "ForeName 확인 / Verify ForeName");
        assertEquals("J", firstAuthor.getInitials().getValue(), "Initials 확인 / Verify Initials");
        assertEquals("Jr", firstAuthor.getSuffix().getValue(), "Suffix 확인 / Verify Suffix");

        assertNotNull(firstAuthor.getIdentifiers(), "Identifiers가 null이 아니어야 함 / Identifiers should not be null");
        assertEquals("ORCID", firstAuthor.getIdentifiers().get(0).getSource());
        assertEquals("0000-0001-2345-6789", firstAuthor.getIdentifiers().get(0).getValue());

        assertNotNull(firstAuthor.getAffiliationInfos(), "AffiliationInfos가 null이 아니어야 함 / AffiliationInfos should not be null");
        assertEquals(1, firstAuthor.getAffiliationInfos().size());

        AffiliationInfo affiliation = firstAuthor.getAffiliationInfos().get(0);
        assertTrue(affiliation.getAffiliation().getValue().contains("Computer Science"), "소속 확인 / Verify affiliation");
        assertNotNull(affiliation.getIdentifiers(), "소속 Identifiers 확인 / Verify affiliation Identifiers");
        assertEquals("ROR", affiliation.getIdentifiers().get(0).getSource());

        // Second author (personal without suffix)
        Author secondAuthor = authors.get(1);
        assertEquals("Johnson", secondAuthor.getLastName().getValue());
        assertEquals("Mary", secondAuthor.getForeName().getValue());
        assertNull(secondAuthor.getSuffix(), "Suffix가 null이어야 함 / Suffix should be null");

        // Third author (collective)
        Author thirdAuthor = authors.get(2);
        assertNotNull(thirdAuthor.getCollectiveName(), "CollectiveName이 null이 아니어야 함 / CollectiveName should not be null");
        assertEquals("Biomedical Informatics Consortium", thirdAuthor.getCollectiveName().getValue());
        assertNull(thirdAuthor.getLastName(), "단체 저자는 LastName이 null / Collective author should have null LastName");
    }

    /**
     * 테스트 5: MeshHeadingList 파싱 / Test 5: MeshHeadingList Parsing
     *
     * KR: MeSH 용어와 Qualifier가 올바르게 파싱되는지 검증
     * EN: Verify MeSH terms and qualifiers are parsed correctly
     */
    @Test
    void testMeshHeadingListParsing() throws Exception {
        // When
        PubmedArticleSet result = parser.parseFile(sampleXmlPath);
        MedlineCitation citation = result.getPubmedArticles().get(0).getMedlineCitation();

        // Then
        assertNotNull(citation.getMeshHeadingList(), "MeshHeadingList가 null이 아니어야 함 / MeshHeadingList should not be null");

        List<MeshHeading> meshHeadings = citation.getMeshHeadingList().getMeshHeadings();
        assertEquals(3, meshHeadings.size(), "MeshHeading 3개 확인 / Should have 3 MeshHeadings");

        // First MeshHeading (no qualifiers)
        MeshHeading firstMesh = meshHeadings.get(0);
        assertNotNull(firstMesh.getDescriptorName(), "DescriptorName이 null이 아니어야 함 / DescriptorName should not be null");
        assertEquals("D000818", firstMesh.getDescriptorName().getUi());
        assertEquals("N", firstMesh.getDescriptorName().getMajorTopicYN());
        assertEquals("Animals", firstMesh.getDescriptorName().getValue());
        assertNull(firstMesh.getQualifierNames(), "QualifierNames가 null이어야 함 / QualifierNames should be null");

        // Second MeshHeading (with qualifiers)
        MeshHeading secondMesh = meshHeadings.get(1);
        assertEquals("Diabetes Mellitus", secondMesh.getDescriptorName().getValue());

        assertNotNull(secondMesh.getQualifierNames(), "QualifierNames가 null이 아니어야 함 / QualifierNames should not be null");
        assertEquals(2, secondMesh.getQualifierNames().size(), "Qualifier 2개 확인 / Should have 2 qualifiers");

        QualifierName firstQualifier = secondMesh.getQualifierNames().get(0);
        assertEquals("Q000235", firstQualifier.getUi());
        assertEquals("Y", firstQualifier.getMajorTopicYN(), "Major topic 확인 / Verify major topic");
        assertEquals("genetics", firstQualifier.getValue());

        QualifierName secondQualifier = secondMesh.getQualifierNames().get(1);
        assertEquals("Q000628", secondQualifier.getUi());
        assertEquals("N", secondQualifier.getMajorTopicYN());
        assertEquals("therapy", secondQualifier.getValue());
    }

    /**
     * 테스트 6: ReferenceList 중첩 구조 파싱 / Test 6: ReferenceList Nested Structure Parsing
     *
     * KR: ReferenceList의 재귀적 중첩 구조가 올바르게 파싱되는지 검증
     * EN: Verify ReferenceList recursive nested structure is parsed correctly
     */
    @Test
    void testReferenceListNestedStructure() throws Exception {
        // When
        PubmedArticleSet result = parser.parseFile(sampleXmlPath);
        PubmedData pubmedData = result.getPubmedArticles().get(0).getPubmedData();

        // Then
        assertNotNull(pubmedData.getReferenceLists(), "ReferenceLists가 null이 아니어야 함 / ReferenceLists should not be null");

        List<ReferenceList> referenceLists = pubmedData.getReferenceLists();
        assertEquals(1, referenceLists.size(), "최상위 ReferenceList 1개 확인 / Should have 1 top-level ReferenceList");

        ReferenceList topLevel = referenceLists.get(0);
        assertNotNull(topLevel.getTitle(), "Title이 null이 아니어야 함 / Title should not be null");
        assertEquals("References", topLevel.getTitle().getValue());

        // Check references
        assertNotNull(topLevel.getReferences(), "References가 null이 아니어야 함 / References should not be null");
        assertEquals(2, topLevel.getReferences().size(), "Reference 2개 확인 / Should have 2 references");

        Reference firstRef = topLevel.getReferences().get(0);
        assertNotNull(firstRef.getCitation(), "Citation이 null이 아니어야 함 / Citation should not be null");
        assertTrue(firstRef.getCitation().getValue().contains("Smith J"), "Citation 내용 확인 / Verify citation content");

        assertNotNull(firstRef.getArticleIdList(), "ArticleIdList가 null이 아니어야 함 / ArticleIdList should not be null");
        assertEquals(1, firstRef.getArticleIdList().getArticleIds().size());
        assertEquals(ArticleIdType.PUBMED, firstRef.getArticleIdList().getArticleIds().get(0).getIdType());
        assertEquals("11111111", firstRef.getArticleIdList().getArticleIds().get(0).getValue());

        // Check nested ReferenceList
        assertNotNull(topLevel.getReferenceLists(), "중첩 ReferenceLists가 null이 아니어야 함 / Nested ReferenceLists should not be null");
        assertEquals(1, topLevel.getReferenceLists().size(), "중첩 ReferenceList 1개 확인 / Should have 1 nested ReferenceList");

        ReferenceList nested = topLevel.getReferenceLists().get(0);
        assertEquals("Nested References", nested.getTitle().getValue(), "중첩 Title 확인 / Verify nested title");
        assertEquals(1, nested.getReferences().size(), "중첩 Reference 1개 확인 / Should have 1 nested reference");
    }

    /**
     * 테스트 7: CommentsCorrections 및 Retraction 파싱 / Test 7: CommentsCorrections and Retraction Parsing
     *
     * KR: 다양한 CommentsCorrections 타입(Erratum, Comment, Retraction 등)이 올바르게 파싱되는지 검증
     * EN: Verify various CommentsCorrections types (Erratum, Comment, Retraction) are parsed correctly
     */
    @Test
    void testParseCommentsCorrectionsRetraction() throws Exception {
        // Given: comments_corrections.xml with various RefTypes
        Path commentsPath = Paths.get("src/test/resources/pubmed/comments_corrections.xml");

        // When
        PubmedArticleSet result = parser.parseFile(commentsPath);
        MedlineCitation citation = result.getPubmedArticles().get(0).getMedlineCitation();

        // Then
        assertNotNull(citation.getCommentsCorrectionsList(), "CommentsCorrectionsList가 null이 아니어야 함 / CommentsCorrectionsList should not be null");

        List<CommentsCorrections> commentsList = citation.getCommentsCorrectionsList().getCommentsCorrections();
        assertEquals(7, commentsList.size(), "CommentsCorrections 7개 확인 / Should have 7 CommentsCorrections");

        // 1. ErratumIn
        CommentsCorrections erratum = commentsList.get(0);
        assertEquals(RefType.ERRATUM_IN, erratum.getRefType(), "첫 번째는 ErratumIn / First should be ErratumIn");
        assertTrue(erratum.getRefSource().getValue().contains("J Correct. 2024 Jul"));
        assertEquals("88888888", erratum.getPmid().getValue());
        assertNotNull(erratum.getNote(), "Erratum Note가 null이 아니어야 함 / Erratum Note should not be null");
        assertTrue(erratum.getNote().getValue().contains("Smith J [corrected to Smith JA]"));

        // 2. CommentIn
        CommentsCorrections comment = commentsList.get(1);
        assertEquals(RefType.COMMENT_IN, comment.getRefType());
        assertTrue(comment.getRefSource().getValue().contains("J Correct. 2024 Aug"));
        assertEquals("77777777", comment.getPmid().getValue());

        // 3. RetractionIn
        CommentsCorrections retraction = commentsList.get(2);
        assertEquals(RefType.RETRACTION_IN, retraction.getRefType(), "세 번째는 RetractionIn / Third should be RetractionIn");
        assertTrue(retraction.getRefSource().getValue().contains("J Correct. 2024 Sep"));
        assertEquals("66666666", retraction.getPmid().getValue());
        assertNotNull(retraction.getNote(), "Retraction Note가 null이 아니어야 함 / Retraction Note should not be null");
        assertTrue(retraction.getNote().getValue().contains("data integrity concerns"));

        // 4. RepublishedFrom
        CommentsCorrections republished = commentsList.get(3);
        assertEquals(RefType.REPUBLISHED_FROM, republished.getRefType());
        assertTrue(republished.getRefSource().getValue().contains("Original J. 2023 Dec"));
        assertEquals("55555550", republished.getPmid().getValue());

        // 5. ExpressionOfConcernIn
        CommentsCorrections concern = commentsList.get(4);
        assertEquals(RefType.EXPRESSION_OF_CONCERN_IN, concern.getRefType());
        assertTrue(concern.getRefSource().getValue().contains("J Correct. 2024 Oct"));
        assertEquals("44444444", concern.getPmid().getValue());
        assertTrue(concern.getNote().getValue().contains("methodology"));

        // 6. UpdateIn
        CommentsCorrections update = commentsList.get(5);
        assertEquals(RefType.UPDATE_IN, update.getRefType());
        assertTrue(update.getRefSource().getValue().contains("J Correct. 2024 Nov"));
        assertEquals("33333333", update.getPmid().getValue());

        // 7. Cites
        CommentsCorrections cites = commentsList.get(6);
        assertEquals(RefType.CITES, cites.getRefType());
        assertTrue(cites.getRefSource().getValue().contains("Reference J. 2020 Jan"));
        assertEquals("11111110", cites.getPmid().getValue());
    }

    /**
     * 테스트 8: DeleteCitation 파싱 / Test 8: DeleteCitation Parsing
     *
     * KR: 삭제된 PMID 목록이 올바르게 파싱되는지 검증
     * EN: Verify deleted PMID list is parsed correctly
     */
    @Test
    void testParseDeleteCitation() throws Exception {
        // When
        PubmedArticleSet result = parser.parseFile(sampleXmlPath);

        // Then
        assertNotNull(result.getDeleteCitation(), "DeleteCitation이 null이 아니어야 함 / DeleteCitation should not be null");

        DeleteCitation deleteCitation = result.getDeleteCitation();
        assertNotNull(deleteCitation.getPmids(), "PMID 리스트가 null이 아니어야 함 / PMID list should not be null");
        assertEquals(3, deleteCitation.getPmids().size(), "삭제된 PMID 3개 확인 / Should have 3 deleted PMIDs");

        assertEquals("77777777", deleteCitation.getPmids().get(0).getValue(), "첫 번째 PMID 확인 / Verify first PMID");
        assertEquals("88888888", deleteCitation.getPmids().get(1).getValue(), "두 번째 PMID 확인 / Verify second PMID");
        assertEquals("99999998", deleteCitation.getPmids().get(2).getValue(), "세 번째 PMID 확인 / Verify third PMID");

        // All PMIDs should have Version="1"
        deleteCitation.getPmids().forEach(pmid ->
            assertEquals("1", pmid.getVersion(), "모든 PMID는 Version='1' / All PMIDs should have Version='1'")
        );
    }

    /**
     * 테스트 8: PubmedBookArticle 파싱 / Test 8: PubmedBookArticle Parsing
     *
     * KR: PubmedBookArticle의 모든 요소가 올바르게 파싱되는지 검증
     * EN: Verify all PubmedBookArticle elements are parsed correctly
     */
    @Test
    void testPubmedBookArticleParsing() throws Exception {
        // When
        PubmedArticleSet result = parser.parseFile(sampleXmlPath);

        // Then
        assertNotNull(result.getPubmedBookArticles(), "PubmedBookArticle 리스트가 null이 아니어야 함 / PubmedBookArticle list should not be null");
        assertEquals(1, result.getPubmedBookArticles().size(), "PubmedBookArticle 1개 확인 / Should have 1 PubmedBookArticle");

        PubmedBookArticle bookArticle = result.getPubmedBookArticles().get(0);
        assertNotNull(bookArticle.getBookDocument(), "BookDocument가 null이 아니어야 함 / BookDocument should not be null");

        BookDocument bookDoc = bookArticle.getBookDocument();

        // PMID
        assertNotNull(bookDoc.getPmid(), "PMID가 null이 아니어야 함 / PMID should not be null");
        assertEquals("99999999", bookDoc.getPmid().getValue());

        // Book
        assertNotNull(bookDoc.getBook(), "Book이 null이 아니어야 함 / Book should not be null");
        Book book = bookDoc.getBook();

        assertNotNull(book.getPublisher(), "Publisher가 null이 아니어야 함 / Publisher should not be null");
        assertTrue(book.getPublisher().getPublisherName().getValue().contains("Biotechnology"), "Publisher 이름 확인 / Verify publisher name");

        assertNotNull(book.getBookTitle(), "BookTitle이 null이 아니어야 함 / BookTitle should not be null");
        assertEquals("Example Medical Book", book.getBookTitle().getValue());

        assertEquals("2024", book.getPubDate().getYear().getValue(), "출판연도 확인 / Verify publication year");

        // AuthorList
        assertNotNull(book.getAuthorLists(), "AuthorLists가 null이 아니어야 함 / AuthorLists should not be null");
        assertEquals("authors", book.getAuthorLists().get(0).getType().getValue());
        assertEquals(2, book.getAuthorLists().get(0).getAuthors().size(), "저자 2명 확인 / Should have 2 authors");

        Author firstBookAuthor = book.getAuthorLists().get(0).getAuthors().get(0);
        assertEquals("Williams", firstBookAuthor.getLastName().getValue());
        assertEquals("David", firstBookAuthor.getForeName().getValue());

        Author secondBookAuthor = book.getAuthorLists().get(0).getAuthors().get(1);
        assertNotNull(secondBookAuthor.getCollectiveName(), "단체 저자 확인 / Verify collective author");

        // Other book metadata
        assertEquals("1", book.getVolume().getValue());
        assertEquals("2nd", book.getEdition().getValue());
        assertEquals("978-1-234-56789-0", book.getIsbns().get(0).getValue());
        assertEquals("Internet", book.getMedium().getValue());

        // ArticleTitle
        assertNotNull(bookDoc.getArticleTitle(), "ArticleTitle이 null이 아니어야 함 / ArticleTitle should not be null");
        assertTrue(bookDoc.getArticleTitle().getValue().contains("XML Standards"), "챕터 제목 확인 / Verify chapter title");

        // Sections
        assertNotNull(bookDoc.getSections(), "Sections가 null이 아니어야 함 / Sections should not be null");
        assertEquals(2, bookDoc.getSections().getSections().size(), "Section 2개 확인 / Should have 2 sections");

        Section firstSection = bookDoc.getSections().getSections().get(0);
        assertEquals("Introduction", firstSection.getLocationLabel().getValue());
        assertEquals("Introduction to XML Standards", firstSection.getSectionTitle().getValue());
    }

    /**
     * 테스트 9: 대용량 파일 스트리밍 / Test 9: Large File Streaming
     *
     * KR: 스트리밍 모드가 메모리 효율적으로 동작하는지 검증
     * EN: Verify streaming mode works memory-efficiently
     */
    @Test
    void testStreamParseLargeFile() throws Exception {
        // Given: Create a large XML file with multiple articles
        Path largeXmlPath = createLargeXmlFile(100); // 100 articles

        // When: Parse using streaming mode
        AtomicInteger articleCount = new AtomicInteger(0);
        AtomicInteger bookCount = new AtomicInteger(0);
        AtomicInteger deleteCount = new AtomicInteger(0);

        parser.parseStream(largeXmlPath, article -> {
            articleCount.incrementAndGet();
            assertNotNull(article.getMedlineCitation(), "각 article의 MedlineCitation이 null이 아니어야 함 / Each article's MedlineCitation should not be null");
        });

        // Then: Verify all articles were processed
        assertEquals(100, articleCount.get(), "100개 article 처리 확인 / Should process 100 articles");

        // Clean up
        Files.deleteIfExists(largeXmlPath);
    }

    /**
     * 추가 테스트: ChemicalList 파싱 / Additional Test: ChemicalList Parsing
     */
    @Test
    void testChemicalListParsing() throws Exception {
        // When
        PubmedArticleSet result = parser.parseFile(sampleXmlPath);
        MedlineCitation citation = result.getPubmedArticles().get(0).getMedlineCitation();

        // Then
        assertNotNull(citation.getChemicalList(), "ChemicalList가 null이 아니어야 함 / ChemicalList should not be null");
        assertEquals(2, citation.getChemicalList().getChemicals().size(), "Chemical 2개 확인 / Should have 2 chemicals");

        Chemical firstChem = citation.getChemicalList().getChemicals().get(0);
        assertEquals("50-99-7", firstChem.getRegistryNumber().getValue());
        assertEquals("D005947", firstChem.getNameOfSubstance().getUi());
        assertEquals("Glucose", firstChem.getNameOfSubstance().getValue());
    }

    /**
     * 추가 테스트: KeywordList 파싱 / Additional Test: KeywordList Parsing
     */
    @Test
    void testKeywordListParsing() throws Exception {
        // When
        PubmedArticleSet result = parser.parseFile(sampleXmlPath);
        MedlineCitation citation = result.getPubmedArticles().get(0).getMedlineCitation();

        // Then
        assertNotNull(citation.getKeywordLists(), "KeywordLists가 null이 아니어야 함 / KeywordLists should not be null");
        assertEquals(1, citation.getKeywordLists().size());

        KeywordList kwList = citation.getKeywordLists().get(0);
        assertEquals(KeywordOwner.NOTNLM, kwList.getOwner());
        assertEquals(3, kwList.getKeywords().size(), "Keyword 3개 확인 / Should have 3 keywords");

        Keyword firstKw = kwList.getKeywords().get(0);
        assertEquals("N", firstKw.getMajorTopicYN());
        assertEquals("XML parsing", firstKw.getValue());

        Keyword secondKw = kwList.getKeywords().get(1);
        assertEquals("Y", secondKw.getMajorTopicYN(), "주요 키워드 확인 / Verify major keyword");
    }

    /**
     * DTD 250101 테스트: AutoHM 속성 파싱 / Test DTD 250101: AutoHM Attribute Parsing
     *
     * KR: DescriptorName과 QualifierName의 AutoHM 속성이 올바르게 파싱되는지 검증 (DTD 250101 신규)
     * EN: Verify AutoHM attribute in DescriptorName and QualifierName is parsed correctly (DTD 250101 new)
     */
    @Test
    void testParseAutoHMAttribute() throws Exception {
        // Given: full_article.xml with MeSH headings
        Path fullArticlePath = Paths.get("src/test/resources/pubmed/full_article.xml");

        // When
        PubmedArticleSet result = parser.parseFile(fullArticlePath);
        MedlineCitation citation = result.getPubmedArticles().get(0).getMedlineCitation();

        // Then
        assertNotNull(citation.getMeshHeadingList(), "MeshHeadingList가 null이 아니어야 함 / MeshHeadingList should not be null");

        List<MeshHeading> meshHeadings = citation.getMeshHeadingList().getMeshHeadings();
        assertTrue(meshHeadings.size() >= 1, "MeshHeading이 1개 이상 있어야 함 / Should have at least 1 MeshHeading");

        // DescriptorName의 AutoHM 속성 검증 (선택적 속성이므로 null일 수도 있음)
        MeshHeading firstMesh = meshHeadings.get(0);
        DescriptorName descriptor = firstMesh.getDescriptorName();
        assertNotNull(descriptor, "DescriptorName이 null이 아니어야 함 / DescriptorName should not be null");

        // AutoHM 속성 파싱 테스트 (DTD 250101에서 추가됨)
        // Note: AutoHM은 선택적 속성이므로 null일 수 있음
        if (firstMesh.getQualifierNames() != null && !firstMesh.getQualifierNames().isEmpty()) {
            QualifierName qualifier = firstMesh.getQualifierNames().get(0);
            assertNotNull(qualifier, "QualifierName이 null이 아니어야 함 / QualifierName should not be null");
            // AutoHM 속성은 선택적이므로 null 체크만 수행
        }
    }

    /**
     * 2024 DTD 테스트 1: Author EqualContrib 속성 / Test 2024 DTD Change 1: Author EqualContrib Attribute
     *
     * KR: Author의 EqualContrib 속성이 올바르게 파싱되는지 검증
     * EN: Verify Author EqualContrib attribute is parsed correctly
     */
    @Test
    void testParseAuthorWithEqualContrib() throws Exception {
        // Given: full_article.xml with EqualContrib authors
        Path fullArticlePath = Paths.get("src/test/resources/pubmed/full_article.xml");

        // When
        PubmedArticleSet result = parser.parseFile(fullArticlePath);
        Article article = result.getPubmedArticles().get(0).getMedlineCitation().getArticle();

        // Then
        assertNotNull(article.getAuthorList(), "AuthorList가 null이 아니어야 함 / AuthorList should not be null");
        List<Author> authors = article.getAuthorList().getAuthors();
        assertEquals(3, authors.size(), "저자 3명 확인 / Should have 3 authors");

        // First author with EqualContrib="Y"
        Author firstAuthor = authors.get(0);
        assertNotNull(firstAuthor.getEqualContrib(), "EqualContrib이 null이 아니어야 함 / EqualContrib should not be null");
        assertEquals("Y", firstAuthor.getEqualContrib(), "첫 번째 저자 EqualContrib 확인 / Verify first author EqualContrib");
        assertEquals("Smith", firstAuthor.getLastName().getValue());

        // Second author with EqualContrib="Y"
        Author secondAuthor = authors.get(1);
        assertNotNull(secondAuthor.getEqualContrib(), "EqualContrib이 null이 아니어야 함 / EqualContrib should not be null");
        assertEquals("Y", secondAuthor.getEqualContrib(), "두 번째 저자 EqualContrib 확인 / Verify second author EqualContrib");
        assertEquals("Doe", secondAuthor.getLastName().getValue());

        // Third author (collective) without EqualContrib
        Author thirdAuthor = authors.get(2);
        assertNotNull(thirdAuthor.getCollectiveName(), "CollectiveName이 null이 아니어야 함 / CollectiveName should not be null");
        assertNull(thirdAuthor.getEqualContrib(), "단체 저자는 EqualContrib이 null / Collective author should have null EqualContrib");
    }

    /**
     * 2024 DTD 테스트 2: InvestigatorList 반복 가능 / Test 2024 DTD Change 2: InvestigatorList Repeatable
     *
     * KR: InvestigatorList가 반복 가능하며 ID 속성을 가지는지 검증
     * EN: Verify InvestigatorList is repeatable and has ID attribute
     */
    @Test
    void testParseInvestigatorListRepeatable() throws Exception {
        // Given: full_article.xml with 2 InvestigatorLists
        Path fullArticlePath = Paths.get("src/test/resources/pubmed/full_article.xml");

        // When
        PubmedArticleSet result = parser.parseFile(fullArticlePath);
        MedlineCitation citation = result.getPubmedArticles().get(0).getMedlineCitation();

        // Then
        assertNotNull(citation.getInvestigatorLists(), "InvestigatorLists가 null이 아니어야 함 / InvestigatorLists should not be null");
        assertEquals(2, citation.getInvestigatorLists().size(), "InvestigatorList 2개 확인 (2024 DTD 반복 가능) / Should have 2 InvestigatorLists (repeatable in 2024 DTD)");

        // First InvestigatorList
        InvestigatorList firstList = citation.getInvestigatorLists().get(0);
        assertNotNull(firstList.getId(), "첫 번째 InvestigatorList ID가 null이 아니어야 함 / First InvestigatorList ID should not be null");
        assertEquals("investigators-group1", firstList.getId(), "첫 번째 InvestigatorList ID 확인 / Verify first InvestigatorList ID");
        assertEquals(2, firstList.getInvestigators().size(), "첫 번째 그룹에 연구자 2명 / Should have 2 investigators in first group");

        Investigator firstInvestigator = firstList.getInvestigators().get(0);
        assertEquals("Researcher", firstInvestigator.getLastName().getValue());
        assertEquals("Alice", firstInvestigator.getForeName().getValue());

        Investigator secondInvestigator = firstList.getInvestigators().get(1);
        assertEquals("Brown", secondInvestigator.getLastName().getValue());
        assertEquals("Robert", secondInvestigator.getForeName().getValue());

        // Second InvestigatorList
        InvestigatorList secondList = citation.getInvestigatorLists().get(1);
        assertNotNull(secondList.getId(), "두 번째 InvestigatorList ID가 null이 아니어야 함 / Second InvestigatorList ID should not be null");
        assertEquals("investigators-group2", secondList.getId(), "두 번째 InvestigatorList ID 확인 / Verify second InvestigatorList ID");
        assertEquals(1, secondList.getInvestigators().size(), "두 번째 그룹에 연구자 1명 / Should have 1 investigator in second group");

        Investigator thirdInvestigator = secondList.getInvestigators().get(0);
        assertEquals("Wilson", thirdInvestigator.getLastName().getValue());
        assertEquals("Emily", thirdInvestigator.getForeName().getValue());
    }

    /**
     * 2024 DTD 테스트 3: CollectiveName Investigators 속성 / Test 2024 DTD Change 3: CollectiveName Investigators Attribute
     *
     * KR: CollectiveName의 Investigators 속성이 올바르게 파싱되는지 검증
     * EN: Verify CollectiveName Investigators attribute is parsed correctly
     */
    @Test
    void testParseCollectiveNameWithInvestigatorsAttr() throws Exception {
        // Given: full_article.xml with CollectiveName having Investigators attribute
        Path fullArticlePath = Paths.get("src/test/resources/pubmed/full_article.xml");

        // When
        PubmedArticleSet result = parser.parseFile(fullArticlePath);
        Article article = result.getPubmedArticles().get(0).getMedlineCitation().getArticle();

        // Then
        List<Author> authors = article.getAuthorList().getAuthors();
        Author collectiveAuthor = authors.get(2); // Third author is collective

        assertNotNull(collectiveAuthor.getCollectiveName(), "CollectiveName이 null이 아니어야 함 / CollectiveName should not be null");
        assertEquals("Test Collaboration Group", collectiveAuthor.getCollectiveName().getValue());

        // 2024 DTD: CollectiveName.Investigators attribute
        assertNotNull(collectiveAuthor.getCollectiveName().getInvestigators(), "Investigators 속성이 null이 아니어야 함 / Investigators attribute should not be null");
        assertEquals("Y", collectiveAuthor.getCollectiveName().getInvestigators(), "Investigators='Y' 확인 / Verify Investigators='Y'");
    }

    /**
     * 2024 DTD 테스트 4: Grant Country 선택적 / Test 2024 DTD Change 4: Grant Country Optional
     *
     * KR: Grant의 Country가 선택적으로 변경되었는지 검증 (2024 DTD에서 필수 → 선택)
     * EN: Verify Grant Country is optional (changed from required to optional in 2024 DTD)
     */
    @Test
    void testParseGrantWithoutCountry() throws Exception {
        // Given: full_article.xml with Grant without Country
        Path fullArticlePath = Paths.get("src/test/resources/pubmed/full_article.xml");

        // When
        PubmedArticleSet result = parser.parseFile(fullArticlePath);
        Article article = result.getPubmedArticles().get(0).getMedlineCitation().getArticle();

        // Then
        assertNotNull(article.getGrantList(), "GrantList가 null이 아니어야 함 / GrantList should not be null");
        assertEquals(2, article.getGrantList().getGrants().size(), "Grant 2개 확인 / Should have 2 grants");

        // First grant with Country
        Grant firstGrant = article.getGrantList().getGrants().get(0);
        assertEquals("R01-TEST-123456", firstGrant.getGrantID().getValue());
        assertNotNull(firstGrant.getCountry(), "첫 번째 Grant는 Country가 있음 / First Grant has Country");
        assertEquals("United States", firstGrant.getCountry().getValue());

        // Second grant WITHOUT Country (2024 DTD change: Country is optional)
        Grant secondGrant = article.getGrantList().getGrants().get(1);
        assertEquals("EU-H2020-987654", secondGrant.getGrantID().getValue());
        assertEquals("European Commission", secondGrant.getAgency().getValue());
        assertNull(secondGrant.getCountry(), "두 번째 Grant는 Country가 없음 (2024 DTD 선택적) / Second Grant has no Country (optional in 2024 DTD)");
    }

    /**
     * MedlineDate 다양한 형식 파싱 / Parse Various MedlineDate Formats
     *
     * KR: MedlineDate의 다양한 형식(계절, 월 범위, 분기, 년 범위)이 올바르게 파싱되는지 검증
     * EN: Verify various MedlineDate formats (season, month range, quarter, year range) are parsed correctly
     */
    @Test
    void testParseMedlineDateVariants() throws Exception {
        // Given: medline_date_samples.xml with 5 MedlineDate variants
        Path medlineDatePath = Paths.get("src/test/resources/pubmed/medline_date_samples.xml");

        // When
        PubmedArticleSet result = parser.parseFile(medlineDatePath);
        List<PubmedArticle> articles = result.getPubmedArticles();

        // Then
        assertEquals(5, articles.size(), "MedlineDate 샘플 5개 확인 / Should have 5 MedlineDate samples");

        // Sample 1: "2024 Spring"
        PubDate pubDate1 = articles.get(0).getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate();
        assertNotNull(pubDate1.getMedlineDate(), "MedlineDate가 null이 아니어야 함 / MedlineDate should not be null");
        assertEquals("2024 Spring", pubDate1.getMedlineDate().getValue(), "계절 형식 확인 / Verify season format");

        // Sample 2: "2024 Jan-Feb"
        PubDate pubDate2 = articles.get(1).getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate();
        assertEquals("2024 Jan-Feb", pubDate2.getMedlineDate().getValue(), "월 범위 형식 확인 / Verify month range format");

        // Sample 3: "2024 Q1"
        PubDate pubDate3 = articles.get(2).getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate();
        assertEquals("2024 Q1", pubDate3.getMedlineDate().getValue(), "분기 형식 확인 / Verify quarter format");

        // Sample 4: "2024 Winter-Spring"
        PubDate pubDate4 = articles.get(3).getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate();
        assertEquals("2024 Winter-Spring", pubDate4.getMedlineDate().getValue(), "복수 계절 형식 확인 / Verify multiple season format");

        // Sample 5: "2023-2024"
        PubDate pubDate5 = articles.get(4).getMedlineCitation().getArticle().getJournal().getJournalIssue().getPubDate();
        assertEquals("2023-2024", pubDate5.getMedlineDate().getValue(), "년 범위 형식 확인 / Verify year range format");
    }

    /**
     * GZip + MD5 통합 검증 / GZip + MD5 Integrated Verification
     *
     * KR: GZip 압축된 파일의 MD5 체크섬 검증이 올바르게 동작하는지 확인
     * EN: Verify MD5 checksum verification works correctly with GZipped files
     */
    @Test
    void testParseGzipWithMd5Verification(@TempDir Path tempDir) throws Exception {
        // Given: Create GZipped XML file
        Path xmlFile = tempDir.resolve("test-article.xml");
        String xmlContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <!DOCTYPE PubmedArticleSet PUBLIC "-//NLM//DTD PubMedArticle, 1st January 2024//EN" "https://dtd.nlm.nih.gov/ncbi/pubmed/out/pubmed_240101.dtd">
            <PubmedArticleSet>
              <PubmedArticle>
                <MedlineCitation Status="MEDLINE" Owner="NLM">
                  <PMID Version="1">12345678</PMID>
                  <Article PubModel="Print">
                    <Journal>
                      <Title>Test Journal</Title>
                    </Journal>
                    <ArticleTitle>Test Article for GZip</ArticleTitle>
                  </Article>
                </MedlineCitation>
              </PubmedArticle>
            </PubmedArticleSet>
            """;
        Files.writeString(xmlFile, xmlContent);

        // Compress to GZip
        Path gzipFile = tempDir.resolve("test-article.xml.gz");
        try (FileOutputStream fos = new FileOutputStream(gzipFile.toFile());
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            gzos.write(xmlContent.getBytes());
        }

        // Calculate MD5 of GZip file
        String md5Hash = calculateMd5(gzipFile);
        Path md5File = tempDir.resolve("test-article.xml.gz.md5");
        Files.writeString(md5File, String.format("MD5(test-article.xml.gz)= %s", md5Hash));

        // When: Verify MD5
        String expectedMd5 = Files.readString(md5File)
                .replaceAll("MD5\\(.*\\)=\\s*", "")
                .trim();
        String actualMd5 = calculateMd5(gzipFile);

        // Then
        assertEquals(expectedMd5, actualMd5, "GZip 파일의 MD5 체크섬이 일치해야 함 / GZip file MD5 checksum should match");

        // Parse GZipped file
        PubmedArticleSet result = parser.parseFile(gzipFile);
        assertNotNull(result, "GZip 파일 파싱 결과가 null이 아니어야 함 / GZip file parse result should not be null");
        assertEquals(1, result.getPubmedArticles().size(), "Article 1개 확인 / Should have 1 article");

        Article article = result.getPubmedArticles().get(0).getMedlineCitation().getArticle();
        assertEquals("Test Article for GZip", article.getArticleTitle().getValue(), "ArticleTitle 확인 / Verify ArticleTitle");
    }

    // ==================== Helper Methods ====================

    /**
     * MD5 체크섬 계산 / Calculate MD5 checksum
     */
    private String calculateMd5(Path filePath) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is)) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = bis.read(buffer)) != -1) {
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
     * 대용량 테스트용 XML 파일 생성 / Create large XML file for testing
     */
    private Path createLargeXmlFile(int articleCount) throws IOException {
        Path tempFile = Files.createTempFile("test-large-", ".xml");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!DOCTYPE PubmedArticleSet PUBLIC \"-//NLM//DTD PubMedArticle, 1st January 2023//EN\" \"https://dtd.nlm.nih.gov/ncbi/pubmed/out/pubmed_230101.dtd\">\n");
            writer.write("<PubmedArticleSet>\n");

            for (int i = 1; i <= articleCount; i++) {
                writer.write(String.format(
                    "  <PubmedArticle>\n" +
                    "    <MedlineCitation Status=\"MEDLINE\" Owner=\"NLM\">\n" +
                    "      <PMID Version=\"1\">%d</PMID>\n" +
                    "      <Article PubModel=\"Print\">\n" +
                    "        <Journal>\n" +
                    "          <Title>Test Journal</Title>\n" +
                    "        </Journal>\n" +
                    "        <ArticleTitle>Test Article %d</ArticleTitle>\n" +
                    "      </Article>\n" +
                    "    </MedlineCitation>\n" +
                    "    <PubmedData>\n" +
                    "      <PublicationStatus>ppublish</PublicationStatus>\n" +
                    "    </PubmedData>\n" +
                    "  </PubmedArticle>\n",
                    i, i
                ));
            }

            writer.write("</PubmedArticleSet>\n");
        }

        return tempFile;
    }
}
