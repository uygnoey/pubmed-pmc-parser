package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * JATS article-version-alternatives 요소 / JATS article-version-alternatives element
 *
 * <p>DTD: {@code <!ELEMENT article-version-alternatives (article-version)+>}</p>
 *
 * <p>
 * KR: 여러 기사 버전의 대안<br>
 * EN: Alternative article versions
 * </p>
 *
 * @see <a href="https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/article-version-alternatives.html">
 *      JATS article-version-alternatives Documentation</a>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVersionAlternatives {

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
     * 기사 버전 목록 / Article versions
     */
    @Builder.Default
    private List<ArticleVersion> articleVersions = new ArrayList<>();

}
