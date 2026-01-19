package com.brillianttiger.bio.parser.pubmed.parser;

import com.brillianttiger.bio.parser.pubmed.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pubmed.parser.CommonElementParser.*;

/**
 * PubmedDataParser / PubmedData 파서
 *
 * KR: PubmedData 및 관련 요소들을 파싱하는 클래스
 * EN: Class for parsing PubmedData and related elements
 */
public class PubmedDataParser {

    /**
     * PubmedData 파싱 / Parse PubmedData
     * DTD: <!ELEMENT PubmedData (History?, PublicationStatus, ArticleIdList, ObjectList?, ReferenceList*)>
     */
    public static PubmedData parsePubmedData(XMLStreamReader reader) throws XMLStreamException {
        PubmedData.PubmedDataBuilder builder = PubmedData.builder();
        List<ReferenceList> referenceLists = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "History":
                        builder.history(parseHistory(reader));
                        break;
                    case "PublicationStatus":
                        builder.publicationStatus(parsePublicationStatus(reader));
                        break;
                    case "ArticleIdList":
                        builder.articleIdList(parseArticleIdList(reader));
                        break;
                    case "ObjectList":
                        builder.objectList(parseObjectList(reader));
                        break;
                    case "ReferenceList":
                        referenceLists.add(parseReferenceList(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PubmedData")) {
                    break;
                }
            }
        }

        builder.referenceLists(referenceLists.isEmpty() ? null : referenceLists);
        return builder.build();
    }

    /**
     * History 파싱 / Parse History
     * DTD: <!ELEMENT History (PubMedPubDate+)>
     */
    public static History parseHistory(XMLStreamReader reader) throws XMLStreamException {
        List<PubMedPubDate> pubMedPubDates = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("PubMedPubDate".equals(localName)) {
                    pubMedPubDates.add(parsePubMedPubDate(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("History")) {
                    break;
                }
            }
        }

        return History.builder()
                .pubMedPubDates(pubMedPubDates)
                .build();
    }

    /**
     * ArticleIdList 파싱 / Parse ArticleIdList
     * DTD: <!ELEMENT ArticleIdList (ArticleId+)>
     */
    public static ArticleIdList parseArticleIdList(XMLStreamReader reader) throws XMLStreamException {
        List<ArticleId> articleIds = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("ArticleId".equals(localName)) {
                    articleIds.add(parseArticleId(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ArticleIdList")) {
                    break;
                }
            }
        }

        return ArticleIdList.builder()
                .articleIds(articleIds)
                .build();
    }

    /**
     * ArticleId 파싱 / Parse ArticleId
     * DTD: <!ELEMENT ArticleId (#PCDATA)>
     * DTD: <!ATTLIST ArticleId IdType (...) "pubmed">
     */
    public static ArticleId parseArticleId(XMLStreamReader reader) throws XMLStreamException {
        String idTypeStr = reader.getAttributeValue(null, "IdType");
        ArticleIdType idType = idTypeStr != null ? ArticleIdType.fromValue(idTypeStr) : ArticleIdType.PUBMED;

        String value = parseTextContent(reader, "ArticleId");

        return ArticleId.builder()
                .idType(idType)
                .value(value)
                .build();
    }

    /**
     * ObjectList 파싱 / Parse ObjectList
     * DTD: <!ELEMENT ObjectList (Object+)>
     */
    public static ObjectList parseObjectList(XMLStreamReader reader) throws XMLStreamException {
        List<PubmedObject> objects = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Object".equals(localName)) {
                    objects.add(parsePubmedObject(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ObjectList")) {
                    break;
                }
            }
        }

        return ObjectList.builder()
                .objects(objects)
                .build();
    }

    /**
     * PubmedObject 파싱 / Parse PubmedObject (Object 요소)
     * DTD: <!ELEMENT Object (Param*)>
     * DTD: <!ATTLIST Object Type CDATA #REQUIRED>
     */
    public static PubmedObject parsePubmedObject(XMLStreamReader reader) throws XMLStreamException {
        String type = reader.getAttributeValue(null, "Type");

        PubmedObject.PubmedObjectBuilder builder = PubmedObject.builder()
                .type(type);

        List<Param> params = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Param".equals(localName)) {
                    params.add(parseParam(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Object")) {
                    break;
                }
            }
        }

        builder.params(params.isEmpty() ? null : params);
        return builder.build();
    }

    /**
     * Param 파싱 / Parse Param
     * DTD: <!ELEMENT Param (#PCDATA)>
     * DTD: <!ATTLIST Param Name CDATA #REQUIRED>
     */
    public static Param parseParam(XMLStreamReader reader) throws XMLStreamException {
        String name = reader.getAttributeValue(null, "Name");
        String value = parseTextContent(reader, "Param");

        return Param.builder()
                .name(name)
                .value(value)
                .build();
    }

    /**
     * ReferenceList 파싱 / Parse ReferenceList
     * DTD: <!ELEMENT ReferenceList (Title?, Reference*, ReferenceList*)>
     */
    public static ReferenceList parseReferenceList(XMLStreamReader reader) throws XMLStreamException {
        ReferenceList.ReferenceListBuilder builder = ReferenceList.builder();
        List<Reference> references = new ArrayList<>();
        List<ReferenceList> nestedReferenceLists = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Title":
                        ReferenceListTitle refListTitle = parseReferenceListTitle(reader);
                        builder.title(refListTitle != null ? Title.builder().value(refListTitle.getValue()).build() : null);
                        break;
                    case "Reference":
                        references.add(parseReference(reader));
                        break;
                    case "ReferenceList":
                        nestedReferenceLists.add(parseReferenceList(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ReferenceList")) {
                    break;
                }
            }
        }

        builder.references(references.isEmpty() ? null : references);
        builder.referenceLists(nestedReferenceLists.isEmpty() ? null : nestedReferenceLists);

        return builder.build();
    }

    /**
     * Reference 파싱 / Parse Reference
     * DTD: <!ELEMENT Reference (Citation, ArticleIdList?)>
     */
    public static Reference parseReference(XMLStreamReader reader) throws XMLStreamException {
        Reference.ReferenceBuilder builder = Reference.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Citation":
                        builder.citation(parseCitation(reader));
                        break;
                    case "ArticleIdList":
                        builder.articleIdList(parseArticleIdList(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Reference")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * PubmedBookData 파싱 / Parse PubmedBookData
     * DTD: <!ELEMENT PubmedBookData (History?, PublicationStatus, ArticleIdList, ObjectList?)>
     */
    public static PubmedBookData parsePubmedBookData(XMLStreamReader reader) throws XMLStreamException {
        PubmedBookData.PubmedBookDataBuilder builder = PubmedBookData.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "History":
                        builder.history(parseHistory(reader));
                        break;
                    case "PublicationStatus":
                        builder.publicationStatus(parsePublicationStatus(reader));
                        break;
                    case "ArticleIdList":
                        builder.articleIdList(parseArticleIdList(reader));
                        break;
                    case "ObjectList":
                        builder.objectList(parseObjectList(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PubmedBookData")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    // Simple element parsers
    public static PublicationStatus parsePublicationStatus(XMLStreamReader reader) throws XMLStreamException {
        return PublicationStatus.builder().value(parseTextContent(reader, "PublicationStatus")).build();
    }

    public static ReferenceListTitle parseReferenceListTitle(XMLStreamReader reader) throws XMLStreamException {
        return ReferenceListTitle.builder().value(parseTextContent(reader, "Title")).build();
    }

    public static Citation parseCitation(XMLStreamReader reader) throws XMLStreamException {
        return Citation.builder().value(parseTextContent(reader, "Citation")).build();
    }
}
