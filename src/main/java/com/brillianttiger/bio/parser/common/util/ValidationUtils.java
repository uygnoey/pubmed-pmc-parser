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

        // TODO: Implement actual validation logic with PubmedArticle model
        // Required elements:
        // - MedlineCitation (required)
        //   - PMID (required)
        //   - Article (required)
        //     - Journal (required)
        //     - ArticleTitle (required)
        //     - PublicationTypeList (required)
        //   - MedlineJournalInfo (required)

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

        // TODO: Implement actual validation logic with JATS Article model
        // Required elements:
        // - front (required)
        //   - article-meta (required)

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
