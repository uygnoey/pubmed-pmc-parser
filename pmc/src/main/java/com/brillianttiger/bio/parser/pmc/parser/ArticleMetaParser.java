package com.brillianttiger.bio.parser.pmc.parser;

import com.brillianttiger.bio.parser.pmc.model.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.brillianttiger.bio.parser.pmc.parser.CommonPmcElementParser.*;
import static com.brillianttiger.bio.parser.pmc.parser.BodyParser.*;

/**
 * ArticleMetaParser / ArticleMeta 파서
 *
 * KR: PMC XML의 ArticleMeta 및 관련 요소들을 파싱하는 클래스
 * EN: Class for parsing ArticleMeta and related elements in PMC XML
 */
public class ArticleMetaParser {

    /**
     * ArticleMeta 파싱 / Parse ArticleMeta
     * DTD: <!ELEMENT article-meta (article-id*, article-categories?, title-group, contrib-group*, aff*, ...)>
     */
    public static ArticleMeta parseArticleMeta(XMLStreamReader reader) throws XMLStreamException {
        ArticleMeta.ArticleMetaBuilder builder = ArticleMeta.builder();

        List<PmcArticleId> articleIds = new ArrayList<>();
        List<ContribGroup> contribGroups = new ArrayList<>();
        List<Aff> affiliations = new ArrayList<>();
        List<PmcPubDate> pubDates = new ArrayList<>();
        List<PmcIsbn> isbns = new ArrayList<>();
        List<Email> emails = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        List<Uri> uris = new ArrayList<>();
        List<SupplementaryMaterial> supplementaryMaterials = new ArrayList<>();
        List<SelfUri> selfUris = new ArrayList<>();
        List<RelatedArticle> relatedArticles = new ArrayList<>();
        List<RelatedObject> relatedObjects = new ArrayList<>();
        List<PmcAbstract> abstracts = new ArrayList<>();
        List<TransAbstract> transAbstracts = new ArrayList<>();
        List<KwdGroup> kwdGroups = new ArrayList<>();
        List<FundingGroup> fundingGroups = new ArrayList<>();
        List<SupportGroup> supportGroups = new ArrayList<>();
        List<Conference> conferences = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "article-id":
                        articleIds.add(parsePmcArticleId(reader, localName));
                        break;
                    case "article-categories":
                        builder.articleCategories(parseArticleCategories(reader));
                        break;
                    case "title-group":
                        builder.titleGroup(parseTitleGroup(reader));
                        break;
                    case "contrib-group":
                        contribGroups.add(parseContribGroup(reader));
                        break;
                    case "aff":
                        affiliations.add(parseAff(reader));
                        break;
                    case "author-notes":
                        builder.authorNotes(parseAuthorNotes(reader));
                        break;
                    case "pub-date":
                        pubDates.add(parsePmcPubDate(reader));
                        break;
                    case "volume":
                        builder.volume(parseVolume(reader));
                        break;
                    case "volume-id":
                        builder.volumeId(parseVolumeId(reader));
                        break;
                    case "volume-series":
                        builder.volumeSeries(parseVolumeSeries(reader));
                        break;
                    case "issue":
                        builder.issue(parsePmcIssue(reader));
                        break;
                    case "issue-id":
                        builder.issueId(parseIssueId(reader));
                        break;
                    case "issue-title":
                        builder.issueTitle(parseIssueTitle(reader));
                        break;
                    case "issue-sponsor":
                        builder.issueSponsor(parseIssueSponsor(reader));
                        break;
                    case "issue-part":
                        builder.issuePart(parseIssuePart(reader));
                        break;
                    case "isbn":
                        isbns.add(parsePmcIsbn(reader));
                        break;
                    case "supplement":
                        builder.supplement(parseSupplement(reader));
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
                    case "email":
                        emails.add(parseEmail(reader));
                        break;
                    case "ext-link":
                        extLinks.add(parseExtLink(reader));
                        break;
                    case "uri":
                        uris.add(parseUri(reader));
                        break;
                    case "supplementary-material":
                        supplementaryMaterials.add(parseSupplementaryMaterial(reader));
                        break;
                    case "history":
                        builder.history(parsePmcHistory(reader));
                        break;
                    case "pub-history":
                        builder.pubHistory(parsePubHistory(reader));
                        break;
                    case "permissions":
                        builder.permissions(parsePermissions(reader));
                        break;
                    case "self-uri":
                        selfUris.add(parseSelfUri(reader));
                        break;
                    case "related-article":
                        relatedArticles.add(parseRelatedArticle(reader));
                        break;
                    case "related-object":
                        relatedObjects.add(parseRelatedObject(reader));
                        break;
                    case "abstract":
                        abstracts.add(parsePmcAbstract(reader));
                        break;
                    case "trans-abstract":
                        transAbstracts.add(parseTransAbstract(reader));
                        break;
                    case "kwd-group":
                        kwdGroups.add(parseKwdGroup(reader));
                        break;
                    case "funding-group":
                        fundingGroups.add(parseFundingGroup(reader));
                        break;
                    case "support-group":
                        supportGroups.add(parseSupportGroup(reader));
                        break;
                    case "conference":
                        conferences.add(parseConference(reader));
                        break;
                    case "counts":
                        builder.counts(parseCounts(reader));
                        break;
                    case "custom-meta-group":
                        builder.customMetaGroup(parseCustomMetaGroup(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("article-meta")) {
                    break;
                }
            }
        }

        builder.articleIds(articleIds.isEmpty() ? null : articleIds);
        builder.contribGroups(contribGroups.isEmpty() ? null : contribGroups);
        builder.affiliations(affiliations.isEmpty() ? null : affiliations);
        builder.pubDates(pubDates.isEmpty() ? null : pubDates);
        builder.isbns(isbns.isEmpty() ? null : isbns);
        builder.emails(emails.isEmpty() ? null : emails);
        builder.extLinks(extLinks.isEmpty() ? null : extLinks);
        builder.uris(uris.isEmpty() ? null : uris);
        builder.supplementaryMaterials(supplementaryMaterials.isEmpty() ? null : supplementaryMaterials);
        builder.selfUris(selfUris.isEmpty() ? null : selfUris);
        builder.relatedArticles(relatedArticles.isEmpty() ? null : relatedArticles);
        builder.relatedObjects(relatedObjects.isEmpty() ? null : relatedObjects);
        builder.abstracts(abstracts.isEmpty() ? null : abstracts);
        builder.transAbstracts(transAbstracts.isEmpty() ? null : transAbstracts);
        builder.kwdGroups(kwdGroups.isEmpty() ? null : kwdGroups);
        builder.fundingGroups(fundingGroups.isEmpty() ? null : fundingGroups);
        builder.supportGroups(supportGroups.isEmpty() ? null : supportGroups);
        builder.conferences(conferences.isEmpty() ? null : conferences);

        return builder.build();
    }

    /**
     * TitleGroup 파싱 / Parse TitleGroup
     */
    public static TitleGroup parseTitleGroup(XMLStreamReader reader) throws XMLStreamException {
        TitleGroup.TitleGroupBuilder builder = TitleGroup.builder();

        List<Subtitle> subtitles = new ArrayList<>();
        List<TransTitleGroup> transTitleGroups = new ArrayList<>();
        List<AltTitle> altTitles = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "article-title":
                        builder.articleTitle(parseArticleTitle(reader));
                        break;
                    case "subtitle":
                        subtitles.add(parseSubtitle(reader));
                        break;
                    case "trans-title-group":
                        transTitleGroups.add(parseTransTitleGroup(reader));
                        break;
                    case "alt-title":
                        altTitles.add(parseAltTitle(reader));
                        break;
                    case "fn-group":
                        builder.fnGroup(parseFnGroup(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("title-group")) {
                    break;
                }
            }
        }

        builder.subtitles(subtitles.isEmpty() ? null : subtitles);
        builder.transTitleGroups(transTitleGroups.isEmpty() ? null : transTitleGroups);
        builder.altTitles(altTitles.isEmpty() ? null : altTitles);

        return builder.build();
    }

    /**
     * ContribGroup 파싱 / Parse ContribGroup
     * DTD: <!ELEMENT contrib-group (contrib+, xref*, aff*)>
     * DTD: <!ATTLIST contrib-group content-type CDATA #IMPLIED>
     */
    public static ContribGroup parseContribGroup(XMLStreamReader reader) throws XMLStreamException {
        String contentType = reader.getAttributeValue(null, "content-type");

        ContribGroup.ContribGroupBuilder builder = ContribGroup.builder()
                .contentType(contentType);

        List<Contrib> contributors = new ArrayList<>();
        List<Xref> xrefs = new ArrayList<>();
        List<Aff> affiliations = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "contrib":
                        contributors.add(parseContrib(reader));
                        break;
                    case "xref":
                        xrefs.add(parseXref(reader));
                        break;
                    case "aff":
                        affiliations.add(parseAff(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("contrib-group")) {
                    break;
                }
            }
        }

        builder.contributors(contributors.isEmpty() ? null : contributors);
        builder.xrefs(xrefs.isEmpty() ? null : xrefs);
        builder.affiliations(affiliations.isEmpty() ? null : affiliations);

        return builder.build();
    }

    /**
     * Contrib 파싱 / Parse Contrib
     * DTD: <!ATTLIST contrib contrib-type CDATA #IMPLIED corresp (yes | no) #IMPLIED ...>
     */
    public static Contrib parseContrib(XMLStreamReader reader) throws XMLStreamException {
        String contribType = reader.getAttributeValue(null, "contrib-type");
        String corresp = reader.getAttributeValue(null, "corresp");
        String deceased = reader.getAttributeValue(null, "deceased");
        String equalContrib = reader.getAttributeValue(null, "equal-contrib");
        String id = reader.getAttributeValue(null, "id");
        String rid = reader.getAttributeValue(null, "rid");

        Contrib.ContribBuilder builder = Contrib.builder()
                .contribType(contribType)
                .corresp(corresp)
                .deceased(deceased)
                .equalContrib(equalContrib)
                .id(id)
                .rid(rid);

        List<ContribId> contribIds = new ArrayList<>();
        List<Degrees> degrees = new ArrayList<>();
        List<Address> addresses = new ArrayList<>();
        List<Aff> affiliations = new ArrayList<>();
        List<Email> emails = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        List<Fn> footnotes = new ArrayList<>();
        List<Role> roles = new ArrayList<>();
        List<Uri> uris = new ArrayList<>();
        List<Xref> xrefs = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "contrib-id":
                        contribIds.add(parseContribId(reader));
                        break;
                    case "name":
                        builder.name(parseName(reader));
                        break;
                    case "string-name":
                        builder.stringName(parseStringName(reader));
                        break;
                    case "collab":
                        builder.collab(parseCollab(reader));
                        break;
                    case "anonymous":
                        builder.anonymous(parseAnonymous(reader));
                        break;
                    case "degrees":
                        degrees.add(parseDegrees(reader));
                        break;
                    case "address":
                        addresses.add(parseAddress(reader));
                        break;
                    case "aff":
                        affiliations.add(parseAff(reader));
                        break;
                    case "author-comment":
                        builder.authorComment(parseAuthorComment(reader));
                        break;
                    case "bio":
                        builder.bio(parseBio(reader));
                        break;
                    case "email":
                        emails.add(parseEmail(reader));
                        break;
                    case "etal":
                        builder.etal(parseEtal(reader));
                        break;
                    case "ext-link":
                        extLinks.add(parseExtLink(reader));
                        break;
                    case "fn":
                        footnotes.add(parseFn(reader));
                        break;
                    case "on-behalf-of":
                        builder.onBehalfOf(parseOnBehalfOf(reader));
                        break;
                    case "role":
                        roles.add(parseRole(reader));
                        break;
                    case "uri":
                        uris.add(parseUri(reader));
                        break;
                    case "xref":
                        xrefs.add(parseXref(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("contrib")) {
                    break;
                }
            }
        }

        builder.contribIds(contribIds.isEmpty() ? null : contribIds);
        builder.degrees(degrees.isEmpty() ? null : degrees);
        builder.addresses(addresses.isEmpty() ? null : addresses);
        builder.affiliations(affiliations.isEmpty() ? null : affiliations);
        builder.emails(emails.isEmpty() ? null : emails);
        builder.extLinks(extLinks.isEmpty() ? null : extLinks);
        builder.footnotes(footnotes.isEmpty() ? null : footnotes);
        builder.roles(roles.isEmpty() ? null : roles);
        builder.uris(uris.isEmpty() ? null : uris);
        builder.xrefs(xrefs.isEmpty() ? null : xrefs);

        return builder.build();
    }

    /**
     * Aff 파싱 / Parse Aff
     * DTD: <!ATTLIST aff id ID #IMPLIED>
     */
    /**
     * Aff (소속) 파싱 / Parse Aff (Affiliation)
     *
     * KR: 소속 정보 파싱. 텍스트 또는 구조화된 institution-wrap, addr-line 등을 포함.
     * EN: Parse affiliation info. Contains text or structured institution-wrap, addr-line, etc.
     *
     * DTD: <!ELEMENT aff (#PCDATA | %address-elements; | %aff-elements;)*>
     */
    public static Aff parseAff(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String rid = reader.getAttributeValue(null, "rid");
        String contentType = reader.getAttributeValue(null, "content-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        List<InstitutionWrap> institutionWraps = new ArrayList<>();
        List<AddrLine> addrLines = new ArrayList<>();
        StringBuilder textContent = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "institution-wrap":
                        institutionWraps.add(parseInstitutionWrap(reader));
                        break;
                    case "addr-line":
                        addrLines.add(parseAddrLine(reader));
                        break;
                    default:
                        // 기타 요소는 스킵하되 텍스트는 수집
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.CHARACTERS) {
                String text = reader.getText().trim();
                if (!text.isEmpty()) {
                    if (textContent.length() > 0) {
                        textContent.append(" ");
                    }
                    textContent.append(text);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("aff")) {
                    break;
                }
            }
        }

        return Aff.builder()
                .id(id)
                .rid(rid)
                .contentType(contentType)
                .specificUse(specificUse)
                .institutionWraps(institutionWraps.isEmpty() ? null : institutionWraps)
                .addrLines(addrLines.isEmpty() ? null : addrLines)
                .value(textContent.length() > 0 ? textContent.toString() : null)
                .build();
    }

    /**
     * AddrLine 파싱 / Parse AddrLine
     *
     * KR: 주소 라인 파싱 (하나의 물리적/논리적 주소 라인).
     * EN: Parse address line (one physical/logical line of address).
     */
    private static AddrLine parseAddrLine(XMLStreamReader reader) throws XMLStreamException {
        String content = parseTextContent(reader, "addr-line");
        return AddrLine.builder().value(content).build();
    }

    /**
     * InstitutionWrap 파싱 / Parse InstitutionWrap
     *
     * KR: 기관 래퍼 파싱 (재귀 구조 지원).
     * EN: Parse institution wrap (supports recursive structure).
     */
    private static InstitutionWrap parseInstitutionWrap(XMLStreamReader reader) throws XMLStreamException {
        List<InstitutionId> institutionIds = new ArrayList<>();
        List<Institution> institutions = new ArrayList<>();
        List<InstitutionWrap> childWraps = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "institution-id":
                        institutionIds.add(parseInstitutionId(reader));
                        break;
                    case "institution":
                        institutions.add(parseInstitution(reader));
                        break;
                    case "institution-wrap":
                        childWraps.add(parseInstitutionWrap(reader)); // Recursive
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("institution-wrap")) {
                    break;
                }
            }
        }

        return InstitutionWrap.builder()
                .institutionIds(institutionIds.isEmpty() ? null : institutionIds)
                .institutions(institutions.isEmpty() ? null : institutions)
                .institutionWraps(childWraps.isEmpty() ? null : childWraps)
                .build();
    }

    /**
     * Institution 파싱 / Parse Institution
     *
     * KR: 기관명 파싱.
     * EN: Parse institution name.
     */
    private static Institution parseInstitution(XMLStreamReader reader) throws XMLStreamException {
        String contentType = reader.getAttributeValue(null, "content-type");
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xlinkHref = reader.getAttributeValue(null, "xlink:href");
        String value = parseTextContent(reader, "institution");

        return Institution.builder()
                .contentType(contentType)
                .id(id)
                .specificUse(specificUse)
                .xlinkHref(xlinkHref)
                .content(value)
                .build();
    }

    /**
     * InstitutionId 파싱 / Parse InstitutionId
     *
     * KR: 기관 ID 파싱 (ROR, ISNI 등).
     * EN: Parse institution ID (ROR, ISNI, etc.).
     */
    private static InstitutionId parseInstitutionId(XMLStreamReader reader) throws XMLStreamException {
        String contentType = reader.getAttributeValue(null, "content-type");
        String institutionIdType = reader.getAttributeValue(null, "institution-id-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String value = parseTextContent(reader, "institution-id");

        return InstitutionId.builder()
                .contentType(contentType)
                .institutionIdType(InstitutionIdType.fromValue(institutionIdType))
                .specificUse(specificUse)
                .value(value)
                .build();
    }

    /**
     * PmcPubDate 파싱 / Parse PmcPubDate
     * DTD: <!ATTLIST pub-date pub-type CDATA #IMPLIED publication-format CDATA #IMPLIED ...>
     */
    public static PmcPubDate parsePmcPubDate(XMLStreamReader reader) throws XMLStreamException {
        String pubType = reader.getAttributeValue(null, "pub-type");
        String publicationFormat = reader.getAttributeValue(null, "publication-format");
        String dateType = reader.getAttributeValue(null, "date-type");
        String iso8601Date = reader.getAttributeValue(null, "iso-8601-date");

        PmcPubDate.PmcPubDateBuilder builder = PmcPubDate.builder()
                .pubType(pubType)
                .publicationFormat(publicationFormat)
                .dateType(dateType)
                .iso8601Date(iso8601Date);

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "day":
                        builder.day(parseDay(reader));
                        break;
                    case "month":
                        builder.month(parseMonth(reader));
                        break;
                    case "season":
                        builder.season(parseSeason(reader));
                        break;
                    case "year":
                        builder.year(parseYear(reader));
                        break;
                    case "era":
                        builder.era(parseEra(reader));
                        break;
                    case "string-date":
                        builder.stringDate(parseStringDate(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("pub-date")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * KwdGroup 파싱 / Parse KwdGroup
     */
    public static KwdGroup parseKwdGroup(XMLStreamReader reader) throws XMLStreamException {
        String assigningAuthority = reader.getAttributeValue(null, "assigning-authority");
        String kwdGroupType = reader.getAttributeValue(null, "kwd-group-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String vocab = reader.getAttributeValue(null, "vocab");
        String vocabIdentifier = reader.getAttributeValue(null, "vocab-identifier");
        String xmlLang = reader.getAttributeValue(null, "xml:lang");

        KwdGroup.KwdGroupBuilder builder = KwdGroup.builder()
                .assigningAuthority(assigningAuthority)
                .kwdGroupType(kwdGroupType)
                .specificUse(specificUse)
                .vocab(vocab)
                .vocabIdentifier(vocabIdentifier)
                .xmlLang(xmlLang);

        List<Label> labels = new ArrayList<>();
        List<Title> titles = new ArrayList<>();
        List<Kwd> keywords = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        labels.add(parseLabel(reader));
                        break;
                    case "title":
                        titles.add(parseTitle(reader));
                        break;
                    case "kwd":
                        keywords.add(parseKwd(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("kwd-group")) {
                    break;
                }
            }
        }

        builder.labels(labels.isEmpty() ? null : labels);
        builder.titles(titles.isEmpty() ? null : titles);
        builder.keywords(keywords.isEmpty() ? null : keywords);
        return builder.build();
    }

    /**
     * PmcAbstract 파싱 / Parse PmcAbstract
     */
    public static PmcAbstract parsePmcAbstract(XMLStreamReader reader) throws XMLStreamException {
        String abstractType = reader.getAttributeValue(null, "abstract-type");

        PmcAbstract.PmcAbstractBuilder builder = PmcAbstract.builder()
                .abstractType(abstractType);

        List<P> paragraphs = new ArrayList<>();
        List<Sec> sections = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "title":
                        builder.title(parseTitle(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "sec":
                        sections.add(BodyParser.parseSec(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("abstract")) {
                    break;
                }
            }
        }

        builder.paragraphs(paragraphs.isEmpty() ? null : paragraphs);
        builder.sections(sections.isEmpty() ? null : sections);

        return builder.build();
    }

    /**
     * SupplementaryMaterial 파싱 / Parse SupplementaryMaterial
     * DTD: <!ELEMENT supplementary-material (
     *          (object-id)*,
     *          label?,
     *          (caption)*,
     *          abstract*,
     *          kwd-group*,
     *          alt-text*,
     *          long-desc*,
     *          (email | ext-link | uri)*,
     *          (alternatives | disp-formula | disp-formula-group |
     *           chem-struct-wrap | disp-quote | speech | statement |
     *           verse-group | table-wrap | p | def-list | list |
     *           array | code | graphic | media | preformat)*,
     *          (attrib | permissions)*
     *      )>
     */
    public static SupplementaryMaterial parseSupplementaryMaterial(XMLStreamReader reader) throws XMLStreamException {
        // Parse attributes
        String contentType = reader.getAttributeValue(null, "content-type");
        String id = reader.getAttributeValue(null, "id");
        String mimeSubtype = reader.getAttributeValue(null, "mime-subtype");
        String mimetype = reader.getAttributeValue(null, "mimetype");
        String orientation = reader.getAttributeValue(null, "orientation");
        String position = reader.getAttributeValue(null, "position");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        // Parse xlink attributes
        String xlinkActuate = reader.getAttributeValue("http://www.w3.org/1999/xlink", "actuate");
        String xlinkHref = reader.getAttributeValue("http://www.w3.org/1999/xlink", "href");
        String xlinkRole = reader.getAttributeValue("http://www.w3.org/1999/xlink", "role");
        String xlinkShow = reader.getAttributeValue("http://www.w3.org/1999/xlink", "show");
        String xlinkTitle = reader.getAttributeValue("http://www.w3.org/1999/xlink", "title");
        String xlinkType = reader.getAttributeValue("http://www.w3.org/1999/xlink", "type");

        // Parse xml attributes
        String xmlBase = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "base");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        SupplementaryMaterial.SupplementaryMaterialBuilder builder = SupplementaryMaterial.builder()
                .contentType(contentType)
                .id(id)
                .mimeSubtype(mimeSubtype)
                .mimetype(mimetype)
                .orientation(Orientation.fromValue(orientation))
                .position(Position.fromValue(position))
                .specificUse(specificUse)
                .xlinkActuate(XlinkActuate.fromValue(xlinkActuate))
                .xlinkHref(xlinkHref)
                .xlinkRole(xlinkRole)
                .xlinkShow(XlinkShow.fromValue(xlinkShow))
                .xlinkTitle(xlinkTitle)
                .xlinkType(xlinkType)
                .xmlBase(xmlBase)
                .xmlLang(xmlLang);

        // Parse child elements
        List<ObjectId> objectIds = new ArrayList<>();
        List<Caption> captions = new ArrayList<>();
        List<PmcAbstract> abstracts = new ArrayList<>();
        List<KwdGroup> kwdGroups = new ArrayList<>();
        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();
        List<Email> emails = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        List<Uri> uris = new ArrayList<>();
        List<Alternatives> alternatives = new ArrayList<>();
        List<DispFormula> dispFormulas = new ArrayList<>();
        List<DispFormulaGroup> dispFormulaGroups = new ArrayList<>();
        List<ChemStructWrap> chemStructWraps = new ArrayList<>();
        List<DispQuote> dispQuotes = new ArrayList<>();
        List<Speech> speeches = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();
        List<VerseGroup> verseGroups = new ArrayList<>();
        List<TableWrap> tableWraps = new ArrayList<>();
        List<P> paragraphs = new ArrayList<>();
        List<DefList> defLists = new ArrayList<>();
        List<PmcList> lists = new ArrayList<>();
        List<Array> arrays = new ArrayList<>();
        List<Code> codes = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();
        List<Media> medias = new ArrayList<>();
        List<Preformat> preformats = new ArrayList<>();
        List<Attrib> attribs = new ArrayList<>();
        List<Permissions> permissionsList = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "object-id":
                        objectIds.add(parseObjectId(reader));
                        break;
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "caption":
                        captions.add(parseCaption(reader));
                        break;
                    case "abstract":
                        abstracts.add(parsePmcAbstract(reader));
                        break;
                    case "kwd-group":
                        kwdGroups.add(parseKwdGroup(reader));
                        break;
                    case "alt-text":
                        altTexts.add(PmcXmlParser.parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(PmcXmlParser.parseLongDesc(reader));
                        break;
                    case "email":
                        emails.add(parseEmail(reader));
                        break;
                    case "ext-link":
                        extLinks.add(parseExtLink(reader));
                        break;
                    case "uri":
                        uris.add(parseUri(reader));
                        break;
                    case "alternatives":
                        alternatives.add(parseAlternatives(reader));
                        break;
                    case "disp-formula":
                        dispFormulas.add(parseDispFormula(reader));
                        break;
                    case "disp-formula-group":
                        dispFormulaGroups.add(parseDispFormulaGroup(reader));
                        break;
                    case "chem-struct-wrap":
                        chemStructWraps.add(parseChemStructWrap(reader));
                        break;
                    case "disp-quote":
                        dispQuotes.add(parseDispQuote(reader));
                        break;
                    case "speech":
                        speeches.add(parseSpeech(reader));
                        break;
                    case "statement":
                        statements.add(parseStatement(reader));
                        break;
                    case "verse-group":
                        verseGroups.add(parseVerseGroup(reader));
                        break;
                    case "table-wrap":
                        tableWraps.add(PmcXmlParser.parseTableWrap(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    case "def-list":
                        defLists.add(parseDefList(reader));
                        break;
                    case "list":
                        lists.add(parseList(reader));
                        break;
                    case "array":
                        arrays.add(parseArray(reader));
                        break;
                    case "code":
                        codes.add(parseCode(reader));
                        break;
                    case "graphic":
                        graphics.add(PmcXmlParser.parseGraphic(reader));
                        break;
                    case "media":
                        medias.add(PmcXmlParser.parseMedia(reader));
                        break;
                    case "preformat":
                        preformats.add(parsePreformat(reader));
                        break;
                    case "attrib":
                        attribs.add(parseAttrib(reader));
                        break;
                    case "permissions":
                        permissionsList.add(parsePermissions(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("supplementary-material")) {
                    break;
                }
            }
        }

        return builder
                .objectIds(objectIds.isEmpty() ? null : objectIds)
                .captions(captions.isEmpty() ? null : captions)
                .abstracts(abstracts.isEmpty() ? null : abstracts)
                .kwdGroups(kwdGroups.isEmpty() ? null : kwdGroups)
                .altTexts(altTexts.isEmpty() ? null : altTexts)
                .longDescs(longDescs.isEmpty() ? null : longDescs)
                .emails(emails.isEmpty() ? null : emails)
                .extLinks(extLinks.isEmpty() ? null : extLinks)
                .uris(uris.isEmpty() ? null : uris)
                .alternatives(alternatives.isEmpty() ? null : alternatives)
                .dispFormulas(dispFormulas.isEmpty() ? null : dispFormulas)
                .dispFormulaGroups(dispFormulaGroups.isEmpty() ? null : dispFormulaGroups)
                .chemStructWraps(chemStructWraps.isEmpty() ? null : chemStructWraps)
                .speeches(speeches.isEmpty() ? null : speeches)
                .statements(statements.isEmpty() ? null : statements)
                .verseGroups(verseGroups.isEmpty() ? null : verseGroups)
                .tableWraps(tableWraps.isEmpty() ? null : tableWraps)
                .arrays(arrays.isEmpty() ? null : arrays)
                .paragraphs(paragraphs.isEmpty() ? null : paragraphs)
                .defLists(defLists.isEmpty() ? null : defLists)
                .lists(lists.isEmpty() ? null : lists)
                .codes(codes.isEmpty() ? null : codes)
                .graphics(graphics.isEmpty() ? null : graphics)
                .medias(medias.isEmpty() ? null : medias)
                .preformats(preformats.isEmpty() ? null : preformats)
                .dispQuotes(dispQuotes.isEmpty() ? null : dispQuotes)
                .attribs(attribs.isEmpty() ? null : attribs)
                .permissions(permissionsList.isEmpty() ? null : permissionsList)
                .build();
    }

    // Simple element parsers
    public static ArticleCategories parseArticleCategories(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "article-categories");
        return ArticleCategories.builder().value(value).build();
    }

    /**
     * ArticleTitle 파싱 / Parse ArticleTitle
     * DTD: <!ELEMENT article-title (#PCDATA | %all-phrase;)*>
     * DTD: <!ATTLIST article-title xml:lang NMTOKEN #IMPLIED>
     */
    public static ArticleTitle parseArticleTitle(XMLStreamReader reader) throws XMLStreamException {
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");
        String content = parseTextContent(reader, "article-title");
        return ArticleTitle.builder()
                .xmlLang(xmlLang)
                .content(content)
                .build();
    }

    /**
     * PmcArticleTitle 파싱 / Parse PmcArticleTitle (legacy)
     */
    public static PmcArticleTitle parsePmcArticleTitle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "article-title");
        return PmcArticleTitle.builder().value(value).build();
    }

    /**
     * Subtitle 파싱 / Parse Subtitle
     * DTD: <!ELEMENT subtitle (#PCDATA | %all-phrase;)*>
     */
    public static Subtitle parseSubtitle(XMLStreamReader reader) throws XMLStreamException {
        String content = parseTextContent(reader, "subtitle");
        return Subtitle.builder().content(content).build();
    }

    /**
     * TransTitleGroup 파싱 / Parse TransTitleGroup
     * DTD: <!ELEMENT trans-title-group (trans-title, trans-subtitle*)>
     * DTD: <!ATTLIST trans-title-group xml:lang NMTOKEN #IMPLIED>
     */
    public static TransTitleGroup parseTransTitleGroup(XMLStreamReader reader) throws XMLStreamException {
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        TransTitleGroup.TransTitleGroupBuilder builder = TransTitleGroup.builder()
                .xmlLang(xmlLang);

        List<TransSubtitle> transSubtitles = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "trans-title":
                        builder.transTitle(parseTransTitle(reader));
                        break;
                    case "trans-subtitle":
                        transSubtitles.add(parseTransSubtitle(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("trans-title-group")) {
                    break;
                }
            }
        }

        builder.transSubtitles(transSubtitles.isEmpty() ? null : transSubtitles);
        return builder.build();
    }

    /**
     * TransTitle 파싱 / Parse TransTitle
     * DTD: <!ELEMENT trans-title (#PCDATA | %all-phrase;)*>
     */
    public static TransTitle parseTransTitle(XMLStreamReader reader) throws XMLStreamException {
        String content = parseTextContent(reader, "trans-title");
        return TransTitle.builder().content(content).build();
    }

    /**
     * TransSubtitle 파싱 / Parse TransSubtitle
     * DTD: <!ELEMENT trans-subtitle (#PCDATA | %all-phrase;)*>
     */
    public static TransSubtitle parseTransSubtitle(XMLStreamReader reader) throws XMLStreamException {
        String content = parseTextContent(reader, "trans-subtitle");
        return TransSubtitle.builder().content(content).build();
    }

    /**
     * AltTitle 파싱 / Parse AltTitle
     * DTD: <!ELEMENT alt-title (#PCDATA | %all-phrase;)*>
     * DTD: <!ATTLIST alt-title alt-title-type CDATA #IMPLIED>
     */
    public static AltTitle parseAltTitle(XMLStreamReader reader) throws XMLStreamException {
        String altTitleType = reader.getAttributeValue(null, "alt-title-type");
        String content = parseTextContent(reader, "alt-title");
        return AltTitle.builder()
                .altTitleType(altTitleType)
                .content(content)
                .build();
    }

    /**
     * FnGroup 파싱 / Parse FnGroup
     * DTD: <!ELEMENT fn-group (label?, title?, (fn)+)>
     */
    public static FnGroup parseFnGroup(XMLStreamReader reader) throws XMLStreamException {
        String contentType = reader.getAttributeValue(null, "content-type");
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlBase = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "base");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        FnGroup.FnGroupBuilder builder = FnGroup.builder()
                .contentType(contentType)
                .id(id)
                .specificUse(specificUse)
                .xmlBase(xmlBase)
                .xmlLang(xmlLang);

        List<Fn> footnotes = new ArrayList<>();

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
                    case "fn":
                        footnotes.add(parseFn(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("fn-group")) {
                    break;
                }
            }
        }

        return builder
                .footnotes(footnotes.isEmpty() ? null : footnotes)
                .build();
    }

    public static ContribId parseContribId(XMLStreamReader reader) throws XMLStreamException {
        String authenticated = reader.getAttributeValue(null, "authenticated");
        String contentType = reader.getAttributeValue(null, "content-type");
        String contribIdTypeStr = reader.getAttributeValue(null, "contrib-id-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String value = parseTextContent(reader, "contrib-id");

        return ContribId.builder()
                .authenticated(authenticated)
                .contentType(contentType)
                .contribIdType(ContribIdType.fromValue(contribIdTypeStr))
                .specificUse(specificUse)
                .value(value)
                .build();
    }

    public static Name parseName(XMLStreamReader reader) throws XMLStreamException {
        // Parse attributes
        String contentType = reader.getAttributeValue(null, "content-type");
        String nameStyleStr = reader.getAttributeValue(null, "name-style");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        Name.NameBuilder builder = Name.builder()
                .contentType(contentType)
                .nameStyle(NameStyle.fromValue(nameStyleStr))
                .specificUse(specificUse);

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "surname":
                        builder.surname(BackParser.parseSurname(reader));
                        break;
                    case "given-names":
                        builder.givenNames(BackParser.parseGivenNames(reader));
                        break;
                    case "prefix":
                        builder.prefix(BackParser.parsePrefix(reader));
                        break;
                    case "suffix":
                        builder.suffix(parsePmcSuffix(reader));
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

    public static StringName parseStringName(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "string-name");
        return StringName.builder().value(value).build();
    }

    public static Collab parseCollab(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "collab");
        return Collab.builder().value(value).build();
    }

    public static Anonymous parseAnonymous(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "anonymous");
        return Anonymous.builder().value(value).build();
    }

    public static Degrees parseDegrees(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "degrees");
        return Degrees.builder().value(value).build();
    }

    public static Address parseAddress(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "address");
        return Address.builder().value(value).build();
    }

    public static AuthorComment parseAuthorComment(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "author-comment");
        return AuthorComment.builder().value(value).build();
    }

    public static Bio parseBio(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "bio");
        return Bio.builder().value(value).build();
    }

    public static Etal parseEtal(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "etal");
        return Etal.builder().value(value).build();
    }

    /**
     * Fn 파싱 / Parse Fn
     * DTD: <!ELEMENT fn (label?, (p)+)>
     */
    public static Fn parseFn(XMLStreamReader reader) throws XMLStreamException {
        String fnType = reader.getAttributeValue(null, "fn-type");
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String symbol = reader.getAttributeValue(null, "symbol");
        String xmlBase = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "base");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        Fn.FnBuilder builder = Fn.builder()
                .fnType(fnType)
                .id(id)
                .specificUse(specificUse)
                .symbol(symbol)
                .xmlBase(xmlBase)
                .xmlLang(xmlLang);

        List<P> paragraphs = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "p":
                        paragraphs.add(parseP(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("fn")) {
                    break;
                }
            }
        }

        return builder
                .paragraphs(paragraphs.isEmpty() ? null : paragraphs)
                .build();
    }

    public static OnBehalfOf parseOnBehalfOf(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "on-behalf-of");
        return OnBehalfOf.builder().value(value).build();
    }

    public static Role parseRole(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "role");
        return Role.builder().value(value).build();
    }

    public static AuthorNotes parseAuthorNotes(XMLStreamReader reader) throws XMLStreamException {
        List<Corresp> corresps = new ArrayList<>();
        List<Fn> footnotes = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "corresp":
                        corresps.add(parseCorresp(reader));
                        break;
                    case "fn":
                        footnotes.add(parseFn(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("author-notes")) {
                    break;
                }
            }
        }

        return AuthorNotes.builder()
                .corresps(corresps.isEmpty() ? null : corresps)
                .footnotes(footnotes.isEmpty() ? null : footnotes)
                .build();
    }

    public static Corresp parseCorresp(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String value = parseTextContent(reader, "corresp");

        return Corresp.builder()
                .id(id)
                .value(value)
                .build();
    }

    public static PmcHistory parsePmcHistory(XMLStreamReader reader) throws XMLStreamException {
        List<PmcDate> dates = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("date".equals(localName)) {
                    dates.add(parsePmcDate(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("history")) {
                    break;
                }
            }
        }

        return PmcHistory.builder()
                .dates(dates.isEmpty() ? null : dates)
                .build();
    }

    public static PmcDate parsePmcDate(XMLStreamReader reader) throws XMLStreamException {
        String dateType = reader.getAttributeValue(null, "date-type");
        PmcDate.PmcDateBuilder builder = PmcDate.builder().dateType(dateType);

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "day":
                        builder.day(parseDay(reader));
                        break;
                    case "month":
                        builder.month(parseMonth(reader));
                        break;
                    case "year":
                        builder.year(parseYear(reader));
                        break;
                    case "season":
                        builder.season(parseSeason(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("date")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    public static Permissions parsePermissions(XMLStreamReader reader) throws XMLStreamException {
        Permissions.PermissionsBuilder builder = Permissions.builder();

        List<CopyrightStatement> copyrightStatements = new ArrayList<>();
        List<CopyrightYear> copyrightYears = new ArrayList<>();
        List<CopyrightHolder> copyrightHolders = new ArrayList<>();
        List<License> licenses = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "copyright-statement":
                        copyrightStatements.add(parseCopyrightStatement(reader));
                        break;
                    case "copyright-year":
                        copyrightYears.add(parseCopyrightYear(reader));
                        break;
                    case "copyright-holder":
                        copyrightHolders.add(parseCopyrightHolder(reader));
                        break;
                    case "license":
                        licenses.add(parseLicense(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("permissions")) {
                    break;
                }
            }
        }

        builder.copyrightStatements(copyrightStatements.isEmpty() ? null : copyrightStatements);
        builder.copyrightYears(copyrightYears.isEmpty() ? null : copyrightYears);
        builder.copyrightHolders(copyrightHolders.isEmpty() ? null : copyrightHolders);
        builder.licenses(licenses.isEmpty() ? null : licenses);

        return builder.build();
    }

    public static RelatedArticle parseRelatedArticle(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "related-article");
        return RelatedArticle.builder().value(value).build();
    }

    public static RelatedObject parseRelatedObject(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "related-object");
        return RelatedObject.builder().value(value).build();
    }

    public static TransAbstract parseTransAbstract(XMLStreamReader reader) throws XMLStreamException {
        String xmlLang = reader.getAttributeValue(null, "lang");
        if (xmlLang == null) {
            xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");
        }
        String value = parseTextContent(reader, "trans-abstract");

        return TransAbstract.builder()
                .xmlLang(xmlLang)
                .value(value)
                .build();
    }

    public static Kwd parseKwd(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "kwd");
        return Kwd.builder().value(value).build();
    }

    public static FundingGroup parseFundingGroup(XMLStreamReader reader) throws XMLStreamException {
        List<AwardGroup> awardGroups = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("award-group".equals(localName)) {
                    awardGroups.add(parseAwardGroup(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("funding-group")) {
                    break;
                }
            }
        }

        return FundingGroup.builder()
                .awardGroups(awardGroups.isEmpty() ? null : awardGroups)
                .build();
    }

    public static AwardGroup parseAwardGroup(XMLStreamReader reader) throws XMLStreamException {
        List<FundingSource> fundingSources = new ArrayList<>();
        List<AwardId> awardIds = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "funding-source":
                        fundingSources.add(parseFundingSource(reader));
                        break;
                    case "award-id":
                        awardIds.add(parseAwardId(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("award-group")) {
                    break;
                }
            }
        }

        return AwardGroup.builder()
                .fundingSources(fundingSources.isEmpty() ? null : fundingSources)
                .awardIds(awardIds.isEmpty() ? null : awardIds)
                .build();
    }

    public static FundingSource parseFundingSource(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "funding-source");
        return FundingSource.builder().value(value).build();
    }

    public static AwardId parseAwardId(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "award-id");
        return AwardId.builder().value(value).build();
    }

    public static SupportGroup parseSupportGroup(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "support-group");
        return SupportGroup.builder().value(value).build();
    }

    public static Conference parseConference(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "conference");
        return Conference.builder().value(value).build();
    }

    public static Counts parseCounts(XMLStreamReader reader) throws XMLStreamException {
        Counts.CountsBuilder builder = Counts.builder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "page-count":
                        String pageCountAttr = reader.getAttributeValue(null, "count");
                        if (pageCountAttr != null) {
                            builder.pageCount(Integer.parseInt(pageCountAttr));
                        }
                        skipElement(reader);
                        break;
                    case "fig-count":
                        String figCountAttr = reader.getAttributeValue(null, "count");
                        if (figCountAttr != null) {
                            builder.figCount(Integer.parseInt(figCountAttr));
                        }
                        skipElement(reader);
                        break;
                    case "table-count":
                        String tableCountAttr = reader.getAttributeValue(null, "count");
                        if (tableCountAttr != null) {
                            builder.tableCount(Integer.parseInt(tableCountAttr));
                        }
                        skipElement(reader);
                        break;
                    case "equation-count":
                        String equationCountAttr = reader.getAttributeValue(null, "count");
                        if (equationCountAttr != null) {
                            builder.equationCount(Integer.parseInt(equationCountAttr));
                        }
                        skipElement(reader);
                        break;
                    case "ref-count":
                        String refCountAttr = reader.getAttributeValue(null, "count");
                        if (refCountAttr != null) {
                            builder.refCount(Integer.parseInt(refCountAttr));
                        }
                        skipElement(reader);
                        break;
                    case "word-count":
                        String wordCountAttr = reader.getAttributeValue(null, "count");
                        if (wordCountAttr != null) {
                            builder.wordCount(Integer.parseInt(wordCountAttr));
                        }
                        skipElement(reader);
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("counts")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    public static CustomMetaGroup parseCustomMetaGroup(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "custom-meta-group");
        return CustomMetaGroup.builder().value(value).build();
    }

    public static Season parseSeason(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "season");
        return Season.builder().value(value).build();
    }

    public static Era parseEra(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "era");
        return Era.builder().value(value).build();
    }

    public static StringDate parseStringDate(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "string-date");
        return StringDate.builder().value(value).build();
    }

    /**
     * ObjectId 파싱 / Parse ObjectId
     * DTD: <!ELEMENT object-id (#PCDATA)>
     * DTD: <!ATTLIST object-id
     *          assigning-authority CDATA #IMPLIED
     *          content-type CDATA #IMPLIED
     *          id ID #IMPLIED
     *          pub-id-type CDATA #IMPLIED
     *          specific-use CDATA #IMPLIED>
     */
    public static ObjectId parseObjectId(XMLStreamReader reader) throws XMLStreamException {
        String assigningAuthority = reader.getAttributeValue(null, "assigning-authority");
        String contentType = reader.getAttributeValue(null, "content-type");
        String id = reader.getAttributeValue(null, "id");
        String pubIdTypeStr = reader.getAttributeValue(null, "pub-id-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String value = parseTextContent(reader, "object-id");

        return ObjectId.builder()
                .assigningAuthority(assigningAuthority)
                .contentType(contentType)
                .id(id)
                .pubIdType(PubIdType.fromValue(pubIdTypeStr))
                .specificUse(specificUse)
                .value(value)
                .build();
    }

    /**
     * Attrib 파싱 / Parse Attrib
     * DTD: <!ELEMENT attrib (#PCDATA | %all-phrase;)*>
     * Note: Attribution/source information, simplest model (value only)
     */
    public static Attrib parseAttrib(XMLStreamReader reader) throws XMLStreamException {
        String value = parseTextContent(reader, "attrib");
        return Attrib.builder().value(value).build();
    }

    /**
     * Speech 파싱 / Parse Speech
     * DTD: <!ELEMENT speech (speaker, p+)>
     * DTD: <!ATTLIST speech id ID #IMPLIED xml:lang NMTOKEN #IMPLIED>
     */
    public static Speech parseSpeech(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");
        String value = parseTextContent(reader, "speech");

        return Speech.builder()
                .id(id)
                .xmlLang(xmlLang)
                .value(value)
                .build();
    }

    /**
     * Statement 파싱 / Parse Statement
     * DTD: <!ELEMENT statement (label?, title?, p+)>
     * DTD: <!ATTLIST statement id ID #IMPLIED xml:lang NMTOKEN #IMPLIED>
     */
    public static Statement parseStatement(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");
        String value = parseTextContent(reader, "statement");

        return Statement.builder()
                .id(id)
                .xmlLang(xmlLang)
                .value(value)
                .build();
    }

    /**
     * VerseGroup 파싱 / Parse VerseGroup
     * DTD: <!ELEMENT verse-group (label?, title?, subtitle*, verse-line+, attrib?, permissions?)>
     * DTD: <!ATTLIST verse-group id ID #IMPLIED xml:lang NMTOKEN #IMPLIED>
     */
    public static VerseGroup parseVerseGroup(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");
        String value = parseTextContent(reader, "verse-group");

        return VerseGroup.builder()
                .id(id)
                .xmlLang(xmlLang)
                .value(value)
                .build();
    }

    /**
     * DispFormula 파싱 / Parse DispFormula
     * DTD: <!ELEMENT disp-formula (
     *          object-id*,
     *          label?,
     *          (tex-math | mml:math)*,
     *          graphic*,
     *          attrib?,
     *          permissions?)>
     * DTD: <!ATTLIST disp-formula id ID #IMPLIED xml:lang NMTOKEN #IMPLIED>
     */
    public static DispFormula parseDispFormula(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        DispFormula.DispFormulaBuilder builder = DispFormula.builder()
                .id(id)
                .xmlLang(xmlLang);

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseTextContent(reader, "label"));
                        break;
                    case "tex-math":
                        builder.texMath(parseTextContent(reader, "tex-math"));
                        break;
                    case "mml:math":
                    case "math":
                        builder.mmlMath(parseTextContent(reader, localName));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("disp-formula")) {
                    break;
                }
            }
        }

        return builder.build();
    }

    /**
     * DispFormulaGroup 파싱 / Parse DispFormulaGroup
     * DTD: <!ELEMENT disp-formula-group (label?, (disp-formula)+, caption?)>
     * DTD: <!ATTLIST disp-formula-group id ID #IMPLIED xml:lang NMTOKEN #IMPLIED>
     */
    public static DispFormulaGroup parseDispFormulaGroup(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        DispFormulaGroup.DispFormulaGroupBuilder builder = DispFormulaGroup.builder()
                .id(id)
                .xmlLang(xmlLang);

        List<DispFormula> dispFormulas = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseTextContent(reader, "label"));
                        break;
                    case "disp-formula":
                        dispFormulas.add(parseDispFormula(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("disp-formula-group")) {
                    break;
                }
            }
        }

        return builder
                .dispFormulas(dispFormulas.isEmpty() ? null : dispFormulas)
                .build();
    }

    /**
     * Alternatives 파싱 / Parse Alternatives
     * DTD: <!ELEMENT alternatives (
     *          object-id*,
     *          (graphic | media | table | code | textual-form | tex-math |
     *           supplementary-material | preformat | array | inline-graphic | mml:math)+)>
     * DTD: <!ATTLIST alternatives id ID #IMPLIED>
     */
    public static Alternatives parseAlternatives(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");

        Alternatives.AlternativesBuilder builder = Alternatives.builder().id(id);

        List<ObjectId> objectIds = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();
        List<Media> medias = new ArrayList<>();
        List<Table> tables = new ArrayList<>();
        List<Code> codes = new ArrayList<>();
        List<SupplementaryMaterial> supplementaryMaterials = new ArrayList<>();
        List<Preformat> preformats = new ArrayList<>();
        List<Array> arrays = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "object-id":
                        objectIds.add(parseObjectId(reader));
                        break;
                    case "graphic":
                        graphics.add(PmcXmlParser.parseGraphic(reader));
                        break;
                    case "media":
                        medias.add(PmcXmlParser.parseMedia(reader));
                        break;
                    case "table":
                        tables.add(PmcXmlParser.parseTable(reader));
                        break;
                    case "table-wrap":
                        // table-wrap is not supported in Alternatives, only table
                        skipElement(reader);
                        break;
                    case "code":
                        codes.add(parseCode(reader));
                        break;
                    case "supplementary-material":
                        supplementaryMaterials.add(parseSupplementaryMaterial(reader));
                        break;
                    case "preformat":
                        preformats.add(parsePreformat(reader));
                        break;
                    case "array":
                        arrays.add(parseArray(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("alternatives")) {
                    break;
                }
            }
        }

        return builder
                .objectIds(objectIds.isEmpty() ? null : objectIds)
                .graphics(graphics.isEmpty() ? null : graphics)
                .medias(medias.isEmpty() ? null : medias)
                .tables(tables.isEmpty() ? null : tables)
                .codes(codes.isEmpty() ? null : codes)
                .supplementaryMaterials(supplementaryMaterials.isEmpty() ? null : supplementaryMaterials)
                .preformats(preformats.isEmpty() ? null : preformats)
                .arrays(arrays.isEmpty() ? null : arrays)
                .build();
    }

    /**
     * Preformat 파싱 / Parse Preformat
     * DTD: <!ELEMENT preformat (#PCDATA | %all-phrase;)*>
     * DTD: <!ATTLIST preformat
     *          id ID #IMPLIED
     *          orientation (portrait | landscape) #IMPLIED
     *          position (anchor | background | float | margin) "float"
     *          preformat-type CDATA #IMPLIED
     *          specific-use CDATA #IMPLIED
     *          xml:lang NMTOKEN #IMPLIED
     *          xml:space (preserve) #FIXED "preserve">
     */
    public static Preformat parsePreformat(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String orientation = reader.getAttributeValue(null, "orientation");
        String position = reader.getAttributeValue(null, "position");
        String preformatType = reader.getAttributeValue(null, "preformat-type");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");
        String xmlSpace = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "space");

        Preformat.PreformatBuilder builder = Preformat.builder()
                .id(id)
                .orientation(Orientation.fromValue(orientation))
                .position(Position.fromValue(position))
                .preformatType(preformatType)
                .specificUse(specificUse)
                .xmlLang(xmlLang)
                .xmlSpace(xmlSpace);

        List<Attrib> attribs = new ArrayList<>();
        List<Permissions> permissionsList = new ArrayList<>();

        StringBuilder content = new StringBuilder();
        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "attrib":
                        attribs.add(parseAttrib(reader));
                        break;
                    case "permissions":
                        permissionsList.add(parsePermissions(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.CHARACTERS) {
                content.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("preformat")) {
                    break;
                }
            }
        }

        return builder
                .content(content.length() > 0 ? content.toString() : null)
                .attribs(attribs.isEmpty() ? null : attribs)
                .permissions(permissionsList.isEmpty() ? null : permissionsList)
                .build();
    }

    /**
     * Array 파싱 / Parse Array
     * DTD: <!ELEMENT array (
     *          object-id*,
     *          label?,
     *          caption?,
     *          alt-text*,
     *          long-desc*,
     *          (email | ext-link | uri)*,
     *          alternatives?,
     *          (graphic | media)*,
     *          tbody+,
     *          (attrib | permissions)*)>
     * DTD: <!ATTLIST array
     *          content-type CDATA #IMPLIED
     *          id ID #IMPLIED
     *          orientation (portrait | landscape) #IMPLIED
     *          specific-use CDATA #IMPLIED
     *          xml:lang NMTOKEN #IMPLIED>
     */
    public static Array parseArray(XMLStreamReader reader) throws XMLStreamException {
        String contentType = reader.getAttributeValue(null, "content-type");
        String id = reader.getAttributeValue(null, "id");
        String orientation = reader.getAttributeValue(null, "orientation");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        Array.ArrayBuilder builder = Array.builder()
                .contentType(contentType)
                .id(id)
                .orientation(Orientation.fromValue(orientation))
                .specificUse(specificUse)
                .xmlLang(xmlLang);

        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();
        List<Email> emails = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        List<Uri> uris = new ArrayList<>();
        List<Alternatives> alternatives = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();
        List<Media> medias = new ArrayList<>();
        List<Tbody> tbodies = new ArrayList<>();
        List<Attrib> attribs = new ArrayList<>();
        List<Permissions> permissionsList = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "alt-text":
                        altTexts.add(PmcXmlParser.parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(PmcXmlParser.parseLongDesc(reader));
                        break;
                    case "email":
                        emails.add(parseEmail(reader));
                        break;
                    case "ext-link":
                        extLinks.add(parseExtLink(reader));
                        break;
                    case "uri":
                        uris.add(parseUri(reader));
                        break;
                    case "alternatives":
                        alternatives.add(parseAlternatives(reader));
                        break;
                    case "graphic":
                        graphics.add(PmcXmlParser.parseGraphic(reader));
                        break;
                    case "media":
                        medias.add(PmcXmlParser.parseMedia(reader));
                        break;
                    case "tbody":
                        tbodies.add(PmcXmlParser.parseTbody(reader));
                        break;
                    case "attrib":
                        attribs.add(parseAttrib(reader));
                        break;
                    case "permissions":
                        permissionsList.add(parsePermissions(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("array")) {
                    break;
                }
            }
        }

        return builder
                .altTexts(altTexts.isEmpty() ? null : altTexts)
                .longDescs(longDescs.isEmpty() ? null : longDescs)
                .emails(emails.isEmpty() ? null : emails)
                .extLinks(extLinks.isEmpty() ? null : extLinks)
                .uris(uris.isEmpty() ? null : uris)
                .alternatives(alternatives.isEmpty() ? null : alternatives)
                .graphics(graphics.isEmpty() ? null : graphics)
                .medias(medias.isEmpty() ? null : medias)
                .tbodies(tbodies.isEmpty() ? null : tbodies)
                .attribs(attribs.isEmpty() ? null : attribs)
                .permissions(permissionsList.isEmpty() ? null : permissionsList)
                .build();
    }

    /**
     * ChemStructWrap 파싱 / Parse ChemStructWrap
     * DTD: <!ELEMENT chem-struct-wrap (
     *          object-id*,
     *          label?,
     *          caption?,
     *          abstract*,
     *          kwd-group*,
     *          alt-text*,
     *          long-desc*,
     *          (email | ext-link | uri)*,
     *          alternatives?,
     *          chem-struct+,
     *          (code | graphic | media | preformat | textual-form)*,
     *          (attrib | permissions)*)>
     * DTD: <!ATTLIST chem-struct-wrap
     *          content-type CDATA #IMPLIED
     *          id ID #IMPLIED
     *          orientation (portrait | landscape) #IMPLIED
     *          position (anchor | background | float | margin) "float"
     *          specific-use CDATA #IMPLIED
     *          xml:base CDATA #IMPLIED
     *          xml:lang NMTOKEN #IMPLIED>
     */
    public static ChemStructWrap parseChemStructWrap(XMLStreamReader reader) throws XMLStreamException {
        String contentType = reader.getAttributeValue(null, "content-type");
        String id = reader.getAttributeValue(null, "id");
        String orientation = reader.getAttributeValue(null, "orientation");
        String position = reader.getAttributeValue(null, "position");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlBase = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "base");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        ChemStructWrap.ChemStructWrapBuilder builder = ChemStructWrap.builder()
                .contentType(contentType)
                .id(id)
                .orientation(Orientation.fromValue(orientation))
                .position(Position.fromValue(position))
                .specificUse(specificUse)
                .xmlBase(xmlBase)
                .xmlLang(xmlLang);

        List<ObjectId> objectIds = new ArrayList<>();
        List<Caption> captions = new ArrayList<>();
        List<PmcAbstract> abstracts = new ArrayList<>();
        List<KwdGroup> kwdGroups = new ArrayList<>();
        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();
        List<Email> emails = new ArrayList<>();
        List<ExtLink> extLinks = new ArrayList<>();
        List<Uri> uris = new ArrayList<>();
        List<Alternatives> alternatives = new ArrayList<>();
        List<ChemStruct> chemStructs = new ArrayList<>();
        List<Code> codes = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();
        List<Media> medias = new ArrayList<>();
        List<Preformat> preformats = new ArrayList<>();
        List<TextualForm> textualForms = new ArrayList<>();
        List<Attrib> attribs = new ArrayList<>();
        List<Permissions> permissionsList = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "object-id":
                        objectIds.add(parseObjectId(reader));
                        break;
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "caption":
                        captions.add(parseCaption(reader));
                        break;
                    case "abstract":
                        abstracts.add(parsePmcAbstract(reader));
                        break;
                    case "kwd-group":
                        kwdGroups.add(parseKwdGroup(reader));
                        break;
                    case "alt-text":
                        altTexts.add(PmcXmlParser.parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(PmcXmlParser.parseLongDesc(reader));
                        break;
                    case "email":
                        emails.add(parseEmail(reader));
                        break;
                    case "ext-link":
                        extLinks.add(parseExtLink(reader));
                        break;
                    case "uri":
                        uris.add(parseUri(reader));
                        break;
                    case "alternatives":
                        alternatives.add(parseAlternatives(reader));
                        break;
                    case "chem-struct":
                        chemStructs.add(parseChemStruct(reader));
                        break;
                    case "code":
                        codes.add(parseCode(reader));
                        break;
                    case "graphic":
                        graphics.add(PmcXmlParser.parseGraphic(reader));
                        break;
                    case "media":
                        medias.add(PmcXmlParser.parseMedia(reader));
                        break;
                    case "preformat":
                        preformats.add(parsePreformat(reader));
                        break;
                    case "textual-form":
                        textualForms.add(parseTextualForm(reader));
                        break;
                    case "attrib":
                        attribs.add(parseAttrib(reader));
                        break;
                    case "permissions":
                        permissionsList.add(parsePermissions(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("chem-struct-wrap")) {
                    break;
                }
            }
        }

        return builder
                .objectIds(objectIds.isEmpty() ? null : objectIds)
                .captions(captions.isEmpty() ? null : captions)
                .abstracts(abstracts.isEmpty() ? null : abstracts)
                .kwdGroups(kwdGroups.isEmpty() ? null : kwdGroups)
                .altTexts(altTexts.isEmpty() ? null : altTexts)
                .longDescs(longDescs.isEmpty() ? null : longDescs)
                .emails(emails.isEmpty() ? null : emails)
                .extLinks(extLinks.isEmpty() ? null : extLinks)
                .uris(uris.isEmpty() ? null : uris)
                .alternatives(alternatives.isEmpty() ? null : alternatives)
                .codes(codes.isEmpty() ? null : codes)
                .graphics(graphics.isEmpty() ? null : graphics)
                .medias(medias.isEmpty() ? null : medias)
                .preformats(preformats.isEmpty() ? null : preformats)
                .attribs(attribs.isEmpty() ? null : attribs)
                .permissions(permissionsList.isEmpty() ? null : permissionsList)
                .chemStructs(chemStructs.isEmpty() ? null : chemStructs)
                .textualForms(textualForms.isEmpty() ? null : textualForms)
                .build();
    }

    /**
     * TextualForm 파싱 / Parse TextualForm
     *
     * KR: 대안 표현의 텍스트 형식을 파싱합니다.
     * EN: Parses textual form of alternative representation.
     *
     * DTD: <!ELEMENT textual-form (#PCDATA | %all-phrase;)*>
     * DTD: <!ATTLIST textual-form
     *          id ID #IMPLIED
     *          specific-use CDATA #IMPLIED
     *          xml:lang NMTOKEN #IMPLIED>
     */
    public static TextualForm parseTextualForm(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        String content = parseTextContent(reader, "textual-form");

        return TextualForm.builder()
                .id(id)
                .specificUse(specificUse)
                .xmlLang(xmlLang)
                .content(content)
                .build();
    }

    /**
     * ChemStruct 파싱 / Parse ChemStruct
     *
     * KR: 화학 구조 요소를 파싱합니다.
     * EN: Parses chemical structure element.
     *
     * DTD: <!ELEMENT chem-struct (#PCDATA | %access.class; | %address-link.class; |
     *          %break.class; | %emphasis.class; | %inline-display-noalt.class; |
     *          %inline-math.class; | %list.class; | %simple-link.class; |
     *          %simple-text.class; | %subsup.class; | alt-text | array |
     *          attrib | code | graphic | label | long-desc | media |
     *          permissions | preformat | textual-form)*>
     *
     * DTD: <!ATTLIST chem-struct
     *          content-type CDATA #IMPLIED
     *          id ID #IMPLIED
     *          specific-use CDATA #IMPLIED
     *          xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
     *          xlink:href CDATA #IMPLIED
     *          xlink:role CDATA #IMPLIED
     *          xlink:show (embed | new | none | other | replace) #IMPLIED
     *          xlink:title CDATA #IMPLIED
     *          xlink:type (simple) #IMPLIED
     *          xml:base CDATA #IMPLIED
     *          xml:lang NMTOKEN #IMPLIED>
     */
    public static ChemStruct parseChemStruct(XMLStreamReader reader) throws XMLStreamException {
        // Attributes
        String contentType = reader.getAttributeValue(null, "content-type");
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xlinkActuateStr = reader.getAttributeValue("http://www.w3.org/1999/xlink", "actuate");
        String xlinkHref = reader.getAttributeValue("http://www.w3.org/1999/xlink", "href");
        String xlinkRole = reader.getAttributeValue("http://www.w3.org/1999/xlink", "role");
        String xlinkShowStr = reader.getAttributeValue("http://www.w3.org/1999/xlink", "show");
        String xlinkTitle = reader.getAttributeValue("http://www.w3.org/1999/xlink", "title");
        String xlinkType = reader.getAttributeValue("http://www.w3.org/1999/xlink", "type");
        String xmlBase = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "base");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        ChemStruct.ChemStructBuilder builder = ChemStruct.builder()
                .contentType(contentType)
                .id(id)
                .specificUse(specificUse)
                .xlinkActuate(XlinkActuate.fromValue(xlinkActuateStr))
                .xlinkHref(xlinkHref)
                .xlinkRole(xlinkRole)
                .xlinkShow(XlinkShow.fromValue(xlinkShowStr))
                .xlinkTitle(xlinkTitle)
                .xlinkType(xlinkType)
                .xmlBase(xmlBase)
                .xmlLang(xmlLang);

        List<AltText> altTexts = new ArrayList<>();
        List<LongDesc> longDescs = new ArrayList<>();
        List<Graphic> graphics = new ArrayList<>();
        List<Media> medias = new ArrayList<>();
        List<Array> arrays = new ArrayList<>();
        List<Code> codes = new ArrayList<>();
        List<TextualForm> textualForms = new ArrayList<>();
        StringBuilder value = new StringBuilder();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "label":
                        builder.label(parseLabel(reader));
                        break;
                    case "alt-text":
                        altTexts.add(PmcXmlParser.parseAltText(reader));
                        break;
                    case "long-desc":
                        longDescs.add(PmcXmlParser.parseLongDesc(reader));
                        break;
                    case "graphic":
                        graphics.add(PmcXmlParser.parseGraphic(reader));
                        break;
                    case "media":
                        medias.add(PmcXmlParser.parseMedia(reader));
                        break;
                    case "array":
                        arrays.add(parseArray(reader));
                        break;
                    case "code":
                        codes.add(parseCode(reader));
                        break;
                    case "attrib":
                        builder.attrib(parseAttrib(reader));
                        break;
                    case "permissions":
                        builder.permissions(parsePermissions(reader));
                        break;
                    case "textual-form":
                        textualForms.add(parseTextualForm(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.CHARACTERS || event == XMLStreamConstants.CDATA) {
                value.append(reader.getText());
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("chem-struct")) {
                    break;
                }
            }
        }

        return builder
                .value(value.length() > 0 ? value.toString().trim() : null)
                .altTexts(altTexts.isEmpty() ? null : altTexts)
                .longDescs(longDescs.isEmpty() ? null : longDescs)
                .graphics(graphics.isEmpty() ? null : graphics)
                .medias(medias.isEmpty() ? null : medias)
                .arrays(arrays.isEmpty() ? null : arrays)
                .codes(codes.isEmpty() ? null : codes)
                .textualForms(textualForms.isEmpty() ? null : textualForms)
                .build();
    }

    /**
     * AffAlternatives 파싱 / Parse AffAlternatives
     *
     * KR: 다양한 언어 또는 형식의 소속 정보를 파싱합니다.
     * EN: Parses affiliation information in various languages or formats.
     *
     * DTD: <!ELEMENT aff-alternatives (aff+)>
     */
    public static AffAlternatives parseAffAlternatives(XMLStreamReader reader) throws XMLStreamException {
        List<Aff> affiliations = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                if ("aff".equals(localName)) {
                    affiliations.add(parseAff(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("aff-alternatives")) {
                    break;
                }
            }
        }

        return AffAlternatives.builder()
                .affiliations(affiliations.isEmpty() ? null : affiliations)
                .build();
    }

    /**
     * PubDateNotAvailable 파싱 / Parse PubDateNotAvailable
     *
     * KR: 출판일을 사용할 수 없음을 나타내는 요소를 파싱합니다.
     * EN: Parses element indicating that publication date is not available.
     *
     * DTD: <!ELEMENT pub-date-not-available MIXED>
     * DTD: <!ATTLIST pub-date-not-available
     *          id ID #IMPLIED
     *          xml:lang NMTOKEN #IMPLIED
     *      >
     */
    public static PubDateNotAvailable parsePubDateNotAvailable(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String xmlLang = reader.getAttributeValue("http://www.w3.org/XML/1998/namespace", "lang");

        String value = parseTextContent(reader, "pub-date-not-available");

        return PubDateNotAvailable.builder()
                .id(id)
                .xmlLang(xmlLang)
                .value(value)
                .build();
    }

    /**
     * PubHistory 파싱 / Parse PubHistory
     *
     * KR: 논문의 상세 출판 이력을 파싱합니다 (JATS 1.3+).
     * EN: Parses detailed publication history of article (JATS 1.3+).
     *
     * DTD: <!ELEMENT pub-history (date | event)*>
     * DTD: <!ATTLIST pub-history
     *          id ID #IMPLIED
     *          specific-use CDATA #IMPLIED
     *      >
     */
    public static PubHistory parsePubHistory(XMLStreamReader reader) throws XMLStreamException {
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");

        List<PmcDate> dates = new ArrayList<>();
        List<Event> events = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "date":
                        dates.add(parsePmcDate(reader));
                        break;
                    case "event":
                        events.add(parseEvent(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("pub-history")) {
                    break;
                }
            }
        }

        return PubHistory.builder()
                .id(id)
                .specificUse(specificUse)
                .dates(dates.isEmpty() ? null : dates)
                .events(events.isEmpty() ? null : events)
                .build();
    }

    /**
     * Event 파싱 / Parse Event
     *
     * KR: 출판 이력의 이벤트를 파싱합니다.
     * EN: Parses an event in publication history.
     *
     * DTD: <!ELEMENT event (...)>
     */
    private static Event parseEvent(XMLStreamReader reader) throws XMLStreamException {
        String eventType = reader.getAttributeValue(null, "event-type");
        String id = reader.getAttributeValue(null, "id");
        String specificUse = reader.getAttributeValue(null, "specific-use");
        String xmlLang = reader.getAttributeValue(null, "xml:lang");

        String eventDesc = null;
        String title = null;
        List<PmcDate> dates = new ArrayList<>();

        while (reader.hasNext()) {
            int event = reader.next();

            if (event == XMLStreamConstants.START_ELEMENT) {
                String localName = reader.getLocalName();

                switch (localName) {
                    case "event-desc":
                        eventDesc = parseTextContent(reader, "event-desc");
                        break;
                    case "title":
                        title = parseTextContent(reader, "title");
                        break;
                    case "date":
                        dates.add(parsePmcDate(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if (reader.getLocalName().equals("event")) {
                    break;
                }
            }
        }

        return Event.builder()
                .eventType(eventType)
                .id(id)
                .specificUse(specificUse)
                .xmlLang(xmlLang)
                .eventDesc(eventDesc)
                .title(title)
                .dates(dates.isEmpty() ? null : dates)
                .build();
    }
}
