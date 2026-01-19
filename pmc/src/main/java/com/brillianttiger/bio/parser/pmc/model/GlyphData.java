package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS glyph-data 요소 / JATS glyph-data element
 *
 * <p>DTD: {@code <!ELEMENT glyph-data MIXED>}</p>
 *
 * <p>
 * KR: glyph-data 요소<br>
 * EN: glyph-data element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/glyph-data.html">
 *      JATS glyph-data Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlyphData {

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
