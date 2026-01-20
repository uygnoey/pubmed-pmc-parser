package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.SubArticle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;

/**
 * PmcXmlParser의 xml:lang 속성 파싱 커버리지 테스트
 *
 * Lines 1454, 1462의 브랜치를 커버하기 위한 테스트
 */
@DisplayName("PmcXmlParser xml:lang Attribute Parsing Tests")
class PmcXmlParserXmlLangTest {

    @Test
    @DisplayName("parseSubArticle() - xml:lang이 없는 경우 (null 브랜치)")
    void testParseSubArticle_NoXmlLang() throws Exception {
        // Given: sub-article without xml:lang attribute
        String xml = """
            <sub-article article-type="reply" id="s1" xmlns:xlink="http://www.w3.org/1999/xlink">
                <front-stub>
                    <title-group>
                        <article-title>Reply Title</article-title>
                    </title-group>
                </front-stub>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        // Then: xmlLang should be null, covering the null branches
        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getId()).isEqualTo("s1");
        assertThat(subArticle.getXmlLang()).isNull();
    }

    @Test
    @DisplayName("parseSubArticle() - xml:lang이 첫 번째 시도로 발견되는 경우")
    void testParseSubArticle_XmlLangFoundFirstAttempt() throws Exception {
        // Given: sub-article with xml:lang attribute (standard format)
        String xml = """
            <sub-article article-type="reply" id="s1" xml:lang="en" xmlns:xlink="http://www.w3.org/1999/xlink">
                <front-stub>
                    <title-group>
                        <article-title>Reply Title</article-title>
                    </title-group>
                </front-stub>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        // Then: xmlLang found, Line 1454 false branch covered
        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getXmlLang()).isEqualTo("en");
    }

    @Test
    @DisplayName("parseSubArticle() - xml namespace를 명시적으로 선언한 경우")
    void testParseSubArticle_XmlNamespaceExplicit() throws Exception {
        // Given: sub-article with explicit xml namespace declaration
        String xml = """
            <sub-article article-type="reply" id="s1" xmlns:xml="http://www.w3.org/XML/1998/namespace" xml:lang="fr" xmlns:xlink="http://www.w3.org/1999/xlink">
                <front-stub>
                    <title-group>
                        <article-title>Reply Title</article-title>
                    </title-group>
                </front-stub>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        // Then: xmlLang should be found via namespace URI
        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getXmlLang()).isEqualTo("fr");
    }

    @Test
    @DisplayName("parseSubArticle() - xml:lang이 for loop에서만 발견되는 경우 시도")
    void testParseSubArticle_XmlLangFoundInForLoop() throws Exception {
        // Given: Try to create scenario where xml:lang is found only in for loop
        // This is theoretically difficult because StAX should find it in earlier attempts
        String xml = """
            <sub-article article-type="reply" id="s1" xml:lang="de" xmlns:xlink="http://www.w3.org/1999/xlink">
                <front-stub>
                    <title-group>
                        <article-title>Reply Title</article-title>
                    </title-group>
                </front-stub>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        // Then: Attempt to cover Line 1462 true branch
        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getXmlLang()).isEqualTo("de");
    }

    @Test
    @DisplayName("parseSubArticle() - 다른 lang 속성이 있지만 xml prefix가 아닌 경우")
    void testParseSubArticle_LangWithoutXmlPrefix() throws Exception {
        // Given: sub-article with lang attribute but wrong prefix
        String xml = """
            <sub-article article-type="reply" id="s1" custom:lang="fr" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:custom="http://example.com">
                <front-stub>
                    <title-group>
                        <article-title>Reply Title</article-title>
                    </title-group>
                </front-stub>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        // Then: should not find xml:lang, covering Line 1462 false branches
        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getXmlLang()).isNull();
    }

    @Test
    @DisplayName("parseSubArticle() - xml prefix를 가진 다른 속성이 있는 경우")
    void testParseSubArticle_XmlPrefixWithoutLang() throws Exception {
        // Given: sub-article with xml prefix but not lang attribute
        String xml = """
            <sub-article article-type="reply" id="s1" xml:space="preserve" xmlns:xlink="http://www.w3.org/1999/xlink">
                <front-stub>
                    <title-group>
                        <article-title>Reply Title</article-title>
                    </title-group>
                </front-stub>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        // Then: should not find xml:lang, covering Line 1462 false branches
        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getXmlLang()).isNull();
    }

    @Test
    @DisplayName("parseSubArticle() - 여러 속성이 있지만 xml:lang이 없는 경우")
    void testParseSubArticle_MultipleAttributesNoXmlLang() throws Exception {
        // Given: sub-article with multiple attributes but no xml:lang
        String xml = """
            <sub-article article-type="reply" id="s1" custom:attr1="value1" other:attr2="value2" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:custom="http://example.com" xmlns:other="http://other.com">
                <front-stub>
                    <title-group>
                        <article-title>Reply Title</article-title>
                    </title-group>
                </front-stub>
            </sub-article>
            """;

        XMLStreamReader reader = createReader(xml);
        reader.nextTag();

        // When
        Method method = PmcXmlParser.class.getDeclaredMethod("parseSubArticle", XMLStreamReader.class);
        method.setAccessible(true);
        PmcXmlParser parser = new PmcXmlParser();
        SubArticle subArticle = (SubArticle) method.invoke(parser, reader);

        // Then: loops through all attributes but doesn't find xml:lang
        assertThat(subArticle).isNotNull();
        assertThat(subArticle.getXmlLang()).isNull();
    }

    // ==================== Helper Methods ====================

    private XMLStreamReader createReader(String xml) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        return factory.createXMLStreamReader(new StringReader(xml));
    }
}
