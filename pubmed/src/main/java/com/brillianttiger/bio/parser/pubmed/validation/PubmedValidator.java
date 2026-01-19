package com.brillianttiger.bio.parser.pubmed.validation;

import com.brillianttiger.bio.parser.common.validation.ValidationError;
import com.brillianttiger.bio.parser.common.validation.ValidationUtils;
import com.brillianttiger.bio.parser.pubmed.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * PubmedValidator / PubMed 검증기
 *
 * KR: PubmedArticle의 필수 필드 및 형식을 검증하는 클래스.
 *     DTD pubmed_250101 기준으로 필수 요소와 속성을 검증.
 * EN: Validator for PubmedArticle required fields and formats.
 *     Validates required elements and attributes based on DTD pubmed_250101.
 *
 * 검증 항목 / Validation Items:
 * - 필수 필드: PMID, MedlineCitation, Article
 * - PMID 형식: 숫자만 (1-8자리)
 * - 날짜 범위: 1809년 ~ 현재+5년
 * - MeSH UI 형식: D000000 또는 C000000
 * - ORCID 형식: xxxx-xxxx-xxxx-xxxx
 */
public class PubmedValidator {

    /**
     * PubmedArticle 검증 / Validate PubmedArticle
     *
     * KR: PubmedArticle 전체를 검증하여 모든 오류를 반환.
     *     필수 필드, 형식, 범위 등을 체계적으로 검증.
     * EN: Validates entire PubmedArticle and returns all errors.
     *     Systematically validates required fields, formats, and ranges.
     *
     * DTD: <!ELEMENT PubmedArticle (MedlineCitation, PubmedData?)>
     *
     * @param article 검증할 PubmedArticle / PubmedArticle to validate
     * @return 검증 오류 목록 (빈 리스트면 통과) / List of validation errors (empty if valid)
     */
    public static List<ValidationError> validateArticle(PubmedArticle article) {
        List<ValidationError> errors = new ArrayList<>();

        if (article == null) {
            errors.add(ValidationError.error("PubmedArticle", "PubmedArticle is null"));
            return errors;
        }

        // MedlineCitation 검증 (필수)
        // Validate MedlineCitation (required)
        MedlineCitation citation = article.getMedlineCitation();
        if (citation == null) {
            errors.add(ValidationError.error("MedlineCitation", "MedlineCitation is required"));
            return errors; // MedlineCitation 없으면 더 이상 검증 불가
        }

        // MedlineCitation 하위 검증
        // Validate MedlineCitation fields
        errors.addAll(validateMedlineCitation(citation));

        // PubmedData 검증 (선택적이지만 있으면 검증)
        // Validate PubmedData (optional but validate if present)
        PubmedData pubmedData = article.getPubmedData();
        if (pubmedData != null) {
            errors.addAll(validatePubmedData(pubmedData));
        }

        return errors;
    }

    /**
     * MedlineCitation 검증 / Validate MedlineCitation
     *
     * DTD: <!ELEMENT MedlineCitation (PMID, DateCompleted?, DateRevised?, Article, MedlineJournalInfo,
     *                                  ChemicalList?, SupplMeshList?, CitationSubset*, CommentsCorrectionsList?,
     *                                  GeneSymbolList?, MeshHeadingList?, NumberOfReferences?, PersonalNameSubjectList?,
     *                                  OtherID*, OtherAbstract*, KeywordList*, CoiStatement?, SpaceFlightMission*,
     *                                  InvestigatorList*, GeneralNote*)>
     * DTD: <!ATTLIST MedlineCitation Status (Completed | In-Process | PubMed-not-MEDLINE | Publisher | MEDLINE | OLDMEDLINE) #REQUIRED
     *                               Owner (NLM | NASA | PIP | KIE | HSR | HMD | NOTNLM) "NLM"
     *                               IndexingMethod (Automated | Curated) #IMPLIED
     *                               VersionID CDATA #IMPLIED
     *                               VersionDate CDATA #IMPLIED>
     */
    private static List<ValidationError> validateMedlineCitation(MedlineCitation citation) {
        List<ValidationError> errors = new ArrayList<>();

        // Status 속성 (필수)
        // Status attribute (required)
        if (citation.getStatus() == null) {
            errors.add(ValidationError.error("MedlineCitation.Status", "Status attribute is required"));
        }

        // PMID (필수)
        // PMID (required)
        PMID pmid = citation.getPmid();
        ValidationUtils.validateRequired(pmid, "MedlineCitation.PMID")
                .ifPresent(errors::add);

        if (pmid != null) {
            // PMID 형식 검증
            // Validate PMID format
            ValidationUtils.validatePmid(pmid.getValue())
                    .ifPresent(errors::add);
        }

        // Article (필수)
        // Article (required)
        Article article = citation.getArticle();
        ValidationUtils.validateRequired(article, "MedlineCitation.Article")
                .ifPresent(errors::add);

        if (article != null) {
            errors.addAll(validateArticle(article));
        }

        // MedlineJournalInfo (필수)
        // MedlineJournalInfo (required)
        ValidationUtils.validateRequired(citation.getMedlineJournalInfo(), "MedlineCitation.MedlineJournalInfo")
                .ifPresent(errors::add);

        // 날짜 검증 (선택적 필드지만 있으면 검증)
        // Validate dates (optional fields but validate if present)
        if (citation.getDateCompleted() != null) {
            errors.addAll(validateDate(citation.getDateCompleted(), "MedlineCitation.DateCompleted"));
        }
        if (citation.getDateRevised() != null) {
            errors.addAll(validateDate(citation.getDateRevised(), "MedlineCitation.DateRevised"));
        }

        // MeSH 검증
        // Validate MeSH
        if (citation.getMeshHeadingList() != null) {
            errors.addAll(validateMeshHeadingList(citation.getMeshHeadingList()));
        }

        // 저자 검증
        // Validate authors
        if (article != null && article.getAuthorList() != null) {
            errors.addAll(validateAuthorList(article.getAuthorList()));
        }

        return errors;
    }

    /**
     * Article 검증 / Validate Article
     *
     * DTD: <!ELEMENT Article (Journal, ArticleTitle, ((Pagination, ELocationID*) | ELocationID+),
     *                         Abstract?, AuthorList?, Language+, DataBankList?, GrantList?,
     *                         PublicationTypeList, VernacularTitle?, ArticleDate*)>
     * DTD: <!ATTLIST Article PubModel (Print | Print-Electronic | Electronic | Electronic-Print | Electronic-eCollection) #REQUIRED>
     */
    private static List<ValidationError> validateArticle(Article article) {
        List<ValidationError> errors = new ArrayList<>();

        // PubModel 속성 (필수)
        // PubModel attribute (required)
        if (article.getPubModel() == null) {
            errors.add(ValidationError.error("Article.PubModel", "PubModel attribute is required"));
        }

        // Journal (필수)
        // Journal (required)
        ValidationUtils.validateRequired(article.getJournal(), "Article.Journal")
                .ifPresent(errors::add);

        // ArticleTitle (필수)
        // ArticleTitle (required)
        ValidationUtils.validateRequired(article.getArticleTitle(), "Article.ArticleTitle")
                .ifPresent(errors::add);

        // PublicationTypeList (필수)
        // PublicationTypeList (required)
        ValidationUtils.validateRequired(article.getPublicationTypeList(), "Article.PublicationTypeList")
                .ifPresent(errors::add);

        if (article.getPublicationTypeList() != null) {
            List<PublicationType> pubTypes = article.getPublicationTypeList().getPublicationTypes();
            if (pubTypes == null || pubTypes.isEmpty()) {
                errors.add(ValidationError.error(
                        "Article.PublicationTypeList",
                        "PublicationTypeList must contain at least one PublicationType"
                ));
            }
        }

        // Language (필수, 1개 이상)
        // Language (required, at least one)
        List<Language> languages = article.getLanguages();
        if (languages == null || languages.isEmpty()) {
            errors.add(ValidationError.error(
                    "Article.Language",
                    "At least one Language is required"
            ));
        }

        return errors;
    }

    /**
     * 날짜 검증 / Validate date
     *
     * KR: DateCompleted, DateRevised 등의 날짜 객체 검증
     * EN: Validate date objects like DateCompleted, DateRevised
     */
    private static List<ValidationError> validateDate(Object dateObj, String fieldPath) {
        List<ValidationError> errors = new ArrayList<>();

        try {
            // 리플렉션으로 Year, Month, Day 가져오기
            // Get Year, Month, Day via reflection
            java.lang.reflect.Method getYear = dateObj.getClass().getMethod("getYear");
            java.lang.reflect.Method getMonth = dateObj.getClass().getMethod("getMonth");
            java.lang.reflect.Method getDay = dateObj.getClass().getMethod("getDay");

            Object yearObj = getYear.invoke(dateObj);
            Object monthObj = getMonth.invoke(dateObj);
            Object dayObj = getDay.invoke(dateObj);

            // Year 검증
            // Validate year
            if (yearObj != null) {
                Integer year = Integer.parseInt(yearObj.toString());
                ValidationUtils.validateYear(year)
                        .ifPresent(error -> errors.add(ValidationError.error(
                                fieldPath + ".Year",
                                error.message()
                        )));
            }

            // Month 검증
            // Validate month
            if (monthObj != null) {
                Integer month = Integer.parseInt(monthObj.toString());
                ValidationUtils.validateMonth(month)
                        .ifPresent(error -> errors.add(ValidationError.error(
                                fieldPath + ".Month",
                                error.message()
                        )));
            }

            // Day 검증
            // Validate day
            if (dayObj != null) {
                Integer day = Integer.parseInt(dayObj.toString());
                ValidationUtils.validateDay(day)
                        .ifPresent(error -> errors.add(ValidationError.error(
                                fieldPath + ".Day",
                                error.message()
                        )));
            }
        } catch (Exception e) {
            // 리플렉션 실패 시 경고
            // Warning on reflection failure
            errors.add(ValidationError.warning(
                    fieldPath,
                    "Could not validate date: " + e.getMessage()
            ));
        }

        return errors;
    }

    /**
     * MeshHeadingList 검증 / Validate MeshHeadingList
     *
     * KR: MeSH Heading 목록의 UI 형식 검증
     * EN: Validate MeSH Heading list UI format
     */
    private static List<ValidationError> validateMeshHeadingList(MeshHeadingList meshHeadingList) {
        List<ValidationError> errors = new ArrayList<>();

        if (meshHeadingList.getMeshHeadings() == null) {
            return errors;
        }

        for (int i = 0; i < meshHeadingList.getMeshHeadings().size(); i++) {
            final int headingIndex = i; // Lambda에서 사용하기 위해 final 변수로 복사
            MeshHeading heading = meshHeadingList.getMeshHeadings().get(i);
            if (heading.getDescriptorName() != null) {
                DescriptorName descriptor = heading.getDescriptorName();
                if (descriptor.getUi() != null) {
                    ValidationUtils.validateMeshUi(descriptor.getUi())
                            .ifPresent(error -> errors.add(ValidationError.error(
                                    "MeshHeadingList.MeshHeading[" + headingIndex + "].DescriptorName.UI",
                                    error.message()
                            )));
                }
            }

            // QualifierName UI 검증
            // Validate QualifierName UI
            if (heading.getQualifierNames() != null) {
                for (int j = 0; j < heading.getQualifierNames().size(); j++) {
                    final int qualifierIndex = j; // Lambda에서 사용하기 위해 final 변수로 복사
                    QualifierName qualifier = heading.getQualifierNames().get(j);
                    if (qualifier.getUi() != null) {
                        ValidationUtils.validateMeshUi(qualifier.getUi())
                                .ifPresent(error -> errors.add(ValidationError.error(
                                        "MeshHeadingList.MeshHeading[" + headingIndex + "].QualifierName[" + qualifierIndex + "].UI",
                                        error.message()
                                )));
                    }
                }
            }
        }

        return errors;
    }

    /**
     * AuthorList 검증 / Validate AuthorList
     *
     * KR: 저자 목록의 ORCID 형식 검증
     * EN: Validate author list ORCID format
     */
    private static List<ValidationError> validateAuthorList(AuthorList authorList) {
        List<ValidationError> errors = new ArrayList<>();

        if (authorList.getAuthors() == null) {
            return errors;
        }

        for (int i = 0; i < authorList.getAuthors().size(); i++) {
            final int authorIndex = i; // Lambda에서 사용하기 위해 final 변수로 복사
            Author author = authorList.getAuthors().get(i);
            if (author.getIdentifiers() != null) {
                for (int j = 0; j < author.getIdentifiers().size(); j++) {
                    final int identifierIndex = j; // Lambda에서 사용하기 위해 final 변수로 복사
                    Identifier identifier = author.getIdentifiers().get(j);
                    if ("ORCID".equalsIgnoreCase(identifier.getSource())) {
                        ValidationUtils.validateOrcid(identifier.getValue())
                                .ifPresent(error -> errors.add(ValidationError.error(
                                        "AuthorList.Author[" + authorIndex + "].Identifier[" + identifierIndex + "]",
                                        error.message()
                                )));
                    }
                }
            }
        }

        return errors;
    }

    /**
     * PubmedData 검증 / Validate PubmedData
     *
     * KR: PubmedData의 History 날짜 검증
     * EN: Validate PubmedData history dates
     */
    private static List<ValidationError> validatePubmedData(PubmedData pubmedData) {
        List<ValidationError> errors = new ArrayList<>();

        // History 날짜 검증
        // Validate history dates
        if (pubmedData.getHistory() != null && pubmedData.getHistory().getPubMedPubDates() != null) {
            for (int i = 0; i < pubmedData.getHistory().getPubMedPubDates().size(); i++) {
                PubMedPubDate pubDate = pubmedData.getHistory().getPubMedPubDates().get(i);
                errors.addAll(validateDate(pubDate, "PubmedData.History.PubMedPubDate[" + i + "]"));
            }
        }

        return errors;
    }
}
