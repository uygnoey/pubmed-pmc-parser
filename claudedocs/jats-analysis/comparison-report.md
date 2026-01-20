# JATS 1.4 vs 현재 PMC 모델 비교 리포트

## 📊 요약 / Summary

- **JATS 1.4 총 Element 개수**: 311
- **현재 구현된 모델 개수**: 245
- **일치하는 모델**: 205 (65.9%)
- **누락된 모델**: 106 (34.1%)
- **DTD에 없는 추가 모델**: 40

## ❌ 누락된 모델 (JATS에는 있지만 구현 안됨)

총 106개의 모델이 누락되었습니다.


### A

- `Abbrev` (JATS: `<abbrev>`)
- `AccessDate` (JATS: `<access-date>`)
- `AddrLine` (JATS: `<addr-line>`)
- `AliFreeToRead` (JATS: `<ali-free-to-read>`)
- `AliLicenseRef` (JATS: `<ali-license-ref>`)
- `Answer` (JATS: `<answer>`)
- `AnswerSet` (JATS: `<answer-set>`)
- `Article` (JATS: `<article>`)
- `ArticleVersion` (JATS: `<article-version>`)
- `ArticleVersionAlternatives` (JATS: `<article-version-alternatives>`)
- `AwardDesc` (JATS: `<award-desc>`)
- `AwardName` (JATS: `<award-name>`)

### B

- `BlockAlternatives` (JATS: `<block-alternatives>`)
- `Break` (JATS: `<break>`)

### C

- `CitationAlternatives` (JATS: `<citation-alternatives>`)
- `City` (JATS: `<city>`)
- `CollabName` (JATS: `<collab-name>`)
- `CollabNameAlternatives` (JATS: `<collab-name-alternatives>`)
- `CollabWrap` (JATS: `<collab-wrap>`)
- `ConfAcronym` (JATS: `<conf-acronym>`)
- `ConfNum` (JATS: `<conf-num>`)
- `ConfTheme` (JATS: `<conf-theme>`)
- `ContentLanguage` (JATS: `<content-language>`)
- `ContributedResourceGroup` (JATS: `<contributed-resource-group>`)
- `Count` (JATS: `<count>`)
- `Country` (JATS: `<country>`)

### D

- `Date` (JATS: `<date>`)
- `DefHead` (JATS: `<def-head>`)
- `DispFormula` (JATS: `<disp-formula>`)
- `DispFormulaGroup` (JATS: `<disp-formula-group>`)

### E

- `EquationCount` (JATS: `<equation-count>`)
- `EventDesc` (JATS: `<event-desc>`)
- `Explanation` (JATS: `<explanation>`)
- `ExtendedBy` (JATS: `<extended-by>`)

### F

- `Fax` (JATS: `<fax>`)
- `FigCount` (JATS: `<fig-count>`)
- `FixedCase` (JATS: `<fixed-case>`)

### G

- `GlyphData` (JATS: `<glyph-data>`)
- `GlyphRef` (JATS: `<glyph-ref>`)
- `Gov` (JATS: `<gov>`)

### H

- `Hr` (JATS: `<hr>`)

### I

- `IndexTerm` (JATS: `<index-term>`)
- `IndexTermRangeEnd` (JATS: `<index-term-range-end>`)
- `InlineFormula` (JATS: `<inline-formula>`)
- `InlineMedia` (JATS: `<inline-media>`)
- `Isbn` (JATS: `<isbn>`)
- `IssnL` (JATS: `<issn-l>`)
- `IssueSubtitle` (JATS: `<issue-subtitle>`)
- `IssueTitleGroup` (JATS: `<issue-title-group>`)

### L

- `Legend` (JATS: `<legend>`)
- `LicenseP` (JATS: `<license-p>`)
- `List` (JATS: `<list>`)

### M

- `MilestoneEnd` (JATS: `<milestone-end>`)
- `MilestoneStart` (JATS: `<milestone-start>`)
- `MmlMath` (JATS: `<mml-math>`)

### N

- `NamedContent` (JATS: `<named-content>`)

### O

- `OpenAccess` (JATS: `<open-access>`)
- `Option` (JATS: `<option>`)
- `OverlineEnd` (JATS: `<overline-end>`)
- `OverlineStart` (JATS: `<overline-start>`)

### P

- `Phone` (JATS: `<phone>`)
- `PostalCode` (JATS: `<postal-code>`)
- `Price` (JATS: `<price>`)
- `PrincipalInvestigator` (JATS: `<principal-investigator>`)
- `PrivateChar` (JATS: `<private-char>`)
- `ProcessingMeta` (JATS: `<processing-meta>`)
- `PubDateNotAvailable` (JATS: `<pub-date-not-available>`)

### Q

- `Question` (JATS: `<question>`)
- `QuestionPreamble` (JATS: `<question-preamble>`)
- `QuestionWrap` (JATS: `<question-wrap>`)
- `QuestionWrapGroup` (JATS: `<question-wrap-group>`)

### R

- `Rb` (JATS: `<rb>`)
- `RefCount` (JATS: `<ref-count>`)
- `ResourceGroup` (JATS: `<resource-group>`)
- `ResourceId` (JATS: `<resource-id>`)
- `ResourceName` (JATS: `<resource-name>`)
- `ResourceWrap` (JATS: `<resource-wrap>`)
- `RestrictedBy` (JATS: `<restricted-by>`)
- `Rp` (JATS: `<rp>`)
- `Rt` (JATS: `<rt>`)
- `Ruby` (JATS: `<ruby>`)

### S

- `See` (JATS: `<see>`)
- `SeeAlso` (JATS: `<see-also>`)
- `Sig` (JATS: `<sig>`)
- `SigBlock` (JATS: `<sig-block>`)
- `Speaker` (JATS: `<speaker>`)
- `Speech` (JATS: `<speech>`)
- `State` (JATS: `<state>`)
- `Statement` (JATS: `<statement>`)
- `Std` (JATS: `<std>`)
- `StdOrganization` (JATS: `<std-organization>`)
- `StringConf` (JATS: `<string-conf>`)
- `StyledContent` (JATS: `<styled-content>`)
- `SupportDescription` (JATS: `<support-description>`)

### T

- `TableCount` (JATS: `<table-count>`)
- `Target` (JATS: `<target>`)
- `TermHead` (JATS: `<term-head>`)
- `TimeStamp` (JATS: `<time-stamp>`)

### U

- `UnderlineEnd` (JATS: `<underline-end>`)
- `UnderlineStart` (JATS: `<underline-start>`)
- `UnstructuredKwdGroup` (JATS: `<unstructured-kwd-group>`)

### V

- `VerseGroup` (JATS: `<verse-group>`)
- `VerseLine` (JATS: `<verse-line>`)
- `VolumeIssueGroup` (JATS: `<volume-issue-group>`)

### W

- `WordCount` (JATS: `<word-count>`)

### X

- `X` (JATS: `<x>`)


## ➕ DTD에 없는 추가 모델 (구현되었지만 JATS 1.4에 없음)

총 40개의 추가 모델이 있습니다.

**주의**: 이들은 이전 버전 호환성, PMC 특화 요소, 또는 enum 타입일 수 있습니다.


### A

- `ArticleType`

### C

- `CellAlign`
- `CellValign`
- `ContribIdType`

### F

- `FigType`
- `FnType`

### G

- `GlossaryEntry`

### I

- `InstitutionIdType`

### J

- `JatsArticle`
- `JournalIdType`

### N

- `NameStyle`

### O

- `Orientation`

### P

- `PersonGroupType`
- `PersonName`
- `PmcAbstract`
- `PmcArticle`
- `PmcArticleId`
- `PmcArticleSet`
- `PmcArticleTitle`
- `PmcDate`
- `PmcHistory`
- `PmcIsbn`
- `PmcIssn`
- `PmcIssue`
- `PmcList`
- `PmcPubDate`
- `PmcPublisher`
- `PmcSuffix`
- `Position`
- `PubIdType`
- `PubType`
- `PublicationFormat`
- `PublicationType`

### R

- `ResponseType`

### T

- `TableCell`
- `TableFrame`
- `TableRow`
- `TableRules`

### X

- `XlinkActuate`
- `XlinkShow`


## ✅ 이미 구현된 모델 (205개)

이 모델들은 JATS 1.4와 일치합니다.

<details>
<summary>구현된 모델 목록 보기 (처음 50개)</summary>

- `AbbrevJournalTitle` (`<abbrev-journal-title>`)
- `Abstract` (`<abstract>`)
- `Ack` (`<ack>`)
- `Address` (`<address>`)
- `Aff` (`<aff>`)
- `AffAlternatives` (`<aff-alternatives>`)
- `AltText` (`<alt-text>`)
- `AltTitle` (`<alt-title>`)
- `Alternatives` (`<alternatives>`)
- `Annotation` (`<annotation>`)
- `Anonymous` (`<anonymous>`)
- `App` (`<app>`)
- `AppGroup` (`<app-group>`)
- `Array` (`<array>`)
- `ArticleCategories` (`<article-categories>`)
- `ArticleId` (`<article-id>`)
- `ArticleMeta` (`<article-meta>`)
- `ArticleTitle` (`<article-title>`)
- `Attrib` (`<attrib>`)
- `AuthorComment` (`<author-comment>`)
- `AuthorNotes` (`<author-notes>`)
- `AwardGroup` (`<award-group>`)
- `AwardId` (`<award-id>`)
- `Back` (`<back>`)
- `Bio` (`<bio>`)
- `Body` (`<body>`)
- `Bold` (`<bold>`)
- `BoxedText` (`<boxed-text>`)
- `Caption` (`<caption>`)
- `ChapterTitle` (`<chapter-title>`)
- `ChemStruct` (`<chem-struct>`)
- `ChemStructWrap` (`<chem-struct-wrap>`)
- `Code` (`<code>`)
- `Col` (`<col>`)
- `Colgroup` (`<colgroup>`)
- `Collab` (`<collab>`)
- `CollabAlternatives` (`<collab-alternatives>`)
- `Comment` (`<comment>`)
- `CompoundKwd` (`<compound-kwd>`)
- `CompoundKwdPart` (`<compound-kwd-part>`)
- `CompoundSubject` (`<compound-subject>`)
- `CompoundSubjectPart` (`<compound-subject-part>`)
- `ConfDate` (`<conf-date>`)
- `ConfLoc` (`<conf-loc>`)
- `ConfName` (`<conf-name>`)
- `ConfSponsor` (`<conf-sponsor>`)
- `Conference` (`<conference>`)
- `Contrib` (`<contrib>`)
- `ContribGroup` (`<contrib-group>`)
- `ContribId` (`<contrib-id>`)

... 외 155개

</details>


## 🎯 Action Items

### 우선순위 1: 핵심 누락 모델

다음 핵심 element들이 누락되어 있습니다:

- [ ] `LicenseP` (`<license-p>`)
- [ ] `OpenAccess` (`<open-access>`)
- [ ] `DispFormula` (`<disp-formula>`)
- [ ] `DispFormulaGroup` (`<disp-formula-group>`)
- [ ] `InlineFormula` (`<inline-formula>`)
- [ ] `Speech` (`<speech>`)
- [ ] `Speaker` (`<speaker>`)
- [ ] `Question` (`<question>`)
- [ ] `Answer` (`<answer>`)
- [ ] `ProcessingMeta` (`<processing-meta>`)
- [ ] `ArticleVersion` (`<article-version>`)
- [ ] `ArticleVersionAlternatives` (`<article-version-alternatives>`)

### 우선순위 2: 컨텐츠 관련 누락 모델

- [ ] `AwardDesc` (`<award-desc>`)
- [ ] `AwardName` (`<award-name>`)
- [ ] `CitationAlternatives` (`<citation-alternatives>`)
- [ ] `ContributedResourceGroup` (`<contributed-resource-group>`)
- [ ] `DispFormula` (`<disp-formula>`)
- [ ] `DispFormulaGroup` (`<disp-formula-group>`)
- [ ] `IndexTerm` (`<index-term>`)
- [ ] `IndexTermRangeEnd` (`<index-term-range-end>`)
- [ ] `InlineFormula` (`<inline-formula>`)
- [ ] `MilestoneEnd` (`<milestone-end>`)
- [ ] `MilestoneStart` (`<milestone-start>`)
- [ ] `Question` (`<question>`)
- [ ] `QuestionPreamble` (`<question-preamble>`)
- [ ] `QuestionWrap` (`<question-wrap>`)
- [ ] `QuestionWrapGroup` (`<question-wrap-group>`)
- [ ] `VerseGroup` (`<verse-group>`)
- [ ] `VerseLine` (`<verse-line>`)

### 우선순위 3: 나머지 누락 모델

나머지 77개의 모델은 필요시 구현
