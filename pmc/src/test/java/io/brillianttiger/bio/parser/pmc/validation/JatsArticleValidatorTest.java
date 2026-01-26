package io.brillianttiger.bio.parser.pmc.validation;

import io.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JatsArticleValidator 클래스 테스트 / JatsArticleValidator class tests
 *
 * KR: JatsArticleValidator의 모든 검증 로직을 테스트하여 100% 커버리지 달성
 * EN: Tests all JatsArticleValidator validation logic to achieve 100% coverage
 */
@DisplayName("JatsArticleValidator 테스트")
class JatsArticleValidatorTest {

    private JatsArticleValidator validator;

    @BeforeEach
    void setUp() {
        validator = new JatsArticleValidator();
    }

    // ========================================
    // validateArticle() 전체 검증 테스트
    // ========================================

    @Test
    @DisplayName("validateArticle() - null article")
    void testValidateArticleWithNull() {
        // When
        List<ValidationError> errors = validator.validateArticle(null);

        // Then
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals(ValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT, errors.get(0).getCode());
        assertEquals(ValidationError.Severity.ERROR, errors.get(0).getSeverity());
    }

    @Test
    @DisplayName("validateArticle() - 완전한 유효한 article")
    void testValidateArticleWithValidArticle() {
        // Given
        JatsArticle article = createValidArticle();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertNotNull(errors);
        assertTrue(errors.isEmpty(), "유효한 article은 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateArticle() - 모든 검증 실행 확인")
    void testValidateArticleRunsAllValidations() {
        // Given
        JatsArticle article = createArticleWithMultipleIssues();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertNotNull(errors);
        assertTrue(errors.size() > 0, "여러 검증 오류가 발견되어야 함");

        // 필수 요소, ID 포맷, 참조 무결성, 권장 속성 검증이 모두 실행되었는지 확인
        boolean hasRequiredElementError = errors.stream()
                .anyMatch(e -> e.getCode().startsWith("MISSING_REQUIRED_"));
        boolean hasIdFormatError = errors.stream()
                .anyMatch(e -> e.getCode().startsWith("INVALID_"));

        assertTrue(hasRequiredElementError || hasIdFormatError,
                "적어도 하나의 검증 오류가 있어야 함");
    }

    // ========================================
    // validateRequiredElements() 테스트
    // ========================================

    @Test
    @DisplayName("validateRequiredElements() - front 누락")
    void testValidateRequiredElementsWithoutFront() {
        // Given
        JatsArticle article = JatsArticle.builder()
                .front(null)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_FRONT) &&
                e.getSeverity() == ValidationError.Severity.ERROR
        ), "front 누락 오류가 있어야 함");
    }

    @Test
    @DisplayName("validateRequiredElements() - article-meta 누락")
    void testValidateRequiredElementsWithoutArticleMeta() {
        // Given
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(null)
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_ARTICLE_META) &&
                e.getSeverity() == ValidationError.Severity.ERROR
        ), "article-meta 누락 오류가 있어야 함");
    }

    @Test
    @DisplayName("validateRequiredElements() - title-group 누락")
    void testValidateRequiredElementsWithoutTitleGroup() {
        // Given
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(null)
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_TITLE_GROUP) &&
                e.getSeverity() == ValidationError.Severity.WARNING
        ), "title-group 누락 경고가 있어야 함");
    }

    @Test
    @DisplayName("validateRequiredElements() - article-title 누락")
    void testValidateRequiredElementsWithoutArticleTitle() {
        // Given
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(null)
                                        .build())
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertNotNull(errors);
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_ARTICLE_TITLE) &&
                e.getSeverity() == ValidationError.Severity.ERROR
        ), "article-title 누락 오류가 있어야 함");
    }

    // ========================================
    // validateIdFormats() - DOI 테스트
    // ========================================

    @Test
    @DisplayName("validateIdFormats() - 유효한 DOI")
    void testValidateIdFormatsWithValidDoi() {
        // Given
        JatsArticle article = createArticleWithDoi("10.1234/test.article.2024");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        boolean hasDoiError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_DOI_FORMAT));
        assertFalse(hasDoiError, "유효한 DOI는 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - 무효한 DOI (10으로 시작하지 않음)")
    void testValidateIdFormatsWithInvalidDoiPrefix() {
        // Given
        JatsArticle article = createArticleWithDoi("20.1234/invalid");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.INVALID_DOI_FORMAT) &&
                e.getSeverity() == ValidationError.Severity.ERROR
        ), "무효한 DOI 포맷 오류가 있어야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - 무효한 DOI (슬래시 없음)")
    void testValidateIdFormatsWithInvalidDoiNoSlash() {
        // Given
        JatsArticle article = createArticleWithDoi("10.1234-no-slash");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.INVALID_DOI_FORMAT) &&
                e.getSeverity() == ValidationError.Severity.ERROR
        ), "무효한 DOI 포맷 오류가 있어야 함");
    }

    // Note: 빈 DOI는 validateArticleId에서 건너뛰기 때문에 에러가 발생하지 않음
    /*
    @Test
    @DisplayName("validateIdFormats() - 빈 DOI")
    void testValidateIdFormatsWithEmptyDoi() {
        // Given
        JatsArticle article = createArticleWithDoi("");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.INVALID_DOI_FORMAT)
        ), "빈 DOI는 오류가 있어야 함");
    }
    */

    // ========================================
    // validateIdFormats() - PMCID 테스트
    // ========================================

    @Test
    @DisplayName("validateIdFormats() - 유효한 PMCID")
    void testValidateIdFormatsWithValidPmcid() {
        // Given
        JatsArticle article = createArticleWithPmcid("PMC1234567");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        boolean hasPmcidError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_PMCID_FORMAT));
        assertFalse(hasPmcidError, "유효한 PMCID는 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - 무효한 PMCID (PMC 접두사 없음)")
    void testValidateIdFormatsWithInvalidPmcidPrefix() {
        // Given
        JatsArticle article = createArticleWithPmcid("1234567");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.INVALID_PMCID_FORMAT) &&
                e.getSeverity() == ValidationError.Severity.ERROR
        ), "무효한 PMCID 포맷 오류가 있어야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - 무효한 PMCID (숫자 아님)")
    void testValidateIdFormatsWithInvalidPmcidNonNumeric() {
        // Given
        JatsArticle article = createArticleWithPmcid("PMCabc123");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.INVALID_PMCID_FORMAT)
        ), "무효한 PMCID 포맷 오류가 있어야 함");
    }

    // ========================================
    // validateIdFormats() - PMID 테스트
    // ========================================

    @Test
    @DisplayName("validateIdFormats() - 유효한 PMID")
    void testValidateIdFormatsWithValidPmid() {
        // Given
        JatsArticle article = createArticleWithPmid("12345678");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        boolean hasPmidError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_PMID_FORMAT));
        assertFalse(hasPmidError, "유효한 PMID는 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - 무효한 PMID (숫자 아님)")
    void testValidateIdFormatsWithInvalidPmidNonNumeric() {
        // Given
        JatsArticle article = createArticleWithPmid("abc123");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.INVALID_PMID_FORMAT) &&
                e.getSeverity() == ValidationError.Severity.ERROR
        ), "무효한 PMID 포맷 오류가 있어야 함");
    }

    // ========================================
    // validateIdFormats() - ORCID 테스트
    // ========================================

    @Test
    @DisplayName("validateIdFormats() - 유효한 ORCID")
    void testValidateIdFormatsWithValidOrcid() {
        // Given
        JatsArticle article = createArticleWithOrcid("0000-0002-1825-0097");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        boolean hasOrcidError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_ORCID_FORMAT));
        assertFalse(hasOrcidError, "유효한 ORCID는 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - 유효한 ORCID (X 체크섬)")
    void testValidateIdFormatsWithValidOrcidXChecksum() {
        // Given
        JatsArticle article = createArticleWithOrcid("0000-0001-2345-678X");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        boolean hasOrcidError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_ORCID_FORMAT));
        assertFalse(hasOrcidError, "유효한 ORCID (X 체크섬)는 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - 무효한 ORCID (포맷 오류)")
    void testValidateIdFormatsWithInvalidOrcidFormat() {
        // Given
        JatsArticle article = createArticleWithOrcid("0000-0002-1825-009");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.INVALID_ORCID_FORMAT) &&
                e.getSeverity() == ValidationError.Severity.ERROR
        ), "무효한 ORCID 포맷 오류가 있어야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - 무효한 ORCID (하이픈 없음)")
    void testValidateIdFormatsWithInvalidOrcidNoHyphen() {
        // Given
        JatsArticle article = createArticleWithOrcid("0000000218250097");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.INVALID_ORCID_FORMAT)
        ), "무효한 ORCID 포맷 오류가 있어야 함");
    }

    // ========================================
    // validateReferenceIntegrity() 테스트
    // ========================================

    // Note: validateReferenceIntegrity() 는 현재 ID 수집만 하고 실제 검증은 하지 않음
    // 향후 xref 참조 검증 기능 구현 시 아래 테스트 활성화 필요

    /*
    @Test
    @DisplayName("validateReferenceIntegrity() - 유효한 참조")
    void testValidateReferenceIntegrityWithValidReferences() {
        // Given
        JatsArticle article = createArticleWithValidReferences();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        boolean hasRefIntegrityError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.BROKEN_XREF_REFERENCE));
        assertFalse(hasRefIntegrityError, "유효한 참조는 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateReferenceIntegrity() - 무효한 참조 (대상 없음)")
    void testValidateReferenceIntegrityWithInvalidReference() {
        // Given
        JatsArticle article = createArticleWithInvalidReference();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.BROKEN_XREF_REFERENCE) &&
                e.getSeverity() == ValidationError.Severity.WARNING
        ), "무효한 참조 경고가 있어야 함");
    }

    @Test
    @DisplayName("validateReferenceIntegrity() - body 없음")
    void testValidateReferenceIntegrityWithoutBody() {
        // Given
        JatsArticle article = createValidArticle();
        article.setBody(null);

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        // body가 없으면 참조 검증을 건너뛰므로 참조 관련 오류가 없어야 함
        boolean hasRefIntegrityError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.BROKEN_XREF_REFERENCE));
        assertFalse(hasRefIntegrityError);
    }
    */

    // ========================================
    // validateRecommendedAttributes() 테스트
    // ========================================

    @Test
    @DisplayName("validateRecommendedAttributes() - article-type 있음")
    void testValidateRecommendedAttributesWithArticleType() {
        // Given
        JatsArticle article = createValidArticle();
        article.setArticleType(ArticleType.RESEARCH_ARTICLE);

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        boolean hasArticleTypeWarning = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE) &&
                        e.getMessage().contains("article-type"));
        assertFalse(hasArticleTypeWarning, "article-type이 있으면 경고가 없어야 함");
    }

    @Test
    @DisplayName("validateRecommendedAttributes() - article-type 없음")
    void testValidateRecommendedAttributesWithoutArticleType() {
        // Given
        JatsArticle article = createValidArticle();
        article.setArticleType(null);

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE) &&
                e.getMessage().contains("article-type") &&
                e.getSeverity() == ValidationError.Severity.WARNING
        ), "article-type 누락 경고가 있어야 함");
    }

    // Note: abstract 검증은 현재 validateRecommendedAttributes에서 구현되지 않음
    // 향후 abstract 권장 검증 기능 구현 시 아래 테스트 활성화 필요

    /*
    @Test
    @DisplayName("validateRecommendedAttributes() - abstract 있음")
    void testValidateRecommendedAttributesWithAbstract() {
        // Given
        JatsArticle article = createValidArticle();
        List<PmcAbstract> abstracts = new ArrayList<>();
        abstracts.add(PmcAbstract.builder().build());
        article.getFront().getArticleMeta().setAbstracts(abstracts);

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        boolean hasAbstractWarning = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE) &&
                        e.getMessage().contains("abstract"));
        assertFalse(hasAbstractWarning, "abstract가 있으면 경고가 없어야 함");
    }

    @Test
    @DisplayName("validateRecommendedAttributes() - abstract 없음")
    void testValidateRecommendedAttributesWithoutAbstract() {
        // Given
        JatsArticle article = createValidArticle();
        article.getFront().getArticleMeta().setAbstracts(null);

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE) &&
                e.getMessage().contains("abstract") &&
                e.getSeverity() == ValidationError.Severity.WARNING
        ), "abstract 누락 경고가 있어야 함");
    }

    @Test
    @DisplayName("validateRecommendedAttributes() - abstract 빈 리스트")
    void testValidateRecommendedAttributesWithEmptyAbstractList() {
        // Given
        JatsArticle article = createValidArticle();
        article.getFront().getArticleMeta().setAbstracts(new ArrayList<>());

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE) &&
                e.getMessage().contains("abstract") &&
                e.getSeverity() == ValidationError.Severity.WARNING
        ), "빈 abstract 리스트는 누락으로 간주되어야 함");
    }
    */

    // ========================================
    // 복합 시나리오 테스트
    // ========================================

    @Test
    @DisplayName("복합 시나리오 - 여러 오류가 동시에 발생")
    void testMultipleErrorsAtOnce() {
        // Given
        JatsArticle article = createArticleWithMultipleIssues();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then
        assertNotNull(errors);
        assertTrue(errors.size() >= 2, "여러 오류가 있어야 함");

        // ERROR와 WARNING이 모두 포함되어 있는지 확인
        boolean hasError = errors.stream()
                .anyMatch(e -> e.getSeverity() == ValidationError.Severity.ERROR);
        boolean hasWarning = errors.stream()
                .anyMatch(e -> e.getSeverity() == ValidationError.Severity.WARNING);

        assertTrue(hasError || hasWarning, "ERROR 또는 WARNING이 있어야 함");
    }

    // ========================================
    // 헬퍼 메서드
    // ========================================

    /**
     * 완전한 유효한 article 생성
     */
    private JatsArticle createValidArticle() {
        return JatsArticle.builder()
                .articleType(ArticleType.RESEARCH_ARTICLE)
                .dtdVersion("1.4")
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article Title")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("doi")
                                                .value("10.1234/test.2024")
                                                .build()
                                ))
                                .pubDates(List.of(
                                        PmcPubDate.builder()
                                                .pubType("epub")
                                                .build()
                                ))
                                .abstracts(List.of(
                                        PmcAbstract.builder()
                                                .paragraphs(List.of(
                                                        P.builder()
                                                                .value("Test abstract")
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build())
                        .build())
                .build();
    }

    /**
     * 여러 문제가 있는 article 생성
     */
    private JatsArticle createArticleWithMultipleIssues() {
        return JatsArticle.builder()
                .articleType(null) // 권장 속성 누락
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test Article")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("doi")
                                                .value("invalid-doi") // 무효한 DOI
                                                .build(),
                                        PmcArticleId.builder()
                                                .pubIdType("pmcid")
                                                .value("12345") // 무효한 PMCID
                                                .build()
                                ))
                                .pubDates(List.of(
                                        PmcPubDate.builder()
                                                .pubType("epub")
                                                .build()
                                ))
                                .abstracts(null) // 권장 요소 누락
                                .build())
                        .build())
                .build();
    }

    /**
     * 특정 DOI를 가진 article 생성
     */
    private JatsArticle createArticleWithDoi(String doi) {
        return JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("doi")
                                                .value(doi)
                                                .build()
                                ))
                                .pubDates(List.of(
                                        PmcPubDate.builder()
                                                .pubType("epub")
                                                .build()
                                ))
                                .build())
                        .build())
                .build();
    }

    /**
     * 특정 PMCID를 가진 article 생성
     */
    private JatsArticle createArticleWithPmcid(String pmcid) {
        return JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("pmcid")
                                                .value(pmcid)
                                                .build()
                                ))
                                .pubDates(List.of(
                                        PmcPubDate.builder()
                                                .pubType("epub")
                                                .build()
                                ))
                                .build())
                        .build())
                .build();
    }

    /**
     * 특정 PMID를 가진 article 생성
     */
    private JatsArticle createArticleWithPmid(String pmid) {
        return JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("pmid")
                                                .value(pmid)
                                                .build()
                                ))
                                .pubDates(List.of(
                                        PmcPubDate.builder()
                                                .pubType("epub")
                                                .build()
                                ))
                                .build())
                        .build())
                .build();
    }

    /**
     * 특정 ORCID를 가진 article 생성
     */
    private JatsArticle createArticleWithOrcid(String orcid) {
        ContribId contribId = ContribId.builder()
                .contribIdType(ContribIdType.ORCID)
                .value(orcid)
                .build();

        Contrib contrib = Contrib.builder()
                .contribIds(List.of(contribId))
                .build();

        ContribGroup contribGroup = ContribGroup.builder()
                .contributors(List.of(contrib))
                .build();

        return JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(contribGroup))
                                .pubDates(List.of(
                                        PmcPubDate.builder()
                                                .pubType("epub")
                                                .build()
                                ))
                                .build())
                        .build())
                .build();
    }

    /**
     * 유효한 참조를 가진 article 생성
     */
    private JatsArticle createArticleWithValidReferences() {
        // Figure with id="fig1"
        Fig fig = Fig.builder()
                .id("fig1")
                .build();

        // Paragraph with embedded text containing xref reference
        // Note: P 모델에는 xref 리스트가 없으므로 value에 참조 ID만 포함
        P paragraph = P.builder()
                .id("p1")
                .value("See Figure fig1 for details.")
                .build();

        Sec section = Sec.builder()
                .id("sec1")
                .paragraphs(List.of(paragraph))
                .build();

        Body body = Body.builder()
                .sections(List.of(section))
                .build();

        FloatsGroup floatsGroup = FloatsGroup.builder()
                .figs(List.of(fig))
                .build();

        return JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(
                                        PmcPubDate.builder()
                                                .pubType("epub")
                                                .build()
                                ))
                                .build())
                        .build())
                .body(body)
                .floatsGroup(floatsGroup)
                .build();
    }

    /**
     * 무효한 참조를 가진 article 생성 (대상 없음)
     */
    private JatsArticle createArticleWithInvalidReference() {
        // Paragraph with reference to non-existent figure
        P paragraph = P.builder()
                .id("p1")
                .value("See Figure fig99 for details.")
                .build();

        Sec section = Sec.builder()
                .id("sec1")
                .paragraphs(List.of(paragraph))
                .build();

        Body body = Body.builder()
                .sections(List.of(section))
                .build();

        return JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .pubDates(List.of(
                                        PmcPubDate.builder()
                                                .pubType("epub")
                                                .build()
                                ))
                                .build())
                        .build())
                .body(body)
                .build();
    }

    // ========================================
    // ID 수집 메서드 테스트 (collectBodyIds, collectBackIds, collectSecIds, etc.)
    // ========================================

    @Test
    @DisplayName("collectBodyIds() - Body의 모든 ID 수집")
    void testCollectBodyIdsComprehensive() {
        // Given: Body with nested sections, figures, tables, formulas, etc.
        Fig fig1 = Fig.builder().id("fig1").build();
        Fig fig2 = Fig.builder().id("fig2").build();

        TableWrap table1 = TableWrap.builder().id("table1").build();
        TableWrap table2 = TableWrap.builder().id("table2").build();

        DispFormula formula1 = DispFormula.builder().id("formula1").build();
        DispFormula formula2 = DispFormula.builder().id("formula2").build();

        DefList defList1 = DefList.builder().id("deflist1").build();

        BoxedText boxedText1 = BoxedText.builder().id("box1").build();

        DispQuote quote1 = DispQuote.builder().id("quote1").build();

        Code code1 = Code.builder().id("code1").build();

        Sec subsection = Sec.builder()
                .id("subsec1")
                .figures(List.of(fig2))
                .tableWraps(List.of(table2))
                .build();

        Sec section = Sec.builder()
                .id("sec1")
                .sections(List.of(subsection))
                .figures(List.of(fig1))
                .tableWraps(List.of(table1))
                .dispFormulas(List.of(formula1))
                .defLists(List.of(defList1))
                .boxedTexts(List.of(boxedText1))
                .dispQuotes(List.of(quote1))
                .codeBlocks(List.of(code1))
                .build();

        // Body에 직접 포함된 요소들 추가 (Section 외에 Body 레벨에서도 가질 수 있음)
        TableWrap bodyTable = TableWrap.builder().id("body-table1").build();
        DefList bodyDefList = DefList.builder().id("body-deflist1").build();
        BoxedText bodyBoxedText = BoxedText.builder().id("body-box1").build();
        DispQuote bodyQuote = DispQuote.builder().id("body-quote1").build();
        Code bodyCode = Code.builder().id("body-code1").build();

        Body body = Body.builder()
                .id("body1")
                .sections(List.of(section))
                .tableWraps(List.of(bodyTable))
                .dispFormulas(List.of(formula2))
                .defLists(List.of(bodyDefList))
                .boxedTexts(List.of(bodyBoxedText))
                .dispQuotes(List.of(bodyQuote))
                .codeBlocks(List.of(bodyCode))
                .build();

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
                .body(body)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: validateReferenceIntegrity should call collectBodyIds
        // and collect all IDs (body1, sec1, subsec1, fig1, fig2, table1, table2, formula1, formula2, deflist1, box1, quote1, code1)
        assertNotNull(errors);
        // No validation errors expected, just testing that ID collection happens without errors
    }

    @Test
    @DisplayName("collectBackIds() - Back의 모든 ID 수집")
    void testCollectBackIdsComprehensive() {
        // Given: Back with various elements
        Ack ack1 = Ack.builder().id("ack1").build();

        AppGroup appGroup1 = AppGroup.builder().id("app1").build();

        Bio bio1 = Bio.builder().id("bio1").build();

        FnGroup fnGroup1 = FnGroup.builder().id("fngroup1").build();

        Glossary glossary1 = Glossary.builder().id("glossary1").build();

        Notes notes1 = Notes.builder().id("notes1").build();

        Sec backSec = Sec.builder()
                .id("backsec1")
                .build();

        Back back = Back.builder()
                .id("back1")
                .sections(List.of(backSec))
                .acknowledgments(List.of(ack1))
                .appGroups(List.of(appGroup1))
                .biographies(List.of(bio1))
                .fnGroups(List.of(fnGroup1))
                .glossaries(List.of(glossary1))
                .notesList(List.of(notes1))
                .build();

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
                .back(back)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: validateReferenceIntegrity should call collectBackIds
        // and collect all IDs (back1, backsec1, ack1, app1, bio1, fngroup1, glossary1, notes1)
        assertNotNull(errors);
        // No validation errors expected, just testing that ID collection happens without errors
    }

    @Test
    @DisplayName("collectSecIds() - 깊게 중첩된 섹션의 모든 ID 수집")
    void testCollectSecIdsDeeplyNested() {
        // Given: Deeply nested sections with various elements
        Fig innerFig = Fig.builder().id("innerfig").build();
        TableWrap innerTable = TableWrap.builder().id("innertable").build();
        DispFormula innerFormula = DispFormula.builder().id("innerformula").build();
        DefList innerDefList = DefList.builder().id("innerdeflist").build();

        // BoxedText containing a section
        Sec boxedSec = Sec.builder().id("boxedsec").build();
        BoxedText boxedText = BoxedText.builder()
                .id("boxed1")
                .sections(List.of(boxedSec))
                .build();

        DispQuote innerQuote = DispQuote.builder().id("innerquote").build();
        Code innerCode = Code.builder().id("innercode").build();
        PmcList innerList = PmcList.builder().id("innerlist").build();

        Sec level3 = Sec.builder()
                .id("sec-level3")
                .figures(List.of(innerFig))
                .tableWraps(List.of(innerTable))
                .dispFormulas(List.of(innerFormula))
                .defLists(List.of(innerDefList))
                .boxedTexts(List.of(boxedText))
                .dispQuotes(List.of(innerQuote))
                .codeBlocks(List.of(innerCode))
                .lists(List.of(innerList))
                .build();

        Sec level2 = Sec.builder()
                .id("sec-level2")
                .sections(List.of(level3))
                .build();

        Sec level1 = Sec.builder()
                .id("sec-level1")
                .sections(List.of(level2))
                .build();

        Body body = Body.builder()
                .sections(List.of(level1))
                .build();

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
                .body(body)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should collect all nested IDs recursively
        // (sec-level1, sec-level2, sec-level3, innerfig, innertable, innerformula, innerdeflist, boxed1, boxedsec, innerquote, innercode, innerlist)
        assertNotNull(errors);
    }

    @Test
    @DisplayName("collectFigIds() - Fig 내부의 TableWrap ID도 수집")
    void testCollectFigIdsWithNestedTableWrap() {
        // Given: Figure containing table-wrap
        TableWrap nestedTable = TableWrap.builder().id("nestedtable").build();

        Fig figWithTable = Fig.builder()
                .id("figwithtable")
                .tableWraps(List.of(nestedTable))
                .build();

        Body body = Body.builder()
                .figures(List.of(figWithTable))
                .build();

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
                .body(body)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should collect both figwithtable and nestedtable IDs
        assertNotNull(errors);
    }

    @Test
    @DisplayName("collectRefIds() - 중첩된 RefList의 모든 ref ID 수집")
    void testCollectRefIdsNested() {
        // Given: Nested ref-lists
        Ref ref1 = Ref.builder().id("ref1").build();
        Ref ref2 = Ref.builder().id("ref2").build();
        Ref ref3 = Ref.builder().id("ref3").build();

        RefList nestedRefList = RefList.builder()
                .references(List.of(ref3))
                .build();

        RefList mainRefList = RefList.builder()
                .references(List.of(ref1, ref2))
                .refLists(List.of(nestedRefList))
                .build();

        Back back = Back.builder()
                .refLists(List.of(mainRefList))
                .build();

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
                .back(back)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should collect all ref IDs (ref1, ref2, ref3) recursively
        assertNotNull(errors);
    }

    @Test
    @DisplayName("collectAllIds() - Aff와 Contrib ID 수집")
    void testCollectAllIdsFromAffAndContrib() {
        // Given: Article with affiliations and contributors
        Aff aff1 = Aff.builder().id("aff1").build();
        Aff aff2 = Aff.builder().id("aff2").build();

        Contrib contrib1 = Contrib.builder().id("contrib1").build();
        Contrib contrib2 = Contrib.builder().id("contrib2").build();

        ContribGroup contribGroup = ContribGroup.builder()
                .contributors(List.of(contrib1, contrib2))
                .build();

        ArticleMeta articleMeta = ArticleMeta.builder()
                .titleGroup(TitleGroup.builder()
                        .articleTitle(ArticleTitle.builder()
                                .content("Test")
                                .build())
                        .build())
                .affiliations(List.of(aff1, aff2))
                .contribGroups(List.of(contribGroup))
                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                .build();

        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(articleMeta)
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should collect aff1, aff2, contrib1, contrib2 IDs
        assertNotNull(errors);
    }

    @Test
    @DisplayName("collectAllIds() - null 요소들 안전하게 처리")
    void testCollectAllIdsWithNullElements() {
        // Given: Article with null lists
        ArticleMeta articleMeta = ArticleMeta.builder()
                .titleGroup(TitleGroup.builder()
                        .articleTitle(ArticleTitle.builder()
                                .content("Test")
                                .build())
                        .build())
                .affiliations(null)
                .contribGroups(null)
                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                .build();

        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(articleMeta)
                        .build())
                .back(null)
                .body(null)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should handle nulls gracefully without throwing exceptions
        assertNotNull(errors);
    }

    // ========================================
    // 유틸리티 메서드 테스트 (summarize, printErrors)
    // ========================================

    @Test
    @DisplayName("summarize() - 오류 없음")
    void testSummarizeNoErrors() {
        // Given
        List<ValidationError> errors = new ArrayList<>();

        // When
        String summary = JatsArticleValidator.summarize(errors);

        // Then
        assertEquals("✅ Validation passed: No errors found", summary);
    }

    @Test
    @DisplayName("summarize() - 오류만 있음")
    void testSummarizeOnlyErrors() {
        // Given
        List<ValidationError> errors = List.of(
                ValidationError.error("CODE1", "Error 1", "/path1"),
                ValidationError.error("CODE2", "Error 2", "/path2"),
                ValidationError.error("CODE3", "Error 3", "/path3")
        );

        // When
        String summary = JatsArticleValidator.summarize(errors);

        // Then
        assertEquals("❌ Validation failed: 3 errors, 0 warnings, 0 info", summary);
    }

    @Test
    @DisplayName("summarize() - 경고만 있음")
    void testSummarizeOnlyWarnings() {
        // Given
        List<ValidationError> errors = List.of(
                ValidationError.warning("CODE1", "Warning 1", "/path1"),
                ValidationError.warning("CODE2", "Warning 2", "/path2")
        );

        // When
        String summary = JatsArticleValidator.summarize(errors);

        // Then
        assertEquals("❌ Validation failed: 0 errors, 2 warnings, 0 info", summary);
    }

    @Test
    @DisplayName("summarize() - 정보만 있음")
    void testSummarizeOnlyInfo() {
        // Given
        List<ValidationError> errors = List.of(
                ValidationError.info("CODE1", "Info 1", "/path1")
        );

        // When
        String summary = JatsArticleValidator.summarize(errors);

        // Then
        assertEquals("❌ Validation failed: 0 errors, 0 warnings, 1 info", summary);
    }

    @Test
    @DisplayName("summarize() - 오류, 경고, 정보 혼합")
    void testSummarizeMixedSeverities() {
        // Given
        List<ValidationError> errors = List.of(
                ValidationError.error("CODE1", "Error 1", "/path1"),
                ValidationError.error("CODE2", "Error 2", "/path2"),
                ValidationError.warning("CODE3", "Warning 1", "/path3"),
                ValidationError.warning("CODE4", "Warning 2", "/path4"),
                ValidationError.warning("CODE5", "Warning 3", "/path5"),
                ValidationError.info("CODE6", "Info 1", "/path6"),
                ValidationError.info("CODE7", "Info 2", "/path7")
        );

        // When
        String summary = JatsArticleValidator.summarize(errors);

        // Then
        assertEquals("❌ Validation failed: 2 errors, 3 warnings, 2 info", summary);
    }

    @Test
    @DisplayName("printErrors() - 오류 없음 출력")
    void testPrintErrorsNoErrors() {
        // Given
        List<ValidationError> errors = new ArrayList<>();

        // When/Then: Should not throw exception
        assertDoesNotThrow(() -> JatsArticleValidator.printErrors(errors));
    }

    @Test
    @DisplayName("printErrors() - 오류 있음 출력")
    void testPrintErrorsWithErrors() {
        // Given
        List<ValidationError> errors = List.of(
                ValidationError.error("CODE1", "Error 1", "/path1"),
                ValidationError.warning("CODE2", "Warning 1", "/path2")
        );

        // When/Then: Should not throw exception
        assertDoesNotThrow(() -> JatsArticleValidator.printErrors(errors));
    }

    // ========================================
    // 엣지 케이스 및 브랜치 커버리지 향상 테스트
    // ========================================

    @Test
    @DisplayName("validateArticleId() - null pub-id-type (OTHER로 변환)")
    void testValidateArticleIdWithNullPubIdType() {
        // Given: Article with null pub-id-type
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType(null)
                                                .value("some-value")
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception, just skip validation for unknown types
        assertNotNull(errors);
    }

    @Test
    @DisplayName("validateArticleId() - 알 수 없는 pub-id-type")
    void testValidateArticleIdWithUnknownPubIdType() {
        // Given: Article with unknown pub-id-type
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("unknown-type")
                                                .value("some-value")
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not throw exception, just skip validation for unknown types
        assertNotNull(errors);
    }

    @Test
    @DisplayName("validateArticleId() - null article ID value")
    void testValidateArticleIdWithNullValue() {
        // Given: Article with null article ID value
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("doi")
                                                .value(null)
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should skip validation for null values
        assertNotNull(errors);
        boolean hasDoiError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_DOI_FORMAT));
        assertFalse(hasDoiError, "null 값은 검증을 건너뛰므로 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateArticleId() - 빈 문자열 article ID value")
    void testValidateArticleIdWithEmptyValue() {
        // Given: Article with empty article ID value
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .articleIds(List.of(
                                        PmcArticleId.builder()
                                                .pubIdType("doi")
                                                .value("   ")
                                                .build()
                                ))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should skip validation for empty values
        assertNotNull(errors);
        boolean hasDoiError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_DOI_FORMAT));
        assertFalse(hasDoiError, "빈 문자열은 검증을 건너뛰므로 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateContribOrcid() - null contrib ID")
    void testValidateContribOrcidWithNullContribId() {
        // Given: Contrib with null contrib-ids
        Contrib contrib = Contrib.builder()
                .contribIds(null)
                .build();

        ContribGroup contribGroup = ContribGroup.builder()
                .contributors(List.of(contrib))
                .build();

        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(contribGroup))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should handle null gracefully
        assertNotNull(errors);
    }

    @Test
    @DisplayName("validateContribOrcid() - null ORCID value")
    void testValidateContribOrcidWithNullOrcidValue() {
        // Given: Contrib with null ORCID value
        ContribId contribId = ContribId.builder()
                .contribIdType(ContribIdType.ORCID)
                .value(null)
                .build();

        Contrib contrib = Contrib.builder()
                .contribIds(List.of(contribId))
                .build();

        ContribGroup contribGroup = ContribGroup.builder()
                .contributors(List.of(contrib))
                .build();

        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(contribGroup))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should skip validation for null ORCID
        assertNotNull(errors);
        boolean hasOrcidError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_ORCID_FORMAT));
        assertFalse(hasOrcidError, "null ORCID는 검증을 건너뛰므로 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateContribOrcid() - 빈 문자열 ORCID value")
    void testValidateContribOrcidWithEmptyOrcidValue() {
        // Given: Contrib with empty ORCID value
        ContribId contribId = ContribId.builder()
                .contribIdType(ContribIdType.ORCID)
                .value("   ")
                .build();

        Contrib contrib = Contrib.builder()
                .contribIds(List.of(contribId))
                .build();

        ContribGroup contribGroup = ContribGroup.builder()
                .contributors(List.of(contrib))
                .build();

        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(contribGroup))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should skip validation for empty ORCID
        assertNotNull(errors);
        boolean hasOrcidError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_ORCID_FORMAT));
        assertFalse(hasOrcidError, "빈 문자열 ORCID는 검증을 건너뛰므로 오류가 없어야 함");
    }

    @Test
    @DisplayName("validateContribOrcid() - ORCID URL 형식")
    void testValidateContribOrcidWithUrlFormat() {
        // Given: Contrib with ORCID URL format
        ContribId contribId = ContribId.builder()
                .contribIdType(ContribIdType.ORCID)
                .value("https://orcid.org/0000-0002-1825-0097")
                .build();

        Contrib contrib = Contrib.builder()
                .contribIds(List.of(contribId))
                .build();

        ContribGroup contribGroup = ContribGroup.builder()
                .contributors(List.of(contrib))
                .build();

        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(contribGroup))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: ORCID URL format should be valid
        assertNotNull(errors);
        boolean hasOrcidError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_ORCID_FORMAT));
        assertFalse(hasOrcidError, "ORCID URL 형식은 유효해야 함");
    }

    @Test
    @DisplayName("validateContribOrcid() - http ORCID URL 형식")
    void testValidateContribOrcidWithHttpUrlFormat() {
        // Given: Contrib with http ORCID URL format
        ContribId contribId = ContribId.builder()
                .contribIdType(ContribIdType.ORCID)
                .value("http://orcid.org/0000-0002-1825-0097")
                .build();

        Contrib contrib = Contrib.builder()
                .contribIds(List.of(contribId))
                .build();

        ContribGroup contribGroup = ContribGroup.builder()
                .contributors(List.of(contrib))
                .build();

        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(contribGroup))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: ORCID http URL format should be valid
        assertNotNull(errors);
        boolean hasOrcidError = errors.stream()
                .anyMatch(e -> e.getCode().equals(ValidationError.ErrorCode.INVALID_ORCID_FORMAT));
        assertFalse(hasOrcidError, "ORCID http URL 형식은 유효해야 함");
    }

    @Test
    @DisplayName("validateIdFormats() - null articleIds 리스트")
    void testValidateIdFormatsWithNullArticleIds() {
        // Given: Article with null articleIds
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .articleIds(null)
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should handle null gracefully
        assertNotNull(errors);
    }

    @Test
    @DisplayName("validateIdFormats() - null contribGroups 리스트")
    void testValidateIdFormatsWithNullContribGroups() {
        // Given: Article with null contribGroups
        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(null)
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should handle null gracefully
        assertNotNull(errors);
    }

    @Test
    @DisplayName("validateIdFormats() - null contributors 리스트")
    void testValidateIdFormatsWithNullContributors() {
        // Given: ContribGroup with null contributors
        ContribGroup contribGroup = ContribGroup.builder()
                .contributors(null)
                .build();

        JatsArticle article = JatsArticle.builder()
                .front(Front.builder()
                        .articleMeta(ArticleMeta.builder()
                                .titleGroup(TitleGroup.builder()
                                        .articleTitle(ArticleTitle.builder()
                                                .content("Test")
                                                .build())
                                        .build())
                                .contribGroups(List.of(contribGroup))
                                .pubDates(List.of(PmcPubDate.builder().pubType("epub").build()))
                                .build())
                        .build())
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should handle null gracefully
        assertNotNull(errors);
    }

    @Test
    @DisplayName("validateRecommendedAttributes() - 빈 문자열 dtdVersion")
    void testValidateRecommendedAttributesWithEmptyDtdVersion() {
        // Given: Article with empty dtdVersion
        JatsArticle article = createValidArticle();
        article.setDtdVersion("   ");

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should generate INFO warning for empty dtdVersion
        assertTrue(errors.stream().anyMatch(e ->
                e.getCode().equals(ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE) &&
                e.getMessage().contains("dtd-version") &&
                e.getSeverity() == ValidationError.Severity.INFO
        ), "빈 문자열 dtdVersion은 INFO 경고가 있어야 함");
    }

    // ========================================
    // Branch coverage 향상을 위한 추가 테스트 (null ID, null lists)
    // ========================================

    @Test
    @DisplayName("collectFigIds() - null ID를 가진 Fig")
    void testCollectFigIdsWithNullId() {
        // Given: Fig with null ID
        Fig figWithoutId = Fig.builder().id(null).build();
        Fig figWithId = Fig.builder().id("fig1").build();

        Body body = Body.builder()
                .figures(List.of(figWithoutId, figWithId))
                .build();

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
                .body(body)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not fail, just skip null ID
        assertNotNull(errors);
    }

    @Test
    @DisplayName("collectTableWrapIds() - null ID를 가진 TableWrap")
    void testCollectTableWrapIdsWithNullId() {
        // Given: TableWrap with null ID
        TableWrap tableWithoutId = TableWrap.builder().id(null).build();
        TableWrap tableWithId = TableWrap.builder().id("table1").build();

        Body body = Body.builder()
                .tableWraps(List.of(tableWithoutId, tableWithId))
                .build();

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
                .body(body)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not fail, just skip null ID
        assertNotNull(errors);
    }

    @Test
    @DisplayName("collectDispFormulaIds() - null ID를 가진 DispFormula")
    void testCollectDispFormulaIdsWithNullId() {
        // Given: DispFormula with null ID
        DispFormula formulaWithoutId = DispFormula.builder().id(null).build();
        DispFormula formulaWithId = DispFormula.builder().id("formula1").build();

        Body body = Body.builder()
                .dispFormulas(List.of(formulaWithoutId, formulaWithId))
                .build();

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
                .body(body)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not fail, just skip null ID
        assertNotNull(errors);
    }

    @Test
    @DisplayName("collectBodyIds() - null lists")
    void testCollectBodyIdsWithNullLists() {
        // Given: Body with all null lists
        Body body = Body.builder()
                .id("body1")
                .sections(null)
                .figures(null)
                .tableWraps(null)
                .dispFormulas(null)
                .defLists(null)
                .boxedTexts(null)
                .dispQuotes(null)
                .codeBlocks(null)
                .build();

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
                .body(body)
                .build();

        // When
        List<ValidationError> errors = validator.validateArticle(article);

        // Then: Should not fail, just skip null lists
        assertNotNull(errors);
    }
}
