package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Back / 후면부
 *
 * KR: 논문의 후면부 (참고문헌, 감사의 글, 부록 등). JATS 1.4 DTD 완전 준수 모델.
 * EN: Article back matter (references, acknowledgments, appendices, etc.). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT back (label?, title*, ack*, app-group*, bio*, fn-group*,
 *                      glossary*, ref-list*, notes*, sec*)>
 *
 * DTD: <!ATTLIST back
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/back.html
 *
 * Example:
 * <back>
 *   <ack>
 *     <title>Acknowledgments</title>
 *     <p>We thank Dr. Smith for helpful discussions.</p>
 *   </ack>
 *   <ref-list>...</ref-list>
 *   <fn-group>...</fn-group>
 * </back>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Back {

    // ========== Attributes / 속성 ==========

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자.
     * EN: Unique identifier within the XML document.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 특정 용도나 목적을 나타내는 문자열.
     * EN: String indicating specific use or purpose.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * XML Base / XML base
     *
     * KR: 상대 URI 해석을 위한 기본 URI.
     * EN: Base URI for resolving relative URIs.
     *
     * DTD: xml:base CDATA #IMPLIED
     * Required: NO
     */
    private String xmlBase;

    /**
     * XML 언어 / XML language
     *
     * KR: 내용의 언어 코드 (ISO 639).
     * EN: Language code for content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 레이블 / Label
     *
     * KR: 후면부의 레이블 (선택적).
     * EN: Optional label for back matter.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 목록 / Title list
     *
     * KR: 후면부 제목 목록.
     * EN: List of titles for back matter.
     *
     * DTD: title*
     * Required: NO (0 or more)
     */
    private List<Title> titles;

    /**
     * 감사의 글 목록 / Acknowledgment list
     *
     * KR: 감사의 글 목록.
     * EN: List of acknowledgments.
     *
     * DTD: ack*
     * Required: NO (0 or more)
     */
    private List<Ack> acknowledgments;

    /**
     * 부록 그룹 목록 / Appendix group list
     *
     * KR: 부록 그룹 목록.
     * EN: List of appendix groups.
     *
     * DTD: app-group*
     * Required: NO (0 or more)
     */
    private List<AppGroup> appGroups;

    /**
     * 저자 약력 목록 / Biography list
     *
     * KR: 저자 약력 목록.
     * EN: List of author biographies.
     *
     * DTD: bio*
     * Required: NO (0 or more)
     */
    private List<Bio> biographies;

    /**
     * 각주 그룹 목록 / Footnote group list
     *
     * KR: 각주 그룹 목록.
     * EN: List of footnote groups.
     *
     * DTD: fn-group*
     * Required: NO (0 or more)
     */
    private List<FnGroup> fnGroups;

    /**
     * 용어집 목록 / Glossary list
     *
     * KR: 용어집 목록.
     * EN: List of glossaries.
     *
     * DTD: glossary*
     * Required: NO (0 or more)
     */
    private List<Glossary> glossaries;

    /**
     * 참조 목록 / Reference list
     *
     * KR: 참고문헌 목록.
     * EN: List of reference lists.
     *
     * DTD: ref-list*
     * Required: NO (0 or more)
     */
    private List<RefList> refLists;

    /**
     * 노트 목록 / Notes list
     *
     * KR: 노트 목록.
     * EN: List of notes.
     *
     * DTD: notes*
     * Required: NO (0 or more)
     */
    private List<Notes> notesList;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 구조화된 섹션 목록.
     * EN: List of structured sections.
     *
     * DTD: sec*
     * Required: NO (0 or more)
     */
    private List<Sec> sections;
}
