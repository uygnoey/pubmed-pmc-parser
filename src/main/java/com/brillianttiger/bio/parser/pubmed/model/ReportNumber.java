package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ReportNumber / 보고서 번호
 *
 * DTD: <!ELEMENT ReportNumber (#PCDATA)>
 *
 * KR: 보고서 번호
 * EN: Report number
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportNumber {

    /**
     * 보고서 번호 / Report number
     */
    private String value;
}
