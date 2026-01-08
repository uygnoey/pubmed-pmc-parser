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
     */
    private String contentType;

    /**
     * 이름 스타일 (기본값: "western") / Name style (default: "western")
     */
    @Builder.Default
    private String nameStyle = "western";

    /**
     * 특정 용도 / Specific use
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
