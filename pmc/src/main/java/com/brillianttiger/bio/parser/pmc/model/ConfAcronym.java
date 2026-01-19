package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS conf-acronym 요소 / JATS conf-acronym element
 *
 * <p>DTD: {@code <!ELEMENT conf-acronym (#PCDATA)>}</p>
 *
 * <p>
 * KR: conf-acronym 요소<br>
 * EN: conf-acronym element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/conf-acronym.html">
 *      JATS conf-acronym Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfAcronym {

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
