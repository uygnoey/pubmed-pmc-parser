package com.bioxml.parser.pubmed.model;

import com.bioxml.parser.common.model.PubMedDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MedlineCitation / MEDLINE 인용
 *
 * DTD: <!ELEMENT MedlineCitation (
 *      PMID,
 *      DateCompleted?,
 *      DateRevised?,
 *      Article,
 *      MedlineJournalInfo,
 *      ChemicalList?,
 *      SupplMeshList?,
 *      CitationSubset*,
 *      CommentsCorrectionsList?,
 *      GeneSymbolList?,
 *      MeshHeadingList?,
 *      NumberOfReferences?,
 *      PersonalNameSubjectList?,
 *      OtherID*,
 *      OtherAbstract*,
 *      KeywordList*,
 *      CoiStatement?,
 *      SpaceFlightMission*,
 *      InvestigatorList?,
 *      GeneralNote*
 *  )>
 * DTD: <!ATTLIST MedlineCitation
 *          Status (Completed | In-Process | PubMed-not-MEDLINE | In-Data-Review | Publisher | MEDLINE | OLDMEDLINE) #REQUIRED
 *          Owner (NLM | NASA | PIP | KIE | HSR | HMD | NOTNLM) "NLM"
 *          IndexingMethod (Automated | Curated) #IMPLIED
 *          VersionID CDATA #IMPLIED
 *          VersionDate CDATA #IMPLIED>
 *
 * KR: MEDLINE 데이터베이스의 핵심 인용 정보
 * EN: Core citation information in MEDLINE database
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedlineCitation {

    // ====== Attributes ======
    /**
     * 상태 (필수) / Status (required)
     */
    private String status;

    /**
     * 소유자 (기본값: "NLM") / Owner (default: "NLM")
     */
    @Builder.Default
    private String owner = "NLM";

    /**
     * 색인 방법 / Indexing method
     */
    private String indexingMethod;

    /**
     * 버전 ID / Version ID
     */
    private String versionID;

    /**
     * 버전 날짜 / Version date
     */
    private String versionDate;

    // ====== Elements ======
    /**
     * PMID / PMID
     */
    private PMID pmid;

    /**
     * 완료 날짜 / Date completed
     */
    private PubMedDate dateCompleted;

    /**
     * 개정 날짜 / Date revised
     */
    private PubMedDate dateRevised;

    /**
     * 논문 / Article
     */
    private Article article;

    /**
     * MEDLINE 저널 정보 / MEDLINE journal info
     */
    private MedlineJournalInfo medlineJournalInfo;

    /**
     * 화학물질 목록 / Chemical list
     */
    private ChemicalList chemicalList;

    /**
     * 보충 MeSH 목록 / Supplementary MeSH list
     */
    private SupplMeshList supplMeshList;

    /**
     * 인용 하위집합 목록 / Citation subset list
     */
    private List<CitationSubset> citationSubsets;

    /**
     * 코멘트 및 정정 목록 / Comments and corrections list
     */
    private CommentsCorrectionsList commentsCorrectionsList;

    /**
     * 유전자 심볼 목록 / Gene symbol list
     */
    private GeneSymbolList geneSymbolList;

    /**
     * MeSH 주제어 목록 / MeSH heading list
     */
    private MeshHeadingList meshHeadingList;

    /**
     * 참고문헌 수 / Number of references
     */
    private NumberOfReferences numberOfReferences;

    /**
     * 인물 주제 목록 / Personal name subject list
     */
    private PersonalNameSubjectList personalNameSubjectList;

    /**
     * 기타 ID 목록 / Other ID list
     */
    private List<OtherID> otherIDs;

    /**
     * 기타 초록 목록 / Other abstract list
     */
    private List<OtherAbstract> otherAbstracts;

    /**
     * 키워드 목록 / Keyword list
     */
    private List<KeywordList> keywordLists;

    /**
     * 이해충돌 성명 / Conflict of interest statement
     */
    private CoiStatement coiStatement;

    /**
     * 우주비행 미션 목록 / Space flight mission list
     */
    private List<SpaceFlightMission> spaceFlightMissions;

    /**
     * 조사자 목록 / Investigator list
     */
    private InvestigatorList investigatorList;

    /**
     * 일반 노트 목록 / General note list
     */
    private List<GeneralNote> generalNotes;
}
