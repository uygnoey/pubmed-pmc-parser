package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS index-term-range-end 요소 / JATS index-term-range-end element
 *
 * <p>DTD: {@code <!ELEMENT index-term-range-end MIXED>}</p>
 *
 * <p>
 * KR: index-term-range-end 요소<br>
 * EN: index-term-range-end element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/index-term-range-end.html">
 *      JATS index-term-range-end Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexTermRangeEnd {

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
