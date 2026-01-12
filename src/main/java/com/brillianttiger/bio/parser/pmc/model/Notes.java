package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Notes / 노트
 *
 * KR: 편집자 또는 출판사 노트. JATS 1.4 DTD 완전 준수 모델.
 * EN: Editorial or publisher notes. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT notes (label?, title?, (%sec-opt-title-model;)*, (sec)*)>
 *
 * DTD: <!ATTLIST notes
 *          id ID #IMPLIED
 *          notes-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/notes.html
 *
 * Common notes-type values:
 * - editor-note: Note from the editor
 * - update: Update or correction
 * - disclaimer: Legal disclaimer
 * - conflict-of-interest: Conflict of interest statement
 * - data-availability: Data availability statement
 * - ethics: Ethics statement
 * - proof: Proof note
 *
 * Example:
 * <notes notes-type="editor-note">
 *   <title>Editor's Note</title>
 *   <p>This article was fast-tracked for publication due to its public health importance.</p>
 * </notes>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notes {

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
     * 노트 유형 / Notes type
     *
     * KR: 노트의 유형.
     * EN: Type of the note.
     *
     * DTD: notes-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values: editor-note, update, disclaimer, conflict-of-interest, data-availability
     */
    private String notesType;

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
     * KR: 노트의 레이블.
     * EN: Label for the note.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 노트의 제목.
     * EN: Title of the note.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 노트 내용 단락 목록.
     * EN: List of paragraphs in the note.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 노트 내 섹션 목록.
     * EN: List of sections in the note.
     *
     * DTD: (sec)*
     * Required: NO (0 or more)
     */
    private List<Sec> sections;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 노트 내 그래픽 목록.
     * EN: List of graphics in the note.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 노트 내 미디어 목록.
     * EN: List of media in the note.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 외부 링크 목록 / External link list
     *
     * KR: 노트 내 외부 링크 목록.
     * EN: List of external links in the note.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<ExtLink> extLinks;
}
