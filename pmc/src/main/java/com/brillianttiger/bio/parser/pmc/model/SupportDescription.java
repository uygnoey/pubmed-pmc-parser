package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS support-description 요소 / JATS support-description element
 *
 * <p>DTD: {@code <!ELEMENT support-description (#PCDATA)>}</p>
 *
 * <p>
 * KR: support-description 요소<br>
 * EN: support-description element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/support-description.html">
 *      JATS support-description Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportDescription {

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
