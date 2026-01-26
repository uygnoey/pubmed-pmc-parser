package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Alternatives / 대안 표현 컨테이너
 *
 * KR: 동일 콘텐츠의 대안 표현을 담는 컨테이너. JATS 1.4 DTD 완전 준수 모델.
 * EN: Container for alternative representations of the same content. Fully compliant with JATS 1.4 DTD.
 *
 * DTD: <!ELEMENT alternatives (
 *          (array | chem-struct | code | graphic | inline-graphic |
 *           inline-supplementary-material | media | mml:math |
 *           object-id | preformat | private-char | supplementary-material |
 *           table | tex-math | textual-form)+
 *      )>
 *
 * DTD: <!ATTLIST alternatives
 *          id ID #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/alternatives.html
 *
 * Note: This element is used to provide multiple representations of the same content,
 * such as:
 * - A graphic image and its MathML equivalent for an equation
 * - A PNG graphic and a high-resolution TIFF version
 * - A table in XHTML and as a graphic image
 * - Plain text and MathML for a formula
 *
 * Example:
 * <alternatives>
 *     <graphic xlink:href="equation1.png" mimetype="image" mime-subtype="png"/>
 *     <mml:math>...</mml:math>
 * </alternatives>
 *
 * Example:
 * <alternatives>
 *     <graphic xlink:href="fig1-lowres.png" specific-use="print"/>
 *     <graphic xlink:href="fig1-hires.tiff" specific-use="online"/>
 * </alternatives>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alternatives {

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

    // ========== Child Elements / 자식 요소 ==========

    /**
     * 객체 ID 목록 / Object ID list
     *
     * KR: 대안의 식별자 목록.
     * EN: List of identifiers for the alternatives.
     *
     * DTD: object-id*
     * Required: NO (0 or more)
     */
    private List<ObjectId> objectIds;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 그래픽/이미지 대안 목록.
     * EN: List of graphic/image alternatives.
     *
     * DTD: graphic*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 미디어 대안 목록.
     * EN: List of media alternatives.
     *
     * DTD: media*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * 테이블 목록 / Table list
     *
     * KR: XHTML 테이블 대안 목록.
     * EN: List of XHTML table alternatives.
     *
     * DTD: table*
     * Required: NO (0 or more)
     */
    private List<Table> tables;

    /**
     * 코드 목록 / Code list
     *
     * KR: 코드 대안 목록.
     * EN: List of code alternatives.
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codes;

    /**
     * 텍스트 형식 목록 / Textual form list
     *
     * KR: 텍스트 형식 대안 목록.
     * EN: List of textual form alternatives.
     *
     * DTD: textual-form*
     * Required: NO (0 or more)
     */
    private List<TextualForm> textualForms;

    /**
     * TeX 수식 목록 / TeX math list
     *
     * KR: TeX 수학 표현 대안 목록.
     * EN: List of TeX math alternatives.
     *
     * DTD: tex-math*
     * Required: NO (0 or more)
     */
    private List<TexMath> texMaths;

    /**
     * 보충 자료 목록 / Supplementary material list
     *
     * KR: 보충 자료 대안 목록.
     * EN: List of supplementary material alternatives.
     *
     * DTD: supplementary-material*
     * Required: NO (0 or more)
     */
    private List<SupplementaryMaterial> supplementaryMaterials;

    /**
     * Preformat 목록 / Preformat list
     *
     * KR: 미리 형식화된 텍스트 대안 목록.
     * EN: List of preformatted text alternatives.
     *
     * DTD: preformat*
     * Required: NO (0 or more)
     */
    private List<Preformat> preformats;

    /**
     * 배열 목록 / Array list
     *
     * KR: 배열/행렬 대안 목록.
     * EN: List of array/matrix alternatives.
     *
     * DTD: array*
     * Required: NO (0 or more)
     */
    private List<Array> arrays;

    /**
     * 인라인 그래픽 목록 / Inline graphic list
     *
     * KR: 인라인 그래픽 대안 목록.
     * EN: List of inline graphic alternatives.
     *
     * DTD: inline-graphic*
     * Required: NO (0 or more)
     */
    private List<InlineGraphic> inlineGraphics;

    /**
     * MathML 수식 원본 / MathML raw content
     *
     * KR: MathML 수식의 원본 XML (mml:math).
     *     StAX 파싱 시 네임스페이스 처리를 위해 문자열로 저장.
     * EN: Raw XML of MathML formula (mml:math).
     *     Stored as string for namespace handling during StAX parsing.
     *
     * DTD: mml:math
     * Required: NO (0 or more)
     */
    private String mathml;
}
