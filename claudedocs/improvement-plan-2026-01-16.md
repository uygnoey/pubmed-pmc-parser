# 테스트 커버리지 개선 계획

**작성일:** 2026-01-16
**목표:** PMC 51% → 80%, Validation 0-1% → 80%
**예상 기간:** 5-7일

---

## 📊 현재 상태

| 항목 | 현재 | 목표 | 격차 |
|------|------|------|------|
| PMC Instruction | 51% | 80% | **-29%** |
| PMC Branch | 36% | 75% | **-39%** |
| PubMed Validation | 0% | 80% | **-80%** |
| PMC Validation | 1% | 80% | **-79%** |

---

## 🎯 Phase 1: PMC Body 파서 테스트 강화 (Day 1-2)

### 목표: 55% → 70% (+15%p)

### 1.1 중첩 섹션(Nested Sections) 테스트

**파일 생성:** `pmc/src/test/java/com/brillianttiger/bio/parser/pmc/parser/BodyParserDeepNestedTest.java`

```java
package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.*;
import io.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Body 파서 깊은 중첩 구조 테스트 / Body Parser Deep Nesting Test
 *
 * KR: Body 내부의 복잡한 중첩 구조를 테스트
 * EN: Tests complex nested structures within Body element
 *
 * Coverage Target: Body parsing 60% → 85%
 */
class BodyParserDeepNestedTest {

    private final PmcXmlParser parser = new PmcXmlParser();

    /**
     * 테스트 1: 5단계 중첩 섹션
     */
    @Test
    void testParseFiveLevelNestedSections(@TempDir Path tempDir) throws Exception {
        // Given: 5단계 중첩된 섹션 XML
        String xml = """
            <?xml version="1.0"?>
            <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                "JATS-archivearticle1-4.dtd">
            <article xmlns:xlink="http://www.w3.org/1999/xlink" dtd-version="1.4">
              <front>
                <article-meta>
                  <title-group>
                    <article-title>Deep Nested Article</article-title>
                  </title-group>
                </article-meta>
              </front>
              <body>
                <sec id="s1">
                  <label>1</label>
                  <title>Level 1 Section</title>
                  <p>Level 1 content</p>
                  <sec id="s1-1">
                    <label>1.1</label>
                    <title>Level 2 Section</title>
                    <p>Level 2 content</p>
                    <sec id="s1-1-1">
                      <label>1.1.1</label>
                      <title>Level 3 Section</title>
                      <p>Level 3 content</p>
                      <sec id="s1-1-1-1">
                        <label>1.1.1.1</label>
                        <title>Level 4 Section</title>
                        <p>Level 4 content</p>
                        <sec id="s1-1-1-1-1">
                          <label>1.1.1.1.1</label>
                          <title>Level 5 Section</title>
                          <p>Level 5 deepest content</p>
                        </sec>
                      </sec>
                    </sec>
                  </sec>
                </sec>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("deep_nested.xml");
        Files.writeString(xmlFile, xml);

        // When
        JatsArticle article = parser.parseFile(xmlFile);

        // Then
        Body body = article.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getSections()).hasSize(1);

        // Level 1
        Sec level1 = body.getSections().get(0);
        assertThat(level1.getId()).isEqualTo("s1");
        assertThat(level1.getLabel()).isNotNull();
        assertThat(level1.getLabel().getContent()).isEqualTo("1");
        assertThat(level1.getTitle()).isNotNull();
        assertThat(level1.getTitle().getContent()).contains("Level 1");

        // Level 2
        assertThat(level1.getSections()).hasSize(1);
        Sec level2 = level1.getSections().get(0);
        assertThat(level2.getId()).isEqualTo("s1-1");
        assertThat(level2.getLabel().getContent()).isEqualTo("1.1");

        // Level 3
        assertThat(level2.getSections()).hasSize(1);
        Sec level3 = level2.getSections().get(0);
        assertThat(level3.getId()).isEqualTo("s1-1-1");

        // Level 4
        assertThat(level3.getSections()).hasSize(1);
        Sec level4 = level3.getSections().get(0);
        assertThat(level4.getId()).isEqualTo("s1-1-1-1");

        // Level 5 (deepest)
        assertThat(level4.getSections()).hasSize(1);
        Sec level5 = level4.getSections().get(0);
        assertThat(level5.getId()).isEqualTo("s1-1-1-1-1");
        assertThat(level5.getTitle().getContent()).contains("Level 5");
        assertThat(level5.getParagraphs()).hasSize(1);
        assertThat(level5.getParagraphs().get(0).getContent()).contains("deepest");
    }

    /**
     * 테스트 2: 복잡한 리스트 구조 (중첩 리스트)
     */
    @Test
    void testParseNestedLists(@TempDir Path tempDir) throws Exception {
        String xml = """
            <?xml version="1.0"?>
            <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                "JATS-archivearticle1-4.dtd">
            <article dtd-version="1.4">
              <front>
                <article-meta>
                  <title-group><article-title>Nested Lists</article-title></title-group>
                </article-meta>
              </front>
              <body>
                <sec>
                  <title>Methods</title>
                  <list list-type="order">
                    <list-item>
                      <p>Step 1: Preparation</p>
                      <list list-type="bullet">
                        <list-item><p>Sub-step 1.1</p></list-item>
                        <list-item>
                          <p>Sub-step 1.2</p>
                          <list list-type="alpha-lower">
                            <list-item><p>Detail a</p></list-item>
                            <list-item><p>Detail b</p></list-item>
                          </list>
                        </list-item>
                      </list>
                    </list-item>
                    <list-item><p>Step 2: Execution</p></list-item>
                  </list>
                </sec>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("nested_lists.xml");
        Files.writeString(xmlFile, xml);

        // When
        JatsArticle article = parser.parseFile(xmlFile);

        // Then
        Body body = article.getBody();
        Sec sec = body.getSections().get(0);

        // Outer list
        assertThat(sec.getLists()).hasSize(1);
        PmcList outerList = sec.getLists().get(0);
        assertThat(outerList.getListType()).isEqualTo("order");
        assertThat(outerList.getListItems()).hasSize(2);

        // First item with nested list
        ListItem item1 = outerList.getListItems().get(0);
        assertThat(item1.getParagraphs()).hasSize(1);
        assertThat(item1.getParagraphs().get(0).getContent()).contains("Step 1");
        assertThat(item1.getNestedLists()).hasSize(1);

        // Nested bullet list
        PmcList nestedList = item1.getNestedLists().get(0);
        assertThat(nestedList.getListType()).isEqualTo("bullet");
        assertThat(nestedList.getListItems()).hasSize(2);

        // Third level alpha list
        ListItem item12 = nestedList.getListItems().get(1);
        assertThat(item12.getNestedLists()).hasSize(1);
        PmcList alphaList = item12.getNestedLists().get(0);
        assertThat(alphaList.getListType()).isEqualTo("alpha-lower");
        assertThat(alphaList.getListItems()).hasSize(2);
    }

    /**
     * 테스트 3: BoxedText 복잡한 내용
     */
    @Test
    void testParseBoxedTextWithComplexContent(@TempDir Path tempDir) throws Exception {
        String xml = """
            <?xml version="1.0"?>
            <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                "JATS-archivearticle1-4.dtd">
            <article dtd-version="1.4">
              <front>
                <article-meta>
                  <title-group><article-title>BoxedText Test</article-title></title-group>
                </article-meta>
              </front>
              <body>
                <sec>
                  <title>Key Findings</title>
                  <boxed-text id="box1">
                    <label>Box 1</label>
                    <caption>
                      <title>Important Note</title>
                      <p>This box contains critical information</p>
                    </caption>
                    <p>Main content paragraph 1</p>
                    <list list-type="bullet">
                      <list-item><p>Point 1</p></list-item>
                      <list-item><p>Point 2</p></list-item>
                    </list>
                    <p>Main content paragraph 2</p>
                    <sec>
                      <title>Sub-section in box</title>
                      <p>Nested section content</p>
                    </sec>
                  </boxed-text>
                </sec>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("boxed_text.xml");
        Files.writeString(xmlFile, xml);

        // When
        JatsArticle article = parser.parseFile(xmlFile);

        // Then
        Body body = article.getBody();
        Sec sec = body.getSections().get(0);

        assertThat(sec.getBoxedTexts()).hasSize(1);
        BoxedText box = sec.getBoxedTexts().get(0);
        assertThat(box.getId()).isEqualTo("box1");
        assertThat(box.getLabel()).isNotNull();
        assertThat(box.getLabel().getContent()).isEqualTo("Box 1");

        // Caption
        assertThat(box.getCaption()).isNotNull();
        assertThat(box.getCaption().getTitle()).isNotNull();
        assertThat(box.getCaption().getTitle().getContent()).contains("Important Note");

        // Content
        assertThat(box.getParagraphs()).hasSize(2);
        assertThat(box.getLists()).hasSize(1);
        assertThat(box.getSections()).hasSize(1);

        Sec nestedSec = box.getSections().get(0);
        assertThat(nestedSec.getTitle().getContent()).contains("Sub-section");
    }

    /**
     * 테스트 4: DispQuote 복잡한 인용
     */
    @Test
    void testParseDispQuoteWithAttribution(@TempDir Path tempDir) throws Exception {
        String xml = """
            <?xml version="1.0"?>
            <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                "JATS-archivearticle1-4.dtd">
            <article dtd-version="1.4">
              <front>
                <article-meta>
                  <title-group><article-title>Quote Test</article-title></title-group>
                </article-meta>
              </front>
              <body>
                <sec>
                  <title>Discussion</title>
                  <p>As the author states:</p>
                  <disp-quote>
                    <p>This is a multi-paragraph quote.</p>
                    <p>It contains important insights.</p>
                    <attrib>Smith et al., 2024</attrib>
                  </disp-quote>
                </sec>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("disp_quote.xml");
        Files.writeString(xmlFile, xml);

        // When
        JatsArticle article = parser.parseFile(xmlFile);

        // Then
        Body body = article.getBody();
        Sec sec = body.getSections().get(0);

        assertThat(sec.getDispQuotes()).hasSize(1);
        DispQuote quote = sec.getDispQuotes().get(0);
        assertThat(quote.getParagraphs()).hasSize(2);
        assertThat(quote.getParagraphs().get(0).getContent()).contains("multi-paragraph");
        assertThat(quote.getAttrib()).isNotNull();
        assertThat(quote.getAttrib().getContent()).contains("Smith et al.");
    }

    /**
     * 테스트 5: DefList (정의 리스트)
     */
    @Test
    void testParseDefinitionList(@TempDir Path tempDir) throws Exception {
        String xml = """
            <?xml version="1.0"?>
            <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                "JATS-archivearticle1-4.dtd">
            <article dtd-version="1.4">
              <front>
                <article-meta>
                  <title-group><article-title>DefList Test</article-title></title-group>
                </article-meta>
              </front>
              <body>
                <sec>
                  <title>Glossary</title>
                  <def-list>
                    <def-item>
                      <term>DNA</term>
                      <def>
                        <p>Deoxyribonucleic acid, the molecule carrying genetic information.</p>
                      </def>
                    </def-item>
                    <def-item>
                      <term>RNA</term>
                      <def>
                        <p>Ribonucleic acid, involved in protein synthesis.</p>
                        <p>Multiple forms exist including mRNA, tRNA, and rRNA.</p>
                      </def>
                    </def-item>
                  </def-list>
                </sec>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("def_list.xml");
        Files.writeString(xmlFile, xml);

        // When
        JatsArticle article = parser.parseFile(xmlFile);

        // Then
        Body body = article.getBody();
        Sec sec = body.getSections().get(0);

        assertThat(sec.getDefLists()).hasSize(1);
        DefList defList = sec.getDefLists().get(0);
        assertThat(defList.getDefItems()).hasSize(2);

        // First term
        DefItem item1 = defList.getDefItems().get(0);
        assertThat(item1.getTerm()).isNotNull();
        assertThat(item1.getTerm().getContent()).isEqualTo("DNA");
        assertThat(item1.getDef()).isNotNull();
        assertThat(item1.getDef().getParagraphs()).hasSize(1);

        // Second term with multiple paragraphs
        DefItem item2 = defList.getDefItems().get(1);
        assertThat(item2.getTerm().getContent()).isEqualTo("RNA");
        assertThat(item2.getDef().getParagraphs()).hasSize(2);
    }
}
```

**예상 커버리지 증가:** +10%p (55% → 65%)

---

### 1.2 Table 처리 테스트

**파일 생성:** `pmc/src/test/java/com/brillianttiger/bio/parser/pmc/parser/TableParserComplexTest.java`

```java
package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.*;
import io.brillianttiger.bio.parser.pmc.parser.PmcXmlParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

/**
 * 복잡한 테이블 파싱 테스트 / Complex Table Parsing Test
 *
 * Coverage Target: Table parsing 40% → 70%
 */
class TableParserComplexTest {

    private final PmcXmlParser parser = new PmcXmlParser();

    @Test
    void testParseTableWithColspanRowspan(@TempDir Path tempDir) throws Exception {
        String xml = """
            <?xml version="1.0"?>
            <!DOCTYPE article PUBLIC "-//NLM//DTD JATS (Z39.96) Journal Archiving and Interchange DTD v1.4 20250101//EN"
                "JATS-archivearticle1-4.dtd">
            <article dtd-version="1.4">
              <front>
                <article-meta>
                  <title-group><article-title>Complex Table</article-title></title-group>
                </article-meta>
              </front>
              <body>
                <table-wrap id="t1">
                  <label>Table 1</label>
                  <caption>
                    <title>Patient Demographics</title>
                    <p>Data from clinical trial (n=100)</p>
                  </caption>
                  <table frame="hsides" rules="groups">
                    <thead>
                      <tr>
                        <th rowspan="2">Parameter</th>
                        <th colspan="2" align="center">Treatment A</th>
                        <th colspan="2" align="center">Treatment B</th>
                      </tr>
                      <tr>
                        <th>Mean</th>
                        <th>SD</th>
                        <th>Mean</th>
                        <th>SD</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>Age (years)</td>
                        <td align="right">45.2</td>
                        <td align="right">12.3</td>
                        <td align="right">46.8</td>
                        <td align="right">11.9</td>
                      </tr>
                      <tr>
                        <td>Weight (kg)</td>
                        <td align="right">72.5</td>
                        <td align="right">8.4</td>
                        <td align="right">71.3</td>
                        <td align="right">9.1</td>
                      </tr>
                    </tbody>
                    <tfoot>
                      <tr>
                        <td colspan="5">SD = Standard Deviation</td>
                      </tr>
                    </tfoot>
                  </table>
                  <table-wrap-foot>
                    <fn id="t1f1">
                      <p>All values rounded to one decimal place</p>
                    </fn>
                  </table-wrap-foot>
                </table-wrap>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("complex_table.xml");
        Files.writeString(xmlFile, xml);

        // When
        JatsArticle article = parser.parseFile(xmlFile);

        // Then
        Body body = article.getBody();
        assertThat(body.getTableWraps()).hasSize(1);

        TableWrap tableWrap = body.getTableWraps().get(0);
        assertThat(tableWrap.getId()).isEqualTo("t1");
        assertThat(tableWrap.getLabel()).isNotNull();
        assertThat(tableWrap.getLabel().getContent()).isEqualTo("Table 1");

        // Caption
        Caption caption = tableWrap.getCaption();
        assertThat(caption).isNotNull();
        assertThat(caption.getTitle()).isNotNull();
        assertThat(caption.getParagraphs()).hasSize(1);

        // Table
        Table table = tableWrap.getTable();
        assertThat(table).isNotNull();
        assertThat(table.getFrame()).isEqualTo(TableFrame.HSIDES);
        assertThat(table.getRules()).isEqualTo(TableRules.GROUPS);

        // Thead - 2 rows
        assertThat(table.getThead()).isNotNull();
        assertThat(table.getThead().getRows()).hasSize(2);

        // First header row with colspan
        TableRow headerRow1 = table.getThead().getRows().get(0);
        assertThat(headerRow1.getCells()).hasSize(3);
        TableCell cell1 = headerRow1.getCells().get(0);
        assertThat(cell1.getRowspan()).isEqualTo(2);

        TableCell cell2 = headerRow1.getCells().get(1);
        assertThat(cell2.getColspan()).isEqualTo(2);
        assertThat(cell2.getAlign()).isEqualTo(CellAlign.CENTER);

        // Tbody
        assertThat(table.getTbody()).isNotNull();
        assertThat(table.getTbody().getRows()).hasSize(2);

        TableRow dataRow1 = table.getTbody().getRows().get(0);
        assertThat(dataRow1.getCells()).hasSize(5);
        assertThat(dataRow1.getCells().get(0).getContent()).contains("Age");

        // Tfoot
        assertThat(table.getTfoot()).isNotNull();
        assertThat(table.getTfoot().getRows()).hasSize(1);
        TableCell footCell = table.getTfoot().getRows().get(0).getCells().get(0);
        assertThat(footCell.getColspan()).isEqualTo(5);

        // Table wrap foot (footnotes)
        assertThat(tableWrap.getTableWrapFoot()).isNotNull();
        assertThat(tableWrap.getTableWrapFoot().getFootnotes()).hasSize(1);
    }
}
```

**예상 커버리지 증가:** +5%p (65% → 70%)

---

## 🎯 Phase 2: Validation 테스트 작성 (Day 3-4)

### 목표: 0-1% → 80%

### 2.1 PubMed Validation 테스트

**파일 생성:** `pubmed/src/test/java/com/brillianttiger/bio/parser/pubmed/validation/MedlineCitationValidatorTest.java`

```java
package io.brillianttiger.bio.parser.pubmed.validation;

import io.brillianttiger.bio.parser.common.validation.ValidationError;
import io.brillianttiger.bio.parser.pubmed.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * MedlineCitation 검증 테스트 / MedlineCitation Validation Test
 *
 * Coverage Target: 0% → 80%
 */
class MedlineCitationValidatorTest {

    private MedlineCitationValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MedlineCitationValidator();
    }

    @Test
    void testValidMedlineCitation() {
        // Given: 올바른 MedlineCitation
        MedlineCitation citation = MedlineCitation.builder()
            .pmid(Pmid.builder().value("12345678").build())
            .dateCreated(DateCreated.builder()
                .year("2024")
                .month("01")
                .day("15")
                .build())
            .article(Article.builder()
                .articleTitle(ArticleTitle.builder().value("Valid Title").build())
                .journal(Journal.builder()
                    .title("Test Journal")
                    .build())
                .build())
            .build();

        // When
        List<ValidationError> errors = validator.validate(citation);

        // Then
        assertThat(errors).isEmpty();
    }

    @Test
    void testMissingPmid() {
        // Given: PMID 없음
        MedlineCitation citation = MedlineCitation.builder()
            .pmid(null)
            .build();

        // When
        List<ValidationError> errors = validator.validate(citation);

        // Then
        assertThat(errors).isNotEmpty();
        assertThat(errors).anyMatch(e ->
            e.getMessage().contains("PMID") &&
            e.getMessage().contains("required")
        );
    }

    @Test
    void testInvalidPmidFormat() {
        // Given: 잘못된 PMID 형식
        MedlineCitation citation = MedlineCitation.builder()
            .pmid(Pmid.builder().value("ABC123").build()) // 숫자만 가능
            .build();

        // When
        List<ValidationError> errors = validator.validate(citation);

        // Then
        assertThat(errors).isNotEmpty();
        assertThat(errors).anyMatch(e ->
            e.getMessage().contains("PMID") &&
            e.getMessage().contains("numeric")
        );
    }

    @Test
    void testPmidTooShort() {
        // Given: PMID 너무 짧음 (최소 1자리)
        MedlineCitation citation = MedlineCitation.builder()
            .pmid(Pmid.builder().value("").build())
            .build();

        // When
        List<ValidationError> errors = validator.validate(citation);

        // Then
        assertThat(errors).isNotEmpty();
    }

    @Test
    void testPmidTooLong() {
        // Given: PMID 너무 김 (최대 8자리)
        MedlineCitation citation = MedlineCitation.builder()
            .pmid(Pmid.builder().value("123456789").build())
            .build();

        // When
        List<ValidationError> errors = validator.validate(citation);

        // Then
        assertThat(errors).isNotEmpty();
        assertThat(errors).anyMatch(e -> e.getMessage().contains("length"));
    }

    @Test
    void testMissingArticle() {
        // Given: Article 없음
        MedlineCitation citation = MedlineCitation.builder()
            .pmid(Pmid.builder().value("12345678").build())
            .article(null)
            .build();

        // When
        List<ValidationError> errors = validator.validate(citation);

        // Then
        assertThat(errors).anyMatch(e -> e.getMessage().contains("Article"));
    }

    @Test
    void testInvalidDateCreated() {
        // Given: 잘못된 날짜
        MedlineCitation citation = MedlineCitation.builder()
            .pmid(Pmid.builder().value("12345678").build())
            .dateCreated(DateCreated.builder()
                .year("2024")
                .month("13") // 13월은 없음
                .day("32")   // 32일은 없음
                .build())
            .build();

        // When
        List<ValidationError> errors = validator.validate(citation);

        // Then
        assertThat(errors).anyMatch(e -> e.getMessage().contains("month"));
        assertThat(errors).anyMatch(e -> e.getMessage().contains("day"));
    }

    @Test
    void testValidStatusValues() {
        // Given: 유효한 Status 값들
        String[] validStatuses = {"Completed", "In-Process", "PubMed", "MEDLINE",
                                  "In-Data-Review", "Publisher"};

        for (String status : validStatuses) {
            MedlineCitation citation = MedlineCitation.builder()
                .pmid(Pmid.builder().value("12345678").build())
                .status(status)
                .build();

            // When
            List<ValidationError> errors = validator.validate(citation);

            // Then
            assertThat(errors).noneMatch(e -> e.getMessage().contains("status"));
        }
    }

    @Test
    void testInvalidStatus() {
        // Given: 잘못된 Status 값
        MedlineCitation citation = MedlineCitation.builder()
            .pmid(Pmid.builder().value("12345678").build())
            .status("InvalidStatus")
            .build();

        // When
        List<ValidationError> errors = validator.validate(citation);

        // Then
        assertThat(errors).anyMatch(e ->
            e.getMessage().contains("status") &&
            e.getMessage().contains("invalid")
        );
    }
}
```

**예상 커버리지:** 0% → 85%

---

### 2.2 PMC Validation 테스트

**파일 생성:** `pmc/src/test/java/com/brillianttiger/bio/parser/pmc/validation/JatsArticleValidatorTest.java`

```java
package io.brillianttiger.bio.parser.pmc.validation;

import io.brillianttiger.bio.parser.common.validation.ValidationError;
import io.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * JATS Article 검증 테스트 / JATS Article Validation Test
 *
 * Coverage Target: 1% → 80%
 */
class JatsArticleValidatorTest {

    private JatsArticleValidator validator;

    @BeforeEach
    void setUp() {
        validator = new JatsArticleValidator();
    }

    @Test
    void testValidJatsArticle() {
        // Given
        JatsArticle article = JatsArticle.builder()
            .dtdVersion("1.4")
            .articleType(ArticleType.builder().value("research-article").build())
            .front(Front.builder()
                .articleMeta(ArticleMeta.builder()
                    .titleGroup(TitleGroup.builder()
                        .articleTitle(PmcArticleTitle.builder()
                            .content("Valid Title")
                            .build())
                        .build())
                    .build())
                .build())
            .build();

        // When
        List<ValidationError> errors = validator.validate(article);

        // Then
        assertThat(errors).isEmpty();
    }

    @Test
    void testMissingFront() {
        // Given
        JatsArticle article = JatsArticle.builder()
            .dtdVersion("1.4")
            .front(null)
            .build();

        // When
        List<ValidationError> errors = validator.validate(article);

        // Then
        assertThat(errors).anyMatch(e ->
            e.getMessage().contains("front") &&
            e.getMessage().contains("required")
        );
    }

    @Test
    void testMissingArticleMeta() {
        // Given
        JatsArticle article = JatsArticle.builder()
            .front(Front.builder()
                .articleMeta(null)
                .build())
            .build();

        // When
        List<ValidationError> errors = validator.validate(article);

        // Then
        assertThat(errors).anyMatch(e -> e.getMessage().contains("article-meta"));
    }

    @Test
    void testMissingArticleTitle() {
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
        List<ValidationError> errors = validator.validate(article);

        // Then
        assertThat(errors).anyMatch(e -> e.getMessage().contains("article-title"));
    }

    @Test
    void testInvalidDtdVersion() {
        // Given
        JatsArticle article = JatsArticle.builder()
            .dtdVersion("0.9") // 지원하지 않는 버전
            .build();

        // When
        List<ValidationError> errors = validator.validate(article);

        // Then
        assertThat(errors).anyMatch(e ->
            e.getMessage().contains("dtd-version") &&
            e.getMessage().contains("unsupported")
        );
    }

    @Test
    void testValidArticleTypes() {
        String[] validTypes = {"research-article", "review-article", "case-report",
                              "editorial", "letter", "correction", "retraction"};

        for (String type : validTypes) {
            JatsArticle article = JatsArticle.builder()
                .articleType(ArticleType.builder().value(type).build())
                .front(Front.builder()
                    .articleMeta(ArticleMeta.builder()
                        .titleGroup(TitleGroup.builder()
                            .articleTitle(PmcArticleTitle.builder()
                                .content("Test")
                                .build())
                            .build())
                        .build())
                    .build())
                .build();

            List<ValidationError> errors = validator.validate(article);
            assertThat(errors).noneMatch(e -> e.getMessage().contains("article-type"));
        }
    }

    @Test
    void testArticleIdValidation() {
        // Given: 중복된 pub-id-type
        JatsArticle article = JatsArticle.builder()
            .front(Front.builder()
                .articleMeta(ArticleMeta.builder()
                    .articleIds(List.of(
                        PmcArticleId.builder()
                            .pubIdType("pmc")
                            .value("PMC123456")
                            .build(),
                        PmcArticleId.builder()
                            .pubIdType("pmc") // 중복
                            .value("PMC789012")
                            .build()
                    ))
                    .titleGroup(TitleGroup.builder()
                        .articleTitle(PmcArticleTitle.builder()
                            .content("Test")
                            .build())
                        .build())
                    .build())
                .build())
            .build();

        // When
        List<ValidationError> errors = validator.validate(article);

        // Then
        assertThat(errors).anyMatch(e ->
            e.getMessage().contains("duplicate") &&
            e.getMessage().contains("pmc")
        );
    }
}
```

**예상 커버리지:** 1% → 80%

---

## 🎯 Phase 3: Branch Coverage 개선 (Day 5)

### 목표: Null/Optional 케이스 테스트

**파일 추가:** 기존 테스트 파일에 추가

```java
// PubMed 예시
@Test
void testParseArticleWithoutAbstract() {
    // Abstract가 없는 경우 (Optional)
}

@Test
void testParseAuthorWithoutAffiliation() {
    // 소속 정보 없는 저자
}

@Test
void testParseMedlineDateInsteadOfStructuredDate() {
    // 비표준 날짜 포맷
}

// PMC 예시
@Test
void testParseBodyWithoutSections() {
    // 섹션 없이 직접 paragraph만 있는 경우
}

@Test
void testParseTableWithoutCaption() {
    // Caption 없는 테이블
}

@Test
void testParseFigureWithoutGraphic() {
    // Graphic 없는 Figure
}
```

**예상 커버리지 증가:** +10-15%p

---

## 📅 실행 일정

| Day | 작업 | 파일 | 예상 시간 | 커버리지 목표 |
|-----|------|------|-----------|--------------|
| **Day 1** | Body 파서 중첩 구조 | BodyParserDeepNestedTest.java | 6h | PMC 55% → 65% |
| **Day 2** | Table/Figure 복잡 케이스 | TableParserComplexTest.java | 6h | PMC 65% → 70% |
| **Day 3** | PubMed Validation | MedlineCitationValidatorTest.java | 4h | PubMed Val 0% → 80% |
| **Day 4** | PMC Validation | JatsArticleValidatorTest.java | 4h | PMC Val 1% → 80% |
| **Day 5** | Branch Coverage | 기존 파일 확장 | 6h | Branch +10%p |
| **Day 6-7** | 통합 테스트 확대 + 버그 수정 | - | 8h | 최종 검증 |

---

## 📊 예상 최종 결과

| 항목 | 현재 | Day 2 | Day 4 | Day 5 | 최종 목표 |
|------|------|-------|-------|-------|-----------|
| PMC Instruction | 51% | 70% | 73% | 78% | **80%** ✅ |
| PMC Branch | 36% | 45% | 50% | 65% | **75%** ✅ |
| PubMed Validation | 0% | 0% | 80% | 85% | **80%** ✅ |
| PMC Validation | 1% | 1% | 80% | 82% | **80%** ✅ |

---

## ✅ 검증 방법

### 각 Phase 완료 후 실행

```bash
# 커버리지 확인
./gradlew test jacocoTestReport

# PubMed 커버리지
open pubmed/build/reports/jacoco/test/html/index.html

# PMC 커버리지
open pmc/build/reports/jacoco/test/html/index.html

# Common 커버리지
open common/build/reports/jacoco/test/html/index.html

# 전체 통합 리포트
./gradlew jacocoRootReport
open build/reports/jacoco/html/index.html
```

### 목표 달성 확인

```bash
# JaCoCo 커버리지 검증 (80% 이상)
./gradlew jacocoTestCoverageVerification

# 실패 시: 추가 테스트 작성 필요
# 성공 시: ✅ 목표 달성!
```

---

## 🚨 주의사항

### 1. 테스트 리소스 파일 준비
각 테스트는 `src/test/resources/` 디렉토리에 XML 샘플 파일이 필요합니다.
`@TempDir`를 사용하여 동적으로 생성하거나, 미리 준비된 파일을 사용할 수 있습니다.

### 2. AssertJ 의존성 확인
```gradle
testImplementation 'org.assertj:assertj-core:3.24.2'
```

### 3. 점진적 진행
- 한 번에 모든 테스트를 추가하지 말고, Phase별로 커밋
- 각 Phase 완료 후 커버리지 확인
- 목표 미달 시 추가 테스트 작성

### 4. 실제 파일 테스트
통합 테스트에서는 실제 PubMed/PMC 파일 사용:
- `test-data/pubmed/baseline/*.xml.gz`
- `test-data/pmc/*.tar.gz`

---

## 📝 커밋 메시지 예시

```
test(pmc): Add deep nested structure tests for Body parser

- Add BodyParserDeepNestedTest with 5-level nested sections
- Add nested list parsing tests
- Add BoxedText and DispQuote tests
- Coverage: PMC 55% → 65% (+10%p)

Related to #issue-number
```

---

## 🎯 성공 기준

**Phase 1 완료:**
- ✅ PMC Body 파서 커버리지 70% 달성
- ✅ 중첩 구조 5단계 테스트 통과
- ✅ Table colspan/rowspan 테스트 통과

**Phase 2 완료:**
- ✅ PubMed Validation 80% 커버리지
- ✅ PMC Validation 80% 커버리지
- ✅ 모든 검증 규칙 테스트됨

**Phase 3 완료:**
- ✅ Branch coverage 75%+ 달성
- ✅ Null/Optional 케이스 테스트
- ✅ 예외 처리 테스트

**최종 목표:**
- ✅ 전체 모듈 80% 커버리지
- ✅ `./gradlew jacocoTestCoverageVerification` 통과
- ✅ 프로덕션 레디

---

이 개선안을 따라 진행하면 **5-7일 내에 모든 목표를 달성**할 수 있습니다! 💪
