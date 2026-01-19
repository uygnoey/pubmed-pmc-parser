package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS question-wrap-group 요소 / JATS question-wrap-group element
 *
 * <p>DTD: {@code <!ELEMENT question-wrap-group MIXED>}</p>
 *
 * <p>
 * KR: question-wrap-group 요소<br>
 * EN: question-wrap-group element
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/question-wrap-group.html">
 *      JATS question-wrap-group Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWrapGroup {

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
