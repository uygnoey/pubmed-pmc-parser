package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS restricted-by 요소 / JATS restricted-by element
 *
 * <p>DTD: {@code <!ELEMENT restricted-by MIXED>}</p>
 *
 * <p>
 * KR: restricted-by 요소<br>
 * EN: restricted-by element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/restricted-by.html">
 *      JATS restricted-by Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestrictedBy {

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
