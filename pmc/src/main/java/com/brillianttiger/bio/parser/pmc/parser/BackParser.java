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
                // Note: 이 시점에서 END_ELEMENT는 항상 "back"입니다.
                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.
                break;
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
                // Note: 이 시점에서 END_ELEMENT는 항상 "ref-list"입니다.
                break;
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
                // Note: 이 시점에서 END_ELEMENT는 항상 "ref"입니다.
                break;
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
        List<PubId> pubIds = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
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
                        articleIds.add(parsePmcArticleId(reader, localName));
                        break;
                    case "pub-id":
                        pubIds.add(parsePubId(reader));
                        break;
                    case "comment":
                        comments.add(parseComment(reader));
                        break;
                    case "ext-link":
                        extLinks.add(parseExtLink(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "element-citation"입니다.
                break;
            }
        }

        builder.personGroups(personGroups.isEmpty() ? null : personGroups);
        builder.articleIds(articleIds.isEmpty() ? null : articleIds);
        builder.pubIds(pubIds.isEmpty() ? null : pubIds);
        builder.extLinks(extLinks.isEmpty() ? null : extLinks);
        builder.comments(comments.isEmpty() ? null : comments);

        return builder.build();
    }

    /**
     * MixedCitation 파싱 / Parse MixedCitation
     * DTD: <!ELEMENT mixed-citation (#PCDATA | %citation-elements;)*>
     * DTD: <!ATTLIST mixed-citation publication-type CDATA #IMPLIED>
     *
     * Note: MixedCitation preserves ALL text content including text from nested elements,
     * while also extracting structured data.
     */
    public static MixedCitation parseMixedCitation(XMLStreamReader reader) throws XMLStreamException {
        String publicationType = reader.getAttributeValue(null, "publication-type");

        MixedCitation.MixedCitationBuilder builder = MixedCitation.builder()
                .publicationType(publicationType);

        List<PersonGroup> personGroups = new ArrayList<>();
        List<StringName> stringNames = new ArrayList<>();
        List<Collab> collabs = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        List<PubId> pubIds = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        StringBuilder textContent = new StringBuilder();

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                // Collect text from child element while also parsing structure
                String childText = null;

                switch (localName) {
                    case "person-group":
                        childText = collectTextAndParsePersonGroup(reader, personGroups);
                        break;
                    case "article-title":
                        PmcArticleTitle articleTitle = ArticleMetaParser.parsePmcArticleTitle(reader);
                        builder.articleTitle(articleTitle);
                        childText = articleTitle.getValue();
                        break;
                    case "source":
                        Source source = parseSource(reader);
                        builder.source(source);
                        childText = source.getValue();
                        break;
                    case "year":
                        Year year = parseYear(reader);
                        builder.year(year);
                        childText = year.getValue();
                        break;
                    case "month":
                        Month month = parseMonth(reader);
                        builder.month(month);
                        childText = month.getValue();
                        break;
                    case "day":
                        Day day = parseDay(reader);
                        builder.day(day);
                        childText = day.getValue();
                        break;
                    case "volume":
                        Volume volume = parseVolume(reader);
                        builder.volume(volume);
                        childText = volume.getValue();
                        break;
                    case "issue":
                        PmcIssue issue = parsePmcIssue(reader);
                        builder.issue(issue);
                        childText = issue.getValue();
                        break;
                    case "fpage":
                        Fpage fpage = parseFpage(reader);
                        builder.fpage(fpage);
                        childText = fpage.getValue();
                        break;
                    case "lpage":
                        Lpage lpage = parseLpage(reader);
                        builder.lpage(lpage);
                        childText = lpage.getValue();
                        break;
                    case "page-range":
                        PageRange pageRange = parsePageRange(reader);
                        builder.pageRange(pageRange);
                        childText = pageRange.getValue();
                        break;
                    case "publisher-name":
                        PublisherName publisherName = parsePublisherName(reader);
                        builder.publisherName(publisherName);
                        childText = publisherName.getValue();
                        break;
                    case "publisher-loc":
                        PublisherLoc publisherLoc = parsePublisherLoc(reader);
                        builder.publisherLoc(publisherLoc);
                        childText = publisherLoc.getValue();
                        break;
                    case "edition":
                        Edition edition = parseEdition(reader);
                        builder.edition(edition);
                        childText = edition.getValue();
                        break;
                    case "conf-name":
                        ConfName confName = parseConfName(reader);
                        builder.confName(confName);
                        childText = confName.getValue();
                        break;
                    case "pub-id":
                        PubId pubId = parsePubId(reader);
                        pubIds.add(pubId);
                        childText = pubId.getValue();
                        break;
                    case "ext-link":
                        ExtLink extLink = parseExtLink(reader);
                        extLinks.add(extLink);
                        childText = extLink.getValue();
                        break;
                    case "string-name":
                        StringName stringName = ArticleMetaParser.parseStringName(reader);
                        stringNames.add(stringName);
                        childText = stringName.getValue();
                        break;
                    case "etal":
                        Etal etal = parseEtal(reader);
                        builder.etal(etal);
                        childText = etal.getValue();
                        break;
                    case "collab":
                        Collab collab = ArticleMetaParser.parseCollab(reader);
                        collabs.add(collab);
                        childText = collab.getValue();
                        break;
                    case "comment":
                        Comment comment = parseComment(reader);
                        comments.add(comment);
                        childText = comment.getValue();
                        break;
                    case "date-in-citation":
                    case "isbn":
                    case "page-count":
                    case "conf-date":
                    case "conf-loc":
                        childText = collectElementText(reader, localName);
                        break;
                    default:
                        // For unknown elements, collect their text but don't parse structure
                        childText = collectElementText(reader, localName);
                        break;
                }

                if (childText != null) {
                    textContent.append(childText);
                }
            } else if (event == XMLStreamConstants.CHARACTERS) {
                textContent.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "mixed-citation"입니다.
                break;
            }
        }

        builder.value(textContent.toString().trim());
        builder.personGroups(personGroups.isEmpty() ? null : personGroups);
        builder.stringNames(stringNames.isEmpty() ? null : stringNames);
        builder.collabs(collabs.isEmpty() ? null : collabs);
        builder.pubIds(pubIds.isEmpty() ? null : pubIds);
        builder.extLinks(extLinks.isEmpty() ? null : extLinks);
        builder.comments(comments.isEmpty() ? null : comments);

        return builder.build();
    }

    /**
     * 요소의 모든 텍스트 내용을 재귀적으로 수집 / Recursively collect all text content from an element
     *
     * @param reader XMLStreamReader positioned at START_ELEMENT
     * @param elementName Name of the element to collect text from
     * @return All text content including nested elements
     */
    private static String collectElementText(XMLStreamReader reader, String elementName) throws XMLStreamException {
        StringBuilder text = new StringBuilder();
        int depth = 1;

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 depth가 0이 되면 종료되고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (depth > 0) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                depth++;
            } else if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
                text.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                depth--;
            }
        }

        return text.toString();
    }

    /**
     * PersonGroup을 파싱하면서 텍스트도 수집 / Parse PersonGroup while collecting text
     */
    private static String collectTextAndParsePersonGroup(XMLStreamReader reader, List<PersonGroup> personGroups) throws XMLStreamException {
        StringBuilder text = new StringBuilder();
        String personGroupType = reader.getAttributeValue(null, "person-group-type");

        PersonGroup.PersonGroupBuilder builder = PersonGroup.builder()
                .personGroupType(personGroupType);

        List<PersonName> names = new ArrayList<>();

        int depth = 1;
        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 depth가 0이 되면 종료되고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (depth > 0) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                depth++;
                String localName = reader.getLocalName();

                if ("name".equals(localName)) {
                    PersonName name = parsePersonName(reader);
                    names.add(name);
                    // Collect name text
                    if (name.getSurname() != null) text.append(name.getSurname());
                    if (name.getSurname() != null && name.getGivenNames() != null) text.append(" ");
                    if (name.getGivenNames() != null) text.append(name.getGivenNames());
                    depth--; // parsePersonName already consumed the end element
                }
            } else if (event == XMLStreamConstants.CHARACTERS) {
                text.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                depth--;
            }
        }

        builder.names(names.isEmpty() ? null : names);

        personGroups.add(builder.build());
        return text.toString();
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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
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
                // Note: 이 시점에서 END_ELEMENT는 항상 "person-group"입니다.
                break;
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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
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
                // Note: 이 시점에서 END_ELEMENT는 항상 "name"입니다.
                break;
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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,
        // malformed XML에서는 next()가 XMLStreamException을 던집니다.
        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("fn".equals(localName)) {
                    footnotes.add(ArticleMetaParser.parseFn(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                // Note: 이 시점에서 END_ELEMENT는 항상 "fn-group"입니다.
                break;
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
