package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Note / 노트
 *
 * KR: 참조 또는 인용에 대한 노트. JATS 1.4 DTD 완전 준수 모델.
 *     참조 목록 내에서 추가 정보를 제공하는 데 사용됨.
 * EN: Note on a reference or citation. Fully compliant with JATS 1.4 DTD.
 *     Used to provide additional information within a reference list.
 *
 * DTD: <!ELEMENT note (%note-model;)*>
 *
 * DTD: <!ATTLIST note
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/note.html
 *
 * Example:
 * <note>
 *   <p>This paper was retracted on January 15, 2024.</p>
 * </note>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 노트의 콘텐츠 유형.
     * EN: Type of content in the note.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "retraction", "erratum", "update"
     */
    private String contentType;

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
     * KR: 노트의 레이블.
     * EN: Label for the note.
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 노트 내용 단락 목록.
     * EN: List of paragraphs in the note.
     *
     * DTD: %note-model; includes p
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 제품 정보 목록 / Product list
     *
     * KR: 제품 정보 목록 (리뷰의 경우).
     * EN: List of product information (for reviews).
     *
     * DTD: %note-model; includes product
     * Required: NO (0 or more)
     */
    private List<Product> products;
}
