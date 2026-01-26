package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS sig-block 요소 / JATS sig-block element
 *
 * <p>DTD: {@code <!ELEMENT sig-block MIXED>}</p>
 *
 * <p>
 * KR: sig-block 요소<br>
 * EN: sig-block element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/sig-block.html">
 *      JATS sig-block Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigBlock {

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
