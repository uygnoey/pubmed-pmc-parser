package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS collab-name-alternatives 요소 / JATS collab-name-alternatives element
 *
 * <p>DTD: {@code <!ELEMENT collab-name-alternatives MIXED>}</p>
 *
 * <p>
 * KR: collab-name-alternatives 요소<br>
 * EN: collab-name-alternatives element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/collab-name-alternatives.html">
 *      JATS collab-name-alternatives Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollabNameAlternatives {

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
