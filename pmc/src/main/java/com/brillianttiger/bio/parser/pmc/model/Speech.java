package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS speech 요소 / JATS speech element
 *
 * <p>DTD: {@code <!ELEMENT speech MIXED>}</p>
 *
 * <p>
 * KR: speech 요소<br>
 * EN: speech element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/speech.html">
 *      JATS speech Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Speech {

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
