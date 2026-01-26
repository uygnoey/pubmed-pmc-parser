package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS issn-l 요소 / JATS issn-l element
 *
 * <p>DTD: {@code <!ELEMENT issn-l (#PCDATA)>}</p>
 *
 * <p>
 * KR: issn-l 요소<br>
 * EN: issn-l element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/issn-l.html">
 *      JATS issn-l Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssnL {

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
