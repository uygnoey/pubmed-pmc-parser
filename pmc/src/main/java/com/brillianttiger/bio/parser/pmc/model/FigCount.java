package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS fig-count 요소 / JATS fig-count element
 *
 * <p>DTD: {@code <!ELEMENT fig-count MIXED>}</p>
 *
 * <p>
 * KR: fig-count 요소<br>
 * EN: fig-count element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/fig-count.html">
 *      JATS fig-count Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FigCount {

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
