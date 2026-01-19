package com.brillianttiger.bio.parser.pubmed.parser;

import com.brillianttiger.bio.parser.pubmed.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pubmed.parser.CommonElementParser.*;

/**
 * ArticleParser / Article 파서
 *
 * KR: Article 및 관련 요소들을 파싱하는 클래스
 * EN: Class for parsing Article and related elements
 */
public class ArticleParser {

    /**
     * Article 파싱 / Parse Article
     * DTD: <!ELEMENT Article (Journal, ArticleTitle, ((Pagination, ELocationID*) | ELocationID+),
     *                         Abstract?, AuthorList?, Language+, DataBankList?, GrantList?,
     *                         PublicationTypeList, VernacularTitle?, ArticleDate*)>
     * DTD: <!ATTLIST Article PubModel (...) #REQUIRED>
     */
    public static Article parseArticle(XMLStreamReader reader) throws XMLStreamException {
        String pubModelStr = reader.getAttributeValue(null, "PubModel");
        PubModel pubModel = pubModelStr != null ? PubModel.fromValue(pubModelStr) : null;

        Article.ArticleBuilder builder = Article.builder()
                .pubModel(pubModel);

        List<ELocationID> eLocationIDs = new ArrayList<>();
        List<Language> languages = new ArrayList<>();
        List<ArticleDate> articleDates = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Journal":
                        builder.journal(parseJournal(reader));
                        break;
                    case "ArticleTitle":
                        builder.articleTitle(parseArticleTitle(reader));
                        break;
                    case "Pagination":
                        builder.pagination(parsePagination(reader));
                        break;
                    case "ELocationID":
                        eLocationIDs.add(parseELocationID(reader));
                        break;
                    case "Abstract":
                        builder.abstractInfo(parseAbstract(reader));
                        break;
                    case "AuthorList":
                        builder.authorList(parseAuthorList(reader));
                        break;
                    case "Language":
                        languages.add(parseLanguage(reader));
                        break;
                    case "DataBankList":
                        builder.dataBankList(parseDataBankList(reader));
                        break;
                    case "GrantList":
                        builder.grantList(parseGrantList(reader));
                        break;
                    case "PublicationTypeList":
                        builder.publicationTypeList(parsePublicationTypeList(reader));
                        break;
                    case "VernacularTitle":
                        builder.vernacularTitle(parseVernacularTitle(reader));
                        break;
                    case "ArticleDate":
                        articleDates.add(parseArticleDate(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Article")) {
                    break;
                }
            }
        }

        builder.eLocationIDs(eLocationIDs.isEmpty() ? null : eLocationIDs);
        builder.languages(languages);
        builder.articleDates(articleDates.isEmpty() ? null : articleDates);

        return builder.build();
    }

    /**
     * Journal 파싱 / Parse Journal
     * DTD: <!ELEMENT Journal (ISSN?, JournalIssue, Title?, ISOAbbreviation?)>
     */
    public static Journal parseJournal(XMLStreamReader reader) throws XMLStreamException {
        Journal.JournalBuilder builder = Journal.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "ISSN":
                        builder.issn(parseISSN(reader));
                        break;
                    case "JournalIssue":
                        builder.journalIssue(parseJournalIssue(reader));
                        break;
                    case "Title":
                        builder.title(parseTitle(reader));
                        break;
                    case "ISOAbbreviation":
                        builder.isoAbbreviation(parseISOAbbreviation(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Journal")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * JournalIssue 파싱 / Parse JournalIssue
     * DTD: <!ELEMENT JournalIssue (Volume?, Issue?, PubDate)>
     * DTD: <!ATTLIST JournalIssue CitedMedium (Internet | Print) #REQUIRED>
     */
    public static JournalIssue parseJournalIssue(XMLStreamReader reader) throws XMLStreamException {
        String citedMediumStr = reader.getAttributeValue(null, "CitedMedium");
        CitedMedium citedMedium = citedMediumStr != null ? CitedMedium.fromValue(citedMediumStr) : null;

        JournalIssue.JournalIssueBuilder builder = JournalIssue.builder()
                .citedMedium(citedMedium);

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Volume":
                        builder.volume(parseVolume(reader));
                        break;
                    case "Issue":
                        builder.issue(parseIssue(reader));
                        break;
                    case "PubDate":
                        builder.pubDate(parsePubDate(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("JournalIssue")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * ArticleTitle 파싱 / Parse ArticleTitle
     */
    public static ArticleTitle parseArticleTitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "ArticleTitle");
        return ArticleTitle.builder()
                .value(value)
                .build();
    }

    /**
     * Pagination 파싱 / Parse Pagination
     * DTD: <!ELEMENT Pagination ((StartPage, EndPage?, MedlinePgn?) | MedlinePgn)>
     */
    public static Pagination parsePagination(XMLStreamReader reader) throws XMLStreamException {
        Pagination.PaginationBuilder builder = Pagination.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "StartPage":
                        builder.startPage(parseStartPage(reader));
                        break;
                    case "EndPage":
                        builder.endPage(parseEndPage(reader));
                        break;
                    case "MedlinePgn":
                        builder.medlinePgn(parseMedlinePgn(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Pagination")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * ELocationID 파싱 / Parse ELocationID
     * DTD: <!ELEMENT ELocationID (#PCDATA)>
     * DTD: <!ATTLIST ELocationID EIdType (doi | pii) #REQUIRED ValidYN (Y | N) "Y">
     */
    public static ELocationID parseELocationID(XMLStreamReader reader) throws XMLStreamException {
        String eIdTypeStr = reader.getAttributeValue(null, "EIdType");
        EIdType eIdType = eIdTypeStr != null ? EIdType.fromValue(eIdTypeStr) : null;

        String validYN = reader.getAttributeValue(null, "ValidYN");
        if (validYN == null) {
            validYN = "Y";
        }

        String value = parseTextContent(reader, "ELocationID");

        return ELocationID.builder()
                .eIdType(eIdType)
                .validYN(validYN)
                .value(value)
                .build();
    }

    /**
     * Abstract 파싱 / Parse Abstract
     * DTD: <!ELEMENT Abstract (AbstractText+, CopyrightInformation?)>
     */
    public static Abstract parseAbstract(XMLStreamReader reader) throws XMLStreamException {
        Abstract.AbstractBuilder builder = Abstract.builder();
        List<AbstractText> abstractTexts = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "AbstractText":
                        abstractTexts.add(parseAbstractText(reader));
                        break;
                    case "CopyrightInformation":
                        builder.copyrightInformation(parseCopyrightInformation(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Abstract")) {
                    break;
                }
            }
        }

        builder.abstractTexts(abstractTexts);
        return builder.build();
    }

    /**
     * AbstractText 파싱 / Parse AbstractText
     * DTD: <!ATTLIST AbstractText Label CDATA #IMPLIED NlmCategory (...) #IMPLIED>
     */
    public static AbstractText parseAbstractText(XMLStreamReader reader) throws XMLStreamException {
        String label = reader.getAttributeValue(null, "Label");
        String nlmCategoryStr = reader.getAttributeValue(null, "NlmCategory");
        NlmCategory nlmCategory = nlmCategoryStr != null ? NlmCategory.fromValue(nlmCategoryStr) : null;

        String value = parseTextContent(reader, "AbstractText");

        return AbstractText.builder()
                .label(label)
                .nlmCategory(nlmCategory)
                .value(value)
                .build();
    }

    /**
     * AuthorList 파싱 / Parse AuthorList
     * DTD: <!ELEMENT AuthorList (Author+)>
     * DTD: <!ATTLIST AuthorList CompleteYN (Y | N) "Y" Type (authors | editors) #IMPLIED>
     */
    public static AuthorList parseAuthorList(XMLStreamReader reader) throws XMLStreamException {
        String completeYN = reader.getAttributeValue(null, "CompleteYN");
        String typeStr = reader.getAttributeValue(null, "Type");
        AuthorListType type = typeStr != null ? AuthorListType.fromValue(typeStr) : null;

        if (completeYN == null) {
            completeYN = "Y";
        }

        AuthorList.AuthorListBuilder builder = AuthorList.builder()
                .completeYN(completeYN)
                .type(type);

        List<Author> authors = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Author".equals(localName)) {
                    authors.add(parseAuthor(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("AuthorList")) {
                    break;
                }
            }
        }

        builder.authors(authors);
        return builder.build();
    }

    /**
     * DataBankList 파싱 / Parse DataBankList
     * DTD: <!ELEMENT DataBankList (DataBank+)>
     * DTD: <!ATTLIST DataBankList CompleteYN (Y | N) "Y">
     */
    public static DataBankList parseDataBankList(XMLStreamReader reader) throws XMLStreamException {
        String completeYN = reader.getAttributeValue(null, "CompleteYN");
        if (completeYN == null) {
            completeYN = "Y";
        }

        DataBankList.DataBankListBuilder builder = DataBankList.builder()
                .completeYN(completeYN);

        List<DataBank> dataBanks = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("DataBank".equals(localName)) {
                    dataBanks.add(parseDataBank(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("DataBankList")) {
                    break;
                }
            }
        }

        builder.dataBanks(dataBanks);
        return builder.build();
    }

    /**
     * DataBank 파싱 / Parse DataBank
     * DTD: <!ELEMENT DataBank (DataBankName, AccessionNumberList?)>
     */
    public static DataBank parseDataBank(XMLStreamReader reader) throws XMLStreamException {
        DataBank.DataBankBuilder builder = DataBank.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "DataBankName":
                        builder.dataBankName(parseDataBankName(reader));
                        break;
                    case "AccessionNumberList":
                        builder.accessionNumberList(parseAccessionNumberList(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("DataBank")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * AccessionNumberList 파싱 / Parse AccessionNumberList
     */
    public static AccessionNumberList parseAccessionNumberList(XMLStreamReader reader) throws XMLStreamException {
        List<AccessionNumber> accessionNumbers = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("AccessionNumber".equals(localName)) {
                    accessionNumbers.add(parseAccessionNumber(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("AccessionNumberList")) {
                    break;
                }
            }
        }

        return AccessionNumberList.builder()
                .accessionNumbers(accessionNumbers)
                .build();
    }

    /**
     * GrantList 파싱 / Parse GrantList
     * DTD: <!ELEMENT GrantList (Grant+)>
     * DTD: <!ATTLIST GrantList CompleteYN (Y | N) "Y">
     */
    public static GrantList parseGrantList(XMLStreamReader reader) throws XMLStreamException {
        String completeYN = reader.getAttributeValue(null, "CompleteYN");
        if (completeYN == null) {
            completeYN = "Y";
        }

        GrantList.GrantListBuilder builder = GrantList.builder()
                .completeYN(completeYN);

        List<Grant> grants = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Grant".equals(localName)) {
                    grants.add(parseGrant(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("GrantList")) {
                    break;
                }
            }
        }

        builder.grants(grants);
        return builder.build();
    }

    /**
     * Grant 파싱 / Parse Grant
     * DTD: <!ELEMENT Grant (GrantID?, Acronym?, Agency, Country)>
     */
    public static Grant parseGrant(XMLStreamReader reader) throws XMLStreamException {
        Grant.GrantBuilder builder = Grant.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "GrantID":
                        builder.grantID(parseGrantID(reader));
                        break;
                    case "Acronym":
                        builder.acronym(parseAcronym(reader));
                        break;
                    case "Agency":
                        builder.agency(parseAgency(reader));
                        break;
                    case "Country":
                        builder.country(parseCountry(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Grant")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * PublicationTypeList 파싱 / Parse PublicationTypeList
     * DTD: <!ELEMENT PublicationTypeList (PublicationType+)>
     */
    public static PublicationTypeList parsePublicationTypeList(XMLStreamReader reader) throws XMLStreamException {
        List<PublicationType> publicationTypes = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("PublicationType".equals(localName)) {
                    publicationTypes.add(parsePublicationType(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PublicationTypeList")) {
                    break;
                }
            }
        }

        return PublicationTypeList.builder()
                .publicationTypes(publicationTypes)
                .build();
    }

    /**
     * PublicationType 파싱 / Parse PublicationType
     * DTD: <!ELEMENT PublicationType (#PCDATA)>
     * DTD: <!ATTLIST PublicationType UI CDATA #REQUIRED>
     */
    public static PublicationType parsePublicationType(XMLStreamReader reader) throws XMLStreamException {
        String ui = reader.getAttributeValue(null, "UI");
        String value = parseTextContent(reader, "PublicationType");

        return PublicationType.builder()
                .ui(ui)
                .value(value)
                .build();
    }

    /**
     * ArticleDate 파싱 / Parse ArticleDate
     * DTD: <!ELEMENT ArticleDate (Year, Month, Day)>
     * DTD: <!ATTLIST ArticleDate DateType CDATA #REQUIRED>
     */
    public static ArticleDate parseArticleDate(XMLStreamReader reader) throws XMLStreamException {
        String dateType = reader.getAttributeValue(null, "DateType");

        ArticleDate.ArticleDateBuilder builder = ArticleDate.builder()
                .dateType(dateType);

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
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ArticleDate")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    // Simple element parsers
    public static Title parseTitle(XMLStreamReader reader) throws XMLStreamException {
        return Title.builder().value(parseTextContent(reader, "Title")).build();
    }

    public static ISOAbbreviation parseISOAbbreviation(XMLStreamReader reader) throws XMLStreamException {
        return ISOAbbreviation.builder().value(parseTextContent(reader, "ISOAbbreviation")).build();
    }

    public static Volume parseVolume(XMLStreamReader reader) throws XMLStreamException {
        return Volume.builder().value(parseTextContent(reader, "Volume")).build();
    }

    public static Issue parseIssue(XMLStreamReader reader) throws XMLStreamException {
        return Issue.builder().value(parseTextContent(reader, "Issue")).build();
    }

    public static StartPage parseStartPage(XMLStreamReader reader) throws XMLStreamException {
        return StartPage.builder().value(parseTextContent(reader, "StartPage")).build();
    }

    public static EndPage parseEndPage(XMLStreamReader reader) throws XMLStreamException {
        return EndPage.builder().value(parseTextContent(reader, "EndPage")).build();
    }

    public static MedlinePgn parseMedlinePgn(XMLStreamReader reader) throws XMLStreamException {
        return MedlinePgn.builder().value(parseTextContent(reader, "MedlinePgn")).build();
    }

    public static CopyrightInformation parseCopyrightInformation(XMLStreamReader reader) throws XMLStreamException {
        return CopyrightInformation.builder().value(parseTextContent(reader, "CopyrightInformation")).build();
    }

    public static VernacularTitle parseVernacularTitle(XMLStreamReader reader) throws XMLStreamException {
        return VernacularTitle.builder().value(parseTextContent(reader, "VernacularTitle")).build();
    }

    public static DataBankName parseDataBankName(XMLStreamReader reader) throws XMLStreamException {
        return DataBankName.builder().value(parseTextContent(reader, "DataBankName")).build();
    }

    public static AccessionNumber parseAccessionNumber(XMLStreamReader reader) throws XMLStreamException {
        return AccessionNumber.builder().value(parseTextContent(reader, "AccessionNumber")).build();
    }

    public static GrantID parseGrantID(XMLStreamReader reader) throws XMLStreamException {
        return GrantID.builder().value(parseTextContent(reader, "GrantID")).build();
    }

    public static Acronym parseAcronym(XMLStreamReader reader) throws XMLStreamException {
        return Acronym.builder().value(parseTextContent(reader, "Acronym")).build();
    }

    public static Agency parseAgency(XMLStreamReader reader) throws XMLStreamException {
        return Agency.builder().value(parseTextContent(reader, "Agency")).build();
    }

    public static Country parseCountry(XMLStreamReader reader) throws XMLStreamException {
        return Country.builder().value(parseTextContent(reader, "Country")).build();
    }
}
