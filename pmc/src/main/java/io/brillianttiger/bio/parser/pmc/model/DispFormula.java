package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS disp-formula 요소 / JATS disp-formula element
 *
 * <p>DTD: {@code <!ELEMENT disp-formula (label?, (tex-math | mml:math | graphic)+, alternatives?)>}</p>
 *
 * <p>
 * KR: 표시 수식<br>
 * EN: Display formula
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/disp-formula.html">
 *      JATS disp-formula Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispFormula {

    /**
     * 텍스트 내용 / Text content
     */
    private String value;

    /**
     * 공통 속성: id
     */
    private String id;

    /**
     * 공통 속성: xml:lang
     */
    private String xmlLang;

    /**
     * 라벨
     */
    private String label;

    /**
     * TeX 수식
     */
    private String texMath;

    /**
     * MathML
     */
    private String mmlMath;

}
