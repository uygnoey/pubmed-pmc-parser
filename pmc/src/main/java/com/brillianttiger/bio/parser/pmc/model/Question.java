package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS question 요소 / JATS question element
 *
 * <p>DTD: {@code <!ELEMENT question (label?, title?, (%para-level;)*)>}</p>
 *
 * <p>
 * KR: 질문<br>
 * EN: Question
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/question.html">
 *      JATS question Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question {

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

    /**
     * 라벨
     */
    private Label label;

    /**
     * 제목
     */
    private Title title;

    /**
     * 질문 내용 / Question content
     */
    @Builder.Default
    private List<P> paragraphs = new ArrayList<>();

}
