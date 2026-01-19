package com.brillianttiger.bio.parser.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * ValidationUtils / 파싱 결과 검증 유틸리티
 *
 * KR: 파싱 결과 검증 유틸리티.
 *     PubMed와 PMC 파싱 결과의 필수 필드 검증.
 * EN: Parsing result validation utility.
 *     Validates required fields for PubMed and PMC parsing results.
 *
 * Note: This is a placeholder implementation.
 *       Actual validation logic will be implemented with concrete model classes.
 */
public final class ValidationUtils {

    private ValidationUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * PubMed 필수 필드 검증 / Validate PubMed required fields
     *
     * KR: PubMed 논문의 필수 필드를 검증합니다.
     * EN: Validates required fields for PubMed articles.
     *
     * @param article PubmedArticle instance
     * @return list of validation error messages (empty if valid)
     */
    public static List<String> validatePubmedArticle(Object article) {
        List<String> errors = new ArrayList<>();

        if (article == null) {
            errors.add("PubmedArticle is null");
            return errors;
        }

        // Basic validation using reflection to avoid tight coupling
        // Actual model-specific validation should be done in the pubmed module

        try {
            // Check MedlineCitation
            var medlineCitationMethod = article.getClass().getMethod("getMedlineCitation");
            Object medlineCitation = medlineCitationMethod.invoke(article);

            if (medlineCitation == null) {
                errors.add("MedlineCitation is required");
                return errors;
            }

            // Check PMID
            try {
                var pmidMethod = medlineCitation.getClass().getMethod("getPmid");
                Object pmid = pmidMethod.invoke(medlineCitation);
                if (pmid == null || isEmpty(String.valueOf(pmid))) {
                    errors.add("PMID is required");
                }
            } catch (Exception e) {
                errors.add("PMID field not accessible");
            }

            // Check Article
            try {
                var articleMethod = medlineCitation.getClass().getMethod("getArticle");
                Object pmArticle = articleMethod.invoke(medlineCitation);
                if (pmArticle == null) {
                    errors.add("Article is required");
                } else {
                    // Check ArticleTitle
                    try {
                        var titleMethod = pmArticle.getClass().getMethod("getArticleTitle");
                        Object title = titleMethod.invoke(pmArticle);
                        if (title == null || isEmpty(String.valueOf(title))) {
                            errors.add("ArticleTitle is required");
                        }
                    } catch (Exception e) {
                        // Field may not exist
                    }
                }
            } catch (Exception e) {
                errors.add("Article field not accessible");
            }

        } catch (Exception e) {
            errors.add("Failed to validate PubmedArticle structure: " + e.getMessage());
        }

        return errors;
    }

    /**
     * JATS 필수 필드 검증 / Validate JATS required fields
     *
     * KR: JATS 논문의 필수 필드를 검증합니다.
     * EN: Validates required fields for JATS articles.
     *
     * @param article JATS Article instance
     * @return list of validation error messages (empty if valid)
     */
    public static List<String> validateJatsArticle(Object article) {
        List<String> errors = new ArrayList<>();

        if (article == null) {
            errors.add("Article is null");
            return errors;
        }

        // Basic validation using reflection to avoid tight coupling
        // Actual model-specific validation should be done in the pmc module

        try {
            // Check Front
            var frontMethod = article.getClass().getMethod("getFront");
            Object front = frontMethod.invoke(article);

            if (front == null) {
                errors.add("Front is required for JATS article");
                return errors;
            }

            // Check ArticleMeta
            try {
                var articleMetaMethod = front.getClass().getMethod("getArticleMeta");
                Object articleMeta = articleMetaMethod.invoke(front);
                if (articleMeta == null) {
                    errors.add("ArticleMeta is required");
                } else {
                    // Check TitleGroup
                    try {
                        var titleGroupMethod = articleMeta.getClass().getMethod("getTitleGroup");
                        Object titleGroup = titleGroupMethod.invoke(articleMeta);
                        if (titleGroup == null) {
                            errors.add("TitleGroup is recommended in ArticleMeta");
                        }
                    } catch (Exception e) {
                        // Field may not exist
                    }
                }
            } catch (Exception e) {
                errors.add("ArticleMeta field not accessible");
            }

        } catch (Exception e) {
            errors.add("Failed to validate JATS Article structure: " + e.getMessage());
        }

        return errors;
    }

    /**
     * 문자열이 비어있지 않은지 확인 / Check if string is not empty
     *
     * @param text text to check
     * @return true if text is not null and not blank
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }

    /**
     * 문자열이 비어있는지 확인 / Check if string is empty
     *
     * @param text text to check
     * @return true if text is null or blank
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}
