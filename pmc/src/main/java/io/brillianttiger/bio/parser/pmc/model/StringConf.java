package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS string-conf 요소 / JATS string-conf element
 *
 * <p>DTD: {@code <!ELEMENT string-conf (#PCDATA)>}</p>
 *
 * <p>
 * KR: string-conf 요소<br>
 * EN: string-conf element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/string-conf.html">
 *      JATS string-conf Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringConf {

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
