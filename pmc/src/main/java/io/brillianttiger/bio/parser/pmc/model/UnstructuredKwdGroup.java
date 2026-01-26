package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS unstructured-kwd-group 요소 / JATS unstructured-kwd-group element
 *
 * <p>DTD: {@code <!ELEMENT unstructured-kwd-group MIXED>}</p>
 *
 * <p>
 * KR: unstructured-kwd-group 요소<br>
 * EN: unstructured-kwd-group element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/unstructured-kwd-group.html">
 *      JATS unstructured-kwd-group Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnstructuredKwdGroup {

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
