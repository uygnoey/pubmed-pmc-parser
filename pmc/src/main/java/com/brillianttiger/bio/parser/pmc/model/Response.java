package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response / 응답
 *
 * KR: 논문에 대한 응답. JATS 1.4 DTD 완전 준수 모델.
 *     토론, 답변, 심사자 보고서 등 논문에 대한 공식적 응답.
 * EN: Response to an article. Fully compliant with JATS 1.4 DTD.
 *     Formal responses including discussions, replies, reviewer reports, etc.
 *
 * DTD: <!ELEMENT response (
 *          (front | front-stub),
 *          body?,
 *          back?,
 *          floats-group?
 *      )>
 *
 * DTD: <!ATTLIST response
 *          id ID #IMPLIED
 *          response-type CDATA #IMPLIED
 *          specific-use CDATA #IMPLIED
 *          xml:lang NMTOKEN #IMPLIED
 *      >
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/element/response.html
 *
 * Note: Responses are used for:
 * - Author's reply to peer review
 * - Discussions of the article
 * - Reviewer reports
 * - Editor comments
 * - Addenda and corrections
 *
 * Common response-type values:
 * - addendum: 추가 사항
 * - discussion: 토론
 * - reply: 답변
 * - author-comment: 저자 코멘트
 * - reviewer-report: 심사자 보고서
 *
 * Example:
 * <response response-type="reply" id="R1">
 *     <front-stub>
 *         <title-group>
 *             <article-title>Author's Response to Reviewer Comments</article-title>
 *         </title-group>
 *     </front-stub>
 *     <body>
 *         <p>We thank the reviewer for their insightful comments...</p>
 *     </body>
 * </response>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    // ========== Attributes / 속성 ==========

    /**
     * ID 속성 / ID attribute
     *
     * KR: XML 문서 내 고유 식별자.
     * EN: Unique identifier within the XML document.
     *
     * DTD: id ID #IMPLIED
     * Required: NO
     *
     * Example: "R1", "resp-author-reply"
     */
    private String id;

    /**
     * 응답 유형 / Response type
     *
     * KR: 응답의 유형.
     * EN: Type of the response.
     *
     * DTD: response-type CDATA #IMPLIED
     * Required: NO
     *
     * Values: addendum, discussion, reply, author-comment, reviewer-report, etc.
     */
    private ResponseType responseType;

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
     * KR: 응답의 언어 코드.
     * EN: Language code of the response.
     *
     * DTD: xml:lang NMTOKEN #IMPLIED
     * Required: NO
     *
     * Example: "en", "de", "fr"
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
     *     응답에서 가장 일반적으로 사용됨.
     * EN: Abbreviated front matter metadata (no journal meta).
     *     Either front or frontStub is used, not both.
     *     Most commonly used in responses.
     *
     * DTD: front-stub (choice with front)
     * Required: NO (one of front or front-stub)
     */
    private FrontStub frontStub;

    /**
     * 본문 / Body
     *
     * KR: 응답의 본문 내용.
     * EN: Body content of the response.
     *
     * DTD: body?
     * Required: NO
     */
    private Body body;

    /**
     * 후면부 / Back matter
     *
     * KR: 응답의 후면부 (참고문헌 등).
     * EN: Back matter of the response (references, etc.).
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
}
