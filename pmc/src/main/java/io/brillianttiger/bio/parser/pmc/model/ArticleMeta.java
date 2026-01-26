package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ArticleMeta / 논문 메타데이터
 *
 * KR: 논문의 메타데이터 정보를 포함하는 요소.
 *     논문 ID, 카테고리, 제목, 저자, 출판 정보, 초록, 키워드, 연구비 등을 포함.
 * EN: Element containing article metadata information.
 *     Includes article ID, categories, title, authors, publication info, abstract, keywords, funding, etc.
 *
 * DTD: <!ELEMENT article-meta (
 *          (article-id)*,
 *          article-categories?,
 *          title-group?,
 *          (contrib-group | aff | aff-alternatives | x)*,
 *          author-notes?,
 *          pub-date*,
 *          pub-date-not-available?,
 *          volume?, volume-id*, volume-series?,
 *          issue?, issue-id*, issue-title*, issue-title-group*, issue-sponsor*,
 *          issue-part?,
 *          volume-issue-group*,
 *          isbn*,
 *          supplement?,
 *          ((fpage, lpage?, page-range?) | elocation-id)?,
 *          (email | ext-link | uri | product | supplementary-material)*,
 *          history?,
 *          pub-history?,
 *          permissions?,
 *          self-uri*,
 *          (related-article | related-object)*,
 *          abstract*,
 *          trans-abstract*,
 *          kwd-group*,
 *          funding-group*,
 *          support-group*,
 *          conference*,
 *          counts?,
 *          custom-meta-group*,
 *          content-language*
 *      )>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/article-meta.html
 *
 * Note: JATS 1.4 complete DTD structure.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleMeta {

    /**
     * 논문 ID 목록 / Article ID list
     */
    private List<PmcArticleId> articleIds;

    /**
     * 논문 카테고리 / Article categories
     */
    private ArticleCategories articleCategories;

    /**
     * 제목 그룹 (필수) / Title group (required)
     */
    private TitleGroup titleGroup;

    /**
     * 기여자 그룹 목록 / Contributor group list
     */
    private List<ContribGroup> contribGroups;

    /**
     * 소속 목록 / Affiliation list
     */
    private List<Aff> affiliations;

    /**
     * 저자 노트 / Author notes
     */
    private AuthorNotes authorNotes;

    /**
     * 출판 날짜 목록 (1개 이상 필수) / Publication date list (at least one required)
     */
    private List<PmcPubDate> pubDates;

    /**
     * 권 / Volume
     */
    private Volume volume;

    /**
     * 권 ID / Volume ID
     */
    private VolumeId volumeId;

    /**
     * 권 시리즈 / Volume series
     */
    private VolumeSeries volumeSeries;

    /**
     * 호 / Issue
     */
    private PmcIssue issue;

    /**
     * 호 ID / Issue ID
     */
    private IssueId issueId;

    /**
     * 호 제목 / Issue title
     */
    private IssueTitle issueTitle;

    /**
     * 호 후원자 / Issue sponsor
     */
    private IssueSponsor issueSponsor;

    /**
     * 호 파트 / Issue part
     */
    private IssuePart issuePart;

    /**
     * ISBN 목록 / ISBN list
     */
    private List<PmcIsbn> isbns;

    /**
     * 보충 자료 / Supplement
     */
    private Supplement supplement;

    /**
     * 시작 페이지 / First page
     */
    private Fpage fpage;

    /**
     * 마지막 페이지 / Last page
     */
    private Lpage lpage;

    /**
     * 페이지 범위 / Page range
     */
    private PageRange pageRange;

    /**
     * 전자 위치 ID / Electronic location ID
     */
    private ElocationId elocationId;

    /**
     * 이메일 목록 / Email list
     */
    private List<Email> emails;

    /**
     * 외부 링크 목록 / External link list
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     */
    private List<Uri> uris;

    /**
     * 보충 자료 목록 / Supplementary material list
     */
    private List<SupplementaryMaterial> supplementaryMaterials;

    /**
     * 이력 / History
     */
    private PmcHistory history;

    /**
     * 출판 이력 / Publication history
     *
     * KR: 논문의 출판 이력 (접수, 승인, 온라인 출판 등의 이벤트).
     * EN: Publication history of the article (events such as received, accepted, online publication, etc.).
     *
     * DTD: pub-history?
     * Required: NO
     *
     * Note: JATS 1.4에서는 history 대신 pub-history 사용 권장.
     * Note: JATS 1.4 recommends using pub-history instead of history.
     */
    private PubHistory pubHistory;

    /**
     * 권한 / Permissions
     */
    private Permissions permissions;

    /**
     * 자체 URI 목록 / Self URI list
     */
    private List<SelfUri> selfUris;

    /**
     * 관련 논문 목록 / Related article list
     */
    private List<RelatedArticle> relatedArticles;

    /**
     * 관련 객체 목록 / Related object list
     */
    private List<RelatedObject> relatedObjects;

    /**
     * 초록 목록 / Abstract list
     */
    private List<PmcAbstract> abstracts;

    /**
     * 번역 초록 목록 / Translated abstract list
     */
    private List<TransAbstract> transAbstracts;

    /**
     * 키워드 그룹 목록 / Keyword group list
     */
    private List<KwdGroup> kwdGroups;

    /**
     * 연구비 그룹 목록 / Funding group list
     */
    private List<FundingGroup> fundingGroups;

    /**
     * 지원 그룹 목록 / Support group list
     */
    private List<SupportGroup> supportGroups;

    /**
     * 학회 정보 목록 / Conference list
     */
    private List<Conference> conferences;

    /**
     * 카운트 / Counts
     */
    private Counts counts;

    /**
     * 사용자 정의 메타 그룹 / Custom meta group
     */
    private CustomMetaGroup customMetaGroup;
}
