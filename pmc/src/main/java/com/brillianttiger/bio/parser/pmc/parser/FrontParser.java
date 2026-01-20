package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pmc.parser.CommonPmcElementParser.*;

/**
 * FrontParser / Front 파서
 *
 * KR: PMC XML의 Front (전면부) 및 관련 요소들을 파싱하는 클래스
 * EN: Class for parsing Front (front matter) and related elements in PMC XML
 */
public class FrontParser {

    /**
     * Front 파싱 / Parse Front
     * DTD: <!ELEMENT front (journal-meta?, article-meta, (def-list | list | ack | bio | fn-group | glossary | notes)*)>
     */
    public static Front parseFront(XMLStreamReader reader) throws XMLStreamException {
        Front.FrontBuilder builder = Front.builder();

        List<Notes> notesList = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "journal-meta":
                        builder.journalMeta(parseJournalMeta(reader));
                        break;
                    case "article-meta":
                        builder.articleMeta(ArticleMetaParser.parseArticleMeta(reader));
                        break;
                    case "notes":
                        notesList.add(parseNotes(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "front"입니다.
                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.
                break;
            }
        }

        builder.notesList(notesList.isEmpty() ? null : notesList);
        return builder.build();
    }

    /**
     * JournalMeta 파싱 / Parse JournalMeta
     * DTD: <!ELEMENT journal-meta (journal-id+, journal-title-group?, issn*, isbn*, publisher?, notes?)>
     */
    public static JournalMeta parseJournalMeta(XMLStreamReader reader) throws XMLStreamException {
        JournalMeta.JournalMetaBuilder builder = JournalMeta.builder();

        List<JournalId> journalIds = new ArrayList<>();
        List<JournalTitleGroup> journalTitleGroups = new ArrayList<>();
        List<Issn> issns = new ArrayList<>();
        List<PmcIsbn> isbns = new ArrayList<>();
        List<Notes> notesList = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "journal-id":
                        journalIds.add(parseJournalId(reader));
                        break;
                    case "journal-title-group":
                        journalTitleGroups.add(parseJournalTitleGroup(reader));
                        break;
                    case "issn":
                        issns.add(parseIssn(reader));
                        break;
                    case "isbn":
                        isbns.add(parsePmcIsbn(reader));
                        break;
                    case "publisher":
                        builder.publisher(parsePublisher(reader));
                        break;
                    case "notes":
                        notesList.add(parseNotes(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "journal-meta"입니다.
                break;
            }
        }

        builder.journalIds(journalIds.isEmpty() ? null : journalIds);
        builder.journalTitleGroups(journalTitleGroups.isEmpty() ? null : journalTitleGroups);
        builder.issns(issns.isEmpty() ? null : issns);
        builder.isbns(isbns.isEmpty() ? null : isbns);
        builder.notesList(notesList.isEmpty() ? null : notesList);

        return builder.build();
    }

    /**
     * JournalId 파싱 / Parse JournalId
     * DTD: <!ELEMENT journal-id (#PCDATA)>
     * DTD: <!ATTLIST journal-id journal-id-type CDATA #IMPLIED>
     */
    public static JournalId parseJournalId(XMLStreamReader reader) throws XMLStreamException {
        String journalIdTypeStr = reader.getAttributeValue(null, "journal-id-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlLang = reader.getAttributeValue(null, "xml:lang");
        String value = parseTextContent(reader, "journal-id");

        return JournalId.builder()
                .journalIdType(JournalIdType.fromValue(journalIdTypeStr))
                .specificUse(specificUse)
                .xmlLang(xmlLang)
                .value(value)
                .build();
    }

    /**
     * JournalTitleGroup 파싱 / Parse JournalTitleGroup
     * DTD: <!ELEMENT journal-title-group (journal-title*, journal-subtitle*, trans-title-group*, abbrev-journal-title*)>
     */
    public static JournalTitleGroup parseJournalTitleGroup(XMLStreamReader reader) throws XMLStreamException {
        JournalTitleGroup.JournalTitleGroupBuilder builder = JournalTitleGroup.builder();

        List<JournalTitle> journalTitles = new ArrayList<>();
        List<JournalSubtitle> journalSubtitles = new ArrayList<>();
        List<TransTitleGroup> transTitleGroups = new ArrayList<>();
        List<AbbrevJournalTitle> abbrevJournalTitles = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "journal-title":
                        journalTitles.add(parseJournalTitle(reader));
                        break;
                    case "journal-subtitle":
                        journalSubtitles.add(parseJournalSubtitle(reader));
                        break;
                    case "trans-title-group":
                        transTitleGroups.add(parseTransTitleGroup(reader));
                        break;
                    case "abbrev-journal-title":
                        abbrevJournalTitles.add(parseAbbrevJournalTitle(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "journal-title-group"입니다.
                break;
            }
        }

        builder.journalTitles(journalTitles.isEmpty() ? null : journalTitles);
        builder.journalSubtitles(journalSubtitles.isEmpty() ? null : journalSubtitles);
        builder.transTitleGroups(transTitleGroups.isEmpty() ? null : transTitleGroups);
        builder.abbrevJournalTitles(abbrevJournalTitles.isEmpty() ? null : abbrevJournalTitles);

        return builder.build();
    }

    /**
     * Issn 파싱 / Parse Issn
     * DTD: <!ELEMENT issn (#PCDATA)>
     * DTD: <!ATTLIST issn
     *          content-type CDATA #IMPLIED
     *          publication-format (print | electronic | print-electronic | online) #IMPLIED
     *          pub-type (ppub | epub | ppub-epub | epub-ppub | collection | epreprint) #IMPLIED>
     */
    public static Issn parseIssn(XMLStreamReader reader) throws XMLStreamException {
        String contentType = reader.getAttributeValue(null, "content-type");
        String publicationFormatStr = reader.getAttributeValue(null, "publication-format");
        String pubTypeStr = reader.getAttributeValue(null, "pub-type");
        String value = parseTextContent(reader, "issn");

        return Issn.builder()
                .contentType(contentType)
                .publicationFormat(PublicationFormat.fromValue(publicationFormatStr))
                .pubType(PubType.fromValue(pubTypeStr))
                .value(value)
                .build();
    }

    /**
     * PmcIssn 파싱 / Parse PmcIssn (backward compatibility)
     * DTD: <!ATTLIST issn pub-type (ppub | epub) #IMPLIED content-type CDATA #IMPLIED>
     */
    public static PmcIssn parsePmcIssn(XMLStreamReader reader) throws XMLStreamException {
        String pubType = reader.getAttributeValue(null, "pub-type");
        String contentType = reader.getAttributeValue(null, "content-type");
        String value = parseTextContent(reader, "issn");

        return PmcIssn.builder()
                .pubType(pubType)
                .contentType(contentType)
                .value(value)
                .build();
    }

    /**
     * Publisher 파싱 / Parse Publisher
     * DTD: <!ELEMENT publisher (publisher-name+, publisher-loc*)>
     */
    public static Publisher parsePublisher(XMLStreamReader reader) throws XMLStreamException {
        Publisher.PublisherBuilder builder = Publisher.builder();

        List<PublisherName> publisherNames = new ArrayList<>();
        List<PublisherLoc> publisherLocs = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "publisher-name":
                        publisherNames.add(parsePublisherName(reader));
                        break;
                    case "publisher-loc":
                        publisherLocs.add(parsePublisherLoc(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "publisher"입니다.
                break;
            }
        }

        builder.publisherNames(publisherNames.isEmpty() ? null : publisherNames);
        builder.publisherLocs(publisherLocs.isEmpty() ? null : publisherLocs);

        return builder.build();
    }

    /**
     * PmcPublisher 파싱 / Parse PmcPublisher (backward compatibility)
     * DTD: <!ELEMENT publisher (publisher-name, publisher-loc?)>
     */
    public static PmcPublisher parsePmcPublisher(XMLStreamReader reader) throws XMLStreamException {
        PmcPublisher.PmcPublisherBuilder builder = PmcPublisher.builder();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "publisher-name":
                        builder.publisherName(parsePublisherName(reader));
                        break;
                    case "publisher-loc":
                        builder.publisherLoc(parsePublisherLoc(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "publisher"입니다.
                break;
            }
        }

        return builder.build();
    }

    /**
     * Notes 파싱 / Parse Notes
     */
    public static Notes parseNotes(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "notes");
        return Notes.builder().value(value).build();
    }

    // Simple element parsers
    public static JournalTitle parseJournalTitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "journal-title");
        return JournalTitle.builder().value(value).build();
    }

    public static JournalSubtitle parseJournalSubtitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "journal-subtitle");
        return JournalSubtitle.builder().value(value).build();
    }

    /**
     * TransTitleGroup 파싱 / Parse TransTitleGroup
     * DTD: <!ELEMENT trans-title-group (trans-title, trans-subtitle*)>
     * DTD: <!ATTLIST trans-title-group xml:lang NMTOKEN #IMPLIED>
     */
    public static TransTitleGroup parseTransTitleGroup(XMLStreamReader reader) throws XMLStreamException {
        // Delegate to ArticleMetaParser for proper JATS 1.4 parsing
        return ArticleMetaParser.parseTransTitleGroup(reader);
    }

    public static AbbrevJournalTitle parseAbbrevJournalTitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "abbrev-journal-title");
        return AbbrevJournalTitle.builder().value(value).build();
    }
}
