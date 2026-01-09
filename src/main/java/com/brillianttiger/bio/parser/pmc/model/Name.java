package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Name / 이름 (구조화)
 *
 * DTD: <!ELEMENT name (((surname, given-names?) | given-names), prefix?, suffix?)>
 * DTD: <!ATTLIST name
 *          content-type CDATA #IMPLIED
 *          name-style (western | eastern | islensk | given-only) "western"
 *          specific-use CDATA #IMPLIED>
 *
 * KR: 구조화된 개인 이름
 * EN: Structured personal name
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Name {

    /**
     * 내용 유형 / Content type
     *
     * KR: 이름의 내용 유형 (선택적 분류).
     * EN: Content type of the name (optional classification).
     *
     * DTD: content-type CDATA #IMPLIED
     */
    private String contentType;

    /**
     * 이름 스타일 / Name style
     *
     * KR: 이름 표기 방식 (western, eastern, islensk, given-only).
     *     기본값: WESTERN
     * EN: Name formatting style (western, eastern, islensk, given-only).
     *     Default: WESTERN
     *
     * DTD: name-style (western | eastern | islensk | given-only) "western"
     *
     * Values:
     * - WESTERN: 서양식 (이름 성) / Western style (given-name surname)
     * - EASTERN: 동양식 (성 이름) / Eastern style (surname given-name)
     * - ISLENSK: 아이슬란드식 / Icelandic style
     * - GIVEN_ONLY: 이름만 / Given name only
     */
    @Builder.Default
    private NameStyle nameStyle = NameStyle.WESTERN;

    /**
     * 특정 용도 / Specific use
     *
     * KR: 이 이름의 특정 용도나 응용 (선택적).
     * EN: Specific use or application of this name (optional).
     *
     * DTD: specific-use CDATA #IMPLIED
     */
    private String specificUse;

    /**
     * 성 (필수) / Surname (required)
     */
    private Surname surname;

    /**
     * 이름 / Given names
     */
    private GivenNames givenNames;

    /**
     * 접두사 / Prefix
     */
    private Prefix prefix;

    /**
     * 접미사 / Suffix
     */
    private PmcSuffix suffix;

    /**
     * 값 / Value
     */
    private String value;
}
