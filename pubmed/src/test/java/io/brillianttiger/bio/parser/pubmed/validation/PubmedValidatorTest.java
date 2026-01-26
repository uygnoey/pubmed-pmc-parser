package io.brillianttiger.bio.parser.pubmed.validation;

import io.brillianttiger.bio.parser.common.validation.ValidationError;
import io.brillianttiger.bio.parser.pubmed.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PubmedValidator 테스트 / PubmedValidator tests
 */
class PubmedValidatorTest {

    // ========== 기본 검증 테스트 / Basic validation tests ==========

    @Test
    @DisplayName("Test 1: null PubmedArticle should return error")
    void test01_nullArticle() {
        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(null);

        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("PubmedArticle") && e.message().contains("null")
        ));
    }

    @Test
    @DisplayName("Test 2: PubmedArticle without MedlineCitation should return error")
    void test02_noMedlineCitation() {
        PubmedArticle article = PubmedArticle.builder().build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.isEmpty());
        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("MedlineCitation") && e.message().contains("required")
        ));
    }

    @Test
    @DisplayName("Test 3: MedlineCitation without Status should return error")
    void test03_noStatus() {
        MedlineCitation citation = MedlineCitation.builder()
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("MedlineCitation.Status")
        ));
    }

    @Test
    @DisplayName("Test 4: MedlineCitation without PMID should return error")
    void test04_noPmid() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("MedlineCitation.PMID")
        ));
    }

    @Test
    @DisplayName("Test 5: Invalid PMID format should return error")
    void test05_invalidPmidFormat() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("123456789").build()) // 9자리 (too long)
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("PMID") && e.message().contains("Invalid PMID format")
        ));
    }

    @Test
    @DisplayName("Test 6: MedlineCitation without Article should return error")
    void test06_noArticle() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("MedlineCitation.Article")
        ));
    }

    @Test
    @DisplayName("Test 7: MedlineCitation without MedlineJournalInfo should return error")
    void test07_noMedlineJournalInfo() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("MedlineCitation.MedlineJournalInfo")
        ));
    }

    @Test
    @DisplayName("Test 8: Article without PubModel should return error")
    void test08_noPubModel() {
        Article art = Article.builder()
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test Title").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("Article.PubModel")
        ));
    }

    @Test
    @DisplayName("Test 9: Article without Journal should return error")
    void test09_noJournal() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .articleTitle(ArticleTitle.builder().value("Test Title").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("Article.Journal")
        ));
    }

    @Test
    @DisplayName("Test 10: Article without ArticleTitle should return error")
    void test10_noArticleTitle() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("Article.ArticleTitle")
        ));
    }

    @Test
    @DisplayName("Test 11: Article without PublicationTypeList should return error")
    void test11_noPublicationTypeList() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test Title").build())
                .languages(List.of(Language.builder().value("eng").build()))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("Article.PublicationTypeList")
        ));
    }

    @Test
    @DisplayName("Test 12: Empty PublicationTypeList should return error")
    void test12_emptyPublicationTypeList() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test Title").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of())
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("Article.PublicationTypeList") &&
            e.message().contains("at least one")
        ));
    }

    @Test
    @DisplayName("Test 13: Article without Language should return error")
    void test13_noLanguage() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test Title").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("Article.Language")
        ));
    }

    @Test
    @DisplayName("Test 14: Valid minimal PubmedArticle should pass")
    void test14_validMinimalArticle() {
        PubmedArticle article = createValidMinimalArticle();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.isEmpty(), "Valid minimal article should have no errors");
    }

    // ========== 날짜 검증 테스트 / Date validation tests ==========

    @Test
    @DisplayName("Test 15: Invalid year in DateCompleted should return error")
    void test15_invalidYear() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("1800").build())  // Too old (before 1809)
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted") && e.message().contains("Year")
        ));
    }

    @Test
    @DisplayName("Test 16: Invalid month should return error")
    void test16_invalidMonth() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("13").build())  // Invalid month
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted") && e.message().contains("Month")
        ));
    }

    @Test
    @DisplayName("Test 17: Invalid day should return error")
    void test17_invalidDay() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("32").build())  // Invalid day
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted") && e.message().contains("Day")
        ));
    }

    // ========== MeSH 검증 테스트 / MeSH validation tests ==========

    @Test
    @DisplayName("Test 18: Invalid MeSH UI format should return error")
    void test18_invalidMeshUi() {
        DescriptorName descriptor = DescriptorName.builder()
                .ui("E000001")  // Invalid: must start with D or C
                .majorTopicYN("Y")
                .value("Test Descriptor")
                .build();

        MeshHeading heading = MeshHeading.builder()
                .descriptorName(descriptor)
                .build();

        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(List.of(heading))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("MeshHeading") && e.field().contains("DescriptorName.UI")
        ));
    }

    @Test
    @DisplayName("Test 19: Valid MeSH UI should pass")
    void test19_validMeshUi() {
        DescriptorName descriptor = DescriptorName.builder()
                .ui("D000001")  // Valid
                .majorTopicYN("Y")
                .value("Test Descriptor")
                .build();

        MeshHeading heading = MeshHeading.builder()
                .descriptorName(descriptor)
                .build();

        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(List.of(heading))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e ->
            e.field().contains("MeshHeading")
        ));
    }

    @Test
    @DisplayName("Test 20: Invalid QualifierName UI should return error")
    void test20_invalidQualifierUi() {
        QualifierName qualifier = QualifierName.builder()
                .ui("Q123456")  // Invalid format
                .majorTopicYN("N")
                .value("Test Qualifier")
                .build();

        DescriptorName descriptor = DescriptorName.builder()
                .ui("D000001")
                .majorTopicYN("Y")
                .value("Test Descriptor")
                .build();

        MeshHeading heading = MeshHeading.builder()
                .descriptorName(descriptor)
                .qualifierNames(List.of(qualifier))
                .build();

        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(List.of(heading))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("QualifierName") && e.field().contains("UI")
        ));
    }

    // ========== ORCID 검증 테스트 / ORCID validation tests ==========

    @Test
    @DisplayName("Test 21: Invalid ORCID format should return error")
    void test21_invalidOrcid() {
        Identifier orcidId = Identifier.builder()
                .source("ORCID")
                .value("0000-0002-1825-00971")  // Too long
                .build();

        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .foreName(ForeName.builder().value("John").build())
                .initials(Initials.builder().value("J").build())
                .identifiers(List.of(orcidId))
                .build();

        AuthorList authorList = AuthorList.builder()
                .authors(List.of(author))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test Title").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(authorList)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("Author") && e.field().contains("Identifier")
        ));
    }

    @Test
    @DisplayName("Test 22: Valid ORCID should pass")
    void test22_validOrcid() {
        Identifier orcidId = Identifier.builder()
                .source("ORCID")
                .value("0000-0002-1825-0097")  // Valid ORCID
                .build();

        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .foreName(ForeName.builder().value("John").build())
                .initials(Initials.builder().value("J").build())
                .identifiers(List.of(orcidId))
                .build();

        AuthorList authorList = AuthorList.builder()
                .authors(List.of(author))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test Title").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(authorList)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e ->
            e.field().contains("Author") && e.field().contains("Identifier")
        ));
    }

    // ========== PubmedData 검증 테스트 / PubmedData validation tests ==========

    @Test
    @DisplayName("Test 23: Invalid date in PubmedData History should return error")
    void test23_invalidHistoryDate() {
        PubMedPubDate pubDate = PubMedPubDate.builder()
                .year(Year.builder().value("2100").build())  // Future year
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .pubStatus(PubStatus.RECEIVED)
                .build();

        History history = History.builder()
                .pubMedPubDates(List.of(pubDate))
                .build();

        PubmedData pubmedData = PubmedData.builder()
                .history(history)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .pubmedData(pubmedData)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("PubmedData.History")
        ));
    }

    @Test
    @DisplayName("Test 24: Valid PubmedData should pass")
    void test24_validPubmedData() {
        PubMedPubDate pubDate = PubMedPubDate.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("15").build())
                .pubStatus(PubStatus.RECEIVED)
                .build();

        History history = History.builder()
                .pubMedPubDates(List.of(pubDate))
                .build();

        PubmedData pubmedData = PubmedData.builder()
                .history(history)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .pubmedData(pubmedData)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e ->
            e.field().contains("PubmedData.History") && e.message().contains("Year")
        ));
    }

    // ========== Helper methods ==========

    private Article createMinimalArticle() {
        return Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test Title").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(
                                PublicationType.builder().value("Journal Article").build()
                        ))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .build();
    }

    private PubmedArticle createValidMinimalArticle() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        return PubmedArticle.builder()
                .medlineCitation(citation)
                .build();
    }

    // ========== 100% 커버리지를 위한 추가 테스트 / Additional tests for 100% coverage ==========

    @Test
    @DisplayName("Test 25: Instantiate PubmedValidator for constructor coverage")
    void test25_instantiateValidator() {
        assertDoesNotThrow(() -> new PubmedValidator());
    }

    @Test
    @DisplayName("Test 26: Null PublicationTypeList.publicationTypes should return error")
    void test26_nullPublicationTypes() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder().publicationTypes(null).build())
                .languages(List.of(Language.builder().value("eng").build()))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("Article.PublicationTypeList") && e.message().contains("at least one")
        ));
    }

    @Test
    @DisplayName("Test 27: Null MeshHeadings list should not cause error")
    void test27_nullMeshHeadings() {
        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(null)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("MeshHeading")));
    }

    @Test
    @DisplayName("Test 28: Null DescriptorName should not cause error")
    void test28_nullDescriptorName() {
        MeshHeading heading = MeshHeading.builder()
                .descriptorName(null)
                .build();

        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(List.of(heading))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("DescriptorName")));
    }

    @Test
    @DisplayName("Test 29: Null QualifierNames should not cause error")
    void test29_nullQualifierNames() {
        DescriptorName descriptor = DescriptorName.builder()
                .ui("D000001")
                .majorTopicYN("Y")
                .value("Test")
                .build();

        MeshHeading heading = MeshHeading.builder()
                .descriptorName(descriptor)
                .qualifierNames(null)
                .build();

        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(List.of(heading))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("QualifierName")));
    }

    @Test
    @DisplayName("Test 30: Null Authors list should not cause error")
    void test30_nullAuthors() {
        AuthorList authorList = AuthorList.builder()
                .authors(null)
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(authorList)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("Author")));
    }

    @Test
    @DisplayName("Test 31: Null Identifiers should not cause error")
    void test31_nullIdentifiers() {
        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(null)
                .build();

        AuthorList authorList = AuthorList.builder()
                .authors(List.of(author))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(authorList)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("Identifier")));
    }

    @Test
    @DisplayName("Test 32: Null History should not cause error")
    void test32_nullHistory() {
        PubmedData pubmedData = PubmedData.builder()
                .history(null)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .pubmedData(pubmedData)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("History")));
    }

    @Test
    @DisplayName("Test 33: Null PubMedPubDates should not cause error")
    void test33_nullPubMedPubDates() {
        History history = History.builder()
                .pubMedPubDates(null)
                .build();

        PubmedData pubmedData = PubmedData.builder()
                .history(history)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .pubmedData(pubmedData)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("PubMedPubDate")));
    }

    @Test
    @DisplayName("Test 34: DateCompleted with null Year should not cause error")
    void test34_nullYearInDate() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(null)
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted.Year")
        ));
    }

    @Test
    @DisplayName("Test 35: DateCompleted with null Month should not cause error")
    void test35_nullMonthInDate() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(null)
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted.Month")
        ));
    }

    @Test
    @DisplayName("Test 36: DateCompleted with null Day should not cause error")
    void test36_nullDayInDate() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(null)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted.Day")
        ));
    }

    @Test
    @DisplayName("Test 37: DateRevised validation should work")
    void test37_dateRevisedValidation() {
        DateRevised dateRevised = DateRevised.builder()
                .year(Year.builder().value("1800").build())  // Invalid year
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("DateRevised") && e.message().contains("Year")
        ));
    }

    @Test
    @DisplayName("Test 38: Year with null value should not cause error")
    void test38_yearWithNullValue() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value(null).build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("DateCompleted.Year")));
    }

    @Test
    @DisplayName("Test 39: Month with null value should not cause error")
    void test39_monthWithNullValue() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value(null).build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("DateCompleted.Month")));
    }

    @Test
    @DisplayName("Test 40: Day with null value should not cause error")
    void test40_dayWithNullValue() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value(null).build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("DateCompleted.Day")));
    }

    @Test
    @DisplayName("Test 41: Year with empty value should not cause error")
    void test41_yearWithEmptyValue() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("DateCompleted.Year")));
    }

    @Test
    @DisplayName("Test 42: Month with empty value should not cause error")
    void test42_monthWithEmptyValue() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("DateCompleted.Month")));
    }

    @Test
    @DisplayName("Test 43: Day with empty value should not cause error")
    void test43_dayWithEmptyValue() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("DateCompleted.Day")));
    }

    @Test
    @DisplayName("Test 44: Null DescriptorName UI should not cause error")
    void test44_nullDescriptorNameUI() {
        DescriptorName descriptor = DescriptorName.builder()
                .ui(null)
                .majorTopicYN("Y")
                .value("Test")
                .build();

        MeshHeading heading = MeshHeading.builder()
                .descriptorName(descriptor)
                .build();

        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(List.of(heading))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("DescriptorName.UI")));
    }

    @Test
    @DisplayName("Test 45: Null QualifierName UI should not cause error")
    void test45_nullQualifierNameUI() {
        QualifierName qualifier = QualifierName.builder()
                .ui(null)
                .majorTopicYN("N")
                .value("Test")
                .build();

        DescriptorName descriptor = DescriptorName.builder()
                .ui("D000001")
                .majorTopicYN("Y")
                .value("Test")
                .build();

        MeshHeading heading = MeshHeading.builder()
                .descriptorName(descriptor)
                .qualifierNames(List.of(qualifier))
                .build();

        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(List.of(heading))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("QualifierName") && e.field().contains("UI")));
    }

    @Test
    @DisplayName("Test 46: Non-ORCID identifier should not be validated")
    void test46_nonOrcidIdentifier() {
        Identifier otherId = Identifier.builder()
                .source("ResearcherID")
                .value("A-1234-2024")
                .build();

        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(List.of(otherId))
                .build();

        AuthorList authorList = AuthorList.builder()
                .authors(List.of(author))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(authorList)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertFalse(errors.stream().anyMatch(e -> e.field().contains("Identifier")));
    }

    @Test
    @DisplayName("Test 47: Empty Language list should return error")
    void test47_emptyLanguageList() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        assertTrue(errors.stream().anyMatch(e ->
            e.field().equals("Article.Language") && e.message().contains("At least one")
        ));
    }

    @Test
    @DisplayName("Test 48: Invalid year string should trigger reflection exception")
    void test48_invalidYearString() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("not-a-number").build())  // Invalid: not parseable as integer
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        // Should get a warning about date validation failure
        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted") && e.message().contains("Could not validate date")
        ));
    }

    @Test
    @DisplayName("Test 49: Invalid month string should trigger reflection exception")
    void test49_invalidMonthString() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("abc").build())  // Invalid: not parseable as integer
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        // Should get a warning about date validation failure
        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted") && e.message().contains("Could not validate date")
        ));
    }

    @Test
    @DisplayName("Test 50: Invalid day string should trigger reflection exception")
    void test50_invalidDayString() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("xyz").build())  // Invalid: not parseable as integer
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(createMinimalArticle())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<ValidationError> errors = PubmedArticleValidator.validateArticleCommon(article);

        // Should get a warning about date validation failure
        assertTrue(errors.stream().anyMatch(e ->
            e.field().contains("DateCompleted") && e.message().contains("Could not validate date")
        ));
    }
}
