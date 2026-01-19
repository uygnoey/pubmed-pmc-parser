package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;
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
}
