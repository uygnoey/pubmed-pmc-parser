package com.brillianttiger.bio.parser.pmc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Name / 이름 (구조화)
 *
 * DTD: <!ELEMENT name (surname, given-names?, prefix?, suffix?)>
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
