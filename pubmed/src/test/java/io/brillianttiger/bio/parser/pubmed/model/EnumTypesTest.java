package io.brillianttiger.bio.parser.pubmed.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EnumTypesTest / Enum 타입 테스트
 *
 * KR: PubMed 모델의 모든 Enum 타입에 대한 포괄적인 테스트
 * EN: Comprehensive tests for all Enum types in PubMed model
 *
 * 목적 / Purpose:
 * - 100% 코드 커버리지 달성
 * - getValue() 메서드 테스트
 * - fromValue() 정상/예외 케이스 테스트
 */
@DisplayName("PubMed Enum Types Test")
class EnumTypesTest {

    // ========================================
    // ArticleIdType Tests
    // ========================================

    @Test
    @DisplayName("ArticleIdType: fromValue() 정상 케이스")
    void testArticleIdType_FromValue_Success() {
        assertEquals(ArticleIdType.DOI, ArticleIdType.fromValue("doi"));
        assertEquals(ArticleIdType.PII, ArticleIdType.fromValue("pii"));
        assertEquals(ArticleIdType.PMCPID, ArticleIdType.fromValue("pmcpid"));
        assertEquals(ArticleIdType.PMPID, ArticleIdType.fromValue("pmpid"));
        assertEquals(ArticleIdType.PMC, ArticleIdType.fromValue("pmc"));
        assertEquals(ArticleIdType.MID, ArticleIdType.fromValue("mid"));
        assertEquals(ArticleIdType.SICI, ArticleIdType.fromValue("sici"));
        assertEquals(ArticleIdType.PUBMED, ArticleIdType.fromValue("pubmed"));
        assertEquals(ArticleIdType.MEDLINE, ArticleIdType.fromValue("medline"));
        assertEquals(ArticleIdType.PMCID, ArticleIdType.fromValue("pmcid"));
    }

    @Test
    @DisplayName("ArticleIdType: getValue() 메서드")
    void testArticleIdType_GetValue() {
        assertEquals("doi", ArticleIdType.DOI.getValue());
        assertEquals("pii", ArticleIdType.PII.getValue());
        assertEquals("pmcpid", ArticleIdType.PMCPID.getValue());
    }

    @Test
    @DisplayName("ArticleIdType: fromValue() null 케이스")
    void testArticleIdType_FromValue_Null() {
        assertNull(ArticleIdType.fromValue("invalid"));
        assertNull(ArticleIdType.fromValue(null));
    }

    // ========================================
    // AuthorListType Tests
    // ========================================

    @Test
    @DisplayName("AuthorListType: fromValue() 정상 케이스")
    void testAuthorListType_FromValue_Success() {
        assertEquals(AuthorListType.authors, AuthorListType.fromValue("authors"));
        assertEquals(AuthorListType.editors, AuthorListType.fromValue("editors"));
    }

    @Test
    @DisplayName("AuthorListType: getValue() 메서드")
    void testAuthorListType_GetValue() {
        assertEquals("authors", AuthorListType.authors.getValue());
        assertEquals("editors", AuthorListType.editors.getValue());
    }

    @Test
    @DisplayName("AuthorListType: fromValue() 예외 케이스")
    void testAuthorListType_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> AuthorListType.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown AuthorListType value"));
    }

    // ========================================
    // CitedMedium Tests
    // ========================================

    @Test
    @DisplayName("CitedMedium: fromValue() 정상 케이스")
    void testCitedMedium_FromValue_Success() {
        assertEquals(CitedMedium.Internet, CitedMedium.fromValue("Internet"));
        assertEquals(CitedMedium.Print, CitedMedium.fromValue("Print"));
    }

    @Test
    @DisplayName("CitedMedium: getValue() 메서드")
    void testCitedMedium_GetValue() {
        assertEquals("Internet", CitedMedium.Internet.getValue());
        assertEquals("Print", CitedMedium.Print.getValue());
    }

    @Test
    @DisplayName("CitedMedium: fromValue() 예외 케이스")
    void testCitedMedium_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> CitedMedium.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown CitedMedium value"));
    }

    // ========================================
    // DescriptorNameType Tests
    // ========================================

    @Test
    @DisplayName("DescriptorNameType: fromValue() 정상 케이스")
    void testDescriptorNameType_FromValue_Success() {
        assertEquals(DescriptorNameType.Geographic, DescriptorNameType.fromValue("Geographic"));
    }

    @Test
    @DisplayName("DescriptorNameType: getValue() 메서드")
    void testDescriptorNameType_GetValue() {
        assertEquals("Geographic", DescriptorNameType.Geographic.getValue());
    }

    @Test
    @DisplayName("DescriptorNameType: fromValue() 예외 케이스")
    void testDescriptorNameType_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> DescriptorNameType.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown DescriptorNameType value"));
    }

    // ========================================
    // EIdType Tests
    // ========================================

    @Test
    @DisplayName("EIdType: fromValue() 정상 케이스")
    void testEIdType_FromValue_Success() {
        assertEquals(EIdType.doi, EIdType.fromValue("doi"));
        assertEquals(EIdType.pii, EIdType.fromValue("pii"));
    }

    @Test
    @DisplayName("EIdType: getValue() 메서드")
    void testEIdType_GetValue() {
        assertEquals("doi", EIdType.doi.getValue());
        assertEquals("pii", EIdType.pii.getValue());
    }

    @Test
    @DisplayName("EIdType: fromValue() 예외 케이스")
    void testEIdType_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> EIdType.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown EIdType value"));
    }

    // ========================================
    // GeneralNoteOwner Tests
    // ========================================

    @Test
    @DisplayName("GeneralNoteOwner: fromValue() 정상 케이스")
    void testGeneralNoteOwner_FromValue_Success() {
        assertEquals(GeneralNoteOwner.NLM, GeneralNoteOwner.fromValue("NLM"));
        assertEquals(GeneralNoteOwner.NASA, GeneralNoteOwner.fromValue("NASA"));
        assertEquals(GeneralNoteOwner.PIP, GeneralNoteOwner.fromValue("PIP"));
        assertEquals(GeneralNoteOwner.KIE, GeneralNoteOwner.fromValue("KIE"));
        assertEquals(GeneralNoteOwner.HSR, GeneralNoteOwner.fromValue("HSR"));
        assertEquals(GeneralNoteOwner.HMD, GeneralNoteOwner.fromValue("HMD"));
    }

    @Test
    @DisplayName("GeneralNoteOwner: getValue() 메서드")
    void testGeneralNoteOwner_GetValue() {
        assertEquals("NLM", GeneralNoteOwner.NLM.getValue());
        assertEquals("NASA", GeneralNoteOwner.NASA.getValue());
        assertEquals("HMD", GeneralNoteOwner.HMD.getValue());
    }

    @Test
    @DisplayName("GeneralNoteOwner: fromValue() null 케이스")
    void testGeneralNoteOwner_FromValue_Null() {
        // GeneralNoteOwner는 예외를 던지지 않고 null을 반환
        assertNull(GeneralNoteOwner.fromValue("invalid"));
        assertNull(GeneralNoteOwner.fromValue(null));
    }

    // ========================================
    // IndexingMethod Tests
    // ========================================

    @Test
    @DisplayName("IndexingMethod: fromValue() 정상 케이스")
    void testIndexingMethod_FromValue_Success() {
        assertEquals(IndexingMethod.Automated, IndexingMethod.fromValue("Automated"));
        assertEquals(IndexingMethod.Curated, IndexingMethod.fromValue("Curated"));
    }

    @Test
    @DisplayName("IndexingMethod: getValue() 메서드")
    void testIndexingMethod_GetValue() {
        assertEquals("Automated", IndexingMethod.Automated.getValue());
        assertEquals("Curated", IndexingMethod.Curated.getValue());
    }

    @Test
    @DisplayName("IndexingMethod: fromValue() 예외 케이스")
    void testIndexingMethod_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> IndexingMethod.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown IndexingMethod value"));
    }

    // ========================================
    // IssnType Tests
    // ========================================

    @Test
    @DisplayName("IssnType: fromValue() 정상 케이스")
    void testIssnType_FromValue_Success() {
        assertEquals(IssnType.Electronic, IssnType.fromValue("Electronic"));
        assertEquals(IssnType.Print, IssnType.fromValue("Print"));
    }

    @Test
    @DisplayName("IssnType: getValue() 메서드")
    void testIssnType_GetValue() {
        assertEquals("Electronic", IssnType.Electronic.getValue());
        assertEquals("Print", IssnType.Print.getValue());
    }

    @Test
    @DisplayName("IssnType: fromValue() 예외 케이스")
    void testIssnType_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> IssnType.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown IssnType value"));
    }

    // ========================================
    // KeywordOwner Tests
    // ========================================

    @Test
    @DisplayName("KeywordOwner: fromValue() 정상 케이스")
    void testKeywordOwner_FromValue_Success() {
        assertEquals(KeywordOwner.NLM, KeywordOwner.fromValue("NLM"));
        assertEquals(KeywordOwner.NLM_AUTO, KeywordOwner.fromValue("NLM-AUTO"));
        assertEquals(KeywordOwner.NASA, KeywordOwner.fromValue("NASA"));
        assertEquals(KeywordOwner.PIP, KeywordOwner.fromValue("PIP"));
        assertEquals(KeywordOwner.KIE, KeywordOwner.fromValue("KIE"));
        assertEquals(KeywordOwner.NOTNLM, KeywordOwner.fromValue("NOTNLM"));
        assertEquals(KeywordOwner.HHS, KeywordOwner.fromValue("HHS"));
    }

    @Test
    @DisplayName("KeywordOwner: getValue() 메서드")
    void testKeywordOwner_GetValue() {
        assertEquals("NLM", KeywordOwner.NLM.getValue());
        assertEquals("NLM-AUTO", KeywordOwner.NLM_AUTO.getValue());
        assertEquals("NOTNLM", KeywordOwner.NOTNLM.getValue());
    }

    @Test
    @DisplayName("KeywordOwner: fromValue() null 케이스")
    void testKeywordOwner_FromValue_Null() {
        // KeywordOwner는 예외를 던지지 않고 null을 반환
        assertNull(KeywordOwner.fromValue("invalid"));
        assertNull(KeywordOwner.fromValue(null));
    }

    // ========================================
    // LocationLabelType Tests
    // ========================================

    @Test
    @DisplayName("LocationLabelType: fromValue() 정상 케이스")
    void testLocationLabelType_FromValue_Success() {
        assertEquals(LocationLabelType.PART, LocationLabelType.fromValue("part"));
        assertEquals(LocationLabelType.CHAPTER, LocationLabelType.fromValue("chapter"));
        assertEquals(LocationLabelType.SECTION, LocationLabelType.fromValue("section"));
        assertEquals(LocationLabelType.APPENDIX, LocationLabelType.fromValue("appendix"));
        assertEquals(LocationLabelType.FIGURE, LocationLabelType.fromValue("figure"));
        assertEquals(LocationLabelType.TABLE, LocationLabelType.fromValue("table"));
        assertEquals(LocationLabelType.BOX, LocationLabelType.fromValue("box"));
    }

    @Test
    @DisplayName("LocationLabelType: getValue() 메서드")
    void testLocationLabelType_GetValue() {
        assertEquals("part", LocationLabelType.PART.getValue());
        assertEquals("chapter", LocationLabelType.CHAPTER.getValue());
        assertEquals("figure", LocationLabelType.FIGURE.getValue());
    }

    @Test
    @DisplayName("LocationLabelType: fromValue() null 케이스")
    void testLocationLabelType_FromValue_Null() {
        // LocationLabelType은 예외를 던지지 않고 null을 반환
        assertNull(LocationLabelType.fromValue("invalid"));
        assertNull(LocationLabelType.fromValue(null));
    }

    // ========================================
    // NlmCategory Tests
    // ========================================

    @Test
    @DisplayName("NlmCategory: fromValue() 정상 케이스")
    void testNlmCategory_FromValue_Success() {
        assertEquals(NlmCategory.BACKGROUND, NlmCategory.fromValue("BACKGROUND"));
        assertEquals(NlmCategory.OBJECTIVE, NlmCategory.fromValue("OBJECTIVE"));
        assertEquals(NlmCategory.METHODS, NlmCategory.fromValue("METHODS"));
        assertEquals(NlmCategory.RESULTS, NlmCategory.fromValue("RESULTS"));
        assertEquals(NlmCategory.CONCLUSIONS, NlmCategory.fromValue("CONCLUSIONS"));
        assertEquals(NlmCategory.UNASSIGNED, NlmCategory.fromValue("UNASSIGNED"));
    }

    @Test
    @DisplayName("NlmCategory: getValue() 메서드")
    void testNlmCategory_GetValue() {
        assertEquals("BACKGROUND", NlmCategory.BACKGROUND.getValue());
        assertEquals("METHODS", NlmCategory.METHODS.getValue());
        assertEquals("UNASSIGNED", NlmCategory.UNASSIGNED.getValue());
    }

    @Test
    @DisplayName("NlmCategory: fromValue() 예외 케이스")
    void testNlmCategory_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> NlmCategory.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown NlmCategory value"));
    }

    // ========================================
    // OtherAbstractType Tests
    // ========================================

    @Test
    @DisplayName("OtherAbstractType: fromValue() 정상 케이스")
    void testOtherAbstractType_FromValue_Success() {
        assertEquals(OtherAbstractType.AAMC, OtherAbstractType.fromValue("AAMC"));
        assertEquals(OtherAbstractType.AIDS, OtherAbstractType.fromValue("AIDS"));
        assertEquals(OtherAbstractType.KIE, OtherAbstractType.fromValue("KIE"));
        assertEquals(OtherAbstractType.PIP, OtherAbstractType.fromValue("PIP"));
        assertEquals(OtherAbstractType.NASA, OtherAbstractType.fromValue("NASA"));
        assertEquals(OtherAbstractType.Publisher, OtherAbstractType.fromValue("Publisher"));
    }

    @Test
    @DisplayName("OtherAbstractType: getValue() 메서드")
    void testOtherAbstractType_GetValue() {
        assertEquals("AAMC", OtherAbstractType.AAMC.getValue());
        assertEquals("AIDS", OtherAbstractType.AIDS.getValue());
        assertEquals("Publisher", OtherAbstractType.Publisher.getValue());
    }

    @Test
    @DisplayName("OtherAbstractType: fromValue() 예외 케이스")
    void testOtherAbstractType_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> OtherAbstractType.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown OtherAbstractType value"));
    }

    // ========================================
    // OtherIDSource Tests
    // ========================================

    @Test
    @DisplayName("OtherIDSource: fromValue() 정상 케이스")
    void testOtherIDSource_FromValue_Success() {
        assertEquals(OtherIDSource.NASA, OtherIDSource.fromValue("NASA"));
        assertEquals(OtherIDSource.KIE, OtherIDSource.fromValue("KIE"));
        assertEquals(OtherIDSource.PIP, OtherIDSource.fromValue("PIP"));
        assertEquals(OtherIDSource.POP, OtherIDSource.fromValue("POP"));
        assertEquals(OtherIDSource.ARPL, OtherIDSource.fromValue("ARPL"));
        assertEquals(OtherIDSource.CPC, OtherIDSource.fromValue("CPC"));
        assertEquals(OtherIDSource.IND, OtherIDSource.fromValue("IND"));
        assertEquals(OtherIDSource.CPFH, OtherIDSource.fromValue("CPFH"));
        assertEquals(OtherIDSource.CLML, OtherIDSource.fromValue("CLML"));
        assertEquals(OtherIDSource.NRCBL, OtherIDSource.fromValue("NRCBL"));
    }

    @Test
    @DisplayName("OtherIDSource: getValue() 메서드")
    void testOtherIDSource_GetValue() {
        assertEquals("NASA", OtherIDSource.NASA.getValue());
        assertEquals("KIE", OtherIDSource.KIE.getValue());
        assertEquals("NRCBL", OtherIDSource.NRCBL.getValue());
    }

    @Test
    @DisplayName("OtherIDSource: fromValue() null 케이스")
    void testOtherIDSource_FromValue_Null() {
        // OtherIDSource는 예외를 던지지 않고 null을 반환
        assertNull(OtherIDSource.fromValue("invalid"));
        assertNull(OtherIDSource.fromValue(null));
    }

    // ========================================
    // Owner Tests
    // ========================================

    @Test
    @DisplayName("Owner: fromValue() 정상 케이스")
    void testOwner_FromValue_Success() {
        assertEquals(Owner.NLM, Owner.fromValue("NLM"));
        assertEquals(Owner.NASA, Owner.fromValue("NASA"));
        assertEquals(Owner.PIP, Owner.fromValue("PIP"));
        assertEquals(Owner.KIE, Owner.fromValue("KIE"));
        assertEquals(Owner.HSR, Owner.fromValue("HSR"));
        assertEquals(Owner.HMD, Owner.fromValue("HMD"));
        assertEquals(Owner.NOTNLM, Owner.fromValue("NOTNLM"));
    }

    @Test
    @DisplayName("Owner: getValue() 메서드")
    void testOwner_GetValue() {
        assertEquals("NLM", Owner.NLM.getValue());
        assertEquals("NASA", Owner.NASA.getValue());
        assertEquals("NOTNLM", Owner.NOTNLM.getValue());
    }

    @Test
    @DisplayName("Owner: fromValue() 예외 케이스")
    void testOwner_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Owner.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown Owner value"));
    }

    // ========================================
    // PubModel Tests
    // ========================================

    @Test
    @DisplayName("PubModel: fromValue() 정상 케이스")
    void testPubModel_FromValue_Success() {
        assertEquals(PubModel.Print, PubModel.fromValue("Print"));
        assertEquals(PubModel.Print_Electronic, PubModel.fromValue("Print-Electronic"));
        assertEquals(PubModel.Electronic, PubModel.fromValue("Electronic"));
        assertEquals(PubModel.Electronic_Print, PubModel.fromValue("Electronic-Print"));
        assertEquals(PubModel.Electronic_eCollection, PubModel.fromValue("Electronic-eCollection"));
    }

    @Test
    @DisplayName("PubModel: getValue() 메서드")
    void testPubModel_GetValue() {
        assertEquals("Print", PubModel.Print.getValue());
        assertEquals("Print-Electronic", PubModel.Print_Electronic.getValue());
        assertEquals("Electronic-eCollection", PubModel.Electronic_eCollection.getValue());
    }

    @Test
    @DisplayName("PubModel: fromValue() 예외 케이스")
    void testPubModel_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> PubModel.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown PubModel value"));
    }

    // ========================================
    // PubStatus Tests
    // ========================================

    @Test
    @DisplayName("PubStatus: fromValue() 정상 케이스")
    void testPubStatus_FromValue_Success() {
        assertEquals(PubStatus.RECEIVED, PubStatus.fromValue("received"));
        assertEquals(PubStatus.ACCEPTED, PubStatus.fromValue("accepted"));
        assertEquals(PubStatus.EPUBLISH, PubStatus.fromValue("epublish"));
        assertEquals(PubStatus.PPUBLISH, PubStatus.fromValue("ppublish"));
        assertEquals(PubStatus.REVISED, PubStatus.fromValue("revised"));
        assertEquals(PubStatus.PMC, PubStatus.fromValue("pmc"));
        assertEquals(PubStatus.PMC_RELEASE, PubStatus.fromValue("pmc-release"));
        assertEquals(PubStatus.PUBMED, PubStatus.fromValue("pubmed"));
        assertEquals(PubStatus.PUBMEDR, PubStatus.fromValue("pubmedr"));
        assertEquals(PubStatus.PREMEDLINE, PubStatus.fromValue("premedline"));
        assertEquals(PubStatus.MEDLINE, PubStatus.fromValue("medline"));
        assertEquals(PubStatus.MEDLINER, PubStatus.fromValue("medliner"));
        assertEquals(PubStatus.ENTREZ, PubStatus.fromValue("entrez"));
    }

    @Test
    @DisplayName("PubStatus: getValue() 메서드")
    void testPubStatus_GetValue() {
        assertEquals("received", PubStatus.RECEIVED.getValue());
        assertEquals("accepted", PubStatus.ACCEPTED.getValue());
        assertEquals("pubmed", PubStatus.PUBMED.getValue());
    }

    @Test
    @DisplayName("PubStatus: fromValue() 예외 케이스")
    void testPubStatus_FromValue_Null() {
        assertNull(PubStatus.fromValue("invalid"));
        assertNull(PubStatus.fromValue(null));
    }

    // ========================================
    // RefType Tests
    // ========================================

    @Test
    @DisplayName("RefType: fromValue() 정상 케이스")
    void testRefType_FromValue_Success() {
        assertEquals(RefType.ASSOCIATED_DATASET, RefType.fromValue("AssociatedDataset"));
        assertEquals(RefType.ASSOCIATED_PUBLICATION, RefType.fromValue("AssociatedPublication"));
        assertEquals(RefType.COMMENT_IN, RefType.fromValue("CommentIn"));
        assertEquals(RefType.COMMENT_ON, RefType.fromValue("CommentOn"));
        assertEquals(RefType.CORRECTED_AND_REPUBLISHED_IN, RefType.fromValue("CorrectedandRepublishedIn"));
        assertEquals(RefType.CORRECTED_AND_REPUBLISHED_FROM, RefType.fromValue("CorrectedandRepublishedFrom"));
        assertEquals(RefType.ERRATUM_IN, RefType.fromValue("ErratumIn"));
        assertEquals(RefType.ERRATUM_FOR, RefType.fromValue("ErratumFor"));
        assertEquals(RefType.EXPRESSION_OF_CONCERN_IN, RefType.fromValue("ExpressionOfConcernIn"));
        assertEquals(RefType.EXPRESSION_OF_CONCERN_FOR, RefType.fromValue("ExpressionOfConcernFor"));
        assertEquals(RefType.REPUBLISHED_IN, RefType.fromValue("RepublishedIn"));
        assertEquals(RefType.REPUBLISHED_FROM, RefType.fromValue("RepublishedFrom"));
        assertEquals(RefType.RETRACTED_AND_REPUBLISHED_IN, RefType.fromValue("RetractedandRepublishedIn"));
        assertEquals(RefType.RETRACTED_AND_REPUBLISHED_FROM, RefType.fromValue("RetractedandRepublishedFrom"));
        assertEquals(RefType.RETRACTION_IN, RefType.fromValue("RetractionIn"));
        assertEquals(RefType.RETRACTION_OF, RefType.fromValue("RetractionOf"));
        assertEquals(RefType.UPDATE_IN, RefType.fromValue("UpdateIn"));
        assertEquals(RefType.UPDATE_OF, RefType.fromValue("UpdateOf"));
        assertEquals(RefType.SUMMARY_FOR_PATIENTS_IN, RefType.fromValue("SummaryForPatientsIn"));
        assertEquals(RefType.ORIGINAL_REPORT_IN, RefType.fromValue("OriginalReportIn"));
        assertEquals(RefType.REPRINT_IN, RefType.fromValue("ReprintIn"));
        assertEquals(RefType.REPRINT_OF, RefType.fromValue("ReprintOf"));
        assertEquals(RefType.CITES, RefType.fromValue("Cites"));
    }

    @Test
    @DisplayName("RefType: getValue() 메서드")
    void testRefType_GetValue() {
        assertEquals("CommentIn", RefType.COMMENT_IN.getValue());
        assertEquals("ErratumIn", RefType.ERRATUM_IN.getValue());
        assertEquals("RetractionOf", RefType.RETRACTION_OF.getValue());
        assertEquals("Cites", RefType.CITES.getValue());
    }

    @Test
    @DisplayName("RefType: fromValue() null 케이스")
    void testRefType_FromValue_Null() {
        // RefType은 예외를 던지지 않고 null을 반환
        assertNull(RefType.fromValue("invalid"));
        assertNull(RefType.fromValue(null));
    }

    // ========================================
    // Status Tests
    // ========================================

    @Test
    @DisplayName("Status: fromValue() 정상 케이스")
    void testStatus_FromValue_Success() {
        assertEquals(Status.Completed, Status.fromValue("Completed"));
        assertEquals(Status.In_Process, Status.fromValue("In-Process"));
        assertEquals(Status.PubMed_not_MEDLINE, Status.fromValue("PubMed-not-MEDLINE"));
        assertEquals(Status.In_Data_Review, Status.fromValue("In-Data-Review"));
        assertEquals(Status.Publisher, Status.fromValue("Publisher"));
        assertEquals(Status.MEDLINE, Status.fromValue("MEDLINE"));
        assertEquals(Status.OLDMEDLINE, Status.fromValue("OLDMEDLINE"));
    }

    @Test
    @DisplayName("Status: getValue() 메서드")
    void testStatus_GetValue() {
        assertEquals("Completed", Status.Completed.getValue());
        assertEquals("In-Process", Status.In_Process.getValue());
        assertEquals("MEDLINE", Status.MEDLINE.getValue());
    }

    @Test
    @DisplayName("Status: fromValue() 예외 케이스")
    void testStatus_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> Status.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown Status value"));
    }

    // ========================================
    // SupplMeshNameType Tests
    // ========================================

    @Test
    @DisplayName("SupplMeshNameType: fromValue() 정상 케이스")
    void testSupplMeshNameType_FromValue_Success() {
        assertEquals(SupplMeshNameType.Disease, SupplMeshNameType.fromValue("Disease"));
        assertEquals(SupplMeshNameType.Organism, SupplMeshNameType.fromValue("Organism"));
        assertEquals(SupplMeshNameType.Protocol, SupplMeshNameType.fromValue("Protocol"));
    }

    @Test
    @DisplayName("SupplMeshNameType: getValue() 메서드")
    void testSupplMeshNameType_GetValue() {
        assertEquals("Disease", SupplMeshNameType.Disease.getValue());
        assertEquals("Organism", SupplMeshNameType.Organism.getValue());
        assertEquals("Protocol", SupplMeshNameType.Protocol.getValue());
    }

    @Test
    @DisplayName("SupplMeshNameType: fromValue() 예외 케이스")
    void testSupplMeshNameType_FromValue_Exception() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> SupplMeshNameType.fromValue("invalid")
        );
        assertTrue(exception.getMessage().contains("Unknown SupplMeshNameType value"));
    }
}
