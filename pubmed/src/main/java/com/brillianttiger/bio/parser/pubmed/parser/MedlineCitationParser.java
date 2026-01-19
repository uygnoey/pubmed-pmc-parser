package com.brillianttiger.bio.parser.pubmed.parser;

import com.brillianttiger.bio.parser.pubmed.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pubmed.parser.ArticleParser.*;
import static com.brillianttiger.bio.parser.pubmed.parser.CommonElementParser.*;

/**
 * MedlineCitationParser / MedlineCitation 파서
 *
 * KR: MedlineCitation 및 관련 요소들을 파싱하는 클래스
 * EN: Class for parsing MedlineCitation and related elements
 */
public class MedlineCitationParser {

    /**
     * MedlineCitation 파싱 / Parse MedlineCitation
     * DTD: <!ELEMENT MedlineCitation (PMID, DateCompleted?, DateRevised?, Article, MedlineJournalInfo,
     *                                 ChemicalList?, SupplMeshList?, CitationSubset*,
     *                                 CommentsCorrectionsList?, GeneSymbolList?, MeshHeadingList?,
     *                                 NumberOfReferences?, PersonalNameSubjectList?, OtherID*,
     *                                 OtherAbstract*, KeywordList*, CoiStatement?, SpaceFlightMission*,
     *                                 InvestigatorList?, GeneralNote*)>
     * DTD: <!ATTLIST MedlineCitation Status (...) #REQUIRED Owner (...) "NLM"
     *                                IndexingMethod (...) #IMPLIED VersionID CDATA #IMPLIED
     *                                VersionDate CDATA #IMPLIED>
     */
    public static MedlineCitation parseMedlineCitation(XMLStreamReader reader) throws XMLStreamException {
        // Parse attributes
        String status = reader.getAttributeValue(null, "Status");
        String owner = reader.getAttributeValue(null, "Owner");
        String indexingMethod = reader.getAttributeValue(null, "IndexingMethod");
        String versionID = reader.getAttributeValue(null, "VersionID");
        String versionDate = reader.getAttributeValue(null, "VersionDate");

        // Convert String attributes to enums
        Status statusEnum = status != null ? Status.fromValue(status) : null;
        Owner ownerEnum = owner != null ? Owner.fromValue(owner) : Owner.NLM;
        IndexingMethod indexingMethodEnum = indexingMethod != null ? IndexingMethod.fromValue(indexingMethod) : null;

        MedlineCitation.MedlineCitationBuilder builder = MedlineCitation.builder()
                .status(statusEnum)
                .owner(ownerEnum)
                .indexingMethod(indexingMethodEnum)
                .versionID(versionID)
                .versionDate(versionDate);

        List<CitationSubset> citationSubsets = new ArrayList<>();
        List<OtherID> otherIDs = new ArrayList<>();
        List<OtherAbstract> otherAbstracts = new ArrayList<>();
        List<KeywordList> keywordLists = new ArrayList<>();
        List<SpaceFlightMission> spaceFlightMissions = new ArrayList<>();
        List<InvestigatorList> investigatorLists = new ArrayList<>();
        List<GeneralNote> generalNotes = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "PMID":
                        builder.pmid(parsePMID(reader));
                        break;
                    case "DateCompleted":
                        builder.dateCompleted(parseDateCompleted(reader));
                        break;
                    case "DateRevised":
                        builder.dateRevised(parseDateRevised(reader));
                        break;
                    case "Article":
                        builder.article(parseArticle(reader));
                        break;
                    case "MedlineJournalInfo":
                        builder.medlineJournalInfo(parseMedlineJournalInfo(reader));
                        break;
                    case "ChemicalList":
                        builder.chemicalList(parseChemicalList(reader));
                        break;
                    case "SupplMeshList":
                        builder.supplMeshList(parseSupplMeshList(reader));
                        break;
                    case "CitationSubset":
                        citationSubsets.add(parseCitationSubset(reader));
                        break;
                    case "CommentsCorrectionsList":
                        builder.commentsCorrectionsList(parseCommentsCorrectionsList(reader));
                        break;
                    case "GeneSymbolList":
                        builder.geneSymbolList(parseGeneSymbolList(reader));
                        break;
                    case "MeshHeadingList":
                        builder.meshHeadingList(parseMeshHeadingList(reader));
                        break;
                    case "NumberOfReferences":
                        builder.numberOfReferences(parseNumberOfReferences(reader));
                        break;
                    case "PersonalNameSubjectList":
                        builder.personalNameSubjectList(parsePersonalNameSubjectList(reader));
                        break;
                    case "OtherID":
                        otherIDs.add(parseOtherID(reader));
                        break;
                    case "OtherAbstract":
                        otherAbstracts.add(parseOtherAbstract(reader));
                        break;
                    case "KeywordList":
                        keywordLists.add(parseKeywordList(reader));
                        break;
                    case "CoiStatement":
                        builder.coiStatement(parseCoiStatement(reader));
                        break;
                    case "SpaceFlightMission":
                        spaceFlightMissions.add(parseSpaceFlightMission(reader));
                        break;
                    case "InvestigatorList":
                        investigatorLists.add(parseInvestigatorList(reader));
                        break;
                    case "GeneralNote":
                        generalNotes.add(parseGeneralNote(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("MedlineCitation")) {
                    break;
                }
            }
        }

        builder.citationSubsets(citationSubsets.isEmpty() ? null : citationSubsets);
        builder.otherIDs(otherIDs.isEmpty() ? null : otherIDs);
        builder.otherAbstracts(otherAbstracts.isEmpty() ? null : otherAbstracts);
        builder.keywordLists(keywordLists.isEmpty() ? null : keywordLists);
        builder.spaceFlightMissions(spaceFlightMissions.isEmpty() ? null : spaceFlightMissions);
        builder.investigatorLists(investigatorLists.isEmpty() ? null : investigatorLists);
        builder.generalNotes(generalNotes.isEmpty() ? null : generalNotes);

        return builder.build();
    }

    /**
     * MedlineJournalInfo 파싱 / Parse MedlineJournalInfo
     * DTD: <!ELEMENT MedlineJournalInfo (Country?, MedlineTA, NlmUniqueID?, ISSNLinking?)>
     */
    public static MedlineJournalInfo parseMedlineJournalInfo(XMLStreamReader reader) throws XMLStreamException {
        MedlineJournalInfo.MedlineJournalInfoBuilder builder = MedlineJournalInfo.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "Country":
                        builder.country(parseCountry(reader));
                        break;
                    case "MedlineTA":
                        builder.medlineTA(parseMedlineTA(reader));
                        break;
                    case "NlmUniqueID":
                        builder.nlmUniqueID(parseNlmUniqueID(reader));
                        break;
                    case "ISSNLinking":
                        builder.issnLinking(parseISSNLinking(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("MedlineJournalInfo")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * ChemicalList 파싱 / Parse ChemicalList
     * DTD: <!ELEMENT ChemicalList (Chemical+)>
     */
    public static ChemicalList parseChemicalList(XMLStreamReader reader) throws XMLStreamException {
        List<Chemical> chemicals = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Chemical".equals(localName)) {
                    chemicals.add(parseChemical(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("ChemicalList")) {
                    break;
                }
            }
        }

        return ChemicalList.builder()
                .chemicals(chemicals)
                .build();
    }

    /**
     * Chemical 파싱 / Parse Chemical
     * DTD: <!ELEMENT Chemical (RegistryNumber, NameOfSubstance)>
     */
    public static Chemical parseChemical(XMLStreamReader reader) throws XMLStreamException {
        Chemical.ChemicalBuilder builder = Chemical.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "RegistryNumber":
                        builder.registryNumber(parseRegistryNumber(reader));
                        break;
                    case "NameOfSubstance":
                        builder.nameOfSubstance(parseNameOfSubstance(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("Chemical")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * NameOfSubstance 파싱 / Parse NameOfSubstance
     * DTD: <!ELEMENT NameOfSubstance (#PCDATA)>
     * DTD: <!ATTLIST NameOfSubstance UI CDATA #REQUIRED>
     */
    public static NameOfSubstance parseNameOfSubstance(XMLStreamReader reader) throws XMLStreamException {
        String ui = reader.getAttributeValue(null, "UI");
        String value = parseTextContent(reader, "NameOfSubstance");

        return NameOfSubstance.builder()
                .ui(ui)
                .value(value)
                .build();
    }

    /**
     * SupplMeshList 파싱 / Parse SupplMeshList
     * DTD: <!ELEMENT SupplMeshList (SupplMeshName+)>
     */
    public static SupplMeshList parseSupplMeshList(XMLStreamReader reader) throws XMLStreamException {
        List<SupplMeshName> supplMeshNames = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("SupplMeshName".equals(localName)) {
                    supplMeshNames.add(parseSupplMeshName(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("SupplMeshList")) {
                    break;
                }
            }
        }

        return SupplMeshList.builder()
                .supplMeshNames(supplMeshNames)
                .build();
    }

    /**
     * SupplMeshName 파싱 / Parse SupplMeshName
     * DTD: <!ELEMENT SupplMeshName (#PCDATA)>
     * DTD: <!ATTLIST SupplMeshName Type (Disease | Protocol | Organism) #REQUIRED UI CDATA #REQUIRED>
     */
    public static SupplMeshName parseSupplMeshName(XMLStreamReader reader) throws XMLStreamException {
        String typeStr = reader.getAttributeValue(null, "Type");
        SupplMeshNameType type = typeStr != null ? SupplMeshNameType.fromValue(typeStr) : null;
        String ui = reader.getAttributeValue(null, "UI");
        String value = parseTextContent(reader, "SupplMeshName");

        return SupplMeshName.builder()
                .type(type)
                .ui(ui)
                .value(value)
                .build();
    }

    /**
     * CommentsCorrectionsList 파싱 / Parse CommentsCorrectionsList
     * DTD: <!ELEMENT CommentsCorrectionsList (CommentsCorrections+)>
     */
    public static CommentsCorrectionsList parseCommentsCorrectionsList(XMLStreamReader reader) throws XMLStreamException {
        List<CommentsCorrections> commentsCorrections = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("CommentsCorrections".equals(localName)) {
                    commentsCorrections.add(parseCommentsCorrections(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("CommentsCorrectionsList")) {
                    break;
                }
            }
        }

        return CommentsCorrectionsList.builder()
                .commentsCorrections(commentsCorrections)
                .build();
    }

    /**
     * CommentsCorrections 파싱 / Parse CommentsCorrections
     * DTD: <!ELEMENT CommentsCorrections (RefSource, PMID?, Note?)>
     * DTD: <!ATTLIST CommentsCorrections RefType (...) #REQUIRED>
     */
    public static CommentsCorrections parseCommentsCorrections(XMLStreamReader reader) throws XMLStreamException {
        String refTypeStr = reader.getAttributeValue(null, "RefType");
        RefType refType = RefType.fromValue(refTypeStr);

        CommentsCorrections.CommentsCorrectionsBuilder builder = CommentsCorrections.builder()
                .refType(refType);

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "RefSource":
                        builder.refSource(parseRefSource(reader));
                        break;
                    case "PMID":
                        builder.pmid(parsePMID(reader));
                        break;
                    case "Note":
                        builder.note(parseNote(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("CommentsCorrections")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * GeneSymbolList 파싱 / Parse GeneSymbolList
     * DTD: <!ELEMENT GeneSymbolList (GeneSymbol+)>
     */
    public static GeneSymbolList parseGeneSymbolList(XMLStreamReader reader) throws XMLStreamException {
        List<GeneSymbol> geneSymbols = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("GeneSymbol".equals(localName)) {
                    geneSymbols.add(parseGeneSymbol(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("GeneSymbolList")) {
                    break;
                }
            }
        }

        return GeneSymbolList.builder()
                .geneSymbols(geneSymbols)
                .build();
    }

    /**
     * MeshHeadingList 파싱 / Parse MeshHeadingList
     * DTD: <!ELEMENT MeshHeadingList (MeshHeading+)>
     */
    public static MeshHeadingList parseMeshHeadingList(XMLStreamReader reader) throws XMLStreamException {
        List<MeshHeading> meshHeadings = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("MeshHeading".equals(localName)) {
                    meshHeadings.add(parseMeshHeading(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("MeshHeadingList")) {
                    break;
                }
            }
        }

        return MeshHeadingList.builder()
                .meshHeadings(meshHeadings)
                .build();
    }

    /**
     * MeshHeading 파싱 / Parse MeshHeading
     * DTD: <!ELEMENT MeshHeading (DescriptorName, QualifierName*)>
     */
    public static MeshHeading parseMeshHeading(XMLStreamReader reader) throws XMLStreamException {
        MeshHeading.MeshHeadingBuilder builder = MeshHeading.builder();
        List<QualifierName> qualifierNames = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "DescriptorName":
                        builder.descriptorName(parseDescriptorName(reader));
                        break;
                    case "QualifierName":
                        qualifierNames.add(parseQualifierName(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("MeshHeading")) {
                    break;
                }
            }
        }

        builder.qualifierNames(qualifierNames.isEmpty() ? null : qualifierNames);
        return builder.build();
    }

    /**
     * DescriptorName 파싱 / Parse DescriptorName
     * DTD: <!ELEMENT DescriptorName (#PCDATA)>
     * DTD: <!ATTLIST DescriptorName UI CDATA #REQUIRED MajorTopicYN (Y | N) "N" AutoHM (Y) #IMPLIED Type (Geographic) #IMPLIED>
     */
    public static DescriptorName parseDescriptorName(XMLStreamReader reader) throws XMLStreamException {
        String ui = reader.getAttributeValue(null, "UI");
        String majorTopicYN = reader.getAttributeValue(null, "MajorTopicYN");
        String autoHM = reader.getAttributeValue(null, "AutoHM");
        String typeStr = reader.getAttributeValue(null, "Type");
        DescriptorNameType type = typeStr != null ? DescriptorNameType.fromValue(typeStr) : null;

        if (majorTopicYN == null) {
            majorTopicYN = "N";
        }

        String value = parseTextContent(reader, "DescriptorName");

        return DescriptorName.builder()
                .ui(ui)
                .majorTopicYN(majorTopicYN)
                .autoHM(autoHM)
                .type(type)
                .value(value)
                .build();
    }

    /**
     * QualifierName 파싱 / Parse QualifierName
     * DTD: <!ELEMENT QualifierName (#PCDATA)>
     * DTD: <!ATTLIST QualifierName UI CDATA #REQUIRED AutoHM (Y) #IMPLIED MajorTopicYN (Y | N) "N">
     */
    public static QualifierName parseQualifierName(XMLStreamReader reader) throws XMLStreamException {
        String ui = reader.getAttributeValue(null, "UI");
        String autoHM = reader.getAttributeValue(null, "AutoHM");
        String majorTopicYN = reader.getAttributeValue(null, "MajorTopicYN");

        if (majorTopicYN == null) {
            majorTopicYN = "N";
        }

        String value = parseTextContent(reader, "QualifierName");

        return QualifierName.builder()
                .ui(ui)
                .autoHM(autoHM)
                .majorTopicYN(majorTopicYN)
                .value(value)
                .build();
    }

    /**
     * PersonalNameSubjectList 파싱 / Parse PersonalNameSubjectList
     * DTD: <!ELEMENT PersonalNameSubjectList (PersonalNameSubject+)>
     */
    public static PersonalNameSubjectList parsePersonalNameSubjectList(XMLStreamReader reader) throws XMLStreamException {
        List<PersonalNameSubject> personalNameSubjects = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("PersonalNameSubject".equals(localName)) {
                    personalNameSubjects.add(parsePersonalNameSubject(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("PersonalNameSubjectList")) {
                    break;
                }
            }
        }

        return PersonalNameSubjectList.builder()
                .personalNameSubjects(personalNameSubjects)
                .build();
    }

    /**
     * OtherID 파싱 / Parse OtherID
     * DTD: <!ELEMENT OtherID (#PCDATA)>
     * DTD: <!ATTLIST OtherID Source (NASA | KIE | PIP | POP | ARPL | CPC | IND | CPFH | CLML | NRCBL | NLM | QCIM) #REQUIRED>
     */
    public static OtherID parseOtherID(XMLStreamReader reader) throws XMLStreamException {
        String sourceStr = reader.getAttributeValue(null, "Source");
        OtherIDSource source = OtherIDSource.fromValue(sourceStr);
        String value = parseTextContent(reader, "OtherID");

        return OtherID.builder()
                .source(source)
                .value(value)
                .build();
    }

    /**
     * OtherAbstract 파싱 / Parse OtherAbstract
     * DTD: <!ELEMENT OtherAbstract (AbstractText+, CopyrightInformation?)>
     * DTD: <!ATTLIST OtherAbstract Type (AAMC | AIDS | KIE | PIP | NASA | Publisher) #REQUIRED Language CDATA "eng">
     */
    public static OtherAbstract parseOtherAbstract(XMLStreamReader reader) throws XMLStreamException {
        String typeStr = reader.getAttributeValue(null, "Type");
        OtherAbstractType type = typeStr != null ? OtherAbstractType.fromValue(typeStr) : null;

        String language = reader.getAttributeValue(null, "Language");
        if (language == null) {
            language = "eng";
        }

        OtherAbstract.OtherAbstractBuilder builder = OtherAbstract.builder()
                .type(type)
                .language(language);

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
                if (reader.getLocalName().equals("OtherAbstract")) {
                    break;
                }
            }
        }

        builder.abstractTexts(abstractTexts);
        return builder.build();
    }

    /**
     * KeywordList 파싱 / Parse KeywordList
     * DTD: <!ELEMENT KeywordList (Keyword+)>
     * DTD: <!ATTLIST KeywordList Owner (NLM | NLM-AUTO | NASA | PIP | KIE | NOTNLM | HHS) "NLM">
     */
    public static KeywordList parseKeywordList(XMLStreamReader reader) throws XMLStreamException {
        String ownerStr = reader.getAttributeValue(null, "Owner");
        KeywordOwner owner = ownerStr != null ? KeywordOwner.fromValue(ownerStr) : KeywordOwner.NLM;

        KeywordList.KeywordListBuilder builder = KeywordList.builder()
                .owner(owner);

        List<Keyword> keywords = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Keyword".equals(localName)) {
                    keywords.add(parseKeyword(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("KeywordList")) {
                    break;
                }
            }
        }

        builder.keywords(keywords);
        return builder.build();
    }

    /**
     * Keyword 파싱 / Parse Keyword
     * DTD: <!ELEMENT Keyword (#PCDATA)>
     * DTD: <!ATTLIST Keyword MajorTopicYN (Y | N) "N">
     */
    public static Keyword parseKeyword(XMLStreamReader reader) throws XMLStreamException {
        String majorTopicYN = reader.getAttributeValue(null, "MajorTopicYN");
        if (majorTopicYN == null) {
            majorTopicYN = "N";
        }

        String value = parseTextContent(reader, "Keyword");

        return Keyword.builder()
                .majorTopicYN(majorTopicYN)
                .value(value)
                .build();
    }

    /**
     * InvestigatorList 파싱 / Parse InvestigatorList
     * DTD: <!ELEMENT InvestigatorList (Investigator+)>
     * DTD: <!ATTLIST InvestigatorList ID ID #IMPLIED>
     *
     * 2024 DTD: InvestigatorList now repeatable with optional ID attribute
     */
    public static InvestigatorList parseInvestigatorList(XMLStreamReader reader) throws XMLStreamException {
        // 2024 DTD: ID attribute (optional)
        String id = reader.getAttributeValue(null, "ID");

        List<Investigator> investigators = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("Investigator".equals(localName)) {
                    investigators.add(parseInvestigator(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("InvestigatorList")) {
                    break;
                }
            }
        }

        return InvestigatorList.builder()
                .id(id)  // 2024 DTD: ID attribute
                .investigators(investigators)
                .build();
    }

    /**
     * GeneralNote 파싱 / Parse GeneralNote
     * DTD: <!ELEMENT GeneralNote (#PCDATA)>
     * DTD: <!ATTLIST GeneralNote Owner (NLM | NASA | PIP | KIE | HSR | HMD) "NLM">
     */
    public static GeneralNote parseGeneralNote(XMLStreamReader reader) throws XMLStreamException {
        String ownerStr = reader.getAttributeValue(null, "Owner");
        GeneralNoteOwner owner = ownerStr != null ? GeneralNoteOwner.fromValue(ownerStr) : GeneralNoteOwner.NLM;

        String value = parseTextContent(reader, "GeneralNote");

        return GeneralNote.builder()
                .owner(owner)
                .value(value)
                .build();
    }

    // Simple element parsers
    public static CitationSubset parseCitationSubset(XMLStreamReader reader) throws XMLStreamException {
        return CitationSubset.builder().value(parseTextContent(reader, "CitationSubset")).build();
    }

    public static NumberOfReferences parseNumberOfReferences(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "NumberOfReferences");
        return NumberOfReferences.builder().value(value != null && !value.isEmpty() ? Integer.parseInt(value) : null).build();
    }

    public static CoiStatement parseCoiStatement(XMLStreamReader reader) throws XMLStreamException {
        return CoiStatement.builder().value(parseTextContent(reader, "CoiStatement")).build();
    }

    public static SpaceFlightMission parseSpaceFlightMission(XMLStreamReader reader) throws XMLStreamException {
        return SpaceFlightMission.builder().value(parseTextContent(reader, "SpaceFlightMission")).build();
    }

    public static MedlineTA parseMedlineTA(XMLStreamReader reader) throws XMLStreamException {
        return MedlineTA.builder().value(parseTextContent(reader, "MedlineTA")).build();
    }

    public static NlmUniqueID parseNlmUniqueID(XMLStreamReader reader) throws XMLStreamException {
        return NlmUniqueID.builder().value(parseTextContent(reader, "NlmUniqueID")).build();
    }

    public static ISSNLinking parseISSNLinking(XMLStreamReader reader) throws XMLStreamException {
        return ISSNLinking.builder().value(parseTextContent(reader, "ISSNLinking")).build();
    }

    public static RegistryNumber parseRegistryNumber(XMLStreamReader reader) throws XMLStreamException {
        return RegistryNumber.builder().value(parseTextContent(reader, "RegistryNumber")).build();
    }

    public static RefSource parseRefSource(XMLStreamReader reader) throws XMLStreamException {
        return RefSource.builder().value(parseTextContent(reader, "RefSource")).build();
    }

    public static Note parseNote(XMLStreamReader reader) throws XMLStreamException {
        return Note.builder().value(parseTextContent(reader, "Note")).build();
    }

    public static GeneSymbol parseGeneSymbol(XMLStreamReader reader) throws XMLStreamException {
        return GeneSymbol.builder().value(parseTextContent(reader, "GeneSymbol")).build();
    }

    /**
     * DateCompleted 파싱 / Parse DateCompleted
     * DTD: <!ELEMENT DateCompleted (Year, Month, Day)>
     */
    public static DateCompleted parseDateCompleted(XMLStreamReader reader) throws XMLStreamException {
        Date date = parseDate(reader, "DateCompleted");
        if (date == null) return null;
        return DateCompleted.builder()
                .year(date.getYear())
                .month(date.getMonth())
                .day(date.getDay())
                .build();
    }

    /**
     * DateRevised 파싱 / Parse DateRevised
     * DTD: <!ELEMENT DateRevised (Year, Month, Day)>
     */
    public static DateRevised parseDateRevised(XMLStreamReader reader) throws XMLStreamException {
        Date date = parseDate(reader, "DateRevised");
        if (date == null) return null;
        return DateRevised.builder()
                .year(date.getYear())
                .month(date.getMonth())
                .day(date.getDay())
                .build();
    }
}
