package io.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JatsArticle / JATS 논문 (루트 요소)
 *
 * KR: JATS/NLM DTD 형식의 학술 논문 전체 구조를 나타내는 루트 모델.
 *     JATS 1.4 (ANSI/NISO Z39.96-2024) 표준 기준.
 * EN: Root model representing complete scholarly article structure in JATS/NLM DTD format.
 *     Based on JATS 1.4 (ANSI/NISO Z39.96-2024) standard.
 *
 * DTD Reference: https://jats.nlm.nih.gov/archiving/1.4/JATS-archivearticle1-4.dtd
 * Tag Library: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/article.html
 *
 * DTD Definition:
 * <!ELEMENT article (
 *     front,
 *     body?,
 *     back?,
 *     floats-group?,
 *     (sub-article* | response*)
 * )>
 *
 * <!ATTLIST article
 *     article-type CDATA #IMPLIED
 *     dtd-version CDATA #IMPLIED
 *     xml:lang NMTOKEN "en"
 *     specific-use CDATA #IMPLIED
 *     xmlns:xlink CDATA #FIXED "http://www.w3.org/1999/xlink"
 *     xmlns:mml CDATA #FIXED "http://www.w3.org/1998/Math/MathML"
 * >
 *
 * Structure:
 * - front: 논문 메타데이터 (필수) / Article metadata (required)
 * - body: 본문 (선택) / Body content (optional)
 * - back: 후면부 - 참고문헌, 부록 등 (선택) / Back matter - references, appendices (optional)
 * - floats-group: 부유 요소 - 그림, 표 등 (선택) / Floating elements - figures, tables (optional)
 * - sub-article: 하위 논문 목록 (선택) / Sub-articles (optional)
 * - response: 응답 목록 (선택) / Responses (optional)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JatsArticle {

    // ===================================================================
    // Attributes / 속성
    // ===================================================================

    /**
     * 논문 유형 / Article type
     *
     * KR: 논문의 유형을 나타내는 열거형 값.
     *     예: RESEARCH_ARTICLE, REVIEW_ARTICLE, LETTER 등.
     * EN: Enumeration value indicating the type of article.
     *     Examples: RESEARCH_ARTICLE, REVIEW_ARTICLE, LETTER, etc.
     *
     * DTD: article-type CDATA #IMPLIED
     * Values: research-article, review-article, letter, editorial, case-report, etc.
     */
    private ArticleType articleType;

    /**
     * DTD 버전 / DTD version
     *
     * KR: 사용된 JATS DTD 버전.
     *     예: "1.4", "1.3", "1.2"
     * EN: Version of JATS DTD used.
     *     Examples: "1.4", "1.3", "1.2"
     *
     * DTD: dtd-version CDATA #IMPLIED
     */
    private String dtdVersion;

    /**
     * 언어 코드 / Language code
     *
     * KR: 논문의 주 언어 (ISO 639-1 코드).
     *     기본값: "en" (영어)
     * EN: Primary language of the article (ISO 639-1 code).
     *     Default: "en" (English)
     *
     * DTD: xml:lang NMTOKEN "en"
     * Examples: "en", "ko", "fr", "de", "ja", "zh"
     */
    @Builder.Default
    private String xmlLang = "en";

    /**
     * 특수 용도 / Specific use
     *
     * KR: 특정 용도나 처리 방식을 나타내는 자유 형식 속성.
     *     출판사나 응용 프로그램별 용도로 사용.
     * EN: Free-form attribute indicating specific use or processing.
     *     Used for publisher or application-specific purposes.
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * XLink 네임스페이스 / XLink namespace
     *
     * KR: XLink 표준 네임스페이스 URI (고정값).
     *     외부 링크 처리에 사용.
     * EN: XLink standard namespace URI (fixed value).
     *     Used for external link processing.
     *
     * DTD: xmlns:xlink CDATA #FIXED "http://www.w3.org/1999/xlink"
     * Fixed: http://www.w3.org/1999/xlink
     */
    @Builder.Default
    private String xmlnsXlink = "http://www.w3.org/1999/xlink";

    /**
     * MathML 네임스페이스 / MathML namespace
     *
     * KR: MathML 표준 네임스페이스 URI (고정값).
     *     수학 공식 표현에 사용.
     * EN: MathML standard namespace URI (fixed value).
     *     Used for mathematical formula representation.
     *
     * DTD: xmlns:mml CDATA #FIXED "http://www.w3.org/1998/Math/MathML"
     * Fixed: http://www.w3.org/1998/Math/MathML
     */
    @Builder.Default
    private String xmlnsMml = "http://www.w3.org/1998/Math/MathML";

    // ===================================================================
    // Elements / 요소
    // ===================================================================

    /**
     * 전면부 (필수) / Front matter (required)
     *
     * KR: 논문의 메타데이터 섹션.
     *     저널 정보, 논문 정보, 저자, 초록, 키워드 등 포함.
     * EN: Metadata section of the article.
     *     Includes journal info, article info, authors, abstract, keywords, etc.
     *
     * DTD: <!ELEMENT front (journal-meta?, article-meta, ...)>
     * Required: YES
     */
    private Front front;

    /**
     * 본문 / Body
     *
     * KR: 논문의 본문 내용.
     *     섹션, 단락, 그림, 표 등으로 구성.
     * EN: Body content of the article.
     *     Composed of sections, paragraphs, figures, tables, etc.
     *
     * DTD: <!ELEMENT body (...)>
     * Required: NO
     */
    private Body body;

    /**
     * 후면부 / Back matter
     *
     * KR: 논문의 후면부.
     *     참고문헌, 부록, 감사의 글, 용어집 등 포함.
     * EN: Back matter of the article.
     *     Includes references, appendices, acknowledgments, glossary, etc.
     *
     * DTD: <!ELEMENT back (label?, title*, ...)>
     * Required: NO
     */
    private Back back;

    /**
     * 부유 요소 그룹 / Floating elements group
     *
     * KR: 본문에서 분리된 부유 요소들의 컨테이너.
     *     그림, 표, 수식 등을 모아둔 영역.
     * EN: Container for floating elements separated from body.
     *     Collection area for figures, tables, formulas, etc.
     *
     * DTD: <!ELEMENT floats-group (alternatives | boxed-text | ...)>
     * Required: NO
     */
    private FloatsGroup floatsGroup;

    /**
     * 하위 논문 목록 / Sub-article list
     *
     * KR: 주 논문에 포함된 하위 논문들.
     *     예: 번역본, 요약본, 관련 논문 등.
     * EN: Sub-articles embedded in the main article.
     *     Examples: translations, summaries, related articles, etc.
     *
     * DTD: <!ELEMENT sub-article (front | front-stub, body?, back?, floats-group?, ...)>
     * DTD: (sub-article* | response*)
     * Note: DTD에서는 sub-article과 response 중 하나만 선택하지만,
     *       유연한 처리를 위해 둘 다 허용하도록 구현.
     */
    private List<SubArticle> subArticles;

    /**
     * 응답 목록 / Response list
     *
     * KR: 논문에 대한 응답들.
     *     예: 추가 정보, 토론, 답변 등.
     * EN: Responses to the article.
     *     Examples: addenda, discussions, replies, etc.
     *
     * DTD: <!ELEMENT response (front | front-stub, body?, back?, floats-group?)>
     * DTD: (sub-article* | response*)
     */
    private List<Response> responses;
}
