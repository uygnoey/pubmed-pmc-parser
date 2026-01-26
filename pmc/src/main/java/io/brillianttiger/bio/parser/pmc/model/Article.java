package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS article 요소 / JATS article element
 *
 * <p>DTD: {@code <!ELEMENT article (processing-meta?, front, body?, back?, floats-group?, (sub-article* | response*))>}</p>
 *
 * <p>
 * KR: 저널 기사의 루트 요소<br>
 * EN: Root element for a journal article
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/article.html">
 *      JATS article Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

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
     * Article type (예: research-article, review-article)
     */
    private String articleType;

    /**
     * DTD version
     */
    private String dtdVersion;

    /**
     * Front matter (메타데이터)
     */
    private Front front;

    /**
     * Body (본문)
     */
    private Body body;

    /**
     * Back matter (부록)
     */
    private Back back;

}
