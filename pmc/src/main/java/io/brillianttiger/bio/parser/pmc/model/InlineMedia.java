package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS inline-media 요소 / JATS inline-media element
 *
 * <p>DTD: {@code <!ELEMENT inline-media MIXED>}</p>
 *
 * <p>
 * KR: inline-media 요소<br>
 * EN: inline-media element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/inline-media.html">
 *      JATS inline-media Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InlineMedia {

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
