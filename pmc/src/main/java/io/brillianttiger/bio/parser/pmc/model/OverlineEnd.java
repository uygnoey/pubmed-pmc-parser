package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS overline-end 요소 / JATS overline-end element
 *
 * <p>DTD: {@code <!ELEMENT overline-end MIXED>}</p>
 *
 * <p>
 * KR: overline-end 요소<br>
 * EN: overline-end element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/overline-end.html">
 *      JATS overline-end Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverlineEnd {

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
