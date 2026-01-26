package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS city 요소 / JATS city element
 *
 * <p>DTD: {@code <!ELEMENT city (#PCDATA)>}</p>
 *
 * <p>
 * KR: city 요소<br>
 * EN: city element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/city.html">
 *      JATS city Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {

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
