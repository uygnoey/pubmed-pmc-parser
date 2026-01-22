package com.brillianttiger.bio.parser.pubmed.validation;

import com.brillianttiger.bio.parser.pubmed.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PubmedArticleValidatorTest / PubMed Article Validator 테스트
 *
 * KR: PMC 스타일 PubmedArticleValidator의 테스트.
 *     100% 커버리지 달성 목표.
 * EN: Test for PMC-style PubmedArticleValidator.
 *     Target: 100% coverage.
 */
class PubmedArticleValidatorTest {

    private final PubmedArticleValidator validator = new PubmedArticleValidator();

    // ========== 편의 메서드 / Helper Methods ==========

    private PubmedArticle createMinimalValidArticle() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test Article").build())
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

        return PubmedArticle.builder()
                .medlineCitation(citation)
                .build();
    }

    // ========== 1. Null 및 필수 요소 테스트 / Null and Required Element Tests ==========

    @Test
    @DisplayName("Test 1: Null article should return error")
    void test01_nullArticle() {
        List<PubmedValidationError> errors = validator.validateArticle(null);

        assertEquals(1, errors.size());
        assertEquals(PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT, errors.get(0).getCode());
        assertTrue(errors.get(0).getMessage().contains("null"));
    }

    @Test
    @DisplayName("Test 2: Missing MedlineCitation should return error")
    void test02_missingMedlineCitation() {
        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(null)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("MedlineCitation")
        ));
    }

    @Test
    @DisplayName("Test 3: Missing Status attribute should return error")
    void test03_missingStatus() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(null)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ATTRIBUTE &&
                e.getMessage().contains("Status")
        ));
    }

    @Test
    @DisplayName("Test 4: Missing PMID should return error")
    void test04_missingPmid() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(null)
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("PMID")
        ));
    }

    @Test
    @DisplayName("Test 5: Missing Article should return error")
    void test05_missingArticle() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(null)
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("Article is required")
        ));
    }

    @Test
    @DisplayName("Test 6: Missing PubModel attribute should return error")
    void test06_missingPubModel() {
        Article art = Article.builder()
                .pubModel(null)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ATTRIBUTE &&
                e.getMessage().contains("PubModel")
        ));
    }

    @Test
    @DisplayName("Test 7: Missing Journal should return error")
    void test07_missingJournal() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(null)
                .articleTitle(ArticleTitle.builder().value("Test").build())
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("Journal")
        ));
    }

    @Test
    @DisplayName("Test 8: Missing ArticleTitle should return error")
    void test08_missingArticleTitle() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(null)
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("ArticleTitle")
        ));
    }

    @Test
    @DisplayName("Test 9: Missing PublicationTypeList should return error")
    void test09_missingPublicationTypeList() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(null)
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("PublicationTypeList")
        ));
    }

    @Test
    @DisplayName("Test 10: Empty PublicationTypeList should return error")
    void test10_emptyPublicationTypeList() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.EMPTY_REQUIRED_ELEMENT &&
                e.getMessage().contains("at least one PublicationType")
        ));
    }

    @Test
    @DisplayName("Test 11: Missing Language should return error")
    void test11_missingLanguage() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(null)
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("Language")
        ));
    }

    @Test
    @DisplayName("Test 12: Missing MedlineJournalInfo should return error")
    void test12_missingMedlineJournalInfo() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(art)
                .medlineJournalInfo(null)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("MedlineJournalInfo")
        ));
    }

    @Test
    @DisplayName("Test 13: Valid minimal article should have no errors")
    void test13_validMinimalArticle() {
        PubmedArticle article = createMinimalValidArticle();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.isEmpty(), "Valid minimal article should have no errors. Got: " + errors);
    }

    // ========== 2. ID 형식 테스트 / ID Format Tests ==========

    @Test
    @DisplayName("Test 14: Invalid PMID format (too long) should return error")
    void test14_invalidPmidTooLong() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("123456789").build())  // 9 digits - invalid
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_PMID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 15: Invalid PMID format (contains letters) should return error")
    void test15_invalidPmidWithLetters() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("ABC12345").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_PMID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 16: Valid PMID format should pass")
    void test16_validPmid() {
        PubmedArticle article = createMinimalValidArticle();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_PMID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 17: Invalid DOI format should return error")
    void test17_invalidDoiFormat() {
        Article art = Article.builder()
                .pubModel(PubModel.Electronic)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .eLocationIDs(List.of(
                        ELocationID.builder()
                                .eIdType(EIdType.doi)
                                .value("invalid-doi")  // Invalid format
                                .build()
                ))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 18: Valid DOI format should pass")
    void test18_validDoiFormat() {
        Article art = Article.builder()
                .pubModel(PubModel.Electronic)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .eLocationIDs(List.of(
                        ELocationID.builder()
                                .eIdType(EIdType.doi)
                                .value("10.1234/example.2024")
                                .build()
                ))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 19: Invalid ORCID format should return error")
    void test19_invalidOrcidFormat() {
        Identifier orcid = Identifier.builder()
                .source("ORCID")
                .value("invalid-orcid")
                .build();

        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(List.of(orcid))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(AuthorList.builder()
                        .authors(List.of(author))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_ORCID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 20: Valid ORCID format should pass")
    void test20_validOrcidFormat() {
        Identifier orcid = Identifier.builder()
                .source("ORCID")
                .value("0000-0002-1825-0097")
                .build();

        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(List.of(orcid))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(AuthorList.builder()
                        .authors(List.of(author))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_ORCID_FORMAT
        ));
    }

    // ========== 3. 날짜 범위 테스트 / Date Range Tests ==========

    @Test
    @DisplayName("Test 21: Invalid year (too old) should return error")
    void test21_invalidYearTooOld() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("1808").build())  // Before 1809
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 22: Invalid month (too high) should return error")
    void test22_invalidMonthTooHigh() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("13").build())  // Invalid
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MONTH_RANGE
        ));
    }

    @Test
    @DisplayName("Test 23: Invalid day (too high) should return error")
    void test23_invalidDayTooHigh() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("32").build())  // Invalid
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DAY_RANGE
        ));
    }

    @Test
    @DisplayName("Test 24: DateRevised validation should work")
    void test24_dateRevisedValidation() {
        DateRevised date = DateRevised.builder()
                .year(Year.builder().value("1800").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateRevised(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 25: PubMedPubDate validation in History should work")
    void test25_pubMedPubDateValidation() {
        PubMedPubDate pubDate = PubMedPubDate.builder()
                .pubStatus(PubStatus.RECEIVED)
                .year(Year.builder().value("0").build())  // Invalid
                .month(Month.builder().value("13").build())  // Invalid
                .day(Day.builder().value("32").build())  // Invalid
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
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .pubmedData(pubmedData)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE ||
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MONTH_RANGE ||
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DAY_RANGE
        ));
    }

    @Test
    @DisplayName("Test 26: Invalid date string (non-numeric) should return warning")
    void test26_invalidDateString() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("not-a-number").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_FORMAT &&
                e.getSeverity() == PubmedValidationError.Severity.WARNING
        ));
    }

    // ========== 4. MeSH 검증 테스트 / MeSH Validation Tests ==========

    @Test
    @DisplayName("Test 27: Invalid MeSH UI format (DescriptorName) should return error")
    void test27_invalidMeshDescriptorUI() {
        DescriptorName descriptor = DescriptorName.builder()
                .ui("INVALID")  // Invalid format
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
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 28: Valid MeSH UI format (D prefix) should pass")
    void test28_validMeshDescriptorUI_D() {
        DescriptorName descriptor = DescriptorName.builder()
                .ui("D000001")  // Valid
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
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 29: Valid MeSH UI format (C prefix) should pass")
    void test29_validMeshDescriptorUI_C() {
        DescriptorName descriptor = DescriptorName.builder()
                .ui("C000657")  // Valid
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
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 30: Invalid MeSH UI format (QualifierName) should return error")
    void test30_invalidMeshQualifierUI() {
        QualifierName qualifier = QualifierName.builder()
                .ui("BADUI123")  // Invalid
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
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT &&
                e.getLocation().contains("QualifierName")
        ));
    }

    @Test
    @DisplayName("Test 31: Null MeshHeadingList should not cause error")
    void test31_nullMeshHeadingList() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(null)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        // Should not crash, just return no MeSH errors
        assertFalse(errors.stream().anyMatch(e ->
                e.getMessage().contains("MeSH")
        ));
    }

    // ========== 5. Edge Case Tests ==========

    @Test
    @DisplayName("Test 32: Null ELocationID list should not cause error")
    void test32_nullELocationIDList() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .eLocationIDs(null)
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.isEmpty() || errors.stream().noneMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 33: Null AuthorList should not cause error")
    void test33_nullAuthorList() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(null)
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.isEmpty() || errors.stream().noneMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_ORCID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 34: Non-ORCID identifier should not be validated")
    void test34_nonOrcidIdentifier() {
        Identifier otherId = Identifier.builder()
                .source("ResearcherID")
                .value("ABC-1234-2024")
                .build();

        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(List.of(otherId))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(AuthorList.builder()
                        .authors(List.of(author))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_ORCID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 35: Non-DOI ELocationID should not be validated")
    void test35_nonDoiELocationID() {
        Article art = Article.builder()
                .pubModel(PubModel.Electronic)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .eLocationIDs(List.of(
                        ELocationID.builder()
                                .eIdType(EIdType.pii)
                                .value("S1234-5678(24)00001-X")
                                .build()
                ))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }

    // ========== 6. 추가 브랜치 커버리지 테스트 / Additional Branch Coverage Tests ==========

    @Test
    @DisplayName("Test 36: Constructor instantiation should work")
    void test36_constructorInstantiation() {
        // Trigger class initialization bytecode
        PubmedArticleValidator validator2 = new PubmedArticleValidator();
        assertNotNull(validator2);
    }

    @Test
    @DisplayName("Test 37: Null Year object should not cause error")
    void test37_nullYearObject() {
        DateCompleted date = DateCompleted.builder()
                .year(null)
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        // Should not crash, just skip year validation
        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 38: Null Month object should not cause error")
    void test38_nullMonthObject() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(null)
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MONTH_RANGE
        ));
    }

    @Test
    @DisplayName("Test 39: Null Day object should not cause error")
    void test39_nullDayObject() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(null)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DAY_RANGE
        ));
    }

    @Test
    @DisplayName("Test 40: Empty Year string should not cause error")
    void test40_emptyYearString() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 41: Empty Month string should not cause error")
    void test41_emptyMonthString() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MONTH_RANGE
        ));
    }

    @Test
    @DisplayName("Test 42: Empty Day string should not cause error")
    void test42_emptyDayString() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("2024").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DAY_RANGE
        ));
    }

    @Test
    @DisplayName("Test 43: Null PubmedData should not cause error")
    void test43_nullPubmedData() {
        PubmedArticle article = createMinimalValidArticle();
        // PubmedData is already null in minimal article

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Test 44: Null History should not cause error")
    void test44_nullHistory() {
        PubmedData pubmedData = PubmedData.builder()
                .history(null)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .pubmedData(pubmedData)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Test 45: Null PubMedPubDates list should not cause error")
    void test45_nullPubMedPubDates() {
        History history = History.builder()
                .pubMedPubDates(null)
                .build();

        PubmedData pubmedData = PubmedData.builder()
                .history(history)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .pubmedData(pubmedData)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Test 46: Valid year at minimum boundary should pass")
    void test46_validYearMinBoundary() {
        DateCompleted date = DateCompleted.builder()
                .year(Year.builder().value("1809").build())
                .month(Month.builder().value("01").build())
                .day(Day.builder().value("01").build())
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(date)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 47: Null DescriptorName should not cause error")
    void test47_nullDescriptorName() {
        MeshHeading heading = MeshHeading.builder()
                .descriptorName(null)
                .build();

        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(List.of(heading))
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        // Should not crash
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 48: Null QualifierNames list should not cause error")
    void test48_nullQualifierNamesList() {
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
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getMessage().contains("QualifierName")
        ));
    }

    @Test
    @DisplayName("Test 49: Null PMID value should not cause error")
    void test49_nullPmidValue() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value(null).build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_PMID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 50: Null DOI value should not cause error")
    void test50_nullDoiValue() {
        Article art = Article.builder()
                .pubModel(PubModel.Electronic)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .eLocationIDs(List.of(
                        ELocationID.builder()
                                .eIdType(EIdType.doi)
                                .value(null)
                                .build()
                ))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 51: Null ORCID value should not cause error")
    void test51_nullOrcidValue() {
        Identifier orcid = Identifier.builder()
                .source("ORCID")
                .value(null)
                .build();

        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(List.of(orcid))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(AuthorList.builder()
                        .authors(List.of(author))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_ORCID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 52: Null identifier source should not cause error")
    void test52_nullIdentifierSource() {
        Identifier id = Identifier.builder()
                .source(null)
                .value("some-value")
                .build();

        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(List.of(id))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(AuthorList.builder()
                        .authors(List.of(author))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_ORCID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 53: Null DescriptorName UI should not cause error")
    void test53_nullDescriptorNameUI() {
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
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 54: Null QualifierName UI should not cause error")
    void test54_nullQualifierNameUI() {
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
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 55: Null DateCompleted should not cause error")
    void test55_nullDateCompleted() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateCompleted(null)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 56: Null DateRevised should not cause error")
    void test56_nullDateRevised() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .dateRevised(null)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 57: Null AuthorList authors should not cause error")
    void test57_nullAuthorListAuthors() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(AuthorList.builder()
                        .authors(null)
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_ORCID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 58: Null MeshHeadingList meshHeadings should not cause error")
    void test58_nullMeshHeadingListMeshHeadings() {
        MeshHeadingList meshList = MeshHeadingList.builder()
                .meshHeadings(null)
                .build();

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .meshHeadingList(meshList)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getMessage().contains("MeSH")
        ));
    }

    @Test
    @DisplayName("Test 59: Empty Language list should return error")
    void test59_emptyLanguageList() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of())  // Empty list
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("Language")
        ));
    }

    @Test
    @DisplayName("Test 60: Null Article inner should skip ID validation")
    void test60_nullArticleInner() {
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(null)  // Null article
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);

        // Should have missing Article error but not crash
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT
        ));
    }

    @Test
    @DisplayName("Test 61: Null EIdType should not cause error")
    void test61_nullEIdType() {
        Article art = Article.builder()
                .pubModel(PubModel.Electronic)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .eLocationIDs(List.of(
                        ELocationID.builder()
                                .eIdType(null)  // Null type
                                .value("some-value")
                                .build()
                ))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        assertFalse(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 62: Null identifier in list should not cause error")
    void test62_nullIdentifierInList() {
        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(new java.util.ArrayList<>(java.util.Collections.singletonList(null)))  // Null identifier
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                        .build())
                .languages(List.of(Language.builder().value("eng").build()))
                .authorList(AuthorList.builder()
                        .authors(List.of(author))
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

        List<PubmedValidationError> errors = validator.validateArticle(article);

        // Should not crash
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 63: PublicationTypeList with null publicationTypes should error")
    void test63_nullPublicationTypes() {
        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .publicationTypeList(PublicationTypeList.builder()
                        .publicationTypes(null)  // Null list
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

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.EMPTY_REQUIRED_ELEMENT
        ));
    }

    @Test
    @DisplayName("Test 64: Year with empty string should be handled")
    void test64_emptyYearString() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("").build())  // Empty string
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertNotNull(errors);  // Should not throw NPE
    }

    @Test
    @DisplayName("Test 65: Year below MIN_YEAR should error")
    void test65_yearBelowMinimum() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("1800").build())  // Below MIN_YEAR (1900)
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 66: Month with empty string should be handled")
    void test66_emptyMonthString() {
        DateRevised dateRevised = DateRevised.builder()
                .month(Month.builder().value("").build())  // Empty string
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 67: Month below 1 should error")
    void test67_monthBelowMinimum() {
        DateRevised dateRevised = DateRevised.builder()
                .month(Month.builder().value("0").build())  // Below 1
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MONTH_RANGE
        ));
    }

    @Test
    @DisplayName("Test 68: Invalid month format should warn")
    void test68_invalidMonthFormat() {
        DateRevised dateRevised = DateRevised.builder()
                .month(Month.builder().value("abc").build())  // Non-numeric
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getSeverity() == PubmedValidationError.Severity.WARNING &&
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 69: Day with empty string should be handled")
    void test69_emptyDayString() {
        DateRevised dateRevised = DateRevised.builder()
                .day(Day.builder().value("").build())  // Empty string
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 70: Day below 1 should error")
    void test70_dayBelowMinimum() {
        DateRevised dateRevised = DateRevised.builder()
                .day(Day.builder().value("0").build())  // Below 1
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DAY_RANGE
        ));
    }

    @Test
    @DisplayName("Test 71: Invalid day format should warn")
    void test71_invalidDayFormat() {
        DateRevised dateRevised = DateRevised.builder()
                .day(Day.builder().value("xyz").build())  // Non-numeric
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getSeverity() == PubmedValidationError.Severity.WARNING &&
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 72: Invalid MESH UI format should error")
    void test72_invalidMeshUiFormat() {
        DescriptorName descriptorName = DescriptorName.builder()
                .ui("INVALID")  // Invalid format (should be Dxxxxxx or Cxxxxxx)
                .value("Test Descriptor")
                .build();

        MeshHeading meshHeading = MeshHeading.builder()
                .descriptorName(descriptorName)
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .meshHeadingList(MeshHeadingList.builder()
                        .meshHeadings(List.of(meshHeading))
                        .build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 73: Author with null identifiers list should be skipped")
    void test73_authorWithNullIdentifiers() {
        Author author = Author.builder()
                .validYN("Y")
                .lastName(LastName.builder().value("Smith").build())
                .identifiers(null)  // Null identifiers list
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
                .authorList(AuthorList.builder()
                        .authors(List.of(author))
                        .build())
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

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertNotNull(errors);  // Should not throw NPE
    }

    @Test
    @DisplayName("Test 74: Year above MAX_YEAR should error")
    void test74_yearAboveMaximum() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value("2100").build())  // Above MAX_YEAR (current+5)
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE
        ));
    }

    @Test
    @DisplayName("Test 75: Null year value should be handled gracefully")
    void test75_nullYearValue() {
        DateCompleted dateCompleted = DateCompleted.builder()
                .year(Year.builder().value(null).build())  // Null value
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateCompleted(dateCompleted)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertNotNull(errors);  // Should not throw NPE
    }

    @Test
    @DisplayName("Test 76: Null month value should be handled gracefully")
    void test76_nullMonthValue() {
        DateRevised dateRevised = DateRevised.builder()
                .month(Month.builder().value(null).build())  // Null value
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertNotNull(errors);  // Should not throw NPE
    }

    @Test
    @DisplayName("Test 77: Null day value should be handled gracefully")
    void test77_nullDayValue() {
        DateRevised dateRevised = DateRevised.builder()
                .day(Day.builder().value(null).build())  // Null value
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .dateRevised(dateRevised)
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertNotNull(errors);  // Should not throw NPE
    }

    @Test
    @DisplayName("Test 78: Valid MESH UI D-format should pass")
    void test78_validMeshUiDFormat() {
        DescriptorName descriptorName = DescriptorName.builder()
                .ui("D123456")  // Valid D-format
                .value("Test Descriptor")
                .build();

        MeshHeading meshHeading = MeshHeading.builder()
                .descriptorName(descriptorName)
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .meshHeadingList(MeshHeadingList.builder()
                        .meshHeadings(List.of(meshHeading))
                        .build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().noneMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT
        ));
    }

    @Test
    @DisplayName("Test 79: Valid MESH QualifierName UI should pass")
    void test79_validMeshQualifierUi() {
        DescriptorName descriptorName = DescriptorName.builder()
                .ui("D123456")
                .value("Test Descriptor")
                .build();

        QualifierName qualifierName = QualifierName.builder()
                .ui("D654321")  // Valid D-format (D or C followed by 6 digits)
                .value("Test Qualifier")
                .build();

        MeshHeading meshHeading = MeshHeading.builder()
                .descriptorName(descriptorName)
                .qualifierNames(List.of(qualifierName))
                .build();

        Article art = Article.builder()
                .pubModel(PubModel.Print)
                .journal(Journal.builder().build())
                .articleTitle(ArticleTitle.builder().value("Test").build())
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
                .meshHeadingList(MeshHeadingList.builder()
                        .meshHeadings(List.of(meshHeading))
                        .build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().noneMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT
        ));
    }

    @Test
    public void test80_pmidWithWhitespaceOnlyValue() {
        // Test PMID with whitespace-only value (covers pmid.getValue().trim().isEmpty() branch)
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("   ").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.EMPTY_REQUIRED_ELEMENT &&
                e.getMessage().contains("PMID value is empty")
        ));
    }

    @Test
    public void test81_articleTitleWithNullValue() {
        // Test ArticleTitle with null getValue() (covers articleTitle.getValue() == null branch)
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value(null).build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("ArticleTitle is required")
        ));
    }

    @Test
    public void test82_articleTitleWithWhitespaceOnlyValue() {
        // Test ArticleTitle with whitespace-only value (covers getValue().trim().isEmpty() branch)
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("   ").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT &&
                e.getMessage().contains("ArticleTitle is required")
        ));
    }

    @Test
    public void test83_eLocationIDWithNullEIdType() {
        // Test ELocationID with null eIdType (covers eIdType.equals() == false branch)
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .eLocationIDs(List.of(ELocationID.builder()
                                .eIdType(null)
                                .value("some-value")
                                .build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        // Should not have DOI format error (eIdType is not doi)
        assertTrue(errors.stream().noneMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }

    @Test
    public void test84_eLocationIDWithDoiTypeButNullValue() {
        // Test ELocationID with eIdType=doi but value=null (covers getValue() == null branch)
        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .eLocationIDs(List.of(ELocationID.builder()
                                .eIdType(EIdType.doi)
                                .value(null)
                                .build()))
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        // Should not have DOI format error (value is null, so validation is skipped)
        assertTrue(errors.stream().noneMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }

    @Test
    public void test85_eLocationIDListWithNullElement() {
        // Test ELocationID list with null element (covers elocation == null branch)
        List<ELocationID> eLocationIDsWithNull = new ArrayList<>();
        eLocationIDsWithNull.add(null);

        MedlineCitation citation = MedlineCitation.builder()
                .status(Status.Completed)
                .pmid(PMID.builder().value("12345678").build())
                .article(Article.builder()
                        .pubModel(PubModel.Print)
                        .journal(Journal.builder().build())
                        .articleTitle(ArticleTitle.builder().value("Test").build())
                        .publicationTypeList(PublicationTypeList.builder()
                                .publicationTypes(List.of(PublicationType.builder().value("Journal Article").build()))
                                .build())
                        .languages(List.of(Language.builder().value("eng").build()))
                        .eLocationIDs(eLocationIDsWithNull)
                        .build())
                .medlineJournalInfo(MedlineJournalInfo.builder().build())
                .build();

        PubmedArticle article = PubmedArticle.builder()
                .medlineCitation(citation)
                .build();

        List<PubmedValidationError> errors = validator.validateArticle(article);
        // Should not have DOI format error (elocation is null, so validation is skipped)
        assertTrue(errors.stream().noneMatch(e ->
                e.getCode() == PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT
        ));
    }
}

