package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

import static com.brillianttiger.bio.parser.pmc.parser.BackParser.*;
import static com.brillianttiger.bio.parser.pmc.parser.BodyParser.*;
import static com.brillianttiger.bio.parser.pmc.parser.CommonPmcElementParser.*;
import static com.brillianttiger.bio.parser.pmc.parser.FrontParser.*;

/**
 * PmcXmlParser / PMC XML 파서
 *
 * KR: PMC (PubMed Central) XML 파일을 파싱하는 메인 클래스
 * EN: Main class for parsing PMC (PubMed Central) XML files
 *
 * Features:
 * - StAX-based streaming parser for memory efficiency
 * - XXE attack prevention
 * - Automatic GZip file handling
 * - Full JATS DTD compliance
 */
public class PmcXmlParser {

    private final XMLInputFactory xmlInputFactory;

    /**
     * 생성자 / Constructor
     * XXE 공격 방지 설정 포함 / Includes XXE attack prevention settings
     */
    public PmcXmlParser() {
        this.xmlInputFactory = createSecureXMLInputFactory();
    }

    /**
     * XXE 공격 방지가 적용된 XMLInputFactory 생성 / Create secure XMLInputFactory with XXE prevention
     *
     * KR: XXE (XML External Entity) 공격을 방지하기 위한 보안 설정
     * EN: Security settings to prevent XXE (XML External Entity) attacks
     */
    private XMLInputFactory createSecureXMLInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();

        // XXE 공격 방지 / Prevent XXE attacks
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);

        // 성능 최적화 / Performance optimization
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);

        return factory;
    }

    /**
     * PMC XML 파일 전체 파싱 / Parse entire PMC XML file
     *
     * KR: XML 파일을 파싱하여 PmcArticleSet 반환
     * EN: Parse XML file and return PmcArticleSet
     *
     * @param xmlPath XML 파일 경로 (gzip 자동 처리) / XML file path (auto-handles gzip)
     * @return PmcArticleSet 객체 / PmcArticleSet object
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public PmcArticleSet parse(Path xmlPath) throws XMLStreamException, IOException {
        try (InputStream inputStream = openInputStream(xmlPath)) {
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

            try {
                return parsePmcArticleSet(reader);
            } finally {
                reader.close();
            }
        }
    }

    /**
     * PMC XML 파일 스트리밍 파싱 / Stream parse PMC XML file
     *
     * KR: 대용량 XML 파일을 메모리 효율적으로 파싱 (Consumer 콜백 패턴)
     * EN: Memory-efficient parsing of large XML files (Consumer callback pattern)
     *
     * @param xmlPath XML 파일 경로 / XML file path
     * @param articleConsumer PmcArticle 처리 콜백 / PmcArticle processing callback
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public void parseStream(Path xmlPath, Consumer<PmcArticle> articleConsumer)
            throws XMLStreamException, IOException {
        try (InputStream inputStream = openInputStream(xmlPath)) {
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

            try {
                parseStreamInternal(reader, articleConsumer);
            } finally {
                reader.close();
            }
        }
    }

    /**
     * InputStream 열기 (GZip 자동 처리) / Open InputStream (auto-handle GZip)
     *
     * @param filePath 파일 경로 / File path
     * @return InputStream
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    private InputStream openInputStream(Path filePath) throws IOException {
        InputStream fileInputStream = Files.newInputStream(filePath);
        InputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        // GZip 파일 자동 감지 및 처리 / Auto-detect and handle GZip files
        if (filePath.toString().endsWith(".gz")) {
            return new GZIPInputStream(bufferedInputStream);
        }

        return bufferedInputStream;
    }

    /**
     * PmcArticleSet 파싱 / Parse PmcArticleSet
     * DTD: <!ELEMENT pmc-articleset (article+)>
     */
    private PmcArticleSet parsePmcArticleSet(XMLStreamReader reader) throws XMLStreamException {
        List<PmcArticle> articles = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("article".equals(localName)) {
                    articles.add(parsePmcArticle(reader));
                } else if (!"pmc-articleset".equals(localName)) {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("pmc-articleset")) {
                    break;
                }
            }
        }

        return PmcArticleSet.builder()
                .articles(articles)
                .build();
    }

    /**
     * 스트리밍 파싱 내부 구현 / Internal streaming parse
     */
    private void parseStreamInternal(XMLStreamReader reader, Consumer<PmcArticle> articleConsumer)
            throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("article".equals(localName)) {
                    PmcArticle article = parsePmcArticle(reader);
                    articleConsumer.accept(article);
                } else if (!"pmc-articleset".equals(localName)) {
                    skipElement(reader);
                }
            }
        }
    }

    /**
     * PmcArticle 파싱 / Parse PmcArticle
     * DTD: <!ELEMENT article (front, body?, back?, floats-group?, sub-article*, response*)>
     * DTD: <!ATTLIST article article-type CDATA #IMPLIED dtd-version CDATA #IMPLIED xml:lang CDATA "en">
     */
    private PmcArticle parsePmcArticle(XMLStreamReader reader) throws XMLStreamException {
        String articleType = reader.getAttributeValue(null, "article-type");
        String dtdVersion = reader.getAttributeValue(null, "dtd-version");
        String xmlLang = reader.getAttributeValue(null, "xml:lang");
        if (xmlLang == null) {
            xmlLang = "en";
        }

        PmcArticle.PmcArticleBuilder builder = PmcArticle.builder()
                .articleType(articleType)
                .dtdVersion(dtdVersion)
                .xmlLang(xmlLang);

        List<SubArticle> subArticles = new ArrayList<>();
        List<Response> responses = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "front":
                        builder.front(parseFront(reader));
                        break;
                    case "body":
                        builder.body(parseBody(reader));
                        break;
                    case "back":
                        builder.back(parseBack(reader));
                        break;
                    case "floats-group":
                        builder.floatsGroup(parseFloatsGroup(reader));
                        break;
                    case "sub-article":
                        subArticles.add(parseSubArticle(reader));
                        break;
                    case "response":
                        responses.add(parseResponse(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("article")) {
                    break;
                }
            }
        }

        builder.subArticles(subArticles.isEmpty() ? null : subArticles);
        builder.responses(responses.isEmpty() ? null : responses);

        return builder.build();
    }

    /**
     * FloatsGroup 파싱 / Parse FloatsGroup
     */
    private FloatsGroup parseFloatsGroup(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "floats-group");
        return FloatsGroup.builder().value(value).build();
    }

    /**
     * SubArticle 파싱 / Parse SubArticle
     */
    private SubArticle parseSubArticle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "sub-article");
        return SubArticle.builder().value(value).build();
    }

    /**
     * Response 파싱 / Parse Response
     */
    private Response parseResponse(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "response");
        return Response.builder().value(value).build();
    }
}
