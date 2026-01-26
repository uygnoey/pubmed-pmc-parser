package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS mml:math 요소 / JATS mml:math element
 *
 * <p>DTD: {@code <!ELEMENT mml:math ANY (MathML content)>}</p>
 *
 * <p>
 * KR: MathML 수학 표현<br>
 * EN: MathML mathematical expression
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/mml-math.html">
 *      JATS mml:math Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MmlMath {

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
     * MathML 내용 (XML 문자열)
     */
    private String mathmlContent;

}
