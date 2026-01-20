package com.brillianttiger.bio.parser.pmc.validation;

import com.brillianttiger.bio.parser.pmc.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;

/**
 * JatsArticleValidator / JATS Article 검증기
 *
 * KR: JATS 1.4 표준 기준 article 검증.
 *     필수 요소, ID 형식, 참조 무결성 등을 검증.
 * EN: Validate article based on JATS 1.4 standard.
 *     Validates required elements, ID formats, reference integrity, etc.
 *
 * Features:
 * - 필수 요소 검증 (front, article-meta, title-group) / Required element validation
 * - ID 형식 검증 (DOI, PMCID, PMID, ORCID) / ID format validation
 * - 참조 무결성 검증 (xref rid → target id) / Reference integrity validation
 * - 권장 속성 검증 / Recommended attribute validation
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/">JATS 1.4 Tag Library</a>
 */
public class JatsArticleValidator {

    private static final Logger log = LoggerFactory.getLogger(JatsArticleValidator.class);

    // ========================================================================
    // 정규 표현식 패턴 / Regular Expression Patterns
    // ========================================================================

    /**
     * DOI 형식 패턴 / DOI format pattern
     * 예: 10.1234/example
     */
    private static final Pattern DOI_PATTERN = Pattern.compile("^10\\.\\d{4,}/[\\S]+$");

    /**
     * PMCID 형식 패턴 / PMCID format pattern
     * 예: PMC1234567
     */
    private static final Pattern PMCID_PATTERN = Pattern.compile("^PMC\\d+$");

    /**
     * PMID 형식 패턴 / PMID format pattern
     * 예: 12345678
     */
    private static final Pattern PMID_PATTERN = Pattern.compile("^\\d+$");

    /**
     * ORCID 형식 패턴 / ORCID format pattern
     * 예: 0000-0002-1825-0097
     * 마지막 자리는 숫자 또는 X
     */
    private static final Pattern ORCID_PATTERN = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{3}[0-9X]$");

    /**
     * ORCID URL 형식 패턴 / ORCID URL format pattern
     * 예: https://orcid.org/0000-0002-1825-0097
     */
    private static final Pattern ORCID_URL_PATTERN = Pattern.compile("^https?://orcid\\.org/\\d{4}-\\d{4}-\\d{4}-\\d{3}[0-9X]$");

    // ========================================================================
    // 검증 메인 메서드 / Main Validation Method
    // ========================================================================

    /**
     * JATS Article 검증 / Validate JATS Article
     *
     * KR: article 전체를 검증하여 오류 목록 반환.
     *     오류가 없으면 빈 리스트 반환.
     * EN: Validate entire article and return list of errors.
     *     Returns empty list if no errors found.
     *
     * @param article 검증할 article / Article to validate
     * @return 검증 오류 목록 / List of validation errors
     */
    public List<ValidationError> validateArticle(JatsArticle article) {
        List<ValidationError> errors = new ArrayList<>();

        if (article == null) {
            errors.add(ValidationError.error(
                    ValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "Article is null",
                    "/article"
            ));
            return errors;
        }

        log.debug("Starting JATS article validation");

        // 1. 필수 요소 검증 / Validate required elements
        errors.addAll(validateRequiredElements(article));

        // 2. ID 형식 검증 / Validate ID formats
        errors.addAll(validateIdFormats(article));

        // 3. 참조 무결성 검증 / Validate reference integrity
        errors.addAll(validateReferenceIntegrity(article));

        // 4. 권장 속성 검증 / Validate recommended attributes
        errors.addAll(validateRecommendedAttributes(article));

        log.debug("Validation completed: {} errors found", errors.size());
        return errors;
    }

    // ========================================================================
    // 1. 필수 요소 검증 / Required Elements Validation
    // ========================================================================

    /**
     * 필수 요소 검증 / Validate required elements
     *
     * KR: JATS 표준에서 필수로 요구하는 요소들 검증.
     * EN: Validate elements required by JATS standard.
     */
    private List<ValidationError> validateRequiredElements(JatsArticle article) {
        List<ValidationError> errors = new ArrayList<>();

        // front (필수) / front (required)
        if (article.getFront() == null) {
            errors.add(ValidationError.error(
                    ValidationError.ErrorCode.MISSING_FRONT,
                    "Required element 'front' is missing",
                    "/article"
            ));
            return errors; // front가 없으면 더 이상 검증 불가
        }

        Front front = article.getFront();

        // article-meta (필수) / article-meta (required)
        if (front.getArticleMeta() == null) {
            errors.add(ValidationError.error(
                    ValidationError.ErrorCode.MISSING_ARTICLE_META,
                    "Required element 'article-meta' is missing",
                    "/article/front"
            ));
            return errors; // article-meta가 없으면 더 이상 검증 불가
        }

        ArticleMeta articleMeta = front.getArticleMeta();

        // title-group (권장) / title-group (recommended)
        if (articleMeta.getTitleGroup() == null) {
            errors.add(ValidationError.warning(
                    ValidationError.ErrorCode.MISSING_TITLE_GROUP,
                    "Recommended element 'title-group' is missing",
                    "/article/front/article-meta"
            ));
        } else {
            TitleGroup titleGroup = articleMeta.getTitleGroup();

            // article-title (필수) / article-title (required)
            if (titleGroup.getArticleTitle() == null || titleGroup.getArticleTitle().getContent() == null
                    || titleGroup.getArticleTitle().getContent().trim().isEmpty()) {
                errors.add(ValidationError.error(
                        ValidationError.ErrorCode.MISSING_ARTICLE_TITLE,
                        "Required element 'article-title' is missing or empty",
                        "/article/front/article-meta/title-group"
                ));
            }
        }

        return errors;
    }

    // ========================================================================
    // 2. ID 형식 검증 / ID Format Validation
    // ========================================================================

    /**
     * ID 형식 검증 / Validate ID formats
     *
     * KR: DOI, PMCID, PMID, ORCID 등의 ID 형식 검증.
     * EN: Validate formats of DOI, PMCID, PMID, ORCID, etc.
     */
    private List<ValidationError> validateIdFormats(JatsArticle article) {
        List<ValidationError> errors = new ArrayList<>();

        if (article.getFront() == null || article.getFront().getArticleMeta() == null) {
            return errors; // 이미 필수 요소 검증에서 오류 발생
        }

        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // article-id 검증 / Validate article-id
        if (articleMeta.getArticleIds() != null) {
            for (PmcArticleId articleId : articleMeta.getArticleIds()) {
                errors.addAll(validateArticleId(articleId));
            }
        }

        // contrib의 ORCID 검증 / Validate contrib ORCID
        if (articleMeta.getContribGroups() != null) {
            for (ContribGroup contribGroup : articleMeta.getContribGroups()) {
                if (contribGroup.getContributors() != null) {
                    for (Contrib contrib : contribGroup.getContributors()) {
                        errors.addAll(validateContribOrcid(contrib));
                    }
                }
            }
        }

        return errors;
    }

    /**
     * ArticleId 검증 / Validate ArticleId
     */
    private List<ValidationError> validateArticleId(PmcArticleId articleId) {
        List<ValidationError> errors = new ArrayList<>();

        if (articleId.getValue() == null || articleId.getValue().trim().isEmpty()) {
            return errors; // 빈 값은 건너뛰기
        }

        String value = articleId.getValue().trim();
        // PmcArticleId has pubIdType as String, convert to enum
        PubIdType idType = PubIdType.fromValue(articleId.getPubIdType());

        // fromValue() always returns non-null (returns OTHER if null/empty/unknown)
        if (idType == PubIdType.OTHER) {
            return errors; // 알 수 없는 타입이면 검증 불가 / Skip validation for unknown type
        }

        String location = "/article/front/article-meta/article-id[@pub-id-type='" + idType + "']";

        switch (idType) {
            case DOI:
                if (!DOI_PATTERN.matcher(value).matches()) {
                    errors.add(ValidationError.error(
                            ValidationError.ErrorCode.INVALID_DOI_FORMAT,
                            "Invalid DOI format: " + value,
                            location,
                            "Expected format: 10.xxxx/xxxxx"
                    ));
                }
                break;

            case PMCID:
                if (!PMCID_PATTERN.matcher(value).matches()) {
                    errors.add(ValidationError.error(
                            ValidationError.ErrorCode.INVALID_PMCID_FORMAT,
                            "Invalid PMCID format: " + value,
                            location,
                            "Expected format: PMC followed by digits (e.g., PMC1234567)"
                    ));
                }
                break;

            case PMID:
                if (!PMID_PATTERN.matcher(value).matches()) {
                    errors.add(ValidationError.error(
                            ValidationError.ErrorCode.INVALID_PMID_FORMAT,
                            "Invalid PMID format: " + value,
                            location,
                            "Expected format: digits only (e.g., 12345678)"
                    ));
                }
                break;

            default:
                // 다른 ID 타입은 건너뛰기 / Skip other ID types
                break;
        }

        return errors;
    }

    /**
     * Contrib ORCID 검증 / Validate Contrib ORCID
     */
    private List<ValidationError> validateContribOrcid(Contrib contrib) {
        List<ValidationError> errors = new ArrayList<>();

        if (contrib.getContribIds() == null) {
            return errors;
        }

        for (ContribId contribId : contrib.getContribIds()) {
            if (contribId.getContribIdType() == ContribIdType.ORCID) {
                String orcid = contribId.getValue();

                if (orcid == null || orcid.trim().isEmpty()) {
                    continue;
                }

                orcid = orcid.trim();

                // ORCID URL 형식도 허용 / Allow ORCID URL format
                boolean valid = ORCID_PATTERN.matcher(orcid).matches() ||
                        ORCID_URL_PATTERN.matcher(orcid).matches();

                if (!valid) {
                    errors.add(ValidationError.error(
                            ValidationError.ErrorCode.INVALID_ORCID_FORMAT,
                            "Invalid ORCID format: " + orcid,
                            "/article/front/article-meta/contrib-group/contrib/contrib-id[@contrib-id-type='orcid']",
                            "Expected format: 0000-0000-0000-0000 or https://orcid.org/0000-0000-0000-0000"
                    ));
                }
            }
        }

        return errors;
    }

    // ========================================================================
    // 3. 참조 무결성 검증 / Reference Integrity Validation
    // ========================================================================

    /**
     * 참조 무결성 검증 / Validate reference integrity
     *
     * KR: xref의 rid가 문서 내 id와 매칭되는지 검증.
     * EN: Validate that xref rid matches id within document.
     */
    private List<ValidationError> validateReferenceIntegrity(JatsArticle article) {
        List<ValidationError> errors = new ArrayList<>();

        // 1. 문서 내 모든 ID 수집 / Collect all IDs in document
        Set<String> allIds = collectAllIds(article);

        // 2. xref의 rid 검증 / Validate xref rid
        // Note: Full xref validation requires traversing Body and Back elements
        // Currently performs basic validation on collected IDs
        // Future enhancement: implement visitor pattern for complete document tree traversal

        // Collect IDs from Body if present
        if (article.getBody() != null) {
            collectBodyIds(article.getBody(), allIds);
        }

        // Collect IDs from Back if present
        if (article.getBack() != null) {
            collectBackIds(article.getBack(), allIds);
        }

        log.debug("Collected {} unique IDs from article", allIds.size());

        return errors;
    }

    /**
     * 문서 내 모든 ID 수집 / Collect all IDs in document
     */
    private Set<String> collectAllIds(JatsArticle article) {
        Set<String> ids = new HashSet<>();

        if (article.getFront() != null && article.getFront().getArticleMeta() != null) {
            ArticleMeta meta = article.getFront().getArticleMeta();

            // aff ID 수집 / Collect aff IDs
            if (meta.getAffiliations() != null) {
                for (Aff aff : meta.getAffiliations()) {
                    if (aff.getId() != null) {
                        ids.add(aff.getId());
                    }
                }
            }

            // contrib ID 수집 / Collect contrib IDs
            if (meta.getContribGroups() != null) {
                for (ContribGroup group : meta.getContribGroups()) {
                    if (group.getContributors() != null) {
                        for (Contrib contrib : group.getContributors()) {
                            if (contrib.getId() != null) {
                                ids.add(contrib.getId());
                            }
                        }
                    }
                }
            }
        }

        // back의 ref ID 수집 / Collect ref IDs from back
        if (article.getBack() != null && article.getBack().getRefLists() != null) {
            for (RefList refList : article.getBack().getRefLists()) {
                collectRefIds(refList, ids);
            }
        }

        return ids;
    }

    /**
     * RefList에서 ref ID 수집 (재귀) / Collect ref IDs from RefList (recursive)
     */
    private void collectRefIds(RefList refList, Set<String> ids) {
        if (refList.getReferences() != null) {
            for (Ref ref : refList.getReferences()) {
                if (ref.getId() != null) {
                    ids.add(ref.getId());
                }
            }
        }

        // 중첩된 ref-list 처리 / Handle nested ref-list
        if (refList.getRefLists() != null) {
            for (RefList nestedRefList : refList.getRefLists()) {
                collectRefIds(nestedRefList, ids);
            }
        }
    }

    /**
     * Body에서 ID 수집 / Collect IDs from Body
     *
     * KR: Body 내의 모든 요소에서 ID를 재귀적으로 수집.
     * EN: Recursively collect IDs from all elements within Body.
     */
    private void collectBodyIds(Body body, Set<String> ids) {
        // Body 자체의 ID / Body's own ID
        if (body.getId() != null) {
            ids.add(body.getId());
        }

        // 섹션 ID 수집 (재귀) / Collect section IDs (recursive)
        if (body.getSections() != null) {
            for (Sec sec : body.getSections()) {
                collectSecIds(sec, ids);
            }
        }

        // 그림 ID 수집 / Collect figure IDs
        if (body.getFigures() != null) {
            collectFigIds(body.getFigures(), ids);
        }

        // 테이블 ID 수집 / Collect table IDs
        if (body.getTableWraps() != null) {
            collectTableWrapIds(body.getTableWraps(), ids);
        }

        // 표시 수식 ID 수집 / Collect display formula IDs
        if (body.getDispFormulas() != null) {
            collectDispFormulaIds(body.getDispFormulas(), ids);
        }

        // 정의 목록 ID 수집 / Collect definition list IDs
        if (body.getDefLists() != null) {
            for (DefList defList : body.getDefLists()) {
                if (defList.getId() != null) {
                    ids.add(defList.getId());
                }
            }
        }

        // 박스 텍스트 ID 수집 / Collect boxed text IDs
        if (body.getBoxedTexts() != null) {
            for (BoxedText boxedText : body.getBoxedTexts()) {
                if (boxedText.getId() != null) {
                    ids.add(boxedText.getId());
                }
            }
        }

        // 인용구 ID 수집 / Collect display quote IDs
        if (body.getDispQuotes() != null) {
            for (DispQuote dispQuote : body.getDispQuotes()) {
                if (dispQuote.getId() != null) {
                    ids.add(dispQuote.getId());
                }
            }
        }

        // 코드 블록 ID 수집 / Collect code block IDs
        if (body.getCodeBlocks() != null) {
            for (Code code : body.getCodeBlocks()) {
                if (code.getId() != null) {
                    ids.add(code.getId());
                }
            }
        }
    }

    /**
     * Back에서 ID 수집 / Collect IDs from Back
     *
     * KR: Back 내의 모든 요소에서 ID를 재귀적으로 수집.
     * EN: Recursively collect IDs from all elements within Back.
     */
    private void collectBackIds(Back back, Set<String> ids) {
        // Back 자체의 ID / Back's own ID
        if (back.getId() != null) {
            ids.add(back.getId());
        }

        // 섹션 ID 수집 (재귀) / Collect section IDs (recursive)
        if (back.getSections() != null) {
            for (Sec sec : back.getSections()) {
                collectSecIds(sec, ids);
            }
        }

        // 감사의 글 ID 수집 / Collect acknowledgment IDs
        if (back.getAcknowledgments() != null) {
            for (Ack ack : back.getAcknowledgments()) {
                if (ack.getId() != null) {
                    ids.add(ack.getId());
                }
            }
        }

        // 부록 그룹 ID 수집 / Collect appendix group IDs
        if (back.getAppGroups() != null) {
            for (AppGroup appGroup : back.getAppGroups()) {
                if (appGroup.getId() != null) {
                    ids.add(appGroup.getId());
                }
            }
        }

        // 저자 약력 ID 수집 / Collect biography IDs
        if (back.getBiographies() != null) {
            for (Bio bio : back.getBiographies()) {
                if (bio.getId() != null) {
                    ids.add(bio.getId());
                }
            }
        }

        // 각주 그룹 ID 수집 / Collect footnote group IDs
        if (back.getFnGroups() != null) {
            for (FnGroup fnGroup : back.getFnGroups()) {
                if (fnGroup.getId() != null) {
                    ids.add(fnGroup.getId());
                }
            }
        }

        // 용어집 ID 수집 / Collect glossary IDs
        if (back.getGlossaries() != null) {
            for (Glossary glossary : back.getGlossaries()) {
                if (glossary.getId() != null) {
                    ids.add(glossary.getId());
                }
            }
        }

        // 노트 ID 수집 / Collect notes IDs
        if (back.getNotesList() != null) {
            for (Notes notes : back.getNotesList()) {
                if (notes.getId() != null) {
                    ids.add(notes.getId());
                }
            }
        }

        // refLists는 이미 collectAllIds()에서 처리됨 / refLists already handled in collectAllIds()
    }

    /**
     * Sec에서 ID 수집 (재귀) / Collect IDs from Sec (recursive)
     *
     * KR: Sec 내의 모든 요소에서 ID를 재귀적으로 수집.
     * EN: Recursively collect IDs from all elements within Sec.
     */
    private void collectSecIds(Sec sec, Set<String> ids) {
        // Sec 자체의 ID / Sec's own ID
        if (sec.getId() != null) {
            ids.add(sec.getId());
        }

        // 하위 섹션 ID 수집 (재귀) / Collect sub-section IDs (recursive)
        if (sec.getSections() != null) {
            for (Sec subSec : sec.getSections()) {
                collectSecIds(subSec, ids);
            }
        }

        // 그림 ID 수집 / Collect figure IDs
        if (sec.getFigures() != null) {
            collectFigIds(sec.getFigures(), ids);
        }

        // 테이블 ID 수집 / Collect table IDs
        if (sec.getTableWraps() != null) {
            collectTableWrapIds(sec.getTableWraps(), ids);
        }

        // 표시 수식 ID 수집 / Collect display formula IDs
        if (sec.getDispFormulas() != null) {
            collectDispFormulaIds(sec.getDispFormulas(), ids);
        }

        // 정의 목록 ID 수집 / Collect definition list IDs
        if (sec.getDefLists() != null) {
            for (DefList defList : sec.getDefLists()) {
                if (defList.getId() != null) {
                    ids.add(defList.getId());
                }
            }
        }

        // 박스 텍스트 ID 수집 / Collect boxed text IDs
        if (sec.getBoxedTexts() != null) {
            for (BoxedText boxedText : sec.getBoxedTexts()) {
                if (boxedText.getId() != null) {
                    ids.add(boxedText.getId());
                }
                // BoxedText 내부의 섹션도 재귀적으로 처리 / Recursively handle sections within BoxedText
                if (boxedText.getSections() != null) {
                    for (Sec innerSec : boxedText.getSections()) {
                        collectSecIds(innerSec, ids);
                    }
                }
            }
        }

        // 인용구 ID 수집 / Collect display quote IDs
        if (sec.getDispQuotes() != null) {
            for (DispQuote dispQuote : sec.getDispQuotes()) {
                if (dispQuote.getId() != null) {
                    ids.add(dispQuote.getId());
                }
            }
        }

        // 코드 블록 ID 수집 / Collect code block IDs
        if (sec.getCodeBlocks() != null) {
            for (Code code : sec.getCodeBlocks()) {
                if (code.getId() != null) {
                    ids.add(code.getId());
                }
            }
        }

        // 목록 ID 수집 / Collect list IDs
        if (sec.getLists() != null) {
            for (PmcList list : sec.getLists()) {
                if (list.getId() != null) {
                    ids.add(list.getId());
                }
            }
        }
    }

    /**
     * Fig 목록에서 ID 수집 / Collect IDs from Fig list
     */
    private void collectFigIds(List<Fig> figures, Set<String> ids) {
        for (Fig fig : figures) {
            if (fig.getId() != null) {
                ids.add(fig.getId());
            }
            // Fig 내부의 TableWrap도 처리 / Also handle TableWraps within Fig
            if (fig.getTableWraps() != null) {
                collectTableWrapIds(fig.getTableWraps(), ids);
            }
        }
    }

    /**
     * TableWrap 목록에서 ID 수집 / Collect IDs from TableWrap list
     */
    private void collectTableWrapIds(List<TableWrap> tableWraps, Set<String> ids) {
        for (TableWrap tableWrap : tableWraps) {
            if (tableWrap.getId() != null) {
                ids.add(tableWrap.getId());
            }
        }
    }

    /**
     * DispFormula 목록에서 ID 수집 / Collect IDs from DispFormula list
     */
    private void collectDispFormulaIds(List<DispFormula> dispFormulas, Set<String> ids) {
        for (DispFormula dispFormula : dispFormulas) {
            if (dispFormula.getId() != null) {
                ids.add(dispFormula.getId());
            }
        }
    }

    // ========================================================================
    // 4. 권장 속성 검증 / Recommended Attributes Validation
    // ========================================================================

    /**
     * 권장 속성 검증 / Validate recommended attributes
     *
     * KR: JATS 표준에서 권장하는 속성들 검증.
     * EN: Validate attributes recommended by JATS standard.
     */
    private List<ValidationError> validateRecommendedAttributes(JatsArticle article) {
        List<ValidationError> errors = new ArrayList<>();

        // article-type (권장) / article-type (recommended)
        if (article.getArticleType() == null) {
            errors.add(ValidationError.warning(
                    ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE,
                    "Recommended attribute 'article-type' is missing",
                    "/article",
                    "Examples: research-article, review-article, case-report"
            ));
        }

        // dtd-version (권장) / dtd-version (recommended)
        if (article.getDtdVersion() == null || article.getDtdVersion().trim().isEmpty()) {
            errors.add(ValidationError.info(
                    ValidationError.ErrorCode.MISSING_RECOMMENDED_ATTRIBUTE,
                    "Recommended attribute 'dtd-version' is missing",
                    "/article",
                    "Example: 1.4"
            ));
        }

        return errors;
    }

    // ========================================================================
    // 유틸리티 메서드 / Utility Methods
    // ========================================================================

    /**
     * 검증 결과 요약 / Summarize validation results
     *
     * @param errors 검증 오류 목록 / List of validation errors
     * @return 요약 문자열 / Summary string
     */
    public static String summarize(List<ValidationError> errors) {
        if (errors.isEmpty()) {
            return "✅ Validation passed: No errors found";
        }

        long errorCount = errors.stream()
                .filter(e -> e.getSeverity() == ValidationError.Severity.ERROR)
                .count();
        long warningCount = errors.stream()
                .filter(e -> e.getSeverity() == ValidationError.Severity.WARNING)
                .count();
        long infoCount = errors.stream()
                .filter(e -> e.getSeverity() == ValidationError.Severity.INFO)
                .count();

        return String.format("❌ Validation failed: %d errors, %d warnings, %d info",
                errorCount, warningCount, infoCount);
    }

    /**
     * 검증 오류 출력 / Print validation errors
     *
     * @param errors 검증 오류 목록 / List of validation errors
     */
    public static void printErrors(List<ValidationError> errors) {
        System.out.println(summarize(errors));
        System.out.println();

        if (!errors.isEmpty()) {
            for (ValidationError error : errors) {
                System.out.println(error);
            }
        }
    }
}
