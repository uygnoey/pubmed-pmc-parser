package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CopyrightInformation / 저작권 정보
 *
 * DTD: <!ELEMENT CopyrightInformation (#PCDATA)>
 *
 * KR: 논문의 저작권 정보
 * EN: Article copyright information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CopyrightInformation {

    /**
     * 저작권 정보 내용 / Copyright information content
     */
    private String value;
}
