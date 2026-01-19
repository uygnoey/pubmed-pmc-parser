package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS article-version 요소 / JATS article-version element
 *
 * <p>DTD: {@code <!ELEMENT article-version (#PCDATA)>}</p>
 *
 * <p>
 * KR: 기사 버전 정보<br>
 * EN: Article version information
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/article-version.html">
 *      JATS article-version Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVersion {

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
     * 버전 지정자 (예: preprint, vor)
     */
    private String designator;

}
