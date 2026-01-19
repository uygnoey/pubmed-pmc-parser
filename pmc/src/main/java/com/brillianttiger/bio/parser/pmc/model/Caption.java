package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Caption / 캡션
 *
 * KR: 그림/테이블 캡션. JATS 1.4 DTD 완전 준수 모델.
 * EN: Figure/Table caption. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT caption (
 *          title?,
 *          (p | fn-group)*
 *      )>
 *
 * DTD: <!ATTLIST caption
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          style CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/caption.html
 *
 * Example:
 * <caption>
 *     <title>Study results over time</title>
 *     <p>Figure shows the progression of patient recovery.</p>
 * </caption>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Caption {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 캡션의 콘텐츠 유형을 나타내는 문자열.
     * EN: String indicating the content type of the caption.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
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
     * 스타일 / Style
     *
     * KR: CSS 스타일 정보.
     * EN: CSS style information.
     *
     * DTD: style CDATA #IMPLIED
     * Required: NO
     */
    private String style;

    /**
     * XML 언어 / XML language
     *
     * KR: 캡션 내용의 언어 코드 (ISO 639).
     * EN: Language code for caption content (ISO 639).
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "ko", "ja"
     */
    private String xmlLang;

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 제목 / Title
     *
     * KR: 캡션의 제목 (짧은 설명).
     * EN: Title of the caption (short description).
     *
     * DTD: title?
     * Required: NO (0 or 1)
     */
    private Title title;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 캡션 내 단락 목록 (상세 설명).
     * EN: List of paragraphs in the caption (detailed description).
     *
     * DTD: p*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 각주 그룹 목록 / Footnote group list
     *
     * KR: 캡션 내 각주 그룹 목록.
     * EN: List of footnote groups in the caption.
     *
     * DTD: fn-group*
     * Required: NO (0 or more)
     */
    private List<FnGroup> fnGroups;
}
