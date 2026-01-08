# SKILL: PMC/JATS XML Parser (NISO JATS 1.4)

## Overview
PMC(PubMed Central) Full-text XML을 JATS DTD 기준 100% 완전 파싱하는 Java 파서 개발 스킬.

**Standard**: ANSI/NISO Z39.96-2024 (JATS 1.4)
**DTD URL**: https://jats.nlm.nih.gov/archiving/
**Tag Library**: https://jats.nlm.nih.gov/archiving/tag-library/1.4/
**PMC Tagging Guidelines**: https://www.ncbi.nlm.nih.gov/pmc/pmcdoc/tagging-guidelines/article/style.html

---

# Part 1: Article Root Structure

## 1.1 Article (Root Element)
```xml
<!ELEMENT article (
    front,
    body?,
    back?,
    floats-group?,
    (sub-article* | response*)
)>

<!ATTLIST article
    article-type CDATA #IMPLIED
    dtd-version CDATA #IMPLIED
    xml:lang NMTOKEN "en"
    specific-use CDATA #IMPLIED
    xmlns:xlink CDATA #FIXED "http://www.w3.org/1999/xlink"
    xmlns:mml CDATA #FIXED "http://www.w3.org/1998/Math/MathML"
>
```

**article-type 값**:
- `research-article`, `review-article`, `letter`, `editorial`
- `case-report`, `abstract`, `book-review`, `product-review`
- `meeting-report`, `discussion`, `obituary`, `oration`
- `reply`, `retraction`, `correction`, `addendum`

---

# Part 2: Front Matter

## 2.1 Front
```xml
<!ELEMENT front (
    journal-meta?,
    article-meta,
    (def-list | list | ack | bio | fn-group | glossary | notes)*
)>
```

## 2.2 Journal Metadata

### 2.2.1 journal-meta
```xml
<!ELEMENT journal-meta (
    (journal-id+, journal-title-group*, 
     (contrib-group | aff | aff-alternatives)*,
     issn*, issn-l?, isbn*, publisher?, notes*, self-uri*)+,
    custom-meta-group*
)>
```

### 2.2.2 journal-id
```xml
<!ELEMENT journal-id (#PCDATA)>

<!ATTLIST journal-id
    journal-id-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>
```

**journal-id-type 값**: `nlm-ta`, `iso-abbrev`, `publisher-id`, `pmc`, `doi`, `hwp`

### 2.2.3 journal-title-group
```xml
<!ELEMENT journal-title-group (
    journal-title*,
    journal-subtitle*,
    trans-title-group*,
    abbrev-journal-title*
)>

<!ELEMENT journal-title (#PCDATA | %all-phrase;)*>
<!ELEMENT journal-subtitle (#PCDATA | %all-phrase;)*>
<!ELEMENT abbrev-journal-title (#PCDATA | %all-phrase;)*>

<!ATTLIST abbrev-journal-title
    abbrev-type CDATA #IMPLIED
>
```

### 2.2.4 issn
```xml
<!ELEMENT issn (#PCDATA)>

<!ATTLIST issn
    content-type CDATA #IMPLIED
    publication-format (print | electronic | print-electronic | online) #IMPLIED
    pub-type (ppub | epub | ppub-epub | epub-ppub) #IMPLIED
>
```

### 2.2.5 publisher
```xml
<!ELEMENT publisher (publisher-name+, publisher-loc*)>
<!ELEMENT publisher-name (#PCDATA | %all-phrase;)*>
<!ELEMENT publisher-loc (#PCDATA | %address-elements;)*>
```

---

## 2.3 Article Metadata

### 2.3.1 article-meta
```xml
<!ELEMENT article-meta (
    (article-id)*,
    article-categories?,
    title-group?,
    (contrib-group | aff | aff-alternatives | x)*,
    author-notes?,
    pub-date*,
    pub-date-not-available?,
    volume?, volume-id*, volume-series?,
    issue?, issue-id*, issue-title*, issue-title-group*, issue-sponsor*,
    issue-part?,
    volume-issue-group*,
    isbn*,
    supplement?,
    ((fpage, lpage?, page-range?) | elocation-id)?,
    (email | ext-link | uri | product | supplementary-material)*,
    history?,
    pub-history?,
    permissions?,
    self-uri*,
    (related-article | related-object)*,
    abstract*,
    trans-abstract*,
    kwd-group*,
    funding-group*,
    support-group*,
    conference*,
    counts?,
    custom-meta-group*,
    content-language*
)>
```

### 2.3.2 article-id
```xml
<!ELEMENT article-id (#PCDATA)>

<!ATTLIST article-id
    assigning-authority CDATA #IMPLIED
    pub-id-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
>
```

**pub-id-type 값**: `doi`, `pmid`, `pmcid`, `pmc-uid`, `publisher-id`, `manuscript`, `ark`, `art-access-id`

### 2.3.3 article-categories
```xml
<!ELEMENT article-categories (subj-group+)>

<!ELEMENT subj-group (
    (subject | compound-subject)+,
    subj-group*
)>

<!ATTLIST subj-group
    subj-group-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
>

<!ELEMENT subject (#PCDATA | %all-phrase;)*>
<!ELEMENT compound-subject (compound-subject-part+)>
<!ELEMENT compound-subject-part (#PCDATA | %all-phrase;)*>
```

### 2.3.4 title-group
```xml
<!ELEMENT title-group (
    article-title,
    subtitle*,
    trans-title-group*,
    alt-title*,
    fn-group?
)>

<!ELEMENT article-title (#PCDATA | %all-phrase;)*>
<!ELEMENT subtitle (#PCDATA | %all-phrase;)*>

<!ELEMENT trans-title-group (trans-title, trans-subtitle*)>

<!ATTLIST trans-title-group
    xml:lang NMTOKEN #IMPLIED
>

<!ELEMENT trans-title (#PCDATA | %all-phrase;)*>
<!ELEMENT trans-subtitle (#PCDATA | %all-phrase;)*>

<!ELEMENT alt-title (#PCDATA | %all-phrase;)*>

<!ATTLIST alt-title
    alt-title-type CDATA #IMPLIED
>
```

---

## 2.4 Contributors

### 2.4.1 contrib-group
```xml
<!ELEMENT contrib-group (
    (contrib | address | aff | aff-alternatives | author-comment |
     bio | email | etal | ext-link | fn | on-behalf-of | role |
     uri | xref | x)+
)>

<!ATTLIST contrib-group
    content-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
>
```

### 2.4.2 contrib
```xml
<!ELEMENT contrib (
    (contrib-id | anonymous | collab | collab-alternatives |
     name | name-alternatives | string-name | degrees | address | aff |
     aff-alternatives | author-comment | bio | email | etal |
     ext-link | fn | on-behalf-of | role | uri | xref | x)*
)>

<!ATTLIST contrib
    contrib-type CDATA #IMPLIED
    corresp (yes | no) #IMPLIED
    deceased (yes | no) #IMPLIED
    equal-contrib (yes | no) #IMPLIED
    id ID #IMPLIED
    rid IDREFS #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
    xlink:href CDATA #IMPLIED
    xlink:role CDATA #IMPLIED
    xlink:show (embed | new | none | other | replace) #IMPLIED
    xlink:title CDATA #IMPLIED
    xlink:type (simple) #IMPLIED
>
```

**contrib-type 값**: `author`, `editor`, `guest-editor`, `translator`, `reviewer`, `compiler`, `collab`

### 2.4.3 name
```xml
<!ELEMENT name (
    ((surname, given-names?) | given-names),
    prefix?,
    suffix?
)>

<!ATTLIST name
    content-type CDATA #IMPLIED
    name-style (western | eastern | islensk | given-only) "western"
    specific-use CDATA #IMPLIED
>

<!ELEMENT surname (#PCDATA | %all-phrase;)*>
<!ELEMENT given-names (#PCDATA | %all-phrase;)*>
<!ELEMENT prefix (#PCDATA | %all-phrase;)*>
<!ELEMENT suffix (#PCDATA | %all-phrase;)*>
```

### 2.4.4 collab
```xml
<!ELEMENT collab (#PCDATA | %all-phrase; | %contrib-elements;)*>

<!ATTLIST collab
    collab-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
    xlink:href CDATA #IMPLIED
>
```

### 2.4.5 contrib-id
```xml
<!ELEMENT contrib-id (#PCDATA)>

<!ATTLIST contrib-id
    authenticated (true | false) #IMPLIED
    content-type CDATA #IMPLIED
    contrib-id-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
>
```

**contrib-id-type 값**: `orcid`, `isni`, `scopus`, `researcher-id`, `wos-researcher-id`

### 2.4.6 aff
```xml
<!ELEMENT aff (#PCDATA | %address-elements; | %aff-elements;)*>

<!ATTLIST aff
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    rid IDREFS #IMPLIED
    specific-use CDATA #IMPLIED
>
```

### 2.4.7 aff-elements
```xml
<!ELEMENT institution (#PCDATA | %all-phrase;)*>

<!ATTLIST institution
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:href CDATA #IMPLIED
>

<!ELEMENT institution-wrap (institution-id*, institution*, institution-wrap*)>

<!ELEMENT institution-id (#PCDATA)>

<!ATTLIST institution-id
    content-type CDATA #IMPLIED
    institution-id-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
>
```

**institution-id-type 값**: `ror`, `isni`, `ringgold`, `grid`

---

## 2.5 Publication Dates

### 2.5.1 pub-date
```xml
<!ELEMENT pub-date (
    ((day?, month?) | season)?,
    year,
    era?
)>

<!ATTLIST pub-date
    assigning-authority CDATA #IMPLIED
    calendar CDATA #IMPLIED
    date-type CDATA #IMPLIED
    iso-8601-date CDATA #IMPLIED
    pub-type (ppub | epub | epub-ppub | ppub-epub | collection | epreprint) #IMPLIED
    publication-format (print | electronic | print-electronic | online) #IMPLIED
>
```

### 2.5.2 history
```xml
<!ELEMENT history (date | era | string-date)*>

<!ELEMENT date (
    ((day?, month?) | season)?,
    year?,
    era?,
    string-date?
)>

<!ATTLIST date
    calendar CDATA #IMPLIED
    date-type CDATA #IMPLIED
    iso-8601-date CDATA #IMPLIED
    publication-format CDATA #IMPLIED
    specific-use CDATA #IMPLIED
>
```

**date-type 값**: `received`, `accepted`, `rev-recd`, `corrected`, `pub`, `retracted`

---

## 2.6 Abstract & Keywords

### 2.6.1 abstract
```xml
<!ELEMENT abstract (
    (object-id)*,
    (title | label)*,
    (address | alternatives | answer | answer-set | array |
     block-alternatives | boxed-text | chem-struct-wrap | code |
     explanation | fig | fig-group | graphic | media | preformat |
     question | question-wrap | question-wrap-group |
     supplementary-material | table-wrap | table-wrap-group |
     disp-formula | disp-formula-group | def-list | list |
     tex-math | mml:math | p | related-article | related-object |
     disp-quote | speech | statement | verse-group | x | sec)*,
    (sec-meta?, (title | label)*,
     (%block-display.class; | %block-math.class; | %just-para.class; |
      %list.class; | %nothing-but-para.class; | %related-article.class; |
      %rest-of-para.class; | x)*, sec*)*
)>

<!ATTLIST abstract
    abstract-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>
```

**abstract-type 값**: `summary`, `short`, `executive-summary`, `toc`, `web-summary`, `graphical`, `author-highlights`, `plain-language-summary`

### 2.6.2 kwd-group
```xml
<!ELEMENT kwd-group (
    (label | title)*,
    (kwd | compound-kwd | nested-kwd | x | unstructured-kwd-group)*
)>

<!ATTLIST kwd-group
    assigning-authority CDATA #IMPLIED
    kwd-group-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    vocab CDATA #IMPLIED
    vocab-identifier CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>

<!ELEMENT kwd (#PCDATA | %all-phrase;)*>

<!ATTLIST kwd
    content-type CDATA #IMPLIED
    id ID #IMPLIED
>

<!ELEMENT compound-kwd (compound-kwd-part+)>
<!ELEMENT compound-kwd-part (#PCDATA | %all-phrase;)*>

<!ATTLIST compound-kwd-part
    content-type CDATA #IMPLIED
>

<!ELEMENT nested-kwd (kwd, nested-kwd*)>
```

---

## 2.7 Permissions & Funding

### 2.7.1 permissions
```xml
<!ELEMENT permissions (
    copyright-statement*,
    copyright-year*,
    copyright-holder*,
    (ali:free_to_read | license)*
)>

<!ELEMENT copyright-statement (#PCDATA | %all-phrase;)*>
<!ELEMENT copyright-year (#PCDATA)>
<!ELEMENT copyright-holder (#PCDATA | %all-phrase;)*>

<!ELEMENT license (ali:license_ref*, (license-p)*)>

<!ATTLIST license
    license-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:href CDATA #IMPLIED
>

<!ELEMENT license-p (#PCDATA | %all-phrase; | %block-elements;)*>
```

### 2.7.2 funding-group
```xml
<!ELEMENT funding-group (
    award-group*,
    funding-statement*,
    open-access?
)>

<!ELEMENT award-group (
    funding-source*,
    award-id*,
    award-name?,
    award-desc?,
    principal-award-recipient*,
    principal-investigator*,
    support-source*
)>

<!ATTLIST award-group
    award-type CDATA #IMPLIED
    id ID #IMPLIED
    rid IDREFS #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:href CDATA #IMPLIED
>

<!ELEMENT funding-source (#PCDATA | %funding-source-elements;)*>

<!ATTLIST funding-source
    country CDATA #IMPLIED
    id ID #IMPLIED
    rid IDREFS #IMPLIED
    source-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:href CDATA #IMPLIED
>

<!ELEMENT award-id (#PCDATA | %all-phrase;)*>

<!ATTLIST award-id
    award-id-type CDATA #IMPLIED
    award-type CDATA #IMPLIED
    id ID #IMPLIED
    rid IDREFS #IMPLIED
    specific-use CDATA #IMPLIED
>
```

---

# Part 3: Body Structure

## 3.1 Body
```xml
<!ELEMENT body (
    (address | alternatives | answer | answer-set | array |
     block-alternatives | boxed-text | chem-struct-wrap | code |
     explanation | fig | fig-group | graphic | media | preformat |
     question | question-wrap | question-wrap-group |
     supplementary-material | table-wrap | table-wrap-group |
     disp-formula | disp-formula-group | def-list | list |
     tex-math | mml:math | p | related-article | related-object |
     ack | disp-quote | speech | statement | verse-group | x)*,
    (sec)*,
    sig-block?
)>

<!ATTLIST body
    specific-use CDATA #IMPLIED
>
```

## 3.2 Section
```xml
<!ELEMENT sec (
    sec-meta?,
    (label | title)*,
    (address | alternatives | answer | answer-set | array |
     block-alternatives | boxed-text | chem-struct-wrap | code |
     explanation | fig | fig-group | graphic | media | preformat |
     question | question-wrap | question-wrap-group |
     supplementary-material | table-wrap | table-wrap-group |
     disp-formula | disp-formula-group | def-list | list |
     tex-math | mml:math | p | related-article | related-object |
     ack | disp-quote | speech | statement | verse-group | x)*,
    (sec)*,
    (fn-group | glossary | ref-list | sig-block)*
)>

<!ATTLIST sec
    disp-level CDATA #IMPLIED
    id ID #IMPLIED
    sec-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>
```

**sec-type 값**:
- `intro`, `materials`, `methods`, `materials|methods`
- `results`, `discussion`, `results|discussion`
- `conclusions`, `cases`, `subjects`, `supplementary-material`

## 3.3 Paragraph
```xml
<!ELEMENT p (#PCDATA | %all-phrase; | %block-elements;)*>

<!ATTLIST p
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>
```

---

# Part 4: Tables & Figures

## 4.1 Figure
```xml
<!ELEMENT fig (
    (object-id)*,
    label?,
    (caption)*,
    abstract*,
    kwd-group*,
    alt-text*,
    long-desc*,
    (email | ext-link | uri)*,
    (alternatives | disp-formula | disp-formula-group |
     chem-struct-wrap | disp-quote | speech | statement |
     verse-group | table-wrap | p | def-list | list |
     array | code | graphic | media | preformat)*,
    (attrib | permissions)*
)>

<!ATTLIST fig
    fig-type CDATA #IMPLIED
    id ID #IMPLIED
    orientation (portrait | landscape) #IMPLIED
    position (anchor | background | float | margin) "float"
    specific-use CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>
```

**fig-type 값**: `map`, `chart`, `scheme`, `drawing`, `photo`, `illustration`

## 4.2 Caption
```xml
<!ELEMENT caption (
    title?,
    (p | fn-group)*
)>

<!ATTLIST caption
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
    style CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>

<!ELEMENT title (#PCDATA | %all-phrase;)*>
```

## 4.3 Graphic
```xml
<!ELEMENT graphic (
    (alt-text | long-desc)*,
    (abstract)*,
    (attrib)*,
    (permissions)*
)>

<!ATTLIST graphic
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    mime-subtype CDATA #IMPLIED
    mimetype CDATA #IMPLIED
    orientation (portrait | landscape) #IMPLIED
    position (anchor | background | float | margin) #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
    xlink:href CDATA #REQUIRED
    xlink:role CDATA #IMPLIED
    xlink:show (embed | new | none | other | replace) #IMPLIED
    xlink:title CDATA #IMPLIED
    xlink:type (simple) #IMPLIED
>
```

## 4.4 Table Wrap
```xml
<!ELEMENT table-wrap (
    (object-id)*,
    label?,
    (caption)*,
    abstract*,
    kwd-group*,
    alt-text*,
    long-desc*,
    (alternatives | disp-quote | speech | statement |
     verse-group | def-list | list | array | code | graphic |
     media | preformat | table | oasis:table)*,
    (table-wrap-foot | attrib | permissions)*
)>

<!ATTLIST table-wrap
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    orientation (portrait | landscape) #IMPLIED
    position (anchor | background | float | margin) "float"
    specific-use CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>
```

## 4.5 XHTML Table
```xml
<!ELEMENT table (
    (col* | colgroup*),
    (thead?, tfoot?, (tbody+ | tr+))
)>

<!ATTLIST table
    border CDATA #IMPLIED
    cellpadding CDATA #IMPLIED
    cellspacing CDATA #IMPLIED
    content-type CDATA #IMPLIED
    frame (void | above | below | hsides | lhs | rhs | vsides | box | border) #IMPLIED
    id ID #IMPLIED
    rules (none | groups | rows | cols | all) #IMPLIED
    specific-use CDATA #IMPLIED
    style CDATA #IMPLIED
    summary CDATA #IMPLIED
    width CDATA #IMPLIED
>
```

---

# Part 5: Back Matter

## 5.1 Back
```xml
<!ELEMENT back (
    label?,
    title*,
    (ack | app-group | bio | fn-group | glossary | notes | ref-list |
     sec | sig-block)*
)>
```

## 5.2 Acknowledgments
```xml
<!ELEMENT ack (
    (object-id)*,
    (label | title)*,
    (abstract)*,
    (kwd-group)*,
    (address | alternatives | answer | answer-set | array |
     block-alternatives | boxed-text | chem-struct-wrap | code |
     explanation | fig | fig-group | graphic | media | preformat |
     question | question-wrap | question-wrap-group |
     supplementary-material | table-wrap | table-wrap-group |
     disp-formula | disp-formula-group | def-list | list |
     tex-math | mml:math | p | related-article | related-object |
     disp-quote | speech | statement | verse-group | x)*,
    (sec)*,
    (fn-group | glossary | ref-list)*
)>

<!ATTLIST ack
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
>
```

## 5.3 Appendix
```xml
<!ELEMENT app-group (
    (label | title)*,
    (abstract)*,
    (kwd-group)*,
    (address | alternatives | answer | answer-set | array |
     block-alternatives | boxed-text | chem-struct-wrap | code |
     explanation | fig | fig-group | graphic | media | preformat |
     question | question-wrap | question-wrap-group |
     supplementary-material | table-wrap | table-wrap-group |
     disp-formula | disp-formula-group | def-list | list |
     tex-math | mml:math | p | related-article | related-object |
     ack | disp-quote | speech | statement | verse-group | x)*,
    (app | ref-list)*
)>

<!ELEMENT app (
    sec-meta?,
    (label | title)*,
    (address | alternatives | answer | answer-set | array |
     block-alternatives | boxed-text | chem-struct-wrap | code |
     explanation | fig | fig-group | graphic | media | preformat |
     question | question-wrap | question-wrap-group |
     supplementary-material | table-wrap | table-wrap-group |
     disp-formula | disp-formula-group | def-list | list |
     tex-math | mml:math | p | related-article | related-object |
     ack | disp-quote | speech | statement | verse-group | x)*,
    (sec)*,
    (fn-group | glossary | ref-list | sig-block)*,
    (permissions)?
)>

<!ATTLIST app
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
>
```

---

# Part 6: References

## 6.1 Reference List
```xml
<!ELEMENT ref-list (
    (label | title)*,
    (address | alternatives | answer | answer-set | array |
     block-alternatives | boxed-text | chem-struct-wrap | code |
     explanation | fig | fig-group | graphic | media | preformat |
     question | question-wrap | question-wrap-group |
     supplementary-material | table-wrap | table-wrap-group |
     disp-formula | disp-formula-group | def-list | list |
     tex-math | mml:math | p | related-article | related-object |
     ack | disp-quote | speech | statement | verse-group | x)*,
    (ref | ref-list)*
)>

<!ATTLIST ref-list
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
>
```

## 6.2 Reference
```xml
<!ELEMENT ref (
    (label)?,
    (citation-alternatives | element-citation | mixed-citation |
     nlm-citation | note | x)+
)>

<!ATTLIST ref
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
>
```

## 6.3 Element Citation (Structured)
```xml
<!ELEMENT element-citation (
    %all-elements; | %citation-elements;
)*>

<!ATTLIST element-citation
    id ID #IMPLIED
    publication-type CDATA #IMPLIED
    publisher-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
    xlink:href CDATA #IMPLIED
    xlink:role CDATA #IMPLIED
    xlink:show (embed | new | none | other | replace) #IMPLIED
    xlink:title CDATA #IMPLIED
    xlink:type (simple) #IMPLIED
>
```

**publication-type 값**:
- `journal`, `book`, `confproc`, `thesis`, `report`
- `patent`, `standard`, `webpage`, `software`
- `data`, `database`, `working-paper`, `preprint`

## 6.4 Mixed Citation (Unstructured/Semi-structured)
```xml
<!ELEMENT mixed-citation (
    #PCDATA | %all-elements; | %citation-elements;
)*>

<!ATTLIST mixed-citation
    id ID #IMPLIED
    publication-type CDATA #IMPLIED
    publisher-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:href CDATA #IMPLIED
>
```

## 6.5 Citation Elements
```xml
<!ELEMENT article-title (#PCDATA | %all-phrase;)*>
<!ELEMENT source (#PCDATA | %all-phrase;)*>
<!ELEMENT year (#PCDATA | era)*>
<!ELEMENT month (#PCDATA)>
<!ELEMENT day (#PCDATA)>
<!ELEMENT volume (#PCDATA | %all-phrase;)*>
<!ELEMENT issue (#PCDATA | %all-phrase;)*>
<!ELEMENT fpage (#PCDATA)>
<!ELEMENT lpage (#PCDATA)>
<!ELEMENT page-range (#PCDATA)>
<!ELEMENT elocation-id (#PCDATA)>
<!ELEMENT pub-id (#PCDATA)>

<!ATTLIST pub-id
    assigning-authority CDATA #IMPLIED
    pub-id-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:href CDATA #IMPLIED
>

<!ELEMENT person-group (
    (collab | collab-alternatives | name | name-alternatives |
     string-name | aff | aff-alternatives | etal | role)*
)>

<!ATTLIST person-group
    person-group-type CDATA #IMPLIED
>
```

**person-group-type 값**: `author`, `editor`, `translator`, `transed`, `guest-editor`, `compiler`

---

# Part 7: Supplementary Material

## 7.1 Supplementary Material
```xml
<!ELEMENT supplementary-material (
    (object-id)*,
    label?,
    (caption)*,
    abstract*,
    kwd-group*,
    alt-text*,
    long-desc*,
    (email | ext-link | uri)*,
    (alternatives | disp-formula | disp-formula-group |
     chem-struct-wrap | disp-quote | speech | statement |
     verse-group | table-wrap | p | def-list | list |
     array | code | graphic | media | preformat)*,
    (attrib | permissions)*
)>

<!ATTLIST supplementary-material
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    mime-subtype CDATA #IMPLIED
    mimetype CDATA #IMPLIED
    orientation (portrait | landscape) #IMPLIED
    position (anchor | background | float | margin) #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
    xlink:href CDATA #IMPLIED
    xlink:role CDATA #IMPLIED
    xlink:show (embed | new | none | other | replace) #IMPLIED
    xlink:title CDATA #IMPLIED
    xlink:type (simple) #IMPLIED
>
```

## 7.2 Media
```xml
<!ELEMENT media (
    (alt-text | long-desc)*,
    (abstract)*,
    (attrib)*,
    (permissions)*
)>

<!ATTLIST media
    content-type CDATA #IMPLIED
    id ID #IMPLIED
    mime-subtype CDATA #IMPLIED
    mimetype CDATA #IMPLIED
    orientation (portrait | landscape) #IMPLIED
    position (anchor | background | float | margin) #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
    xlink:href CDATA #REQUIRED
    xlink:role CDATA #IMPLIED
    xlink:show (embed | new | none | other | replace) #IMPLIED
    xlink:title CDATA #IMPLIED
    xlink:type (simple) #IMPLIED
>
```

---

# Part 8: Floats Group

## 8.1 Floats Group
```xml
<!ELEMENT floats-group (
    alternatives | block-alternatives | boxed-text | chem-struct-wrap |
    code | explanation | fig | fig-group | graphic | media | preformat |
    supplementary-material | table-wrap | table-wrap-group
)*>
```

---

# Part 9: Sub-article & Response

## 9.1 Sub-article
```xml
<!ELEMENT sub-article (
    (front | front-stub),
    body?,
    back?,
    floats-group?,
    (sub-article | response)*
)>

<!ATTLIST sub-article
    article-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>
```

## 9.2 Response
```xml
<!ELEMENT response (
    (front | front-stub),
    body?,
    back?,
    floats-group?
)>

<!ATTLIST response
    id ID #IMPLIED
    response-type CDATA #IMPLIED
    specific-use CDATA #IMPLIED
    xml:lang NMTOKEN #IMPLIED
>
```

**response-type 값**: `addendum`, `discussion`, `reply`, `author-comment`, `reviewer-report`

---

# Part 10: Inline Elements

## 10.1 Formatting Elements
```xml
<!ELEMENT bold (#PCDATA | %all-phrase;)*>
<!ELEMENT italic (#PCDATA | %all-phrase;)*>
<!ELEMENT underline (#PCDATA | %all-phrase;)*>
<!ELEMENT overline (#PCDATA | %all-phrase;)*>
<!ELEMENT strike (#PCDATA | %all-phrase;)*>
<!ELEMENT roman (#PCDATA | %all-phrase;)*>
<!ELEMENT sans-serif (#PCDATA | %all-phrase;)*>
<!ELEMENT sc (#PCDATA | %all-phrase;)*>
<!ELEMENT monospace (#PCDATA | %all-phrase;)*>
<!ELEMENT sup (#PCDATA | %all-phrase;)*>
<!ELEMENT sub (#PCDATA | %all-phrase;)*>
```

## 10.2 Links
```xml
<!ELEMENT ext-link (#PCDATA | %all-phrase;)*>

<!ATTLIST ext-link
    assigning-authority CDATA #IMPLIED
    ext-link-type CDATA #IMPLIED
    id ID #IMPLIED
    specific-use CDATA #IMPLIED
    xlink:actuate (onLoad | onRequest | other | none) #IMPLIED
    xlink:href CDATA #IMPLIED
    xlink:role CDATA #IMPLIED
    xlink:show (embed | new | none | other | replace) #IMPLIED
    xlink:title CDATA #IMPLIED
    xlink:type (simple) #IMPLIED
>

<!ELEMENT xref (#PCDATA | %all-phrase;)*>

<!ATTLIST xref
    id ID #IMPLIED
    ref-type CDATA #IMPLIED
    rid IDREFS #IMPLIED
    specific-use CDATA #IMPLIED
>
```

**ref-type 값**: `aff`, `bibr`, `fig`, `table`, `sec`, `supplementary-material`, `fn`, `disp-formula`

---

# Part 11: Model Class Checklist

## Required Models

### Root & Front
- [ ] article (+ article-type, dtd-version, xml:lang)
- [ ] front
- [ ] journal-meta
- [ ] journal-id (+ journal-id-type)
- [ ] journal-title-group
- [ ] journal-title, abbrev-journal-title
- [ ] issn (+ content-type, publication-format, pub-type)
- [ ] publisher (publisher-name, publisher-loc)
- [ ] article-meta

### Article Identifiers
- [ ] article-id (+ pub-id-type)
- [ ] article-categories
- [ ] subj-group (+ subj-group-type)
- [ ] subject

### Title & Contributors
- [ ] title-group
- [ ] article-title, subtitle
- [ ] trans-title-group (+ xml:lang)
- [ ] trans-title, trans-subtitle
- [ ] alt-title (+ alt-title-type)
- [ ] contrib-group (+ content-type)
- [ ] contrib (+ contrib-type, corresp, equal-contrib)
- [ ] contrib-id (+ contrib-id-type)
- [ ] name (+ name-style)
- [ ] surname, given-names, prefix, suffix
- [ ] collab, collab-alternatives
- [ ] name-alternatives, string-name
- [ ] degrees, role, on-behalf-of
- [ ] aff (+ id)
- [ ] aff-alternatives
- [ ] institution (+ content-type)
- [ ] institution-wrap
- [ ] institution-id (+ institution-id-type)
- [ ] author-notes
- [ ] email, ext-link, uri

### Dates
- [ ] pub-date (+ date-type, pub-type, publication-format, iso-8601-date)
- [ ] history
- [ ] date (+ date-type)
- [ ] day, month, year, season, era
- [ ] string-date

### Abstract & Keywords
- [ ] abstract (+ abstract-type)
- [ ] trans-abstract
- [ ] kwd-group (+ kwd-group-type, vocab)
- [ ] kwd (+ content-type)
- [ ] compound-kwd, compound-kwd-part
- [ ] nested-kwd

### Permissions & Funding
- [ ] permissions
- [ ] copyright-statement, copyright-year, copyright-holder
- [ ] license (+ license-type, xlink:href)
- [ ] license-p
- [ ] funding-group
- [ ] award-group (+ award-type)
- [ ] funding-source (+ source-type)
- [ ] award-id (+ award-id-type)
- [ ] principal-investigator

### Body Structure
- [ ] body
- [ ] sec (+ sec-type)
- [ ] sec-meta
- [ ] label, title
- [ ] p (+ content-type)

### Figures & Tables
- [ ] fig (+ fig-type, position)
- [ ] fig-group
- [ ] graphic (+ xlink:href, mimetype)
- [ ] media (+ xlink:href, mimetype)
- [ ] caption
- [ ] alt-text, long-desc
- [ ] table-wrap (+ position)
- [ ] table-wrap-foot
- [ ] table (+ frame, rules)
- [ ] thead, tbody, tfoot
- [ ] tr, th, td

### Back Matter
- [ ] back
- [ ] ack
- [ ] app-group
- [ ] app
- [ ] glossary
- [ ] fn-group
- [ ] fn (+ fn-type)
- [ ] notes
- [ ] bio

### References
- [ ] ref-list
- [ ] ref
- [ ] element-citation (+ publication-type)
- [ ] mixed-citation (+ publication-type)
- [ ] citation-alternatives
- [ ] person-group (+ person-group-type)
- [ ] article-title, source
- [ ] volume, issue, fpage, lpage, page-range
- [ ] elocation-id
- [ ] pub-id (+ pub-id-type)
- [ ] year, month, day

### Supplementary
- [ ] supplementary-material (+ xlink:href, mimetype)
- [ ] floats-group
- [ ] boxed-text

### Sub-article & Response
- [ ] sub-article (+ article-type)
- [ ] response (+ response-type)
- [ ] front-stub

### Inline Elements
- [ ] bold, italic, underline
- [ ] sup, sub
- [ ] monospace, sc
- [ ] xref (+ ref-type, rid)
- [ ] ext-link (+ ext-link-type, xlink:href)

### Math & Chemistry
- [ ] disp-formula
- [ ] inline-formula
- [ ] tex-math
- [ ] mml:math
- [ ] chem-struct-wrap
- [ ] chem-struct

### Lists
- [ ] def-list
- [ ] def-item, term, def
- [ ] list (+ list-type)
- [ ] list-item

---

# Part 12: Common Pitfalls

## 12.1 자주 누락되는 요소
1. `front-stub` - sub-article용 축약 메타데이터
2. `floats-group` - 부유 요소 컨테이너
3. `alt-text`, `long-desc` - 접근성 요소
4. `pub-history` - 출판 이력 (JATS 1.2+)
5. `content-language` - 다국어 표시 (JATS 1.4)
6. `collab-alternatives`, `name-alternatives` - 대안 표기
7. `support-group` - 지원 정보 (JATS 1.2+)

## 12.2 자주 누락되는 속성
1. `article/@article-type` - 논문 유형
2. `pub-date/@iso-8601-date` - ISO 날짜
3. `name/@name-style` - 이름 스타일 (eastern/western)
4. `contrib/@equal-contrib` - 동등 기여자
5. `institution-id/@institution-id-type` - ROR, ISNI 등
6. `contrib-id/@contrib-id-type` - ORCID 등
7. `xref/@ref-type` - 참조 유형

## 12.3 특수 케이스
1. **중첩 sec**: sec 안에 sec 가능 (무한 중첩)
2. **alternatives**: 동일 콘텐츠의 대안 표현
3. **Mixed content**: 대부분의 텍스트 요소
4. **OASIS Tables**: XHTML 테이블 외에 CALS 테이블도 가능
5. **MathML**: 네임스페이스 처리 필요
6. **XLink**: 외부 링크 처리

## 12.4 버전별 차이
| JATS 버전 | 주요 변경사항 |
|-----------|--------------|
| 1.4 (2024) | content-language, legend 추가, collab-name 추가 |
| 1.3 (2021) | pub-history, data-title 추가 |
| 1.2 (2019) | support-group, index-term 추가 |
| 1.1 (2015) | funding-group 개선 |
| 1.0 (2012) | 최초 NISO 표준 |

---

# Part 13: Data Sources

## PMC FTP
- **Open Access Subset**: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_bulk/
- **Commercial Use Collection**: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_comm/
- **Non-Commercial Collection**: https://ftp.ncbi.nlm.nih.gov/pub/pmc/oa_noncomm/

## File Integrity: 체크섬 미제공 ⚠️
PMC FTP는 PubMed와 달리 **MD5 체크섬 파일을 제공하지 않습니다**.

| 서비스 | 체크섬 | 비고 |
|--------|--------|------|
| PMC FTP | ❌ 없음 | filelist.csv에 체크섬 컬럼 없음 |
| PMC AWS S3 | ⚠️ ETag | "may or may not be MD5" - 보장 안됨 |
| PubMed FTP | ✅ 있음 | 각 파일별 `.md5` 파일 제공 |

**filelist.csv 컬럼:**
```csv
File,Article Citation,Accession ID,Last Updated,PMID,License
oa_package/08/e0/PMC13900.tar.gz,Breast Cancer Res...,PMC13900,2019-11-05,11250746,NO-CC CODE
```

**대안 1: tar.gz 무결성 검증**
```java
/**
 * tar.gz 압축 해제 시 무결성 검증
 * 손상된 파일은 IOException 발생
 */
public boolean validateTarGz(Path tarGzFile) {
    try (TarArchiveInputStream tar = new TarArchiveInputStream(
            new GZIPInputStream(Files.newInputStream(tarGzFile)))) {
        TarArchiveEntry entry;
        while ((entry = tar.getNextEntry()) != null) {
            // 각 엔트리 읽기 시도 - 손상 시 예외 발생
            if (entry.isFile()) {
                tar.readAllBytes();
            }
        }
        return true;
    } catch (Exception e) {
        return false; // 손상된 파일
    }
}
```

**대안 2: XML 파싱 검증**
```java
/**
 * XML 웰폼드(well-formed) 검증
 */
public boolean validateXml(Path xmlFile) {
    try {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try (InputStream is = Files.newInputStream(xmlFile)) {
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            while (reader.hasNext()) {
                reader.next();
            }
        }
        return true;
    } catch (Exception e) {
        return false;
    }
}
```

## Documentation
- **JATS Home**: https://jats.nlm.nih.gov/
- **Tag Library 1.4**: https://jats.nlm.nih.gov/archiving/tag-library/1.4/
- **PMC Tagging Guidelines**: https://www.ncbi.nlm.nih.gov/pmc/pmcdoc/tagging-guidelines/

---

# Quick Reference

```java
// JATS 논문 구조
article
├── front (메타데이터)
│   ├── journal-meta
│   │   ├── journal-id+
│   │   ├── journal-title-group*
│   │   ├── issn*
│   │   └── publisher?
│   └── article-meta
│       ├── article-id*
│       ├── article-categories?
│       ├── title-group
│       ├── contrib-group*
│       ├── aff*
│       ├── pub-date*
│       ├── abstract*
│       ├── kwd-group*
│       ├── funding-group*
│       └── permissions?
├── body? (본문)
│   ├── sec*
│   │   ├── title
│   │   ├── p*
│   │   └── sec* (중첩)
│   └── fig*, table-wrap* (인라인)
├── back? (후미)
│   ├── ack?
│   ├── app-group?
│   ├── ref-list*
│   └── fn-group?
├── floats-group? (부유 요소)
└── sub-article* | response* (부속 논문)

// 주요 속성 기본값
name/@name-style = "western"
fig/@position = "float"
table-wrap/@position = "float"
article/@xml:lang = "en"
```
