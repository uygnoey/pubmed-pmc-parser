package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS price 요소 / JATS price element
 *
 * <p>DTD: {@code <!ELEMENT price (#PCDATA)>}</p>
 *
 * <p>
 * KR: price 요소<br>
 * EN: price element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/price.html">
 *      JATS price Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Price {

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
