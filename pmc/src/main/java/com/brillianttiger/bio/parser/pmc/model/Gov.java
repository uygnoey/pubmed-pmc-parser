package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS gov 요소 / JATS gov element
 *
 * <p>DTD: {@code <!ELEMENT gov (#PCDATA)>}</p>
 *
 * <p>
 * KR: gov 요소<br>
 * EN: gov element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/gov.html">
 *      JATS gov Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gov {

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
