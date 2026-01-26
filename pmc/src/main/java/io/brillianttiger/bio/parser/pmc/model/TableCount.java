package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS table-count 요소 / JATS table-count element
 *
 * <p>DTD: {@code <!ELEMENT table-count MIXED>}</p>
 *
 * <p>
 * KR: table-count 요소<br>
 * EN: table-count element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/table-count.html">
 *      JATS table-count Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableCount {

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
