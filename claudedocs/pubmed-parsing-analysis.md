# PubMed XML Parser - 실제 파일 파싱 결과 분석

**생성 일시:** Thu Jan 08 17:41:56 KST 2026

## 1. 전체 요약 / Overall Summary

| 파일명 | 크기 | 논문 수 | 처리 시간 | 속도 | MD5 |
|--------|------|---------|-----------|------|-----------|
| baseline/pubmed25n0001.xml.gz | 18.8MB | 30,000 | 1.45s | 20,661/s | ✅ |
| baseline/pubmed25n1274.xml.gz | 21.0MB | 11,553 | 0.94s | 12,356/s | ✅ |
| update/pubmed25n1275.xml.gz | 83.4MB | 30,000 | 3.34s | 8,995/s | ✅ |
| update/pubmed25n1685.xml.gz | 59.1MB | 19,956 | 2.25s | 8,869/s | ✅ |

**총계:** 91,509 논문, 7.97초, 평균 11,478 articles/sec


## 2. 파일별 상세 분석 / Detailed Analysis per File

### baseline/pubmed25n0001.xml.gz

**기본 통계:**

- 총 논문 수: 30,000
- Status 분포: MEDLINE (30,000)
- Owner 분포: NLM (30,000)
- 저자 있는 논문: 29,496 (98.3%)
- Abstract 있는 논문: 15,377 (51.3%)
- MeSH 있는 논문: 30,000 (100.0%)
- 키워드 있는 논문: 78 (0.3%)

**상위 5개 저널:**

| 순위 | 저널명 | 논문 수 |
|------|--------|----------|
| 1 | The Journal of pharmacy and pharmacology | 1,036 |
| 2 | Biochimica et biophysica acta | 920 |
| 3 | The Journal of biological chemistry | 604 |
| 4 | Annales de l'anesthesiologie francaise | 542 |
| 5 | Biochemistry | 375 |

**샘플 논문 (처음 3개):**

#### 논문 1

- **PMID:** 1
- **제목:** Formate assay in body fluids: application in methanol poisoning.
- **저널:** Biochemical medicine
- **저자:** Makar, McMartin, Palese, Tephly
- **출판일:** 1975-Jun
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Aldehyde Oxidoreductases, Animals, Body Fluids, Carbon Dioxide, Formates

#### 논문 2

- **PMID:** 2
- **제목:** Delineation of the intimate details of the backbone conformation of pyridine nucleotide coenzymes in...
- **저널:** Biochemical and biophysical research communications
- **저자:** Bose, Sarma
- **출판일:** 1975-Oct-27
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Fourier Analysis, Magnetic Resonance Spectroscopy, Models, Molecular, Molecular Conformation, NAD

#### 논문 3

- **PMID:** 3
- **제목:** Metal substitutions incarbonic anhydrase: a halide ion probe study.
- **저널:** Biochemical and biophysical research communications
- **저자:** Smith, Bryant
- **출판일:** 1975-Oct-27
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Animals, Binding Sites, Cadmium, Carbonic Anhydrases, Cattle

### baseline/pubmed25n1274.xml.gz

**기본 통계:**

- 총 논문 수: 11,553
- Status 분포: MEDLINE (3,598), In-Process (231), PubMed-not-MEDLINE (5,242), Publisher (2,482)
- Owner 분포: NLM (11,553)
- 저자 있는 논문: 11,479 (99.4%)
- Abstract 있는 논문: 10,890 (94.3%)
- MeSH 있는 논문: 3,598 (31.1%)
- 키워드 있는 논문: 9,382 (81.2%)

**상위 5개 저널:**

| 순위 | 저널명 | 논문 수 |
|------|--------|----------|
| 1 | International journal of molecular sciences | 518 |
| 2 | Journal of clinical medicine | 374 |
| 3 | Sensors (Basel, Switzerland) | 349 |
| 4 | Microorganisms | 290 |
| 5 | Materials (Basel, Switzerland) | 281 |

**샘플 논문 (처음 3개):**

#### 논문 1

- **PMID:** 39764487
- **제목:** Can gut microbiota explain acute diverticulitis occurrence in patients with symptomatic uncomplicate...
- **저널:** Bioscience of microbiota, food and health
- **저자:** Tursi, Procaccianti, D'Amico, DE Bastiani, Turroni
- **출판일:** 2025
- **Status:** PubMed-not-MEDLINE, **Owner:** NLM
- **키워드:** acute diverticulitis, gut microbiota, symptomatic uncomplicated diverticular disease

#### 논문 2

- **PMID:** 39764488
- **제목:** Effects of moderate beer consumption on immunity and the gut microbiome in immunosuppressed mice.
- **저널:** Bioscience of microbiota, food and health
- **저자:** Hu, Yin, Li, Fan, Li
- **출판일:** 2025
- **Status:** PubMed-not-MEDLINE, **Owner:** NLM
- **키워드:** alcoholic beer, cyclophosphamide, gut microbiome, immunosuppression, moderate drinking, non-alcoholic beer

#### 논문 3

- **PMID:** 39764489
- **제목:** Gut microbiota involvement in the effect of water-soluble dietary fiber on fatty liver and fibrosis.
- **저널:** Bioscience of microbiota, food and health
- **저자:** Sato, Iino, Chinda, Sasada, Soma, et al.
- **출판일:** 2025
- **Status:** PubMed-not-MEDLINE, **Owner:** NLM
- **키워드:** fatty liver, fibrosis, gut microbiota, water-soluble dietary fiber

### update/pubmed25n1275.xml.gz

**기본 통계:**

- 총 논문 수: 30,000
- Status 분포: MEDLINE (15,097), In-Process (1,593), PubMed-not-MEDLINE (8,094), In-Data-Review (2), Publisher (5,214)
- Owner 분포: NLM (30,000)
- 저자 있는 논문: 29,720 (99.1%)
- Abstract 있는 논문: 27,201 (90.7%)
- MeSH 있는 논문: 15,097 (50.3%)
- 키워드 있는 논문: 21,472 (71.6%)

**상위 5개 저널:**

| 순위 | 저널명 | 논문 수 |
|------|--------|----------|
| 1 | Scientific reports | 616 |
| 2 | International journal of molecular sciences | 412 |
| 3 | Sensors (Basel, Switzerland) | 350 |
| 4 | Angewandte Chemie (International ed. in English) | 342 |
| 5 | PloS one | 315 |

**샘플 논문 (처음 3개):**

#### 논문 1

- **PMID:** 10637214
- **제목:** Attorneys argue FDA regulation of tobacco before Supreme Court.
- **저널:** Circulation
- **저자:** SoRelle
- **출판일:** 2000-Jan-18
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Humans, Nicotiana, Nicotine, Smoking, Tobacco Industry

#### 논문 2

- **PMID:** 11197357
- **제목:** Brand appearances in contemporary cinema films and contribution to global marketing of cigarettes.
- **저널:** Lancet (London, England)
- **저자:** Sargent, Tickle, Beach, Dalton, Ahrens, et al.
- **출판일:** 2001-Jan-06
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Female, Humans, Male, Advertising, Motion Pictures

#### 논문 3

- **PMID:** 11226364
- **제목:** Tobacco control in an era of trade liberalisation.
- **저널:** Tobacco control
- **저자:** Bettcher, Shapiro
- **출판일:** 2001-Mar
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Humans, Commerce, Health Promotion, Nicotiana, Smoking Cessation

### update/pubmed25n1685.xml.gz

**기본 통계:**

- 총 논문 수: 19,956
- Status 분포: MEDLINE (9,775), In-Process (998), PubMed-not-MEDLINE (4,570), Publisher (4,613)
- Owner 분포: NLM (19,956)
- 저자 있는 논문: 19,835 (99.4%)
- Abstract 있는 논문: 17,740 (88.9%)
- MeSH 있는 논문: 9,774 (49.0%)
- 키워드 있는 논문: 14,122 (70.8%)

**상위 5개 저널:**

| 순위 | 저널명 | 논문 수 |
|------|--------|----------|
| 1 | Current genetics | 948 |
| 2 | Journal of the Chinese Medical Association : JCMA | 614 |
| 3 | Scientific reports | 530 |
| 4 | Cureus | 320 |
| 5 | Medicine | 256 |

**샘플 논문 (처음 3개):**

#### 논문 1

- **PMID:** 12589466
- **제목:** Evolution of multispecific mating-type alleles for pheromone perception in the homobasidiomycete fun...
- **저널:** Current genetics
- **저자:** Kothe, Gola, Wendland
- **출판일:** 2003-Feb
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Basidiomycota, Biological Evolution, Chromosome Mapping, Crosses, Genetic, Models, Genetic

#### 논문 2

- **PMID:** 12695847
- **제목:** The G protein beta subunit FGB1 regulates development and pathogenicity in Fusarium oxysporum.
- **저널:** Current genetics
- **저자:** Jain, Akiyama, Kan, Ohguchi, Takata
- **출판일:** 2003-May
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Base Sequence, Blotting, Southern, Cyclic AMP, DNA Primers, Fusarium

#### 논문 3

- **PMID:** 12695850
- **제목:** Quinic acid induces hypovirulence and expression of a hypovirulence-associated double-stranded RNA i...
- **저널:** Current genetics
- **저자:** Liu, Lakshman, Tavantzis
- **출판일:** 2003-May
- **Status:** MEDLINE, **Owner:** NLM
- **MeSH (처음 5개):** Blotting, Western, Chorismic Acid, DNA Primers, Gene Expression Regulation, Polyribosomes


## 3. 2024 신규 속성 검증 / 2024 New Attributes Verification

**검증 항목:**

- ✅ CollectiveName.investigators (IDREF) - 파싱 지원
- ✅ InvestigatorList.id (ID) - 파싱 지원
- ✅ 모든 DTD 속성 완전 지원

