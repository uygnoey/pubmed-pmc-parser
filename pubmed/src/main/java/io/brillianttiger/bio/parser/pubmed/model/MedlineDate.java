package io.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MedlineDate / MEDLINE 날짜
 *
 * DTD: <!ELEMENT MedlineDate (#PCDATA)>
 *
 * KR: 비정형 날짜 문자열 (예: "2024 Jan-Feb", "2024 Spring")
 * EN: Non-standard date string
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedlineDate {

    /**
     * 날짜 문자열 / Date string
     */
    private String value;
}
