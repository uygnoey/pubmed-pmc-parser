package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS break 요소 / JATS break element
 *
 * <p>DTD: {@code <!ELEMENT break MIXED>}</p>
 *
 * <p>
 * KR: break 요소<br>
 * EN: break element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/break.html">
 *      JATS break Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Break {

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
