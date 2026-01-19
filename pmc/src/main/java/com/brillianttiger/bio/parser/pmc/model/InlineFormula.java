package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS inline-formula 요소 / JATS inline-formula element
 *
 * <p>DTD: {@code <!ELEMENT inline-formula (tex-math | mml:math | graphic)+>}</p>
 *
 * <p>
 * KR: 인라인 수식<br>
 * EN: Inline formula
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/inline-formula.html">
 *      JATS inline-formula Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InlineFormula {

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
     * TeX 수식
     */
    private String texMath;

    /**
     * MathML
     */
    private String mmlMath;

}
