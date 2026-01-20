package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pmc.parser.CommonPmcElementParser.*;
import static com.brillianttiger.bio.parser.pmc.parser.ArticleMetaParser.parsePermissions;

/**
 * BodyParser / Body 파서
 *
 * KR: PMC XML의 Body (본문) 및 Sec (섹션) 요소들을 파싱하는 클래스
 * EN: Class for parsing Body and Sec (section) elements in PMC XML
 */
public class BodyParser {

    /**
     * Body 파싱 / Parse Body
     * DTD: <!ELEMENT body (%body-model;)*>
     */
    public static Body parseBody(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        Body.BodyBuilder builder = Body.builder()
                .id(id)
                .specificUse(specificUse);

        List<Sec> sections = new ArrayList<>();
        List<P> paragraphs = new ArrayList<>();
        List<PmcList> lists = new ArrayList<>();
        List<DefList> defLists = new ArrayList<>();
        List<DispQuote> dispQuotes = new ArrayList<>();
        List<BoxedText> boxedTexts = new ArrayList<>();
        List<Code> codeBlocks = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "sec":
                        sections.add(parseSec(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "list":
                        lists.add(parseList(reader));
                        break;
                    case "def-list":
                        defLists.add(parseDefList(reader));
                        break;
                    case "disp-quote":
                        dispQuotes.add(parseDispQuote(reader));
                        break;
                    case "boxed-text":
                        boxedTexts.add(parseBoxedText(reader));
                        break;
                    case "code":
                        codeBlocks.add(parseCode(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "body"입니다.
                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.
                break;
            }
        }

        builder.sections(sections.isEmpty() ? null : sections);
        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.lists(lists.isEmpty() ? null : lists);
        builder.defLists(defLists.isEmpty() ? null : defLists);
        builder.dispQuotes(dispQuotes.isEmpty() ? null : dispQuotes);
        builder.boxedTexts(boxedTexts.isEmpty() ? null : boxedTexts);
        builder.codeBlocks(codeBlocks.isEmpty() ? null : codeBlocks);

        return builder.build();
    }

    /**
     * Sec 파싱 (재귀 구조) / Parse Sec (recursive structure)
     * DTD: <!ELEMENT sec (sec-meta?, label?, title?, (%sec-model;)*, (%sec-back-matter-mix;)*, sec*)>
     * DTD: <!ATTLIST sec id ID #IMPLIED sec-type CDATA #IMPLIED xml:lang CDATA #IMPLIED>
     */
    public static Sec parseSec(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String dispLevel = reader.getAttributeValue(null, "disp-level");
        String secType = reader.getAttributeValue(null, "sec-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlLang = reader.getAttributeValue(null, "xml:lang");

        Sec.SecBuilder builder = Sec.builder()
                .id(id)
                .dispLevel(dispLevel)
                .secType(secType)
                .specificUse(specificUse)
                .xmlLang(xmlLang);

        List<P> paragraphs = new ArrayList<>();
        List<Sec> sections = new ArrayList<>();
        List<PmcList> lists = new ArrayList<>();
        List<DefList> defLists = new ArrayList<>();
        List<DispQuote> dispQuotes = new ArrayList<>();
        List<BoxedText> boxedTexts = new ArrayList<>();
        List<Code> codeBlocks = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "sec-meta":
                        builder.secMeta(parseSecMeta(reader));
                        break;
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "sec":
                        // 재귀: 하위 섹션 파싱 / Recursive: parse sub-section
                        sections.add(parseSec(reader));
                        break;
                    case "list":
                        lists.add(parseList(reader));
                        break;
                    case "def-list":
                        defLists.add(parseDefList(reader));
                        break;
                    case "disp-quote":
                        dispQuotes.add(parseDispQuote(reader));
                        break;
                    case "boxed-text":
                        boxedTexts.add(parseBoxedText(reader));
                        break;
                    case "code":
                        codeBlocks.add(parseCode(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "sec"입니다.
                break;
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.sections(sections.isEmpty() ? null : sections);
        builder.lists(lists.isEmpty() ? null : lists);
        builder.defLists(defLists.isEmpty() ? null : defLists);
        builder.dispQuotes(dispQuotes.isEmpty() ? null : dispQuotes);
        builder.boxedTexts(boxedTexts.isEmpty() ? null : boxedTexts);
        builder.codeBlocks(codeBlocks.isEmpty() ? null : codeBlocks);

        return builder.build();
    }

    /**
     * SecMeta 파싱 / Parse SecMeta
     */
    public static SecMeta parseSecMeta(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "sec-meta");
        return SecMeta.builder().value(value).build();
    }

    /**
     * PmcList 파싱 / Parse PmcList
     * DTD: <!ELEMENT list (object-id*, label?, title?, list-item+, ...)>
     * DTD: <!ATTLIST list id ID #IMPLIED
     *                   list-type CDATA #IMPLIED
     *                   continued-from IDREF #IMPLIED
     *                   list-content CDATA #IMPLIED
     *                   prefix-word CDATA #IMPLIED
     *                   specific-use CDATA #IMPLIED>
     */
    public static PmcList parseList(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String listType = reader.getAttributeValue(null, "list-type");
        String continuedFrom = reader.getAttributeValue(null, "continued-from");
        String listContent = reader.getAttributeValue(null, "list-content");
        String prefixWord = reader.getAttributeValue(null, "prefix-word");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        PmcList.PmcListBuilder builder = PmcList.builder()
                .id(id)
                .listType(listType)
                .continuedFrom(continuedFrom)
                .listContent(listContent)
                .prefixWord(prefixWord)
                .specificUse(specificUse);

        List<ListItem> items = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "list-item":
                        items.add(parseListItem(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "list"입니다.
                break;
            }
        }

        builder.items(items.isEmpty() ? null : items);
        return builder.build();
    }

    /**
     * ListItem 파싱 / Parse ListItem
     * DTD: <!ELEMENT list-item (object-id*, label?, title?, (%para-level;)*)>
     * DTD: <!ATTLIST list-item id ID #IMPLIED specific-use CDATA #IMPLIED>
     */
    public static ListItem parseListItem(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        ListItem.ListItemBuilder builder = ListItem.builder()
                .id(id)
                .specificUse(specificUse);

        List<P> paragraphs = new ArrayList<>();
        List<PmcList> nestedLists = new ArrayList<>();
        List<DefList> defLists = new ArrayList<>();
        List<BoxedText> boxedTexts = new ArrayList<>();
        List<DispQuote> dispQuotes = new ArrayList<>();
        List<Code> codeBlocks = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "list":
                        // 재귀: 중첩된 리스트 파싱 / Recursive: parse nested list
                        nestedLists.add(parseList(reader));
                        break;
                    case "def-list":
                        defLists.add(parseDefList(reader));
                        break;
                    case "boxed-text":
                        boxedTexts.add(parseBoxedText(reader));
                        break;
                    case "disp-quote":
                        dispQuotes.add(parseDispQuote(reader));
                        break;
                    case "code":
                        codeBlocks.add(parseCode(reader));
                        break;
                    default:
                        // Skip fig, table-wrap, disp-formula and other elements for now
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "list-item"입니다.
                break;
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.nestedLists(nestedLists.isEmpty() ? null : nestedLists);
        builder.defLists(defLists.isEmpty() ? null : defLists);
        builder.boxedTexts(boxedTexts.isEmpty() ? null : boxedTexts);
        builder.dispQuotes(dispQuotes.isEmpty() ? null : dispQuotes);
        builder.codeBlocks(codeBlocks.isEmpty() ? null : codeBlocks);

        return builder.build();
    }

    /**
     * DefList 파싱 / Parse DefList (Definition List)
     * DTD: <!ELEMENT def-list (label?, title?, term-head?, def-head?, def-item+, def-list*)>
     * DTD: <!ATTLIST def-list id ID #IMPLIED
     *                        list-type CDATA #IMPLIED
     *                        continued-from IDREF #IMPLIED
     *                        list-content CDATA #IMPLIED
     *                        prefix-word CDATA #IMPLIED
     *                        specific-use CDATA #IMPLIED>
     */
    public static DefList parseDefList(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String listType = reader.getAttributeValue(null, "list-type");
        String continuedFrom = reader.getAttributeValue(null, "continued-from");
        String listContent = reader.getAttributeValue(null, "list-content");
        String prefixWord = reader.getAttributeValue(null, "prefix-word");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        DefList.DefListBuilder builder = DefList.builder()
                .id(id)
                .listType(listType)
                .continuedFrom(continuedFrom)
                .listContent(listContent)
                .prefixWord(prefixWord)
                .specificUse(specificUse);

        List<DefItem> items = new ArrayList<>();
        List<DefList> nestedDefLists = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "def-item":
                        items.add(parseDefItem(reader));
                        break;
                    case "def-list":
                        // 재귀: 중첩된 정의 리스트 파싱 / Recursive: parse nested def-list
                        nestedDefLists.add(parseDefList(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "def-list"입니다.
                break;
            }
        }

        builder.items(items.isEmpty() ? null : items);
        builder.nestedDefLists(nestedDefLists.isEmpty() ? null : nestedDefLists);

        return builder.build();
    }

    /**
     * DefItem 파싱 / Parse DefItem
     * DTD: <!ELEMENT def-item (label?, term+, def*)>
     * DTD: <!ATTLIST def-item id ID #IMPLIED specific-use CDATA #IMPLIED>
     */
    public static DefItem parseDefItem(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        DefItem.DefItemBuilder builder = DefItem.builder()
                .id(id)
                .specificUse(specificUse);

        List<Term> terms = new ArrayList<>();
        List<Def> definitions = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "term":
                        terms.add(parseTerm(reader));
                        break;
                    case "def":
                        definitions.add(parseDef(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "def-item"입니다.
                break;
            }
        }

        builder.terms(terms.isEmpty() ? null : terms);
        builder.definitions(definitions.isEmpty() ? null : definitions);

        return builder.build();
    }

    /**
     * Term 파싱 / Parse Term
     * DTD: <!ELEMENT term (#PCDATA | %term-elements;)*>
     * DTD: <!ATTLIST term id ID #IMPLIED
     *                    rid IDREFS #IMPLIED
     *                    term-status CDATA #IMPLIED
     *                    vocab CDATA #IMPLIED
     *                    vocab-identifier CDATA #IMPLIED
     *                    vocab-term CDATA #IMPLIED
     *                    vocab-term-identifier CDATA #IMPLIED
     *                    specific-use CDATA #IMPLIED>
     */
    public static Term parseTerm(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String rid = reader.getAttributeValue(null, "rid");
        String termStatus = reader.getAttributeValue(null, "term-status");
        String vocab = reader.getAttributeValue(null, "vocab");
        String vocabIdentifier = reader.getAttributeValue(null, "vocab-identifier");
        String vocabTerm = reader.getAttributeValue(null, "vocab-term");
        String vocabTermIdentifier = reader.getAttributeValue(null, "vocab-term-identifier");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        String value = parseTextContent(reader, "term");

        return Term.builder()
                .id(id)
                .rid(rid)
                .termStatus(termStatus)
                .vocab(vocab)
                .vocabIdentifier(vocabIdentifier)
                .vocabTerm(vocabTerm)
                .vocabTermIdentifier(vocabTermIdentifier)
                .specificUse(specificUse)
                .value(value)
                .build();
    }

    /**
     * Def 파싱 / Parse Def (Definition)
     * DTD: <!ELEMENT def (label?, title?, (%para-level;)*)>
     * DTD: <!ATTLIST def id ID #IMPLIED rid IDREFS #IMPLIED specific-use CDATA #IMPLIED>
     */
    public static Def parseDef(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String rid = reader.getAttributeValue(null, "rid");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        Def.DefBuilder builder = Def.builder()
                .id(id)
                .rid(rid)
                .specificUse(specificUse);

        List<P> paragraphs = new ArrayList<>();
        List<DefList> defLists = new ArrayList<>();
        List<PmcList> lists = new ArrayList<>();
        List<BoxedText> boxedTexts = new ArrayList<>();
        List<DispQuote> dispQuotes = new ArrayList<>();
        List<Code> codeBlocks = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "def-list":
                        defLists.add(parseDefList(reader));
                        break;
                    case "list":
                        lists.add(parseList(reader));
                        break;
                    case "boxed-text":
                        boxedTexts.add(parseBoxedText(reader));
                        break;
                    case "disp-quote":
                        dispQuotes.add(parseDispQuote(reader));
                        break;
                    case "code":
                        codeBlocks.add(parseCode(reader));
                        break;
                    default:
                        // Skip fig, table-wrap, disp-formula and other elements for now
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "def"입니다.
                break;
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.defLists(defLists.isEmpty() ? null : defLists);
        builder.lists(lists.isEmpty() ? null : lists);
        builder.boxedTexts(boxedTexts.isEmpty() ? null : boxedTexts);
        builder.dispQuotes(dispQuotes.isEmpty() ? null : dispQuotes);
        builder.codeBlocks(codeBlocks.isEmpty() ? null : codeBlocks);

        return builder.build();
    }

    /**
     * DispQuote 파싱 / Parse DispQuote (Displayed Quote)
     * DTD: <!ELEMENT disp-quote (object-id*, label?, title?, (%para-level;)*, (attrib | permissions)*)>
     * DTD: <!ATTLIST disp-quote id ID #IMPLIED
     *                          content-type CDATA #IMPLIED
     *                          specific-use CDATA #IMPLIED
     *                          xml:lang NMTOKEN #IMPLIED>
     */
    public static DispQuote parseDispQuote(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String contentType = reader.getAttributeValue(null, "content-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlLang = reader.getAttributeValue(null, "xml:lang");

        DispQuote.DispQuoteBuilder builder = DispQuote.builder()
                .id(id)
                .contentType(contentType)
                .specificUse(specificUse)
                .xmlLang(xmlLang);

        List<P> paragraphs = new ArrayList<>();
        List<PmcList> lists = new ArrayList<>();
        List<DefList> defLists = new ArrayList<>();
        List<DispQuote> nestedQuotes = new ArrayList<>();
        List<Code> codeBlocks = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "list":
                        lists.add(parseList(reader));
                        break;
                    case "def-list":
                        defLists.add(parseDefList(reader));
                        break;
                    case "disp-quote":
                        // 재귀: 중첩된 인용구 파싱 / Recursive: parse nested quote
                        nestedQuotes.add(parseDispQuote(reader));
                        break;
                    case "code":
                        codeBlocks.add(parseCode(reader));
                        break;
                    case "attrib":
                        builder.attrib(parseTextContent(reader, "attrib"));
                        break;
                    case "permissions":
                        builder.permissions(parsePermissions(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "disp-quote"입니다.
                break;
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.lists(lists.isEmpty() ? null : lists);
        builder.defLists(defLists.isEmpty() ? null : defLists);
        builder.nestedQuotes(nestedQuotes.isEmpty() ? null : nestedQuotes);
        builder.codeBlocks(codeBlocks.isEmpty() ? null : codeBlocks);

        return builder.build();
    }

    /**
     * BoxedText 파싱 / Parse BoxedText
     * DTD: <!ELEMENT boxed-text (object-id*, sec-meta?, label?, caption?, (%para-level;)*, sec*, (attrib | permissions)*)>
     * DTD: <!ATTLIST boxed-text id ID #IMPLIED
     *                          content-type CDATA #IMPLIED
     *                          orientation (portrait | landscape) #IMPLIED
     *                          position (anchor | background | float | margin) "float"
     *                          specific-use CDATA #IMPLIED>
     */
    public static BoxedText parseBoxedText(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String contentType = reader.getAttributeValue(null, "content-type");
        String orientation = reader.getAttributeValue(null, "orientation");
        String position = reader.getAttributeValue(null, "position");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        BoxedText.BoxedTextBuilder builder = BoxedText.builder()
                .id(id)
                .contentType(contentType)
                .orientation(orientation)
                .position(position)
                .specificUse(specificUse);

        List<P> paragraphs = new ArrayList<>();
        List<Sec> sections = new ArrayList<>();
        List<PmcList> lists = new ArrayList<>();
        List<DefList> defLists = new ArrayList<>();
        List<DispQuote> dispQuotes = new ArrayList<>();
        List<Code> codeBlocks = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "sec-meta":
                        builder.secMeta(parseSecMeta(reader));
                        break;
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "caption":
                        builder.caption(parseCaption(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "sec":
                        sections.add(parseSec(reader));
                        break;
                    case "list":
                        lists.add(parseList(reader));
                        break;
                    case "def-list":
                        defLists.add(parseDefList(reader));
                        break;
                    case "disp-quote":
                        dispQuotes.add(parseDispQuote(reader));
                        break;
                    case "code":
                        codeBlocks.add(parseCode(reader));
                        break;
                    case "attrib":
                        builder.attrib(parseTextContent(reader, "attrib"));
                        break;
                    case "permissions":
                        builder.permissions(parsePermissions(reader));
                        break;
                    default:
                        // Skip fig, table-wrap, disp-formula and other elements for now
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "boxed-text"입니다.
                break;
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.sections(sections.isEmpty() ? null : sections);
        builder.lists(lists.isEmpty() ? null : lists);
        builder.defLists(defLists.isEmpty() ? null : defLists);
        builder.dispQuotes(dispQuotes.isEmpty() ? null : dispQuotes);
        builder.codeBlocks(codeBlocks.isEmpty() ? null : codeBlocks);

        return builder.build();
    }

    /**
     * Code 파싱 / Parse Code
     * DTD: <!ELEMENT code (#PCDATA | %code-elements;)*>
     * DTD: <!ATTLIST code id ID #IMPLIED
     *                    code-type CDATA #IMPLIED
     *                    code-version CDATA #IMPLIED
     *                    executable (yes | no) "no"
     *                    language CDATA #IMPLIED
     *                    language-version CDATA #IMPLIED
     *                    orientation (portrait | landscape) #IMPLIED
     *                    platforms CDATA #IMPLIED
     *                    position (anchor | background | float | margin) #IMPLIED
     *                    specific-use CDATA #IMPLIED>
     */
    public static Code parseCode(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String codeType = reader.getAttributeValue(null, "code-type");
        String codeVersion = reader.getAttributeValue(null, "code-version");
        String executable = reader.getAttributeValue(null, "executable");
        String language = reader.getAttributeValue(null, "language");
        String languageVersion = reader.getAttributeValue(null, "language-version");
        String orientation = reader.getAttributeValue(null, "orientation");
        String platforms = reader.getAttributeValue(null, "platforms");
        String position = reader.getAttributeValue(null, "position");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        String value = parseTextContent(reader, "code");

        return Code.builder()
                .id(id)
                .codeType(codeType)
                .codeVersion(codeVersion)
                .executable(executable)
                .language(language)
                .languageVersion(languageVersion)
                .orientation(orientation)
                .platforms(platforms)
                .position(position)
                .specificUse(specificUse)
                .value(value)
                .build();
    }

    /**
     * Caption 파싱 / Parse Caption
     * DTD: <!ELEMENT caption (title?, (p | fn-group)*)>
     * DTD: <!ATTLIST caption
     *                content-type CDATA #IMPLIED
     *                id ID #IMPLIED
     *                specific-use CDATA #IMPLIED
     *                style CDATA #IMPLIED
     *                xml:lang NMTOKEN #IMPLIED>
     */
    public static Caption parseCaption(XMLStreamReader reader) throws XMLStreamException {
        String contentType = reader.getAttributeValue(null, "content-type");
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String style = reader.getAttributeValue(null, "style");
        String xmlLang = reader.getAttributeValue(null, "xml:lang");

        Caption.CaptionBuilder builder = Caption.builder()
                .contentType(contentType)
                .id(id)
                .specificUse(specificUse)
                .style(style)
                .xmlLang(xmlLang);

        List<P> paragraphs = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
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
                        skipElement(reader);
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
}
