package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CommentsCorrections / 코멘트 및 정정
 *
 * DTD: <!ELEMENT CommentsCorrections (RefSource, PMID?, Note?)>
 * DTD: <!ATTLIST CommentsCorrections
 *          RefType (AssociatedDataset | AssociatedPublication | CommentIn | CommentOn |
 *                   CorrectedandRepublishedIn | CorrectedandRepublishedFrom |
 *                   ErratumIn | ErratumFor | ExpressionOfConcernIn | ExpressionOfConcernFor |
 *                   RepublishedIn | RepublishedFrom | RetractedandRepublishedIn |
 *                   RetractedandRepublishedFrom | RetractionIn | RetractionOf |
 *                   UpdateIn | UpdateOf | SummaryForPatientsIn | OriginalReportIn |
 *                   ReprintIn | ReprintOf | Cites) #REQUIRED>
 *
 * KR: 논문의 코멘트, 정정, 철회 등 관련 정보
 * EN: Article comment, correction, retraction information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsCorrections {

    /**
     * 참조 유형 (필수) / Reference type (required)
     */
    private RefType refType;

    /**
     * 참조 출처 / Reference source
     */
    private RefSource refSource;

    /**
     * PMID / PMID
     */
    private PMID pmid;

    /**
     * 노트 / Note
     */
    private Note note;
}
