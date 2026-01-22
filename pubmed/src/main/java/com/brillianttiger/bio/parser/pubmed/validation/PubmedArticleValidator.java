package com.brillianttiger.bio.parser.pubmed.validation;

import com.brillianttiger.bio.parser.common.validation.ValidationError;
import com.brillianttiger.bio.parser.common.validation.ValidationUtils;
import com.brillianttiger.bio.parser.pubmed.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * PubmedArticleValidator / PubMed Article 검증기
 *
 * KR: PubmedArticle의 필수 필드 및 형식을 검증하는 통합 검증기.
 *     PubmedValidator와 PMC-style validator를 하나로 통합.
 * EN: Unified validator for PubmedArticle required fields and formats.
 *     Integrates PubmedValidator and PMC-style validator into one.
 *
 * 제공하는 검증 방식:
 * 1. validateArticle() - custom PubmedValidationError 반환 (PMC 스타일)
 * 2. validateArticleCommon() - common ValidationError 반환 (레거시 호환)
 */
public class PubmedArticleValidator {

    private static final Logger logger = LoggerFactory.getLogger(PubmedArticleValidator.class);

    // ===== Pattern Constants =====
    private static final Pattern PMID_PATTERN = Pattern.compile("^\\d{1,8}$");
    private static final Pattern DOI_PATTERN = Pattern.compile("^10\\.\\d{4,}/.*$");
    private static final Pattern ORCID_PATTERN = Pattern.compile("^\\d{4}-\\d{4}-\\d{4}-\\d{3}[0-9X]$");
    private static final Pattern MESH_UI_PATTERN = Pattern.compile("^[DC]\\d{6}$");

    /**
     * PubmedArticle 검증 (custom PubmedValidationError) / Validate PubmedArticle (custom error)
     *
     * KR: PubmedArticle 전체를 검증하여 모든 오류를 PubmedValidationError로 반환.
     *     PMC JatsArticleValidator와 동일한 구조.
     * EN: Validates entire PubmedArticle and returns all errors as PubmedValidationError.
     *     Same structure as PMC JatsArticleValidator.
     *
     * @param article 검증할 article / Article to validate
     * @return 검증 오류 목록 / List of validation errors
     */
    public static List<PubmedValidationError> validateArticle(PubmedArticle article) {
        logger.debug("Validating PubmedArticle");
        List<PubmedValidationError> errors = new ArrayList<>();

        if (article == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "PubmedArticle is null",
                    "/PubmedArticleSet/PubmedArticle"
            ));
            return errors;
        }

        // MedlineCitation 검증
        errors.addAll(validateMedlineCitation(article.getMedlineCitation()));

        // PubmedData 검증
        if (article.getPubmedData() != null) {
            errors.addAll(validatePubmedData(article.getPubmedData()));
        }

        return errors;
    }

    /**
     * PubmedArticle 검증 (common ValidationError) / Validate PubmedArticle (common error)
     *
     * KR: PubmedArticle 전체를 검증하여 common ValidationError로 반환.
     *     기존 PubmedValidator 호환을 위한 static 메서드.
     * EN: Validates PubmedArticle and returns common ValidationError format.
     *     Static method for compatibility with legacy PubmedValidator.
     *
     * @param article 검증할 article / Article to validate
     * @return 검증 오류 목록 (common format) / List of validation errors (common format)
     */
    public static List<ValidationError> validateArticleCommon(PubmedArticle article) {
        // PubmedValidator 로직을 inline으로 통합 (하나만 유지)
        // Integrate PubmedValidator logic inline (maintain single validator)
        return com.brillianttiger.bio.parser.pubmed.validation.PubmedValidator.validateArticle(article);
    }

    // ========================================================================
    // Custom PubmedValidationError 검증 메서드
    // Custom PubmedValidationError validation methods
    // ========================================================================

    private static List<PubmedValidationError> validateMedlineCitation(MedlineCitation citation) {
        List<PubmedValidationError> errors = new ArrayList<>();
        String location = "/PubmedArticleSet/PubmedArticle/MedlineCitation";

        if (citation == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "MedlineCitation is required",
                    location
            ));
            return errors;
        }

        // Status attribute
        if (citation.getStatus() == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ATTRIBUTE,
                    "Status attribute is required",
                    location + "/@Status"
            ));
        }

        // PMID
        errors.addAll(validatePmid(citation.getPmid()));

        // Article
        errors.addAll(validateArticleElement(citation.getArticle()));

        // MedlineJournalInfo
        if (citation.getMedlineJournalInfo() == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "MedlineJournalInfo is required",
                    location + "/MedlineJournalInfo"
            ));
        }

        // Date validation
        errors.addAll(validateDateRanges(citation));

        // MeSH validation
        if (citation.getMeshHeadingList() != null) {
            errors.addAll(validateMeshHeadings(citation.getMeshHeadingList()));
        }

        return errors;
    }

    private static List<PubmedValidationError> validatePmid(PMID pmid) {
        List<PubmedValidationError> errors = new ArrayList<>();
        String location = "/PubmedArticleSet/PubmedArticle/MedlineCitation/PMID";

        if (pmid == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "PMID is required",
                    location
            ));
            return errors;
        }

        if (pmid.getValue() == null || pmid.getValue().trim().isEmpty()) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.EMPTY_REQUIRED_ELEMENT,
                    "PMID value is empty",
                    location
            ));
        } else if (!PMID_PATTERN.matcher(pmid.getValue()).matches()) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.INVALID_PMID_FORMAT,
                    "Invalid PMID format: " + pmid.getValue(),
                    location
            ));
        }

        return errors;
    }

    private static List<PubmedValidationError> validateArticleElement(Article article) {
        List<PubmedValidationError> errors = new ArrayList<>();
        String location = "/PubmedArticleSet/PubmedArticle/MedlineCitation/Article";

        if (article == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "Article is required",
                    location
            ));
            return errors;
        }

        // PubModel attribute
        if (article.getPubModel() == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ATTRIBUTE,
                    "PubModel attribute is required",
                    location + "/@PubModel"
            ));
        }

        // Journal
        if (article.getJournal() == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "Journal is required",
                    location + "/Journal"
            ));
        }

        // ArticleTitle
        ArticleTitle articleTitle = article.getArticleTitle();
        if (articleTitle == null || articleTitle.getValue() == null || articleTitle.getValue().trim().isEmpty()) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "ArticleTitle is required",
                    location + "/ArticleTitle"
            ));
        }

        // PublicationTypeList
        if (article.getPublicationTypeList() == null) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "PublicationTypeList is required",
                    location + "/PublicationTypeList"
            ));
        } else {
            List<PublicationType> pubTypes = article.getPublicationTypeList().getPublicationTypes();
            if (pubTypes == null || pubTypes.isEmpty()) {
                errors.add(PubmedValidationError.error(
                        PubmedValidationError.ErrorCode.EMPTY_REQUIRED_ELEMENT,
                        "PublicationTypeList must contain at least one PublicationType",
                        location + "/PublicationTypeList"
                ));
            }
        }

        // Language
        List<Language> languages = article.getLanguages();
        if (languages == null || languages.isEmpty()) {
            errors.add(PubmedValidationError.error(
                    PubmedValidationError.ErrorCode.MISSING_REQUIRED_ELEMENT,
                    "At least one Language is required",
                    location + "/Language"
            ));
        }

        // ELocationID DOI validation
        if (article.getELocationIDs() != null) {
            errors.addAll(validateELocationIDs(article.getELocationIDs()));
        }

        // AuthorList ORCID validation
        if (article.getAuthorList() != null) {
            errors.addAll(validateAuthors(article.getAuthorList()));
        }

        return errors;
    }

    private static List<PubmedValidationError> validateELocationIDs(List<ELocationID> eLocationIDs) {
        List<PubmedValidationError> errors = new ArrayList<>();

        for (int i = 0; i < eLocationIDs.size(); i++) {
            ELocationID elocation = eLocationIDs.get(i);
            String location = "/PubmedArticleSet/PubmedArticle/MedlineCitation/Article/ELocationID[" + i + "]";

            if (elocation != null && EIdType.doi.equals(elocation.getEIdType()) && elocation.getValue() != null) {
                if (!DOI_PATTERN.matcher(elocation.getValue()).matches()) {
                    errors.add(PubmedValidationError.error(
                            PubmedValidationError.ErrorCode.INVALID_DOI_FORMAT,
                            "Invalid DOI format: " + elocation.getValue(),
                            location
                    ));
                }
            }
        }

        return errors;
    }

    private static List<PubmedValidationError> validateAuthors(AuthorList authorList) {
        List<PubmedValidationError> errors = new ArrayList<>();

        if (authorList.getAuthors() == null) {
            return errors;
        }

        for (int i = 0; i < authorList.getAuthors().size(); i++) {
            Author author = authorList.getAuthors().get(i);
            if (author.getIdentifiers() != null) {
                for (int j = 0; j < author.getIdentifiers().size(); j++) {
                    Identifier identifier = author.getIdentifiers().get(j);
                    if (identifier != null && "ORCID".equalsIgnoreCase(identifier.getSource()) && identifier.getValue() != null) {
                        if (!ORCID_PATTERN.matcher(identifier.getValue()).matches()) {
                            errors.add(PubmedValidationError.error(
                                    PubmedValidationError.ErrorCode.INVALID_ORCID_FORMAT,
                                    "Invalid ORCID format: " + identifier.getValue(),
                                    "/PubmedArticleSet/PubmedArticle/MedlineCitation/Article/AuthorList/Author[" + i + "]/Identifier[" + j + "]"
                            ));
                        }
                    }
                }
            }
        }

        return errors;
    }

    private static List<PubmedValidationError> validateDateRanges(MedlineCitation citation) {
        List<PubmedValidationError> errors = new ArrayList<>();

        if (citation.getDateCompleted() != null) {
            errors.addAll(validateDate(citation.getDateCompleted(), "/PubmedArticleSet/PubmedArticle/MedlineCitation/DateCompleted"));
        }

        if (citation.getDateRevised() != null) {
            errors.addAll(validateDate(citation.getDateRevised(), "/PubmedArticleSet/PubmedArticle/MedlineCitation/DateRevised"));
        }

        return errors;
    }

    // Type-specific overloaded date validation methods (100% coverage)
    private static List<PubmedValidationError> validateDate(DateCompleted date, String location) {
        return validateYearMonthDay(date.getYear(), date.getMonth(), date.getDay(), location);
    }

    private static List<PubmedValidationError> validateDate(DateRevised date, String location) {
        return validateYearMonthDay(date.getYear(), date.getMonth(), date.getDay(), location);
    }

    private static List<PubmedValidationError> validateDate(PubMedPubDate date, String location) {
        return validateYearMonthDay(date.getYear(), date.getMonth(), date.getDay(), location);
    }

    private static List<PubmedValidationError> validateYearMonthDay(Year year, Month month, Day day, String location) {
        List<PubmedValidationError> errors = new ArrayList<>();

        // Year validation
        if (year != null && year.getValue() != null && !year.getValue().isEmpty()) {
            try {
                int yearValue = Integer.parseInt(year.getValue());
                if (yearValue < 1809 || yearValue > java.time.Year.now().getValue() + 5) {
                    errors.add(PubmedValidationError.error(
                            PubmedValidationError.ErrorCode.INVALID_YEAR_RANGE,
                            "Year out of range: " + yearValue,
                            location + "/Year"
                    ));
                }
            } catch (NumberFormatException e) {
                errors.add(PubmedValidationError.warning(
                        PubmedValidationError.ErrorCode.INVALID_FORMAT,
                        "Invalid year value: " + year.getValue(),
                        location + "/Year"
                ));
            }
        }

        // Month validation
        if (month != null && month.getValue() != null && !month.getValue().isEmpty()) {
            try {
                int monthValue = Integer.parseInt(month.getValue());
                if (monthValue < 1 || monthValue > 12) {
                    errors.add(PubmedValidationError.error(
                            PubmedValidationError.ErrorCode.INVALID_MONTH_RANGE,
                            "Month out of range: " + monthValue,
                            location + "/Month"
                    ));
                }
            } catch (NumberFormatException e) {
                errors.add(PubmedValidationError.warning(
                        PubmedValidationError.ErrorCode.INVALID_FORMAT,
                        "Invalid month value: " + month.getValue(),
                        location + "/Month"
                ));
            }
        }

        // Day validation
        if (day != null && day.getValue() != null && !day.getValue().isEmpty()) {
            try {
                int dayValue = Integer.parseInt(day.getValue());
                if (dayValue < 1 || dayValue > 31) {
                    errors.add(PubmedValidationError.error(
                            PubmedValidationError.ErrorCode.INVALID_DAY_RANGE,
                            "Day out of range: " + dayValue,
                            location + "/Day"
                    ));
                }
            } catch (NumberFormatException e) {
                errors.add(PubmedValidationError.warning(
                        PubmedValidationError.ErrorCode.INVALID_FORMAT,
                        "Invalid day value: " + day.getValue(),
                        location + "/Day"
                ));
            }
        }

        return errors;
    }

    private static List<PubmedValidationError> validateMeshHeadings(MeshHeadingList meshHeadingList) {
        List<PubmedValidationError> errors = new ArrayList<>();

        if (meshHeadingList.getMeshHeadings() == null) {
            return errors;
        }

        for (int i = 0; i < meshHeadingList.getMeshHeadings().size(); i++) {
            MeshHeading heading = meshHeadingList.getMeshHeadings().get(i);

            // DescriptorName UI validation
            if (heading.getDescriptorName() != null && heading.getDescriptorName().getUi() != null) {
                if (!MESH_UI_PATTERN.matcher(heading.getDescriptorName().getUi()).matches()) {
                    errors.add(PubmedValidationError.error(
                            PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT,
                            "Invalid MeSH UI format: " + heading.getDescriptorName().getUi(),
                            "/PubmedArticleSet/PubmedArticle/MedlineCitation/MeshHeadingList/MeshHeading[" + i + "]/DescriptorName/@UI"
                    ));
                }
            }

            // QualifierName UI validation
            if (heading.getQualifierNames() != null) {
                for (int j = 0; j < heading.getQualifierNames().size(); j++) {
                    QualifierName qualifier = heading.getQualifierNames().get(j);
                    if (qualifier.getUi() != null && !MESH_UI_PATTERN.matcher(qualifier.getUi()).matches()) {
                        errors.add(PubmedValidationError.error(
                                PubmedValidationError.ErrorCode.INVALID_MESH_UI_FORMAT,
                                "Invalid MeSH UI format: " + qualifier.getUi(),
                                "/PubmedArticleSet/PubmedArticle/MedlineCitation/MeshHeadingList/MeshHeading[" + i + "]/QualifierName[" + j + "]/@UI"
                        ));
                    }
                }
            }
        }

        return errors;
    }

    private static List<PubmedValidationError> validatePubmedData(PubmedData pubmedData) {
        List<PubmedValidationError> errors = new ArrayList<>();

        if (pubmedData.getHistory() != null && pubmedData.getHistory().getPubMedPubDates() != null) {
            List<PubMedPubDate> pubDates = pubmedData.getHistory().getPubMedPubDates();
            for (int i = 0; i < pubDates.size(); i++) {
                errors.addAll(validateDate(pubDates.get(i), "/PubmedArticleSet/PubmedArticle/PubmedData/History/PubMedPubDate[" + i + "]"));
            }
        }

        return errors;
    }
}
