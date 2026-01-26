package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JournalMeta / 저널 메타데이터
 *
 * KR: 저널의 메타데이터 정보를 담는 요소.
 *     저널 ID, 제목, ISSN, 출판사 등의 정보를 포함.
 * EN: Element containing journal metadata information.
 *     Includes journal ID, title, ISSN, publisher, etc.
 *
 * DTD: <!ELEMENT journal-meta (
 *          (journal-id+, journal-title-group*,
 *           (contrib-group | aff | aff-alternatives)*,
 *           issn*, issn-l?, isbn*, publisher?, notes*, self-uri*)+,
 *          custom-meta-group*
 *      )>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/journal-meta.html
 *
 * Note: JATS 1.4 DTD의 완전한 구조를 지원.
 *       journal-id는 1개 이상 필수(+), 나머지는 선택적(*).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalMeta {

    /**
     * 저널 ID 목록 (1개 이상 필수) / Journal ID list (at least one required)
     *
     * KR: 저널을 식별하는 다양한 ID 목록.
     *     NLM-TA, PMC, DOI, Publisher ID 등을 포함.
     * EN: List of various IDs identifying the journal.
     *     Includes NLM-TA, PMC, DOI, Publisher ID, etc.
     *
     * DTD: journal-id+
     * Required: YES (at least one)
     */
    private List<JournalId> journalIds;

    /**
     * 저널 제목 그룹 목록 / Journal title group list
     *
     * KR: 저널의 다양한 형식의 제목들.
     *     정식 제목, 약칭, 번역 제목 등을 포함.
     * EN: Various formats of journal titles.
     *     Includes full title, abbreviations, translated titles, etc.
     *
     * DTD: journal-title-group*
     * Required: NO
     */
    private List<JournalTitleGroup> journalTitleGroups;

    /**
     * 기여자 그룹 목록 / Contributor group list
     *
     * KR: 저널 수준의 기여자 정보 (편집자 등).
     * EN: Journal-level contributor information (editors, etc.).
     *
     * DTD: contrib-group*
     * Required: NO
     */
    private List<ContribGroup> contribGroups;

    /**
     * 소속 정보 목록 / Affiliation list
     *
     * KR: 저널 수준의 소속 정보.
     * EN: Journal-level affiliation information.
     *
     * DTD: aff*
     * Required: NO
     */
    private List<Aff> affs;

    /**
     * 소속 대안 목록 / Affiliation alternatives list
     *
     * KR: 소속 정보의 대안 표현.
     * EN: Alternative representations of affiliation.
     *
     * DTD: aff-alternatives*
     * Required: NO
     */
    private List<AffAlternatives> affAlternatives;

    /**
     * ISSN 목록 / ISSN list
     *
     * KR: 저널의 ISSN 목록 (인쇄본/전자본).
     * EN: Journal's ISSN list (print/electronic).
     *
     * DTD: issn*
     * Required: NO
     */
    private List<Issn> issns;

    /**
     * Linking ISSN / 연결 ISSN
     *
     * KR: ISSN-L (Linking ISSN) - 모든 매체의 저널을 연결하는 ISSN.
     * EN: ISSN-L (Linking ISSN) - ISSN linking all media of the journal.
     *
     * DTD: issn-l?
     * Required: NO
     */
    private String issnL;

    /**
     * ISBN 목록 / ISBN list
     *
     * KR: 저널의 ISBN 목록 (주로 책 형태 저널).
     * EN: Journal's ISBN list (mainly for book-form journals).
     *
     * DTD: isbn*
     * Required: NO
     */
    private List<PmcIsbn> isbns;

    /**
     * 출판사 / Publisher
     *
     * KR: 저널 출판사 정보.
     * EN: Journal publisher information.
     *
     * DTD: publisher?
     * Required: NO
     */
    private Publisher publisher;

    /**
     * 노트 목록 / Notes list
     *
     * KR: 저널에 대한 추가 노트.
     * EN: Additional notes about the journal.
     *
     * DTD: notes*
     * Required: NO
     */
    private List<Notes> notesList;

    /**
     * Self URI 목록 / Self URI list
     *
     * KR: 저널 자체를 가리키는 URI 목록.
     * EN: List of URIs pointing to the journal itself.
     *
     * DTD: self-uri*
     * Required: NO
     */
    private List<String> selfUris;

    /**
     * 커스텀 메타데이터 그룹 목록 / Custom metadata group list
     *
     * KR: 사용자 정의 메타데이터 그룹.
     * EN: Custom metadata groups.
     *
     * DTD: custom-meta-group*
     * Required: NO
     */
    private List<CustomMetaGroup> customMetaGroups;
}
