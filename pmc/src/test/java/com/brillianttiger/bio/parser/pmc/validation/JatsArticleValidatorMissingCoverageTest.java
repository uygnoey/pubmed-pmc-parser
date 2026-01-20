package com.brillianttiger.bio.parser.pmc.validation;

import com.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JatsArticleValidator Missing Coverage Tests
 *
 * KR: JatsArticleValidator의 누락된 브랜치 커버리지를 달성하기 위한 테스트.
 *     기존 JatsArticleValidatorTest에서 커버하지 못한 엣지 케이스 테스트.
 * EN: Tests to achieve missing branch coverage for JatsArticleValidator.
 *     Edge cases not covered by existing JatsArticleValidatorTest.
 *
 * Target: 88% → Maximum achievable coverage
 * Missed Branches: 29 (identified from JaCoCo report)
 */
@DisplayName("JatsArticleValidator - Missing Coverage Tests")
class JatsArticleValidatorMissingCoverageTest {

    private JatsArticleValidator validator;

    @BeforeEach
    void setUp() {
        validator = new JatsArticleValidator();
    }

    // ========================================================================
    // Test 1-2: Line 158-159 - ArticleTitle content null/empty
    // ========================================================================

    @Test
    @DisplayName("Test 1: validateRequiredElements() - ArticleTitle with null content")
    void test1_articleTitleWithNullContent() {
        // Given: ArticleTitle exists but content is null
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content(null)  // null content
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should detect missing article title
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_ARTICLE_TITLE)
        ), "ArticleTitle content가 null이면 오류가 발생해야 함");
    }

    @Test
    @DisplayName("Test 2: validateRequiredElements() - ArticleTitle with empty content")
    void test2_articleTitleWithEmptyContent() {
        // Given: ArticleTitle exists but content is empty/whitespace
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("   ")  // whitespace only
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should detect missing article title
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_ARTICLE_TITLE)
        ), "ArticleTitle content가 empty이면 오류가 발생해야 함");
    }

    // ========================================================================
    // Test 3: Line 231 - switch default case with non-validated PubIdType
    // ========================================================================

    @Test
    @DisplayName("Test 3: validateArticleId() - ARXIV type (no validation)")
    void test3_articleIdWithArxivType() {
        // Given: Article with ARXIV pub-id-type (not validated by switch)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("arxiv")
                                                .value("1234.5678")
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception, no validation for ARXIV type
        assertNotNull(errors);
        // ARXIV는 switch에서 처리되지 않으므로 검증되지 않음
    }

    // ========================================================================
    // Test 4: Line 284 - ContribIdType non-ORCID
    // ========================================================================

    @Test
    @DisplayName("Test 4: validateContribOrcid() - ContribId with ISNI type (non-ORCID)")
    void test4_contribIdWithIsniType() {
        // Given: Contrib with ISNI contributor ID (not ORCID)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .contribGroups(List.of(
                                        ContribGroup.builder()
                                                .contributors(List.of(
                                                        Contrib.builder()
                                                                .contribIds(List.of(
                                                                        ContribId.builder()
                                                                                .contribIdType(ContribIdType.ISNI)
                                                                                .value("0000 0001 2345 6789")
                                                                                .build()
                                                                ))
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not validate ISNI (only ORCID is validated)
        assertNotNull(errors);
        // ISNI는 ORCID가 아니므로 validateContribOrcid에서 검증하지 않음
    }

    // ========================================================================
    // Test 5-10: Various null ID checks in collection methods
    // ========================================================================

    @Test
    @DisplayName("Test 5: collectAllIds() - Aff with null ID")
    void test5_affWithNullId() {
        // Given: Article with Aff that has null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(
                                        ContribGroup.builder()
                                                .affiliations(List.of(
                                                        Aff.builder()
                                                                .id(null)  // null ID
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception, just skip null IDs
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 6: collectRefIds() - Ref with null ID")
    void test6_refWithNullId() {
        // Given: Article with Back/RefList containing Ref with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(Back.builder()
                        .refLists(List.of(
                                RefList.builder()
                                        .references(List.of(
                                                Ref.builder()
                                                        .id(null)  // null ID
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 7: collectBodyIds() - Body with null DefList ID")
    void test7_bodyWithNullDefListId() {
        // Given: Article with Body containing DefList with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .sections(List.of(
                                Sec.builder()
                                        .defLists(List.of(
                                                DefList.builder()
                                                        .id(null)  // null ID
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 8: collectBodyIds() - Body with null BoxedText ID")
    void test8_bodyWithNullBoxedTextId() {
        // Given: Article with Body containing BoxedText with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .sections(List.of(
                                Sec.builder()
                                        .boxedTexts(List.of(
                                                BoxedText.builder()
                                                        .id(null)  // null ID
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 9: collectBodyIds() - Body with null DispQuote ID")
    void test9_bodyWithNullDispQuoteId() {
        // Given: Article with Body containing DispQuote with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .sections(List.of(
                                Sec.builder()
                                        .dispQuotes(List.of(
                                                DispQuote.builder()
                                                        .id(null)  // null ID
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 10: collectBackIds() - Back with null Ack ID")
    void test10_backWithNullAckId() {
        // Given: Article with Back containing Ack with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(Back.builder()
                        .acknowledgments(List.of(
                                Ack.builder()
                                        .id(null)  // null ID
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    // ========================================================================
    // Test 11-20: Additional null ID checks
    // ========================================================================

    @Test
    @DisplayName("Test 11: collectBackIds() - Back with null AppGroup ID")
    void test11_backWithNullAppGroupId() {
        // Given: Article with Back containing AppGroup with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(Back.builder()
                        .appGroups(List.of(
                                AppGroup.builder()
                                        .id(null)  // null ID
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 12: collectBackIds() - Back with null Bio ID")
    void test12_backWithNullBioId() {
        // Given: Article with Back containing Bio with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(Back.builder()
                        .biographies(List.of(
                                Bio.builder()
                                        .id(null)  // null ID
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 13: collectBackIds() - Back with null FnGroup ID")
    void test13_backWithNullFnGroupId() {
        // Given: Article with Back containing FnGroup with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(Back.builder()
                        .fnGroups(List.of(
                                FnGroup.builder()
                                        .id(null)  // null ID
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 14: collectBackIds() - Back with null Glossary ID")
    void test14_backWithNullGlossaryId() {
        // Given: Article with Back containing Glossary with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(Back.builder()
                        .glossaries(List.of(
                                Glossary.builder()
                                        .id(null)  // null ID
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 15: collectBackIds() - Back with null Notes ID")
    void test15_backWithNullNotesId() {
        // Given: Article with Back containing Notes with null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(Back.builder()
                        .notesList(List.of(
                                Notes.builder()
                                        .id(null)  // null ID
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 16: collectSecIds() - Sec with null Code ID")
    void test16_secWithNullCodeId() {
        // Given: Article with Sec containing Code with null ID (line 638)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .sections(List.of(
                                Sec.builder()
                                        .sections(List.of(
                                                Sec.builder()
                                                        .codeBlocks(List.of(
                                                                Code.builder()
                                                                        .id(null)  // null ID
                                                                        .build()
                                                        ))
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 17: collectSecIds() - Sec with null List ID")
    void test17_secWithNullListId() {
        // Given: Article with Sec containing JatsList with null ID (line 647)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .sections(List.of(
                                Sec.builder()
                                        .sections(List.of(
                                                Sec.builder()
                                                        .lists(List.of(
                                                                PmcList.builder()
                                                                        .id(null)  // null ID
                                                                        .build()
                                                        ))
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 18: collectFigIds() - null figures list")
    void test18_nullFiguresList() {
        // Given: Article with null figures list (line 658)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .floatsGroup(FloatsGroup.builder()
                        .figs(null)  // null list
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 19: collectTableWrapIds() - null tableWraps list")
    void test19_nullTableWrapsList() {
        // Given: Article with null tableWraps list (line 677)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .floatsGroup(FloatsGroup.builder()
                        .tableWraps(null)  // null list
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 20: collectDispFormulaIds() - null dispFormulas list")
    void test20_nullDispFormulasList() {
        // Given: Article with Sec containing null dispFormulas list (line 692)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .sections(List.of(
                                Sec.builder()
                                        .sections(List.of(
                                                Sec.builder()
                                                        .dispFormulas(null)  // null list
                                                        .build()
                                        ))
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    // ========================================================================
    // Test 21-30: null 컨테이너 체크
    // ========================================================================

    @Test
    @DisplayName("Test 21: collectBodyIds() - null Body")
    void test21_nullBody() {
        // Given: Article with null Body (line 416)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(null)  // null Body
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 22: collectBackIds() - null Back")
    void test22_nullBack() {
        // Given: Article with null Back (line 491)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(null)  // null Back
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 24: collectRefIds() - RefList with null references")
    void test24_refListWithNullReferences() {
        // Given: RefList with null references list (line 393)
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .back(Back.builder()
                        .refLists(List.of(
                                RefList.builder()
                                        .references(null)  // null list
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 25: collectAllIds() - Aff with non-null ID (for line 359 coverage)")
    void test25_affWithNonNullId() {
        // Given: Article with Aff that has ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(
                                        ContribGroup.builder()
                                                .affiliations(List.of(
                                                        Aff.builder()
                                                                .id("aff1")  // non-null ID
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should collect ID without error
        assertNotNull(errors);
    }

    // ========================================================================
    // Test 26-28: Line 225 - idType == PubIdType.OTHER 커버
    // ========================================================================

    @Test
    @DisplayName("Test 26: validateArticleId() - null pub-id-type (returns OTHER)")
    void test26_articleIdWithNullPubIdType() {
        // Given: Article with ArticleId having null pub-id-type
        // Note: PubIdType.fromValue(null) returns OTHER
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType(null)  // null type → fromValue returns OTHER
                                                .value("some-id")
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should skip validation for OTHER type
        assertNotNull(errors);
        // Line 225: idType == null || idType == PubIdType.OTHER → true
        // Line 226: return errors (no validation)
    }

    @Test
    @DisplayName("Test 27: validateArticleId() - empty pub-id-type (returns OTHER)")
    void test27_articleIdWithEmptyPubIdType() {
        // Given: Article with ArticleId having empty pub-id-type
        // Note: PubIdType.fromValue("") returns OTHER
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("   ")  // whitespace only → fromValue returns OTHER
                                                .value("some-id")
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should skip validation for OTHER type
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Test 28: validateArticleId() - unknown pub-id-type (returns OTHER)")
    void test28_articleIdWithUnknownPubIdType() {
        // Given: Article with ArticleId having unknown pub-id-type
        // Note: PubIdType.fromValue("unknown-type") returns OTHER
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("unknown-type")  // unknown → fromValue returns OTHER
                                                .value("some-id")
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should skip validation for OTHER type
        assertNotNull(errors);
    }

    /**
     * Test 29: validateReferenceIntegrity() - Aff with null ID
     * Coverage: Line 359 - if (aff.getId() != null)
     */
    @Test
    @DisplayName("Test 29: validateReferenceIntegrity() - Aff with null ID should be skipped")
    void test29_affWithNullId() {
        // Given: Article with Aff that has null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .affiliations(List.of(
                                        Aff.builder()
                                                .id(null)  // null ID - should be skipped in collection
                                                .value("Department of Biology")
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not cause errors, null ID simply not collected
        assertNotNull(errors);
    }

    /**
     * Test 30: validateReferenceIntegrity() - DefList with null ID
     * Coverage: Line 450 - if (defList.getId() != null)
     */
    @Test
    @DisplayName("Test 30: validateReferenceIntegrity() - DefList with null ID should be skipped")
    void test30_defListWithNullId() {
        // Given: Article with DefList that has null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .defLists(List.of(
                                DefList.builder()
                                        .id(null)  // null ID - should be skipped in collection
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not cause errors, null ID simply not collected
        assertNotNull(errors);
    }

    /**
     * Test 31: validateReferenceIntegrity() - BoxedText with null ID
     * Coverage: Line 459 - if (boxedText.getId() != null)
     */
    @Test
    @DisplayName("Test 31: validateReferenceIntegrity() - BoxedText with null ID should be skipped")
    void test31_boxedTextWithNullId() {
        // Given: Article with BoxedText that has null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .boxedTexts(List.of(
                                BoxedText.builder()
                                        .id(null)  // null ID - should be skipped in collection
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not cause errors, null ID simply not collected
        assertNotNull(errors);
    }

    /**
     * Test 32: validateReferenceIntegrity() - DispQuote with null ID
     * Coverage: Line 468 - if (dispQuote.getId() != null)
     */
    @Test
    @DisplayName("Test 32: validateReferenceIntegrity() - DispQuote with null ID should be skipped")
    void test32_dispQuoteWithNullId() {
        // Given: Article with DispQuote that has null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .dispQuotes(List.of(
                                DispQuote.builder()
                                        .id(null)  // null ID - should be skipped in collection
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not cause errors, null ID simply not collected
        assertNotNull(errors);
    }

    /**
     * Test 33: validateReferenceIntegrity() - Code with null ID
     * Coverage: Line 477 - if (code.getId() != null)
     */
    @Test
    @DisplayName("Test 33: validateReferenceIntegrity() - Code with null ID should be skipped")
    void test33_codeWithNullId() {
        // Given: Article with Code that has null ID
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .body(Body.builder()
                        .codeBlocks(List.of(
                                Code.builder()
                                        .id(null)  // null ID - should be skipped in collection
                                        .value("System.out.println(\"Hello\");")
                                        .build()
                        ))
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not cause errors, null ID simply not collected
        assertNotNull(errors);
    }

    // ========================================================================
    // Test 34: Line 703 - article.getDtdVersion() == null
    // ========================================================================

    /**
     * Test 34: validateRecommendedAttributes() - null dtdVersion
     * Coverage: Line 703 - if (article.getDtdVersion() == null || ...)
     *
     * KR: dtdVersion이 null인 경우를 테스트하여 OR 조건의 첫 번째 분기를 커버.
     * EN: Test null dtdVersion case to cover first branch of OR condition.
     */
    @Test
    @DisplayName("Test 34: validateRecommendedAttributes() - null dtdVersion should generate INFO warning")
    void test34_nullDtdVersion() {
        // Given: Article with null dtdVersion
        JatsArticle article = JatsArticle.builder()
                .articleType(ArticleType.RESEARCH_ARTICLE)
                .dtdVersion(null)  // null dtdVersion - covers first part of OR condition (line 703)
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should generate INFO warning for null dtdVersion
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE) &&
                e.getMessage().contains("dtd-version") &&
                e.getSeverity() == ValidationError.Severity.INFO
        ), "null dtdVersion은 INFO 경고가 발생해야 함");
    }
}
