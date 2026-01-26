package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS underline-start 요소 / JATS underline-start element
 *
 * <p>DTD: {@code <!ELEMENT underline-start MIXED>}</p>
 *
 * <p>
 * KR: underline-start 요소<br>
 * EN: underline-start element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/underline-start.html">
 *      JATS underline-start Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnderlineStart {

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
