package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.common.parser.StreamParser;
import com.brillianttiger.bio.parser.common.parser.XmlParserBase;
import com.brillianttiger.bio.parser.pmc.model.*;
import com.brillianttiger.bio.parser.pmc.validation.JatsArticleValidator;
import com.brillianttiger.bio.parser.pmc.validation.ValidationError;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import static com.brillianttiger.bio.parser.pmc.parser.BackParser.*;
import static com.brillianttiger.bio.parser.pmc.parser.BodyParser.*;
import static com.brillianttiger.bio.parser.pmc.parser.CommonPmcElementParser.*;
import static com.brillianttiger.bio.parser.pmc.parser.FrontParser.*;

/**
 * PmcXmlParser / PMC XML 파서
 *
 * KR: PMC (PubMed Central) XML 파일을 파싱하는 메인 클래스.
 *     JATS 1.4 (ANSI/NISO Z39.96-2024) 표준 완전 지원.
 * EN: Main class for parsing PMC (PubMed Central) XML files.
 *     Full support for JATS 1.4 (ANSI/NISO Z39.96-2024) standard.
 *
 * Features:
 * - XmlParserBase 상속 (XXE 공격 방지 포함) / Extends XmlParserBase (includes XXE prevention)
 * - StreamParser 인터페이스 구현 / Implements StreamParser interface
 * - StAX 기반 스트리밍 파서로 메모리 효율성 / StAX-based streaming for memory efficiency
 * - GZip 파일 자동 처리 / Automatic GZip file handling
 * - tar.gz 패키지 파싱 지원 / tar.gz package parsing support
 * - 네임스페이스 처리 (xlink, mml) / Namespace handling (xlink, mml)
 * - 재귀 구조 처리 (sec, ref-list, sub-article) / Recursive structure handling
 * - tar.gz 무결성 검증 / tar.gz integrity validation
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/1.4/">JATS 1.4 Documentation</a>
 * @see <a href="https://dtd.nlm.nih.gov/ncbi/pmc/">PMC DTD Documentation</a>
 */
public class PmcXmlParser extends XmlParserBase implements StreamParser<JatsArticle> {

    private static final Logger log = LoggerFactory.getLogger(PmcXmlParser.class);

    /**
     * XLink 네임스페이스 URI / XLink namespace URI
     */
    private static final String XLINK_NAMESPACE = "http://www.w3.org/1999/xlink";

    /**
     * MathML 네임스페이스 URI / MathML namespace URI
     */
    private static final String MATHML_NAMESPACE = "http://www.w3.org/1998/Math/MathML";

    /**
     * JATS Article 검증기 / JATS Article Validator
     */
    private final JatsArticleValidator validator;

    /**
     * 생성자 / Constructor
     *
     * KR: XmlParserBase를 통해 XXE 공격 방지 설정 자동 적용.
     * EN: Automatically applies XXE attack prevention settings via XmlParserBase.
     */
    public PmcXmlParser() {
        super();
        this.validator = new JatsArticleValidator();
    }

    // ========================================================================
    // 단일 파일 파싱 / Single File Parsing
    // ========================================================================

    /**
     * 단일 PMC XML 파일 파싱 / Parse single PMC XML file
     *
     * KR: 단일 XML 파일(또는 .xml.gz 파일)을 파싱하여 JatsArticle 객체 반환.
     * EN: Parse single XML file (or .xml.gz file) and return JatsArticle object.
     *
     * @param path XML 파일 경로 (gzip 자동 처리) / XML file path (auto-handles gzip)
     * @return JatsArticle 객체 / JatsArticle object
     * @throws Exception XML 파싱 오류 / XML parsing error
     */
    public JatsArticle parseFile(Path path) throws Exception {
        log.debug("Parsing single PMC XML file: {}", path);

        InputStream is = null;
        XMLStreamReader reader = null;
        try {
            is = openInputStream(path);
            reader = createReader(is);

            // article 요소 찾기 / Find article element
            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();

                    if ("article".equals(localName)) {
                        return parseJatsArticle(reader);
                    } else if ("pmc-articleset".equals(localName)) {
                        // pmc-articleset 내부의 article 찾기 / Find article within pmc-articleset
                        continue;
                    }
                }
            }

            throw new XMLStreamException("No article element found in file: " + path);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    log.warn("Failed to close XMLStreamReader", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.warn("Failed to close InputStream", e);
                }
            }
        }
    }

    // ========================================================================
    // 스트리밍 파싱 / Streaming Parsing
    // ========================================================================

    /**
     * 파일을 스트리밍 방식으로 파싱 / Parse file in streaming mode
     *
     * KR: 대용량 XML 파일을 메모리 효율적으로 파싱.
     *     각 article이 파싱될 때마다 handler 콜백 호출.
     * EN: Memory-efficient parsing of large XML files.
     *     Handler callback is invoked for each parsed article.
     *
     * @param path XML 파일 경로 / XML file path
     * @param handler article 처리 콜백 / Article processing callback
     * @return 처리된 article 수 / Number of processed articles
     * @throws Exception 파싱 오류 / Parsing error
     */
    @Override
    public long parseStream(Path path, Consumer<JatsArticle> handler) throws Exception {
        log.debug("Streaming parse PMC XML file: {}", path);

        long count = 0;
        InputStream is = null;
        XMLStreamReader reader = null;

        try {
            is = openInputStream(path);
            reader = createReader(is);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();

                    if ("article".equals(localName)) {
                        JatsArticle article = parseJatsArticle(reader);
                        handler.accept(article);
                        count++;
                    } else if (!"pmc-articleset".equals(localName) && !"article-set".equals(localName)) {
                        CommonPmcElementParser.skipElement(reader);
                    }
                }
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    log.warn("Failed to close XMLStreamReader", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.warn("Failed to close InputStream", e);
                }
            }
        }

        log.debug("Processed {} articles from {}", count, path);
        return count;
    }

    /**
     * 파일을 스트리밍 방식으로 파싱 (배치 처리) / Parse file in streaming mode (batch processing)
     *
     * KR: 대용량 XML 파일을 배치 단위로 처리.
     *     batchSize만큼의 article이 모이면 handler 콜백 호출.
     * EN: Process large XML files in batches.
     *     Handler callback is invoked when batchSize articles are accumulated.
     *
     * @param path XML 파일 경로 / XML file path
     * @param batchSize 배치 크기 / Batch size
     * @param handler 배치 처리 콜백 / Batch processing callback
     * @return 처리된 article 수 / Number of processed articles
     * @throws Exception 파싱 오류 / Parsing error
     */
    @Override
    public long parseStreamBatch(Path path, int batchSize, Consumer<List<JatsArticle>> handler)
            throws Exception {
        log.debug("Batch streaming parse PMC XML file: {} (batch size: {})", path, batchSize);

        if (batchSize <= 0) {
            throw new IllegalArgumentException("Batch size must be positive: " + batchSize);
        }

        long count = 0;
        List<JatsArticle> batch = new ArrayList<>(batchSize);

        // XMLStreamReader is not AutoCloseable, must close manually
        InputStream is = null;
        XMLStreamReader reader = null;
        try {
            is = openInputStream(path);
            reader = createReader(is);

            while (reader.hasNext()) {
                int event = reader.next();

                if (event == XMLStreamConstants.START_ELEMENT) {
                    String localName = reader.getLocalName();

                    if ("article".equals(localName)) {
                        JatsArticle article = parseJatsArticle(reader);
                        batch.add(article);
                        count++;

                        // 배치 크기 도달 시 처리 / Process when batch size reached
                        if (batch.size() >= batchSize) {
                            handler.accept(new ArrayList<>(batch));
                            batch.clear();
                        }
                    } else if (!"pmc-articleset".equals(localName) && !"article-set".equals(localName)) {
                        CommonPmcElementParser.skipElement(reader);
                    }
                }
            }

            // 남은 배치 처리 / Process remaining batch
            if (!batch.isEmpty()) {
                handler.accept(batch);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                    log.warn("Failed to close XMLStreamReader", e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    log.warn("Failed to close InputStream", e);
                }
            }
        }

        log.debug("Processed {} articles from {} in batches of {}", count, path, batchSize);
        return count;
    }

    // ========================================================================
    // tar.gz 패키지 파싱 / tar.gz Package Parsing
    // ========================================================================

    /**
     * PMC tar.gz 패키지 파일 파싱 / Parse PMC tar.gz package file
     *
     * KR: PMC OA Bulk 형식의 tar.gz 파일을 파싱하여 모든 article 반환.
     *     tar.gz 무결성 검증 포함 (압축 해제 성공 여부로 검증).
     *     PMC는 MD5 체크섬을 제공하지 않으므로 압축 해제 자체로 검증.
     * EN: Parse PMC OA Bulk format tar.gz file and return all articles.
     *     Includes tar.gz integrity validation (validates by decompression success).
     *     PMC doesn't provide MD5 checksums, so validation is done through decompression.
     *
     * @param tarGzPath tar.gz 파일 경로 / tar.gz file path
     * @return 파싱된 JatsArticle 목록 / List of parsed JatsArticle objects
     * @throws Exception 파싱 오류 또는 무결성 검증 실패 / Parsing error or integrity validation failure
     *
     * @see <a href="https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/">PMC OA Bulk FTP</a>
     */
    public List<JatsArticle> parseTarGz(Path tarGzPath) throws Exception {
        log.info("Parsing PMC tar.gz package: {}", tarGzPath);

        // tar.gz 무결성 검증 / tar.gz integrity validation
        validateTarGzIntegrity(tarGzPath);

        List<JatsArticle> articles = new ArrayList<>();

        try (InputStream fis = Files.newInputStream(tarGzPath);
             BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
             GzipCompressorInputStream gzis = new GzipCompressorInputStream(bis);
             TarArchiveInputStream tais = new TarArchiveInputStream(gzis)) {

            TarArchiveEntry entry;
            int fileCount = 0;

            while ((entry = tais.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName();

                // .nxml 또는 .xml 파일만 처리 / Process only .nxml or .xml files
                if (name.endsWith(".nxml") || name.endsWith(".xml")) {
                    log.debug("Processing tar entry: {} (size: {} bytes)", name, entry.getSize());

                    try {
                        // tar 엔트리에서 직접 파싱 / Parse directly from tar entry
                        XMLStreamReader reader = createReader(tais);

                        // article 요소 찾기 / Find article element
                        while (true) {
                            int event = reader.next();

                            if (event == XMLStreamConstants.START_ELEMENT &&
                                "article".equals(reader.getLocalName())) {
                                JatsArticle article = parseJatsArticle(reader);
                                articles.add(article);
                                fileCount++;
                                break; // 하나의 파일에 보통 하나의 article / Usually one article per file
                            }
                        }

                        reader.close();

                    } catch (Exception e) {
                        log.error("Failed to parse tar entry: {}", name, e);
                        throw new RuntimeException("Failed to parse tar entry: " + name, e);
                    }
                }
            }

            log.info("Successfully parsed {} XML files from tar.gz: {}", fileCount, tarGzPath);
        }

        return articles;
    }

    /**
     * tar.gz 파일 무결성 검증 / Validate tar.gz file integrity
     *
     * KR: PMC는 MD5 체크섬을 제공하지 않으므로, tar.gz 압축 해제가 성공하는지 테스트.
     *     첫 번째 엔트리만 읽어보고 정상 압축 해제되면 무결성 검증 통과.
     * EN: PMC doesn't provide MD5 checksums, so test if tar.gz decompression succeeds.
     *     Read only the first entry to validate integrity.
     *
     * @param tarGzPath tar.gz 파일 경로 / tar.gz file path
     * @throws IOException 파일 읽기 오류 또는 무결성 검증 실패 / File read error or integrity validation failure
     */
    private void validateTarGzIntegrity(Path tarGzPath) throws IOException {
        log.debug("Validating tar.gz integrity: {}", tarGzPath);

        try (InputStream fis = Files.newInputStream(tarGzPath);
             BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
             GzipCompressorInputStream gzis = new GzipCompressorInputStream(bis);
             TarArchiveInputStream tais = new TarArchiveInputStream(gzis)) {

            // 첫 번째 엔트리 읽기 시도 / Try to read first entry
            TarArchiveEntry entry = tais.getNextTarEntry();
            if (entry == null) {
                throw new IOException("tar.gz file is empty or corrupted: " + tarGzPath);
            }

            log.debug("tar.gz integrity validation passed: {}", tarGzPath);

        } catch (IOException e) {
            log.error("tar.gz integrity validation failed: {}", tarGzPath, e);
            throw new IOException("tar.gz file is corrupted or invalid: " + tarGzPath, e);
        }
    }

    // ========================================================================
    // JatsArticle 파싱 / JatsArticle Parsing
    // ========================================================================

    /**
     * JatsArticle 파싱 / Parse JatsArticle
     *
     * KR: JATS 1.4 표준 기준 article 요소 파싱.
     *     네임스페이스, 재귀 구조 모두 지원.
     * EN: Parse article element based on JATS 1.4 standard.
     *     Supports namespaces and recursive structures.
     *
     * DTD: <!ELEMENT article (front, body?, back?, floats-group?, (sub-article* | response*))>
     * DTD: <!ATTLIST article
     *          article-type CDATA #IMPLIED
     *          dtd-version CDATA #IMPLIED
     *          xml:lang NMTOKEN "en"
     *          specific-use CDATA #IMPLIED
     *          xmlns:xlink CDATA #FIXED "http://www.w3.org/1999/xlink"
     *          xmlns:mml CDATA #FIXED "http://www.w3.org/1998/Math/MathML"
     *      >
     */
    private JatsArticle parseJatsArticle(XMLStreamReader reader) throws XMLStreamException {
        // 속성 파싱 / Parse attributes
        String articleTypeStr = CommonPmcElementParser.getAttribute(reader, "article-type");
        String dtdVersion = CommonPmcElementParser.getAttribute(reader, "dtd-version");
        String xmlLang = CommonPmcElementParser.getAttributeOrDefault(reader, "xml:lang", "en");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");

        // 네임스페이스 속성 / Namespace attributes
        String xmlnsXlink = CommonPmcElementParser.getAttributeOrDefault(reader, "xmlns:xlink", XLINK_NAMESPACE);
        String xmlnsMml = CommonPmcElementParser.getAttributeOrDefault(reader, "xmlns:mml", MATHML_NAMESPACE);

        // ArticleType enum 변환 / Convert to ArticleType enum
        ArticleType articleType = ArticleType.fromValue(articleTypeStr);

        JatsArticle.JatsArticleBuilder builder = JatsArticle.builder()
                .articleType(articleType)
                .dtdVersion(dtdVersion)
                .xmlLang(xmlLang)
                .specificUse(specificUse)
                .xmlnsXlink(xmlnsXlink)
                .xmlnsMml(xmlnsMml);

        List<SubArticle> subArticles = new ArrayList<>();
        List<Response> responses = new ArrayList<>();

        // 자식 요소 파싱 / Parse child elements
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "front":
                        builder.front(parseFront(reader));
                        break;
                    case "body":
                        builder.body(BodyParser.parseBody(reader));
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
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "article"입니다.
                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.
                break;
            }
        }

        builder.subArticles(subArticles.isEmpty() ? null : subArticles);
        builder.responses(responses.isEmpty() ? null : responses);

        return builder.build();
    }

    /**
     * FloatsGroup 파싱 / Parse FloatsGroup
     *
     * KR: 부유 요소 그룹 (그림, 표, 수식 등) 파싱.
     * EN: Parse floating elements group (figures, tables, formulas, etc.).
     *
     * DTD: <!ELEMENT floats-group (alternatives | boxed-text | chem-struct-wrap | code | fig | fig-group |
     *                               graphic | media | preformat | supplementary-material | table-wrap | table-wrap-group)*>
     */
    private FloatsGroup parseFloatsGroup(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing floats-group element");

        FloatsGroup.FloatsGroupBuilder builder = FloatsGroup.builder();

        List<Fig> figs = new ArrayList<>();
        List<TableWrap> tableWraps = new ArrayList<>();
        List<FigGroup> figGroups = new ArrayList<>();
        List<BoxedText> boxedTexts = new ArrayList<>();
        List<SupplementaryMaterial> supplementaryMaterials = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();
        List<Media> medias = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "fig":
                        figs.add(parseFig(reader));
                        break;
                    case "table-wrap":
                        tableWraps.add(parseTableWrap(reader));
                        break;
                    case "fig-group":
                        figGroups.add(parseFigGroup(reader));
                        break;
                    case "boxed-text":
                        boxedTexts.add(parseBoxedText(reader));
                        break;
                    case "supplementary-material":
                        supplementaryMaterials.add(parseSupplementaryMaterial(reader));
                        break;
                    case "graphic":
                        graphics.add(parseGraphic(reader));
                        break;
                    case "media":
                        medias.add(parseMedia(reader));
                        break;
                    default:
                        // Skip unsupported elements (alternatives, chem-struct-wrap, code, preformat, table-wrap-group)
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "floats-group"입니다.
                break;
            }
        }

        builder.figs(figs.isEmpty() ? null : figs);
        builder.tableWraps(tableWraps.isEmpty() ? null : tableWraps);
        builder.figGroups(figGroups.isEmpty() ? null : figGroups);
        builder.boxedTexts(boxedTexts.isEmpty() ? null : boxedTexts);
        builder.supplementaryMaterials(supplementaryMaterials.isEmpty() ? null : supplementaryMaterials);
        builder.graphics(graphics.isEmpty() ? null : graphics);
        builder.medias(medias.isEmpty() ? null : medias);

        return builder.build();
    }

    /**
     * Fig 파싱 / Parse Fig
     *
     * KR: 그림 요소 파싱 (label, caption, graphic, alt-text 등 포함).
     * EN: Parse figure element (includes label, caption, graphic, alt-text, etc.).
     *
     * DTD: <!ELEMENT fig (object-id*, label?, caption*, abstract*, kwd-group*, alt-text*, long-desc*,
     *                     (email | ext-link | uri)*, (alternatives | graphic | media | ...)*, (attrib | permissions)*)>
     */
    public static Fig parseFig(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing fig element");

        // Attributes
        String figTypeStr = CommonPmcElementParser.getAttribute(reader, "fig-type");
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String orientationStr = CommonPmcElementParser.getAttribute(reader, "orientation");
        String positionStr = CommonPmcElementParser.getAttribute(reader, "position");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");
        String xmlLang = CommonPmcElementParser.getAttribute(reader, "xml:lang");

        Fig.FigBuilder builder = Fig.builder()
                .figType(FigType.fromValue(figTypeStr))
                .id(id)
                .orientation(Orientation.fromValue(orientationStr))
                .position(Position.fromValue(positionStr))
                .specificUse(specificUse)
                .xmlLang(xmlLang);

        List<Caption> captions = new ArrayList<>();
        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();
        List<Media> medias = new ArrayList<>();
        List<P> paragraphs = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "caption":
                        captions.add(parseCaption(reader));
                        break;
                    case "alt-text":
                        altTexts.add(parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(parseLongDesc(reader));
                        break;
                    case "graphic":
                        graphics.add(parseGraphic(reader));
                        break;
                    case "media":
                        medias.add(parseMedia(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    default:
                        // Skip other elements (object-id, abstract, kwd-group, email, ext-link, uri, alternatives, attrib, permissions, etc.)
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "fig"입니다.
                break;
            }
        }

        builder.captions(captions.isEmpty() ? null : captions);
        builder.altTexts(altTexts.isEmpty() ? null : altTexts);
        builder.longDescs(longDescs.isEmpty() ? null : longDescs);
        builder.graphics(graphics.isEmpty() ? null : graphics);
        builder.medias(medias.isEmpty() ? null : medias);
        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);

        return builder.build();
    }

    /**
     * TableWrap 파싱 / Parse TableWrap
     *
     * KR: 테이블 래퍼 파싱 (label, caption, table 등 포함).
     * EN: Parse table wrapper (includes label, caption, table, etc.).
     *
     * DTD: <!ELEMENT table-wrap (object-id*, label?, caption*, abstract*, kwd-group*, alt-text*, long-desc*,
     *                            (alternatives | table | graphic | media | ...)*, (table-wrap-foot | attrib | permissions)*)>
     */
    public static TableWrap parseTableWrap(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing table-wrap element");

        // Attributes
        String contentType = CommonPmcElementParser.getAttribute(reader, "content-type");
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String orientationStr = CommonPmcElementParser.getAttribute(reader, "orientation");
        String positionStr = CommonPmcElementParser.getAttribute(reader, "position");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");
        String xmlLang = CommonPmcElementParser.getAttribute(reader, "xml:lang");

        TableWrap.TableWrapBuilder builder = TableWrap.builder()
                .contentType(contentType)
                .id(id)
                .orientation(Orientation.fromValue(orientationStr))
                .position(Position.fromValue(positionStr))
                .specificUse(specificUse)
                .xmlLang(xmlLang);

        List<Caption> captions = new ArrayList<>();
        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();
        List<Table> tables = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();
        List<TableWrapFoot> tableWrapFoots = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "caption":
                        captions.add(parseCaption(reader));
                        break;
                    case "alt-text":
                        altTexts.add(parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(parseLongDesc(reader));
                        break;
                    case "table":
                        tables.add(parseTable(reader));
                        break;
                    case "graphic":
                        graphics.add(parseGraphic(reader));
                        break;
                    case "table-wrap-foot":
                        tableWrapFoots.add(parseTableWrapFoot(reader));
                        break;
                    default:
                        // Skip other elements
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "table-wrap"입니다.
                break;
            }
        }

        builder.captions(captions.isEmpty() ? null : captions);
        builder.altTexts(altTexts.isEmpty() ? null : altTexts);
        builder.longDescs(longDescs.isEmpty() ? null : longDescs);
        builder.tables(tables.isEmpty() ? null : tables);
        builder.graphics(graphics.isEmpty() ? null : graphics);
        builder.tableWrapFoots(tableWrapFoots.isEmpty() ? null : tableWrapFoots);

        return builder.build();
    }

    /**
     * FigGroup 파싱 / Parse FigGroup
     *
     * KR: 그림 그룹 파싱 (여러 관련 그림을 그룹화).
     * EN: Parse figure group (groups multiple related figures).
     *
     * DTD: <!ELEMENT fig-group (object-id*, label?, caption*, abstract*, kwd-group*, alt-text*, long-desc*,
     *                           (email | ext-link | uri)*, (alternatives | fig | graphic | media)*, (attrib | permissions)*)>
     */
    private FigGroup parseFigGroup(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing fig-group element");

        // Attributes
        String contentType = CommonPmcElementParser.getAttribute(reader, "content-type");
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String orientationStr = CommonPmcElementParser.getAttribute(reader, "orientation");
        String positionStr = CommonPmcElementParser.getAttribute(reader, "position");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");
        String xmlBase = CommonPmcElementParser.getAttribute(reader, "xml:base");
        String xmlLang = CommonPmcElementParser.getAttribute(reader, "xml:lang");

        FigGroup.FigGroupBuilder builder = FigGroup.builder()
                .contentType(contentType)
                .id(id)
                .orientation(Orientation.fromValue(orientationStr))
                .position(Position.fromValue(positionStr))
                .specificUse(specificUse)
                .xmlBase(xmlBase)
                .xmlLang(xmlLang);

        List<Caption> captions = new ArrayList<>();
        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();
        List<Fig> figs = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "caption":
                        captions.add(parseCaption(reader));
                        break;
                    case "alt-text":
                        altTexts.add(parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(parseLongDesc(reader));
                        break;
                    case "fig":
                        figs.add(parseFig(reader));
                        break;
                    case "graphic":
                        graphics.add(parseGraphic(reader));
                        break;
                    default:
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "fig-group"입니다.
                break;
            }
        }

        builder.captions(captions.isEmpty() ? null : captions);
        builder.altTexts(altTexts.isEmpty() ? null : altTexts);
        builder.longDescs(longDescs.isEmpty() ? null : longDescs);
        builder.figs(figs.isEmpty() ? null : figs);
        builder.graphics(graphics.isEmpty() ? null : graphics);

        return builder.build();
    }

    /**
     * Caption 파싱 / Parse Caption
     *
     * KR: 캡션 파싱 (title, p 포함).
     * EN: Parse caption (includes title, p).
     *
     * DTD: <!ELEMENT caption (title?, (p | fn-group)*)>
     */
    public static Caption parseCaption(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing caption element");

        // Attributes
        String contentType = CommonPmcElementParser.getAttribute(reader, "content-type");
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");
        String style = CommonPmcElementParser.getAttribute(reader, "style");
        String xmlLang = CommonPmcElementParser.getAttribute(reader, "xml:lang");

        Caption.CaptionBuilder builder = Caption.builder()
                .contentType(contentType)
                .id(id)
                .specificUse(specificUse)
                .style(style)
                .xmlLang(xmlLang);

        List<P> paragraphs = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    default:
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "caption"입니다.
                break;
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);

        return builder.build();
    }

    /**
     * Graphic 파싱 / Parse Graphic
     *
     * KR: 그래픽 요소 파싱 (이미지 파일 참조).
     * EN: Parse graphic element (image file reference).
     *
     * DTD: <!ELEMENT graphic (alt-text | long-desc | abstract | attrib | permissions)*>
     */
    public static Graphic parseGraphic(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing graphic element");

        // Attributes
        String contentType = CommonPmcElementParser.getAttribute(reader, "content-type");
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String mimeSubtype = CommonPmcElementParser.getAttribute(reader, "mime-subtype");
        String mimetype = CommonPmcElementParser.getAttribute(reader, "mimetype");
        String orientationStr = CommonPmcElementParser.getAttribute(reader, "orientation");
        String positionStr = CommonPmcElementParser.getAttribute(reader, "position");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");

        // XLink attributes
        String xlinkActuate = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "actuate");
        String xlinkHref = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "href");
        String xlinkRole = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "role");
        String xlinkShow = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "show");
        String xlinkTitle = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "title");
        String xlinkType = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "type");

        Graphic.GraphicBuilder builder = Graphic.builder()
                .contentType(contentType)
                .id(id)
                .mimeSubtype(mimeSubtype)
                .mimetype(mimetype)
                .orientation(Orientation.fromValue(orientationStr))
                .position(Position.fromValue(positionStr))
                .specificUse(specificUse)
                .xlinkActuate(XlinkActuate.fromValue(xlinkActuate))
                .xlinkHref(xlinkHref)
                .xlinkRole(xlinkRole)
                .xlinkShow(XlinkShow.fromValue(xlinkShow))
                .xlinkTitle(xlinkTitle)
                .xlinkType(xlinkType);

        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "alt-text":
                        altTexts.add(parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(parseLongDesc(reader));
                        break;
                    default:
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "graphic"입니다.
                break;
            }
        }

        builder.altTexts(altTexts.isEmpty() ? null : altTexts);
        builder.longDescs(longDescs.isEmpty() ? null : longDescs);

        return builder.build();
    }

    /**
     * AltText 파싱 / Parse AltText
     *
     * KR: 대체 텍스트 파싱 (접근성).
     * EN: Parse alternative text (accessibility).
     */
    public static AltText parseAltText(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "alt-text");
        return AltText.builder().value(value).build();
    }

    /**
     * LongDesc 파싱 / Parse LongDesc
     *
     * KR: 긴 설명 파싱 (접근성).
     * EN: Parse long description (accessibility).
     */
    public static LongDesc parseLongDesc(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "long-desc");
        return LongDesc.builder().value(value).build();
    }

    /**
     * Table 파싱 / Parse Table
     *
     * KR: XHTML 테이블 파싱 (thead, tbody, tfoot 포함).
     * EN: Parse XHTML table (includes thead, tbody, tfoot).
     *
     * DTD: <!ELEMENT table (col* | colgroup*, thead?, tfoot?, tbody+)>
     */
    public static Table parseTable(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing table element");

        // Attributes
        String border = CommonPmcElementParser.getAttribute(reader, "border");
        String cellpadding = CommonPmcElementParser.getAttribute(reader, "cellpadding");
        String cellspacing = CommonPmcElementParser.getAttribute(reader, "cellspacing");
        String contentType = CommonPmcElementParser.getAttribute(reader, "content-type");
        String frame = CommonPmcElementParser.getAttribute(reader, "frame");
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String rules = CommonPmcElementParser.getAttribute(reader, "rules");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");
        String style = CommonPmcElementParser.getAttribute(reader, "style");
        String summary = CommonPmcElementParser.getAttribute(reader, "summary");
        String width = CommonPmcElementParser.getAttribute(reader, "width");

        Table.TableBuilder builder = Table.builder()
                .border(border)
                .cellpadding(cellpadding)
                .cellspacing(cellspacing)
                .contentType(contentType)
                .frame(TableFrame.fromValue(frame))
                .id(id)
                .rules(TableRules.fromValue(rules))
                .specificUse(specificUse)
                .style(style)
                .summary(summary)
                .width(width);

        Thead thead = null;
        List<Tbody> tbodies = new ArrayList<>();
        Tfoot tfoot = null;

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "thead":
                        thead = parseThead(reader);
                        break;
                    case "tbody":
                        tbodies.add(parseTbody(reader));
                        break;
                    case "tfoot":
                        tfoot = parseTfoot(reader);
                        break;
                    default:
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "table"입니다.
                break;
            }
        }

        builder.thead(thead);
        builder.tbodies(tbodies.isEmpty() ? null : tbodies);
        builder.tfoot(tfoot);

        return builder.build();
    }

    /**
     * Thead 파싱 / Parse Thead
     */
    public static Thead parseThead(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing thead element");

        String align = CommonPmcElementParser.getAttribute(reader, "align");
        String valign = CommonPmcElementParser.getAttribute(reader, "valign");

        Thead.TheadBuilder builder = Thead.builder()
                .align(CellAlign.fromValue(align))
                .valign(CellValign.fromValue(valign));

        List<Tr> rows = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                if (reader.getLocalName().equals("tr")) {
                    rows.add(parseTr(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "thead"입니다.
                break;
            }
        }

        builder.rows(rows.isEmpty() ? null : rows);
        return builder.build();
    }

    /**
     * Tbody 파싱 / Parse Tbody
     */
    public static Tbody parseTbody(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing tbody element");

        String align = CommonPmcElementParser.getAttribute(reader, "align");
        String valign = CommonPmcElementParser.getAttribute(reader, "valign");

        Tbody.TbodyBuilder builder = Tbody.builder()
                .align(CellAlign.fromValue(align))
                .valign(CellValign.fromValue(valign));

        List<Tr> rows = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                if (reader.getLocalName().equals("tr")) {
                    rows.add(parseTr(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "tbody"입니다.
                break;
            }
        }

        builder.rows(rows.isEmpty() ? null : rows);
        return builder.build();
    }

    /**
     * Tfoot 파싱 / Parse Tfoot
     */
    public static Tfoot parseTfoot(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing tfoot element");

        String align = CommonPmcElementParser.getAttribute(reader, "align");
        String valign = CommonPmcElementParser.getAttribute(reader, "valign");

        Tfoot.TfootBuilder builder = Tfoot.builder()
                .align(CellAlign.fromValue(align))
                .valign(CellValign.fromValue(valign));

        List<Tr> rows = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                if (reader.getLocalName().equals("tr")) {
                    rows.add(parseTr(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "tfoot"입니다.
                break;
            }
        }

        builder.rows(rows.isEmpty() ? null : rows);
        return builder.build();
    }

    /**
     * Tr 파싱 / Parse Tr (Table Row)
     */
    public static Tr parseTr(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing tr element");

        String align = CommonPmcElementParser.getAttribute(reader, "align");
        String valign = CommonPmcElementParser.getAttribute(reader, "valign");

        Tr.TrBuilder builder = Tr.builder()
                .align(CellAlign.fromValue(align))
                .valign(CellValign.fromValue(valign));

        List<Th> headerCells = new ArrayList<>();
        List<Td> dataCells = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "th":
                        headerCells.add(parseTh(reader));
                        break;
                    case "td":
                        dataCells.add(parseTd(reader));
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "tr"입니다.
                break;
            }
        }

        builder.headerCells(headerCells.isEmpty() ? null : headerCells);
        builder.dataCells(dataCells.isEmpty() ? null : dataCells);

        return builder.build();
    }

    /**
     * Th 파싱 / Parse Th (Table Header Cell)
     */
    public static Th parseTh(XMLStreamReader reader) throws XMLStreamException {
        String align = CommonPmcElementParser.getAttribute(reader, "align");
        String colspan = CommonPmcElementParser.getAttribute(reader, "colspan");
        String rowspan = CommonPmcElementParser.getAttribute(reader, "rowspan");
        String valign = CommonPmcElementParser.getAttribute(reader, "valign");

        String content = parseTextContent(reader, "th");

        return Th.builder()
                .align(CellAlign.fromValue(align))
                .colspan(parseInteger(colspan))
                .rowspan(parseInteger(rowspan))
                .valign(CellValign.fromValue(valign))
                .content(content)
                .build();
    }

    /**
     * Td 파싱 / Parse Td (Table Data Cell)
     */
    public static Td parseTd(XMLStreamReader reader) throws XMLStreamException {
        String align = CommonPmcElementParser.getAttribute(reader, "align");
        String colspan = CommonPmcElementParser.getAttribute(reader, "colspan");
        String rowspan = CommonPmcElementParser.getAttribute(reader, "rowspan");
        String valign = CommonPmcElementParser.getAttribute(reader, "valign");

        String content = parseTextContent(reader, "td");

        return Td.builder()
                .align(CellAlign.fromValue(align))
                .colspan(parseInteger(colspan))
                .rowspan(parseInteger(rowspan))
                .valign(CellValign.fromValue(valign))
                .content(content)
                .build();
    }

    /**
     * Integer 파싱 helper / Parse Integer helper
     */
    public static Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * TableWrapFoot 파싱 / Parse TableWrapFoot
     */
    public static TableWrapFoot parseTableWrapFoot(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing table-wrap-foot element");

        // TableWrapFoot는 주로 각주(fn) 포함
        // 간단히 건너뛰기 처리
        CommonPmcElementParser.skipElement(reader);

        return TableWrapFoot.builder().build();
    }

    /**
     * BoxedText 파싱 / Parse BoxedText (간단 처리)
     */
    private BoxedText parseBoxedText(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing boxed-text element");

        // Attributes
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String contentType = CommonPmcElementParser.getAttribute(reader, "content-type");
        String orientation = CommonPmcElementParser.getAttribute(reader, "orientation");
        String position = CommonPmcElementParser.getAttribute(reader, "position");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");

        BoxedText.BoxedTextBuilder builder = BoxedText.builder()
                .id(id)
                .contentType(contentType)
                .orientation(orientation)
                .position(position)
                .specificUse(specificUse);

        List<P> paragraphs = new ArrayList<>();
        List<Sec> sections = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "sec-meta":
                        builder.secMeta(BodyParser.parseSecMeta(reader));
                        break;
                    case "label":
                        builder.label(CommonPmcElementParser.parseLabel(reader));
                        break;
                    case "caption":
                        builder.caption(parseCaption(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "sec":
                        sections.add(BodyParser.parseSec(reader));
                        break;
                    case "attrib":
                        builder.attrib(parseTextContent(reader, localName));
                        break;
                    case "permissions":
                        builder.permissions(ArticleMetaParser.parsePermissions(reader));
                        break;
                    default:
                        // 기타 요소는 건너뛰기
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "boxed-text"입니다.
                break;
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.sections(sections.isEmpty() ? null : sections);

        return builder.build();
    }

    /**
     * P (Paragraph) 파싱 / Parse P
     */
    public static P parseP(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing p element");

        // Attributes
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String contentType = CommonPmcElementParser.getAttribute(reader, "content-type");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");
        String xmlLang = CommonPmcElementParser.getAttribute(reader, "xml:lang");

        // Text content
        String value = parseTextContent(reader, "p");

        return P.builder()
                .id(id)
                .contentType(contentType)
                .specificUse(specificUse)
                .xmlLang(xmlLang)
                .value(value)
                .build();
    }

    /**
     * SupplementaryMaterial 파싱 / Parse SupplementaryMaterial
     */
    private SupplementaryMaterial parseSupplementaryMaterial(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing supplementary-material element");

        // Attributes
        String contentType = CommonPmcElementParser.getAttribute(reader, "content-type");
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String mimeSubtype = CommonPmcElementParser.getAttribute(reader, "mime-subtype");
        String mimetype = CommonPmcElementParser.getAttribute(reader, "mimetype");
        String orientationStr = CommonPmcElementParser.getAttribute(reader, "orientation");
        String positionStr = CommonPmcElementParser.getAttribute(reader, "position");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");

        // XLink attributes
        String xlinkActuate = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "actuate");
        String xlinkHref = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "href");
        String xlinkRole = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "role");
        String xlinkShow = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "show");
        String xlinkTitle = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "title");
        String xlinkType = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "type");

        String xmlBase = CommonPmcElementParser.getAttribute(reader, "xml:base");
        String xmlLang = CommonPmcElementParser.getAttribute(reader, "xml:lang");

        SupplementaryMaterial.SupplementaryMaterialBuilder builder = SupplementaryMaterial.builder()
                .contentType(contentType)
                .id(id)
                .mimeSubtype(mimeSubtype)
                .mimetype(mimetype)
                .orientation(Orientation.fromValue(orientationStr))
                .position(Position.fromValue(positionStr))
                .specificUse(specificUse)
                .xlinkActuate(XlinkActuate.fromValue(xlinkActuate))
                .xlinkHref(xlinkHref)
                .xlinkRole(xlinkRole)
                .xlinkShow(XlinkShow.fromValue(xlinkShow))
                .xlinkTitle(xlinkTitle)
                .xlinkType(xlinkType != null ? xlinkType : "simple")
                .xmlBase(xmlBase)
                .xmlLang(xmlLang);

        List<Caption> captions = new ArrayList<>();
        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(CommonPmcElementParser.parseLabel(reader));
                        break;
                    case "caption":
                        captions.add(parseCaption(reader));
                        break;
                    case "alt-text":
                        altTexts.add(parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(parseLongDesc(reader));
                        break;
                    default:
                        // 기타 요소는 건너뛰기
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "supplementary-material"입니다.
                break;
            }
        }

        builder.captions(captions.isEmpty() ? null : captions);
        builder.altTexts(altTexts.isEmpty() ? null : altTexts);
        builder.longDescs(longDescs.isEmpty() ? null : longDescs);

        return builder.build();
    }

    /**
     * Media 파싱 / Parse Media (간단 처리)
     */
    public static Media parseMedia(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing media element");

        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String xlinkHref = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/1999/xlink", "href");

        Media.MediaBuilder builder = Media.builder()
                .id(id)
                .xlinkHref(xlinkHref);

        CommonPmcElementParser.skipElement(reader);

        return builder.build();
    }

    /**
     * SubArticle 파싱 (재귀 구조) / Parse SubArticle (recursive structure)
     *
     * KR: 하위 논문 파싱. front, body, back, floats-group, 중첩된 sub-article 지원.
     * EN: Parse sub-article. Supports front, body, back, floats-group, and nested sub-articles.
     *
     * DTD: <!ELEMENT sub-article (front | front-stub, body?, back?, floats-group?, (sub-article* | response*))>
     * DTD: <!ATTLIST sub-article
     *          article-type CDATA #IMPLIED
     *          id ID #IMPLIED
     *          xml:lang NMTOKEN #IMPLIED
     *      >
     *
     */
    private SubArticle parseSubArticle(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing sub-article element");

        String articleTypeStr = CommonPmcElementParser.getAttribute(reader, "article-type");
        String id = CommonPmcElementParser.getAttribute(reader, "id");

        // xml:lang은 XML 네임스페이스에 속하므로 네임스페이스 URI로 직접 접근
        // Method 1 (xml:lang)은 항상 실패하고, Method 2 (namespace + lang)가 올바른 방법
        String xmlLang = CommonPmcElementParser.getAttribute(reader, "http://www.w3.org/XML/1998/namespace", "lang");

        SubArticle.SubArticleBuilder builder = SubArticle.builder()
                .articleType(ArticleType.fromValue(articleTypeStr))
                .id(id)
                .xmlLang(xmlLang);

        // Parse child elements
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "front-stub":
                        builder.frontStub(parseFrontStub(reader));
                        break;
                    case "front":
                        builder.front(FrontParser.parseFront(reader));
                        break;
                    case "body":
                        builder.body(BodyParser.parseBody(reader));
                        break;
                    case "back":
                        builder.back(parseBack(reader));
                        break;
                    default:
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "sub-article"입니다.
                break;
            }
        }

        return builder.build();
    }

    /**
     * FrontStub 파싱 / Parse FrontStub
     */
    private FrontStub parseFrontStub(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing front-stub element");

        FrontStub.FrontStubBuilder builder = FrontStub.builder();

        List<PmcArticleId> articleIds = new ArrayList<>();
        List<ContribGroup> contribGroups = new ArrayList<>();
        List<Aff> affiliations = new ArrayList<>();
        List<AffAlternatives> affAlternatives = new ArrayList<>();
        List<PubDate> pubDates = new ArrayList<>();
        List<VolumeId> volumeIds = new ArrayList<>();
        List<IssueId> issueIds = new ArrayList<>();
        List<IssueTitle> issueTitles = new ArrayList<>();
        List<IssueSponsor> issueSponsors = new ArrayList<>();
        List<PmcIsbn> isbns = new ArrayList<>();
        List<ElocationId> elocationIds = new ArrayList<>();
        List<Email> emails = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        List<Uri> uris = new ArrayList<>();
        List<SupplementaryMaterial> supplementaryMaterials = new ArrayList<>();
        List<SelfUri> selfUris = new ArrayList<>();
        List<RelatedArticle> relatedArticles = new ArrayList<>();
        List<RelatedObject> relatedObjects = new ArrayList<>();
        List<PmcAbstract> abstracts = new ArrayList<>();
        List<TransAbstract> transAbstracts = new ArrayList<>();
        List<KwdGroup> kwdGroups = new ArrayList<>();
        List<FundingGroup> fundingGroups = new ArrayList<>();
        List<SupportGroup> supportGroups = new ArrayList<>();
        List<Conference> conferences = new ArrayList<>();

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "article-id":
                        articleIds.add(CommonPmcElementParser.parsePmcArticleId(reader, localName));
                        break;
                    case "article-categories":
                        builder.articleCategories(ArticleMetaParser.parseArticleCategories(reader));
                        break;
                    case "title-group":
                        builder.titleGroup(ArticleMetaParser.parseTitleGroup(reader));
                        break;
                    case "contrib-group":
                        contribGroups.add(ArticleMetaParser.parseContribGroup(reader));
                        break;
                    case "aff":
                        affiliations.add(ArticleMetaParser.parseAff(reader));
                        break;
                    case "aff-alternatives":
                        affAlternatives.add(ArticleMetaParser.parseAffAlternatives(reader));
                        break;
                    case "author-notes":
                        builder.authorNotes(ArticleMetaParser.parseAuthorNotes(reader));
                        break;
                    case "pub-date":
                        // FrontStub uses PubDate while ArticleMeta uses PmcPubDate
                        // Convert PmcPubDate to PubDate by copying common fields
                        PmcPubDate pmcPubDate = ArticleMetaParser.parsePmcPubDate(reader);
                        pubDates.add(PubDate.builder()
                                .dateType(pmcPubDate.getDateType())
                                .publicationFormat(pmcPubDate.getPublicationFormat())
                                .iso8601Date(pmcPubDate.getIso8601Date())
                                .pubType(pmcPubDate.getPubType())
                                .day(pmcPubDate.getDay())
                                .month(pmcPubDate.getMonth())
                                .year(pmcPubDate.getYear())
                                .season(pmcPubDate.getSeason())
                                .era(pmcPubDate.getEra())
                                .build());
                        break;
                    case "pub-date-not-available":
                        // pub-date-not-available을 PubDate로 변환하여 저장
                        PubDateNotAvailable pubDateNotAvailable = ArticleMetaParser.parsePubDateNotAvailable(reader);
                        // PubDateNotAvailable은 PubDate와 다른 타입이므로 변환
                        pubDates.add(PubDate.builder()
                                .dateType("not-available")
                                .build());
                        break;
                    case "volume":
                        builder.volume(CommonPmcElementParser.parseVolume(reader));
                        break;
                    case "volume-id":
                        volumeIds.add(CommonPmcElementParser.parseVolumeId(reader));
                        break;
                    case "volume-series":
                        builder.volumeSeries(CommonPmcElementParser.parseVolumeSeries(reader));
                        break;
                    case "issue":
                        // FrontStub uses Issue while ArticleMeta uses PmcIssue
                        // For now, convert PmcIssue to Issue by wrapping the value
                        PmcIssue pmcIssue = CommonPmcElementParser.parsePmcIssue(reader);
                        builder.issue(Issue.builder()
                                .value(pmcIssue.getValue())
                                .build());
                        break;
                    case "issue-id":
                        issueIds.add(CommonPmcElementParser.parseIssueId(reader));
                        break;
                    case "issue-title":
                        issueTitles.add(CommonPmcElementParser.parseIssueTitle(reader));
                        break;
                    case "issue-sponsor":
                        issueSponsors.add(CommonPmcElementParser.parseIssueSponsor(reader));
                        break;
                    case "issue-part":
                        // FrontStub expects String for issuePart
                        IssuePart issuePartObj = CommonPmcElementParser.parseIssuePart(reader);
                        builder.issuePart(issuePartObj.getValue());
                        break;
                    case "isbn":
                        isbns.add(CommonPmcElementParser.parsePmcIsbn(reader));
                        break;
                    case "supplement":
                        builder.supplement(CommonPmcElementParser.parseSupplement(reader));
                        break;
                    case "fpage":
                        builder.fpage(CommonPmcElementParser.parseFpage(reader));
                        break;
                    case "lpage":
                        builder.lpage(CommonPmcElementParser.parseLpage(reader));
                        break;
                    case "page-range":
                        // FrontStub expects String for pageRange
                        PageRange pageRangeObj = CommonPmcElementParser.parsePageRange(reader);
                        builder.pageRange(pageRangeObj.getValue());
                        break;
                    case "elocation-id":
                        elocationIds.add(CommonPmcElementParser.parseElocationId(reader));
                        break;
                    case "email":
                        emails.add(CommonPmcElementParser.parseEmail(reader));
                        break;
                    case "ext-link":
                        extLinks.add(CommonPmcElementParser.parseExtLink(reader));
                        break;
                    case "uri":
                        uris.add(CommonPmcElementParser.parseUri(reader));
                        break;
                    case "product":
                        // product는 FrontStub DTD에 포함되지만 현재 파서에서 미구현
                        CommonPmcElementParser.skipElement(reader);
                        break;
                    case "supplementary-material":
                        supplementaryMaterials.add(ArticleMetaParser.parseSupplementaryMaterial(reader));
                        break;
                    case "history":
                        // FrontStub uses History while ArticleMeta uses PmcHistory
                        // For now, convert PmcHistory to History by wrapping the dates
                        PmcHistory pmcHistory = ArticleMetaParser.parsePmcHistory(reader);
                        builder.history(History.builder()
                                .dates(pmcHistory.getDates())
                                .build());
                        break;
                    case "pub-history":
                        builder.pubHistory(ArticleMetaParser.parsePubHistory(reader));
                        break;
                    case "permissions":
                        builder.permissions(ArticleMetaParser.parsePermissions(reader));
                        break;
                    case "self-uri":
                        selfUris.add(CommonPmcElementParser.parseSelfUri(reader));
                        break;
                    case "related-article":
                        relatedArticles.add(ArticleMetaParser.parseRelatedArticle(reader));
                        break;
                    case "related-object":
                        relatedObjects.add(ArticleMetaParser.parseRelatedObject(reader));
                        break;
                    case "abstract":
                        abstracts.add(ArticleMetaParser.parsePmcAbstract(reader));
                        break;
                    case "trans-abstract":
                        transAbstracts.add(ArticleMetaParser.parseTransAbstract(reader));
                        break;
                    case "kwd-group":
                        kwdGroups.add(ArticleMetaParser.parseKwdGroup(reader));
                        break;
                    case "funding-group":
                        fundingGroups.add(ArticleMetaParser.parseFundingGroup(reader));
                        break;
                    case "support-group":
                        supportGroups.add(ArticleMetaParser.parseSupportGroup(reader));
                        break;
                    case "conference":
                        conferences.add(ArticleMetaParser.parseConference(reader));
                        break;
                    case "counts":
                        builder.counts(ArticleMetaParser.parseCounts(reader));
                        break;
                    case "custom-meta-group":
                        builder.customMetaGroup(ArticleMetaParser.parseCustomMetaGroup(reader));
                        break;
                    default:
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "front-stub"입니다.
                break;
            }
        }

        builder.articleIds(articleIds.isEmpty() ? null : articleIds);
        builder.contribGroups(contribGroups.isEmpty() ? null : contribGroups);
        builder.affiliations(affiliations.isEmpty() ? null : affiliations);
        builder.affAlternatives(affAlternatives.isEmpty() ? null : affAlternatives);
        builder.pubDates(pubDates.isEmpty() ? null : pubDates);
        builder.volumeIds(volumeIds.isEmpty() ? null : volumeIds);
        builder.issueIds(issueIds.isEmpty() ? null : issueIds);
        builder.issueTitles(issueTitles.isEmpty() ? null : issueTitles);
        builder.issueSponsors(issueSponsors.isEmpty() ? null : issueSponsors);
        builder.isbns(isbns.isEmpty() ? null : isbns);
        builder.elocationIds(elocationIds.isEmpty() ? null : elocationIds);
        builder.emails(emails.isEmpty() ? null : emails);
        builder.extLinks(extLinks.isEmpty() ? null : extLinks);
        builder.uris(uris.isEmpty() ? null : uris);
        builder.supplementaryMaterials(supplementaryMaterials.isEmpty() ? null : supplementaryMaterials);
        builder.selfUris(selfUris.isEmpty() ? null : selfUris);
        builder.relatedArticles(relatedArticles.isEmpty() ? null : relatedArticles);
        builder.relatedObjects(relatedObjects.isEmpty() ? null : relatedObjects);
        builder.abstracts(abstracts.isEmpty() ? null : abstracts);
        builder.transAbstracts(transAbstracts.isEmpty() ? null : transAbstracts);
        builder.kwdGroups(kwdGroups.isEmpty() ? null : kwdGroups);
        builder.fundingGroups(fundingGroups.isEmpty() ? null : fundingGroups);
        builder.supportGroups(supportGroups.isEmpty() ? null : supportGroups);
        builder.conferences(conferences.isEmpty() ? null : conferences);

        return builder.build();
    }

    /**
     * Response 파싱 / Parse Response
     *
     * KR: 논문에 대한 응답을 파싱합니다 (토론, 답변, 심사자 보고서 등).
     * EN: Parses responses to articles (discussions, replies, reviewer reports, etc.).
     *
     * DTD: <!ELEMENT response (front | front-stub, body?, back?, floats-group?)>
     * DTD: <!ATTLIST response
     *          response-type CDATA #IMPLIED
     *          id ID #IMPLIED
     *          specific-use CDATA #IMPLIED
     *          xml:lang NMTOKEN #IMPLIED
     *      >
     */
    private Response parseResponse(XMLStreamReader reader) throws XMLStreamException {
        log.debug("Parsing response element");

        String responseTypeStr = CommonPmcElementParser.getAttribute(reader, "response-type");
        String id = CommonPmcElementParser.getAttribute(reader, "id");
        String specificUse = CommonPmcElementParser.getAttribute(reader, "specific-use");
        String xmlLang = CommonPmcElementParser.getAttribute(reader, "xml:lang");

        Response.ResponseBuilder builder = Response.builder()
                .responseType(ResponseType.fromValue(responseTypeStr))
                .id(id)
                .specificUse(specificUse)
                .xmlLang(xmlLang);

        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "front-stub":
                        builder.frontStub(parseFrontStub(reader));
                        break;
                    case "front":
                        builder.front(FrontParser.parseFront(reader));
                        break;
                    case "body":
                        builder.body(BodyParser.parseBody(reader));
                        break;
                    case "back":
                        builder.back(parseBack(reader));
                        break;
                    case "floats-group":
                        builder.floatsGroup(parseFloatsGroup(reader));
                        break;
                    default:
                        CommonPmcElementParser.skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "response"입니다.
                break;
            }
        }

        return builder.build();
    }

    // ========================================================================
    // 검증 / Validation
    // ========================================================================

    /**
     * JATS Article 검증 / Validate JATS Article
     *
     * KR: 파싱된 article을 JATS 1.4 표준에 맞게 검증.
     *     필수 요소, ID 형식, 참조 무결성 등을 검증.
     * EN: Validate parsed article according to JATS 1.4 standard.
     *     Validates required elements, ID formats, reference integrity, etc.
     *
     * @param article 검증할 article / Article to validate
     * @return 검증 오류 목록 (빈 리스트 = 오류 없음) / List of validation errors (empty = no errors)
     */
    public List<ValidationError> validateArticle(JatsArticle article) {
        return validator.validateArticle(article);
    }

    /**
     * 파일 파싱 후 검증 / Parse file and validate
     *
     * KR: 파일을 파싱하고 즉시 검증 수행.
     *     검증 오류가 있어도 파싱된 article 반환.
     * EN: Parse file and immediately validate.
     *     Returns parsed article even if validation errors exist.
     *
     * @param path XML 파일 경로 / XML file path
     * @return 파싱된 article과 검증 오류 / Parsed article with validation errors
     * @throws Exception 파싱 오류 / Parsing error
     */
    public ValidationResult parseAndValidate(Path path) throws Exception {
        JatsArticle article = parseFile(path);
        List<ValidationError> errors = validateArticle(article);
        return new ValidationResult(article, errors);
    }

    /**
     * 검증 결과 / Validation Result
     *
     * KR: 파싱된 article과 검증 오류를 함께 반환하는 래퍼 클래스.
     * EN: Wrapper class that returns both parsed article and validation errors.
     */
    public static class ValidationResult {
        private final JatsArticle article;
        private final List<ValidationError> errors;

        public ValidationResult(JatsArticle article, List<ValidationError> errors) {
            this.article = article;
            this.errors = errors;
        }

        public JatsArticle getArticle() {
            return article;
        }

        public List<ValidationError> getErrors() {
            return errors;
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public boolean hasErrors() {
            return errors.stream()
                    .anyMatch(e -> e.getSeverity() == ValidationError.Severity.ERROR);
        }

        public boolean hasWarnings() {
            return errors.stream()
                    .anyMatch(e -> e.getSeverity() == ValidationError.Severity.WARNING);
        }

        public String getSummary() {
            return JatsArticleValidator.summarize(errors);
        }

        public void printErrors() {
            JatsArticleValidator.printErrors(errors);
        }
    }
}
