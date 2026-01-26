package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS ref-count 요소 / JATS ref-count element
 *
 * <p>DTD: {@code <!ELEMENT ref-count MIXED>}</p>
 *
 * <p>
 * KR: ref-count 요소<br>
 * EN: ref-count element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/ref-count.html">
 *      JATS ref-count Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefCount {

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
