package com.brillianttiger.bio.parser.pmc;

import com.brillianttiger.bio.parser.pmc.model.*;
import com.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PmcXmlParserTest / PMC XML 파서 테스트
 *
 * KR: PMC XML 파서의 모든 기능을 테스트하는 종합 테스트 클래스 (JATS 1.4 DTD 기준)
 * EN: Comprehensive test class for all PMC XML parser functionality (based on JATS 1.4 DTD)
 *
 * Required Test Cases (11):
 * 1. testParseSimpleArticle() - Simple article parsing
 * 2. testParseContributorWithOrcid() - ORCID contributor parsing
 * 3. testParseNestedSections() - 5-level nested sections
 * 4. testParseElementCitation() - Structured references
 * 5. testParseMixedCitation() - Mixed-citation references
 * 6. testParseXhtmlTable() - XHTML table parsing
 * 7. testParseFigureWithGraphic() - Figure and graphic elements
 * 8. testParseSubArticleRecursive() - Recursive sub-article parsing
 * 9. testParseFloatsGroup() - Floats-group element
 * 10. testParseTarGzPackage() - Tar.gz package parsing
 * 11. testValidateTarGzIntegrity() - Tar.gz integrity validation
 *
 * Additional Test Cases:
 * 12-25. Legacy tests for compatibility
 */
class PmcXmlParserTest {

    private static PmcXmlParser parser;
    private static Path sampleXmlPath;

    @BeforeAll
    static void setUp() {
        parser = new PmcXmlParser();
        // src/test/resources/sample-pmc.xml
        sampleXmlPath = Paths.get("src/test/resources/sample-pmc.xml");
    }

    // ==================== Required Tests (11) ====================

    /**
     * 테스트 1: 간단한 Article 파싱 / Test 1: Simple Article Parsing
     *
     * KR: 기본 Article 요소들이 올바르게 파싱되는지 검증
     * EN: Verify basic Article elements are parsed correctly
     */
    @Test
    void testParseSimpleArticle() throws Exception {
        // Given
        Path simplePath = Paths.get("src/test/resources/pmc/simple_article.xml");

        // When
        JatsArticle article = parser.parseFile(simplePath);

        // Then
        assertNotNull(article, "파싱 결과가 null이 아니어야 함 / Parse result should not be null");
        assertEquals("research-article", article.getArticleType().getValue(), "Article type 검증 / Verify article type");
        assertEquals("1.4", article.getDtdVersion(), "DTD version 검증 / Verify DTD version");
        assertEquals("en", article.getXmlLang(), "언어 속성 검증 / Verify language attribute");

        // Verify Front
        assertNotNull(article.getFront(), "Front가 null이 아니어야 함 / Front should not be null");
        ArticleMeta articleMeta = article.getFront().getArticleMeta();
        assertNotNull(articleMeta, "ArticleMeta가 null이 아니어야 함 / ArticleMeta should not be null");

        // Verify Article IDs
        assertNotNull(articleMeta.getArticleIds(), "Article ID 리스트가 null이 아니어야 함 / Article ID list should not be null");
        assertTrue(articleMeta.getArticleIds().size() >= 3, "Article ID 3개 이상 / Should have at least 3 article IDs");

        PmcArticleId pmcId = articleMeta.getArticleIds().stream()
                .filter(id -> "pmc".equals(id.getPubIdType()))
                .findFirst()
                .orElse(null);
        assertNotNull(pmcId, "PMC ID가 존재해야 함 / PMC ID should exist");
        assertEquals("PMC1234567", pmcId.getValue());

        // Verify Title
        assertNotNull(articleMeta.getTitleGroup(), "TitleGroup이 null이 아니어야 함 / TitleGroup should not be null");
        assertNotNull(articleMeta.getTitleGroup().getArticleTitle(), "ArticleTitle이 null이 아니어야 함 / ArticleTitle should not be null");
        assertTrue(articleMeta.getTitleGroup().getArticleTitle().getContent().contains("Simple JATS Article"));

        // Verify Contributor
        assertNotNull(articleMeta.getContribGroups(), "ContribGroup 리스트가 null이 아니어야 함 / ContribGroup list should not be null");
        assertTrue(articleMeta.getContribGroups().size() > 0, "최소 1개의 ContribGroup / Should have at least 1 ContribGroup");

        Contrib firstAuthor = articleMeta.getContribGroups().get(0).getContributors().get(0);
        assertNotNull(firstAuthor.getName(), "Name이 null이 아니어야 함 / Name should not be null");
        assertEquals("Smith", firstAuthor.getName().getSurname().getValue());
        assertEquals("John", firstAuthor.getName().getGivenNames().getValue());

        // Verify Body
        assertNotNull(article.getBody(), "Body가 null이 아니어야 함 / Body should not be null");
        assertNotNull(article.getBody().getSections(), "Section 리스트가 null이 아니어야 함 / Section list should not be null");
        assertTrue(article.getBody().getSections().size() > 0, "최소 1개의 Section / Should have at least 1 section");
    }

    /**
     * 테스트 2: ORCID가 있는 Contributor 파싱 / Test 2: Contributor with ORCID Parsing
     *
     * KR: ORCID 속성이 올바르게 파싱되는지 검증
     * EN: Verify ORCID attribute is parsed correctly
     */
    @Test
    void testParseContributorWithOrcid() throws Exception {
        // Given
        Path fullPath = Paths.get("src/test/resources/pmc/full_article.xml");

        // When
        JatsArticle article = parser.parseFile(fullPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getContribGroups(), "ContribGroup 리스트가 null이 아니어야 함 / ContribGroup list should not be null");
        assertFalse(articleMeta.getContribGroups().isEmpty(), "ContribGroup이 비어있지 않아야 함 / ContribGroup should not be empty");

        ContribGroup contribGroup = articleMeta.getContribGroups().get(0);
        assertNotNull(contribGroup.getContributors(), "Contributor 리스트가 null이 아니어야 함 / Contributor list should not be null");
        assertFalse(contribGroup.getContributors().isEmpty(), "Contributor가 비어있지 않아야 함 / Contributor should not be empty");

        // Find contributor with ORCID
        Contrib contributorWithOrcid = contribGroup.getContributors().stream()
                .filter(c -> c.getContribIds() != null && !c.getContribIds().isEmpty())
                .findFirst()
                .orElse(null);

        assertNotNull(contributorWithOrcid, "ORCID가 있는 Contributor가 존재해야 함 / Contributor with ORCID should exist");
        assertNotNull(contributorWithOrcid.getContribIds(), "ContribId 리스트가 null이 아니어야 함 / ContribId list should not be null");
        assertFalse(contributorWithOrcid.getContribIds().isEmpty(), "ContribId가 비어있지 않아야 함 / ContribId should not be empty");

        ContribId orcidId = contributorWithOrcid.getContribIds().stream()
                .filter(id -> id.getContribIdType() == ContribIdType.ORCID)
                .findFirst()
                .orElse(null);

        assertNotNull(orcidId, "ORCID ID가 존재해야 함 / ORCID ID should exist");
        assertEquals(ContribIdType.ORCID, orcidId.getContribIdType(), "ContribIdType이 ORCID여야 함 / ContribIdType should be ORCID");
        assertTrue(orcidId.getValue().startsWith("0000-"), "ORCID 형식 검증 / Verify ORCID format");
        assertEquals("0000-0001-2345-6789", orcidId.getValue(), "정확한 ORCID 값 검증 / Verify exact ORCID value");

        // Verify other contributor attributes
        assertEquals("author", contributorWithOrcid.getContribType(), "ContribType 검증 / Verify contrib type");
        assertEquals("yes", contributorWithOrcid.getCorresp(), "Corresponding author 검증 / Verify corresponding author");
    }

    /**
     * 테스트 3: 중첩된 Section 파싱 (5단계) / Test 3: Nested Sections Parsing (5 levels)
     *
     * KR: 5단계 중첩 섹션이 올바르게 재귀 파싱되는지 검증
     * EN: Verify 5-level nested sections are parsed recursively
     */
    @Test
    void testParseNestedSections() throws Exception {
        // Given
        Path nestedPath = Paths.get("src/test/resources/pmc/nested_sections.xml");

        // When
        JatsArticle article = parser.parseFile(nestedPath);
        Body body = article.getBody();

        // Then
        assertNotNull(body, "Body가 null이 아니어야 함 / Body should not be null");
        assertNotNull(body.getSections(), "Section 리스트가 null이 아니어야 함 / Section list should not be null");
        assertEquals(3, body.getSections().size(), "최상위 Section 3개 / Should have 3 top-level sections");

        // Level 1: sec1
        Sec level1 = body.getSections().stream()
                .filter(s -> "sec1".equals(s.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(level1, "Level 1 section이 존재해야 함 / Level 1 section should exist");
        assertEquals("1", level1.getLabel().getValue(), "Label 검증 / Verify label");
        assertEquals("Level 1: Introduction", level1.getTitle().getValue(), "Title 검증 / Verify title");

        // Level 2: sec1-1
        assertNotNull(level1.getSections(), "Level 2 section 리스트가 null이 아니어야 함 / Level 2 section list should not be null");
        assertEquals(2, level1.getSections().size(), "Level 2 section 2개 / Should have 2 level-2 sections");

        Sec level2 = level1.getSections().stream()
                .filter(s -> "sec1-1".equals(s.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(level2, "Level 2 section이 존재해야 함 / Level 2 section should exist");
        assertEquals("1.1", level2.getLabel().getValue());
        assertEquals("Level 2: Background", level2.getTitle().getValue());

        // Level 3: sec1-1-1
        assertNotNull(level2.getSections(), "Level 3 section 리스트가 null이 아니어야 함 / Level 3 section list should not be null");
        assertEquals(2, level2.getSections().size(), "Level 3 section 2개 / Should have 2 level-3 sections");

        Sec level3 = level2.getSections().stream()
                .filter(s -> "sec1-1-1".equals(s.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(level3, "Level 3 section이 존재해야 함 / Level 3 section should exist");
        assertEquals("1.1.1", level3.getLabel().getValue());
        assertEquals("Level 3: Historical Context", level3.getTitle().getValue());

        // Level 4: sec1-1-1-1
        assertNotNull(level3.getSections(), "Level 4 section 리스트가 null이 아니어야 함 / Level 4 section list should not be null");
        assertEquals(2, level3.getSections().size(), "Level 4 section 2개 / Should have 2 level-4 sections");

        Sec level4 = level3.getSections().stream()
                .filter(s -> "sec1-1-1-1".equals(s.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(level4, "Level 4 section이 존재해야 함 / Level 4 section should exist");
        assertEquals("1.1.1.1", level4.getLabel().getValue());
        assertEquals("Level 4: Early Studies", level4.getTitle().getValue());

        // Level 5: sec1-1-1-1-1 (deepest level)
        assertNotNull(level4.getSections(), "Level 5 section 리스트가 null이 아니어야 함 / Level 5 section list should not be null");
        assertEquals(2, level4.getSections().size(), "Level 5 section 2개 / Should have 2 level-5 sections");

        Sec level5 = level4.getSections().stream()
                .filter(s -> "sec1-1-1-1-1".equals(s.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(level5, "Level 5 section이 존재해야 함 / Level 5 section should exist");
        assertEquals("1.1.1.1.1", level5.getLabel().getValue());
        assertEquals("Level 5: Foundational Work", level5.getTitle().getValue());
        assertTrue(level5.getParagraphs().get(0).getValue().contains("Fifth level of nesting"));

        // Verify no Level 6 (should be null or empty)
        assertTrue(level5.getSections() == null || level5.getSections().isEmpty(),
                "Level 6 section이 없어야 함 / Level 6 sections should not exist");

        // Verify another Level 5 sibling section
        Sec level5Sibling = level4.getSections().stream()
                .filter(s -> "sec1-1-1-1-2".equals(s.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(level5Sibling, "Level 5 sibling section이 존재해야 함 / Level 5 sibling section should exist");
        assertEquals("1.1.1.1.2", level5Sibling.getLabel().getValue());
        assertEquals("Level 5: Pioneering Research", level5Sibling.getTitle().getValue());
    }

    /**
     * 테스트 4: ElementCitation 파싱 / Test 4: Element-Citation Parsing
     *
     * KR: 구조화된 참고문헌(element-citation)이 올바르게 파싱되는지 검증
     * EN: Verify structured references (element-citation) are parsed correctly
     */
    @Test
    void testParseElementCitation() throws Exception {
        // Given
        Path structuredPath = Paths.get("src/test/resources/pmc/structured_refs.xml");

        // When
        JatsArticle article = parser.parseFile(structuredPath);
        Back back = article.getBack();

        // Then
        assertNotNull(back, "Back이 null이 아니어야 함 / Back should not be null");
        assertNotNull(back.getRefLists(), "RefList가 null이 아니어야 함 / RefList should not be null");
        assertFalse(back.getRefLists().isEmpty(), "RefList가 비어있지 않아야 함 / RefList should not be empty");

        RefList refList = back.getRefLists().get(0);
        assertNotNull(refList.getReferences(), "Reference 리스트가 null이 아니어야 함 / Reference list should not be null");
        assertTrue(refList.getReferences().size() >= 5, "Reference 5개 이상 / Should have at least 5 references");

        // Verify first reference (journal article)
        Ref ref1 = refList.getReferences().stream()
                .filter(r -> "ref1".equals(r.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(ref1, "Reference 1이 존재해야 함 / Reference 1 should exist");
        assertEquals("1", ref1.getLabel().getValue(), "Label 검증 / Verify label");

        assertNotNull(ref1.getElementCitations(), "ElementCitation 리스트가 null이 아니어야 함 / ElementCitation list should not be null");
        assertFalse(ref1.getElementCitations().isEmpty(), "ElementCitation이 비어있지 않아야 함 / ElementCitation should not be empty");
        assertEquals("journal", ref1.getElementCitations().get(0).getPublicationType(), "Publication type 검증 / Verify publication type");

        ElementCitation citation1 = ref1.getElementCitations().get(0);
        assertNotNull(citation1.getPersonGroups(), "PersonGroup 리스트가 null이 아니어야 함 / PersonGroup list should not be null");
        assertFalse(citation1.getPersonGroups().isEmpty(), "PersonGroup이 비어있지 않아야 함 / PersonGroup should not be empty");

        PersonGroup authorGroup = citation1.getPersonGroups().get(0);
        assertEquals("author", authorGroup.getPersonGroupType(), "PersonGroupType 검증 / Verify person group type");
        assertNotNull(authorGroup.getNames(), "Name 리스트가 null이 아니어야 함 / Name list should not be null");
        assertEquals(3, authorGroup.getNames().size(), "저자 3명 / Should have 3 authors");

        PersonName firstAuthor = authorGroup.getNames().get(0);
        assertEquals("Smith", firstAuthor.getSurname().getValue());
        assertEquals("John A", firstAuthor.getGivenNames().getValue());

        assertNotNull(citation1.getArticleTitle(), "ArticleTitle이 null이 아니어야 함 / ArticleTitle should not be null");
        assertTrue(citation1.getArticleTitle().getValue().contains("comprehensive study"));

        assertNotNull(citation1.getSource(), "Source가 null이 아니어야 함 / Source should not be null");
        assertEquals("Journal of XML Standards", citation1.getSource().getValue());

        assertNotNull(citation1.getYear(), "Year가 null이 아니어야 함 / Year should not be null");
        assertEquals("2023", citation1.getYear().getValue());

        assertNotNull(citation1.getVolume(), "Volume이 null이 아니어야 함 / Volume should not be null");
        assertEquals("15", citation1.getVolume().getValue());

        assertNotNull(citation1.getPubIds(), "PubId 리스트가 null이 아니어야 함 / PubId list should not be null");
        assertTrue(citation1.getPubIds().size() >= 2, "PubId 2개 이상 / Should have at least 2 pub IDs");

        // Verify second reference (book)
        Ref ref2 = refList.getReferences().stream()
                .filter(r -> "ref2".equals(r.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(ref2, "Reference 2가 존재해야 함 / Reference 2 should exist");

        assertNotNull(ref2.getElementCitations(), "ElementCitation 리스트가 null이 아니어야 함 / ElementCitation list should not be null");
        assertFalse(ref2.getElementCitations().isEmpty(), "ElementCitation이 비어있지 않아야 함 / ElementCitation should not be empty");

        ElementCitation citation2 = ref2.getElementCitations().get(0);
        assertNotNull(citation2, "ElementCitation이 null이 아니어야 함 / ElementCitation should not be null");
        assertEquals("book", citation2.getPublicationType(), "Publication type이 book이어야 함 / Publication type should be book");

        assertNotNull(citation2.getPublisherName(), "PublisherName이 null이 아니어야 함 / PublisherName should not be null");
        assertEquals("Academic Press", citation2.getPublisherName().getValue());
    }

    /**
     * 테스트 5: MixedCitation 파싱 / Test 5: Mixed-Citation Parsing
     *
     * KR: 혼합 형식 참고문헌(mixed-citation)이 올바르게 파싱되는지 검증
     * EN: Verify mixed-format references (mixed-citation) are parsed correctly
     */
    @Test
    void testParseMixedCitation() throws Exception {
        // Given
        Path mixedPath = Paths.get("src/test/resources/pmc/mixed_refs.xml");

        // When
        JatsArticle article = parser.parseFile(mixedPath);
        Back back = article.getBack();

        // Then
        assertNotNull(back, "Back이 null이 아니어야 함 / Back should not be null");
        assertNotNull(back.getRefLists(), "RefList가 null이 아니어야 함 / RefList should not be null");
        assertFalse(back.getRefLists().isEmpty(), "RefList가 비어있지 않아야 함 / RefList should not be empty");

        RefList refList = back.getRefLists().get(0);
        assertNotNull(refList.getReferences(), "Reference 리스트가 null이 아니어야 함 / Reference list should not be null");
        assertTrue(refList.getReferences().size() >= 3, "Reference 3개 이상 / Should have at least 3 references");

        // Verify first mixed citation (journal)
        Ref ref1 = refList.getReferences().stream()
                .filter(r -> "ref1".equals(r.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(ref1, "Reference 1이 존재해야 함 / Reference 1 should exist");
        assertEquals("1", ref1.getLabel().getValue());

        assertNotNull(ref1.getMixedCitations(), "MixedCitation 리스트가 null이 아니어야 함 / MixedCitation list should not be null");
        assertFalse(ref1.getMixedCitations().isEmpty(), "MixedCitation이 비어있지 않아야 함 / MixedCitation should not be empty");
        assertEquals("journal", ref1.getMixedCitations().get(0).getPublicationType(), "Publication type 검증 / Verify publication type");

        MixedCitation mixed1 = ref1.getMixedCitations().get(0);
        assertNotNull(mixed1.getValue(), "Value가 null이 아니어야 함 / Value should not be null");
        assertTrue(mixed1.getValue().contains("comprehensive study"), "Value 검증 / Verify value");

        // Verify that mixed citation can contain both structured and unstructured content
        assertNotNull(mixed1.getStringNames(), "StringName 리스트가 null이 아니어야 함 / StringName list should not be null");
        assertFalse(mixed1.getStringNames().isEmpty(), "StringName이 비어있지 않아야 함 / StringName should not be empty");

        // Verify second mixed citation (book)
        Ref ref2 = refList.getReferences().stream()
                .filter(r -> "ref2".equals(r.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(ref2, "Reference 2가 존재해야 함 / Reference 2 should exist");

        assertNotNull(ref2.getMixedCitations(), "MixedCitation 리스트가 null이 아니어야 함 / MixedCitation list should not be null");
        assertFalse(ref2.getMixedCitations().isEmpty(), "MixedCitation이 비어있지 않아야 함 / MixedCitation should not be empty");

        MixedCitation mixed2 = ref2.getMixedCitations().get(0);
        assertNotNull(mixed2, "MixedCitation이 null이 아니어야 함 / MixedCitation should not be null");
        assertEquals("book", mixed2.getPublicationType(), "Publication type이 book이어야 함 / Publication type should be book");
        assertTrue(mixed2.getValue().contains("Complete Guide"), "Value 검증 / Verify value");

        // Verify third mixed citation (conference)
        Ref ref3 = refList.getReferences().stream()
                .filter(r -> "ref3".equals(r.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(ref3, "Reference 3이 존재해야 함 / Reference 3 should exist");

        assertNotNull(ref3.getMixedCitations(), "MixedCitation 리스트가 null이 아니어야 함 / MixedCitation list should not be null");
        assertFalse(ref3.getMixedCitations().isEmpty(), "MixedCitation이 비어있지 않아야 함 / MixedCitation should not be empty");

        MixedCitation mixed3 = ref3.getMixedCitations().get(0);
        assertNotNull(mixed3, "MixedCitation이 null이 아니어야 함 / MixedCitation should not be null");
        assertEquals("confproc", mixed3.getPublicationType(), "Publication type이 confproc이어야 함 / Publication type should be confproc");
    }

    /**
     * 테스트 6: XHTML Table 파싱 / Test 6: XHTML Table Parsing
     *
     * KR: XHTML 테이블이 올바르게 파싱되는지 검증 (floats-group 내)
     * EN: Verify XHTML tables are parsed correctly (in floats-group)
     */
    @Test
    void testParseXhtmlTable() throws Exception {
        // Given - floats_group.xml에 테이블이 포함되어 있음 / floats_group.xml contains tables
        Path floatsPath = Paths.get("src/test/resources/pmc/floats_group.xml");

        // When
        JatsArticle article = parser.parseFile(floatsPath);
        FloatsGroup floatsGroup = article.getFloatsGroup();

        // Then
        assertNotNull(floatsGroup, "FloatsGroup이 null이 아니어야 함 / FloatsGroup should not be null");
        assertNotNull(floatsGroup.getTableWraps(), "TableWrap 리스트가 null이 아니어야 함 / TableWrap list should not be null");
        assertTrue(floatsGroup.getTableWraps().size() >= 2, "TableWrap 2개 이상 / Should have at least 2 table wraps");

        // Verify Table 1: Simple table
        TableWrap table1 = floatsGroup.getTableWraps().stream()
                .filter(t -> "tbl1".equals(t.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(table1, "Table 1이 존재해야 함 / Table 1 should exist");
        assertEquals("Table 1", table1.getLabel().getValue(), "Label 검증 / Verify label");

        assertNotNull(table1.getCaptions(), "Caption 리스트가 null이 아니어야 함 / Caption list should not be null");
        assertFalse(table1.getCaptions().isEmpty(), "Caption이 비어있지 않아야 함 / Caption should not be empty");
        assertTrue(table1.getCaptions().get(0).getTitle().getValue().contains("Sample Characteristics"));

        assertNotNull(table1.getTables(), "Table 리스트가 null이 아니어야 함 / Table list should not be null");
        assertFalse(table1.getTables().isEmpty(), "Table이 비어있지 않아야 함 / Table should not be empty");

        Table firstTable = table1.getTables().get(0);
        assertEquals(TableFrame.HSIDES, firstTable.getFrame(), "Frame 속성 검증 / Verify frame attribute");
        assertEquals(TableRules.GROUPS, firstTable.getRules(), "Rules 속성 검증 / Verify rules attribute");

        assertNotNull(firstTable.getThead(), "Thead가 null이 아니어야 함 / Thead should not be null");
        assertNotNull(firstTable.getTbodies(), "Tbody 리스트가 null이 아니어야 함 / Tbody list should not be null");
        assertFalse(firstTable.getTbodies().isEmpty(), "Tbody가 비어있지 않아야 함 / Tbody should not be empty");

        // Verify thead structure
        assertNotNull(firstTable.getThead().getRows(), "Thead rows가 null이 아니어야 함 / Thead rows should not be null");
        assertEquals(1, firstTable.getThead().getRows().size(), "Thead row 1개 / Should have 1 thead row");

        Tr headerRow = firstTable.getThead().getRows().get(0);
        assertNotNull(headerRow.getHeaderCells(), "Header cells가 null이 아니어야 함 / Header cells should not be null");
        assertEquals(4, headerRow.getHeaderCells().size(), "Header cell 4개 / Should have 4 header cells");

        // Verify tbody structure
        Tbody firstTbody = firstTable.getTbodies().get(0);
        assertNotNull(firstTbody.getRows(), "Tbody rows가 null이 아니어야 함 / Tbody rows should not be null");
        assertTrue(firstTbody.getRows().size() >= 3, "Tbody row 3개 이상 / Should have at least 3 tbody rows");

        // Verify table-wrap-foot
        assertNotNull(table1.getTableWrapFoots(), "TableWrapFoot 리스트가 null이 아니어야 함 / TableWrapFoot list should not be null");
        assertFalse(table1.getTableWrapFoots().isEmpty(), "TableWrapFoot이 비어있지 않아야 함 / TableWrapFoot should not be empty");

        // Verify Table 2: Complex table
        TableWrap table2 = floatsGroup.getTableWraps().stream()
                .filter(t -> "tbl2".equals(t.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(table2, "Table 2가 존재해야 함 / Table 2 should exist");

        assertNotNull(table2.getTables(), "Table 리스트가 null이 아니어야 함 / Table list should not be null");
        assertFalse(table2.getTables().isEmpty(), "Table이 비어있지 않아야 함 / Table should not be empty");

        Table secondTable = table2.getTables().get(0);
        assertEquals(TableFrame.BOX, secondTable.getFrame(), "Frame이 box여야 함 / Frame should be box");
        assertEquals(TableRules.ALL, secondTable.getRules(), "Rules가 all이어야 함 / Rules should be all");

        // Verify tfoot
        assertNotNull(secondTable.getTfoot(), "Tfoot이 null이 아니어야 함 / Tfoot should not be null");
        assertNotNull(secondTable.getTfoot().getRows(), "Tfoot rows가 null이 아니어야 함 / Tfoot rows should not be null");
        assertTrue(secondTable.getTfoot().getRows().size() >= 1, "Tfoot row 1개 이상 / Should have at least 1 tfoot row");
    }

    /**
     * 테스트 7: Figure와 Graphic 파싱 / Test 7: Figure and Graphic Parsing
     *
     * KR: Figure와 Graphic 요소가 올바르게 파싱되는지 검증 (floats-group 내)
     * EN: Verify Figure and Graphic elements are parsed correctly (in floats-group)
     */
    @Test
    void testParseFigureWithGraphic() throws Exception {
        // Given - floats_group.xml에 그림이 포함되어 있음 / floats_group.xml contains figures
        Path floatsPath = Paths.get("src/test/resources/pmc/floats_group.xml");

        // When
        JatsArticle article = parser.parseFile(floatsPath);
        FloatsGroup floatsGroup = article.getFloatsGroup();

        // Then
        assertNotNull(floatsGroup, "FloatsGroup이 null이 아니어야 함 / FloatsGroup should not be null");
        assertNotNull(floatsGroup.getFigs(), "Figure 리스트가 null이 아니어야 함 / Figure list should not be null");
        assertTrue(floatsGroup.getFigs().size() >= 3, "Figure 3개 이상 / Should have at least 3 figures");

        // Verify Figure 1: Simple figure with single graphic
        Fig fig1 = floatsGroup.getFigs().stream()
                .filter(f -> "fig1".equals(f.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(fig1, "Figure 1이 존재해야 함 / Figure 1 should exist");
        assertEquals("Figure 1", fig1.getLabel().getValue(), "Label 검증 / Verify label");
        assertEquals(Position.FLOAT, fig1.getPosition(), "Position 검증 / Verify position");

        assertNotNull(fig1.getCaptions(), "Caption 리스트가 null이 아니어야 함 / Caption list should not be null");
        assertFalse(fig1.getCaptions().isEmpty(), "Caption이 비어있지 않아야 함 / Caption should not be empty");
        assertNotNull(fig1.getCaptions().get(0).getTitle(), "Caption title이 null이 아니어야 함 / Caption title should not be null");
        assertTrue(fig1.getCaptions().get(0).getTitle().getValue().contains("Experimental Setup"));

        assertNotNull(fig1.getGraphics(), "Graphics 리스트가 null이 아니어야 함 / Graphics list should not be null");
        assertEquals(1, fig1.getGraphics().size(), "Graphic 1개 / Should have 1 graphic");

        Graphic graphic1 = fig1.getGraphics().get(0);
        assertEquals("floats-fig1-setup.tif", graphic1.getXlinkHref(), "xlink:href 검증 / Verify xlink:href");
        assertEquals("image", graphic1.getMimetype(), "Mimetype 검증 / Verify mimetype");
        assertEquals("tiff", graphic1.getMimeSubtype(), "Mime-subtype 검증 / Verify mime-subtype");

        // Verify Figure 2: Multiple graphic versions
        Fig fig2 = floatsGroup.getFigs().stream()
                .filter(f -> "fig2".equals(f.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(fig2, "Figure 2가 존재해야 함 / Figure 2 should exist");

        assertNotNull(fig2.getGraphics(), "Graphics 리스트가 null이 아니어야 함 / Graphics list should not be null");
        assertEquals(2, fig2.getGraphics().size(), "Graphic 2개 (TIFF, JPEG) / Should have 2 graphics");

        // Verify different graphic formats
        Graphic tiffGraphic = fig2.getGraphics().stream()
                .filter(g -> g.getXlinkHref().endsWith(".tif"))
                .findFirst()
                .orElse(null);
        assertNotNull(tiffGraphic, "TIFF graphic이 존재해야 함 / TIFF graphic should exist");

        Graphic jpegGraphic = fig2.getGraphics().stream()
                .filter(g -> g.getXlinkHref().endsWith(".jpg"))
                .findFirst()
                .orElse(null);
        assertNotNull(jpegGraphic, "JPEG graphic이 존재해야 함 / JPEG graphic should exist");
        assertEquals("jpeg", jpegGraphic.getMimeSubtype());

        // Verify chemical structure figure
        Fig chem1 = floatsGroup.getFigs().stream()
                .filter(f -> "chem1".equals(f.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(chem1, "Chemical structure figure가 존재해야 함 / Chemical structure figure should exist");
        assertEquals(FigType.CHEMICAL_STRUCTURE, chem1.getFigType(), "FigType이 chemical-structure여야 함 / FigType should be chemical-structure");

        // Verify FigGroup
        assertNotNull(floatsGroup.getFigGroups(), "FigGroup 리스트가 null이 아니어야 함 / FigGroup list should not be null");
        assertFalse(floatsGroup.getFigGroups().isEmpty(), "FigGroup이 비어있지 않아야 함 / FigGroup should not be empty");

        FigGroup figGroup1 = floatsGroup.getFigGroups().stream()
                .filter(fg -> "figgrp1".equals(fg.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(figGroup1, "FigGroup 1이 존재해야 함 / FigGroup 1 should exist");

        assertNotNull(figGroup1.getFigs(), "FigGroup의 Figure 리스트가 null이 아니어야 함 / FigGroup's figure list should not be null");
        assertEquals(3, figGroup1.getFigs().size(), "FigGroup에 Figure 3개 / Should have 3 figures in FigGroup");

        // Verify supplementary material
        assertNotNull(floatsGroup.getSupplementaryMaterials(), "Supplementary material 리스트가 null이 아니어야 함 / Supplementary material list should not be null");
        assertFalse(floatsGroup.getSupplementaryMaterials().isEmpty(), "Supplementary material이 비어있지 않아야 함 / Supplementary material should not be empty");
    }

    /**
     * 테스트 8: 재귀적 SubArticle 파싱 / Test 8: Recursive Sub-Article Parsing
     *
     * KR: Sub-article와 Response 요소가 올바르게 재귀 파싱되는지 검증
     * EN: Verify sub-article and response elements are parsed recursively
     */
    @Test
    void testParseSubArticleRecursive() throws Exception {
        // Given
        Path subArticlePath = Paths.get("src/test/resources/pmc/sub_article.xml");

        // When
        JatsArticle article = parser.parseFile(subArticlePath);

        // Then - Main article
        assertNotNull(article, "Main article이 null이 아니어야 함 / Main article should not be null");
        assertEquals("research-article", article.getArticleType().getValue(), "Main article type 검증 / Verify main article type");

        assertNotNull(article.getFront(), "Front가 null이 아니어야 함 / Front should not be null");
        assertNotNull(article.getFront().getArticleMeta(), "ArticleMeta가 null이 아니어야 함 / ArticleMeta should not be null");
        assertTrue(article.getFront().getArticleMeta().getTitleGroup().getArticleTitle().getContent()
                .contains("Main Article with Sub-Articles"));

        // Verify main article body
        assertNotNull(article.getBody(), "Body가 null이 아니어야 함 / Body should not be null");
        assertNotNull(article.getBody().getSections(), "Section 리스트가 null이 아니어야 함 / Section list should not be null");
        assertTrue(article.getBody().getSections().size() >= 2, "Main article section 2개 이상 / Should have at least 2 main sections");

        // Verify sub-articles
        assertNotNull(article.getSubArticles(), "SubArticle 리스트가 null이 아니어야 함 / SubArticle list should not be null");
        assertTrue(article.getSubArticles().size() >= 1, "SubArticle 1개 이상 / Should have at least 1 sub-article");

        // Verify first sub-article (commentary)
        SubArticle subArticle1 = article.getSubArticles().stream()
                .filter(sa -> "sub1".equals(sa.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(subArticle1, "SubArticle 1이 존재해야 함 / SubArticle 1 should exist");
        assertEquals(ArticleType.ARTICLE_COMMENTARY, subArticle1.getArticleType(), "SubArticle type 검증 / Verify sub-article type");
        assertEquals("en", subArticle1.getXmlLang(), "SubArticle language 검증 / Verify sub-article language");

        // Verify front-stub (simplified metadata for sub-articles)
        assertNotNull(subArticle1.getFrontStub(), "FrontStub이 null이 아니어야 함 / FrontStub should not be null");
        assertNotNull(subArticle1.getFrontStub().getArticleIds(), "ArticleIds가 null이 아니어야 함 / ArticleIds should not be null");
        assertFalse(subArticle1.getFrontStub().getArticleIds().isEmpty(), "ArticleIds가 비어있지 않아야 함 / ArticleIds should not be empty");

        PmcArticleId subDoi = subArticle1.getFrontStub().getArticleIds().stream()
                .filter(id -> "doi".equals(id.getPubIdType()))
                .findFirst()
                .orElse(null);
        assertNotNull(subDoi, "SubArticle DOI가 존재해야 함 / SubArticle DOI should exist");
        assertTrue(subDoi.getValue().contains("commentary1"));

        assertNotNull(subArticle1.getFrontStub().getTitleGroup(), "TitleGroup이 null이 아니어야 함 / TitleGroup should not be null");
        assertTrue(subArticle1.getFrontStub().getTitleGroup().getArticleTitle().getContent()
                .contains("Commentary: Critical Analysis"));

        assertNotNull(subArticle1.getFrontStub().getContribGroups(), "ContribGroup이 null이 아니어야 함 / ContribGroup should not be null");
        assertFalse(subArticle1.getFrontStub().getContribGroups().isEmpty(), "ContribGroup이 비어있지 않아야 함 / ContribGroup should not be empty");

        Contrib commentator = subArticle1.getFrontStub().getContribGroups().get(0).getContributors().get(0);
        assertEquals("Commentator", commentator.getName().getSurname().getValue());
        assertEquals("First", commentator.getName().getGivenNames().getValue());

        // Verify sub-article body
        assertNotNull(subArticle1.getBody(), "SubArticle body가 null이 아니어야 함 / SubArticle body should not be null");
        assertNotNull(subArticle1.getBody().getSections(), "SubArticle sections가 null이 아니어야 함 / SubArticle sections should not be null");
        assertEquals(3, subArticle1.getBody().getSections().size(), "SubArticle section 3개 / Should have 3 sub-article sections");

        Sec commentSec1 = subArticle1.getBody().getSections().stream()
                .filter(s -> "sub1-sec1".equals(s.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(commentSec1, "Commentary section 1이 존재해야 함 / Commentary section 1 should exist");
        assertEquals("Commentary Introduction", commentSec1.getTitle().getValue());

        // Verify sub-article back (references)
        assertNotNull(subArticle1.getBack(), "SubArticle back이 null이 아니어야 함 / SubArticle back should not be null");
        assertNotNull(subArticle1.getBack().getRefLists(), "RefLists가 null이 아니어야 함 / RefLists should not be null");
        assertFalse(subArticle1.getBack().getRefLists().isEmpty(), "RefLists가 비어있지 않아야 함 / RefLists should not be empty");
    }

    /**
     * 테스트 9: FloatsGroup 파싱 / Test 9: Floats-Group Parsing
     *
     * KR: Floats-group 요소가 올바르게 파싱되는지 검증
     * EN: Verify floats-group element is parsed correctly
     */
    @Test
    void testParseFloatsGroup() throws Exception {
        // Given
        Path floatsPath = Paths.get("src/test/resources/pmc/floats_group.xml");

        // When
        JatsArticle article = parser.parseFile(floatsPath);

        // Then
        assertNotNull(article, "Article이 null이 아니어야 함 / Article should not be null");

        // Verify floats-group exists
        assertNotNull(article.getFloatsGroup(), "FloatsGroup이 null이 아니어야 함 / FloatsGroup should not be null");

        FloatsGroup floatsGroup = article.getFloatsGroup();

        // Verify figures in floats-group
        assertNotNull(floatsGroup.getFigs(), "Figures 리스트가 null이 아니어야 함 / Figures list should not be null");
        assertTrue(floatsGroup.getFigs().size() >= 3, "Figure 3개 이상 / Should have at least 3 figures");

        Fig floatsFig1 = floatsGroup.getFigs().stream()
                .filter(f -> "fig1".equals(f.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(floatsFig1, "Floats Figure 1이 존재해야 함 / Floats Figure 1 should exist");
        assertEquals("Figure 1", floatsFig1.getLabel().getValue());
        assertNotNull(floatsFig1.getCaptions(), "Caption 리스트가 null이 아니어야 함 / Caption list should not be null");
        assertFalse(floatsFig1.getCaptions().isEmpty(), "Caption이 비어있지 않아야 함 / Caption should not be empty");
        assertTrue(floatsFig1.getCaptions().get(0).getTitle().getValue().contains("Experimental Setup"));

        // Verify tables in floats-group
        assertNotNull(floatsGroup.getTableWraps(), "TableWraps 리스트가 null이 아니어야 함 / TableWraps list should not be null");
        assertTrue(floatsGroup.getTableWraps().size() >= 2, "TableWrap 2개 이상 / Should have at least 2 table wraps");

        TableWrap floatsTable1 = floatsGroup.getTableWraps().stream()
                .filter(t -> "tbl1".equals(t.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(floatsTable1, "Floats Table 1이 존재해야 함 / Floats Table 1 should exist");
        assertEquals("Table 1", floatsTable1.getLabel().getValue());
        assertNotNull(floatsTable1.getCaptions(), "Caption 리스트가 null이 아니어야 함 / Caption list should not be null");
        assertFalse(floatsTable1.getCaptions().isEmpty(), "Caption이 비어있지 않아야 함 / Caption should not be empty");
        assertTrue(floatsTable1.getCaptions().get(0).getTitle().getValue().contains("Sample Characteristics"));

        // Verify boxed-text in floats-group
        assertNotNull(floatsGroup.getBoxedTexts(), "BoxedTexts 리스트가 null이 아니어야 함 / BoxedTexts list should not be null");
        assertTrue(floatsGroup.getBoxedTexts().size() >= 2, "BoxedText 2개 이상 / Should have at least 2 boxed texts");

        BoxedText box1 = floatsGroup.getBoxedTexts().stream()
                .filter(b -> "box1".equals(b.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(box1, "BoxedText 1이 존재해야 함 / BoxedText 1 should exist");
        assertEquals("Box 1", box1.getLabel().getValue());
        assertTrue(box1.getCaption().getTitle().getValue().contains("Key Concepts"));

        assertNotNull(box1.getSections(), "BoxedText sections가 null이 아니어야 함 / BoxedText sections should not be null");
        assertEquals(3, box1.getSections().size(), "BoxedText section 3개 / Should have 3 sections");

        // Verify fig-group in floats-group
        assertNotNull(floatsGroup.getFigGroups(), "FigGroups 리스트가 null이 아니어야 함 / FigGroups list should not be null");
        assertFalse(floatsGroup.getFigGroups().isEmpty(), "FigGroups가 비어있지 않아야 함 / FigGroups should not be empty");

        FigGroup figGroup = floatsGroup.getFigGroups().stream()
                .filter(fg -> "figgrp1".equals(fg.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(figGroup, "FigGroup이 존재해야 함 / FigGroup should exist");
        assertEquals("Figure 3", figGroup.getLabel().getValue());
        assertNotNull(figGroup.getCaptions(), "Caption 리스트가 null이 아니어야 함 / Caption list should not be null");
        assertFalse(figGroup.getCaptions().isEmpty(), "Caption이 비어있지 않아야 함 / Caption should not be empty");
        assertTrue(figGroup.getCaptions().get(0).getTitle().getValue().contains("Multi-Panel"));

        assertNotNull(figGroup.getFigs(), "FigGroup figures가 null이 아니어야 함 / FigGroup figures should not be null");
        assertEquals(3, figGroup.getFigs().size(), "FigGroup에 figure 3개 (A, B, C) / Should have 3 figures in FigGroup");

        // Verify supplementary-material in floats-group
        assertNotNull(floatsGroup.getSupplementaryMaterials(), "SupplementaryMaterials 리스트가 null이 아니어야 함 / SupplementaryMaterials list should not be null");
        assertFalse(floatsGroup.getSupplementaryMaterials().isEmpty(), "SupplementaryMaterials가 비어있지 않아야 함 / SupplementaryMaterials should not be empty");

        SupplementaryMaterial supp1 = floatsGroup.getSupplementaryMaterials().get(0);
        assertEquals("supp1", supp1.getId());
        assertEquals("application", supp1.getMimetype());
        assertEquals("pdf", supp1.getMimeSubtype());
        assertTrue(supp1.getXlinkHref().endsWith(".pdf"));
    }

    /**
     * 테스트 10: Tar.gz 패키지 파싱 / Test 10: Tar.gz Package Parsing
     *
     * KR: Tar.gz 압축된 PMC 패키지가 올바르게 파싱되는지 검증
     * EN: Verify tar.gz compressed PMC packages are parsed correctly
     */
    @Test
    void testParseTarGzPackage(@TempDir Path tempDir) throws Exception {
        // Given: Create a tar.gz package with XML file
        Path tarGzFile = tempDir.resolve("test-pmc-package.tar.gz");
        Path simpleXml = Paths.get("src/test/resources/pmc/simple_article.xml");

        // Create tar.gz package
        try (FileOutputStream fos = new FileOutputStream(tarGzFile.toFile());
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(gzos)) {

            // Add XML file to tar archive
            byte[] xmlContent = Files.readAllBytes(simpleXml);
            TarArchiveEntry entry = new TarArchiveEntry("PMC1234567/article.xml");
            entry.setSize(xmlContent.length);
            taos.putArchiveEntry(entry);
            taos.write(xmlContent);
            taos.closeArchiveEntry();
        }

        // When: Parse tar.gz package
        AtomicInteger articleCount = new AtomicInteger(0);
        try (FileInputStream fis = new FileInputStream(tarGzFile.toFile());
             GZIPInputStream gzis = new GZIPInputStream(fis);
             TarArchiveInputStream tais = new TarArchiveInputStream(gzis)) {

            TarArchiveEntry entry;
            while ((entry = tais.getNextEntry()) != null) {
                if (entry.isFile() && entry.getName().endsWith(".xml")) {
                    // Extract XML to temporary file and parse
                    Path tempXmlFile = tempDir.resolve("temp-article.xml");
                    try (FileOutputStream fos = new FileOutputStream(tempXmlFile.toFile())) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = tais.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                    }

                    // Parse extracted XML file
                    JatsArticle article = parser.parseFile(tempXmlFile);
                    assertNotNull(article, "Article이 null이 아니어야 함 / Article should not be null");
                    articleCount.incrementAndGet();

                    // Verify article content
                    assertEquals("research-article", article.getArticleType().getValue());
                    assertEquals("1.4", article.getDtdVersion());
                    assertNotNull(article.getFront().getArticleMeta());

                    // Clean up temp file
                    Files.deleteIfExists(tempXmlFile);
                }
            }
        }

        // Then
        assertEquals(1, articleCount.get(), "1개의 article 처리 확인 / Should process 1 article");
    }

    /**
     * 테스트 11: Tar.gz 무결성 검증 / Test 11: Tar.gz Integrity Validation
     *
     * KR: Tar.gz 파일의 무결성을 검증 (체크섬 대안)
     * EN: Verify tar.gz file integrity (checksum alternative)
     */
    @Test
    void testValidateTarGzIntegrity(@TempDir Path tempDir) throws Exception {
        // Given: Create valid and corrupted tar.gz files
        Path validTarGz = tempDir.resolve("valid-package.tar.gz");
        Path corruptedTarGz = tempDir.resolve("corrupted-package.tar.gz");
        Path simpleXml = Paths.get("src/test/resources/pmc/simple_article.xml");

        // Create valid tar.gz
        try (FileOutputStream fos = new FileOutputStream(validTarGz.toFile());
             GZIPOutputStream gzos = new GZIPOutputStream(fos);
             TarArchiveOutputStream taos = new TarArchiveOutputStream(gzos)) {

            byte[] xmlContent = Files.readAllBytes(simpleXml);
            TarArchiveEntry entry = new TarArchiveEntry("article.xml");
            entry.setSize(xmlContent.length);
            taos.putArchiveEntry(entry);
            taos.write(xmlContent);
            taos.closeArchiveEntry();
        }

        // Create corrupted tar.gz (truncated)
        byte[] validBytes = Files.readAllBytes(validTarGz);
        byte[] corruptedBytes = new byte[validBytes.length / 2]; // Truncate to half
        System.arraycopy(validBytes, 0, corruptedBytes, 0, corruptedBytes.length);
        Files.write(corruptedTarGz, corruptedBytes);

        // When & Then: Validate valid tar.gz
        boolean validResult = validateTarGzIntegrity(validTarGz);
        assertTrue(validResult, "Valid tar.gz는 무결성 검증 통과 / Valid tar.gz should pass integrity check");

        // Validate corrupted tar.gz
        boolean corruptedResult = validateTarGzIntegrity(corruptedTarGz);
        assertFalse(corruptedResult, "Corrupted tar.gz는 무결성 검증 실패 / Corrupted tar.gz should fail integrity check");

        // Additional test: Validate XML well-formedness
        boolean xmlValid = validateXmlWellFormedness(simpleXml);
        assertTrue(xmlValid, "XML이 well-formed여야 함 / XML should be well-formed");
    }

    /**
     * Tar.gz 무결성 검증 헬퍼 메서드 / Tar.gz integrity validation helper
     *
     * KR: Tar.gz 파일의 무결성을 검증하는 헬퍼 메서드
     * EN: Helper method to validate tar.gz file integrity
     */
    private boolean validateTarGzIntegrity(Path tarGzFile) {
        try (FileInputStream fis = new FileInputStream(tarGzFile.toFile());
             GZIPInputStream gzis = new GZIPInputStream(fis);
             TarArchiveInputStream tais = new TarArchiveInputStream(gzis)) {

            TarArchiveEntry entry;
            while ((entry = tais.getNextEntry()) != null) {
                if (entry.isFile()) {
                    // Try to read all bytes - will fail if corrupted
                    byte[] buffer = new byte[8192];
                    while (tais.read(buffer) != -1) {
                        // Just read to verify integrity
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false; // Corrupted file
        }
    }

    /**
     * XML Well-formedness 검증 헬퍼 메서드 / XML well-formedness validation helper
     *
     * KR: XML 파일의 Well-formedness를 검증하는 헬퍼 메서드
     * EN: Helper method to validate XML well-formedness
     */
    private boolean validateXmlWellFormedness(Path xmlFile) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            try (InputStream is = Files.newInputStream(xmlFile)) {
                XMLStreamReader reader = factory.createXMLStreamReader(is);
                while (reader.hasNext()) {
                    reader.next();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== Legacy Tests (for compatibility) ====================

    /**
     * 테스트 1: Article 속성 파싱 / Test 1: Article Attributes Parsing
     *
     * KR: Article 요소의 모든 속성이 올바르게 파싱되는지 검증
     * EN: Verify all Article element attributes are parsed correctly
     */
    @Test
    void testArticleAttributes() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);

        // Then
        assertNotNull(article, "파싱 결과가 null이 아니어야 함 / Parse result should not be null");
        assertEquals("research-article", article.getArticleType().getValue(), "Article type 검증 / Verify article type");
        assertEquals("1.3", article.getDtdVersion(), "DTD version 검증 / Verify DTD version");
        assertEquals("en", article.getXmlLang(), "언어 속성 검증 / Verify language attribute");
    }

    /**
     * 테스트 2: Journal Metadata 파싱 / Test 2: Journal Metadata Parsing
     *
     * KR: Journal-meta의 모든 하위 요소가 올바르게 파싱되는지 검증
     * EN: Verify all Journal-meta child elements are parsed correctly
     */
    @Test
    void testJournalMetadata() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        Front front = article.getFront();

        // Then
        assertNotNull(front, "Front가 null이 아니어야 함 / Front should not be null");

        JournalMeta journalMeta = front.getJournalMeta();
        assertNotNull(journalMeta, "JournalMeta가 null이 아니어야 함 / JournalMeta should not be null");

        // Journal IDs
        assertNotNull(journalMeta.getJournalIds(), "Journal ID 리스트가 null이 아니어야 함 / Journal ID list should not be null");
        assertEquals(4, journalMeta.getJournalIds().size(), "Journal ID 4개 확인 / Should have 4 journal IDs");

        JournalId nlmTaId = journalMeta.getJournalIds().stream()
                .filter(id -> id.getJournalIdType() == JournalIdType.NLM_TA)
                .findFirst()
                .orElse(null);
        assertNotNull(nlmTaId, "NLM-TA ID가 존재해야 함 / NLM-TA ID should exist");
        assertEquals("J Biomed Inform", nlmTaId.getValue());

        // Journal Title Group
        assertNotNull(journalMeta.getJournalTitleGroups(), "JournalTitleGroups가 null이 아니어야 함 / JournalTitleGroups should not be null");
        JournalTitleGroup titleGroup = journalMeta.getJournalTitleGroups().get(0);
        assertNotNull(titleGroup, "JournalTitleGroup이 null이 아니어야 함 / JournalTitleGroup should not be null");
        assertNotNull(titleGroup.getJournalTitles(), "JournalTitles가 null이 아니어야 함 / JournalTitles should not be null");
        assertEquals("Journal of Biomedical Informatics", titleGroup.getJournalTitles().get(0).getValue());

        // ISSN
        assertNotNull(journalMeta.getIssns(), "ISSN 리스트가 null이 아니어야 함 / ISSN list should not be null");
        assertEquals(2, journalMeta.getIssns().size(), "ISSN 2개 확인 / Should have 2 ISSNs");

        Issn ppubIssn = journalMeta.getIssns().stream()
                .filter(issn -> issn.getPubType() != null && issn.getPubType() == PubType.PPUB)
                .findFirst()
                .orElse(null);
        assertNotNull(ppubIssn, "Print ISSN이 존재해야 함 / Print ISSN should exist");
        assertEquals("1532-0464", ppubIssn.getValue());

        // Publisher
        assertNotNull(journalMeta.getPublisher(), "Publisher가 null이 아니어야 함 / Publisher should not be null");
        assertNotNull(journalMeta.getPublisher().getPublisherNames(), "PublisherNames가 null이 아니어야 함 / PublisherNames should not be null");
        assertEquals("Elsevier Science", journalMeta.getPublisher().getPublisherNames().get(0).getValue());
    }

    /**
     * 테스트 3: Article Metadata 기본 정보 파싱 / Test 3: Article Metadata Basic Info Parsing
     *
     * KR: Article-meta의 기본 정보 (ID, 카테고리 등)가 올바르게 파싱되는지 검증
     * EN: Verify Article-meta basic information is parsed correctly
     */
    @Test
    void testArticleMetadataBasicInfo() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta, "ArticleMeta가 null이 아니어야 함 / ArticleMeta should not be null");

        // Article IDs
        assertNotNull(articleMeta.getArticleIds(), "Article ID 리스트가 null이 아니어야 함 / Article ID list should not be null");
        assertEquals(4, articleMeta.getArticleIds().size(), "Article ID 4개 확인 / Should have 4 article IDs");

        PmcArticleId pmid = articleMeta.getArticleIds().stream()
                .filter(id -> "pmid".equals(id.getPubIdType()))
                .findFirst()
                .orElse(null);
        assertNotNull(pmid, "PMID가 존재해야 함 / PMID should exist");
        assertEquals("12345678", pmid.getValue());

        PmcArticleId doi = articleMeta.getArticleIds().stream()
                .filter(id -> "doi".equals(id.getPubIdType()))
                .findFirst()
                .orElse(null);
        assertNotNull(doi, "DOI가 존재해야 함 / DOI should exist");
        assertEquals("10.1016/j.jbi.2023.104321", doi.getValue());

        // Article Categories
        assertNotNull(articleMeta.getArticleCategories(), "ArticleCategories가 null이 아니어야 함 / ArticleCategories should not be null");

        // Volume and Issue
        assertNotNull(articleMeta.getVolume(), "Volume이 null이 아니어야 함 / Volume should not be null");
        assertEquals("145", articleMeta.getVolume().getValue());

        assertNotNull(articleMeta.getIssue(), "Issue가 null이 아니어야 함 / Issue should not be null");
        assertEquals("3", articleMeta.getIssue().getValue());

        // Page information
        assertNotNull(articleMeta.getFpage(), "Fpage가 null이 아니어야 함 / Fpage should not be null");
        assertEquals("100", articleMeta.getFpage().getValue());

        assertNotNull(articleMeta.getLpage(), "Lpage가 null이 아니어야 함 / Lpage should not be null");
        assertEquals("125", articleMeta.getLpage().getValue());

        assertNotNull(articleMeta.getPageRange(), "PageRange가 null이 아니어야 함 / PageRange should not be null");
        assertEquals("100-125", articleMeta.getPageRange().getValue());
    }

    /**
     * 테스트 4: Title과 Authors 파싱 / Test 4: Title and Authors Parsing
     *
     * KR: 제목 그룹과 저자 정보가 올바르게 파싱되는지 검증
     * EN: Verify title group and author information are parsed correctly
     */
    @Test
    void testTitleAndAuthors() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then: Title Group
        assertNotNull(articleMeta.getTitleGroup(), "TitleGroup이 null이 아니어야 함 / TitleGroup should not be null");

        ArticleTitle articleTitle = articleMeta.getTitleGroup().getArticleTitle();
        assertNotNull(articleTitle, "ArticleTitle이 null이 아니어야 함 / ArticleTitle should not be null");
        assertTrue(articleTitle.getContent().contains("Advanced XML Parsing"), "제목 내용 확인 / Verify title content");

        assertNotNull(articleMeta.getTitleGroup().getSubtitles(), "Subtitles가 null이 아니어야 함 / Subtitles should not be null");
        Subtitle subtitle = articleMeta.getTitleGroup().getSubtitles().get(0);
        assertNotNull(subtitle, "Subtitle이 null이 아니어야 함 / Subtitle should not be null");
        assertEquals("A Comprehensive Study", subtitle.getContent());

        // Contrib Groups
        assertNotNull(articleMeta.getContribGroups(), "ContribGroup 리스트가 null이 아니어야 함 / ContribGroup list should not be null");
        assertEquals(1, articleMeta.getContribGroups().size());

        ContribGroup contribGroup = articleMeta.getContribGroups().get(0);
        assertNotNull(contribGroup.getContributors(), "Contributor 리스트가 null이 아니어야 함 / Contributor list should not be null");
        assertEquals(3, contribGroup.getContributors().size(), "저자 3명 확인 / Should have 3 contributors");

        // First author (personal)
        Contrib firstAuthor = contribGroup.getContributors().get(0);
        assertEquals("author", firstAuthor.getContribType(), "ContribType 확인 / Verify contrib type");
        assertEquals("yes", firstAuthor.getCorresp(), "Corresponding author 확인 / Verify corresponding author");

        assertNotNull(firstAuthor.getContribIds(), "ContribId 리스트가 null이 아니어야 함 / ContribId list should not be null");
        assertEquals(1, firstAuthor.getContribIds().size());
        assertEquals(ContribIdType.ORCID, firstAuthor.getContribIds().get(0).getContribIdType());
        assertEquals("0000-0002-1234-5678", firstAuthor.getContribIds().get(0).getValue());

        Name name = firstAuthor.getName();
        assertNotNull(name, "Name이 null이 아니어야 함 / Name should not be null");
        assertEquals("Kim", name.getSurname().getValue());
        assertEquals("Yong-Min", name.getGivenNames().getValue());
        assertEquals("Dr.", name.getPrefix().getValue());
        assertEquals("PhD", name.getSuffix().getValue());

        assertNotNull(firstAuthor.getEmails(), "Emails가 null이 아니어야 함 / Emails should not be null");
        assertEquals("yongmin.kim@example.edu", firstAuthor.getEmails().get(0).getValue());

        assertNotNull(firstAuthor.getRoles(), "Roles가 null이 아니어야 함 / Roles should not be null");
        assertEquals("Principal Investigator", firstAuthor.getRoles().get(0).getValue());

        // Second author
        Contrib secondAuthor = contribGroup.getContributors().get(1);
        assertEquals("Lee", secondAuthor.getName().getSurname().getValue());
        assertEquals("Su-Jin", secondAuthor.getName().getGivenNames().getValue());

        assertNotNull(secondAuthor.getDegrees(), "Degrees가 null이 아니어야 함 / Degrees should not be null");
        assertEquals("MSc", secondAuthor.getDegrees().get(0).getValue());

        // Third author (collaborative)
        Contrib thirdAuthor = contribGroup.getContributors().get(2);
        assertNotNull(thirdAuthor.getCollab(), "Collab이 null이 아니어야 함 / Collab should not be null");
        assertEquals("Biomedical Informatics Research Consortium", thirdAuthor.getCollab().getValue());

        // Affiliations
        assertNotNull(articleMeta.getAffiliations(), "Affiliation 리스트가 null이 아니어야 함 / Affiliation list should not be null");
        assertEquals(2, articleMeta.getAffiliations().size(), "소속 2개 확인 / Should have 2 affiliations");

        Aff firstAff = articleMeta.getAffiliations().get(0);
        assertEquals("aff1", firstAff.getId());
    }

    /**
     * 테스트 5: Abstract와 Keywords 파싱 / Test 5: Abstract and Keywords Parsing
     *
     * KR: 초록과 키워드가 올바르게 파싱되는지 검증
     * EN: Verify abstract and keywords are parsed correctly
     */
    @Test
    void testAbstractAndKeywords() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then: Abstract
        assertNotNull(articleMeta.getAbstracts(), "Abstract 리스트가 null이 아니어야 함 / Abstract list should not be null");
        assertEquals(1, articleMeta.getAbstracts().size());

        PmcAbstract abstract1 = articleMeta.getAbstracts().get(0);
        assertNotNull(abstract1.getTitle(), "Abstract title이 null이 아니어야 함 / Abstract title should not be null");
        assertEquals("Abstract", abstract1.getTitle().getValue());

        assertNotNull(abstract1.getSections(), "Abstract section 리스트가 null이 아니어야 함 / Abstract section list should not be null");
        assertEquals(4, abstract1.getSections().size(), "Section 4개 확인 / Should have 4 sections");

        Sec backgroundSec = abstract1.getSections().stream()
                .filter(sec -> sec.getTitle() != null && "Background".equals(sec.getTitle().getValue()))
                .findFirst()
                .orElse(null);
        assertNotNull(backgroundSec, "Background section이 존재해야 함 / Background section should exist");
        assertNotNull(backgroundSec.getParagraphs(), "Paragraph 리스트가 null이 아니어야 함 / Paragraph list should not be null");
        assertTrue(backgroundSec.getParagraphs().get(0).getValue().contains("Biomedical literature mining"));

        // Translated Abstract
        assertNotNull(articleMeta.getTransAbstracts(), "TransAbstract 리스트가 null이 아니어야 함 / TransAbstract list should not be null");
        assertEquals(1, articleMeta.getTransAbstracts().size());

        TransAbstract transAbstract = articleMeta.getTransAbstracts().get(0);
        assertEquals("ko", transAbstract.getXmlLang());

        // Keywords
        assertNotNull(articleMeta.getKwdGroups(), "KwdGroup 리스트가 null이 아니어야 함 / KwdGroup list should not be null");
        assertEquals(2, articleMeta.getKwdGroups().size());

        KwdGroup authorKwds = articleMeta.getKwdGroups().stream()
                .filter(kg -> "author".equals(kg.getKwdGroupType()))
                .findFirst()
                .orElse(null);
        assertNotNull(authorKwds, "Author keywords가 존재해야 함 / Author keywords should exist");
        if (authorKwds.getTitles() != null && !authorKwds.getTitles().isEmpty()) {
            assertEquals("Keywords", authorKwds.getTitles().get(0).getValue());
        }

        assertNotNull(authorKwds.getKeywords(), "Keyword 리스트가 null이 아니어야 함 / Keyword list should not be null");
        assertEquals(5, authorKwds.getKeywords().size(), "Keyword 5개 확인 / Should have 5 keywords");

        Kwd firstKwd = authorKwds.getKeywords().get(0);
        assertEquals("XML parsing", firstKwd.getValue());
    }

    /**
     * 테스트 6: Publication Dates와 History 파싱 / Test 6: Publication Dates and History Parsing
     *
     * KR: 출판 날짜와 이력 정보가 올바르게 파싱되는지 검증
     * EN: Verify publication dates and history information are parsed correctly
     */
    @Test
    void testPublicationDatesAndHistory() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then: Publication Dates
        assertNotNull(articleMeta.getPubDates(), "PubDate 리스트가 null이 아니어야 함 / PubDate list should not be null");
        assertEquals(3, articleMeta.getPubDates().size(), "PubDate 3개 확인 / Should have 3 pub dates");

        PmcPubDate ppubDate = articleMeta.getPubDates().stream()
                .filter(pd -> "ppub".equals(pd.getPubType()))
                .findFirst()
                .orElse(null);
        assertNotNull(ppubDate, "Print pub date가 존재해야 함 / Print pub date should exist");
        assertEquals("15", ppubDate.getDay().getValue());
        assertEquals("03", ppubDate.getMonth().getValue());
        assertEquals("2024", ppubDate.getYear().getValue());

        PmcPubDate epubDate = articleMeta.getPubDates().stream()
                .filter(pd -> "epub".equals(pd.getPubType()))
                .findFirst()
                .orElse(null);
        assertNotNull(epubDate, "Electronic pub date가 존재해야 함 / Electronic pub date should exist");

        // History
        assertNotNull(articleMeta.getHistory(), "History가 null이 아니어야 함 / History should not be null");
        assertNotNull(articleMeta.getHistory().getDates(), "History Date 리스트가 null이 아니어야 함 / History Date list should not be null");
        assertEquals(3, articleMeta.getHistory().getDates().size(), "History date 3개 확인 / Should have 3 history dates");

        PmcDate receivedDate = articleMeta.getHistory().getDates().stream()
                .filter(date -> "received".equals(date.getDateType()))
                .findFirst()
                .orElse(null);
        assertNotNull(receivedDate, "Received date가 존재해야 함 / Received date should exist");
        assertEquals("10", receivedDate.getDay().getValue());
        assertEquals("11", receivedDate.getMonth().getValue());
        assertEquals("2023", receivedDate.getYear().getValue());

        PmcDate acceptedDate = articleMeta.getHistory().getDates().stream()
                .filter(date -> "accepted".equals(date.getDateType()))
                .findFirst()
                .orElse(null);
        assertNotNull(acceptedDate, "Accepted date가 존재해야 함 / Accepted date should exist");
    }

    /**
     * 테스트 7: Permissions와 Copyright 파싱 / Test 7: Permissions and Copyright Parsing
     *
     * KR: 권한과 저작권 정보가 올바르게 파싱되는지 검증
     * EN: Verify permissions and copyright information are parsed correctly
     */
    @Test
    void testPermissionsAndCopyright() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getPermissions(), "Permissions가 null이 아니어야 함 / Permissions should not be null");

        Permissions permissions = articleMeta.getPermissions();

        assertNotNull(permissions.getCopyrightStatements(), "CopyrightStatements가 null이 아니어야 함 / CopyrightStatements should not be null");
        assertFalse(permissions.getCopyrightStatements().isEmpty(), "CopyrightStatements가 비어있지 않아야 함 / CopyrightStatements should not be empty");
        assertTrue(permissions.getCopyrightStatements().get(0).getValue().contains("Copyright"));
        assertTrue(permissions.getCopyrightStatements().get(0).getValue().contains("2024"));

        assertNotNull(permissions.getCopyrightYears(), "CopyrightYears가 null이 아니어야 함 / CopyrightYears should not be null");
        assertFalse(permissions.getCopyrightYears().isEmpty(), "CopyrightYears가 비어있지 않아야 함 / CopyrightYears should not be empty");
        assertEquals("2024", permissions.getCopyrightYears().get(0).getValue());

        assertNotNull(permissions.getCopyrightHolders(), "CopyrightHolders가 null이 아니어야 함 / CopyrightHolders should not be null");
        assertFalse(permissions.getCopyrightHolders().isEmpty(), "CopyrightHolders가 비어있지 않아야 함 / CopyrightHolders should not be empty");
        assertEquals("Elsevier Science", permissions.getCopyrightHolders().get(0).getValue());

        assertNotNull(permissions.getLicenses(), "Licenses가 null이 아니어야 함 / Licenses should not be null");
        assertFalse(permissions.getLicenses().isEmpty(), "Licenses가 비어있지 않아야 함 / Licenses should not be empty");
        assertEquals("open-access", permissions.getLicenses().get(0).getLicenseType());
        assertTrue(permissions.getLicenses().get(0).getXlinkHref().contains("creativecommons.org"));
    }

    /**
     * 테스트 8: Body Section 파싱 / Test 8: Body Section Parsing
     *
     * KR: Body의 Section 구조가 올바르게 파싱되는지 검증
     * EN: Verify Body section structure is parsed correctly
     */
    @Test
    void testBodySectionParsing() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        Body body = article.getBody();

        // Then
        assertNotNull(body, "Body가 null이 아니어야 함 / Body should not be null");
        assertNotNull(body.getSections(), "Section 리스트가 null이 아니어야 함 / Section list should not be null");
        assertEquals(4, body.getSections().size(), "Section 4개 확인 / Should have 4 sections");

        // Introduction section
        Sec introSec = body.getSections().stream()
                .filter(sec -> "sec1".equals(sec.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(introSec, "Introduction section이 존재해야 함 / Introduction section should exist");
        assertEquals("1", introSec.getLabel().getValue());
        assertEquals("Introduction", introSec.getTitle().getValue());

        assertNotNull(introSec.getParagraphs(), "Paragraph 리스트가 null이 아니어야 함 / Paragraph list should not be null");
        assertTrue(introSec.getParagraphs().size() > 0, "Paragraph가 존재해야 함 / Should have paragraphs");

        // Nested subsections
        assertNotNull(introSec.getSections(), "Section 리스트가 null이 아니어야 함 / Section list should not be null");
        assertEquals(1, introSec.getSections().size(), "Subsection 1개 확인 / Should have 1 subsection");

        Sec motivationSec = introSec.getSections().get(0);
        assertEquals("sec1-1", motivationSec.getId());
        assertEquals("1.1", motivationSec.getLabel().getValue());
        assertEquals("Motivation", motivationSec.getTitle().getValue());

        // Results section
        Sec resultsSec = body.getSections().stream()
                .filter(sec -> "sec3".equals(sec.getId()))
                .findFirst()
                .orElse(null);
        assertNotNull(resultsSec, "Results section이 존재해야 함 / Results section should exist");

        // Note: Sec 모델에 figures와 tableWraps 필드가 없음 (DTD에서 sec 요소에 포함되지 않음)
        // Figure와 Table은 별도의 최상위 요소이거나 Body 또는 Article에 직접 속함
    }

    /**
     * 테스트 9: Back References 파싱 / Test 9: Back References Parsing
     *
     * KR: Back의 참고문헌 리스트가 올바르게 파싱되는지 검증
     * EN: Verify Back reference list is parsed correctly
     */
    @Test
    void testBackReferencesParsing() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        Back back = article.getBack();

        // Then
        assertNotNull(back, "Back이 null이 아니어야 함 / Back should not be null");

        // Acknowledgments
        assertNotNull(back.getAcknowledgments(), "Acknowledgments가 null이 아니어야 함 / Acknowledgments should not be null");

        // Reference List
        assertNotNull(back.getRefLists(), "RefLists가 null이 아니어야 함 / RefLists should not be null");
        RefList refList = back.getRefLists().get(0);
        assertNotNull(refList.getTitle(), "RefList title이 null이 아니어야 함 / RefList title should not be null");
        assertEquals("References", refList.getTitle().getValue());

        assertNotNull(refList.getReferences(), "Reference 리스트가 null이 아니어야 함 / Reference list should not be null");
        assertEquals(3, refList.getReferences().size(), "Reference 3개 확인 / Should have 3 references");

        // First reference (journal article)
        Ref firstRef = refList.getReferences().get(0);
        assertEquals("ref1", firstRef.getId());
        assertEquals("1", firstRef.getLabel().getValue());

        // Second reference (book)
        Ref secondRef = refList.getReferences().get(1);
        assertEquals("ref2", secondRef.getId());
        assertEquals("2", secondRef.getLabel().getValue());

        // Third reference (web)
        Ref thirdRef = refList.getReferences().get(2);
        assertEquals("ref3", thirdRef.getId());

        // Footnote Group
        assertNotNull(back.getFnGroups(), "FnGroups가 null이 아니어야 함 / FnGroups should not be null");
        FnGroup fnGroup = back.getFnGroups().get(0);
        assertNotNull(fnGroup.getFootnotes(), "Footnote 리스트가 null이 아니어야 함 / Footnote list should not be null");
        assertEquals(2, fnGroup.getFootnotes().size(), "Footnote 2개 확인 / Should have 2 footnotes");

        Fn conflictFn = fnGroup.getFootnotes().stream()
                .filter(fn -> "conflict".equals(fn.getFnType()))
                .findFirst()
                .orElse(null);
        assertNotNull(conflictFn, "Conflict of interest footnote가 존재해야 함 / Conflict footnote should exist");
    }

    /**
     * 테스트 10: Counts 파싱 / Test 10: Counts Parsing
     *
     * KR: Article counts 정보가 올바르게 파싱되는지 검증
     * EN: Verify article counts information is parsed correctly
     */
    @Test
    void testCountsParsing() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getCounts(), "Counts가 null이 아니어야 함 / Counts should not be null");

        Counts counts = articleMeta.getCounts();
        assertEquals(5, counts.getFigCount(), "Figure count 확인 / Verify figure count");
        assertEquals(3, counts.getTableCount(), "Table count 확인 / Verify table count");
        assertEquals(2, counts.getEquationCount(), "Equation count 확인 / Verify equation count");
        assertEquals(45, counts.getRefCount(), "Reference count 확인 / Verify reference count");
        assertEquals(26, counts.getPageCount(), "Page count 확인 / Verify page count");
    }

    /**
     * 테스트 11: 대용량 파일 스트리밍 / Test 11: Large File Streaming
     *
     * KR: 스트리밍 모드가 메모리 효율적으로 동작하는지 검증
     * EN: Verify streaming mode works memory-efficiently
     */
    @Test
    void testLargeFileStreaming() throws Exception {
        // Given: Create a large XML file with multiple articles
        Path largeXmlPath = createLargeXmlFile(50); // 50 articles

        // When: Parse using streaming mode
        AtomicInteger articleCount = new AtomicInteger(0);

        parser.parseStream(largeXmlPath, article -> {
            articleCount.incrementAndGet();
            assertNotNull(article.getFront(), "각 article의 Front가 null이 아니어야 함 / Each article's Front should not be null");
        });

        // Then: Verify all articles were processed
        assertEquals(50, articleCount.get(), "50개 article 처리 확인 / Should process 50 articles");

        // Clean up
        Files.deleteIfExists(largeXmlPath);
    }

    /**
     * 테스트 12: GZip 파일 처리 / Test 12: GZip File Handling
     *
     * KR: GZip으로 압축된 파일이 올바르게 처리되는지 검증
     * EN: Verify GZip compressed files are handled correctly
     */
    @Test
    void testGzipFileHandling(@TempDir Path tempDir) throws Exception {
        // Given: Create a gzipped XML file
        Path gzipFile = tempDir.resolve("test-pmc.xml.gz");

        try (FileOutputStream fos = new FileOutputStream(gzipFile.toFile());
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            Files.copy(sampleXmlPath, gzos);
        }

        // When: Parse the gzipped file
        JatsArticle article = parser.parseFile(gzipFile);

        // Then: Should parse successfully
        assertNotNull(article, "GZip 파일 파싱 결과가 null이 아니어야 함 / GZip file parse result should not be null");
        assertEquals("research-article", article.getArticleType().getValue());
    }

    /**
     * 테스트 13: Funding Group 파싱 / Test 13: Funding Group Parsing
     *
     * KR: 연구비 지원 정보가 올바르게 파싱되는지 검증
     * EN: Verify funding information is parsed correctly
     */
    @Test
    void testFundingGroupParsing() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getFundingGroups(), "FundingGroup 리스트가 null이 아니어야 함 / FundingGroup list should not be null");
        assertEquals(1, articleMeta.getFundingGroups().size());

        FundingGroup fundingGroup = articleMeta.getFundingGroups().get(0);
        assertNotNull(fundingGroup.getAwardGroups(), "AwardGroup 리스트가 null이 아니어야 함 / AwardGroup list should not be null");
        assertEquals(1, fundingGroup.getAwardGroups().size());
    }

    /**
     * 테스트 14: Author Notes 파싱 / Test 14: Author Notes Parsing
     *
     * KR: 저자 노트 정보가 올바르게 파싱되는지 검증
     * EN: Verify author notes information is parsed correctly
     */
    @Test
    void testAuthorNotesParsing() throws Exception {
        // When
        JatsArticle article = parser.parseFile(sampleXmlPath);
        ArticleMeta articleMeta = article.getFront().getArticleMeta();

        // Then
        assertNotNull(articleMeta.getAuthorNotes(), "AuthorNotes가 null이 아니어야 함 / AuthorNotes should not be null");

        AuthorNotes authorNotes = articleMeta.getAuthorNotes();
        assertNotNull(authorNotes.getCorresps(), "Corresps가 null이 아니어야 함 / Corresps should not be null");
        assertEquals("cor1", authorNotes.getCorresps().get(0).getId());

        assertNotNull(authorNotes.getFootnotes(), "Footnotes가 null이 아니어야 함 / Footnotes should not be null");
        assertEquals(1, authorNotes.getFootnotes().size());

        Fn contributionFn = authorNotes.getFootnotes().get(0);
        assertEquals("con", contributionFn.getFnType());
    }

    // ==================== Helper Methods ====================

    /**
     * 대용량 테스트용 XML 파일 생성 / Create large XML file for testing
     */
    private Path createLargeXmlFile(int articleCount) throws IOException {
        Path tempFile = Files.createTempFile("test-large-pmc-", ".xml");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<!DOCTYPE pmc-articleset PUBLIC \"-//NLM//DTD ARTICLE SET 2.0//EN\" \"https://dtd.nlm.nih.gov/ncbi/pmc/articleset/nlm-articleset-2.0.dtd\">\n");
            writer.write("<pmc-articleset>\n");

            for (int i = 1; i <= articleCount; i++) {
                writer.write(String.format(
                    "  <article article-type=\"research-article\" dtd-version=\"1.3\" xml:lang=\"en\">\n" +
                    "    <front>\n" +
                    "      <journal-meta>\n" +
                    "        <journal-title-group>\n" +
                    "          <journal-title>Test Journal</journal-title>\n" +
                    "        </journal-title-group>\n" +
                    "      </journal-meta>\n" +
                    "      <article-meta>\n" +
                    "        <article-id pub-id-type=\"pmid\">%d</article-id>\n" +
                    "        <title-group>\n" +
                    "          <article-title>Test Article %d</article-title>\n" +
                    "        </title-group>\n" +
                    "        <pub-date pub-type=\"epub\">\n" +
                    "          <year>2024</year>\n" +
                    "        </pub-date>\n" +
                    "      </article-meta>\n" +
                    "    </front>\n" +
                    "  </article>\n",
                    i, i
                ));
            }

            writer.write("</pmc-articleset>\n");
        }

        return tempFile;
    }
}
