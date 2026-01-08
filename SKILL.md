# SKILL: PubMed & PMC XML Parser

## Overview
PubMed와 PMC XML을 DTD 기준 100% 완전 파싱하는 Java 라이브러리 개발 스킬.

---

# Part 1: PubMed XML Specification

## 1.1 Root Element
```xml
<!ELEMENT PubmedArticleSet ((PubmedArticle | PubmedBookArticle)+, DeleteCitation?)>
```

## 1.2 PubmedArticle Structure
```xml
<!ELEMENT PubmedArticle (MedlineCitation, PubmedData?)>
```

## 1.3 MedlineCitation (핵심)
```xml
<!ELEMENT MedlineCitation (
    PMID,
    DateCompleted?,
    DateRevised?, 
    Article,
    MedlineJournalInfo,
    ChemicalList?,
    SupplMeshList?,
    CitationSubset*,
    CommentsCorrectionsList?,
    GeneSymbolList?,
    MeshHeadingList?,
    NumberOfReferences?,
    PersonalNameSubjectList?,
    OtherID*,
    OtherAbstract*,
    KeywordList*,
    CoiStatement?,
    SpaceFlightMission*,
    InvestigatorList?,
    GeneralNote*
)>

<!ATTLIST MedlineCitation
    Status (Completed | In-Process | PubMed-not-MEDLINE | In-Data-Review | Publisher | MEDLINE | OLDMEDLINE) #REQUIRED
    Owner (NLM | NASA | PIP | KIE | HSR | HMD | NOTNLM) "NLM"
    IndexingMethod (Automated | Curated) #IMPLIED
    VersionID CDATA #IMPLIED
    VersionDate CDATA #IMPLIED
>
```

## 1.4 Article
```xml
<!ELEMENT Article (
    Journal,
    ArticleTitle,
    ((Pagination, ELocationID*) | ELocationID+),
    Abstract?,
    AuthorList?,
    Language+,
    DataBankList?,
    GrantList?,
    PublicationTypeList,
    VernacularTitle?,
    ArticleDate*
)>

<!ATTLIST Article
    PubModel (Print | Print-Electronic | Electronic | Electronic-Print | Electronic-eCollection) #REQUIRED
>
```

## 1.5 Journal
```xml
<!ELEMENT Journal (ISSN?, JournalIssue, Title?, ISOAbbreviation?)>
<!ELEMENT JournalIssue (Volume?, Issue?, PubDate)>
<!ATTLIST JournalIssue CitedMedium (Internet | Print) #REQUIRED>
<!ELEMENT PubDate ((Year, ((Month, Day?) | Season)?) | MedlineDate)>
```

## 1.6 PMID
```xml
<!ELEMENT PMID (#PCDATA)>
<!ATTLIST PMID Version CDATA "1">
```

## 1.7 ISSN
```xml
<!ELEMENT ISSN (#PCDATA)>
<!ATTLIST ISSN IssnType (Electronic | Print) #REQUIRED>
```

## 1.8 Pagination
```xml
<!ELEMENT Pagination ((StartPage, EndPage?, MedlinePgn?) | MedlinePgn)>
```

## 1.9 ELocationID
```xml
<!ELEMENT ELocationID (#PCDATA)>
<!ATTLIST ELocationID
    EIdType (doi | pii) #REQUIRED
    ValidYN (Y | N) "Y"
>
```

## 1.10 Abstract
```xml
<!ELEMENT Abstract (AbstractText+, CopyrightInformation?)>
<!ELEMENT AbstractText (%text; | mml:math)*>
<!ATTLIST AbstractText
    Label CDATA #IMPLIED
    NlmCategory (BACKGROUND | OBJECTIVE | METHODS | RESULTS | CONCLUSIONS | UNASSIGNED) #IMPLIED
>
```

## 1.11 AuthorList & Author
```xml
<!ELEMENT AuthorList (Author+)>
<!ATTLIST AuthorList
    CompleteYN (Y | N) "Y"
    Type (authors | editors) #IMPLIED
>

<!ELEMENT Author (
    ((LastName, ForeName?, Initials?, Suffix?) | CollectiveName),
    Identifier*,
    AffiliationInfo*
)>
<!ATTLIST Author
    ValidYN (Y | N) "Y"
    EqualContrib (Y | N) #IMPLIED
>

<!ELEMENT AffiliationInfo (Affiliation, Identifier*)>
<!ELEMENT Identifier (#PCDATA)>
<!ATTLIST Identifier Source CDATA #REQUIRED>
```

## 1.12 DataBankList
```xml
<!ELEMENT DataBankList (DataBank+)>
<!ATTLIST DataBankList CompleteYN (Y | N) "Y">
<!ELEMENT DataBank (DataBankName, AccessionNumberList?)>
<!ELEMENT AccessionNumberList (AccessionNumber+)>
```

## 1.13 GrantList
```xml
<!ELEMENT GrantList (Grant+)>
<!ATTLIST GrantList CompleteYN (Y | N) "Y">
<!ELEMENT Grant (GrantID?, Acronym?, Agency, Country)>
```

## 1.14 PublicationType
```xml
<!ELEMENT PublicationTypeList (PublicationType+)>
<!ELEMENT PublicationType (#PCDATA)>
<!ATTLIST PublicationType UI CDATA #REQUIRED>
```

## 1.15 ArticleDate
```xml
<!ELEMENT ArticleDate (Year, Month, Day)>
<!ATTLIST ArticleDate DateType CDATA #REQUIRED>
```

## 1.16 MedlineJournalInfo
```xml
<!ELEMENT MedlineJournalInfo (Country?, MedlineTA, NlmUniqueID?, ISSNLinking?)>
```

## 1.17 ChemicalList
```xml
<!ELEMENT ChemicalList (Chemical+)>
<!ELEMENT Chemical (RegistryNumber, NameOfSubstance)>
<!ELEMENT NameOfSubstance (#PCDATA)>
<!ATTLIST NameOfSubstance UI CDATA #REQUIRED>
```

## 1.18 SupplMeshList
```xml
<!ELEMENT SupplMeshList (SupplMeshName+)>
<!ELEMENT SupplMeshName (#PCDATA)>
<!ATTLIST SupplMeshName
    Type (Disease | Protocol | Organism) #REQUIRED
    UI CDATA #REQUIRED
>
```

## 1.19 CommentsCorrectionsList
```xml
<!ELEMENT CommentsCorrectionsList (CommentsCorrections+)>
<!ELEMENT CommentsCorrections (RefSource, PMID?, Note?)>
<!ATTLIST CommentsCorrections
    RefType (AssociatedDataset | AssociatedPublication | CommentIn | CommentOn |
             CorrectedandRepublishedIn | CorrectedandRepublishedFrom |
             ErratumIn | ErratumFor | ExpressionOfConcernIn | ExpressionOfConcernFor |
             RepublishedIn | RepublishedFrom | RetractedandRepublishedIn |
             RetractedandRepublishedFrom | RetractionIn | RetractionOf |
             UpdateIn | UpdateOf | Cites) #REQUIRED
>
```

## 1.20 MeshHeadingList
```xml
<!ELEMENT MeshHeadingList (MeshHeading+)>
<!ELEMENT MeshHeading (DescriptorName, QualifierName*)>

<!ELEMENT DescriptorName (#PCDATA)>
<!ATTLIST DescriptorName
    UI CDATA #REQUIRED
    MajorTopicYN (Y | N) "N"
    Type (Geographic) #IMPLIED
>

<!ELEMENT QualifierName (#PCDATA)>
<!ATTLIST QualifierName
    UI CDATA #REQUIRED
    MajorTopicYN (Y | N) "N"
>
```

## 1.21 PersonalNameSubjectList
```xml
<!ELEMENT PersonalNameSubjectList (PersonalNameSubject+)>
<!ELEMENT PersonalNameSubject (LastName, ForeName?, Initials?, Suffix?)>
```

## 1.22 OtherID
```xml
<!ELEMENT OtherID (#PCDATA)>
<!ATTLIST OtherID Source (NASA | KIE | PIP | POP | ARPL | CPC | IND | CPFH | CLML | NRCBL | NLM | QCIM) #REQUIRED>
```

## 1.23 OtherAbstract
```xml
<!ELEMENT OtherAbstract (AbstractText+, CopyrightInformation?)>
<!ATTLIST OtherAbstract
    Type (AAMC | AIDS | KIE | PIP | NASA | Publisher) #REQUIRED
    Language CDATA "eng"
>
```

## 1.24 KeywordList
```xml
<!ELEMENT KeywordList (Keyword+)>
<!ATTLIST KeywordList Owner (NLM | NLM-AUTO | NASA | PIP | KIE | NOTNLM | HHS) "NLM">

<!ELEMENT Keyword (#PCDATA)>
<!ATTLIST Keyword MajorTopicYN (Y | N) "N">
```

## 1.25 InvestigatorList
```xml
<!ELEMENT InvestigatorList (Investigator+)>
<!ELEMENT Investigator (LastName, ForeName?, Initials?, Suffix?, Identifier*, AffiliationInfo*)>
<!ATTLIST Investigator ValidYN (Y | N) "Y">
```

## 1.26 GeneralNote
```xml
<!ELEMENT GeneralNote (#PCDATA)>
<!ATTLIST GeneralNote Owner (NLM | NASA | PIP | KIE | HSR | HMD) "NLM">
```

## 1.27 PubmedData
```xml
<!ELEMENT PubmedData (History?, PublicationStatus, ArticleIdList, ObjectList?, ReferenceList*)>
```

## 1.28 History
```xml
<!ELEMENT History (PubMedPubDate+)>
<!ELEMENT PubMedPubDate (Year, Month, Day, (Hour, (Minute, Second?)?)?)>
<!ATTLIST PubMedPubDate
    PubStatus (received | accepted | epublish | ppublish | revised | aheadofprint |
               retracted | ecollection | pmc | pmcr | pubmed | pubmedr |
               premedline | medline | medliner | entrez | pmc-release) #REQUIRED
>
```

## 1.29 ArticleIdList
```xml
<!ELEMENT ArticleIdList (ArticleId+)>
<!ELEMENT ArticleId (#PCDATA)>
<!ATTLIST ArticleId IdType (doi | pii | pmcpid | pmpid | pmc | mid | sici | pubmed | medline | pmcid | pmcbook) "pubmed">
```

## 1.30 ObjectList
```xml
<!ELEMENT ObjectList (Object+)>
<!ELEMENT Object (Param*)>
<!ATTLIST Object Type CDATA #REQUIRED>
<!ELEMENT Param (#PCDATA)>
<!ATTLIST Param Name CDATA #REQUIRED>
```

## 1.31 ReferenceList
```xml
<!ELEMENT ReferenceList (Title?, Reference*, ReferenceList*)>
<!ELEMENT Reference (Citation, ArticleIdList?)>
<!ELEMENT Citation (%text;)*>
```

## 1.32 PubmedBookArticle
```xml
<!ELEMENT PubmedBookArticle (BookDocument, PubmedBookData?)>
```

## 1.33 BookDocument
```xml
<!ELEMENT BookDocument (
    PMID,
    ArticleIdList,
    Book,
    ArticleTitle?,
    VernacularTitle?,
    Pagination?,
    Language*,
    AuthorList*,
    InvestigatorList?,
    PublicationType*,
    Abstract?,
    Sections?,
    KeywordList*,
    CoiStatement?,
    GrantList?,
    ItemList*,
    ReferenceList*
)>
```

## 1.34 Book
```xml
<!ELEMENT Book (
    Publisher,
    BookTitle,
    PubDate,
    BeginningDate?,
    EndingDate?,
    AuthorList*,
    InvestigatorList?,
    Volume?,
    VolumeTitle?,
    Edition?,
    CollectionTitle?,
    Isbn*,
    ELocationID*,
    Medium?,
    ReportNumber?
)>

<!ELEMENT Publisher (PublisherName, PublisherLocation?)>
```

## 1.35 Sections
```xml
<!ELEMENT Sections (Section+)>
<!ELEMENT Section (LocationLabel?, SectionTitle?, Section*)>
<!ELEMENT LocationLabel (#PCDATA)>
<!ATTLIST LocationLabel Type (part | chapter | section | appendix | figure | table | box) #IMPLIED>
```

## 1.36 ItemList
```xml
<!ELEMENT ItemList (Item+)>
<!ATTLIST ItemList ListType CDATA #REQUIRED>
<!ELEMENT Item (#PCDATA)>
```

## 1.37 PubmedBookData
```xml
<!ELEMENT PubmedBookData (History?, PublicationStatus, ArticleIdList, ObjectList?)>
```

## 1.38 DeleteCitation
```xml
<!ELEMENT DeleteCitation (PMID+)>
```

---

# Part 2: PMC XML Specification

## 2.1 PMC Article Set
```xml
<!ELEMENT pmc-articleset (article+)>
```

## 2.2 Article (JATS/NLM DTD)
PMC는 JATS (Journal Article Tag Suite) DTD를 사용.
주요 구조:

```xml
<!ELEMENT article (
    front,
    body?,
    back?,
    floats-group?,
    sub-article*,
    response*
)>

<!ATTLIST article
    article-type CDATA #IMPLIED
    dtd-version CDATA #IMPLIED
    xml:lang CDATA "en"
>
```

## 2.3 Front Matter
```xml
<!ELEMENT front (journal-meta?, article-meta, notes?)>

<!ELEMENT journal-meta (
    journal-id+,
    journal-title-group?,
    issn*,
    isbn*,
    publisher?,
    notes?
)>

<!ELEMENT article-meta (
    article-id*,
    article-categories?,
    title-group,
    contrib-group*,
    aff*,
    author-notes?,
    pub-date+,
    volume?,
    volume-id?,
    volume-series?,
    issue?,
    issue-id?,
    issue-title?,
    issue-sponsor?,
    issue-part?,
    volume-issue-group*,
    isbn*,
    supplement?,
    fpage?,
    lpage?,
    page-range?,
    elocation-id?,
    email*,
    ext-link*,
    uri*,
    product*,
    supplementary-material*,
    history?,
    permissions?,
    self-uri*,
    related-article*,
    related-object*,
    abstract*,
    trans-abstract*,
    kwd-group*,
    funding-group*,
    support-group*,
    conference*,
    counts?,
    custom-meta-group?
)>
```

## 2.4 Title Group
```xml
<!ELEMENT title-group (article-title, subtitle*, trans-title-group*, alt-title*, fn-group?)>
<!ELEMENT article-title (#PCDATA | %all-phrase;)*>
```

## 2.5 Contributor Group
```xml
<!ELEMENT contrib-group (contrib+, xref*, aff*)>
<!ATTLIST contrib-group content-type CDATA #IMPLIED>

<!ELEMENT contrib (
    contrib-id*,
    name?,
    name-alternatives?,
    string-name?,
    collab?,
    collab-alternatives?,
    anonymous?,
    degrees*,
    address*,
    aff*,
    aff-alternatives*,
    author-comment?,
    bio?,
    email*,
    etal?,
    ext-link*,
    fn*,
    on-behalf-of?,
    role*,
    uri*,
    xref*
)>

<!ATTLIST contrib
    contrib-type (author | editor | guest-editor | collab | compiler | director |
                  inventor | reviewer | translator | series-editor) #IMPLIED
    corresp (yes | no) #IMPLIED
    deceased (yes | no) #IMPLIED
    equal-contrib (yes | no) #IMPLIED
    id ID #IMPLIED
    rid IDREFS #IMPLIED
>
```

## 2.6 Affiliation
```xml
<!ELEMENT aff (#PCDATA | %aff-elements;)*>
<!ATTLIST aff id ID #IMPLIED>

<!ELEMENT aff-alternatives (aff+)>
```

## 2.7 Abstract
```xml
<!ELEMENT abstract (label?, title?, (%abstract-model;)*, (%sec-back-matter-mix;)*)>
<!ATTLIST abstract
    abstract-type CDATA #IMPLIED
    id ID #IMPLIED
    xml:lang CDATA #IMPLIED
>

<!ELEMENT kwd-group (label?, title?, (kwd | compound-kwd | nested-kwd | x)+)>
<!ATTLIST kwd-group
    kwd-group-type CDATA #IMPLIED
    xml:lang CDATA #IMPLIED
>
```

## 2.8 Publication Date
```xml
<!ELEMENT pub-date (((day?, month?) | season)?, year, era?, string-date?)>
<!ATTLIST pub-date
    pub-type (ppub | epub | epub-ppub | epreprint | collection | nihms-submitted | pmc-release) #IMPLIED
    publication-format (print | electronic | print-electronic) #IMPLIED
    date-type (pub | preprint | epublish | ppublish | ecorrected | pcorrected | eretracted | pretracted) #IMPLIED
    iso-8601-date CDATA #IMPLIED
>
```

## 2.9 Body
```xml
<!ELEMENT body (%body-model;)*>

<!ELEMENT sec (
    sec-meta?,
    label?,
    title?,
    (%sec-model;)*,
    (%sec-back-matter-mix;)*,
    sec*
)>

<!ATTLIST sec
    id ID #IMPLIED
    sec-type CDATA #IMPLIED
    xml:lang CDATA #IMPLIED
>
```

## 2.10 Back Matter
```xml
<!ELEMENT back (
    label?,
    title*,
    ack*,
    app-group*,
    bio*,
    fn-group*,
    glossary*,
    ref-list*,
    notes*,
    sec*
)>
```

## 2.11 Reference List
```xml
<!ELEMENT ref-list (label?, title?, (%ref-list.class;)*, ref*, ref-list*)>

<!ELEMENT ref (label?, (%citation.class;)+)>
<!ATTLIST ref id ID #IMPLIED>

<!ELEMENT element-citation (%element-citation-model;)*>
<!ATTLIST element-citation
    publication-type (book | confproc | gov | journal | other | patent | 
                      standard | thesis | webpage | software | data | database) #IMPLIED
>

<!ELEMENT mixed-citation (#PCDATA | %citation-elements;)*>
<!ATTLIST mixed-citation
    publication-type CDATA #IMPLIED
>
```

## 2.12 Figures and Tables
```xml
<!ELEMENT fig ((%fig-model;)*)>
<!ATTLIST fig
    fig-type CDATA #IMPLIED
    id ID #IMPLIED
    position (anchor | float | margin) "float"
>

<!ELEMENT table-wrap ((%table-wrap-model;)*)>
<!ATTLIST table-wrap
    id ID #IMPLIED
    position (anchor | float | margin) "float"
>
```

## 2.13 Supplementary Material
```xml
<!ELEMENT supplementary-material (
    label?,
    caption?,
    abstract*,
    kwd-group*,
    alt-text*,
    long-desc*,
    email*,
    ext-link*,
    uri*,
    (%display-back-matter.class;)*,
    attrib?,
    permissions?
)>

<!ATTLIST supplementary-material
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    mime-subtype CDATA #IMPLIED
    mimetype CDATA #IMPLIED
    xlink:href CDATA #IMPLIED
>
```

---

# Part 3: Implementation Guidelines

## 3.1 Model Classes
각 Element/Attribute에 대응하는 Java 모델 클래스 생성.

```java
/**
 * DTD: <!ELEMENT MedlineCitation (...)>
 * Attributes: Status, Owner, IndexingMethod, VersionID, VersionDate
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedlineCitation {
    // Attributes
    private String status;      // Required
    private String owner;       // Default: "NLM"
    private String indexingMethod;
    private String versionId;
    private String versionDate;
    
    // Elements
    private PMID pmid;
    private PubMedDate dateCompleted;
    // ... 모든 하위 요소
}
```

## 3.2 Parser Structure
```java
public class PubmedXmlParser {
    
    private final XMLInputFactory factory;
    
    public PubmedXmlParser() {
        factory = XMLInputFactory.newInstance();
        // XXE 방지
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
    }
    
    // 전체 파싱
    public PubmedArticleSet parse(Path xmlPath) throws Exception { }
    
    // 스트리밍 파싱
    public void parseStream(Path xmlPath, 
                           Consumer<PubmedArticle> handler) throws Exception { }
}
```

## 3.3 Parsing Pattern
```java
private MedlineCitation parseMedlineCitation(XMLStreamReader reader) 
        throws XMLStreamException {
    
    var builder = MedlineCitation.builder();
    
    // 1. Attributes 먼저
    builder.status(reader.getAttributeValue(null, "Status"));
    builder.owner(reader.getAttributeValue(null, "Owner"));
    
    // 2. Child Elements
    while (reader.hasNext()) {
        int event = reader.next();
        
        if (event == XMLStreamConstants.START_ELEMENT) {
            switch (reader.getLocalName()) {
                case "PMID":
                    builder.pmid(parsePMID(reader));
                    break;
                case "Article":
                    builder.article(parseArticle(reader));
                    break;
                // ... 모든 자식 요소
            }
        } else if (event == XMLStreamConstants.END_ELEMENT
                && "MedlineCitation".equals(reader.getLocalName())) {
            break;
        }
    }
    
    return builder.build();
}
```

## 3.4 Mixed Content Handling
제목, 초록 등에 포함된 마크업 처리:

```java
private String getElementTextContent(XMLStreamReader reader, String endTag) 
        throws XMLStreamException {
    StringBuilder sb = new StringBuilder();
    int depth = 1;
    
    while (reader.hasNext() && depth > 0) {
        int event = reader.next();
        
        switch (event) {
            case XMLStreamConstants.CHARACTERS:
            case XMLStreamConstants.CDATA:
                sb.append(reader.getText());
                break;
            case XMLStreamConstants.START_ELEMENT:
                depth++;
                // 선택: 마크업 보존 또는 제거
                break;
            case XMLStreamConstants.END_ELEMENT:
                depth--;
                break;
        }
    }
    
    return sb.toString().trim();
}
```

## 3.5 GZip Support
```java
private InputStream openInputStream(Path path) throws IOException {
    InputStream is = Files.newInputStream(path);
    if (path.toString().toLowerCase().endsWith(".gz")) {
        is = new GZIPInputStream(is);
    }
    return new BufferedInputStream(is, 65536);
}
```

---

# Part 4: Model Class Checklist

## PubMed Models (필수)
- [ ] PubmedArticleSet
- [ ] PubmedArticle
- [ ] MedlineCitation (+ 모든 속성)
- [ ] Article (+ PubModel 속성)
- [ ] Journal
- [ ] JournalIssue (+ CitedMedium 속성)
- [ ] PubDate
- [ ] PMID (+ Version 속성)
- [ ] ISSN (+ IssnType 속성)
- [ ] Pagination
- [ ] ELocationID (+ EIdType, ValidYN 속성)
- [ ] Abstract
- [ ] AbstractText (+ Label, NlmCategory 속성)
- [ ] AuthorList (+ CompleteYN, Type 속성)
- [ ] Author (+ ValidYN, EqualContrib 속성)
- [ ] AffiliationInfo
- [ ] Identifier (+ Source 속성)
- [ ] DataBank
- [ ] Grant
- [ ] PublicationType (+ UI 속성)
- [ ] ArticleDate (+ DateType 속성)
- [ ] MedlineJournalInfo
- [ ] Chemical
- [ ] NameOfSubstance (+ UI 속성)
- [ ] SupplMeshName (+ Type, UI 속성)
- [ ] CommentsCorrections (+ RefType 속성)
- [ ] MeshHeading
- [ ] DescriptorName (+ UI, MajorTopicYN, Type 속성)
- [ ] QualifierName (+ UI, MajorTopicYN 속성)
- [ ] PersonalNameSubject
- [ ] OtherId (+ Source 속성)
- [ ] OtherAbstract (+ Type, Language 속성)
- [ ] KeywordList (+ Owner 속성)
- [ ] Keyword (+ MajorTopicYN 속성)
- [ ] Investigator (+ ValidYN 속성)
- [ ] GeneralNote (+ Owner 속성)
- [ ] PubmedData
- [ ] PubMedPubDate (+ PubStatus 속성)
- [ ] ArticleId (+ IdType 속성)
- [ ] PubmedObject (+ Type 속성)
- [ ] Param (+ Name 속성)
- [ ] ReferenceList
- [ ] Reference
- [ ] PubmedBookArticle
- [ ] BookDocument
- [ ] Book
- [ ] Publisher
- [ ] Sections
- [ ] Section
- [ ] LocationLabel (+ Type 속성)
- [ ] ItemList (+ ListType 속성)
- [ ] PubmedBookData
- [ ] DeleteCitation

## PMC Models (필수)
- [ ] PmcArticleSet
- [ ] PmcArticle
- [ ] Front
- [ ] JournalMeta
- [ ] JournalId
- [ ] ArticleMeta
- [ ] TitleGroup
- [ ] ContribGroup
- [ ] Contrib (+ 모든 속성)
- [ ] Aff
- [ ] PmcAbstract
- [ ] KwdGroup
- [ ] PmcPubDate
- [ ] Body
- [ ] Sec
- [ ] Back
- [ ] RefList
- [ ] Ref
- [ ] ElementCitation
- [ ] MixedCitation
- [ ] Fig
- [ ] TableWrap
- [ ] SupplementaryMaterial

---

# Part 5: Testing

## 5.1 Test Files
- PubMed: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/ 에서 샘플 다운로드
- PMC: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/ 에서 샘플 다운로드

## 5.2 Test Cases
```java
@Test
void shouldParseAllMedlineCitationAttributes() {
    // Status, Owner, IndexingMethod, VersionID, VersionDate 검증
}

@Test
void shouldParseAllArticleElements() {
    // Journal, ArticleTitle, Pagination, ELocationID, Abstract, 
    // AuthorList, Language, DataBankList, GrantList, 
    // PublicationTypeList, VernacularTitle, ArticleDate 검증
}

@Test
void shouldHandleDeleteCitation() {
    // 삭제된 PMID 목록 파싱 검증
}

@Test
void shouldStreamLargeFile() {
    // 메모리 효율성 검증
}
```

---

# Part 6: Common Pitfalls

## 6.1 빠뜨리기 쉬운 요소
1. `SupplMeshList` - 자주 누락됨
2. `CommentsCorrectionsList` - 정정/철회 정보
3. `SpaceFlightMission` - 우주비행 미션 관련
4. `GeneralNote` - 일반 노트
5. `OtherAbstract` - 다국어 초록
6. `PersonalNameSubjectList` - 인물 주제

## 6.2 빠뜨리기 쉬운 속성
1. `MedlineCitation.IndexingMethod`
2. `Author.EqualContrib`
3. `DescriptorName.Type` (Geographic)
4. `ELocationID.ValidYN`
5. `ArticleId.IdType`

## 6.3 특수 케이스
1. `MedlineDate` - "2024 Jan-Feb" 같은 비정형 날짜
2. `CollectiveName` - 단체 저자
3. Nested `ReferenceList` - 재귀 구조
4. Mixed content in titles/abstracts - 인라인 마크업

---

# Quick Start

```bash
# 1. 프로젝트 생성
mkdir -p pubmed-pmc-parser/src/main/java/com/bioxml/parser/{pubmed,pmc}/{model,parser}
mkdir -p pubmed-pmc-parser/src/test/java

# 2. 모델 클래스 먼저 생성 (Part 4 체크리스트 기준)

# 3. 파서 구현 (Part 3 가이드라인 기준)

# 4. 테스트 작성 (Part 5 기준)
```
