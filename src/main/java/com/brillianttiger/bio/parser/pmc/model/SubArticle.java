package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SubArticle / 하위 논문
 *
 * KR: 주 논문 내에 포함된 하위 논문. JATS 1.4 DTD 완전 준수 모델.
 *     번역본, 부록, 관련 논문 등을 포함하는 재귀 구조.
 * EN: Sub-article contained within a main article. Fully compliant with JATS 1.4 DTD.
 *     Recursive structure for translations, appendices, related articles, etc.
 *
 * DTD: <!ELEMENT sub-article (
 *          (front | front-stub),
 *          body?,
 *          back?,
 *          floats-group?,
 *          (sub-article | response)*
 *      )>
 *
 * DTD: <!ATTLIST sub-article
 *          article-type CDATA #IMPLIED
 *          id ID #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/sub-article.html
 *
 * Note: Sub-articles are used for:
 * - Translations of the main article
 * - Related but distinct articles
 * - Appendices with substantial content
 * - Companion pieces (e.g., commentary, data papers)
 *
 * IMPORTANT: This is a recursive structure - sub-articles can contain sub-articles.
 *
 * Example:
 * <sub-article article-type="translation" xml:lang="es" id="S1">
 *     <front-stub>
 *         <title-group>
 *             <article-title>Título en español</article-title>
 *         </title-group>
 *     </front-stub>
 *     <body>...</body>
 * </sub-article>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubArticle {

    // ========== Attributes / 속성 ==========

    /**
     * 논문 유형 / Article type
     *
     * KR: 하위 논문의 유형.
     * EN: Type of the sub-article.
     *
     * DTD: article-type CDATA #IMPLIED
     * Required: NO
     *
     * Common values:
     * - translation: 번역본
     * - abstract: 초록만 포함
     * - letter: 서신
     * - reply: 답변
     * - addendum: 추가 사항
     * - commentary: 해설
     * - correction: 정정
     * - retraction: 철회
     */
    private ArticleType articleType;

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자.
     * EN: Unique identifier within the XML document.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     *
     * Example: "S1", "subart-trans-es"
     */
    private String id;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 특정 용도나 목적을 나타내는 문자열.
     * EN: String indicating specific use or purpose.
     *
     * DTD: specific-use CDATA #IMPLIED
     * Required: NO
     */
    private String specificUse;

    /**
     * XML 언어 / XML Language
     *
     * KR: 하위 논문의 언어 코드.
     * EN: Language code of the sub-article.
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "es", "ko", "zh"
     */
    private String xmlLang;

    // ========== Content / 콘텐츠 ==========

    /**
     * 전면부 (완전) / Front (full)
     *
     * KR: 완전한 전면부 메타데이터 (저널 메타 포함).
     *     front 또는 frontStub 중 하나만 사용.
     * EN: Full front matter metadata (includes journal meta).
     *     Either front or frontStub is used, not both.
     *
     * DTD: front (choice with front-stub)
     * Required: NO (one of front or front-stub)
     */
    private Front front;

    /**
     * 축약 전면부 / Front stub
     *
     * KR: 축약된 전면부 메타데이터 (저널 메타 제외).
     *     front 또는 frontStub 중 하나만 사용.
     * EN: Abbreviated front matter metadata (no journal meta).
     *     Either front or frontStub is used, not both.
     *
     * DTD: front-stub (choice with front)
     * Required: NO (one of front or front-stub)
     */
    private FrontStub frontStub;

    /**
     * 본문 / Body
     *
     * KR: 하위 논문의 본문 내용.
     * EN: Body content of the sub-article.
     *
     * DTD: body?
     * Required: NO
     */
    private Body body;

    /**
     * 후면부 / Back matter
     *
     * KR: 하위 논문의 후면부 (참고문헌, 부록 등).
     * EN: Back matter of the sub-article (references, appendices, etc.).
     *
     * DTD: back?
     * Required: NO
     */
    private Back back;

    /**
     * 부유 요소 그룹 / Floats group
     *
     * KR: 부유 요소 (그림, 테이블 등)의 그룹.
     * EN: Group of floating elements (figures, tables, etc.).
     *
     * DTD: floats-group?
     * Required: NO
     */
    private FloatsGroup floatsGroup;

    // ========== Nested Content (Recursive) / 중첩 콘텐츠 (재귀) ==========

    /**
     * 하위 논문 목록 (재귀) / Sub-article list (recursive)
     *
     * KR: 중첩된 하위 논문 목록. 재귀 구조를 지원.
     * EN: List of nested sub-articles. Supports recursive structure.
     *
     * DTD: sub-article*
     * Required: NO (0 or more)
     *
     * IMPORTANT: This enables recursive nesting of sub-articles.
     */
    private List<SubArticle> subArticles;

    /**
     * 응답 목록 / Response list
     *
     * KR: 하위 논문에 대한 응답 목록.
     * EN: List of responses to the sub-article.
     *
     * DTD: response*
     * Required: NO (0 or more)
     */
    private List<Response> responses;
}
