# SKILL: PubMed XML Parser (pubmed_240101.dtd)

## Overview
PubMed XML을 DTD 기준 100% 완전 파싱하는 Java 파서 개발 스킬.

**DTD Version**: pubmed_240101.dtd (2024년 1월 1일 기준)
**DTD URL**: https://dtd.nlm.nih.gov/ncbi/pubmed/out/pubmed_240101.dtd
**Documentation**: https://dtd.nlm.nih.gov/ncbi/pubmed/doc/out/240101/index.html

---

# Part 1: Root Element

## 1.1 PubmedArticleSet
```xml
<!ELEMENT PubmedArticleSet ((PubmedArticle | PubmedBookArticle)+, DeleteCitation?)>
```

**주의**: DeleteCitation은 FTP 업데이트 파일에서만 포함됨. E-Utilities API로는 조회 불가.

---

# Part 2: PubmedArticle Structure

## 2.1 PubmedArticle
```xml
<!ELEMENT PubmedArticle (MedlineCitation, PubmedData?)>
```

## 2.2 MedlineCitation (핵심)
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
    InvestigatorList*,
    GeneralNote*
)>

<!ATTLIST MedlineCitation
    Status (Completed | In-Process | PubMed-not-MEDLINE | 
            In-Data-Review | Publisher | MEDLINE | OLDMEDLINE) #REQUIRED
    Owner (NLM | NASA | PIP | KIE | HSR | HMD | NOTNLM) "NLM"
    IndexingMethod (Automated | Curated) #IMPLIED
    VersionID CDATA #IMPLIED
    VersionDate CDATA #IMPLIED
>
```

**2024년 변경사항**: 
- `InvestigatorList`가 반복 가능하게 변경 (기존: 0-1개 → 변경: 0-N개)
- `CollectiveName`에 `Investigators` 속성 추가

---

# Part 3: Article Elements

## 3.1 Article
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
    PubModel (Print | Print-Electronic | Electronic | 
              Electronic-Print | Electronic-eCollection) #REQUIRED
>
```

## 3.2 Journal
```xml
<!ELEMENT Journal (ISSN?, JournalIssue, Title?, ISOAbbreviation?)>
```

## 3.3 JournalIssue
```xml
<!ELEMENT JournalIssue (Volume?, Issue?, PubDate)>

<!ATTLIST JournalIssue
    CitedMedium (Internet | Print) #REQUIRED
>
```

## 3.4 PubDate
```xml
<!ELEMENT PubDate ((Year, ((Month, Day?) | Season)?) | MedlineDate)>
```

**주의**: `MedlineDate`는 "2024 Jan-Feb", "2024 Spring" 같은 비정형 날짜 처리용

## 3.5 PMID
```xml
<!ELEMENT PMID (#PCDATA)>

<!ATTLIST PMID 
    Version CDATA "1"
>
```

## 3.6 ISSN
```xml
<!ELEMENT ISSN (#PCDATA)>

<!ATTLIST ISSN 
    IssnType (Electronic | Print) #REQUIRED
>
```

## 3.7 ArticleTitle
```xml
<!ELEMENT ArticleTitle (%text; | mml:math)*>

<!ATTLIST ArticleTitle
    book CDATA #IMPLIED
    part CDATA #IMPLIED
    sec CDATA #IMPLIED
>
```

**주의**: Mixed content - 인라인 마크업(sup, sub, b, i 등) 포함 가능

## 3.8 Pagination
```xml
<!ELEMENT Pagination ((StartPage, EndPage?, MedlinePgn?) | MedlinePgn)>
<!ELEMENT StartPage (#PCDATA)>
<!ELEMENT EndPage (#PCDATA)>
<!ELEMENT MedlinePgn (#PCDATA)>
```

## 3.9 ELocationID
```xml
<!ELEMENT ELocationID (#PCDATA)>

<!ATTLIST ELocationID
    EIdType (doi | pii) #REQUIRED
    ValidYN (Y | N) "Y"
>
```

---

# Part 4: Abstract Elements

## 4.1 Abstract
```xml
<!ELEMENT Abstract (AbstractText+, CopyrightInformation?)>
```

## 4.2 AbstractText
```xml
<!ELEMENT AbstractText (%text; | mml:math)*>

<!ATTLIST AbstractText
    Label CDATA #IMPLIED
    NlmCategory (BACKGROUND | OBJECTIVE | METHODS | RESULTS | 
                 CONCLUSIONS | UNASSIGNED) #IMPLIED
>
```

**예시**:
```xml
<Abstract>
    <AbstractText Label="BACKGROUND" NlmCategory="BACKGROUND">
        Structured abstract background...
    </AbstractText>
    <AbstractText Label="METHODS" NlmCategory="METHODS">
        Structured abstract methods...
    </AbstractText>
</Abstract>
```

## 4.3 OtherAbstract
```xml
<!ELEMENT OtherAbstract (AbstractText+, CopyrightInformation?)>

<!ATTLIST OtherAbstract
    Type (AAMC | AIDS | KIE | PIP | NASA | Publisher) #REQUIRED
    Language CDATA "eng"
>
```

---

# Part 5: Author Elements

## 5.1 AuthorList
```xml
<!ELEMENT AuthorList (Author+)>

<!ATTLIST AuthorList
    CompleteYN (Y | N) "Y"
    Type (authors | editors) #IMPLIED
>
```

## 5.2 Author
```xml
<!ELEMENT Author (
    ((LastName, ForeName?, Initials?, Suffix?) | CollectiveName),
    Identifier*,
    AffiliationInfo*
)>

<!ATTLIST Author
    ValidYN (Y | N) "Y"
    EqualContrib (Y | N) #IMPLIED
>
```

## 5.3 CollectiveName
```xml
<!ELEMENT CollectiveName (%text;)*>

<!ATTLIST CollectiveName
    Investigators IDREF #IMPLIED
>
```

**2024년 추가**: `Investigators` 속성으로 InvestigatorList 연결 지원

## 5.4 Name Components
```xml
<!ELEMENT LastName (#PCDATA)>
<!ELEMENT ForeName (#PCDATA)>
<!ELEMENT Initials (#PCDATA)>
<!ELEMENT Suffix (#PCDATA)>
```

## 5.5 AffiliationInfo
```xml
<!ELEMENT AffiliationInfo (Affiliation, Identifier*)>
<!ELEMENT Affiliation (%text;)*>
```

## 5.6 Identifier
```xml
<!ELEMENT Identifier (#PCDATA)>

<!ATTLIST Identifier 
    Source CDATA #REQUIRED
>
```

**예시**: `<Identifier Source="ORCID">0000-0001-2345-6789</Identifier>`

---

# Part 6: Data & Grant Elements

## 6.1 DataBankList
```xml
<!ELEMENT DataBankList (DataBank+)>

<!ATTLIST DataBankList 
    CompleteYN (Y | N) "Y"
>

<!ELEMENT DataBank (DataBankName, AccessionNumberList?)>
<!ELEMENT DataBankName (#PCDATA)>
<!ELEMENT AccessionNumberList (AccessionNumber+)>
<!ELEMENT AccessionNumber (#PCDATA)>
```

## 6.2 GrantList
```xml
<!ELEMENT GrantList (Grant+)>

<!ATTLIST GrantList 
    CompleteYN (Y | N) "Y"
>

<!ELEMENT Grant (GrantID?, Acronym?, Agency, Country?)>
<!ELEMENT GrantID (#PCDATA)>
<!ELEMENT Acronym (#PCDATA)>
<!ELEMENT Agency (%text;)*>
<!ELEMENT Country (#PCDATA)>
```

**2024년 변경**: `Country`가 선택적으로 변경 (기존: 필수 → 변경: 선택)

---

# Part 7: Publication Type & Date

## 7.1 PublicationTypeList
```xml
<!ELEMENT PublicationTypeList (PublicationType+)>

<!ELEMENT PublicationType (#PCDATA)>

<!ATTLIST PublicationType 
    UI CDATA #REQUIRED
>
```

## 7.2 ArticleDate
```xml
<!ELEMENT ArticleDate (Year, Month, Day)>

<!ATTLIST ArticleDate 
    DateType CDATA #REQUIRED
>
```

## 7.3 Date Components
```xml
<!ELEMENT Year (#PCDATA)>
<!ELEMENT Month (#PCDATA)>
<!ELEMENT Day (#PCDATA)>
<!ELEMENT Season (#PCDATA)>
<!ELEMENT MedlineDate (#PCDATA)>

<!ELEMENT DateCompleted (Year, Month, Day)>
<!ELEMENT DateRevised (Year, Month, Day)>
```

---

# Part 8: Journal Info

## 8.1 MedlineJournalInfo
```xml
<!ELEMENT MedlineJournalInfo (Country?, MedlineTA, NlmUniqueID?, ISSNLinking?)>

<!ELEMENT Country (#PCDATA)>
<!ELEMENT MedlineTA (#PCDATA)>
<!ELEMENT NlmUniqueID (#PCDATA)>
<!ELEMENT ISSNLinking (#PCDATA)>
```

---

# Part 9: MeSH & Chemical

## 9.1 ChemicalList
```xml
<!ELEMENT ChemicalList (Chemical+)>

<!ELEMENT Chemical (RegistryNumber, NameOfSubstance)>
<!ELEMENT RegistryNumber (#PCDATA)>

<!ELEMENT NameOfSubstance (#PCDATA)>

<!ATTLIST NameOfSubstance 
    UI CDATA #REQUIRED
>
```

## 9.2 SupplMeshList
```xml
<!ELEMENT SupplMeshList (SupplMeshName+)>

<!ELEMENT SupplMeshName (#PCDATA)>

<!ATTLIST SupplMeshName
    Type (Disease | Protocol | Organism) #REQUIRED
    UI CDATA #REQUIRED
>
```

## 9.3 MeshHeadingList
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

---

# Part 10: Comments & Corrections

## 10.1 CommentsCorrectionsList
```xml
<!ELEMENT CommentsCorrectionsList (CommentsCorrections+)>

<!ELEMENT CommentsCorrections (RefSource, PMID?, Note?)>

<!ATTLIST CommentsCorrections
    RefType (
        AssociatedDataset | AssociatedPublication |
        CommentIn | CommentOn |
        CorrectedandRepublishedIn | CorrectedandRepublishedFrom |
        ErratumIn | ErratumFor |
        ExpressionOfConcernIn | ExpressionOfConcernFor |
        RepublishedIn | RepublishedFrom |
        RetractedandRepublishedIn | RetractedandRepublishedFrom |
        RetractionIn | RetractionOf |
        UpdateIn | UpdateOf |
        Cites
    ) #REQUIRED
>

<!ELEMENT RefSource (#PCDATA)>
<!ELEMENT Note (#PCDATA)>
```

---

# Part 11: Keywords & Investigators

## 11.1 KeywordList
```xml
<!ELEMENT KeywordList (Keyword+)>

<!ATTLIST KeywordList 
    Owner (NLM | NLM-AUTO | NASA | PIP | KIE | NOTNLM | HHS) "NLM"
>

<!ELEMENT Keyword (%text;)*>

<!ATTLIST Keyword 
    MajorTopicYN (Y | N) "N"
>
```

## 11.2 InvestigatorList
```xml
<!ELEMENT InvestigatorList (Investigator+)>

<!ATTLIST InvestigatorList
    ID ID #IMPLIED
>

<!ELEMENT Investigator (LastName, ForeName?, Initials?, Suffix?, Identifier*, AffiliationInfo*)>

<!ATTLIST Investigator 
    ValidYN (Y | N) "Y"
>
```

**2024년 추가**: `InvestigatorList`에 `ID` 속성 추가 (CollectiveName.Investigators와 연결용)

---

# Part 12: Other Elements

## 12.1 GeneSymbolList
```xml
<!ELEMENT GeneSymbolList (GeneSymbol+)>
<!ELEMENT GeneSymbol (#PCDATA)>
```

## 12.2 PersonalNameSubjectList
```xml
<!ELEMENT PersonalNameSubjectList (PersonalNameSubject+)>
<!ELEMENT PersonalNameSubject (LastName, ForeName?, Initials?, Suffix?)>
```

## 12.3 OtherID
```xml
<!ELEMENT OtherID (#PCDATA)>

<!ATTLIST OtherID 
    Source (NASA | KIE | PIP | POP | ARPL | CPC | IND | 
            CPFH | CLML | NRCBL | NLM | QCIM) #REQUIRED
>
```

## 12.4 GeneralNote
```xml
<!ELEMENT GeneralNote (#PCDATA)>

<!ATTLIST GeneralNote 
    Owner (NLM | NASA | PIP | KIE | HSR | HMD) "NLM"
>
```

## 12.5 CitationSubset
```xml
<!ELEMENT CitationSubset (#PCDATA)>
```

## 12.6 NumberOfReferences
```xml
<!ELEMENT NumberOfReferences (#PCDATA)>
```

## 12.7 SpaceFlightMission
```xml
<!ELEMENT SpaceFlightMission (#PCDATA)>
```

## 12.8 CoiStatement
```xml
<!ELEMENT CoiStatement (%text;)*>
```

---

# Part 13: PubmedData

## 13.1 PubmedData
```xml
<!ELEMENT PubmedData (History?, PublicationStatus, ArticleIdList, ObjectList?, ReferenceList*)>
```

## 13.2 History
```xml
<!ELEMENT History (PubMedPubDate+)>

<!ELEMENT PubMedPubDate (Year, Month, Day, (Hour, (Minute, Second?)?)?)>

<!ATTLIST PubMedPubDate
    PubStatus (received | accepted | epublish | ppublish | revised |
               aheadofprint | retracted | ecollection | pmc | pmcr |
               pubmed | pubmedr | premedline | medline | medliner |
               entrez | pmc-release) #REQUIRED
>

<!ELEMENT Hour (#PCDATA)>
<!ELEMENT Minute (#PCDATA)>
<!ELEMENT Second (#PCDATA)>
```

## 13.3 PublicationStatus
```xml
<!ELEMENT PublicationStatus (#PCDATA)>
```

## 13.4 ArticleIdList
```xml
<!ELEMENT ArticleIdList (ArticleId+)>

<!ELEMENT ArticleId (#PCDATA)>

<!ATTLIST ArticleId 
    IdType (doi | pii | pmcpid | pmpid | pmc | mid | sici | 
            pubmed | medline | pmcid | pmcbook) "pubmed"
>
```

## 13.5 ObjectList
```xml
<!ELEMENT ObjectList (Object+)>

<!ELEMENT Object (Param*)>

<!ATTLIST Object 
    Type CDATA #REQUIRED
>

<!ELEMENT Param (#PCDATA)>

<!ATTLIST Param 
    Name CDATA #REQUIRED
>
```

## 13.6 ReferenceList
```xml
<!ELEMENT ReferenceList (Title?, Reference*, ReferenceList*)>

<!ELEMENT Reference (Citation, ArticleIdList?)>

<!ELEMENT Citation (%text;)*>

<!ELEMENT Title (#PCDATA)>
```

**주의**: ReferenceList는 재귀 구조 (중첩 가능)

---

# Part 14: PubmedBookArticle

## 14.1 PubmedBookArticle
```xml
<!ELEMENT PubmedBookArticle (BookDocument, PubmedBookData?)>
```

## 14.2 BookDocument
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

## 14.3 Book
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
<!ELEMENT PublisherName (%text;)*>
<!ELEMENT PublisherLocation (#PCDATA)>
<!ELEMENT BookTitle (%text; | mml:math)*>
<!ELEMENT VolumeTitle (#PCDATA)>
<!ELEMENT Edition (#PCDATA)>
<!ELEMENT CollectionTitle (%text; | mml:math)*>
<!ELEMENT Isbn (#PCDATA)>
<!ELEMENT Medium (#PCDATA)>
<!ELEMENT ReportNumber (#PCDATA)>
<!ELEMENT BeginningDate (Year, ((Month, Day?) | Season)?)>
<!ELEMENT EndingDate (Year, ((Month, Day?) | Season)?)>
```

## 14.4 Sections
```xml
<!ELEMENT Sections (Section+)>

<!ELEMENT Section (
    LocationLabel?,
    SectionTitle?,
    Section*
)>

<!ELEMENT LocationLabel (#PCDATA)>

<!ATTLIST LocationLabel 
    Type (part | chapter | section | appendix) #IMPLIED
>

<!ELEMENT SectionTitle (%text;)*>
```

## 14.5 ItemList
```xml
<!ELEMENT ItemList (Item+)>

<!ATTLIST ItemList 
    ListType CDATA #REQUIRED
>

<!ELEMENT Item (%text;)*>
```

## 14.6 PubmedBookData
```xml
<!ELEMENT PubmedBookData (History?, PublicationStatus, ArticleIdList, ObjectList?)>
```

---

# Part 15: DeleteCitation

## 15.1 DeleteCitation
```xml
<!ELEMENT DeleteCitation (PMID+)>
```

**주의**: FTP 업데이트 파일에서만 포함됨. 삭제된 PMID 목록.

---

# Part 16: Text Content Model

## 16.1 %text; Entity
```xml
<!ENTITY % text "#PCDATA | b | i | u | sup | sub">
```

인라인 마크업 요소:
```xml
<!ELEMENT b (%text;)*>        <!-- Bold -->
<!ELEMENT i (%text;)*>        <!-- Italic -->
<!ELEMENT u (%text;)*>        <!-- Underline -->
<!ELEMENT sup (%text;)*>      <!-- Superscript -->
<!ELEMENT sub (%text;)*>      <!-- Subscript -->
```

---

# Part 17: Model Class Checklist

## Required Models

### Core Models
- [ ] PubmedArticleSet
- [ ] PubmedArticle
- [ ] MedlineCitation (+ Status, Owner, IndexingMethod, VersionID, VersionDate)
- [ ] Article (+ PubModel)
- [ ] Journal
- [ ] JournalIssue (+ CitedMedium)
- [ ] PubDate
- [ ] MedlineDate
- [ ] PMID (+ Version)
- [ ] ISSN (+ IssnType)

### Article Content
- [ ] ArticleTitle (+ book, part, sec)
- [ ] Pagination (StartPage, EndPage, MedlinePgn)
- [ ] ELocationID (+ EIdType, ValidYN)
- [ ] Abstract
- [ ] AbstractText (+ Label, NlmCategory)
- [ ] CopyrightInformation
- [ ] VernacularTitle
- [ ] Language

### Author/Investigator
- [ ] AuthorList (+ CompleteYN, Type)
- [ ] Author (+ ValidYN, EqualContrib)
- [ ] LastName, ForeName, Initials, Suffix
- [ ] CollectiveName (+ Investigators)
- [ ] AffiliationInfo
- [ ] Affiliation
- [ ] Identifier (+ Source)
- [ ] InvestigatorList (+ ID)
- [ ] Investigator (+ ValidYN)

### Data & Grants
- [ ] DataBankList (+ CompleteYN)
- [ ] DataBank
- [ ] DataBankName
- [ ] AccessionNumberList
- [ ] AccessionNumber
- [ ] GrantList (+ CompleteYN)
- [ ] Grant
- [ ] GrantID, Acronym, Agency, Country

### Publication Info
- [ ] PublicationTypeList
- [ ] PublicationType (+ UI)
- [ ] ArticleDate (+ DateType)
- [ ] MedlineJournalInfo
- [ ] MedlineTA, NlmUniqueID, ISSNLinking

### MeSH & Chemicals
- [ ] ChemicalList
- [ ] Chemical (RegistryNumber, NameOfSubstance)
- [ ] NameOfSubstance (+ UI)
- [ ] SupplMeshList
- [ ] SupplMeshName (+ Type, UI)
- [ ] MeshHeadingList
- [ ] MeshHeading
- [ ] DescriptorName (+ UI, MajorTopicYN, Type)
- [ ] QualifierName (+ UI, MajorTopicYN)

### Comments/Corrections
- [ ] CommentsCorrectionsList
- [ ] CommentsCorrections (+ RefType)
- [ ] RefSource, Note

### Keywords & Others
- [ ] KeywordList (+ Owner)
- [ ] Keyword (+ MajorTopicYN)
- [ ] GeneSymbolList, GeneSymbol
- [ ] PersonalNameSubjectList, PersonalNameSubject
- [ ] OtherID (+ Source)
- [ ] OtherAbstract (+ Type, Language)
- [ ] GeneralNote (+ Owner)
- [ ] CitationSubset
- [ ] NumberOfReferences
- [ ] SpaceFlightMission
- [ ] CoiStatement

### PubmedData
- [ ] PubmedData
- [ ] History
- [ ] PubMedPubDate (+ PubStatus)
- [ ] Hour, Minute, Second
- [ ] PublicationStatus
- [ ] ArticleIdList
- [ ] ArticleId (+ IdType)
- [ ] ObjectList
- [ ] Object (+ Type)
- [ ] Param (+ Name)
- [ ] ReferenceList (재귀)
- [ ] Reference
- [ ] Citation

### Book Models
- [ ] PubmedBookArticle
- [ ] BookDocument
- [ ] Book
- [ ] Publisher (PublisherName, PublisherLocation)
- [ ] BookTitle
- [ ] BeginningDate, EndingDate
- [ ] VolumeTitle, Edition, CollectionTitle
- [ ] Isbn, Medium, ReportNumber
- [ ] Sections
- [ ] Section
- [ ] LocationLabel (+ Type)
- [ ] SectionTitle
- [ ] ItemList (+ ListType)
- [ ] Item
- [ ] PubmedBookData
- [ ] DeleteCitation

### Inline Markup
- [ ] TextContent (b, i, u, sup, sub)

---

# Part 18: Common Pitfalls

## 18.1 자주 누락되는 요소
1. `SupplMeshList` - Supplementary MeSH 용어
2. `CommentsCorrectionsList` - 정정/철회/코멘트 정보
3. `SpaceFlightMission` - 우주비행 미션 관련 논문
4. `GeneralNote` - 일반 노트
5. `OtherAbstract` - 다국어 초록
6. `PersonalNameSubjectList` - 인물 주제
7. `GeneSymbolList` - 유전자 기호 목록
8. `CoiStatement` - 이해충돌 성명

## 18.2 자주 누락되는 속성
1. `MedlineCitation.IndexingMethod` - Automated/Curated
2. `Author.EqualContrib` - 동등 기여자 표시
3. `DescriptorName.Type` - Geographic 타입
4. `ELocationID.ValidYN` - 유효성 표시
5. `ArticleId.IdType` - 기본값 "pubmed" 주의
6. `CollectiveName.Investigators` - InvestigatorList 연결 (2024 신규)
7. `InvestigatorList.ID` - CollectiveName 연결용 (2024 신규)

## 18.3 특수 케이스
1. **MedlineDate**: "2024 Jan-Feb", "2024 Spring" 같은 비정형 날짜
2. **CollectiveName**: 단체 저자 (개인 저자와 구조 다름)
3. **Nested ReferenceList**: 재귀 구조 처리 필요
4. **Mixed content**: ArticleTitle, AbstractText 등에 인라인 마크업
5. **DeleteCitation**: FTP 업데이트 파일 전용
6. **PubmedBookArticle**: 일반 Article과 구조 상이

## 18.4 데이터 소스별 차이
- **FTP Baseline/Update**: DeleteCitation 포함
- **E-Utilities API**: DeleteCitation 미포함, Book 가능
- **Web Interface**: DeleteCitation 미포함, Book 가능

---

# Part 19: Data Sources

## FTP Download
- **Baseline**: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/
- **Updates**: https://ftp.ncbi.nlm.nih.gov/pubmed/updatefiles/
- **Terms**: https://ftp.ncbi.nlm.nih.gov/pubmed/baseline/README.txt

## File Integrity: MD5 Checksum (필수)
PubMed FTP는 각 XML 파일에 대해 `.md5` 체크섬 파일을 제공합니다.

```
pubmed25n0001.xml.gz       (약 17-20MB)
pubmed25n0001.xml.gz.md5   (60 bytes)
```

**.md5 파일 형식:**
```
MD5(pubmed25n0001.xml.gz)= d41d8cd98f00b204e9800998ecf8427e
```

**⚠️ 주의사항:**
- 다운로드 후 반드시 MD5 검증 수행
- 검증 실패 시 재다운로드 필요
- README.txt: "Please use the .md5 file to verify the integrity of the XML"

**검증 방법 (Linux/Mac):**
```bash
# 단일 파일 검증
md5sum -c pubmed25n0001.xml.gz.md5

# 전체 배치 검증
for f in *.xml.gz; do
    md5sum -c "${f}.md5" || echo "FAILED: $f"
done
```

**Java 구현 예시:** SKILL-COMMON.md Part 9.4 참조

## API Access
- **E-Utilities**: https://www.ncbi.nlm.nih.gov/books/NBK25501/

---

# Part 20: Version History

| Version | Date | Key Changes |
|---------|------|-------------|
| pubmed_240101 | 2024-01-01 | InvestigatorList 반복 가능, Grant.Country 선택적, CollectiveName.Investigators 추가 |
| pubmed_230101 | 2023-01-01 | 이전 버전 |

---

# Quick Reference

```java
// 필수 파싱 순서
1. PubmedArticleSet (root)
2. PubmedArticle | PubmedBookArticle | DeleteCitation
3. MedlineCitation → Article → 하위 요소들
4. PubmedData (선택적)

// 속성 기본값 주의
Author.ValidYN = "Y"
PMID.Version = "1"
MedlineCitation.Owner = "NLM"
KeywordList.Owner = "NLM"
ArticleId.IdType = "pubmed"
GrantList.CompleteYN = "Y"
DataBankList.CompleteYN = "Y"
AuthorList.CompleteYN = "Y"
```
