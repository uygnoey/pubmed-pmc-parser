package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Front / 전면부
 *
 * KR: 논문의 메타데이터를 포함하는 전면부.
 *     저널 정보, 논문 정보, 저자, 초록 등의 메타데이터를 포함.
 * EN: Front matter containing article metadata.
 *     Includes journal info, article info, authors, abstract, etc.
 *
 * DTD: <!ELEMENT front (
 *          journal-meta?,
 *          article-meta,
 *          (def-list | list | ack | bio | fn-group | glossary | notes)*
 *      )>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/front.html
 *
 * Structure:
 * - journal-meta: 저널 메타데이터 (선택) / Journal metadata (optional)
 * - article-meta: 논문 메타데이터 (필수) / Article metadata (required)
 * - Additional elements: 정의 목록, 감사의 글, 각주 등 / Definition lists, acknowledgments, footnotes, etc.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Front {

    /**
     * 저널 메타데이터 / Journal metadata
     *
     * KR: 저널 관련 메타데이터.
     *     저널 ID, 제목, ISSN, 출판사 등을 포함.
     * EN: Journal-related metadata.
     *     Includes journal ID, title, ISSN, publisher, etc.
     *
     * DTD: journal-meta?
     * Required: NO
     */
    private JournalMeta journalMeta;

    /**
     * 논문 메타데이터 (필수) / Article metadata (required)
     *
     * KR: 논문 관련 메타데이터.
     *     논문 ID, 제목, 저자, 초록, 키워드 등을 포함.
     * EN: Article-related metadata.
     *     Includes article ID, title, authors, abstract, keywords, etc.
     *
     * DTD: article-meta
     * Required: YES
     */
    private ArticleMeta articleMeta;

    /**
     * 정의 목록 / Definition list
     *
     * KR: 용어와 정의의 목록.
     * EN: List of terms and definitions.
     *
     * DTD: def-list*
     * Required: NO
     */
    private List<Object> defLists;

    /**
     * 목록 / List
     *
     * KR: 일반 목록 요소.
     * EN: General list elements.
     *
     * DTD: list*
     * Required: NO
     */
    private List<Object> lists;

    /**
     * 감사의 글 목록 / Acknowledgments list
     *
     * KR: 감사의 글, 기여자 목록 등.
     * EN: Acknowledgments, contributor lists, etc.
     *
     * DTD: ack*
     * Required: NO
     */
    private List<Ack> acks;

    /**
     * 약력 목록 / Biography list
     *
     * KR: 저자 약력, 이력 등.
     * EN: Author biographies, profiles, etc.
     *
     * DTD: bio*
     * Required: NO
     */
    private List<Bio> bios;

    /**
     * 각주 그룹 목록 / Footnote group list
     *
     * KR: 각주 그룹.
     * EN: Footnote groups.
     *
     * DTD: fn-group*
     * Required: NO
     */
    private List<FnGroup> fnGroups;

    /**
     * 용어집 목록 / Glossary list
     *
     * KR: 용어집, 용어 해설.
     * EN: Glossaries, term explanations.
     *
     * DTD: glossary*
     * Required: NO
     */
    private List<Glossary> glossaries;

    /**
     * 노트 목록 / Notes list
     *
     * KR: 추가 노트, 주석 등.
     * EN: Additional notes, annotations, etc.
     *
     * DTD: notes*
     * Required: NO
     */
    private List<Notes> notesList;
}
