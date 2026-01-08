package com.brillianttiger.bio.parser.pubmed.parser;

import com.brillianttiger.bio.parser.pubmed.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CommonElementParser / 공통 요소 파서
 *
 * KR: PubMed XML의 공통 요소들을 파싱하는 유틸리티 클래스
 * EN: Utility class for parsing common elements in PubMed XML
 */
public class CommonElementParser {

    /**
     * PMID 파싱 / Parse PMID
     * DTD: <!ELEMENT PMID (#PCDATA)>
     * DTD: <!ATTLIST PMID Version CDATA "1">
     */
    public static PMID parsePMID(XMLStreamReader reader) throws XMLStreamException {
        String version = reader.getAttributeValue(null, "Version");
        if (version == null) {
            version = "1";
        }

        String value = parseTextContent(reader, "PMID");

        return PMID.builder()
                .value(value)
                .version(version)
                .build();
    }

    /**
     * Date 파싱 (DateCompleted, DateRevised) / Parse Date
     * DTD: <!ELEMENT DateCompleted (Year, Month, Day)>
     */
    public static Date parseDate(XMLStreamReader reader, String elementName) throws XMLStreamException {
        Date.DateBuilder builder = Date.builder();

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
                if (reader.getLocalName().equals(elementName)) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * PubDate 파싱 / Parse PubDate
     * DTD: <!ELEMENT PubDate ((Year, ((Month, Day?) | Season)?) | MedlineDate)>
     */
    public static PubDate parsePubDate(XMLStreamReader reader) throws XMLStreamException {
        PubDate.PubDateBuilder builder = PubDate.builder();

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
                    case "MedlineDate":
                        builder.medlineDate(MedlineDate.builder().value(parseTextContent(reader, "MedlineDate")).build());
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PubDate")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * ISSN 파싱 / Parse ISSN
     * DTD: <!ELEMENT ISSN (#PCDATA)>
     * DTD: <!ATTLIST ISSN IssnType (Electronic | Print) #REQUIRED>
     */
    public static ISSN parseISSN(XMLStreamReader reader) throws XMLStreamException {
        String issnType = reader.getAttributeValue(null, "IssnType");
        String value = parseTextContent(reader, "ISSN");

        return ISSN.builder()
                .issnType(issnType)
                .value(value)
                .build();
    }

    /**
     * Language 파싱 / Parse Language
     */
    public static Language parseLanguage(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "Language");
        return Language.builder()
                .value(value)
                .build();
    }

    /**
     * Author 파싱 / Parse Author
     * DTD: <!ELEMENT Author (
     *          ((LastName, ForeName?, Initials?, Suffix?) | CollectiveName),
     *          Identifier*,
     *          AffiliationInfo*)>
     * DTD: <!ATTLIST Author ValidYN (Y | N) "Y" EqualContrib (Y | N) #IMPLIED>
     */
    public static Author parseAuthor(XMLStreamReader reader) throws XMLStreamException {
        String validYN = reader.getAttributeValue(null, "ValidYN");
        String equalContrib = reader.getAttributeValue(null, "EqualContrib");

        if (validYN == null) {
            validYN = "Y";
        }

        Author.AuthorBuilder builder = Author.builder()
                .validYN(validYN)
                .equalContrib(equalContrib);

        List<Identifier> identifiers = new ArrayList<>();
        List<AffiliationInfo> affiliationInfos = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "LastName":
                        builder.lastName(LastName.builder().value(parseTextContent(reader, "LastName")).build());
                        break;
                    case "ForeName":
                        builder.foreName(ForeName.builder().value(parseTextContent(reader, "ForeName")).build());
                        break;
                    case "Initials":
                        builder.initials(Initials.builder().value(parseTextContent(reader, "Initials")).build());
                        break;
                    case "Suffix":
                        builder.suffix(com.brillianttiger.bio.parser.pubmed.model.Suffix.builder().value(parseTextContent(reader, "Suffix")).build());
                        break;
                    case "CollectiveName":
                        builder.collectiveName(CollectiveName.builder().value(parseTextContent(reader, "CollectiveName")).build());
                        break;
                    case "Identifier":
                        identifiers.add(parseIdentifier(reader));
                        break;
                    case "AffiliationInfo":
                        affiliationInfos.add(parseAffiliationInfo(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Author")) {
                    break;
                }
            }
        }

        builder.identifiers(identifiers.isEmpty() ? null : identifiers);
        builder.affiliationInfos(affiliationInfos.isEmpty() ? null : affiliationInfos);

        return builder.build();
    }

    /**
     * Investigator 파싱 / Parse Investigator
     * DTD: <!ELEMENT Investigator (LastName, ForeName?, Initials?, Suffix?, Identifier*, AffiliationInfo*)>
     * DTD: <!ATTLIST Investigator ValidYN (Y | N) "Y">
     */
    public static Investigator parseInvestigator(XMLStreamReader reader) throws XMLStreamException {
        String validYN = reader.getAttributeValue(null, "ValidYN");
        if (validYN == null) {
            validYN = "Y";
        }

        Investigator.InvestigatorBuilder builder = Investigator.builder()
                .validYN(validYN);

        List<Identifier> identifiers = new ArrayList<>();
        List<AffiliationInfo> affiliationInfos = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "LastName":
                        builder.lastName(LastName.builder().value(parseTextContent(reader, "LastName")).build());
                        break;
                    case "ForeName":
                        builder.foreName(ForeName.builder().value(parseTextContent(reader, "ForeName")).build());
                        break;
                    case "Initials":
                        builder.initials(Initials.builder().value(parseTextContent(reader, "Initials")).build());
                        break;
                    case "Suffix":
                        builder.suffix(com.brillianttiger.bio.parser.pubmed.model.Suffix.builder().value(parseTextContent(reader, "Suffix")).build());
                        break;
                    case "Identifier":
                        identifiers.add(parseIdentifier(reader));
                        break;
                    case "AffiliationInfo":
                        affiliationInfos.add(parseAffiliationInfo(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Investigator")) {
                    break;
                }
            }
        }

        builder.identifiers(identifiers.isEmpty() ? null : identifiers);
        builder.affiliationInfos(affiliationInfos.isEmpty() ? null : affiliationInfos);

        return builder.build();
    }

    /**
     * Identifier 파싱 / Parse Identifier
     * DTD: <!ELEMENT Identifier (#PCDATA)>
     * DTD: <!ATTLIST Identifier Source CDATA #REQUIRED>
     */
    public static Identifier parseIdentifier(XMLStreamReader reader) throws XMLStreamException {
        String source = reader.getAttributeValue(null, "Source");
        String value = parseTextContent(reader, "Identifier");

        return Identifier.builder()
                .source(source)
                .value(value)
                .build();
    }

    /**
     * AffiliationInfo 파싱 / Parse AffiliationInfo
     * DTD: <!ELEMENT AffiliationInfo (Affiliation, Identifier*)>
     */
    public static AffiliationInfo parseAffiliationInfo(XMLStreamReader reader) throws XMLStreamException {
        AffiliationInfo.AffiliationInfoBuilder builder = AffiliationInfo.builder();
        List<Identifier> identifiers = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Affiliation":
                        builder.affiliation(parseAffiliation(reader));
                        break;
                    case "Identifier":
                        identifiers.add(parseIdentifier(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("AffiliationInfo")) {
                    break;
                }
            }
        }

        builder.identifiers(identifiers.isEmpty() ? null : identifiers);
        return builder.build();
    }

    /**
     * Affiliation 파싱 / Parse Affiliation
     */
    public static Affiliation parseAffiliation(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "Affiliation");
        return Affiliation.builder()
                .value(value)
                .build();
    }

    /**
     * PersonalNameSubject 파싱 / Parse PersonalNameSubject
     * DTD: <!ELEMENT PersonalNameSubject (LastName, ForeName?, Initials?, Suffix?)>
     */
    public static PersonalNameSubject parsePersonalNameSubject(XMLStreamReader reader) throws XMLStreamException {
        PersonalNameSubject.PersonalNameSubjectBuilder builder = PersonalNameSubject.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "LastName":
                        builder.lastName(LastName.builder().value(parseTextContent(reader, "LastName")).build());
                        break;
                    case "ForeName":
                        builder.foreName(ForeName.builder().value(parseTextContent(reader, "ForeName")).build());
                        break;
                    case "Initials":
                        builder.initials(Initials.builder().value(parseTextContent(reader, "Initials")).build());
                        break;
                    case "Suffix":
                        builder.suffix(com.brillianttiger.bio.parser.pubmed.model.Suffix.builder().value(parseTextContent(reader, "Suffix")).build());
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PersonalNameSubject")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * PubMedPubDate 파싱 / Parse PubMedPubDate
     * DTD: <!ELEMENT PubMedPubDate (Year, Month, Day, (Hour, (Minute, Second?)?)?)>
     * DTD: <!ATTLIST PubMedPubDate PubStatus (...) #REQUIRED>
     */
    public static PubMedPubDate parsePubMedPubDate(XMLStreamReader reader) throws XMLStreamException {
        String pubStatus = reader.getAttributeValue(null, "PubStatus");

        PubMedPubDate.PubMedPubDateBuilder builder = PubMedPubDate.builder()
                .pubStatus(pubStatus);

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
                    case "Hour":
                        builder.hour(Hour.builder().value(parseTextContent(reader, "Hour")).build());
                        break;
                    case "Minute":
                        builder.minute(Minute.builder().value(parseTextContent(reader, "Minute")).build());
                        break;
                    case "Second":
                        builder.second(Second.builder().value(parseTextContent(reader, "Second")).build());
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PubMedPubDate")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * 텍스트 컨텐츠 파싱 / Parse text content
     */
    public static String parseTextContent(XMLStreamReader reader, String elementName) throws XMLStreamException {
        StringBuilder content = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
                content.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals(elementName)) {
                    break;
                }
            }
        }

        String result = content.toString().trim();
        return result.isEmpty() ? null : result;
    }

    /**
     * 요소 건너뛰기 / Skip element
     */
    public static void skipElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 1;
        while (depth > 0 && reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                depth++;
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                depth--;
            }
        }
    }
}
