package io.brillianttiger.bio.parser.pubmed.parser;

import io.brillianttiger.bio.parser.pubmed.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static io.brillianttiger.bio.parser.pubmed.parser.CommonElementParser.*;

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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,


        // malformed XML에서는 next()가 XMLStreamException을 던집니다.


        while (true) {
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

                // Note: 이 시점에서 END_ELEMENT는 항상 "PubmedData"입니다.

                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.

                break;

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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,


        // malformed XML에서는 next()가 XMLStreamException을 던집니다.


        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("PubMedPubDate".equals(localName)) {
                    pubMedPubDates.add(parsePubMedPubDate(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {

                // Note: 이 시점에서 END_ELEMENT는 항상 "History"입니다.

                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.

                break;

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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,


        // malformed XML에서는 next()가 XMLStreamException을 던집니다.


        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("ArticleId".equals(localName)) {
                    articleIds.add(parseArticleId(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {

                // Note: 이 시점에서 END_ELEMENT는 항상 "ArticleIdList"입니다.

                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.

                break;

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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,


        // malformed XML에서는 next()가 XMLStreamException을 던집니다.


        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Object".equals(localName)) {
                    objects.add(parsePubmedObject(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {

                // Note: 이 시점에서 END_ELEMENT는 항상 "ObjectList"입니다.

                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.

                break;

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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,


        // malformed XML에서는 next()가 XMLStreamException을 던집니다.


        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Param".equals(localName)) {
                    params.add(parseParam(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {

                // Note: 이 시점에서 END_ELEMENT는 항상 "Object"입니다.

                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.

                break;

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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,


        // malformed XML에서는 next()가 XMLStreamException을 던집니다.


        while (true) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Title":
                        // parseReferenceListTitle always returns non-null object (value can be null)
                        builder.title(Title.builder().value(parseReferenceListTitle(reader).getValue()).build());
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

                // Note: 이 시점에서 END_ELEMENT는 항상 "ReferenceList"입니다.

                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.

                break;

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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,


        // malformed XML에서는 next()가 XMLStreamException을 던집니다.


        while (true) {
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

                // Note: 이 시점에서 END_ELEMENT는 항상 "Reference"입니다.

                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.

                break;

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

        // Note: hasNext() 체크는 불필요합니다. 정상 XML에서는 항상 END_ELEMENT를 만나고,


        // malformed XML에서는 next()가 XMLStreamException을 던집니다.


        while (true) {
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

                // Note: 이 시점에서 END_ELEMENT는 항상 "PubmedBookData"입니다.

                // 자식 요소들은 각각의 파서가 완전히 소비하므로 부모 파서는 자식의 END_ELEMENT를 만나지 않습니다.

                break;

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
