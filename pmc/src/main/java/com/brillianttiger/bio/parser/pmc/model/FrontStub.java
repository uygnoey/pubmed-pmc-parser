package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FrontStub / 축약 전면부
 *
 * KR: 하위 논문(sub-article)이나 응답(response)을 위한 축약된 전면부 메타데이터.
 *     JATS 1.4 DTD 완전 준수 모델.
 *     저널 메타데이터 없이 논문 메타데이터만 포함하는 축약형.
 * EN: Abbreviated front matter for sub-articles or responses.
 *     Fully compliant with JATS 1.4 DTD.
 *     Contains only article metadata without journal metadata.
 *
 * DTD: <!ELEMENT front-stub (
 *          (article-id)*,
 *          (article-version | article-version-alternatives)*,
 *          article-categories?,
 *          title-group?,
 *          (contrib-group | aff | aff-alternatives | x)*,
 *          author-notes?,
 *          (pub-date | pub-date-not-available)*,
 *          volume?,
 *          (volume-id)*,
 *          volume-series?,
 *          issue?,
 *          (issue-id)*,
 *          (issue-title)*,
 *          (issue-sponsor)*,
 *          issue-part?,
 *          (volume-issue-group)*,
 *          (isbn)*,
 *          supplement?,
 *          (fpage | (fpage-end, fpage?))?,
 *          lpage?,
 *          page-range?,
 *          (elocation-id)*,
 *          (email | ext-link | uri | product | supplementary-material)*,
 *          history?,
 *          pub-history?,
 *          permissions?,
 *          (self-uri)*,
 *          (related-article | related-object)*,
 *          (abstract)*,
 *          (trans-abstract)*,
 *          (kwd-group)*,
 *          (funding-group)*,
 *          (support-group)*,
 *          (conference)*,
 *          counts?,
 *          custom-meta-group?
 *      )>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/front-stub.html
 *
 * Note: front-stub is used when a sub-article or response doesn't need full
 * journal metadata but only article-specific metadata. It's essentially
 * article-meta content without the journal-meta wrapper.
 *
 * Example:
 * <sub-article article-type="reply">
 *     <front-stub>
 *         <title-group>
 *             <article-title>Author's Response</article-title>
 *         </title-group>
 *         <contrib-group>...</contrib-group>
 *     </front-stub>
 *     <body>...</body>
 * </sub-article>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrontStub {

    // ========== Article Identification / 논문 식별 ==========

    /**
     * 논문 ID 목록 / Article ID list
     *
     * KR: 논문 식별자 목록 (DOI, PMID 등).
     * EN: List of article identifiers (DOI, PMID, etc.).
     *
     * DTD: (article-id)*
     * Required: NO (0 or more)
     */
    private List<PmcArticleId> articleIds;

    /**
     * 논문 카테고리 / Article categories
     *
     * KR: 논문의 주제 분류.
     * EN: Subject categories for the article.
     *
     * DTD: article-categories?
     * Required: NO
     */
    private ArticleCategories articleCategories;

    /**
     * 제목 그룹 / Title group
     *
     * KR: 논문 제목, 부제목, 번역 제목 등.
     * EN: Article title, subtitle, translated titles, etc.
     *
     * DTD: title-group?
     * Required: NO
     */
    private TitleGroup titleGroup;

    // ========== Contributors / 기여자 ==========

    /**
     * 기여자 그룹 목록 / Contributor group list
     *
     * KR: 저자, 편집자 등 기여자 그룹 목록.
     * EN: List of contributor groups (authors, editors, etc.).
     *
     * DTD: contrib-group*
     * Required: NO (0 or more)
     */
    private List<ContribGroup> contribGroups;

    /**
     * 소속 목록 / Affiliation list
     *
     * KR: 기관 소속 정보 목록.
     * EN: List of institutional affiliations.
     *
     * DTD: aff*
     * Required: NO (0 or more)
     */
    private List<Aff> affiliations;

    /**
     * 소속 대안 목록 / Affiliation alternatives list
     *
     * KR: 소속의 대안 표기 목록.
     * EN: List of alternative affiliation representations.
     *
     * DTD: aff-alternatives*
     * Required: NO (0 or more)
     */
    private List<AffAlternatives> affAlternatives;

    /**
     * 저자 노트 / Author notes
     *
     * KR: 저자 관련 노트 (교신저자, 현재 소속 등).
     * EN: Author-related notes (corresponding author, current affiliation, etc.).
     *
     * DTD: author-notes?
     * Required: NO
     */
    private AuthorNotes authorNotes;

    // ========== Publication Info / 출판 정보 ==========

    /**
     * 출판일 목록 / Publication date list
     *
     * KR: 출판일 정보 목록.
     * EN: List of publication dates.
     *
     * DTD: pub-date*
     * Required: NO (0 or more)
     */
    private List<PubDate> pubDates;

    /**
     * 권 / Volume
     *
     * KR: 저널 권 번호.
     * EN: Journal volume number.
     *
     * DTD: volume?
     * Required: NO
     */
    private Volume volume;

    /**
     * 권 ID 목록 / Volume ID list
     *
     * KR: 권 식별자 목록.
     * EN: List of volume identifiers.
     *
     * DTD: volume-id*
     * Required: NO (0 or more)
     */
    private List<VolumeId> volumeIds;

    /**
     * 권 시리즈 / Volume series
     *
     * KR: 권 시리즈 정보.
     * EN: Volume series information.
     *
     * DTD: volume-series?
     * Required: NO
     */
    private VolumeSeries volumeSeries;

    /**
     * 호 / Issue
     *
     * KR: 저널 호 번호.
     * EN: Journal issue number.
     *
     * DTD: issue?
     * Required: NO
     */
    private Issue issue;

    /**
     * 호 ID 목록 / Issue ID list
     *
     * KR: 호 식별자 목록.
     * EN: List of issue identifiers.
     *
     * DTD: issue-id*
     * Required: NO (0 or more)
     */
    private List<IssueId> issueIds;

    /**
     * 호 제목 목록 / Issue title list
     *
     * KR: 특별호 제목 등.
     * EN: Special issue titles, etc.
     *
     * DTD: issue-title*
     * Required: NO (0 or more)
     */
    private List<IssueTitle> issueTitles;

    /**
     * 호 스폰서 목록 / Issue sponsor list
     *
     * KR: 호 스폰서 정보 목록.
     * EN: List of issue sponsors.
     *
     * DTD: issue-sponsor*
     * Required: NO (0 or more)
     */
    private List<IssueSponsor> issueSponsors;

    /**
     * 호 파트 / Issue part
     *
     * KR: 호 파트 정보.
     * EN: Issue part information.
     *
     * DTD: issue-part?
     * Required: NO
     */
    private String issuePart;

    /**
     * ISBN 목록 / ISBN list
     *
     * KR: ISBN 목록.
     * EN: List of ISBNs.
     *
     * DTD: isbn*
     * Required: NO (0 or more)
     */
    private List<PmcIsbn> isbns;

    /**
     * 보충 정보 / Supplement
     *
     * KR: 보충호 정보.
     * EN: Supplement information.
     *
     * DTD: supplement?
     * Required: NO
     */
    private Supplement supplement;

    // ========== Page Info / 페이지 정보 ==========

    /**
     * 시작 페이지 / First page
     *
     * KR: 시작 페이지 번호.
     * EN: First page number.
     *
     * DTD: fpage?
     * Required: NO
     */
    private Fpage fpage;

    /**
     * 종료 페이지 / Last page
     *
     * KR: 종료 페이지 번호.
     * EN: Last page number.
     *
     * DTD: lpage?
     * Required: NO
     */
    private Lpage lpage;

    /**
     * 페이지 범위 / Page range
     *
     * KR: 페이지 범위 문자열.
     * EN: Page range string.
     *
     * DTD: page-range?
     * Required: NO
     */
    private String pageRange;

    /**
     * 전자 위치 ID 목록 / E-location ID list
     *
     * KR: 전자 논문 위치 식별자 목록.
     * EN: List of electronic article location identifiers.
     *
     * DTD: elocation-id*
     * Required: NO (0 or more)
     */
    private List<ElocationId> elocationIds;

    // ========== Links / 링크 ==========

    /**
     * 이메일 목록 / Email list
     *
     * KR: 관련 이메일 주소 목록.
     * EN: List of related email addresses.
     *
     * DTD: email*
     * Required: NO (0 or more)
     */
    private List<Email> emails;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 외부 링크 목록.
     * EN: List of external links.
     *
     * DTD: ext-link*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;

    /**
     * URI 목록 / URI list
     *
     * KR: URI 목록.
     * EN: List of URIs.
     *
     * DTD: uri*
     * Required: NO (0 or more)
     */
    private List<Uri> uris;

    /**
     * 보충 자료 목록 / Supplementary material list
     *
     * KR: 보충 자료 목록.
     * EN: List of supplementary materials.
     *
     * DTD: supplementary-material*
     * Required: NO (0 or more)
     */
    private List<SupplementaryMaterial> supplementaryMaterials;

    // ========== History / 이력 ==========

    /**
     * 이력 / History
     *
     * KR: 논문 이력 (접수일, 수정일, 승인일 등).
     * EN: Article history (received, revised, accepted dates, etc.).
     *
     * DTD: history?
     * Required: NO
     */
    private History history;

    /**
     * 출판 이력 / Publication history
     *
     * KR: 출판 이력 (JATS 1.2+).
     * EN: Publication history (JATS 1.2+).
     *
     * DTD: pub-history?
     * Required: NO
     */
    private PubHistory pubHistory;

    /**
     * 권한 / Permissions
     *
     * KR: 저작권 및 라이선스 정보.
     * EN: Copyright and license information.
     *
     * DTD: permissions?
     * Required: NO
     */
    private Permissions permissions;

    /**
     * 자기 참조 URI 목록 / Self URI list
     *
     * KR: 논문 자체를 가리키는 URI 목록.
     * EN: List of URIs pointing to the article itself.
     *
     * DTD: self-uri*
     * Required: NO (0 or more)
     */
    private List<SelfUri> selfUris;

    // ========== Related Content / 관련 콘텐츠 ==========

    /**
     * 관련 논문 목록 / Related article list
     *
     * KR: 관련 논문 목록.
     * EN: List of related articles.
     *
     * DTD: related-article*
     * Required: NO (0 or more)
     */
    private List<RelatedArticle> relatedArticles;

    /**
     * 관련 객체 목록 / Related object list
     *
     * KR: 관련 객체 목록.
     * EN: List of related objects.
     *
     * DTD: related-object*
     * Required: NO (0 or more)
     */
    private List<RelatedObject> relatedObjects;

    // ========== Abstract & Keywords / 초록 및 키워드 ==========

    /**
     * 초록 목록 / Abstract list
     *
     * KR: 초록 목록.
     * EN: List of abstracts.
     *
     * DTD: abstract*
     * Required: NO (0 or more)
     */
    private List<PmcAbstract> abstracts;

    /**
     * 번역 초록 목록 / Translated abstract list
     *
     * KR: 번역된 초록 목록.
     * EN: List of translated abstracts.
     *
     * DTD: trans-abstract*
     * Required: NO (0 or more)
     */
    private List<TransAbstract> transAbstracts;

    /**
     * 키워드 그룹 목록 / Keyword group list
     *
     * KR: 키워드 그룹 목록.
     * EN: List of keyword groups.
     *
     * DTD: kwd-group*
     * Required: NO (0 or more)
     */
    private List<KwdGroup> kwdGroups;

    // ========== Funding / 펀딩 ==========

    /**
     * 펀딩 그룹 목록 / Funding group list
     *
     * KR: 펀딩/연구비 지원 정보 그룹 목록.
     * EN: List of funding/grant support information groups.
     *
     * DTD: funding-group*
     * Required: NO (0 or more)
     */
    private List<FundingGroup> fundingGroups;

    /**
     * 지원 그룹 목록 / Support group list
     *
     * KR: 지원 정보 그룹 목록 (JATS 1.2+).
     * EN: List of support information groups (JATS 1.2+).
     *
     * DTD: support-group*
     * Required: NO (0 or more)
     */
    private List<SupportGroup> supportGroups;

    // ========== Conference / 학회 ==========

    /**
     * 학회 목록 / Conference list
     *
     * KR: 학회 정보 목록.
     * EN: List of conference information.
     *
     * DTD: conference*
     * Required: NO (0 or more)
     */
    private List<Conference> conferences;

    // ========== Counts / 개수 ==========

    /**
     * 개수 정보 / Counts
     *
     * KR: 페이지, 그림, 테이블 등의 개수.
     * EN: Counts of pages, figures, tables, etc.
     *
     * DTD: counts?
     * Required: NO
     */
    private Counts counts;

    // ========== Custom Metadata / 사용자 정의 메타데이터 ==========

    /**
     * 사용자 정의 메타데이터 그룹 / Custom metadata group
     *
     * KR: 사용자 정의 메타데이터 그룹.
     * EN: Custom metadata group.
     *
     * DTD: custom-meta-group?
     * Required: NO
     */
    private CustomMetaGroup customMetaGroup;
}
