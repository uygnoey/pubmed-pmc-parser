package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS question-wrap 요소 / JATS question-wrap element
 *
 * <p>DTD: {@code <!ELEMENT question-wrap MIXED>}</p>
 *
 * <p>
 * KR: question-wrap 요소<br>
 * EN: question-wrap element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/question-wrap.html">
 *      JATS question-wrap Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWrap {

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
