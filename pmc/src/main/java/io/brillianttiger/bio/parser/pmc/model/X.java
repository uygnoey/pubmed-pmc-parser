package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS x 요소 / JATS x element
 *
 * <p>DTD: {@code <!ELEMENT x (#PCDATA)>}</p>
 *
 * <p>
 * KR: x 요소<br>
 * EN: x element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/x.html">
 *      JATS x Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class X {

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

}
