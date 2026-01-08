package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Back / 후면부
 *
 * DTD: <!ELEMENT back (label?, title*, ack*, app-group*, bio*, fn-group*,
 *                      glossary*, ref-list*, notes*, sec*)>
 *
 * KR: 논문의 후면부 (참고문헌, 감사의 글 등)
 * EN: Article back matter (references, acknowledgments, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Back {

    /**
     * 레이블 / Label
     */
    private Label label;

    /**
     * 제목 목록 / Title list
     */
    private List<Title> titles;

    /**
     * 감사의 글 목록 / Acknowledgment list
     */
    private List<Ack> acknowledgments;

    /**
     * 부록 그룹 목록 / Appendix group list
     */
    private List<AppGroup> appGroups;

    /**
     * 저자 약력 목록 / Biography list
     */
    private List<Bio> biographies;

    /**
     * 각주 그룹 목록 / Footnote group list
     */
    private List<FnGroup> fnGroups;

    /**
     * 용어집 목록 / Glossary list
     */
    private List<Glossary> glossaries;

    /**
     * 참조 목록 / Reference list
     */
    private List<RefList> refLists;

    /**
     * 노트 목록 / Notes list
     */
    private List<Notes> notesList;

    /**
     * 섹션 목록 / Section list
     */
    private List<Sec> sections;
}
