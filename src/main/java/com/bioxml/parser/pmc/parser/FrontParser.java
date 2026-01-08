package com.bioxml.parser.pmc.parser;

import com.bioxml.parser.pmc.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.bioxml.parser.pmc.parser.CommonPmcElementParser.*;

/**
 * FrontParser / Front 파서
 *
 * KR: PMC XML의 Front (전면부) 및 관련 요소들을 파싱하는 클래스
 * EN: Class for parsing Front (front matter) and related elements in PMC XML
 */
public class FrontParser {

    /**
     * Front 파싱 / Parse Front
     * DTD: <!ELEMENT front (journal-meta?, article-meta, notes?)>
     */
    public static Front parseFront(XMLStreamReader reader) throws XMLStreamException {
        Front.FrontBuilder builder = Front.builder();

        while (reader.hasNext()) {
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
                        builder.notes(parseNotes(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("front")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * JournalMeta 파싱 / Parse JournalMeta
     * DTD: <!ELEMENT journal-meta (journal-id+, journal-title-group?, issn*, isbn*, publisher?, notes?)>
     */
    public static JournalMeta parseJournalMeta(XMLStreamReader reader) throws XMLStreamException {
        JournalMeta.JournalMetaBuilder builder = JournalMeta.builder();

        List<JournalId> journalIds = new ArrayList<>();
        List<PmcIssn> issns = new ArrayList<>();
        List<PmcIsbn> isbns = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "journal-id":
                        journalIds.add(parseJournalId(reader));
                        break;
                    case "journal-title-group":
                        builder.journalTitleGroup(parseJournalTitleGroup(reader));
                        break;
                    case "issn":
                        issns.add(parsePmcIssn(reader));
                        break;
                    case "isbn":
                        isbns.add(parsePmcIsbn(reader));
                        break;
                    case "publisher":
                        builder.publisher(parsePmcPublisher(reader));
                        break;
                    case "notes":
                        builder.notes(parseNotes(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("journal-meta")) {
                    break;
                }
            }
        }

        builder.journalIds(journalIds.isEmpty() ? null : journalIds);
        builder.issns(issns.isEmpty() ? null : issns);
        builder.isbns(isbns.isEmpty() ? null : isbns);

        return builder.build();
    }

    /**
     * JournalId 파싱 / Parse JournalId
     * DTD: <!ELEMENT journal-id (#PCDATA)>
     * DTD: <!ATTLIST journal-id journal-id-type CDATA #IMPLIED>
     */
    public static JournalId parseJournalId(XMLStreamReader reader) throws XMLStreamException {
        String journalIdType = reader.getAttributeValue(null, "journal-id-type");
        String value = parseTextContent(reader, "journal-id");

        return JournalId.builder()
                .journalIdType(journalIdType)
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

        while (reader.hasNext()) {
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
                if (reader.getLocalName().equals("journal-title-group")) {
                    break;
                }
            }
        }

        builder.journalTitles(journalTitles.isEmpty() ? null : journalTitles);
        builder.journalSubtitles(journalSubtitles.isEmpty() ? null : journalSubtitles);
        builder.transTitleGroups(transTitleGroups.isEmpty() ? null : transTitleGroups);
        builder.abbrevJournalTitles(abbrevJournalTitles.isEmpty() ? null : abbrevJournalTitles);

        return builder.build();
    }

    /**
     * PmcIssn 파싱 / Parse PmcIssn
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
     * PmcPublisher 파싱 / Parse PmcPublisher
     * DTD: <!ELEMENT publisher (publisher-name, publisher-loc?)>
     */
    public static PmcPublisher parsePmcPublisher(XMLStreamReader reader) throws XMLStreamException {
        PmcPublisher.PmcPublisherBuilder builder = PmcPublisher.builder();

        while (reader.hasNext()) {
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
                if (reader.getLocalName().equals("publisher")) {
                    break;
                }
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

    public static TransTitleGroup parseTransTitleGroup(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "trans-title-group");
        return TransTitleGroup.builder().value(value).build();
    }

    public static AbbrevJournalTitle parseAbbrevJournalTitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "abbrev-journal-title");
        return AbbrevJournalTitle.builder().value(value).build();
    }
}
