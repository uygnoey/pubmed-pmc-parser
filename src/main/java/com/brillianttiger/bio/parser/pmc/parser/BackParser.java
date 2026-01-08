package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pmc.parser.CommonPmcElementParser.*;

/**
 * BackParser / Back 파서
 *
 * KR: PMC XML의 Back (후면부), RefList, Ref, Citation 요소들을 파싱하는 클래스
 * EN: Class for parsing Back (back matter), RefList, Ref, and Citation elements in PMC XML
 */
public class BackParser {

    /**
     * Back 파싱 / Parse Back
     * DTD: <!ELEMENT back (label?, title*, ack*, app-group*, bio*, fn-group*,
     *                      glossary*, ref-list*, notes*, sec*)>
     */
    public static Back parseBack(XMLStreamReader reader) throws XMLStreamException {
        Back.BackBuilder builder = Back.builder();

        List<Title> titles = new ArrayList<>();
        List<Ack> acknowledgments = new ArrayList<>();
        List<AppGroup> appGroups = new ArrayList<>();
        List<Bio> biographies = new ArrayList<>();
        List<FnGroup> fnGroups = new ArrayList<>();
        List<Glossary> glossaries = new ArrayList<>();
        List<RefList> refLists = new ArrayList<>();
        List<Notes> notesList = new ArrayList<>();
        List<Sec> sections = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "title":
                        titles.add(parseTitle(reader));
                        break;
                    case "ack":
                        acknowledgments.add(parseAck(reader));
                        break;
                    case "app-group":
                        appGroups.add(parseAppGroup(reader));
                        break;
                    case "bio":
                        biographies.add(parseBio(reader));
                        break;
                    case "fn-group":
                        fnGroups.add(parseFnGroup(reader));
                        break;
                    case "glossary":
                        glossaries.add(parseGlossary(reader));
                        break;
                    case "ref-list":
                        refLists.add(parseRefList(reader));
                        break;
                    case "notes":
                        notesList.add(parseNotes(reader));
                        break;
                    case "sec":
                        sections.add(BodyParser.parseSec(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("back")) {
                    break;
                }
            }
        }

        builder.titles(titles.isEmpty() ? null : titles);
        builder.acknowledgments(acknowledgments.isEmpty() ? null : acknowledgments);
        builder.appGroups(appGroups.isEmpty() ? null : appGroups);
        builder.biographies(biographies.isEmpty() ? null : biographies);
        builder.fnGroups(fnGroups.isEmpty() ? null : fnGroups);
        builder.glossaries(glossaries.isEmpty() ? null : glossaries);
        builder.refLists(refLists.isEmpty() ? null : refLists);
        builder.notesList(notesList.isEmpty() ? null : notesList);
        builder.sections(sections.isEmpty() ? null : sections);

        return builder.build();
    }

    /**
     * RefList 파싱 (재귀 구조) / Parse RefList (recursive structure)
     * DTD: <!ELEMENT ref-list (label?, title?, (%ref-list.class;)*, ref*, ref-list*)>
     */
    public static RefList parseRefList(XMLStreamReader reader) throws XMLStreamException {
        RefList.RefListBuilder builder = RefList.builder();

        List<Ref> references = new ArrayList<>();
        List<RefList> refLists = new ArrayList<>();

        while (reader.hasNext()) {
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
                    case "ref":
                        references.add(parseRef(reader));
                        break;
                    case "ref-list":
                        // 재귀: 하위 RefList 파싱 / Recursive: parse sub-RefList
                        refLists.add(parseRefList(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ref-list")) {
                    break;
                }
            }
        }

        builder.references(references.isEmpty() ? null : references);
        builder.refLists(refLists.isEmpty() ? null : refLists);

        return builder.build();
    }

    /**
     * Ref 파싱 / Parse Ref
     * DTD: <!ELEMENT ref (label?, (%citation.class;)+)>
     * DTD: <!ATTLIST ref id ID #IMPLIED>
     */
    public static Ref parseRef(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");

        Ref.RefBuilder builder = Ref.builder()
                .id(id);

        List<ElementCitation> elementCitations = new ArrayList<>();
        List<MixedCitation> mixedCitations = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "element-citation":
                        elementCitations.add(parseElementCitation(reader));
                        break;
                    case "mixed-citation":
                        mixedCitations.add(parseMixedCitation(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ref")) {
                    break;
                }
            }
        }

        builder.elementCitations(elementCitations.isEmpty() ? null : elementCitations);
        builder.mixedCitations(mixedCitations.isEmpty() ? null : mixedCitations);

        return builder.build();
    }

    /**
     * ElementCitation 파싱 / Parse ElementCitation
     * DTD: <!ELEMENT element-citation (%element-citation-model;)*>
     * DTD: <!ATTLIST element-citation publication-type CDATA #IMPLIED>
     */
    public static ElementCitation parseElementCitation(XMLStreamReader reader) throws XMLStreamException {
        String publicationType = reader.getAttributeValue(null, "publication-type");

        ElementCitation.ElementCitationBuilder builder = ElementCitation.builder()
                .publicationType(publicationType);

        List<PersonGroup> personGroups = new ArrayList<>();
        List<PmcArticleId> articleIds = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "person-group":
                        personGroups.add(parsePersonGroup(reader));
                        break;
                    case "article-title":
                        builder.articleTitle(ArticleMetaParser.parsePmcArticleTitle(reader));
                        break;
                    case "source":
                        builder.source(parseSource(reader));
                        break;
                    case "year":
                        builder.year(parseYear(reader));
                        break;
                    case "month":
                        builder.month(parseMonth(reader));
                        break;
                    case "day":
                        builder.day(parseDay(reader));
                        break;
                    case "volume":
                        builder.volume(parseVolume(reader));
                        break;
                    case "issue":
                        builder.issue(parsePmcIssue(reader));
                        break;
                    case "fpage":
                        builder.fpage(parseFpage(reader));
                        break;
                    case "lpage":
                        builder.lpage(parseLpage(reader));
                        break;
                    case "page-range":
                        builder.pageRange(parsePageRange(reader));
                        break;
                    case "elocation-id":
                        builder.elocationId(parseElocationId(reader));
                        break;
                    case "publisher-name":
                        builder.publisherName(parsePublisherName(reader));
                        break;
                    case "publisher-loc":
                        builder.publisherLoc(parsePublisherLoc(reader));
                        break;
                    case "edition":
                        builder.edition(parseEdition(reader));
                        break;
                    case "chapter-title":
                        builder.chapterTitle(parseChapterTitle(reader));
                        break;
                    case "conf-name":
                        builder.confName(parseConfName(reader));
                        break;
                    case "conf-loc":
                        builder.confLoc(parseConfLoc(reader));
                        break;
                    case "article-id":
                    case "pub-id":
                        articleIds.add(parsePmcArticleId(reader, localName));
                        break;
                    case "comment":
                        builder.comment(parseComment(reader));
                        break;
                    case "ext-link":
                        extLinks.add(parseExtLink(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("element-citation")) {
                    break;
                }
            }
        }

        builder.personGroups(personGroups.isEmpty() ? null : personGroups);
        builder.articleIds(articleIds.isEmpty() ? null : articleIds);
        builder.extLinks(extLinks.isEmpty() ? null : extLinks);

        return builder.build();
    }

    /**
     * MixedCitation 파싱 / Parse MixedCitation
     * DTD: <!ELEMENT mixed-citation (#PCDATA | %citation-elements;)*>
     * DTD: <!ATTLIST mixed-citation publication-type CDATA #IMPLIED>
     */
    public static MixedCitation parseMixedCitation(XMLStreamReader reader) throws XMLStreamException {
        String publicationType = reader.getAttributeValue(null, "publication-type");

        MixedCitation.MixedCitationBuilder builder = MixedCitation.builder()
                .publicationType(publicationType);

        List<PersonGroup> personGroups = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        StringBuilder textContent = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "person-group":
                        personGroups.add(parsePersonGroup(reader));
                        break;
                    case "article-title":
                        builder.articleTitle(ArticleMetaParser.parsePmcArticleTitle(reader));
                        break;
                    case "source":
                        builder.source(parseSource(reader));
                        break;
                    case "year":
                        builder.year(parseYear(reader));
                        break;
                    case "month":
                        builder.month(parseMonth(reader));
                        break;
                    case "volume":
                        builder.volume(parseVolume(reader));
                        break;
                    case "issue":
                        builder.issue(parsePmcIssue(reader));
                        break;
                    case "fpage":
                        builder.fpage(parseFpage(reader));
                        break;
                    case "lpage":
                        builder.lpage(parseLpage(reader));
                        break;
                    case "publisher-name":
                        builder.publisherName(parsePublisherName(reader));
                        break;
                    case "ext-link":
                        extLinks.add(parseExtLink(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.CHARACTERS) {
                textContent.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("mixed-citation")) {
                    break;
                }
            }
        }

        builder.value(textContent.toString().trim());
        builder.personGroups(personGroups.isEmpty() ? null : personGroups);
        builder.extLinks(extLinks.isEmpty() ? null : extLinks);

        return builder.build();
    }

    /**
     * PersonGroup 파싱 / Parse PersonGroup
     */
    public static PersonGroup parsePersonGroup(XMLStreamReader reader) throws XMLStreamException {
        String personGroupType = reader.getAttributeValue(null, "person-group-type");

        PersonGroup.PersonGroupBuilder builder = PersonGroup.builder()
                .personGroupType(personGroupType);

        List<PersonName> names = new ArrayList<>();
        List<Collab> collabs = new ArrayList<>();
        List<Etal> etals = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "name":
                        names.add(parsePersonName(reader));
                        break;
                    case "collab":
                        collabs.add(parseCollab(reader));
                        break;
                    case "etal":
                        etals.add(parseEtal(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("person-group")) {
                    break;
                }
            }
        }

        builder.names(names.isEmpty() ? null : names);
        builder.collabs(collabs.isEmpty() ? null : collabs);
        builder.etals(etals.isEmpty() ? null : etals);
        return builder.build();
    }

    /**
     * PersonName 파싱 / Parse PersonName (name 요소)
     */
    public static PersonName parsePersonName(XMLStreamReader reader) throws XMLStreamException {
        PersonName.PersonNameBuilder builder = PersonName.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "surname":
                        builder.surname(parseSurname(reader));
                        break;
                    case "given-names":
                        builder.givenNames(parseGivenNames(reader));
                        break;
                    case "prefix":
                        builder.prefix(parsePrefix(reader));
                        break;
                    case "suffix":
                        builder.suffix(parseSuffix(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("name")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    // Simple element parsers
    public static Ack parseAck(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "ack");
        return Ack.builder().value(value).build();
    }

    public static AppGroup parseAppGroup(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "app-group");
        return AppGroup.builder().value(value).build();
    }

    public static Bio parseBio(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "bio");
        return Bio.builder().value(value).build();
    }

    public static FnGroup parseFnGroup(XMLStreamReader reader) throws XMLStreamException {
        List<Fn> footnotes = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("fn".equals(localName)) {
                    footnotes.add(ArticleMetaParser.parseFn(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("fn-group")) {
                    break;
                }
            }
        }

        return FnGroup.builder()
                .footnotes(footnotes.isEmpty() ? null : footnotes)
                .build();
    }

    public static Glossary parseGlossary(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "glossary");
        return Glossary.builder().value(value).build();
    }

    public static Notes parseNotes(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "notes");
        return Notes.builder().value(value).build();
    }

    public static Collab parseCollab(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "collab");
        return Collab.builder().value(value).build();
    }

    public static Etal parseEtal(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "etal");
        return Etal.builder().value(value).build();
    }

    public static Surname parseSurname(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "surname");
        return Surname.builder().value(value).build();
    }

    public static GivenNames parseGivenNames(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "given-names");
        return GivenNames.builder().value(value).build();
    }

    public static Prefix parsePrefix(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "prefix");
        return Prefix.builder().value(value).build();
    }

    public static Suffix parseSuffix(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "suffix");
        return Suffix.builder().value(value).build();
    }
}
