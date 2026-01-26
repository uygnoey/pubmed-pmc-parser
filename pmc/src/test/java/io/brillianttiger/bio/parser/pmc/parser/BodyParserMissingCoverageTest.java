package io.brillianttiger.bio.parser.pmc.parser;

import io.brillianttiger.bio.parser.pmc.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BodyParser 100% 커버리지 달성을 위한 테스트
 *
 * KR: BodyParser의 커버되지 않은 메서드들을 테스트
 * EN: Tests for uncovered methods in BodyParser to achieve 100% coverage
 */
class BodyParserMissingCoverageTest {

    /**
     * 테스트 0: BodyParser 생성자 / Test 0: BodyParser constructor
     *
     * KR: BodyParser 인스턴스 생성을 통해 생성자 커버리지 확보
     * EN: Cover constructor through BodyParser instance creation
     */
    @Test
    void testBodyParserConstructor() {
        // Given & When
        BodyParser parser = new BodyParser();

        // Then
        assertThat(parser).isNotNull();
    }

    /**
     * 테스트 1: BoxedText 파싱 / Test 1: Parse BoxedText
     */
    @Test
    void testParseBoxedText(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <boxed-text id="box1" position="float" content-type="supplementary">
                  <label>Box 1</label>
                  <caption>
                    <title>Important Information</title>
                    <p>This is a caption paragraph.</p>
                  </caption>
                  <sec id="box-sec1">
                    <title>Section in Box</title>
                    <p>Content inside the boxed text.</p>
                  </sec>
                  <p>Additional paragraph.</p>
                  <list list-type="bullet">
                    <list-item><p>Item 1</p></list-item>
                    <list-item><p>Item 2</p></list-item>
                  </list>
                  <def-list>
                    <def-item>
                      <term>Term A</term>
                      <def><p>Definition A</p></def>
                    </def-item>
                  </def-list>
                  <disp-quote>
                    <p>A quote inside boxed text.</p>
                  </disp-quote>
                  <code>Sample code block</code>
                </boxed-text>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("boxed_text.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody()).isNotNull();
        assertThat(article.getBody().getBoxedTexts()).hasSize(1);

        BoxedText box = article.getBody().getBoxedTexts().get(0);
        assertThat(box.getId()).isEqualTo("box1");
        assertThat(box.getPosition()).isEqualTo("float");
        assertThat(box.getLabel()).isNotNull();
        assertThat(box.getLabel().getValue()).contains("Box 1");
        assertThat(box.getCaption()).isNotNull();
        assertThat(box.getSections()).isNotEmpty();
        assertThat(box.getParagraphs()).isNotEmpty();
        assertThat(box.getLists()).isNotEmpty();
        assertThat(box.getDefLists()).isNotEmpty();
        assertThat(box.getDispQuotes()).isNotEmpty();
        assertThat(box.getCodeBlocks()).isNotEmpty();
    }

    /**
     * 테스트 2: DispQuote 파싱 / Test 2: Parse DispQuote
     */
    @Test
    void testParseDispQuote(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <disp-quote id="q1" content-type="epigraph">
                  <label>Quote 1</label>
                  <p>This is the first paragraph of the quote.</p>
                  <p>This is the second paragraph.</p>
                  <list list-type="simple">
                    <list-item><p>Point 1</p></list-item>
                    <list-item><p>Point 2</p></list-item>
                  </list>
                  <def-list>
                    <def-item>
                      <term>Key Term</term>
                      <def><p>Definition text</p></def>
                    </def-item>
                  </def-list>
                  <attrib>Source: Author Name, 2024</attrib>
                </disp-quote>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("disp_quote.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDispQuotes()).hasSize(1);

        DispQuote quote = article.getBody().getDispQuotes().get(0);
        assertThat(quote.getId()).isEqualTo("q1");
        assertThat(quote.getContentType()).isEqualTo("epigraph");
        assertThat(quote.getLabel()).isNotNull();
        assertThat(quote.getLabel().getValue()).contains("Quote 1");
        assertThat(quote.getParagraphs()).hasSize(2);
        assertThat(quote.getLists()).isNotEmpty();
        assertThat(quote.getDefLists()).isNotEmpty();
        assertThat(quote.getAttrib()).isNotNull();
        assertThat(quote.getAttrib()).contains("Author Name");
    }

    /**
     * 테스트 3: DefList 및 관련 요소 파싱 / Test 3: Parse DefList and related elements
     */
    @Test
    void testParseDefList(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <def-list id="dl1" list-type="gloss" continued-from="dl0">
                  <label>Glossary</label>
                  <title>Key Terms</title>
                  <term-head>Term</term-head>
                  <def-head>Definition</def-head>
                  <def-item id="di1">
                    <label>1.</label>
                    <term id="t1" vocab="MeSH">DNA</term>
                    <term>Deoxyribonucleic Acid</term>
                    <def id="d1">
                      <label>Def:</label>
                      <title>Definition of DNA</title>
                      <p>A molecule that carries genetic instructions.</p>
                      <p>Found in all living organisms.</p>
                      <list list-type="bullet">
                        <list-item><p>Double helix structure</p></list-item>
                        <list-item><p>Contains nucleotides</p></list-item>
                      </list>
                      <def-list>
                        <def-item>
                          <term>Nucleotide</term>
                          <def><p>Basic unit of DNA</p></def>
                        </def-item>
                      </def-list>
                    </def>
                    <def>
                      <p>Alternative definition</p>
                    </def>
                  </def-item>
                  <def-item>
                    <term>RNA</term>
                    <def><p>Ribonucleic acid definition</p></def>
                  </def-item>
                  <def-item>
                    <term>PCR</term>
                  </def-item>
                </def-list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("def_list.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDefLists()).hasSize(1);

        DefList defList = article.getBody().getDefLists().get(0);
        assertThat(defList.getId()).isEqualTo("dl1");
        assertThat(defList.getListType()).isEqualTo("gloss");
        assertThat(defList.getContinuedFrom()).isEqualTo("dl0");
        assertThat(defList.getLabel()).isNotNull();
        assertThat(defList.getTitle()).isNotNull();

        assertThat(defList.getItems()).hasSize(3);

        // First def-item: multiple terms and defs
        DefItem item1 = defList.getItems().get(0);
        assertThat(item1.getId()).isEqualTo("di1");
        assertThat(item1.getLabel()).isNotNull();
        assertThat(item1.getTerms()).hasSize(2);

        Term term1 = item1.getTerms().get(0);
        assertThat(term1.getId()).isEqualTo("t1");
        assertThat(term1.getVocab()).isEqualTo("MeSH");
        assertThat(term1.getValue()).contains("DNA");

        assertThat(item1.getDefinitions()).hasSize(2);
        Def def1 = item1.getDefinitions().get(0);
        assertThat(def1.getId()).isEqualTo("d1");
        assertThat(def1.getLabel()).isNotNull();
        assertThat(def1.getTitle()).isNotNull();
        assertThat(def1.getParagraphs()).hasSize(2);
        assertThat(def1.getLists()).isNotEmpty();
        assertThat(def1.getDefLists()).isNotEmpty();

        // Second def-item
        DefItem item2 = defList.getItems().get(1);
        assertThat(item2.getTerms()).hasSize(1);
        assertThat(item2.getTerms().get(0).getValue()).contains("RNA");
        assertThat(item2.getDefinitions()).hasSize(1);

        // Third def-item: no definitions
        DefItem item3 = defList.getItems().get(2);
        assertThat(item3.getTerms()).hasSize(1);
        assertThat(item3.getDefinitions()).isNullOrEmpty();
    }

    /**
     * 테스트 4: Code 블록 파싱 / Test 4: Parse Code blocks
     */
    @Test
    void testParseCode(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <code id="code1" code-type="java" language="java"
                      executable="yes" position="float">
                  public class HelloWorld {
                      public static void main(String[] args) {
                          System.out.println("Hello World");
                      }
                  }
                </code>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("code.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getCodeBlocks()).hasSize(1);

        Code code = article.getBody().getCodeBlocks().get(0);
        assertThat(code.getId()).isEqualTo("code1");
        assertThat(code.getCodeType()).isEqualTo("java");
        assertThat(code.getLanguage()).isEqualTo("java");
        assertThat(code.getExecutable()).isEqualTo("yes");
        assertThat(code.getPosition()).isEqualTo("float");
        assertThat(code.getValue()).contains("HelloWorld");
        assertThat(code.getValue()).contains("main");
    }

    /**
     * 테스트 5: Caption 파싱 / Test 5: Parse Caption
     */
    @Test
    void testParseCaption(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <boxed-text>
                  <caption id="cap1" content-type="full">
                    <title>Box Caption Title</title>
                    <p>First paragraph of caption.</p>
                    <p>Second paragraph of caption.</p>
                    <fn-group>
                      <fn id="fn1"><p>Footnote in caption</p></fn>
                    </fn-group>
                  </caption>
                  <p>Box content</p>
                </boxed-text>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("caption.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getBoxedTexts()).hasSize(1);
        BoxedText box = article.getBody().getBoxedTexts().get(0);

        assertThat(box.getCaption()).isNotNull();
        Caption caption = box.getCaption();
        assertThat(caption.getId()).isEqualTo("cap1");
        assertThat(caption.getContentType()).isEqualTo("full");
        assertThat(caption.getTitle()).isNotNull();
        assertThat(caption.getTitle().getValue()).contains("Box Caption Title");
        assertThat(caption.getParagraphs()).hasSize(2);
        // Note: fn-group is currently not parsed by parseCaption()
    }

    /**
     * 테스트 6: SecMeta 파싱 / Test 6: Parse SecMeta
     */
    @Test
    void testParseSecMeta(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <sec id="sec1">
                  <sec-meta>
                    <kwd-group>
                      <kwd>keyword1</kwd>
                      <kwd>keyword2</kwd>
                    </kwd-group>
                  </sec-meta>
                  <title>Section Title</title>
                  <p>Section content.</p>
                </sec>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("sec_meta.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getSections()).hasSize(1);
        Sec sec = article.getBody().getSections().get(0);
        assertThat(sec.getSecMeta()).isNotNull();
        // Note: keywords are currently not parsed by parseSecMeta()
        assertThat(sec.getSecMeta().getValue()).isNotNull();
    }

    /**
     * 테스트 7: List와 ListItem의 모든 브랜치 / Test 7: All branches of List and ListItem
     */
    @Test
    void testParseListAllBranches(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <list id="list1" list-type="order" prefix-word="Step" continued-from="list0">
                  <label>Steps</label>
                  <title>Procedure</title>
                  <list-item id="li1">
                    <label>1.</label>
                    <p>First step with paragraph.</p>
                    <list list-type="bullet">
                      <list-item><p>Sub-item A</p></list-item>
                      <list-item><p>Sub-item B</p></list-item>
                    </list>
                    <def-list>
                      <def-item>
                        <term>Sub-term</term>
                        <def><p>Sub-definition</p></def>
                      </def-item>
                    </def-list>
                    <boxed-text>
                      <p>Boxed content in list item</p>
                    </boxed-text>
                    <disp-quote>
                      <p>Quote in list item</p>
                    </disp-quote>
                    <code>code in list</code>
                  </list-item>
                  <list-item>
                    <p>Second step</p>
                  </list-item>
                </list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("list_all_branches.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getLists()).hasSize(1);
        PmcList list = article.getBody().getLists().get(0);
        assertThat(list.getId()).isEqualTo("list1");
        assertThat(list.getListType()).isEqualTo("order");
        assertThat(list.getPrefixWord()).isEqualTo("Step");
        assertThat(list.getContinuedFrom()).isEqualTo("list0");
        assertThat(list.getLabel()).isNotNull();
        assertThat(list.getTitle()).isNotNull();

        assertThat(list.getItems()).hasSize(2);
        ListItem item1 = list.getItems().get(0);
        assertThat(item1.getId()).isEqualTo("li1");
        assertThat(item1.getLabel()).isNotNull();
        assertThat(item1.getParagraphs()).isNotEmpty();
        assertThat(item1.getNestedLists()).isNotEmpty();
        assertThat(item1.getDefLists()).isNotEmpty();
        assertThat(item1.getBoxedTexts()).isNotEmpty();
        assertThat(item1.getDispQuotes()).isNotEmpty();
        assertThat(item1.getCodeBlocks()).isNotEmpty();
    }

    /**
     * 테스트 8: Body의 모든 브랜치 / Test 8: All branches of Body
     */
    @Test
    void testParseBodyAllBranches(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body id="body1" specific-use="web-only">
                <p>Direct paragraph in body.</p>
                <list list-type="bullet">
                  <list-item><p>Body list item</p></list-item>
                </list>
                <def-list>
                  <def-item>
                    <term>Body term</term>
                    <def><p>Body definition</p></def>
                  </def-item>
                </def-list>
                <boxed-text>
                  <p>Boxed text in body</p>
                </boxed-text>
                <disp-quote>
                  <p>Quote in body</p>
                </disp-quote>
                <code>Code in body</code>
                <disp-formula id="eq1">
                  <tex-math>E = mc^2</tex-math>
                </disp-formula>
                <sec id="sec1">
                  <title>First Section</title>
                  <p>Section content</p>
                </sec>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("body_all_branches.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        Body body = article.getBody();
        assertThat(body.getId()).isEqualTo("body1");
        assertThat(body.getSpecificUse()).isEqualTo("web-only");
        assertThat(body.getParagraphs()).isNotEmpty();
        assertThat(body.getLists()).isNotEmpty();
        assertThat(body.getDefLists()).isNotEmpty();
        assertThat(body.getBoxedTexts()).isNotEmpty();
        assertThat(body.getDispQuotes()).isNotEmpty();
        assertThat(body.getCodeBlocks()).isNotEmpty();
        // Note: disp-formula is currently not parsed by parseBody()
        assertThat(body.getSections()).isNotEmpty();
    }

    /**
     * 테스트 9: Sec의 모든 브랜치 / Test 9: All branches of Sec
     */
    @Test
    void testParseSecAllBranches(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <sec id="sec1" sec-type="methods" specific-use="print-only">
                  <label>Section 1</label>
                  <title>Methods</title>
                  <sec-meta>
                    <kwd-group><kwd>method</kwd></kwd-group>
                  </sec-meta>
                  <p>Section paragraph</p>
                  <list list-type="order">
                    <list-item><p>Step 1</p></list-item>
                  </list>
                  <def-list>
                    <def-item>
                      <term>Section term</term>
                      <def><p>Section def</p></def>
                    </def-item>
                  </def-list>
                  <boxed-text>
                    <p>Box in section</p>
                  </boxed-text>
                  <disp-quote>
                    <p>Quote in section</p>
                  </disp-quote>
                  <code>Section code</code>
                  <disp-formula id="sec-eq1">
                    <tex-math>x = y + z</tex-math>
                  </disp-formula>
                  <sec id="subsec1">
                    <title>Subsection</title>
                    <p>Nested content</p>
                  </sec>
                </sec>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("sec_all_branches.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getSections()).hasSize(1);
        Sec sec = article.getBody().getSections().get(0);
        assertThat(sec.getId()).isEqualTo("sec1");
        assertThat(sec.getSecType()).isEqualTo("methods");
        assertThat(sec.getSpecificUse()).isEqualTo("print-only");
        assertThat(sec.getLabel()).isNotNull();
        assertThat(sec.getTitle()).isNotNull();
        assertThat(sec.getSecMeta()).isNotNull();
        assertThat(sec.getParagraphs()).isNotEmpty();
        assertThat(sec.getLists()).isNotEmpty();
        assertThat(sec.getDefLists()).isNotEmpty();
        assertThat(sec.getBoxedTexts()).isNotEmpty();
        assertThat(sec.getDispQuotes()).isNotEmpty();
        assertThat(sec.getCodeBlocks()).isNotEmpty();
        // Note: disp-formula is currently not parsed by parseSec()
        assertThat(sec.getSections()).isNotEmpty();
    }

    /**
     * 테스트 10: List with unknown elements (default case) / Test 10: List with unknown elements
     * Line 226, 237 커버 (parseList default case)
     */
    @Test
    void testParseList_WithUnknownElement(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <list list-type="bullet">
                  <label>List Label</label>
                  <unknown-element>This should be skipped</unknown-element>
                  <list-item><p>Item 1</p></list-item>
                </list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("list_unknown.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getLists()).hasSize(1);
        PmcList list = article.getBody().getLists().get(0);
        assertThat(list.getItems()).hasSize(1);
    }

    /**
     * 테스트 11: List with empty items (Line 246 true branch)
     */
    @Test
    void testParseList_EmptyItems(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <list list-type="simple">
                  <label>Empty List</label>
                  <title>No Items</title>
                </list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("list_empty.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getLists()).hasSize(1);
        PmcList list = article.getBody().getLists().get(0);
        assertThat(list.getItems()).isNull();  // isEmpty() == true
        assertThat(list.getLabel()).isNotNull();
        assertThat(list.getTitle()).isNotNull();
    }

    /**
     * 테스트 12: ListItem with title (Line 283 커버)
     */
    @Test
    void testParseListItem_WithTitle(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <list list-type="simple">
                  <list-item id="li1">
                    <label>Item 1</label>
                    <title>Item Title</title>
                    <p>Item paragraph</p>
                  </list-item>
                </list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("list_item_title.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getLists()).hasSize(1);
        ListItem item = article.getBody().getLists().get(0).getItems().get(0);
        assertThat(item.getTitle()).isNotNull();
        assertThat(item.getTitle().getValue()).contains("Item Title");
    }

    /**
     * 테스트 13: ListItem with unknown element (Line 306 default case)
     */
    @Test
    void testParseListItem_WithUnknownElement(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <list list-type="simple">
                  <list-item>
                    <unknown-element>Skip this</unknown-element>
                    <p>Valid paragraph</p>
                  </list-item>
                </list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("list_item_unknown.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getLists()).hasSize(1);
        ListItem item = article.getBody().getLists().get(0).getItems().get(0);
        assertThat(item.getParagraphs()).hasSize(1);
    }

    /**
     * 테스트 14: ListItem with empty paragraphs (Line 315 true branch)
     */
    @Test
    void testParseListItem_EmptyParagraphs(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <list list-type="simple">
                  <list-item id="li1">
                    <label>Item without paragraph</label>
                  </list-item>
                </list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("list_item_empty_para.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getLists()).hasSize(1);
        ListItem item = article.getBody().getLists().get(0).getItems().get(0);
        assertThat(item.getParagraphs()).isNull();  // isEmpty() == true
        assertThat(item.getLabel()).isNotNull();
    }

    /**
     * 테스트 15: DefList with nested def-list (Line 374 커버)
     */
    @Test
    void testParseDefList_WithNestedDefList(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <def-list id="dl1">
                  <label>Outer List</label>
                  <title>Definitions</title>
                  <def-item>
                    <term>Outer Term</term>
                    <def><p>Outer definition</p></def>
                  </def-item>
                  <def-list id="dl2">
                    <def-item>
                      <term>Inner Term</term>
                      <def><p>Inner definition</p></def>
                    </def-item>
                  </def-list>
                </def-list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("deflist_nested.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDefLists()).hasSize(1);
        DefList defList = article.getBody().getDefLists().get(0);
        assertThat(defList.getNestedDefLists()).isNotNull().hasSize(1);
        assertThat(defList.getNestedDefLists().get(0).getId()).isEqualTo("dl2");
    }

    /**
     * 테스트 16: DefList with empty items and nestedDefLists (Lines 386, 387 true branches)
     */
    @Test
    void testParseDefList_Empty(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <def-list id="dl-empty">
                  <label>Empty DefList</label>
                  <title>No Items</title>
                </def-list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("deflist_empty.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDefLists()).hasSize(1);
        DefList defList = article.getBody().getDefLists().get(0);
        assertThat(defList.getItems()).isNull();  // Line 386: isEmpty() == true
        assertThat(defList.getNestedDefLists()).isNull();  // Line 387: isEmpty() == true
    }

    /**
     * 테스트 17: DefItem with unknown element (Line 427 default case)
     */
    @Test
    void testParseDefItem_WithUnknownElement(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <def-list>
                  <def-item>
                    <unknown-element>Skip</unknown-element>
                    <term>Valid Term</term>
                    <def><p>Valid definition</p></def>
                  </def-item>
                </def-list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("defitem_unknown.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDefLists()).hasSize(1);
        DefItem item = article.getBody().getDefLists().get(0).getItems().get(0);
        assertThat(item.getTerms()).hasSize(1);
        assertThat(item.getDefinitions()).hasSize(1);
    }

    /**
     * 테스트 18: DefItem with empty terms (Line 436 true branch)
     */
    @Test
    void testParseDefItem_EmptyTerms(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <def-list>
                  <def-item id="di-no-terms">
                    <label>Item without terms</label>
                  </def-item>
                </def-list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("defitem_empty_terms.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDefLists()).hasSize(1);
        DefItem item = article.getBody().getDefLists().get(0).getItems().get(0);
        assertThat(item.getTerms()).isNull();  // Line 436: isEmpty() == true
    }

    /**
     * 테스트 19: Def with boxed-text, disp-quote, code (Lines 526, 529, 532 커버)
     */
    @Test
    void testParseDef_WithAllElements(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <def-list>
                  <def-item>
                    <term>Complex Term</term>
                    <def id="complex-def">
                      <label>Def Label</label>
                      <title>Definition Title</title>
                      <p>First paragraph</p>
                      <boxed-text><p>Boxed content in def</p></boxed-text>
                      <disp-quote><p>Quote in def</p></disp-quote>
                      <code>Code in def</code>
                    </def>
                  </def-item>
                </def-list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("def_all_elements.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDefLists()).hasSize(1);
        Def def = article.getBody().getDefLists().get(0).getItems().get(0).getDefinitions().get(0);
        assertThat(def.getId()).isEqualTo("complex-def");
        assertThat(def.getBoxedTexts()).hasSize(1);  // Line 526
        assertThat(def.getDispQuotes()).hasSize(1);  // Line 529
        assertThat(def.getCodeBlocks()).hasSize(1);  // Line 532
    }

    /**
     * 테스트 20: Def with unknown element (Line 536 default case)
     */
    @Test
    void testParseDef_WithUnknownElement(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <def-list>
                  <def-item>
                    <term>Term</term>
                    <def>
                      <unknown-element>Skip</unknown-element>
                      <p>Valid paragraph</p>
                    </def>
                  </def-item>
                </def-list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("def_unknown.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDefLists()).hasSize(1);
        Def def = article.getBody().getDefLists().get(0).getItems().get(0).getDefinitions().get(0);
        assertThat(def.getParagraphs()).hasSize(1);
    }

    /**
     * 테스트 21: Def with all empty collections (Lines 545, 548, 549, 550 true branches)
     */
    @Test
    void testParseDef_AllEmpty(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <def-list>
                  <def-item>
                    <term>Empty Def Term</term>
                    <def id="empty-def">
                      <label>Only label</label>
                      <title>Only title</title>
                    </def>
                  </def-item>
                </def-list>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("def_all_empty.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDefLists()).hasSize(1);
        Def def = article.getBody().getDefLists().get(0).getItems().get(0).getDefinitions().get(0);
        assertThat(def.getParagraphs()).isNull();  // Line 545: isEmpty() == true
        assertThat(def.getBoxedTexts()).isNull();  // Line 548: isEmpty() == true
        assertThat(def.getDispQuotes()).isNull();  // Line 549: isEmpty() == true
        assertThat(def.getCodeBlocks()).isNull();  // Line 550: isEmpty() == true
    }

    /**
     * 테스트 22: DispQuote with title (Line 594 커버)
     */
    @Test
    void testParseDispQuote_WithTitle(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <disp-quote id="q1">
                  <label>Quote 1</label>
                  <title>Quote Title</title>
                  <p>Quote content</p>
                </disp-quote>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("dispquote_title.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDispQuotes()).hasSize(1);
        DispQuote quote = article.getBody().getDispQuotes().get(0);
        assertThat(quote.getTitle()).isNotNull();
        assertThat(quote.getTitle().getValue()).contains("Quote Title");
    }

    /**
     * 테스트 23: DispQuote with nested disp-quote (Line 607 커버)
     */
    @Test
    void testParseDispQuote_Nested(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <disp-quote id="outer-quote">
                  <p>Outer quote</p>
                  <disp-quote id="inner-quote">
                    <p>Nested quote</p>
                  </disp-quote>
                </disp-quote>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("dispquote_nested.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDispQuotes()).hasSize(1);
        DispQuote outerQuote = article.getBody().getDispQuotes().get(0);
        assertThat(outerQuote.getNestedQuotes()).hasSize(1);  // Line 607
        assertThat(outerQuote.getNestedQuotes().get(0).getId()).isEqualTo("inner-quote");
    }

    /**
     * 테스트 24: DispQuote with code and permissions (Lines 610, 616 커버)
     */
    @Test
    void testParseDispQuote_WithCodeAndPermissions(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <disp-quote id="q-code">
                  <p>Quote with code</p>
                  <code>Sample code</code>
                  <permissions>
                    <copyright-statement>Copyright 2024</copyright-statement>
                    <license>
                      <license-p>CC BY 4.0</license-p>
                    </license>
                  </permissions>
                </disp-quote>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("dispquote_code_permissions.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDispQuotes()).hasSize(1);
        DispQuote quote = article.getBody().getDispQuotes().get(0);
        assertThat(quote.getCodeBlocks()).hasSize(1);  // Line 610
        assertThat(quote.getPermissions()).isNotNull();  // Line 616
    }

    /**
     * 테스트 25: DispQuote with unknown element (Line 619 default case)
     */
    @Test
    void testParseDispQuote_WithUnknownElement(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <disp-quote id="q-unknown">
                  <unknown-element>Skip this</unknown-element>
                  <p>Valid paragraph</p>
                </disp-quote>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("dispquote_unknown.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDispQuotes()).hasSize(1);
        DispQuote quote = article.getBody().getDispQuotes().get(0);
        assertThat(quote.getParagraphs()).hasSize(1);
    }

    /**
     * 테스트 26: DispQuote all empty (Lines 628, 631, 632 true branches)
     */
    @Test
    void testParseDispQuote_AllEmpty(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <disp-quote id="q-empty">
                  <label>Empty Quote</label>
                </disp-quote>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("dispquote_empty.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getDispQuotes()).hasSize(1);
        DispQuote quote = article.getBody().getDispQuotes().get(0);
        assertThat(quote.getParagraphs()).isNull();  // Line 628: isEmpty() == true
        assertThat(quote.getNestedQuotes()).isNull();  // Line 631: isEmpty() == true
        assertThat(quote.getCodeBlocks()).isNull();  // Line 632: isEmpty() == true
    }

    /**
     * 테스트 27: BoxedText with sec-meta (Line 677 커버)
     */
    @Test
    void testParseBoxedText_WithSecMeta(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <boxed-text id="box-secmeta">
                  <sec-meta>
                    <kwd-group><kwd>test</kwd></kwd-group>
                  </sec-meta>
                  <label>Box with sec-meta</label>
                  <p>Box content</p>
                </boxed-text>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("boxed_secmeta.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getBoxedTexts()).hasSize(1);
        BoxedText box = article.getBody().getBoxedTexts().get(0);
        assertThat(box.getSecMeta()).isNotNull();  // Line 677
    }

    /**
     * 테스트 28: BoxedText with attrib and permissions (Lines 704, 707 커버)
     */
    @Test
    void testParseBoxedText_WithAttribAndPermissions(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <boxed-text id="box-attrib">
                  <p>Boxed content</p>
                  <attrib>Source: Author, 2024</attrib>
                  <permissions>
                    <copyright-statement>All rights reserved</copyright-statement>
                    <license>
                      <license-p>License text</license-p>
                    </license>
                  </permissions>
                </boxed-text>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("boxed_attrib_permissions.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getBoxedTexts()).hasSize(1);
        BoxedText box = article.getBody().getBoxedTexts().get(0);
        assertThat(box.getAttrib()).isNotNull();  // Line 704
        assertThat(box.getAttrib()).contains("Author");
        assertThat(box.getPermissions()).isNotNull();  // Line 707
    }

    /**
     * 테스트 29: BoxedText with unknown element (Line 711 default case)
     */
    @Test
    void testParseBoxedText_WithUnknownElement(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <boxed-text id="box-unknown">
                  <unknown-element>Skip</unknown-element>
                  <p>Valid paragraph</p>
                </boxed-text>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("boxed_unknown.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getBoxedTexts()).hasSize(1);
        BoxedText box = article.getBody().getBoxedTexts().get(0);
        assertThat(box.getParagraphs()).hasSize(1);
    }

    /**
     * 테스트 30: BoxedText with empty paragraphs (Line 720 true branch)
     */
    @Test
    void testParseBoxedText_EmptyParagraphs(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <boxed-text id="box-no-para">
                  <label>Box without paragraphs</label>
                  <caption><title>Caption only</title></caption>
                </boxed-text>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("boxed_empty_para.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getBoxedTexts()).hasSize(1);
        BoxedText box = article.getBody().getBoxedTexts().get(0);
        assertThat(box.getParagraphs()).isNull();  // Line 720: isEmpty() == true
        assertThat(box.getLabel()).isNotNull();
        assertThat(box.getCaption()).isNotNull();
    }

    /**
     * 테스트 31: Caption with empty paragraphs (Line 824 확인 - 이미 커버되어 있을 수 있음)
     */
    @Test
    void testParseCaption_EmptyParagraphs(@TempDir Path tempDir) throws Exception {
        String xml = """
            <article dtd-version="1.4">
              <body>
                <boxed-text>
                  <caption id="cap-no-para">
                    <title>Caption Title Only</title>
                  </caption>
                  <p>Box content</p>
                </boxed-text>
              </body>
            </article>
            """;

        Path xmlFile = tempDir.resolve("caption_empty_para.xml");
        Files.writeString(xmlFile, xml);

        PmcXmlParser parser = new PmcXmlParser();
        JatsArticle article = parser.parseFile(xmlFile);

        assertThat(article.getBody().getBoxedTexts()).hasSize(1);
        Caption caption = article.getBody().getBoxedTexts().get(0).getCaption();
        assertThat(caption).isNotNull();
        assertThat(caption.getTitle()).isNotNull();
        assertThat(caption.getParagraphs()).isNullOrEmpty();  // Line 824: could be null or empty
    }
}
