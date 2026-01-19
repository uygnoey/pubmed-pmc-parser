package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * App / 부록
 *
 * KR: 개별 부록 (논문 끝에 포함되는 보충 자료). JATS 1.4 DTD 완전 준수 모델.
 * EN: Individual appendix (supplementary material at the end of an article). Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT app (%sec-model;)>
 *
 * DTD: <!ATTLIST app
 *          content-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:base CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/app.html
 *
 * Note: %sec-model; is the section content model which includes:
 * label?, title?, (%sec-opt-title-model;)*, (sec)*, (%sec-back-matter-mix;)*
 *
 * Example:
 * <app id="app1" content-type="methods">
 *   <label>Appendix A</label>
 *   <title>Supplementary Methods</title>
 *   <sec>
 *     <title>Cell Culture</title>
 *     <p>Detailed protocols for cell culture...</p>
 *   </sec>
 *   <sec>
 *     <title>Statistical Analysis</title>
 *     <p>Detailed statistical methods...</p>
 *   </sec>
 * </app>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class App {

    // ========== Attributes / 속성 ==========

    /**
     * 콘텐츠 유형 / Content type
     *
     * KR: 부록의 콘텐츠 유형.
     * EN: Type of content in the appendix.
     *
     * DTD: content-type CDATA #IMPLIED
     * Required: NO
     *
     * Example: "methods", "data", "code", "supplementary-material"
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
     * KR: 부록의 레이블 (예: "Appendix A").
     * EN: Label for the appendix (e.g., "Appendix A").
     *
     * DTD: label?
     * Required: NO
     */
    private Label label;

    /**
     * 제목 / Title
     *
     * KR: 부록의 제목.
     * EN: Title of the appendix.
     *
     * DTD: title?
     * Required: NO
     */
    private Title title;

    /**
     * 부제목 목록 / Subtitle list
     *
     * KR: 부록의 부제목 목록.
     * EN: List of subtitles for the appendix.
     *
     * DTD: subtitle*
     * Required: NO (0 or more)
     */
    private List<Subtitle> subtitles;

    /**
     * 대체 제목 목록 / Alternative title list
     *
     * KR: 대체 제목 목록.
     * EN: List of alternative titles.
     *
     * DTD: alt-title*
     * Required: NO (0 or more)
     */
    private List<AltTitle> altTitles;

    /**
     * 단락 목록 / Paragraph list
     *
     * KR: 부록 내용 단락 목록.
     * EN: List of paragraphs in the appendix.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<P> paragraphs;

    /**
     * 섹션 목록 / Section list
     *
     * KR: 하위 섹션 목록.
     * EN: List of subsections.
     *
     * DTD: (sec)*
     * Required: NO (0 or more)
     */
    private List<Sec> sections;

    /**
     * 그림 목록 / Figure list
     *
     * KR: 부록 내 그림 목록.
     * EN: List of figures in the appendix.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<Fig> figures;

    /**
     * 테이블 래퍼 목록 / Table wrapper list
     *
     * KR: 부록 내 테이블 목록.
     * EN: List of tables in the appendix.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<TableWrap> tableWraps;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 부록 내 그래픽 목록.
     * EN: List of graphics in the appendix.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 부록 내 미디어 객체 목록.
     * EN: List of media objects in the appendix.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 보충 자료 목록 / Supplementary material list
     *
     * KR: 부록 내 보충 자료 목록.
     * EN: List of supplementary materials in the appendix.
     *
     * DTD: (%sec-opt-title-model;)*
     * Required: NO (0 or more)
     */
    private List<SupplementaryMaterial> supplementaryMaterials;

    /**
     * 참조 목록 / Reference list
     *
     * KR: 부록 내 참고문헌 목록.
     * EN: List of references in the appendix.
     *
     * DTD: (%sec-back-matter-mix;)*
     * Required: NO (0 or more)
     */
    private List<RefList> refLists;

    /**
     * 용어집 목록 / Glossary list
     *
     * KR: 부록 내 용어집 목록.
     * EN: List of glossaries in the appendix.
     *
     * DTD: (%sec-back-matter-mix;)*
     * Required: NO (0 or more)
     */
    private List<Glossary> glossaries;

    /**
     * 각주 그룹 목록 / Footnote group list
     *
     * KR: 부록 내 각주 그룹 목록.
     * EN: List of footnote groups in the appendix.
     *
     * DTD: (%sec-back-matter-mix;)*
     * Required: NO (0 or more)
     */
    private List<FnGroup> fnGroups;

    /**
     * 권한 정보 / Permissions
     *
     * KR: 부록의 저작권 및 라이선스 정보.
     * EN: Copyright and license information for the appendix.
     *
     * DTD: permissions?
     * Required: NO
     */
    private Permissions permissions;
}
