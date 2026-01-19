package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS award-desc 요소 / JATS award-desc element
 *
 * <p>DTD: {@code <!ELEMENT award-desc (#PCDATA)>}</p>
 *
 * <p>
 * KR: award-desc 요소<br>
 * EN: award-desc element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/award-desc.html">
 *      JATS award-desc Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AwardDesc {

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
