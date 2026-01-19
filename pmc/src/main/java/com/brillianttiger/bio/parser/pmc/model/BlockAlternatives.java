package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS block-alternatives 요소 / JATS block-alternatives element
 *
 * <p>DTD: {@code <!ELEMENT block-alternatives MIXED>}</p>
 *
 * <p>
 * KR: block-alternatives 요소<br>
 * EN: block-alternatives element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/block-alternatives.html">
 *      JATS block-alternatives Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockAlternatives {

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
