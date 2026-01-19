package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS question-preamble 요소 / JATS question-preamble element
 *
 * <p>DTD: {@code <!ELEMENT question-preamble MIXED>}</p>
 *
 * <p>
 * KR: question-preamble 요소<br>
 * EN: question-preamble element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/question-preamble.html">
 *      JATS question-preamble Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPreamble {

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
