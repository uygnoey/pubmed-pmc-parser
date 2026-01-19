package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS addr-line 요소 / JATS addr-line element
 *
 * <p>DTD: {@code <!ELEMENT addr-line (#PCDATA)>}</p>
 *
 * <p>
 * KR: addr-line 요소<br>
 * EN: addr-line element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/addr-line.html">
 *      JATS addr-line Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddrLine {

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
