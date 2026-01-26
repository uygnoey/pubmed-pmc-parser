package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS glyph-ref 요소 / JATS glyph-ref element
 *
 * <p>DTD: {@code <!ELEMENT glyph-ref MIXED>}</p>
 *
 * <p>
 * KR: glyph-ref 요소<br>
 * EN: glyph-ref element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/glyph-ref.html">
 *      JATS glyph-ref Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlyphRef {

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
