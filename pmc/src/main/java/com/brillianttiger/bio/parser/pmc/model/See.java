package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS see 요소 / JATS see element
 *
 * <p>DTD: {@code <!ELEMENT see (#PCDATA)>}</p>
 *
 * <p>
 * KR: see 요소<br>
 * EN: see element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/see.html">
 *      JATS see Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class See {

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
