package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS answer 요소 / JATS answer element
 *
 * <p>DTD: {@code <!ELEMENT answer (label?, title?, (%para-level;)*)>}</p>
 *
 * <p>
 * KR: 답변<br>
 * EN: Answer
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/answer.html">
 *      JATS answer Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

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
     * 답변 내용 / Answer content
     */
    @Builder.Default
    private List<P> paragraphs = new ArrayList<>();

}
