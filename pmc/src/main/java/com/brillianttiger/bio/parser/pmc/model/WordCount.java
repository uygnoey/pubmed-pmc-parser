package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS word-count 요소 / JATS word-count element
 *
 * <p>DTD: {@code <!ELEMENT word-count MIXED>}</p>
 *
 * <p>
 * KR: word-count 요소<br>
 * EN: word-count element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/word-count.html">
 *      JATS word-count Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordCount {

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
