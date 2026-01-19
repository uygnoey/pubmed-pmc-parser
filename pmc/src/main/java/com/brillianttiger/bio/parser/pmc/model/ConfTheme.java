package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS conf-theme 요소 / JATS conf-theme element
 *
 * <p>DTD: {@code <!ELEMENT conf-theme (#PCDATA)>}</p>
 *
 * <p>
 * KR: conf-theme 요소<br>
 * EN: conf-theme element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/conf-theme.html">
 *      JATS conf-theme Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfTheme {

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
