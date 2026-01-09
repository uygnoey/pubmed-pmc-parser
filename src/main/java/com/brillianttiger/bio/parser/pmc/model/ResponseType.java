package com.brillianttiger.bio.parser.pmc.model;

import lombok.Getter;

/**
 * ResponseType / 응답 유형
 *
 * KR: JATS response-type 속성 값을 나타내는 열거형.
 *     논문에 대한 응답의 유형을 지정.
 * EN: Enumeration representing JATS response-type attribute values.
 *     Specifies the type of response to an article.
 *
 * DTD: response-type CDATA #IMPLIED
 *
 * Reference: https://jats.nlm.nih.gov/archiving/tag-library/1.4/attribute/response-type.html
 *
 * Common values from SKILL-PMC.md:
 * - addendum: 추가 사항
 * - discussion: 토론
 * - reply: 답변
 * - author-comment: 저자 코멘트
 * - reviewer-report: 심사자 보고서
 */
@Getter
public enum ResponseType {

    /**
     * 추가 사항 / Addendum
     * KR: 논문에 대한 추가 사항이나 보충 정보
     * EN: Additional material or supplementary information
     */
    ADDENDUM("addendum"),

    /**
     * 토론 / Discussion
     * KR: 논문에 대한 학술적 토론
     * EN: Academic discussion about the article
     */
    DISCUSSION("discussion"),

    /**
     * 답변 / Reply
     * KR: 코멘트나 비평에 대한 저자의 답변
     * EN: Author's reply to comments or critiques
     */
    REPLY("reply"),

    /**
     * 저자 코멘트 / Author comment
     * KR: 저자의 추가 코멘트
     * EN: Additional comment from the author
     */
    AUTHOR_COMMENT("author-comment"),

    /**
     * 심사자 보고서 / Reviewer report
     * KR: 심사자의 평가 보고서
     * EN: Peer reviewer's evaluation report
     */
    REVIEWER_REPORT("reviewer-report"),

    /**
     * 정정 / Correction
     * KR: 오류 정정
     * EN: Correction of errors
     */
    CORRECTION("correction"),

    /**
     * 편집자 코멘트 / Editor comment
     * KR: 편집자의 코멘트
     * EN: Comment from the editor
     */
    EDITOR_COMMENT("editor-comment"),

    /**
     * 서신 / Letter
     * KR: 서신 형태의 응답
     * EN: Response in letter form
     */
    LETTER("letter"),

    /**
     * 기타 / Other
     * KR: 기타 응답 유형
     * EN: Other response types
     */
    OTHER("other");

    private final String value;

    ResponseType(String value) {
        this.value = value;
    }

    /**
     * 문자열에서 ResponseType 변환 / Convert from string to ResponseType
     *
     * KR: XML에서 파싱한 문자열을 ResponseType enum으로 변환.
     *     매칭되는 값이 없으면 OTHER 반환.
     * EN: Converts parsed string from XML to ResponseType enum.
     *     Returns OTHER if no matching value found.
     *
     * @param value response-type 속성 값 / response-type attribute value
     * @return 해당하는 ResponseType, 없으면 OTHER / Corresponding ResponseType, or OTHER if not found
     */
    public static ResponseType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalized = value.trim().toLowerCase();
        for (ResponseType type : values()) {
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
