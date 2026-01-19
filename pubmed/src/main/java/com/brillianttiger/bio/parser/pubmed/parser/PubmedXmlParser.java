package com.brillianttiger.bio.parser.pubmed.parser;

import com.brillianttiger.bio.parser.common.parser.StreamParser;
import com.brillianttiger.bio.parser.common.parser.XmlParserBase;
import com.brillianttiger.bio.parser.pubmed.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.brillianttiger.bio.parser.pubmed.parser.BookArticleParser.parsePubmedBookArticle;
import static com.brillianttiger.bio.parser.pubmed.parser.CommonElementParser.*;
import static com.brillianttiger.bio.parser.pubmed.parser.MedlineCitationParser.parseMedlineCitation;
import static com.brillianttiger.bio.parser.pubmed.parser.PubmedDataParser.parsePubmedData;

/**
 * PubmedXmlParser / PubMed XML 파서
 *
 * KR: PubMed XML 파일을 파싱하는 메인 클래스. DTD pubmed_250101 기준.
 * EN: Main class for parsing PubMed XML files. Based on DTD pubmed_250101.
 *
 * Features:
 * - StAX-based streaming parser for memory efficiency
 * - XXE attack prevention (from XmlParserBase)
 * - Automatic GZip file handling (from XmlParserBase)
 * - Full DTD pubmed_250101 compliance
 * - Support for PubmedArticle, PubmedBookArticle, DeleteCitation
 * - Support for %text; entity (mixed content with inline markup)
 *
 * DTD: https://dtd.nlm.nih.gov/ncbi/pubmed/out/pubmed_250101.dtd
 * Documentation: https://dtd.nlm.nih.gov/ncbi/pubmed/doc/out/250101/index.html
 */
public class PubmedXmlParser extends XmlParserBase implements StreamParser<PubmedArticle> {

    /**
     * 기본 생성자 / Default constructor
     *
     * KR: XXE 공격 방지 설정 자동 적용 (XmlParserBase에서 처리)
     * EN: XXE attack prevention automatically applied (handled by XmlParserBase)
     */
    public PubmedXmlParser() {
        super();
    }

    /**
     * PubMed XML 파일 전체 파싱 / Parse entire PubMed XML file
     *
     * KR: XML 파일을 파싱하여 PubmedArticleSet 반환 (GZip 자동 처리)
     * EN: Parse XML file and return PubmedArticleSet (auto-handles GZip)
     *
     * DTD: <!ELEMENT PubmedArticleSet ((PubmedArticle | PubmedBookArticle)+, DeleteCitation?)>
     *
     * @param path XML 파일 경로 (.gz 파일 자동 처리) / XML file path (auto-handles .gz)
     * @return PubmedArticleSet 객체 / PubmedArticleSet object
     * @throws Exception XML 파싱 또는 파일 읽기 오류 / XML parsing or file reading error
     */
    public PubmedArticleSet parseFile(Path path) throws Exception {
        try (var inputStream = openInputStream(path)) {
            XMLStreamReader reader = createReader(inputStream);

            try {
                return parsePubmedArticleSet(reader);
            } finally {
                reader.close();
            }
        }
    }

    /**
     * PubmedArticleSet 파싱 / Parse PubmedArticleSet
     *
     * DTD: <!ELEMENT PubmedArticleSet ((PubmedArticle | PubmedBookArticle)+, DeleteCitation?)>
     *
     * @param reader XMLStreamReader
     * @return PubmedArticleSet 객체 / PubmedArticleSet object
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     */
    public PubmedArticleSet parsePubmedArticleSet(XMLStreamReader reader) throws XMLStreamException {
        List<PubmedArticle> pubmedArticles = new ArrayList<>();
        List<PubmedBookArticle> pubmedBookArticles = new ArrayList<>();
        DeleteCitation deleteCitation = null;

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "PubmedArticle":
                        pubmedArticles.add(parseArticle(reader));
                        break;
                    case "PubmedBookArticle":
                        pubmedBookArticles.add(parsePubmedBookArticle(reader));
                        break;
                    case "DeleteCitation":
                        deleteCitation = parseDeleteCitation(reader);
                        break;
                    default:
                        // Skip unknown elements (but not the root)
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
     * PubmedArticle 파싱 / Parse PubmedArticle
     *
     * DTD: <!ELEMENT PubmedArticle (MedlineCitation, PubmedData?)>
     *
     * @param reader XMLStreamReader
     * @return PubmedArticle 객체 / PubmedArticle object
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     */
    public PubmedArticle parseArticle(XMLStreamReader reader) throws XMLStreamException {
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
     *
     * DTD: <!ELEMENT DeleteCitation (PMID+)>
     *
     * KR: FTP 업데이트 파일에서만 포함됨. 삭제된 PMID 목록.
     * EN: Included only in FTP update files. List of deleted PMIDs.
     *
     * @param reader XMLStreamReader
     * @return DeleteCitation 객체 / DeleteCitation object
     * @throws XMLStreamException XML 파싱 오류 / XML parsing error
     */
    public DeleteCitation parseDeleteCitation(XMLStreamReader reader) throws XMLStreamException {
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

    // ==================== StreamParser<PubmedArticle> 인터페이스 구현 ====================

    /**
     * 파일을 스트리밍 방식으로 파싱 / Parse file in streaming mode
     *
     * KR: 대용량 XML 파일을 메모리 효율적으로 파싱. 각 PubmedArticle이 파싱될 때마다 handler 호출.
     * EN: Memory-efficient parsing of large XML files. Handler is called for each parsed PubmedArticle.
     *
     * @param path XML 파일 경로 (.gz 자동 처리) / XML file path (auto-handles .gz)
     * @param handler PubmedArticle 처리 콜백 / PubmedArticle processing callback
     * @return 처리된 아이템 수 / Number of processed items
     * @throws Exception XML 파싱 또는 파일 읽기 오류 / XML parsing or file reading error
     */
    @Override
    public long parseStream(Path path, Consumer<PubmedArticle> handler) throws Exception {
        long count = 0;

        try (var inputStream = openInputStream(path)) {
            XMLStreamReader reader = createReader(inputStream);

            try {
                while (reader.hasNext()) {
                    int event = reader.next();

                    if (event == XMLStreamConstants.START_ELEMENT) {
                        String localName = reader.getLocalName();

                        if ("PubmedArticle".equals(localName)) {
                            PubmedArticle article = parseArticle(reader);
                            handler.accept(article);
                            count++;
                        } else if (!"PubmedArticleSet".equals(localName)) {
                            // Skip non-PubmedArticle elements (like PubmedBookArticle, DeleteCitation)
                            skipElement(reader);
                        }
                    }
                }
            } finally {
                reader.close();
            }
        }

        return count;
    }

    /**
     * 파일을 스트리밍 방식으로 파싱 (배치 처리) / Parse file in streaming mode (batch processing)
     *
     * KR: 대용량 XML 파일을 배치 단위로 메모리 효율적으로 파싱.
     * EN: Memory-efficient batch parsing of large XML files.
     *
     * @param path XML 파일 경로 (.gz 자동 처리) / XML file path (auto-handles .gz)
     * @param batchSize 배치 크기 / Batch size
     * @param handler 배치 처리 콜백 / Batch processing callback
     * @return 처리된 아이템 수 / Number of processed items
     * @throws Exception XML 파싱 또는 파일 읽기 오류 / XML parsing or file reading error
     */
    @Override
    public long parseStreamBatch(Path path, int batchSize, Consumer<List<PubmedArticle>> handler) throws Exception {
        long count = 0;
        List<PubmedArticle> batch = new ArrayList<>(batchSize);

        try (var inputStream = openInputStream(path)) {
            XMLStreamReader reader = createReader(inputStream);

            try {
                while (reader.hasNext()) {
                    int event = reader.next();

                    if (event == XMLStreamConstants.START_ELEMENT) {
                        String localName = reader.getLocalName();

                        if ("PubmedArticle".equals(localName)) {
                            PubmedArticle article = parseArticle(reader);
                            batch.add(article);
                            count++;

                            if (batch.size() >= batchSize) {
                                handler.accept(new ArrayList<>(batch));
                                batch.clear();
                            }
                        } else if (!"PubmedArticleSet".equals(localName)) {
                            skipElement(reader);
                        }
                    }
                }

                // 남은 배치 처리 / Process remaining batch
                if (!batch.isEmpty()) {
                    handler.accept(batch);
                }
            } finally {
                reader.close();
            }
        }

        return count;
    }

    // ==================== 추가 유틸리티 메서드 ====================

    /**
     * PubmedArticle + PubmedBookArticle 스트리밍 파싱 / Stream parse with BookArticle support
     *
     * KR: PubmedArticle과 PubmedBookArticle을 모두 처리하는 스트리밍 파서.
     * EN: Streaming parser that handles both PubmedArticle and PubmedBookArticle.
     *
     * @param path XML 파일 경로 (.gz 자동 처리) / XML file path (auto-handles .gz)
     * @param articleHandler PubmedArticle 처리 콜백 / PubmedArticle callback
     * @param bookArticleHandler PubmedBookArticle 처리 콜백 / PubmedBookArticle callback
     * @return 처리된 총 아이템 수 / Total number of processed items
     * @throws Exception XML 파싱 또는 파일 읽기 오류 / XML parsing or file reading error
     */
    public long parseStreamAll(Path path,
                                Consumer<PubmedArticle> articleHandler,
                                Consumer<PubmedBookArticle> bookArticleHandler) throws Exception {
        long count = 0;

        try (var inputStream = openInputStream(path)) {
            XMLStreamReader reader = createReader(inputStream);

            try {
                while (reader.hasNext()) {
                    int event = reader.next();

                    if (event == XMLStreamConstants.START_ELEMENT) {
                        String localName = reader.getLocalName();

                        switch (localName) {
                            case "PubmedArticle":
                                if (articleHandler != null) {
                                    PubmedArticle article = parseArticle(reader);
                                    articleHandler.accept(article);
                                    count++;
                                } else {
                                    skipElement(reader);
                                }
                                break;
                            case "PubmedBookArticle":
                                if (bookArticleHandler != null) {
                                    PubmedBookArticle bookArticle = parsePubmedBookArticle(reader);
                                    bookArticleHandler.accept(bookArticle);
                                    count++;
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
            } finally {
                reader.close();
            }
        }

        return count;
    }

    /**
     * DeleteCitation 추출 (FTP 업데이트 파일 전용) / Extract DeleteCitation (FTP update files only)
     *
     * KR: FTP 업데이트 파일에서 삭제된 PMID 목록을 추출.
     * EN: Extract list of deleted PMIDs from FTP update files.
     *
     * @param path XML 파일 경로 (.gz 자동 처리) / XML file path (auto-handles .gz)
     * @return DeleteCitation 객체 (없으면 null) / DeleteCitation object (null if not found)
     * @throws Exception XML 파싱 또는 파일 읽기 오류 / XML parsing or file reading error
     */
    public DeleteCitation extractDeleteCitation(Path path) throws Exception {
        try (var inputStream = openInputStream(path)) {
            XMLStreamReader reader = createReader(inputStream);

            try {
                while (reader.hasNext()) {
                    int event = reader.next();

                    if (event == XMLStreamConstants.START_ELEMENT) {
                        String localName = reader.getLocalName();

                        if ("DeleteCitation".equals(localName)) {
                            return parseDeleteCitation(reader);
                        }
                    }
                }
            } finally {
                reader.close();
            }
        }

        return null;
    }
}
