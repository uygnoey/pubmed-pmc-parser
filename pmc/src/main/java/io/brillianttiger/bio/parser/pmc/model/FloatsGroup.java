package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * FloatsGroup / 플로트 그룹
 *
 * KR: 문서 내 부유 요소(그림, 테이블 등)를 담는 컨테이너. JATS 1.4 DTD 완전 준수 모델.
 *     본문과 별도로 배치되는 그림, 테이블, 박스 텍스트, 화학 구조 등을 포함.
 * EN: Container for floating elements (figures, tables, etc.) in document.
 *     Fully compliant with JATS 1.4 DTD.
 *     Contains figures, tables, boxed text, chemical structures that are placed separately from body text.
 *
 * DTD: <!ELEMENT floats-group (
 *          alternatives | block-alternatives | boxed-text | chem-struct-wrap |
 *          code | explanation | fig | fig-group | graphic | media | preformat |
 *          supplementary-material | table-wrap | table-wrap-group
 *      )*>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/floats-group.html
 *
 * Note: The floats-group element is used to collect all floating objects
 * (figures, tables, boxed text, etc.) that are referenced from the body
 * but may be placed at the end of the article or in a separate section.
 *
 * Example:
 * <floats-group>
 *     <fig id="fig1">...</fig>
 *     <fig id="fig2">...</fig>
 *     <table-wrap id="tbl1">...</table-wrap>
 *     <supplementary-material id="S1">...</supplementary-material>
 * </floats-group>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloatsGroup {

    // ========== Floating Elements / 부유 요소 ==========

    /**
     * 대안 목록 / Alternatives list
     *
     * KR: 다양한 포맷의 동일 콘텐츠 대안 목록.
     * EN: List of alternative representations of the same content.
     *
     * DTD: alternatives*
     * Required: NO (0 or more)
     */
    private List<Alternatives> alternatives;

    /**
     * 박스 텍스트 목록 / Boxed text list
     *
     * KR: 박스 텍스트 요소 목록.
     * EN: List of boxed text elements.
     *
     * DTD: boxed-text*
     * Required: NO (0 or more)
     */
    private List<BoxedText> boxedTexts;

    /**
     * 화학 구조 래퍼 목록 / Chemical structure wrapper list
     *
     * KR: 화학 구조 래퍼 요소 목록.
     * EN: List of chemical structure wrapper elements.
     *
     * DTD: chem-struct-wrap*
     * Required: NO (0 or more)
     */
    private List<ChemStructWrap> chemStructWraps;

    /**
     * 코드 목록 / Code list
     *
     * KR: 코드 블록 목록.
     * EN: List of code blocks.
     *
     * DTD: code*
     * Required: NO (0 or more)
     */
    private List<Code> codes;

    /**
     * 그림 목록 / Figure list
     *
     * KR: 그림/이미지 요소 목록.
     * EN: List of figure/image elements.
     *
     * DTD: fig*
     * Required: NO (0 or more)
     */
    private List<Fig> figs;

    /**
     * 그림 그룹 목록 / Figure group list
     *
     * KR: 관련 그림을 그룹화한 요소 목록.
     * EN: List of grouped figure elements.
     *
     * DTD: fig-group*
     * Required: NO (0 or more)
     */
    private List<FigGroup> figGroups;

    /**
     * 그래픽 목록 / Graphic list
     *
     * KR: 독립 그래픽 요소 목록.
     * EN: List of standalone graphic elements.
     *
     * DTD: graphic*
     * Required: NO (0 or more)
     */
    private List<Graphic> graphics;

    /**
     * 미디어 목록 / Media list
     *
     * KR: 미디어 요소 목록 (비디오, 오디오 등).
     * EN: List of media elements (video, audio, etc.).
     *
     * DTD: media*
     * Required: NO (0 or more)
     */
    private List<Media> medias;

    /**
     * Preformat 목록 / Preformat list
     *
     * KR: 미리 형식화된 텍스트 목록.
     * EN: List of preformatted text elements.
     *
     * DTD: preformat*
     * Required: NO (0 or more)
     */
    private List<Preformat> preformats;

    /**
     * 보충 자료 목록 / Supplementary material list
     *
     * KR: 보충 자료 요소 목록.
     * EN: List of supplementary material elements.
     *
     * DTD: supplementary-material*
     * Required: NO (0 or more)
     */
    private List<SupplementaryMaterial> supplementaryMaterials;

    /**
     * 테이블 래퍼 목록 / Table wrapper list
     *
     * KR: 테이블 래퍼 요소 목록.
     * EN: List of table wrapper elements.
     *
     * DTD: table-wrap*
     * Required: NO (0 or more)
     */
    private List<TableWrap> tableWraps;

    /**
     * 테이블 래퍼 그룹 목록 / Table wrapper group list
     *
     * KR: 관련 테이블을 그룹화한 요소 목록.
     * EN: List of grouped table wrapper elements.
     *
     * DTD: table-wrap-group*
     * Required: NO (0 or more)
     */
    private List<TableWrapGroup> tableWrapGroups;
}
