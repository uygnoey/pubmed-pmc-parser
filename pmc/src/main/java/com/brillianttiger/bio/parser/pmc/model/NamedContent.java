package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS named-content 요소 / JATS named-content element
 *
 * <p>DTD: {@code <!ELEMENT named-content (#PCDATA)>}</p>
 *
 * <p>
 * KR: named-content 요소<br>
 * EN: named-content element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/named-content.html">
 *      JATS named-content Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NamedContent {

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
