package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS citation-alternatives 요소 / JATS citation-alternatives element
 *
 * <p>DTD: {@code <!ELEMENT citation-alternatives MIXED>}</p>
 *
 * <p>
 * KR: citation-alternatives 요소<br>
 * EN: citation-alternatives element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/citation-alternatives.html">
 *      JATS citation-alternatives Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitationAlternatives {

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
