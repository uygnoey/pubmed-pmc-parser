package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS def-head 요소 / JATS def-head element
 *
 * <p>DTD: {@code <!ELEMENT def-head (#PCDATA)>}</p>
 *
 * <p>
 * KR: def-head 요소<br>
 * EN: def-head element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/def-head.html">
 *      JATS def-head Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefHead {

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
