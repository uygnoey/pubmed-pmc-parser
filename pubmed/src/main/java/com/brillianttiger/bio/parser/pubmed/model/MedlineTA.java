package com.brillianttiger.bio.parser.pubmed.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MedlineTA (MEDLINE Title Abbreviation) / MEDLINE 제목 약어
 *
 * DTD: <!ELEMENT MedlineTA (#PCDATA)>
 *
 * KR: MEDLINE 데이터베이스에서 사용하는 저널 제목 약어
 * EN: Journal title abbreviation used in MEDLINE database
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedlineTA {

    /**
     * 저널 약어 / Journal abbreviation
     */
    private String value;
}
