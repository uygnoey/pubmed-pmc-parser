package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS fixed-case 요소 / JATS fixed-case element
 *
 * <p>DTD: {@code <!ELEMENT fixed-case (#PCDATA)>}</p>
 *
 * <p>
 * KR: fixed-case 요소<br>
 * EN: fixed-case element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/fixed-case.html">
 *      JATS fixed-case Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedCase {

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
