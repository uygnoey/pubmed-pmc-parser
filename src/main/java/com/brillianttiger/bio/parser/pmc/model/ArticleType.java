package com.brillianttiger.bio.parser.pmc.model;

/**
 * ArticleType / 논문 유형
 *
 * KR: JATS article-type 속성 값을 나타내는 열거형.
 *     JATS 1.4 DTD 기준 주요 논문 유형을 포함.
 * EN: Enumeration representing JATS article-type attribute values.
 *     Includes major article types based on JATS 1.4 DTD.
 *
 * DTD: <!ATTLIST article article-type CDATA #IMPLIED>
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/article-type.html
 *
 * Common Values:
 * - research-article: Original research article
 * - review-article: Review article
 * - letter: Letter to the editor
 * - editorial: Editorial
 * - case-report: Clinical case report
 * - retraction: Retraction notice
 * - correction: Correction/erratum
 * - addendum: Addendum to published article
 * - abstract: Meeting abstract
 * - book-review: Book review
 * - product-review: Product review
 * - meeting-report: Meeting report
 * - discussion: Discussion
 * - obituary: Obituary
 * - oration: Oration
 * - reply: Reply/response
 */
public enum ArticleType {

    /**
     * 연구 논문 / Research article
     * KR: 원저 (original research)
     * EN: Original research article
     */
    RESEARCH_ARTICLE("research-article"),

    /**
     * 리뷰 논문 / Review article
     * KR: 종설, 문헌 고찰
     * EN: Review article, systematic review
     */
    REVIEW_ARTICLE("review-article"),

    /**
     * 편지 / Letter
     * KR: 편집자에게 보내는 편지
     * EN: Letter to the editor
     */
    LETTER("letter"),

    /**
     * 사설 / Editorial
     * KR: 편집자의 글, 논평
     * EN: Editorial, commentary
     */
    EDITORIAL("editorial"),

    /**
     * 증례 보고 / Case report
     * KR: 임상 증례 보고
     * EN: Clinical case report
     */
    CASE_REPORT("case-report"),

    /**
     * 철회 / Retraction
     * KR: 논문 철회 공지
     * EN: Retraction notice
     */
    RETRACTION("retraction"),

    /**
     * 정정 / Correction
     * KR: 오류 수정, 정오표
     * EN: Correction, erratum
     */
    CORRECTION("correction"),

    /**
     * 추가 / Addendum
     * KR: 출판된 논문에 대한 추가 정보
     * EN: Addendum to published article
     */
    ADDENDUM("addendum"),

    /**
     * 초록 / Abstract
     * KR: 학회 초록, 회의록
     * EN: Meeting abstract, conference abstract
     */
    ABSTRACT("abstract"),

    /**
     * 서평 / Book review
     * KR: 도서 리뷰
     * EN: Book review
     */
    BOOK_REVIEW("book-review"),

    /**
     * 제품 리뷰 / Product review
     * KR: 제품 평가
     * EN: Product review
     */
    PRODUCT_REVIEW("product-review"),

    /**
     * 회의 보고 / Meeting report
     * KR: 학회 보고, 회의록
     * EN: Conference report, meeting report
     */
    MEETING_REPORT("meeting-report"),

    /**
     * 토론 / Discussion
     * KR: 토론, 논의
     * EN: Discussion
     */
    DISCUSSION("discussion"),

    /**
     * 부고 / Obituary
     * KR: 사망 기사
     * EN: Obituary
     */
    OBITUARY("obituary"),

    /**
     * 연설 / Oration
     * KR: 기념 연설, 강연
     * EN: Oration, memorial lecture
     */
    ORATION("oration"),

    /**
     * 답변 / Reply
     * KR: 답변, 응답
     * EN: Reply, response
     */
    REPLY("reply"),

    /**
     * 기타 / Other
     * KR: 기타 논문 유형
     * EN: Other article types not listed above
     */
    OTHER("other");

    private final String value;

    ArticleType(String value) {
        this.value = value;
    }

    /**
     * DTD 속성 값 / DTD attribute value
     *
     * KR: XML article-type 속성값 반환
     * EN: Returns XML article-type attribute value
     *
     * @return article-type 값 (예: "research-article")
     */
    public String getValue() {
        return value;
    }

    /**
     * 문자열에서 ArticleType 변환 / Convert from string to ArticleType
     *
     * KR: XML에서 파싱한 문자열을 ArticleType enum으로 변환.
     *     매칭되는 값이 없으면 OTHER 반환.
     * EN: Converts parsed string from XML to ArticleType enum.
     *     Returns OTHER if no matching value found.
     *
     * @param value article-type 속성 값 / article-type attribute value
     * @return 해당하는 ArticleType, 없으면 OTHER / Corresponding ArticleType, or OTHER if not found
     */
    public static ArticleType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return OTHER;
        }

        String normalized = value.trim().toLowerCase();
        for (ArticleType type : values()) {
            if (type.value.equals(normalized)) {
                return type;
            }
        }

        return OTHER;
    }

    @Override
    public String toString() {
        return value;
    }
}
