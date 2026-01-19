package com.brillianttiger.bio.parser.pubmed.parser;

import com.brillianttiger.bio.parser.pubmed.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pubmed.parser.ArticleParser.*;
import static com.brillianttiger.bio.parser.pubmed.parser.CommonElementParser.*;
import static com.brillianttiger.bio.parser.pubmed.parser.MedlineCitationParser.*;
import static com.brillianttiger.bio.parser.pubmed.parser.PubmedDataParser.*;

/**
 * BookArticleParser / PubmedBookArticle 파서
 *
 * KR: PubmedBookArticle 및 관련 요소들을 파싱하는 클래스
 * EN: Class for parsing PubmedBookArticle and related elements
 */
public class BookArticleParser {

    /**
     * PubmedBookArticle 파싱 / Parse PubmedBookArticle
     * DTD: <!ELEMENT PubmedBookArticle (BookDocument, PubmedBookData?)>
     */
    public static PubmedBookArticle parsePubmedBookArticle(XMLStreamReader reader) throws XMLStreamException {
        PubmedBookArticle.PubmedBookArticleBuilder builder = PubmedBookArticle.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "BookDocument":
                        builder.bookDocument(parseBookDocument(reader));
                        break;
                    case "PubmedBookData":
                        builder.pubmedBookData(parsePubmedBookData(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PubmedBookArticle")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * BookDocument 파싱 / Parse BookDocument
     * DTD: <!ELEMENT BookDocument (PMID, ArticleIdList, Book, LocationLabel*, ArticleTitle?, VernacularTitle?,
     *                              Pagination?, Language*, AuthorList*, InvestigatorList?,
     *                              PublicationType*, Abstract?, Sections?, KeywordList*,
     *                              ContributionDate?, DateRevised?, GrantList?, ItemList*, ReferenceList*)>
     */
    public static BookDocument parseBookDocument(XMLStreamReader reader) throws XMLStreamException {
        BookDocument.BookDocumentBuilder builder = BookDocument.builder();

        List<LocationLabel> locationLabels = new ArrayList<>();
        List<Language> languages = new ArrayList<>();
        List<AuthorList> authorLists = new ArrayList<>();
        List<PublicationType> publicationTypes = new ArrayList<>();
        List<KeywordList> keywordLists = new ArrayList<>();
        List<ItemList> itemLists = new ArrayList<>();
        List<ReferenceList> referenceLists = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "PMID":
                        builder.pmid(parsePMID(reader));
                        break;
                    case "ArticleIdList":
                        builder.articleIdList(parseArticleIdList(reader));
                        break;
                    case "Book":
                        builder.book(parseBook(reader));
                        break;
                    case "LocationLabel":
                        locationLabels.add(parseLocationLabel(reader));
                        break;
                    case "ArticleTitle":
                        builder.articleTitle(parseArticleTitle(reader));
                        break;
                    case "VernacularTitle":
                        builder.vernacularTitle(parseVernacularTitle(reader));
                        break;
                    case "Pagination":
                        builder.pagination(parsePagination(reader));
                        break;
                    case "Language":
                        languages.add(parseLanguage(reader));
                        break;
                    case "AuthorList":
                        authorLists.add(parseAuthorList(reader));
                        break;
                    case "InvestigatorList":
                        builder.investigatorList(parseInvestigatorList(reader));
                        break;
                    case "PublicationType":
                        publicationTypes.add(parsePublicationType(reader));
                        break;
                    case "Abstract":
                        builder.abstractInfo(parseAbstract(reader));
                        break;
                    case "Sections":
                        builder.sections(parseSections(reader));
                        break;
                    case "KeywordList":
                        keywordLists.add(parseKeywordList(reader));
                        break;
                    case "ContributionDate":
                        builder.contributionDate(parseContributionDate(reader));
                        break;
                    case "DateRevised":
                        builder.dateRevised(parseDateRevised(reader));
                        break;
                    case "GrantList":
                        builder.grantList(parseGrantList(reader));
                        break;
                    case "ItemList":
                        itemLists.add(parseItemList(reader));
                        break;
                    case "ReferenceList":
                        referenceLists.add(parseReferenceList(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("BookDocument")) {
                    break;
                }
            }
        }

        builder.locationLabels(locationLabels.isEmpty() ? null : locationLabels);
        builder.languages(languages.isEmpty() ? null : languages);
        builder.authorLists(authorLists.isEmpty() ? null : authorLists);
        builder.publicationTypes(publicationTypes.isEmpty() ? null : publicationTypes);
        builder.keywordLists(keywordLists.isEmpty() ? null : keywordLists);
        builder.itemLists(itemLists.isEmpty() ? null : itemLists);
        builder.referenceLists(referenceLists.isEmpty() ? null : referenceLists);

        return builder.build();
    }

    /**
     * Book 파싱 / Parse Book
     * DTD: <!ELEMENT Book (Publisher, BookTitle, PubDate, BeginningDate?, EndingDate?,
     *                     AuthorList*, InvestigatorList?, Volume?, VolumeTitle?, Edition?,
     *                     CollectionTitle?, Isbn*, ELocationID*, Medium?, ReportNumber?)>
     */
    public static Book parseBook(XMLStreamReader reader) throws XMLStreamException {
        Book.BookBuilder builder = Book.builder();

        List<AuthorList> authorLists = new ArrayList<>();
        List<Isbn> isbns = new ArrayList<>();
        List<ELocationID> eLocationIDs = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Publisher":
                        builder.publisher(parsePublisher(reader));
                        break;
                    case "BookTitle":
                        builder.bookTitle(parseBookTitle(reader));
                        break;
                    case "PubDate":
                        builder.pubDate(parsePubDate(reader));
                        break;
                    case "BeginningDate":
                        builder.beginningDate(BeginningDate.builder().value(parseTextContent(reader, "BeginningDate")).build());
                        break;
                    case "EndingDate":
                        builder.endingDate(EndingDate.builder().value(parseTextContent(reader, "EndingDate")).build());
                        break;
                    case "AuthorList":
                        authorLists.add(parseAuthorList(reader));
                        break;
                    case "InvestigatorList":
                        builder.investigatorList(parseInvestigatorList(reader));
                        break;
                    case "Volume":
                        builder.volume(parseVolume(reader));
                        break;
                    case "VolumeTitle":
                        builder.volumeTitle(parseVolumeTitle(reader));
                        break;
                    case "Edition":
                        builder.edition(parseEdition(reader));
                        break;
                    case "CollectionTitle":
                        builder.collectionTitle(parseCollectionTitle(reader));
                        break;
                    case "Isbn":
                        isbns.add(parseIsbn(reader));
                        break;
                    case "ELocationID":
                        eLocationIDs.add(parseELocationID(reader));
                        break;
                    case "Medium":
                        builder.medium(parseMedium(reader));
                        break;
                    case "ReportNumber":
                        builder.reportNumber(parseReportNumber(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Book")) {
                    break;
                }
            }
        }

        builder.authorLists(authorLists.isEmpty() ? null : authorLists);
        builder.isbns(isbns.isEmpty() ? null : isbns);
        builder.eLocationIDs(eLocationIDs.isEmpty() ? null : eLocationIDs);

        return builder.build();
    }

    /**
     * Publisher 파싱 / Parse Publisher
     * DTD: <!ELEMENT Publisher (PublisherName, PublisherLocation?)>
     */
    public static Publisher parsePublisher(XMLStreamReader reader) throws XMLStreamException {
        Publisher.PublisherBuilder builder = Publisher.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "PublisherName":
                        builder.publisherName(parsePublisherName(reader));
                        break;
                    case "PublisherLocation":
                        builder.publisherLocation(parsePublisherLocation(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Publisher")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * Sections 파싱 / Parse Sections
     * DTD: <!ELEMENT Sections (Section+)>
     */
    public static Sections parseSections(XMLStreamReader reader) throws XMLStreamException {
        List<Section> sections = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Section".equals(localName)) {
                    sections.add(parseSection(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Sections")) {
                    break;
                }
            }
        }

        return Sections.builder()
                .sections(sections)
                .build();
    }

    /**
     * Section 파싱 / Parse Section
     * DTD: <!ELEMENT Section (LocationLabel?, SectionTitle?, Section*)>
     */
    public static Section parseSection(XMLStreamReader reader) throws XMLStreamException {
        Section.SectionBuilder builder = Section.builder();
        List<Section> sections = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "LocationLabel":
                        builder.locationLabel(parseLocationLabel(reader));
                        break;
                    case "Title":
                        builder.sectionTitle(parseSectionTitle(reader));
                        break;
                    case "Section":
                        sections.add(parseSection(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Section")) {
                    break;
                }
            }
        }

        builder.sections(sections.isEmpty() ? null : sections);
        return builder.build();
    }

    /**
     * LocationLabel 파싱 / Parse LocationLabel
     * DTD: <!ELEMENT LocationLabel (#PCDATA)>
     * DTD: <!ATTLIST LocationLabel Type (part | chapter | section | appendix | figure | table | box) #IMPLIED>
     */
    public static LocationLabel parseLocationLabel(XMLStreamReader reader) throws XMLStreamException {
        String typeStr = reader.getAttributeValue(null, "Type");
        LocationLabelType type = LocationLabelType.fromValue(typeStr);
        String value = parseTextContent(reader, "LocationLabel");

        return LocationLabel.builder()
                .type(type)
                .value(value)
                .build();
    }

    /**
     * ItemList 파싱 / Parse ItemList
     * DTD: <!ELEMENT ItemList (Item+)>
     * DTD: <!ATTLIST ItemList ListType CDATA #REQUIRED>
     */
    public static ItemList parseItemList(XMLStreamReader reader) throws XMLStreamException {
        String listType = reader.getAttributeValue(null, "ListType");

        ItemList.ItemListBuilder builder = ItemList.builder()
                .listType(listType);

        List<Item> items = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Item".equals(localName)) {
                    items.add(parseItem(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ItemList")) {
                    break;
                }
            }
        }

        builder.items(items);
        return builder.build();
    }

    // Simple element parsers
    public static BookTitle parseBookTitle(XMLStreamReader reader) throws XMLStreamException {
        return BookTitle.builder().value(parseTextContent(reader, "BookTitle")).build();
    }

    public static VolumeTitle parseVolumeTitle(XMLStreamReader reader) throws XMLStreamException {
        return VolumeTitle.builder().value(parseTextContent(reader, "VolumeTitle")).build();
    }

    public static Edition parseEdition(XMLStreamReader reader) throws XMLStreamException {
        return Edition.builder().value(parseTextContent(reader, "Edition")).build();
    }

    public static CollectionTitle parseCollectionTitle(XMLStreamReader reader) throws XMLStreamException {
        return CollectionTitle.builder().value(parseTextContent(reader, "CollectionTitle")).build();
    }

    public static Isbn parseIsbn(XMLStreamReader reader) throws XMLStreamException {
        return Isbn.builder().value(parseTextContent(reader, "Isbn")).build();
    }

    public static Medium parseMedium(XMLStreamReader reader) throws XMLStreamException {
        return Medium.builder().value(parseTextContent(reader, "Medium")).build();
    }

    public static ReportNumber parseReportNumber(XMLStreamReader reader) throws XMLStreamException {
        return ReportNumber.builder().value(parseTextContent(reader, "ReportNumber")).build();
    }

    public static PublisherName parsePublisherName(XMLStreamReader reader) throws XMLStreamException {
        return PublisherName.builder().value(parseTextContent(reader, "PublisherName")).build();
    }

    public static PublisherLocation parsePublisherLocation(XMLStreamReader reader) throws XMLStreamException {
        return PublisherLocation.builder().value(parseTextContent(reader, "PublisherLocation")).build();
    }

    public static SectionTitle parseSectionTitle(XMLStreamReader reader) throws XMLStreamException {
        return SectionTitle.builder().value(parseTextContent(reader, "Title")).build();
    }

    public static Item parseItem(XMLStreamReader reader) throws XMLStreamException {
        return Item.builder().value(parseTextContent(reader, "Item")).build();
    }

    /**
     * ContributionDate 파싱 / Parse ContributionDate
     * DTD: <!ELEMENT ContributionDate (Year, ((Month, Day?) | Season)?)>
     */
    public static ContributionDate parseContributionDate(XMLStreamReader reader) throws XMLStreamException {
        ContributionDate.ContributionDateBuilder builder = ContributionDate.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Year":
                        builder.year(Year.builder().value(parseTextContent(reader, "Year")).build());
                        break;
                    case "Month":
                        builder.month(Month.builder().value(parseTextContent(reader, "Month")).build());
                        break;
                    case "Day":
                        builder.day(Day.builder().value(parseTextContent(reader, "Day")).build());
                        break;
                    case "Season":
                        builder.season(Season.builder().value(parseTextContent(reader, "Season")).build());
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ContributionDate")) {
                    break;
                }
            }
        }

        return builder.build();
    }
}
