package com.bioxml.parser.pubmed.parser;

import com.bioxml.parser.pubmed.model.*;

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

import static com.bioxml.parser.pubmed.parser.BookArticleParser.parsePubmedBookArticle;
import static com.bioxml.parser.pubmed.parser.CommonElementParser.*;
import static com.bioxml.parser.pubmed.parser.MedlineCitationParser.parseMedlineCitation;
import static com.bioxml.parser.pubmed.parser.PubmedDataParser.parsePubmedData;

/**
 * PubmedXmlParser / PubMed XML 파서
 *
 * KR: PubMed XML 파일을 파싱하는 메인 클래스
 * EN: Main class for parsing PubMed XML files
 *
 * Features:
 * - StAX-based streaming parser for memory efficiency
 * - XXE attack prevention
 * - Automatic GZip file handling
 * - Full DTD compliance
 */
public class PubmedXmlParser {

    private final XMLInputFactory xmlInputFactory;

    /**
     * 생성자 / Constructor
     * XXE 공격 방지 설정 포함 / Includes XXE attack prevention settings
     */
    public PubmedXmlParser() {
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
     * PubMed XML 파일 전체 파싱 / Parse entire PubMed XML file
     *
     * KR: XML 파일을 파싱하여 PubmedArticleSet 반환
     * EN: Parse XML file and return PubmedArticleSet
     *
     * @param xmlPath XML 파일 경로 (gzip 자동 처리) / XML file path (auto-handles gzip)
     * @return PubmedArticleSet 객체 / PubmedArticleSet object
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public PubmedArticleSet parse(Path xmlPath) throws XMLStreamException, IOException {
        try (InputStream inputStream = openInputStream(xmlPath)) {
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

            try {
                return parsePubmedArticleSet(reader);
            } finally {
                reader.close();
            }
        }
    }

    /**
     * PubMed XML 파일 스트리밍 파싱 / Stream parse PubMed XML file
     *
     * KR: 대용량 XML 파일을 메모리 효율적으로 파싱 (Consumer 콜백 패턴)
     * EN: Memory-efficient parsing of large XML files (Consumer callback pattern)
     *
     * @param xmlPath XML 파일 경로 / XML file path
     * @param articleConsumer PubmedArticle 처리 콜백 / PubmedArticle processing callback
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public void parseStream(Path xmlPath, Consumer<PubmedArticle> articleConsumer)
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
     * PubMed XML 파일 스트리밍 파싱 (BookArticle 포함) / Stream parse with BookArticle support
     *
     * @param xmlPath XML 파일 경로 / XML file path
     * @param articleConsumer PubmedArticle 처리 콜백 / PubmedArticle callback
     * @param bookArticleConsumer PubmedBookArticle 처리 콜백 / PubmedBookArticle callback
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     * @throws IOException 파일 읽기 오류 / File reading error
     */
    public void parseStream(Path xmlPath,
                            Consumer<PubmedArticle> articleConsumer,
                            Consumer<PubmedBookArticle> bookArticleConsumer)
            throws XMLStreamException, IOException {
        try (InputStream inputStream = openInputStream(xmlPath)) {
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

            try {
                parseStreamInternal(reader, articleConsumer, bookArticleConsumer);
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
     * PubmedArticleSet 파싱 / Parse PubmedArticleSet
     * DTD: <!ELEMENT PubmedArticleSet ((PubmedArticle | PubmedBookArticle)+, DeleteCitation?)>
     */
    private PubmedArticleSet parsePubmedArticleSet(XMLStreamReader reader) throws XMLStreamException {
        List<PubmedArticle> pubmedArticles = new ArrayList<>();
        List<PubmedBookArticle> pubmedBookArticles = new ArrayList<>();
        DeleteCitation deleteCitation = null;

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "PubmedArticle":
                        pubmedArticles.add(parsePubmedArticle(reader));
                        break;
                    case "PubmedBookArticle":
                        pubmedBookArticles.add(parsePubmedBookArticle(reader));
                        break;
                    case "DeleteCitation":
                        deleteCitation = parseDeleteCitation(reader);
                        break;
                    default:
                        // Skip unknown elements
                        if (!"PubmedArticleSet".equals(localName)) {
                            skipElement(reader);
                        }
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PubmedArticleSet")) {
                    break;
                }
            }
        }

        return PubmedArticleSet.builder()
                .pubmedArticles(pubmedArticles.isEmpty() ? null : pubmedArticles)
                .pubmedBookArticles(pubmedBookArticles.isEmpty() ? null : pubmedBookArticles)
                .deleteCitation(deleteCitation)
                .build();
    }

    /**
     * 스트리밍 파싱 내부 구현 (PubmedArticle만) / Internal streaming parse (PubmedArticle only)
     */
    private void parseStreamInternal(XMLStreamReader reader, Consumer<PubmedArticle> articleConsumer)
            throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("PubmedArticle".equals(localName)) {
                    PubmedArticle article = parsePubmedArticle(reader);
                    articleConsumer.accept(article);
                } else if (!"PubmedArticleSet".equals(localName)) {
                    skipElement(reader);
                }
            }
        }
    }

    /**
     * 스트리밍 파싱 내부 구현 (PubmedArticle + PubmedBookArticle) / Internal streaming parse (both types)
     */
    private void parseStreamInternal(XMLStreamReader reader,
                                      Consumer<PubmedArticle> articleConsumer,
                                      Consumer<PubmedBookArticle> bookArticleConsumer)
            throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "PubmedArticle":
                        if (articleConsumer != null) {
                            PubmedArticle article = parsePubmedArticle(reader);
                            articleConsumer.accept(article);
                        } else {
                            skipElement(reader);
                        }
                        break;
                    case "PubmedBookArticle":
                        if (bookArticleConsumer != null) {
                            PubmedBookArticle bookArticle = parsePubmedBookArticle(reader);
                            bookArticleConsumer.accept(bookArticle);
                        } else {
                            skipElement(reader);
                        }
                        break;
                    default:
                        if (!"PubmedArticleSet".equals(localName)) {
                            skipElement(reader);
                        }
                        break;
                }
            }
        }
    }

    /**
     * PubmedArticle 파싱 / Parse PubmedArticle
     * DTD: <!ELEMENT PubmedArticle (MedlineCitation, PubmedData?)>
     */
    private PubmedArticle parsePubmedArticle(XMLStreamReader reader) throws XMLStreamException {
        PubmedArticle.PubmedArticleBuilder builder = PubmedArticle.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "MedlineCitation":
                        builder.medlineCitation(parseMedlineCitation(reader));
                        break;
                    case "PubmedData":
                        builder.pubmedData(parsePubmedData(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PubmedArticle")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * DeleteCitation 파싱 / Parse DeleteCitation
     * DTD: <!ELEMENT DeleteCitation (PMID+)>
     */
    private DeleteCitation parseDeleteCitation(XMLStreamReader reader) throws XMLStreamException {
        List<PMID> pmids = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("PMID".equals(localName)) {
                    pmids.add(parsePMID(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("DeleteCitation")) {
                    break;
                }
            }
        }

        return DeleteCitation.builder()
                .pmids(pmids)
                .build();
    }
}
